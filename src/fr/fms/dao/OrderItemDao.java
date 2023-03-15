package fr.fms.dao;

/**
 * Composant d'accès aux données de la table T_Order_Items dans la base de données MyCoursePlanner
 * @author Dylan De Albuquerque - 2023
 * 
 */

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.fms.entities.OrderItem;

public class OrderItemDao implements Dao<OrderItem> {

	@Override
	public boolean create(OrderItem obj) {
		String str = "INSERT INTO T_Order_Items (IdCourse, UnitaryPrice, IdOrder) VALUES (?,?,?);";	
		try (PreparedStatement ps = connection.prepareStatement(str)){	
			ps.setInt(1, obj.getIdCourse());
			ps.setDouble(2, obj.getUnitaryPrice());
			ps.setInt(3, obj.getIdOrder());
					
			if( ps.executeUpdate() == 1)	return true;
		} catch (SQLException e) {
			logger.severe("pb sql sur la création d'un orderItem : " + e.getMessage());
		}
		return false;
	}

	@Override
	public OrderItem read(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(OrderItem obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(OrderItem obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<OrderItem> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
