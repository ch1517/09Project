<?php
  include("./db_connect.php");

    $id=$_POST["id"];

    $result = mysqli_prepare($con,"SELECT * FROM product ORDER BY ordernum2 DESC LIMIT 5");//주문량 많은 5가지 게시글
    $response = array();

    while($row=mysqli_fetch_array($result)){
      array_push($response, array("idx"=>$row[0],"id"=>$row[1], "title"=>$row[2], "price"=>$row[3],
                  "content"=>$row[4], "photo"=>$dir.$row[5], "ordernum"=>$row[6], "ordernum2"=>$row[7],
                  "hit"=>$row[8], "wdate"=>$row[9] ));
    }

    echo json_encode(array("response"=>$response),JSON_UNESCAPED_UNICODE);
    mysqli_close($con);
    echo json_encode($response);
?>
