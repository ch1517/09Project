<?php
  include("./db_connect.php");

  $id = $_POST["id"];
  $title = $_POST["title"];
  $price = intval($_POST["price"]);
  $content = $_POST["content"];
  $ordernum = intval($_POST["ordernum"]);
  $wdate = date("Y-m-d");

  $data = $_POST["data1"];//newImage란 값이 넘어옴
  $file_path = $data."/";// newImage밑을 의미하므로 /를 붙여줌

  echo $file_path;
      if(is_dir($data)){
          echo "폴더 존재 O"; // pass
      } else {
          echo "폴더 존재 X";
          @mkdir($data, 0777);
          @chmod($data, 0777);
      }
      $file_name= basename( $_FILES['uploaded_file']['name']);
      // basename : 디렉토리명이 있다면, 그 부분을 제외하고 파일명만 출력, 즉 abc/def/ghi.jpg 면 ghi.jpg만 가져올 수 있음
      $file_path = $file_path . $file_name;
      if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
          echo "file upload success";
      } else{
          echo "file upload fail";
      }

  // $dir = "./photo/";
  // // $file_hash = $date.$_FILES['file']['name'];
  // // $file_hash = md5($file_hash);
  // $upfile = $dir.$_FILES['photo']['name'];
  // if(is_uploaded_file($_FILES['photo']['tmp_name'])){
  // 	          if(!move_uploaded_file($_FILES['photo']['tmp_name'], $upfile)){
  // 	                  echo "upload error";
  // 	                  exit;
  // 	          }
  // }

  $statement = mysqli_prepare($con,"INSERT INTO product(id, title, price, content, photo, ordernum, wdate) VALUES (?,?,?,?,?,?,?)");
  mysqli_stmt_bind_param($statement, "ssissis" ,$id, $title, $price, $content, $file_path , $ordernum, $wdate);
  mysqli_stmt_execute($statement);

  $response = array();
  $response["success"]= true;

  echo json_encode($response);
?>