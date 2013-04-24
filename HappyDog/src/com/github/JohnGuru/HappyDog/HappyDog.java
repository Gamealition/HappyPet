package com.github.JohnGuru.HappyDog;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class HappyDog extends JavaPlugin {
	public static Material wand;

	@Override
    public void onEnable(){
		// process the configuration parameters
		String mat = getConfig().getString("wand", "arrow");
		wand = Material.matchMaterial(mat);
		if (wand == null) {
			getLogger().warning("Wand: material " + mat + " unrecognized");
			wand = Material.ARROW; // set default
		}
		getLogger().info("Wand material is " + wand);
		// Register the listener
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }
 
    @Override
    public void onDisable() {
        // Unregister this plugin's event handlers
    	HandlerList.unregisterAll(this);
    	saveConfig();
    }

    public final class PlayerInteractListener implements Listener {
    	
    	@EventHandler
    	public void OnPlayerInteract( PlayerInteractEntityEvent ev) {
    		// Analyze the event
    		Entity theAnimal = ev.getRightClicked();
    		if (theAnimal.getType() == EntityType.WOLF) {
    			Wolf dog = (Wolf)theAnimal;
    			Player p = ev.getPlayer();
    			if (p.getItemInHand().getType() != wand)
    				// Not the key wand to make a dog un-angry
    				return;
    			if (dog.getOwner() == null) {
    				p.sendMessage(ChatColor.RED + "This is a wild dog");
    				return;
    			}
    			if (p != dog.getOwner()) {
    				p.sendMessage(ChatColor.RED + "This dog is owned by " + dog.getOwner().getName());
    				return;
    			}
    			ev.setCancelled(true);
    			dog.setSitting(true); // force dog to heel
    			if (!dog.isAngry()) {
    				p.sendMessage(ChatColor.RED + "This dog is not angry");
    				return;
    			}
    			dog.setAngry(false);
    			p.sendMessage(ChatColor.GREEN + "This dog is now happy!");
    		}
    	}
    }
}
