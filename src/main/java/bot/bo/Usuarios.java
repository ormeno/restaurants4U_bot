package bot.bo;

import java.util.Date;

public class Usuarios {

	private int id_usuario;
	private int user_id;
	private int chat_id;
	private String aliastg; 
	private String nombre;
	private String correo;
	private String telefono;
	private String idioma;
	private String pais;
	private String ciudad;
	private String tipo;	
	
	private String tipo_usuario;
	private String provincia;
	private int edad;
	private String observaciones;
	
	private int cont_mensajes;
	private Date fechaAlta;
	private Date fechaModif;
	private Date fechaBaja;
	
	public Usuarios(int id_usuario, int user_id, int chat_id, String aliastg, String nombre, String correo,
			         String telefono, String idioma, String pais,String ciudad,String tipo,String tipo_usuario,String provincia,int edad,String observaciones,
			         int cont_mensajes, Date fechaAlta, Date fechaModif, Date fechaBaja) {
		super();
		this.id_usuario = id_usuario;
		this.user_id = user_id;
		this.chat_id = chat_id;
		this.aliastg = aliastg;
		this.nombre = nombre;
		this.correo = correo;
		this.telefono = telefono;
		this.idioma = idioma;
		this.pais = pais;
		this.ciudad = ciudad;
		this.tipo = tipo;		
		this.tipo_usuario = tipo_usuario;
		this.provincia = provincia;
		this.edad = edad;
		this.observaciones = observaciones;		
		this.cont_mensajes = cont_mensajes;
		this.fechaAlta = fechaAlta;
		this.fechaModif = fechaModif;
		this.fechaBaja = fechaBaja;
	}
	
	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getChat_id() {
		return chat_id;
	}

	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}

	public String getAliastg() {
		return aliastg;
	}

	public void setAliastg(String aliastg) {
		this.aliastg = aliastg;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
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

	public String getTipo() {
		return tipo;
	}
	
	public String getTipoUsuario() {
		return tipo_usuario;
	}

	public void setTipoUsuario(String tipo_usuario) {
		this.tipo_usuario = tipo_usuario;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getCont_mensajes() {
		return cont_mensajes;
	}

	public void setCont_mensajes(int cont_mensajes) {
		this.cont_mensajes = cont_mensajes;
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
