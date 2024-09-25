import com.google.gson.Gson;
import model.Cena;
import model.Item;
import model.ResponseMessage;
import repository.CenaDAO;
import repository.InventoryDAO;
import repository.ItemDAO;
import repository.SaveGameDAO;
import repository.ActionDAO;
import repository.ResponseDAO;

import static spark.Spark.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

        post("/api/actions", (req, res) -> {
            res.type("application/json");
            String inventoryItem = req.queryParams("inventoryItem");
            String sceneItem = req.queryParams("sceneItem");
            int sceneId = Integer.parseInt(req.queryParams("sceneId"));

            try {
                // Processa a ação de usar o item
                String actionResult = ActionDAO.executeUseAction(inventoryItem, sceneItem, sceneId, 1);  // playerId = 1
                return gson.toJson(Map.of("message", actionResult));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("message", "Erro no servidor ao processar a ação."));
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
                List<String> inventory = InventoryDAO.getInventory(1); // Supondo id_jogador = 1
                return gson.toJson(inventory);
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro ao buscar inventário\"}";
            }
        });

        // Rota para processar o comando "get"
        post("/api/inventory/add", (req, res) -> {
            res.type("application/json"); // Define o tipo de resposta como JSON

            String itemName = req.queryParams("name");
            try {
                Item item = ItemDAO.findItemByName(itemName); // Busca o item pelo nome no banco
                if (item != null) {
                    boolean success = InventoryDAO.addItemToInventory(1, item.getId()); // Adiciona ao inventário do jogador
                    if (success) {
                        return "{\"message\":\"Item " + itemName + " foi adicionado ao inventário.\"}";
                    } else {
                        res.status(500);
                        return "{\"message\":\"Erro ao adicionar o item.\"}";
                    }
                } else {
                    res.status(404);
                    return "{\"message\":\"Item não encontrado.\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor\"}";
            }
        });


        post("/api/inventory/reset", (req, res) -> {
            res.type("application/json");
            try {
                boolean success = InventoryDAO.clearInventory(1); // Supondo playerId = 1
                if (success) {
                    return gson.toJson(new ResponseMessage("Inventário limpo e jogo reiniciado.")); // Resposta como JSON
                } else {
                    res.status(500);
                    return gson.toJson(new ResponseMessage("Erro ao reiniciar o jogo.")); // Retorna JSON com a mensagem de erro
                }
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(new ResponseMessage("Erro no servidor ao reiniciar o jogo."));
            }
        });



        // Rota para salvar o estado do jogo
        post("/api/save", (req, res) -> {
            int playerId = 1; // Supondo um jogador com ID 1
            int currentScene = Integer.parseInt(req.queryParams("scene"));

            try {
                boolean success = SaveGameDAO.saveGame(playerId, currentScene);
                if (success) {
                    return "{\"message\":\"Jogo salvo com sucesso\"}";
                } else {
                    res.status(500);
                    return "{\"message\":\"Falha ao salvar o jogo\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor ao salvar o jogo\"}";
            }
        });

        // Rota para carregar o estado do jogo
        get("/api/load", (req, res) -> {
            int playerId = 1; // Supondo um jogador com ID 1

            try {
                int loadedScene = SaveGameDAO.loadGame(playerId);
                if (loadedScene != -1) {
                    return "{\"currentScene\": " + loadedScene + ", \"message\":\"Jogo carregado com sucesso\"}";
                } else {
                    res.status(404);
                    return "{\"message\":\"Nenhum jogo salvo encontrado\"}";
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor ao carregar o jogo\"}";
            }
        });
    }
}
