package hiberspring.domain.dtos.employees_import;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeImportRootDto {

    @XmlElement(name = "employee")
    private List<EmployeeImportDto> employeeImportDtos;

    public EmployeeImportRootDto() {
    }

    public List<EmployeeImportDto> getEmployeeImportDtos() {
        return employeeImportDtos;
    }

    public void setEmployeeImportDtos(List<EmployeeImportDto> employeeImportDtos) {
        this.employeeImportDtos = employeeImportDtos;
    }
}
