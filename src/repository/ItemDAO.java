package repository;

import model.Cena;
import model.Item;
import util.Mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public static List<Item> findItensByIds(int... ids) throws SQLException {
        Connection connection = Mysql.getConnection();
        StringBuilder sql = new StringBuilder("SELECT * FROM itens WHERE id_item IN (");

        for (int i = 0; i < ids.length; i++) {
            sql.append("?");
            if (i < ids.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(");");

        PreparedStatement ps = connection.prepareStatement(sql.toString());
        for (int i = 0; i < ids.length; i++) {
            ps.setInt(i + 1, ids[i]);
        }

        ResultSet resultSet = ps.executeQuery();
        List<Item> itens = new ArrayList<>();

        while (resultSet.next()) {
            int itemId = resultSet.getInt("id_item");
            String nome = resultSet.getString("nome");
            String descricao = resultSet.getString("descricao");
            Item item = new Item(itemId, nome, descricao);
            itens.add(item);
        }

        resultSet.close();
        ps.close();
        connection.close();

        return itens;
    }

    public static Item findItemByName(String name) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "SELECT * FROM itens WHERE nome = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet resultSet = ps.executeQuery();

        Item item = null;
        if (resultSet.next()) {
            int itemId = resultSet.getInt("id_item");
            String nome = resultSet.getString("nome");
            String descricao = resultSet.getString("descricao");
            item = new Item(itemId, nome, descricao);
        }

        resultSet.close();
        ps.close();
        connection.close();

        return item;
    }


    public static List<Item> findAll() throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "SELECT * FROM itens";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();

        List<Item> itens = new ArrayList<>();
        while (resultSet.next()) {
            int itemId = resultSet.getInt("id_item");
            String nome = resultSet.getString("nome");
            String descricao = resultSet.getString("descricao");
            Item item = new Item(itemId, nome, descricao);
            itens.add(item);
        }

        resultSet.close();
        ps.close();
        connection.close();

        return itens;
    }

    public static Item findItemById(int id) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "SELECT * FROM itens WHERE id_item = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet resultSet = ps.executeQuery();

        Item item = null;
        if (resultSet.next()) {
            int itemId = resultSet.getInt("id_item");
            String nome = resultSet.getString("nome");
            String descricao = resultSet.getString("descricao");
            item = new Item(itemId, nome, descricao);
        }

        resultSet.close();
        ps.close();
        connection.close();

        return item;
    }

    public static Item findItemByNameAndScene(String nome, int idCena) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "SELECT * FROM itens WHERE nome = ? AND id_cena = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, nome);
        ps.setInt(2, idCena);
        ResultSet resultSet = ps.executeQuery();

        Item item = null;
        if (resultSet.next()) {
            int itemId = resultSet.getInt("id_item");
            String descricao = resultSet.getString("descricao");
            item = new Item(itemId, nome, descricao);
        }

        resultSet.close();
        ps.close();
        connection.close();

        return item;
    }



    public static List<Item> findItensByScene(Cena cena) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "SELECT * FROM itens WHERE id_cena = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, cena.getIdCena());
        ResultSet resultSet = ps.executeQuery();

        List<Item> itens = new ArrayList<>();
        while (resultSet.next()) {
            int itemId = resultSet.getInt("id_item");
            String nome = resultSet.getString("nome");
            String descricao = resultSet.getString("descricao");
            Item item = new Item(itemId, nome, descricao);
            itens.add(item);
        }

        resultSet.close();
        ps.close();
        connection.close();

        return itens;
    }

}
