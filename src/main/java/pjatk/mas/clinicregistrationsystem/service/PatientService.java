package pjatk.mas.clinicregistrationsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.clinicregistrationsystem.dto.PatientRegistrationForm;
import pjatk.mas.clinicregistrationsystem.model.Address;
import pjatk.mas.clinicregistrationsystem.model.MedicalRecord;
import pjatk.mas.clinicregistrationsystem.model.Patient;
import pjatk.mas.clinicregistrationsystem.repository.MedicalRecordRepository;
import pjatk.mas.clinicregistrationsystem.repository.PatientRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PatientService(PatientRepository patientRepository,
                          MedicalRecordRepository medicalRecordRepository) {
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public Optional<Patient> findByPesel(String pesel) {
        return patientRepository.findByPesel(pesel);
    }

    @Transactional
    public Patient register(PatientRegistrationForm form) {
        if (patientRepository.findByPesel(form.getPesel()).isPresent()) {
            throw new IllegalArgumentException("A patient with this PESEL already exists.");
        }
        Address address = new Address(
                form.getCountry(), form.getStreetName(), form.getStreetNumber(),
                form.getPostalCode(), form.getCityName(), form.getApartmentNumber());

        Patient patient = new Patient(
                form.getFirstName(), form.getLastName(), form.getDateOfBirth(),
                address, form.getPhoneNumber(), form.getEmailAddress(), form.getPesel());

        patientRepository.save(patient);

        MedicalRecord record = new MedicalRecord(patient);
        medicalRecordRepository.save(record);

        return patient;
    }
}
