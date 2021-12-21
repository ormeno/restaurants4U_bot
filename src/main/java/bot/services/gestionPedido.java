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
import java.util.concurrent.TimeUnit;

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

import bot.bo.Anuncios;
import bot.bo.LocalUsuarios;
import bot.bo.Locales;
import bot.bo.Pedidos;
import bot.bo.ProductosPedido;
import bot.bo.Usuarios;
import bot.dao.AnuncioDAO;
import bot.dao.LocalUsuariosDAO;
import bot.dao.LocalesDAO;
import bot.dao.PedidoDAO;
import bot.dao.ProductoPedidoDAO;
import bot.dao.UsuariosDAO;
import res.resourceLoader;

public class gestionPedido extends TelegramLongPollingBot {
	
	Properties propGeneral = resourceLoader.loadProperties("configuracion.properties");
	String[] usuariosAdm = propGeneral.getProperty("list.usersAdm").split(",");
	List<String> listaAdmins = Arrays.asList(usuariosAdm);
	
	private static final BigDecimal TWO = new BigDecimal("2");
	private static BigDecimal HUNDRED = new BigDecimal("100");
	private static BigDecimal PERCENTAGE = new BigDecimal("17.34");
    private static int DECIMALS = 2;
	private static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
	
	//NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(prop.getProperty("var.lenguaje"),prop.getProperty("var.pais")));
	
	private BigDecimal getIVA(BigDecimal precio){
	    BigDecimal result = precio.multiply(PERCENTAGE);
	    result = result.divide(HUNDRED, ROUNDING_MODE);
	    return rounded(result);
	  }
	
	private BigDecimal rounded(BigDecimal number){
	    return number.setScale(DECIMALS, ROUNDING_MODE);
	  }
	
	public void GestionFiltrosPedidos(long chat_id, long user_id, Usuarios usuario, int id_local) {
    	SendMessage msg = new SendMessage();
    	msg.setChatId(Long.toString(chat_id));
    	msg.setParseMode(ParseMode.HTML);		   
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
	    String manEmoji = EmojiParser.parseToUnicode(":man_cook:");             		               	            
   	    String texto = null;
        LocalUsuarios localUsuario = null;
        LocalesDAO loc = new LocalesDAO();
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        localUsuario = locUsu.obtenerLocalUsuarioId(id_local, usuario.getId_usuario());
   	    if (localUsuario.getTipo_usuario().equals("PR")) {   
   	      Locales local = loc.obtenerLocalId(id_local);
          texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
		  texto = texto.concat("\n");
 	      texto = texto.concat(prop.getProperty("var.Pedidos5") + " \n"); 	    
 	      InlineKeyboardMarkup markupInline = botoneraEstadoPedidos(0,id_local,usuario);
		  msg.setReplyMarkup(markupInline);
    	 } else {
        	texto = prop.getProperty("var.Ppal25");
        }
   	    msg.setText(texto);
	    try {
	        execute(msg);
	    } catch (TelegramApiException e) {
	        e.printStackTrace();
	    }
    }
	
	public void MostrarPedidos(long chat_id, long user_id, String call_data, Usuarios usuario, int id_local) {
		SendMessage message = new SendMessage();
		message.setChatId(Long.toString(chat_id));
		message.setParseMode(ParseMode.HTML);     	            	                                              
        PedidoDAO ped = new PedidoDAO();    	
        Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
        String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
        String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
	    String manEmoji = EmojiParser.parseToUnicode(":man_cook:");   
        String texto = null;
        String tipoEstado = call_data.substring(call_data.indexOf("&")+1, call_data.indexOf("Estado_"));
        LocalesDAO loc = new LocalesDAO();
        Locales local = loc.obtenerLocalId(id_local);
        texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
        texto = texto.concat("\n");
        List<Pedidos> pedidos =  ped.obtenerPedidos(tipoEstado, id_local);
        if (!pedidos.isEmpty()) {
	        	int j = 0;
	        	int z = 0;
	        	if (tipoEstado.equals("TODOS")) {
	          	  texto = texto.concat(ballEmoji + prop.getProperty("var.Pedidos6") + " \n");
	        	} else {
	        	  texto = texto.concat(ballEmoji + prop.getProperty("var.Pedidos7") + tipoEstado + " \n");
	        	} 
	        	texto = texto.concat(" \n");
	        	while (pedidos.size() > j) {
	        	  texto = texto.concat(squareEmoji + "<b>/"+id_local+"Pedido_" + pedidos.get(j).getId_pedido());
	        	  if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH")) {
	                texto = texto.concat(" - " + pedidos.get(j).getEstado() + " </b> - ");   
	        	  } else {
	        		String estadoIng = conversionEstados(pedidos.get(j).getEstado());
	        		texto = texto.concat(" - " + estadoIng + " </b> - ");   
	        	  }
	              if (pedidos.get(j).getPayload()!=null) {
	                texto = texto.concat(pedidos.get(j).getPayload() + " \n");
	              } else {
	            	UsuariosDAO usu = new UsuariosDAO();
	                Usuarios usuarioPed = usu.obtenerUsuarioId(pedidos.get(j).getId_usuario());
	                texto = texto.concat(prop.getProperty("var.Carro20") + usuarioPed.getNombre() + " - " + prop.getProperty("var.Carro21") + usuarioPed.getAliastg() + "\n");
	              }
	              
	              j++;
	              z++;
	              if (z>50) {
	            	 message.setText(texto);       
	                 try {            
	                     execute(message);            
	                 } catch (TelegramApiException e) {
	                               e.printStackTrace();
	                 }
	                 z=0;
	                 texto=" ";
	              }
	        	}
        } else {
        	if (tipoEstado.equals("TODOS")) {
        		texto = texto.concat(prop.getProperty("var.Pedidos8"));
        	} else {
        		texto = texto.concat(prop.getProperty("var.Pedidos9") + tipoEstado + " !! </b>");
        	}
	    }
	        
        message.setText(texto);       
        try {            
            execute(message);            
          } catch (TelegramApiException e) {
                     e.printStackTrace();
          }
    }

	public void ConsultaPedido(long chat_id, long user_id, Usuarios usuario, int idPedido, int id_local) {
		SendMessage message = new SendMessage();
		message.setChatId(Long.toString(chat_id));
		message.setParseMode(ParseMode.HTML);     	       
		Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        PedidoDAO ped = new PedidoDAO();    	
        String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
        String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
	    String manEmoji = EmojiParser.parseToUnicode(":man_cook:");
        String texto = null;
        gestionCarro gCarro = new gestionCarro();
        ProductoPedidoDAO carro = new ProductoPedidoDAO();
        List<ProductosPedido> productosPedido = null;
        LocalUsuarios localUsuario = null;
        LocalesDAO loc = new LocalesDAO();
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        localUsuario = locUsu.obtenerLocalUsuarioId(id_local, usuario.getId_usuario());
        Pedidos pedido = ped.obtenerPedido(idPedido);
        if (localUsuario==null) {
        	texto = prop.getProperty("var.Ppal25");
        } else if (localUsuario.getTipo_usuario().equals("PR")
   	      	      || (usuario.getId_usuario() == pedido.getId_usuario() && id_local == pedido.getId_local())) {  
   	        Locales local = loc.obtenerLocalId(id_local);
        	if (pedido == null) {
        		texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
                texto = texto.concat("\n");
        		texto = texto.concat(prop.getProperty("var.Pedidos10") + idPedido);
        	} else {
                AnuncioDAO anun = new AnuncioDAO();
                Anuncios anuncio = anun.leerAnuncio(pedido.getId_anuncio(),"B",usuario.getId_usuario(),null, id_local);
	            productosPedido =  carro.obtenerProductosPedido(idPedido);
	            if (localUsuario.getTipo_usuario().equals("PR")) {
	              texto = gCarro.textoAdmPedidoCarro(productosPedido,pedido, usuario, local);
	              InlineKeyboardMarkup markupInline = botoneraModifPedido(pedido.getId_pedido(),local.getId_local(),usuario);
	              message.setReplyMarkup(markupInline);
	            } else {
	              texto = gCarro.textoClientePedidoCarro(productosPedido,pedido, local);
	            }
	        }
	        
        } else {
        	texto = prop.getProperty("var.Ppal25");
        }
        message.setText(texto);       
        try {            
            execute(message);            
          } catch (TelegramApiException e) {
                     e.printStackTrace();
          }
    }
	public void ModificarEstadoPedido(long chat_id, long user_id, Usuarios usuario, int idPedido, int id_local) {
		SendMessage message = new SendMessage();
		message.setChatId(Long.toString(chat_id));
		message.setParseMode(ParseMode.HTML);     
		Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
        String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
	    String manEmoji = EmojiParser.parseToUnicode(":man_cook:");
        String texto = null;
        LocalUsuarios localUsuario = null;
        LocalesDAO loc = new LocalesDAO();
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        localUsuario = locUsu.obtenerLocalUsuarioId(id_local, usuario.getId_usuario());
   	    if (localUsuario.getTipo_usuario().equals("PR")) {    	
   	       Locales local = loc.obtenerLocalId(id_local);
           texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
		   texto = texto.concat("\n");
	       texto =  texto.concat(ballEmoji + prop.getProperty("var.Pedidos11") + idPedido);
	       InlineKeyboardMarkup markupInline = botoneraEstadoPedidos(idPedido, id_local,usuario);
	       message.setReplyMarkup(markupInline);
        } else {
        	texto = prop.getProperty("var.Ppal25");
        }
        message.setText(texto);       
        try {            
            execute(message);            
          } catch (TelegramApiException e) {
                     e.printStackTrace();
          }
    }
	
	public void BorrarPedido(long chat_id, long user_id, Usuarios usuario, int idPedido, Locales local) {
		SendMessage message = new SendMessage();
		message.setChatId(Long.toString(chat_id));
		message.setParseMode(ParseMode.HTML);     	
		Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
		GestionAnuncios gAnuncio = new GestionAnuncios();
        String texto = null;
        LocalUsuarios localUsuario = null;
        LocalesDAO loc = new LocalesDAO();
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        localUsuario = locUsu.obtenerLocalUsuarioId(local.getId_local(), usuario.getId_usuario());
   	    if (localUsuario.getTipo_usuario().equals("PR")) {
   	    	texto = "\n "+ prop.getProperty("var.Pedidos26") + idPedido + " </b> \n"; 
  	        InlineKeyboardMarkup markupInline = gAnuncio.botoneraSiNo(idPedido, "p",local,usuario);	
  	        message.setReplyMarkup(markupInline);
        } else {
        	texto = prop.getProperty("var.Ppal25");
        }
        message.setText(texto);       
        try {            
            execute(message);            
          } catch (TelegramApiException e) {
                     e.printStackTrace();
          }
	}
	
	public void  GestionBorradoPedidoCB(long message_id, long chat_id, long user_id, String call_data, int id,Usuarios usuario) {
    	String texto = null;    	
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	EditMessageText resAnuncio = new EditMessageText();
    	resAnuncio.setChatId(Long.toString(chat_id));
    	resAnuncio.setMessageId(toIntExact(message_id));
    	resAnuncio.setParseMode(ParseMode.HTML);    	
    	AnuncioDAO anun = new AnuncioDAO();    	
    	if (call_data.contains("si") )	{
            texto =  prop.getProperty("var.Pedidos27") + id + " " + prop.getProperty("var.Pedidos28");
        	PedidoDAO ped = new PedidoDAO();
        	int row = ped.borrarPedido(id);
    	} else if (call_data.contains("no")) {	
      	  texto =  prop.getProperty("var.Pedidos29");      	  	 
      	}
    	resAnuncio.setText(texto);	                                	                                	                                	                                       
        try {
          execute(resAnuncio);          
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
    }
	
	 private InlineKeyboardMarkup botoneraEstadoPedidos(int idPedido,int id_local,Usuarios usuario) {
	    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
	        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
	        Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
	        
	        if (idPedido==0) {
		        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();   
		        InlineKeyboardButton iKBEsT = new InlineKeyboardButton();
		        iKBEsT.setText(prop.getProperty("var.Pedidos22"));
		        iKBEsT.setCallbackData(id_local + "?estPedido&TODOSEstado_" + idPedido);
		        rowInline1.add(iKBEsT);
		        rowsInline.add(rowInline1);
	        }     
	        String[] pedidosOk = prop.getProperty("list.estadosPedido").split(",");
	    	List<String> listaPedOk = Arrays.asList(pedidosOk); 
	        int nBot = 0;
	        int j = 0;
	        while (j < listaPedOk.size()) {
	          List<InlineKeyboardButton> rowInlineCat = new ArrayList<>();
	          int x = 0;
	          while (x<2 && nBot<listaPedOk.size()) {
	        	    InlineKeyboardButton iKBEs = new InlineKeyboardButton();
	        	    iKBEs.setText(listaPedOk.get(nBot));
	        	    iKBEs.setCallbackData(id_local + "?estPedido&" + listaPedOk.get(nBot)+ "Estado_" + idPedido);
		             rowInlineCat.add(iKBEs);      
		             nBot++;
		             x++;
		    	  }
		      rowsInline.add(rowInlineCat);
	    	  j=nBot;
	        }
           
	        markupInline.setKeyboard(rowsInline);        
	    	return markupInline;	
	    }
	 
	 
	 private InlineKeyboardMarkup botoneraModifPedido(int idPedido,int id_local,Usuarios usuario) {
	    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
	    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
	        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
	        
		    List<InlineKeyboardButton> rowInline1 = new ArrayList<>();    
		    InlineKeyboardButton iKBModP = new InlineKeyboardButton();
		    iKBModP.setText(prop.getProperty("var.Pedidos14"));
		    iKBModP.setCallbackData(id_local + "?modificarEstPed_" + idPedido);
		    rowInline1.add(iKBModP);
		    InlineKeyboardButton iKBBorrP = new InlineKeyboardButton();
		    iKBBorrP.setText(prop.getProperty("var.Pedidos15"));
		    iKBBorrP.setCallbackData(id_local + "?BorrarPed_" + idPedido);
		    rowInline1.add(iKBBorrP);
		    rowsInline.add(rowInline1);
	        markupInline.setKeyboard(rowsInline);        
	    	return markupInline;	
	    }
	 
	 public void  GestionEstadoCB(long chat_id, long user_id, String call_data, Usuarios usuario, int id_pedido, int id_local) {
	    	
		    String texto = null;    	
		    Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
	    	SendMessage msg = new SendMessage();
	    	msg.setChatId(Long.toString(chat_id));
	    	msg.setParseMode(ParseMode.HTML);
	    	PedidoDAO ped = new PedidoDAO();
	    	ProductoPedidoDAO carro = new ProductoPedidoDAO();
	    	AnuncioDAO anun = new AnuncioDAO();
	    	
	    	String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
	 	    String manEmoji = EmojiParser.parseToUnicode(":man_cook:");
	 	    LocalesDAO loc = new LocalesDAO();
	 	    Locales local = loc.obtenerLocalId(id_local);
		    
			   Pedidos p =  ped.obtenerPedido(id_pedido);
		       if (p == null) {
		    	 texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
				 texto = texto.concat("\n");
	             texto = texto.concat(prop.getProperty("var.Pedidos16") + id_pedido + " </b>");                    	
	           } else if (call_data.contains("BorrarPed_") )	{    		
	        	 BorrarPedido(chat_id, user_id, usuario, id_pedido,local);	 
	           } else if (call_data.contains("modificarEstPed_") )	{    		
	        	 ModificarEstadoPedido(chat_id, user_id, usuario,id_pedido,id_local);
	 		   } else if (call_data.contains("Estado")) {
	 			 texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
	 			 texto = texto.concat("\n");
	 			 String tipoEstado = call_data.substring(call_data.indexOf("&")+1, call_data.indexOf("Estado_"));
	        	 ped.actualizarEstadoPedido(tipoEstado, id_pedido);
	        	 texto = texto.concat(prop.getProperty("var.Pedidos17") + id_pedido + " " + prop.getProperty("var.Pedidos18") + tipoEstado + " </b>");     	 
	        	 SendMessage msgCliente = new SendMessage();
	        	 msgCliente.setParseMode(ParseMode.HTML);
	        	 msgCliente.setChatId(Long.toString(chat_id));
        		 UsuariosDAO usu = new UsuariosDAO();
     	    	 Usuarios usuarioCliente = usu.obtenerUsuarioId(p.getId_usuario());
     	    	 Properties propCliente = resourceLoader.ObtenerProperties(usuarioCliente.getIdioma());
        		 long idChat = usuarioCliente.getChat_id();	
        		 String textoCliente = usuarioCliente.getNombre().toUpperCase() + propCliente.getProperty("var.Pedidos19") +id_local + "Pedido_"+id_pedido +propCliente.getProperty("var.Pedidos20") + tipoEstado + "</b>\n"; 
        		 if (tipoEstado.equals("ANULADO") || tipoEstado.equals("CANCELED")) {
        			 textoCliente = textoCliente.concat(" \n");
	        	     List<Usuarios> listaPropietarios = usu.obtenerUsuariosPropietarios(local.getId_local());
	        		 int j = 0;
	        		 while (j < listaPropietarios.size()) {
	        		   if (j==0) { 
	        			  textoCliente = textoCliente.concat(propCliente.getProperty("var.Pedidos25") + "@" + listaPropietarios.get(j).getAliastg());
	        			} else {
	       				  textoCliente = textoCliente.concat(" - @" + listaPropietarios.get(j).getAliastg());
	       				}  
	        			j++; 
	       			 }
	        		 textoCliente = textoCliente.concat(" \n");
	        	 }
        		 msgCliente.setChatId(Long.toString(idChat));	        
        		 msgCliente.setText(textoCliente);
 			     try {
 			        execute(msgCliente);     
 			      } catch (TelegramApiException e) {
 			        e.printStackTrace();
 			      }
	        	 
	           }
	      	
	    		                                	                                	                                	                                       
	        try {
	          if (texto!=null) {
	        	  msg.setText(texto);
	            execute(msg);        
	          }
	        } catch (TelegramApiException e) {
	          e.printStackTrace();
	        }
	    }
		
		 protected InlineKeyboardMarkup botoneraMetalico(int id_anuncio, int id_pedido) {
		    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		                
		        String[] campos = propGeneral.getProperty("list.camposEnvio").split(",");
		    	List<String> listaCampos = Arrays.asList(campos); 
		        int nBot = 0;
		        int j = 0;
		        while (j < listaCampos.size()) {
		          List<InlineKeyboardButton> rowInlineCat = new ArrayList<>();
		          int x = 0;
		          while (x<2 && nBot<listaCampos.size()) {
		        	  InlineKeyboardButton iKBEnv = new InlineKeyboardButton();
		        	  iKBEnv.setText(listaCampos.get(nBot));
		        	  iKBEnv.setCallbackData("campoEnvio" + listaCampos.get(nBot)+ "CampoEnvio_" + id_anuncio + "_&_" + id_pedido);
			             rowInlineCat.add(iKBEnv);      
			             nBot++;
			             x++;
			    	  }
			      rowsInline.add(rowInlineCat);
		    	  j=nBot;
		        }
	           
		        markupInline.setKeyboard(rowsInline);        
		    	return markupInline;	
		    }
		 
		 protected String gestionInsercionCamposEnvioMetalico(String message_text, Pedidos pedido, int id_pedido) {
				
				String texto = null; 
				String okEmoji = EmojiParser.parseToUnicode(":ok_hand:");
				if (pedido.getEstado().equals("GESTION")) {
					String campo = "";
					if (pedido.getFlag().equals("direccion1")) {campo="DIRECCION";};
					if (pedido.getFlag().equals("cod_postal")) {campo="CODIGO POSTAL";};
					if (pedido.getFlag().equals("ciudad")) {campo="CIUDAD";};
					if (pedido.getFlag().equals("nombre_ped")) {campo="DESTINATARIO";};
					if (pedido.getFlag().equals("telefono_ped")) {campo="TELEFONO";};
					texto = okEmoji + " ¡¡¡EL CAMPO " + campo + " HA SIDO ACTUALIZADO!!!!  ";
				    texto = texto.concat(" \n");
					texto = texto.concat(" Pulse GUARDAR cuando haya finalizdo ");
					
				}
				return texto;
			}
		 
		 public String conversionEstados(String estado) {
		    	String estadoIng = null;
		    	if (estado.equals("PENDIENTE")) estadoIng="PENDING";
			    if (estado.equals("EN COCINA")) estadoIng="IN KITCHEN";
			    if (estado.equals("PREPARADO")) estadoIng="PREPARED";
			    if (estado.equals("EN CAMINO")) estadoIng="ON THE WAY";
			    if (estado.equals("ENTREGADO")) estadoIng="DELIVERED";
			    if (estado.equals("ANULADO")) estadoIng="CANCELED";
			    return estadoIng;
		    } 
		 
		 public void  GestionEnvioMetalicoCB(long message_id, long chat_id, long user_id, String call_data, int id, int id_local) {
			    String texto = null;
		    	PedidoDAO ped = new PedidoDAO();
		    	AnuncioDAO anun = new AnuncioDAO(); 
		    	UsuariosDAO usu = new UsuariosDAO();
		    	Pedidos pedido = ped.obtenerPedido(id);
		    	Usuarios usuario = usu.obtenerUsuarioId(pedido.getId_usuario());
		    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
		    	ProductoPedidoDAO carro = new ProductoPedidoDAO();
		    	EditMessageText resAnuncio = new EditMessageText();
		    	resAnuncio.setChatId(Long.toString(chat_id));
		    	resAnuncio.setMessageId(toIntExact(message_id));
		    	resAnuncio.setParseMode(ParseMode.HTML);  
		    	Anuncios anuncioAgotado = null;
		    	gestionCarro gCarro = new gestionCarro();
		    	List<ProductosPedido> productosPedido = null;
		    	
		    	if (call_data.contains("si") )	{
		    		if (!pedido.getEstado().equals("GESTION")) {
		    			texto = prop.getProperty("var.Pedidos1");
		    			texto = texto.concat(" \n");
		          	    texto = texto.concat(prop.getProperty("var.Pedidos2") + " \n");
		    		} else {
				    	productosPedido =  carro.obtenerProductosPedido(id);
				    	anuncioAgotado = gCarro.obtenerProductoAgotado(productosPedido,id_local); 
		    			if (anuncioAgotado!=null) {
		    			  texto = prop.getProperty("var.Anuncios7") +":" + anuncioAgotado.getTitulo() + "\n";
		    			  texto = texto.concat(prop.getProperty("var.Pedidos3") + anuncioAgotado.getUnidades() + "\n");
		    			  texto = texto.concat(" \n");
		    		    } else {
		    			  int numRow = ped.actuEnvioDirecto(id);
			              Anuncios anuncio = anun.leerAnuncio(pedido.getId_anuncio(),"B",0,null,id_local); 	    
			              if (prop.getProperty("var.stock").equals("SI")) {
			              	  // actualizo el stock de todos los productos que componen el pedido
			              	  int j = 0;	
			            	  while (productosPedido.size() > j) {
			            	    anun.actualizarUnidadesAnuncio(productosPedido.get(j).getId_anuncio(),productosPedido.get(j).getCantidad());
			            	    j++;
			                  }
			              }
			             Pedidos pedidoActu = ped.obtenerPedido(id);
			             String textoAdm = null;
			             LocalesDAO loc = new LocalesDAO();
			             Locales local = loc.obtenerLocalId(id_local);
			             // Texto de pedido enviado por carro de la compra
			             texto = gCarro.textoClientePedidoCarro(productosPedido,pedidoActu,local);
			             textoAdm = gCarro.textoAdmPedidoCarro(productosPedido,pedidoActu, usuario,local);
			             LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
			             List<String> listaPropietarios = locUsu.obtenerIdUserPropietarios(local.getId_local());
			             gestionNotificacionPropietarios(textoAdm,listaPropietarios,pedidoActu,local,usuario);
		    		  }
		    		}
		    	} else if (call_data.contains("no")) {	
		    		carro.borrarCarro(id);		   
			    	ped.borrarPedido(id);
		         	texto = prop.getProperty("var.Pedidos4");     	  	 
	         	}
	    	    resAnuncio.setText(texto);	                                	                                	                                	                                       
		        try {
		          execute(resAnuncio);          
		        } catch (TelegramApiException e) {
		          e.printStackTrace();
		        }
		    }
		
		private void gestionNotificacionPropietarios(String textoAdm, List<String> listaPropietarios, Pedidos pedido, Locales local,Usuarios usuario) {
			int j = 0;
			long chat_idAdm = 0;
		    while (j < listaPropietarios.size()) {
		    	chat_idAdm = Long.parseLong(listaPropietarios.get(j));
				SendMessage msgAdm = new SendMessage();
				msgAdm.setChatId(Long.toString(chat_idAdm));
				msgAdm.setParseMode(ParseMode.HTML);
				msgAdm.setText(textoAdm);
				InlineKeyboardMarkup markupInline = botoneraModifPedido(pedido.getId_pedido(),local.getId_local(),usuario);
				msgAdm.setReplyMarkup(markupInline);
				try {
				      execute(msgAdm); 
				    } catch (TelegramApiException e) {
				      e.printStackTrace();
				    }
				j++;
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
        return propGeneral.getProperty("var.BotUsername");
	}
	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		//return null;
        return propGeneral.getProperty("var.BotToken");
	}

}
