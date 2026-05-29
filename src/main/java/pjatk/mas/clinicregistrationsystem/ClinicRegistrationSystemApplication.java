package pjatk.mas.clinicregistrationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import pjatk.mas.clinicregistrationsystem.model.*;
import pjatk.mas.clinicregistrationsystem.model.enums.DocumentType;
import pjatk.mas.clinicregistrationsystem.model.enums.MedicalEntryType;
import pjatk.mas.clinicregistrationsystem.model.enums.SeniorityLevel;
import pjatk.mas.clinicregistrationsystem.model.enums.Shift;
import pjatk.mas.clinicregistrationsystem.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class ClinicRegistrationSystemApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ClinicRegistrationSystemApplication.class, args);
        demo(
                context.getBean(EmployeeRepository.class),
                context.getBean(DoctorRepository.class),
                context.getBean(PatientRepository.class),
                context.getBean(MedicalRecordRepository.class),
                context.getBean(AppointmentRepository.class)
        );
    }

    public static void demo(EmployeeRepository employeeRepository,
                            DoctorRepository doctorRepository,
                            PatientRepository patientRepository,
                            MedicalRecordRepository medicalRecordRepository,
                            AppointmentRepository appointmentRepository) {
        System.out.println("=== LuxuryMed — Class Instantiation Demo ===\n");

        Address address = new Address("Poland", "Marszałkowska", "10", "00-001", "Warsaw", null);

        // ----------------------------------------------------------------
        // Manager  (FullTime contract — created via factory, composition enforced)
        // ----------------------------------------------------------------
        System.out.println("[Manager]");
        Manager manager = Manager.createWithFullTime(
                "Anna", "Kowalska", LocalDate.of(1978, 3, 15),
                address, "+48 600 100 100", "anna@clinic.pl",
                "Clinic Manager", 120f, "PL00000000000000000000000001",
                "anna", "secret", "DEMO000001",
                DocumentType.NATIONAL_ID,
                SeniorityLevel.SENIOR, 1.5f);
        FullTime managerContract = (FullTime) manager.getContractType();
        WorkRecord managerWorkRecord = manager.addWorkRecord(5, 2026, 160f);
        System.out.println("  name:               " + manager.getFirstName() + " " + manager.getLastName());
        System.out.println("  address:            " + manager.getAddress());
        System.out.println("  date of birth:      " + manager.getDateOfBirth());
        System.out.println("  phone:              " + manager.getPhoneNumber());
        System.out.println("  email:              " + manager.getEmailAddress());
        System.out.println("  job title:          " + manager.getJobTitle());
        System.out.println("  hourly rate:        " + manager.getHourlyRate());
        System.out.println("  bank account:       " + manager.getBankAccount());
        System.out.println("  document number:" + manager.getDocumentNumber());
        System.out.println("  seniority:          " + manager.getSeniorityLevel());
        System.out.println("  contract type:      " + managerContract.getClass().getSimpleName()
                + " | overtime rate: " + managerContract.getOvertimeRate()
                + " | vacation entitlement: " + managerContract.getVacationEntitlement() + " days");
        System.out.println("  vacation left:      " + manager.getVacationDaysLeft() + " days");
        System.out.println("  bonus:              " + manager.getBonus());
        System.out.println("  salary (May 2026):  " + manager.calculateSalary(managerWorkRecord));
        employeeRepository.save(manager);

        // ----------------------------------------------------------------
        // Receptionist  (PartTime contract — created via factory, composition enforced)
        // ----------------------------------------------------------------
        System.out.println("\n[Receptionist]");
        Receptionist receptionist = Receptionist.createWithPartTime(
                "Katarzyna", "Zielińska", LocalDate.of(1992, 7, 20),
                address, "+48 600 200 200", "luiza@clinic.pl",
                "Receptionist", 45f, "PL00000000000000000000000002",
                "luiza", "secret", "DEMO000002",
                DocumentType.NATIONAL_ID,
                List.of("Polish", "English", "German"),
                Shift.MORNING, 30);
        PartTime receptionistContract = (PartTime) receptionist.getContractType();
        WorkRecord receptionistWorkRecord = receptionist.addWorkRecord(5, 2026, 120f);
        System.out.println("  name:               " + receptionist.getFirstName() + " " + receptionist.getLastName());
        System.out.println("  date of birth:      " + receptionist.getDateOfBirth());
        System.out.println("  phone:              " + receptionist.getPhoneNumber());
        System.out.println("  email:              " + receptionist.getEmailAddress());
        System.out.println("  job title:          " + receptionist.getJobTitle());
        System.out.println("  hourly rate:        " + receptionist.getHourlyRate());
        System.out.println("  bank account:       " + receptionist.getBankAccount());
        System.out.println("  document number:" + receptionist.getDocumentNumber());
        System.out.println("  languages spoken:   " + receptionist.getLanguagesSpoken());
        System.out.println("  contract type:      " + receptionistContract.getClass().getSimpleName()
                + " | shift: " + receptionistContract.getShift()
                + " | hours: " + receptionistContract.getHoursInContract()
                + " | vacation entitlement: " + receptionistContract.getVacationEntitlement() + " days");
        System.out.println("  vacation left:      " + receptionist.getVacationDaysLeft() + " days");
        System.out.println("  salary (May 2026):  " + receptionist.calculateSalary(receptionistWorkRecord));
        employeeRepository.save(receptionist);

        // ----------------------------------------------------------------
        // Doctor  (FullTime contract — created via factory, composition enforced)
        // ----------------------------------------------------------------
        System.out.println("\n[Doctor]");
        Doctor doctor = Doctor.createWithFullTime(
                "Jan", "Nowak", LocalDate.of(1975, 4, 10),
                address, "+48 600 300 100", "jan@clinic.pl",
                "Cardiologist", 100f, "PL00000000000000000000000003",
                "jan", "secret", "DEMO-D001",
                DocumentType.PASSPORT,
                "Cardiology", "DEMO/LIC001", 1.5f);
        FullTime doctorContract = (FullTime) doctor.getContractType();
        Schedule schedule = doctor.addSchedule(
                LocalDate.of(2026, 5, 26), LocalDate.of(2026, 6, 30),
                LocalTime.of(8, 0), LocalTime.of(16, 0));
        WorkRecord doctorWorkRecord = doctor.addWorkRecord(5, 2026, 160f);
        System.out.println("  name:               Dr. " + doctor.getFirstName() + " " + doctor.getLastName());
        System.out.println("  date of birth:      " + doctor.getDateOfBirth());
        System.out.println("  phone:              " + doctor.getPhoneNumber());
        System.out.println("  email:              " + doctor.getEmailAddress());
        System.out.println("  job title:          " + doctor.getJobTitle());
        System.out.println("  hourly rate:        " + doctor.getHourlyRate());
        System.out.println("  bank account:       " + doctor.getBankAccount());
        System.out.println("  document number:" + doctor.getDocumentNumber());
        System.out.println("  specialization:     " + doctor.getSpecialization());
        System.out.println("  license number:     " + doctor.getLicenseNumber());
        System.out.println("  contract type:      " + doctorContract.getClass().getSimpleName()
                + " | overtime rate: " + doctorContract.getOvertimeRate()
                + " | vacation entitlement: " + doctorContract.getVacationEntitlement() + " days");
        System.out.println("  vacation left:      " + doctor.getVacationDaysLeft() + " days");
        System.out.println("  salary (May 2026):  " + doctor.calculateSalary(doctorWorkRecord));
        System.out.println("  schedule:           " + schedule.getValidFrom() + " – " + schedule.getValidTo()
                + " | " + schedule.getWorkDayStart() + "–" + schedule.getWorkDayEnd());
        doctorRepository.save(doctor);

        // ----------------------------------------------------------------
        // Patient (adult)
        // ----------------------------------------------------------------
        System.out.println("\n[Patient]");
        Patient adult = new Patient(
                "Piotr", "Malinowski", LocalDate.of(1990, 2, 15),
                address, "+48 500 100 100", "piotr@email.com",
                "90021512345");
        adult.setLogin("piotr");
        adult.setPassword("secret");
        System.out.println("  name:                      " + adult.getFirstName() + " " + adult.getLastName());
        System.out.println("  date of birth:             " + adult.getDateOfBirth());
        System.out.println("  age:                       " + adult.getAge());
        System.out.println("  pesel:                     " + adult.getPesel());
        System.out.println("  phone:                     " + adult.getPhoneNumber());
        System.out.println("  login:                     " + adult.getLogin());
        System.out.println("  guardian consent required: " + adult.isRequiresGuardianConsent());
        patientRepository.save(adult);

        // ----------------------------------------------------------------
        // Patient (minor)
        // ----------------------------------------------------------------
        System.out.println("\n[Patient — minor]");
        Patient minor = new Patient(
                "Tomasz", "Kowalczyk", LocalDate.of(2014, 4, 18),
                address, "+48 500 300 300", null,
                "14041898765");
        System.out.println("  name:                      " + minor.getFirstName() + " " + minor.getLastName());
        System.out.println("  date of birth:             " + minor.getDateOfBirth());
        System.out.println("  age:                       " + minor.getAge());
        System.out.println("  pesel:                     " + minor.getPesel());
        System.out.println("  login:                     " + minor.getLogin() + " (optional, null)");
        System.out.println("  guardian consent required: " + minor.isRequiresGuardianConsent());
        patientRepository.save(minor);

        // ----------------------------------------------------------------
        // MedicalRecord + MedicalEntry
        // ----------------------------------------------------------------
        System.out.println("\n[MedicalRecord]");
        MedicalRecord record = new MedicalRecord(adult);
        record.updateMedicalRecord(82f, "120/80");
        record.addAllergy("Penicillin");
        record.addChronicCondition("Hypertension");
        record.addPastSurgery("Appendectomy 2018");
        System.out.println("  patient:            " + record.getPatient().getFirstName() + " " + record.getPatient().getLastName());
        System.out.println("  date created:       " + record.getDateCreated());
        System.out.println("  active:             " + record.isActive());
        System.out.println("  weight:             " + record.getWeight() + " kg");
        System.out.println("  blood pressure:     " + record.getBloodPressure());
        System.out.println("  allergies:          " + record.getAllergies());
        System.out.println("  chronic conditions: " + record.getChronicConditions());
        System.out.println("  past surgeries:     " + record.getPastSurgeries());

        System.out.println("\n[MedicalEntry]");
        MedicalEntry entry = record.addEntry(
                MedicalEntryType.VISIT_SUMMARY,
                "Routine cardiology check", "Mild hypertension", "Monitor blood pressure weekly");
        System.out.println("  type:          " + entry.getType());
        System.out.println("  description:   " + entry.getDescription());
        System.out.println("  diagnosis:     " + entry.getDiagnosis());
        System.out.println("  clinical notes:" + entry.getClinicalNotes());
        System.out.println("  date recorded: " + entry.getDateRecorded());
        medicalRecordRepository.save(record);

        // ----------------------------------------------------------------
        // Appointment — future, full state machine
        // ----------------------------------------------------------------
        System.out.println("\n[Appointment — future]");
        LocalDateTime future = LocalDateTime.now().plusDays(7)
                .withHour(10).withMinute(0).withSecond(0).withNano(0);
        Appointment apt = new Appointment(adult, doctor, future, "Room 101", 30f);
        System.out.println("  patient:   " + apt.getPatient().getFirstName() + " " + apt.getPatient().getLastName());
        System.out.println("  doctor:    Dr. " + apt.getDoctor().getFirstName() + " " + apt.getDoctor().getLastName());
        System.out.println("  date:      " + apt.getDate());
        System.out.println("  room:      " + apt.getRoom());
        System.out.println("  duration:  " + apt.getDuration() + " min");
        System.out.println("  status after creation: " + apt.getStatus());
        apt.confirm();
        System.out.println("  status after confirm:  " + apt.getStatus());
        apt.cancel();
        System.out.println("  status after cancel:   " + apt.getStatus());
        appointmentRepository.save(apt);

        // ----------------------------------------------------------------
        // Appointment (past) — CONFIRMED → COMPLETED
        // ----------------------------------------------------------------
        System.out.println("\n[Appointment — past, completed]");
        Appointment pastCompleted = Appointment.forSeeding(
                adult, doctor, LocalDateTime.now().minusDays(3), "Room 102", 30f);
        pastCompleted.confirm();
        pastCompleted.markAsCompleted();
        System.out.println("  status: " + pastCompleted.getStatus());
        appointmentRepository.save(pastCompleted);

        // ----------------------------------------------------------------
        // Appointment (past) — CONFIRMED → NOSHOW
        // ----------------------------------------------------------------
        System.out.println("\n[Appointment — past, no-show]");
        Appointment pastNoShow = Appointment.forSeeding(
                adult, doctor, LocalDateTime.now().minusDays(2), "Room 103", 45f);
        pastNoShow.confirm();
        pastNoShow.markPatientsNoShow();
        System.out.println("  status: " + pastNoShow.getStatus());
        appointmentRepository.save(pastNoShow);

        System.out.println("\n=== All classes instantiated and saved to H2 database ===");
    }
}
