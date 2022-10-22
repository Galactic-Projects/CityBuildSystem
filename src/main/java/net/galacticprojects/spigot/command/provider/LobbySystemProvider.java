package net.galacticprojects.spigot.command.provider;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.IProviderArgumentType;
import net.galacticprojects.lobbysystem.LobbySystem;

public final class LobbySystemProvider implements IProviderArgumentType<LobbySystem> {

	private final LobbySystem plugin;
	
	public LobbySystemProvider(final LobbySystem plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public LobbySystem provide(Actor<?> actor) {
		return plugin;
	}

}
