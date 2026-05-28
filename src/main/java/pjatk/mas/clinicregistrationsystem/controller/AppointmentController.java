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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    @GetMapping
    public String list(Model model) {
        model.addAttribute("appointments", appointmentService.findAll());
        return "appointments/list";
    }


    @GetMapping("/book")
    public String bookStep1(Model model) {
        model.addAttribute("doctorsBySpecialization", doctorService.getAllGroupedBySpecialization());
        return "appointments/book/doctors";
    }


    @GetMapping("/book/{doctorId}")
    public String bookStep2(@PathVariable Long doctorId,
                            @RequestParam(defaultValue = "30") int duration,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            Model model) {
        Map<LocalDate, List<LocalTime>> slotsByDate = doctorService.getAvailableSlotsByDate(doctorId, duration);
        List<LocalDate> availableDates = new ArrayList<>(slotsByDate.keySet());
        LocalDate selectedDate = (date != null && slotsByDate.containsKey(date)) ? date
                : (availableDates.isEmpty() ? null : availableDates.get(0));

        model.addAttribute("doctor", doctorService.findById(doctorId));
        model.addAttribute("availableDates", availableDates);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("selectedSlots", selectedDate != null ? slotsByDate.get(selectedDate) : List.of());
        model.addAttribute("duration", duration);
        return "appointments/book/slots";
    }


    @GetMapping("/book/{doctorId}/find-patient")
    public String findPatientGet(@PathVariable Long doctorId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                 @RequestParam(defaultValue = "30") int duration,
                                 @RequestParam(required = false) String pesel,
                                 Model model) {
        return doFindPatient(doctorId, dateTime, duration, pesel, model);
    }

    @PostMapping("/book/{doctorId}/find-patient")
    public String findPatient(@PathVariable Long doctorId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                              @RequestParam(defaultValue = "30") int duration,
                              @RequestParam(required = false) String pesel,
                              Model model) {
        return doFindPatient(doctorId, dateTime, duration, pesel, model);
    }

    private String doFindPatient(Long doctorId, LocalDateTime dateTime, int duration, String pesel, Model model) {
        model.addAttribute("doctor", doctorService.findById(doctorId));
        model.addAttribute("selectedDateTime", dateTime);
        model.addAttribute("duration", duration);

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


    @GetMapping("/book/{doctorId}/register-patient")
    public String registerPatientForm(@PathVariable Long doctorId,
                                      @RequestParam String dateTime,
                                      @RequestParam(defaultValue = "30") int duration,
                                      Model model) {
        PatientRegistrationForm form = new PatientRegistrationForm();
        form.setReturnDoctorId(doctorId);
        form.setReturnDateTime(dateTime);
        form.setReturnDuration(duration);
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
                + form.getReturnDateTime() + "&pesel=" + form.getPesel() + "&duration=" + form.getReturnDuration();
    }


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
