package fr.fms.entities;

public class Admin {
	private int idAdmin;
	private String lastName;
	private String firstName;
	private String email;
	private int idUser;

	public Admin(int idAdmin, String lastName, String firstName, String email, int idUser) {
		super();
		this.idAdmin = idAdmin;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.idUser = idUser;
	}
	
	public Admin(String lastName, String firstName, String email, int idUser) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.idUser = idUser;
	}
	
	public int getIdAdmin() {
		return idAdmin;
	}
	
	public void setIdAdmin(int idAdmin) {
		this.idAdmin = idAdmin;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	
	public int getIdUser() {
		return idUser;
	}
	
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

}
