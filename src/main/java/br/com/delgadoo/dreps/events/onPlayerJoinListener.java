package br.com.delgadoo.dreps.events;

import br.com.delgadoo.dreps.models.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (Account.getAccount(p.getName()) == null) {
            Account.accounts.add(new Account(p.getName(), 0, 0, 0));
        }
    }
}
