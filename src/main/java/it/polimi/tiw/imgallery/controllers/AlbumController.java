package it.polimi.tiw.imgallery.controllers;

import it.polimi.tiw.imgallery.beans.User;
import it.polimi.tiw.imgallery.services.AlbumService;
import it.polimi.tiw.imgallery.services.CommentService;
import it.polimi.tiw.imgallery.services.ImageService;
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
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        this.templateEngine = TemplateEngineRepo.getTemplateEngine(getServletContext());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var title = request.getParameter("title");

        if (title == null || title.isBlank()){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "fieldsRequired", "/dashboard");
            return;
        }

        var user = (User) request.getSession().getAttribute("user");
        var albumService = new AlbumService();
        try {
            var album = albumService.create(title, user.getId());
            if (album == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "albumCreateError", "/dashboard");
                return;
            }
            FlashScopeMessageHandler.handleSuccessMessage(request, response, "albumCreated", "/albums?albumId=" + album.getId() + "&page=1");
        } catch (SQLException e) {
            var error = DbErrorHandler.evaluateError(e.getErrorCode());
            FlashScopeMessageHandler.handleErrorMessage(request, response, error, "/dashboard");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var albumId = request.getParameter("albumId");
        var page = request.getParameter("page");
        var imageId = request.getParameter("imageId");

        if (albumId==null || albumId.isBlank() || page == null || page.isBlank()){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "albumNotSelected", "/dashboard");
            return;
        }

        var webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        var albumService = new AlbumService();
        var imageService = new ImageService();
        var commentService = new CommentService();
        var pageSize = 5;
        try {
            var album = albumService.findOneById(Integer.parseInt(albumId));
            if (album == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "albumNotFound", "/dashboard");
                return;
            }
            var totalPages = imageService.getTotalPages(album.getId());
            var pageNum = Integer.parseInt(page);
            if (pageNum < 1) pageNum = 1;
            if (pageNum > totalPages) pageNum = totalPages;
            var start = pageNum > 1 ? pageSize * (pageNum-1) : 0;
            var images = imageService.findAllByAlbum(album.getId(), start, pageSize);

            if (imageId != null){
                var image = imageService.findOneById(Integer.parseInt(imageId));
                if (image == null){
                    FlashScopeMessageHandler.handleErrorMessage(request, response, "imageNotFound", "/albums?albumId="+album.getId()+"&page="+pageNum);
                    return;
                }
                var comments = commentService.findAllByImage(image.getId());
                webContext.setVariable("comments", comments);
                webContext.setVariable("image", image);
            }

            webContext.setVariable("pageNum", pageNum);
            webContext.setVariable("album", album);
            webContext.setVariable("totalPages", totalPages);
            webContext.setVariable("images", images);

            this.templateEngine.process("album", webContext, response.getWriter());
        } catch (SQLException e) {
            var error = DbErrorHandler.evaluateError(e.getErrorCode());
            FlashScopeMessageHandler.handleErrorMessage(request, response, error, "/dashboard");
        } catch (NumberFormatException e){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "numberFormatError", "/dashboard");
        }
    }
}
