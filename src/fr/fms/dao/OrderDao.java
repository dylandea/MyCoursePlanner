package fr.fms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import fr.fms.entities.Order;

public class OrderDao implements Dao<Order> {

	@Override
	public boolean create(Order obj) {
		String str = "INSERT INTO T_Orders (Amount , IdCustomer) VALUES (?,?);";	
		try (PreparedStatement ps = connection.prepareStatement(str,Statement.RETURN_GENERATED_KEYS)){	
			ps.setDouble(1, obj.getAmount());
			ps.setInt(2, obj.getIdCustomer());
			ps.executeUpdate();
			try(ResultSet generatedKeySet = ps.getGeneratedKeys()){
				if(generatedKeySet.next()) {
					obj.setIdOrder(generatedKeySet.getInt(1));
					return true;
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Order read(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Order obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Order obj) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<Order> readAll(int idCustomer) {
		ArrayList<Order> orders = new ArrayList<>();
		String strSql = "SELECT * FROM T_Orders where IdCustomer=" + idCustomer +";";

		try(Statement statement = connection.createStatement ()){
			try(ResultSet resultSet = statement.executeQuery(strSql)) {
				while(resultSet.next()) {
					int rsIdOrder = resultSet.getInt(1); 
					double rsTotalAmount = resultSet.getDouble(2);
					Date rsDate = resultSet.getDate(3);
					int rsIdCustomer = resultSet.getInt(4);
				    
					orders.add((new Order(rsIdOrder,rsTotalAmount, rsDate, rsIdCustomer)));
				}
			}
		} catch(SQLException e) {
			throw new RuntimeException("Erreur lors de la récupération de l'ensemble des commandes:\n" + e.getMessage());
		}
		return orders;
	}

	@Override
	public ArrayList<Order> readAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
