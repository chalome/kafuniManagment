-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 21, 2024 at 09:37 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kafunidb`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customerID` int(11) NOT NULL,
  `customerName` varchar(50) NOT NULL,
  `customerAdresse` varchar(255) NOT NULL,
  `customerPhone` varchar(50) NOT NULL,
  `customerDate` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customerID`, `customerName`, `customerAdresse`, `customerPhone`, `customerDate`) VALUES
(1, 'chalome', 'kanyosha', '68894773', 'Jan 26, 2024'),
(2, 'Eric', 'kanyosha', '68894773', 'Jan 26, 2024'),
(3, 'Estella', 'kayanza', '674830392', 'Jan 26, 2024'),
(4, 'Marius', 'kayanza', '674830392', 'Jan 26, 2024'),
(5, 'Chris', 'rumonge', '76895643', 'Jan 26, 2024'),
(6, 'Balos', 'carama', '76895643', 'Jan 26, 2024'),
(7, 'Boris', 'kinama', '67584944', 'Jan 26, 2024'),
(8, 'eliane', 'bururi', '567484930', 'Jan 29, 2024');

-- --------------------------------------------------------

--
-- Table structure for table `expense`
--

CREATE TABLE `expense` (
  `expenseID` int(11) NOT NULL,
  `expenseReason` varchar(255) DEFAULT NULL,
  `expenseAmount` int(11) DEFAULT NULL,
  `expenseWorker` int(11) DEFAULT NULL,
  `expenseDetails` text DEFAULT NULL,
  `expenseDate` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `expense`
--

INSERT INTO `expense` (`expenseID`, `expenseReason`, `expenseAmount`, `expenseWorker`, `expenseDetails`, `expenseDate`) VALUES
(1, 'electricity', 30000, 1, 'buying electricity', 'Jan 27, 2024'),
(2, 'car repairing', 50000, 3, 'we repaired the car The banks cited a need to reduce expenses to offset the cost of credit souring during the pandemic as well as spending to comply with stricter regulation and invest in digital technology.', 'Jan 27, 2024'),
(3, 'cash power', 100000, 1, 'If Oracle’s interest in TikTok is primarily about cloud computing, the deal could come at the expense of Google, which is Oracle’s longtime nemesis and which currently provides cloud services to TikTok.', 'Jan 27, 2024'),
(4, 'food', 20000, 3, 'Still, Walter Hyde hasn’t been able to raise his staff’s pay to $10 an hour this year like he planned, with all the extra expenses of masks and gloves.It will make markets and the capitalist system function better by rewarding positive contributions to well-being and prosperity, not wealth transfers at the expense of others', 'Jan 27, 2024'),
(5, 'Transport', 100000, 3, 'Expense accounts are records of the amount a company spends on day-to-day costs during a given accounting period. These accounts exist for a set period of time - a month, quarter, or year - and then new accounts are created for each new period. For this reason, they\'re considered temporary accounts.', 'Jan 27, 2024'),
(6, 'repair computer', 100000, 1, 'When company funds are spent (a debit), the account increases. When funds are credited from another account into an expense account, the account decreases. The goal of these debits and credits is to finish the bookkeeping period with a balanced account.\n\nExpense accounts appear in the company income statement - also known as the profit and loss (P&L) statement.', 'Jan 27, 2024'),
(7, 'beverage', 25000, 1, 'money used for beverage of the guest offset (an item of expenditure) as an expense against taxable income.\n\"up to $17,500 in capital expenditures can be expensed in the year they were incurred\"offset (an item of expenditure) as an expense against taxable income.\n\"up to $17,500 in capital expenditures can be expensed in the year they were incurred\"', 'Jan 29, 2024'),
(8, 'food', 13000, 4, 'When company funds are spent (a debit), the account increases. When funds are credited from another account into an expense account, the account decreases. The goal of these debits and credits is to finish the bookkeeping period with a balanced account.\n\nExpense accounts appear in the company income statement - also known as the profit and loss (P&L) statement.Expense accounts are records of the amount a company spends on day-to-day costs during a given accounting period. These accounts exist for a set period of time - a month, quarter, or year - and then new accounts are created for each new period. For this reason, they\'re considered temporary accounts.', 'Jan 29, 2024');

-- --------------------------------------------------------

--
-- Table structure for table `history`
--

CREATE TABLE `history` (
  `historyID` int(11) NOT NULL,
  `historyProduct` int(11) NOT NULL,
  `historyCustomer` int(11) NOT NULL,
  `historyProductUnit` int(11) DEFAULT NULL,
  `historyProductPrice` int(11) DEFAULT NULL,
  `historyDate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `history`
--

INSERT INTO `history` (`historyID`, `historyProduct`, `historyCustomer`, `historyProductUnit`, `historyProductPrice`, `historyDate`) VALUES
(1, 6, 6, 10, 23000, '2024-01-26 20:48:30'),
(2, 5, 1, 10, 6000, '2024-01-26 20:49:14'),
(3, 4, 3, 10, 7000, '2024-01-26 20:50:08'),
(4, 3, 2, 20, 4000, '2024-01-26 20:50:49'),
(5, 7, 1, 20, 11000, '2024-01-27 04:26:11'),
(6, 3, 1, 400, 5000, '2024-01-27 04:26:59'),
(7, 4, 5, 290, 7000, '2024-01-27 04:27:41'),
(8, 2, 3, 300, 5000, '2024-01-27 04:28:23'),
(9, 1, 4, 200, 13000, '2024-01-27 04:28:57'),
(10, 5, 6, 90, 7000, '2024-01-27 04:58:26'),
(11, 10, 8, 5, 310000, '2024-01-30 17:11:01'),
(12, 10, 8, 5, 310000, '2024-01-30 17:27:46'),
(13, 5, 5, 10, 60000, '2024-02-08 19:57:38');

-- --------------------------------------------------------

--
-- Table structure for table `income`
--

CREATE TABLE `income` (
  `incomeID` int(11) NOT NULL,
  `incomeSource` varchar(255) NOT NULL,
  `incomeAmount` int(11) NOT NULL,
  `incomeDate` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `income`
--

INSERT INTO `income` (`incomeID`, `incomeSource`, `incomeAmount`, `incomeDate`) VALUES
(1, 'credit from bank', 10000000, 'Jan 28, 2024'),
(2, 'funds from family', 1000000, 'Jan 28, 2024');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `productID` int(11) NOT NULL,
  `productName` varchar(50) NOT NULL,
  `productUnit` int(11) NOT NULL,
  `productPrice` int(11) NOT NULL,
  `productDate` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`productID`, `productName`, `productUnit`, `productPrice`, `productDate`) VALUES
(1, 'SuperGlue', 200, 10000, 'Jan 26, 2024'),
(2, 'SteelWire', 300, 3000, 'Jan 26, 2024'),
(3, 'Soap', 500, 3000, 'Jan 26, 2024'),
(4, 'Scissors', 300, 5000, 'Jan 26, 2024'),
(5, 'PadLock', 100, 5000, 'Jan 26, 2024'),
(6, 'curtain', 40, 20000, 'Jan 26, 2024'),
(7, 'toothpaste', 20, 10000, 'Jan 26, 2024'),
(8, 'dish', 100, 5000, 'Jan 29, 2024'),
(9, 'soap', 500, 1000, 'Jan 29, 2024'),
(10, 'carpet', 200, 300000, 'Jan 29, 2024'),
(11, 'bags', 40, 35000, 'Feb 8, 2024');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userID` int(11) NOT NULL,
  `userName` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userID`, `userName`, `password`) VALUES
(1, 'chalome', 'chalome1999'),
(2, 'abm', 'abm2024'),
(3, 'aime', 'aime2000'),
(4, 'didier', 'didier1995'),
(5, 'Digne', 'digne2000');

-- --------------------------------------------------------

--
-- Table structure for table `worker`
--

CREATE TABLE `worker` (
  `workerID` int(11) NOT NULL,
  `workerFname` varchar(50) NOT NULL,
  `workerLname` varchar(50) NOT NULL,
  `workerAdress` varchar(255) NOT NULL,
  `workerPhone` varchar(50) NOT NULL,
  `workerSalary` int(11) NOT NULL,
  `workerDate` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `worker`
--

INSERT INTO `worker` (`workerID`, `workerFname`, `workerLname`, `workerAdress`, `workerPhone`, `workerSalary`, `workerDate`) VALUES
(1, 'Izompansavye', 'chalome', 'kanyosha', '68894773', 500000, 'Jan 26, 2024'),
(3, 'Dusenge', 'Didier', 'kanyosha', '675859403', 100000, 'Jan 26, 2024'),
(4, 'mugisha', 'jackin', 'ruyigi', '6758504-3', 300000, 'Jan 29, 2024');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customerID`);

--
-- Indexes for table `expense`
--
ALTER TABLE `expense`
  ADD PRIMARY KEY (`expenseID`),
  ADD KEY `expenseWorker` (`expenseWorker`);

--
-- Indexes for table `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`historyID`),
  ADD KEY `historyProduct` (`historyProduct`),
  ADD KEY `historyCustomer` (`historyCustomer`);

--
-- Indexes for table `income`
--
ALTER TABLE `income`
  ADD PRIMARY KEY (`incomeID`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`productID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`);

--
-- Indexes for table `worker`
--
ALTER TABLE `worker`
  ADD PRIMARY KEY (`workerID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customerID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `expense`
--
ALTER TABLE `expense`
  MODIFY `expenseID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `history`
--
ALTER TABLE `history`
  MODIFY `historyID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `income`
--
ALTER TABLE `income`
  MODIFY `incomeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `productID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `worker`
--
ALTER TABLE `worker`
  MODIFY `workerID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `expense`
--
ALTER TABLE `expense`
  ADD CONSTRAINT `expense_ibfk_1` FOREIGN KEY (`expenseWorker`) REFERENCES `worker` (`workerID`);

--
-- Constraints for table `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `history_ibfk_1` FOREIGN KEY (`historyProduct`) REFERENCES `product` (`productID`),
  ADD CONSTRAINT `history_ibfk_2` FOREIGN KEY (`historyCustomer`) REFERENCES `customer` (`customerID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
