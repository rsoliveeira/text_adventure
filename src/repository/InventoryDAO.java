package repository;
import java.sql.SQLException;

import model.Cena;
import model.Item;
import util.Mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class InventoryDAO {

    public static boolean addItem(int itemId, int jogadorId) throws SQLException {
        Connection conn = Mysql.getConnection();
        String sql = "INSERT INTO inventario (id_item, id_jogador, quantidade) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, itemId);
        ps.setInt(2, jogadorId);
        ps.setInt(3, 1);  // Quantidade inicial 1

        int rowsAffected = ps.executeUpdate();
        ps.close();
        conn.close();

        return rowsAffected > 0;
    }

    public static List<Item> getInventory(int jogadorId) throws SQLException {
        Connection conn = Mysql.getConnection();
        String sql = "SELECT i.id_item, i.nome, i.descricao, inv.quantidade FROM inventario inv " +
                "JOIN itens i ON inv.id_item = i.id_item WHERE inv.id_jogador = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, jogadorId);

        ResultSet rs = ps.executeQuery();
        List<Item> inventory = new ArrayList<>();
        while (rs.next()) {
            int idItem = rs.getInt("id_item");
            String nome = rs.getString("nome");
            String descricao = rs.getString("descricao");
            int quantidade = rs.getInt("quantidade");

            // Usando o construtor que aceita quantidade
            Item item = new Item(idItem, nome, descricao, quantidade);
            inventory.add(item);
        }

        rs.close();
        ps.close();
        conn.close();

        return inventory;
    }

}
