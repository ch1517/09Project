<?
include("./db_connect.php");
$uid = $_POST["id"];
$pid = intval($_POST["idx"]);
$uname = $_POST["name"];

$response = array();
$response["success"]= false;
if(isset($_POST["delivery"])){
    $delivery=$_POST["delivery"];
    mysqli_query($con, "UPDATE orderlist SET delivery='$delivery' where pid=$pid AND uid='$uid' AND uname='$uname'");
    $response["success"]= true;
}
echo json_encode($response);
mysqli_close($con);
?>
