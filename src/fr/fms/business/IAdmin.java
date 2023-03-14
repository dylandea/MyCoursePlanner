package fr.fms.business;

import java.util.ArrayList;

import fr.fms.entities.Category;
import fr.fms.entities.Course;
import fr.fms.entities.Customer;

public interface IAdmin {

	public boolean addCourseToDb(Course course);

	public boolean removeCourseFromDb(int id);

	public boolean updateCourseFromDb(Course updatedCourse);

	public boolean addCategoryToDb(Category category);

	public boolean removeCategoryFromDb(int id);

	public boolean updateCategoryFromDb(Category updatedCategory);

	public ArrayList<Customer> getAllCustomers();
	
}
