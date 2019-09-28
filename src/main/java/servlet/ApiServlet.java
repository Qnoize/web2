package servlet;

import com.google.gson.Gson;
import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiServlet extends HttpServlet {

   // UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = null;
        try {
            id =  Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) { }
        Gson gson = new Gson();
        String json = "";

        if (req.getPathInfo().contains("auth")) {
            if (id == null) {
                json = gson.toJson(UserService.getInstance().getAllAuth());
            } else {
                json = gson.toJson(UserService.getInstance().isUserAuthById(id));
            }
        } else if (req.getPathInfo().contains("reg")) {
            if (id == null) {
                json = gson.toJson(UserService.getInstance().getAllUsers());
            } else {
                json = gson.toJson(UserService.getInstance().getUserById(id));
            }
        }

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(json);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        UserService.getInstance().authUser(new User(email, password));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        UserService.getInstance().addUser(new User(email, password));
        resp.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = null;
        try {
            id =  Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) { }
        if (req.getPathInfo().contains("auth")) {
            if (id == null) {
                UserService.getInstance().logoutAllUsers();
            }
        } else {
            if (id == null) {
                UserService.getInstance().deleteAllUser();
            }
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
