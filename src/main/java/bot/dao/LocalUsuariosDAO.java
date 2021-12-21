package bot.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import bot.database;
import bot.bo.LocalUsuarios;
import bot.bo.Locales;

public class LocalUsuariosDAO {
	
	database db = new database();
	
  public int altaLocalUsuario(int id_local,int id_usuario) {     	    
     	
    	Connection con = null;
    	int pk = 0;
    	try {    	
    		con = db.connectMySQL(); 
    		String query = " insert into localUsuarios (id_local, id_usuario, cont_mensajes, tipo_usuario, fecha_alta)"
    			           + " values (?,?,1,'CL',?)";
    		Calendar calendar = Calendar.getInstance();
    	    java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	      
        	PreparedStatement pst2 = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); 
          	pst2.setInt(1, id_local);          	
          	pst2.setInt(2, id_usuario);
          	pst2.setDate(3, startDate);
          	pst2.execute();                	            
            ResultSet rs = pst2.getGeneratedKeys();  
            pk = rs.next() ? rs.getInt(1) : 0;
            pst2.close();
            
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
    	    return pk;
    }
  
  public LocalUsuarios obtenerLocalUsuarioId(int id_local,int id_usuario) {    	
  	LocalUsuarios localUsuario = null;
  	Connection con = null;
  	try {    		
  		con = db.connectMySQL();
  		
  		PreparedStatement pst = con.prepareStatement("select id_localUsuario, id_local, id_usuario, cont_mensajes, tipo, tipo_usuario, "
  				+ " fecha_alta, fecha_modif, fecha_baja from localUsuarios where id_local = ? and id_usuario = ? ");
        	pst.setInt(1, id_local);  
        	pst.setInt(2, id_usuario);  
        	ResultSet rs = pst.executeQuery();                	
          while (rs.next()) {       	        
        	  localUsuario = new LocalUsuarios(rs.getInt(1),  rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6),
          			rs.getDate(7), rs.getDate(8),rs.getDate(9));
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
         return localUsuario;
  }
 
  
  public List<LocalUsuarios> obtenerUsuariosLocal(int id_local) {    	
	  LocalUsuarios localUsuario = null;
  	List<LocalUsuarios> usuariosLocal = new ArrayList<LocalUsuarios>();
  	Connection con = null;
  	try {    			        	
  		con = db.connectMySQL();
  		PreparedStatement pst = con.prepareStatement("select id_localUsuario, id_local, id_usuario, cont_mensajes, tipo, tipo_usuario, "
  				+ " fecha_alta, fecha_modif, fecha_baja from localUsuarios where id_local = ? and fecha_baja is null order by id_usuario ");
  		pst.setInt(1, id_local);
  		ResultSet rs = pst.executeQuery();                	
          while (rs.next()) {        	        
        	  localUsuario = new LocalUsuarios(rs.getInt(1),  rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6),
            			rs.getDate(7), rs.getDate(8),rs.getDate(9));
        	  usuariosLocal.add(localUsuario);
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
         return usuariosLocal;
  }
  
  public List<String> obtenerTiposUsuario(int id_usuario) {    	
	String tipoUsuario = null;
  	List<String> listaTipos = new ArrayList<>();
  	Connection con = null;
  	try {    			        	
  		con = db.connectMySQL();
  		PreparedStatement pst = con.prepareStatement("select distinct tipo_usuario "
  				+ " from localUsuarios where id_usuario = ? and fecha_baja is null ");
  		pst.setInt(1, id_usuario);
        ResultSet rs = pst.executeQuery();                	
          while (rs.next()) {        	        
        	  tipoUsuario = rs.getString(1);
        	  listaTipos.add(tipoUsuario);
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
         return listaTipos;
  }
  
  public List<String> obtenerIdUserPropietarios(int id_local) {    	
		int user_id = 0;
	  	List<String> lista = new ArrayList<>();
	  	Connection con = null;
	  	try {    			        	
	  		con = db.connectMySQL();
	  		PreparedStatement pst = con.prepareStatement("select u.user_id from localUsuarios lu, usuarios u where lu.id_local = ? and lu.tipo_usuario = 'PR' and lu.fecha_baja is null and lu.id_usuario = u.id_usuario order by u.user_id ");
	  		pst.setInt(1, id_local);
	        ResultSet rs = pst.executeQuery();                	
	          while (rs.next()) {        	        
	        	  user_id = rs.getInt(1);
	        	  lista.add(String.valueOf(user_id));
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
	         return lista;
	  }
  
  

  public void actuContador(int id_local, int id_usuario) {
	    Connection con = null;  
  	try {    		
  		con = db.connectMySQL();
  		String query = "Update localUsuarios set cont_mensajes = cont_mensajes + 1 where id_local = ? and id_usuario= ?";	    		    	   	    	      
      	PreparedStatement pst = con.prepareStatement(query);         		          
      	pst.setInt(1, id_local);  
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
  
  public int bajaUsuarioLocal(int id_usuario) {
	Connection con = null;  
	int numRow = 0;
  	try {    		
  		con = db.connectMySQL();
  		String query = "Update localUsuarios set fecha_baja = NOW() where id_usuario = ?";	    		    	   	    	      
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

}
