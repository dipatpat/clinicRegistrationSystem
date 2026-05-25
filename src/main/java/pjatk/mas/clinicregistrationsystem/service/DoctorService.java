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

    public List<Appointment> getAppointments(Long doctorId) {
        Doctor doctor = findById(doctorId);
        return new ArrayList<>(doctor.getAppointments());
    }

    public Map<LocalDate, List<LocalTime>> getAvailableSlotsByDate(Long doctorId, int durationMinutes) {
        Doctor doctor = findById(doctorId);

        List<Appointment> booked = appointmentRepository.findByDoctorId(doctorId).stream()
                .filter(a -> a.getStatus() != Status.CANCELLED)
                .collect(Collectors.toList());

        Map<LocalDate, List<LocalTime>> result = new TreeMap<>();

        for (Schedule schedule : doctor.getSchedules()) {
            LocalDate day = schedule.getValidFrom();
            while (!day.isAfter(schedule.getValidTo())) {
                List<LocalTime> slots = new ArrayList<>();
                LocalTime cursor = schedule.getWorkDayStart();
                while (!cursor.plusMinutes(durationMinutes).isAfter(schedule.getWorkDayEnd())) {
                    LocalDateTime slotStart = LocalDateTime.of(day, cursor);
                    LocalDateTime slotEnd = slotStart.plusMinutes(durationMinutes);
                    boolean free = booked.stream().noneMatch(a -> {
                        LocalDateTime aEnd = a.getDate().plusMinutes((long) a.getDuration());
                        return slotStart.isBefore(aEnd) && slotEnd.isAfter(a.getDate());
                    });
                    if (slotStart.isAfter(LocalDateTime.now()) && free) slots.add(cursor);
                    cursor = cursor.plusMinutes(15);
                }
                if (!slots.isEmpty()) result.put(day, slots);
                day = day.plusDays(1);
            }
        }
        return result;
    }
}
