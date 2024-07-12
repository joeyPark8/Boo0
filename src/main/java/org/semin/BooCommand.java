package org.semin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

import static org.bukkit.util.NumberConversions.floor;
import static org.semin.Boo.levels;

public class BooCommand {
    public static void register() {
        CommandAPICommand command;

        StringArgument arg = new StringArgument("name");

        command = new CommandAPICommand("bong")
                .withSubcommand(
                        new CommandAPICommand("create")
                                .withArguments(arg)
                                .executesPlayer(BooCommand::create)
                )
                .withSubcommand(
                        new CommandAPICommand("start")
                                .withArguments(new StringArgument("level")
                                        .replaceSuggestions(
                                                ArgumentSuggestions.strings(
                                                        levels.keySet()
                                                )
                                        )
                                )
                                .withArguments(new PlayerArgument("player"))
                                .executesPlayer(BooCommand::start)
                );

        command.register();
    }


    public static void create(Player player, CommandArguments args) {
        String name = args.getRaw(0);

        com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);
        Region region;

        World selectionWorld = localSession.getSelectionWorld();
        try {
            if (selectionWorld == null) throw new IncompleteRegionException();
            region = localSession.getSelection(selectionWorld);

            player.sendMessage(Component.text("level [" + name + "] is created"));

            levels.put(name, new ParkourLevel(name, region));
        } catch (IncompleteRegionException e) {
            actor.printError(TextComponent.of("Please make a region selection first."));
        }
    }

    public static void start(Player player, CommandArguments args) {
        String levelName = (String) args.get(0);
        Player target = (Player) args.get(1);

        ParkourLevel level = levels.get(levelName);
        Region region = level.region;


        region.forEach(
            vec -> {
                Location loc = new Location(player.getWorld(), vec.getX(), vec.getY(), vec.getZ());
                Block block = player.getWorld().getBlockAt(loc);

                if (block.getType() == Material.EMERALD_BLOCK) {
                    target.teleport(loc.add(0, 1, 0));

                }
            }
        );

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                assert target != null;
                BoundingBox box = target.getBoundingBox();

                int y = floor(box.getMinY());
                int stepY = floor(box.getMinY() - 0.000001);

                if (stepY != y) {
                    org.bukkit.World world = target.getWorld();
                    int minX = floor(box.getMinX());
                    int minZ = floor(box.getMinX());
                    int maxX = floor(box.getMaxX());
                    int maxZ = floor(box.getMaxZ());

                    for (int x = minX; x <= maxX; x+=1) {
                        for (int z = minZ; z <= maxZ; z+=1) {
                            Block stepBlock = world.getBlockAt(x, stepY, z);

//                            challenge.dataByBlock[stepBlock]?.run {
//                                onStep(challenge, traceur, event)
//                            }
                        }
                    }
                }
            }
        }.runTaskTimer(new Main(), 0, 1);


    }
}
