<?php


class DB_Connection
{

	private $connect;
	private $firebaseApi;
	function __construct()
	{
		$hostname= 'localhost';
		$username='root';
		$password='';
		$db_name='cashlessdb';

		$GOOGLE_API_KEY='AIzaSyDY_PPB2j8Rj8Pk6jzv1WqAQ2TaAYfRpzg';
		$this->connect = new mysqli($hostname,$username,$password,$db_name) or die('DB Connection failed');
		$this->firebaseApi = $GOOGLE_API_KEY;
		
	}
	
	public function get_connection()
	{
		
		return $this->connect;
		
	}	
	public function get_firebase_api()
	{
		
		return $this->firebaseApi;
	}
	
	
}
	/*
			$stmt= $this->connection->prepare();
			$stmt->bind_param();
			$stmt->execute();
			$result = $stmt->get_result();
			while($row=$result->fetch_assoc())
				{
					$rows[] = $row;

				}
			
			
								$stmt->store_result();
 $stmt->num_rows;
 
 
			$stmt->close();
				$this->connection->close();		
				
				
				
					date_default_timezone_set('Asia/Bangkok');
				$datetime = date('Y-m-d H:i:s',time());
			
	*/
				
				

				?>