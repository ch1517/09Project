package com.suhyeon.it.ble;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeviceScanActivity extends ListActivity {
    // BLE 디바이스 검색 하는 Adapter
    private LeDeviceListAdapter mLeDeviceListAdapter;
    // API21 부터는 디바이스 검색은 LeDeviceListAdapter에서
    private BluetoothAdapter mBluetoothAdapter;
    // scan 상태
    private boolean mScanning;
    // 핸들러
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // 10초 동안 scan
    private static final long SCAN_PERIOD = 10000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 액션바 이름
        getActionBar().setTitle(R.string.title_devices);
        // 핸들러 생성
        mHandler = new Handler();

        // 선택적으로 BLE 관련 기능을 비활성화
        // BLE가 장치에서 지원되는지 여부를 판별
        // PackageManager : 현재 장치에 설치된 응용 프로그램 패키지와 관련된 다양한 종류의 정보를 검색하는 클래스
        // Bluetooth Low Energy 무선을 통해 다른 장치와 통신 할 수 없으면
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }




        // BluetoothManager 통한 BluetoothAdapter 선언 및 초기화 API>18
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 블루투스를 지원하는 기기가 아니면
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    // 옵션 메뉴 초기화
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // scan 상태가 아닐때
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true); // scan 버튼 보이게
            menu.findItem(R.id.menu_refresh).setActionView(null); // 버퍼링 없음
        } else { // scan 상태일 때
            menu.findItem(R.id.menu_stop).setVisible(true); // stop 버튼 보이게
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress); // 액션바에 버퍼링 보이게
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan: // scan 버튼 클릭시
                mLeDeviceListAdapter.clear(); // deviceadapter 초기화
                scanLeDevice(true); // scan 상태 true
                break;
            case R.id.menu_stop: // stop 버튼 클릭시
                scanLeDevice(false); // scan 상태 false
                break;
        }
        return true;
    }


    //활성화 상태에 있다가 다른 Activity가 활성화되면 onResume()이 실행
    @Override
    protected void onResume() {
        super.onResume(); // 제일 위에 써줘야함
        // 장치에서 Bluetooth가 활성화되었는지 확인
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        // 활성화 안돼 있는 경우
        if (!mBluetoothAdapter.isEnabled()) {
            // 사용자가 Bluetooth를 켜도록하는 시스템 활동을 표시
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }

        // Listview Adapter 초기화
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    //액티비티로부터 결과 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //사용자가 블루투스를 사용하지 않도록 선택, 액티비티 종료.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // 다른 액티비티가 보여질 때 호출됨. 데이터 저장, 스레드 중지 등의 처리를 하기에 적당한 메소드.
    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false); // 스레드 중지
        mLeDeviceListAdapter.clear(); // mLeDeviceListAdapter 초기화
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;
        // DeviceControlActivity로 이동
        final Intent intent = new Intent(this, DeviceControlActivity.class);
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        if (mScanning) { // scan 상태일 때
            mBluetoothAdapter.stopLeScan(mLeScanCallback); // 콜백함수 중지
            mScanning = false; // scan 상태 false
        }
        startActivity(intent); // DeviceControlActivity 시작
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) { // true
            // 미리 정의 된 스캔 기간 후 스캔 중지
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false; // scan 중지
                    mBluetoothAdapter.stopLeScan(mLeScanCallback); // 콜백 중지
                    invalidateOptionsMenu(); // 다음번 메뉴를 열때 onCreateOptionsMenu를 다시 호출
                }
            }, SCAN_PERIOD); // scan 주기만큼

            mScanning = true; // scan 시작
            mBluetoothAdapter.startLeScan(mLeScanCallback); // 콜백 시작
        } else {
            mScanning = false; // scan 중지
            mBluetoothAdapter.stopLeScan(mLeScanCallback); // 콜백 중지
        }
        invalidateOptionsMenu();
    }

    // 스캔을 통해 발견 된 장치를 보관하기위한 어댑터
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator; // 레이아웃 XML 파일을 해당 View 객체로 인스턴스화

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            // 현재 컨텍스트에 이미 연결되어 있고 실행중인 장치에 대해 올바르게 구성되어있는 표준 LayoutInflater 인스턴스를 검색
            mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) { // List에 device가 없으면
                mLeDevices.add(device);
            }
        }
        // device 접근자
        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }
        // device 초기화
        public void clear() {
            mLeDevices.clear();
        }
        // device 개수
        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // 일반 ListView 최적화 코드
            if (view == null) { // view가 없을때
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName); // textview에 deviceName 출력
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress()); // textview에 deviceAddress 출력

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            //기본 데이터가 변경되었으며 데이터 세트를 반영한 ​​뷰가 자체적으로 새로 고쳐 져야 함을 알립
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}