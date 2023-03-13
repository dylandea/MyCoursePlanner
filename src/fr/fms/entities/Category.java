package fr.fms.entities;

public class Category {
	private int id;
	private String name;
	
	public Category(int id, String name) {
		this.id = id;
		this.name = name;

	}

	public Category(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	@Override
	public String toString() {
		return centerString(String.valueOf(id)) + centerString(name);
	}
	
	public static String centerString(String str) {
		if(str.length() >= 20) return str;
		String dest = "                                        ";
		int deb = (20 - str.length())/2 ;
		String data = new StringBuilder(dest).replace( deb, deb + str.length(), str ).toString();
		return data;
	}
}
