package hiberspring.service;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.TownImportDto;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TownServiceImpl implements TownService {

    private static final String TOWNS_FILE_PATH = Constants.PATH_TO_FILES + "towns.json";

    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final Gson gson;

    public TownServiceImpl(TownRepository townRepository, FileUtil fileUtil, Gson gson, ValidationUtil validationUtil, ModelMapper mapper) {
        this.townRepository = townRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() != 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return this.fileUtil.readFile(TOWNS_FILE_PATH);
    }

    @Override
    public String importTowns(String townsFileContent) {
        TownImportDto[] townImportDtos
                = this.gson.fromJson(townsFileContent, TownImportDto[].class);

        StringBuilder builder = new StringBuilder();
        for (TownImportDto townImportDto : townImportDtos) {
            if (!this.validationUtil.isValid(townImportDto)) {
                builder.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());

                continue;
            }

            Town town = this.mapper.map(townImportDto, Town.class);
            this.townRepository.saveAndFlush(town);

            builder.append(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, "Town", town.getName()))
                    .append(System.lineSeparator());
        }

        return builder.toString().trim();
    }
}
