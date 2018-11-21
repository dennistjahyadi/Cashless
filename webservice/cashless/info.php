<?php
require_once "connection.php";
//header("Content-Type: application/json");

class Info
{
	private $db;
	private $connection;

	function __construct()
	{
		$this->db = new DB_Connection();
		$this->connection = $this->db->get_connection();

	}

	

	public function GetInfo($category)
	{
		$query="select * from infoapp where category=? and stat='1'";
		$stmt= $this->connection->prepare($query);
		$stmt->bind_param("s",$category);
		$stmt->execute();	
		$result = $stmt->get_result();
		$rows="";
		while($row=$result->fetch_assoc())
		{
			$rows = $row;

		}
		echo json_encode($rows);
		$stmt->close();
		$this->connection->close();	
	}	




}

$info = new Info();

if(isset($_GET["category"]))
{
	$category = $_GET["category"];
	$info->GetInfo($category);			
}

?>