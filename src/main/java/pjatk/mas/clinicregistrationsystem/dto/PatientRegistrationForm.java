package pjatk.mas.clinicregistrationsystem.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PatientRegistrationForm {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @NotBlank(message = "PESEL is required")
    @Size(min = 11, max = 11, message = "PESEL must be exactly 11 digits")
    @Pattern(regexp = "\\d{11}", message = "PESEL must contain only digits")
    private String pesel;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Email(message = "Email address is not valid")
    private String emailAddress;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Street name is required")
    private String streetName;

    @NotBlank(message = "Street number is required")
    private String streetNumber;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Postal code must be in format XX-XXX")
    private String postalCode;

    @NotBlank(message = "City is required")
    private String cityName;

    private String apartmentNumber;

    private Long returnDoctorId;
    private String returnDateTime;
    private int returnDuration = 30;

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getPesel() { return pesel; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmailAddress() { return emailAddress; }
    public String getCountry() { return country; }
    public String getStreetName() { return streetName; }
    public String getStreetNumber() { return streetNumber; }
    public String getPostalCode() { return postalCode; }
    public String getCityName() { return cityName; }
    public String getApartmentNumber() { return apartmentNumber; }
    public Long getReturnDoctorId() { return returnDoctorId; }
    public String getReturnDateTime() { return returnDateTime; }
    public int getReturnDuration() { return returnDuration; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setPesel(String pesel) { this.pesel = pesel; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public void setCountry(String country) { this.country = country; }
    public void setStreetName(String streetName) { this.streetName = streetName; }
    public void setStreetNumber(String streetNumber) { this.streetNumber = streetNumber; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public void setReturnDoctorId(Long returnDoctorId) { this.returnDoctorId = returnDoctorId; }
    public void setReturnDateTime(String returnDateTime) { this.returnDateTime = returnDateTime; }
    public void setReturnDuration(int returnDuration) { this.returnDuration = returnDuration; }
}
