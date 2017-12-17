<?php
  include("./db_connect.php");

  $pid = intval($_POST["idx"]);
  $ordernum= intval($_POST["ordernum"]);
  $ordernum2= intval($_POST["ordernum2"]);

  if($ordernum2=$ordernum){
    mysqli_query($con,"UPDATE orderlist SET state=2 where pid=$pid AND state=1");

    $response["success"]= true;
  }
  else {
    $response["success"]= false;
  }
  echo json_encode($response);
  mysqli_close($con);
?>
