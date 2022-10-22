package net.galacticprojects.spigot.config.impl;

public interface IValueSerializer<T> extends ISerializer<T> {

    T deserialize(String path, ISection<?, ?> section);

    void serialize(String path, ISection<?, ?> section, T value);

    @Override
    default T deserializeList(int index, ISectionList<?, ?> list) {
        return deserialize("value", list.get(index));
    }

    @Override
    default T deserializeSection(String path, ISection<?, ?> section) {
        return deserialize(path, section);
    }

    @Override
    default void serializeList(ISectionList<?, ?> list, T value) {
        serialize("value", list.create(), value);
    }

    @Override
    default void serializeSection(String path, ISection<?, ?> section, T value) {
        serialize(path, section, value);
    }

}
