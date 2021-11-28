-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 28, 2021 at 08:01 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 7.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `plt`
--

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE `menu` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `harga` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`id`, `nama`, `harga`) VALUES
(1, 'Nasi Goreng', 15000),
(2, 'Mie Goreng', 10000),
(3, 'Mie Rebus', 12000),
(4, 'Es Teh Manis', 8000),
(5, 'Es Jeruk', 7000),
(6, 'Air Putih', 5000);

-- --------------------------------------------------------

--
-- Table structure for table `penjualan`
--

CREATE TABLE `penjualan` (
  `id` int(11) NOT NULL,
  `id_transaksi` int(11) NOT NULL,
  `menu` varchar(255) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `harga` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `penjualan`
--

INSERT INTO `penjualan` (`id`, `id_transaksi`, `menu`, `jumlah`, `harga`) VALUES
(1, 202111281, 'Mie Goreng', 3, 30000),
(2, 202111281, 'Nasi Goreng', 2, 30000),
(3, 202111281, 'Air Putih', 6, 30000),
(4, 202111283, 'Mie Rebus', 3, 36000),
(5, 202111283, 'Es Teh Manis', 3, 24000),
(6, 202111285, 'Mie Goreng', 2, 20000),
(7, 202111285, 'Es Jeruk', 3, 21000),
(8, 202111291, 'Mie Goreng', 2, 20000),
(9, 202111291, 'Es Jeruk', 2, 14000),
(10, 202111292, 'Mie Rebus', 3, 36000),
(11, 202111292, 'Nasi Goreng', 2, 30000),
(12, 202111292, 'Air Putih', 2, 10000),
(13, 202111292, 'Es Jeruk', 3, 21000),
(14, 202111293, 'Es Teh Manis', 3, 24000),
(15, 202111293, 'Mie Goreng', 2, 20000);

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id` int(11) NOT NULL,
  `pemesan` varchar(255) NOT NULL,
  `antrian` int(11) NOT NULL,
  `total` double NOT NULL,
  `status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id`, `pemesan`, `antrian`, `total`, `status`) VALUES
(202111281, 'Zulma', 1, 90000, 'Selesai'),
(202111283, 'Azhar', 3, 60000, 'Selesai'),
(202111285, 'Rizki', 5, 41000, 'Belum Selesai'),
(202111291, 'Nadiya', 1, 34000, 'Belum Selesai'),
(202111292, 'Amanda', 2, 97000, 'Belum Selesai'),
(202111293, 'Rizkania', 3, 44000, 'Selesai');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `penjualan`
--
ALTER TABLE `penjualan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `penjualan`
--
ALTER TABLE `penjualan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=202111294;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
