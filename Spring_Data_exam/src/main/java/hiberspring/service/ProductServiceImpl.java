package hiberspring.service;

import hiberspring.common.Constants;
import hiberspring.domain.dtos.products_import.ProductImportDto;
import hiberspring.domain.dtos.products_import.ProductImportRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.ProductRepository;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCTS_FILE_PATH = Constants.PATH_TO_FILES + "products.xml";

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;

    public ProductServiceImpl(ProductRepository productRepository, BranchRepository branchRepository,
                              FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper mapper, XmlParser xmlParser) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count() != 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return this.fileUtil.readFile(PRODUCTS_FILE_PATH);
    }

    @Override
    public String importProducts() throws JAXBException {
        ProductImportRootDto productImportRootDto
                = this.xmlParser.parseXml(ProductImportRootDto.class, PRODUCTS_FILE_PATH);

        StringBuilder builder = new StringBuilder();
        for (ProductImportDto productImportDto : productImportRootDto.getProductImportDtos()) {
            Branch branch = this.branchRepository.findByName(productImportDto.getBranch()).orElse(null);

            if (!this.validationUtil.isValid(productImportDto) || branch == null) {
                builder.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());

                continue;
            }

            Product product = this.mapper.map(productImportDto, Product.class);
            product.setBranch(branch);
            this.productRepository.saveAndFlush(product);

            builder.append(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, "Product", product.getName()))
                    .append(System.lineSeparator());
        }

        return builder.toString().trim();
    }
}
