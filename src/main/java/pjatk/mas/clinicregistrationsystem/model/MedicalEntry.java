package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;
import pjatk.mas.clinicregistrationsystem.model.enums.MedicalEntryType;

import java.time.LocalDateTime;

@Entity
@Table(name = "MedicalEntry")
public class MedicalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateRecorded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicalEntryType type;

    @Column(nullable = false)
    private String description;

    private String diagnosis;
    private String clinicalNotes;

    @ManyToOne
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    MedicalEntry() {}

    MedicalEntry(MedicalRecord medicalRecord, MedicalEntryType type, String description,
                 String diagnosis, String clinicalNotes) {
        if (medicalRecord == null) throw new IllegalArgumentException("Medical record cannot be null");
        if (type == null) throw new IllegalArgumentException("Entry type cannot be null");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description cannot be blank");
        this.medicalRecord = medicalRecord;
        this.dateRecorded = LocalDateTime.now();
        this.type = type;
        this.description = description;
        this.diagnosis = diagnosis;
        this.clinicalNotes = clinicalNotes;
    }

    public Long getId() { return id; }
    public LocalDateTime getDateRecorded() { return dateRecorded; }
    public MedicalEntryType getType() { return type; }
    public String getDescription() { return description; }
    public String getDiagnosis() { return diagnosis; }
    public String getClinicalNotes() { return clinicalNotes; }
    public MedicalRecord getMedicalRecord() { return medicalRecord; }
}
