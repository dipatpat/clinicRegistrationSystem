package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column(nullable = false)
    private LocalDate validTo;

    @Column(nullable = false)
    private LocalTime workDayStart;

    @Column(nullable = false)
    private LocalTime workDayEnd;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    Schedule() {}

    private Schedule(Doctor doctor, LocalDate validFrom, LocalDate validTo,
                     LocalTime workDayStart, LocalTime workDayEnd) {
        if (doctor == null) throw new IllegalArgumentException("Doctor cannot be null");
        if (validFrom == null) throw new IllegalArgumentException("Valid from cannot be null");
        if (validTo == null) throw new IllegalArgumentException("Valid to cannot be null");
        if (validTo.isBefore(validFrom)) throw new IllegalArgumentException("Valid to must be after valid from");
        if (workDayStart == null) throw new IllegalArgumentException("Work day start cannot be null");
        if (workDayEnd == null) throw new IllegalArgumentException("Work day end cannot be null");
        if (!workDayEnd.isAfter(workDayStart)) throw new IllegalArgumentException("Work day end must be after work day start");
        this.doctor = doctor;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.workDayStart = workDayStart;
        this.workDayEnd = workDayEnd;
    }

    public static Schedule createSchedule(Doctor doctor, LocalDate validFrom, LocalDate validTo,
                                          LocalTime workDayStart, LocalTime workDayEnd) {
        return new Schedule(doctor, validFrom, validTo, workDayStart, workDayEnd);
    }

    public List<LocalTime> getAvailableTime(LocalDate date) {
        if (date.isBefore(validFrom) || date.isAfter(validTo)) return List.of();
        List<LocalTime> slots = new ArrayList<>();
        LocalTime cursor = workDayStart;
        while (cursor.plusMinutes(30).compareTo(workDayEnd) <= 0) {
            slots.add(cursor);
            cursor = cursor.plusMinutes(30);
        }
        return slots;
    }

    public boolean isWithinWorkingHours(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        return !date.isBefore(validFrom)
                && !date.isAfter(validTo)
                && !time.isBefore(workDayStart)
                && !time.isAfter(workDayEnd);
    }

    public Long getId() { return id; }
    public LocalDate getValidFrom() { return validFrom; }
    public LocalDate getValidTo() { return validTo; }
    public LocalTime getWorkDayStart() { return workDayStart; }
    public LocalTime getWorkDayEnd() { return workDayEnd; }
    public Doctor getDoctor() { return doctor; }
}
