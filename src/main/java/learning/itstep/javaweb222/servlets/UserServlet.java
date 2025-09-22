package learning.itstep.javaweb222.servlets;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import learning.itstep.javaweb222.data.dto.User;

@Singleton
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User()
                .setId(UUID.randomUUID())
                .setName("Петрович")
                .setEmail("user@i.ua");
        Gson gson = new Gson();
        resp.setHeader("Content-Type", "application/json");
        resp.getWriter().print(
                gson.toJson(user)
        );
    }
}

       