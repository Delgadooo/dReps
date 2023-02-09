package br.com.delgadoo.dreps.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Account {

    public static ArrayList<Account> accounts = new ArrayList<>();

    @Getter @Setter
    private String playerName;

    @Getter @Setter
    private int positivo;

    @Getter @Setter
    private int negativo;

    @Getter @Setter
    private long cooldown;

    public Account(String playerName, int positivo, int negativo, long cooldown) {
        this.playerName = playerName;
        this.positivo = positivo;
        this.negativo = negativo;
        this.cooldown = cooldown;
    }

    public static Account getAccount(String player) {
        for (Account account : Account.accounts)
            if (account.getPlayerName().equalsIgnoreCase(player.toLowerCase())) return account;
        return null;
    }
}
