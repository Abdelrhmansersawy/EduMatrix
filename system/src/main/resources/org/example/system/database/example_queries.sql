-- Create a new teacher
INSERT INTO USER (userSerialNumber, firstName, lastName, phoneNumber, gmail, password, birthOfDate, role) 
VALUES ('T001', 'John', 'Doe', '1234567890', 'john.doe@gmail.com', 'securePassword123', '1980-05-10', 'TEACHER');
INSERT INTO TEACHER (userSerialNumber, departmentNumber) 
VALUES ('T001', 'D001');

-- Create a new student
INSERT INTO USER (userSerialNumber, firstName, lastName, phoneNumber, gmail, password, birthOfDate, role) 
VALUES ('S001', 'Jane', 'Smith', '0987654321', 'jane.smith@gmail.com', 'securePassword456', '2002-09-15', 'STUDENT');
INSERT INTO STUDENT (userSerialNumber, departmentNumber, schoolYear, GPA, academicStatus, isScholarship) 
VALUES ('S001', 'D001', 1, 3.50, 'ACTIVE', TRUE);

-- Create a new course
INSERT INTO COURSE (courseCode, name, description, departmentNumber, teacherSerialNumber, isActive, maxCapacity, semester, academicYear, courseType) 
VALUES ('C001', 'Introduction to Java', 'Basic Java programming course', 'D001', 'T001', TRUE, 30, 'FALL', 2024, 'MANDATORY');

-- Create a new department
INSERT INTO DEPARTMENT (departmentNumber, name, description, location) 
VALUES ('D001', 'Computer Science', 'Department of Computer Science and Engineering', 'Building A');

-- Set grade for a specific student in a specific course
UPDATE STUDENT_COURSE 
SET grade = 85.50 
WHERE userSerialNumber = 'S001' AND courseCode = 'C001' AND semester = 'FALL' AND academicYear = 2024;

-- Withdraw a specific course
UPDATE STUDENT_COURSE 
SET withdrawalDate = '2024-12-01' 
WHERE userSerialNumber = 'S001' AND courseCode = 'C001' AND semester = 'FALL' AND academicYear = 2024;

-- Enroll a student in a course
INSERT INTO STUDENT_COURSE (userSerialNumber, courseCode, enrollmentYear, semester, academicYear, attendanceRate) 
VALUES ('S001', 'C001', '2024-09-01', 'FALL', 2024, 100.00);

-- Deactivate a course
UPDATE COURSE 
SET isActive = FALSE 
WHERE courseCode = 'C001';

-- Update a teacher's department
UPDATE TEACHER 
SET departmentNumber = 'D002' 
WHERE userSerialNumber = 'T001';

-- Promote a student to the next school year
UPDATE STUDENT 
SET schoolYear = schoolYear + 1 
WHERE userSerialNumber = 'S001' AND schoolYear < 4;

-- Retrieve all active courses in a department
SELECT * FROM COURSE 
WHERE departmentNumber = 'D001' AND isActive = TRUE;

-- Retrieve all students in a specific department
SELECT S.userSerialNumber, U.firstName, U.lastName, S.schoolYear, S.GPA 
FROM STUDENT S 
JOIN USER U ON S.userSerialNumber = U.userSerialNumber 
WHERE S.departmentNumber = 'D001';

-- Retrieve the GPA of a specific student
SELECT S.GPA 
FROM STUDENT S 
WHERE S.userSerialNumber = 'S001';

-- Retrieve all courses taught by a specific teacher
SELECT C.courseCode, C.name, C.semester, C.academicYear 
FROM COURSE C 
WHERE C.teacherSerialNumber = 'T001';

-- Retrieve students enrolled in a specific course
SELECT SC.userSerialNumber, U.firstName, U.lastName, SC.grade 
FROM STUDENT_COURSE SC 
JOIN USER U ON SC.userSerialNumber = U.userSerialNumber 
WHERE SC.courseCode = 'C001' AND SC.semester = 'FALL' AND SC.academicYear = 2024;

-- Retrieve the attendance rate of students in a specific course
SELECT SC.userSerialNumber, U.firstName, U.lastName, SC.attendanceRate 
FROM STUDENT_COURSE SC 
JOIN USER U ON SC.userSerialNumber = U.userSerialNumber 
WHERE SC.courseCode = 'C001' AND SC.semester = 'FALL' AND SC.academicYear = 2024;

-- Retrieve active courses with remaining capacity
SELECT C.courseCode, C.name, C.maxCapacity - COUNT(SC.userSerialNumber) AS remainingCapacity 
FROM COURSE C 
LEFT JOIN STUDENT_COURSE SC ON C.courseCode = SC.courseCode AND C.semester = SC.semester AND C.academicYear = SC.academicYear 
WHERE C.isActive = TRUE 
GROUP BY C.courseCode, C.name, C.maxCapacity 
HAVING remainingCapacity > 0;
