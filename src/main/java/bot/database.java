package bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import res.resourceLoader;

//import org.slf4j.Logger;
//import org.slf4j.event.Level;

import bot.bo.Usuarios;

/** 
 * Conexión básica a la base de datos PostgreSQL.
 *
 * @author Xules You can follow me on my website http://www.codigoxules.org/en
 * Puedes seguirme en mi web http://www.codigoxules.org).
 */
public class database {
	
	
	public Connection connectMySQL() {
		 
		Connection conn = null;
		try {
			
			Properties prop = resourceLoader.loadProperties("configuracion.properties");
			
			String url = prop.getProperty("var.url");
			String user = prop.getProperty("var.user");
			String password = prop.getProperty("var.password");
			
			conn = DriverManager.getConnection(url, user, password);

			
			if (conn == null) {
				System.out.println("Failed to make connection MySQL!");
			}
		} catch (SQLException e) {
			System.out.println("MySQL Connection Failed!");
			e.printStackTrace();
		}
		return conn;
 
	}
 	 
                  
   }
