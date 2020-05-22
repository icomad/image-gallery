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

@WebServlet("/albums")
public class AlbumController extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        this.templateEngine = TemplateEngineRepo.getTemplateEngine(getServletContext());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var title = request.getParameter("title");

        if (title == null || title.isBlank()){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "", "/dashboard");
            return;
        }

        var user = (User) request.getSession().getAttribute("user");
        var albumService = new AlbumService();
        try {
            var album = albumService.create(title, user.getId());
            if (album == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "", "/dashboard");
                return;
            }
            FlashScopeMessageHandler.handleSuccessMessage(request, response, "", "/albums?albumId=" + album.getId());
        } catch (SQLException e) {
            var error = DbErrorHandler.evaluateError(e.getErrorCode());
            FlashScopeMessageHandler.handleErrorMessage(request, response, error, "/dashboard");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var albumId = request.getParameter("albumId");

        if (albumId==null || albumId.isBlank()){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "albumNotSelected", "/dashboard");
            return;
        }

        var webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        var albumService = new AlbumService();
        try {
            var album = albumService.findOneById(Integer.parseInt(albumId));
            if (album == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "", "/dashboard");
                return;
            }
            webContext.setVariable("album", album);
            this.templateEngine.process("album", webContext, response.getWriter());
        } catch (SQLException e) {
            var error = DbErrorHandler.evaluateError(e.getErrorCode());
            FlashScopeMessageHandler.handleErrorMessage(request, response, error, "/dashboard");
        } catch (NumberFormatException e){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "Selected Wrong Album Id", "/dashboard");
        }
    }
}
