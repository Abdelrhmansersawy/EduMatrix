    package org.example.system.models;

    import org.example.system.enums.CourseType;
    import org.example.system.enums.Semester;
    import org.example.system.users.Student;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    public class Course {
        public String courseCode;
        private String name;
        private String description;
        private String departmentNumber;
        private String teacherSerialNumber;
        public boolean isActive;
        private int maxCapacity;
        private Semester semester;
        private int academicYear;
        private CourseType courseType;

        protected Connection connection;

        public Course(Connection connection) {
            this.connection = connection;
        }

        public Course(String courseCode, String name, String description, String departmentNumber,
                      String teacherSerialNumber, boolean isActive, int maxCapacity, Semester semester,
                      int academicYear, CourseType courseType) {
            this.courseCode = courseCode;
            this.name = name;
            this.description = description;
            this.departmentNumber = departmentNumber;
            this.teacherSerialNumber = teacherSerialNumber;
            this.isActive = isActive;
            this.maxCapacity = maxCapacity;
            this.semester = semester;
            this.academicYear = academicYear;
            this.courseType = courseType;

            validateCourseData();
        }

        // Getters and Setters
        public String getCourseCode() {
            return courseCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDepartmentNumber() {
            return departmentNumber;
        }

        public void setDepartmentNumber(String departmentNumber) {
            this.departmentNumber = departmentNumber;
        }

        public boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public void setMaxCapacity(int maxCapacity) {
            this.maxCapacity = maxCapacity;
        }

        public Semester getSemester() {
            return semester;
        }

        public void setSemester(Semester semester) {
            this.semester = semester;
        }

        public int getAcademicYear() {
            return academicYear;
        }

        public void setAcademicYear(int academicYear) {
            this.academicYear = academicYear;
        }

        public CourseType getCourseType() {
            return courseType;
        }

        public void setCourseType(CourseType courseType) {
            this.courseType = courseType;
        }


        // Additional helper methods for enum conversions
        public void setSemesterFromString(String semesterStr) {
            try {
                this.semester = Semester.valueOf(semesterStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid semester value: " + semesterStr);
            }
        }

        public void setCourseTypeFromString(String courseTypeStr) {
            try {
                this.courseType = CourseType.valueOf(courseTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid course type value: " + courseTypeStr);
            }
        }

        public String getSemesterAsString() {
            return semester != null ? semester.name() : null;
        }

        public String getCourseTypeAsString() {
            return courseType != null ? courseType.name() : null;
        }
        public String getTeacherSerialNumber() {
            return teacherSerialNumber;
        }

        public void setTeacherSerialNumber(String teacherSerialNumber) {
            this.teacherSerialNumber = teacherSerialNumber;
        }

        private void validateCourseData() {
            if (maxCapacity <= 0) {
                throw new IllegalArgumentException("Max capacity must be greater than 0");
            }
            if (academicYear < 2000) {
                throw new IllegalArgumentException("Academic year must be 2000 or later");
            }
        }
        public int getStudentsCount() {
            String sql = "SELECT COUNT(*) FROM STUDENT_COURSE WHERE courseCode = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, getCourseCode());
                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                // Log the error
                return 0;
            }
            return 0;
        }

        public Optional<List<Student>> getEnrolledStudents(Connection connection) {
            List<Student> enrolledStudents = new ArrayList<>();
            String query = """
                               SELECT s.*, sc.grade, sc.attendanceRate
                               FROM STUDENT s 
                               JOIN STUDENT_COURSE sc ON s.userSerialNumber = sc.userSerialNumber
                               WHERE sc.courseCode = ? AND sc.semester = ? AND sc.academicYear = ?
                           """;

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, this.courseCode);
                stmt.setString(2, this.semester.name());
                stmt.setInt(3, this.academicYear);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Student student = new Student(connection);
                    student.setUserSerialNumber(rs.getString("userSerialNumber"));
                    student.setGrade(rs.getDouble("grade"));
                    enrolledStudents.add(student);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error fetching enrolled students: " + e.getMessage());
            }
            return Optional.of(enrolledStudents);
        }


    }