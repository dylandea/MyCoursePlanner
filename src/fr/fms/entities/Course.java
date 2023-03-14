package fr.fms.entities;

public class Course {
	private int idCourse;
	private String name;
	private String description;
	private int durationInDays;
	private boolean isRemote;
	private double price;
	private int category;
	
	public static final int MAX_STRING_LENGTH = 30;
	


	public Course(int idCourse, String name, String description, int durationInDays, boolean isRemote, double price) {
		this.idCourse = idCourse;
		this.name = name;
		this.description = description;
		this.durationInDays = durationInDays;
		this.isRemote = isRemote;
		this.price = price;
	}

	
	public Course(String name, String description, int durationInDays, boolean isRemote, double price, int category) {
		this.name = name;
		this.description = description;
		this.durationInDays = durationInDays;
		this.isRemote = isRemote;
		this.price = price;
		this.category = category;
	}


	public Course(String name, String description, int durationInDays, boolean isRemote, double price) {
		this.name = name;
		this.description = description;
		this.durationInDays = durationInDays;
		this.isRemote = isRemote;
		this.price = price;
	}


	public int getIdCourse() {
		return idCourse;
	}


	public void setIdCourse(int idCourse) {
		this.idCourse = idCourse;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getDurationInDays() {
		return durationInDays;
	}


	public void setDurationInDays(int durationInDays) {
		this.durationInDays = durationInDays;
	}


	public boolean getIsRemote() {
		return isRemote;
	}


	public void setRemote(boolean isRemote) {
		this.isRemote = isRemote;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}

	

	public int getCategory() {
		return category;
	}


	public void setCategory(int category) {
		this.category = category;
	}


	@Override
	public String toString() {
		return  centerString(String.valueOf(idCourse)) + centerString(name) + centerString(description) + centerString(String.valueOf(durationInDays)+" jours") + (isRemote ? centerString("oui") : centerString("non")) + centerString(String.valueOf(price)+"€");
	}
	
	public static String centerString(String str) {
		if(str.length() >= 20) str=str.substring(0, 20)+"...";
		String dest = "                              ";
		int deb = (MAX_STRING_LENGTH - str.length())/2 ;
		String data = new StringBuilder(dest).replace( deb, deb + str.length(), str ).toString();
		return data;
	}
	
	
}
