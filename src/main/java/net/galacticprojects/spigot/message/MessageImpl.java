package net.galacticprojects.spigot.message;

import me.lauriichan.laylib.localization.IMessage;

public class MessageImpl implements IMessage {

    private final MessageProviderImpl provider;
    private final String language;

    private String translation;

    public MessageImpl(final MessageProviderImpl provider, final String language) {
        this.provider = provider;
        this.language = language;
    }

    public void translation(String translation) {
        if (translation == null || translation.isBlank()) {
            this.translation = null;
            return;
        }
        this.translation = translation;
    }

    @Override
    public String id() {
        return provider.getId();
    }

    @Override
    public String language() {
        return language;
    }

    @Override
    public String value() {
        if (translation == null) {
            return provider.getFallback();
        }
        return translation;
    }

}
