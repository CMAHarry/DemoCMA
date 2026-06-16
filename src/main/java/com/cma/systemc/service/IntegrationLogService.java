package com.cma.systemc.service;

import com.cma.systemc.entity.IntegrationLog;
import com.cma.systemc.repository.IntegrationLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IntegrationLogService {

    private static final Logger logger = LogManager.getLogger(IntegrationLogService.class);

    private final IntegrationLogRepository repository;
    private final ObjectMapper objectMapper;

    public IntegrationLogService(IntegrationLogRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * Save integration request/response log.
     *
     * This method intentionally catches its own exception because integration logging
     * should not break the main work order API flow. For example, if the log table
     * column is too small or unavailable, the work order should still be created.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String sourceSystem, String messageType, String referenceNo, Object request, Object response, String status) {
        try {
            IntegrationLog log = new IntegrationLog();
            log.setSourceSystem(sourceSystem);
            log.setMessageType(messageType);
            log.setReferenceNo(referenceNo);
            log.setRequestPayload(toJson(request));
            log.setResponsePayload(toJson(response));
            log.setStatus(status);

            repository.save(log);
        } catch (Exception ex) {
            logger.error("Failed to save integration log. sourceSystem={}, messageType={}, referenceNo={}, status={}",
                    sourceSystem, messageType, referenceNo, status, ex);
        }
    }

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            logger.warn("Failed to convert object to JSON. Using String.valueOf fallback.", ex);
            return String.valueOf(value);
        }
    }
}
