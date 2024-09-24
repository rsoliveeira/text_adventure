package repository;

import util.Mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // Adiciona um item ao inventário de um jogador
    public static boolean addItemToInventory(int playerId, int itemId) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "INSERT INTO inventario (id_item, id_jogador) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, itemId);
        ps.setInt(2, playerId);
        int rowsAffected = ps.executeUpdate();

        ps.close();
        connection.close();

        return rowsAffected > 0;
    }

    // Limpa o inventário de um jogador
    public static boolean clearInventory(int playerId) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "DELETE FROM inventario WHERE id_jogador = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, playerId);
        int rowsAffected = ps.executeUpdate();

        ps.close();
        connection.close();

        return rowsAffected > 0;
    }

    // Verifica se o jogador já possui um item no inventário
    public static boolean hasItemInInventory(int playerId, String itemName) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "SELECT COUNT(*) FROM inventario i JOIN itens it ON i.id_item = it.id_item WHERE it.nome = ? AND i.id_jogador = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, itemName);
        ps.setInt(2, playerId);
        ResultSet resultSet = ps.executeQuery();

        boolean hasItem = false;
        if (resultSet.next()) {
            hasItem = resultSet.getInt(1) > 0;
        }

        resultSet.close();
        ps.close();
        connection.close();

        return hasItem;
    }

    // Retorna todos os itens do inventário de um jogador
    public static List<String> getInventory(int playerId) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "SELECT it.nome FROM inventario i JOIN itens it ON i.id_item = it.id_item WHERE i.id_jogador = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, playerId);
        ResultSet resultSet = ps.executeQuery();

        List<String> inventory = new ArrayList<>();
        while (resultSet.next()) {
            inventory.add(resultSet.getString("nome"));
        }

        resultSet.close();
        ps.close();
        connection.close();

        return inventory;
    }
}
