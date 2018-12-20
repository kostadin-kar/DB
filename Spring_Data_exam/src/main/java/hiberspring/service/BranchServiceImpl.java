package hiberspring.service;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.BranchImportDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.TownRepository;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BranchServiceImpl implements BranchService {
    private static final String BRANCHES_FILE_PATH = Constants.PATH_TO_FILES + "branches.json";

    private final BranchRepository branchRepository;
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final Gson gson;

    public BranchServiceImpl(BranchRepository branchRepository, TownRepository townRepository,
                             FileUtil fileUtil, Gson gson, ValidationUtil validationUtil, ModelMapper mapper) {
        this.branchRepository = branchRepository;
        this.townRepository = townRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() != 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return this.fileUtil.readFile(BRANCHES_FILE_PATH);
    }

    @Override
    public String importBranches(String branchesFileContent) {
        BranchImportDto[] branchImportDtos
                = this.gson.fromJson(branchesFileContent, BranchImportDto[].class);

        StringBuilder builder = new StringBuilder();
        for (BranchImportDto branchImportDto : branchImportDtos) {
            if (!this.validationUtil.isValid(branchImportDto)) {
                builder.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());

                continue;
            }

            Town town = this.townRepository.findByName(branchImportDto.getTown()).orElse(null);
            if (town == null) {
                builder.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());

                continue;
            }

            Branch branch = this.mapper.map(branchImportDto, Branch.class);
            branch.setTown(town);
            this.branchRepository.saveAndFlush(branch);

            builder.append(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, "Branch", branch.getName()))
                    .append(System.lineSeparator());
        }

        return builder.toString().trim();
    }
}
