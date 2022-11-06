package net.galacticprojects.spigot.database;

public enum SQLTable {

    CityBuild("CityBuild"), PLAYER_TABLE("Player"), SYNC_TABLE("Sync"), BOOST_TABLE("Boost");
    ;

    private final String defaultTableName;
    private String tableName;

    SQLTable(final String defaultTableName) {
        this.defaultTableName = defaultTableName;
    }

    public String defaultTableName() {
        return defaultTableName;
    }

    public String tableName() {
        if(tableName == null) {
            return (tableName = defaultTableName);
        }
        return tableName;
    }

    public void tableName(String tableName){
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return tableName;
    }

}
