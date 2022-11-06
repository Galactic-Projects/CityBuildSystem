package net.galacticprojects.spigot.command.provider;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.IProviderArgumentType;
import net.galacticprojects.spigot.CityBuild;

public final class CityBuildProvider implements IProviderArgumentType<CityBuild> {

	private final CityBuild plugin;
	
	public CityBuildProvider(final CityBuild plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public CityBuild provide(Actor<?> actor) {
		return plugin;
	}

}
