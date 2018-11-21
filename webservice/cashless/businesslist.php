<?php
	require_once "connection.php";
	require_once "crypto.php";
	//header("Content-Type: application/json");
	
	class BusinessList
	{
		private $db;
		private $connection;
		private $crypto;
		
		function __construct()
		{
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
			$this->crypto = new Encryption();
		}
		
		public function addBusiness($email,$pass,$businessName,$businessDesc,$amount)
		{
			$pass = $this->crypto->decrypt($pass);
			$pass = md5($pass);
			$query="select id from users where email=? and pass=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("ss",$email,$pass);
			$stmt->execute();	
			$result = $stmt->get_result();
			$row=$result->fetch_assoc();
			$userId = $row["id"];
			if(count($row)==1){
				
				$query="insert into business_list (fk_users,business_name,business_desc,amount) values (?,?,?,?)";
				$stmt= $this->connection->prepare($query);
				$stmt->bind_param("ssss",$userId,$businessName,$businessDesc,$amount);
				if($stmt->execute())
				{
					echo "1";
					//send email to user.					
				}else
				{
					echo "0";
				}
			}
			
			$stmt->close();
			$this->connection->close();	
		}
		
		public function fetchAllBusinessListByUserId($userId,$offset)
		{
			$query="select * from business_list where fk_users=? limit 15 offset ?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("ss",$userId,$offset);
			$stmt->execute();	
			$result = $stmt->get_result();
			$rows = array();
			while($row=$result->fetch_assoc())
			{
				$rows[] = $row;
			}
			echo json_encode($rows);
			$stmt->close();
			$this->connection->close();	
		}
		
		public function getBusinessInfoById($id)
		{
			$query="select bl.id as id,bl.fk_users as fk_users,bl.business_name as business_name,
			bl.business_desc as business_desc, bl.amount as amount,u.fullname as receiverFullname, u.email as receiverEmail
			from business_list as bl inner join users as u on bl.fk_users=u.id where bl.id=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("s",$id);
			$stmt->execute();	
			$result = $stmt->get_result();
			$row=$result->fetch_assoc();
			
			
			echo json_encode($row);
			$stmt->close();
			$this->connection->close();	
		}
		
		public function deleteBusinessById($businessId)
		{
			$query="delete FROM business_list where id = ?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("s",$businessId);
			if($stmt->execute())	
			{
				echo "1";
			}
			$stmt->close();
			$this->connection->close();	
		}
		
		
		
	}
	
	$bl = new BusinessList();
	
	if(isset($_POST["email"])&&isset($_POST["pass"])&&isset($_POST["businessName"])&&isset($_POST["businessDesc"])&&isset($_POST["amount"]))
	{
		$email = $_POST["email"];
		$pass = $_POST["pass"];
		$businessName = $_POST["businessName"];
		$businessDesc = $_POST["businessDesc"];
		$amount = $_POST["amount"];
		
		$bl->addBusiness($email,$pass,$businessName,$businessDesc,$amount);			
	}else if(isset($_GET["userId"])&&isset($_GET["fetchAllBusinessListByUserId"]))
	{
		$userId = $_GET["userId"];
		$offset = $_GET["fetchAllBusinessListByUserId"];
		
		$bl->fetchAllBusinessListByUserId($userId,$offset);
	}else if(isset($_GET["businessId"])&&isset($_GET["getBusinessInfoById"]))
	{
		$id = $_GET["businessId"];
		
		$bl->getBusinessInfoById($id);
		
	}else if(isset($_POST["businessId"])&&isset($_POST["deleteBusinessById"]))
	{
		$businessId = $_POST["businessId"];
		
		$bl->deleteBusinessById($businessId);
	}
	
	
?>																																