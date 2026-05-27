package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Receptionist")
@PrimaryKeyJoinColumn(name = "id")
public class Receptionist extends Employee {

    @ElementCollection
    @CollectionTable(name = "Receptionist_languages", joinColumns = @JoinColumn(name = "receptionist_id"))
    @Column(name = "language", nullable = false)
    @Size(min = 1, message = "Receptionist must speak at least one language")
    private List<String> languagesSpoken = new ArrayList<>();

    protected Receptionist() {}

    public Receptionist(String firstName, String lastName, LocalDate dateOfBirth,
                        Address address, String phoneNumber, String emailAddress,
                        String jobTitle, float hourlyRate, String bankAccount,
                        String login, String password, String documentId,
                        List<String> languagesSpoken) {
        super(firstName, lastName, dateOfBirth, address, phoneNumber, emailAddress,
                jobTitle, hourlyRate, bankAccount, login, password, documentId);
        if (languagesSpoken == null || languagesSpoken.isEmpty())
            throw new IllegalArgumentException("Receptionist must speak at least one language");
        this.languagesSpoken = new ArrayList<>(languagesSpoken);
    }

    public static List<String> getSpokenLanguages(List<Receptionist> receptionists) {
        return receptionists.stream()
                .flatMap(r -> r.languagesSpoken.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public void addLanguage(String language) {
        if (language == null || language.isBlank()) throw new IllegalArgumentException("Language cannot be blank");
        if (!languagesSpoken.contains(language)) languagesSpoken.add(language);
    }

    public void removeLanguage(String language) {
        if (languagesSpoken.size() <= 1) throw new IllegalStateException("Receptionist must speak at least one language");
        languagesSpoken.remove(language);
    }

    public void registerPatient() {}
    public void scheduleAppointment() {}
    public void findPatientByPESEL() {}

    public List<String> getLanguagesSpoken() { return Collections.unmodifiableList(languagesSpoken); }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " [" + String.join(", ", languagesSpoken) + "]";
    }
}
