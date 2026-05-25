package pjatk.mas.clinicregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.mas.clinicregistrationsystem.model.Appointment;
import pjatk.mas.clinicregistrationsystem.model.enums.Status;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientPesel(String pesel);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByStatus(Status status);
}
