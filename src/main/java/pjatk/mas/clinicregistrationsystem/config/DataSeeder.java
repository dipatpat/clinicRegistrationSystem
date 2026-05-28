package pjatk.mas.clinicregistrationsystem.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pjatk.mas.clinicregistrationsystem.model.*;
import pjatk.mas.clinicregistrationsystem.model.enums.*;
import pjatk.mas.clinicregistrationsystem.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            EmployeeRepository employeeRepository,
            MedicalRecordRepository medicalRecordRepository,
            AppointmentRepository appointmentRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (patientRepository.count() > 0) return;

            String encodedPassword = passwordEncoder.encode("password123");

            Address warsawCenter = new Address("Poland", "Marszałkowska", "10", "00-001", "Warsaw", null);
            Address warsawMokotow = new Address("Poland", "Puławska", "45", "02-001", "Warsaw", "3A");
            Address krakow = new Address("Poland", "Floriańska", "12", "30-001", "Kraków", "2");
            Address gdansk = new Address("Poland", "Długa", "7", "80-001", "Gdańsk", null);
            Address poznan = new Address("Poland", "Święty Marcin", "22", "61-001", "Poznań", "5B");
            Address lodz = new Address("Poland", "Piotrkowska", "100", "90-001", "Łódź", null);

            // --- Manager ---
            Manager manager = Manager.createWithFullTime(
                    "Anna", "Kowalska", LocalDate.of(1978, 3, 15),
                    warsawCenter, "+48 600 100 100", "anna.kowalska@clinic.pl",
                    "Clinic Manager", 120f, "PL10 1090 0043 0000 0710 8100 0001",
                    "anna", encodedPassword, "DOC123456",
                    SeniorityLevel.SENIOR, 1.5f);
            manager.addWorkRecord(5, 2026, 160f);
            employeeRepository.save(manager);

            // --- Receptionist ---
            Receptionist receptionist = Receptionist.createWithPartTime(
                    "Katarzyna", "Zielińska", LocalDate.of(1992, 7, 20),
                    warsawMokotow, "+48 600 200 200", "katarzyna.zielinska@clinic.pl",
                    "Receptionist", 45f, "PL10 1090 0043 0000 0710 8100 0002",
                    "katarzyna", encodedPassword, "DOC234567",
                    List.of("Polish", "English"),
                    Shift.MORNING, 30);
            receptionist.addWorkRecord(5, 2026, 120f);
            employeeRepository.save(receptionist);

            // --- Doctors ---
            LocalDate scheduleFrom = LocalDate.of(2026, 5, 26);
            LocalDate scheduleTo = LocalDate.of(2026, 6, 30);
            LocalTime dayStart = LocalTime.of(8, 0);
            LocalTime dayEnd = LocalTime.of(16, 0);

            Doctor drNowak = Doctor.createWithFullTime(
                    "Jan", "Nowak", LocalDate.of(1975, 4, 10),
                    warsawCenter, "+48 600 300 100", "jan.nowak@clinic.pl",
                    "Cardiologist", 100f, "PL10 1090 0043 0000 0710 8100 0003",
                    "jan", encodedPassword, "LIC100001",
                    "Cardiology", "PWZ/100001", 1.5f);
            drNowak.addSchedule(scheduleFrom, scheduleTo, dayStart, dayEnd);
            drNowak.addWorkRecord(5, 2026, 160f);
            doctorRepository.save(drNowak);

            Doctor drWisniewska = Doctor.createWithPartTime(
                    "Maria", "Wiśniewska", LocalDate.of(1983, 9, 5),
                    krakow, "+48 600 300 200", "maria.wisniewska@clinic.pl",
                    "GP", 80f, "PL10 1090 0043 0000 0710 8100 0004",
                    "maria", encodedPassword, "LIC100002",
                    "General Practice", "PWZ/100002", Shift.MORNING, 20);
            drWisniewska.addSchedule(scheduleFrom, scheduleTo, LocalTime.of(8, 0), LocalTime.of(12, 0));
            drWisniewska.addWorkRecord(5, 2026, 80f);
            doctorRepository.save(drWisniewska);

            Doctor drKowalczyk = Doctor.createWithFullTime(
                    "Ewa", "Kowalczyk", LocalDate.of(1980, 1, 22),
                    gdansk, "+48 600 300 300", "ewa.kowalczyk@clinic.pl",
                    "Gynaecologist", 95f, "PL10 1090 0043 0000 0710 8100 0005",
                    "ewa", encodedPassword, "LIC100003",
                    "Gynaecology", "PWZ/100003", 1.5f);
            drKowalczyk.addSchedule(scheduleFrom, scheduleTo, dayStart, dayEnd);
            drKowalczyk.addWorkRecord(5, 2026, 160f);
            doctorRepository.save(drKowalczyk);

            Doctor drDabrowska = Doctor.createWithFullTime(
                    "Joanna", "Dąbrowska", LocalDate.of(1987, 6, 14),
                    warsawMokotow, "+48 600 300 400", "joanna.dabrowska@clinic.pl",
                    "Gynaecologist", 95f, "PL10 1090 0043 0000 0710 8100 0006",
                    "joanna", encodedPassword, "LIC100004",
                    "Gynaecology", "PWZ/100004", 1.5f);
            drDabrowska.addSchedule(scheduleFrom, scheduleTo, dayStart, dayEnd);
            drDabrowska.addWorkRecord(5, 2026, 160f);
            doctorRepository.save(drDabrowska);

            Doctor drWojcik = Doctor.createWithFullTime(
                    "Tomasz", "Wójcik", LocalDate.of(1979, 11, 30),
                    poznan, "+48 600 300 500", "tomasz.wojcik@clinic.pl",
                    "Dermatologist", 90f, "PL10 1090 0043 0000 0710 8100 0007",
                    "tomasz", encodedPassword, "LIC100005",
                    "Dermatology", "PWZ/100005", 1.5f);
            drWojcik.addSchedule(scheduleFrom, scheduleTo, dayStart, dayEnd);
            drWojcik.addWorkRecord(5, 2026, 160f);
            doctorRepository.save(drWojcik);

            Doctor drLewandowska = Doctor.createWithPartTime(
                    "Agnieszka", "Lewandowska", LocalDate.of(1985, 3, 8),
                    lodz, "+48 600 300 600", "agnieszka.lewandowska@clinic.pl",
                    "Dermatologist", 90f, "PL10 1090 0043 0000 0710 8100 0008",
                    "agnieszka.l", encodedPassword, "LIC100006",
                    "Dermatology", "PWZ/100006", Shift.AFTERNOON, 30);
            drLewandowska.addSchedule(scheduleFrom, scheduleTo, LocalTime.of(12, 0), LocalTime.of(18, 0));
            drLewandowska.addWorkRecord(5, 2026, 120f);
            doctorRepository.save(drLewandowska);

            // --- Patients ---
            Patient piotr = new Patient(
                    "Piotr", "Malinowski", LocalDate.of(1990, 2, 15),
                    warsawCenter, "+48 500 100 100", "piotr.malinowski@email.com",
                    "85021512345");
            piotr.setLogin("piotr");
            piotr.setPassword(encodedPassword);
            patientRepository.save(piotr);

            Patient agnieszka = new Patient(
                    "Agnieszka", "Wójcik", LocalDate.of(1995, 8, 3),
                    krakow, "+48 500 200 200", "agnieszka.wojcik@email.com",
                    "95080367890");
            agnieszka.setLogin("agnieszka");
            agnieszka.setPassword(encodedPassword);
            patientRepository.save(agnieszka);

            Patient tomek = new Patient(
                    "Tomasz", "Kowalczyk", LocalDate.of(2012, 4, 18),
                    gdansk, "+48 500 300 300", "tomasz.parent@email.com",
                    "12041898765");
            patientRepository.save(tomek);

            // --- Medical Records ---
            MedicalRecord piotrRecord = new MedicalRecord(piotr);
            piotrRecord.updateMedicalRecord(82f, "120/80");
            piotrRecord.addAllergy("Penicillin");
            piotrRecord.addChronicCondition("Hypertension");
            piotrRecord.addEntry(MedicalEntryType.VISIT_SUMMARY,
                    "Routine cardiology check", "Mild hypertension", "Monitor blood pressure weekly");
            medicalRecordRepository.save(piotrRecord);

            MedicalRecord agnieszkaRecord = new MedicalRecord(agnieszka);
            agnieszkaRecord.updateMedicalRecord(65f, "110/70");
            agnieszkaRecord.addAllergy("Latex");
            agnieszkaRecord.addPastSurgery("Appendectomy 2018");
            agnieszkaRecord.addEntry(MedicalEntryType.TEST_RESULTS,
                    "Annual blood panel", "All values within normal range", "No follow-up required");
            agnieszkaRecord.addEntry(MedicalEntryType.PRESCRIPTION_NOTE,
                    "Skin irritation", "Contact dermatitis", "Prescribed hydrocortisone cream 1%");
            medicalRecordRepository.save(agnieszkaRecord);

            // --- Past appointments (for testing Complete / No-show) ---
            Appointment pastApt1 = Appointment.forSeeding(piotr, drNowak,
                    LocalDateTime.of(2026, 5, 20, 10, 0), "Room 101", 30f);
            pastApt1.confirm();
            appointmentRepository.save(pastApt1);

            Appointment pastApt2 = Appointment.forSeeding(agnieszka, drWojcik,
                    LocalDateTime.of(2026, 5, 21, 14, 0), "Room 204", 30f);
            pastApt2.confirm();
            pastApt2.markPatientsNoShow();
            appointmentRepository.save(pastApt2);

            Appointment pastApt3 = Appointment.forSeeding(tomek, drKowalczyk,
                    LocalDateTime.of(2026, 5, 22, 9, 0), "Room 301", 30f);
            pastApt3.confirm();
            pastApt3.markAsCompleted();
            appointmentRepository.save(pastApt3);

            Appointment pastApt4 = Appointment.forSeeding(agnieszka, drNowak,
                    LocalDateTime.of(2026, 5, 23, 11, 0), "Room 102", 30f);
            appointmentRepository.save(pastApt4);

            // --- Future appointments ---
            Appointment apt1 = new Appointment(piotr, drNowak,
                    LocalDateTime.of(2026, 5, 29, 10, 0), "Room 101", 30f);
            apt1.confirm();
            appointmentRepository.save(apt1);

            Appointment apt2 = new Appointment(agnieszka, drWojcik,
                    LocalDateTime.of(2026, 5, 29, 14, 0), "Room 204", 30f);
            appointmentRepository.save(apt2);

            Appointment apt3 = new Appointment(piotr, drLewandowska,
                    LocalDateTime.of(2026, 6, 2, 9, 0), "Room 205", 30f);
            appointmentRepository.save(apt3);

            Appointment apt4 = new Appointment(agnieszka, drKowalczyk,
                    LocalDateTime.of(2026, 6, 3, 11, 0), "Room 301", 45f);
            apt4.cancel();
            appointmentRepository.save(apt4);

            Appointment apt5 = new Appointment(piotr, drDabrowska,
                    LocalDateTime.of(2026, 6, 5, 15, 30), "Room 302", 30f);
            appointmentRepository.save(apt5);
        };
    }
}
