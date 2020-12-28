package me.MathiasMC.PvPClans.commands;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Perk;
import me.MathiasMC.PvPClans.api.events.*;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.data.ClanStats;
import me.MathiasMC.PvPClans.gui.menu.ClanGUI;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class PvPClans_Command implements CommandExecutor {

    private final PvPClans plugin;

    public PvPClans_Command(final PvPClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pvpclans")) {
            Player player = null;
            ClanPlayer clanPlayer = null;
            Clan clan = null;
            if (sender instanceof Player) {
                player = (Player) sender;
                clanPlayer = plugin.getClanPlayer(player.getUniqueId());
                clan = clanPlayer.getClan();
            }
            if (clanPlayer != null && !plugin.getStatsManager().canProgress(player)) {
                Utils.dispatchCommandList(clanPlayer, "command.world");
                return true;
            }
            if (!sender.hasPermission("pvpclans")) {
                Utils.dispatchCommandList(clanPlayer, "command.permission");
                return true;
            }
            if (args.length == 0) {
                if (clanPlayer == null) {
                    Utils.sendMessageList(sender, "console.command.message");
                    return true;
                }
                if (!sender.hasPermission("pvpclans.player.clan")) {
                    Utils.dispatchCommandList(clanPlayer, "command.permission");
                    return true;
                }
                if (clan == null) {
                    Utils.dispatchCommandList(clanPlayer, "command.none");
                    return true;
                }
                new ClanGUI(plugin.getPlayerMenu(player)).open();
                return true;
            }
            final String arg = args[0];
            switch (arg) {
                case "help":
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.help");
                        return true;
                    }
                    if (sender.hasPermission("pvpclans.admin.help")) {
                        Utils.dispatchCommandList(clanPlayer, "help.admin");
                        return true;
                    } else if (sender.hasPermission("pvpclans.player.help")) {
                        Utils.dispatchCommandList(clanPlayer, "help.player");
                        return true;
                    }
                    Utils.dispatchCommandList(clanPlayer, "command.permission");
                    return true;
                case "reload": {
                    if (clanPlayer != null && !sender.hasPermission("pvpclans.admin.reload")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    plugin.getFileUtils().loadConfigs();
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.reload");
                        return true;
                    }
                    Utils.dispatchCommandList(clanPlayer, "reload");
                    return true;
                }
                case "message": {
                    if (!sender.hasPermission("pvpclans.admin.message")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (args.length < 3) {
                        if (clanPlayer == null) {
                            Utils.sendMessageList(sender, "console.message");
                            return true;
                        }
                        Utils.dispatchCommandList(clanPlayer, "message");
                        return true;
                    }
                    final Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        return true;
                    }
                    final StringBuilder sb = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    final String text = sb.toString().trim();
                    if (!text.contains("\\n")) {
                        target.sendMessage(Utils.color(text));
                        return true;
                    }
                    for (String message : text.split(Pattern.quote("\\n"))) {
                        target.sendMessage(Utils.color(message));
                    }
                    return true;
                }
                case "broadcast": {
                    if (!sender.hasPermission("pvpclans.admin.broadcast")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (args.length < 4) {
                        if (clanPlayer == null) {
                            Utils.sendMessageList(sender, "console.broadcast");
                            return true;
                        }
                        Utils.dispatchCommandList(clanPlayer, "broadcast");
                        return true;
                    }
                    final StringBuilder sb = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    final String text = sb.toString().trim();
                    if (!text.contains("\\n")) {
                        broadcast(ChatColor.translateAlternateColorCodes('&', text), args);
                        return true;
                    }
                    for (String message : text.split(Pattern.quote("\\n"))) {
                        broadcast(ChatColor.translateAlternateColorCodes('&', message), args);
                    }
                    return true;
                }
                case "create": {
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.command.player");
                        return true;
                    }
                    if (!player.hasPermission("pvpclans.player.create")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (clan != null) {
                        Utils.dispatchCommandList(clanPlayer, "create.already");
                        return true;
                    }
                    if (args.length < 2) {
                        Utils.dispatchCommandList(clanPlayer, "create.usage");
                        return true;
                    }
                    final String clanName = args[1];
                    final ClanCreateEvent clanCreateEvent = new ClanCreateEvent(clanPlayer, clanName, plugin.getFileUtils().config.getLong("default.create", 0));
                    plugin.getServer().getPluginManager().callEvent(clanCreateEvent);
                    if (clanCreateEvent.isCancelled()) {
                        return true;
                    }
                    switch (clanCreateEvent.getResponse()) {
                        case EXISTS:
                            for (String command : Utils.getCommands("create.exists")) {
                                Utils.dispatchCommand(clanPlayer, command.replace("{clan}", clanName));
                            }
                            return true;
                        case NAME:
                            for (String command : Utils.getCommands("create.valid")) {
                                Utils.dispatchCommand(clanPlayer, command.replace("{clan}", clanName));
                            }
                            return true;
                        case ENOUGH:
                            for (String command : Utils.getCommands("create.enough")) {
                                Utils.dispatchCommand(clanPlayer, command.replace("{clan}", clanName));
                            }
                            return true;
                        case SUCCESS:
                            clanCreateEvent.execute();
                            for (String command : Utils.getCommands("create.player")) {
                                Utils.dispatchCommand(clanPlayer, command.replace("{clan}", clanName).replace("{id}", String.valueOf(clanCreateEvent.getID())));
                            }
                            return true;
                    }
                    return true;
                }
                case "invite": {
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.command.player");
                        return true;
                    }
                    if (!player.hasPermission("pvpclans.player.invite")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (clan == null) {
                        Utils.dispatchCommandList(clanPlayer, "invite.none");
                        return true;
                    }
                    if (args.length < 2) {
                        Utils.dispatchCommandList(clanPlayer, "invite.usage");
                        return true;
                    }
                    final Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        Utils.sendCommands(clanPlayer, sender, "command.online");
                        return true;
                    }
                    final ClanPlayer clanTarget = plugin.getClanPlayer(target.getUniqueId());
                    if (clanTarget.getClan() != null) {
                        if (clanPlayer.getClanID() != clanTarget.getClanID()) {
                            Utils.dispatchCommandList(clanPlayer, clanTarget, "invite.another");
                            return true;
                        }
                        Utils.dispatchCommandList(clanPlayer, clanTarget, "invite.own");
                        return true;
                    }
                    if (plugin.getInvites().containsKey(clanTarget.getUniqueId())) {
                        Utils.dispatchCommandList(clanPlayer, clanTarget, "invite.already");
                        return true;
                    }
                    if (!clan.isLeader(clanPlayer.getUniqueId()) && !clan.isModerator(clanPlayer.getUniqueId())) {
                        Utils.dispatchCommandList(clanPlayer, clanTarget, "invite.rank");
                        return true;
                    }
                    final ClanInviteEvent clanInviteEvent = new ClanInviteEvent(clanPlayer, clanTarget);
                    plugin.getServer().getPluginManager().callEvent(clanInviteEvent);
                    if (clanInviteEvent.isCancelled()) {
                        return true;
                    }
                    switch (clanInviteEvent.getResponse()) {
                        case LIMIT:
                            Utils.dispatchCommandList(clanPlayer, clanTarget, "invite.limit");
                            return true;
                        case SUCCESS:
                            Utils.dispatchCommandList(clanPlayer, clanTarget, "invite.target");
                            Utils.alertClan(clan, clanPlayer, clanTarget, "invite");
                            clanInviteEvent.execute();
                            plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.getInvites().remove(target.getUniqueId()), 300);
                            return true;
                    }
                    break;
                }
                case "accept": {
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.command.player");
                        return true;
                    }
                    if (!player.hasPermission("pvpclans.player.accept")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (clan != null) {
                        Utils.dispatchCommandList(clanPlayer, "accept.already");
                        return true;
                    }
                    if (args.length < 2) {
                        Utils.dispatchCommandList(clanPlayer, "accept.usage");
                        return true;
                    }
                    final Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        Utils.sendCommands(clanPlayer, sender, "command.online");
                        return true;
                    }
                    final ClanPlayer clanTarget = plugin.getClanPlayer(target.getUniqueId());
                    if (!plugin.getInvites().containsKey(clanPlayer.getUniqueId())) {
                        Utils.dispatchCommandList(clanPlayer, clanTarget, "accept.none");
                        return true;
                    }
                    Clan targetClan = plugin.getClan(clanTarget.getClanID());
                    if (targetClan == null) {
                        Utils.dispatchCommandList(clanPlayer, clanTarget, "accept.deleted");
                        return true;
                    }
                    final ClanAcceptEvent clanAcceptEvent = new ClanAcceptEvent(clanPlayer, clanTarget);
                    plugin.getServer().getPluginManager().callEvent(clanAcceptEvent);
                    if (clanAcceptEvent.isCancelled()) {
                        return true;
                    }
                    switch (clanAcceptEvent.getResponse()) {
                        case LIMIT:
                            Utils.dispatchCommandList(clanPlayer, clanTarget, "accept.limit");
                            return true;
                        case SUCCESS:
                            clanAcceptEvent.execute();
                            targetClan.saveAsync();
                            clanPlayer.saveAsync();
                            Utils.alertClan(targetClan, clanPlayer, clanTarget, "accept");
                            Utils.dispatchCommandList(clanPlayer, clanTarget, "accept.target");
                            return true;
                    }
                    return true;
                }
                case "leave": {
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.command.player");
                        return true;
                    }
                    if (!player.hasPermission("pvpclans.player.leave")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (clan == null) {
                        Utils.dispatchCommandList(clanPlayer, "leave.none");
                        return true;
                    }
                    if (clan.isLeader(clanPlayer.getUniqueId())) {
                        Utils.dispatchCommandList(clanPlayer, "leave.cannot");
                        return true;
                    }
                    final ClanLeaveEvent clanLeaveEvent = new ClanLeaveEvent(clanPlayer);
                    plugin.getServer().getPluginManager().callEvent(clanLeaveEvent);
                    if (clanLeaveEvent.isCancelled()) {
                        return true;
                    }
                    Utils.alertClan(clan, clanPlayer, clanPlayer, "leave");
                    Utils.dispatchCommandList(clanPlayer, clanPlayer, "leave.target");
                    clanLeaveEvent.execute();
                    clan.saveAsync();
                    clanPlayer.saveAsync();
                    return true;
                }
                case "deny": {
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.command.player");
                        return true;
                    }
                    if (!player.hasPermission("pvpclans.player.deny")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (args.length < 2) {
                        Utils.dispatchCommandList(clanPlayer, "deny.usage");
                        return true;
                    }
                    final Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        Utils.sendCommands(clanPlayer, sender, "command.online");
                        return true;
                    }
                    final ClanPlayer clanTarget = plugin.getClanPlayer(target.getUniqueId());
                    if (!plugin.getInvites().containsKey(clanPlayer.getUniqueId())) {
                        Utils.dispatchCommandList(clanPlayer, clanTarget, "deny.none");
                        return true;
                    }
                    final Clan targetClan = plugin.getClan(clanTarget.getClanID());
                    if (targetClan == null) return true;
                    final ClanDenyEvent clanDenyEvent = new ClanDenyEvent(clanPlayer, clanTarget);
                    plugin.getServer().getPluginManager().callEvent(clanDenyEvent);
                    if (clanDenyEvent.isCancelled()) {
                        return true;
                    }
                    clanDenyEvent.execute();
                    Utils.dispatchCommandList(clanTarget, clanPlayer, "deny.target");
                    Utils.alertClan(targetClan, clanTarget, clanPlayer, "deny");
                    return true;
                }
                case "kick": {
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.command.player");
                        return true;
                    }
                    if (!player.hasPermission("pvpclans.player.kick")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (clan == null) {
                        Utils.dispatchCommandList(clanPlayer, "kick.none");
                        return true;
                    }
                    if (!clan.isLeader(clanPlayer.getUniqueId()) && !clan.isModerator(clanPlayer.getUniqueId())) {
                        Utils.dispatchCommandList(clanPlayer, "kick.rank");
                        return true;
                    }
                    if (args.length < 2) {
                        Utils.dispatchCommandList(clanPlayer, "kick.usage");
                        return true;
                    }
                    final Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        Utils.sendCommands(clanPlayer, sender, "command.online");
                        return true;
                    }
                    final ClanPlayer clanTarget = plugin.getClanPlayer(target.getUniqueId());
                    if (clanTarget.getClan() == null || clan.getID() != clanTarget.getClanID()) {
                        Utils.dispatchCommandList(clanPlayer, clanTarget, "kick.clan");
                        return true;
                    }
                    if (clanPlayer.equals(clanTarget)) {
                        Utils.dispatchCommandList(clanPlayer, clanTarget, "kick.cannot");
                        return true;
                    }
                    final ClanKickEvent clanKickEvent = new ClanKickEvent(clanPlayer, clanTarget);
                    plugin.getServer().getPluginManager().callEvent(clanKickEvent);
                    if (clanKickEvent.isCancelled()) {
                        return true;
                    }
                    Utils.alertClan(clan, clanPlayer, clanTarget, "kick");
                    Utils.dispatchCommandList(clanTarget, clanPlayer, "kick.target");
                    clanKickEvent.execute();
                    clan.saveAsync();
                    clanTarget.saveAsync();
                    return true;
                }
                case "delete": {
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.command.player");
                        return true;
                    }
                    if (!player.hasPermission("pvpclans.player.delete")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (clan == null) {
                        Utils.dispatchCommandList(clanPlayer, "delete.none");
                        return true;
                    }
                    if (!clan.isLeader(clanPlayer.getUniqueId())) {
                        Utils.dispatchCommandList(clanPlayer, "delete.only");
                        return true;
                    }
                    if (args.length == 1) {
                        if (plugin.getPendingDelete().contains(clanPlayer.getUniqueId())) {
                            Utils.dispatchCommandList(clanPlayer, "delete.request");
                            return true;
                        }
                        plugin.getPendingDelete().add(clanPlayer.getUniqueId());
                        Utils.dispatchCommandList(clanPlayer, "delete.clan");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("confirm")) {
                        if (!plugin.getPendingDelete().contains(clanPlayer.getUniqueId())) {
                            Utils.dispatchCommandList(clanPlayer, "delete.cannot");
                            return true;
                        }
                        final ClanDeleteEvent clanDeleteEvent = new ClanDeleteEvent(clan);
                        plugin.getServer().getPluginManager().callEvent(clanDeleteEvent);
                        if (clanDeleteEvent.isCancelled()) {
                            return true;
                        }
                        Utils.alertClan(clan, clanPlayer, clanPlayer, "delete");
                        clanDeleteEvent.execute();
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("cancel")) {
                        if (!plugin.getPendingDelete().contains(clanPlayer.getUniqueId())) {
                            Utils.dispatchCommandList(clanPlayer, "delete.cannot");
                            return true;
                        }
                        plugin.getPendingDelete().remove(clanPlayer.getUniqueId());
                        Utils.dispatchCommandList(clanPlayer, "delete.cancel");
                        return true;
                    }
                    Utils.dispatchCommandList(clanPlayer, "delete.usage");
                    return true;
                }
                case "coins": {
                    if (!sender.hasPermission("pvpclans.admin.coins")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    runSet(clanPlayer, sender, args, "coins");
                    return true;
                }
                case "xp": {
                    if (!sender.hasPermission("pvpclans.admin.xp")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    runSet(clanPlayer, sender, args, "xp");
                    return true;
                }
                case "perk": {
                    if (!sender.hasPermission("pvpclans.admin.perk")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (args.length < 4) {
                        Utils.sendCommands(clanPlayer, sender, "perk.usage");
                        return true;
                    }
                    Player target = plugin.getServer().getPlayer(args[2]);
                    if (target == null) {
                        Utils.sendCommands(clanPlayer, sender, "command.online");
                        return true;
                    }
                    ClanPlayer clanTarget = plugin.getClanPlayer(target.getUniqueId());
                    Clan targetClan = clanTarget.getClan();
                    if (targetClan == null) {
                        Utils.sendCommands(clanPlayer, sender, "perk.none");
                        return true;
                    }
                    if (!Utils.isValidPerk(args[3])) {
                        Utils.sendCommands(clanPlayer, sender, "command.number");
                        return true;
                    }
                    String perk = args[3];

                    if (!plugin.getPerks().containsKey(perk)) {
                        if (clanPlayer != null) {
                            for (String command : plugin.getFileUtils().language.getStringList("perk.valid")) {
                                Utils.dispatchCommand(clanPlayer, command.replace("{perk}", perk));
                            }
                            return true;
                        }
                        for (String command : plugin.getFileUtils().language.getStringList("console.perk.valid")) {
                            Utils.sendMessage(sender, command.replace("{perk}", perk));
                        }
                        return true;
                    }
                    Perk currentPerk = plugin.getPerks().get(perk);
                    if (args[1].equals("remove")) {
                        targetClan.removePerk(perk);
                        targetClan.saveAsync();
                            if (currentPerk.getConfig().contains("leader-remove")) {
                                for (String command : currentPerk.getConfig().getStringList("leader-remove")) {
                                    Utils.dispatchCommand(plugin.getClanPlayer(targetClan.getLeader()), command);
                                }
                            }
                            for (UUID member : targetClan.getMembers()) {
                                if (!targetClan.isModerator(member)) {
                                    if (currentPerk.getConfig().contains(perk + ".remove.member")) {
                                        for (String command : currentPerk.getConfig().getStringList(perk + ".remove.member")) {
                                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("{player}", plugin.getClanPlayer(member).getName()));
                                        }
                                    }
                                } else {
                                    if (currentPerk.getConfig().contains(perk + ".remove.moderator")) {
                                        for (String command : currentPerk.getConfig().getStringList(perk + ".remove.moderator")) {
                                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("{player}", plugin.getClanPlayer(member).getName()));
                                        }
                                    }
                                }
                            }
                        if (clanPlayer != null) {
                            for (String command : plugin.getFileUtils().language.getStringList("perk.remove")) {
                                Utils.dispatchCommand(clanPlayer, command.replace("{clan}", targetClan.getName()).replace("{perk}", perk));
                            }
                            return true;
                        }
                        for (String command : plugin.getFileUtils().language.getStringList("console.perk.remove")) {
                            Utils.sendMessage(sender, command.replace("{clan}", targetClan.getName()).replace("{perk}", perk));
                        }
                        return true;
                    }
                    if (args[1].equals("add")) {
                        if (args.length == 6) {
                            if (!Utils.isLong(args[4]) || !Utils.isLong(args[5])) {
                                Utils.sendCommands(clanPlayer, sender, "command.number");
                                return true;
                            }
                            targetClan.addPerk(currentPerk.getIdentifier(), Integer.parseInt(args[4]), Integer.parseInt(args[5]), true);
                            targetClan.saveAsync();
                            if (currentPerk.getConfig().contains("leader")) {
                                for (String command : currentPerk.getConfig().getStringList("leader")) {
                                    Utils.dispatchCommand(plugin.getClanPlayer(targetClan.getLeader()), command.replace("{value}", args[4]).replace("{value_last}", args[5]));
                                }
                            }
                            for (UUID member : targetClan.getMembers()) {
                                if (!targetClan.isModerator(member)) {
                                    if (currentPerk.getConfig().contains("member")) {
                                        for (String command : currentPerk.getConfig().getStringList("member")) {
                                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("{player}", plugin.getClanPlayer(member).getName()).replace("{value}", args[4]).replace("{value_last}", args[5]));
                                        }
                                    }
                                } else {
                                    if (currentPerk.getConfig().contains("moderator")) {
                                        for (String command : currentPerk.getConfig().getStringList("moderator")) {
                                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("{player}", plugin.getClanPlayer(member).getName()).replace("{value}", args[4]).replace("{value_last}", args[5]));
                                        }
                                    }
                                }
                            }
                            if (clanPlayer != null) {
                                for (String command : plugin.getFileUtils().language.getStringList("perk.add")) {
                                    Utils.dispatchCommand(clanPlayer, command.replace("{clan}", targetClan.getName()).replace("{perk}", perk).replace("{value}", args[4]).replace("{value_last}", args[5]));
                                }
                                return true;
                            }
                            for (String command : plugin.getFileUtils().language.getStringList("console.perk.add")) {
                                Utils.sendMessage(sender, command.replace("{clan}", targetClan.getName()).replace("{perk}", perk).replace("{value}", args[4]).replace("{value_last}", args[5]));
                            }
                            return true;
                        }
                    }
                    if (clanPlayer != null) {
                        Utils.dispatchCommandList(clanPlayer, "perk.usage");
                        return true;
                    }
                    Utils.sendMessageList(sender, "console.perk.usage");
                    return true;
                }
                case "c":
                case "chat": {
                    if (clanPlayer == null) {
                        Utils.sendMessageList(sender, "console.command.player");
                        return true;
                    }
                    if (!sender.hasPermission("pvpclans.player.chat")) {
                        Utils.dispatchCommandList(clanPlayer, "command.permission");
                        return true;
                    }
                    if (clan == null) {
                        Utils.dispatchCommandList(clanPlayer, "chat.none");
                        return true;
                    }
                    if (args.length < 2) {
                        Utils.dispatchCommandList(clanPlayer, "chat.usage");
                        return true;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String text = Utils.color(sb.toString().trim());
                    List<String> commands;
                    if (clan.isLeader(clanPlayer.getUniqueId())) {
                        commands = Utils.getCommands("chat.leader");
                    } else if (clan.isModerator(clanPlayer.getUniqueId())) {
                        commands = Utils.getCommands("chat.moderator");
                    } else {
                        commands = Utils.getCommands("chat.member");
                    }
                    for (String command : commands) {
                        Utils.dispatchCommand(plugin.getClanPlayer(clan.getLeader()), command.replace("{member}", player.getName()).replace("{text}", text));
                    }
                    List<UUID> members = clan.getMembers();
                    List<UUID> moderators = clan.getModerators();
                    members.removeAll(moderators);
                    for (UUID member : members) {
                        for (String command : commands) {
                            Utils.dispatchCommand(plugin.getClanPlayer(member), command.replace("{member}", player.getName()).replace("{text}", text));
                        }
                    }
                    for (UUID moderator : moderators) {
                        for (String command : commands) {
                            Utils.dispatchCommand(plugin.getClanPlayer(moderator), command.replace("{member}", player.getName()).replace("{text}", text));
                        }
                    }
                    return true;
                }
                default: {
                    if (clanPlayer == null) {
                        for (String message : Utils.getCommands("console.command.unknown")) {
                            Utils.sendMessage(sender, message.replace("{command}", args[0]));
                        }
                        return true;
                    }
                    for (String command : Utils.getCommands("command.unknown")) {
                        Utils.dispatchCommand(clanPlayer, command.replace("{command}", args[0]));
                    }
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    private void broadcast(final String text, final String[] args) {
        if (args[1].equalsIgnoreCase("null")) {
            plugin.getServer().broadcastMessage(text);
            return;
        }
        plugin.getServer().broadcast(text, args[1]);
    }

    private void runSet(ClanPlayer clanPlayer, CommandSender sender, String[] args, String type) {
        if (args.length != 3) {
            Utils.sendCommands(clanPlayer, sender, type + ".usage");
            return;
        }
        final Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            Utils.sendCommands(clanPlayer, sender, "command.online");
            return;
        }
        if (!Utils.isLong(args[2])) {
            Utils.sendCommands(clanPlayer, sender, "command.number");
            return;
        }
        ClanPlayer clanTarget = plugin.getClanPlayer(target.getUniqueId());
        long toSet;
        ClanStats clanStats = null;
        Clan targetClan = clanTarget.getClan();
        if (targetClan != null) {
            clanStats = clanTarget.getStats();
            if (type.equals("coins")) {
                toSet = clanStats.getCoins();
            } else {
                toSet = clanStats.getXp();
            }
        } else {
            toSet = clanTarget.getCoins();
        }
        Boolean setVal = null;
        final long amount = Long.parseLong(args[2].replace("+", "").replace("-", ""));
        long set = Long.parseLong(args[2]);
        if (args[2].contains("+")) {
            set = toSet + Long.parseLong(args[2].replace("+", ""));
            setVal = true;
        } else if (args[2].contains("-")) {
            set = toSet - Long.parseLong(args[2].replace("-", ""));
            setVal = false;
        }
        long bonusValue = 0;
        String path = "";
        if (targetClan != null) {
            if (setVal == null || setVal) {
                if (type.equals("xp")) {
                    Clan clan = plugin.getClan(clanTarget.getClanID());
                    if (clan.hasPerk("xp-bonus") && clan.isPerkActive("xp-bonus")) {
                        int[] value = clan.getPerk("xp-bonus");
                        long add = (long) (Long.parseLong(args[2]) * (value[0] / 100.0f));
                        bonusValue = add;
                        path = "-bonus";
                        set += add;
                    }
                } else if (type.equals("coins")) {
                    Clan clan = plugin.getClan(clanTarget.getClanID());
                    if (clan.hasPerk("coins-bonus") && clan.isPerkActive("coins-bonus")) {
                        int[] value = clan.getPerk("coins-bonus");
                        long add = (long) (Long.parseLong(args[2]) * (value[0] / 100.0f));
                        bonusValue = add;
                        path = "-bonus";
                        set += add;
                    }
                }
            }
        }
        if (set < 0) {
            if (clanPlayer != null) {
                Utils.dispatchCommandList(clanPlayer, "command.bounds");
                return;
            }
            Utils.sendMessageList(sender, "console.command.bounds");
            return;
        }
        if (setVal == null) {
            if (clanStats != null) {
                if (type.equals("coins")) {
                    ClanSetCoinsEvent clanSetCoinsEvent = new ClanSetCoinsEvent(clanStats, set);
                    plugin.getServer().getPluginManager().callEvent(clanSetCoinsEvent);
                    if (clanSetCoinsEvent.isCancelled()) {
                        return;
                    }
                    clanSetCoinsEvent.execute();
                    clanStats.requestSave();
                } else {
                    ClanSetXpEvent clanSetXpEvent = new ClanSetXpEvent(clanStats, set);
                    plugin.getServer().getPluginManager().callEvent(clanSetXpEvent);
                    if (clanSetXpEvent.isCancelled()) {
                        return;
                    }
                    clanSetXpEvent.execute();
                    clanStats.requestSave();
                }
            } else {
                clanTarget.setCoins(set);
                clanTarget.requestSave();
            }
            for (String message : Utils.getCommands(type + ".set" + path)) {
                Utils.dispatchCommand(clanTarget, message.replace("{" + type + "}", args[2]).replace("{" + type + "_bonus}", String.valueOf(bonusValue)));
            }
            if (clanPlayer != null) {
                for (String message : Utils.getCommands(type + ".player-set")) {
                    Utils.dispatchCommand(clanPlayer, clanTarget, message.replace("{" + type + "}", args[2]));
                }
                return;
            }
            for (String command : Utils.getCommands("console." + type + ".set")) {
                Utils.sendMessage(sender, command.replace("{" + type + "}", args[2]).replace("{target}", target.getName()));
            }
            return;
        }
        if (setVal) {
            if (clanStats != null) {
                if (type.equals("coins")) {
                    ClanAddCoinsEvent clanAddCoinsEvent = new ClanAddCoinsEvent(clanStats, amount);
                    plugin.getServer().getPluginManager().callEvent(clanAddCoinsEvent);
                    if (clanAddCoinsEvent.isCancelled()) {
                        return;
                    }
                    clanAddCoinsEvent.execute();
                    clanStats.requestSave();
                } else {
                    ClanAddXpEvent clanAddXpEvent = new ClanAddXpEvent(clanStats, amount);
                    plugin.getServer().getPluginManager().callEvent(clanAddXpEvent);
                    if (clanAddXpEvent.isCancelled()) {
                        return;
                    }
                    clanAddXpEvent.execute();
                    clanStats.requestSave();
                }
            } else {
                clanTarget.setCoins(set);
                clanTarget.requestSave();
            }
            for (String message : Utils.getCommands(type + ".add" + path)) {
                Utils.dispatchCommand(clanTarget, message.replace("{" + type + "}", args[2]).replace("{" + type + "_bonus}", String.valueOf(bonusValue)));
            }
            if (clanPlayer != null) {
                for (String message : Utils.getCommands(type + ".player-add")) {
                    Utils.dispatchCommand(clanPlayer, clanTarget, message.replace("{" + type + "}", args[2]));
                }
                return;
            }
            for (String command : Utils.getCommands("console." + type + ".add")) {
                Utils.sendMessage(sender, command.replace("{" + type + "}", args[2]).replace("{target}", target.getName()));
            }
        } else {
            if (clanStats != null) {
                if (type.equals("coins")) {
                    ClanRemoveCoinsEvent clanRemoveCoinsEvent = new ClanRemoveCoinsEvent(clanStats, amount);
                    plugin.getServer().getPluginManager().callEvent(clanRemoveCoinsEvent);
                    if (clanRemoveCoinsEvent.isCancelled()) {
                        return;
                    }
                    clanRemoveCoinsEvent.execute();
                    clanStats.requestSave();
                } else {
                    ClanRemoveXpEvent clanRemoveXpEvent = new ClanRemoveXpEvent(clanStats, amount);
                    plugin.getServer().getPluginManager().callEvent(clanRemoveXpEvent);
                    if (clanRemoveXpEvent.isCancelled()) {
                        return;
                    }
                    clanRemoveXpEvent.execute();
                    clanStats.requestSave();
                }
            } else {
                clanTarget.setCoins(set);
                clanTarget.requestSave();
            }
            for (String message : Utils.getCommands(type + ".remove")) {
                Utils.dispatchCommand(clanTarget, message.replace("{" + type + "}", args[2]));
            }
            if (clanPlayer != null) {
                for (String message : Utils.getCommands(type + ".player-remove")) {
                    Utils.dispatchCommand(clanPlayer, clanTarget, message.replace("{" + type + "}", args[2]));
                }
                return;
            }
            for (String command : Utils.getCommands("console." + type + ".remove")) {
                Utils.sendMessage(sender, command.replace("{" + type + "}", args[2]).replace("{target}", target.getName()));
            }
        }
    }
}
