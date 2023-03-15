/**
 * @author Dylan De Albuquerque - 2023
 * 
 */

package fr.fms.business;

import java.util.ArrayList;

import fr.fms.dao.CategoryDao;
import fr.fms.dao.CourseDao;
import fr.fms.dao.Dao;
import fr.fms.dao.DaoFactory;
import fr.fms.dao.OrderDao;
import fr.fms.entities.Category;
import fr.fms.entities.Course;
import fr.fms.entities.Customer;
import fr.fms.entities.Order;

public class IAdminImpl implements IAdmin {
	private Dao<Course> courseDao = DaoFactory.getCourseDao();
	private Dao<Category> categoryDao = DaoFactory.getCategoryDao();
	private Dao<Customer> customerDao = DaoFactory.getCustomerDao();
	private Dao<Order> orderDao = DaoFactory.getOrderDao();
	
	
	@Override
	public boolean addCourseToDb(Course course) {
		return courseDao.create(course);
	}

	@Override
	public boolean removeCourseFromDb(int id) {
		return ((CourseDao) courseDao).delete(id);
	}

	@Override
	public boolean updateCourseFromDb(Course updatedCourse) {
		return ((CourseDao) courseDao).update(updatedCourse);
	}

	@Override
	public boolean addCategoryToDb(Category category) {
		return categoryDao.create(category) ;
	}

	@Override
	public boolean removeCategoryFromDb(int id) {
		 if (((CourseDao) courseDao).updateCourseCatBeforeCatRemoved(id))
		return ((CategoryDao) categoryDao).delete(id);
		 else return false;
	}

	@Override
	public boolean updateCategoryFromDb(Category updatedCategory) {
		return ((CategoryDao) categoryDao).update(updatedCategory) ;
	}

	@Override
	public ArrayList<Customer> getAllCustomers() {
		return customerDao.readAll();
	}
	@Override
	public ArrayList<Order> getAllOrders(int id) {
		return ((OrderDao)orderDao).readAll(id);
	}
	@Override
	public Customer readCustomer(int id) {
		return customerDao.read(id);
	}
	@Override
	public boolean checkIfCategoryExists(int idCategory) {
		if (categoryDao.read(idCategory) != null) return true;
		else return false;
	}
	@Override
	public ArrayList<Course> getCoursesFromThisOrder(int idOrder) {
		return ((CourseDao) courseDao).readAllByOrder(idOrder);
	}

}
