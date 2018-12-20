package hiberspring.service;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.EmployeeCardImportDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {
    private static final String EMPLOYEE_CARDS_FILE_PATH = Constants.PATH_TO_FILES + "employee-cards.json";

    private final EmployeeCardRepository employeeCardRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final Gson gson;

    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, FileUtil fileUtil, Gson gson, ValidationUtil validationUtil, ModelMapper mapper) {
        this.employeeCardRepository = employeeCardRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count() != 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return this.fileUtil.readFile(EMPLOYEE_CARDS_FILE_PATH);
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) {
        EmployeeCardImportDto[] employeeCardImportDtos
                = this.gson.fromJson(employeeCardsFileContent, EmployeeCardImportDto[].class);

        StringBuilder builder = new StringBuilder();
        for (EmployeeCardImportDto employeeCardImportDto : employeeCardImportDtos) {

            EmployeeCard employeeCard
                    = this.employeeCardRepository.findByNumber(employeeCardImportDto.getNumber())
                    .orElse(null);

            if (!this.validationUtil.isValid(employeeCardImportDto) || employeeCard != null) {
                builder.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());

                continue;
            }

            employeeCard = this.mapper.map(employeeCardImportDto, EmployeeCard.class);

            this.employeeCardRepository.saveAndFlush(employeeCard);

            builder.append(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, "Employee Card", employeeCard.getNumber()))
                    .append(System.lineSeparator());
        }

        return builder.toString().trim();
    }
}
