package it.polimi.tiw.imgallery.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebFilter(filterName = "FlashScopeFilter")
public class FlashScopeFilter implements Filter {
    private static final String FLASH_SESSION_KEY = "FLASH_SESSION_KEY";

    public void destroy() {
    }

    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            var session = httpRequest.getSession(false);
            if (session != null) {
                Map<String, Object> flashParams = (Map<String, Object>) session.getAttribute(FLASH_SESSION_KEY);
                if (flashParams != null) {
                    flashParams.forEach(request::setAttribute);
                    session.removeAttribute(FLASH_SESSION_KEY);
                }
            }
        }

        chain.doFilter(request, response);

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            Map<String, Object> flashParams = new HashMap<>();
            Enumeration<String> e = httpRequest.getAttributeNames();
            while (e.hasMoreElements()) {
                String paramName = e.nextElement();
                if (paramName.startsWith("flash.")) {
                    Object value = request.getAttribute(paramName);
                    paramName = paramName.substring(6);
                    flashParams.put(paramName, value);
                }
            }
            if (flashParams.size() > 0) {
                var session = httpRequest.getSession(false);
                session.setAttribute(FLASH_SESSION_KEY, flashParams);
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
