package net.galacticprojects.spigot.util.array;

public enum ArrayType {

    STRING("String"), LIST("List"), ARRAYLIST("ArrayList"), INT("Integer"), DOUBLE("Double"), FLOAT("Float"), LONG("Long"), BYTE("Byte"), CHAR("Char");

    private String name;
    private ArrayType(String name) {
        this.name = name;
    }

}
