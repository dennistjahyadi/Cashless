<?php
	require_once "connection.php";
	require_once "crypto.php";
	//header("Content-Type: application/json");
	
	class HistoryTransaction
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
		
		public function fetchAllHistoryTransaction($userId,$offset)
		{
			$query = "select combine.* from (select ht.id as id,ht.fk_userId_sender as senderUserId,ht.description as description, usender.email as senderEmail, usender.fullname as senderFullname,
			ht.fk_userId_receiver as receiverUserId, ureceiver.email as receiverEmail,ureceiver.fullname as receiverFullname,
			ht.amount as amount,ht.fee as fee,ht.totalAmount as totalAmount,ht.method as method, ht.status as status,
			ht.fk_bank as bank,ht._datetime as datetime,ba.atas_nama as bankAccountName, ba.no_rekening as bankAccountNumber,bl.bank_name as bankName   
			from history_transaction as ht left join users as usender 
			on ht.fk_userId_sender=usender.id left join users as ureceiver 
			on ht.fk_userId_receiver = ureceiver.id left join bank_account 
			as ba on ba.id=ht.fk_bank left join bank_list as bl on bl.id=ba.fk_bank
			where fk_userId_sender=? or fk_userId_receiver=?
			union 
			select ht.id as id,ht.fk_userId_sender as senderUserId,ht.description as description, usender.email as senderEmail, usender.fullname as senderFullname,
			ht.fk_userId_receiver as receiverUserId, ureceiver.email as receiverEmail,ureceiver.fullname as receiverFullname,
			ht.amount as amount,ht.fee as fee,ht.totalAmount as totalAmount,ht.method as method, ht.status as status,
			ht.fk_bank as bank,ht._datetime as datetime,ba.atas_nama as bankAccountName, ba.no_rekening as bankAccountNumber,bl.bank_name as bankName   
			from history_transaction as ht right join users as usender 
			on ht.fk_userId_sender=usender.id right join users as ureceiver 
			on ht.fk_userId_receiver = ureceiver.id right join bank_account 
			as ba on ba.id=ht.fk_bank right join bank_list as bl on bl.id=ba.fk_bank
			where fk_userId_sender=? or fk_userId_receiver=?) as combine order by combine.datetime DESC limit 20 offset ?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("sssss",$userId,$userId,$userId,$userId,$offset);
			
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
		
		public function fetchInHistoryTransaction($userId,$offset)
		{
			$query = "select ht.id as id,ht.fk_userId_sender as senderUserId,ht.description as description, ht.description as description, usender.email as senderEmail, usender.fullname as senderFullname,
			ht.fk_userId_receiver as receiverUserId, ureceiver.email as receiverEmail,ureceiver.fullname as receiverFullname,
			ht.amount as amount,ht.fee as fee,ht.totalAmount as totalAmount,ht.method as method, ht.status as status,
			ht.fk_bank as bank,ht._datetime as datetime  
			from history_transaction as ht left join users as usender 
			on ht.fk_userId_sender=usender.id right join users as ureceiver 
			on ht.fk_userId_receiver = ureceiver.id where fk_userId_receiver=? order by ht._datetime DESC limit 20 offset ?";
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
		
		public function fetchOutHistoryTransaction($userId,$offset)
		{
			$query = "select ht.id as id,ht.fk_userId_sender as senderUserId,usender.email as senderEmail,ht.description as description,  usender.fullname as senderFullname,
			ht.fk_userId_receiver as receiverUserId, ureceiver.email as receiverEmail,ureceiver.fullname as receiverFullname,
			ht.amount as amount,ht.fee as fee,ht.totalAmount as totalAmount,ht.method as method, ht.status as status,
			ht.fk_bank as bank,ht._datetime as datetime,ba.atas_nama as bankAccountName, ba.no_rekening as bankAccountNumber,bl.bank_name as bankName  
			from history_transaction as ht inner join users as usender 
			on ht.fk_userId_sender=usender.id left join users as ureceiver 
			on ht.fk_userId_receiver = ureceiver.id left join bank_account as ba on ba.id=ht.fk_bank left join bank_list as bl on bl.id=ba.fk_bank
			where fk_userId_sender=? order by ht._datetime DESC limit 20 offset ?";
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
		
		
		
	}
	
	$transaction = new HistoryTransaction();
	if(isset($_POST["userId"])&&isset($_POST["offset"])&&isset($_POST["fetchAllHistoryTransaction"]))
	{
		//the data must be encrypted first;
		$userId = $_POST["userId"];
		$offset = $_POST["offset"];
		
		$transaction->fetchAllHistoryTransaction($userId,$offset);
	}else if(isset($_POST["userId"])&&isset($_POST["offset"])&&isset($_POST["fetchInHistoryTransaction"]))
	{
		$userId = $_POST["userId"];
		$offset = $_POST["offset"];
		
		$transaction->fetchInHistoryTransaction($userId,$offset);
	}else if(isset($_POST["userId"])&&isset($_POST["offset"])&&isset($_POST["fetchOutHistoryTransaction"]))
	{
		$userId = $_POST["userId"];
		$offset = $_POST["offset"];
		
		$transaction->fetchOutHistoryTransaction($userId,$offset);
	}
	
?>																																	