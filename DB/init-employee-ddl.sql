USE esun;
CREATE TABLE `employee` (
  `EMP_ID` varchar(10) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `FLOOR_SEAT_SEQ` int DEFAULT NULL,
  PRIMARY KEY (`EMP_ID`),
  KEY `employee_seatingchart_FK` (`FLOOR_SEAT_SEQ`),
  CONSTRAINT `employee_seatingchart_FK` FOREIGN KEY (`FLOOR_SEAT_SEQ`) REFERENCES `seatingchart` (`FLOOR_SEAT_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
