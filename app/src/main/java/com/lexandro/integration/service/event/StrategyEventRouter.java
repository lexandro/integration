package com.lexandro.integration.service.event;

import com.lexandro.integration.model.ErrorCode;
import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.repository.EventLoggingRepository;
import com.lexandro.integration.service.event.strategy.EventProcessorStrategy;
import com.lexandro.integration.service.exception.AccountMissingException;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.exception.UserMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        String rawEvent = eventService.get(eventUrl);
        // Add logging
        eventLoggingRepository.save(rawEvent);
        //
        EventResponse result = new EventResponse();
        result.setSuccess(true);
        //
        Optional<EventProcessorStrategy> eventProcessorStrategy = eventProcessorStrategyProvider.get(rawEvent);
        try {
            if (eventProcessorStrategy.isPresent()) {
                eventProcessorStrategy.get().process(rawEvent);
            } else {
                result.setSuccess(false);
                result.setMessage("Received unknown event");
                result.setErrorCode(ErrorCode.INVALID_RESPONSE);
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
            result.setErrorCode(ErrorCode.USER_NOT_FOUND);
        } catch (AccountMissingException uee) {
            result.setSuccess(false);
            result.setMessage("Account missing");
            result.setErrorCode(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return result;
    }

}
