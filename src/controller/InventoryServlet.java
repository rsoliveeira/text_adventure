package controller;

import com.google.gson.Gson;
import repository.InventoryDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/inventory/*")
public class InventoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        int playerId = 1; // ID padrão do jogador

        try {
            if ("/add".equals(action)) {
                String itemName = request.getParameter("name");
                if (itemName != null) {
                    // Encontre o ID do item pelo nome e adicione ao inventário
                    int itemId = repository.ItemDAO.findItemByName(itemName).getId();
                    boolean success = InventoryDAO.addItemToInventory(playerId, itemId);
                    if (success) {
                        response.getWriter().write("{\"message\":\"" + itemName + " foi adicionado ao inventário.\"}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"message\":\"Falha ao adicionar o item.\"}");
                    }
                }
            } else if ("/check".equals(action)) {
                String itemName = request.getParameter("name");
                if (itemName != null) {
                    boolean hasItem = InventoryDAO.hasItemInInventory(playerId, itemName);
                    if (hasItem) {
                        response.getWriter().write("{\"message\":\"Você tem " + itemName + " no inventário.\"}");
                    } else {
                        response.getWriter().write("{\"message\":\"Você não possui " + itemName + " no inventário.\"}");
                    }
                }
            } else if ("/list".equals(action)) {
                List<String> inventory = InventoryDAO.getInventory(playerId);
                Gson gson = new Gson();
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(inventory));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
