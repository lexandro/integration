package com.lexandro.integration.service.event;

import com.lexandro.integration.model.ApplicationUser;
import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.EventType;
import com.lexandro.integration.model.UnAssignUserEvent;
import com.lexandro.integration.service.user.UserService;
import com.lexandro.integration.service.xml.XmlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

@Service
@Slf4j
public class UnAssignUserStrategy implements EventProcessorStrategy {

    @Resource
    private XmlService xmlService;

    @Resource
    private UserService userService;

    @Override
    public Boolean apply(String xmlString) {
        return xmlString.contains(EventType.USER_UNASSIGNMENT.toString());
    }

    @Override
    public EventResponse process(String rawXml) throws JAXBException {
        UnAssignUserEvent userEvent = xmlService.toObject(rawXml, UnAssignUserEvent.class);
        //
        ApplicationUser applicationUser = userService.unAssign(userEvent);
        //
        EventResponse result = new EventResponse();
        result.setSuccess(true);
        result.setAccountIdentifier(applicationUser.getId());
        result.setMessage("User unassigned");
        //
        log.info("User unassigned: {}", result);
        return result;
    }
}
