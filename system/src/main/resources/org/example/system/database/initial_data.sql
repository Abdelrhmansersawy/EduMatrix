INSERT INTO DEPARTMENT (departmentNumber, name, description, location)
VALUES ('GEN001', 'General Department', 'Main department for general education courses', 'Main Building, Floor 1');

INSERT INTO USER (userSerialNumber, firstName, lastName, phoneNumber, gmail, password, birthOfDate, role)
VALUES ('ADM001', 'Admin', 'User', '1234567890', 'admin@university.edu',
        '$2a$10$encrypted_password_hash', '1980-01-01', 'ADMIN');

INSERT INTO ADMIN (userSerialNumber)
VALUES ('ADM001');

INSERT INTO USER (userSerialNumber, firstName, lastName, phoneNumber, gmail, password, birthOfDate, role)
VALUES ('TCH001', 'Dean', 'Smith', '1234567891', 'dean@university.edu',
        '$2a$10$encrypted_password_hash', '1975-01-01', 'TEACHER');

INSERT INTO TEACHER (userSerialNumber, departmentNumber)
VALUES ('TCH001', 'GEN001');

INSERT INTO COURSE (courseCode, name, description, departmentNumber, teacherSerialNumber, maxCapacity, semester, academicYear, courseType)
VALUES ('INTRO101', 'Introductory Course', 'Basic introduction to the university program',
        'GEN001', 'TCH001', 30, 'FALL', 2024, 'GENERAL');