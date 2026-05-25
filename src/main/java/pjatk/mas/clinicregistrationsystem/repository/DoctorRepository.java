package pjatk.mas.clinicregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.mas.clinicregistrationsystem.model.Doctor;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialization(String specialization);
}
