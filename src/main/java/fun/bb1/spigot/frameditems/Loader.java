package fun.bb1.spigot.frameditems;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * 
 * Copyright 2022 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Loader extends JavaPlugin implements Listener {

	private static final String PERMISSION = "frameditems.allow";
	
	private static final Set<Material> ITEM_FRAMES = Set.of(Material.ITEM_FRAME, Material.GLOW_ITEM_FRAME);
	
	private final Set<UUID> map = Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>());
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public final void interactItemEventHandler(final PlayerInteractEvent event) {
		if (!event.getPlayer().hasPermission(PERMISSION)) return;
		final ItemStack itemStack = event.getItem();
		if (!event.hasItem() || !ITEM_FRAMES.contains(itemStack.getType()) || !event.getPlayer().isSneaking()) return;
		event.setCancelled(true);
		final UUID uuid = event.getPlayer().getUniqueId();
		if (this.map.contains(uuid)) {
			this.map.remove(uuid);
			event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You will now place invisible item frames!"));
		} else {
			this.map.add(uuid);
			event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You will now place visible item frames!"));
		}
	}
	
	@EventHandler
	public final void blockPlaceEventHandler(final HangingPlaceEvent event) {
		if (event.getEntity() instanceof ItemFrame frame && event.getPlayer().hasPermission(PERMISSION)) frame.setVisible(!this.map.contains(event.getPlayer().getUniqueId()));
	}
	
}
