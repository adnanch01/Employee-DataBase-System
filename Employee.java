public class Employee {
    private int empId;
    private String firstName;
    private String lastName;
    private String ssn;
    private String jobTitle;
    private String division;
    private String email;
    private String phoneNumber;
    private String hireDate;
    private double salary;

    // Constructor
    public Employee(int empId, String firstName, String lastName, String ssn, String jobTitle, String division,
                    String email, String phoneNumber, String hireDate, double salary) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.jobTitle = jobTitle;
        this.division = division;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    // Getters and Setters
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    @Override
    public String toString() {
        return String.format("Employee [ID=%d, Name=%s %s, SSN=%s, Job Title=%s, Division=%s, Email=%s, Phone=%s, Hire Date=%s, Salary=%.2f]",
                empId, firstName, lastName, ssn, jobTitle, division, email, phoneNumber, hireDate, salary);
    }
}
