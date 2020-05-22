package it.polimi.tiw.imgallery.services;

import it.polimi.tiw.imgallery.beans.Image;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ImageService {
    private DataSource ds;

    public ImageService(){
        try {
            this.ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/db");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public Image mapDBToBean(ResultSet rs) throws SQLException {
        var image = new Image();
        image.setId(rs.getInt("id"));
        image.setTitle(rs.getString("title"));
        image.setDescription(rs.getString("description"));
        image.setPath(rs.getString("path"));
        image.setCreatedAt(rs.getTimestamp("created_at"));
        image.setUserId(rs.getInt("user_id"));
        image.setAlbumId(rs.getInt("album_id"));
        return image;
    }

    public Image findOneById(int id) throws SQLException{
        var query = "SELECT * FROM images WHERE id = ?";
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query);){
            pStatement.setInt(1, id);
            try(var rs = pStatement.executeQuery();){
                if (rs.next()) return this.mapDBToBean(rs);
                return null;
            }
        }
    }

    public int getTotalPages(int albumId) throws SQLException{
        var query = "SELECT CEIL(COUNT(*)/5) FROM images WHERE album_id=?";
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query);){
            pStatement.setInt(1, albumId);
            try(var rs = pStatement.executeQuery();){
                if (rs.next()) return rs.getInt(1);
                return 1;
            }
        }
    }

    public List<Image> findAllByAlbum(int albumId, int start, int count) throws SQLException{
        var query = "SELECT * FROM images WHERE album_id=? ORDER BY created_at DESC LIMIT ?, ?";
        var images = new ArrayList<Image>();
        try(var connection = this.ds.getConnection();
            var pStatement = connection.prepareStatement(query);){
            pStatement.setInt(1, albumId);
            pStatement.setInt(2, start);
            pStatement.setInt(3, count);
            try(var rs = pStatement.executeQuery();){
                while (rs.next()){
                    images.add(this.mapDBToBean(rs));
                }
            }
        }
        return images;
    }

    public List<Image> findAllByUser(int userId) throws SQLException{
        var query = "SELECT * FROM images WHERE user_id = ? ORDER BY created_at DESC";
        var images = new ArrayList<Image>();
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query);){
            pStatement.setInt(1, userId);
            try(var rs = pStatement.executeQuery();){
                while (rs.next()){
                    images.add(this.mapDBToBean(rs));
                }
            }
        }
        return images;
    }

    public Image create(String title, String description, int userId, int albumId, String path) throws SQLException{
        var query = "INSERT INTO images(title, description, user_id, album_id, path) VALUES (?, ?, ?, ?, ?)";
        try(var connection = this.ds.getConnection(); var pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);){
            pStatement.setString(1, title);
            pStatement.setString(2, description);
            pStatement.setInt(3, userId);
            pStatement.setInt(4, albumId);
            pStatement.setString(5, path);
            pStatement.executeUpdate();
            try(var rs = pStatement.getGeneratedKeys();){
                if (rs.next()) return this.findOneById(rs.getInt(1));
                return null;
            }
        }
    }
}
