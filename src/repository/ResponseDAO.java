package repository;

import model.Resposta;
import util.Mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResponseDAO {
    public static Resposta findRespostaByAcaoId(int acaoId) throws SQLException {
        Connection conn = Mysql.getConnection();
        String sql = "SELECT * FROM respostas WHERE id_acao = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, acaoId);
        ResultSet rs = ps.executeQuery();

        Resposta resposta = null;
        if (rs.next()) {
            resposta = new Resposta(
                    rs.getInt("id_resposta"),
                    rs.getString("descricao"),
                    rs.getString("resultado"),
                    rs.getInt("id_acao")
            );
        }

        rs.close();
        ps.close();
        conn.close();

        return resposta;
    }
}
