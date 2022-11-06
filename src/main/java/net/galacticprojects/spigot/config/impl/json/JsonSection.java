package net.galacticprojects.spigot.config.impl.json;

import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import net.galacticprojects.spigot.config.impl.AbstractSection;
import net.galacticprojects.spigot.config.impl.ConfigHelper;
import net.galacticprojects.spigot.config.impl.ISectionList;

import java.util.Arrays;
import java.util.Collection;

public class JsonSection extends AbstractSection<JsonValue<?>, ValueType> {

    private final JsonObject data;

    public JsonSection(final AbstractSection<JsonValue<?>, ValueType> parent, JsonObject data) {
        super(parent, null);
        this.data = data;
    }

    public JsonSection(final AbstractSection<JsonValue<?>, ValueType> parent, final String name) {
        super(parent, name);
        if (parent.has(name, ValueType.OBJECT)) {
            this.data = (JsonObject) parent.get(name);
            return;
        }
        this.data = new JsonObject();
        parent.set(name, data);
    }

    @Override
    protected void clearImpl() {
        for (final String key : data.keys()) {
            data.set(key, null);
        }
    }

    @Override
    protected Collection<String> keysImpl() {
        return Arrays.asList(data.keys());
    }

    @Override
    protected void setImpl(final String key, final JsonValue<?> value) {
        data.set(key, value);
    }

    @Override
    protected void setValueImpl(final String key, final Object value) {
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
