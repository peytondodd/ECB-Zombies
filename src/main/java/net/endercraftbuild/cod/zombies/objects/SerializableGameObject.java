package net.endercraftbuild.cod.zombies.objects;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

public class SerializableGameObject {
	
	private UUID id;
	
	public SerializableGameObject() {
		id = UUID.randomUUID();
	}
	
	public UUID getId() {
		return id;
	}
	
	public ConfigurationSection load(ConfigurationSection config) {
		id = UUID.fromString(config.getName());
		return config;
	}
	
	public ConfigurationSection save(ConfigurationSection parent) {
		return parent.createSection(id.toString());
	}

}
