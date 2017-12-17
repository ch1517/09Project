<?php
  include("./db_connect.php");

  $result = mysqli_query($con,"SELECT * FROM product order by idx desc");//테이블 조회
  $response = array();
 // $dir="./newImage/";
  while($row=mysqli_fetch_array($result)){
    array_push($response, array("idx"=>$row[0],"id"=>$row[1], "title"=>$row[2], "price"=>$row[3],
                "content"=>$row[4], "photo"=>$row[5], "ordernum"=>$row[6], "ordernum2"=>$row[7],
                "hit"=>$row[8], "wdate"=>$row[9] ));
  }

  echo json_encode(array("response"=>$response),JSON_UNESCAPED_UNICODE);
  mysqli_close($con);
?>