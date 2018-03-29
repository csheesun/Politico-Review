<?php
	header("Content-Type: text/html; charset=UTF-8");
	$con = mysqli_connect("localhost", "politicoreview", "memetgubler93!", "politicoreview");
	mysqli_set_charset($con, "utf8");

	$userID = $_GET["userID"];

	$result = mysqli_query($con, "
	SELECT POLITICO.politicoID, POLITICO.politicoState, POLITICO.politicoCity, POLITICO.localProportional, POLITICO.politicoParty, POLITICO.politicoName, COUNT(VOTE.politicoID),  POLITICO.politicoElectionNumber
	FROM VOTE, POLITICO
	WHERE VOTE.politicoID IN (SELECT VOTE.politicoID 
								FROM VOTE
								WHERE VOTE.userID = '$userID') AND VOTE.politicoID = POLITICO.politicoID 
	GROUP BY VOTE.politicoID");


	$response = array();
	while($row = mysqli_fetch_array($result)){
		array_push($response, array("politicoID"=>$row[0],  "politicoState"=>$row[1], "politicoCity"=>$row[2], "localProportional"=>$row[3],"politicoName"=>$row[5], "politicoParty"=>$row[4], "COUNT(VOTE.politicoID)"=>$row[6], "politicoElectionNumber"=>$row[7]));
	}

	echo json_encode(array("response"=>$response), JSON_UNESCAPED_UNICODE); 
	mysqli_close($con);
?>