package pjatk.mas.clinicregistrationsystem.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pjatk.mas.clinicregistrationsystem.repository.EmployeeRepository;
import pjatk.mas.clinicregistrationsystem.repository.PatientRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final PatientRepository patientRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository,
                                    PatientRepository patientRepository) {
        this.employeeRepository = employeeRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return employeeRepository.findByLogin(login)
                .<UserDetails>map(ClinicUserDetails::new)
                .orElseGet(() -> patientRepository.findByLogin(login)
                        .map(ClinicUserDetails::new)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login)));
    }
}
