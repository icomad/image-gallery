package it.polimi.tiw.imgallery.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FlashScopeMessageHandler {
    public static void handleErrorMessage(HttpServletRequest req, HttpServletResponse res, String message, String redirect) throws IOException {
        var redirectPath = req.getServletContext().getContextPath() + redirect;
        req.setAttribute("flash.error", message);
        res.sendRedirect(redirectPath);
    }

    public static void handleSuccessMessage(HttpServletRequest req, HttpServletResponse res, String message, String redirect) throws IOException {
        var redirectPath = req.getServletContext().getContextPath() + redirect;
        req.setAttribute("flash.success", message);
        res.sendRedirect(redirectPath);
    }
}
