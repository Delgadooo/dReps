package br.com.delgadoo.dreps.commands;

import br.com.delgadoo.dreps.managers.TimeManager;
import br.com.delgadoo.dreps.models.Account;
import br.com.delgadoo.dreps.statements.Statements;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static br.com.delgadoo.dreps.dReps.*;

public class RepCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (!(s instanceof Player)) return false;

        if (c.getName().equalsIgnoreCase("rep")) {
            Player p = (Player) s;
            if (Account.getAccount(p.getName()) == null) {
                Account.accounts.add(new Account(p.getName(), 0, 0, 0));
            }
            Account account = Account.getAccount(p.getName());
            if (!p.hasPermission(getInstance().getConfig().getString("Config.permissão"))) {
                p.sendMessage(getInstance().getConfig().getString("Mensagens.sem-permissão").replace("&", "§"));

            } else {
                if (args.length >= 1) {
                    String option = args[0];
                    if (option.equalsIgnoreCase("+")) {
                        if (account.getCooldown() == 0) {
                            if (args.length >= 2) {
                                Account target = Account.getAccount(args[1]);
                                if (target == null) {
                                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 5.0F, 1.0F);
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));
                                } else {
                                    if (target.getPlayerName().equalsIgnoreCase(p.getName())) {
                                        p.playSound(p.getLocation(), Sound.VILLAGER_NO, 5.0F, 1.0F);
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.erro-ponto-positivo").replace("&", "§"));
                                    } else {
                                        target.setPositivo(target.getPositivo() + 1);
                                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 5.0F, 1.0F);
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-positivo-enviado").replace("&", "§").replace("%jogador%", target.getPlayerName()));
                                        Statements.saveAccounts();
                                        long cooldown = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                        account.setCooldown(System.currentTimeMillis() + cooldown);
                                        if (Bukkit.getPlayer(target.getPlayerName()) != null) {
                                            Player playerTarget = Bukkit.getPlayer(target.getPlayerName());
                                            playerTarget.playSound(p.getLocation(), Sound.ORB_PICKUP, 5.0F, 1.0F);
                                            playerTarget.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-positivo-recebido")
                                                    .replace("&", "§")
                                                    .replace("%jogador%", p.getName())
                                            );
                                        }
                                    }
                                }
                            } else {
                                List<String> msgList = getInstance().getConfig().getStringList("Mensagens.comando-params");
                                msgList.replaceAll(m -> m.replace("&", "§"));
                                for (String msg : msgList) {
                                    p.sendMessage(msg);
                                }
                            }
                            return false;
                        } else {
                            if (System.currentTimeMillis() > account.getCooldown()) {
                                account.setCooldown(0);
                                if (args.length >= 2) {
                                    Account target = Account.getAccount(args[1]);
                                    if (target == null) {
                                        p.playSound(p.getLocation(), Sound.VILLAGER_NO, 5.0F, 1.0F);
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));
                                    } else {
                                        if (target.getPlayerName().equalsIgnoreCase(p.getName())) {
                                            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 5.0F, 1.0F);
                                            p.sendMessage(getInstance().getConfig().getString("Mensagens.erro-ponto-positivo").replace("&", "§"));
                                        } else {
                                            target.setPositivo(target.getPositivo() + 1);
                                            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 5.0F, 1.0F);
                                            p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-positivo-enviado").replace("&", "§").replace("%jogador%", target.getPlayerName()));
                                            Statements.saveAccounts();
                                            long cooldown = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                            account.setCooldown(System.currentTimeMillis() + cooldown);
                                            if (Bukkit.getPlayer(target.getPlayerName()) != null) {
                                                Player playerTarget = Bukkit.getPlayer(target.getPlayerName());
                                                playerTarget.playSound(p.getLocation(), Sound.ORB_PICKUP, 5.0F, 1.0F);
                                                playerTarget.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-positivo-recebido")
                                                        .replace("&", "§")
                                                        .replace("%jogador%", p.getName())
                                                );
                                            }
                                        }
                                    }
                                } else {
                                    List<String> msgList = getInstance().getConfig().getStringList("Mensagens.comando-params");
                                    msgList.replaceAll(m -> m.replace("&", "§"));
                                    for (String msg : msgList) {
                                        p.sendMessage(msg);
                                    }
                                }
                            } else {
                                p.sendMessage(getInstance().getConfig().getString("Mensagens.cooldown-msg").replace("&", "§").replace("%tempo%", TimeManager.getTime(account.getCooldown() - System.currentTimeMillis())));
                            }
                        }

                    } else {
                        if (option.equalsIgnoreCase("-")) {
                            if (account.getCooldown() == 0) {
                                if (args.length >= 2) {
                                    Account target = Account.getAccount(args[1]);
                                    if (target == null) {
                                        p.playSound(p.getLocation(), Sound.VILLAGER_NO, 5.0F, 1.0F);
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));
                                    } else {
                                        if (target.getPlayerName().equalsIgnoreCase(p.getName())) {
                                            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 5.0F, 1.0F);
                                            p.sendMessage(getInstance().getConfig().getString("Mensagens.erro-ponto-negativo").replace("&", "§"));
                                        } else {
                                            target.setNegativo(target.getNegativo() + 1);
                                            p.playSound(p.getLocation(), Sound.CLICK, 5.0F, 1.0F);
                                            p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-negativo-enviado").replace("&", "§").replace("%jogador%", target.getPlayerName()));
                                            Statements.saveAccounts();
                                            long time = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                            account.setCooldown(System.currentTimeMillis() + time);
                                            if (Bukkit.getPlayer(target.getPlayerName()) != null) {
                                                Player playerTarget = Bukkit.getPlayer(target.getPlayerName());
                                                playerTarget.playSound(p.getLocation(), Sound.CLICK, 5.0F, 1.0F);
                                                playerTarget.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-negativo-recebido")
                                                        .replace("&", "§")
                                                        .replace("%jogador%", p.getName())
                                                );
                                            }
                                        }
                                    }
                                } else {
                                    List<String> msgList = getInstance().getConfig().getStringList("Mensagens.comando-params");
                                    msgList.replaceAll(m -> m.replace("&", "§"));
                                    for (String msg : msgList) {
                                        p.sendMessage(msg);
                                    }
                                }
                                return false;
                            } else {
                                if (System.currentTimeMillis() > account.getCooldown()) {
                                    account.setCooldown(0);
                                    if (args.length >= 2) {
                                        Account target = Account.getAccount(args[1]);
                                        if (target == null) {
                                            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 5.0F, 1.0F);
                                            p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));
                                        } else {
                                            if (target.getPlayerName().equalsIgnoreCase(p.getName())) {
                                                p.playSound(p.getLocation(), Sound.VILLAGER_NO, 5.0F, 1.0F);
                                                p.sendMessage(getInstance().getConfig().getString("Mensagens.erro-ponto-negativo").replace("&", "§"));
                                            } else {
                                                target.setNegativo(target.getNegativo() + 1);
                                                p.playSound(p.getLocation(), Sound.CLICK, 5.0F, 1.0F);
                                                p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-negativo-enviado").replace("&", "§").replace("%jogador%", target.getPlayerName()));
                                                Statements.saveAccounts();
                                                long time = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                                account.setCooldown(System.currentTimeMillis() + time);
                                                if (Bukkit.getPlayer(target.getPlayerName()) != null) {
                                                    Player playerTarget = Bukkit.getPlayer(target.getPlayerName());
                                                    playerTarget.playSound(p.getLocation(), Sound.CLICK, 5.0F, 1.0F);
                                                    playerTarget.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-negativo-recebido")
                                                            .replace("&", "§")
                                                            .replace("%jogador%", p.getName())
                                                    );
                                                }
                                            }
                                        }
                                    } else {
                                        List<String> msgList = getInstance().getConfig().getStringList("Mensagens.comando-params");
                                        msgList.replaceAll(m -> m.replace("&", "§"));
                                        for (String msg : msgList) {
                                            p.sendMessage(msg);
                                        }
                                    }
                                } else {
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.cooldown-msg").replace("&", "§").replace("%tempo%", TimeManager.getTime(account.getCooldown() - System.currentTimeMillis())));
                                }
                            }
                        } else {
                            if (option.equalsIgnoreCase("ver")) {
                                if (args.length >= 2) {
                                    Account target = Account.getAccount(args[1]);
                                    if (target == null) {
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));
                                    } else {
                                        String targetPositivos = String.valueOf(target.getPositivo());
                                        String targetNegativos = String.valueOf(target.getNegativo());
                                        List<String> list = getInstance().getConfig().getStringList("Mensagens.ver-pontos");
                                        list.replaceAll(l -> l.replace("&", "§"));
                                        list.replaceAll(l -> l.replace("%jogador%", target.getPlayerName().toUpperCase(Locale.ROOT)));
                                        list.replaceAll(l -> l.replace("%pontos-positivos%", targetPositivos));
                                        list.replaceAll(l -> l.replace("%pontos-negativos%", targetNegativos));
                                        for (String msg : list) {
                                            p.sendMessage(msg);
                                        }
                                    }
                                } else {
                                    String accountPositivos = String.valueOf(account.getPositivo());
                                    String accountNegativos = String.valueOf(account.getNegativo());
                                    List<String> list = getInstance().getConfig().getStringList("Mensagens.ver-pontos");
                                    list.replaceAll(l -> l.replace("&", "§"));
                                    list.replaceAll(l -> l.replace("%jogador%", p.getName().toUpperCase(Locale.ROOT)));
                                    list.replaceAll(l -> l.replace("%pontos-positivos%", accountPositivos));
                                    list.replaceAll(l -> l.replace("%pontos-negativos%", accountNegativos));
                                    for (String msg : list) {
                                        p.sendMessage(msg);
                                    }
                                }
                            } else {
                                if (option.equalsIgnoreCase("top")) {
                                    List<String> lines = getInstance().getConfig().getStringList("Mensagens.msg-top");
                                    lines.replaceAll(l -> l.replace("&", "§"));
                                    List<String> topList = Statements.getTops();
                                    lines.replaceAll(l -> l.replace("%top10%", topList.toString().replace("[", "").replace("]", "").replace(", ", "\n")));
                                    for (String msg : lines) {
                                        p.sendMessage(msg);
                                    }
                                } else {
                                    List<String> msgList = getInstance().getConfig().getStringList("Mensagens.comando-params");
                                    msgList.replaceAll(m -> m.replace("&", "§"));
                                    for (String msg : msgList) {
                                        p.sendMessage(msg);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    List<String> msgList = getInstance().getConfig().getStringList("Mensagens.comando-params");
                    msgList.replaceAll(m -> m.replace("&", "§"));
                    for (String msg : msgList) {
                        p.sendMessage(msg);
                    }
                }
            }
        }
        return false;
    }

}
