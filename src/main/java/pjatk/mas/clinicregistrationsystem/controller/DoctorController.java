package pjatk.mas.clinicregistrationsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pjatk.mas.clinicregistrationsystem.service.DoctorService;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/{id}/appointments")
    public String viewAppointments(@PathVariable Long id, Model model) {
        model.addAttribute("doctor", doctorService.findById(id));
        model.addAttribute("appointments", doctorService.getAppointments(id));
        return "doctors/appointments";
    }
}
