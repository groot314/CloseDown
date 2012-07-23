/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.groot314.CloseDown;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Geoffrey
 */
public class CloseDown extends JavaPlugin implements Listener {
    Logger log;
    
    
    public String KickMsg;
    public String LoginKickMsg;

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);


        //config stuff
        getConfig().options().copyDefaults(true);
        saveConfig();

        KickMsg = getConfig().getString("KickMsg");
        LoginKickMsg = getConfig().getString("LoginKickMsg");

        log = this.getLogger();
        log.info("Close Down has been Enabled!");
    }

    @Override
    public void onDisable() {

        log.info("Close Down has been Disabled!");
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {

        if (CloseDownConfigBoolean() == true) {
            if (event.getPlayer().hasPermission("closedown.login") == false) {

                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, LoginKickMsg);
                //player kicked because they dont have permission
            } else {
                //nothing because they have permission
            }
        } else if (CloseDownConfigBoolean() == false) {
            //closedown is not on so it wont kick

        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player == null) {
            if (cmd.getName().equalsIgnoreCase("closedown")) {
                if (args.length == 0) {
                    return false;
                }

                if (args[0].equalsIgnoreCase("on")) {
                    sender.sendMessage(ChatColor.GOLD + "Close down enabled");
                    setConfigBoolean = true;
                    setCloseDownBoolean();

                    for (Player kickee : Bukkit.getOnlinePlayers()) {
                        kickee.kickPlayer(getConfig().getString("KickMsg"));
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("off")) {
                    sender.sendMessage(ChatColor.GOLD + "Close down disabled");
                    setConfigBoolean = false;
                    setCloseDownBoolean();

                    return true;
                }
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(ChatColor.GOLD + "CloseDown" + ChatColor.RED + "---------------------------");
                    sender.sendMessage(ChatColor.GOLD + "/closedown help - View this Help Message");
                    sender.sendMessage(ChatColor.GOLD + "/closedown on - Make server go into close down");
                    sender.sendMessage(ChatColor.GOLD + "/closedown off - Take the server out of close down");
                    sender.sendMessage(ChatColor.GOLD + "/closedown check - See if the server is closed down");
                    
                    return true;
                }
                if (args[0].equalsIgnoreCase("check")) {
                    this.reloadConfig();
                    if (this.getConfig().getBoolean("ServerClosedDown") == true) {
                        sender.sendMessage(ChatColor.GOLD + "Server is on Close Down");
                    } else if (this.getConfig().getBoolean("ServerClosedDown") == false) {
                        sender.sendMessage(ChatColor.GOLD + "Server is not on Close Down");
                    }
                    return true;
                }

                if (args.length > 2) {
                    return false;
                }
            }
        } else {
            if (player.hasPermission("closedown.commands")) {
                if (cmd.getName().equalsIgnoreCase("closedown")) {
                    if (args.length == 0) {
                        return false;
                    }

                    if (args[0].equalsIgnoreCase("on")) {
                        sender.sendMessage(ChatColor.GOLD + "Server is now Closed Down");
                        setConfigBoolean = true;
                        setCloseDownBoolean();

                        for (Player kickee : Bukkit.getOnlinePlayers()) {
                            kickee.kickPlayer(KickMsg);
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("off")) {
                        sender.sendMessage(ChatColor.GOLD + "Server is no longer Closed Down");
                        System.out.println("Server is no longer on Close Down");
                        setConfigBoolean = false;
                        setCloseDownBoolean();

                        return true;
                    }
                    if (args[0].equalsIgnoreCase("help")) {
                        sender.sendMessage(ChatColor.GOLD + "CloseDown" + ChatColor.RED + "---------------------------");
                        sender.sendMessage(ChatColor.GOLD + "/closedown help - View this Help Message");
                        sender.sendMessage(ChatColor.GOLD + "/closedown on - Make server go into close down");
                        sender.sendMessage(ChatColor.GOLD + "/closedown off - Take the server out of close down");
                        sender.sendMessage(ChatColor.GOLD + "/closedown check - See if the server is closed down");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("check")) {
                        this.reloadConfig();
                        if (this.getConfig().getBoolean("ServerClosedDown") == true) {
                            sender.sendMessage(ChatColor.GOLD + "Server is on Close Down");
                        } else if (this.getConfig().getBoolean("ServerClosedDown") == false) {
                            sender.sendMessage(ChatColor.GOLD + "Server is not on Close Down");
                        }
                        return true;
                    }

                    if (args.length > 2) {
                        return false;
                    }
                }
            } else {
                player.sendMessage("You dont have access to that command");
                return true;
            }
        }


        return false;
    }
    public boolean setConfigBoolean;

    public void setCloseDownBoolean() {
        reloadConfig();

        if (setConfigBoolean == true) {
            this.getConfig().set("ServerClosedDown", true);
        } else if (setConfigBoolean == false) {
            this.getConfig().set("ServerClosedDown", false);
        }

        saveConfig();
    }

    private boolean CloseDownBoolean;
    public boolean CloseDownConfigBoolean() {
        this.reloadConfig();
        if(this.getConfig().getBoolean("ServerClosedDown") == true){
            CloseDownBoolean = true;
        }
        else if(this.getConfig().getBoolean("ServerClosedDown") == false){
            CloseDownBoolean = false;
        }
        return CloseDownBoolean;
    }
}
