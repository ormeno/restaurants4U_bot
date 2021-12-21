package bot;


import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerShippingQuery;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingOption;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.vdurmont.emoji.EmojiParser;

import bot.bo.Anuncios;
import bot.bo.CuponesAnuncio;
import bot.bo.LocalUsuarios;
import bot.bo.Locales;
import bot.bo.Pedidos;
import bot.bo.ProductosPedido;
import bot.bo.Usuarios;
import bot.dao.AnuncioDAO;
import bot.dao.CuponDAO;
import bot.dao.ErrorRangoPrecio;
import bot.dao.ErrorRangoUnidades;
import bot.dao.LocalUsuariosDAO;
import bot.dao.LocalesDAO;
import bot.dao.PedidoDAO;
import bot.dao.ProductoPedidoDAO;
import bot.dao.UsuariosDAO;
import bot.services.GestionAnuncios;
import bot.services.gestionCarro;
import bot.services.gestionCupon;
import bot.services.gestionFoto;
import bot.services.gestionLocales;
//import bot.services.gestionPago;
import bot.services.gestionPedido;
import bot.services.gestionPerfil;
import res.resourceLoader;

public class Principal extends TelegramLongPollingBot {
		
	
	protected Principal(DefaultBotOptions botOptions) {
        super(botOptions);
    }

	public Principal() {
		// TODO Auto-generated constructor stub
	}
	
	private GestionAnuncios gAnuncio = new GestionAnuncios();
	//private gestionPago gPago = new gestionPago();
	private gestionPerfil gPerfil = new gestionPerfil();
	private gestionLocales gLocal = new gestionLocales();
	private gestionPedido gPedido = new gestionPedido();
	private gestionCupon gCupon = new gestionCupon();
	private gestionCarro gCarro = new gestionCarro();
	
	private Integer CACHETIME = 300;
 
	private static Properties prop = null;
	private String[] usuariosAdm = null;
	private List<String> listaAdmins = null; 
	
	//NumberFormat df = null;
	    
    @SuppressWarnings("unused")
	@Override
    public void onUpdateReceived(Update update) {
    try {
    	    if (update.hasInlineQuery()) {
    	      long user_id = update.getInlineQuery().getFrom().getId();
    	      UsuariosDAO usu = new UsuariosDAO();
    	      Usuarios usuario = usu.obtenerUsuario((int)user_id);
              handleIncomingInlineQuery(update.getInlineQuery(),usuario);
              borrarImagenesInLine(update.getInlineQuery(),usuario);
            } else if (update.hasMessage() && update.getMessage().hasText()) {            
        	// Set variables        	
            String message_text = update.getMessage().getText();
            String user_username = update.getMessage().getChat().getUserName();            
            String first_username = update.getMessage().getChat().getFirstName();
            long user_id = update.getMessage().getChat().getId();
            long chat_id = update.getMessage().getChatId();              
			
            String numFoto = null;
            
            String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
	    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:"); 
            
            String[] categoriasOk = null;
            List<String> listaCatOk = null;
			                    
        	String msgPrincipal = null;
        	UsuariosDAO usu = new UsuariosDAO();       
        	LocalesDAO loc = new LocalesDAO();
        	Locales verLocal = null;
        	Usuarios usuario = GestionAcceso(user_id, chat_id, user_username, first_username);
        	Boolean existe = true;
        	if (usuario.getCont_mensajes()==1) {
        	    GestionNuevoUsuario(chat_id, first_username, usuario);      
        	    existe = false;
        	    if (message_text.startsWith("/start ")) {
        			verLocal = loc.obtenerLocalNombre(message_text.substring(message_text.indexOf("/start ") + 7,message_text.length()));
        		} 
        	    if (verLocal!=null)  existe = true;
        	} else {
        		if (usuario.getFechaBaja()!=null) {
        			GestionUsuarioDeBaja(chat_id, first_username);      
            	    existe = false;
        		} else {
	        		prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
	        		categoriasOk = prop.getProperty("list.catValidas").split(",");
	            	listaCatOk = Arrays.asList(categoriasOk);
	            	usuariosAdm = prop.getProperty("list.usersAdm").split(",");
	            	listaAdmins = Arrays.asList(usuariosAdm); 
	            	if (message_text.substring(0,1).equals("/")) {
	            		if (message_text.contains("_")) {
	            			verLocal = loc.obtenerLocalNombre(message_text.substring(1,message_text.indexOf("_")));
	            		} else if (message_text.startsWith("/start ")) {
	            			verLocal = loc.obtenerLocalNombre(message_text.substring(message_text.indexOf("/start ") + 7,message_text.length()));
	            		} else {
	            			verLocal = loc.obtenerLocalNombre(message_text.substring(1,message_text.length()));
	            		}
	            	} else {
	            		if (message_text.contains("_")) {
	            			verLocal = loc.obtenerLocalNombre(message_text.substring(0,message_text.indexOf("_")));
	            		} else {
	            			verLocal = loc.obtenerLocalNombre(message_text.substring(0,message_text.length()));
	            		} 
	            	}
        		}
        	}	
        	if (existe) {
	        	if (message_text.toUpperCase().equals("/START")) {
	            	GestionComandoStart(chat_id, first_username, usuario);	            	
	            } else if (message_text.toUpperCase().equals("PERFIL") || message_text.toUpperCase().equals("/PERFIL")
	            		   || message_text.toUpperCase().equals("PROFILE") || message_text.toUpperCase().equals("/PROFILE")) {
	            	gPerfil.MostrarPerfil(chat_id, user_id, usuario);            	            
	            }  else if (message_text.toUpperCase().equals("RESTAURANTES") || message_text.toUpperCase().equals("/RESTAURANTES")
	            		   || message_text.toUpperCase().equals("RESTAURANTS") || message_text.toUpperCase().equals("/RESTAURANTS")) {
	            	gLocal.MostrarLocales(chat_id, user_id, usuario);            	            		    
				} else if (verLocal!=null && message_text.contains("_")){
					gAnuncio.ConsultaAnunciosPorId(chat_id, user_id, usuario, message_text, verLocal);		
				} else if (verLocal!=null) {	
					GestionAccesoLocal(usuario.getId_usuario(),verLocal.getId_local());
					gAnuncio.GestionFiltros(chat_id, user_id, usuario, verLocal);
				} else if (listaCatOk.contains(message_text.substring(1, message_text.length()))){
					String data = "categoria&" + message_text.substring(1, message_text.length()) + "Filtro";
				    gAnuncio.GestionFiltroCB(chat_id, user_id, data, usuario);                													
				} else if (message_text.toUpperCase().equals("AYUDA") || message_text.toUpperCase().equals("/AYUDA") 
						  || message_text.toUpperCase().equals("HELP") || message_text.toUpperCase().equals("/HELP")) {
				    GestionComandoAyuda(chat_id, usuario, user_id, listaAdmins, first_username);
				} // Comandos administrador del bot
				  else if (message_text.contains("/AnuncioAdm")){
					  gAnuncio.GestionAnuncioAdm(chat_id, user_id, usuario, listaAdmins, message_text);				    
				} else if (message_text.contains("/AvisoAdm")){
				    GestionAvisoAdm(chat_id, user_id, listaAdmins, message_text.substring(10, message_text.length()));		
				} else if (message_text.contains("/BajaUsuario")){
				    GestionBajaUsu(chat_id, user_id, listaAdmins, message_text.substring(13, message_text.length()));		
				} else if (message_text.toUpperCase().contains("?INFO")){
					int id_local = Integer.parseInt(message_text.substring(0,message_text.indexOf("?")));
					GestionInfo(chat_id, user_id, message_text.substring(message_text.indexOf("INFO")+5, message_text.length()), usuario,id_local);	
				} else if (message_text.toUpperCase().contains("?PENDIENTES") || message_text.toUpperCase().contains("?PENDING")){
					int id_local = Integer.parseInt(message_text.substring(0,message_text.indexOf("?")));
					gAnuncio.ConsultaAnunciosBajaNoPub(chat_id, user_id, usuario,id_local);		
				} else if (message_text.toUpperCase().contains("?PEDIDOS") || message_text.toUpperCase().contains("?ORDERS")){
					int id_local = Integer.parseInt(message_text.substring(0,message_text.indexOf("?")));
					gPedido.GestionFiltrosPedidos(chat_id, user_id, usuario,id_local);			    	 
				} else if (message_text.toUpperCase().contains("PEDIDO_") || message_text.toUpperCase().contains("ORDER_")){
					int id_local = 0;
					if (message_text.substring(0,1).equals("/")) {
					  id_local = Integer.parseInt(message_text.substring(1,message_text.toUpperCase().indexOf("PEDIDO_")));
					}
					else {
					  id_local = Integer.parseInt(message_text.substring(0,message_text.toUpperCase().indexOf("PEDIDO_")));
				    }
					int idPedido = Integer.parseInt(message_text.substring(message_text.indexOf("Pedido_")+7,message_text.length()));
				    gPedido.ConsultaPedido(chat_id, user_id, usuario,idPedido,id_local);			    			    			    		    			    			    
				} else {               
					SendMessage message = new SendMessage();
					message.setChatId(Long.toString(chat_id));
					message.setParseMode(ParseMode.HTML);
					boolean enviarMensaje = true;
					// Validar si es información de un anuncio 
					AnuncioDAO anun = new AnuncioDAO();
	            	Anuncios aPend = anun.anuncioPendiente(usuario.getId_usuario());
	            	PedidoDAO ped = new PedidoDAO();
	            	Pedidos pedidoPen = ped.pedidoPendiente(usuario.getId_usuario());
	            	if ( aPend != null && pedidoPen != null) {
	            		String texto = null; 
	            		String warningEmoji = EmojiParser.parseToUnicode(":warning:");
	            		texto = prop.getProperty("var.Ppal2") + " \n";
	            		texto = texto.concat(" \n");
	            		texto = texto.concat(prop.getProperty("var.Ppal3") +" \n");
	            		texto = texto.concat(" \n");
	            		Locales localAnuncio = loc.obtenerLocalId(aPend.getId_local());
	            		texto = texto.concat(prop.getProperty("var.Ppal4") +" <b>/" + localAnuncio.getNombre() +  "</b> \n");
	            		texto = texto.concat(prop.getProperty("var.Ppal5") +" <b>/" + localAnuncio.getNombre() + "_"+ aPend.getId_anuncio() +  "</b> \n");
	            		texto = texto.concat(prop.getProperty("var.Ppal6") +" <b>" + aPend.getTitulo() +  "</b> \n");
	            		texto = texto.concat(" \n");
					    texto = texto.concat(prop.getProperty("var.Ppal7") +"<b>" + pedidoPen.getId_pedido() + "</b> \n");
					    Locales localPedido = loc.obtenerLocalId(pedidoPen.getId_local());
					    texto = texto.concat(prop.getProperty("var.Ppal8") +" <b>/" + localPedido.getNombre() +  "</b> \n");
					    texto = texto.concat(" \n");
					    texto = texto.concat(prop.getProperty("var.Ppal9") +" \n");
						texto = texto.concat(" \n");
						texto = texto.concat(prop.getProperty("var.Ppal10") +" \n");
						texto = texto.concat(prop.getProperty("var.Ppal11") +" \n");
						texto = texto.concat(prop.getProperty("var.Ppal12") +" \n");
						texto = texto.concat(" \n");
						message.setText(texto);
	            	//} else if (pedidoPen != null && (message_text.toUpperCase().startsWith("DATOS ENVIO") || message_text.toUpperCase().startsWith("DATOS ENVÍO"))) {
	            	} else if (pedidoPen != null) {
	            		if (pedidoPen.getFlag()!=null) {
							ProductoPedidoDAO carro = new ProductoPedidoDAO();
		            		List<ProductosPedido> productosPedido =  carro.obtenerProductosPedido(pedidoPen.getId_pedido());
		                    Locales local = loc.obtenerLocalId(pedidoPen.getId_local());
		                    NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
		                	String textoDet = gCarro.textoDetallePedido(productosPedido,local,usuario);
		                	String texto = womanEmoji + "<b>" + prop.getProperty("var.Ppal13")+ local.getNombre() + "</b> " +manEmoji + "\n";
		                	texto = texto.concat(" \n");
		        		    texto = texto.concat(prop.getProperty("var.Ppal14") + pedidoPen.getId_pedido() + "\n"); 
		                	texto = texto.concat("\n");
		            	    texto = texto.concat(local.getDatosConfirPago() + " \n"); 
		                	texto = texto.concat("  \n");
		         			texto = texto.concat(textoDet);
		         			BigDecimal precioTotal = gCarro.obtenerPrecioCarro(productosPedido);
		         			texto = texto.concat(" TOTAL: " + df.format(precioTotal) + " \n");
		            		// Datos de envio en campo libre
		            		ped.actuDireccionEnvioDirecto(message_text, pedidoPen.getId_pedido());
		            		texto = texto.concat(" \n");
		        		    texto = texto.concat(prop.getProperty("var.Ppal15") + " \n"); 
		            		texto = texto.concat(message_text + " \n");
		            		if (local.getDatosSupleEnvio()!=null) {
		     				   texto = texto.concat(" \n");
		     				   texto = texto.concat(local.getDatosSupleEnvio() + " \n");
		     			    }
		            		texto = texto.concat(" \n");
		            		texto = texto.concat(prop.getProperty("var.Ppal16")); 
		            		message.setText(texto);
		            	    InlineKeyboardMarkup markupInline = gAnuncio.botoneraSiNo(pedidoPen.getId_pedido(),"P",local,usuario);
		            	    message.setReplyMarkup(markupInline);
	            		} else {
	            			 int numRow =  ped.actuFlagPedido(null, pedidoPen.getId_pedido());
	            			 enviarMensaje = false;		
	            		}
	            	} else if ( aPend != null ) {      
	                  //Un usuario cliente nunca pasará por aquí pq no puede dar de alta anuncios
					  try {
						Anuncios anuncio = anun.actuAnuncio(message_text, aPend.getId_anuncio());

						  String okEmoji = EmojiParser.parseToUnicode(":ok_hand:");
						  String texto = null; 
						  if (anuncio.getValidado().equals("N")) { 
							Anuncios anuncioActualizado = anun.leerAnuncio(aPend.getId_anuncio(),usuario.getTipo(), usuario.getId_usuario(), null, anuncio.getId_local());
				            texto = gAnuncio.cuerpoAnuncio(anuncioActualizado, usuario);                	  
				            texto = texto.concat(" \n");
				            texto = texto.concat(okEmoji + prop.getProperty("var.Ppal17")+"<b>" + anuncio.getFlag_campo() + "</b> "+prop.getProperty("var.Ppal18"));
						    texto = texto.concat(" \n");
							texto = texto.concat(prop.getProperty("var.Ppal19"));
							message.setText(texto);
						    InlineKeyboardMarkup markupInline = gAnuncio.botoneraAltaAnuncio(aPend.getId_anuncio(),true,anuncio.getId_local(),usuario);
						    message.setReplyMarkup(markupInline);
						  } else {                	    
							  numFoto = aPend.getId_anuncio() + "_" + anuncio.getVersion_foto();
							  Anuncios anuncioActualizado = anun.leerAnuncio(aPend.getId_anuncio(),"B", usuario.getId_usuario(), null,anuncio.getId_local());
							  texto = gAnuncio.cuerpoAnuncio(anuncioActualizado, usuario);                	  
							  texto = texto.concat(" \n");
						      texto = texto.concat(okEmoji +  prop.getProperty("var.Ppal17")+"<b>" + anuncio.getFlag_campo() + "</b> "+prop.getProperty("var.Ppal20")+"\n");    
						      //InlineKeyboardMarkup markupInline = gAnuncio.botoneraSigAnt(anuncioActualizado, anuncioActualizado.getUser_id(), user_id, usuario.getTipo(), null);	
						      InlineKeyboardMarkup markupInline = gAnuncio.botoneraAltaAnuncio(aPend.getId_anuncio(),false,anuncio.getId_local(),usuario);
						      message.setReplyMarkup(markupInline);
						      message.setText(texto);
						 }
					  } catch (SQLException e) {
						    message.setText(prop.getProperty("var.Ppal21"));	
				  			// TODO Auto-generated catch block
						    System.out.println("Error SQL: " + e);
					  } catch (NumberFormatException e) {
						    message.setText(prop.getProperty("var.Ppal22"));	
				  			// TODO Auto-generated catch block
						    System.out.println("Error SQL: " + e);
					  }  catch (ErrorRangoPrecio e) {
						    message.setText(prop.getProperty("var.Ppal23")+prop.getProperty("var.limMin")+" - Max: "+prop.getProperty("var.limMax")+") ");	
				  			// TODO Auto-generated catch block
						    System.out.println("Error rango: " + e);
					  } catch (ErrorRangoUnidades e) {
						    message.setText(prop.getProperty("var.Ppal24"));	
				  			// TODO Auto-generated catch block
						    System.out.println("Error rango: " + e);
					  }
	                } else {
	                	enviarMensaje = false;
	                }
	                             		          
		             try {
		            	  if (enviarMensaje) {
		                   execute(message); 	                   
		                   if (numFoto!=null) {
		                     gestionFoto gf = new gestionFoto();
		                     gf.eliminarFichero(numFoto);
		                   }
		            	  }
		             } catch (TelegramApiException e) {
		                   e.printStackTrace();
		             }
	              }
              }
           }  else if (update.hasCallbackQuery()) {
        	   String call_data = update.getCallbackQuery().getData();
        	   long message_id = 0;
        	   long chat_id = 0;
        	   if (update.getCallbackQuery().getMessage() != null) {
                 message_id = update.getCallbackQuery().getMessage().getMessageId();
                 chat_id = update.getCallbackQuery().getMessage().getChatId();
        	   } else {
        		 chat_id = update.getCallbackQuery().getFrom().getId();
        	   }
               long user_id = update.getCallbackQuery().getFrom().getId();    
               String user_username = update.getCallbackQuery().getFrom().getUserName();
               UsuariosDAO usu = new UsuariosDAO();
           	   Usuarios usuario = usu.obtenerUsuario((int)user_id); 
           	   
           	   if (call_data.contains("Anuncio_")) {
           		 int id_local = Integer.parseInt(call_data.substring(0,call_data.indexOf("?")));
           		 int id = Integer.parseInt(call_data.substring(call_data.indexOf("Anuncio_")+8,call_data.length()));	            		             	
           		 gAnuncio.GestionAnuncioCB(message_id, chat_id, user_id, call_data, id, usuario, id_local);               	            	                         
               } else if (call_data.contains("Filtro")) {
            	 gAnuncio.GestionFiltroCB(chat_id, user_id, call_data, usuario);  
               } else if (call_data.contains("Perfil")) {
            	 gPerfil.GestionPerfilCB(chat_id, user_id, call_data, usuario);  
               } else if (call_data.contains("Confirma")) {
            	 int id = Integer.parseInt(call_data.substring(call_data.indexOf("Confirma_")+9,call_data.length()));
            	 String tipoCon = call_data.substring(call_data.indexOf("?")+3,call_data.indexOf("?")+4);
            	 if (tipoCon.equals("B")) {
            	   gAnuncio.GestionBorradoCB(message_id, chat_id, user_id, call_data,id,usuario);  
            	 } else if (tipoCon.equals("P")) {
            		 int id_local = Integer.parseInt(call_data.substring(0,call_data.indexOf("?")));
            		gPedido.GestionEnvioMetalicoCB(message_id, chat_id, user_id, call_data,id,id_local);  
            	 } else if (tipoCon.equals("p")) {
            		int id_local = Integer.parseInt(call_data.substring(0,call_data.indexOf("?")));
            		gPedido.GestionBorradoPedidoCB(message_id, chat_id, user_id, call_data,id,usuario); 
            	 }
               } else if (call_data.contains("Carro_")) {
            	 gCarro.GestionCarroCB(chat_id, user_id, call_data, usuario);  
               } else if (call_data.contains("Ped_")) {
            	 int id_local = Integer.parseInt(call_data.substring(0,call_data.indexOf("?")));
             	 int id_pedido = Integer.parseInt(call_data.substring(call_data.indexOf("Ped_")+4,call_data.length()));
             	 gPedido.GestionEstadoCB(chat_id, user_id, call_data, usuario, id_pedido, id_local); 
               } else if (call_data.contains("Estado")) {
            	  int id_local = Integer.parseInt(call_data.substring(0,call_data.indexOf("?")));
            	  int id = Integer.parseInt(call_data.substring(call_data.indexOf("Estado_")+7,call_data.length()));	
            	  if (id==0) {
            		  gPedido.MostrarPedidos(chat_id, user_id, call_data,usuario,id_local);
            	  } else {
            		  gPedido.GestionEstadoCB(chat_id, user_id, call_data, usuario, id, id_local);
            	  }
               }
           	   // viene cuando en el invoice ponemos .setFlexible(true)
//           } else if (update.getShippingQuery() != null) {
//        	   gPago.gestionShipping(update);
//           } else if (update.getPreCheckoutQuery() != null) {
//              gPago.gestionPreCheckout(update);
//           }  else if (update.getMessage().hasSuccessfulPayment()) {
//        	   gPago.gestionPagoOk(update, listaAdmins);
           }  else if (update.hasMessage() && update.getMessage().hasPhoto()) {
        	   long chat_id = update.getMessage().getChatId();
               long user_id = update.getMessage().getChat().getId();
               UsuariosDAO usu = new UsuariosDAO();
           	   Usuarios usuario = usu.obtenerUsuario((int)user_id);
               gAnuncio.GestionFoto(chat_id, user_id, update,usuario);
           }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
    }

	
    
    private Usuarios GestionAcceso(long user_id, long chat_id, String user_username, String first_username) {
    	// comprobar si usuario existe, si no existe lo registro en la tabla de usuarios. Si existe aumento su contador de accesos    	
    	UsuariosDAO usu = new UsuariosDAO();
    	Usuarios usuario = usu.obtenerUsuario((int)user_id);
        if ( usuario == null) {            	
        	usuario = usu.altaUsuario(user_id, chat_id, user_username, first_username);			
        } else {
            usu.actuContador(user_id, usuario.getId_usuario(), user_username, first_username);     
            if (usuario.getCont_mensajes()==1) {
            	usuario = usu.obtenerUsuario((int)user_id);
            }
        }       
        return usuario;
    }
    
    private void GestionAccesoLocal(int id_usuario, int id_local) {
    	// comprobar si usuario existe en el local, si no existe lo registro en la tabla de localUsuarios. Si existe aumento su contador de accesos    	
    	LocalUsuariosDAO localUsu = new LocalUsuariosDAO();
    	LocalUsuarios locUsuario = localUsu.obtenerLocalUsuarioId(id_local, id_usuario);
        if (locUsuario == null) {            	
        	int pk = localUsu.altaLocalUsuario(id_local, id_usuario);			
        } else {
        	localUsu.actuContador(id_local, id_usuario);     
        }       
    }
    
    private void GestionNuevoUsuario(long chat_id, String name, Usuarios usuario) {
    	String msgPrincipal = null;
    	        
        msgPrincipal = "Bienvenido - Welcome <b>" + name + "</b>.\n";
    	msgPrincipal = msgPrincipal.concat(" \n");
    	msgPrincipal = msgPrincipal.concat("@Restaurants4UBot es el espacio donde todos los restaurantes tienen cabida. \n");
    	msgPrincipal = msgPrincipal.concat("Consulta la carta de tu restaurante favorito pulsando /Restaurantes y luego en su nombre y haz tu pedido. Rápido y sencillo, sin salir de Telegram. \n");
    	msgPrincipal = msgPrincipal.concat("Puedes cambiar el idioma del Bot y tu ubicación (País-Ciudad) en /PERFIL. Si el País y la Ciudad no se informan verá todos los restaurantes sin filtrar. \n");
    	msgPrincipal = msgPrincipal.concat(" \n"); 
    	msgPrincipal = msgPrincipal.concat("@Restaurants4UBot is the space where all restaurants have a place. \n"); 
    	msgPrincipal = msgPrincipal.concat("Check the menu of your favorite restaurant by clicking /Restaurants and after on its name and place your order. Quick and easy, without leaving Telegram. \n"); 
    	msgPrincipal = msgPrincipal.concat("You can change the language of the Bot and your location (Country-City) in /PROFILE. If the Country and City are not reported, you will see all the restaurants without filtering \n");    	
    	msgPrincipal = msgPrincipal.concat(" \n");    	
     	     	     	
    	ReplyKeyboardMarkup keyboardMarkup = botoneraPrincipal(usuario);    	    	    	     
        SendMessage message = new SendMessage();
        message.setChatId(Long.toString(chat_id));
        message.setParseMode(ParseMode.HTML);
        message.setText(msgPrincipal);
        message.setReplyMarkup(keyboardMarkup);     
        try {
            execute(message); 
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    
    private void GestionUsuarioDeBaja(long chat_id, String name) {
    	String msgPrincipal = null;
    	        
        msgPrincipal = "Estimado <b>" + name + "</b>, se encuentra dado de baja, por favor, contacte con el administrador. \n";
    	msgPrincipal = msgPrincipal.concat(" \n");
    	msgPrincipal = msgPrincipal.concat("Dear <b>" + name + "</b>, you have been discharged, please contact the administrator. \n");   	
    	msgPrincipal = msgPrincipal.concat(" \n");    	
     	     	     		    	     
        SendMessage message = new SendMessage();
        message.setChatId(Long.toString(chat_id));
        message.setParseMode(ParseMode.HTML);
        message.setText(msgPrincipal);     
        try {
            execute(message); 
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void GestionComandoStart(long chat_id, String name, Usuarios usuario) {
    	String msgPrincipal = null;
    	String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
        String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
        
        String texto0 = prop.getProperty("var.textoStart0");
        String texto1 = prop.getProperty("var.textoStart1");
        String texto2 = prop.getProperty("var.textoStart2");
        String texto3 = prop.getProperty("var.textoStart3");
        String texto4 = prop.getProperty("var.textoStart4");
        String texto5 = prop.getProperty("var.textoStart5");
    	
        msgPrincipal = texto0 + " <b>" + name + "</b>.\n";
    	msgPrincipal = msgPrincipal.concat(" \n");
    	msgPrincipal = msgPrincipal.concat(texto1 + "\n");
    	msgPrincipal = msgPrincipal.concat(" \n"); 
    	msgPrincipal = msgPrincipal.concat(texto2 + "\n");    	
    	msgPrincipal = msgPrincipal.concat(" \n");    	
    	msgPrincipal = msgPrincipal.concat(texto3 + " \n");
    	msgPrincipal = msgPrincipal.concat(" \n");
    	msgPrincipal = msgPrincipal.concat(texto4 + " \n");
    	msgPrincipal = msgPrincipal.concat(" \n");
    	msgPrincipal = msgPrincipal.concat(texto5 + " \n");
     	msgPrincipal = msgPrincipal.concat(" \n"); 
     	msgPrincipal = msgPrincipal.concat(squareEmoji + " Admin: @rormeno75 \n");
     	msgPrincipal = msgPrincipal.concat(" \n"); 
     	
    	ReplyKeyboardMarkup keyboardMarkup = botoneraPrincipal(usuario);    	    	    	     
        SendMessage message = new SendMessage();
        message.setChatId(Long.toString(chat_id));
        message.setParseMode(ParseMode.HTML);
        message.setText(msgPrincipal);
        message.setReplyMarkup(keyboardMarkup);     
        try {
            execute(message); 
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    
    
  
    private void GestionAvisoAdm(long chat_id, long user_id, List<String> listaAdmins, String message) {
    	
    	SendMessage msg = new SendMessage();
    	msg.setParseMode(ParseMode.HTML);
    	msg.setChatId(Long.toString(chat_id));
    	UsuariosDAO usu = new UsuariosDAO();    	
    	 
		if (listaAdmins.contains(Long.toString(user_id))) {
			// obtener todos los usarios
			List<Usuarios> usuarios = usu.obtenerUsuarios();									                
            msg.setText(message);
            int j = 0;
            long idChat = 0;
            while (usuarios.size() > j) {	                
            	idChat = usuarios.get(j).getChat_id();	                	
            	msg.setChatId(Long.toString(idChat));	                	
			    try {
			        execute(msg); 
			        // cada 30 llamadas hago una pausa de 1 segundo
			        if (j%30==0) {
			        	System.out.println("Pausa!!!!! ");          
			        	TimeUnit.SECONDS.sleep(1);	
			        }					        
			      } catch (TelegramApiException | InterruptedException e) {
			        e.printStackTrace();
			      }					    			    
            	j++;
            }
		} else {
			  msg.setText(prop.getProperty("var.Ppal25"));
			  try {
					execute(msg); // Call method to send the photo
				} catch (TelegramApiException e) {
				   e.printStackTrace();
				}
		}
				    	    	    
    }
    
   private void GestionBajaUsu(long chat_id, long user_id, List<String> listaAdmins, String alias) {
    	
    	SendMessage msg = new SendMessage();
    	msg.setParseMode(ParseMode.HTML);
    	msg.setChatId(Long.toString(chat_id));
    	Usuarios usuBaja = null;
    	UsuariosDAO usu = new UsuariosDAO();
    	LocalUsuariosDAO localUsu = new LocalUsuariosDAO();
    	 
		if (listaAdmins.contains(Long.toString(user_id))) {
			usuBaja = usu.obtenerUsuarioAlias(alias);
			if (usuBaja == null) {
			  msg.setText("El usuario " + alias + " no existe");
			} else {
			  int indB = usu.bajaUsuario(usuBaja.getId_usuario()); 
			  int ind  = localUsu.bajaUsuarioLocal(usuBaja.getId_usuario());
		      msg.setText("El usuario " + alias + " ha sido dado de baja");
			}
            try {
				execute(msg); 
			} catch (TelegramApiException e) {
				   e.printStackTrace();
			}
		} else {
			  msg.setText(prop.getProperty("var.Ppal25"));
			  try {
				  execute(msg); 
				} catch (TelegramApiException e) {
				   e.printStackTrace();
				}
		}    	    
    }
    
    private void GestionInfo(long chat_id, long user_id, String message, Usuarios usuario, int id_local) {
    	
    	SendMessage msg = new SendMessage();
    	msg.setParseMode(ParseMode.HTML);
    	msg.setChatId(Long.toString(chat_id));
    	String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");  
    	UsuariosDAO usu = new UsuariosDAO();    	
        LocalesDAO loc = new LocalesDAO();
        Usuarios usuDetalle = null;
        LocalUsuarios localUsuario = null;
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        localUsuario = locUsu.obtenerLocalUsuarioId(id_local, usuario.getId_usuario());
        if (localUsuario==null) {
        	 msg.setText(prop.getProperty("var.Ppal25"));
			  try {
					execute(msg); // Call method to send the photo
				} catch (TelegramApiException e) {
				   e.printStackTrace();
				}
        } else if (localUsuario.getTipo_usuario().equals("PR")) {
         // obtener todos los usarios
        	Locales local = loc.obtenerLocalId(id_local);
        	String texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
  	        texto = texto.concat("\n");
  	        texto = texto.concat(message + "\n");
			List<LocalUsuarios> usuarios = locUsu.obtenerUsuariosLocal(id_local);									                
            msg.setText(texto);
            int j = 0;
            long idChat = 0;
            while (usuarios.size() > j) {	   
            	usuDetalle = usu.obtenerUsuarioId(usuarios.get(j).getId_usuario());
            	idChat = usuDetalle.getChat_id();	                	
            	msg.setChatId(Long.toString(idChat));	                	 
			    try {
			        execute(msg); 
			        // cada 30 llamadas hago una pausa de 1 segundo
			        if (j%30==0) {
			        	System.out.println("Pausa!!!!! ");          
			        	TimeUnit.SECONDS.sleep(1);	
			        }					        
			      } catch (TelegramApiException | InterruptedException e) {
			        e.printStackTrace();
			      }					    			    
            	j++;
            }
		} else {
			  msg.setText(prop.getProperty("var.Ppal25"));
			  try {
					execute(msg); // Call method to send the photo
				} catch (TelegramApiException e) {
				   e.printStackTrace();
				}
		}
				    	    	    
    }
    
    
    private void GestionComandoAyuda(long chat_id, Usuarios usuario, long user_id, List<String> listaAdmins, String name) {
    	String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
    	String imgAnt = EmojiParser.parseToUnicode(":rewind:");
    	String imgSig = EmojiParser.parseToUnicode(":fast_forward:");
    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");
    	
    	String texto0 = prop.getProperty("var.textoStart0");
    	String texto1 = prop.getProperty("var.textoStart1");
    	String texto2 = prop.getProperty("var.textoStart2");
        String texto3 = prop.getProperty("var.textoStart3");
        String texto4 = prop.getProperty("var.textoStart4");
        String texto5 = prop.getProperty("var.textoStart5");

      
        String finalMessage = null;
        finalMessage = texto0 + " <b>" + name + "</b>.\n";
        finalMessage = finalMessage.concat(" \n");
        finalMessage = finalMessage.concat(texto1 + "\n");
        finalMessage = finalMessage.concat(" \n"); 
        finalMessage = finalMessage.concat(texto2 + "\n");    	
        finalMessage = finalMessage.concat(" \n");    	
        finalMessage = finalMessage.concat(texto3 + " \n");
        finalMessage = finalMessage.concat(" \n");
        finalMessage = finalMessage.concat(texto4 + " \n");
        finalMessage = finalMessage.concat(" \n");  
        finalMessage = finalMessage.concat(texto5 + " \n");
        finalMessage = finalMessage.concat(" \n");  
        finalMessage = finalMessage.concat(squareEmoji + " Admin: @rormeno75 \n");
        finalMessage = finalMessage.concat(" \n");
    	//
        finalMessage = finalMessage.concat(prop.getProperty("var.Ppal26") + " \n");	 
    	finalMessage = finalMessage.concat(" \n");
    	finalMessage = finalMessage.concat(squareEmoji + " /Start \n");    
    	finalMessage = finalMessage.concat(squareEmoji + " /ayuda - /help \n");    	       
    	finalMessage = finalMessage.concat(squareEmoji + " /restaurantes - /restaurants -> " + prop.getProperty("var.Ppal27") + " \n");
    	finalMessage = finalMessage.concat(" \n");
        LocalUsuariosDAO locUsu = new LocalUsuariosDAO();
        List<String> listaTiposUsu = locUsu.obtenerTiposUsuario(usuario.getId_usuario());
   	    if (listaTiposUsu.contains("PR")) {
    		finalMessage = finalMessage.concat(" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal28") + " \n");
    		finalMessage = finalMessage.concat(" \n");
    		finalMessage = finalMessage.concat(manEmoji + prop.getProperty("var.Ppal29") + " \n");
    		finalMessage = finalMessage.concat(" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal30")  +" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal31")  +" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal32")  +" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal33")  +" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal34")  +" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal35")  +" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal36")  +" \n");
    		finalMessage = finalMessage.concat(" \n");
    	}
    	// Comandos para Administradores del bot. Suele coincidir con los propietarios pero no tiene por que
    	if (listaAdmins.contains(Long.toString(user_id))) {
    		finalMessage = finalMessage.concat(" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal37") + " \n");
    		finalMessage = finalMessage.concat(" \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal38") + " \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal39") + " \n");
    		finalMessage = finalMessage.concat(prop.getProperty("var.Ppal40") + " \n");
    		finalMessage = finalMessage.concat(" \n");
    	}
    				
		SendMessage msg = new SendMessage();
		msg.setChatId(Long.toString(chat_id));
		msg.setParseMode(ParseMode.HTML);
		msg.setText(finalMessage);	                		               
	    try {
	        execute(msg); // Call method to send the photo
	    } catch (TelegramApiException e) {
	        e.printStackTrace();
	    }
    }

    
    private ReplyKeyboardMarkup botoneraPrincipal(Usuarios usuario) {
    	
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
	    List<KeyboardRow> keyboard = new ArrayList<>();
	    KeyboardRow rowBotonera1 = new KeyboardRow();
	    KeyboardRow rowBotonera2 = new KeyboardRow();	    
	    
	    String texto1 = prop.getProperty("var.botPpal1");
	    String texto2 = prop.getProperty("var.botPpal2");
	    String texto3 = prop.getProperty("var.botPpal3");
	   
	    rowBotonera1.add(texto1);	
	    rowBotonera1.add(texto2);	
	    rowBotonera2.add(texto3);
    	// Add the first row to the keyboard
	    keyboard.add(rowBotonera1);	    
	    keyboard.add(rowBotonera2);	  
	    keyboardMarkup.setKeyboard(keyboard);
	    keyboardMarkup.setResizeKeyboard(true);
	    return keyboardMarkup;
    	
    }            
    
    private void handleIncomingInlineQuery(InlineQuery inlineQuery,Usuarios usuario) {
        String query = inlineQuery.getQuery();
        UsuariosDAO usu = new UsuariosDAO();
        Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        try {
            if (!query.isEmpty()) {
            	AnuncioDAO anun = new AnuncioDAO();
            	String tipoCatB = query.substring(query.indexOf("?")+1, query.length());
            	String[] categoriasOk = prop.getProperty("list.catValidas").split(",");
          	    List<String> listaCatOk = Arrays.asList(categoriasOk); 
            	if (listaCatOk.contains(tipoCatB)) {
	            	int id_local = Integer.parseInt(query.substring(0,query.indexOf("?")));
	            	usu.actuTipo("B", usuario.getId_usuario());
	            	List<Anuncios> results = anun.listAnuncios(tipoCatB, null, id_local);
	                execute(converteResultsToResponse(inlineQuery, results, id_local,usuario,tipoCatB));
            	}
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    
    private void borrarImagenesInLine(InlineQuery inlineQuery,Usuarios usuario) {
        String query = inlineQuery.getQuery();
        Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        if (!query.isEmpty()) {
        	AnuncioDAO anun = new AnuncioDAO();
        	String tipoCatB = query.substring(query.indexOf("?")+1, query.length());
        	String[] categoriasOk = prop.getProperty("list.catValidas").split(",");
      	    List<String> listaCatOk = Arrays.asList(categoriasOk); 
        	if (listaCatOk.contains(tipoCatB)) {
	        	int id_local = Integer.parseInt(query.substring(0,query.indexOf("?")));
	        	List<Anuncios> results = anun.listAnuncios(tipoCatB, null, id_local);
	        	gestionFoto gf = new gestionFoto();
	        	for (int i = 0; i < results.size(); i++) {
	              String nFoto = results.get(i).getId_anuncio() + "_" + results.get(i).getVersion_foto();
	      	      gf.eliminarFichero(nFoto);
	        	}
        	}
        }
    }
    
    private AnswerInlineQuery converteResultsToResponse(InlineQuery inlineQuery, List<Anuncios> results, int id_local,Usuarios usuario, String categoria) {
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        answerInlineQuery.setCacheTime(CACHETIME);
        answerInlineQuery.setIsPersonal(false);
        answerInlineQuery.setSwitchPmText(categoria + ": " + results.size());
        answerInlineQuery.setSwitchPmParameter("_");
        answerInlineQuery.setResults(convertResults(results,id_local,usuario));
        return answerInlineQuery;
    }
    
    private List<InlineQueryResult> convertResults(List<Anuncios> anunciosResult, int id_local, Usuarios usuario) {
    	List<InlineQueryResult> results = new ArrayList<>();
    	LocalesDAO loc = new LocalesDAO();
    	Locales local = loc.obtenerLocalId(id_local);
    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
    	NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
    	df.setMaximumFractionDigits(2);

        for (int i = 0; i < anunciosResult.size(); i++) {
        	
        	String cuerpo = gAnuncio.cuerpoAnuncio(anunciosResult.get(i), usuario);
        	LocalUsuariosDAO localUsu = new LocalUsuariosDAO();
            LocalUsuarios localUsuario = localUsu.obtenerLocalUsuarioId(local.getId_local(), usuario.getId_usuario());
            if (localUsuario.getTipo_usuario().equals("PR")) {
            	//cuerpo = cuerpo.concat(" \n");
            	cuerpo = cuerpo.concat(prop.getProperty("var.Anuncios64") + " /"  + local.getNombre() +"_"+ anunciosResult.get(i).getId_anuncio());
            } 
        	     	
            InputTextMessageContent messageContent = new InputTextMessageContent();
            //messageContent.disableWebPagePreview();
            //messageContent.enableMarkdown(true);
            messageContent.setParseMode(ParseMode.HTML);
            messageContent.setMessageText(cuerpo);


            InlineQueryResultArticle article = new InlineQueryResultArticle();
            article.setInputMessageContent(messageContent);
            InlineKeyboardMarkup markupInline = botoneraInLine(anunciosResult.get(i), local,usuario);
            article.setReplyMarkup(markupInline);
            article.setId(Integer.toString(i));
            article.setTitle(anunciosResult.get(i).getTitulo());
            article.setDescription(df.format(anunciosResult.get(i).getPrecio()));
            String urlFoto = gAnuncio.obtenerUrlInLine(anunciosResult.get(i));
            System.out.println("urlFoto  --> " + urlFoto);
            article.setThumbUrl(urlFoto);
            results.add(article);
        }

        return results;
    }
    
    private InlineKeyboardMarkup botoneraInLine(Anuncios anuncio, Locales local,Usuarios usuario) {
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();    
        Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
        
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        if (anuncio.getPrecio().compareTo(BigDecimal.ZERO) != 0 && anuncio.getTitulo() != null && anuncio.getUnidades() > 0) {
        	InlineKeyboardButton iKBA = new InlineKeyboardButton();
        	iKBA.setText(prop.getProperty("var.Anuncios54"));
        	iKBA.setCallbackData(local.getId_local() + "?carroAnuncio_" + anuncio.getId_anuncio());
	    	rowInline.add(iKBA);
		} 
        rowsInline.add(rowInline);
        
        String[] categoriasOk = prop.getProperty("list.catValidas").split(",");
    	List<String> listaCatOk = Arrays.asList(categoriasOk); 
        int nBot = 0;
        int j = 0;
        while (j < listaCatOk.size()) {
          List<InlineKeyboardButton> rowInlineCat = new ArrayList<>();
          int x = 0;
          while (x<2 && nBot<listaCatOk.size()) {
        	  InlineKeyboardButton iKBLis = new InlineKeyboardButton();
        	  iKBLis.setText(listaCatOk.get(nBot));
        	  iKBLis.setSwitchInlineQueryCurrentChat(local.getId_local()+"?" + listaCatOk.get(nBot));
	             rowInlineCat.add(iKBLis);  
	             nBot++;
	             x++;
	    	  }
	      rowsInline.add(rowInlineCat);
    	  j=nBot;
        }
        List<InlineKeyboardButton> rowInlineCarro = new ArrayList<>();
        InlineKeyboardButton iKBCFil = new InlineKeyboardButton();
        iKBCFil.setText(prop.getProperty("var.Anuncios53"));
        iKBCFil.setCallbackData(local.getId_local()+"?carroFiltro");
        rowInlineCarro.add(iKBCFil);
        rowsInline.add(rowInlineCarro);
        
        markupInline.setKeyboard(rowsInline);        
    	return markupInline;	
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
