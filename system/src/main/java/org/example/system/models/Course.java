    package org.example.system.models;

    import org.example.system.enums.CourseType;
    import org.example.system.enums.Semester;

    import java.sql.Connection;
    import java.time.LocalDateTime;

    public class Course {
        private String courseCode;
        private String name;
        private String description;
        private String departmentNumber;
        private String teacherSerialNumber;
        private boolean isActive;
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
    }