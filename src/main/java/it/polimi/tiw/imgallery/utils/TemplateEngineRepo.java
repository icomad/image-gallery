package it.polimi.tiw.imgallery.utils;

import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;

public class TemplateEngineRepo {
    private static final String TEMPLATE_ENGINE_ATTR = "com.thymeleafexamples.thymeleaf3.TemplateEngineInstance";

    public static void storeTemplateEngine(ServletContext sc, TemplateEngine templateEngine){
        sc.setAttribute(TEMPLATE_ENGINE_ATTR, templateEngine);
    }

    public static TemplateEngine getTemplateEngine(ServletContext sc){
        return (TemplateEngine) sc.getAttribute(TEMPLATE_ENGINE_ATTR);
    }

    public static void removeTemplateEngine(ServletContext sc){
        sc.removeAttribute(TEMPLATE_ENGINE_ATTR);
    }
}
