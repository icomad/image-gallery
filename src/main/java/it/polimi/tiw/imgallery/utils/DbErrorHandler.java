package it.polimi.tiw.imgallery.utils;

public class DbErrorHandler {
    public static String evaluateError(int code){
        switch (code){
            case 1062:
                return "dbErrorUnique";
            case 1406:
                return "dbErrorMaxCharacters";
            default:
                return "dbConnectionError";
        }
    }
}
