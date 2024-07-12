package org.semin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;


import static org.semin.Boo.isStarted;
import static org.semin.BooCommand.register;


public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println(this.getName() + " is enabled");

        getServer().getPluginManager()
                .registerEvents(new BooEvent(), this);

        register();
    }

    @Override
    public void onDisable() {
        System.out.println(this.getName() + " is disabled");
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            if (command.getName().equalsIgnoreCase("melon")) {
                BlockDisplay displayBlock = player.getWorld().spawn(player.getLocation(), BlockDisplay.class, p -> {
                    p.setTransformation(new Transformation(
                            new Vector3f(2.0F, 2.0F, 2.0F),
                            new AxisAngle4f(0.0F, 0.0F, 0.0F, 0.0F),
                            new Vector3f(2.0F, 2.0F, 2.0F),
                            new AxisAngle4f(0.0F, 0.0F, 0.0F, 0.0F)
                    ));

                    p.setGlowing(true);
                });

                Bukkit.getScheduler().runTaskTimer(this, () -> {
                    displayBlock.teleport(player.getEyeLocation().multiply(5));
                }, 0, 1);

                if (args.length > 1) {
                    if (args[0].equalsIgnoreCase("start")) {
                        isStarted = true;

                        Component text = Component.text("Hello");

                        player.sendMessage(
                                Component.text("Started")
                                        .clickEvent(ClickEvent.suggestCommand("/boom"))
                        );


                        ItemStack stack = new ItemStack(Material.PAPER);

                        getServer().getOnlinePlayers().forEach(p -> {
                            p.getInventory()
                                    .addItem(stack);

                            p.sendMessage(Component.text("To join the game, throw it!!"));
                        });


                        Team team = player.getScoreboard().registerNewTeam("reed");
                        team.color(NamedTextColor.RED);

                        
                    }
                }
                
                return true;
            }
        }

        return true;
    }
}