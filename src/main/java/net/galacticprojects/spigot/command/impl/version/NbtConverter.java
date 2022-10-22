package net.galacticprojects.spigot.command.impl.version;

import com.syntaxphoenix.syntaxapi.nbt.*;
import me.lauriichan.laylib.reflection.ClassUtil;
import me.lauriichan.laylib.reflection.JavaAccess;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class NbtConverter {

    public static final NbtConverter CONVERTER = new NbtConverter();

    public static NbtTag convert(Object minecraftTag) {
        return CONVERTER.tagFromMinecraft(minecraftTag);
    }

    public static Object convert(NbtTag tag) {
        return CONVERTER.tagToMinecraft(tag);
    }

    private final Class<?> nmsNBTBaseClass = ClassUtil.findClass("net.minecraft.nbt.NBTBase");

    private final Class<?> nmsNBTTagEndClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagEnd");
    private final Class<?> nmsNBTTagByteClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagByte");
    private final Class<?> nmsNBTTagShortClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagShort");
    private final Class<?> nmsNBTTagIntClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagInt");
    private final Class<?> nmsNBTTagLongClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagLong");
    private final Class<?> nmsNBTTagFloatClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagFloat");
    private final Class<?> nmsNBTTagDoubleClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagDouble");
    private final Class<?> nmsNBTTagStringClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagString");
    private final Class<?> nmsNBTTagByteArrayClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagByteArray");
    private final Class<?> nmsNBTTagIntArrayClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagIntArray");
    private final Class<?> nmsNBTTagLongArrayClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagLongArray");
    private final Class<?> nmsNBTTagListClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagList");
    private final Class<?> nmsNBTTagCompoundClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagCompound");

    private final Method getType = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTBaseClass, byte.class));

    private final Field getEnd = Objects.requireNonNull(ClassUtil.getStaticField(nmsNBTTagEndClass, nmsNBTTagEndClass));
    private final Method createByte = Objects.requireNonNull(ClassUtil.getStaticMethod(nmsNBTTagByteClass, nmsNBTTagByteClass, byte.class));
    private final Method createShort = Objects
            .requireNonNull(ClassUtil.getStaticMethod(nmsNBTTagShortClass, nmsNBTTagShortClass, short.class));
    private final Method createInt = Objects.requireNonNull(ClassUtil.getStaticMethod(nmsNBTTagIntClass, nmsNBTTagIntClass, int.class));
    private final Method createLong = Objects.requireNonNull(ClassUtil.getStaticMethod(nmsNBTTagLongClass, nmsNBTTagLongClass, long.class));
    private final Method createFloat = Objects
            .requireNonNull(ClassUtil.getStaticMethod(nmsNBTTagFloatClass, nmsNBTTagFloatClass, float.class));
    private final Method createDouble = Objects
            .requireNonNull(ClassUtil.getStaticMethod(nmsNBTTagDoubleClass, nmsNBTTagDoubleClass, double.class));
    private final Method createString = Objects
            .requireNonNull(ClassUtil.getStaticMethod(nmsNBTTagStringClass, nmsNBTTagStringClass, String.class));
    private final Constructor<?> createByteArray = Objects.requireNonNull(ClassUtil.getConstructor(nmsNBTTagByteArrayClass, byte[].class));
    private final Constructor<?> createIntArray = Objects.requireNonNull(ClassUtil.getConstructor(nmsNBTTagIntArrayClass, int[].class));
    private final Constructor<?> createLongArray = Objects.requireNonNull(ClassUtil.getConstructor(nmsNBTTagLongArrayClass, long[].class));
    private final Constructor<?> createList = Objects.requireNonNull(ClassUtil.getConstructor(nmsNBTTagListClass));
    private final Constructor<?> createCompound = Objects.requireNonNull(ClassUtil.getConstructor(nmsNBTTagCompoundClass));

    private final Method getByte = Objects
            .requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagByteClass, byte.class, Arrays.asList(getType.getName())));
    private final Method getShort = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagShortClass, short.class));
    private final Method getInt = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagIntClass, int.class));
    private final Method getLong = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagLongClass, long.class));
    private final Method getFloat = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagFloatClass, float.class));
    private final Method getDouble = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagDoubleClass, double.class));
    private final Method getString = Objects
            .requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagStringClass, String.class, Arrays.asList("toString")));
    private final Method getByteArray = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagByteArrayClass, byte[].class));
    private final Method getIntArray = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagIntArrayClass, int[].class));
    private final Method getLongArray = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagLongArrayClass, long[].class));

    private final Method compoundAddItem = Objects
            .requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagCompoundClass, nmsNBTBaseClass, String.class, nmsNBTBaseClass));
    private final Method compoundGetItem = Objects
            .requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagCompoundClass, nmsNBTBaseClass, String.class));
    private final Method compoundGetKeys = Objects.requireNonNull(ClassUtil.getDeclaredMethod(nmsNBTTagCompoundClass, Set.class));

    private Object tagToMinecraft(NbtTag tag) {
        switch (tag.getType()) {
            case END:
                return JavaAccess.getStaticValue(getEnd);
            case BYTE:
                return JavaAccess.invokeStatic(createByte, ((NbtByte) tag).getByteValue());
            case SHORT:
                return JavaAccess.invokeStatic(createShort, ((NbtShort) tag).getShortValue());
            case INT:
                return JavaAccess.invokeStatic(createInt, ((NbtInt) tag).getIntValue());
            case LONG:
                return JavaAccess.invokeStatic(createLong, ((NbtLong) tag).getLongValue());
            case FLOAT:
                return JavaAccess.invokeStatic(createFloat, ((NbtFloat) tag).getFloatValue());
            case DOUBLE:
                return JavaAccess.invokeStatic(createDouble, ((NbtDouble) tag).getDoubleValue());
            case STRING:
                return JavaAccess.invokeStatic(createString, ((NbtString) tag).getValue());
            case BYTE_ARRAY:
                return JavaAccess.instance(createByteArray, ((NbtByteArray) tag).getValue());
            case INT_ARRAY:
                return JavaAccess.instance(createIntArray, ((NbtIntArray) tag).getValue());
            case LONG_ARRAY:
                return JavaAccess.instance(createLongArray, ((NbtLongArray) tag).getValue());
            case LIST:
                return listToMinecraft((NbtList<?>) tag);
            case COMPOUND:
                return compoundToMinecraft((NbtCompound) tag);
            default:
                throw new IllegalArgumentException("Unsupported Type!");
        }
    }

    @SuppressWarnings({
            "rawtypes",
            "unchecked"
    })
    private List listToMinecraft(NbtList<?> list) {
        List minecraft = (List) JavaAccess.instance(createList);
        for (NbtTag tag : list) {
            if (tag == null) {
                continue;
            }
            minecraft.add(tagToMinecraft(tag));
        }
        return minecraft;
    }

    private Object compoundToMinecraft(NbtCompound compound) {
        Object minecraft = JavaAccess.instance(createCompound);
        for (String key : compound.getKeys()) {
            if (compound.get(key) == null) {
                continue;
            }
            JavaAccess.invoke(minecraft, compoundAddItem, key, tagToMinecraft(compound.get(key)));
        }
        return minecraft;
    }

    @SuppressWarnings("rawtypes")
    private NbtTag tagFromMinecraft(Object minecraftTag) {
        switch (NbtType.getById((byte) JavaAccess.invoke(minecraftTag, getType))) {
            case END:
                return NbtEnd.INSTANCE;
            case BYTE:
                return new NbtByte((byte) JavaAccess.invoke(minecraftTag, getByte));
            case SHORT:
                return new NbtShort((short) JavaAccess.invoke(minecraftTag, getShort));
            case INT:
                return new NbtInt((int) JavaAccess.invoke(minecraftTag, getInt));
            case LONG:
                return new NbtLong((long) JavaAccess.invoke(minecraftTag, getLong));
            case FLOAT:
                return new NbtFloat((float) JavaAccess.invoke(minecraftTag, getFloat));
            case DOUBLE:
                return new NbtDouble((double) JavaAccess.invoke(minecraftTag, getDouble));
            case STRING:
                return new NbtString((String) JavaAccess.invoke(minecraftTag, getString));
            case BYTE_ARRAY:
                return new NbtByteArray((byte[]) JavaAccess.invoke(minecraftTag, getByteArray));
            case INT_ARRAY:
                return new NbtIntArray((int[]) JavaAccess.invoke(minecraftTag, getIntArray));
            case LONG_ARRAY:
                return new NbtLongArray((long[]) JavaAccess.invoke(minecraftTag, getLongArray));
            case LIST:
                return listFromMinecraft((List) minecraftTag);
            case COMPOUND:
                return compoundFromMinecraft(minecraftTag);
            default:
                throw new IllegalArgumentException("Unsupported Type!");
        }
    }

    @SuppressWarnings("rawtypes")
    private NbtList<?> listFromMinecraft(List minecraftList) {
        NbtList<?> list = new NbtList<>();
        for (Object object : minecraftList) {
            if (object == null) {
                continue;
            }
            list.addTag(tagFromMinecraft(object));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private NbtCompound compoundFromMinecraft(Object minecraftCompound) {
        NbtCompound compound = new NbtCompound();
        Set<String> keys = (Set<String>) JavaAccess.invoke(minecraftCompound, compoundGetKeys);
        for (String key : keys) {
            Object object = JavaAccess.invoke(minecraftCompound, compoundGetItem, key);
            if (object == null) {
                continue;
            }
            compound.set(key, tagFromMinecraft(object));
        }
        return compound;
    }

}
