package servlet;

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

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    private final UserService userService;

    public RegistrationServlet() {
        this.userService = UserService.getInstance();
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
        pageVariables.put("emailExist", false);
        pageVariables.put("email", email);
        pageVariables.put("password", pass);
        resp.getWriter().println(PageGenerator.getInstance().getPage("registerPage.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Map<String, Object> pageVariables = createPageVariablesMap(req);
        boolean emailExist = false;
        pageVariables.put("emailExist", emailExist);

        if ((password == null || email == null)||(password.equals("")||email.equals(""))) {
            pageVariables.put("message", "Bad data, please enter new");
        } else if(emailExist) {
            pageVariables.put("message", "This email is already exist, please enter new or login");
        }
        else {
            userService.addUser(new User(email, password));
            pageVariables.put("message", "Thank you for registration");
        }

        User user = new User(email, password);

        resp.getWriter().println(PageGenerator.getInstance().getPage("registerPage.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        return pageVariables;
    }
}
