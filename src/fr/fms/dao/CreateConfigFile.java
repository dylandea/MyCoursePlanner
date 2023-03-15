package fr.fms.dao;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * programme permettant de regénérer le fichier de configuration
 * @author Dylan De Albuquerque - 2023
 * 
 */

public class CreateConfigFile {
	public static void main(String[] args) {
		try (OutputStream ops = new FileOutputStream("files/config.properties")) {		
			Properties properties = new Properties();	
			properties.setProperty("db.driver", "org.mariadb.jdbc.Driver");
			properties.setProperty("db.url", "jdbc:mariadb://localhost:3306/MyCoursePlanner");
			properties.setProperty("db.login", "dylan");
			properties.setProperty("db.password", "Tintin*64");			
			//properties.store(ops , "No comment !");
			//System.out.println("file config.properties generated ! ");
		}
		 catch (IOException io) {
	            io.printStackTrace();
		}
	}

}
