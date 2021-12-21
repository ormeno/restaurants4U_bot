package bot.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import bot.bo.Anuncios;
import res.resourceLoader;

import java.io.IOException;


public class gestionFoto {
	
	Properties prop = resourceLoader.loadProperties("configuracion.properties");
	
	public void descargaFoto(String Url, String numFoto) {
			
    	//File fileDestino = new File(idAnuncio + "fotoDescTienda"+prop.getProperty("var.BotUsername")+".jpg");
		File fileDestino = new File("..//..//..//var//www//html//"+numFoto+"fotoDescTienda" + prop.getProperty("var.BotUsername") + ".jpg");
	    try {	    			
	    	
			URLConnection conn = new URL(Url.toString()).openConnection();
			conn.connect();
		    
			//String sDirectorioTrabajo = System.getProperty("user.dir");
        	//System.out.println("El directorio de trabajo es " + sDirectorioTrabajo);
        	
			InputStream in = conn.getInputStream();
			OutputStream out = new FileOutputStream(fileDestino);
			int b = 0;
			while (b != -1) {
			  b = in.read();
			  if (b != -1)
			    out.write(b);
			}
			out.close();
			in.close();		
			//System.out.println("El archivo fue descargado. IdAnuncio : " + numFoto);
	    } catch (MalformedURLException e) {
	    	System.out.println("descargaFoto - la url no es valida!");
	    } catch (Exception ex) {
	    	 System.out.println("descargaFoto - Exception descargaFoto");
	        ex.printStackTrace();
	    }
	    
	}
			
	public void eliminarFichero(String numFoto) {
		//File fichero = new File(idAnuncio + "fotoDescTienda"+prop.getProperty("var.BotUsername")+".jpg");
		File fichero = new File("..//..//..//var//www//html//"+numFoto+"fotoDescTienda"+prop.getProperty("var.BotUsername")+".jpg");
        if (!fichero.exists()) {
            System.out.println("El archivo a borrar no existe. IdAnuncio : " + numFoto);
        } else {
        	try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("eliminarFichero - Exception");
				e.printStackTrace();
			}	
            fichero.delete();
            System.out.println("eliminarFichero a los 3 segundos - La imagen fue eliminada. IdAnuncio : " + numFoto);
        }

    }
	
}
