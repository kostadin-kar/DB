package hiberspring.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlParserImpl implements XmlParser {
    @Override
    public <O> O parseXml(Class<O> objectClass, String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(objectClass);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        return objectClass.cast(unmarshaller.unmarshal(new File(filePath)));
    }
}
