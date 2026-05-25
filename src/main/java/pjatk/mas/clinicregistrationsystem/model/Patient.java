package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;
import pjatk.mas.clinicregistrationsystem.model.enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Patient")
@PrimaryKeyJoinColumn(name = "id")
public class Patient extends Person {

    public static final int MINIMUM_CONSENT_AGE = 18;

    @Column(nullable = false, unique = true)
    private String pesel;

    private String login;
    private String password;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToOne(mappedBy = "patient")
    private MedicalRecord medicalRecord;

    protected Patient() {}

    public Patient(String firstName, String lastName, LocalDate dateOfBirth,
                   Address address, String phoneNumber, String emailAddress,
                   String pesel) {
        super(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress);
        if (pesel == null || pesel.isBlank()) throw new IllegalArgumentException("PESEL cannot be blank");
        this.pesel = pesel;
    }

    @Transient
    public boolean isRequiresGuardianConsent() {
        return getAge() < MINIMUM_CONSENT_AGE;
    }

    public void addAppointment(Appointment appointment) {
        if (appointments.contains(appointment)) return;
        appointments.add(appointment);
        if (appointment.getPatient() != this) {
            appointment.setPatient(this);
        }
    }

    public void removeAppointment(Appointment appointment) {
        if (!appointments.contains(appointment)) return;
        appointments.remove(appointment);
        if (appointment.getPatient() == this) {
            appointment.setPatient(null);
        }
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public List<Appointment> getActiveAppointments() {
        return appointments.stream()
                .filter(a -> a.getStatus() == Status.SCHEDULED || a.getStatus() == Status.CONFIRMED)
                .toList();
    }

    public String getPesel() { return pesel; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public MedicalRecord getMedicalRecord() { return medicalRecord; }

    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }

    void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}
