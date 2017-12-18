<?php include("../db_connect.php");?>
<?php
	function send_notification ($tokens, $message)
	{
		$url = 'https://fcm.googleapis.com/fcm/send';
		$fields = array(
			 'registration_ids' => $tokens,
			 'data' => $message
			);

		$headers = array(
			'Authorization:key =' . GOOGLE_API_KEY,
			'Content-Type: application/json'
			);

	   $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
       return $result;
	}

define("GOOGLE_API_KEY", "AAAAw3l-knc:APA91bG2WkJkSOUMJH1JPzinLoSVJJN14CCcmixHmEvO4SRyKlhwxqGhN6R7uB0uuTZR52BX8mRo8LYxOz_HCTxwhPYAJrawK5nafI2mwBmqNq--rvq6P0pFg4OYDiQaG7OXPlUcK_hb");

$count = intval($_POST["count"]);
$pid = intval($_POST["idx"]);

$response = array();
$response["success"]= false;
if(empty($_POST["count"])){
  	$sql = "Select token,uname,delivery From orderlist where pid=$pid and state=2";
		$result = mysqli_query($con,$sql);

		if(mysqli_num_rows($result) > 0 ){
			while ($row = mysqli_fetch_assoc($result)) {
				$tokens = array();
				$tokens[] = $row["token"];
				$myMessage = $row["uname"]."님 ".$row["delivery"];
				$message = array("message" => $myMessage);
				$message_status = send_notification($tokens, $message);
echo $message_status;
				}
		}

		$response["success"]= true;
}
else{
	$sql = "Select token From orderlist where pid=$pid and state=2"; //일단 해당 게시글 입금자 일괄 푸시 발송

	$result = mysqli_query($con,$sql);
	$tokens = array();

	if(mysqli_num_rows($result) > 0 ){
		while ($row = mysqli_fetch_assoc($result)) {
			$tokens[] = $row["token"];
		}
	}

  $myMessage = $_POST['message']; //폼에서 입력한 메세지를 받음
	$message = array("message" => $myMessage);
	$message_status = send_notification($tokens, $message);
	echo $message_status;

	$response["success"]= true;
}

echo json_encode($response);
mysqli_close($con);
?>
