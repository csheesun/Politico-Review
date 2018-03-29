<?php
	header("Content-Type: text/html; charset=UTF-8");
	$con = mysqli_connect("localhost", "politicoreview", "memetgubler93!", "politicoreview");
	mysqli_set_charset($con, "utf8");

	$result = mysqli_query($con, "SELECT * FROM VOTE, POLITICO, USER WHERE USER.userAge <= 79 AND USER.userAge >= 70 
 AND USER.userID = VOTE.userID AND VOTE.politicoID = POLITICO.politicoID GROUP BY VOTE.politicoID ORDER BY COUNT(VOTE.politicoID)  DESC;");

	$response = array();
	while($row = mysqli_fetch_array($result)){
array_push($response, array("politicoID"=>$row[1], 
"politicoCity"=>$row[6],"politicoName"=>$row[3],"politicoState"=>$row[4], 
 "politicoParty"=>$row[5],  
"localProportional"=>$row[7], "politicoElectionNumber"=>$row[8]));
	}

	echo json_encode(array("response"=>$response), JSON_UNESCAPED_UNICODE); 
	mysqli_close($con);
?>