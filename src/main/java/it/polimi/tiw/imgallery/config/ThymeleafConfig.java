package it.polimi.tiw.imgallery.config;

import it.polimi.tiw.imgallery.utils.MultiPathMessageResolver;
import it.polimi.tiw.imgallery.utils.TemplateEngineRepo;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener()
public class ThymeleafConfig implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    private TemplateEngine templateEngine;

    public ThymeleafConfig() {
    }

    private TemplateEngine createTemplateEngine(ServletContext sc){
        this.templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolveTemplate(sc));
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.setMessageResolver(new MultiPathMessageResolver(sc, "templates/locale"));
        return this.templateEngine;
    }

    private ITemplateResolver resolveTemplate(ServletContext sc){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(sc);
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCacheable(true);
        return templateResolver;
    }

    public void contextInitialized(ServletContextEvent sce) {
        this.templateEngine = createTemplateEngine(sce.getServletContext());
        TemplateEngineRepo.storeTemplateEngine(sce.getServletContext(), this.templateEngine);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        this.templateEngine.clearDialects();
        this.templateEngine.clearTemplateCache();
        this.templateEngine = null;
        TemplateEngineRepo.removeTemplateEngine(sce.getServletContext());
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attribute
         is replaced in a session.
      */
    }
}
