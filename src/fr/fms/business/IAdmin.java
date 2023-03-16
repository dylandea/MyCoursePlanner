/**
 * @author De-albuquerqueD 2023
 */

package fr.fms.business;

import java.util.ArrayList;

import fr.fms.entities.Category;
import fr.fms.entities.Course;
import fr.fms.entities.Customer;
import fr.fms.entities.Order;
/**
 * Couche métier qui contient toutes les fonctionnalités propres à un administrateur:
 * ajouter, modifier, supprimer une catégorie ou une formation,
 * et afficher la liste des clients pour voir l'historique de leurs commandes.
 * 
 * @author De-albuquerqueD 2023
 */

public interface IAdmin {

	/**
	 * Méthode qui ajoute une formation à la bdd
	 * @param course Objet Course
	 * @return true si la requete en bdd s'est bien passée
	 */
	public boolean addCourseToDb(Course course);
	/**
	 * Méthode qui supprime une formation de la bdd
	 * @param id id de la formation
	 * @return true si la requete en bdd s'est bien passée
	 */
	public boolean removeCourseFromDb(int id);
	/**
	 * Méthode qui met à jour une formation dans la bdd
	 * @param updatedCourse la formation avec les données mises à jour à intégrer dans la bdd
	 * @return true si la requete en bdd s'est bien passée
	 */
	public boolean updateCourseFromDb(Course updatedCourse);
	/**
	 * Méthode qui ajoute une catégorie à la bdd
	 * @param category catégorie à ajouter dans la bdd
	 * @return true si la requete en bdd s'est bien passée
	 */
	public boolean addCategoryToDb(Category category);
	/**
	 * Méthode qui supprime une catégorie de la bdd
	 * @param id id de la catégorie
	 * @return true si la requete en bdd s'est bien passée
	 */
	public boolean removeCategoryFromDb(int id);
	/**
	 * Méthode qui met à jour une catégorie dans la bdd
	 * @param updatedCategory La catégorie qui va prendre la place de l'ancienne dans la bdd
	 * @return true si la requete en bdd s'est bien passée
	 */
	public boolean updateCategoryFromDb(Category updatedCategory);
	/**
	 * Méthode qui renvoie une liste des clients à l'administrateur
	 * @return Liste de tous les clients
	 */
	public ArrayList<Customer> getAllCustomers();
	/**
	 * Méthode qui retourne toutes les commandes pour un client donné
	 * @param id du client
	 * @return une liste des commandes pour cet id de client
	 */
	ArrayList<Order> getAllOrders(int id);
	/**
	 * Méthode qui retourne un client depuis la base de donnée à partir der son id
	 * @param id du client
	 * @return un objet Customer
	 */
	Customer readCustomer(int id);
	/**
	 * Méthode qui vérifie si une catégorie existe bien dans la bdd
	 * @param idCategory id de la catégorie
	 * @return true si la catégorie existe bien dans la bdd
	 */
	boolean checkIfCategoryExists(int idCategory);
	/**
	 * Méthode qui affiche le détail des formations présentes dans une commande
	 * @param idOrder id de la commande
	 * @return liste des formations correspondant à une commande
	 */
	ArrayList<Course> getCoursesFromThisOrder(int idOrder);

}
