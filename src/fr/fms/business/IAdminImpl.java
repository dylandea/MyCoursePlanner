package fr.fms.business;

import java.util.ArrayList;

import fr.fms.dao.CategoryDao;
import fr.fms.dao.CourseDao;
import fr.fms.dao.Dao;
import fr.fms.dao.DaoFactory;
import fr.fms.entities.Category;
import fr.fms.entities.Course;
import fr.fms.entities.Customer;
import fr.fms.entities.Order;
import fr.fms.entities.OrderItem;
import fr.fms.entities.User;

public class IAdminImpl implements IAdmin {
	private Dao<Course> courseDao = DaoFactory.getCourseDao();
	private Dao<Category> categoryDao = DaoFactory.getCategoryDao();
	private Dao<Customer> customerDao = DaoFactory.getCustomerDao();
	
	
	@Override
	public boolean addCourseToDb(Course course) {
		if (courseDao.create(course)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeCourseFromDb(int id) {
		if (((CourseDao) courseDao).delete(id)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCourseFromDb(Course updatedCourse) {
		if (((CourseDao) courseDao).update(updatedCourse)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addCategoryToDb(Category category) {
		if (categoryDao.create(category)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeCategoryFromDb(int id) {
		if (((CategoryDao) categoryDao).delete(id)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCategoryFromDb(Category updatedCategory) {
		if (((CategoryDao) categoryDao).update(updatedCategory)) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Customer> getAllCustomers() {
		return customerDao.readAll();
	}

	public ArrayList<Order> getAllOrders(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Customer readCustomer(int id) {
		return customerDao.read(id);
	}

}
