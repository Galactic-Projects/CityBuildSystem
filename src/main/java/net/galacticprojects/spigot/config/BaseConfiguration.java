package net.galacticprojects.spigot.config;

import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.lobbysystem.config.impl.json.JsonConfig;

import java.io.File;

public class BaseConfiguration {

    protected final JsonConfig config = new JsonConfig();
    protected final ISimpleLogger logger;
    private final File file;

    public BaseConfiguration(ISimpleLogger logger, File dataFolder, String path) {
        this.logger = logger;
        this.file = new File(dataFolder, path);
    }

    public final void load(){
        try {
            config.load(file);
        } catch(Throwable throwable){
            logger.warning("Failed to load file '{0}'", file.getName(), throwable);
        }
        try{
            onLoad();
        } catch(Throwable throwable) {
            logger.error("Failed to run onLoad of config file '{0}'", file.getName(), throwable);
        }
    }

    public final void save() {
        try{
            onSave();
        } catch(Throwable throwable) {
            logger.error("Failed to run onSave of config file '{0}'", file.getName(), throwable);
        }
        try {
            config.save(file);
        } catch(Throwable throwable){
            logger.warning("Failed to save file '{0}'", file.getName(), throwable);
        }
    }

    public final void reload(){
        load();
        save();
    }

    protected void onLoad() throws Throwable {}

    protected void onSave() throws Throwable {}
}
