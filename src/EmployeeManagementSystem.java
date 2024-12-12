import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Class describing an employee
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String position;
    private int age;
    private double salary;

    public Employee(String name, String position, int age, double salary) {
        this.name = name;
        this.position = position;
        this.age = age;
        this.salary = salary;
    }

    // Getters
    public String getName() { return name; }
    public String getPosition() { return position; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}

public class EmployeeManagementSystem {
    private static final String FILENAME = "employees.dat";

    // Method to add an employee
    public static void addEmployee(Employee employee) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(FILENAME, true)))) {
            out.writeObject(employee);
            System.out.println("Employee successfully added.");
        } catch (IOException ex) {
            System.err.println("Error adding employee: " + ex.getMessage());
        }
    }

    // Method to read all employees
    public static List<Employee> readAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(FILENAME)))) {
            while (true) {
                try {
                    Employee employee = (Employee) in.readObject();
                    employees.add(employee);
                } catch (EOFException e) {
                    break; // End of file
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. Employee list is empty.");
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error reading file: " + ex.getMessage());
        }
        
        return employees;
    }

    // Method to find an employee by name
    public static Employee findEmployeeByName(String name) {
        List<Employee> employees = readAllEmployees();
        for (Employee employee : employees) {
            if (employee.getName().equalsIgnoreCase(name)) {
                return employee;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n--- Employee Management System ---");
            System.out.println("1. Add an employee");
            System.out.println("2. Show all employees");
            System.out.println("3. Search for an employee by name");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter position: ");
                    String position = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    System.out.print("Enter salary: ");
                    double salary = scanner.nextDouble();
                    
                    Employee newEmployee = new Employee(name, position, age, salary);
                    addEmployee(newEmployee);
                    break;
                
                case 2:
                    List<Employee> employees = readAllEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("No employees found.");
                    } else {
                        for (Employee emp : employees) {
                            System.out.println(emp);
                        }
                    }
                    break;
                
                case 3:
                    System.out.print("Enter employee name: ");
                    String searchName = scanner.nextLine();
                    Employee foundEmployee = findEmployeeByName(searchName);
                    
                    if (foundEmployee != null) {
                        System.out.println("Employee found: " + foundEmployee);
                    } else {
                        System.out.println("Employee not found.");
                    }
                    break;
                
                case 4:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    System.exit(0);
                
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}