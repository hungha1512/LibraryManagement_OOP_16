package com.hunghq.librarymanagement.Respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Document;

public class DocumentDAO implements IRepository{

    private static final Connection con = MySQLConnection.getConnection();
    
    @Override
    public Document Make(ResultSet reS) {
        Document document = null;
        try {
            document = new Document(
                reS.getString("documentId"), 
                reS.getString("title"),
                reS.getString("author"),
                reS.getDouble("rating"),
                reS.getString("genre"),
                reS.getString("language"),
                reS.getString("description"),
                reS.getInt("numRatings"),
                reS.getString("publisher"),
                reS.getString("isbn"),
                reS.getString("publishedDate"),
                reS.getString("award"),
                reS.getString("coverImg")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Override
    public void Add(Object entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Add'");
    }

    @Override
    public Object GetById(String id) {
        String sql = "SELECT * FROM documents WHERE documentId = ?";
        Document document = null; 
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);
            ResultSet reS = prS.executeQuery();
            if (reS.next()) {
                document = Make(reS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document; 
    }

    public List GetAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetAll'");
    }

    @Override
    public List FindByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'FindByName'");
    }

    @Override
    public void Update(Object entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Update'");
    }

    @Override
    public void Delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Delete'");
    }

    @Override
    public Object Save(Object entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Save'");
    }

}
