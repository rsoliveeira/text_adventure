import com.google.gson.Gson;
import model.Cena;
import model.Item;
import repository.CenaDAO;
import repository.ItemDAO;

import static spark.Spark.*;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        port(8080); // Define a porta do servidor
        Gson gson = new Gson(); // Instância do Gson para converter objetos em JSON

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

        // Rota GET para buscar todos os itens ou um item específico por ID
        get("/api/items", (req, res) -> {
            res.type("application/json");
            String idParam = req.queryParams("id");
            try {
                if (idParam != null) {
                    int id = Integer.parseInt(idParam);
                    Item item = ItemDAO.findItemById(id);
                    if (item != null) {
                        return gson.toJson(item);
                    } else {
                        res.status(404);
                        return "{\"message\":\"Item não encontrado\"}";
                    }
                } else {
                    List<Item> itens = ItemDAO.findAll();
                    return gson.toJson(itens);
                }
            } catch (SQLException e) {
                res.status(500);
                return "{\"message\":\"Erro no servidor\"}";
            }
        });
    }
}
