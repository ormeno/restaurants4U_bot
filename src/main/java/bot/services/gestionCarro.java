package bot.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.vdurmont.emoji.EmojiParser;

import bot.bo.Anuncios;
import bot.bo.Locales;
import bot.bo.Pedidos;
import bot.bo.ProductosPedido;
import bot.bo.Usuarios;
import bot.dao.AnuncioDAO;
import bot.dao.LocalesDAO;
import bot.dao.PedidoDAO;
import bot.dao.ProductoPedidoDAO;
import bot.dao.UsuariosDAO;
import res.resourceLoader;

public class gestionCarro extends TelegramLongPollingBot {
	
	Properties propGeneral = resourceLoader.loadProperties("configuracion.properties");
	String[] usuariosAdm = propGeneral.getProperty("list.usersAdm").split(",");
	List<String> listaAdmins = Arrays.asList(usuariosAdm);
	
	private GestionAnuncios gAnuncio = new GestionAnuncios();
	private gestionPedido gPedido = new gestionPedido();
	private static final BigDecimal TWO = new BigDecimal("2");
	private static BigDecimal HUNDRED = new BigDecimal("100");
	//private static BigDecimal PERCENTAGE = new BigDecimal("17.34");
    private static int DECIMALS = 2;
	private static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
	
	private BigDecimal getIVA(BigDecimal precio, BigDecimal porcentaje){
	    BigDecimal result = precio.multiply(porcentaje);
	    result = result.divide(HUNDRED, ROUNDING_MODE);
	    return rounded(result);
	  }
	
	private BigDecimal rounded(BigDecimal number){
	    return number.setScale(DECIMALS, ROUNDING_MODE);
	  }
	
	public void addProductoCarro(long chat_id, long user_id,int id_anuncio, Pedidos pedido, int id_local) {
		 AnuncioDAO anun = new AnuncioDAO();
		 PedidoDAO ped = new PedidoDAO();
		 ProductoPedidoDAO carro = new ProductoPedidoDAO();
	     Anuncios anuncio = anun.leerAnuncio(id_anuncio,"B",0,null,id_local);
	     UsuariosDAO usu = new UsuariosDAO(); 
		 Usuarios usuario = usu.obtenerUsuario((int)user_id);
	     // Compruebo si existe pedido en curso  (en el carro si se finaliza la compra se pasa a Pagado y tb se podrá borrar)
	     if (pedido == null) {
	    	 int id_pedido = ped.altaPedidoMetalico(usuario.getId_usuario(), id_anuncio, anuncio.getPrecio(),id_local);
	    	 carro.altaProductoPedido(id_pedido,id_anuncio,anuncio.getPrecio());
	     } else {
	    	 // compruebo si en el carro ya existe el producto a añadido
	    	 int id_productoPedido = carro.buscarIdProdPedidoAnuncio(pedido.getId_pedido(), id_anuncio);
	    	 if (id_productoPedido == 0) {
	    		 // añado el producto al pedido
	    		 carro.altaProductoPedido(pedido.getId_pedido(),id_anuncio,anuncio.getPrecio());
	    	 } else {
	    		 // actualizo la cantidad
	    		 carro.actuCantidadProductoPedido(pedido.getId_pedido(),id_anuncio);
	    	 }
	     }
    }
	
	public void MostrarCarro(long chat_id, long user_id, Usuarios usuario,Locales local) {
		
	    SendMessage msg = new SendMessage();
	    msg.setChatId(Long.toString(chat_id));
	    msg.setParseMode(ParseMode.HTML);
	    Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
	    SendInvoice invoice = new SendInvoice();
	    String textCarro = null;
	    String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
     	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");
     	NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
	    df.setMaximumFractionDigits(2);
	    
	    PedidoDAO pedido = new PedidoDAO();
	    ProductoPedidoDAO carro = new ProductoPedidoDAO();
	    
	    Pedidos pedidoPen = pedido.pedidoPendienteLocal(usuario.getId_usuario(), local.getId_local());
	    if (pedidoPen == null) {
	      textCarro = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
		  textCarro = textCarro.concat(" \n");
	      textCarro = textCarro.concat(prop.getProperty("var.Carro1"));
	    } else {
	      List<ProductosPedido> productosPedido =  carro.obtenerProductosPedido(pedidoPen.getId_pedido());
	      textCarro = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
	      textCarro = textCarro.concat(" \n");
	      textCarro = textCarro.concat(prop.getProperty("var.Carro2"));
	      textCarro = textCarro.concat("  \n");
	      String textoDet = textoDetallePedido(productosPedido, local,usuario);
	      textCarro = textCarro.concat("  \n");
	      textCarro = textCarro.concat(textoDet);
	      if (prop.getProperty("var.flagPagoTarjeta").equals("NO")) {
	        BigDecimal precioTotal = obtenerPrecioCarro(productosPedido);
	        textCarro = textCarro.concat(" \n");
	        textCarro = textCarro.concat("<b>Total: " + df.format(precioTotal) + " </b>\n");
	      }
	      int j = 0;
	      Pedidos ped = pedido.obtenerPedido(pedidoPen.getId_pedido());
          InlineKeyboardMarkup  markupInline = botoneraCarro(productosPedido, ped, user_id, null,local.getId_local(),usuario);      
          if (prop.getProperty("var.flagPagoTarjeta").equals("SI")) {
	        invoice = mostrarPagoCarro(chat_id, ped, productosPedido, usuario, markupInline);
          } else {
        	  msg.setReplyMarkup(markupInline);
          }
	    }
	    msg.setText(textCarro);
    try {            
        execute(msg);  
        if (pedido != null) {
        	if (prop.getProperty("var.flagPagoTarjeta").equals("SI")) {
            	execute(invoice);    
        	}
        }
      } catch (TelegramApiException e) {
                     e.printStackTrace();
      }
    }
	
    protected SendInvoice mostrarPagoCarro(long chat_id, Pedidos pedido, List<ProductosPedido> productosPedido, Usuarios usuario, InlineKeyboardMarkup markupInline) {
	  SendInvoice invoice = new SendInvoice();
	  BigDecimal precio = new BigDecimal("0");
	  BigDecimal precioProducto = new BigDecimal("0");
	  BigDecimal cantidadBigD = new BigDecimal("1");
	  precio = obtenerPrecioCarro(productosPedido);
	  Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
      //
	  String texto = prop.getProperty("var.Carro3");
  
	  List<LabeledPrice> prices1 = new ArrayList<>();
	  prices1.add(new LabeledPrice("Carro", precio.movePointRight(2).intValue()));
	  
	  invoice.setChatId(Long.toString(chat_id));
	  invoice.setTitle("Pedido_"+pedido.getId_pedido());
	  invoice.setDescription(texto);
	  invoice.setPayload("idPedido_" + pedido.getId_pedido() + "_Usu_" + usuario.getNombre());               		                   		                                  
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

	public BigDecimal obtenerPrecioCarro(List<ProductosPedido> productosPedido) {
		BigDecimal precio = new BigDecimal("0");
		BigDecimal precioProducto;
		BigDecimal cantidadBigD;
		int j=0;
		while (productosPedido.size() > j) {
			  cantidadBigD =  BigDecimal.valueOf(productosPedido.get(j).getCantidad());
			  precioProducto = productosPedido.get(j).getPrecioProductoPedido();
			  precio = precioProducto.multiply(cantidadBigD).add(precio);
			  j++;
	    }
		return precio;
	}

	 protected InlineKeyboardMarkup botoneraCarro(List<ProductosPedido> productosPedido, Pedidos pedido, long user_id, String texto, int id_local,Usuarios usuario) {
	    	
		    Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
	    	String imgEuro = EmojiParser.parseToUnicode(":euro:");
	    	String imgTarjeta = EmojiParser.parseToUnicode(":credit_card:");
	    	String imgPapelera = EmojiParser.parseToUnicode(":wastebasket:");
	    	String imgEquis = EmojiParser.parseToUnicode(":x:");
            
	    	AnuncioDAO anuncio = new AnuncioDAO();   
	    	int j = 0;
	    	List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
	    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
	    	
	    	List<InlineKeyboardButton> rowBotonPago = new ArrayList<>();
	    	if (prop.getProperty("var.flagPagoTarjeta").equals("SI")) {
	    		InlineKeyboardButton iKBTsi = new InlineKeyboardButton();
	    		iKBTsi.setPay(true);
	    		iKBTsi.setText(prop.getProperty("var.Carro4") + imgTarjeta);
	    		rowBotonPago.add(iKBTsi);
	    	}
	    	 if (prop.getProperty("var.flagPagoMetalico").equals("SI")) {
	    		 InlineKeyboardButton iKBMsi = new InlineKeyboardButton();
	    		 iKBMsi.setCallbackData(id_local + "?metalicoCarro_" + pedido.getId_pedido());
	    		 iKBMsi.setPay(true);
	    		 iKBMsi.setText(prop.getProperty("var.Carro5"));
	    		 rowBotonPago.add(iKBMsi);
	    	 }
	    	
	    	while (productosPedido.size() > j) {
	    		Anuncios anun = anuncio.leerAnuncio(productosPedido.get(j).getId_anuncio(),"B",0, null, id_local);    
	    		List<InlineKeyboardButton> rowBotonProducto = new ArrayList<>();
	    		int cantidad = productosPedido.get(j).getCantidad();
	    		int idProdPedido = productosPedido.get(j).getId_productoPedido();
	    		InlineKeyboardButton iKBP = new InlineKeyboardButton();
	    		iKBP.setText("(" + cantidad + ") " + anun.getTitulo());
	    		iKBP.setCallbackData(id_local + "?prodPedido");
	    		rowBotonProducto.add(iKBP);   
	    		InlineKeyboardButton iKBPc = new InlineKeyboardButton();
	    		iKBPc.setText(prop.getProperty("var.Carro6") + imgEquis);
	    		iKBPc.setCallbackData(id_local + "?borrarProdPedidoCarro_"+idProdPedido);
	    		rowBotonProducto.add(iKBPc);
	    		rowsInline.add(rowBotonProducto);
	            j++;
            }
	    	
	    	List<InlineKeyboardButton> rowBotonBorrarCarro = new ArrayList<>();
	    	InlineKeyboardButton iKBBC = new InlineKeyboardButton();
	    	iKBBC.setText(prop.getProperty("var.Carro7") + imgPapelera);
	    	iKBBC.setCallbackData(id_local+"?borrarCarro_" + pedido.getId_pedido());
	    	rowBotonBorrarCarro.add(iKBBC);
	    	
	    	rowsInline.add(rowBotonPago);
	    	rowsInline.add(rowBotonBorrarCarro);
	        
	        markupInline.setKeyboard(rowsInline);
	        return markupInline;
	    }
	 
	    public void  GestionCarroCB(long chat_id, long user_id, String call_data, Usuarios usuario) {
	    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
	    	String texto = null;    	
	    	String okEmoji = EmojiParser.parseToUnicode(":ok_hand:");
	    	String star2Emoji = EmojiParser.parseToUnicode(":star2:");
	    	String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
	    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");  
	    	int id_local = Integer.parseInt(call_data.substring(0,call_data.indexOf("?")));
	    	LocalesDAO loc = new LocalesDAO();
	        Locales local = loc.obtenerLocalId(id_local);
	    	int id = Integer.parseInt(call_data.substring(call_data.indexOf("Carro_")+6,call_data.length()));
	    	//EditMessageText resAnuncio = new EditMessageText().setChatId(Long.toString(chat_id)).setMessageId(toIntExact(message_id)).setParseMode(ParseMode.HTML);   
	    	SendMessage respuesta = new SendMessage();
	    	respuesta.setChatId(Long.toString(chat_id));
	    	respuesta.setParseMode(ParseMode.HTML);
	    	SendInvoice invoice = new SendInvoice();
	    	boolean existePedido = true;
	    	NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
	    	df.setMaximumFractionDigits(2);

	    	Anuncios res = null;
	    	UsuariosDAO usu = new UsuariosDAO();
	    	ProductoPedidoDAO carro = new ProductoPedidoDAO();
	    	PedidoDAO pedido = new PedidoDAO();
	    	
			if (call_data.contains("borrarProdPedidoCarro_") )	{    
			  // id = id_productoPedido
		      ProductosPedido productoPedido =  carro.obtenerProductoPedido(id);
		      Pedidos ped = pedido.obtenerPedido(productoPedido.getId_pedido());
	    	  carro.borraProductoPedido(id);		   
	    	  // valido si era el último producto del carro
	    	  List<ProductosPedido> productosPedido =  carro.obtenerProductosPedido(productoPedido.getId_pedido());
	    	  if (productosPedido.isEmpty()) { 
	    		  pedido.borrarPedido(ped.getId_pedido());
	    		  texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
	    		  texto = texto.concat("\n");
	    		  texto = texto.concat(prop.getProperty("var.Carro8") + "\n"); 
	    		  texto = texto.concat(" \n");
	     		  texto = texto.concat(prop.getProperty("var.Carro9") + " \n"); 
			   	  existePedido = false;
	    	  } else {
	    		  texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
	    		  texto = texto.concat("\n");
	    		  texto = texto.concat(prop.getProperty("var.Carro8")); 
	    		  if (prop.getProperty("var.flagPagoTarjeta").equals("NO")) {
	    		        BigDecimal precioTotal = obtenerPrecioCarro(productosPedido);
	    		        texto = texto.concat(" \n");
	    		        texto = texto.concat("<b>Total: " + df.format(precioTotal) + " </b>\n");
	    		  }
		    	  InlineKeyboardMarkup  markupInline = botoneraCarro(productosPedido, ped, user_id, null,id_local,usuario);        
		    	  if (prop.getProperty("var.flagPagoTarjeta").equals("SI")) {
			       invoice = mostrarPagoCarro(chat_id, ped, productosPedido, usuario, markupInline);
		    	  } else {
		    		  respuesta.setReplyMarkup(markupInline);
		          }
	    	  }
	    	}
			if (call_data.contains("borrarCarro_") )	{    		
			  // id = id_pedido
			  Pedidos existePed = pedido.obtenerPedido(id);
			  if (existePed!=null) { 
			      carro.borrarCarro(id);		   
			      pedido.borrarPedido(id);
			      texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
	    		  texto = texto.concat("\n");
			   	  texto = texto.concat(prop.getProperty("var.Carro9"));
			   	  existePedido = false;
			  } else {
				  texto = prop.getProperty("var.Carro9");
			  }
		    }
			if (call_data.contains("cuponDescuentoCarro_") )	{    		
				  // id = id_pedido
				  texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
	    		  texto = texto.concat("\n");
			   	  texto = texto.concat("Indique el cupón descuento precedido de '<b>CUPON-</b>' \n");
			   	  texto = texto.concat(" \n");
				  texto = texto.concat("Ejemplo cupón válido-> CUPON-VIP30 \n");
				  texto = texto.concat("Ejemplo cupón NO válido-> VIP30 \n");
			   	  existePedido = false;
			    }
			if (call_data.contains("metalicoCarro_") )	{    		
				  // id = id_pedido
				  Pedidos existePed = pedido.obtenerPedido(id);
				  if (existePed!=null) { 
					  if (local.getDatosEnvio()!=null) {
					    texto=DatosEnvioDirecto(chat_id,user_id,id,local,usuario);
				   	    existePedido = false;
					  }  else {
						  List<ProductosPedido> productosPedido =  carro.obtenerProductosPedido(id);
						  String textoDet = textoDetallePedido(productosPedido,local,usuario);
		                  texto = womanEmoji + "<b>" + prop.getProperty("var.Ppal13")+ local.getNombre() + "</b> " +manEmoji + "\n";
		                  texto = texto.concat(" \n");
		        		  texto = texto.concat(prop.getProperty("var.Ppal14") + id + "\n"); 
		           		  texto = texto.concat("\n");
		            	  texto = texto.concat(local.getDatosConfirPago() + " \n"); 
		         	      texto = texto.concat(textoDet);
		         		  BigDecimal precioTotal = obtenerPrecioCarro(productosPedido);
		         		  texto = texto.concat(" TOTAL: " + df.format(precioTotal) + " \n");
		            	  // Datos de envio en campo libre
		         		  if (local.getDatosSupleEnvio()!=null) {
		  				   texto = texto.concat(" \n");
		  				   texto = texto.concat(local.getDatosSupleEnvio() + " \n");
		  			      }
		            	  texto = texto.concat(" \n");
		            	  texto = texto.concat(prop.getProperty("var.Ppal16")); 
		            	  InlineKeyboardMarkup markupInline = gAnuncio.botoneraSiNo(id,"P",local,usuario);
		            	  respuesta.setReplyMarkup(markupInline);  
					  }
				  } else {
					  texto = prop.getProperty("var.Carro9");  
			      }
			    }
	    	respuesta.setText(texto);	                                	                                	                                	                                       
	        try {
	          execute(respuesta);
	          if (existePedido) {
	        	  if (prop.getProperty("var.flagPagoTarjeta").equals("SI")) {
	                execute(invoice); 
	        	  }
	            }
	        } catch (TelegramApiException e) {
	          e.printStackTrace();
	        }
	    }
	    
	    public String DatosEnvioDirecto(long chat_id, long user_id, int id_pedido,Locales local, Usuarios usuario) {  	  
	    	
	    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
			String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
	        String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
	        String warningEmoji = EmojiParser.parseToUnicode(":warning:");
	        String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
	    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:"); 
	        String texto = null;
	        PedidoDAO ped = new PedidoDAO();
	        int numRow =  ped.actuFlagPedido("DATOSENVIO", id_pedido);
	        texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
  		    texto = texto.concat("\n");
		    texto = texto.concat(ballEmoji + prop.getProperty("var.Carro10") + " \n");
		    texto = texto.concat(" \n");
		    texto = texto.concat(prop.getProperty("var.Ppal14") + id_pedido + "\n");  
		    texto = texto.concat(" \n");
		    texto = texto.concat(prop.getProperty("var.Carro11") + " \n");
		    texto = texto.concat(" \n");
		    int j = 0;
		    List<String> listaCamposEnvio = Arrays.asList(local.getDatosEnvio().split("#"));  
	        while (j < listaCamposEnvio.size()) {
	          texto = texto.concat(squareEmoji +  listaCamposEnvio.get(j) + " \n");
	    	  j++;
	        }
	        if (local.getDatosSupleEnvio()!=null) {
				   texto = texto.concat(" \n");
				   texto = texto.concat(local.getDatosSupleEnvio() + " \n");
			   }
	        texto = texto.concat(" \n");
		    texto = texto.concat(prop.getProperty("var.textoDatosEnvio1") + " \n");
   	        return texto;
	    }
	    
	    public Anuncios obtenerProductoAgotado(List<ProductosPedido> productosPedido,int id_local) {   
	    	Anuncios anuncio = null;    
	    	Anuncios producto = null; 
	    	AnuncioDAO anun = new AnuncioDAO();
	    	int j = 0;	
        	while (productosPedido.size() > j) {
        	  anuncio = anun.leerAnuncio(productosPedido.get(j).getId_anuncio(),"B",0,null,id_local);
        	  if (productosPedido.get(j).getCantidad()>anuncio.getUnidades()) {
        		j=productosPedido.size();
        		producto = anuncio;
        	  } else {
        		j++;
        	  }
            }
	        return producto;
	    }
	    
	    public String textoDetallePedido(List<ProductosPedido> productosPedido, Locales local, Usuarios usuario) {	
	    	
	    	Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
	    	String texto = prop.getProperty("var.Carro12") + " \n";
	    	String squareEmoji = EmojiParser.parseToUnicode(":black_small_square:");
	    	NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
	    	df.setMaximumFractionDigits(2);
	    	int j=0;
	    	AnuncioDAO anuncio = new AnuncioDAO();    
	    	while (productosPedido.size() > j) {
	    		Anuncios anun = anuncio.leerAnuncio(productosPedido.get(j).getId_anuncio(),"B",0, null,local.getId_local());    
	    		int cantidad = productosPedido.get(j).getCantidad();
	    		BigDecimal precio = productosPedido.get(j).getPrecioProductoPedido();
	    		texto = texto.concat(squareEmoji + anun.getTitulo() + " ->" + df.format(precio) + " \n");
	    		if (cantidad >1) {
	    			BigDecimal  precioProductoTotal = precio.multiply(BigDecimal.valueOf(productosPedido.get(j).getCantidad()));
	    			texto = texto.concat("      (X" + cantidad + prop.getProperty("var.Carro13") + df.format(precioProductoTotal) + " ) \n");
	    		}
	    		if (productosPedido.get(j).getFlagDescuento().equals("S")) {
	    			BigDecimal  precioProductoTotal = precio.multiply(BigDecimal.valueOf(productosPedido.get(j).getCantidad()));
	    			texto = texto.concat(prop.getProperty("var.Carro14") + " \n");
	    		}
	            j++;
            }
	    	return texto;
	    }
	    
	    public String textoClientePedidoCarro(List<ProductosPedido> productosPedido, Pedidos pedido, Locales local) {
	    	   UsuariosDAO usu = new UsuariosDAO();
	    	   Usuarios usuario = usu.obtenerUsuarioId(pedido.getId_usuario());
	    	   Properties prop = resourceLoader.ObtenerProperties(usuario.getIdioma());
	    	   String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
		       String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
		       String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
		    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");
		       NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
		       df.setMaximumFractionDigits(2);
		       String texto = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
		       texto = texto.concat("  \n");
			   texto = texto.concat(ballEmoji+ prop.getProperty("var.Pedidos23") + " \n");
			   texto = texto.concat("  \n");
			   texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Ppal14") + pedido.getId_pedido() + " \n");
			   SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			   String fechaAlta= formatter.format(pedido.getFechaAlta());
			   texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Carro15") + fechaAlta + " \n");
			   String textoDet = textoDetallePedido(productosPedido, local,usuario);
			   texto = texto.concat("  \n");
			   texto = texto.concat(textoDet);
			   texto = texto.concat("  \n");
			   BigDecimal precioTotal = obtenerPrecioCarro(productosPedido);
			   if (local.getImpuesto()!=null) {
				     texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Carro16") + df.format(precioTotal.subtract(getIVA(precioTotal,local.getPocenImpuesto()))) + " (sin impuestos) \n");
				     texto = texto.concat(squareEmoji + " " +local.getImpuesto()+ ": " + df.format(getIVA(precioTotal,local.getPocenImpuesto())) + " \n");
					 texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Carro17") + df.format(precioTotal) + " \n");
				   } else {
					 texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Carro17") + df.format(precioTotal) + " \n");
			   }
			   if (local.getDatosSupleEnvio()!=null) {
				   texto = texto.concat(" \n");
				   texto = texto.concat(local.getDatosSupleEnvio() + " \n");
			   }
			   if (local.getDatosEnvio()!=null) {
				   texto = texto.concat(" \n");
				   texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Ppal15") + " \n" + pedido.getDireccion1() + " \n");
			   }
	           if (pedido.getDireccion2()!=null) {
	             if (!pedido.getDireccion2().isEmpty()) {
	 	    	   texto = texto.concat( " " + pedido.getDireccion2()+ " \n");
	 	         }
	           }
	           if (pedido.getCod_postal()!=null) {texto = texto.concat(squareEmoji + " Código postal: " + pedido.getCod_postal()+ " \n");}
	           if (pedido.getCiudad()!=null) {texto = texto.concat(squareEmoji + " Ciudad: " + pedido.getCiudad()+ " \n");}
	 	       texto = texto.concat(" \n");
	 	       if (pedido.getNombre_ped()!=null) {texto = texto.concat(squareEmoji + " Cliente: " + pedido.getNombre_ped()+ " \n");}
	 	       if (pedido.getTelefono_ped()!=null) {texto = texto.concat(squareEmoji + " Telefono pedido: " + pedido.getTelefono_ped()+ " \n");}
	 	       texto = texto.concat(" \n");
	 	       if (pedido.getEstado().equals("PENDIENTE")) {
	 	    	  if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH")) {
	 	    	    texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Carro18") + pedido.getEstado() + " \n");
	 	    	  } else {
	 	    		texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Carro18") + "PENDING" + " \n");
	 	    	  }
	 	    	  texto = texto.concat(" \n");
	 	    	  texto = texto.concat(prop.getProperty("var.Carro19") + " \n");
	 	       } else {
	 	    	  if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH")) {
	 	    	    texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Carro18") + pedido.getEstado() + " \n");
	 	    	  } else {
	 	    	    String estadoIng = gPedido.conversionEstados(pedido.getEstado());
	 	    		texto = texto.concat(squareEmoji + " " + prop.getProperty("var.Carro18") + estadoIng + " \n");
	 	    	  }
	 	       }
			   return texto;
		}
	    
	    
		
		public String textoAdmPedidoCarro(List<ProductosPedido> productosPedido, Pedidos pedido, Usuarios usuarioPropietario, Locales local) {
			UsuariosDAO usu = new UsuariosDAO();
	    	Usuarios usuario = usu.obtenerUsuarioId(pedido.getId_usuario()); 
	    	Properties prop = resourceLoader.ObtenerProperties(usuarioPropietario.getIdioma());
			String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
		    String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
		    String womanEmoji = EmojiParser.parseToUnicode(":woman_cook:");
	    	String manEmoji = EmojiParser.parseToUnicode(":man_cook:");
		    NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(local.getIdioma().substring(0,local.getIdioma().indexOf("-")),local.getIdioma().substring(local.getIdioma().indexOf("-")+1,local.getIdioma().length())));
		    df.setMaximumFractionDigits(2);
		    String textoAdm = womanEmoji + "<b> /"+ local.getNombre() + "</b> " +manEmoji + "\n";
		   textoAdm = textoAdm.concat("  \n");
		   textoAdm = textoAdm.concat(ballEmoji+ prop.getProperty("var.Pedidos24") + " \n");
	       textoAdm = textoAdm.concat(" \n");
	       textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Ppal14") + pedido.getId_pedido() + " \n");
	       textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Carro20") + usuario.getNombre() + " \n");
	       textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Carro21") + usuario.getAliastg() + " \n");
	       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		   String fechaAlta= formatter.format(pedido.getFechaAlta());
	       textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Carro15") + fechaAlta + " \n");
		   String textoDet = textoDetallePedido(productosPedido, local,usuario);
		   textoAdm = textoAdm.concat("  \n");
		   textoAdm = textoAdm.concat(textoDet);
		   textoAdm = textoAdm.concat("  \n");
		   BigDecimal precioTotal = obtenerPrecioCarro(productosPedido);
		   if (local.getImpuesto()!=null) {
	    	     textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Carro16") + df.format(precioTotal.subtract(getIVA(precioTotal,local.getPocenImpuesto()))) + " (sin impuestos) \n");
	    	     textoAdm = textoAdm.concat(squareEmoji + " " +local.getImpuesto()+": " + df.format(getIVA(precioTotal,local.getPocenImpuesto())) + " \n");
	    	     textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Carro17") + df.format(precioTotal) + " \n");
			   } else {
			     textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Carro17") + df.format(precioTotal) + " \n");
			   }
		   if (local.getDatosSupleEnvio()!=null) {
			   textoAdm = textoAdm.concat(" \n");
			   textoAdm = textoAdm.concat(local.getDatosSupleEnvio() + " \n");
		   }
	       if (local.getDatosEnvio()!=null) {
	    	   textoAdm = textoAdm.concat(" \n");
	    	   textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Ppal15") + " \n"  + pedido.getDireccion1() + " \n");
	       }
	       if (pedido.getDireccion2()!=null) {
	         if (!pedido.getDireccion2().isEmpty()) {
	    	   textoAdm = textoAdm.concat( " " + pedido.getDireccion2()+ " \n");
	         }
	       }
	       if (pedido.getCod_postal()!=null) {textoAdm = textoAdm.concat(squareEmoji + " Código postal: " + pedido.getCod_postal()+ " \n");}
           if (pedido.getCiudad()!=null) {textoAdm = textoAdm.concat(squareEmoji + " Ciudad: " + pedido.getCiudad()+ " \n");}
	       textoAdm = textoAdm.concat(" \n");
	       if (pedido.getNombre_ped()!=null) {textoAdm = textoAdm.concat(squareEmoji + " Cliente: " + pedido.getNombre_ped()+ " \n");}
	       if (pedido.getMail_ped()!=null) {textoAdm = textoAdm.concat(squareEmoji + " Mail pedido: " + pedido.getMail_ped()+ " \n");}
	       if (pedido.getTelefono_ped()!=null) {textoAdm = textoAdm.concat(squareEmoji + " Telefono pedido: " + pedido.getTelefono_ped()+ " \n");}
	       textoAdm = textoAdm.concat(" \n");
	       if (usuarioPropietario.getIdioma().equals("ESPAÑOL") || usuarioPropietario.getIdioma().equals("SPANISH")) {
	         textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Carro18") + pedido.getEstado() + " \n");
	       } else {
	    	 String estadoIng = gPedido.conversionEstados(pedido.getEstado());
	    	 textoAdm = textoAdm.concat(squareEmoji + " " + prop.getProperty("var.Carro18") + estadoIng + " \n");  
	       }
	       return textoAdm;
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
