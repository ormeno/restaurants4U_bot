package bot.bo;

import java.math.BigDecimal;
import java.util.Date;

public class Locales {
	   private int id_local;
	   private String nombre;
	   private String descripcion;
	   private String tipo_local;
	   private String plan;
	   private String idioma;
	   private String pais;
       private String ciudad;
	   private String moneda;
	   private String impuesto;
	   private Date fechaAlta;
	   private Date fechaModif;
	   private Date fechaBaja;
	   private String direccion;
	   private String datosEnvio;
	   private String datosConfirPago;
	   private BigDecimal pocenImpuesto;
	   private String datosSupleEnvio;
	   private BigDecimal impSupleEnvio;
	   
	  
	   
	   public Locales(int id_local, String nombre, String descripcion, String tipo_local, String plan, String idioma,
			String pais, String ciudad, String moneda, String impuesto, Date fechaAlta, Date fechaModif,
			Date fechaBaja,String direccion,String datosEnvio,String datosConfirPago, BigDecimal pocenImpuesto, 
			String datosSupleEnvio, BigDecimal impSupleEnvio) {
		super();
		this.id_local = id_local;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.tipo_local = tipo_local;
		this.plan = plan;
		this.idioma = idioma;
		this.pais = pais;
		this.ciudad = ciudad;
		this.moneda = moneda;
		this.impuesto = impuesto;
		this.fechaAlta = fechaAlta;
		this.fechaModif = fechaModif;
		this.fechaBaja = fechaBaja;
		this.direccion = direccion;
		this.datosEnvio = datosEnvio;
		this.datosConfirPago = datosConfirPago;
		this.pocenImpuesto = pocenImpuesto;
		this.datosSupleEnvio = datosSupleEnvio;
		this.impSupleEnvio = impSupleEnvio;
	}


	public int getId_local() {
		return id_local;
	}


	public void setId_local(int id_local) {
		this.id_local = id_local;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getTipo_local() {
		return tipo_local;
	}


	public void setTipo_local(String tipo_local) {
		this.tipo_local = tipo_local;
	}


	public String getPlan() {
		return plan;
	}


	public void setPlan(String plan) {
		this.plan = plan;
	}


	public String getIdioma() {
		return idioma;
	}


	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}


	public String getPais() {
		return pais;
	}


	public void setPais(String pais) {
		this.pais = pais;
	}


	public String getCiudad() {
		return ciudad;
	}


	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}


	public String getMoneda() {
		return moneda;
	}


	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}


	public String getImpuesto() {
		return impuesto;
	}


	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
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


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getDatosEnvio() {
		return datosEnvio;
	}


	public void setDatosEnvio(String datosEnvio) {
		this.datosEnvio = datosEnvio;
	}


	public String getDatosConfirPago() {
		return datosConfirPago;
	}


	public void setDatosConfirPago(String datosConfirPago) {
		this.datosConfirPago = datosConfirPago;
	}


	public BigDecimal getPocenImpuesto() {
		return pocenImpuesto;
	}


	public void setPocenImpuesto(BigDecimal pocenImpuesto) {
		this.pocenImpuesto = pocenImpuesto;
	}


	public String getDatosSupleEnvio() {
		return datosSupleEnvio;
	}


	public void setDatosSupleEnvio(String datosSupleEnvio) {
		this.datosSupleEnvio = datosSupleEnvio;
	}


	public BigDecimal getImpSupleEnvio() {
		return impSupleEnvio;
	}


	public void setImpSupleEnvio(BigDecimal impSupleEnvio) {
		this.impSupleEnvio = impSupleEnvio;
	}
	
	
	   
	   
}
