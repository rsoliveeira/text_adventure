package repository;

import model.Cena;
import model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    public static Item findItemById(Integer id) {
        return new Item();
    }

    public static List<Item> findItensByScene(Cena cena) throws SQLException {
        Connection connection = Mysql.getConnection();
        String sql = "SELECT * FROM itens WHERE id_cena = ?;"; // Alterei para filtrar por id_cena
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, cena.getIdCena());
        ResultSet resultSet = ps.executeQuery();

        List<Item> itens = new ArrayList<>();
        while (resultSet.next()) {
            Item item = new Item();
            item.setIdItem(resultSet.getInt("id_item"));
            item.setNome(resultSet.getString("nome"));
            item.setDescricao(resultSet.getString("descricao"));  // Preenchendo a descrição do item
            // preencher o restante das propriedades

            int idCena = resultSet.getInt("id_cena");
            Cena cenaAtual = CenaDAO.findCenaById(idCena);

            item.setCena(cenaAtual);
            itens.add(item);
        }

        return itens;
    }
}