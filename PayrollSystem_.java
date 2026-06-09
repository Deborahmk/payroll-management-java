/*
 * Employee Payroll Management System
 * Author: Deborah Musuamba
 * GitHub: github.com/Deborahmk
 * Description: A console-based payroll system using OOP concepts:
 *              inheritance, polymorphism, abstract classes, ArrayList,
 *              and file I/O.
 */

import java.util.*;
import java.io.*;

// ─── Abstract Employee Class ──────────────────────────────────
abstract class Employee {
    private String employeeId;
    private String name;
    private String department;
    private String email;

    public Employee(String id, String name, String dept, String email) {
        this.employeeId = id;
        this.name = name;
        this.department = dept;
        this.email = email;
    }

    // Getters
    public String getId()         { return employeeId; }
    public String getName()       { return name; }
    public String getDepartment() { return department; }
    public String getEmail()      { return email; }

    // Abstract method — each employee type calculates pay differently
    public abstract double calculateGrossPay();
    public abstract String getEmployeeType();

    // Common tax calculation (25% flat rate for simplicity)
    public double calculateTax() {
        return calculateGrossPay() * 0.25;
    }

    public double calculateNetPay() {
        return calculateGrossPay() - calculateTax();
    }

    public void printPayStub() {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║            PAYROLL STATEMENT             ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.printf("  ║  Employee ID : %-27s║%n", employeeId);
        System.out.printf("  ║  Name        : %-27s║%n", name);
        System.out.printf("  ║  Department  : %-27s║%n", department);
        System.out.printf("  ║  Type        : %-27s║%n", getEmployeeType());
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.printf("  ║  Gross Pay   : $%-26.2f║%n", calculateGrossPay());
        System.out.printf("  ║  Tax (25%%)   : $%-26.2f║%n", calculateTax());
        System.out.printf("  ║  Net Pay     : $%-26.2f║%n", calculateNetPay());
        System.out.println("  ╚══════════════════════════════════════════╝");
    }
}

// ─── Full-Time Employee ───────────────────────────────────────
class FullTimeEmployee extends Employee {
    private double annualSalary;

    public FullTimeEmployee(String id, String name, String dept,
                             String email, double salary) {
        super(id, name, dept, email);
        this.annualSalary = salary;
    }

    @Override
    public double calculateGrossPay() {
        return annualSalary / 12; // Monthly pay
    }

    @Override
    public String getEmployeeType() { return "Full-Time (Salaried)"; }

    public double getAnnualSalary() { return annualSalary; }
}

// ─── Part-Time Employee ───────────────────────────────────────
class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    public PartTimeEmployee(String id, String name, String dept,
                             String email, double rate, int hours) {
        super(id, name, dept, email);
        this.hourlyRate = rate;
        this.hoursWorked = hours;
    }

    @Override
    public double calculateGrossPay() {
        // Overtime pay (1.5x) for hours over 40
        if (hoursWorked > 40) {
            return (40 * hourlyRate) + ((hoursWorked - 40) * hourlyRate * 1.5);
        }
        return hourlyRate * hoursWorked;
    }

    @Override
    public String getEmployeeType() { return "Part-Time (Hourly)"; }
}

// ─── Contractor ───────────────────────────────────────────────
class Contractor extends Employee {
    private double contractAmount;
    private String projectName;

    public Contractor(String id, String name, String dept,
                       String email, double amount, String project) {
        super(id, name, dept, email);
        this.contractAmount = amount;
        this.projectName = project;
    }

    @Override
    public double calculateGrossPay() { return contractAmount; }

    @Override
    public String getEmployeeType() { return "Contractor - " + projectName; }

    // Contractors pay higher tax (30%)
    @Override
    public double calculateTax() { return contractAmount * 0.30; }
}

// ─── Payroll System ───────────────────────────────────────────
class PayrollSystem {
    private ArrayList<Employee> employees;

    public PayrollSystem() {
        employees = new ArrayList<>();
        loadSampleData();
    }

    private void loadSampleData() {
        employees.add(new FullTimeEmployee("EMP001", "Deborah Musuamba",
                "Technology", "d.musuamba@company.com", 85000));
        employees.add(new FullTimeEmployee("EMP002", "James Wilson",
                "Finance", "j.wilson@company.com", 72000));
        employees.add(new FullTimeEmployee("EMP003", "Maria Garcia",
                "Healthcare IT", "m.garcia@company.com", 95000));
        employees.add(new PartTimeEmployee("EMP004", "John Smith",
                "Customer Support", "j.smith@company.com", 22.50, 32));
        employees.add(new PartTimeEmployee("EMP005", "Ashley Brown",
                "Data Entry", "a.brown@company.com", 18.00, 45));
        employees.add(new Contractor("CON001", "Robert Lee",
                "Technology", "r.lee@company.com", 12000, "AI Dashboard"));
        employees.add(new Contractor("CON002", "Sarah Taylor",
                "Technology", "s.taylor@company.com", 8500, "Database Migration"));
    }

    public void addEmployee(Employee e) { employees.add(e); }

    public Employee findEmployee(String id) {
        for (Employee e : employees) {
            if (e.getId().equalsIgnoreCase(id)) return e;
        }
        return null;
    }

    public void printAllPayStubs() {
        System.out.println("\n  Processing payroll for all employees...\n");
        for (Employee e : employees) {
            e.printPayStub();
        }
    }

    public void printPayrollSummary() {
        double totalGross = 0, totalTax = 0, totalNet = 0;
        int fullTime = 0, partTime = 0, contractors = 0;

        for (Employee e : employees) {
            totalGross += e.calculateGrossPay();
            totalTax   += e.calculateTax();
            totalNet   += e.calculateNetPay();
            if (e instanceof FullTimeEmployee) fullTime++;
            else if (e instanceof PartTimeEmployee) partTime++;
            else contractors++;
        }

        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║         PAYROLL SUMMARY REPORT           ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.printf("  ║  Total Employees  : %-21d║%n", employees.size());
        System.out.printf("  ║  Full-Time        : %-21d║%n", fullTime);
        System.out.printf("  ║  Part-Time        : %-21d║%n", partTime);
        System.out.printf("  ║  Contractors      : %-21d║%n", contractors);
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.printf("  ║  Total Gross Pay  : $%-20.2f║%n", totalGross);
        System.out.printf("  ║  Total Tax        : $%-20.2f║%n", totalTax);
        System.out.printf("  ║  Total Net Pay    : $%-20.2f║%n", totalNet);
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    public void saveReportToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("payroll_report.txt"))) {
            writer.println("PAYROLL REPORT");
            writer.println("Author: Deborah Musuamba | github.com/Deborahmk");
            writer.println("Generated: " + new Date());
            writer.println("=".repeat(50));
            for (Employee e : employees) {
                writer.printf("%-10s | %-25s | %-20s | Gross: $%10.2f | Net: $%10.2f%n",
                    e.getId(), e.getName(), e.getDepartment(),
                    e.calculateGrossPay(), e.calculateNetPay());
            }
            System.out.println("\n  ✅ Report saved to payroll_report.txt");
        } catch (IOException ex) {
            System.out.println("  ❌ Error saving report: " + ex.getMessage());
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║    EMPLOYEE PAYROLL MANAGEMENT SYSTEM    ║");
        System.out.println("  ║    Author: Deborah Musuamba              ║");
        System.out.println("  ║    github.com/Deborahmk                  ║");
        System.out.println("  ╚══════════════════════════════════════════╝");

        int choice;
        do {
            System.out.println("\n  MAIN MENU");
            System.out.println("  1. View All Pay Stubs");
            System.out.println("  2. View Payroll Summary");
            System.out.println("  3. Look Up Employee");
            System.out.println("  4. Save Report to File");
            System.out.println("  5. Exit");
            System.out.print("\n  Enter choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1: printAllPayStubs(); break;
                case 2: printPayrollSummary(); break;
                case 3:
                    System.out.print("  Enter Employee ID: ");
                    String id = scanner.next();
                    Employee emp = findEmployee(id);
                    if (emp != null) emp.printPayStub();
                    else System.out.println("  ❌ Employee not found.");
                    break;
                case 4: saveReportToFile(); break;
                case 5:
                    System.out.println("\n  Goodbye! Payroll system closed.");
                    break;
                default:
                    System.out.println("  ❌ Invalid option.");
            }
        } while (choice != 5);
        scanner.close();
    }
}

// ─── Main ─────────────────────────────────────────────────────
public class PayrollMain {
    public static void main(String[] args) {
        new PayrollSystem().run(); // PayrollSystem is the inner class
    }
}
