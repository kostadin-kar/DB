package hiberspring.service;

import hiberspring.common.Constants;
import hiberspring.domain.dtos.employees_import.EmployeeImportDto;
import hiberspring.domain.dtos.employees_import.EmployeeImportRootDto;
import hiberspring.domain.dtos.products_import.ExportEmployeeDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.repository.EmployeeRepository;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final String EMPLOYEES_FILE_PATH = Constants.PATH_TO_FILES + "employees.xml";

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final EmployeeCardRepository employeeCardRepository;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               BranchRepository branchRepository,
                               EmployeeCardRepository employeeCardRepository,
                               FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper mapper, XmlParser xmlParser) {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.employeeCardRepository = employeeCardRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() != 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return this.fileUtil.readFile(EMPLOYEES_FILE_PATH);
    }

    @Override
    public String importEmployees() throws JAXBException {
        EmployeeImportRootDto employeeImportRootDto
                = this.xmlParser.parseXml(EmployeeImportRootDto.class, EMPLOYEES_FILE_PATH);

        StringBuilder builder = new StringBuilder();
        for (EmployeeImportDto employeeImportDto : employeeImportRootDto.getEmployeeImportDtos()) {
            Branch branch
                    = this.branchRepository.findByName(employeeImportDto.getBranch())
                    .orElse(null);

            EmployeeCard employeeCard
                    = this.employeeCardRepository.findByNumber(employeeImportDto.getCard())
                    .orElse(null);

            Employee employee
                    = this.employeeRepository.findByCard(employeeImportDto.getCard())
                    .orElse(null);

            if (!this.validationUtil.isValid(employeeImportDto) || branch == null || employeeCard == null || employee != null) {
                builder.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());

                continue;
            }

            employee = this.mapper.map(employeeImportDto, Employee.class);
            employee.setBranch(branch);
            employee.setCard(employeeCard);
            this.employeeRepository.saveAndFlush(employee);

            builder.append(String.format(
                    Constants.SUCCESSFUL_IMPORT_MESSAGE,
                    "Employee",
                    String.format(
                            "%s %s",
                            employee.getFirstName(),
                            employee.getLastName())))
                    .append(System.lineSeparator());
        }

        return builder.toString().trim();
    }

    @Override
    public String exportProductiveEmployees() {
        List<Employee> employees
                = this.employeeRepository.exportProductiveEmployees();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < employees.size(); i++) {
            builder.append(employees.get(i).toString())
                    .append(i < employees.size() - 1
                            ? "-------------------------"
                            : "")
                    .append(System.lineSeparator());
        }

        return builder.toString().trim();
    }
}
