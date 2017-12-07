<?

$data = $_POST["data1"];

$file_path = $data."/";



echo $file_path;

    if(is_dir($data)){

        echo "폴더 존재 O"; // pass

    } else {

        echo "폴더 존재 X";

        @mkdir($data, 0777);

        @chmod($data, 0777);

    }



    $file_path = $file_path . basename( $_FILES['uploaded_file']['name']);



    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {

        echo "file upload success";

    } else{

        echo "file upload fail";

    }

?>

<?
include("./db_connect.php");
    $data = $_POST["data1"];
    //$data = $_POST["userfile"];
    $file_path = "/newImage/";
    // if(is_dir($data)){
    //     echo "폴더 존재 O"; // pass
    // } else {
    //     echo "폴더 존재 X";
    //     @mkdir($data, 0777);
    //     @chmod($data, 0777);
    // }
    $file_name= basename( $_FILES['uploaded_file']['name']);
    $file_path = $file_path . $file_name;
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        echo "file upload success";
    } else{
        echo "file upload fail";
    }

    mysqli_close($con);


?>
