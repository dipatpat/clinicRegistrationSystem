package pjatk.mas.clinicregistrationsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.clinicregistrationsystem.dto.AppointmentForm;
import pjatk.mas.clinicregistrationsystem.model.Appointment;
import pjatk.mas.clinicregistrationsystem.model.Doctor;
import pjatk.mas.clinicregistrationsystem.model.Patient;
import pjatk.mas.clinicregistrationsystem.repository.AppointmentRepository;
import pjatk.mas.clinicregistrationsystem.repository.DoctorRepository;
import pjatk.mas.clinicregistrationsystem.repository.PatientRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Transactional
    public Appointment schedule(AppointmentForm form) {
        Patient patient = patientRepository.findByPesel(form.getPatientPesel())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + form.getPatientPesel()));
        Doctor doctor = doctorRepository.findById(form.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + form.getDoctorId()));

        Appointment appointment = new Appointment(
                patient, doctor, form.getDateTime(), form.getRoom(), form.getDuration());
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void confirm(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
        appointment.confirm();
    }

    @Transactional
    public void cancel(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
        appointment.cancel();
    }

    @Transactional
    public void complete(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
        appointment.markAsCompleted();
    }

    @Transactional
    public void noShow(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
        appointment.markPatientsNoShow();
    }
}
