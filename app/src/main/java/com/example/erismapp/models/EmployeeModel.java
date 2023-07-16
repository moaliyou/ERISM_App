package com.example.erismapp.models;

import androidx.annotation.NonNull;

public class EmployeeModel {

    private final int employeeId;
    private final String firstName;
    private final String lastName;
    private final String jobTitle;
    private final String dateOfBirth;
    private final String hireDate;
    private final double salary;

    public EmployeeModel(
            int employeeId,
            String firstName,
            String lastName,
            String dateOfBirth,
            String jobTitle,
            double salary,
            String hireDate
    ) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return String.join(" ", getFirstName(), getLastName());
    }

    public String getLastName() {
        return lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public double getSalary() {
        return salary;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getHireDate() {
        return hireDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "EmployeeModel{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", salary=" + salary +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", hireDate='" + hireDate + '\'' +
                '}';
    }
}
