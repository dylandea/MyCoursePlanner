package fr.fms.dao;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.fms.entities.Category;

public class CategoryDao implements Dao<Category>{

	@Override
	public boolean create(Category obj) {
		String str = "INSERT INTO T_Categories (Name) VALUES (?);";	
		try (PreparedStatement ps = connection.prepareStatement(str)){
			ps.setString(1, obj.getName());
			if( ps.executeUpdate() == 1)	return true;
		} catch (SQLException e) {
			logger.severe("pb sql sur la création d'une catégorie " + e.getMessage());
		} 	
		return false;
	}

	@Override
	public Category read(int id) {
		try (Statement statement = connection.createStatement()){
			String str = "SELECT * FROM T_Categories where IdCategory=" + id + ";";									
			ResultSet rs = statement.executeQuery(str);
			if(rs.next()) return new Category(rs.getInt(1) , rs.getString(2));
		} catch (SQLException e) {
			logger.severe("pb sql sur la lecture d'une catégorie " + e.getMessage());
		} 	
		return null;
	}

	@Override
	public boolean update(Category obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Category obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Category> readAll() {
		ArrayList<Category> categories = new ArrayList<Category>();
		String sql = "select * from T_Categories";
		try(Statement statement = connection.createStatement()){
			try(ResultSet resultSet = statement.executeQuery(sql)){
				while(resultSet.next()) {
					categories.add(new Category(resultSet.getInt("idCategory"), resultSet.getString(2)));
				}
				return categories;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return null;
	}

}
