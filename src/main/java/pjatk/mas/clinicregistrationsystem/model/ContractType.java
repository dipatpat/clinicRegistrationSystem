package pjatk.mas.clinicregistrationsystem.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "contract_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ContractType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    ContractType() {}

    public abstract float calculateVacationEntitlement();

    public Long getId() { return id; }
}
