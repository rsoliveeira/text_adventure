package repository;

import util.Mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryDAO {

    // Método para adicionar um item ao inventário
    public static boolean addItem(int itemId, int playerId) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "INSERT INTO inventario (id_item, id_jogador) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, itemId);
        ps.setInt(2, playerId); // Considerando que o inventário pertence a um jogador
        int rowsAffected = ps.executeUpdate();

        ps.close();
        connection.close();

        return rowsAffected > 0;
    }
}
