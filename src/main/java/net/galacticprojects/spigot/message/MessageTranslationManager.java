package net.galacticprojects.spigot.message;

import com.syntaxphoenix.syntaxapi.json.JsonArray;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.json.value.JsonString;
import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.localization.MessageProvider;
import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.spigot.config.impl.json.JsonConfig;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Objects;

public final class MessageTranslationManager implements FilenameFilter {

    private final ISimpleLogger logger;
    private final MessageManager messageManager;
    private final File directory;
    private final File defaultFile;

    private final ArrayList<String> languages = new ArrayList<>();
    private String[] languageArray;

    public MessageTranslationManager(final MessageManager messageManager, final ISimpleLogger logger, File dataFolder){
        this.logger = logger;
        this.messageManager = messageManager;
        this.directory = new File(dataFolder, "translation");
        this.defaultFile = new File(directory, Actor.DEFAULT_LANGUAGE.replace('-', '_') + ".json");
    }

    public String[] getLanguages() {
        if(languageArray == null){
            return languageArray = languages.toArray(String[]::new);
        }
        return languageArray;
    }

    public final void reload(){
        languageArray = null;
        languages.clear();
        MessageProvider[] providers = messageManager.getProviders();
        if(!directory.exists()){
            directory.mkdirs();
            languages.add(Actor.DEFAULT_LANGUAGE);
            load(defaultFile, Actor.DEFAULT_LANGUAGE, providers);
            return;
        }
        File[] files = directory.listFiles(this);
        for(File file : files) {
            String language = file.getName();
            language = language.substring(0, language.length() - 5).replace('_', '-');
            if(languages.contains(language)){
                logger.warning("Language '{0}' already exists!", language);
                continue;
            }
            load(file, language, providers);
        }
    }

    private final void load(File file, String language, MessageProvider[] providers) {
        JsonConfig config = new JsonConfig();
        try {
            config.load(file);
        } catch (Throwable e) {
            logger.warning("Failed to load translation for language '{0}'", language, e);
        }
        for(MessageProvider providerRaw : providers) {
            if (!(providerRaw instanceof MessageProviderImpl provider)) {
                continue;
            }
            String translation = fromValue(config.get(provider.getId()));
            if (translation == null || translation.isBlank()) {
                config.set(provider.getId(), asValue(provider.getFallback()));
                provider.getMessage(language).translation(null);
                continue;
            }
            provider.getMessage(language).translation(translation);
        }
        try {
            config.save(file);
        } catch (Throwable e) {
            logger.warning("Failed to save translation for language '{0}'", language, e);
        }
    }

    private final String fromValue(JsonValue<?> jsonValue) {
        if (jsonValue == null || (!jsonValue.hasType(ValueType.ARRAY) && jsonValue.hasType(ValueType.JSON))
                || jsonValue.hasType(ValueType.NULL)) {
            return null;
        }
        if (jsonValue instanceof JsonArray array) {
            StringBuilder builder = new StringBuilder();
            for (JsonValue<?> value : array) {
                if (value.hasType(ValueType.JSON)) {
                    continue;
                }
                if (!builder.isEmpty()) {
                    builder.append('\n');
                }
                String string = Objects.toString(value.getValue());
                if (string.isEmpty()) {
                    builder.append(' ');
                    continue;
                }
                builder.append(string);
            }
            return builder.toString();
        }
        return jsonValue.getValue().toString();
    }

    private final JsonValue<?> asValue(String fallback) {
        if (!fallback.contains("\n")) {
            return new JsonString(fallback);
        }
        JsonArray array = new JsonArray();
        String[] lines = fallback.split("\n");
        for (String line : lines) {
            array.add(new JsonString(line));
        }
        return array;
    }

    /**
     * Tests if a specified file should be included in a file list.
     *
     * @param dir  the directory in which the file was found.
     * @param name the name of the file.
     * @return {@code true} if and only if the name should be
     * included in the file list; {@code false} otherwise.
     */
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".json");
    }
}
