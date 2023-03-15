package fr.fms.authentication;

import fr.fms.dao.AdminDao;
import fr.fms.dao.CustomerDao;
import fr.fms.dao.Dao;
import fr.fms.dao.DaoFactory;
import fr.fms.dao.UserDao;
import fr.fms.entities.Customer;
import fr.fms.entities.User;
import fr.fms.entities.Admin;

public class Authenticate {
	private Dao<Customer> customerDao = DaoFactory.getCustomerDao();
	private Dao<Admin> adminDao = DaoFactory.getAdminDao();
	private Dao<User> userDao = DaoFactory.getUserDao();

	/**
	 * méthode qui vérifie si login et pwd correspond à un utilisateur en base
	 * @param log
	 * @param pwd
	 * @return id de l'utilisateur, 0 si non trouvé
	 */
	public int existUser(String log, String pwd) {
		User user = ((UserDao)userDao).findUserByCredentials(log,pwd);
		if(user != null )	return user.getId();
		return 0;
	}

	/**
	 * méthode qui vérifie si login correspond à un utilisateur en base
	 * @param log
	 * @return id de l'utilisateur, 0 si non trouvé
	 */
	public int existUser(String log) {
		User user = ((UserDao)userDao).findUserByLogin(log);
		if(user != null )	return user.getId();
		return 0;
	}

	/**
	 * méthode qui renvoi un client correspondant à un email (unique en base)
	 * @param email 
	 * @return client
	 */
	public Customer existCustomerByEmail(String email) {
		return ((CustomerDao)customerDao).findCustomerByEmail(email);
	}

	/**
	 * Méthode qui rajoute un utilisateur dans la bdd
	 * @param email
	 * @param password
	 */

	public void addUser(String email, String password) {
		userDao.create(new User(email, password));		
	}

	/**
	 * Methode qui rajoute un client dans la bdd
	 * @param customer
	 * @return
	 */

	public boolean addCustomer(Customer customer) {
		return customerDao.create(customer);		
	}

	/**
	 * Méthode qui vérifie si un utilisateur est un admin
	 * @param id
	 * @return
	 */
	public Admin isAdmin(int id) {
		return ((AdminDao)adminDao).read(id);
	}
}
