package bot.bo;

import java.math.BigDecimal;
import java.util.Date;

public class Anuncios {
	   private int id_anuncio;
	   private int id_usuario;
	   private int id_local;
	   private String flag_campo;
	   private String validado;
	   private String titulo;
	   private String descripcion;
	   private String categoria;
	   private String contacto;
	   private String direccion;
	   private String foto;
	   private int id_usu_denuncia;
	   private int version_foto;
	   private Date fechaAlta;
	   private Date fechaModif;
	   private Date fechaBaja;
	   private BigDecimal precio;
	   private int unidades;

		public Anuncios(int id_anuncio, int id_usuario, int id_local, String flag_campo, String validado, String titulo, String descripcion, String categoria,
				String contacto, String direccion, String foto, int id_usu_denuncia, int version_foto, Date fechaAlta, Date fechaModif, Date fechaBaja,
				BigDecimal precio, int unidades) {
			super();
			this.id_anuncio = id_anuncio;
			this.id_usuario = id_usuario;
			this.id_local = id_local;
			this.flag_campo = flag_campo;
			this.validado = validado;
			this.titulo = titulo;
			this.descripcion = descripcion;
			this.categoria = categoria;
			this.contacto = contacto;
			this.direccion = direccion;
			this.foto = foto;
			this.id_usu_denuncia = id_usu_denuncia;
			this.version_foto = version_foto;
			this.fechaAlta = fechaAlta;
			this.fechaModif = fechaModif;
			this.fechaBaja = fechaBaja;
			this.precio = precio;
			this.unidades = unidades;
		}
		

		public int getId_anuncio() {
			return id_anuncio;
		}

		public void setId_anuncio(int id_anuncio) {
			this.id_anuncio = id_anuncio;
		}

		public int getId_usuario() {
			return id_usuario;
		}


		public void setId_usuario(int id_usuario) {
			this.id_usuario = id_usuario;
		}


		public int getId_local() {
			return id_local;
		}

		public void setId_local(int id_local) {
			this.id_local = id_local;
		}


		public String getFlag_campo() {
			return flag_campo;
		}

		public void setFlag_campo(String flag_campo) {
			this.flag_campo = flag_campo;
		}
		
		public String getTitulo() {
			return titulo;
		}

		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}

		public String getValidado() {
			return validado;
		}

		public void setValidado(String validado) {
			this.validado = validado;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public String getCategoria() {
			return categoria;
		}

		public void setCategoria(String categoria) {
			this.categoria = categoria;
		}

		public String getContacto() {
			return contacto;
		}

		public void setContacto(String contacto) {
			this.contacto = contacto;
		}

				
		public String getDireccion() {
			return direccion;
		}


		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}


		public String getFoto() {
			return foto;
		}

		public void setFoto(String foto) {
			this.foto = foto;
		}

		public int getId_usu_denuncia() {
			return id_usu_denuncia;
		}

		public void setId_usu_denuncia(int id_usu_denuncia) {
			this.id_usu_denuncia = id_usu_denuncia;
		}

		
		public int getVersion_foto() {
			return version_foto;
		}


		public void setVersion_foto(int version_foto) {
			this.version_foto = version_foto;
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


		public BigDecimal getPrecio() {
			return precio;
		}

		public void setPrecio(BigDecimal precio) {
			this.precio = precio;
		}
		
		public int getUnidades() {
			return unidades;
		}

		public void setUnidades(int unidades) {
			this.unidades = unidades;
		}
	
}
