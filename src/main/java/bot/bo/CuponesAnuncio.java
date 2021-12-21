package bot.bo;

import java.util.Date;

public class CuponesAnuncio {
	   private int id_cuponAnuncio;
	   private int id_anuncio;
	   private String estado;
	   private String textocupon;
	   private int porcentaje;
	   private Date fechaAlta;
	   private Date fechaModif;
	   private Date fechaBaja;
	  
	   

		public CuponesAnuncio(int id_cuponAnuncio, int id_anuncio, String estado, String textocupon, int porcentaje, Date fechaAlta, Date fechaModif, Date fechaBaja) {
		super();
		this.id_cuponAnuncio = id_cuponAnuncio;
		this.id_anuncio = id_anuncio;
		this.estado = estado;
		this.textocupon = textocupon;
		this.porcentaje = porcentaje;
		this.fechaAlta = fechaAlta;
		this.fechaModif = fechaModif;
		this.fechaBaja = fechaBaja;
	}
		
		public CuponesAnuncio() {
			// TODO Auto-generated constructor stub
		}



		public int getId_cuponAnuncio() {
			return id_cuponAnuncio;
		}

		public void setId_cuponAnuncio(int id_cuponAnuncio) {
			this.id_cuponAnuncio = id_cuponAnuncio;
		}

		public int getId_anuncio() {
			return id_anuncio;
		}



		public void setId_anuncio(int id_anuncio) {
			this.id_anuncio = id_anuncio;
		}



		public String getEstado() {
			return estado;
		}



		public void setEstado(String estado) {
			this.estado = estado;
		}


		public String getTextocupon() {
			return textocupon;
		}

		public void setTextocupon(String textocupon) {
			this.textocupon = textocupon;
		}

		public int getPorcentaje() {
			return porcentaje;
		}



		public void setPorcentaje(int porcentaje) {
			this.porcentaje = porcentaje;
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
