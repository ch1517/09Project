<?
// 데이터베이스에 연결
$mysql_host = "localhost";
$mysql_user = "ekfms35";
$mysql_password = "qlalf123";
$db_name = "ekfms35";

//아이피 주소, 아이디, 비밀번호, DB명
$con = mysqli_connect($mysql_host, $mysql_user, $mysql_password, $db_name);
mysqli_set_charset($con,"utf8");
?>
