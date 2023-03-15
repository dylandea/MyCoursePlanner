/**
 * @author Dylan De Albuquerque - 2023
 * 
 */

package fr.fms.entities;

public class Customer {
	private int idCustomer;
	private String lastName;
	private String firstName;
	private String email;
	private String phone;
	private String address;
	private int idUser;
	
	public Customer(int idCustomer, String lastName, String firstName, String email, String phone, String address, int idUser) {
		this.idCustomer = idCustomer;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.idUser = idUser;
	}
	
	public Customer(String lastName, String firstName, String email, String phone, String address, int idUser) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.idUser = idUser;
	}
	
	

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public Customer(String lastName, String firstName, String email, String phone, String address) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public int getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String name) {
		this.lastName = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return "Client N°" + idCustomer + ", Nom:" + lastName + ", Prénom:" + firstName + ", Email:" + email + ", Tel:" + phone + ", Adresse:" + address;
	}
}
