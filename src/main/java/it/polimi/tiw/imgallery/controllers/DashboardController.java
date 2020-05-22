package it.polimi.tiw.imgallery.controllers;

import it.polimi.tiw.imgallery.beans.User;
import it.polimi.tiw.imgallery.services.AlbumService;
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

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        this.templateEngine = TemplateEngineRepo.getTemplateEngine(getServletContext());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        //var user = (User) request.getSession().getAttribute("user");
        var albumService = new AlbumService();
        try {
            var albums = albumService.findAll();
            webContext.setVariable("albums", albums);
            this.templateEngine.process("dashboard", webContext, response.getWriter());
        } catch (SQLException e) {
            var error = DbErrorHandler.evaluateError(e.getErrorCode());
            FlashScopeMessageHandler.handleErrorMessage(request, response, error, "/dashboard");
        }
    }
}
