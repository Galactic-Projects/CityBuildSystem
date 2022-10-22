package net.galacticprojects.spigot.config.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSection<C, T> implements ISection<C, T> {

    protected final HashMap<String, ISectionList<C, T>> sectionListMap = new HashMap<>();
    protected final HashMap<String, AbstractSection<C, T>> sectionMap = new HashMap<>();
    protected final AbstractSection<C, T> parent;
    protected final String name;

    public AbstractSection(final AbstractSection<C, T> parent, final String name) {
        this.parent = parent;
        this.name = name;
    }

    /*
     * Implementation
     */

    @Override
    public AbstractSection<C, T> getParent() {
        return parent;
    }

    @Override
    public AbstractSection<C, T> getRoot() {
        if (parent == null) {
            return this;
        }
        return parent;
    }

    @Override
    public void clear() {
        sectionListMap.clear();
        sectionMap.clear();
        clearImpl();
    }

    @Override
    public Set<String> keys() {
        final HashSet<String> set = new HashSet<>();
        set.addAll(sectionListMap.keySet());
        set.addAll(sectionMap.keySet());
        set.addAll(keysImpl());
        return set;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public C get(final String path) {
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        return get(key, ConfigHelper.getKeysWithout(keys, key));
    }

    @Override
    public C get(final String path, final T type) {
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        return get(key, ConfigHelper.getKeysWithout(keys, key), type);
    }

    @Override
    public AbstractSection<C, T> getSection(final String path) {
        return getSection(ConfigHelper.getKeys(path), false);
    }

    @Override
    public boolean isSection(final String path) {
        return getSection(path) != null;
    }

    @Override
    public AbstractSection<C, T> createSection(final String path) {
        return getSection(ConfigHelper.getKeys(path), true);
    }
    
    @Override
    public ISectionList<C, T> getSectionList(String path) {
        return getSectionList(ConfigHelper.getKeys(path), false);
    }
    
    @Override
    public boolean isSectionList(String path) {
        return getSectionList(path) != null;
    }
    
    @Override
    public ISectionList<C, T> createSectionList(String path) {
        return getSectionList(ConfigHelper.getKeys(path), true);
    }

    @Override
    public Object getValue(final String path) {
        final C value = get(path);
        if (value == null) {
            return null;
        }
        return getValueImpl(value);
    }

    @Override
    public <P> P getValue(final String path, final Class<P> sample) {
        final Object value = getValue(path);
        if (value == null || !sample.isAssignableFrom(value.getClass())) {
            return null;
        }
        return sample.cast(value);
    }

    @Override
    public Number getValueOrDefault(final String path, final Number fallback) {
        if (fallback == null) {
            return null;
        }
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        final AbstractSection<C, T> section = getSection(ConfigHelper.getKeysWithout(keys, key), true);
        final C value = section.getImpl(key);
        if (value == null) {
            section.setValueImpl(key, fallback);
            return fallback;
        }
        final Object object = section.getValueImpl(value);
        if (object == null || !Number.class.isAssignableFrom(object.getClass())) {
            section.setValueImpl(key, fallback);
            return fallback;
        }
        return (Number) object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P> P getValueOrDefault(final String path, final P fallback) {
        if (fallback == null) {
            return null;
        }
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        final AbstractSection<C, T> section = getSection(ConfigHelper.getKeysWithout(keys, key), true);
        final C value = section.getImpl(key);
        if (value == null) {
            section.setValueImpl(key, fallback);
            return fallback;
        }
        final Object object = section.getValueImpl(value);
        if (object == null || !fallback.getClass().isAssignableFrom(object.getClass())) {
            section.setValueImpl(key, fallback);
            return fallback;
        }
        return (P) object;
    }

    @Override
    public boolean has(final String path) {
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        final AbstractSection<C, T> section = getSection(ConfigHelper.getKeysWithout(keys, key), false);
        if (section == null) {
            return false;
        }
        return section.hasImpl(key);
    }

    @Override
    public boolean has(final String path, final T type) {
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        final AbstractSection<C, T> section = getSection(ConfigHelper.getKeysWithout(keys, key), false);
        if (section == null) {
            return false;
        }
        return section.hasImpl(key, type);
    }

    @Override
    public void set(final String path, final C value) {
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        set(key, ConfigHelper.getKeysWithout(keys, key), value);
    }

    @Override
    public void setValue(final String path, final Object value) {
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        setValue(key, ConfigHelper.getKeysWithout(keys, key), value);
    }

    @Override
    public boolean hasValue(final String path) {
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        final AbstractSection<C, T> section = getSection(ConfigHelper.getKeysWithout(keys, key), false);
        if (section == null) {
            return false;
        }
        if (sectionMap.containsKey(key)) {
            return true;
        }
        final C value = section.getImpl(key);
        if (value == null) {
            return false;
        }
        return section.getValueImpl(value) != null;
    }

    @Override
    public boolean hasValue(final String path, final Class<?> sample) {
        if (sample == null) {
            return false;
        }
        final String[] keys = ConfigHelper.getKeys(path);
        final String key = ConfigHelper.getLastKey(keys);
        final AbstractSection<C, T> section = getSection(ConfigHelper.getKeysWithout(keys, key), false);
        if (section == null) {
            return false;
        }
        if (sectionMap.containsKey(key)) {
            return true;
        }
        final C value = section.getImpl(key);
        if (value == null) {
            return false;
        }
        final Object object = section.getValueImpl(value);
        if (object == null || !sample.isAssignableFrom(object.getClass())) {
            return false;
        }
        return true;
    }

    /*
     * Internals
     */

    protected void onSectionRemove(final String path) {
        setImpl(path, null);
    }

    protected void onSectionListRemove(final String path) {
        setImpl(path, null);
    }

    public ISectionList<C, T> getSectionList(final String[] path, final boolean createIfNotThere) {
        if (path.length == 0) {
            throw new IllegalArgumentException("Can't replace own section (path can't be of zero length)");
        }
        String[] next = ConfigHelper.getNextKeys(path);
        if (next.length != 0) {
            AbstractSection<C, T> section = sectionMap.get(path[0]);
            if (section == null) {
                if (!createIfNotThere) {
                    return null;
                }
                onSectionRemove(path[0]);
                section = createInstance(path[0]);
                sectionMap.put(path[0], section);
            }
            return section.getSectionList(next, createIfNotThere);
        }
        ISectionList<C, T> sectionList = sectionListMap.get(path[0]);
        if (sectionList == null) {
            if (!createIfNotThere) {
                return null;
            }
            onSectionListRemove(path[0]);
            sectionList = createListInstance(path[0]);
            sectionListMap.put(path[0], sectionList);
        }
        return sectionList;
    }

    public AbstractSection<C, T> getSection(final String[] path, final boolean createIfNotThere) {
        if (path.length == 0) {
            return this;
        }
        AbstractSection<C, T> section = sectionMap.get(path[0]);
        if (section == null) {
            if (!createIfNotThere) {
                return this;
            }
            onSectionRemove(path[0]);
            section = createInstance(path[0]);
            sectionMap.put(path[0], section);
        }
        return section.getSection(ConfigHelper.getNextKeys(path), createIfNotThere);
    }

    protected C get(final String key, final String[] path) {
        if (path.length == 0) {
            return getImpl(key);
        }
        final AbstractSection<C, T> section = getSection(path, false);
        if (section == null) {
            return null;
        }
        return section.getImpl(key);
    }

    protected C get(final String key, final String[] path, final T type) {
        if (path.length == 0) {
            return getImpl(key, type);
        }
        final AbstractSection<C, T> section = getSection(path, false);
        if (section == null) {
            return null;
        }
        return section.getImpl(key, type);
    }

    protected void set(final String key, final String[] path, final C value) {
        if (path.length == 0) {
            setImpl(key, value);
            return;
        }
        getSection(path, true).setImpl(key, value);
    }

    protected void setValue(final String key, final String[] path, final Object value) {
        if (path.length == 0) {
            setValueImpl(key, value);
            return;
        }
        getSection(path, true).setValueImpl(key, value);
    }

    /*
     * Abstract
     */

    protected abstract void clearImpl();

    protected abstract Collection<String> keysImpl();

    protected abstract void setImpl(String key, C value);

    protected abstract void setValueImpl(String key, Object value);

    protected abstract C getImpl(String key);

    protected abstract C getImpl(String key, T type);

    protected abstract Object getValueImpl(C value);

    protected abstract boolean hasImpl(String key);

    protected abstract boolean hasImpl(String key, T type);

    protected abstract AbstractSection<C, T> createInstance(String name);

    protected abstract ISectionList<C, T> createListInstance(String name);

}
