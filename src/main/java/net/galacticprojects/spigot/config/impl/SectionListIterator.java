package net.galacticprojects.spigot.config.impl;

import java.util.Iterator;

final class SectionListIterator<C, T> implements Iterator<ISection<C, T>> {

    private final ISectionList<C, T> list;
    private int index = 0;

    public SectionListIterator(final ISectionList<C, T> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public ISection<C, T> next() {
        return list.get(index++);
    }

    @Override
    public void remove() {
        list.remove(index--);
    }

}
