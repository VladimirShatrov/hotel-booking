package org.relax.authservice.logger;

public class AuthLogger implements ILogger {
    private static volatile AuthLogger instance;

    private AuthLogger() {}

    public static AuthLogger getInstance() {
        if (instance == null) {
            synchronized (AuthLogger.class) {
                if (instance == null) {
                    instance = new AuthLogger();
                }
            }
        }
        return instance;
    }

    @Override
    public void log(String message) {
        System.out.println("Auth log massage: " + message);
    }
}
