package bot.services;

import static java.lang.Math.toIntExact;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.vdurmont.emoji.EmojiParser;

import bot.bo.Usuarios;
import bot.dao.LocalesDAO;
import bot.dao.UsuariosDAO;
import res.resourceLoader;

public class gestionPerfil extends TelegramLongPollingBot {
	
		
	public void MostrarPerfil(long chat_id, long user_id, Usuarios usuario) {
    	SendMessage msg = new SendMessage();
    	msg.setChatId(Long.toString(chat_id));
    	msg.setParseMode(ParseMode.HTML);		                		               	               	    
   	    String texto = cuerpoPerfil(usuario);   	     	   
 	    InlineKeyboardMarkup markupInline = botoneraPerfil(usuario);
		msg.setReplyMarkup(markupInline);    	
   	    msg.setText(texto);
	    try {
	        execute(msg);
	    } catch (TelegramApiException e) {
	        e.printStackTrace();
	    }
    }
	
	public String cuerpoPerfil(Usuarios usuario) {    	
		Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");        
        String texto = new String(prop.getProperty("var.cuerpoPerfil1") + ": <b>" + usuario.getNombre() + "</b> \n");        
      	texto = texto.concat(" \n");
      	texto = texto.concat(squareEmoji + " <b>" + prop.getProperty("var.cuerpoPerfil2") + " </b>@"+ usuario.getAliastg() +" \n");        	            	
      	texto = texto.concat(squareEmoji + " <b>" + prop.getProperty("var.cuerpoPerfil3") + " </b> "+ usuario.getIdioma() +" \n");
      	texto = texto.concat(squareEmoji + " <b>" + prop.getProperty("var.cuerpoPerfil4") + " </b> "+ usuario.getPais() +" \n");
      	texto = texto.concat(squareEmoji + " <b>" + prop.getProperty("var.cuerpoPerfil5") + " </b> "+ usuario.getCiudad() +" \n");
      	return texto;
    }
	
	private InlineKeyboardMarkup botoneraPerfil(Usuarios usuario) {
		Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();               
	    List<InlineKeyboardButton> rowInline1 = new ArrayList<>();        
	    InlineKeyboardButton iKBIdi = new InlineKeyboardButton();
	    iKBIdi.setText(prop.getProperty("var.botPerfil1"));
	    iKBIdi.setCallbackData("Perfil&IDIOMA");
	    rowInline1.add(iKBIdi);
	    InlineKeyboardButton iKBPais = new InlineKeyboardButton();
	    iKBPais.setText(prop.getProperty("var.botPerfil2"));
	    iKBPais.setCallbackData("Perfil&PAISES");
	    rowInline1.add(iKBPais);
	    rowsInline.add(rowInline1);                   
        markupInline.setKeyboard(rowsInline);        
    	return markupInline;	
    }
	
	public void  GestionPerfilCB(long chat_id, long user_id, String call_data, Usuarios usuario) {
    	String texto = null;    	
    	SendMessage respuesta = new SendMessage();
    	respuesta.setChatId(Long.toString(chat_id));
    	respuesta.setParseMode(ParseMode.HTML);
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        UsuariosDAO usu = new UsuariosDAO();        
    	String okEmoji = EmojiParser.parseToUnicode(":ok_hand:");
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	LocalesDAO loc = new LocalesDAO();
        if (call_data.contains("IDIOMA")) {
    		texto = new String(prop.getProperty("var.variosPerfil1") + " \n");
    		String[] idiomasOk = prop.getProperty("list.idiomas").split(",");
	    	List<String> listaIdiomasOk = Arrays.asList(idiomasOk); 
	        int nBot = 0;
	        int j = 0;
	        while (j < listaIdiomasOk.size()) {
	          List<InlineKeyboardButton> rowInlineCat = new ArrayList<>();
	          int x = 0;
	          while (x<2 && nBot<listaIdiomasOk.size()) {
	        	     String idioma = listaIdiomasOk.get(nBot).substring(0,listaIdiomasOk.get(nBot).indexOf(" "));
	        	     InlineKeyboardButton iKBId = new InlineKeyboardButton();
	        	     iKBId.setText(listaIdiomasOk.get(nBot));
	        	     iKBId.setCallbackData("Perfil&Idioma_" + idioma);
		             rowInlineCat.add(iKBId);      
		             nBot++;
		             x++;
		    	  }
		      rowsInline.add(rowInlineCat);
	    	  j=nBot;
	        }           
	        markupInline.setKeyboard(rowsInline);
	        respuesta.setReplyMarkup(markupInline);
      	} else if (call_data.contains("PAISES")) {
    		texto = new String(prop.getProperty("var.variosPerfil2") + " \n");
    		List<String> listaPaisesOk = loc.obtenerPaises(usuario);
	        int nBot = 0;
	        int j = 0;
	        while (j < listaPaisesOk.size()) {
	          List<InlineKeyboardButton> rowInlineCat = new ArrayList<>();
	          int x = 0;
	          while (x<2 && nBot<listaPaisesOk.size()) {
	        	     InlineKeyboardButton iKBP = new InlineKeyboardButton();
	        	     iKBP.setText(listaPaisesOk.get(nBot));
	        	     iKBP.setCallbackData("Perfil&Pais_" + listaPaisesOk.get(nBot));
		             rowInlineCat.add(iKBP);      
		             nBot++;
		             x++;
		    	  }
		      rowsInline.add(rowInlineCat);
	    	  j=nBot;
	        }           
	        markupInline.setKeyboard(rowsInline);
	        respuesta.setReplyMarkup(markupInline);
      	} else if (call_data.contains("Pais_NONE") || call_data.contains("Pais_NINGUNO") ) { 
       	   usu.actuUbicacion(user_id, usuario.getId_usuario(), null, null);
       	   Usuarios us = usu.obtenerUsuarioId(usuario.getId_usuario());
       	   texto = cuerpoPerfil(us);                	  
 		   texto = texto.concat(" \n");
 		   texto = texto.concat(okEmoji + prop.getProperty("var.variosPerfil6"));      
 		   markupInline = botoneraPerfil(us);
 		   respuesta.setReplyMarkup(markupInline);
       	} else if (call_data.contains("Pais_")) { 
      		texto = new String(prop.getProperty("var.variosPerfil3") + " \n");
      		String pais =  call_data.substring(call_data.indexOf("Pais_")+5,call_data.length());
      		List<String> listaCiudadesOk = loc.obtenerCiudades(pais,usuario);
	        int nBot = 0;
	        int j = 0;
	        while (j < listaCiudadesOk.size()) {
	          List<InlineKeyboardButton> rowInlineCat = new ArrayList<>();
	          int x = 0;
	          while (x<2 && nBot<listaCiudadesOk.size()) {
	        	    InlineKeyboardButton iKBCi = new InlineKeyboardButton();
	        	    iKBCi.setText(listaCiudadesOk.get(nBot));
	        	    iKBCi.setCallbackData("Perfil&"+pais+"_Ciudad_" + listaCiudadesOk.get(nBot));
		             rowInlineCat.add(iKBCi);      
		             nBot++;
		             x++;
		    	  }
		      rowsInline.add(rowInlineCat);
	    	  j=nBot;
	        }           
	        markupInline.setKeyboard(rowsInline);
	        respuesta.setReplyMarkup(markupInline);      		
      	} else if (call_data.contains("Idioma_")) { 
      	   String idioma =  call_data.substring(call_data.indexOf("Idioma_")+7,call_data.length());
       	   usu.actuIdioma(user_id, usuario.getId_usuario(), idioma);
       	   Usuarios us = usu.obtenerUsuarioId(usuario.getId_usuario());
       	   texto = cuerpoPerfil(us);                	  
 		   texto = texto.concat(" \n");
 		   texto = texto.concat(okEmoji);
 		   markupInline = botoneraPerfil(us);
 		   respuesta.setReplyMarkup(markupInline);
      	} else if (call_data.contains("_Ciudad_")) { 
      	   String pais =  call_data.substring(call_data.indexOf("Perfil&")+7,call_data.indexOf("_Ciudad_"));
      	   String ciudad =  call_data.substring(call_data.indexOf("_Ciudad_")+8,call_data.length());
      	   usu.actuUbicacion(user_id, usuario.getId_usuario(), pais, ciudad);
      	   Usuarios us = usu.obtenerUsuarioId(usuario.getId_usuario());
      	   texto = cuerpoPerfil(us);                	  
		   texto = texto.concat(" \n");
		   texto = texto.concat(okEmoji + prop.getProperty("var.variosPerfil5"));      
		   markupInline = botoneraPerfil(us);
		   respuesta.setReplyMarkup(markupInline);
      	}
    	respuesta.setText(texto);	     
        try {          
          execute(respuesta);                                              
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
    }
			
	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getBotUsername() {
		Properties prop = resourceLoader.loadProperties("configuracion.properties");
        return prop.getProperty("var.BotUsername");
	}
	@Override
	public String getBotToken() {
		Properties prop = resourceLoader.loadProperties("configuracion.properties");
        return prop.getProperty("var.BotToken");
	}

}
