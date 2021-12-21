package bot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bot.database;
import bot.bo.CuponesAnuncio;


public class CuponDAO {
	
    database db = new database();
	
	public CuponDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int altaCuponAnuncio(long user_id, CuponesAnuncio cupon) {     	    
     	
    	Connection con = null;
    	int pk = 0;
    	try {    	
    		con = db.connectMySQL(); 
    		
    		//  id_pedido, user_id, id_anuncio, estado, id_stripe, payload, precio, ciudad, cod_pais, cod_postal, estadoDir, direccion1, direccion2, nombre_ped, mail_ped, telefono_ped, fecha_alta, fecha_modif, fecha_baja
    		
    		String query = " insert into cuponesAnuncio(id_anuncio, textocupon, estado, porcentaje, fecha_alta)"
    			           + " values (?, ?, 'V', ?, ?)";
    		Calendar calendar = Calendar.getInstance();
    	    java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	      
        	PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);         
          	pst.setInt(1, cupon.getId_anuncio());
          	pst.setString(2, cupon.getTextocupon());
          	pst.setInt(3, cupon.getPorcentaje());
          	pst.setDate(4, startDate);
          	pst.execute();                	            
            
            System.out.println("Alta del cupón anuncio!! ");
            ResultSet rs = pst.getGeneratedKeys();  
            pk = rs.next() ? rs.getInt(1) : 0;
            pst.close();
            if(pk!=0){
                System.out.println("Generated key="+pk);
            }
            
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
	
	
	public CuponesAnuncio obtenerCuponAnuncio(int id_cupon) {    	
    	CuponesAnuncio cupon = null;       
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
        	PreparedStatement pst = con.prepareStatement("select id_cuponAnuncio, id_anuncio, estado, textocupon,  porcentaje, fecha_alta, fecha_modif, fecha_baja "
        			+ " FROM cuponesAnuncio WHERE id_cuponAnuncio = ?");        	
          	pst.setInt(1, id_cupon);  
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {       	        
            	cupon = new CuponesAnuncio(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), 
            			rs.getInt(5), rs.getDate(6), rs.getDate(7), rs.getDate(8));
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
           return cupon;
    }
	
	 public List<CuponesAnuncio> obtenerCuponesAnuncio(String estado) {    	
		    CuponesAnuncio cupon = null;
	    	List<CuponesAnuncio> cupones = new ArrayList<CuponesAnuncio>();	        
	    	Connection con = db.connectMySQL();
	    	try {  
	    		PreparedStatement pst = null; 
	    		String query = "select id_cuponAnuncio, id_anuncio,  estado, textocupon, porcentaje, fecha_alta, fecha_modif, fecha_baja "
	        			+ " FROM cuponesAnuncio ";
	    		if (!estado.equals("TODOS")) {
	    		  query = query + "WHERE estado = ? ";
	    		}
	    		query = query + " order by id_cuponAnuncio";
	    		pst = con.prepareStatement(query);
	    		if (!estado.equals("TODOS")) {
		        	  pst.setString(1, estado);
		    		}
	          	ResultSet rs = pst.executeQuery();                	
	            while (rs.next()) {        	        
	            	cupon = new CuponesAnuncio(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), 
	            			rs.getInt(5), rs.getDate(6), rs.getDate(7), rs.getDate(8));
	                cupones.add(cupon);
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
	           return cupones;
	    } 
	 
	 public void actualizarEstadoCuponAnuncio(String estado, int id_cupon) {
	    	Connection con = null;
	    	try {    		
	    		con = db.connectMySQL();
	    		
	    		String query = "Update cuponesAnuncio set estado = ?, fecha_modif = SYSDATE() where id_cuponAnuncio = ? ";
	    		
	    		PreparedStatement pst = con.prepareStatement(query);
	    		pst.setString(1, estado);
	          	pst.setInt(2, id_cupon);
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
	 
	 
	 public int borrarCuponAnuncio(int id_cupon) {
	    	Connection con = null;
	    	int numRow = 0;
	    	try {    		
	    		con = db.connectMySQL();
	    		
	    		String query = "delete from cuponesAnuncio where id_cuponAnuncio = ? ";
	    		
	    		PreparedStatement pst = con.prepareStatement(query);
	          	pst.setInt(1, id_cupon);
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
