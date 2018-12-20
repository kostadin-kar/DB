package hiberspring.domain.dtos.products_import;

public interface ExportEmployeeDto {

    String getFirst_Name();

    String getLast_Name();

    String getPosition();

    String getNumber();

    default String makeToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(this.getFirst_Name() + " "+ this.getLast_Name()).append(System.lineSeparator())
                .append("Position: ").append(this.getPosition()).append(System.lineSeparator())
                .append("Card Number: ").append(this.getNumber()).append(System.lineSeparator());

        return builder.toString();
    }
}
