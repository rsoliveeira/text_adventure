import com.google.gson.Gson;
import model.Cena;
import model.Item;
import repository.CenaDAO;
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

        // Habilitar CORS para todas as origens
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
            String nomeParam = req.queryParams("name");
            try {
                if (nomeParam != null) {
                    Item item = ItemDAO.findItemByNameAndScene(nomeParam, 1); // Considerando cena 1
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
    }
}
