package bot.bo;

import java.util.Date;

public class LocalUsuarios {
	   private int id_localUsuario;
	   private int id_local;
	   private int id_usuario;
	   private int cont_mensajes;
	   private String tipo;	
	   private String tipo_usuario;
	   private Date fechaAlta;
	   private Date fechaModif;
	   private Date fechaBaja;
	   
	public LocalUsuarios(int id_localUsuario, int id_local, int id_usuario, int cont_mensajes, String tipo,
			String tipo_usuario, Date fechaAlta, Date fechaModif, Date fechaBaja) {
		super();
		this.id_localUsuario = id_localUsuario;
		this.id_local = id_local;
		this.id_usuario = id_usuario;
		this.cont_mensajes = cont_mensajes;
		this.tipo = tipo;
		this.tipo_usuario = tipo_usuario;
		this.fechaAlta = fechaAlta;
		this.fechaModif = fechaModif;
		this.fechaBaja = fechaBaja;
	}

	public int getId_localUsuario() {
		return id_localUsuario;
	}

	public void setId_localUsuario(int id_localUsuario) {
		this.id_localUsuario = id_localUsuario;
	}

	public int getId_local() {
		return id_local;
	}

	public void setId_local(int id_local) {
		this.id_local = id_local;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public int getCont_mensajes() {
		return cont_mensajes;
	}

	public void setCont_mensajes(int cont_mensajes) {
		this.cont_mensajes = cont_mensajes;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo_usuario() {
		return tipo_usuario;
	}

	public void setTipo_usuario(String tipo_usuario) {
		this.tipo_usuario = tipo_usuario;
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
