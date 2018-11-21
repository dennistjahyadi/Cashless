<?php
	require_once "connection.php";
	require_once "crypto.php";
	//header("Content-Type: application/json");
	
	class Bank
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
		
		public function fetchAllBank()
		{
			$query="select id as bank_id,bank_name from bank_list";
			$stmt= $this->connection->prepare($query);		
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
		
		public function insertBankAccount($userId,$userPass,$bankId,$bankAccountName,$bankAccountNumber)
		{
			$userPass = $this->crypto->decrypt($userPass);
			
			$userPass = md5($userPass);
			
			$query="select id from users where id=? and pass=?";
			$stmt= $this->connection->prepare($query);		
			$stmt->bind_param("ss",$userId,$userPass);
			
			$stmt->execute();	
			$stmt->store_result();
			if($stmt->num_rows>0){
				$query="insert into bank_account (fk_users,fk_bank,atas_nama,no_rekening) values (?,?,?,?) ";
				$stmt= $this->connection->prepare($query);		
				$stmt->bind_param("ssss",$userId,$bankId,$bankAccountName,$bankAccountNumber);
				if($stmt->execute())
				{
					echo "1";
				}
			}else
			{
				echo "11";
			}
			
			$stmt->close();
			$this->connection->close();	
		}
		
		public function fetchBankAccountByUserId($userId)
		{
			
			$query="select ba.id as bank_account_id,ba.atas_nama,ba.no_rekening,bl.bank_name 
			from bank_account as ba inner join bank_list as bl on ba.fk_bank = bl.id 
			where ba.fk_users = ?";
			$stmt= $this->connection->prepare($query);		
			$stmt->bind_param("s",$userId);			
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
		
		
		
		
	}
	
	$bank = new Bank();
	
	if(isset($_GET["fetchAllBank"]))
	{
		if($_GET["fetchAllBank"] == "fetchAllBank"){
			$bank->fetchAllBank();			
		}
	}else if(isset($_POST["userId"])&&isset($_POST["userPass"])&&isset($_POST["bankId"])
	&&isset($_POST["bankAccountName"])&&isset($_POST["bankAccountNumber"])&&isset($_POST["insertBankAccount"]))
	{
		$userId = $_POST["userId"];
		$userPass = $_POST["userPass"];
		$bankId = $_POST["bankId"];
		$bankAccountName = $_POST["bankAccountName"];
		$bankAccountNumber = $_POST["bankAccountNumber"];
		$bank->insertBankAccount($userId,$userPass,$bankId,$bankAccountName,$bankAccountNumber);
	}else if(isset($_POST["userId"])&&isset($_POST["fetchBankAccountByUserId"]))
	{
		$userId = $_POST["userId"];
		$bank->fetchBankAccountByUserId($userId);
	}
	
?>																																