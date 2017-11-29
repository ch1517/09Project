<?php
  include("./db_connect.php");
  
  $id=$_POST["id"];//유저아이디
  $pw=$_POST["pw"];
  $name=$_POST["name"];
  $phone=$_POST["phone"];
  $address=$_POST["address"];

  $statement = mysqli_prepare($con,"INSERT INTO user VALUES(?,?,?,?,?)");//테이블 조회
  mysqli_stmt_bind_param($statement, "sssss" , $id,$pw,$name,$phone,$address);
  mysqli_stmt_execute($statement);

  $response = array();
  $response["success"]= true;

   while(mysqli_stmt_fetch($statement)){//회원이 존재한다면
      $response ["success"]=false;//true 값 나옴
      $response ["id"]=$id;
    }

  echo json_encode($response);
?>
