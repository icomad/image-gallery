package it.polimi.tiw.imgallery.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "UserIsSignedFilter")
public class UserIsSignedFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) resp;
        var session = request.getSession();
        var contextPath = request.getServletContext().getContextPath();
        var redirectURL = contextPath + "/signin";
        if (session.isNew() || session.getAttribute("user") == null) {
            request.setAttribute("flash.error", "You have to be signed in to view the page!");
            response.sendRedirect(redirectURL);
            return;
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
