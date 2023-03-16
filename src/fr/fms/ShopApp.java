/**
 * Application console de vente de formations permettant d'exploiter une couche métier/dao pour créer un panier en ajoutant ou retirant des formations
 * puis passer commande à tout instant, cela génère une commande en base avec tous les éléments associés
 * @author Dylan De Albuquerque - 2023
 * 
 */
package fr.fms;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import fr.fms.authentication.Authenticate;
import fr.fms.business.IAdminImpl;
import fr.fms.business.IBusinessImpl;
import fr.fms.dao.BddConnection;
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
					scan.close();
					BddConnection.closeConnection();
					break;					
					default : System.out.println("Ce choix n'existe pas");
					}
				} catch (Exception e) {
					System.out.println("Erreur: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			System.out.println("Erreur lors du lancement de l'application : " + e.getMessage());
		} 
	}

	/**
	 * Méthode qui permet de chercher des formations selon un mot clé
	 */

	private static void searchByKeyword() {
		scan.nextLine();
		System.out.println("saisissez le mot-clé à rechercher:");
		String keyword = scan.nextLine();
		ArrayList<Course> courses = business.readByKeyword(keyword);
		if(courses.size()>0) {
			displayTitles();
			courses.forEach(System.out::println);
		}
		else System.out.println("Aucune formation disponible en ce moment.");

	}
	/**
	 * Méthode qui permet d'afficher toutes les formations selon si elles sont en distanciel ou pas
	 * @param isRemote
	 */
	private static void displayAllCoursesIsRemote(boolean isRemote) {
		ArrayList<Course> courses = business.readAllRemoteOrNotRemoteCourses(isRemote);
		if(courses.size()>0) {
			displayTitles();
			courses.forEach(System.out::println);
		}
		else System.out.println("Aucune formation disponible en ce moment.");
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
	 * Méthode qui permet de factoriser l'affichage des titres de colonne pour les formations
	 */
	public static void displayTitles() { 	
		System.out.println(
				Course.centerString(COLUMN_ID) +
				Course.centerString(COLUMN_NAME) + 
				Course.centerString(COLUMN_DESCRIPTION) + 
				Course.centerString(COLUMN_DAYS_DURATION) + 
				Course.centerString(COLUMN_IS_REMOTE) + 
				Course.centerString(COLUMN_PRICE)
				);
	}

	/**
	 * Méthode qui affiche toutes les formations en base en centrant le texte 
	 */
	public static void displayCourses() { 	
		displayTitles();
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
			displayTitles();
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
		displayCourses();
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
		displayCourses();
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
		System.out.println();
		if(business.isCartEmpty()) 	System.out.println("PANIER VIDE");
		else {
			System.out.println("CONTENU DU PANIER :");
			displayTitles();
			business.getCart().forEach(System.out::println);
			if(flag) {
				System.out.println("MONTANT TOTAL : " + business.getTotal() + "€");
				System.out.println("\nSouhaitez vous passer commande ? Oui/Non :");
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
					System.out.println("Votre commande N°" + idOrder + " a bien été validée !");
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
		System.out.println("Avez vous déjà une fiche client ? saisissez une adresse email valide pour vérifier :");
		String email = scan.next();		
		if(isValidEmail(email)) {	
			Customer customer = authenticate.existCustomerByEmail(email);
			if(customer == null) {
				System.out.println("Vous n'avez pas de fiche client chez nous, nous allons en créer une ensemble...");
				scan.nextLine();	
				System.out.println("saisissez votre nom de famille:");
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
				System.out.println("Nous allons associer la commande en cours avec votre fiche client : " + customer.getEmail());
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
				if (authenticate.isAdmin(id)) adminSubMenu();
			}

			else {

				System.out.println("login ou password incorrect");
				System.out.println("1 - Réessayer");
				System.out.println("2 - Créer un compte");
				System.out.println("3 - Annuler");

				int choiceLog = scanInt();
				switch (choiceLog) {
				case 1:
					connection();
					break;
				case 2:
					newUser();
					break;
				case 3:
					break;
				default:
					System.out.println("Mauvaise saisie");
					break;
				}




			}
		}
	}

	/**
	 * Méthode qui ouvre un sous-menu spécial pour l'administrateur, qui lui permettra de gérer les formations et les catégories en BDD + d'afficher les commandes d'un client
	 */


	private static void adminSubMenu() {
		try {
			System.out.println("Bonjour " + login +". Vous êtes dans le sous-menu dédié aux administrateurs.");
			IAdminImpl adminJobs = new IAdminImpl();
			int choiceAdmin = 0;
			while(choiceAdmin != 8) {
				try {
					System.out.println("\n" + "Pour réaliser une action, tapez le code correspondant");

					System.out.println("1 : Ajouter une formation dans la bdd");
					System.out.println("2 : Retirer une formation de la bdd");
					System.out.println("3 : Mettre à jour une formation");

					System.out.println("4 : Ajouter une catégorie dans la bdd");
					System.out.println("5 : Retirer une catégorie de la bdd");
					System.out.println("6 : Mettre à jour une catégorie");

					System.out.println("7 : Afficher les commandes d'un client");
					System.out.println("8 : Quitter le sous-menu administrateur");			


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
				} catch (InputMismatchException ime) {
					System.out.println("Mauvaise saisie");
				} catch (Exception e) {
					System.out.println("Erreur: " + e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("Erreur lors de l'accès au menu Admin : " + e.getMessage());
		}
	}

	/**
	 * Méthode qui permet d'afficher les commandes d'un utilisateur
	 * @param adminJobs
	 */
	private static void displayOrderHistoryFromIdCustomer(IAdminImpl adminJobs) {
		System.out.println();
		ArrayList<Customer> customers = adminJobs.getAllCustomers();
		if(customers.size()>0) {
			customers.forEach(System.out::println);
			System.out.println("\nSaisissez l'ID du client dont vous voulez connaitre l'historique : ");
			int id = scanInt();
			System.out.println();
			if (adminJobs.readCustomer(id) != null) {

				ArrayList<Order> orders = adminJobs.getAllOrders(id);

				if (orders.size()>0) {
					orders.forEach(System.out::println);
					scan.nextLine();
					System.out.println("Souhaitez-vous obtenir les détails d'une des commandes: Oui/non");
					String choiceOrder=scan.nextLine();
					while (choiceOrder.equalsIgnoreCase("oui")) {

						System.out.println("\nSaisissez le N° de la commande dont vous voulez obtenir les détails:");
						int choiceDetailledOrder = scanInt();
						scan.nextLine();
						ArrayList<Course> courses = adminJobs.getCoursesFromThisOrder(choiceDetailledOrder);
						if (courses != null) {
							displayTitles();
							courses.forEach(System.out::println);
						}

						System.out.println("\nSouhaitez-vous obtenir les détails d'une des commandes: Oui/non");
						choiceOrder=scan.nextLine();
					}

				} else System.out.println("Pas encore de commande à afficher");
			}

			else System.out.println("L'ID saisi ne correspond à aucun client");
		}
		else System.out.println("Aucun client à afficher");


	}


	/**
	 * Methode qui pemet de mettre à jour une catégorie de la bdd
	 * @param adminJobs
	 */
	private static void updateCategoryFromDb(IAdminImpl adminJobs) {

		displayCategories();
		System.out.println("Saisissez l'ID de la catégorie que vous souhaitez mettre à jour : ");
		int id = scanInt();

		Category category = business.readOneCategory(id);

		if(category != null) {

			System.out.println("Saisissez le nouveau nom de la catégorie : (30 caractères max)");
			scan.nextLine();
			String name = checkLength(scan.nextLine(), 30);
			category.setName(name);

			if (adminJobs.updateCategoryFromDb(category))
				System.out.println("Mise à jour effectuée avec succès");
			else System.out.println("Erreur lors de la MAJ");
		}
		else System.out.println("L'ID saisi ne correspond à aucune catégorie");
	}

	/**
	 * Methode qui pemet de supprimer une catégorie de la bdd
	 * 
	 */

	private static void removeCategoryFromDb(IAdminImpl adminJobs) {
		displayCategories();
		System.out.println("Saisissez l'ID de la catégorie que vous souhaitez supprimer : ");
		int id = scanInt();

		if (adminJobs.removeCategoryFromDb(id))
			System.out.println("Suppression effectué avec succès");
		else System.out.println("L'ID saisi ne correspond à aucune catégorie en bdd");

	}

	/**
	 * Methode qui pemet d'ajouter une catégorie à la bdd
	 * @param adminJobs
	 */

	private static void addCategoryToDb(IAdminImpl adminJobs) {
		scan.nextLine();
		System.out.println("Saisissez le nom de la catégorie : (30 caractères max)");
		String name = checkLength(scan.nextLine(), 30);

		if (adminJobs.addCategoryToDb(new Category(name)))
			System.out.println("Ajout effectué avec succès");
		else System.out.println("Erreur lors de l'ajout");
	}

	/**
	 * Methode qui pemet de mettre à jour une formation de la bdd
	 * 
	 */

	private static void updateCourseFromDb(IAdminImpl adminJobs) {
		int id = displayOneCourse();
		if (id != 0) {
			Course originalCourse = business.readOneCourse(id);

			String name = originalCourse.getName();
			String desc =originalCourse.getDescription();	
			int duration=originalCourse.getDurationInDays();
			boolean isRemote = originalCourse.getIsRemote();
			double price = originalCourse.getPrice();
			int idCategory = originalCourse.getCategory();


			int choiceUpdate = -1;
			while(choiceUpdate != 0) {

				System.out.println("\nQuel champ souhaitez-vous mettre à jour ? (Tapez 0 pour quitter le menu de mise à jour)");
				System.out.println("1 - Nom");
				System.out.println("2 - Description");
				System.out.println("3 - Durée en jours");
				System.out.println("4 - Distanciel");
				System.out.println("5 - Prix");
				System.out.println("6 - Catégorie");
				choiceUpdate = scanInt();
				System.out.println();
				scan.nextLine();
				switch (choiceUpdate) {
				case 0:System.out.println("Vous quittez le menu de mise à jour...");


				break;
				case 1:System.out.println("Saisissez le nom de la formation : (30 caractères max)");
				name = checkLength(scan.nextLine(), 30);

				break;
				case 2:System.out.println("Saisissez une brève description : (100 caractères max)");
				desc = checkLength(scan.nextLine(), 100);

				break;
				case 3:System.out.println("Saisissez la durée en jours :");

				duration = scanInt();
				while (duration < 1) {
					System.out.println("Erreur: minimum 1 jour");
					System.out.println("Saisissez la durée en jours :");
					duration = scanInt();
				}

				break;
				case 4:System.out.println("La formation est-elle prodiguée en distanciel ? Oui/Non ");
				String remote = scan.next();
				isRemote = false;
				if (remote.equalsIgnoreCase("Oui")) {
					isRemote = true;
				}

				break;

				case 5:System.out.println("Saisissez le prix de la formation: (exemple: 15,99)");
				price = scanDouble();


				break;

				case 6:idCategory = checkCategory(adminJobs);

				break;

				default: System.out.println("Mauvaise valeur");
				break;
				}


			}
			Course updatedCourse = new Course(originalCourse.getIdCourse(), name, desc, duration, isRemote, price, idCategory);

			if (adminJobs.updateCourseFromDb(updatedCourse))
				System.out.println("Mise à jour effectuée avec succès");
			else System.out.println("Erreur lors de la mise à jour");
		} 
	}

	/**
	 * Methode qui pemet d'afficher une formation avec davantage de détails, la description en entier, le nom de la catégorie, etc...
	 * @return
	 */

	private static int displayOneCourse() {
		displayCourses();
		System.out.println("\nSaisissez l'ID de la formation dont vous souhaitez afficher les détails: \n");
		int id = scanInt();
		Course course = business.readOneCourse(id);
		if ( course != null) {
			System.out.println("ID de la formation: " + course.getIdCourse());
			System.out.println("Nom: " + course.getName());
			System.out.println("Description: " + course.getDescription());
			System.out.println("Durée: " + course.getDurationInDays() + " jours");
			System.out.println("Distanciel: " + (course.getIsRemote() ? "oui" : "non"));
			System.out.println("Prix: " + course.getPrice() + "€");
			Category category = business.readOneCategory(course.getCategory());
			System.out.println("Catégorie: " + ((category==null)?"Non classé":category.getName()));

			return id;
		} else {
			System.out.println("L'ID saisi ne correspond à aucune formation.");
			return 0;
		}
	}

	/**
	 * Méthode qui finalise l'ajout d'une formation dans la BDD
	 * @param adminJobs
	 */
	private static void addCourseToDb(IAdminImpl adminJobs) {
		Course course = buildNewCourse(adminJobs);
		if (course != null ) {
			if (adminJobs.addCourseToDb(course))
				System.out.println("Ajout effectué avec succès");
			else System.out.println("Erreur lors de l'ajout");
		}
	}

	/**
	 * Methode qui pemet de supprimer une formation de la bdd
	 * @param adminJobs
	 */

	private static void removeCourseFromDb(IAdminImpl adminJobs) {
		displayCourses();
		System.out.println("Saisissez l'ID de la formation que vous souhaitez supprimer : ");
		int id = scanInt();
		if (adminJobs.removeCourseFromDb(id))
			System.out.println("Suppression effectuée avec succès");
		else System.out.println("Cette formation n'existe pas en BDD");
	}

	/**
	 * Méthode qui permet de construire un nouvel objet Course avant d'en faire ce qu'on veut (update, create..)
	 * @param adminJobs
	 * @return
	 */

	private static Course buildNewCourse(IAdminImpl adminJobs) {
		scan.nextLine();
		System.out.println("\nSaisissez le nom de la formation : (30 caractères max)");
		String name = checkLength(scan.nextLine(), 30);
		System.out.println("Saisissez une brève description : (100 caractères max)");
		String desc = checkLength(scan.nextLine(), 100);
		System.out.println("Saisissez la durée en jours :");
		int duration = scanInt();
		while (duration < 1) {
			System.out.println("Erreur: minimum 1 jour");
			System.out.println("Saisissez la durée en jours :");
			duration = scanInt();
		}
		System.out.println("La formation peut-elle être suivie en distanciel ? Oui/Non ");
		String remote = scan.next();
		boolean isRemote = false;
		if (remote.equalsIgnoreCase("Oui")) {
			isRemote = true;
		}
		System.out.println("Saisissez le prix de la formation: (exemple: 15,99)");
		double price = scanDouble();


		int idCategory = checkCategory(adminJobs);

		return new Course(name, desc, duration, isRemote, price, idCategory);
	}

	/**
	 * Méthode qui vérifie si une catégorie existe
	 * @param idCategory
	 * @param adminJobs
	 * @return l'id de la categorie
	 */
	private static int checkCategory(IAdminImpl adminJobs) {
		int idCategory = -1;
		System.out.println("Saisissez l'ID de la catégorie, ou tapez 0 pour ne pas la classer dans une catégorie:");
		displayCategories();
		idCategory = scanInt();
		while (adminJobs.checkIfCategoryExists(idCategory) == false && idCategory!=0) {
			System.out.println("Erreur, cet ID ne correspond à aucune catégorie en bdd");
			System.out.println("\nSaisissez l'ID de la catégorie, ou tapez 0 pour ne pas la classer dans une catégorie:");
			displayCategories();
			idCategory = scanInt();
		}
		return idCategory;
	}



	/**
	 * Méthode qui vérifie la longueur de la saisie utilisateur, pour empêcher des champs trops longs dans la BDD
	 * @param nextLine
	 * @param maxLength
	 * @return
	 */
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

		System.out.println("\nPour créer votre compte, saisissez un login :");
		String login = scan.next();			
		int id = authenticate.existUser(login);	
		if(id == 0) { 
			System.out.println("saisissez votre password :");
			String password = scan.next();
			authenticate.addUser(login,password);		
			System.out.println("Ne perdez pas ces infos de connexion...");
			stop(2);
			System.out.println("Création de l'utilisateur terminée, merci de vous connecter");
		}
		else	System.out.println("Login déjà existant en base, veuillez vous connecter");
	}

	/**
	 * Méthode qui permet de donner l'impression que l'ordinateur réfléchit quelques secondes
	 * @param time 
	 */

	public static void stop(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode qui permet de gérer les saisies autres qu'un nombre entier, à utiliser à la place de scan.nextInt()
	 * @return
	 */

	public static int scanInt() {
		while(!scan.hasNextInt()) {
			System.out.println("saisissez une valeur entière svp");
			scan.next();
		}
		return scan.nextInt();
	}

	/**
	 * Méthode qui permet de gérer les saisies autres qu'un double, à utiliser à la place de scan.nextDouble()
	 * @return
	 */

	public static double scanDouble() {
		while(!scan.hasNextDouble()) {
			System.out.println("saisissez une valeur valide svp");
			scan.next();
		}
		return scan.nextDouble();
	}

	/**
	 * Méthode qui vérifie que l'email saisi a bien un format d'email valide
	 * @param email
	 * @return
	 */

	public static boolean isValidEmail(String email) {
		String regExp = "^[A-Za-z0-9._-]+@[A-Za-z0-9._-]+\\.[a-z][a-z]+$";	
		return email.matches(regExp);
	}
}
