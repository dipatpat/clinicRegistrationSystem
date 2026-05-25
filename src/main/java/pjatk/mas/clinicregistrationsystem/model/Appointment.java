package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;
import pjatk.mas.clinicregistrationsystem.model.enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "Appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String room;

    @Column(nullable = false)
    private float duration;

    private LocalDateTime cancelledAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime completedAt;

    @Column(nullable = false)
    private boolean noShow = false;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    protected Appointment() {}

    public static Appointment forSeeding(Patient patient, Doctor doctor, LocalDateTime date, String room, float duration) {
        Appointment a = new Appointment();
        a.patient = patient;
        a.doctor = doctor;
        a.date = date;
        a.room = room;
        a.duration = duration;
        a.createdAt = date.minusDays(7);
        a.status = Status.SCHEDULED;
        patient.addAppointment(a);
        doctor.addAppointment(a);
        return a;
    }

    public Appointment(Patient patient, Doctor doctor, LocalDateTime date, String room, float duration) {
        if (patient == null) throw new IllegalArgumentException("Patient cannot be null");
        if (doctor == null) throw new IllegalArgumentException("Doctor cannot be null");
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        if (date.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Appointment date cannot be in the past");
        if (room == null || room.isBlank()) throw new IllegalArgumentException("Room cannot be blank");
        if (duration <= 0) throw new IllegalArgumentException("Duration must be positive");
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.room = room;
        this.duration = duration;
        this.createdAt = LocalDateTime.now();
        this.status = Status.SCHEDULED;
        patient.addAppointment(this);
        doctor.addAppointment(this);
    }

    public void confirm() {
        if (status != Status.SCHEDULED) throw new IllegalStateException("Only scheduled appointments can be confirmed");
        this.status = Status.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status == Status.COMPLETED || status == Status.NOSHOW)
            throw new IllegalStateException("Cannot cancel a completed or no-show appointment");
        this.status = Status.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public void markPatientsNoShow() {
        if (status != Status.CONFIRMED)
            throw new IllegalStateException("Only confirmed appointments can be marked as no-show");
        if (date.isAfter(LocalDateTime.now()))
            throw new IllegalStateException("Cannot mark a future appointment as no-show");
        this.status = Status.NOSHOW;
        this.noShow = true;
    }

    public void markAsCompleted() {
        if (status != Status.CONFIRMED)
            throw new IllegalStateException("Only confirmed appointments can be marked as completed");
        if (date.isAfter(LocalDateTime.now()))
            throw new IllegalStateException("Cannot mark a future appointment as completed");
        this.status = Status.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public boolean isPast() { return date.isBefore(LocalDateTime.now()); }

    public Long getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Status getStatus() { return status; }
    public String getRoom() { return room; }
    public float getDuration() { return duration; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public boolean isNoShow() { return noShow; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }

    void setPatient(Patient patient) { this.patient = patient; }
    void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public void setRoom(String room) {
        if (room == null || room.isBlank()) throw new IllegalArgumentException("Room cannot be blank");
        this.room = room;
    }

    public void setDate(LocalDateTime date) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        if (date.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Appointment date cannot be in the past");
        this.date = date;
    }
}
