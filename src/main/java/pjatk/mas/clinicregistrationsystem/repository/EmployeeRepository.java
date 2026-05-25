package pjatk.mas.clinicregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.mas.clinicregistrationsystem.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByLogin(String login);
    Optional<Employee> findByDocumentId(String documentId);
}
