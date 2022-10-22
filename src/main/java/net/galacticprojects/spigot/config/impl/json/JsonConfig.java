package net.galacticprojects.spigot.config.impl.json;

import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.json.io.JsonParser;
import com.syntaxphoenix.syntaxapi.json.io.JsonWriter;
import net.galacticprojects.lobbysystem.config.impl.AbstractSection;
import net.galacticprojects.lobbysystem.config.impl.ConfigHelper;
import net.galacticprojects.lobbysystem.config.impl.IConfiguration;
import net.galacticprojects.lobbysystem.config.impl.ISectionList;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

public class JsonConfig extends AbstractSection<JsonValue<?>, ValueType> implements IConfiguration<JsonValue<?>, ValueType> {

    public static final JsonWriter WRITER = new JsonWriter().setPretty(true).setSpaces(true).setIndent(4);
    public static final JsonParser PARSER = new JsonParser();

    private JsonObject data = new JsonObject();

    public JsonConfig() {
        super(null, "");
    }

    @Override
    public void load(final File file) throws Throwable {
        sectionMap.clear();
        try {
            final JsonValue<?> value = PARSER.fromFile(file);
            if (value == null || !value.hasType(ValueType.OBJECT)) {
                data = new JsonObject();
                return;
            }
            data = (JsonObject) value;
        } catch (final Throwable throwable) {
            data = new JsonObject();
            throw throwable;
        }
    }

    @Override
    public void save(final File file) throws Throwable {
        WRITER.toFile(data, file);
    }

    @Override
    protected void clearImpl() {
        for (final String key : data.keys()) {
            data.remove(key);
        }
    }

    @Override
    protected Collection<String> keysImpl() {
        return Arrays.asList(data.keys());
    }

    @Override
    protected void setImpl(final String key, final JsonValue<?> value) {
        if (value == null) {
            data.remove(key);
            return;
        }
        data.set(key, value);
    }

    @Override
    protected void setValueImpl(final String key, final Object value) {
        if (value == null) {
            data.remove(key);
            return;
        }
        data.set(key, value);
    }

    @Override
    protected JsonValue<?> getImpl(final String key) {
        return data.get(key);
    }

    @Override
    protected JsonValue<?> getImpl(final String key, final ValueType type) {
        final JsonValue<?> value = data.get(key);
        if (value == null || !value.hasType(type)) {
            return null;
        }
        return value;
    }

    @Override
    protected Object getValueImpl(final JsonValue<?> value) {
        return value.getValue();
    }

    @Override
    protected boolean hasImpl(final String key) {
        return data.has(key);
    }

    @Override
    protected boolean hasImpl(final String key, final ValueType type) {
        return data.has(key, type);
    }

    @Override
    public AbstractSection<JsonValue<?>, ValueType> getSection(final String[] path, final boolean createIfNotThere) {
        if (path.length == 0) {
            return this;
        }
        AbstractSection<JsonValue<?>, ValueType> section = sectionMap.get(path[0]);
        if (section == null) {
            if (!(hasImpl(path[0], ValueType.OBJECT) || createIfNotThere)) {
                return null;
            }
            section = createInstance(path[0]);
            sectionMap.put(path[0], section);
        }
        return section.getSection(ConfigHelper.getNextKeys(path), createIfNotThere);
    }

    @Override
    public ISectionList<JsonValue<?>, ValueType> getSectionList(final String[] path, final boolean createIfNotThere) {
        if (path.length == 0) {
            throw new IllegalArgumentException("Can't replace own section (path can't be of zero length)");
        }
        String[] next = ConfigHelper.getNextKeys(path);
        if (next.length != 0) {
            AbstractSection<JsonValue<?>, ValueType> section = sectionMap.get(path[0]);
            if (section == null) {
                if (!(hasImpl(path[0], ValueType.OBJECT) || createIfNotThere)) {
                    return null;
                }
                onSectionRemove(path[0]);
                section = createInstance(path[0]);
                sectionMap.put(path[0], section);
            }
            return section.getSectionList(next, createIfNotThere);
        }
        ISectionList<JsonValue<?>, ValueType> sectionList = sectionListMap.get(path[0]);
        if (sectionList == null) {
            if (!(hasImpl(path[0], ValueType.ARRAY) || createIfNotThere)) {
                return null;
            }
            onSectionListRemove(path[0]);
            sectionList = createListInstance(path[0]);
            sectionListMap.put(path[0], sectionList);
        }
        return sectionList;
    }

    @Override
    protected AbstractSection<JsonValue<?>, ValueType> createInstance(final String name) {
        return new JsonSection(this, name);
    }

    @Override
    protected ISectionList<JsonValue<?>, ValueType> createListInstance(final String name) {
        return new JsonSectionList(this, name);
    }

}
