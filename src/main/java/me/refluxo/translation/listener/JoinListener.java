package me.refluxo.translation.listener;

import me.refluxo.moduleloader.module.ModuleListener;
import me.refluxo.translation.util.TranslationUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@ModuleListener
public class JoinListener implements Listener {

    /**
     * This function is called when a player joins the server.
     *
     * @param event The event that is being called.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new TranslationUtil().getLanguage(event.getPlayer());
    }

}
