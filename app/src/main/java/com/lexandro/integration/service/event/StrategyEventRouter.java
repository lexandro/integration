package com.lexandro.integration.service.event;

import com.lexandro.integration.model.ErrorCode;
import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.logging.EventLog;
import com.lexandro.integration.repository.EventLoggingRepository;
import com.lexandro.integration.service.event.strategy.EventProcessorStrategy;
import com.lexandro.integration.service.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.util.Optional;

@Service
@Slf4j
public class StrategyEventRouter implements EventRouter {

    @Resource
    private EventLoggingRepository eventLoggingRepository;

    @Resource
    private EventService eventService;

    @Resource
    private EventProcessorStrategyProvider eventProcessorStrategyProvider;

    @Override
    public EventResponse routeEvent(String eventUrl) {
        EventResponse result = new EventResponse();
        result.setSuccess(true);
        //
        String rawEvent;
        try {
            rawEvent = eventService.get(eventUrl);
            storeEvent(eventUrl, rawEvent);
        } catch (EventUnmarshallingException e) {
            result.setSuccess(false);
            result.setMessage("Can't understand Event XML");
            result.setErrorCode(ErrorCode.INVALID_RESPONSE);
            return result;
        } catch (EventReadingException e) {
            result.setSuccess(false);
            result.setMessage("Cant read event unknown event");
            result.setErrorCode(ErrorCode.UNKNOWN_ERROR);
            return result;
        }

        try {
            //
            Assert.hasText(rawEvent);
            //
            Optional<EventProcessorStrategy> eventProcessorStrategy = eventProcessorStrategyProvider.get(rawEvent);
            //
            if (eventProcessorStrategy.isPresent()) {
                result = eventProcessorStrategy.get().process(rawEvent);
            } else {
                result.setSuccess(false);
                result.setMessage("Received unknown event");
                result.setErrorCode(ErrorCode.INVALID_RESPONSE);
            }
        } catch (JAXBException | IllegalArgumentException e) {
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
            result.setErrorCode(ErrorCode.USER_NOT_FOUND);
        } catch (AccountMissingException uee) {
            result.setSuccess(false);
            result.setMessage("Account missing");
            result.setErrorCode(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        log.info("Returning result {}", result);
        return result;
    }

    private void storeEvent(String eventUrl, String rawEvent) {
        // Add logging
        EventLog eventLog = EventLog.builder()
                .url(eventUrl)
                .xml(rawEvent)
                .build();
        eventLog.setXml(rawEvent);
        eventLoggingRepository.save(eventLog);
    }

}
