package bot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bot.database;
import bot.bo.Locales;
import bot.bo.Usuarios;

public class LocalesDAO {
	database db = new database();
	
	public Locales obtenerLocalId(int id_local) {    	
    	Locales local = null;
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
    		
    		PreparedStatement pst = con.prepareStatement("select id_local, nombre, descripcion, tipo_local, plan, idioma, "
    				+ "pais, ciudad, moneda, impuesto, fecha_alta, fecha_modif, fecha_baja, direccion, datosEnvio, datosConfirPago, porcenImpuesto, datosSupleEnvio, impSupleEnvio from locales where id_local = ?");
          	pst.setInt(1, id_local);  
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {       	        
            	local = new Locales(rs.getInt(1),  rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
            			rs.getString(7), rs.getString(8),  rs.getString(9), rs.getString(10),rs.getDate(11), rs.getDate(12),rs.getDate(13), rs.getString(14), rs.getString(15), 
            			rs.getString(16), rs.getBigDecimal(17),rs.getString(18), rs.getBigDecimal(19));
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
           return local;
    }
	
	public Locales obtenerLocalNombre(String nombre) {    	
    	Locales local = null;
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
    		
    		PreparedStatement pst = con.prepareStatement("select id_local, nombre, descripcion, tipo_local, plan, idioma, "
    				+ "pais, ciudad, moneda, impuesto, fecha_alta, fecha_modif, fecha_baja, direccion, datosEnvio, datosConfirPago, porcenImpuesto, datosSupleEnvio, impSupleEnvio from locales where nombre = ? and fecha_baja is null");
          	pst.setString(1, nombre);  
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {       	        
            	local = new Locales(rs.getInt(1),  rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
            			rs.getString(7), rs.getString(8),  rs.getString(9), rs.getString(10),rs.getDate(11), rs.getDate(12),rs.getDate(13), rs.getString(14), rs.getString(15),
            			rs.getString(16),rs.getBigDecimal(17),rs.getString(18), rs.getBigDecimal(19));
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
           return local;
    }
	
	public List<Locales> obtenerLocales() {    	
	    Locales local = null;
    	List<Locales> locales = new ArrayList<Locales>();
    	Connection con = null;
    	try {    			        	
    		con = db.connectMySQL();
    		PreparedStatement pst = con.prepareStatement("select id_local, nombre, descripcion, tipo_local, plan, idioma, "
    				+ "pais, ciudad, moneda, impuesto, fecha_alta, fecha_modif, fecha_baja, direccion, datosEnvio, datosConfirPago, porcenImpuesto, datosSupleEnvio, impSupleEnvio from locales "
    				+ "where fecha_baja is null order by tipo_local, pais, ciudad, id_local");
     
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {        	        
            	local = new Locales(rs.getInt(1),  rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
            			rs.getString(7), rs.getString(8),  rs.getString(9), rs.getString(10),rs.getDate(11), rs.getDate(12),rs.getDate(13), rs.getString(14), rs.getString(15),
            			rs.getString(16), rs.getBigDecimal(17),rs.getString(18), rs.getBigDecimal(19));
                locales.add(local);
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
           return locales;
    }
	
	public List<Locales> obtenerLocales(String pais, String ciudad) {    	
	    Locales local = null;
    	List<Locales> locales = new ArrayList<Locales>();
    	Connection con = null;
    	try {    			        	
    		con = db.connectMySQL();
    		PreparedStatement pst = con.prepareStatement("select id_local, nombre, descripcion, tipo_local, plan, idioma, "
    				+ "pais, ciudad, moneda, impuesto, fecha_alta, fecha_modif, fecha_baja, direccion, datosEnvio, datosConfirPago, porcenImpuesto, datosSupleEnvio, impSupleEnvio from locales "
    				+ "where pais LIKE ? and ciudad LIKE ? and fecha_baja is null  order by tipo_local, pais, ciudad, id_local");
    	 	pst.setString(1, '%' + pais + '%');      
    	 	pst.setString(2, '%' + ciudad + '%');
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {        	        
            	local = new Locales(rs.getInt(1),  rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
            			rs.getString(7), rs.getString(8),  rs.getString(9), rs.getString(10),rs.getDate(11), rs.getDate(12),rs.getDate(13), rs.getString(14), rs.getString(15),
            			rs.getString(16), rs.getBigDecimal(17),rs.getString(18), rs.getBigDecimal(19));
                locales.add(local);
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
           return locales;
    }
	
	public List<String> obtenerPaises(Usuarios usuario) {    	
	    String pais = null;
    	List<String> paises = new ArrayList<String>();
    	Connection con = null;
    	try {    			        	
    		con = db.connectMySQL();
    		 PreparedStatement pst = null;
    		if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH") ) {
    		  pst = con.prepareStatement("select distinct substring(pais,1,instr(pais,'-')-1) pais from locales order by pais");
    		} else {
    		  pst = con.prepareStatement("select distinct substring(pais,instr(pais,'-')+1, length(pais)) pais from locales order by pais");
    		}
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {        	        
            	pais = rs.getString(1);
                paises.add(pais);
            }      
            if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH") ) {
            	paises.add("NINGUNO");	
            } else {
            	paises.add("NONE");	
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
           return paises;
    }
	public List<String> obtenerCiudades(String pais, Usuarios usuario) {    	
	    String ciudad = null;
    	List<String> ciudades = new ArrayList<String>();
    	Connection con = null;
    	try {    			        	
    		con = db.connectMySQL();
    		PreparedStatement pst = null;
    		if (usuario.getIdioma().equals("ESPAÑOL") || usuario.getIdioma().equals("SPANISH") ) {
    		  pst = con.prepareStatement("select distinct substring(ciudad,1,instr(ciudad,'-')-1) ciudad from locales where pais LIKE ? order by ciudad ");
    		} else {
    		  pst = con.prepareStatement("select distinct substring(ciudad,instr(ciudad,'-')+1, length(ciudad)) ciudad from locales where pais LIKE ? order by ciudad ");
    		}
    		pst.setString(1, '%' + pais + '%');    
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {        	        
            	ciudad = rs.getString(1);
                ciudades.add(ciudad);
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
           return ciudades;
    }

}
