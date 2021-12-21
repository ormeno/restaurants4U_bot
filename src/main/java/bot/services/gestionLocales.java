package bot.services;

import java.util.List;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.vdurmont.emoji.EmojiParser;

import bot.bo.Locales;
import bot.bo.Pedidos;
import bot.bo.Usuarios;
import bot.dao.LocalesDAO;
import res.resourceLoader;

public class gestionLocales extends TelegramLongPollingBot {
	
	public void MostrarLocales(long chat_id, long user_id, Usuarios usuario) {
    	SendMessage msg = new SendMessage();
    	msg.setChatId(Long.toString(chat_id));
    	msg.setParseMode(ParseMode.HTML);		 
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");        
        String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
        LocalesDAO loc = new LocalesDAO();
        List<Locales> locales = null;
        String texto = null;
        
    	if (usuario.getPais()==null){
           locales = loc.obtenerLocales();
        } else {
           locales = loc.obtenerLocales(usuario.getPais(),usuario.getCiudad());
        }
    	if (!locales.isEmpty()) {
    		texto = prop.getProperty("var.Locales1") + " \n";
            texto = texto.concat("\n");
            if (usuario.getPais()!=null){          	
            	  texto = texto.concat("<b> " + prop.getProperty("var.Locales2") + usuario.getPais() + " - " + usuario.getCiudad() + " </b> \n");      
            }
            String catAux = "A";
            int z = 0;
            for (int i = 0; i < locales.size(); i++) {
          	 if (!locales.get(i).getTipo_local().equals(catAux)) {
          		texto = texto.concat("\n");
          		if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH") ) {
          		  texto = texto.concat(ballEmoji + "<b>" +locales.get(i).getTipo_local().substring(0,locales.get(i).getTipo_local().indexOf("-")) + " </b> \n");
          		} else {
          			texto = texto.concat(ballEmoji + "<b>" +locales.get(i).getTipo_local().substring(locales.get(i).getTipo_local().indexOf("-")+1,locales.get(i).getTipo_local().length()) + " </b> \n");
          		}
          		texto = texto.concat("\n");
               	catAux = locales.get(i).getTipo_local();
          	 }
          	 texto = texto.concat(squareEmoji + "<b>/" + locales.get(i).getNombre() + "</b>");
          	 if (usuario.getPais()==null){
          		if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH") ) {
            	  texto = texto.concat(" -  " + locales.get(i).getPais().substring(0, locales.get(i).getPais().indexOf("-")) + " - " + locales.get(i).getCiudad().substring(0, locales.get(i).getCiudad().indexOf("-")) + " \n");
          	    } else {
          	      texto = texto.concat(" -  " + locales.get(i).getPais().substring(locales.get(i).getPais().indexOf("-")+1,locales.get(i).getPais().length()) + " - " + locales.get(i).getCiudad().substring(locales.get(i).getCiudad().indexOf("-")+1,locales.get(i).getCiudad().length()) + " \n");
          	    }
             } else {
            	 texto = texto.concat("\n");
             }
           	 z++;
             if (z>50) {
            	 msg.setText(texto);	    
                 try {            
                    execute(msg);            
                 } catch (TelegramApiException e) {
                               e.printStackTrace();
                 }
                 z=0;
                 texto="";
                }
            }  	 
    	} else {
          texto = prop.getProperty("var.Locales3");
          if (usuario.getPais()!=null){
          	texto = texto.concat(" " + usuario.getPais() + " - " + usuario.getCiudad() + " \n");
          	texto = texto.concat("\n");
            texto = texto.concat(prop.getProperty("var.Locales4")+ " \n");
            texto = texto.concat("\n");
           } else {
          	 texto = texto.concat("\n");
           }
          texto = texto.concat(prop.getProperty("var.Locales5") + " \n");
          texto = texto.concat("\n");
    	}
   	    msg.setText(texto);
	    try {
	        execute(msg);
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
