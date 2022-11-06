package net.galacticprojects.spigot.command.provider;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.IProviderArgumentType;
import net.galacticprojects.spigot.config.SQLConfiguration;
import net.galacticprojects.spigot.database.SQLDatabase;
import net.galacticprojects.spigot.util.Ref;

public class SQLProvider implements IProviderArgumentType<Ref<SQLDatabase>>
{

    private final SQLConfiguration configuration;
    
    public SQLProvider(final SQLConfiguration configuration) {
    	this.configuration = configuration;
	}

    @Override
    public Ref<SQLDatabase> provide(Actor<?> actor) {
        return configuration.getDatabaseRef();
    }
}
