package com.lexandro.integration.service.router;

import com.lexandro.integration.model.EventResponse;

import javax.xml.bind.JAXBException;
import java.util.function.Function;

public interface EventProcessorStrategy extends Function<String, Boolean> {

    EventResponse process(String rawXml) throws JAXBException;

}
