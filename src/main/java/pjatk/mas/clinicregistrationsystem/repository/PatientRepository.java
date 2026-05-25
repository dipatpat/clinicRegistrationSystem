package pjatk.mas.clinicregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.mas.clinicregistrationsystem.model.Patient;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPesel(String pesel);
    Optional<Patient> findByLogin(String login);
}
