package res;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class resourceLoader {

	
	public static Properties loadProperties(String propName)
    {
		//is = new FileInputStream("src/main/resources/configuracion.properties");
		//prop.load(is);
		Properties prop = new Properties();	
		try {
			prop.load(resourceLoader.class.getResourceAsStream(propName));
			//System.out.println("LoadProp OK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}
	public static Properties ObtenerProperties(String idioma) {            			    	    	    	    	   	   
	      Properties prop = null;
	       if (idioma==null) {
	    	   prop = loadProperties("configuracion.properties");	
	       } else if (idioma.equals("ESPAÑOL") || idioma.equals("SPANISH")){
	    	   prop = loadProperties("configuracion.properties");	
	 	   } else if (idioma.equals("INGLES") || idioma.equals("ENGLISH")) {
	 		  prop = loadProperties("configuracionEN.properties");	
	 	   } else {
	 		  prop = loadProperties("configuracion.properties");	
	 	   }
	       return prop;  
	    }
}
