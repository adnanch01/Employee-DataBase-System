import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportGenerator {
    private static final Logger logger = Logger.getLogger(ReportGenerator.class.getName());
    private final Connection connection;

    // Constructor to initialize the ReportGenerator with a database connection
    public ReportGenerator(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    // Generate Total Pay by Job Title
    public void generateMonthlyPayByJobTitle() {
        String query = "SELECT job_title, SUM(salary) AS total_pay FROM Employee GROUP BY job_title";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.printf("%-20s | %-10s%n", "Job Title", "Total Pay");
            System.out.println("---------------------|------------");
            while (rs.next()) {
                System.out.printf("%-20s | %-10.2f%n",
                        rs.getString("job_title"),
                        rs.getDouble("total_pay"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while generating report by Job Title: {0}", e.getMessage());
        }
    }

    // Generate Total Pay by Division
    public void generateMonthlyPayByDivision() {
        String query = "SELECT division, SUM(salary) AS total_pay FROM Employee GROUP BY division";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.printf("%-20s | %-10s%n", "Division", "Total Pay");
            System.out.println("---------------------|------------");
            while (rs.next()) {
                System.out.printf("%-20s | %-10.2f%n",
                        rs.getString("division"),
                        rs.getDouble("total_pay"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while generating report by Division: {0}", e.getMessage());
        }
    }

    // Generate Total Pay by Job Title and Division
    public void generateMonthlyPayByJobAndDivision() {
        String query = "SELECT job_title, division, SUM(salary) AS total_pay " +
                       "FROM Employee GROUP BY job_title, division";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.printf("%-20s | %-20s | %-10s%n", "Job Title", "Division", "Total Pay");
            System.out.println("---------------------|---------------------|------------");
            while (rs.next()) {
                System.out.printf("%-20s | %-20s | %-10.2f%n",
                        rs.getString("job_title"),
                        rs.getString("division"),
                        rs.getDouble("total_pay"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while generating report by Job Title and Division: {0}", e.getMessage());
        }
    }

    public void generateFullEmployeeReport() {
        String query = "SELECT * FROM Employee";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            System.out.printf("%-10s | %-15s | %-15s | %-15s | %-15s | %-15s | %-25s | %-15s | %-12s | %-10s%n", 
                              "Emp ID", "First Name", "Last Name", "SSN", "Job Title", "Division", "Email", 
                              "Phone Number", "Hire Date", "Salary");
            System.out.println("----------|-----------------|-----------------|-----------------|-----------------|-----------------|---------------------------|-----------------|-------------|----------");
    
            while (rs.next()) {
                System.out.printf("%-10d | %-15s | %-15s | %-15s | %-15s | %-15s | %-25s | %-15s | %-12s | %.2f%n",
                                  rs.getInt("emp_id"), rs.getString("first_name"), rs.getString("last_name"),
                                  rs.getString("ssn"), rs.getString("job_title"), rs.getString("division"),
                                  rs.getString("email"), rs.getString("phone_number"),
                                  rs.getString("hire_date"), rs.getDouble("salary"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while generating full employee report: {0}", e.getMessage());
        }
    }

    // Close the database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Connection closed successfully.");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error while closing connection: {0}", e.getMessage());
        }
    }
}