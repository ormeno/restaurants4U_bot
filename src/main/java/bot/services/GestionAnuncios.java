package bot.services;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.Math.toIntExact;

import com.vdurmont.emoji.EmojiParser;

//import bot.gestionFoto;
import bot.bo.Anuncios;
import bot.bo.LocalUsuarios;
import bot.bo.Locales;
import bot.bo.Pedidos;
import bot.bo.ProductosPedido;
import bot.bo.Usuarios;
import bot.dao.AnuncioDAO;
import bot.dao.ErrorRangoPrecio;
import bot.dao.ErrorRangoUnidades;
import bot.dao.LocalUsuariosDAO;
import bot.dao.LocalesDAO;
import bot.dao.PedidoDAO;
import bot.dao.UsuariosDAO;
import res.resourceLoader;

public class GestionAnuncios  extends TelegramLongPollingBot  {
	
	private gestionPedido gPedido = new gestionPedido();
	
	public GestionAnuncios() {
		// TODO Auto-generated constructor stub
		
	}
	
	Properties propGeneral = resourceLoader.loadProperties("configuracion.properties");
	//NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(prop.getProperty("var.lenguaje"),prop.getProperty("var.pais")));
	
	
	public void ConsultaAnunciosPorId(long chat_id, long user_id, Usuarios usuario, String message_text, Locales local) {
    	//      
		Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        SendMessage msg = new SendMessage();
        msg.setChatId(Long.toString(chat_id));
        msg.setParseMode(ParseMode.HTML);      
        SendInvoice invoice = new SendInvoice();
        AnuncioDAO anun = new AnuncioDAO();   
        UsuariosDAO usu = new UsuariosDAO(); 
        int id = Integer.parseInt(message_text.substring(message_text.indexOf("_")+1,message_text.length()));
        LocalUsuarios localUsuario = null;
        Anuncios res = null;
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        localUsuario = locUsu.obtenerLocalUsuarioId(local.getId_local(), usuario.getId_usuario());
        if (localUsuario.getTipo_usuario().equals("PR")) {
   	    	res = anun.leerAnuncio(id,"A",usuario.getId_usuario(), null,local.getId_local());
   	    //} else {
   	    //	res = anun.leerAnuncio(id,"V",usuario.getId_usuario(), null,local.getId_local());
   	    //}                     
	        if (res == null) {
	        	 String textAnuncio = prop.getProperty("var.Anuncios1");
	        	 msg.setText(textAnuncio);
	        }else {
	     	  String textAnuncio = cuerpoAnuncio(res, usuario);          
			  //InlineKeyboardMarkup markupInline = botoneraSigAnt(res, usuario, user_id, usuario.getTipo(), "AnunPorId", local.getId_local());
	     	  InlineKeyboardMarkup markupInline = botoneraAltaAnuncio(res.getId_anuncio(),true,local.getId_local(),usuario);
	     	  msg.setReplyMarkup(markupInline);
	
	          msg.setText(textAnuncio);                    
	        }            	  
        } else {
        	String textAnuncio = prop.getProperty("var.Ppal25");
        	msg.setText(textAnuncio);      
        }
        try {            
           execute(msg);            
           gestionFoto gf = new gestionFoto();
           if (res != null) {
             gf.eliminarFichero(res.getId_anuncio() + "_" + res.getVersion_foto());
           }
        } catch (TelegramApiException e) {
                    e.printStackTrace();
        }
    }

	
	public void ConsultaAnunciosBajaNoPub(long chat_id, long user_id, Usuarios usuario, int id_local) {
    	//     
		Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        SendMessage mensajeListaAnuncios = new SendMessage();
        mensajeListaAnuncios.setChatId(Long.toString(chat_id));
        mensajeListaAnuncios.setParseMode(ParseMode.HTML);
        SendMessage mensajeRes = new SendMessage();
        mensajeRes.setChatId(Long.toString(chat_id));
        mensajeRes.setParseMode(ParseMode.HTML);
        String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
    	String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");  
    	String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
    	List<Anuncios> lAnuncios = null;
    	AnuncioDAO anun = new AnuncioDAO(); 
    	String texto = null;
    	String textoLista = null;
    	LocalUsuarios localUsuario = null;
        LocalesDAO loc = new LocalesDAO();
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        
        localUsuario = locUsu.obtenerLocalUsuarioId(id_local, usuario.getId_usuario());
        if (localUsuario.getTipo_usuario().equals("PR")) {  
          lAnuncios = anun.listAnunciosNoPub(id_local);
          Locales local = loc.obtenerLocalId(id_local);
          NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
	      textoLista = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
	      textoLista = textoLista.concat("\n");
	      textoLista = textoLista.concat(prop.getProperty("var.Anuncios2")+ " \n");
          textoLista = textoLista.concat("\n");
          String catAux = "A";
          int z = 0;
          for (int i = 0; i < lAnuncios.size(); i++) {
        	 if (lAnuncios.get(i).getCategoria()==null || !lAnuncios.get(i).getCategoria().equals(catAux)) {
        		 textoLista = textoLista.concat("\n");
        		 if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH")) {
             	   textoLista = textoLista.concat(ballEmoji + "<b> ¡¡ " +lAnuncios.get(i).getCategoria() + " !! </b> \n");
        		 } else {
        	    		String cateIng = null;
        	    		if (lAnuncios.get(i).getCategoria()==null) {
        	    			cateIng=null;
        	    		} else {
	        	    		if (lAnuncios.get(i).getCategoria().equals("ENTRANTES")) cateIng="STARTERS";
	        	            if (lAnuncios.get(i).getCategoria().equals("PRINCIPALES")) cateIng="MAIN DISHES";
	        	            if (lAnuncios.get(i).getCategoria().equals("POSTRES")) cateIng="DESSERTS";
	        	            if (lAnuncios.get(i).getCategoria().equals("BEBIDAS")) cateIng="DRINKS";
        	    		}
        	            textoLista = textoLista.concat(ballEmoji + "<b> ¡¡ " + cateIng + " !! </b> \n");
        	     }
             	 textoLista = textoLista.concat("\n");
             	 catAux = lAnuncios.get(i).getCategoria();
        	 }
        	 textoLista = textoLista.concat(squareEmoji + "<b>" + lAnuncios.get(i).getTitulo() + "</b> - " + df.format(lAnuncios.get(i).getPrecio()) + " (/"+local.getNombre()+"_"+ lAnuncios.get(i).getId_anuncio() +")\n");
         	 z++;
             if (z>25) {
               mensajeListaAnuncios.setText(textoLista);	    
               try {            
                  execute(mensajeListaAnuncios);            
               } catch (TelegramApiException e) {
                             e.printStackTrace();
               }
               z=0;
               textoLista="";
              }
          }
        } else {
        	texto = prop.getProperty("var.Ppal25");
        		    
        }
    try {            
    	if (textoLista!=null) {
        	mensajeListaAnuncios.setText(textoLista);
            execute(mensajeListaAnuncios);
        }
    	if (texto!=null) {
    		mensajeRes.setText(texto);
    		execute(mensajeRes);
    	}
                   
     } catch (TelegramApiException e) {
                 e.printStackTrace();
     }
 }
	
	
	
	
	
    protected SendInvoice mostrarPago(long chat_id, Anuncios res, Usuarios usuario, InlineKeyboardMarkup markupInline) {
	 SendInvoice invoice = new SendInvoice();
	 BigDecimal precioProd = res.getPrecio().movePointRight(2);
  
	  List<LabeledPrice> prices1 = new ArrayList<>();
	  prices1.add(new LabeledPrice("Producto", precioProd.intValue()));
	  //prices1.add(new LabeledPrice("Impuestos", result2.intValue() ));        
	  
	  invoice.setChatId(Long.toString(chat_id));
	  invoice.setTitle(res.getTitulo());
	  invoice.setDescription(" Pago con tarjeta ");
	  invoice.setPayload("idAnuncio_" + res.getId_anuncio() + "_Usu_" + usuario.getNombre());               		                   		                                  
	  invoice.setProviderToken(propGeneral.getProperty("var.BotTokenStripe"));
	  invoice.setStartParameter("u4u-invoice-0001");
	  invoice.setCurrency("EUR");        
	  invoice.setNeedName(true);
	  invoice.setNeedEmail(false);
	  invoice.setNeedPhoneNumber(true);
	  invoice.setNeedShippingAddress(true);
	  invoice.setIsFlexible(false);
	  invoice.setReplyMarkup(markupInline);
	  invoice.setPrices(prices1);
	  return invoice;
    }
 
	public String cuerpoAnuncio(Anuncios anuncio, Usuarios usuario) {
    	//
		Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
        String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
        String warningEmoji = EmojiParser.parseToUnicode(":warning:");
        String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");  
        
        
        String textAnuncio = null;
        LocalesDAO loc = new LocalesDAO();
        Locales local = loc.obtenerLocalId(anuncio.getId_local());
        NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
        textAnuncio = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
        textAnuncio = textAnuncio.concat(" \n");
        textAnuncio = textAnuncio.concat(" <b>" + anuncio.getTitulo() + "</b> \n");
        df.setMaximumFractionDigits(2);
      	textAnuncio = textAnuncio.concat(" \n");
    	if (anuncio.getDescripcion()!=null) {
          textAnuncio = textAnuncio.concat(ballEmoji + " " + anuncio.getDescripcion() + " \n");
      	  textAnuncio = textAnuncio.concat(" \n");
    	}
    	if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH")) {
        	textAnuncio = textAnuncio.concat(squareEmoji + " " + prop.getProperty("var.Anuncios3")+ anuncio.getCategoria() +" \n");        	
    	} else {
    		String cateIng = null;
    		if (anuncio.getCategoria()==null) {
    			cateIng=null;
    		} else {
	    		if (anuncio.getCategoria().equals("ENTRANTES")) cateIng="STARTERS";
	            if (anuncio.getCategoria().equals("PRINCIPALES")) cateIng="MAIN DISHES";
	            if (anuncio.getCategoria().equals("POSTRES")) cateIng="DESSERTS";
	            if (anuncio.getCategoria().equals("BEBIDAS")) cateIng="DRINKS";
    		}
    		textAnuncio = textAnuncio.concat(squareEmoji + " " + prop.getProperty("var.Anuncios3")+ cateIng +" \n");		
    	}
//      	textAnuncio = textAnuncio.concat(squareEmoji + " " + " <b>Contacto:</b> "+ anuncio.getContacto() +" \n");
      	if (local.getImpuesto()!=null) {
      		textAnuncio = textAnuncio.concat(squareEmoji + " " + prop.getProperty("var.Anuncios4") + df.format(anuncio.getPrecio()) + " " + local.getImpuesto() +" "+ prop.getProperty("var.Anuncios55") + " \n");
      	 } else {
      		textAnuncio = textAnuncio.concat(squareEmoji + " " + prop.getProperty("var.Anuncios4") + df.format(anuncio.getPrecio())+" \n"); 
      	 }
      	//textAnuncio = textAnuncio.concat(squareEmoji + " " + " <b>Usuario:</b> @"+ usuario.getAliastg()+" \n");
      	LocalUsuarios localUsuario = null;
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        localUsuario = locUsu.obtenerLocalUsuarioId(local.getId_local(), usuario.getId_usuario());
        if (localUsuario.getTipo_usuario().equals("PR")) {
        	textAnuncio = textAnuncio.concat(squareEmoji + " " + prop.getProperty("var.Anuncios5") + anuncio.getUnidades() +" \n");
        	textAnuncio = textAnuncio.concat(squareEmoji + " " + prop.getProperty("var.Anuncios6") + anuncio.getId_anuncio() +" \n");
        } else {
        	if (anuncio.getUnidades() == 0) {
        	  textAnuncio = textAnuncio.concat(warningEmoji + prop.getProperty("var.Anuncios7") + " \n");
        	}
        }
      	textAnuncio = textAnuncio.concat(" \n");
      	if (anuncio.getFechaBaja()!=null) {
      		textAnuncio = textAnuncio.concat(warningEmoji + " " + prop.getProperty("var.Anuncios8") + " \n");
      	}
      	textAnuncio = textAnuncio.concat(" \n");
      	if (anuncio.getValidado().equals("N")) {
      		textAnuncio = textAnuncio.concat(warningEmoji + " " + prop.getProperty("var.Anuncios9") + " \n");
      	}
      	textAnuncio = textAnuncio.concat(" \n");
      //	if (anuncio.getValidado().equals("D")) {
      //		textAnuncio = textAnuncio.concat(warningEmoji + " " + " <b>ANUNCIO DENUNCIADO</b> \n");
     // 	}
       // descargar imagen, subirla a mi domininio, mostrarla y borrarla de mi dominio
      	String urlFoto = obtenerUrl(anuncio);
      	System.out.println("cuerpoAnuncio - urlFoto-> " + urlFoto);
          														          
	    textAnuncio = textAnuncio.concat(urlFoto);
      	return textAnuncio;
    }
    
    public String obtenerUrl(Anuncios anuncio) {
    	int idAnuncio = anuncio.getId_anuncio();
    	int vFoto = anuncio.getVersion_foto();
    	String numFoto = idAnuncio + "_" + vFoto;
    	String idFoto = anuncio.getFoto(); 
    	//System.out.println("obtenerUrl - idFoto --> " + idFoto);
	    if (idFoto == null) {
	    	return "";
	    } else {
	    	GetFile getFile = new GetFile();
	 	    getFile.setFileId(idFoto);              
	 	                       
	 	    File file;
	 		try {
	 			file = execute(getFile);
	 			URL fileUrl = new URL(file.getFileUrl(getBotToken()));
	 			//System.out.println("obtenerUrl - Photo --> " + fileUrl);
	 			gestionFoto gf = new gestionFoto();
	 			String url = fileUrl.toString();
	 			gf.descargaFoto(url, numFoto);		
	 		} catch (TelegramApiException | MalformedURLException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		//return 	"<a href='http://localhost/" + idAnuncio + "fotoDescTienda"+prop.getProperty("var.BotUsername")+".jpg'>&#8204;</a> \n";
	 		return 	"<a href='http://ormenobots.ga/" + numFoto + "fotoDescTienda"+propGeneral.getProperty("var.BotUsername")+".jpg'>&#8204;</a> \n";
	    }
    }
    
    public String obtenerUrlInLine(Anuncios anuncio) {
    	int idAnuncio = anuncio.getId_anuncio();
    	int vFoto = anuncio.getVersion_foto();
    	String numFoto = idAnuncio + "_" + vFoto;
    	String idFoto = anuncio.getFoto(); 
    	//System.out.println("obtenerUrl - idFoto --> " + idFoto);
	    if (idFoto == null) {
	    	return "";
	    } else {
	    	GetFile getFile = new GetFile();
	 	    getFile.setFileId(idFoto);              
	 	                       
	 	    File file;
	 		try {
	 			file = execute(getFile);
	 			URL fileUrl = new URL(file.getFileUrl(getBotToken()));
	 			//System.out.println("obtenerUrl - Photo --> " + fileUrl);
	 			gestionFoto gf = new gestionFoto();
	 			String url = fileUrl.toString();
	 			gf.descargaFoto(url, numFoto);		
	 		} catch (TelegramApiException | MalformedURLException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		//return 	"<a href='http://localhost/" + idAnuncio + "fotoDescTienda"+prop.getProperty("var.BotUsername")+".jpg'>&#8204;</a> \n";
	 		return 	"http://ormenobots.ga/" + numFoto + "fotoDescTienda"+propGeneral.getProperty("var.BotUsername")+".jpg";
	    }
    }
	
    protected InlineKeyboardMarkup botoneraSigAnt(Anuncios anuncio, Usuarios usuario, long user_id, String tipo, String texto, int id_local) {
    	
    	String imgTarjeta = EmojiParser.parseToUnicode(":credit_card:");
    	String imgAnt = EmojiParser.parseToUnicode(":rewind:");
    	String imgSig = EmojiParser.parseToUnicode(":fast_forward:");
    	String imgCarro = EmojiParser.parseToUnicode(":shopping_trolley:");
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());

    	int idAnuncio = anuncio.getId_anuncio();
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        AnuncioDAO anun = new AnuncioDAO();
        int ant = anun.leerIdAntAnuncio(idAnuncio, tipo, usuario.getId_usuario(),texto,id_local);
        int sig =  anun.leerIdSigAnuncio(idAnuncio, tipo, usuario.getId_usuario(),texto,id_local);
        if (texto!=null && texto.equals("AnunPorId")) {
        	ant = 0;
            sig = 0;
        }

        if (ant != 0){        	
        	if (texto != null) {
        		InlineKeyboardButton iKBant1 = new InlineKeyboardButton();
        		iKBant1.setText(imgAnt);
        		iKBant1.setCallbackData(id_local + "?" + texto + "#anterAnuncio_" + ant);
        		rowInline.add(iKBant1);
        	} else {
        		InlineKeyboardButton iKBant2 = new InlineKeyboardButton();
        		iKBant2.setText(imgAnt);
        		iKBant2.setCallbackData(id_local + "?anterAnuncio_" + ant);
        		rowInline.add(iKBant2);
        	}
            			
        }
        

	    if (anuncio.getPrecio().compareTo(BigDecimal.ZERO) != 0 && anuncio.getTitulo() != null && anuncio.getUnidades() > 0) {
	    	InlineKeyboardButton iKBCarro = new InlineKeyboardButton();
	    	iKBCarro.setText(prop.getProperty("var.Anuncios54"));
	    	iKBCarro.setCallbackData(id_local + "?carroAnuncio_" + idAnuncio);
	    	rowInline.add(iKBCarro);
		} 

       
        if (sig != 0){
        	if (texto != null) {
        		InlineKeyboardButton iKBSig = new InlineKeyboardButton();
        		iKBSig.setText(imgSig);
        		iKBSig.setCallbackData(id_local + "?" + texto + "#sigAnuncio_" + sig);
        		rowInline.add(iKBSig);
        	} else {
        		InlineKeyboardButton iKBSig2 = new InlineKeyboardButton();
        		iKBSig2.setText(imgSig);
        		iKBSig2.setCallbackData(id_local + "?sigAnuncio_" + sig);
        		rowInline.add(iKBSig2);
        	}
        } 
        rowsInline.add(rowInline);
        // Botones de gestion de anuncio si es el propietario
        LocalUsuariosDAO localUsu = new LocalUsuariosDAO();
        LocalUsuarios localUsuario = localUsu.obtenerLocalUsuarioId(id_local, usuario.getId_usuario());
        if ((localUsuario.getTipo_usuario().equals("PR")) && texto!=null && texto.equals("AnunPorId")) {
          List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
          List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
             InlineKeyboardButton iKBTit = new InlineKeyboardButton();
             iKBTit.setText(prop.getProperty("var.Anuncios56"));
             iKBTit.setCallbackData(id_local +"?tituloAnuncio_" + idAnuncio);
             rowInline1.add(iKBTit);
             InlineKeyboardButton iKBDes = new InlineKeyboardButton();
             iKBDes.setText(prop.getProperty("var.Anuncios57"));
             iKBDes.setCallbackData(id_local +"?desAnuncio_" + idAnuncio);
             rowInline1.add(iKBDes);                
             InlineKeyboardButton iKBCat = new InlineKeyboardButton();
             iKBCat.setText(prop.getProperty("var.Anuncios58"));
             iKBCat.setCallbackData(id_local +"?cateAnuncio_" + idAnuncio);
             rowInline1.add(iKBCat);
             InlineKeyboardButton iKBUni = new InlineKeyboardButton();
             iKBUni.setText(prop.getProperty("var.Anuncios59"));
             iKBUni.setText(prop.getProperty("var.Anuncios59"));
             rowInline1.add(iKBUni);
             InlineKeyboardButton iKBPrec = new InlineKeyboardButton();
             iKBPrec.setText(prop.getProperty("var.Anuncios60"));
             iKBPrec.setCallbackData(id_local +"?precioAnuncio_" + idAnuncio);
             rowInline2.add(iKBPrec);
             InlineKeyboardButton iKBFot = new InlineKeyboardButton();
             iKBFot.setText(prop.getProperty("var.Anuncios61"));
             iKBFot.setCallbackData(id_local +"?fotoAnuncio_" + idAnuncio);
             rowInline2.add(iKBFot);            
             if (anuncio.getValidado().equals("N")) {
            	 InlineKeyboardButton iKBPub = new InlineKeyboardButton();
            	 iKBPub.setText(prop.getProperty("var.Anuncios62"));
            	 iKBPub.setCallbackData(id_local + "?pubAnuncio_"  + idAnuncio);
               rowInline2.add(iKBPub);
             }
             if (anuncio.getFechaBaja()==null) {
            	 InlineKeyboardButton iKBBorr = new InlineKeyboardButton();
            	 iKBBorr.setText(prop.getProperty("var.Anuncios63"));
            	 iKBBorr.setCallbackData(id_local + "?borrarAnuncio_"  + idAnuncio);
               rowInline2.add(iKBBorr);
             }
             rowsInline.add(rowInline1);
             rowsInline.add(rowInline2);
        }
        
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public void GestionAltaAnuncio(long chat_id, long user_id, Usuarios usuario, int id_local) {
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	SendMessage msg = new SendMessage();
    	msg.setChatId(Long.toString(chat_id));
    	msg.setParseMode(ParseMode.HTML);		                		                 	
   	    int pk = 0;
   	    UsuariosDAO usu = new UsuariosDAO();
   	    usu.actuTipo("N", usuario.getId_usuario());
 	    String textoAnuncio = new String(prop.getProperty("var.Anuncios10") + " \n"); 	    
 	    textoAnuncio = textoAnuncio.concat(" \n");
 	    textoAnuncio = textoAnuncio.concat(prop.getProperty("var.Anuncios11") + " \n");
        msg.setText(textoAnuncio);
        AnuncioDAO anun = new AnuncioDAO();
        pk = anun.altaAnuncio(usuario.getId_usuario(),id_local);      
        InlineKeyboardMarkup markupInline = botoneraAltaAnuncio(pk,true,id_local,usuario);
		msg.setReplyMarkup(markupInline);
	    try {
	        execute(msg);
	    } catch (TelegramApiException e) {
	        e.printStackTrace();
	    }
    }
    
    public InlineKeyboardMarkup botoneraAltaAnuncio(int pk, boolean botonPub,int id_local, Usuarios usuario) {
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        
        InlineKeyboardButton iKBTit = new InlineKeyboardButton();
        iKBTit.setText(prop.getProperty("var.Anuncios56"));
        iKBTit.setCallbackData(id_local + "?tituloAnuncio_" + pk);
        rowInline1.add(iKBTit);
        InlineKeyboardButton iKBDes = new InlineKeyboardButton();
        iKBDes.setText(prop.getProperty("var.Anuncios57"));
        iKBDes.setCallbackData(id_local +"?desAnuncio_" + pk);
        rowInline1.add(iKBDes);     
        InlineKeyboardButton iKBCat = new InlineKeyboardButton();
        iKBCat.setText(prop.getProperty("var.Anuncios58"));
        iKBCat.setCallbackData(id_local +"?cateAnuncio_" + pk);
        rowInline1.add(iKBCat);
        InlineKeyboardButton iKBPrec = new InlineKeyboardButton();
        iKBPrec.setText(prop.getProperty("var.Anuncios60"));
        iKBPrec.setCallbackData(id_local +"?precioAnuncio_" + pk);
        rowInline1.add(iKBPrec);
        InlineKeyboardButton iKBUni = new InlineKeyboardButton();
        iKBUni.setText(prop.getProperty("var.Anuncios59"));
        iKBUni.setCallbackData(id_local +"?unidadesAnuncio_" + pk);
        rowInline2.add(iKBUni);
        InlineKeyboardButton iKBFot = new InlineKeyboardButton();
        iKBFot.setText(prop.getProperty("var.Anuncios61"));
        iKBFot.setCallbackData(id_local +"?fotoAnuncio_" + pk);
        rowInline2.add(iKBFot);
        if (botonPub) {
        	InlineKeyboardButton iKBPub = new InlineKeyboardButton();
        	iKBPub.setText(prop.getProperty("var.Anuncios62"));
        	iKBPub.setCallbackData(id_local +"?pubAnuncio_"  + pk);
          rowInline2.add(iKBPub);
        }
        InlineKeyboardButton iKBBorr = new InlineKeyboardButton();
        iKBBorr.setText(prop.getProperty("var.Anuncios63"));
        iKBBorr.setCallbackData(id_local +"?borrarAnuncio_"  + pk);
        rowInline2.add(iKBBorr);
        
         
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);

        markupInline.setKeyboard(rowsInline);        
    	return markupInline;	
    }
    	
    public void GestionFiltros(long chat_id, long user_id, Usuarios usuario, Locales local) {
    	SendMessage msg = new SendMessage();
    	msg.setChatId(Long.toString(chat_id));
    	msg.setParseMode(ParseMode.HTML);		   
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");
   	    int pk = 0;
 	    String texto = new String(womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n"); 	    
 	    texto = texto.concat(" \n");
		texto = texto.concat(local.getDescripcion()+ "\n"); 
		texto = texto.concat(" \n");
		texto = texto.concat(local.getDireccion()+ "\n"); 
		texto = texto.concat(" \n");
		UsuariosDAO usu = new UsuariosDAO();
		List<Usuarios> listaPropietarios = usu.obtenerUsuariosPropietarios(local.getId_local());
		int j = 0;
		while (j < listaPropietarios.size()) {
			if (j==0) {
			  texto = texto.concat(prop.getProperty("var.Locales6") + "@" + listaPropietarios.get(j).getAliastg());  
			} else {
			  texto = texto.concat(" - @" + listaPropietarios.get(j).getAliastg());
			}
			j++; 
		}
		texto = texto.concat(" \n");
		LocalUsuarios localUsuario = null;
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        localUsuario = locUsu.obtenerLocalUsuarioId(local.getId_local(), usuario.getId_usuario());
        if (localUsuario.getTipo_usuario().equals("PR")) {
        	texto = texto.concat("ID: " + local.getId_local()); 
    		texto = texto.concat(" \n");
        }
        msg.setText(texto);
        InlineKeyboardMarkup markupInline = botoneraFiltros(usuario,local);
		msg.setReplyMarkup(markupInline);
	    try {
	        execute(msg);
	    } catch (TelegramApiException e) {
	        e.printStackTrace();
	    }
    }
    
    private InlineKeyboardMarkup botoneraFiltros(Usuarios usuario,Locales local) {
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInlineCarro = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();        
       
        
        String[] categoriasOk = prop.getProperty("list.catValidas").split(",");
    	List<String> listaCatOk = Arrays.asList(categoriasOk); 
        int nBot = 0;
        int j = 0;
        while (j < listaCatOk.size()) {
          List<InlineKeyboardButton> rowInlineCat = new ArrayList<>();
          int x = 0;
          while (x<2 && nBot<listaCatOk.size()) {
        	    InlineKeyboardButton iKBLC = new InlineKeyboardButton();
        	    iKBLC.setText(listaCatOk.get(nBot));
        	    iKBLC.setSwitchInlineQueryCurrentChat(local.getId_local()+"?" + listaCatOk.get(nBot));
	             rowInlineCat.add(iKBLC);     
	             nBot++;
	             x++;
	    	  }
	      rowsInline.add(rowInlineCat);
    	  j=nBot;
        }
        InlineKeyboardButton iKBCarr = new InlineKeyboardButton();
        iKBCarr.setText(prop.getProperty("var.Anuncios53"));
        iKBCarr.setCallbackData(local.getId_local()+"?carroFiltro");
        rowInlineCarro.add(iKBCarr);
        rowsInline.add(rowInlineCarro);
        
        LocalUsuarios localUsuario = null;
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        localUsuario = locUsu.obtenerLocalUsuarioId(local.getId_local(), usuario.getId_usuario());
        if (localUsuario.getTipo_usuario().equals("PR")) {
        	InlineKeyboardButton iKBPF = new InlineKeyboardButton();
        	iKBPF.setText(prop.getProperty("var.Anuncios12"));
        	iKBPF.setCallbackData(local.getId_local() + "?pedidosFiltro");
        	rowInline1.add(iKBPF);
        	InlineKeyboardButton iKBAF = new InlineKeyboardButton();
        	iKBAF.setText(prop.getProperty("var.Anuncios13"));
        	iKBAF.setCallbackData(local.getId_local() + "?altaFiltro");
        	rowInline1.add(iKBAF);
        	rowsInline.add(rowInline1);
        }
        
        markupInline.setKeyboard(rowsInline);        
    	return markupInline;	
    }

    
    public InlineKeyboardMarkup botoneraSiNo(int pk, String flag, Locales local, Usuarios usuario) {
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();        
        Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        InlineKeyboardButton iKBS = new InlineKeyboardButton();
        iKBS.setText(prop.getProperty("var.Anuncios14"));
        iKBS.setCallbackData(local.getId_local()+"?si" + flag + "Confirma_" + pk);
        rowInline1.add(iKBS);
        InlineKeyboardButton iKBN = new InlineKeyboardButton();
        iKBN.setText(prop.getProperty("var.Anuncios15"));
        iKBN.setCallbackData(local.getId_local()+"?no" + flag + "Confirma_" + pk);
        rowInline1.add(iKBN);                
         
        rowsInline.add(rowInline1);        

        markupInline.setKeyboard(rowsInline);        
    	return markupInline;	
    }
    
    public void  GestionAnuncioCB(long message_id, long chat_id, long user_id, String call_data, int id, Usuarios usuario, int id_local) {
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	String texto = null;    	
    	String okEmoji = EmojiParser.parseToUnicode(":ok_hand:");
    	String star2Emoji = EmojiParser.parseToUnicode(":star2:");
    	String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
     	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");
    	//EditMessageText resAnuncio = new EditMessageText().setChatId(Long.toString(chat_id)).setMessageId(toIntExact(message_id)).setParseMode(ParseMode.HTML);   
    	SendMessage resAnuncio = new SendMessage();
    	resAnuncio.setChatId(Long.toString(chat_id));
    	resAnuncio.setParseMode(ParseMode.HTML);
    	SendInvoice invoice = new SendInvoice();
    	AnuncioDAO anun = new AnuncioDAO();
    	Anuncios res = null;
    	UsuariosDAO usu = new UsuariosDAO();
    	PedidoDAO pedido = new PedidoDAO();
    	LocalesDAO loc = new LocalesDAO();
        Locales local = loc.obtenerLocalId(id_local);
    	
    	ProductosPedido carrito = null;
    	gestionCarro gCarro = new gestionCarro();
    	
    	if (call_data.contains("tituloAnuncio_") )	{    		
    	  texto = prop.getProperty("var.Anuncios16");
    	  anun.actualizarFlagAnuncio("TITULO", usuario.getId_usuario(), id );    		    		    	
    	}
    	if (call_data.contains("desAnuncio_") ) {
    	  texto = prop.getProperty("var.Anuncios17");;
    	  anun.actualizarFlagAnuncio("DESCRIPCION", usuario.getId_usuario(), id );
    	}
    	if (call_data.contains("cateAnuncio_") ) {
    		texto = prop.getProperty("var.Anuncios18");    		
    		InlineKeyboardMarkup markupInline = botoneraCategorias(id, id_local,usuario);
            resAnuncio.setReplyMarkup(markupInline);    		    		
    	}    	
        if (call_data.contains("kate&")) {    		        	       
        	  anun.actualizarCateAnuncio(call_data.substring(call_data.indexOf("&")+1, call_data.indexOf("Anuncio") ) , id );
        	  InlineKeyboardMarkup markupInline;
        	  if (usuario.getTipo().equals("N")) {
        		  res = anun.leerAnuncio(id,usuario.getTipo(),usuario.getId_usuario(), null,id_local);
        		  texto = cuerpoAnuncio(res,usuario);                	  
        		  texto = texto.concat(" \n");
        		  texto = texto.concat(okEmoji + prop.getProperty("var.Anuncios19")); 
        		  texto = texto.concat(" \n");
        		  texto = texto.concat(prop.getProperty("var.Anuncios20"));
                  markupInline = botoneraAltaAnuncio(id,true,id_local,usuario);        
        	  } else {        	   
        		  res = anun.leerAnuncio(id,"B",usuario.getId_usuario(), null,id_local);
          	      texto = cuerpoAnuncio(res,usuario);                	  
        		  texto = texto.concat(" \n");
        		  texto = texto.concat(okEmoji + prop.getProperty("var.Anuncios21") + " \n");                      
                  //markupInline = botoneraSigAnt(res, res.getUser_id(), user_id, tipo, null);
                  markupInline = botoneraAltaAnuncio(res.getId_anuncio(),false,id_local,usuario);
        	  }              	
             
			resAnuncio.setText(texto);
			resAnuncio.setReplyMarkup(markupInline);
    	}
    	
        if (call_data.contains("unidadesAnuncio_") ) {
  		  texto = prop.getProperty("var.Anuncios22");
  		  anun.actualizarFlagAnuncio("UNIDADES",usuario.getId_usuario(), id );
  	    }
    	if (call_data.contains("fotoAnuncio_") ) {
    		texto = prop.getProperty("var.Anuncios23");
    		anun.actualizarFlagAnuncio("FOTO", usuario.getId_usuario(), id );
    	}
    	if (call_data.contains("precioAnuncio_") ) {
    		texto = prop.getProperty("var.Anuncios24")+prop.getProperty("var.limMin")+prop.getProperty("var.Anuncios25")+prop.getProperty("var.limMax")+"): ";
    		anun.actualizarFlagAnuncio("PRECIO", usuario.getId_usuario(), id );
    	}
    	if (call_data.contains("pubAnuncio_") ) {   
    		int numAnuncios = anun.validarMaxAnuncios(id_local);
            if (numAnuncios>=20 && local.getPlan().equals("B")) {
          	  texto = prop.getProperty("var.Anuncios26")+" \n"; 
          	  texto = texto.concat(" \n");
          	  texto = texto.concat(prop.getProperty("var.Anuncios27")+" \n");
            } else if (anun.obligatorioInformado(id)==0) {
    			res = anun.leerAnuncio(id,"A",usuario.getId_usuario(), null,id_local);
    			texto = cuerpoAnuncio(res,usuario);
	    		texto = texto.concat(" \n");
    			texto = texto.concat(prop.getProperty("var.Anuncios28")+" \n");
    			InlineKeyboardMarkup markupInline = botoneraAltaAnuncio(id,true,id_local,usuario);
                resAnuncio.setReplyMarkup(markupInline);    
    		} else {
	    		anun.finAnuncio(id);    	    	    
	    		res = anun.leerAnuncio(id,"A",usuario.getId_usuario(), null,id_local);
	    		texto = cuerpoAnuncio(res,usuario);
	    		texto = texto.concat(" \n");
	          	texto = texto.concat(star2Emoji + prop.getProperty("var.Anuncios29") + star2Emoji +" \n");
	          	texto = texto.concat(prop.getProperty("var.Anuncios30") + " \n");    	
	          //	Usuarios us = usu.obtenerUsuario((int)user_id);
    		}
    	}
    	if (call_data.contains("borrarAnuncio_") ) {	       
    		texto = "\n "+ prop.getProperty("var.Anuncios31") + id + " </b> \n"; 
  	        InlineKeyboardMarkup markupInline = botoneraSiNo(id, "B",local,usuario);	
  	        resAnuncio.setReplyMarkup(markupInline);
    	}    	
    	if (call_data.contains("anterAnuncio_") ) {
    		String parTexto = null;
    		if (usuario.getTipo().equals("T")) {
    			parTexto = 	call_data.substring(0,call_data.indexOf("#"));
    		}    		
    		res = anun.leerAnuncio(id,usuario.getTipo(),usuario.getId_usuario(), parTexto,id_local);    		
            if (res == null) {
            	texto = prop.getProperty("var.Anuncios32");                    	
            }else {
            	texto = cuerpoAnuncio(res,usuario);            	
            	InlineKeyboardMarkup markupInline = botoneraSigAnt(res,  usuario, user_id, usuario.getTipo(), parTexto,id_local);
            	//if (res.getPrecio().compareTo(BigDecimal.ZERO) == 0 || res.getTitulo() == null || res.getUnidades() <= 0) {
            	//	resAnuncio.setReplyMarkup(markupInline);
       		    //} else {
                  resAnuncio.setReplyMarkup(markupInline);
       		    //}
            }
    	}
    	if (call_data.contains("sigAnuncio_") ) {
    		String parTexto = null;
    		if (usuario.getTipo().equals("T")) {
    			parTexto = 	call_data.substring(0,call_data.indexOf("#"));
    		}
    		res = anun.leerAnuncio(id,usuario.getTipo(),usuario.getId_usuario(), parTexto,id_local);    		
            if (res == null) {
            	texto = prop.getProperty("var.Anuncios32");                    	
            }else {
            	texto = cuerpoAnuncio(res,usuario);            	            	
            	InlineKeyboardMarkup markupInline = botoneraSigAnt(res, usuario, user_id, usuario.getTipo(), parTexto,id_local);
            	//if (res.getPrecio().compareTo(BigDecimal.ZERO) == 0 || res.getTitulo() == null || res.getUnidades() <= 0) {
            //		resAnuncio.setReplyMarkup(markupInline);
       		  //  } else {
                   resAnuncio.setReplyMarkup(markupInline);
       		    //}
            }
    	}
    	if (call_data.contains("carroAnuncio_") ) {	       
    		// Validar que no hay un carro de otro local pendiente
    		Pedidos pedidoPen = pedido.pedidoPendiente(usuario.getId_usuario());
    		if (pedidoPen != null && pedidoPen.getId_local()!=id_local) {
    	        Locales localPendiente = loc.obtenerLocalId(pedidoPen.getId_local());
    			texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
	    		texto = texto.concat(" \n");
	    		texto = texto.concat(prop.getProperty("var.Anuncios33") +localPendiente.getNombre() + " " + prop.getProperty("var.Anuncios34") +"\n"); 
	    		texto = texto.concat(" \n");
	    		
    		} else {
	    		gCarro.addProductoCarro(chat_id, user_id,id, pedidoPen, id_local);
	    		res = anun.leerAnuncio(id,"B",usuario.getId_usuario(), null,id_local);
	    		texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
	    		texto = texto.concat(" \n");
	    		texto = texto.concat("<b>" + res.getTitulo() + prop.getProperty("var.Anuncios35") + "\n"); 
	    		texto = texto.concat(" \n");
	   		    texto = texto.concat(prop.getProperty("var.Anuncios36") + " \n"); 
	   	    	texto = texto.concat(" \n");
	   	    	InlineKeyboardMarkup markupInline = botoneraFiltros(usuario,local);
	   	    	resAnuncio.setReplyMarkup(markupInline);
    		}
   	    	//
    	}
    	
    	resAnuncio.setText(texto);	                                	                                	                                	                                       
        try {
          execute(resAnuncio);
          if (call_data.contains("anterAnuncio_") || call_data.contains("sigAnuncio_") ) {
        	  gestionFoto gf = new gestionFoto();
        	  String nFoto = res.getId_anuncio() + "_" + res.getVersion_foto();
              gf.eliminarFichero(nFoto);
          }
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
    }
    
    private InlineKeyboardMarkup botoneraCategorias(int pk, int id_local,Usuarios usuario) {
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();    
        Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        
        String[] categoriasOk = prop.getProperty("list.catValidas").split(",");
    	List<String> listaCatOk = Arrays.asList(categoriasOk); 
        int nBot = 0;
        int j = 0;
        while (j < listaCatOk.size()) {
          List<InlineKeyboardButton> rowInlineCat = new ArrayList<>();
          int x = 0;
          while (x<2 && nBot<listaCatOk.size()) {
        	     InlineKeyboardButton iKBKate = new InlineKeyboardButton();
        	     iKBKate.setText(listaCatOk.get(nBot)); 
        	     iKBKate.setCallbackData(id_local + "?kate&" + listaCatOk.get(nBot)+ "Anuncio_" + pk);
	             rowInlineCat.add(iKBKate);      
	             nBot++;
	             x++;
	    	  }
	      rowsInline.add(rowInlineCat);
    	  j=nBot;
        }
        
        
        markupInline.setKeyboard(rowsInline);        
    	return markupInline;	
    }
    
    public void  GestionFiltroCB(long chat_id, long user_id, String call_data, Usuarios usuario) {
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	String texto = null;    	
    	SendMessage resAnuncio = new SendMessage();
    	resAnuncio.setChatId(Long.toString(chat_id));
    	resAnuncio.setParseMode(ParseMode.HTML);
    	int id_local = Integer.parseInt(call_data.substring(0,call_data.indexOf("?")));
    	String textoLista = null;    
    	SendMessage mensajeListaAnuncios = new SendMessage();
    	mensajeListaAnuncios.setChatId(Long.toString(chat_id));
    	mensajeListaAnuncios.setParseMode(ParseMode.HTML);
    	List<Anuncios> lAnuncios = null;
    	
    	String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
    	String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
    	String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");  
    	
   	    gestionCarro gCarro = new gestionCarro();
   	    
    	SendInvoice invoice = new SendInvoice();
    	AnuncioDAO anun = new AnuncioDAO();
    	UsuariosDAO usu = new UsuariosDAO();    	
    	String nFoto = null;
    	int pk_anuncio = 0;
    	BigDecimal precio = new BigDecimal(0);
    	if (call_data.contains("propios") )	{
    	  usu.actuTipo("P", usuario.getId_usuario());
    	  Anuncios res = anun.leerAnuncio(0,"P",usuario.getId_usuario(), null, id_local);
          if (res == null) {
          	texto = prop.getProperty("var.Anuncios37");

          }else {
        	pk_anuncio = res.getId_anuncio();
        	nFoto = res.getId_anuncio() + "_" + res.getVersion_foto();
            texto = prop.getProperty("var.Anuncios38") + " \n";
            texto = texto.concat(" \n");
       	    texto = texto.concat(cuerpoAnuncio(res,usuario));        	
  		    InlineKeyboardMarkup markupInline = botoneraSigAnt(res, usuario, user_id, "P",null,id_local);	
  		    if (res.getPrecio().compareTo(BigDecimal.ZERO) == 0 || res.getTitulo() == null || res.getUnidades() <= 0) {
  		       resAnuncio.setReplyMarkup(markupInline);
 		     } else {
 		       precio = res.getPrecio();
               resAnuncio.setReplyMarkup(markupInline);
 		     }
          }           
    	} else if (call_data.contains("titulo")) {
    	  usu.actuTipo("T",usuario.getId_usuario());	
    	  texto = prop.getProperty("var.Anuncios39");      	 
    	} else if (call_data.contains("pedidos")) {
		   gPedido.GestionFiltrosPedidos(chat_id, user_id, usuario,id_local);			
    	} else if (call_data.contains("alta")) {
    	  int numAnuncios = anun.validarMaxAnuncios(id_local);
    	  LocalesDAO loc = new LocalesDAO();
	 	  Locales local = loc.obtenerLocalId(id_local);
          if (numAnuncios>=20 && local.getPlan().equals("B")) {
        	  texto = prop.getProperty("var.Anuncios40") + " \n"; 
        	  texto = texto.concat(" \n");
        	  texto = texto.concat(prop.getProperty("var.Anuncios41") +" \n");
           } else {
	    	   usu.actuTipo("N", usuario.getId_usuario());
	    	   texto = new String(prop.getProperty("var.Anuncios42") +" \n"); 	    
	    	   texto = texto.concat(" \n");
	    	   texto = texto.concat(prop.getProperty("var.Anuncios43") + " \n");
	    	   int pk = anun.altaAnuncio(usuario.getId_usuario(),id_local);      
	    	   InlineKeyboardMarkup markupInline = botoneraAltaAnuncio(pk,true,id_local,usuario);
	    	   resAnuncio.setReplyMarkup(markupInline);
           }
    	} else if (call_data.contains("carro")) {
    	  LocalesDAO loc = new LocalesDAO();
          Locales local = loc.obtenerLocalId(id_local);
    	  gCarro.MostrarCarro(chat_id, user_id, usuario,local);     	 
    	} else if (call_data.contains("categoria")) {
           usu.actuTipo(call_data.substring(call_data.indexOf("&")+1, call_data.indexOf("Filtro")), usuario.getId_usuario()); 
      	   String tipoCatB = call_data.substring(call_data.indexOf("&")+1, call_data.indexOf("Filtro"));        		
      	   Anuncios res = anun.leerAnuncio(0,tipoCatB,usuario.getId_usuario(), null,id_local);
	       if (res == null) {
             texto = prop.getProperty("var.Anuncios44") + tipoCatB + " </b>" ;                    	
           }else {
        	 pk_anuncio = res.getId_anuncio();
        	 nFoto = res.getId_anuncio() + "_" + res.getVersion_foto();
          	 texto = cuerpoAnuncio(res,usuario);            	 
         	 InlineKeyboardMarkup markupInline = botoneraSigAnt(res,  usuario, user_id, tipoCatB , null,id_local);
             if (res.getPrecio().compareTo(BigDecimal.ZERO) == 0 || res.getTitulo() == null || res.getUnidades() <= 0) {
   		       resAnuncio.setReplyMarkup(markupInline);
  		     } else {
  		       precio = res.getPrecio();
               resAnuncio.setReplyMarkup(markupInline);
  		     }

             //***
             lAnuncios = anun.listAnuncios(tipoCatB, null, id_local);
             LocalesDAO loc = new LocalesDAO();
             Locales local = loc.obtenerLocalId(id_local);
             NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
             df.setMaximumFractionDigits(2);
             textoLista = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
             textoLista = textoLista.concat("\n");
             textoLista = textoLista.concat("<b> ¡¡ " + tipoCatB + "!! </b> \n");
             textoLista = textoLista.concat("\n");
             int z = 0;
             for (int i = 0; i < lAnuncios.size(); i++) {
            	 textoLista = textoLista.concat(squareEmoji + "<b>" + lAnuncios.get(i).getTitulo() + "</b> - " + df.format(lAnuncios.get(i).getPrecio()) + " (/"+local.getNombre()+"_"+ lAnuncios.get(i).getId_anuncio() +")\n");
            	 z++;
	             if (z>25) {
	               mensajeListaAnuncios.setText(textoLista);	    
	               try {            
	                  execute(mensajeListaAnuncios);            
	               } catch (TelegramApiException e) {
	                             e.printStackTrace();
	               }
	               z=0;
	               textoLista="";
	              }
             }
         }
      	}
    		     
        try {
          if (textoLista!=null) {
        	mensajeListaAnuncios.setText(textoLista);
            execute(mensajeListaAnuncios);
          }
          if (texto!=null) {
        	  resAnuncio.setText(texto);
	          execute(resAnuncio);
	          if (pk_anuncio != 0) {
	            gestionFoto gf = new gestionFoto();    
	            gf.eliminarFichero(nFoto);
	          }
          }
          
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
    }
    

    
    public void  GestionBorradoCB(long message_id, long chat_id, long user_id, String call_data, int id,Usuarios usuario) {
    	String texto = null;    	
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	EditMessageText resAnuncio = new EditMessageText();
    	resAnuncio.setChatId(Long.toString(chat_id));
    	resAnuncio.setMessageId(toIntExact(message_id));
    	resAnuncio.setParseMode(ParseMode.HTML);    	
    	AnuncioDAO anun = new AnuncioDAO();    	
    	if (call_data.contains("si") )	{
            texto =  prop.getProperty("var.Anuncios48") + id + " " + prop.getProperty("var.Anuncios49");
        	anun.borrarAnuncio(id);    		
    	} else if (call_data.contains("no")) {	
      	  texto =  prop.getProperty("var.Anuncios47");      	  	 
      	}
    	resAnuncio.setText(texto);	                                	                                	                                	                                       
        try {
          execute(resAnuncio);          
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
    }
    
    public void GestionAnuncioAdm(long chat_id, long user_id, Usuarios usuario, List<String> listaAdmins, String message) {
    	//        	            	
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        SendMessage msg = new SendMessage();
        msg.setChatId(Long.toString(chat_id));
        msg.setParseMode(ParseMode.HTML);            
        AnuncioDAO anun = new AnuncioDAO();
        String textAnuncio = null;
        int id = Integer.parseInt(message.substring(message.indexOf("_")+1,message.length()));  	
        Anuncios res = anun.anuncioAdm(id);
        if (listaAdmins.contains(Long.toString(user_id))) {        
            if (res == null) {
	        	 textAnuncio =  prop.getProperty("var.Anuncios50") + id;
	        	 msg.setText(textAnuncio);
	        }else {
	     	  textAnuncio = cuerpoAnuncio(res,usuario);     	       	  
	          msg.setText(textAnuncio);                    
	        }
        } else {
        	textAnuncio = prop.getProperty("var.Ppal25") + id;
       	    msg.setText(textAnuncio);
        }
        try {            
           execute(msg);            
           gestionFoto gf = new gestionFoto();
           if (res != null) {
        	   String nFoto = res.getId_anuncio() + "_" + res.getVersion_foto();
        	   gf.eliminarFichero(nFoto);
           }
        } catch (TelegramApiException e) {
                    e.printStackTrace();
        }
    	
    }
    
    public void GestionFoto(long chat_id, long user_id, Update update, Usuarios usuario) {
    	SendMessage message = new SendMessage();
    	message.setChatId(Long.toString(chat_id));
    	message.setParseMode(ParseMode.HTML);                  
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	String okEmoji = EmojiParser.parseToUnicode(":ok_hand:");

    	SendInvoice invoice = new SendInvoice();            
    	BigDecimal precio = new BigDecimal(0);
    	String nFoto  = null;

        List<PhotoSize> photos = update.getMessage().getPhoto();
        // Know file_id
        String f_id = photos.stream()
                        .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                        .findFirst()
                        .orElse(null).getFileId();
        //              
        //SendPhoto msg = new SendPhoto().setChatId(Long.toString(chat_id)).setPhoto(f_id);            
                     
        // Validar si es información de un anuncio 
		AnuncioDAO anun = new AnuncioDAO();		
    	Anuncios aPend = anun.anuncioPendiente(usuario.getId_usuario());
        if ( aPend != null ) {           
          try { 
	          Anuncios anuncio = anun.actuAnuncio(f_id, aPend.getId_anuncio());
	          String texto = null; 
	          if (anuncio.getValidado().equals("N")) {               	                            	  
	            texto = okEmoji + prop.getProperty("var.Anuncios51");
	            texto = texto.concat(" \n");
	  		    texto = texto.concat(prop.getProperty("var.Ppal19"));
	  		    message.setText(texto);
	            InlineKeyboardMarkup markupInline = botoneraAltaAnuncio(aPend.getId_anuncio(),true,anuncio.getId_local(),usuario);
	            message.setReplyMarkup(markupInline);
	          } else {                	               	  
	        	  Anuncios anuncioActualizado = anun.leerAnuncio(aPend.getId_anuncio(),usuario.getTipo(), usuario.getId_usuario(), null,anuncio.getId_local());
	        	  nFoto = aPend.getId_anuncio() + "_" + anuncioActualizado.getVersion_foto();
	        	  texto = cuerpoAnuncio(anuncioActualizado,usuario);                	  
	      		  texto = texto.concat(" \n");
	              texto = texto.concat(okEmoji + prop.getProperty("var.Anuncios52") + " \n");      
	              //System.out.println("GestionFoto - texto-> " + texto);
	              //InlineKeyboardMarkup markupInline = botoneraSigAnt(anuncioActualizado, usuario, user_id, usuario.getTipo(), null,anuncioActualizado.getId_local());	
	              InlineKeyboardMarkup markupInline = botoneraAltaAnuncio(anuncioActualizado.getId_anuncio(),true,anuncioActualizado.getId_local(),usuario);
	              if (anuncioActualizado.getPrecio().compareTo(BigDecimal.ZERO) == 0 || anuncioActualizado.getTitulo() == null || anuncioActualizado.getUnidades() <= 0) {
	            	  message.setReplyMarkup(markupInline);
	     		   } else {
	     		      precio = anuncioActualizado.getPrecio();
	     		      message.setReplyMarkup(markupInline);
	     		   }
	              message.setText(texto);
	         }          
          } catch (SQLException e) {
			  message.setText(prop.getProperty("var.Ppal21"));	
	  			// TODO Auto-generated catch block
				e.printStackTrace();
		  }  catch (NumberFormatException e) {
			    message.setText(prop.getProperty("var.Ppal22"));	
	  			// TODO Auto-generated catch block
			    System.out.println("Error SQL: " + e);
		  } catch (ErrorRangoPrecio e) {
			    message.setText(prop.getProperty("var.Ppal23")+prop.getProperty("var.limMin")+" - Max: "+prop.getProperty("var.limMax")+") ");	
	  			// TODO Auto-generated catch block
			    System.out.println("Error rango: " + e);
		  } catch (ErrorRangoUnidades e) {
			    message.setText(prop.getProperty("var.Ppal24"));	
	  			// TODO Auto-generated catch block
			    System.out.println("Error rango: " + e);
		  }
        }
       
        try {
           // execute(msg); // Call method to send the photo with caption
            execute(message);
            if (nFoto!=null) {
                gestionFoto gf = new gestionFoto();
                gf.eliminarFichero(nFoto);
              }
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
		// TODO Auto-generated method stub
		//return null;
		//Properties prop = resourceLoader.loadProperties("configuracion.properties");
        return propGeneral.getProperty("var.BotUsername");
	}
	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		//return null;
		//Properties prop = resourceLoader.loadProperties("configuracion.properties");
        return propGeneral.getProperty("var.BotToken");
	}
}