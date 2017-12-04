<?php
  include("./db_connect.php");

  $pid = intval($_POST["idx"]);//상품게시물 idx
  $response = array();
 $query= mysqli_query($con,"SELECT * FROM product where idx=$pid");
 $row=mysqli_fetch_assoc($query);
 array_push($response,array("ordernum2"=>$row['ordernum2']));

  $result = mysqli_query($con,"SELECT * FROM orderlist where pid=$pid");//테이블 조회


  while($row=mysqli_fetch_array($result)){
    array_push($response, array("uid"=>$row[0],"uname"=>$row[1], "memo"=>$row[3], "state"=>$row[4]));
  }
  echo json_encode(array("response"=>$response));
  mysqli_close($con);
?>
