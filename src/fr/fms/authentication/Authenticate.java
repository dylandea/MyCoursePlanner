package fr.fms.authentication;

import fr.fms.dao.CustomerDao;
import fr.fms.dao.Dao;
import fr.fms.dao.DaoFactory;
import fr.fms.dao.UserDao;
import fr.fms.entities.Customer;
import fr.fms.entities.User;

public class Authenticate {
	private Dao<Customer> customerDao = DaoFactory.getCustomerDao();
	private Dao<User> userDao = DaoFactory.getUserDao();

	/**
	 * méthode qui vérifie si login et pwd correspond à un utilisateur en base
	 * @param log l'identifiant de l'utilisateur
	 * @param pwd mot de passe de l'utilisateur
	 * @return id de l'utilisateur, 0 si non trouvé
	 */
	public int existUser(String log, String pwd) {
		User user = ((UserDao)userDao).findUserByCredentials(log,pwd);
		if(user != null )	return user.getId();
		return 0;
	}

	/**
	 * méthode qui vérifie si login correspond à un utilisateur en base
	 * @param log identifiant de l'utilisateur
	 * @return id de l'utilisateur, 0 si non trouvé
	 */
	public int existUser(String log) {
		User user = ((UserDao)userDao).findUserByLogin(log);
		if(user != null )	return user.getId();
		return 0;
	}

	/**
	 * méthode qui renvoi un client correspondant à un email (unique en base)
	 * @param email email de l'utilisateur
	 * @return client un objet Customer
	 */
	public Customer existCustomerByEmail(String email) {
		return ((CustomerDao)customerDao).findCustomerByEmail(email);
	}

	/**
	 * Méthode qui rajoute un utilisateur dans la bdd
	 * @param email email de l'utilisateur
	 * @param password mot de passe de l'utilisateur
	 */

	public void addUser(String email, String password) {
		userDao.create(new User(email, password));		
	}

	/**
	 * Methode qui rajoute un client dans la bdd
	 * @param customer prend un objet Customer
	 * @return true si la création s'est bien passée
	 */

	public boolean addCustomer(Customer customer) {
		return customerDao.create(customer);		
	}

	/**
	 * Méthode qui vérifie si un utilisateur est un admin
	 * @param id id de l'utilisateur
	 * @return boolean vrai si c'est un admin
	 */
	public boolean isAdmin(int id) {
		return ((UserDao)userDao).checkIsAdmin(id);
	}
}
