package de.FSCode.ExtendedInventory.Commands;

import de.FSCode.ExtendedInventory.Utilities.GMessages;
import de.FSCode.ExtendedInventory.Utilities.GPermissions;
import de.FSCode.ExtendedInventory.Utilities.IMainframe;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public record ReloadCommand(IMainframe<JavaPlugin> plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(GMessages.CONSOLE_SENDER.getMessage());
            return false;
        }

        Player p = (Player) sender;
        if (!GPermissions.CMD_RELOAD.check(p)) {
            p.sendMessage(GMessages.NO_PERMISSION.getMessage());
            return false;
        }

        if(args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            p.sendMessage(GMessages.CMD_UNKNOWN.getMessage());
            return false;
        }

        plugin().reload();
        Bukkit.getScheduler().runTaskAsynchronously(plugin().getPluginInstance(), () -> {
            for (Player players : Bukkit.getOnlinePlayers()) {
                for (int i = 0; i <= 10; i++) {
                    if (GPermissions.INV_PAGES.check(p, String.valueOf(i)))
                        plugin().getInventoryHandler().setAllowedPages(players.getUniqueId(), i);
                }
            }
            p.sendMessage(GMessages.CMD_RELOAD.getMessage());
        });
        return true;
    }

}
