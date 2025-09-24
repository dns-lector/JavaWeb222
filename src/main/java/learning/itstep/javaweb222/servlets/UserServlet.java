package learning.itstep.javaweb222.servlets;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import learning.itstep.javaweb222.data.dto.User;

@Singleton
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        // Автентифікація за RFC 7617                         // Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
        String authHeader = req.getHeader("Authorization");   // Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
        if(authHeader == null || "".equals(authHeader)) {
            resp.setStatus(401);
            resp.getWriter().print(
                gson.toJson("Missing 'Authorization' header")
            );
            return;
        }
        String authScheme = "Basic ";
        if( ! authHeader.startsWith(authScheme) ) {
            resp.setStatus(401);
            resp.getWriter().print(
                gson.toJson("Invalid Authorization scheme. Must be " + authScheme)
            );
            return;
        }
        String credentials = authHeader.substring( authScheme.length() );  // QWxhZGRpbjpvcGVuIHNlc2FtZQ==
        String userPass;
        try { 
            userPass = new String( 
                Base64.getDecoder().decode(credentials));   // Aladdin:open sesame
        }
        catch(IllegalArgumentException ex) {
            resp.setStatus(401);
            resp.getWriter().print(
                gson.toJson("Invalid credentials. Base64 decode error " + ex.getMessage())
            );
            return;
        }
        String[] parts = userPass.split(":", 2);   // [0]Aladdin  [1]open sesame
        if(parts.length != 2) {
            resp.setStatus(401);
            resp.getWriter().print(
                gson.toJson("Invalid user-pass. Missing ':' ")
            );
            return;
        }
        
        User user = new User()
                .setId(UUID.randomUUID())
                .setName("Петрович")
                .setEmail("user@i.ua");
        resp.setHeader("Content-Type", "application/json");
        resp.getWriter().print(
                gson.toJson(user)
        );
    }
}

/*
Д.З. Впровадити у курсовий проєкт фронтенд, налаштувати на ньому засоби
автентифікації.
На бекенді підготувати сервлет для автентифікації та реалізувати прийом
даних від фронтенду.
Використовувати процедуру, описану в розділі 2 стандарту RFC 7617 https://datatracker.ietf.org/doc/html/rfc7617#section-2
*/       