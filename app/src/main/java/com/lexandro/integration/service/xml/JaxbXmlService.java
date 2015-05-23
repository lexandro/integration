package com.lexandro.integration.service.xml;

import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@Service
public class JaxbXmlService implements XmlService {

    @Override
    public <T> T toObject(String xml, Class<T> resultClass) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(resultClass);
        StringReader reader = new StringReader(xml);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object result = unmarshaller.unmarshal(reader);
        return (T) result;
    }
}
