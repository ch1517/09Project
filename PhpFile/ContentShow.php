<?php
  include("./db_connect.php");

  $idx = intval($_POST["idx"]);
  $hit = intval($_POST["hit"]);

  $statement = mysqli_prepare($con,"UPDATE product SET hit=$hit+1 WHERE idx = ? DESC LIMIT 10");
  mysqli_stmt_bind_param($statement, "i" ,$hit);
  mysqli_stmt_execute($statement);

  $response = array();
  $response["success"]= true;

  echo json_encode($response);
?>
