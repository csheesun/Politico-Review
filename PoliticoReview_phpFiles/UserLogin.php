<?php
	$con = mysqli_connect("localhost", "politicoreview", "memetgubler93!", "politicoreview");

	$userID = $_POST["userID"];
	$userPassword = $_POST["userPassword"];

	$statement = mysqli_prepare($con, "SELECT * FROM USER WHERE userID = ?");

	mysqli_stmt_bind_param($statement, "s", $userID);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $userID, $checkedPassword, $userGender, $userAge, $userEmail, $userState, $userCity);

	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement)) {
		if(password_verify($userPassword, $checkedPassword)){
		$response["success"] = true;
		$response["userID"] = $userID;
}
	}

	echo json_encode($response);
?>