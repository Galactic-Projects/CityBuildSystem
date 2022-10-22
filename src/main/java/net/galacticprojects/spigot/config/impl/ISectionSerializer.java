package net.galacticprojects.spigot.config.impl;

public interface ISectionSerializer<T> extends ISerializer<T> {

    T deserialize(ISection<?, ?> section);

    void serialize(ISection<?, ?> section, T value);

    @Override
    default T deserializeList(int index, ISectionList<?, ?> list) {
        return deserialize(list.get(index));
    }

    @Override
    default T deserializeSection(String path, ISection<?, ?> section) {
        if (!section.isSection(path)) {
            return null;
        }
        return deserialize(section.getSection(path));
    }

    @Override
    default void serializeList(ISectionList<?, ?> list, T value) {
        serialize(list.create(), value);
    }

    @Override
    default void serializeSection(String path, ISection<?, ?> section, T value) {
        serialize(section, value);
    }

}
