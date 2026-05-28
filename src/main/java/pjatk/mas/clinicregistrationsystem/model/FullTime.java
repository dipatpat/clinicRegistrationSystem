package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FULL_TIME")
public class FullTime extends ContractType {

    public static final int HOURS_IN_CONTRACT = 40;

    private float overtimeRate;

    FullTime() {}

    private FullTime(float overtimeRate) {
        if (overtimeRate < 0) throw new IllegalArgumentException("Overtime rate cannot be negative");
        this.overtimeRate = overtimeRate;
    }

    static FullTime create(float overtimeRate) {
        return new FullTime(overtimeRate);
    }

    @Override
    public float calculateVacationEntitlement() {
        return Employee.VACATION_DAYS_BASE;
    }

    public float getOvertimeRate() { return overtimeRate; }

    public void setOvertimeRate(float overtimeRate) {
        if (overtimeRate < 0) throw new IllegalArgumentException("Overtime rate cannot be negative");
        this.overtimeRate = overtimeRate;
    }
}
