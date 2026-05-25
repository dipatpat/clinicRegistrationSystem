package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "WorkRecord")
public class WorkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_month", nullable = false)
    private int month;

    @Column(name = "work_year", nullable = false)
    private int year;

    @Column(nullable = false)
    private float hoursWorked;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    WorkRecord() {}

    private WorkRecord(Employee employee, int month, int year, float hoursWorked) {
        if (employee == null) throw new IllegalArgumentException("Employee cannot be null");
        if (month < 1 || month > 12) throw new IllegalArgumentException("Month must be between 1 and 12");
        if (year < 2000) throw new IllegalArgumentException("Year must be 2000 or later");
        if (hoursWorked < 0) throw new IllegalArgumentException("Hours worked cannot be negative");
        this.employee = employee;
        this.month = month;
        this.year = year;
        this.hoursWorked = hoursWorked;
    }

    public static WorkRecord createWorkRecord(Employee employee, int month, int year, float hoursWorked) {
        return new WorkRecord(employee, month, year, hoursWorked);
    }

    public Long getId() { return id; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public float getHoursWorked() { return hoursWorked; }
    public Employee getEmployee() { return employee; }

    public void setHoursWorked(float hoursWorked) {
        if (hoursWorked < 0) throw new IllegalArgumentException("Hours worked cannot be negative");
        this.hoursWorked = hoursWorked;
    }
}
