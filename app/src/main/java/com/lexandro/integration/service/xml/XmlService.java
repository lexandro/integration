package com.lexandro.integration.service.xml;

import javax.xml.bind.JAXBException;

public interface XmlService {

    <T> T toObject(String xml, Class<T> resultClass) throws JAXBException;
}
