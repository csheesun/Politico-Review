<?php
	header("Content-Type: text/html; charset=UTF-8");
	$con = mysqli_connect("localhost", "politicoreview", "memetgubler93!", "politicoreview");
	mysqli_set_charset($con, "utf8");

	$result = mysqli_query($con, "SELECT *
 FROM POLITICO, VOTE WHERE POLITICO.politicoID = VOTE.politicoID GROUP BY VOTE.politicoID ORDER BY COUNT(VOTE.politicoID) DESC;");

	$response = array();
	while($row = mysqli_fetch_array($result)){
		array_push($response, array("politicoID"=>$row[0],  "politicoState"=>$row[2], "politicoCity"=>$row[4], "politicoName"=>$row[1], "politicoParty"=>$row[3], 
"localProportional"=>$row[5],"politicoElectionNumber"=>$row[6]));
}

	echo json_encode(array("response"=>$response), JSON_UNESCAPED_UNICODE); 
	mysqli_close($con);
?>