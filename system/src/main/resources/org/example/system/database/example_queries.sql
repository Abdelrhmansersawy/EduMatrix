-- First create department
INSERT INTO DEPARTMENT (departmentNumber, name, description, location)
VALUES ('D001', 'Computer Science', 'Department of Computer Science and Engineering', 'Building A');

-- Create teacher user and record
INSERT INTO USER (userSerialNumber, firstName, lastName, phoneNumber, gmail, password, birthOfDate, role)
VALUES ('T001', 'John', 'Doe', '1234567890', 'john.doe@gmail.com', 'securePassword123', '1980-05-10', 'TEACHER');
INSERT INTO TEACHER (userSerialNumber, departmentNumber)
VALUES ('T001', 'D001');

-- Create student user and record
INSERT INTO USER (userSerialNumber, firstName, lastName, phoneNumber, gmail, password, birthOfDate, role)
VALUES ('S001', 'Jane', 'Smith', '0987654321', 'jane.smith@gmail.com', 'securePassword456', '2002-09-15', 'STUDENT');
INSERT INTO STUDENT (userSerialNumber, departmentNumber, schoolYear, GPA, academicStatus, isScholarship)
VALUES ('S001', 'D001', 1, 3.50, 'ACTIVE', TRUE);

-- Create course
INSERT INTO COURSE (courseCode, name, description, departmentNumber, teacherSerialNumber, isActive, maxCapacity, semester, academicYear, courseType)
VALUES ('C001', 'Introduction to Java', 'Basic Java programming course', 'D001', 'T001', TRUE, 30, 'FALL', 2024, 'MANDATORY');

-- Enroll student in course
INSERT INTO STUDENT_COURSE (userSerialNumber, courseCode, enrollmentYear, semester, academicYear, attendanceRate)
VALUES ('S001', 'C001', CURDATE(), 'FALL', 2024, 100.00);

-- Fixed queries with proper subqueries and joins
UPDATE STUDENT_COURSE
SET grade = 85.50
WHERE userSerialNumber = 'S001'
  AND courseCode = 'C001'
  AND semester = (SELECT semester FROM COURSE WHERE courseCode = 'C001')
  AND academicYear = (SELECT academicYear FROM COURSE WHERE courseCode = 'C001');

SELECT C.courseCode, C.name,
       (C.maxCapacity - COALESCE(COUNT(DISTINCT SC.userSerialNumber), 0)) AS remainingCapacity
FROM COURSE C
         LEFT JOIN STUDENT_COURSE SC ON C.courseCode = SC.courseCode
    AND C.semester = SC.semester
    AND C.academicYear = SC.academicYear
WHERE C.isActive = TRUE
GROUP BY C.courseCode, C.name, C.maxCapacity
HAVING remainingCapacity > 0;

SELECT SC.userSerialNumber, U.firstName, U.lastName,
       COALESCE(SC.grade, 0) as grade,
       COALESCE(SC.attendanceRate, 0) as attendanceRate
FROM STUDENT_COURSE SC
         INNER JOIN USER U ON SC.userSerialNumber = U.userSerialNumber
WHERE SC.courseCode = 'C001'
  AND SC.withdrawalDate IS NULL;