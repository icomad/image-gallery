package it.polimi.tiw.imgallery.controllers;

import it.polimi.tiw.imgallery.services.UserService;
import it.polimi.tiw.imgallery.utils.DbErrorHandler;
import it.polimi.tiw.imgallery.utils.FlashScopeMessageHandler;
import it.polimi.tiw.imgallery.utils.TemplateEngineRepo;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signup")
public class SignUpController extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        this.templateEngine = TemplateEngineRepo.getTemplateEngine(getServletContext());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var username = request.getParameter("username");
        var email = request.getParameter("email");
        var password = request.getParameter("password");
        var passwordCheck = request.getParameter("passwordCheck");

        if (username == null || username.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "fieldsRequired", "/signup");
            return;
        }

        if (!password.equals(passwordCheck)){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "passwordNoMatch", "/signup");
            return;
        }

        var userService = new UserService();
        var session = request.getSession();
        try {
            var user = userService.signUp(username, email, password);
            if (user == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "signUpError", "/signup");
                return;
            }
            session.setAttribute("user", user);
            FlashScopeMessageHandler.handleSuccessMessage(request, response, "userCreated", "/dashboard");
        } catch (SQLException e) {
            var error = DbErrorHandler.evaluateError(e.getErrorCode());
            FlashScopeMessageHandler.handleErrorMessage(request, response, error, "/signup");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var session = request.getSession();
        if (session != null && session.getAttribute("user") != null){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "alreadySigned", "/dashboard");
            return;
        }

        var webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        this.templateEngine.process("signup", webContext, response.getWriter());
    }
}
