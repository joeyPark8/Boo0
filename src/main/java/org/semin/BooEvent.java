package org.semin;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class BooEvent implements Listener {

    @EventHandler
    void onThrow(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getItemDrop().getItemStack();

        if (stack.getEnchantments().containsKey(Enchantment.DURABILITY)) {
            player.getWorld().spawn(player.getLocation(), BlockDisplay.class, p -> {
                p.setBlock(Material.ACACIA_LOG.createBlockData());

                p.setBillboard(Display.Billboard.CENTER);
            });
        }
    }
}
