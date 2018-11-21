<?php
	require_once "connection.php";
	require_once "crypto.php";
	//header("Content-Type: application/json");
	
	class Transaction
	{
		private $db;
		private $connection;
		private $crypto;
		
		function __construct()
		{
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
			$this->crypto = new Encryption();
			date_default_timezone_set('Asia/Bangkok');
			
		}
		
		public function doTransaction($userIdSender,$emailSender,$passSender,$emailReceiver,$userIdReceiver,$amount,$description)
		{
			//decrypt data first
			//encrypted = $userIdSender,$passSender,$userIdReceiver;
			
			$userIdSender = $this->crypto->decrypt($userIdSender);
			$userIdReceiver = $this->crypto->decrypt($userIdReceiver);
			$passSender = $this->crypto->decrypt($passSender);
			
			$passSender = md5($passSender);
			
			$query="select id from users where email=? and pass=? and id=? and verified='1' and pass is not null";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("sss",$emailSender,$passSender,$userIdSender);
			$stmt->execute();	
			$stmt->store_result();
			if($stmt->num_rows==1){
				// can login
				//get user sender balance.
				$query = "select balance from users where email=? and pass=? and id=? and verified='1'";
				$stmt= $this->connection->prepare($query);
				$stmt->bind_param("sss",$emailSender,$passSender,$userIdSender);
				$stmt->execute();	
				$result = $stmt->get_result();
				$row=$result->fetch_assoc();
				
				$userSenderBalance = (int)$row["balance"];
				
				if($userSenderBalance>$amount)
				{
					
					$currentBalanceSender = $userSenderBalance-$amount;
					
					//update user sender balance.
					$query="update users set balance=? where email=? and pass=? and id=? and verified='1'";
					$stmt= $this->connection->prepare($query);
					$stmt->bind_param("ssss",$currentBalanceSender,$emailSender,$passSender,$userIdSender);
					if($stmt->execute())
					{
						
					}
					
					//get user receiver balance.
					$query = "select balance from users where email=? and id=? and verified='1'";
					$stmt= $this->connection->prepare($query);
					$stmt->bind_param("ss",$emailReceiver,$userIdReceiver);
					
					$stmt->execute();	
					$result = $stmt->get_result();
					$row=$result->fetch_assoc();
					
					$userReceiverBalance = (int)$row["balance"];
					
					
					$currentBalanceReceiver = $userReceiverBalance+$amount;
					
					//update user receiver balance.
					$query="update users set balance=? where email=? and id=? and verified='1'";
					$stmt= $this->connection->prepare($query);
					$stmt->bind_param("sss",$currentBalanceReceiver,$emailReceiver,$userIdReceiver);
					if($stmt->execute())
					{
						
					}
					
					
					$datetime = new DateTime();
					$datetimeString = $datetime->format('Y-m-d H:i:s');
					$timestamp = $datetime->getTimestamp();
					$arr1 = str_split($timestamp);
					$characters = 'abcdefghjkmnopqrstuvwxyz';
					$charactersLength = strlen($characters);
					$randomString = '';
					$transactionId = "";
					foreach($arr1 as $arg)
					{
						$randomString = $characters[rand(0, $charactersLength - 1)];
						
						$transactionId .= $arg.$randomString;
					}
					
					
					$fee = 0;
					$totalAmount = $amount-$fee;
					//insert into history transaction
					$query="insert into history_transaction 
					(id,fk_userId_sender,fk_userId_receiver,amount,fee,totalAmount,description,_datetime,fk_bank,method,status) 
					values (?,?,?,?,?,?,?,?,null,'transaction','Success')";
					$stmt= $this->connection->prepare($query);
					$stmt->bind_param("ssssssss",$transactionId,$userIdSender,$userIdReceiver,$amount,$fee,$totalAmount,$description,$datetimeString);
					
					if($stmt->execute())
					{
						$historyTransactionId = $transactionId;
						
						
						$query = "select ht.id as id,ht.fk_userId_sender as senderUserId,ht.description as description, usender.email as senderEmail, usender.fullname as senderFullname,
						ht.fk_userId_receiver as receiverUserId, ureceiver.email as receiverEmail,ureceiver.fullname as receiverFullname,
						ht.amount as amount,ht.fee as fee,ht.totalAmount as totalAmount,ht.method as method, ht.status as status,
						ht.fk_bank as bank,ht._datetime as datetime  
						from history_transaction as ht inner join users as usender 
						on ht.fk_userId_sender=usender.id inner join users as ureceiver 
						on ht.fk_userId_receiver = ureceiver.id where ht.id=?";
						$stmt= $this->connection->prepare($query);
						$stmt->bind_param("s",$historyTransactionId);
						
						$stmt->execute();	
						$result = $stmt->get_result();
						$row=$result->fetch_assoc();
						
						echo json_encode($row);	
						
					}else
					{
						echo "{}";
					}
				}else
				{
					echo "{}";
				}
				
			}else
			{
				echo "{}";
				
			}
			
			$stmt->close();
			$this->connection->close();	
		}
		
		public function doWithdraw($userId,$email,$pass,$bankAccountId,$amount)
		{
			//decrypt data first
			
			$userId = $this->crypto->decrypt($userId);
			$pass = $this->crypto->decrypt($pass);
			
			$pass = md5($pass);
			
			$query="select id from users where email=? and pass=? and id=? and verified='1' and pass is not null";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("sss",$email,$pass,$userId);
			$stmt->execute();	
			$stmt->store_result();
			if($stmt->num_rows==1){
				// can login
				//get user sender balance.
				$query = "select balance from users where email=? and pass=? and id=? and verified='1'";
				$stmt= $this->connection->prepare($query);
				$stmt->bind_param("sss",$email,$pass,$userId);
				$stmt->execute();	
				$result = $stmt->get_result();
				$row=$result->fetch_assoc();
				
				$userSenderBalance = (int)$row["balance"];
				
				if($userSenderBalance>$amount)
				{
					
					$currentBalanceSender = $userSenderBalance-$amount;
					
					//update user sender balance.
					$query="update users set balance=? where email=? and pass=? and id=? and verified='1'";
					$stmt= $this->connection->prepare($query);
					$stmt->bind_param("ssss",$currentBalanceSender,$email,$pass,$userId);
					if($stmt->execute())
					{
						
					}
					
					
					$datetime = new DateTime();
					$datetimeString = $datetime->format('Y-m-d H:i:s');
					$timestamp = $datetime->getTimestamp();
					$arr1 = str_split($timestamp);
					$characters = 'abcdefghjkmnopqrstuvwxyz';
					$charactersLength = strlen($characters);
					$randomString = '';
					$transactionId = "";
					foreach($arr1 as $arg)
					{
						$randomString = $characters[rand(0, $charactersLength - 1)];
						
						$transactionId .= $arg.$randomString;
					}
					
					
					$fee = 0;
					$totalAmount = $amount-$fee;
					//insert into history transaction
					$query="insert into history_transaction 
					(id,fk_userId_sender,fk_userId_receiver,amount,fee,totalAmount,description,_datetime,fk_bank,method,status) 
					values (?,?,null,?,?,?,null,?,?,'withdraw','Pending')";
					$stmt= $this->connection->prepare($query);
					$stmt->bind_param("sssssss",$transactionId,$userId,$amount,$fee,$totalAmount,$datetimeString,$bankAccountId);
					
					if($stmt->execute())
					{
						$historyTransactionId = $transactionId;
						
						
						$query = "select ht.id as id,ht.fk_userId_sender as senderUserId,usender.email as senderEmail, usender.fullname as senderFullname,
						ht.fk_userId_receiver as receiverUserId,
						ht.amount as amount,ht.fee as fee,ht.totalAmount as totalAmount,ht.method as method, ht.status as status,
						ht.fk_bank as bank,ht._datetime as datetime,ba.atas_nama as bankAccountName, ba.no_rekening as bankAccountNumber,bl.bank_name as bankName  
						from history_transaction as ht inner join users as usender 
						on ht.fk_userId_sender=usender.id inner join bank_account as ba on ba.id=ht.fk_bank inner join bank_list as bl on bl.id=ba.fk_bank
						where ht.id=?";
						$stmt= $this->connection->prepare($query);
						$stmt->bind_param("s",$historyTransactionId);
						
						$stmt->execute();	
						$result = $stmt->get_result();
						$row=$result->fetch_assoc();
						
						echo json_encode($row);	
						
					}else
					{
						echo "{\"id\":\"-5\"}";
					}
				}else
				{
					echo "{\"id\":\"-5\"}";
				}
				
			}else
			{
				echo "{\"id\":\"-3\"}";
				
			}
			
			$stmt->close();
			$this->connection->close();	
		}
		
		public function doDeposit($userId,$email,$pass,$amount)
		{
			$userId = $this->crypto->decrypt($userId);
			$pass = $this->crypto->decrypt($pass);
			if($amount >=10000)
			{
				
				$pass = md5($pass);
				$query="select id from users where email=? and pass=? and id=? and verified='1' and pass is not null";
				$stmt= $this->connection->prepare($query);
				$stmt->bind_param("sss",$email,$pass,$userId);
				$stmt->execute();	
				$stmt->store_result();
				if($stmt->num_rows==1){
					
					$datetime = new DateTime();
					$datetimeString = $datetime->format('Y-m-d H:i:s');
					$timestamp = $datetime->getTimestamp();
					$arr1 = str_split($timestamp);
					$characters = 'abcdefghjkmnopqrstuvwxyz';
					$charactersLength = strlen($characters);
					$randomString = '';
					$transactionId = "";
					foreach($arr1 as $arg)
					{
						$randomString = $characters[rand(0, $charactersLength - 1)];
						
						$transactionId .= $arg.$randomString;
					}
					
					$fee = 0;
					$totalAmount = $amount-$fee;
					
					$query="insert into history_transaction 
					(id,fk_userId_sender,fk_userId_receiver,amount,fee,totalAmount,description,_datetime,fk_bank,method,status) 
					values (?,null,?,?,?,?,null,?,null,'deposit','Pending')";
					$stmt= $this->connection->prepare($query);
					$stmt->bind_param("ssssss",$transactionId,$userId,$amount,$fee,$totalAmount,$datetimeString);
					
					if($stmt->execute())
					{
						$historyTransactionId = $transactionId;
						
						
						$query = "select ht.id as id,ht.fk_userId_sender as senderUserId,ureceiver.email as receiverEmail, ureceiver.fullname as receiverFullname,
						ht.fk_userId_receiver as receiverUserId,
						ht.amount as amount,ht.fee as fee,ht.totalAmount as totalAmount,ht.method as method, ht.status as status,
						ht.fk_bank as bank,ht._datetime as datetime,ba.atas_nama as bankAccountName, ba.no_rekening as bankAccountNumber,bl.bank_name as bankName  
						from history_transaction as ht inner join users as ureceiver 
						on ht.fk_userId_receiver=ureceiver.id left join bank_account as ba on ba.id=ht.fk_bank left join bank_list as bl on bl.id=ba.fk_bank
						where ht.id=?";
						$stmt= $this->connection->prepare($query);
						$stmt->bind_param("s",$historyTransactionId);
						
						$stmt->execute();	
						$result = $stmt->get_result();
						$row=$result->fetch_assoc();
						
						echo json_encode($row);	
						
					}else
					{
						echo "{\"id\":\"-5\"}";
					}
				}else
				{
					echo "{\"id\":\"-5\"}";
					
				}
			}else
			{
				echo "{\"id\":\"-5\"}";
				
			}
			
			
			$stmt->close();
			$this->connection->close();
		}
		
	}
	
	$transaction = new Transaction();
	if(isset($_POST["userIdSender"])&& isset($_POST["emailSender"])&&isset($_POST["passSender"])&&isset($_POST["emailReceiver"])
	&&isset($_POST["userIdReceiver"])&&isset($_POST["amount"])&&isset($_POST["description"]))
	{
		//the data must be encrypted first;
		$userIdSender = $_POST["userIdSender"];
		$emailSender = $_POST["emailSender"];
		$passSender = $_POST["passSender"];
		$emailReceiver = $_POST["emailReceiver"];
		$userIdReceiver = $_POST["userIdReceiver"];
		$amount = $_POST["amount"];
		$description = $_POST["description"];
		$transaction->doTransaction($userIdSender,$emailSender,$passSender,$emailReceiver,$userIdReceiver,$amount,$description);
	}else if(isset($_POST["userId"])&&isset($_POST["email"])
	&&isset($_POST["pass"])&&isset($_POST["bankAccountId"])
	&&isset($_POST["amount"])&&isset($_POST["doWithdraw"]))
	{
		$userId = $_POST["userId"];
		$email = $_POST["email"];
		$pass = $_POST["pass"];
		$bankAccountId = $_POST["bankAccountId"];
		$amount = $_POST["amount"];
		
		
		$transaction->doWithdraw($userId,$email,$pass,$bankAccountId,$amount);
	}else if(isset($_POST["userId"])&&isset($_POST["email"])
	&&isset($_POST["pass"])&&isset($_POST["amount"])&&isset($_POST["doDeposit"]))
	{
		$userId = $_POST["userId"];
		$email = $_POST["email"];
		$pass = $_POST["pass"];
		$amount = $_POST["amount"];
		
		
		$transaction->doDeposit($userId,$email,$pass,$amount);
	}
	
?>																																