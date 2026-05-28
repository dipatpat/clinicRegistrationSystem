package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private String phoneNumber;

    private String emailAddress;

    protected Person() {}

    protected Person(String firstName, String lastName, LocalDate dateOfBirth,
                     Address address, String phoneNumber, String emailAddress) {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name cannot be blank");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Last name cannot be blank");
        if (dateOfBirth == null) throw new IllegalArgumentException("Date of birth cannot be null");
        if (dateOfBirth.isAfter(LocalDate.now())) throw new IllegalArgumentException("Date of birth cannot be in the future");
        if (address == null) throw new IllegalArgumentException("Address cannot be null");
        if (phoneNumber == null || phoneNumber.isBlank()) throw new IllegalArgumentException("Phone number cannot be blank");
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    @Transient
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Address getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmailAddress() { return emailAddress; }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name cannot be blank");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Last name cannot be blank");
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) throw new IllegalArgumentException("Date of birth cannot be null");
        if (dateOfBirth.isAfter(LocalDate.now())) throw new IllegalArgumentException("Date of birth cannot be in the future");
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(Address address) {
        if (address == null) throw new IllegalArgumentException("Address cannot be null");
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) throw new IllegalArgumentException("Phone number cannot be blank");
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
