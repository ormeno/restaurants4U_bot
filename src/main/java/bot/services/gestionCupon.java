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
import bot.bo.CuponesAnuncio;
import bot.bo.Pedidos;
import bot.bo.ProductosPedido;
import bot.bo.Usuarios;
import bot.dao.AnuncioDAO;
import bot.dao.CuponDAO;
import bot.dao.PedidoDAO;
import bot.dao.ProductoPedidoDAO;
import bot.dao.UsuariosDAO;
import res.resourceLoader;

public class gestionCupon extends TelegramLongPollingBot {
	
	Properties prop = resourceLoader.loadProperties("configuracion.properties");
	String[] usuariosAdm = prop.getProperty("list.usersAdm").split(",");
	List<String> listaAdmins = Arrays.asList(usuariosAdm);
	NumberFormat df = NumberFormat.getCurrencyInstance(new Locale(prop.getProperty("var.lenguaje"),prop.getProperty("var.pais")));
	
	private static final BigDecimal TWO = new BigDecimal("2");
	private static BigDecimal HUNDRED = new BigDecimal("100");
    private static int DECIMALS = 2;
	private static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
	
	public BigDecimal getDescuento(BigDecimal precio, BigDecimal porcentaje){
	    BigDecimal result = precio.multiply(porcentaje);
	    result = result.divide(HUNDRED, ROUNDING_MODE);
	    return rounded(result);
	  }
	
	private BigDecimal rounded(BigDecimal number){
	    return number.setScale(DECIMALS, ROUNDING_MODE);
	  }
	
	public CuponesAnuncio comprobacionBono(String mensaje, Pedidos pedido) {
		CuponDAO cup = new CuponDAO();
		CuponesAnuncio cupon = null;
		int j = 0;
		List<CuponesAnuncio> cuponesAnuncio = cup.obtenerCuponesAnuncio("V");
	    while (cuponesAnuncio.size() > j) {
	       if (mensaje.contains(cuponesAnuncio.get(j).getTextocupon()) && cuponesAnuncio.get(j).getId_anuncio() == pedido.getId_anuncio()) {
	    	   cupon = cuponesAnuncio.get(j);
	    	   j=cuponesAnuncio.size();
	       }
           j++;
        } 
		return cupon;
    }	
	
	public void comprobacionCuponCarro(long chat_id, long user_id, String mensaje) {
		SendMessage message = new SendMessage();
		message.setChatId(Long.toString(chat_id));
		message.setParseMode(ParseMode.HTML);     	 
		String textoCupon = mensaje.substring(6,mensaje.length());
        String texto = null;
        int id_productoPedido = 0;
        df.setMaximumFractionDigits(2);
        gestionCarro gCarro = new gestionCarro();
		CuponDAO cup = new CuponDAO();
		CuponesAnuncio cupon = null;
		ProductosPedido prodPedido = null;
		PedidoDAO ped = new PedidoDAO();
		AnuncioDAO anuncio = new AnuncioDAO(); 
		Anuncios anun = null;
		ProductoPedidoDAO carro = new ProductoPedidoDAO();
		UsuariosDAO usu = new UsuariosDAO(); 
	    Usuarios usuario = usu.obtenerUsuario((int)user_id);
		Pedidos pedidoPen = ped.pedidoPendiente(usuario.getId_usuario());
		if (pedidoPen==null) {
			texto = "<b>Carro de la compra vacio</b>\n"; 
		} else {
			List<ProductosPedido> productosPedido =  carro.obtenerProductosPedido(pedidoPen.getId_pedido());
			int z=0;
			while (productosPedido.size() > z) {
				int j = 0;
				List<CuponesAnuncio> cuponesAnuncio = cup.obtenerCuponesAnuncio("V");
			    while (cuponesAnuncio.size() > j) {
			       if (textoCupon.equals(cuponesAnuncio.get(j).getTextocupon()) && cuponesAnuncio.get(j).getId_anuncio() == productosPedido.get(z).getId_anuncio() && productosPedido.get(z).getFlagDescuento().equals("N")) {
			    	   cupon = cuponesAnuncio.get(j);
			    	   // NOTA: id_local a 0 para que no de error de compilación mientras no uso cupones 
			    	   anun = anuncio.leerAnuncio(productosPedido.get(z).getId_anuncio(),"B",0, null,0);
			    	   id_productoPedido = productosPedido.get(z).getId_productoPedido();
			    	   prodPedido = productosPedido.get(z);
			    	   j=cuponesAnuncio.size();
			    	   z=productosPedido.size();
			       }
		           j++;
		        } 
			  z++;
	        } 
			if (cupon!=null) {
				BigDecimal precioTotal = gCarro.obtenerPrecioCarro(productosPedido);
				BigDecimal cant = BigDecimal.valueOf(prodPedido.getCantidad());
    			BigDecimal descuentoTotal = getDescuento(prodPedido.getPrecioProductoPedido().multiply(cant), BigDecimal.valueOf(cupon.getPorcentaje()));
    			BigDecimal descuentoUnitario = getDescuento(prodPedido.getPrecioProductoPedido(), BigDecimal.valueOf(cupon.getPorcentaje()));
    			texto = "<b>¡¡ CUPON " + cupon.getTextocupon() + " VALIDO !! </b> \n";
    			texto = texto.concat(" \n");
    			texto = texto.concat("<b>APLICADO " + cupon.getPorcentaje() + "% DE DESCUENTO EN " + anun.getTitulo().toUpperCase() + " (Nuevo importe: " + df.format(prodPedido.getPrecioProductoPedido().multiply(cant).subtract(descuentoTotal))+ ") </b>\n");
    			texto = texto.concat(" \n");
    			texto = texto.concat("Total carro con descuento: <b>" + df.format(precioTotal.subtract(descuentoTotal)) + "</b> \n");
    			texto = texto.concat(" \n");
    			carro.actuPrecioConDescuentoCarro(prodPedido.getPrecioProductoPedido().subtract(descuentoUnitario), id_productoPedido);
    		} else {
    			texto = "<b>Cupón NO válido</b>\n"; 
    		}
		}
		
	    message.setText(texto);       
        try {            
            execute(message);            
          } catch (TelegramApiException e) {
                     e.printStackTrace();
          }
    }
	 
	 public void AltaCuponAnuncio(long chat_id, long user_id, String tipo_usuario, String message_text) {
		    int  id_anuncio= Integer.parseInt(message_text.substring(message_text.indexOf("A:")+2,message_text.indexOf("C:")-1));
	    	String  textocupon= message_text.substring(message_text.indexOf("C:")+2,message_text.indexOf("P:")-1);
	    	int  porcentaje= Integer.parseInt(message_text.substring(message_text.indexOf("P:")+2,message_text.length()));		 
	    	
	    	SendMessage message = new SendMessage();
	    	message.setChatId(Long.toString(chat_id));
	    	message.setParseMode(ParseMode.HTML);     	            	                                               
	        String texto = null;
	    	if (tipo_usuario.equals("PR")) {   
	    	    AnuncioDAO anun = new AnuncioDAO();
	    	    // se valida que el anuncio existe 
	            Boolean existe = anun.leerIdAnuncioValidado(id_anuncio);
	        	if (!existe) {
	        		texto = "<b> NO EXISTE EL ANUCIO: </b>" + id_anuncio;
	        	} else {
	        		// se valida el porcentaje
	        		if (porcentaje <0 || porcentaje >100) {
	        			texto = "<b> PORCENTAJE ERRONEO (Valor entre 0 y 100)</b>";
	        		} else {
	        			CuponesAnuncio cupon = new CuponesAnuncio();
	        			cupon.setId_anuncio(id_anuncio);
	        			cupon.setTextocupon(textocupon);
	        			cupon.setPorcentaje(porcentaje);
	        			CuponDAO cup = new CuponDAO();
	        		    int id_cupon = cup.altaCuponAnuncio(user_id, cupon);
	        		    texto = "<b> ¡¡ SE HA DADO DE ALTA EL CUPON DESCUENTO!! </b>";
	        		    texto = texto.concat(" \n");
	        		    texto = texto.concat("<b>ID Cupón: " + id_cupon  + "</b>\n"); 
	        		    texto = texto.concat("ID Anuncio: " + id_anuncio  + "\n"); 
	        		    texto = texto.concat("Código cupón: " + textocupon + " \n"); 
	        		    texto = texto.concat("Porcentaje descuento: " + porcentaje + " \n"); 
	        		    texto = texto.concat(" \n");
	        		}
		        }
		        
	        } else {
	        	texto = "<b> ¡¡ Comando no valido !! </b>";
	        }
      	    
	        message.setText(texto);       
	        try {            
	            execute(message);            
	          } catch (TelegramApiException e) {
	                     e.printStackTrace();
	          }
	    }
	 
	 public void BorrarCuponAnuncio(long chat_id, long user_id, String tipo_usuario, int idCupon) { 
	    	
	    	SendMessage message = new SendMessage();
	    	message.setChatId(Long.toString(chat_id));
	    	message.setParseMode(ParseMode.HTML);     	            	                                               
	        String texto = null;
	    	if (tipo_usuario.equals("PR")) {   
	         	// se valida que el cupón indicado existe en estado Vigente
	    		CuponDAO cup = new CuponDAO();		
	            int row = cup.borrarCuponAnuncio(idCupon);
	            if (row!=0) {
	              texto = "<b> ¡¡ SE HA BORRADO EL CUPON DESCUENTO!! (idCuponAnuncio: </b>" + idCupon;
        		  texto = texto.concat(" \n");
	            } else {
	               texto = "<b> ¡¡ NO EXISTE EL CUPON DESCUENTO INDICADO !!</b> idCuponAnuncio: " + idCupon;
	            }
	        	
	        } else {
	        	texto = "<b> ¡¡ Comando no valido !! </b>";
	        }
   	    
	        message.setText(texto);       
	        try {            
	            execute(message);            
	          } catch (TelegramApiException e) {
	                     e.printStackTrace();
	          }
	    }
	  
	 public void ConsultaCupones(long chat_id, long user_id, String tipo_usuario) {
			SendMessage message = new SendMessage();
			message.setChatId(Long.toString(chat_id));
			message.setParseMode(ParseMode.HTML);     	            	                                              
			CuponDAO cup = new CuponDAO();	  	
	        String ballEmoji = EmojiParser.parseToUnicode(":small_blue_diamond:");
	        String squareEmoji = EmojiParser.parseToUnicode(":white_small_square:");
	        String texto = null;
	        if (tipo_usuario.equals("PR")) {  
		        List<CuponesAnuncio> cuponesAnuncio = cup.obtenerCuponesAnuncio("V");
		        if (!cuponesAnuncio.isEmpty()) {
			        	int j = 0;
			        	texto = ballEmoji + " LISTADO DE CUPONES ANUNCIO ACTIVOS \n";
			        	texto = texto.concat(" \n");
			        	while (cuponesAnuncio.size() > j) {
			        	  texto = texto.concat(squareEmoji + "<b> Id:</b>" + cuponesAnuncio.get(j).getId_cuponAnuncio());
			              texto = texto.concat(" <b>C:</b>" + cuponesAnuncio.get(j).getTextocupon() + " <b>P:</b>" + cuponesAnuncio.get(j).getPorcentaje());           
			              texto = texto.concat("% - <b>Anuncio:</b>" + cuponesAnuncio.get(j).getId_anuncio() + "\n");
			              j++;
			        	}
		        } else {
			       	texto = "<b> ¡¡ NO EXISTEN CUPONES DESCUENTO ACTIVOS !! </b>";
			    }
	        } else {
	        	texto = "<b> ¡¡ Comando no valido !! </b>";
	        }
	       
	        message.setText(texto);       
	        try {            
	            execute(message);            
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
        return prop.getProperty("var.BotUsername");
	}
	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		//return null;
        return prop.getProperty("var.BotToken");
	}

}
