<?php
  include("./db_connect.php");

  $uid = $_POST["id"];//주문자 id
  $uname = $_POST["name"];//입금자명
  $pid = intval($_POST["idx"]);//상품게시물 idx
  $memo = $_POST["memo"];
  $state = 0 ;//입금 전

  $statement = mysqli_prepare($con,"INSERT INTO orderlist VALUES (?, ?, ?, ?, ?)");
  mysqli_stmt_bind_param($statement, "ssiss" , $uid, $uname, $pid, $memo, $state);
  mysqli_stmt_execute($statement);

  $response = array();
  $response["success"]= true;

  echo json_encode($response);
?>
