package repository;

import model.Cena;
import util.Mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CenaDAO {

    public static Cena findCenaById(int id) throws SQLException {
        Connection conn = Mysql.getConnection();
        String sql = "SELECT * FROM cenas WHERE id_cena = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        Cena cena = null;

        if (rs.next()) {
            cena = new Cena();
            cena.setIdCena(rs.getInt("id_cena"));
            cena.setTitulo(rs.getString("titulo"));
            cena.setDescricao(rs.getString("descricao"));
        }

        rs.close();
        stmt.close();
        conn.close();

        return cena;
    }

    public static List<Cena> findAll() throws SQLException {
        Connection conn = Mysql.getConnection();
        String sql = "SELECT * FROM cenas";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<Cena> cenas = new ArrayList<>();
        while (rs.next()) {
            Cena cena = new Cena();
            cena.setIdCena(rs.getInt("id_cena"));
            cena.setTitulo(rs.getString("titulo"));
            cena.setDescricao(rs.getString("descricao"));

            cenas.add(cena);
        }

        rs.close();
        ps.close();
        conn.close();

        return cenas;
    }
}
