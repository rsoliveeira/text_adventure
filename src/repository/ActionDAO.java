package repository;

import model.Acao;
import model.Item;
import model.Resposta;
import util.Mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActionDAO {

    // Método para buscar uma ação pelo ID do item na cena
    public static Acao findAcaoByItem(int itemId) throws SQLException {
        Connection conn = Mysql.getConnection();
        String sql = "SELECT * FROM acoes WHERE id_item = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, itemId);
        ResultSet rs = ps.executeQuery();

        Acao acao = null;
        if (rs.next()) {
            acao = new Acao(
                    rs.getInt("id_acao"),
                    rs.getString("descricao"),
                    rs.getInt("id_item")
            );
        }

        rs.close();
        ps.close();
        conn.close();

        return acao;
    }

    // Executa a ação e retorna a resposta
    public static String executeAction(int itemId, int sceneId, int playerId) throws SQLException {
        Acao acao = findAcaoByItem(itemId);
        if (acao != null) {
            Resposta resposta = ResponseDAO.findRespostaByAcaoId(acao.getIdAcao());
            if (resposta != null) {
                // Verifica se há um resultado (mudança de cena, por exemplo)
                if (resposta.getResultado() != null) {
                    // Atualiza o estado do jogo com a nova cena
                    SaveGameDAO.saveGame(playerId, Integer.parseInt(resposta.getResultado()));
                    return resposta.getDescricao() + " Avançando para a cena " + resposta.getResultado() + ".";
                } else {
                    return resposta.getDescricao();
                }
            } else {
                return "Nenhuma resposta encontrada para essa ação.";
            }
        } else {
            return "Ação não encontrada.";
        }
    }

    // Executa a ação de "usar" e decide se o jogador avança de cena
    public static String executeUseAction(String inventoryItem, String sceneItem, int sceneId, int playerId) throws SQLException {
        // Verifica a ação com base no item de inventário e item da cena
        Item item = ItemDAO.findItemByNameAndScene(inventoryItem, sceneId);
        if (item != null) {
            return executeAction(item.getId(), sceneId, playerId);
        } else {
            return "Ação não pode ser realizada com esses itens.";
        }
    }
}
