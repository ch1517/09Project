<?php
  include("./db_connect.php");

  $id = $_POST["id"];
  $pw = $_POST["pw"];

  $statement = mysqli_prepare($con,"SELECT * FROM user WHERE id = ? AND pw = ?");//테이블 조회
  mysqli_stmt_bind_param($statement, "ss" , $id, $pw);
  mysqli_stmt_execute($statement);
  mysqli_stmt_store_result($statement);
  if(isset($_POST["token"])){
    $token = $_POST["token"];
    mysqli_query($con, "UPDATE user SET token='$token' WHERE id='$id' AND pw='$pw'");
    mysqli_close($con);
}
  mysqli_stmt_bind_result($statement, $id, $pw, $name, $phone, $address, $token);

  $response = array();
  $response["success"]= false;
   while(mysqli_stmt_fetch($statement)){//회원이 존재한다면
      $response["success"]=true;
      $response["id"]=$id;
    	$response["name"]=$name;
    	$response["phone"]=$phone;
    	$response["address"]=$address;
      $response["token"]=$token;
    }
  echo json_encode($response);
?>
