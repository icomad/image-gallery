package it.polimi.tiw.imgallery.services;

import it.polimi.tiw.imgallery.beans.User;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserService {
    private DataSource ds;

    public UserService(){
        try {
            this.ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/db");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public User mapDBToBean(ResultSet rs) throws SQLException {
        var user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        return user;
    }

    public User findOneById(int id) throws SQLException{
        var query = "SELECT * FROM users WHERE id = ?";
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query);){
            pStatement.setInt(1, id);
            try(var rs = pStatement.executeQuery();){
                if (rs.next()) return this.mapDBToBean(rs);
                return null;
            }
        }
    }

    public User signUp(String username, String email, String password) throws SQLException{
        var query = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            pStatement.setString(1, username);
            pStatement.setString(2, email);
            pStatement.setString(3, password);
            pStatement.executeUpdate();
            try(var rs = pStatement.getGeneratedKeys();){
                if (rs.next()) return this.findOneById(rs.getInt(1));
                return null;
            }
        }
    }

    public User signIn(String username, String password) throws SQLException {
        var query = "SELECT id FROM users WHERE username = ? AND password = ?";
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query);){
            pStatement.setString(1, username);
            pStatement.setString(2, password);
            try(var rs = pStatement.executeQuery();){
                if (rs.next()) return this.findOneById(rs.getInt(1));
                return null;
            }
        }
    }
}
