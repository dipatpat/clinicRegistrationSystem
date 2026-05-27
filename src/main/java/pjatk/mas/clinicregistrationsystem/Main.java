package pjatk.mas.clinicregistrationsystem;

import pjatk.mas.clinicregistrationsystem.model.*;
import pjatk.mas.clinicregistrationsystem.model.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== LuxuryMed — Class Instantiation Demo ===\n");

        // ----------------------------------------------------------------
        // Address  (embedded value object used by every Person subclass)
        // ----------------------------------------------------------------
        Address address = new Address(
                "Poland", "Marszałkowska", "10", "00-001", "Warsaw", null);
        System.out.println("[Address]       " + address);

        // ----------------------------------------------------------------
        // ContractType hierarchy  (SINGLE_TABLE: FullTime | PartTime)
        // ----------------------------------------------------------------
        FullTime fullTime = FullTime.create(1.5f);
        System.out.println("[FullTime]      overtime rate: " + fullTime.getOvertimeRate()
                + ", vacation entitlement: " + fullTime.calculateVacationEntitlement() + " days");

        PartTime partTime = PartTime.create(Shift.MORNING, 20);
        System.out.println("[PartTime]      shift: " + partTime.getShift()
                + ", hours: " + partTime.getHoursInContract()
                + ", vacation entitlement: " + partTime.calculateVacationEntitlement() + " days");

        // ----------------------------------------------------------------
        // Manager  (Person → Employee → Manager, JOINED tables)
        // ----------------------------------------------------------------
        Manager manager = new Manager(
                "Anna", "Kowalska", LocalDate.of(1978, 3, 15),
                address, "+48 600 100 100", "anna@clinic.pl",
                "Clinic Manager", 120f, "PL00000000000000000000000001",
                "anna", "secret", "DOC000001",
                SeniorityLevel.SENIOR);
        manager.assignContract(fullTime);
        WorkRecord managerWorkRecord = manager.addWorkRecord(5, 2026, 160f);
        System.out.println("[Manager]       " + manager.getFirstName() + " " + manager.getLastName()
                + " | seniority: " + manager.getSeniorityLevel()
                + " | bonus: " + manager.getBonus()
                + " | salary (May): " + manager.calculateSalary(managerWorkRecord)
                + " | vacation left: " + manager.getVacationDaysLeft() + " days");

        // ----------------------------------------------------------------
        // Receptionist  (Person → Employee → Receptionist, JOINED tables)
        // ----------------------------------------------------------------
        Receptionist receptionist = new Receptionist(
                "Katarzyna", "Zielińska", LocalDate.of(1992, 7, 20),
                address, "+48 600 200 200", "katarzyna@clinic.pl",
                "Receptionist", 45f, "PL00000000000000000000000002",
                "katarzyna", "secret", "DOC000002",
                List.of("Polish", "English", "German"));
        receptionist.assignContract(PartTime.create(Shift.MORNING, 30));
        WorkRecord receptionistWorkRecord = receptionist.addWorkRecord(5, 2026, 120f);
        System.out.println("[Receptionist]  " + receptionist
                + " | salary (May): " + receptionist.calculateSalary(receptionistWorkRecord)
                + " | vacation left: " + receptionist.getVacationDaysLeft() + " days");

        // ----------------------------------------------------------------
        // Doctor  (Person → Employee → Doctor, JOINED tables)
        //   also demonstrates Schedule creation
        // ----------------------------------------------------------------
        Doctor doctor = new Doctor(
                "Jan", "Nowak", LocalDate.of(1975, 4, 10),
                address, "+48 600 300 100", "jan@clinic.pl",
                "Cardiologist", 100f, "PL00000000000000000000000003",
                "jan", "secret", "LIC100001",
                "Cardiology", "PWZ/100001");
        doctor.assignContract(FullTime.create(1.5f));
        Schedule schedule = doctor.addSchedule(
                LocalDate.of(2026, 5, 26), LocalDate.of(2026, 6, 30),
                LocalTime.of(8, 0), LocalTime.of(16, 0));
        WorkRecord doctorWorkRecord = doctor.addWorkRecord(5, 2026, 160f);
        System.out.println("[Doctor]        Dr. " + doctor.getFirstName() + " " + doctor.getLastName()
                + " | specialization: " + doctor.getSpecialization()
                + " | salary (May): " + doctor.calculateSalary(doctorWorkRecord));
        System.out.println("[Schedule]      valid: " + schedule.getValidFrom()
                + " – " + schedule.getValidTo()
                + " | hours: " + schedule.getWorkDayStart() + "–" + schedule.getWorkDayEnd());

        // ----------------------------------------------------------------
        // Patient (adult)
        // ----------------------------------------------------------------
        Patient adult = new Patient(
                "Piotr", "Malinowski", LocalDate.of(1990, 2, 15),
                address, "+48 500 100 100", "piotr@email.com",
                "90021512345");
        adult.setLogin("piotr");
        adult.setPassword("secret");
        System.out.println("[Patient]       " + adult.getFirstName() + " " + adult.getLastName()
                + " | age: " + adult.getAge()
                + " | guardian consent required: " + adult.isRequiresGuardianConsent());

        // ----------------------------------------------------------------
        // Patient (minor — triggers guardian consent flag)
        // ----------------------------------------------------------------
        Patient minor = new Patient(
                "Tomasz", "Kowalczyk", LocalDate.of(2014, 4, 18),
                address, "+48 500 300 300", null,
                "14041898765");
        System.out.println("[Patient/minor] " + minor.getFirstName() + " " + minor.getLastName()
                + " | age: " + minor.getAge()
                + " | guardian consent required: " + minor.isRequiresGuardianConsent());

        // ----------------------------------------------------------------
        // MedicalRecord + MedicalEntry
        // ----------------------------------------------------------------
        MedicalRecord record = new MedicalRecord(adult);
        record.updateMedicalRecord(82f, "120/80");
        record.addAllergy("Penicillin");
        record.addChronicCondition("Hypertension");
        record.addPastSurgery("Appendectomy 2018");
        MedicalEntry entry = record.addEntry(
                MedicalEntryType.VISIT_SUMMARY,
                "Routine cardiology check", "Mild hypertension", "Monitor blood pressure weekly");
        System.out.println("[MedicalRecord] patient: " + record.getPatient().getLastName()
                + " | weight: " + record.getWeight() + " kg"
                + " | allergies: " + record.getAllergies()
                + " | conditions: " + record.getChronicConditions());
        System.out.println("[MedicalEntry]  type: " + entry.getType()
                + " | diagnosis: " + entry.getDiagnosis());

        // ----------------------------------------------------------------
        // Appointment — future, full state machine: SCHEDULED → CONFIRMED → CANCELLED
        // ----------------------------------------------------------------
        LocalDateTime future = LocalDateTime.now().plusDays(7)
                .withHour(10).withMinute(0).withSecond(0).withNano(0);
        Appointment apt = new Appointment(adult, doctor, future, "Room 101", 30f);
        System.out.println("[Appointment]   status after creation: " + apt.getStatus());
        apt.confirm();
        System.out.println("[Appointment]   status after confirm:  " + apt.getStatus());
        apt.cancel();
        System.out.println("[Appointment]   status after cancel:   " + apt.getStatus());

        // ----------------------------------------------------------------
        // Appointment (past via forSeeding) — CONFIRMED → COMPLETED
        // ----------------------------------------------------------------
        Appointment pastCompleted = Appointment.forSeeding(
                adult, doctor, LocalDateTime.now().minusDays(3), "Room 102", 30f);
        pastCompleted.confirm();
        pastCompleted.markAsCompleted();
        System.out.println("[Appointment]   status after complete: " + pastCompleted.getStatus());

        // ----------------------------------------------------------------
        // Appointment (past via forSeeding) — CONFIRMED → NOSHOW
        // ----------------------------------------------------------------
        Appointment pastNoShow = Appointment.forSeeding(
                adult, doctor, LocalDateTime.now().minusDays(2), "Room 103", 45f);
        pastNoShow.confirm();
        pastNoShow.markPatientsNoShow();
        System.out.println("[Appointment]   status after no-show:  " + pastNoShow.getStatus());

        System.out.println("\n=== All classes instantiated successfully ===");
    }
}
