package net.endercraftbuild.cod.events;

import net.endercraftbuild.cod.Game;

import org.bukkit.entity.Player;

public abstract class PlayerGameEvent extends GameEvent {

	private final Player player;

	public PlayerGameEvent(Player player, Game game) {
		super(game);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

}
