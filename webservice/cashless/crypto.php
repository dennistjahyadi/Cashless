<?php
	class Encryption
    {
		
		private $code = "^Aj7";
		private $alphabet = "ghijklmnopqrstuvwxyz";
		
		public function decrypt($textt) {
			
			foreach (str_split($this->alphabet) as $alph) {
				$textt = str_replace($alph," ",$textt);
				
			}
			$aaa = explode(" ",$textt);
			$hexaString = [];
			
			for ($i = 0; $i < count($aaa); $i++) {
				$hexaString[$i] = $aaa[$i];
			}
			$decrypted = "";
			
			try {
				foreach ($hexaString as $hex) {
					$decrypted = $decrypted . "" . $this->convertHexToString($this->divide($hex));
				}
				} catch (Exception $ex) {
				return "0";
			}
			
			return $decrypted;
		}
		
		public function multiply($hexa) {
			$salt = $this->convertStringToHex($this->code);
			$value = hexdec($salt);
			$value2 = hexdec($hexa);
			
			$answer = $value2 * $value;
			
			$hexa = dechex($answer);
			return $hexa;
		}
		
		public function divide($hexa) {
			$salt = $this->convertStringToHex($this->code);
			$value = hexdec($salt);
			$value2 = hexdec($hexa);
			
			$answer =  $value2/$value; 
			
			$hexa = dechex($answer);
			return $hexa ;
		}
		
		public function convertStringToHex($string) {
			$hex='';
			for ($i=0; $i < strlen($string); $i++){
				$hex .= dechex(ord($string[$i]));
			}
			return $hex;
		}
		
		public function convertHexToString($hex) {
			
			$string='';
			for ($i=0; $i < strlen($hex)-1; $i+=2){
				$string .= chr(hexdec($hex[$i].$hex[$i+1]));
			}
			return $string;
		}
		
	}
	

	
	
?>