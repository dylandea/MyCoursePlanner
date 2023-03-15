/**
 * Composant d'accès aux données de la table T_Courses dans la base de données MyCoursePlanner
 * @author Dylan De Albuquerque - 2023
 * 
 */

package fr.fms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fr.fms.entities.Course;

public class CourseDao implements Dao<Course> {

	public CourseDao() {
		//logger.info("Here we Go !");
	}

	@Override
	public boolean create(Course obj) {
		String str = "INSERT INTO T_Courses (Name, Description, DurationInDays, IsRemote, UnitaryPrice, IdCategory) VALUES (?,?,?,?,?,?);";	
		try (PreparedStatement ps = connection.prepareStatement(str)){
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getDescription());
			ps.setInt(3, obj.getDurationInDays());
			ps.setBoolean(4, obj.getIsRemote());
			ps.setDouble(5, obj.getPrice());	
			if (obj.getCategory() == 0)
				ps.setNull(6, 0);
			else
				ps.setInt(6, obj.getCategory());
			if( ps.executeUpdate() == 1)	return true;
		} catch (SQLException e) {
			logger.severe("pb sql sur la création d'une formation " + e.getMessage());
		} 	
		return false;
	}

	@Override
	public boolean delete(Course obj) {
		try (Statement statement = connection.createStatement()){
			String str = "DELETE FROM T_Courses where IdCourse=" + obj.getIdCourse() + ";";									
			statement.executeUpdate(str);		
			return true;
		} catch (SQLException e) {
			logger.severe("pb sql sur la suppression d'une formation " + e.getMessage());
		} 	
		return false;
	}

	public boolean delete(int id) {
		String strDel = "DELETE FROM T_Courses where IdCourse=?;";
		try (PreparedStatement ps = connection.prepareStatement(strDel)){
			ps.setInt(1, id);
			if ( ps.executeUpdate () == 1 ) 
				return true;
		} catch (SQLException e) {
			throw new RuntimeException("Erreur lors de la suppression de la formation:\n" + e.getMessage());
		}
		return false;
	}

	@Override
	public Course read(int id) {
		try (Statement statement = connection.createStatement()){
			String str = "SELECT * FROM T_Courses where IdCourse=" + id + ";";									
			ResultSet rs = statement.executeQuery(str);
			if(rs.next()) return new Course(rs.getInt(1) , rs.getString(2) , rs.getString(3) , rs.getInt(4), rs.getBoolean(5), rs.getDouble(6), rs.getInt(7));
		} catch (SQLException e) {
			logger.severe("pb sql sur la lecture d'une formation " + e.getMessage());
		} 	
		return null;
	}
	
	@Override
	public ArrayList<Course> readAll() {
		ArrayList<Course> courses = new ArrayList<Course>();
		String strSql = "SELECT * FROM T_Courses";		
		try(Statement statement = connection.createStatement()){
			try(ResultSet resultSet = statement.executeQuery(strSql)){ 			
				while(resultSet.next()) {
					int rsId = resultSet.getInt(1);	
					String rsName = resultSet.getString(2);
					String rsDescription = resultSet.getString(3);
					int rsDurationInDays = resultSet.getInt(4);
					boolean rsIsRemote = resultSet.getBoolean(5);
					double rsPrice = resultSet.getDouble(6);		
					courses.add((new Course(rsId, rsName, rsDescription, rsDurationInDays, rsIsRemote, rsPrice)));	
					
				}	
			}
		} catch (SQLException e) {
			logger.severe("pb sql sur l'affichage des formations " + e.getMessage());
		}	
		catch (Exception e) {
			logger.severe("pb : " + e.getMessage());
		}
		return courses;
	}

	public ArrayList<Course> readAllByCat(int id) {
		ArrayList<Course> courses = new ArrayList<Course>();
		String strSql = "SELECT * FROM T_Courses where idCategory=" + id;		
		try(Statement statement = connection.createStatement()){
			try(ResultSet resultSet = statement.executeQuery(strSql)){ 			
				while(resultSet.next()) {
					int rsId = resultSet.getInt(1);	
					String rsName = resultSet.getString(2);
					String rsDescription = resultSet.getString(3);
					int rsDurationInDays = resultSet.getInt(4);
					boolean rsIsRemote = resultSet.getBoolean(5);
					double rsPrice = resultSet.getDouble(6);		
					courses.add((new Course(rsId, rsName, rsDescription, rsDurationInDays, rsIsRemote, rsPrice)));						
				}	
			}
		} catch (SQLException e) {
			logger.severe("pb sql sur l'affichage des formations par catégories " + e.getMessage());
		}			
		return courses;
	}
	
	public ArrayList<Course> readAllByOrder(int idOrder) {
		ArrayList<Course> courses = new ArrayList<Course>();
		String strSql = "SELECT t_courses.* FROM T_Orders join t_order_items on t_orders.idorder=t_order_items.idorder join t_courses on t_courses.idCourse=t_order_items.idCourse where t_orders.IdOrder=" + idOrder + ";";		
		try(Statement statement = connection.createStatement()){
			try(ResultSet resultSet = statement.executeQuery(strSql)){ 			
				while(resultSet.next()) {
					int rsId = resultSet.getInt(1);	
					String rsName = resultSet.getString(2);
					String rsDescription = resultSet.getString(3);
					int rsDurationInDays = resultSet.getInt(4);
					boolean rsIsRemote = resultSet.getBoolean(5);
					double rsPrice = resultSet.getDouble(6);		
					courses.add((new Course(rsId, rsName, rsDescription, rsDurationInDays, rsIsRemote, rsPrice)));						
				}	
			}
		} catch (SQLException e) {
			logger.severe("pb sql sur l'affichage des formations de cette commande " + e.getMessage());
		}			
		return courses;
	}
	
	public ArrayList<Course> readAllByRemote(boolean isRemote) {
		ArrayList<Course> courses = new ArrayList<Course>();
		String criterion;
		if (isRemote == true) criterion = "1";
		else criterion = "0";
		
		String strSql = "SELECT * FROM T_Courses where isRemote=" + criterion;		
		try(Statement statement = connection.createStatement()){
			try(ResultSet resultSet = statement.executeQuery(strSql)){ 			
				while(resultSet.next()) {
					int rsId = resultSet.getInt(1);	
					String rsName = resultSet.getString(2);
					String rsDescription = resultSet.getString(3);
					int rsDurationInDays = resultSet.getInt(4);
					boolean rsIsRemote = resultSet.getBoolean(5);
					double rsPrice = resultSet.getDouble(6);		
					courses.add((new Course(rsId, rsName, rsDescription, rsDurationInDays, rsIsRemote, rsPrice)));						
				}	
			}
		} catch (SQLException e) {
			logger.severe("pb sql sur l'affichage des formations par présentiel/distanciel " + e.getMessage());
		}			
		return courses;
	}
	
	public ArrayList<Course> readByKeyword(String keyword) {
		ArrayList<Course> courses = new ArrayList<Course>();
		
		String strSql = "SELECT * FROM T_Courses where Description like '%"+ keyword + "%' or Name like '%"+ keyword + "%';" ;		
		try(Statement statement = connection.createStatement()){
			try(ResultSet resultSet = statement.executeQuery(strSql)){ 			
				while(resultSet.next()) {
					int rsId = resultSet.getInt(1);	
					String rsName = resultSet.getString(2);
					String rsDescription = resultSet.getString(3);
					int rsDurationInDays = resultSet.getInt(4);
					boolean rsIsRemote = resultSet.getBoolean(5);
					double rsPrice = resultSet.getDouble(6);		
					courses.add((new Course(rsId, rsName, rsDescription, rsDurationInDays, rsIsRemote, rsPrice)));						
				}	
			}
		} catch (SQLException e) {
			logger.severe("pb sql sur l'affichage des formations par mot-clés " + e.getMessage());
		}			
		return courses;
	}

	@Override
	public boolean update(Course obj) {
		String str = "UPDATE T_Courses set Name=? , Description=?  , DurationInDays=? , IsRemote=? , UnitaryPrice=? , IdCategory=? where idCourse=?;";	
		try (PreparedStatement ps = connection.prepareStatement(str)){				
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getDescription());
			ps.setInt(3, obj.getDurationInDays());
			ps.setBoolean(4, obj.getIsRemote());
			ps.setDouble(5, obj.getPrice());	
			ps.setInt(6, obj.getCategory());
			ps.setInt(7, obj.getIdCourse());
			if( ps.executeUpdate() == 1)	return true;
			return true;
		} catch (SQLException e) {
			logger.severe("pb sql sur la mise à jour d'une formation " + e.getMessage());
		} 	
		return false;
	}

	public boolean updateCourseCatBeforeCatRemoved(int idCategory) {
		String str = "UPDATE T_Courses set IdCategory=null where IdCategory=?;";	
		try (PreparedStatement ps = connection.prepareStatement(str)){				
			ps.setInt(1, idCategory);
			
			if( ps.executeUpdate() == 1)	return true;
			return true;
		} catch (SQLException e) {
			logger.severe("pb sql sur la mise à jour des formations " + e.getMessage());
		} 	
		return false;
	}

}
