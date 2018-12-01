//package org.anticheat.zues.command;
//
//import org.anticheat.zues.Zues;
//import org.anticheat.zues.check.Check;
//import org.bukkit.ChatColor;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RavenCommand implements CommandExecutor {
//
//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (args.length == 0) {
//            sender.sendMessage(ChatColor.DARK_RED + "Cookie 2" + Zues.instance.getDescription().getVersion() + " : Created By " + ChatColor.AQUA + "SilentOrb Developemnt Team");
//        } else if (args[0].equalsIgnoreCase("toggle")) {
//            if (!sender.hasPermission("anticheat.admin")
//                    && !sender.hasPermission("anticheat.toggle")) {
//                sender.sendMessage(ChatColor.RED + "No permission.");
//                return true;
//            }
//            if (args.length == 2) {
//                Check checkName = Zues.instance.getDataManager().getCheckByName(args[1]);
//
//                if (checkName == null) {
//                    sender.sendMessage(ChatColor.RED + "Check " + args[1] + " does not exist!");
//                    return true;
//                }
//
//                checkName.setEnabled(!checkName.isEnabled());
//                sender.sendMessage(ChatColor.GRAY + "Set check's state to: " + (checkName.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + checkName.isEnabled());
//                return true;
//            }
//            sender.sendMessage(ChatColor.RED + "Invalid arguments.");
//            return true;
//        } else if (args[0].equalsIgnoreCase("status")) {
//            if (!sender.hasPermission("cookie.admin")
//                    && !sender.hasPermission("raven.status")) {
//                sender.sendMessage(ChatColor.RED + "No permission.");
//                return true;
//            }
//            List<String> checkNames = new ArrayList<>();
//
//            for (Check checkLoop : Zues.instance.getDataManager().getChecks()) {
//                checkNames.add((checkLoop.isEnabled() ? ChatColor.GREEN + checkLoop.getName() : ChatColor.RED + checkLoop.getName()) + ChatColor.GRAY);
//            }
//
//            sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "-*---------------------------------------*-");
//            sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Cookie AC Status");
//            sender.sendMessage("");
//            sender.sendMessage(ChatColor.RED + "Checks: " + checkNames.toString());
//            sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "-*---------------------------------------*-");
//        }
//
//        return true;
//    }
//}
