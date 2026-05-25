package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import pjatk.mas.clinicregistrationsystem.model.enums.Shift;

@Entity
@DiscriminatorValue("PART_TIME")
public class PartTime extends ContractType {

    @Enumerated(EnumType.STRING)
    private Shift shift;

    private int hoursInContract;

    PartTime() {}

    private PartTime(Shift shift, int hoursInContract) {
        if (shift == null) throw new IllegalArgumentException("Shift cannot be null");
        if (hoursInContract <= 0 || hoursInContract >= FullTime.HOURS_IN_CONTRACT)
            throw new IllegalArgumentException("Hours in contract must be between 1 and 39");
        this.shift = shift;
        this.hoursInContract = hoursInContract;
    }

    public static PartTime create(Shift shift, int hoursInContract) {
        return new PartTime(shift, hoursInContract);
    }

    @Override
    public float calculateVacationEntitlement() {
        return ((float) hoursInContract / FullTime.HOURS_IN_CONTRACT) * Employee.VACATION_DAYS_BASE;
    }

    @Transient
    public float getVacationEntitlement() {
        return calculateVacationEntitlement();
    }

    public Shift getShift() { return shift; }
    public int getHoursInContract() { return hoursInContract; }

    public void setShift(Shift shift) {
        if (shift == null) throw new IllegalArgumentException("Shift cannot be null");
        this.shift = shift;
    }

    public void setHoursInContract(int hoursInContract) {
        if (hoursInContract <= 0 || hoursInContract >= FullTime.HOURS_IN_CONTRACT)
            throw new IllegalArgumentException("Hours in contract must be between 1 and 39");
        this.hoursInContract = hoursInContract;
    }
}
