package controller;

import com.google.gson.Gson;
import model.Cena;
import repository.CenaDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/cenas")
public class CenaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        try {
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                Cena cena = CenaDAO.findCenaById(id);  // Chama o DAO para buscar a cena pelo ID
                if (cena != null) {
                    sendAsJson(response, cena);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                List<Cena> cenas = CenaDAO.findAll();  // Chama o DAO para buscar todas as cenas
                sendAsJson(response, cenas);
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private void sendAsJson(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(obj);
        response.getWriter().write(jsonResponse);
    }
}
