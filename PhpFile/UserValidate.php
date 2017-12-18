<?php
  include("./db_connect.php");
  
  $id=$_POST["id"];

  $statement = mysqli_prepare($con,"SELECT * FROM user WHERE id = ?");//테이블 조회
  mysqli_stmt_bind_param($statement, "s" , $id);
  mysqli_stmt_execute($statement);
  mysqli_stmt_store_result($statement);
  mysqli_stmt_bind_result($statement, $id);

  $response = array();
  $response["success"]= true;

   while(mysqli_stmt_fetch($statement)){//회원이 존재한다면
      $response ["success"]=false;//실패:이미 존재하는 아이디
      $response ["id"]=$id;
    }

  echo json_encode($response);
?>
