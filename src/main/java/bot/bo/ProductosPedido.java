package bot.bo;

import java.math.BigDecimal;
import java.util.Date;

public class ProductosPedido {
	
	private int id_productoPedido;
	private int id_pedido;
	private int id_anuncio;
	private int cantidad;
	private BigDecimal precioProductoPedido;
	private String flagDescuento;
	private Date fechaAlta;
	private Date fechaModif;
	private Date fechaBaja;
	
	public ProductosPedido(int id_productoPedido, int id_pedido, int id_anuncio, int cantidad, BigDecimal precioProductoPedido, String flagDescuento, Date fechaAlta,
			Date fechaModif, Date fechaBaja) {
		super();
		this.id_productoPedido = id_productoPedido;
		this.id_pedido = id_pedido;
		this.id_anuncio = id_anuncio;
		this.cantidad = cantidad;
		this.precioProductoPedido = precioProductoPedido;
		this.flagDescuento = flagDescuento;
		this.fechaAlta = fechaAlta;
		this.fechaModif = fechaModif;
		this.fechaBaja = fechaBaja;
	}

	public ProductosPedido() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public int getId_productoPedido() {
		return id_productoPedido;
	}

	public void setId_productoPedido(int id_productoPedido) {
		this.id_productoPedido = id_productoPedido;
	}

	public int getId_pedido() {
		return id_pedido;
	}

	public void setId_pedido(int id_pedido) {
		this.id_pedido = id_pedido;
	}

	public int getId_anuncio() {
		return id_anuncio;
	}

	public void setId_anuncio(int id_anuncio) {
		this.id_anuncio = id_anuncio;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioProductoPedido() {
		return precioProductoPedido;
	}

	public void setPrecioProductoPedido(BigDecimal precioProductoPedido) {
		this.precioProductoPedido = precioProductoPedido;
	}
	
	public String getFlagDescuento() {
		return flagDescuento;
	}

	public void setFlagDescuento(String flagDescuento) {
		this.flagDescuento = flagDescuento;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Date getFechaModif() {
		return fechaModif;
	}

	public void setFechaModif(Date fechaModif) {
		this.fechaModif = fechaModif;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	

	
}
