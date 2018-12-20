package hiberspring.domain.entities;

import javax.persistence.*;

@Entity(name = "Employee")
@Table(name = "employees")
public class Employee extends BaseEntity {
    private String firstName;
    private String lastName;
    private String position;
    private EmployeeCard card;
    private Branch branch;

    public Employee() {
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "position", nullable = false)
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @OneToOne(targetEntity = EmployeeCard.class, optional = false)
    @JoinColumn(name = "card", referencedColumnName = "id", unique = true, nullable = false)
    public EmployeeCard getCard() {
        return card;
    }

    public void setCard(EmployeeCard card) {
        this.card = card;
    }

    @ManyToOne(targetEntity = Branch.class, optional = false)
    @JoinColumn(name = "branch", referencedColumnName = "id", nullable = false)
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @Transient
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(this.fullName()).append(System.lineSeparator())
                .append("Position: ").append(this.position).append(System.lineSeparator())
                .append("Card Number: ").append(this.card.getNumber()).append(System.lineSeparator());

        return builder.toString();
    }

    @Transient
    private String fullName() {
        return this.firstName + " " + this.lastName;
    }
}
