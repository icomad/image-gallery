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

@WebServlet("/signin")
public class SignInController extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        this.templateEngine = TemplateEngineRepo.getTemplateEngine(getServletContext());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var username = request.getParameter("username");
        var password = request.getParameter("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "fieldsRequired", "/signin");
            return;
        }

        var session = request.getSession();
        var userService = new UserService();

        try {
            var user = userService.signIn(username, password);
            if (user == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "userNotFound", "/signin");
                return;
            }

            session.setAttribute("user", user);
            FlashScopeMessageHandler.handleSuccessMessage(request, response, "userSignedIn", "/dashboard");
        } catch (SQLException e) {
            var error = DbErrorHandler.evaluateError(e.getErrorCode());
            FlashScopeMessageHandler.handleErrorMessage(request, response, error, "/signin");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var session = request.getSession();
        if (session != null && session.getAttribute("user") != null){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "alreadySignedIn", "/dashboard");
            return;
        }

        var webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        this.templateEngine.process("signin", webContext, response.getWriter());
    }
}
