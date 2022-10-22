package net.galacticprojects.spigot.message;

import me.lauriichan.laylib.localization.MessageProvider;
import me.lauriichan.laylib.logger.ISimpleLogger;

import java.util.concurrent.ConcurrentHashMap;

public class MessageProviderImpl extends MessageProvider {

    public static boolean DEBUG = false;

    private final ConcurrentHashMap<String, MessageImpl> messages = new ConcurrentHashMap<>();
    private final String fallback;

    private final ISimpleLogger logger;

    public MessageProviderImpl(ISimpleLogger logger, String id, String fallback) {
        super(id);
        this.fallback = fallback;
        this.logger = logger;
    }

    @Override
    public MessageImpl getMessage(String language) {
        MessageImpl message = messages.get(language);
        if (message != null) {
            return message;
        }
        message = new MessageImpl(this, language);
        if (language != null && !language.isBlank()) {
            messages.put(language, message);
        }
        return message;
    }

    public ISimpleLogger getLogger() {
        return logger;
    }

    public String getFallback() {
        return fallback;
    }

    /*
     * Log
     */

    public void log(Level level, Object... objects) {
        switch (level) {
            case DEBUG:
                logger.debug(fallback, objects);
                return;
            case ERROR:
                logger.error(fallback, objects);
                return;
            case TRACK:
                logger.track(fallback, objects);
                return;
            case WARNING:
                logger.warning(fallback, objects);
                return;
            default:
                logger.info(fallback, objects);
                return;
        }
    }

    public void log(Level level, Throwable throwable, Object... objects) {
        switch (level) {
            case DEBUG:
                logger.debug(fallback, throwable, objects);
                return;
            case ERROR:
                logger.error(fallback, throwable, objects);
                return;
            case TRACK:
                logger.track(fallback, throwable, objects);
                return;
            case WARNING:
                logger.warning(fallback, throwable, objects);
                return;
            default:
                logger.info(fallback, throwable, objects);
                return;
        }
    }

    public void logDebug(Level level, Throwable throwable, Object... objects) {
        if (DEBUG) {
            log(level, throwable, objects);
            return;
        }
        log(level, objects);
    }
}
