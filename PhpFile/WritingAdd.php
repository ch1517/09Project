<?php
  include("./db_connect.php");

  $id = $_POST["id"];
  $title = $_POST["title"];
  $price = intval($_POST["price"]);
  $content = $_POST["content"];
  $ordernum = intval($_POST["ordernum"]);
  $wdate = date("Y-m-d");

  $data = $_POST["userfile"];
  $file_path = "/newImage/".$data;

      //  echo $file_path;
      //$file_name= basename( $_FILES['uploaded_file']['name']);
      // basename : 디렉토리명이 있다면, 그 부분을 제외하고 파일명만 출력, 즉 abc/def/ghi.jpg 면 ghi.jpg만 가져올 수 있음
      // $file_path = $file_path . $data;
      // if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
      //     echo "file upload success";
      // } else{
      //     echo "file upload fail";
      // }

  $statement = mysqli_prepare($con,"INSERT INTO product(id, title, price, content, photo, ordernum, wdate) VALUES (?,?,?,?,?,?,?)");
  mysqli_stmt_bind_param($statement, "ssissis" ,$id, $title, $price, $content, $data , $ordernum, $wdate);
  mysqli_stmt_execute($statement);

  $response = array();
  $response["success"]= true;

  echo json_encode($response);
?>
