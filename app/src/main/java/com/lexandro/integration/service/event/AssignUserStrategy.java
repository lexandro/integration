package com.lexandro.integration.service.event;

import com.lexandro.integration.model.ApplicationUser;
import com.lexandro.integration.model.AssignUserEvent;
import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.EventType;
import com.lexandro.integration.service.user.UserService;
import com.lexandro.integration.service.xml.XmlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

@Service
@Slf4j
public class AssignUserStrategy implements EventProcessorStrategy {

    @Resource
    private XmlService xmlService;

    @Resource
    private UserService userService;

    @Override
    public Boolean apply(String xmlString) {
        return xmlString.contains(EventType.USER_ASSIGNMENT.toString());
    }

    @Override
    public EventResponse process(String rawXml) throws JAXBException {
        AssignUserEvent userEvent = xmlService.toObject(rawXml, AssignUserEvent.class);
        //
        ApplicationUser applicationUser = userService.assign(userEvent);
        //
        EventResponse result = new EventResponse();
        result.setSuccess(true);
        result.setAccountIdentifier(applicationUser.getId());
        result.setMessage("User assigned");
        //
        log.info("User assigned: {}", result);
        return result;
    }
}
