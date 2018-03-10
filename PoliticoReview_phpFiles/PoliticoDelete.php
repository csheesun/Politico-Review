<?php 
	header("Content-Type: text/html; charset=UTF-8");
	$con = mysqli_connect("localhost", "politicoreview", "memetgubler93!", "politicoreview");
	mysqli_set_charset($con, "utf8");

	$userID = $_POST["userID"];
	$politicoID = $_POST["politicoID"];

	$statement = mysqli_query($con, "DELETE FROM VOTE WHERE userID = '$userID' AND politicoID = '$politicoID' ");

	$response = array();
	$response["success"] = true;

	echo json_encode($response);
?>