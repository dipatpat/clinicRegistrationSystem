package pjatk.mas.clinicregistrationsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pjatk.mas.clinicregistrationsystem.model.Patient;
import pjatk.mas.clinicregistrationsystem.service.PatientService;

import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private static final String SEARCH_VIEW = "patients/search";

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/search")
    public String searchForm() {
        return SEARCH_VIEW;
    }

    @PostMapping("/search")
    public String search(@RequestParam(required = false) String pesel, Model model) {
        if (pesel != null && !pesel.isBlank()) {
            Optional<Patient> patient = patientService.findByPesel(pesel);
            if (patient.isPresent()) {
                model.addAttribute("patient", patient.get());
                model.addAttribute("appointments", patientService.getAppointments(pesel));
            } else {
                model.addAttribute("patientNotFound", true);
                model.addAttribute("searchedPesel", pesel);
            }
        }
        return SEARCH_VIEW;
    }

    @GetMapping("/{id}/appointments")
    public String viewAppointments(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.findById(id));
        model.addAttribute("appointments", patientService.getAppointmentsById(id));
        return SEARCH_VIEW;
    }
}
