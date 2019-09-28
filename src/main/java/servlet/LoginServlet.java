package servlet;

import com.google.gson.Gson;
import model.User;
import service.UserService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        if (email == null) {
            email = "please enter email";
        }

        String password = req.getParameter("password");
        User user = new User(email, password);
        UserService.getInstance().authUser(user);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(json);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        pageVariables.put("message", "Please, enter pass/email");
        String email = req.getParameter("email");
        if (email == null) {
            email = "please enter email";
        }
        String pass = req.getParameter("password");
        if (pass == null) {
            pass = "please enter pass";
        }
        pageVariables.put("email", email);
        pageVariables.put("password", pass);
        resp.getWriter().println(PageGenerator.getInstance().getPage("authPage.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        return pageVariables;
    }
}
