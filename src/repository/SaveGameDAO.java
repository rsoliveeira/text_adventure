package repository;

import util.Mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaveGameDAO {

    // Salva o estado do jogo (fase atual e jogador)
    public static boolean saveGame(int playerId, int currentScene) throws SQLException {
        Connection conn = Mysql.getConnection();
        String sql = "UPDATE save_game SET current_scene = ? WHERE id_jogador = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, currentScene);
        ps.setInt(2, playerId);

        int rowsAffected = ps.executeUpdate();
        ps.close();
        conn.close();

        return rowsAffected > 0;
    }


    // Carrega o estado salvo do jogo para o jogador
    public static int loadGame(int playerId) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "SELECT current_scene FROM save_game WHERE id_jogador = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, playerId);
        ResultSet resultSet = ps.executeQuery();

        int currentScene = -1;
        if (resultSet.next()) {
            currentScene = resultSet.getInt("current_scene");
        }

        resultSet.close();
        ps.close();
        connection.close();

        return currentScene;
    }
}
