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

    public static void enableCORS(final String origin, final String methods, final String headers) {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            if ("OPTIONS".equals(request.requestMethod())) {
                response.status(200);
            }
        });
    }

    public static void main(String[] args) {
        port(8080); // Define a porta do servidor
        Gson gson = new Gson();

        enableCORS("*", "*", "*");

        // Rota para buscar a cena específica por ID
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
                    res.status(400);
                    return "{\"message\":\"ID da cena não fornecido\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor\"}";
            }
        });

        // Rota para buscar item pelo nome e cena
        get("/api/items", (req, res) -> {
            res.type("application/json");
            String itemName = req.queryParams("name");
            String sceneIdParam = req.queryParams("sceneId");
            try {
                if (itemName != null && sceneIdParam != null) {
                    int sceneId = Integer.parseInt(sceneIdParam);
                    Item item = ItemDAO.findItemByNameAndScene(itemName, sceneId);
                    if (item != null) {
                        return gson.toJson(item);
                    } else {
                        res.status(404);
                        return "{\"message\":\"Item não encontrado\"}";
                    }
                } else {
                    res.status(400);
                    return "{\"message\":\"Parâmetros inválidos\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor\"}";
            }
        });

        // Rota para adicionar item ao inventário
        post("/api/inventory/add", (req, res) -> {
            String itemName = req.queryParams("name");
            try {
                Item item = ItemDAO.findItemByName(itemName);
                if (item != null) {
                    boolean success = InventoryDAO.addItemToInventory(1, item.getId()); // Supondo id_jogador = 1
                    if (success) {
                        return "{\"message\":\"Item adicionado ao inventário\"}";
                    } else {
                        res.status(500);
                        return "{\"message\":\"Erro ao adicionar item\"}";
                    }
                } else {
                    res.status(404);
                    return "{\"message\":\"Item não encontrado\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor\"}";
            }
        });

        // Rota para verificar se o jogador tem o item no inventário
        get("/api/inventory/check", (req, res) -> {
            String itemName = req.queryParams("name");
            try {
                boolean hasItem = InventoryDAO.hasItemInInventory(1, itemName); // Supondo id_jogador = 1
                if (hasItem) {
                    return "{\"message\":\"Você tem " + itemName + " no inventário.\"}";
                } else {
                    return "{\"message\":\"Você não possui " + itemName + " no inventário.\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor\"}";
            }
        });

        // Rota para buscar o inventário
        get("/api/inventory/list", (req, res) -> {
            res.type("application/json");
            try {
                // Substitui findInventoryByPlayer por getInventory
                List<String> inventory = InventoryDAO.getInventory(1); // Supondo id_jogador = 1
                return gson.toJson(inventory);
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro ao buscar inventário\"}";
            }
        });

        // Rota para reiniciar o jogo e limpar o inventário
        post("/api/inventory/reset", (req, res) -> {
            try {
                boolean success = InventoryDAO.clearInventory(1); // Limpa o inventário do jogador 1
                if (success) {
                    return "{\"message\":\"Inventário limpo\"}";
                } else {
                    res.status(500);
                    return "{\"message\":\"Erro ao limpar inventário\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor\"}";
            }
        });
    }
}
