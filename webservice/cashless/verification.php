<?php
	ini_set('max_execution_time', 7200); 
	require 'phpmailer/PHPMailerAutoload.php';
	
	class Verification
	{
		
		function __construct()
		{
			
		}
		
		public function sendEmailVerificationCode()
		{
			$mail = new PHPMailer;
			//Tell PHPMailer to use SMTP
			$mail->isSMTP();
			//Enable SMTP debugging
			// 0 = off (for production use)
			// 1 = client messages
			// 2 = client and server messages
			$mail->SMTPDebug = 0;
			//Ask for HTML-friendly debug output
			$mail->Debugoutput = 'html';
			//Set the hostname of the mail server
			$mail->Host = 'smtp.gmail.com';
			// use
			// $mail->Host = gethostbyname('smtp.gmail.com');
			// if your network does not support SMTP over IPv6
			//Set the SMTP port number - 587 for authenticated TLS, a.k.a. RFC4409 SMTP submission
			$mail->Port = 587;
			//Set the encryption system to use - ssl (deprecated) or tls
			$mail->SMTPSecure = 'tls';
			//Whether to use SMTP authentication
			$mail->SMTPAuth = true;
			//Username to use for SMTP authentication - use full email address for gmail
			$mail->Username = "";
			//Password to use for SMTP authentication
			$mail->Password = "";
			//Set who the message is to be sent from
			$mail->setFrom('CashlessAdmin@gmail.com', 'Cashless');
			//Set an alternative reply-to address
			//$mail->addReplyTo('Typingtutorhelper@gmail.com', 'Typing Tutor');
			//Set the subject line
			$mail->Subject = "Cashless Verification";
			//Read an HTML message body from an external file, convert referenced images to embedded,
			//convert HTML into a basic plain-text alternative body
			//$mail->msgHTML(file_get_contents('contents.html'), dirname(__FILE__));
			$mail->Body = "pecel lele";
			//Replace the plain text body with one created manually
			$mail->AltBody = "";
			//Attach an image file
			
			
			$mail->addAddress('dennis.tgt@gmail.com', "TypingTutor");
			
			$mail->send();
			
			
			$mail->ClearAllRecipients(); 
			
		}	
		
	}
	
	
?>