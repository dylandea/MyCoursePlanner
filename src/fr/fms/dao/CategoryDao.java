/**
 * Composant d'accès aux données de la table T_Categories dans la base de données MyCoursePlanner
 * @author Dylan De Albuquerque - 2023
 * 
 */

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
		String str = "INSERT INTO T_Categories (CatName) VALUES (?);";	
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
		String str = "UPDATE T_Categories set CatName=? where idCategory=?;";	
		try (PreparedStatement ps = connection.prepareStatement(str)){				
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			if( ps.executeUpdate() == 1)	return true;
			return true;
		} catch (SQLException e) {
			logger.severe("pb sql sur la mise à jour d'une catégorie " + e.getMessage());
		} 	
		return false;
	}

	@Override
	public boolean delete(Category obj) {
		try (Statement statement = connection.createStatement()){
			String str = "DELETE FROM T_Categories where IdCategory=" + obj.getId() + ";";									
			statement.executeUpdate(str);		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean delete(int id) {
		String strDel = "DELETE FROM T_Categories where IdCategory=?;";
		try (PreparedStatement ps = connection.prepareStatement(strDel)){
			ps.setInt(1, id);
			if ( ps.executeUpdate () == 1 ) 
				return true;
		} catch (SQLException e) {
			throw new RuntimeException("Erreur lors de la suppression de la catégorie:\n" + e.getMessage());
		}
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
