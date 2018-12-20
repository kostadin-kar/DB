package hiberspring.domain.dtos;

import javax.persistence.Transient;

public class EmployeeExportDto {
    private String firstName;
    private String lastName;
    private String position;
    private String cardNumber;

    public EmployeeExportDto() {
    }

    public EmployeeExportDto(String firstName, String lastName, String position, String cardNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.cardNumber = cardNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(this.fullName()).append(System.lineSeparator())
                .append("Position: ").append(this.position).append(System.lineSeparator())
                .append("Card Number: ").append(this.cardNumber).append(System.lineSeparator());

        return builder.toString();
    }

    private String fullName() {
        return this.firstName + " " + this.lastName;
    }
}
