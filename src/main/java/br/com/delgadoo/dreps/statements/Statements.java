package br.com.delgadoo.dreps.statements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.delgadoo.dreps.models.Account;
import br.com.delgadoo.dreps.dReps;
import org.bukkit.Bukkit;

public class Statements {

    public static Connection connection;

    private static void openConnection() {
        Statements.connection = dReps.connectionModel.getConnection();
    }

    public static void initialize() {
        try {
            openConnection();
            PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS dreps_pontos (nome varchar(16), positivo int, negativo int, cooldown long)");
            st.executeUpdate();
            st.close();
            loadAccounts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadAccounts() {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM dreps_pontos");
            ResultSet rs = st.executeQuery();
            while (rs.next())
                Account.accounts.add(new Account(rs.getString("nome"), rs.getInt("positivo"), rs.getInt("negativo"), rs.getLong("cooldown")));
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean exists(String playerName) {
        boolean exists = false;
        try {
            PreparedStatement st = connection.prepareStatement("SELECT nome FROM dreps_pontos WHERE nome=?");
            st.setString(1, playerName);
            ResultSet rs = st.executeQuery();
            exists = rs.next();
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public static void saveAccounts() {
        try {
            openConnection();
            PreparedStatement existsStatement = connection.prepareStatement("UPDATE dreps_pontos SET positivo=?, negativo=?, cooldown=? WHERE nome=?");
            PreparedStatement st = connection.prepareStatement("INSERT INTO dreps_pontos (nome, positivo, negativo, cooldown) VALUES (?, ?, ?, ?)");
            for (Account account : Account.accounts) {
                if (exists(account.getPlayerName())) {
                    existsStatement.setInt(1, account.getPositivo());
                    existsStatement.setInt(2, account.getNegativo());
                    existsStatement.setLong(3, account.getCooldown());
                    existsStatement.setString(4, account.getPlayerName());
                    existsStatement.executeUpdate();
                } else {
                    st.setString(1, account.getPlayerName());
                    st.setInt(2, account.getPositivo());
                    st.setInt(3, account.getNegativo());
                    st.setLong(4, account.getCooldown());
                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getTops() {
        List<String> tops = new ArrayList<>();

        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM dreps_pontos ORDER BY positivo DESC");
            ResultSet rs = stm.executeQuery();
            int i = 0;

            while (rs.next()) {
                if (rs.getString("nome") != null) {
                    if (i <= 10) {
                        i++;
                        tops.add(dReps.getInstance().getConfig().getString("Top-config.msg")
                                .replace("&", "§")
                                .replace("%rank%", String.valueOf(i))
                                .replace("%jogador%", rs.getString("nome"))
                                .replace("%pontos-positivos%", String.valueOf(rs.getInt("positivo")))
                        );
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage("");
                    Bukkit.getConsoleSender().sendMessage("§c[dReps - Logs] Ocorreu um erro ao tentar mostrar o TOP 10 pontos para um jogador.");
                    Bukkit.getConsoleSender().sendMessage("§cRelatório: §7Lista de TOP jogadores inexistente.");
                    Bukkit.getConsoleSender().sendMessage("");
                }
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§c[dReps - Logs] Ocorreu um erro ao tentar mostrar o TOP 10 pontos para um jogador.");
            Bukkit.getConsoleSender().sendMessage("§cRelatório: §7" + e.getMessage());
            Bukkit.getConsoleSender().sendMessage("");
        }
        return tops;
    }

}
