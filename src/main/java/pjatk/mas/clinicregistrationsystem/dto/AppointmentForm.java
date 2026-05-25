package pjatk.mas.clinicregistrationsystem.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class AppointmentForm {

    private Long doctorId;
    private String patientPesel;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    private String room;
    private float duration = 30f;

    public Long getDoctorId() { return doctorId; }
    public String getPatientPesel() { return patientPesel; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getRoom() { return room; }
    public float getDuration() { return duration; }

    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public void setPatientPesel(String patientPesel) { this.patientPesel = patientPesel; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setRoom(String room) { this.room = room; }
    public void setDuration(float duration) { this.duration = duration; }
}
