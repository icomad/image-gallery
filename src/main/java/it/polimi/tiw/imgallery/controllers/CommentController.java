package it.polimi.tiw.imgallery.controllers;

import it.polimi.tiw.imgallery.beans.User;
import it.polimi.tiw.imgallery.services.CommentService;
import it.polimi.tiw.imgallery.services.ImageService;
import it.polimi.tiw.imgallery.utils.DbErrorHandler;
import it.polimi.tiw.imgallery.utils.FlashScopeMessageHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/comments")
public class CommentController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var body = request.getParameter("body");
        var imageId = request.getParameter("imageId");
        var albumId = request.getParameter("albumId");
        var page = request.getParameter("page");

        var returnLink = "/albums?albumId=" + albumId + "&page=" + page + "&imageId=" + imageId;

        if (body == null || body.isBlank() || imageId == null || imageId.isBlank() || albumId == null || albumId.isBlank() || page == null || page.isBlank()){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "fieldsRequired", returnLink);
            return;
        }

        var user = (User) request.getSession().getAttribute("user");
        var commentService = new CommentService();
        var imageService = new ImageService();
        try {
            var image = imageService.findOneById(Integer.parseInt(imageId));
            if (image == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "imageNotFound", returnLink);
                return;
            }
            var comment = commentService.create(body, user.getId(), Integer.parseInt(imageId));
            if (comment == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "commentSentError", returnLink);
                return;
            }
            FlashScopeMessageHandler.handleSuccessMessage(request, response, "commentSent", returnLink);
        } catch (SQLException e) {
            var error = DbErrorHandler.evaluateError(e.getErrorCode());
            FlashScopeMessageHandler.handleErrorMessage(request, response, error, returnLink);
        } catch (NumberFormatException e){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "numberFormatError", returnLink);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FlashScopeMessageHandler.handleErrorMessage(request, response, "nothingToSee", "/dashboard");
    }
}
