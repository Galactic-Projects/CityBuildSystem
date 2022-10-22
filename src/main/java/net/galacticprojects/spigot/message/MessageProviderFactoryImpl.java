package net.galacticprojects.spigot.message;

import me.lauriichan.laylib.localization.source.IProviderFactory;
import me.lauriichan.laylib.logger.ISimpleLogger;

public class MessageProviderFactoryImpl implements IProviderFactory {

    private final ISimpleLogger logger;

    public MessageProviderFactoryImpl(final ISimpleLogger logger) {
        this.logger = logger;
    }

    @Override
    public MessageProviderImpl build(String id, String fallback) {
        return new MessageProviderImpl(logger, id, fallback);
    }

}
