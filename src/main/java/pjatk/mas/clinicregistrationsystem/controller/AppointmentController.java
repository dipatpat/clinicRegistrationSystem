package pjatk.mas.clinicregistrationsystem.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.mas.clinicregistrationsystem.dto.AppointmentForm;
import pjatk.mas.clinicregistrationsystem.dto.PatientRegistrationForm;
import pjatk.mas.clinicregistrationsystem.model.Patient;
import pjatk.mas.clinicregistrationsystem.service.AppointmentService;
import pjatk.mas.clinicregistrationsystem.service.DoctorService;
import pjatk.mas.clinicregistrationsystem.service.PatientService;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentController(AppointmentService appointmentService,
                                 DoctorService doctorService,
                                 PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // --- Appointment list ---

    @GetMapping
    public String list(Model model) {
        model.addAttribute("appointments", appointmentService.findAll());
        return "appointments/list";
    }

    // --- Booking flow: Step 1 — doctor list ---

    @GetMapping("/book")
    public String bookStep1(Model model) {
        model.addAttribute("doctorsBySpecialization", doctorService.getAllGroupedBySpecialization());
        return "appointments/book/doctors";
    }

    // --- Booking flow: Step 2 — available slots for selected doctor ---

    @GetMapping("/book/{doctorId}")
    public String bookStep2(@PathVariable Long doctorId, Model model) {
        model.addAttribute("doctor", doctorService.findById(doctorId));
        model.addAttribute("slots", doctorService.getAvailableSlots(doctorId));
        return "appointments/book/slots";
    }

    // --- Booking flow: Step 3 — find patient by PESEL ---

    @PostMapping("/book/{doctorId}/find-patient")
    public String findPatient(@PathVariable Long doctorId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                              @RequestParam(required = false) String pesel,
                              Model model) {
        model.addAttribute("doctor", doctorService.findById(doctorId));
        model.addAttribute("selectedDateTime", dateTime);

        if (pesel != null && !pesel.isBlank()) {
            Optional<Patient> patient = patientService.findByPesel(pesel);
            if (patient.isPresent()) {
                model.addAttribute("patient", patient.get());
            } else {
                model.addAttribute("patientNotFound", true);
                model.addAttribute("searchedPesel", pesel);
            }
        }
        return "appointments/book/patient-search";
    }

    // --- Booking flow: Step 4 — confirm and create appointment ---

    @PostMapping("/book/confirm")
    public String confirm(@ModelAttribute AppointmentForm form,
                          RedirectAttributes redirectAttributes) {
        try {
            appointmentService.schedule(form);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment scheduled successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/appointments";
    }

    // --- Booking flow: Alternative — register new patient inline ---

    @GetMapping("/book/{doctorId}/register-patient")
    public String registerPatientForm(@PathVariable Long doctorId,
                                      @RequestParam String dateTime,
                                      Model model) {
        PatientRegistrationForm form = new PatientRegistrationForm();
        form.setReturnDoctorId(doctorId);
        form.setReturnDateTime(dateTime);
        model.addAttribute("form", form);
        model.addAttribute("doctor", doctorService.findById(doctorId));
        return "appointments/book/patient-register";
    }

    @PostMapping("/book/{doctorId}/register-patient")
    public String registerPatient(@PathVariable Long doctorId,
                                  @Valid @ModelAttribute("form") PatientRegistrationForm form,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctor", doctorService.findById(doctorId));
            return "appointments/book/patient-register";
        }
        try {
            patientService.register(form);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("pesel", "duplicate", e.getMessage());
            model.addAttribute("doctor", doctorService.findById(doctorId));
            return "appointments/book/patient-register";
        }
        return "redirect:/appointments/book/" + doctorId + "/find-patient?dateTime="
                + form.getReturnDateTime() + "&pesel=" + form.getPesel();
    }

    // --- Confirm / Cancel existing appointments ---

    @PostMapping("/{id}/confirm")
    public String confirmAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.confirm(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment confirmed.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.cancel(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment cancelled.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/complete")
    public String completeAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.complete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment marked as completed.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/no-show")
    public String noShowAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.noShow(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment marked as no-show.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/appointments";
    }
}
