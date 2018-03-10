<?php
header("Content-Type: text/html; charset=UTF-8");

	$con = mysqli_connect("localhost", "politicoreview", "memetgubler93!", "politicoreview");

mysqli_set_charset($con, "utf8");



	$userID = $_POST["userID"];
	$userPassword = $_POST["userPassword"];
	$userEmail = $_POST["userEmail"];
	$userGender = $_POST["userGender"];
	$userAge = $_POST["userAge"];
	$userState = $_POST["userState"];
	$userCity = $_POST["userCity"];

$checkedPassword = password_hash($userPassword, PASSWORD_DEFAULT);
	$statement = mysqli_prepare($con, "INSERT INTO USER VALUES (?, ?, ?, ?, ?, ?, ?)");
	mysqli_stmt_bind_param($statement, "sssssss", $userID, $checkedPassword, $userEmail, $userGender, $userAge, $userState, $userCity);
	mysqli_stmt_execute($statement);

	$response = array();
	$response["success"] = true;

	echo json_encode($response);
?>