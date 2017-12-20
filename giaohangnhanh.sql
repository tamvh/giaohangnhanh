-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 12, 2017 at 01:57 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `giaohangnhanh`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `account_id` int(11) NOT NULL,
  `account_name` varchar(64) NOT NULL,
  `password` varchar(64) DEFAULT '123456',
  `account_type` tinyint(3) NOT NULL DEFAULT '0',
  `reset` tinyint(1) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `box`
--

CREATE TABLE `box` (
  `box_id` int(11) NOT NULL,
  `box_label` varchar(64) NOT NULL,
  `box_status` int(11) DEFAULT '0',
  `cabinet_id` int(11) DEFAULT NULL,
  `opencode` varchar(64) DEFAULT '',
  `locktime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `attacheddata` varchar(64) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `box`
--

INSERT INTO `box` (`box_id`, `box_label`, `box_status`, `cabinet_id`, `opencode`, `locktime`, `attacheddata`) VALUES
(1, 'box 1', 0, 1, '12345', '2017-10-12 09:21:36', ''),
(4, 'box 2', 0, 1, '12345', '2017-10-12 09:23:22', '');

-- --------------------------------------------------------

--
-- Table structure for table `cabinet`
--

CREATE TABLE `cabinet` (
  `cabinet_id` int(11) NOT NULL,
  `cabinet_name` varchar(64) NOT NULL,
  `nlat` int(11) NOT NULL DEFAULT '0',
  `nlong` int(11) NOT NULL DEFAULT '0',
  `address` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cabinet`
--

INSERT INTO `cabinet` (`cabinet_id`, `cabinet_name`, `nlat`, `nlong`, `address`) VALUES
(1, 'giao hang nhanh 1', 10, 100, ''),
(4, 'giao hang nhanh 4', 20, 34, '');

-- --------------------------------------------------------

--
-- Table structure for table `history_open_box`
--

CREATE TABLE `history_open_box` (
  `id` int(11) NOT NULL,
  `box_id` int(11) NOT NULL,
  `cabinet_id` int(11) NOT NULL,
  `opencode` varchar(64) NOT NULL,
  `locktime` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `history_open_box`
--

INSERT INTO `history_open_box` (`id`, `box_id`, `cabinet_id`, `opencode`, `locktime`) VALUES
(1, 1, 1, '12345', '2017-10-12 11:02:13'),
(2, 1, 1, '12345', '2017-10-12 11:02:34');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`account_id`),
  ADD UNIQUE KEY `account_name_app_id` (`account_name`);

--
-- Indexes for table `box`
--
ALTER TABLE `box`
  ADD PRIMARY KEY (`box_id`);

--
-- Indexes for table `cabinet`
--
ALTER TABLE `cabinet`
  ADD PRIMARY KEY (`cabinet_id`) USING BTREE,
  ADD UNIQUE KEY `cabinet_name` (`cabinet_name`);

--
-- Indexes for table `history_open_box`
--
ALTER TABLE `history_open_box`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `account_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `box`
--
ALTER TABLE `box`
  MODIFY `box_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `cabinet`
--
ALTER TABLE `cabinet`
  MODIFY `cabinet_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `history_open_box`
--
ALTER TABLE `history_open_box`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
