package me.MathiasMC.PvPClans.commands;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PvPClans_TabComplete implements TabCompleter {

    private final PvPClans plugin;

    public PvPClans_TabComplete(final PvPClans plugin) {
        this.plugin = plugin;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) return null;
            if (!plugin.getStatsManager().canProgress(player)) return null;
            List<String> commands = new ArrayList<>();
            List<String> list = new ArrayList<>();
            if (player.hasPermission("pvpclans.player.help") || player.hasPermission("pvpclans.admin.help")) {
                if (args.length == 1) commands.add("help");
            } else if (args.length > 1 && args[0].equals("help")) return null;
            if (player.hasPermission("pvpclans.player.create")) {
                if (args.length == 1) commands.add("create");
            } else if (args.length > 1 && args[0].equals("create")) return null;
            if (player.hasPermission("pvpclans.player.invite")) {
                if (args.length == 1) commands.add("invite");
            } else if (args.length > 1 && args[0].equals("invite")) return null;
            if (player.hasPermission("pvpclans.player.accept")) {
                if (args.length == 1) commands.add("accept");
            } else if (args.length > 1 && args[0].equals("accept")) return null;
            if (player.hasPermission("pvpclans.player.leave")) {
                if (args.length == 1) commands.add("leave");
            } else if (args.length > 1 && args[0].equals("leave")) return null;
            if (player.hasPermission("pvpclans.player.deny")) {
                if (args.length == 1) commands.add("deny");
            } else if (args.length > 1 && args[0].equals("deny")) return null;
            if (player.hasPermission("pvpclans.player.kick")) {
                if (args.length == 1) commands.add("kick");
            } else if (args.length > 1 && args[0].equals("kick")) return null;
            if (player.hasPermission("pvpclans.player.delete")) {
                if (args.length == 1) commands.add("delete");
            } else if (args.length > 1 && args[0].equals("delete")) return null;
            if (player.hasPermission("pvpclans.player.chat")) {
                if (args.length == 1) commands.add("chat");
            } else if (args.length > 1 && args[0].equals("chat")) return null;
            if (player.hasPermission("pvpclans.admin.reload")) {
                if (args.length == 1) commands.add("reload");
            } else if (args.length > 1 && args[0].equals("reload")) return null;
            if (player.hasPermission("pvpclans.admin.message")) {
                if (args.length == 1) commands.add("message");
            } else if (args.length > 1 && args[0].equals("message")) return null;
            if (player.hasPermission("pvpclans.admin.broadcast")) {
                if (args.length == 1) commands.add("broadcast");
            } else if (args.length > 1 && args[0].equals("broadcast")) return null;
            if (player.hasPermission("pvpclans.admin.coins")) {
                if (args.length == 1) commands.add("coins");
            } else if (args.length > 1 && args[0].equals("coins")) return null;
            if (player.hasPermission("pvpclans.admin.xp")) {
                if (args.length == 1) commands.add("xp");
            } else if (args.length > 1 && args[0].equals("xp")) return null;
            if (player.hasPermission("pvpclans.admin.perk")) {
                if (args.length == 1) commands.add("perk");
            } else if (args.length > 1 && args[0].equals("perk")) return null;
            if (args.length == 2) {
                switch (args[0]) {
                    case "create":
                        commands.add("name");
                        break;
                    case "accept":
                    case "deny": {
                        if (!plugin.getInvites().containsKey(player.getUniqueId())) break;
                        commands.add(plugin.getClanPlayer(plugin.getInvites().get(player.getUniqueId())).getName());
                        break;
                    }
                    case "kick": {
                        Clan clan = plugin.getClanPlayer(player.getUniqueId()).getClan();
                        if (clan == null) return null;
                        for (UUID uuid : clan.getMembers()) {
                            commands.add(plugin.getClanPlayer(uuid).getName());
                        }
                        break;
                    }
                    case "broadcast":
                        commands.add("null");
                        break;
                    case "invite":
                    case "message":
                    case "coins":
                    case "xp":
                        commands.addAll(getPlayers(args[1]));
                        break;
                    case "perk": {
                        commands.add("add");
                        commands.add("remove");
                        break;
                    }
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("perk")) {
                    commands.addAll(getPlayers(args[2]));
                }
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("perk")) {
                    commands.addAll(plugin.getPerks().keySet());
                }
            }
            if (args.length == 5 || args.length == 6) {
                if (args[0].equalsIgnoreCase("perk")) {
                    if (args[1].equalsIgnoreCase("add")) {
                        commands.add("0");
                    }
                }
            }
            StringUtil.copyPartialMatches(args[args.length - 1], commands, list);
            Collections.sort(list);
            return list;
        }
        return null;
    }

    private List<String> getPlayers(String startsWith) {
        List<String> list = new ArrayList<>();
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            String name = onlinePlayer.getName();
            if (name.startsWith(startsWith)) {
                list.add(name);
            }
        }
        return list;
    }
}
