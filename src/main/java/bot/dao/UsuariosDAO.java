package bot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import bot.database;
import bot.bo.Usuarios;
import bot.bo.Usuarios;
import res.resourceLoader;

public class UsuariosDAO {
	
	database db = new database();

	public Usuarios obtenerUsuario(String user_username) {    	
    	Usuarios usuario = null;       
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
        	PreparedStatement pst = con.prepareStatement("select id_usuario, user_id, chat_id, aliastg, nombre, correo, telefono, idioma, pais, ciudad, "
        			+ "tipo, tipo_usuario, provincia, edad, observaciones, cont_mensajes, fecha_alta, fecha_modif, fecha_baja "
        			+ " FROM usuarios WHERE aliastg = ?");        	
          	pst.setString(1, user_username);  
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {       	        
            	usuario = new Usuarios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), 
      			      rs.getString(11), rs.getString(12), rs.getString(13), rs.getInt(14), rs.getString(15), rs.getInt(16), rs.getDate(17), rs.getDate(18),rs.getDate(19));
            }      
            rs.close();
            pst.close();
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            } finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }
           return usuario;
    }
	
	public Usuarios obtenerUsuario(int user_id) {    	
    	Usuarios usuario = null;       
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
        	PreparedStatement pst = con.prepareStatement("select id_usuario, user_id, chat_id, aliastg, nombre, correo, telefono, idioma, pais, ciudad, "
        			+ "tipo, tipo_usuario, provincia, edad, observaciones, cont_mensajes, fecha_alta, fecha_modif, fecha_baja "
        			+ " FROM usuarios WHERE user_id = ?");        	
          	pst.setInt(1, user_id);  
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {       	        
            	usuario = new Usuarios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), 
            			      rs.getString(11), rs.getString(12), rs.getString(13), rs.getInt(14), rs.getString(15), rs.getInt(16), rs.getDate(17), rs.getDate(18),rs.getDate(19));
            }      
            rs.close();
            pst.close();
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            } finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }
           return usuario;
    }
	
	public Usuarios obtenerUsuarioId(int id_usuario) {    	
    	Usuarios usuario = null;
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
        	PreparedStatement pst = con.prepareStatement("select id_usuario, user_id, chat_id, aliastg, nombre, correo, telefono, idioma, pais, ciudad,"
        			+ "tipo, tipo_usuario, provincia, edad, observaciones, cont_mensajes, fecha_alta, fecha_modif, fecha_baja "
        			+ " FROM usuarios WHERE id_usuario = ?");        	
          	pst.setInt(1, id_usuario);  
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {       	        
            	usuario = new Usuarios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), 
      			      rs.getString(11), rs.getString(12), rs.getString(13), rs.getInt(14), rs.getString(15), rs.getInt(16), rs.getDate(17), rs.getDate(18),rs.getDate(19));
            }      
            rs.close();
            pst.close();
            con.close();
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            } finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }
           return usuario;
    }
	
	public Usuarios obtenerUsuarioAlias(String aliastg) {    	
    	Usuarios usuario = null;
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
        	PreparedStatement pst = con.prepareStatement("select id_usuario, user_id, chat_id, aliastg, nombre, correo, telefono, idioma, pais, ciudad, "
        			+ " tipo, tipo_usuario, provincia, edad, observaciones, cont_mensajes, fecha_alta, fecha_modif, fecha_baja "
        			+ " FROM usuarios WHERE aliastg = ?");        	
          	pst.setString(1, aliastg);  
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {       	        
            	usuario = new Usuarios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), 
        			      rs.getString(11), rs.getString(12), rs.getString(13), rs.getInt(14), rs.getString(15), rs.getInt(16), rs.getDate(17), rs.getDate(18),rs.getDate(19));
            }      
            rs.close();
            pst.close();
            con.close();
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            } finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }
           return usuario;
    }
	
	 public List<Usuarios> obtenerUsuarios() {    	
		    Usuarios usuario = null;
	    	List<Usuarios> usuarios = new ArrayList<Usuarios>();
	    	Connection con = null;
	    	try {    			        	
	    		con = db.connectMySQL();
	        	PreparedStatement pst = con.prepareStatement("select id_usuario, user_id, chat_id, aliastg, nombre, correo, telefono, idioma, pais, ciudad, "
	        			+ " tipo, tipo_usuario, provincia, edad, observaciones, cont_mensajes, fecha_alta, fecha_modif, fecha_baja"
	        			+ " FROM usuarios "
	        			+ "WHERE fecha_baja is null order by id_usuario");
	          	ResultSet rs = pst.executeQuery();                	
	            while (rs.next()) {        	        
	            	usuario = new Usuarios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), 
	        			      rs.getString(11), rs.getString(12), rs.getString(13), rs.getInt(14), rs.getString(15), rs.getInt(16), rs.getDate(17), rs.getDate(18),rs.getDate(19));
	                usuarios.add(usuario);
	            }      
	            rs.close();
	            pst.close();
	            } catch (SQLException ex) {
	        	 System.out.println("Error SQL: " + ex);	                 
	            } finally{
	                if(con!=null)
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	             }
	           return usuarios;
	    } 
	
	 
	 public List<Usuarios> obtenerUsuariosPropietarios(int id_local) {    	
		    Usuarios usuario = null;
	    	List<Usuarios> usuarios = new ArrayList<Usuarios>();
		  
		  	Connection con = null;
		  	try {    			        	
		  		con = db.connectMySQL();
		  		PreparedStatement pst = con.prepareStatement("select u.id_usuario, u.user_id, u.chat_id, u.aliastg, u.nombre, u.correo, u.telefono, u.idioma, u.pais, u.ciudad,"
		  				+ " u.tipo, u.tipo_usuario, u.provincia, u.edad, u.observaciones, u.cont_mensajes, u.fecha_alta, u.fecha_modif, u.fecha_baja"
		  				+ " from localUsuarios lu, usuarios u where lu.id_local = ? and lu.tipo_usuario = 'PR' and lu.fecha_baja is null and lu.id_usuario = u.id_usuario order by u.user_id ");
		  		pst.setInt(1, id_local);
		        ResultSet rs = pst.executeQuery();                	
		        while (rs.next()) {        	        
		        	  usuario = new Usuarios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), 
	        			      rs.getString(11), rs.getString(12), rs.getString(13), rs.getInt(14), rs.getString(15), rs.getInt(16), rs.getDate(17), rs.getDate(18),rs.getDate(19));
	                  usuarios.add(usuario);
		        }      
		          rs.close();
		          pst.close(); 
		          } catch (SQLException ex) {
		      	     System.out.println("Error SQL: " + ex);	                 
		          } finally{
		              if(con!=null)
							try {
								con.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		           }
		  	   return usuarios;
		  }
	
	 public void actuIdioma(long user_id, int id_usuario, String idioma) {
		    Connection con = null;  
	    	try {    		
	    		con = db.connectMySQL();
	    		String query = "Update usuarios set idioma = ? where id_usuario= ?";	    		    	   	    	      
	        	PreparedStatement pst = con.prepareStatement(query);         		          
	          	pst.setString(1, idioma);	          	
	          	pst.setInt(2, id_usuario);
	          	pst.execute();                	            
	            pst.close();            
	            } catch (SQLException ex) {
	        	 System.out.println("Error SQL: " + ex);	                 
	            } finally{
	                if(con!=null)
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	             }
	    }
	 
	 public void actuUbicacion(long user_id, int id_usuario, String pais, String ciudad) {
		    Connection con = null;  
	    	try {    		
	    		con = db.connectMySQL();
	    		String query = "Update usuarios set pais = ?, ciudad = ? where id_usuario= ?";	    		    	   	    	      
	        	PreparedStatement pst = con.prepareStatement(query);         		          
	          	pst.setString(1, pais);
	          	pst.setString(2, ciudad);
	          	pst.setInt(3, id_usuario);
	          	pst.execute();                	            
	            pst.close();            
	            } catch (SQLException ex) {
	        	 System.out.println("Error SQL: " + ex);	                 
	            } finally{
	                if(con!=null)
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	             }
	    }
	 
	 public void actuContador(long user_id, int id_usuario, String nombre, String first_username) {
		    Connection con = null;  
	    	try {    		
	    		con = db.connectMySQL();
	    		String query = "Update usuarios set cont_mensajes = cont_mensajes + 1, user_id = ?, chat_id = ?, aliastg = ?, nombre = ? where id_usuario= ?";	    		    	   	    	      
	        	PreparedStatement pst = con.prepareStatement(query);         		          
	          	pst.setLong(1, user_id);
	          	pst.setLong(2, user_id);
	          	pst.setString(3, nombre);
	          	pst.setString(4, first_username);
	          	pst.setInt(5, id_usuario);
	          	pst.execute();                	            
	            pst.close();            
	            } catch (SQLException ex) {
	        	 System.out.println("Error SQL: " + ex);	                 
	            } finally{
	                if(con!=null)
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	             }
	    }
	 
	 public void actuTipo(String tipo, int id_usuario) {
		    Connection con = null;  
		    if (tipo.equals("STARTERS")) tipo="ENTRANTES";
	        if (tipo.equals("MAIN DISHES")) tipo="PRINCIPALES";
	        if (tipo.equals("DESSERTS")) tipo="POSTRES";
	        if (tipo.equals("DRINKS")) tipo="BEBIDAS";
	    	try {    		
	    		con = db.connectMySQL();
	    		String query = "Update usuarios set tipo = ? where id_usuario= ?";	    		    	   	    	      
	        	PreparedStatement pst = con.prepareStatement(query);         		          
	          	pst.setString(1, tipo);	          	
	          	pst.setInt(2, id_usuario);
	          	pst.execute();                	            
	            pst.close();            
	            } catch (SQLException ex) {
	        	 System.out.println("Error SQL: " + ex);	                 
	            } finally{
	                if(con!=null)
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	             }
	    }
	 
	 public int bajaUsuario(int id_usuario) {
		    Connection con = null;  
		    int numRow = 0;
	    	try {    		
	    		con = db.connectMySQL();
	    		String query = "Update usuarios set fecha_baja = NOW() where id_usuario = ?";	    		    	   	    	      
	        	PreparedStatement pst = con.prepareStatement(query);         		          	          	         
	          	pst.setInt(1, id_usuario);
	          	numRow = pst.executeUpdate();               	            
	            pst.close();            
	            } catch (SQLException ex) {
	        	 System.out.println("Error SQL: " + ex);	                 
	            } finally{
	                if(con!=null)
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	             }
	    	return numRow;
	    }
	
	 public Usuarios altaUsuario(long user_id, long chat_id, String nombre, String first_username) {	    	
		    Usuarios usuario = null;
	    	Connection con = null;
	    	try {    		
	    		con = db.connectMySQL();	    		                           
	    		String query = " insert into usuarios (user_id, chat_id, aliastg, nombre, correo, telefono, idioma, pais, ciudad, tipo, tipo_usuario, provincia, edad, observaciones, cont_mensajes, fecha_alta, fecha_modif, fecha_baja)"
	    			           + " values (?, ?, ?, ?, null, null, 'ESPAÑOL', null, null, 'N', 'CL', null, null, null, 1, ?, null, null)";
	    		Calendar calendar = Calendar.getInstance();
	    	    java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
	    	      
	        	
	        	PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	        			
	          	pst.setLong(1, user_id);
	          	pst.setLong(2, chat_id);
	          	pst.setString(3, nombre);
	          	pst.setString(4, first_username);
	          	//pst.setString(5, correo);
	          	//pst.setString(6, telefono);	          	
	          	pst.setDate(5, startDate);
	          	pst.execute();                	  
	          	ResultSet rs = pst.getGeneratedKeys();
	            if (rs.next()) {       	     
	          	  usuario = new Usuarios(rs.getInt(1), (int)user_id, (int)chat_id, nombre, first_username, "", "", null, "", "", "N", "", "", 0, "", 1, startDate, null, null);
	            }
	            //pk = rs.next() ? rs.getInt(1) : 0;
	            pst.close();
	            System.out.println("Alta del usuario: " + nombre);
	            } catch (SQLException ex) {
	        	 System.out.println("Error SQL: " + ex);	                 
	            } finally{
	                if(con!=null)
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	             }           
	    	return usuario;
	    }
}