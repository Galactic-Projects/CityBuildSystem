package net.galacticprojects.spigot.config.impl;

import java.util.ArrayList;

public abstract class AbstractSectionList<C, T> implements ISectionList<C, T> {

    protected final ISection<C, T> parent;
    protected final String name;

    protected final ArrayList<ISection<C, T>> sections = new ArrayList<>();

    public AbstractSectionList(final ISection<C, T> parent, final String name) {
        this.parent = parent;
        this.name = name;
    }

    @Override
    public final ISection<C, T> getParent() {
        return parent;
    }

    @Override
    public final String getName() {
        return name;
    }
    
    @Override
    public int indexOf(ISection<C, T> section) {
        return sections.indexOf(section);
    }

    @Override
    public ISection<C, T> get(int index) {
        if (isInvalid(index)) {
            return null;
        }
        return sections.get(index);
    }

    @Override
    public void swap(int from, int to) {
        if (isInvalid(from) || isInvalid(to)) {
            return;
        }
        ISection<C, T> fromObj = sections.get(from);
        ISection<C, T> toObj = sections.get(to);
        sections.set(to, fromObj);
        sections.set(from, toObj);
        swapImpl(from, to);
    }

    @Override
    public ISection<C, T> remove(int index) {
        if (isInvalid(index)) {
            return null;
        }
        removeImpl(index);
        return sections.remove(index);
    }

    @Override
    public ISection<C, T> create() {
        ISection<C, T> section = createImpl();
        sections.add(section);
        return section;
    }
    
    @Override
    public void clear() {
        sections.clear();
        clearImpl();
    }

    @Override
    public int size() {
        return sections.size();
    }

    /*
     * Internals
     */

    protected abstract ISection<C, T> createImpl();
    
    protected abstract void removeImpl(int index);
    
    protected abstract void swapImpl(int from, int to);
    
    protected abstract void clearImpl();

    /*
     * Helper
     */

    private boolean isInvalid(int index) {
        return index < 0 || index >= sections.size();
    }

}
