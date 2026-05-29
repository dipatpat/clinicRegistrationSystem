package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;
import pjatk.mas.clinicregistrationsystem.model.enums.Shift;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Doctor")
@PrimaryKeyJoinColumn(name = "id")
public class Doctor extends Employee {

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "doctor_id")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    protected Doctor() {}

    private Doctor(String firstName, String lastName, LocalDate dateOfBirth,
                  Address address, String phoneNumber, String emailAddress,
                  String jobTitle, float hourlyRate, String bankAccount,
                  String login, String password, String documentNumber,
                  ContractType contractType,
                  String specialization, String licenseNumber) {
        super(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress,
                jobTitle, hourlyRate, bankAccount, login, password, documentNumber, contractType);
        if (specialization == null || specialization.isBlank()) throw new IllegalArgumentException("Specialization cannot be blank");
        if (licenseNumber == null || licenseNumber.isBlank()) throw new IllegalArgumentException("License number cannot be blank");
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }

    public static Doctor createWithFullTime(String firstName, String lastName, LocalDate dateOfBirth,
                                            Address address, String phoneNumber, String emailAddress,
                                            String jobTitle, float hourlyRate, String bankAccount,
                                            String login, String password, String documentNumber,
                                            String specialization, String licenseNumber,
                                            float overtimeRate) {
        return new Doctor(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress,
                jobTitle, hourlyRate, bankAccount, login, password, documentNumber,
                FullTime.create(overtimeRate), specialization, licenseNumber);
    }

    public static Doctor createWithPartTime(String firstName, String lastName, LocalDate dateOfBirth,
                                            Address address, String phoneNumber, String emailAddress,
                                            String jobTitle, float hourlyRate, String bankAccount,
                                            String login, String password, String documentNumber,
                                            String specialization, String licenseNumber,
                                            Shift shift, int hoursInContract) {
        return new Doctor(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress,
                jobTitle, hourlyRate, bankAccount, login, password, documentNumber,
                PartTime.create(shift, hoursInContract), specialization, licenseNumber);
    }

    public Schedule addSchedule(LocalDate validFrom, LocalDate validTo,
                                java.time.LocalTime workDayStart, java.time.LocalTime workDayEnd) {
        Schedule schedule = Schedule.createSchedule(this, validFrom, validTo, workDayStart, workDayEnd);
        schedules.add(schedule);
        return schedule;
    }

    public void addAppointment(Appointment appointment) {
        if (appointments.contains(appointment)) return;
        appointments.add(appointment);
        if (appointment.getDoctor() != this) {
            appointment.setDoctor(this);
        }
    }

    public void removeAppointment(Appointment appointment) {
        if (!appointments.contains(appointment)) return;
        appointments.remove(appointment);
        if (appointment.getDoctor() == this) {
            appointment.setDoctor(null);
        }
    }

    public MedicalRecord viewMedicalHistory(Patient patient) {
        return patient.getMedicalRecord();
    }

    public List<Schedule> getSchedules() { return Collections.unmodifiableList(schedules); }
    public List<Appointment> getAppointments() { return Collections.unmodifiableList(appointments); }
    public String getSpecialization() { return specialization; }
    public String getLicenseNumber() { return licenseNumber; }

    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.isBlank()) throw new IllegalArgumentException("Specialization cannot be blank");
        this.specialization = specialization;
    }
}
