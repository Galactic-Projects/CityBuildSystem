package net.galacticprojects.spigot.config.impl;

import java.util.Set;

public interface ISection<C, T> {

    ISection<C, T> getParent();

    ISection<C, T> getRoot();

    Set<String> keys();

    String name();

    void clear();

    boolean has(String path);

    boolean has(String path, T type);

    default boolean has(String path, ISerializer<?> serializer) {
        if (serializer == null) {
            return false;
        }
        return serializer.deserializeSection(path, this) != null;
    }

    boolean hasValue(String path);

    boolean hasValue(String path, Class<?> sample);

    C get(String path);

    C get(String path, T type);

    default <P> P get(String path, ISerializer<P> serializer) {
        if (serializer == null) {
            return null;
        }
        return serializer.deserializeSection(path, this);
    }

    default <P> P getOrDefault(String path, ISerializer<P> serializer, P fallback) {
        P value = get(path, serializer);
        if (value == null) {
            return fallback;
        }
        return value;
    }

    Object getValue(String path);

    <P> P getValue(String path, Class<P> sample);

    <P> P getValueOrDefault(String path, P fallback);

    Number getValueOrDefault(String path, Number fallback);

    boolean isSection(String path);

    ISection<C, T> getSection(String path);

    ISection<C, T> createSection(String path);
    
    boolean isSectionList(String path);
    
    ISectionList<C, T> getSectionList(String path);
    
    ISectionList<C, T> createSectionList(String path);

    void set(String path, C value);

    default <P> void set(String path, ISerializer<P> serializer, P value) {
        if (serializer == null) {
            return;
        }
        serializer.serializeSection(path, this, value);
    }

    void setValue(String path, Object value);

}
