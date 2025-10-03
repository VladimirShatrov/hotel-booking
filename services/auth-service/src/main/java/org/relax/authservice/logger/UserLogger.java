package org.relax.authservice.logger;

public class UserLogger implements ILogger {
    private static volatile UserLogger instance;

    private UserLogger() {}

    public static UserLogger getInstance() {
        if (instance == null) {
            synchronized (UserLogger.class) {
                if (instance == null) {
                    instance = new UserLogger();
                }
            }
        }
        return instance;
    }

    @Override
    public void log(String message) {
        System.out.println("User log message: " + message);
    }
}
