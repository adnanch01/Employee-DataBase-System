import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        try {
            String url = "mysql user url";
            String user = "root";
            String password = "mysql password";

            EmployeeManager empManager = new EmployeeManager(url, user, password);
            ReportGenerator reportGenerator = new ReportGenerator(empManager.getConnection());

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n1. Add Employee");
                System.out.println("2. Update Employee");
                System.out.println("3. Search Employee");
                System.out.println("4. Update Salary Range");
                System.out.println("5. Delete Employee");
                System.out.println("6. Generate Report (Job Title)");
                System.out.println("7. Generate Report (Division)");
                System.out.println("8. Generate Full Employee Report");
                System.out.println("9. Exit");

                int choice = getValidatedIntInput(scanner, "Enter your choice: ");
                switch (choice) {
                    case 1 -> addEmployee(scanner, empManager);
                    case 2 -> updateEmployee(scanner, empManager);
                    case 3 -> searchEmployee(scanner, empManager);
                    case 4 -> updateSalaryRange(scanner, empManager);
                    case 5 -> deleteEmployee(scanner, empManager);
                    case 6 -> reportGenerator.generateMonthlyPayByJobTitle();
                    case 7 -> reportGenerator.generateMonthlyPayByDivision();
                    case 8 -> reportGenerator.generateFullEmployeeReport();
                    case 9 -> {
                        empManager.close();
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add Employee logic
    private static void addEmployee(Scanner scanner, EmployeeManager empManager) {
        System.out.print("Enter First Name: ");
        String firstName = scanner.next();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.next();
        System.out.print("Enter SSN: ");
        String ssn = scanner.next();
        System.out.print("Enter Job Title: ");
        String jobTitle = scanner.next();
        System.out.print("Enter Division: ");
        String division = scanner.next();
        System.out.print("Enter Email: ");
        String email = scanner.next();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.next();
        System.out.print("Enter Hire Date (YYYY-MM-DD): ");
        String hireDate = scanner.next();
        double salary = getValidatedDoubleInput(scanner, "Enter Salary: ");

        Employee emp = new Employee(0, firstName, lastName, ssn, jobTitle, division, email, phoneNumber, hireDate, salary);
        try {
            if (empManager.addEmployee(emp)) {
                System.out.println("Employee added successfully!");
            } else {
                System.out.println("Failed to add employee.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
        }
    }

    // Update Employee logic
    private static void updateEmployee(Scanner scanner, EmployeeManager empManager) {
        int empId = getValidatedIntInput(scanner, "Enter Employee ID to Update: ");
        System.out.print("Enter Column to Update (e.g., salary, job_title): ");
        String column = scanner.next();
        System.out.print("Enter New Value: ");
        String value = scanner.next();

        try {
            if (empManager.updateEmployeeData(empId, column, value)) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("Failed to update employee.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
        }
    }

    // Search Employee logic
    private static void searchEmployee(Scanner scanner, EmployeeManager empManager) {
        System.out.println("Search by:");
        System.out.println("1. Employee ID");
        System.out.println("2. SSN");
        System.out.println("3. Name");
        int searchChoice = getValidatedIntInput(scanner, "Enter your choice: ");

        try {
            switch (searchChoice) {
                case 1 -> {
                    int empId = getValidatedIntInput(scanner, "Enter Employee ID: ");
                    Employee emp = empManager.searchById(empId);
                    printEmployee(emp);
                }
                case 2 -> {
                    System.out.print("Enter SSN: ");
                    String ssn = scanner.next();
                    Employee emp = empManager.searchBySsn(ssn);
                    printEmployee(emp);
                }
                case 3 -> {
                    System.out.print("Enter Name: ");
                    String name = scanner.next();
                    Employee emp = empManager.searchByName(name);
                    printEmployee(emp);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } catch (SQLException e) {
            System.err.println("Error searching employee: " + e.getMessage());
        }
    }

    // Delete Employee logic
    private static void deleteEmployee(Scanner scanner, EmployeeManager empManager) {
        System.out.println("Delete by:");
        System.out.println("1. Employee ID");
        System.out.println("2. Name");
        int deleteChoice = getValidatedIntInput(scanner, "Enter your choice: ");

        try {
            switch (deleteChoice) {
                case 1 -> {
                    int empId = getValidatedIntInput(scanner, "Enter Employee ID to Delete: ");
                    if (empManager.deleteEmployeeById(empId)) {
                        System.out.println("Employee deleted successfully!");
                    } else {
                        System.out.println("Failed to delete employee. Employee ID not found.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter Name to Delete: ");
                    String name = scanner.next();
                    if (empManager.deleteEmployeeByName(name)) {
                        System.out.println("Employee(s) deleted successfully!");
                    } else {
                        System.out.println("Failed to delete employee(s). Name not found.");
                    }
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }

    // Update Salary Range logic
    private static void updateSalaryRange(Scanner scanner, EmployeeManager empManager) {
        double percentage = getValidatedDoubleInput(scanner, "Enter Percentage Increase: ");
        double minSalary = getValidatedDoubleInput(scanner, "Enter Minimum Salary: ");
        double maxSalary = getValidatedDoubleInput(scanner, "Enter Maximum Salary: ");

        try {
            if (empManager.updateSalaryForRange(percentage, minSalary, maxSalary)) {
                System.out.println("Salaries updated successfully!");
            } else {
                System.out.println("Failed to update salaries.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating salary range: " + e.getMessage());
        }
    }

    // Print Employee Details
    private static void printEmployee(Employee emp) {
        if (emp != null) {
            System.out.println("Employee Details: " + emp);
        } else {
            System.out.println("Employee not found.");
        }
    }

    // Method to validate integer input
    private static int getValidatedIntInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        }
    }

    // Method to validate double input
    private static double getValidatedDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Clear invalid input
            }
        }
    }
}