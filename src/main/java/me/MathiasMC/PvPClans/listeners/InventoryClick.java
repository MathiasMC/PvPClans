package me.MathiasMC.PvPClans.listeners;

import me.MathiasMC.PvPClans.gui.GUI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClick implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        final InventoryHolder inventoryHolder = e.getInventory().getHolder();
        if (inventoryHolder instanceof GUI) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType().equals(Material.AIR)) return;
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) return;
            final GUI gui = (GUI) inventoryHolder;
            gui.click(e);
        }
    }
}