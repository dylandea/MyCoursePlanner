/**
 * Application console de vente de formations permettant d'exploiter une couche métier/dao pour créer un panier en ajoutant ou retirant des formations
 * puis passer commande à tout instant, cela génère une commande en base avec tous les éléments associés
 * @author Dylan De Albuquerque - 2023
 * 
 */
package fr.fms;

import java.util.ArrayList;
import java.util.Scanner;

import fr.fms.authentication.Authenticate;
import fr.fms.business.IAdminImpl;
import fr.fms.business.IBusinessImpl;
import fr.fms.entities.Course;
import fr.fms.entities.Category;
import fr.fms.entities.Customer;
import fr.fms.entities.Order;

public class ShopApp {
	private static Scanner scan = new Scanner(System.in); 
	private static IBusinessImpl business = new IBusinessImpl();
	private static Authenticate authenticate = new Authenticate();

	public static final String TEXT_BLUE = "\u001B[36m";
	public static final String TEXT_RESET = "\u001B[0m";
	private static final String COLUMN_ID = "ID";
	private static final String COLUMN_NAME = "NOM";
	private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
	private static final String COLUMN_DAYS_DURATION = "DUREE";
	private static final String COLUMN_IS_REMOTE = "DISTANCIEL";
	private static final String COLUMN_PRICE = "PRIX";

	private static int idUser = 0;
	private static String login = null;

	public static void main(String[] args) {
		try {
			System.out.println("Bonjour et bienvenue dans votre plateforme de formation, \nvoici la liste des formations disponibles:\n");
			displayCourses();
			int choice = 0;
			while(choice != 11) {
				try {
					displayMenu();
					choice = scanInt();
					switch(choice) {
					case 1 : addCourseToBasket();				
					break;					
					case 2 : removeCourseFromBasket();
					break;					
					case 3 : displayCart(true);
					break;					
					case 4 : displayCourses();
					break;						
					case 5 : displayCoursesByCategoryId();
					break;
					case 6 : displayAllCoursesIsRemote(true);
					break;
					case 7 : displayAllCoursesIsRemote(false);
					break;
					case 8 : searchByKeyword();
					break;
					case 9 : displayOneCourse();
					break;
					case 10 : connection();
					break;
					case 11 : System.out.println("à bientôt dans notre boutique :)");
					break;					
					default : System.out.println("veuillez saisir une valeur entre 1 et 10");
					}
				} catch (Exception e) {
					System.out.println("Erreur: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			System.out.println("Erreur lors du lancement de l'application : " + e.getMessage());
		}
	}

	private static void searchByKeyword() {
		scan.nextLine();
		System.out.println("saisissez le mot-clé à rechercher:");
		String keyword = scan.nextLine();
		ArrayList<Course> courses = business.readByKeyword(keyword);
		if(courses.size()>0) {
			System.out.println(
					Course.centerString(COLUMN_ID) +
					Course.centerString(COLUMN_NAME) + 
					Course.centerString(COLUMN_DESCRIPTION) + 
					Course.centerString(COLUMN_DAYS_DURATION) + 
					Course.centerString(COLUMN_IS_REMOTE) + 
					Course.centerString(COLUMN_PRICE)
					);
			courses.forEach(System.out::println);
		}
		else System.out.println("Aucune formation disponible actuellement en distanciel");

	}

	private static void displayAllCoursesIsRemote(boolean isRemote) {
		ArrayList<Course> courses = business.readRemoteCourses(isRemote);
		if(courses.size()>0) {
			System.out.println(
					Course.centerString(COLUMN_ID) +
					Course.centerString(COLUMN_NAME) + 
					Course.centerString(COLUMN_DESCRIPTION) + 
					Course.centerString(COLUMN_DAYS_DURATION) + 
					Course.centerString(COLUMN_IS_REMOTE) + 
					Course.centerString(COLUMN_PRICE)
					);
			courses.forEach(System.out::println);
		}
		else System.out.println("Aucune formation disponible actuellement en distanciel");
	}

	/**
	 * Méthode qui affiche le menu principale
	 */
	public static void displayMenu() {	
		if(login != null)	System.out.print(TEXT_BLUE + "Compte : " + login);
		System.out.println("\n" + "Pour réaliser une action, tapez le code correspondant");
		System.out.println("1 : Ajouter une formation au panier");
		System.out.println("2 : Retirer une formation du panier");
		System.out.println("3 : Afficher mon panier + total pour passer commande");
		System.out.println("4 : Afficher toutes les formations disponibles");
		System.out.println("5 : Afficher les formations par catégorie");
		System.out.println("6 : Afficher les formations en distanciel");
		System.out.println("7 : Afficher les formations en présentiel");
		System.out.println("8 : Rechercher une formation par mot clé");
		System.out.println("9 : Afficher les détails d'une formation");
		System.out.println(login == null 
				? "10 : Connexion à votre compte"
						: "10 : Déconnexion");
		System.out.println("11 : Sortir de l'application");
	}

	/**
	 * Méthode qui affiche toutes les formations en base en centrant le texte 
	 */
	public static void displayCourses() { 		
		System.out.println(
				Course.centerString(COLUMN_ID) +
				Course.centerString(COLUMN_NAME) + 
				Course.centerString(COLUMN_DESCRIPTION) + 
				Course.centerString(COLUMN_DAYS_DURATION) + 
				Course.centerString(COLUMN_IS_REMOTE) + 
				Course.centerString(COLUMN_PRICE)
				);
		business.readCourses().forEach(System.out::println);
	}

	/**
	 * Méthode qui affiche toutes les formations par catégorie en utilisant printf
	 */
	private static void displayCoursesByCategoryId() {
		displayCategories();
		System.out.println("saisissez l'id de la catégorie concerné");
		int id = scanInt();
		Category category = business.readOneCategory(id);
		if(category != null) {
			System.out.println(
					Course.centerString(COLUMN_ID) +
					Course.centerString(COLUMN_NAME) + 
					Course.centerString(COLUMN_DESCRIPTION) + 
					Course.centerString(COLUMN_DAYS_DURATION) + 
					Course.centerString(COLUMN_IS_REMOTE) + 
					Course.centerString(COLUMN_PRICE)
					);
			business.readCoursesByCatId(id).forEach(System.out::println);
		}
		else System.out.println("Cette catégorie n'existe pas !");
	}

	/**
	 * Méthode qui affiche toutes les catégories
	 */
	private static void displayCategories() {
		System.out.println(
				Category.centerString(COLUMN_ID) +
				Category.centerString(COLUMN_NAME)
				);
		business.readCategories().forEach(System.out::println);		
	}

	/**
	 * Méthode qui supprime une formation du panier
	 */
	public static void removeCourseFromBasket() {
		System.out.println("Selectionner l'id de la formation à supprimer du panier");
		int id = scanInt();
		if (business.getCart().stream().anyMatch(x -> x.getIdCourse() == id)) {
			business.rmFromCart(id);
		} else {
			System.out.println("L'ID saisie ne correspond à aucune formation dans votre panier");
		}
		displayCart(false);
	}

	/**
	 * Méthode qui ajoute une formation au panier
	 */
	public static void addCourseToBasket() {
		System.out.println("Selectionnez l'id de la formation à ajouter au panier:");
		int id = scanInt();
		Course course = business.readOneCourse(id);
		if(course != null) {
			if (business.getCart().stream().anyMatch(x -> x.getIdCourse() == id)) {
				System.out.println("Vous avez déjà ajouté cette formation à votre panier.");
			} else {
				business.addToCart(course);
			}
			displayCart(false);
		}
		else System.out.println("La formation que vous souhaitez ajouter n'existe pas, vérifiez l'ID saisi.");
	} 

	/**
	 * Méthode qui affiche le contenu du panier + total de la commande + propose de passer commande
	 */
	private static void displayCart(boolean flag) {
		if(business.isCartEmpty()) 	System.out.println("PANIER VIDE");
		else {
			System.out.println("CONTENU DU PANIER :");
			String titles = 
					Course.centerString(COLUMN_ID) +
					Course.centerString(COLUMN_NAME) + 
					Course.centerString(COLUMN_DESCRIPTION) + 
					Course.centerString(COLUMN_DAYS_DURATION) + 
					Course.centerString(COLUMN_IS_REMOTE) + 
					Course.centerString(COLUMN_PRICE)
					;
			System.out.println(titles);
			business.getCart().forEach(System.out::println);
			if(flag) {
				System.out.println("MONTANT TOTAL : " + business.getTotal());
				System.out.println("Souhaitez vous passer commande ? Oui/Non :");
				if(scan.next().equalsIgnoreCase("Oui")) {
					nextStep();
				}
			}
		}
	}

	/**
	 * Méthode qui passe la commande, l'utilisateur doit être connecté
	 * si c'est le cas, l'utilisateur sera invité à associé un client à sa commande
	 * si le client n'existe pas, il devra le créer
	 * puis une commande associée au client sera ajoutée en base
	 * De même, des commandes minifiées seront associées à la commande
	 * une fois toutes les opérations terminées correctement, le panier sera vidé et un numéro de commande attribué
	 */
	private static void nextStep() {
		if(login == null)	{ 
			System.out.println("Vous devez être connecté pour continuer");
			connection();
		}
		if(login != null) {
			int idCustomer = newCustomer(idUser);	
			if(idCustomer != 0) {
				int idOrder = business.order(idCustomer);	
				if(idOrder == 0)	System.out.println("pb lors du passage de commande");
				else {
					System.out.println("Votre commande a bien été validé, voici son numéro : " + idOrder);
					business.clearCart();
				}
			}
		}
	}

	/**
	 * Méthode qui ajoute un client en base s'il n'existe pas déjà 
	 * @return id du client afin de l'associer à la commande en cours
	 */
	private static int newCustomer(int idUser) {
		System.out.println("Avez vous déjà un compte client ? saisissez une adresse email valide pour vérifier :");
		String email = scan.next();		
		if(isValidEmail(email)) {	
			Customer customer = authenticate.existCustomerByEmail(email);
			if(customer == null) {
				scan.nextLine();	
				System.out.println("saisissez votre nom :");
				String name = scan.nextLine();
				System.out.println("saisissez votre prénom :");
				String fName = scan.next();					
				System.out.println("saisissez votre tel :");
				String tel = scan.next();		
				scan.nextLine(); 
				System.out.println("saisissez votre adresse :");
				String address = scan.nextLine();
				Customer cust = new Customer(name, fName, email, tel, address, idUser); 
				if(authenticate.addCustomer(cust))	
					return authenticate.existCustomerByEmail(email).getIdCustomer();
			}
			else {
				System.out.println("Nous allons associer la commande en cours avec le compte client : " + customer);
				return customer.getIdCustomer();
			}
		}
		else System.out.println("vous n'avez pas saisi un email valide");	
		return 0;
	}

	/**
	 * Méthode qui réalise la connexion/deconnexion d'un utilisateur
	 * si l'utilisateur n'existe pas, il lui est proposé d'en créer un
	 */
	private static void connection() {
		if(login != null) {
			System.out.println("Souhaitez-vous vous déconnecter ? Oui/Non");
			String response = scan.next();
			if(response.equalsIgnoreCase("Oui")) {
				System.out.println("A bientôt " + login + TEXT_RESET);
				login = null;
				idUser = 0;
			}				
		}
		else {
			System.out.println("saisissez votre login : ");
			String log = scan.next();
			System.out.println("saisissez votre password : ");
			String pwd = scan.next();

			int id = authenticate.existUser(log,pwd);
			if(id > 0)	{
				login = log;
				idUser = id;
				System.out.print(TEXT_BLUE);
				if (authenticate.isAdmin(id) != null) adminSubMenu();
			}

			else {
				System.out.println("login ou password incorrect");
				System.out.println("Nouvel utilisateur, pour créer un compte, tapez ok");
				String ok = scan.next();
				if(ok.equalsIgnoreCase("ok")) {
					newUser();
				}
			}
		}
	}

	private static void adminSubMenu() {
		try {
			System.out.println("Bonjour " + login +". Vous êtes dans le sous-menu dédié aux administrateurs.");
			IAdminImpl adminJobs = new IAdminImpl();
			int choiceAdmin = 0;
			while(choiceAdmin != 8) {
				try {
					System.out.println("\n" + "Pour réaliser une action, tapez le code correspondant");
					
					System.out.println("" + "1 : Ajouter une formation dans la bdd");
					System.out.println("2 : Retirer une formation de la bdd");
					System.out.println("3 : Mettre à jour une formation");
					
					System.out.println("" + "4 : Ajouter une catégorie dans la bdd");
					System.out.println("5 : Retirer une catégorie de la bdd");
					System.out.println("6 : Mettre à jour une catégorie");
					
					System.out.println("" + "7 : Afficher les commandes d'un client");
					System.out.println("" +"8 : Quitter le sous-menu administrateur");			
					
					
					choiceAdmin = scanInt();
					switch(choiceAdmin) {
					case 1 : addCourseToDb(adminJobs);				
					break;					
					case 2 : removeCourseFromDb(adminJobs);
					break;					
					case 3 : updateCourseFromDb(adminJobs);
					break;					
					case 4 : addCategoryToDb(adminJobs);
					break;						
					case 5 : removeCategoryFromDb(adminJobs);
					break;
					case 6 : updateCategoryFromDb(adminJobs);
					break;
					case 7 : displayOrderHistoryFromIdCustomer(adminJobs);
					break;
					case 8 : System.out.println("Vous quittez le menu admin..."+ TEXT_RESET);
							login = null;
							idUser = 0;
					break;
					default : System.out.println("veuillez saisir une valeur entre 1 et 8");
					}
				} catch (Exception e) {
					System.out.println("Erreur: " + e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("Erreur lors de l'accès au menu Admin : " + e.getMessage());
		}
	}

	private static void displayOrderHistoryFromIdCustomer(IAdminImpl adminJobs) {
		ArrayList<Customer> customers = adminJobs.getAllCustomers();
		if(customers.size()>0) {
			customers.forEach(System.out::println);
			System.out.println("Saisissez l'ID du client dont vous voulez connaitre l'historique : ");
			int id = scanInt();
			if (adminJobs.readCustomer(id) != null) {
				ArrayList<Order> orders = adminJobs.getAllOrders(id);
				if (orders.size()>0) {
					orders.forEach(System.out::println);
				} else System.out.println("Pas encore de commande à afficher");
			}
				
			else System.out.println("L'ID saisi ne correspond à aucun client");
		}
		else System.out.println("Aucun client à afficher");
		
			
	}

	private static void updateCategoryFromDb(IAdminImpl adminJobs) {
		displayCategories();
		System.out.println("Saisissez l'ID de la catégorie que vous souhaitez supprimer : ");
		int id = scanInt();
		Category category = business.readOneCategory(id);
		if(category != null) {
			System.out.println("Saisissez le nouveau nom de la catégorie : (30 caractères max)");
			String name = checkLength(scan.nextLine(), 30);
			if (adminJobs.updateCategoryFromDb(category))
				System.out.println("Mise à jour effectuée avec succès");
			else System.out.println("Erreur lors de la MAJ");
		}
		else System.out.println("L'ID saisi ne correspond à aucune catégorie");

		
	}

	private static void removeCategoryFromDb(IAdminImpl adminJobs) {
		displayCategories();
		System.out.println("Saisissez l'ID de la catégorie que vous souhaitez supprimer : ");
		int id = scanInt();
		if (adminJobs.removeCategoryFromDb(id))
			System.out.println("Suppression effectué avec succès");
		else System.out.println("Erreur lors de la suppression");
	}

	private static void addCategoryToDb(IAdminImpl adminJobs) {
		System.out.println("Saisissez le nom de la catégorie : (30 caractères max)");
		String name = checkLength(scan.nextLine(), 30);
		
		if (adminJobs.addCategoryToDb(new Category(name)))
			System.out.println("Ajout effectué avec succès");
		else System.out.println("Erreur lors de l'ajout");
	}

	private static void updateCourseFromDb(IAdminImpl adminJobs) {
		System.out.println("Rappel des données de la formation:");
		int id = displayOneCourse();
		if (id != 0) {
			Course updatedCourse = buildNewCourse();
			updatedCourse.setIdCourse(id);
			if (adminJobs.updateCourseFromDb(updatedCourse))
				System.out.println("Mise à jour effectuée avec succès");
			else System.out.println("Erreur lors de la mise à jour");
		} 
	}

	private static int displayOneCourse() {
		displayCourses();
		System.out.println("Saisissez l'ID de la formation dont vous souhaitez afficher les détails: ");
		int id = scanInt();
		Course course = business.readOneCourse(id);
		if ( course != null) {
			System.out.println("ID: " + course.getIdCourse());
			System.out.println("Nom: " + course.getName());
			System.out.println("Description: " + course.getDescription());
			System.out.println("Durée: " + course.getDurationInDays() + " jours");
			System.out.println("Distanciel: " + (course.getIsRemote() ? "oui" : "non"));
			System.out.println("Prix: " + course.getPrice() + "€");
			Category category = business.readOneCategory(course.getCategory());
			if(category != null) {
				System.out.println("Catégorie: " + category.getName());
			}
			else System.out.println("Catégorie: Pas encore classé dans une catégorie");
			
			return id;
		} else {
			System.out.println("L'ID saisi ne correspond à aucune formation.");
			return 0;
		}
	}

	private static void removeCourseFromDb(IAdminImpl adminJobs) {
		displayCourses();
		System.out.println("Saisissez l'ID de la formation que vous souhaitez supprimer : ");
		int id = scanInt();
		if (adminJobs.removeCourseFromDb(id))
			System.out.println("Suppression effectué avec succès");
		else System.out.println("Erreur lors de la suppression");
	}
	
	private static Course buildNewCourse() {
		scan.nextLine();
		System.out.println("\nSaisissez le nom de la formation : (30 caractères max)");
		String name = checkLength(scan.nextLine(), 30);
		System.out.println("Saisissez une brève description : (100 caractères max)");
		String desc = checkLength(scan.nextLine(), 100);
		System.out.println("Saisissez la durée en jours : ");
		int duration = scanInt();;
		System.out.println("La formation peut-elle être suivie en distanciel ? Oui/Non ");
		String remote = scan.next();
		boolean isRemote = false;
		if (remote.equalsIgnoreCase("Oui")) {
			isRemote = true;
		}
		System.out.println("Saisissez le prix de la formation:");
		double price = Double.parseDouble(scan.next());
		
		//j'arrive pas à insérer une clé qui n'existe pas pour idCategory ou sans clé TODO
		
		System.out.println("Saisissez l'ID de la catégorie ou 0 si aucune catégorie ne s'applique:");
		displayCategories();
		int idCategory = scan.nextInt();
		if (idCategory!=0) return new Course(name, desc, duration, isRemote, price, idCategory);
		else return new Course(name, desc, duration, isRemote, price);
		
	}

	private static void addCourseToDb(IAdminImpl adminJobs) {
		if (adminJobs.addCourseToDb(buildNewCourse()))
			System.out.println("Ajout effectué avec succès");
		else System.out.println("Erreur lors de l'ajout");
	}

	private static String checkLength(String nextLine, int maxLength) {
		String name = nextLine;
		while (name.length()>maxLength) {
			System.out.println("Votre saisie est trop longue, veuillez n'utiliser que " + maxLength + " caractères.");
			name = scan.nextLine();
		}
		return name;
		
	}

	/**
	 * Méthode qui ajoute un nouvel utilisateur en base
	 */
	public static void newUser() {
		System.out.println("saisissez un login :");
		String login = scan.next();			
		int id = authenticate.existUser(login);	
		if(id == 0) { 
			System.out.println("saisissez votre password :");
			String password = scan.next();
			authenticate.addUser(login,password);		
			System.out.println("Ne perdez pas ces infos de connexion...");
			stop(2);
			System.out.println("création de l'utilisateur terminé, merci de vous connecter");
		}
		else	System.out.println("Login déjà existant en base, veuillez vous connecter");
	}

	public static void stop(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int scanInt() {
		while(!scan.hasNextInt()) {
			System.out.println("saisissez une valeur entière svp");
			scan.next();
		}
		return scan.nextInt();
	}

	public static boolean isValidEmail(String email) {
		String regExp = "^[A-Za-z0-9._-]+@[A-Za-z0-9._-]+\\.[a-z][a-z]+$";	
		return email.matches(regExp);
	}
}
