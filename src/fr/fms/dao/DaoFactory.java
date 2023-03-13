package fr.fms.dao;


import fr.fms.entities.Admin;
import fr.fms.entities.Category;
import fr.fms.entities.Course;
import fr.fms.entities.Customer;
import fr.fms.entities.Order;
import fr.fms.entities.OrderItem;
import fr.fms.entities.User;

public class DaoFactory {
	
	public static Dao<Course> getCourseDao() {
		return new CourseDao();		
	}
	
	public static Dao<User> getUserDao() {
		return new UserDao();
	}
	
	public static Dao<Admin> getAdminDao() {
		return new AdminDao();
	}
	
 	public static Dao<Category> getCategoryDao() {
 		return new CategoryDao();
 	}
 	
 	public static Dao<Order> getOrderDao() {
 		return new OrderDao();
 	}
 	
 	public static Dao<OrderItem> getOrderItemDao() {
 		return new OrderItemDao();
 	}
 	
 	public static Dao<Customer> getCustomerDao() {
 		return new CustomerDao();
 	}
}
