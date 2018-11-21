<?php
	require_once "connection.php";
	require_once "crypto.php";
	//header("Content-Type: application/json");
	
	class User
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
		
		public function registerUserClient($email,$fullname,$pass)
		{
			$query="select id from users where email=? and verified='1'";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("s",$email);
			$stmt->execute();	
			$stmt->store_result();
			
			if($stmt->num_rows==1){
				//email already registered
				echo "0";
			}else
			{
				
				$query="select id from users where email=? and verified='0'";
				$stmt= $this->connection->prepare($query);
				$stmt->bind_param("s",$email);
				$stmt->execute();	
				$stmt->store_result();
				if($stmt->num_rows==1){
					//if there is registered email but haven't done verified.
					//if someone register with fake email and someone who has that real email want to register.
					$query="update users set fullname=?, pass=?, stat='0'";
					$stmt= $this->connection->prepare($query);
					$stmt->bind_param("ss",$fullname,$pass);
					$stmt->execute();	
					if($stmt->execute())
					{
						echo "1";
					}
				}else
				{
					//there is no email registered.
					$six_digit_random_number = mt_rand(100000, 999999);
					$query="insert into users (email,fullname,pass,stat,verified,verification_code) values (?,?,?,'0','0',?)";
					$stmt= $this->connection->prepare($query);
					$pass = md5($pass);
					$stmt->bind_param("ssss",$email,$fullname,$pass,$six_digit_random_number);
					if($stmt->execute())
					{
						echo "1";
						//send email to user.
						
					}
				}
			}
			
			$stmt->close();
			$this->connection->close();	
		}	
		
		public function loginUser($email,$pass){
			
			$pass = $this->crypto->decrypt($pass);
			
			$pass = md5($pass);
			
			$query="select id from users where email=? and pass=? and verified='1' and pass is not null";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("ss",$email,$pass);
			$stmt->execute();	
			$stmt->store_result();
			if($stmt->num_rows==1){
				// can login
				echo "1";
			}else
			{
				$query="select id from users where email=? and pass=? and verified='0' and pass is not null";
				$stmt= $this->connection->prepare($query);
				$stmt->bind_param("ss",$email,$pass);
				$stmt->execute();	
				$stmt->store_result();
				if($stmt->num_rows==1){
					// need verified first
					echo "2";
				}else
				{
					// wrong email or password
					echo "0";
				}
			}
			$stmt->close();
			$this->connection->close();	
		}
		
		public function verification_code($email,$pass,$code){
			$pass = $this->crypto->decrypt($pass);
			
			$pass = md5($pass);
			
			$query = "select verification_code from users where email=? and pass=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("ss",$email,$pass);
			$stmt->execute();
			$result = $stmt->get_result();	
			$result = $result->fetch_assoc();
			if($result["verification_code"]===$code)
			{
				$query = "update users set verified='1' where email=? and pass=?";
				$stmt= $this->connection->prepare($query);
				$stmt->bind_param("ss",$email,$pass);
				if($stmt->execute())
				{
					echo "1";
				}
			}else
			{
				//wrong verified code
				echo "0";
			}
			
			
			$stmt->close();
			$this->connection->close();	
			
		}
		
		public function getUserInfo($email,$pass){
			$pass = $this->crypto->decrypt($pass);
			
			$pass = md5($pass);
			
			$query = "select balance,id,fullname,phone_no,address from users where email=? and pass=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("ss",$email,$pass);
			if($stmt->execute()){
				$result = $stmt->get_result();	
				$result = $result->fetch_assoc();
				echo json_encode($result);
			}
			$stmt->close();
			$this->connection->close();	
		}
		
		public function getUserInfoById($userId)
		{
			
			$query = "select fullname,email from users where id=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("s",$userId);
			if($stmt->execute()){
				$result = $stmt->get_result();	
				$result = $result->fetch_assoc();
				echo json_encode($result);
			}
			$stmt->close();
			$this->connection->close();	
		}
		
		public function resendCodeVerification($email,$pass){
			$pass = $this->crypto->decrypt($pass);
			
			$pass = md5($pass);
			$six_digit_random_number = mt_rand(100000, 999999);
			
			$query="select id from users where email=? and pass=? and verified='0'";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("ss",$email,$pass);
			$stmt->execute();	
			$stmt->store_result();
			if($stmt->num_rows==1){
				$query="update users set verification_code=? where email=? and pass=? and verified='0'";
				$stmt= $this->connection->prepare($query);
				$stmt->bind_param("sss",$six_digit_random_number,$email,$pass);
				if($stmt->execute())
				{
					//send email to user.
				}
			}
			$stmt->close();
			$this->connection->close();	
		}
		
		public function updateFullname($email,$pass,$fullname)
		{
			$pass = $this->crypto->decrypt($pass);
			
			$pass = md5($pass);
			$query="update users set fullname=? where email=? and pass=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("sss",$fullname,$email,$pass);
			if($stmt->execute())
			{
				echo "1";
			}
			
			$stmt->close();
			$this->connection->close();	
		}
		
		public function updateEmail($email,$pass,$updatedEmail)
		{
			$pass = $this->crypto->decrypt($pass);
			
			$pass = md5($pass);
			$query="update users set email=? where email=? and pass=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("sss",$updatedEmail,$email,$pass);
			if($stmt->execute())
			{
				echo "1";
			}else
			{
				$query="select id from users where email=?";
				$stmt= $this->connection->prepare($query);
				$stmt->bind_param("s",$updatedEmail);
				$stmt->execute();	
				$stmt->store_result();
				if($stmt->num_rows>0){
					echo "11"; //email already used
				}
			}
			$stmt->close();
			$this->connection->close();	
		}
		
		public function updatePhoneNo($email,$pass,$phoneNo)
		{
			$pass = $this->crypto->decrypt($pass);
			
			$pass = md5($pass);
			$query="update users set phone_no=? where email=? and pass=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("sss",$phoneNo,$email,$pass);
			if($stmt->execute())
			{
				echo "1";
				}
				$stmt->close();
			$this->connection->close();	
		}
		
		public function updateAddress($email,$pass,$address)
		{
			$pass = $this->crypto->decrypt($pass);
			
			$pass = md5($pass);
			$query="update users set address=? where email=? and pass=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("sss",$address,$email,$pass);
			if($stmt->execute())
			{
				echo "1";
			}
			$stmt->close();
			$this->connection->close();	
		}
		
		
		public function test($email,$pass){
			
			$query="select * from users where email=?";
			$stmt= $this->connection->prepare($query);
			$stmt->bind_param("s",$email);
			$stmt->execute();	
			$result = $stmt->get_result();
			$result = $result->fetch_assoc();
			echo $result["pass"];
			if($result["pass"]==$pass)
			{
				echo "1";
				
			}else
			echo "0";
			$stmt->close();
			$this->connection->close();	
		}
		
	}
	
	$user = new User();
	
	if(isset($_POST["email"])&&isset($_POST["fullname"])&&isset($_POST["pass"]))
	{
		$email = $_POST["email"];
		$fullname = $_POST["fullname"];
		$pass = $_POST["pass"];
		$user->registerUserClient($email,$fullname,$pass);			
	}else if(isset($_POST["email"])&&isset($_POST["pass"])&&isset($_POST["code"]))
	{
		$email = $_POST["email"];
		$pass = $_POST["pass"];
		$code =  $_POST["code"];
		$user->verification_code($email,$pass,$code);
	}else if(isset($_POST["email"])&&isset($_POST["pass"])&&isset($_POST["login"]))
	{
		$email = $_POST["email"];
		$pass = $_POST["pass"];
		$user->loginUser($email,$pass);		
	}else if(isset($_POST["email"])&&isset($_POST["pass"])&&isset($_POST["resendCodeVerification"]))
	{
		$email = $_POST["email"];
		$pass = $_POST["pass"];
		$user->resendCodeVerification($email,$pass);	
		
	}else if(isset($_POST["email"])&&isset($_POST["asd"]))
	{
		$email = $_POST["email"];
		$pass = $_POST["asd"];
		$user->test($email,$pass);		
		
	}else if(isset($_POST["email"])&&isset($_POST["pass"])&&isset($_POST["getUserInfo"]))
	{
		$email = $_POST["email"];
		$pass = $_POST["pass"];
		$user->getUserInfo($email,$pass);
	}else if(isset($_POST["email"])&&isset($_POST["pass"])&&isset($_POST["columnUpdated"])&&isset($_POST["updatedData"]))
	{
		$email = $_POST["email"];
		$pass = $_POST["pass"];
		$columnUpdated = $_POST["columnUpdated"];
		$updatedData = $_POST["updatedData"];
		if($columnUpdated == "fullname")
		{
			$user->updateFullname($email,$pass,$updatedData);
			
		}else if($columnUpdated == "email")
		{
			$user->updateEmail($email,$pass,$updatedData);
			
		}else if($columnUpdated == "phone_no")
		{
			$user->updatePhoneNo($email,$pass,$updatedData);
			
		}else if($columnUpdated == "address")
		{
			$user->updateAddress($email,$pass,$updatedData);
			
		}
	}else if(isset($_GET["userId"])&&isset($_GET["getReceiverInfoById"]))
	{
		$userId = $_GET["userId"];
		
		$user->getUserInfoById($userId);
		
	}
	
?>																																