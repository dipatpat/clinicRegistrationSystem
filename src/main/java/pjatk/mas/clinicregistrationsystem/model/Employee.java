package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Employee")
@PrimaryKeyJoinColumn(name = "id")
public abstract class Employee extends Person {

    public static final int VACATION_DAYS_BASE = 26;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private float hourlyRate;

    @Column(nullable = false)
    private String bankAccount;

    private int vacationDaysTaken;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String documentId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contract_type_id", nullable = false)
    private ContractType contractType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employee_id")
    private List<WorkRecord> workRecords = new ArrayList<>();

    protected Employee() {}

    protected Employee(String firstName, String lastName, LocalDate dateOfBirth,
                       Address address, String phoneNumber, String emailAddress,
                       String jobTitle, float hourlyRate, String bankAccount,
                       String login, String password, String documentId,
                       ContractType contractType) {
        super(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress);
        if (jobTitle == null || jobTitle.isBlank()) throw new IllegalArgumentException("Job title cannot be blank");
        if (hourlyRate <= 0) throw new IllegalArgumentException("Hourly rate must be positive");
        if (bankAccount == null || bankAccount.isBlank()) throw new IllegalArgumentException("Bank account cannot be blank");
        if (login == null || login.isBlank()) throw new IllegalArgumentException("Login cannot be blank");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password cannot be blank");
        if (documentId == null || documentId.isBlank()) throw new IllegalArgumentException("Document ID cannot be blank");
        if (contractType == null) throw new IllegalArgumentException("Contract type cannot be null");
        this.jobTitle = jobTitle;
        this.hourlyRate = hourlyRate;
        this.bankAccount = bankAccount;
        this.login = login;
        this.password = password;
        this.documentId = documentId;
        this.contractType = contractType;
    }

    @Transient
    public int getVacationDaysLeft() {
        if (contractType == null) return 0;
        return (int) contractType.calculateVacationEntitlement() - vacationDaysTaken;
    }

    public float calculateSalary(WorkRecord workRecord) {
        return workRecord.getHoursWorked() * hourlyRate;
    }

    public WorkRecord addWorkRecord(int month, int year, float hoursWorked) {
        WorkRecord workRecord = WorkRecord.createWorkRecord(this, month, year, hoursWorked);
        workRecords.add(workRecord);
        return workRecord;
    }

    public List<WorkRecord> getWorkRecords() {
        return Collections.unmodifiableList(workRecords);
    }

    public void logIn() {}
    public void logOut() {}

    public String getJobTitle() { return jobTitle; }
    public float getHourlyRate() { return hourlyRate; }
    public String getBankAccount() { return bankAccount; }
    public int getVacationDaysTaken() { return vacationDaysTaken; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getDocumentId() { return documentId; }
    public ContractType getContractType() { return contractType; }

    public void setJobTitle(String jobTitle) {
        if (jobTitle == null || jobTitle.isBlank()) throw new IllegalArgumentException("Job title cannot be blank");
        this.jobTitle = jobTitle;
    }

    public void setHourlyRate(float hourlyRate) {
        if (hourlyRate <= 0) throw new IllegalArgumentException("Hourly rate must be positive");
        this.hourlyRate = hourlyRate;
    }

    public void setBankAccount(String bankAccount) {
        if (bankAccount == null || bankAccount.isBlank()) throw new IllegalArgumentException("Bank account cannot be blank");
        this.bankAccount = bankAccount;
    }

    public void setVacationDaysTaken(int vacationDaysTaken) {
        if (vacationDaysTaken < 0) throw new IllegalArgumentException("Vacation days taken cannot be negative");
        this.vacationDaysTaken = vacationDaysTaken;
    }

    public void setLogin(String login) {
        if (login == null || login.isBlank()) throw new IllegalArgumentException("Login cannot be blank");
        this.login = login;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password cannot be blank");
        this.password = password;
    }
}
