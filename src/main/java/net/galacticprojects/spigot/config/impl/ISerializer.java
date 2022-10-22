package net.galacticprojects.spigot.config.impl;

public interface ISerializer<T> {

    T deserializeSection(String path, ISection<?, ?> section);

    void serializeSection(String path, ISection<?, ?> section, T value);
    
    T deserializeList(int index, ISectionList<?, ?> list);
    
    void serializeList(ISectionList<?, ?> list, T value);
    
}
