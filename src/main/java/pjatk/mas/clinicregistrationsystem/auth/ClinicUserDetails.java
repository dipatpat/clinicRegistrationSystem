package pjatk.mas.clinicregistrationsystem.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pjatk.mas.clinicregistrationsystem.model.*;

import java.util.Collection;
import java.util.List;

public class ClinicUserDetails implements UserDetails {

    private final String login;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public ClinicUserDetails(Employee employee) {
        this.login = employee.getLogin();
        this.password = employee.getPassword();
        String role;
        if (employee instanceof Doctor) {
            role = "ROLE_DOCTOR";
        } else if (employee instanceof Receptionist) {
            role = "ROLE_RECEPTIONIST";
        } else if (employee instanceof Manager) {
            role = "ROLE_MANAGER";
        } else {
            role = "ROLE_STAFF";
        }
        this.authorities = List.of(new SimpleGrantedAuthority(role));
    }

    public ClinicUserDetails(Patient patient) {
        this.login = patient.getLogin();
        this.password = patient.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_PATIENT"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
