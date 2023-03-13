/**
 * @author El babili - 2022
 * 
 */

package fr.fms.business;

import java.util.ArrayList;
import fr.fms.entities.Category;
import fr.fms.entities.Course;

public interface IBusiness {	
	/**
	 * méthode qui ajoute un article au panier
	 * @param article à ajouter
	 */
	public void addToCart(Course course);		
	
	/**
	 * méthode qui retire un article au panier s'il est dedans
	 * @param id de l'article à retirer
	 */
	public void rmFromCart(int id);		
	
	/**
	 * méthode qui renvoi sous la forme d'une liste tous les éléments du panier (gestion en mémoire)
	 * @return Liste d'articles du panier
	 */
	public ArrayList<Course> getCart();	
	
	/**
	 * méthode qui réalise la commande en base avec l'idUser + total de la commande en cours + date du jour + contenu du panier :
	 * - la méthode va céer une commande en base -> idOrder + montant + date + idUser
	 * - puis va ajouter autant de commandes minifiées associées : orderItem -> idOrderItem + idArticle + Quantity + Price + idOrder
	 * @param idUser est l'identifiant du client qui est passé commande
	 * @return 1 si tout est ok 0 si pb 
	 */
	public int order(int idCustomer);		
	
	/**
	 * méthode qui renvoie toutes les formations de la table t_courses en bdd
	 * @return Liste d'articles en base
	 */
	public ArrayList<Course> readCourses();	
	
	/**
	 * méthode qui renvoie la formation correspondant à l'id
	 * @param id de l'article à renvoyer
	 * @return article correspondant si trouvé, null sinon
	 */
	public Course readOneCourse(int id);	
	
	/**
	 * méthode qui renvoi toutes les catégories de la table t_catégories en bdd
	 * @return Liste de catégories en base
	 */
	public ArrayList<Category> readCategories();
	
	/**
	 * méthode qui renvoi toutes les formations d'une catégorie
	 * @param id de la catégorie
	 * @return Liste d'articles
	 */
	public ArrayList<Course> readCoursesByCatId(int idCat);

	/**
	 * Méthode qui selon la valeur (true ou false) qui lui est donnée permet de renvoyer soit les formations qui se font en distanciel, soit celles en présentiel 
	 * @param boolean isRemote
	 * @return
	 */
	public ArrayList<Course> readRemoteCourses(boolean isRemote);

	/**
	 * Methode qui permet de chercher dans la liste des formations celles dont le nom ou la description contiennent le mot clé.
	 * @param keyword
	 * @return
	 */
	ArrayList<Course> readByKeyword(String keyword);

}
