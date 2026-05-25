package pjatk.mas.clinicregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.mas.clinicregistrationsystem.model.MedicalRecord;

import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByPatientPesel(String pesel);
}
