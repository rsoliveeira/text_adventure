import com.google.gson.Gson;
import model.Cena;
import model.Item;
import repository.CenaDAO;
import repository.InventoryDAO;
import repository.ItemDAO;

import static spark.Spark.*;

import java.sql.SQLException;
import java.util.List;

public class Main {

    // Método para habilitar CORS no Spark
    public static void enableCORS(final String origin, final String methods, final String headers) {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Permite requisições do tipo OPTIONS
            if ("OPTIONS".equals(request.requestMethod())) {
                response.status(200);
            }
        });
    }

    public static void main(String[] args) {
        port(8080); // Define a porta do servidor
        Gson gson = new Gson(); // Instância do Gson para converter objetos em JSON

        // Habilita CORS para todas as origens (permitir chamadas do frontend PHP)
        enableCORS("*", "*", "*");

        // Rota GET para buscar todas as cenas ou uma cena específica por ID
        get("/api/cenas", (req, res) -> {
            res.type("application/json");
            String idParam = req.queryParams("id");
            try {
                if (idParam != null) {
                    int id = Integer.parseInt(idParam);
                    Cena cena = CenaDAO.findCenaById(id);
                    if (cena != null) {
                        return gson.toJson(cena);
                    } else {
                        res.status(404);
                        return "{\"message\":\"Cena não encontrada\"}";
                    }
                } else {
                    List<Cena> cenas = CenaDAO.findAll();
                    return gson.toJson(cenas);
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor\"}";
            }
        });

        // Rota GET para buscar item por nome e cena
        get("/api/items", (req, res) -> {
            res.type("application/json");
            String nomeParam = req.queryParams("name");
            try {
                if (nomeParam != null) {
                    // Busca o item pelo nome e pela cena 1 (pode ajustar essa parte se quiser usar cena dinâmica)
                    Item item = ItemDAO.findItemByNameAndScene(nomeParam, 1);
                    if (item != null) {
                        return gson.toJson(item);
                    } else {
                        res.status(404);
                        return "{\"message\":\"Item não encontrado\"}";
                    }
                } else {
                    res.status(400);
                    return "{\"message\":\"Nome do item não fornecido\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor\"}";
            }
        });

        // Rota POST para adicionar item ao inventário
        post("/api/inventory/add", (req, res) -> {
            res.type("application/json");
            String itemName = req.queryParams("name");
            try {
                // Aqui você precisa buscar o item pelo nome para obter o ID correto
                Item item = ItemDAO.findItemByName(itemName);
                if (item != null) {
                    boolean added = InventoryDAO.addItem(item.getId(), 1); // 1 representando o id do jogador ou usuário
                    if (added) {
                        return "{\"message\":\"Item adicionado ao inventário.\"}";
                    } else {
                        res.status(400);
                        return "{\"message\":\"Não foi possível adicionar o item ao inventário.\"}";
                    }
                } else {
                    res.status(404);
                    return "{\"message\":\"Item não encontrado.\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro ao adicionar o item ao inventário\"}";
            }
        });

        // Rota GET para buscar o inventário de um jogador (no caso, jogador id 1)
        get("/api/inventory", (req, res) -> {
            res.type("application/json");
            try {
                List<Item> inventory = InventoryDAO.getInventory(1); // ID do jogador pode ser dinâmico
                return gson.toJson(inventory);
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro ao buscar inventário\"}";
            }
        });
    }
}
