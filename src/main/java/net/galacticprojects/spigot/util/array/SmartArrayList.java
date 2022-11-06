package net.galacticprojects.spigot.util.array;

import java.util.*;

public class SmartArrayList<E> {

    HashMap<Object, String> objectHash;
    ArrayList<Object> objects = new ArrayList<>();

    ArrayList<Object> stash = new ArrayList<>();

    public SmartArrayList() {
        objectHash = new HashMap<>();
    }
    public SmartArrayList(ArrayType type, Object... obj) {
        objects.add(obj);
        objectHash = new HashMap<>();
        for(Object objec : obj) {
            objectHash.put(objec, type.name());
        }
    }

    public SmartArrayList(ArrayType type, Object obj) {
        objects.add(obj);
        objectHash = new HashMap<>();
        objectHash.put(obj, type.name());
    }

    public SmartArrayList(ArrayType type, String obj) {
        objects.add(obj);
        objectHash = new HashMap<>();
        objectHash.put(obj, type.name());
    }

    public SmartArrayList(ArrayType type, int integer) {
        objects.add(integer);
        objectHash = new HashMap<>();
        objectHash.put(integer, type.name());
    }

    public SmartArrayList(ArrayType type, double d) {
        objects.add(d);
        objectHash = new HashMap<>();
        objectHash.put(d, type.name());
    }

    public void add(ArrayType type, Object obj) {
        objectHash.put(obj, type.name());
        objects.add(type);
    }

    public void add(ArrayType type, String string) {
        objectHash.put(string, type.name());
        objects.add(type);
    }

    public void add(ArrayType type, int integer) {
        objectHash.put(integer, type.name());
        objects.add(type);
    }

    public void add(ArrayType type, double d) {
        objectHash.put(d, type.name());
        objects.add(type);
    }

    public void remove(int value) {
        stash.add(objects.get(value));
        objectHash.remove(value, objectHash.get(objects.get(value)));
        objects.remove(value);
    }

    public void remove(Object value) {
        stash.add(value);
        objectHash.remove(value, objectHash.get(value));
        objects.remove(value);
    }

    public void recover(Object value) {
        objects.add(value);
        stash.remove(value);
    }

    public void recover(int value) {
        objects.add(stash.get(value));
        stash.remove(value);
    }

    public Object getValueAsObject(int value) {
        return objects.get(value);
    }

    public String getValueAsString(int value) {
        Object obj = objects.get(value);
        if(objectHash.get(obj).equals(ArrayType.STRING.name())) {
            return (String) obj;
        }
        if(objectHash.get(obj).equals(ArrayType.DOUBLE.name())) {
            return Double.toString((Double) obj);
        }
        if(objectHash.get(obj).equals(ArrayType.INT.name())) {
            return Integer.toString((Integer) obj);
        }
        return "";
    }

    public Integer getValueAsInteger(int value) {
        Object obj = objects.get(value);
        if(objectHash.get(obj).equals(ArrayType.INT.name())) {
            return (Integer) obj;
        }
        if(objectHash.get(obj).equals(ArrayType.DOUBLE.name())) {
            return (Integer) obj;
        }
        if(objectHash.get(obj).equals(ArrayType.STRING.name())) {
            return Integer.parseInt((String) obj);
        }
        return 0;
    }

    public double getValueAsDouble(int value) {
        Object obj = objects.get(value);
        if(objectHash.get(obj).equals(ArrayType.DOUBLE.name())) {
            return (Double) obj;
        }
        if(objectHash.get(obj).equals(ArrayType.INT.name())) {
            return (Integer) obj;
        }
        if(objectHash.get(obj).equals(ArrayType.STRING.name())) {
            return Double.parseDouble((String) obj);
        }
        return 0;
    }

    public Object[] getValues() {
        return objects.toArray();
    }

    public int getSize() {
        return objects.size();
    }

    public int getStashSize() {
        return stash.size();
    }

    public Object[] getStashValues() {
        return stash.toArray();
    }

    public Collection<Object> getAsCollection() {
        Collection<Object> collection = objects;
        return collection;
    }

    public Collection<String> getAsStringCollection() {
        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0; i < objects.size(); i++) {
            if(objectHash.get(objects.get(i)).equals(ArrayType.STRING.name())) {
                strings.add((String) objects.get(i));
            }
        }
        Collection<String> collection = strings;
        return collection;
    }

    public ArrayList<Object> getStash() {
        return stash;
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }
}
