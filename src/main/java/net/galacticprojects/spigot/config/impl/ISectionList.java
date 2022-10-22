package net.galacticprojects.spigot.config.impl;

import java.util.Iterator;

public interface ISectionList<C, T> extends Iterable<ISection<C, T>> {

    ISection<C, T> getParent();

    String getName();

    int size();
    
    void clear();

    int indexOf(ISection<C, T> section);

    ISection<C, T> get(int index);

    ISection<C, T> remove(int index);

    void swap(int from, int to);

    default <P> void create(ISerializer<P> serializer, P value) {
        serializer.serializeList(this, value);
    }
    
    default <P> P get(int index, ISerializer<P> serializer) {
        return serializer.deserializeList(index, this);
    }
    
    default <P> P getOrDefault(int index, ISerializer<P> serializer, P fallback) {
        P value = get(index, serializer);
        if(value == null) {
            return fallback;
        }
        return value;
    }

    ISection<C, T> create();

    @Override
    default Iterator<ISection<C, T>> iterator() {
        return new SectionListIterator<>(this);
    }

}
