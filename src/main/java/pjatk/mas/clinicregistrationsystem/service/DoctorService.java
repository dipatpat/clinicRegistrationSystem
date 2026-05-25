package pjatk.mas.clinicregistrationsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.clinicregistrationsystem.model.Appointment;
import pjatk.mas.clinicregistrationsystem.model.Doctor;
import pjatk.mas.clinicregistrationsystem.model.Schedule;
import pjatk.mas.clinicregistrationsystem.model.enums.Status;
import pjatk.mas.clinicregistrationsystem.repository.AppointmentRepository;
import pjatk.mas.clinicregistrationsystem.repository.DoctorRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Map<String, List<Doctor>> getAllGroupedBySpecialization() {
        return doctorRepository.findAll().stream()
                .collect(Collectors.groupingBy(Doctor::getSpecialization,
                        TreeMap::new, Collectors.toList()));
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + id));
    }

    public List<LocalDateTime> getAvailableSlots(Long doctorId) {
        Doctor doctor = findById(doctorId);

        Set<LocalDateTime> booked = appointmentRepository.findByDoctorId(doctorId).stream()
                .filter(a -> a.getStatus() != Status.CANCELLED)
                .map(Appointment::getDate)
                .collect(Collectors.toSet());

        List<LocalDateTime> slots = new ArrayList<>();
        for (Schedule schedule : doctor.getSchedules()) {
            LocalDate day = schedule.getValidFrom();
            while (!day.isAfter(schedule.getValidTo())) {
                for (LocalTime time : schedule.getAvailableTime(day)) {
                    LocalDateTime slot = LocalDateTime.of(day, time);
                    if (slot.isAfter(LocalDateTime.now()) && !booked.contains(slot)) {
                        slots.add(slot);
                    }
                }
                day = day.plusDays(1);
            }
        }
        Collections.sort(slots);
        return slots;
    }
}
