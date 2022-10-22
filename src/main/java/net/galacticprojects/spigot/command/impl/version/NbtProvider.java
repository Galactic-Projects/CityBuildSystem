package net.galacticprojects.spigot.command.impl.version;

import com.syntaxphoenix.syntaxapi.nbt.NbtCompound;
import me.lauriichan.laylib.reflection.ClassUtil;
import me.lauriichan.laylib.reflection.JavaAccess;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

public final class NbtProvider {

    public static final NbtProvider PROVIDER = new NbtProvider();

    private final Class<?> nmsNBTTagCompoundClass = ClassUtil.findClass("net.minecraft.nbt.NBTTagCompound");
    private final Class<?> nmsItemStackClass = ClassUtil.findClass("net.minecraft.world.item.ItemStack");
    private final Class<?> cbCraftItemStackClass = ClassUtil.findClass(VersionConstant.craftClassPath("inventory.CraftItemStack"));

    private final Method nbtToItem = Objects
            .requireNonNull(ClassUtil.getStaticMethod(nmsItemStackClass, nmsItemStackClass, nmsNBTTagCompoundClass));
    private final Method itemToNbt = Objects
            .requireNonNull(ClassUtil.getDeclaredMethod(nmsItemStackClass, nmsNBTTagCompoundClass, nmsNBTTagCompoundClass));

    private final Constructor<?> constructNbt = Objects.requireNonNull(ClassUtil.getConstructor(nmsNBTTagCompoundClass));

    private final Method nmsItemToCbItem = Objects
            .requireNonNull(ClassUtil.getMethod(cbCraftItemStackClass, "asCraftMirror", nmsItemStackClass));
    private final Method cbItemToNmsItem = Objects.requireNonNull(ClassUtil.getMethod(cbCraftItemStackClass, "asNMSCopy", ItemStack.class));

    public static NbtCompound itemStackToNbt(final ItemStack itemStack) {
        return (NbtCompound) NbtConverter.convert(PROVIDER.saveMinecraftStackImpl(PROVIDER.toMinecraftStackImpl(itemStack)));
    }

    public static ItemStack itemStackFromNbt(final NbtCompound compound) {
        return PROVIDER.fromMinecraftStackImpl(PROVIDER.loadMinecraftStackImpl(NbtConverter.convert(compound)));
    }

    private NbtProvider() {}

    public Object saveMinecraftStackImpl(final Object object) {
        if (object == null) {
            return null;
        }
        final Object tag = JavaAccess.instance(constructNbt);
        return JavaAccess.invoke(object, itemToNbt, tag);
    }

    public Object loadMinecraftStackImpl(final Object object) {
        if (object == null) {
            return null;
        }
        return JavaAccess.invokeStatic(nbtToItem, object);
    }

    public ItemStack fromMinecraftStackImpl(final Object object) {
        if (object == null) {
            return null;
        }
        return (ItemStack) JavaAccess.invokeStatic(nmsItemToCbItem, object);
    }

    public Object toMinecraftStackImpl(final ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        return JavaAccess.invokeStatic(cbItemToNmsItem, itemStack);
    }

}
