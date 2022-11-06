package net.galacticprojects.spigot.config.impl.json;

import com.syntaxphoenix.syntaxapi.json.JsonArray;
import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import net.galacticprojects.spigot.config.impl.AbstractSection;
import net.galacticprojects.spigot.config.impl.AbstractSectionList;

import java.util.List;

public class JsonSectionList extends AbstractSectionList<JsonValue<?>, ValueType> {

    private final JsonArray data;

    public JsonSectionList(final AbstractSection<JsonValue<?>, ValueType> parent, String name) {
        super(parent, name);
        if (parent.has(name, ValueType.ARRAY)) {
            this.data = (JsonArray) parent.get(name);
            loadData();
            return;
        }
        this.data = new JsonArray();
        parent.set(name, data);
    }

    private void loadData() {
        final AbstractSection<JsonValue<?>, ValueType> parent = (AbstractSection<JsonValue<?>, ValueType>) this.parent;
        for (int index = 0; index < data.size(); index++) {
            JsonValue<?> value = data.get(index);
            if (!value.hasType(ValueType.OBJECT)) {
                data.remove(index--);
                continue;
            }
            sections.add(new JsonSection(parent, (JsonObject) value));
        }
    }

    @Override
    protected void clearImpl() {
        data.getValue().clear();
    }

    @Override
    protected void removeImpl(int index) {
        data.remove(index);
    }

    @Override
    protected void swapImpl(int from, int to) {
        List<JsonValue<?>> list = data.getValue();
        JsonValue<?> fromObj = list.get(from);
        JsonValue<?> toObj = list.get(to);
        list.set(to, fromObj);
        list.set(from, toObj);
    }

    @Override
    protected AbstractSection<JsonValue<?>, ValueType> createImpl() {
        JsonObject object = new JsonObject();
        data.add(object);
        return new JsonSection((AbstractSection<JsonValue<?>, ValueType>) parent, object);
    }

}
