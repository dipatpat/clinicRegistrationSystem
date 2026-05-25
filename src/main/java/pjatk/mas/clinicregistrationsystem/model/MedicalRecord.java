package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;
import pjatk.mas.clinicregistrationsystem.model.enums.MedicalEntryType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "MedicalRecord")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateCreated;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false, unique = true)
    private String patientPesel;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ElementCollection
    @CollectionTable(name = "MedicalRecord_allergies", joinColumns = @JoinColumn(name = "medical_record_id"))
    @Column(name = "allergy")
    private List<String> allergies = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "MedicalRecord_surgeries", joinColumns = @JoinColumn(name = "medical_record_id"))
    @Column(name = "surgery")
    private List<String> pastSurgeries = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "MedicalRecord_conditions", joinColumns = @JoinColumn(name = "medical_record_id"))
    @Column(name = "condition")
    private List<String> chronicConditions = new ArrayList<>();

    private Float weight;
    private String bloodPressure;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalEntry> entries = new ArrayList<>();

    protected MedicalRecord() {}

    public MedicalRecord(Patient patient) {
        if (patient == null) throw new IllegalArgumentException("Patient cannot be null");
        this.patient = patient;
        this.patientPesel = patient.getPesel();
        this.dateCreated = LocalDate.now();
        this.isActive = true;
        patient.setMedicalRecord(this);
    }

    @Transient
    public LocalDateTime getLastUpdated() {
        return entries.stream()
                .map(MedicalEntry::getDateRecorded)
                .max(LocalDateTime::compareTo)
                .orElse(dateCreated.atStartOfDay());
    }

    public MedicalEntry addEntry(MedicalEntryType type, String description,
                                 String diagnosis, String clinicalNotes) {
        MedicalEntry entry = new MedicalEntry(this, type, description, diagnosis, clinicalNotes);
        entries.add(entry);
        return entry;
    }

    public void updateMedicalRecord(Float weight, String bloodPressure) {
        this.weight = weight;
        this.bloodPressure = bloodPressure;
    }

    public Long getId() { return id; }
    public LocalDate getDateCreated() { return dateCreated; }
    public boolean isActive() { return isActive; }
    public String getPatientPesel() { return patientPesel; }
    public Patient getPatient() { return patient; }
    public List<String> getAllergies() { return Collections.unmodifiableList(allergies); }
    public List<String> getPastSurgeries() { return Collections.unmodifiableList(pastSurgeries); }
    public List<String> getChronicConditions() { return Collections.unmodifiableList(chronicConditions); }
    public Float getWeight() { return weight; }
    public String getBloodPressure() { return bloodPressure; }
    public List<MedicalEntry> getEntries() { return Collections.unmodifiableList(entries); }

    public void setActive(boolean active) { isActive = active; }

    public void addAllergy(String allergy) {
        if (allergy == null || allergy.isBlank()) throw new IllegalArgumentException("Allergy cannot be blank");
        if (!allergies.contains(allergy)) allergies.add(allergy);
    }

    public void addPastSurgery(String surgery) {
        if (surgery == null || surgery.isBlank()) throw new IllegalArgumentException("Surgery cannot be blank");
        if (!pastSurgeries.contains(surgery)) pastSurgeries.add(surgery);
    }

    public void addChronicCondition(String condition) {
        if (condition == null || condition.isBlank()) throw new IllegalArgumentException("Condition cannot be blank");
        if (!chronicConditions.contains(condition)) chronicConditions.add(condition);
    }
}
