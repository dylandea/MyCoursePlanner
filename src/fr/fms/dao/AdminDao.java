/**
 * Composant d'accès aux données de la table T_Admins dans la base de données MyCoursePlanner
 * @author Dylan De Albuquerque - 2023
 * 
 */

package fr.fms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.fms.entities.Admin;

public class AdminDao implements Dao<Admin> {

	@Override
	public boolean create(Admin obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Admin read(int id) {
		String str = "select * from T_Admins where IdAdmin=?";
		try(PreparedStatement ps = connection.prepareStatement(str)){
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return new Admin(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
		} catch (SQLException e) {
			logger.severe("pb sql sur la lecture d'un client " + e.getMessage());
		}
		return null;
	}

	@Override
	public boolean update(Admin obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Admin obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Admin> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
