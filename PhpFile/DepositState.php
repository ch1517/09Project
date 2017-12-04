<?php
  include("./db_connect.php");

// function change($state){//입금상태
//   if($state==1) return 0;//대기중으로 바꿈
//   else return 1;//완료로 바꿈
// }
// function change2($state){//인원증감
//   if($state==0) return -1;//완료->대기중으로 바뀌었으면 -1
//   else return +1;
// }
function change($state,$ordernum2){//입금상태
  global $s,$o;
  if($state==1){$s=0;$o=$ordernum2-1;} //대기중으로 바꿈
  else{$s=1;$o=$ordernum2+1;}//완료로 바꿈
}
  $uid = $_POST["id"];
  $pid = intval($_POST["idx"]);
  $uname= intval($_POST["name"]);
  $state = intval($_POST["state"]);
  $ordernum= intval($_POST["ordernum"]);
  $ordernum2= intval($_POST["ordernum2"]);

  $response = array();
  if($ordernum2<$ordernum){
      change($state,$ordernum2);
      // $state=change($state);
      // $ordernum2=$ordernum2+change2($state);
      mysqli_query($con,"UPDATE orderlist SET state=$s where pid=$pid AND uid='$uid' AND uname='$uname'");//입금완료
      mysqli_query($con,"UPDATE product SET ordernum2=$o  where idx=$pid");//인원증감

      $response["success"]= true;
      $response["name"] = $uname;
      $response["state"] = $s;
      $response["ordernum2"]= $o;
  }
  else $response["success"]= false;

  echo json_encode($response);
  mysqli_close($con);
?>
