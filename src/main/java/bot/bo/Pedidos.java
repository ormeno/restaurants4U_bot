package bot.bo;

import java.math.BigDecimal;
import java.util.Date;

public class Pedidos {
	   private int id_pedido;
	   private int id_usuario;
	   private int id_anuncio;
	   private int id_local;
	   private String estado;
	   private String id_stripe;
	   private String payload;
	   private BigDecimal precio_total;
	   private String ciudad;
	   private String cod_pais;
	   private String cod_postal;
	   private String estadoDir;
	   private String direccion1;
	   private String direccion2;
	   
	   private String nombre_ped;
	   private String mail_ped;
	   private String telefono_ped;
	   
	   private Date fechaAlta;
	   private Date fechaModif;
	   private Date fechaBaja;
	   
	   private String flag;
	   

		public Pedidos(int id_pedido, int id_usuario, int id_anuncio, String estado, String id_stripe, String payload, BigDecimal precio_total, String ciudad,
			String cod_pais, String cod_postal, String estadoDir, String direccion1, String direccion2, String nombre_ped,String mail_ped,String telefono_ped,
			Date fechaAlta,	Date fechaModif, Date fechaBaja, String flag, int id_local) {
		super();
		this.id_pedido = id_pedido;
		this.id_usuario = id_usuario;
		this.id_anuncio = id_anuncio;
		this.estado = estado;
		this.id_stripe = id_stripe;
		this.payload = payload;
		this.precio_total = precio_total;
		this.ciudad = ciudad;
		this.cod_pais = cod_pais;
		this.cod_postal = cod_postal;
		this.estadoDir = estadoDir;
		this.direccion1 = direccion1;
		this.direccion2 = direccion2;
		this.nombre_ped = nombre_ped;
		this.mail_ped = mail_ped;
		this.telefono_ped = telefono_ped;
		this.fechaAlta = fechaAlta;
		this.fechaModif = fechaModif;
		this.fechaBaja = fechaBaja;
		this.flag = flag;
		this.id_local = id_local;
	}


		public Pedidos() {
			// TODO Auto-generated constructor stub
		}


		public int getId_pedido() {
			return id_pedido;
		}


		public void setId_pedido(int id_pedido) {
			this.id_pedido = id_pedido;
		}

		public int getId_usuario() {
			return id_usuario;
		}


		public void setId_usuario(int id_usuario) {
			this.id_usuario = id_usuario;
		}


		public int getId_anuncio() {
			return id_anuncio;
		}


		public void setId_anuncio(int id_anuncio) {
			this.id_anuncio = id_anuncio;
		}


		public String getId_stripe() {
			return id_stripe;
		}


		public void setId_stripe(String id_stripe) {
			this.id_stripe = id_stripe;
		}


		public String getPayload() {
			return payload;
		}


		public void setPayload(String payload) {
			this.payload = payload;
		}




		public BigDecimal getPrecio_total() {
			return precio_total;
		}


		public void setPrecio_total(BigDecimal precio_total) {
			this.precio_total = precio_total;
		}


		public String getCiudad() {
			return ciudad;
		}


		public void setCiudad(String ciudad) {
			this.ciudad = ciudad;
		}


		public String getCod_pais() {
			return cod_pais;
		}


		public void setCod_pais(String cod_pais) {
			this.cod_pais = cod_pais;
		}


		public String getCod_postal() {
			return cod_postal;
		}


		public void setCod_postal(String cod_postal) {
			this.cod_postal = cod_postal;
		}


		public String getEstado() {
			return estado;
		}


		public void setEstado(String estado) {
			this.estado = estado;
		}
 
		

		public String getEstadoDir() {
			return estadoDir;
		}


		public void setEstadoDir(String estadoDir) {
			this.estadoDir = estadoDir;
		}


		public String getDireccion1() {
			return direccion1;
		}


		public void setDireccion1(String direccion1) {
			this.direccion1 = direccion1;
		}


		public String getDireccion2() {
			return direccion2;
		}


		public void setDireccion2(String direccion2) {
			this.direccion2 = direccion2;
		}


		public String getNombre_ped() {
			return nombre_ped;
		}


		public void setNombre_ped(String nombre_ped) {
			this.nombre_ped = nombre_ped;
		}


		public String getMail_ped() {
			return mail_ped;
		}


		public void setMail_ped(String mail_ped) {
			this.mail_ped = mail_ped;
		}


		public String getTelefono_ped() {
			return telefono_ped;
		}


		public void setTelefono_ped(String telefono_ped) {
			this.telefono_ped = telefono_ped;
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


		public String getFlag() {
			return flag;
		}


		public void setFlag(String flag) {
			this.flag = flag;
		}


		public int getId_local() {
			return id_local;
		}


		public void setId_local(int id_local) {
			this.id_local = id_local;
		}
  
	 
		 
}

