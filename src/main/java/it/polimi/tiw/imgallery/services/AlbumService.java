package it.polimi.tiw.imgallery.services;

import it.polimi.tiw.imgallery.beans.Album;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlbumService {
    private DataSource ds;

    public AlbumService(){
        try {
            this.ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/db");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public Album mapDBToBean(ResultSet rs) throws SQLException {
        var album = new Album();
        album.setId(rs.getInt("id"));
        album.setTitle(rs.getString("title"));
        album.setUserId(rs.getInt("user_id"));
        album.setCreatedAt(rs.getTimestamp("created_at"));
        return album;
    }

    public Album findOneById(int id) throws SQLException{
        var query = "SELECT * FROM albums WHERE id = ?";
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query);){
            pStatement.setInt(1, id);
            try(var rs = pStatement.executeQuery();){
                if (rs.next()) return this.mapDBToBean(rs);
                return null;
            }
        }
    }

    public List<Album> findAll() throws SQLException{
        var query = "SELECT * FROM albums ORDER BY created_at DESC";
        var albums = new ArrayList<Album>();
        try(var connection = this.ds.getConnection();
            var pStatement = connection.prepareStatement(query);
            var rs = pStatement.executeQuery();){
            while (rs.next()){
                albums.add(this.mapDBToBean(rs));
            }
        }
        return albums;
    }

    public List<Album> findAllByUser(int userId) throws SQLException{
        var query = "SELECT * FROM albums WHERE user_id = ? ORDER BY created_at DESC";
        var albums = new ArrayList<Album>();
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query);){
            pStatement.setInt(1, userId);
            try(var rs = pStatement.executeQuery();){
                while (rs.next()){
                    albums.add(this.mapDBToBean(rs));
                }
            }
        }
        return albums;
    }

    public Album create(String title, int userId) throws SQLException{
        var query = "INSERT INTO albums(title, user_id) VALUES (?, ?)";
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);){
            pStatement.setString(1, title);
            pStatement.setInt(2, userId);
            pStatement.executeUpdate();
            try(var rs = pStatement.getGeneratedKeys();){
                if (rs.next()) return this.findOneById(rs.getInt(1));
                return null;
            }
        }
    }
}
