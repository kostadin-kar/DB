package hiberspring.domain.dtos.products_import;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductImportRootDto {

    @XmlElement(name = "product")
    private List<ProductImportDto> productImportDtos;

    public ProductImportRootDto() {
    }

    public List<ProductImportDto> getProductImportDtos() {
        return productImportDtos;
    }

    public void setProductImportDtos(List<ProductImportDto> productImportDtos) {
        this.productImportDtos = productImportDtos;
    }
}
