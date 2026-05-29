package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;
import pjatk.mas.clinicregistrationsystem.model.enums.Shift;
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

    private Manager(String firstName, String lastName, LocalDate dateOfBirth,
                   Address address, String phoneNumber, String emailAddress,
                   String jobTitle, float hourlyRate, String bankAccount,
                   String login, String password, String documentNumber,
                   ContractType contractType,
                   SeniorityLevel seniorityLevel) {
        super(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress,
                jobTitle, hourlyRate, bankAccount, login, password, documentNumber, contractType);
        if (seniorityLevel == null) throw new IllegalArgumentException("Seniority level cannot be null");
        this.seniorityLevel = seniorityLevel;
    }

    public static Manager createWithFullTime(String firstName, String lastName, LocalDate dateOfBirth,
                                             Address address, String phoneNumber, String emailAddress,
                                             String jobTitle, float hourlyRate, String bankAccount,
                                             String login, String password, String documentNumber,
                                             SeniorityLevel seniorityLevel,
                                             float overtimeRate) {
        return new Manager(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress,
                jobTitle, hourlyRate, bankAccount, login, password, documentNumber,
                FullTime.create(overtimeRate), seniorityLevel);
    }

    public static Manager createWithPartTime(String firstName, String lastName, LocalDate dateOfBirth,
                                             Address address, String phoneNumber, String emailAddress,
                                             String jobTitle, float hourlyRate, String bankAccount,
                                             String login, String password, String documentNumber,
                                             SeniorityLevel seniorityLevel,
                                             Shift shift, int hoursInContract) {
        return new Manager(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress,
                jobTitle, hourlyRate, bankAccount, login, password, documentNumber,
                PartTime.create(shift, hoursInContract), seniorityLevel);
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
