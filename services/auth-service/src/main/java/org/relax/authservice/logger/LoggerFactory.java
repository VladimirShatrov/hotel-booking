package org.relax.authservice.logger;

import org.springframework.stereotype.Component;

@Component
public class LoggerFactory {

    public enum LoggerType {
        USER, AUTH
    }

    public ILogger createLogger(final LoggerType loggerType) {
        return switch (loggerType) {
            case USER -> UserLogger.getInstance();
            case AUTH -> AuthLogger.getInstance();
            default -> throw new IllegalArgumentException("Unknown logger type: " + loggerType);
        };
    }
}
