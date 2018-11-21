-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 21, 2018 at 07:06 PM
-- Server version: 10.1.28-MariaDB
-- PHP Version: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cashlessdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `bank_account`
--

CREATE TABLE `bank_account` (
  `id` int(11) NOT NULL,
  `fk_users` int(11) NOT NULL,
  `fk_bank` int(11) NOT NULL,
  `atas_nama` varchar(255) NOT NULL,
  `no_rekening` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bank_account`
--

INSERT INTO `bank_account` (`id`, `fk_users`, `fk_bank`, `atas_nama`, `no_rekening`) VALUES
(1, 3, 1, 'Dodol', '21421'),
(2, 2, 1, 'denden', '085256888'),
(3, 2, 2, 'Denden', '04854554');

-- --------------------------------------------------------

--
-- Table structure for table `bank_list`
--

CREATE TABLE `bank_list` (
  `id` int(11) NOT NULL,
  `bank_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bank_list`
--

INSERT INTO `bank_list` (`id`, `bank_name`) VALUES
(1, 'Bank Central Asia (BCA)'),
(2, 'Bank Mandiri'),
(3, 'Bank Negara Indonesia (BNI)'),
(4, 'Bank Rakyat Indonesia (BRI)');

-- --------------------------------------------------------

--
-- Table structure for table `business_list`
--

CREATE TABLE `business_list` (
  `id` int(11) NOT NULL,
  `fk_users` int(11) NOT NULL,
  `business_name` varchar(255) NOT NULL,
  `business_desc` varchar(255) NOT NULL,
  `amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `business_list`
--

INSERT INTO `business_list` (`id`, `fk_users`, `business_name`, `business_desc`, `amount`) VALUES
(1, 3, 'business 1', 'martabak', 10000),
(2, 2, 'Kantin Test', 'sate rendang', 15000);

-- --------------------------------------------------------

--
-- Table structure for table `history_transaction`
--

CREATE TABLE `history_transaction` (
  `id` varchar(255) NOT NULL,
  `fk_userId_sender` int(11) DEFAULT NULL,
  `fk_userId_receiver` int(11) DEFAULT NULL,
  `amount` int(11) NOT NULL,
  `fee` int(11) DEFAULT NULL,
  `totalAmount` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `_datetime` datetime NOT NULL,
  `fk_bank` int(11) DEFAULT NULL,
  `method` enum('transaction','withdraw','deposit') NOT NULL,
  `status` enum('Success','Pending','Rejected') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `history_transaction`
--

INSERT INTO `history_transaction` (`id`, `fk_userId_sender`, `fk_userId_receiver`, `amount`, `fee`, `totalAmount`, `description`, `_datetime`, `fk_bank`, `method`, `status`) VALUES
('1a5g0q7s8v6a2v5c2z6s', 2, 3, 100, 0, 100, '', '2017-10-13 04:42:06', NULL, 'transaction', 'Success'),
('1a5m0c8f8y5x4z4w0d4g', 3, NULL, 200, 0, 200, NULL, '2017-10-24 16:13:24', 1, 'withdraw', 'Pending'),
('1b5x0k9r4d6k6s7p5z4h', 2, 3, 10000, 0, 10000, 'martabak', '2017-10-31 23:19:14', NULL, 'transaction', 'Success'),
('1c5e0c7s9g5d6b1a9j5b', 2, 3, 50, 0, 50, '', '2017-10-14 06:43:15', NULL, 'transaction', 'Success'),
('1d5o0j7q8d6m2x2r1p2v', 2, 3, 500, 0, 500, '', '2017-10-13 04:36:52', NULL, 'transaction', 'Success'),
('1e5b0s8e8s5h4b2e5j9o', 3, NULL, 600, 0, 600, NULL, '2017-10-24 16:10:59', 1, 'withdraw', 'Pending'),
('1e5o0x7u8m6b2c2x8j4r', 2, 3, 100, 0, 100, '', '2017-10-13 04:38:04', NULL, 'transaction', 'Success'),
('1f5b0w7b8m6j3z2d7s4o', 2, 3, 200, 0, 200, '', '2017-10-13 04:54:34', NULL, 'transaction', 'Success'),
('1f5n0e7a8z6m2t7v3e9q', 2, 3, 100, 0, 100, '', '2017-10-13 04:45:39', NULL, 'transaction', 'Success'),
('1g5j0j8a8f5q4m4g1y4j', 3, NULL, 200, 0, 200, NULL, '2017-10-24 16:13:34', 1, 'withdraw', 'Pending'),
('1j5d0o8y9v3y7e0r5j6v', NULL, 3, 12000, 0, 12000, NULL, '2017-10-25 20:10:56', NULL, 'deposit', 'Pending'),
('1j5m0d7z7j8q2m1e2p9p', 2, 3, 3000, 0, 3000, 'asd', '2017-10-12 06:22:09', NULL, 'transaction', 'Success'),
('1k5b0g7j8c6x2w0x2w0p', 2, 3, 1000, 0, 1000, '', '2017-10-13 04:33:40', NULL, 'transaction', 'Success'),
('1k5e0c8o9p3o8j4q4j1a', NULL, 2, 12000, 0, 12000, NULL, '2017-10-25 20:34:01', NULL, 'deposit', 'Pending'),
('1k5o0c7g8b6d2q0o5v7a', 2, 3, 500, 0, 500, '', '2017-10-13 04:34:17', NULL, 'transaction', 'Success'),
('1l5i0t7i6q5b0j1d0m3v', 2, 3, 2500, 0, 2500, '', '2017-10-10 17:41:43', NULL, 'transaction', 'Success'),
('1n5r0g7g8y6m3j4u3e5n', 2, 3, 350, 0, 350, '', '2017-10-13 04:57:15', NULL, 'transaction', 'Success'),
('1p5e0s7e8v6p2s5w5a1h', 2, 3, 100, 0, 100, '', '2017-10-13 04:42:31', NULL, 'transaction', 'Success'),
('1r5g0d7b8o6n2p3t0d0r', 2, 3, 100, 0, 100, '', '2017-10-13 04:38:20', NULL, 'transaction', 'Success'),
('1r5h0p7g8m6o2j7h5z0b', 2, 3, 100, 0, 100, '', '2017-10-13 04:45:50', NULL, 'transaction', 'Success'),
('1r5k0y8b9u3f5j7v4y9c', NULL, 3, 10000, 0, 10000, NULL, '2017-10-25 19:49:09', NULL, 'deposit', 'Pending'),
('1r5n0u7j8m6p2w8t0p1t', 2, 3, 200, 0, 200, '', '2017-10-13 04:46:41', NULL, 'transaction', 'Success'),
('1s5b0b8u8a5p4g3q1f2z', 3, NULL, 600, 0, 600, NULL, '2017-10-24 16:11:52', 1, 'withdraw', 'Pending'),
('1s5g0f8f9t3k5g8j3w6s', NULL, 3, 11000, 0, 11000, NULL, '2017-10-25 19:50:36', NULL, 'deposit', 'Pending'),
('1s5k0c7g8a6h3t3v5h0y', 2, 3, 300, 0, 300, '', '2017-10-13 04:55:50', NULL, 'transaction', 'Success'),
('1s5y0p8a8h5f6c8n1g6n', 3, NULL, 100, 0, 100, NULL, '2017-10-24 16:53:36', 1, 'withdraw', 'Pending'),
('1t5u0d8f8s5n4d4a2s8y', 3, NULL, 200, 0, 200, NULL, '2017-10-24 16:13:48', 1, 'withdraw', 'Pending'),
('1u5h0e7n8g6e3s3n6y7y', 2, 3, 200, 0, 200, '', '2017-10-13 04:56:07', NULL, 'transaction', 'Success'),
('1u5j0w8y8j5e4h7s3h4t', 3, NULL, 3000, 0, 3000, NULL, '2017-10-24 16:18:54', 1, 'withdraw', 'Pending'),
('1u5r0g9s4a6y6r6p0q3q', 2, 3, 10000, 0, 10000, 'martabak', '2017-10-31 23:16:43', NULL, 'transaction', 'Success'),
('1v5m0o7j8q6o2d4s7d7w', 2, 3, 100, 0, 100, '', '2017-10-13 04:41:17', NULL, 'transaction', 'Success'),
('1v5q0t7a8k6s1r7j3c0s', 2, 3, 1000, 0, 1000, '', '2017-10-13 04:28:50', NULL, 'transaction', 'Success'),
('1w5g0t8p8e5b6x3q8b4a', 3, NULL, 200, 0, 200, NULL, '2017-10-24 16:46:24', 1, 'withdraw', 'Pending'),
('1w5m0a8n9c3a5q4n3x0k', NULL, 3, 10000, 0, 10000, NULL, '2017-10-25 19:43:50', NULL, 'deposit', 'Pending'),
('1y5f0d7v8k6c2x4d8m3x', 2, 3, 100, 0, 100, '', '2017-10-13 04:41:23', NULL, 'transaction', 'Success'),
('1z5e0v7r8g6g1w9a2q1u', 2, 3, 1000, 0, 1000, '', '2017-10-13 04:32:01', NULL, 'transaction', 'Success'),
('1z5r0r7w7u8p4b6s1q3c', 2, 3, 1500, 0, 1500, '', '2017-10-12 07:03:33', NULL, 'transaction', 'Success');

-- --------------------------------------------------------

--
-- Table structure for table `infoapp`
--

CREATE TABLE `infoapp` (
  `id` int(11) NOT NULL,
  `infodesc` varchar(255) DEFAULT NULL,
  `stat` enum('0','1') DEFAULT NULL,
  `category` enum('maintenance','updateapp') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `infoapp`
--

INSERT INTO `infoapp` (`id`, `infodesc`, `stat`, `category`) VALUES
(1, 'Maintenance..', '0', 'maintenance'),
(2, 'You have to update the app first.', '0', 'updateapp');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `fullname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pass` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone_no` varchar(20) DEFAULT NULL,
  `balance` int(11) DEFAULT '0',
  `verified` enum('0','1') NOT NULL DEFAULT '0',
  `stat` enum('0','1','2','3') NOT NULL,
  `waring_info` varchar(255) DEFAULT NULL,
  `verification_code` varchar(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `fullname`, `email`, `pass`, `address`, `phone_no`, `balance`, `verified`, `stat`, `waring_info`, `verification_code`) VALUES
(2, 'Dennis Asd', 'asd@gmail.com', '7815696ecbf1c96e6894b779456d330e', NULL, NULL, 10000, '1', '0', NULL, '336600'),
(3, 'Jono Frya', 'asd2@gmail.com', 'a67995ad3ec084cb38d32725fd73d9a3', 'jl aaa no 12', NULL, 34500, '1', '0', NULL, '575714');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bank_account`
--
ALTER TABLE `bank_account`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bank_list`
--
ALTER TABLE `bank_list`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `business_list`
--
ALTER TABLE `business_list`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `history_transaction`
--
ALTER TABLE `history_transaction`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `infoapp`
--
ALTER TABLE `infoapp`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bank_account`
--
ALTER TABLE `bank_account`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `bank_list`
--
ALTER TABLE `bank_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `business_list`
--
ALTER TABLE `business_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
