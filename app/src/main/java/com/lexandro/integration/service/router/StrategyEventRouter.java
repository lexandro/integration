package com.lexandro.integration.service.router;

import com.lexandro.integration.model.ErrorCode;
import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.service.EventService;
import com.lexandro.integration.service.XmlService;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.exception.UserMissingException;
import com.lexandro.integration.service.subscription.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StrategyEventRouter implements EventRouter {

    @Resource
    private EventService eventService;

    @Resource
    private EventProcessorStrategyProvider eventProcessorStrategyProvider;

    @Resource
    private XmlService xmlService;

    @Resource
    private SubscriptionService subscriptionService;

    private List<EventProcessorStrategy> strategyList;

    @Override
    public EventResponse routeEvent(String eventUrl) {
        String rawEvent = eventService.get(eventUrl);
        EventResponse result = new EventResponse();
        result.setSuccess(true);


        Optional<EventProcessorStrategy> eventProcessorStrategy = eventProcessorStrategyProvider.get(rawEvent);

        // consider strategy pattern for routing
        try {
            if (eventProcessorStrategy.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Received unknown event");
                result.setErrorCode(ErrorCode.INVALID_RESPONSE);
            } else {
                eventProcessorStrategy.get().process(rawEvent);
            }

        } catch (JAXBException e) {
            log.error("Error unmarshalling XML event from eventUrl {} error: {}", eventUrl, e);
            result.setSuccess(false);
            result.setMessage("Error reading event data");
            result.setErrorCode(ErrorCode.UNKNOWN_ERROR);
        } catch (UserExistsException uee) {
            result.setSuccess(false);
            result.setMessage("User already exists");
            result.setErrorCode(ErrorCode.USER_ALREADY_EXISTS);
        } catch (UserMissingException uee) {
            result.setSuccess(false);
            result.setMessage("User missing");
            result.setErrorCode(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return result;
    }

}
