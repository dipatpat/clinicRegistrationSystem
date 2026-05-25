package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(nullable = false)
    private String streetName;

    @Column(nullable = false)
    private String streetNumber;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String cityName;

    private String apartmentNumber;

    protected Address() {}

    public Address(String streetName, String streetNumber, String postalCode, String cityName, String apartmentNumber) {
        if (streetName == null || streetName.isBlank()) throw new IllegalArgumentException("Street name cannot be blank");
        if (streetNumber == null || streetNumber.isBlank()) throw new IllegalArgumentException("Street number cannot be blank");
        if (postalCode == null || postalCode.isBlank()) throw new IllegalArgumentException("Postal code cannot be blank");
        if (cityName == null || cityName.isBlank()) throw new IllegalArgumentException("City name cannot be blank");
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.cityName = cityName;
        this.apartmentNumber = apartmentNumber;
    }

    public String getStreetName() { return streetName; }
    public String getStreetNumber() { return streetNumber; }
    public String getPostalCode() { return postalCode; }
    public String getCityName() { return cityName; }
    public String getApartmentNumber() { return apartmentNumber; }

    @Override
    public String toString() {
        String base = streetName + " " + streetNumber;
        if (apartmentNumber != null && !apartmentNumber.isBlank()) base += "/" + apartmentNumber;
        return base + ", " + postalCode + " " + cityName;
    }
}
