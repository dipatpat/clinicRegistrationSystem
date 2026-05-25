package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;
import pjatk.mas.clinicregistrationsystem.model.enums.SeniorityLevel;

import java.time.LocalDate;

@Entity
@Table(name = "Manager")
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends Employee {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeniorityLevel seniorityLevel;

    protected Manager() {}

    public Manager(String firstName, String lastName, LocalDate dateOfBirth,
                   Address address, String phoneNumber, String emailAddress,
                   String jobTitle, float hourlyRate, String bankAccount,
                   String login, String password, String documentId,
                   SeniorityLevel seniorityLevel) {
        super(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress,
                jobTitle, hourlyRate, bankAccount, login, password, documentId);
        if (seniorityLevel == null) throw new IllegalArgumentException("Seniority level cannot be null");
        this.seniorityLevel = seniorityLevel;
    }

    @Transient
    public float getBonus() {
        float multiplier = switch (seniorityLevel) {
            case JUNIOR -> 0.02f;
            case MID    -> 0.05f;
            case SENIOR -> 0.10f;
        };
        return getHourlyRate() * multiplier;
    }

    public void addNewEmployee() {}
    public void updateEmployeeDetails() {}
    public void generatePatientsStatisticsByCity() {}
    public void viewPayroll() {}

    public SeniorityLevel getSeniorityLevel() { return seniorityLevel; }

    public void setSeniorityLevel(SeniorityLevel seniorityLevel) {
        if (seniorityLevel == null) throw new IllegalArgumentException("Seniority level cannot be null");
        this.seniorityLevel = seniorityLevel;
    }
}
