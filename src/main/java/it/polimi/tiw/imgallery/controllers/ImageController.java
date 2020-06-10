package it.polimi.tiw.imgallery.controllers;

import it.polimi.tiw.imgallery.beans.User;
import it.polimi.tiw.imgallery.services.AlbumService;
import it.polimi.tiw.imgallery.services.ImageService;
import it.polimi.tiw.imgallery.utils.DbErrorHandler;
import it.polimi.tiw.imgallery.utils.FlashScopeMessageHandler;
import net.coobird.thumbnailator.Thumbnails;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;

@WebServlet("/images")
@MultipartConfig
public class ImageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var resourcesPath = System.getProperty("static.resources");
        var dirPath = resourcesPath + "/static/";
        Part filePart = request.getPart("imageFile");
        var title = request.getParameter("title");
        var description = request.getParameter("description");
        var albumId = request.getParameter("albumId");

        if (filePart == null || title == null || title.isBlank() || description == null || description.isBlank() || albumId == null || albumId.isBlank()){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "fieldsRequired", "/albums?albumId="+albumId+"&page=1");
            return;
        }

        var user = (User) request.getSession().getAttribute("user");
        var filePath = this.saveImageToStorage(filePart, dirPath);
        var imageService = new ImageService();
        var albumService = new AlbumService();
        try {
            var album = albumService.findOneById(Integer.parseInt(albumId));
            if (album == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "albumNotFound", "/dashboard");
                return;
            }

            var image = imageService.create(title, description, user.getId(), album.getId(), filePath);
            if (image == null){
                FlashScopeMessageHandler.handleErrorMessage(request, response, "imageCreationError", "/albums?albumId="+album.getId()+"&page=1");
                return;
            }
            FlashScopeMessageHandler.handleSuccessMessage(request, response, "imageCreated", "/albums?albumId="+album.getId()+"&page=1");
        } catch (SQLException e) {
            var error = DbErrorHandler.evaluateError(e.getErrorCode());
            FlashScopeMessageHandler.handleErrorMessage(request, response, error, "/albums?albumId="+albumId+"&page=1");
        } catch (NumberFormatException e){
            FlashScopeMessageHandler.handleErrorMessage(request, response, "numberFormatError", "/dashboard");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FlashScopeMessageHandler.handleErrorMessage(request, response, "nothingToSee", "/dashboard");
    }

    private String saveImageToStorage(Part filePart, String dirPath) throws IOException {
        File f = new File(dirPath);
        if (!f.isDirectory()) f.mkdir();
        String ogFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uniqueId = Long.toString(new Date().getTime());
        String fileName = uniqueId + ogFileName;
        File targetFile = new File(dirPath + fileName);
        File thumbnailFile = new File(dirPath + "thumbnail_" + fileName);
        try (OutputStream output = new FileOutputStream(targetFile); var thumbnail = new FileOutputStream(thumbnailFile); var thumbnailContent = filePart.getInputStream(); InputStream fileContent = filePart.getInputStream();) {
            fileContent.transferTo(output);
            Thumbnails.of(thumbnailContent).size(200, 200).toOutputStream(thumbnail);
        }
        return fileName;
    }
}
