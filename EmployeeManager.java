import java.sql.*;

public class EmployeeManager {
    private final Connection connection;

    // Constructor to set up the database connection
    public EmployeeManager(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
        System.out.println("Database connected successfully!");
    }

    // Getter for the connection field
    public Connection getConnection() {
        return connection;
    }

    // Add an Employee
    public boolean addEmployee(Employee emp) throws SQLException {
        String query = "INSERT INTO Employee (first_name, last_name, ssn, job_title, division, email, phone_number, hire_date, salary) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, emp.getFirstName());
            stmt.setString(2, emp.getLastName());
            stmt.setString(3, emp.getSsn());
            stmt.setString(4, emp.getJobTitle());
            stmt.setString(5, emp.getDivision());
            stmt.setString(6, emp.getEmail());
            stmt.setString(7, emp.getPhoneNumber());
            stmt.setString(8, emp.getHireDate());
            stmt.setDouble(9, emp.getSalary());
            return stmt.executeUpdate() > 0;
        }
    }

    // Update Employee Data
    public boolean updateEmployeeData(int empId, String column, Object value) throws SQLException {
        String query = "UPDATE Employee SET " + column + " = ? WHERE emp_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, value);
            stmt.setInt(2, empId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Search Employee by ID
    public Employee searchById(int empId) throws SQLException {
        String query = "SELECT * FROM Employee WHERE emp_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, empId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return constructEmployee(rs);
                }
            }
        }
        return null;
    }

    // Search Employee by SSN
    public Employee searchBySsn(String ssn) throws SQLException {
        if (ssn == null || ssn.isEmpty()) {
            throw new IllegalArgumentException("SSN cannot be null or empty");
        }

        String query = "SELECT * FROM Employee WHERE ssn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, ssn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return constructEmployee(rs);
                }
            }
        }
        return null;
    }

    // Search Employee by Name
    public Employee searchByName(String name) throws SQLException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String query = "SELECT * FROM Employee WHERE first_name LIKE ? OR last_name LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            stmt.setString(2, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return constructEmployee(rs);
                }
            }
        }
        return null;
    }

    // Delete Employee by ID
    public boolean deleteEmployeeById(int empId) throws SQLException {
        String query = "DELETE FROM Employee WHERE emp_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, empId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete Employee by Name
    public boolean deleteEmployeeByName(String name) throws SQLException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String query = "DELETE FROM Employee WHERE first_name = ? OR last_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, name);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update Salary for Employees in a Salary Range
    public boolean updateSalaryForRange(double percentage, double minSalary, double maxSalary) throws SQLException {
        String query = "UPDATE Employee SET salary = salary * ? WHERE salary BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, 1 + percentage / 100);
            stmt.setDouble(2, minSalary);
            stmt.setDouble(3, maxSalary);
            return stmt.executeUpdate() > 0;
        }
    }

    // Close the connection
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    }

    // Helper method to construct an Employee object from a ResultSet
    private Employee constructEmployee(ResultSet rs) throws SQLException {
        return new Employee(
            rs.getInt("emp_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("ssn"),
            rs.getString("job_title"),
            rs.getString("division"),
            rs.getString("email"),
            rs.getString("phone_number"),
            rs.getString("hire_date"),
            rs.getDouble("salary")
        );
    }
}