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
import java.util.Properties;

import javax.servlet.http.HttpSession;

import bot.database;
import bot.bo.Anuncios;
import bot.bo.Usuarios;
import res.resourceLoader;

public class AnuncioDAO {
	
	database db = new database();
	Properties prop = resourceLoader.loadProperties("configuracion.properties");
	
	public int altaAnuncio(int id_usuario, int id_local) {     	    
     	
    	Connection con = null;
    	int pk = 0;
    	try {    	
    		con = db.connectMySQL(); 
    		String query = " insert into anuncios (id_usuario, id_local, validado, precio, unidades, fecha_alta)"
    			           + " values (?,?,'N',0, 1, ?)";
    		Calendar calendar = Calendar.getInstance();
    	    java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	      
        	PreparedStatement pst2 = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); 
          	pst2.setInt(1, id_usuario); 
          	pst2.setInt(2, id_local); 
          	pst2.setDate(3, startDate);
          	pst2.execute();                	            
            
            System.out.println("Alta de anuncio!! ");
            ResultSet rs = pst2.getGeneratedKeys();  
            pk = rs.next() ? rs.getInt(1) : 0;
            pst2.close();
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
    
    public void actualizarFlagAnuncio(String flag, int id_usuario, int id_anuncio) {
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
    		
    		String query = "Update anuncios set id_usuario = ?, flag_campo = '" + flag + "' where id_anuncio = ? ";
    		
    		PreparedStatement pst = con.prepareStatement(query);         	
    		pst.setInt(1, id_usuario);
          	pst.setInt(2, id_anuncio);
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
    
    public void actualizarCateAnuncio(String categoria, int id_anuncio) {
    	Connection con = null;
    	if (categoria.equals("STARTERS")) categoria="ENTRANTES";
        if (categoria.equals("MAIN DISHES")) categoria="PRINCIPALES";
        if (categoria.equals("DESSERTS")) categoria="POSTRES";
        if (categoria.equals("DRINKS")) categoria="BEBIDAS";
    	try {    		
    		con = db.connectMySQL();
    		
    		String query = "Update anuncios set categoria = ? where id_anuncio = ? ";
    		
    		PreparedStatement pst = con.prepareStatement(query);
    		pst.setString(1, categoria);
          	pst.setInt(2, id_anuncio);
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
    
    public void actualizarUnidadesAnuncio(int id_anuncio, int unidades) {
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
    		
    		String query = "Update anuncios set unidades = unidades - ? where id_anuncio = ? ";
    		
    		PreparedStatement pst = con.prepareStatement(query);
          	pst.setInt(1, unidades);
          	pst.setInt(2, id_anuncio);
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
    
    public void reponerUnidadesAnuncio(int id_anuncio, int unidades) {
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
    		
    		String query = "Update anuncios set unidades = unidades + ? where id_anuncio = ? ";
    		
    		PreparedStatement pst = con.prepareStatement(query);
          	pst.setInt(1, unidades);
          	pst.setInt(2, id_anuncio);
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
    
    public Anuncios anuncioAdm(int id_anuncio) {
    	Anuncios anuncio = null;    	
        Connection con = null;
    	try {
    		con = db.connectMySQL();
        	PreparedStatement pst = con.prepareStatement("SELECT id_anuncio, id_usuario, id_local, FLAG_CAMPO,  VALIDADO,  TITULO,  DESCRIPCION,  CATEGORIA,  CONTACTO, DIRECCION, FOTO, ID_USU_DENUNCIA, VERSION_FOTO, FECHA_ALTA,  FECHA_MODIF,  FECHA_BAJA, PRECIO, UNIDADES  FROM anuncios where id_anuncio = ? ");        	
          	pst.setInt(1, id_anuncio);            	
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {        	        		
            	anuncio = new Anuncios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),rs.getString(6), rs.getString(7), rs.getString(8), 
            			rs.getString(9), rs.getString(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getDate(14), rs.getDate(15), rs.getDate(16),
            			rs.getBigDecimal(17), rs.getInt(18));
            }      
            rs.close();
            pst.close();
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             } 
           return anuncio;
    }
    
    public Anuncios anuncioPendiente(int id_usuario) {
    	Anuncios anuncio = null;    	
        Connection con = null;
    	try {
    		con = db.connectMySQL();
        	PreparedStatement pst = con.prepareStatement("SELECT id_anuncio, id_usuario, id_local, FLAG_CAMPO,  VALIDADO,  TITULO,  DESCRIPCION,  CATEGORIA,  CONTACTO, DIRECCION, FOTO, ID_USU_DENUNCIA, VERSION_FOTO, FECHA_ALTA,  FECHA_MODIF,  FECHA_BAJA, PRECIO, UNIDADES  FROM anuncios where id_usuario = ? and flag_campo is not null  and FECHA_BAJA is null ");        	
          	pst.setInt(1, id_usuario);            	
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {        	        		
            	anuncio = new Anuncios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),rs.getString(6), rs.getString(7), rs.getString(8), 
            			rs.getString(9), rs.getString(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getDate(14), rs.getDate(15), rs.getDate(16),
            			rs.getBigDecimal(17), rs.getInt(18));
            }      
            rs.close();
            pst.close();
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             } 
           return anuncio;
    }
    
    public Anuncios actuAnuncio(String texto, int pk) throws SQLException, ErrorRangoPrecio, ErrorRangoUnidades, NumberFormatException {
    	Connection con = null;
    	Anuncios anuncio = null;    	
    	try {       		
    		con = db.connectMySQL();    		
    		PreparedStatement pst = con.prepareStatement("SELECT id_anuncio, id_usuario, id_local, FLAG_CAMPO,  VALIDADO,  TITULO,  DESCRIPCION,  CATEGORIA,  CONTACTO, DIRECCION, FOTO, ID_USU_DENUNCIA, VERSION_FOTO, FECHA_ALTA,  FECHA_MODIF,  FECHA_BAJA, PRECIO, UNIDADES"
    				+ " from anuncios where id_anuncio = ? ");
    		pst.setInt(1, pk);
    		ResultSet rs = pst.executeQuery();
    		if (rs.next()) {           	    			    	
    			anuncio = new Anuncios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),rs.getString(6), rs.getString(7), rs.getString(8), 
            			rs.getString(9), rs.getString(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getDate(14), rs.getDate(15), rs.getDate(16),
            			rs.getBigDecimal(17), rs.getInt(18));
    		} 
    		rs.close();
            pst.close();

            if (anuncio.getFlag_campo() != null) {    
    		  String query = "Update anuncios SET " + anuncio.getFlag_campo() + " = ?, FECHA_MODIF = SYSDATE(), FLAG_CAMPO = NULL ";    		
    		  if (anuncio.getFlag_campo().equals("FOTO")) {
            		query = query + " ,VERSION_FOTO = VERSION_FOTO + 1 ";
            	  }    		  
      		  query = query + " where id_anuncio = ?  ";    
    		  PreparedStatement pst2 = con.prepareStatement(query);         
    		  if (anuncio.getFlag_campo().equals("PRECIO")) {
    			BigDecimal bigD = new BigDecimal(texto);
    			BigDecimal min =  new BigDecimal(prop.getProperty("var.limMin"));
    			BigDecimal max =  new BigDecimal(prop.getProperty("var.limMax"));
    			if (bigD.compareTo(BigDecimal.ZERO) != 0 && bigD.compareTo(min)==-1 || bigD.compareTo(max)==1) {
    				throw new ErrorRangoPrecio();
    			}
    			pst2.setBigDecimal(1, bigD);  
    		  } else if (anuncio.getFlag_campo().equals("UNIDADES")) {
    			  int unidades = Integer.parseInt(texto);
    			  if (unidades<0) {
      				throw new ErrorRangoUnidades();
      		   	  }
    			  pst2.setInt(1, unidades);   
    		  } else {
    			  pst2.setString(1, texto);   
    		  }    	 
          	  pst2.setLong(2, pk);
          	  pst2.execute();                	            
              pst2.close();
            }
            } finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }
    	 return anuncio;    	
    }
    
    public void finAnuncio(int id_anuncio) {
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
    		String query = "Update anuncios set flag_campo = null, validado = 'S', fecha_modif = SYSDATE(), fecha_baja = null  where id_anuncio = ? ";
    		
    		PreparedStatement pst = con.prepareStatement(query);         		                    	          	
          	pst.setInt(1, id_anuncio);
          	pst.execute();                	            
            pst.close();            
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }  finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }   	    
    }
    
    public int obligatorioInformado(int id_anuncio) {
    	Connection con = null;
    	String query = null;
    	PreparedStatement pst = null; 
    	int res = 0;
    	try {    		    
    		con = db.connectMySQL();
    		query = "SELECT TITULO, CATEGORIA FROM anuncios where id_anuncio = ? "; 
    		pst = con.prepareStatement(query);
    		pst.setInt(1, id_anuncio);
    		ResultSet rs = pst.executeQuery();    		
    	    if (rs.next())
    	    {
    	    	if (rs.getString(1)!=null && rs.getString(2)!=null) {
    	    		res = 1;
    	    	} else {
    	    		res = 0;
    	    	}
    	    }
    	    rs.close();                  	            
            pst.close();           
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }  finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }   	    
    	    return res;
    }
    
    public int validarAnuncio(int id_anuncio) {
    	Connection con = null;
    	String query = null;
    	PreparedStatement pst = null; 
    	int res = 0;
    	try {    		    
    		con = db.connectMySQL();
    		query = "SELECT id_anuncio FROM anuncios where id_anuncio = ?  and FECHA_BAJA is null order by id"; 
    		pst = con.prepareStatement(query);
    		pst.setInt(1, id_anuncio);
    		ResultSet rs = pst.executeQuery();    		
    	    if (!rs.next())
    	    {
    	    	res = 0;
    	    } else {
    	    	query = "Update anuncios set validado = 'S' where id_anuncio = ? and FECHA_BAJA is null ";        		
        		pst = con.prepareStatement(query);         		                    	          	
              	pst.setInt(1, id_anuncio);
              	pst.execute();
              	res = 1;
    	    }
    	    rs.close();                  	            
            pst.close();           
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }  finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }   	    
    	    return res;
    }
    
    public int validarMaxAnuncios(int id_local) {
    	Connection con = null;
    	String query = null;
    	PreparedStatement pst = null; 
    	int res = 0;
    	try {    		    
    		con = db.connectMySQL();
    		query = "SELECT count(*) AS total FROM anuncios where FECHA_BAJA is null and id_local = ? and validado = 'S' "; 
    		pst = con.prepareStatement(query);
    		pst.setInt(1, id_local);
    		ResultSet rs = pst.executeQuery();    
    		while(rs.next()){
    	        res = rs.getInt("total");
    	    }
    	    rs.close();                  	            
            pst.close();           
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }  finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             }   	    
    	    return res;
    }
    
 public Anuncios leerAnuncio(int id_anuncio, String condicion, int id_usuario, String texto, int id_local) {    	
	    // Condicion:
	    // A: Para que el Administrador busque cualquier anuncio por id. Sin mirar si esta publicado o si esta de baja 
	    // P: Para anunios pendientes de publicar
	    // B: Para buscar el id menor de los anuncio publicados a partir de un id dado
	    // V: Para buscar un anuncio concreto publicado
	    // N: Para anuncios sin Validar pero no dados de baja
		Anuncios anuncio = null;		
    	List<Anuncios> anuncios = new ArrayList<Anuncios>();
        Connection con = null;
        if (condicion.equals("STARTERS")) condicion="ENTRANTES";
        if (condicion.equals("MAIN DISHES")) condicion="PRINCIPALES";
        if (condicion.equals("DESSERTS")) condicion="POSTRES";
        if (condicion.equals("DRINKS")) condicion="BEBIDAS";
        int i = 1;
    	try {    		
    		con = db.connectMySQL();
    		String query = "select id_anuncio, id_usuario, id_local, FLAG_CAMPO,  VALIDADO,  "
        			+ " TITULO,  DESCRIPCION,  CATEGORIA,  CONTACTO, DIRECCION, FOTO, ID_USU_DENUNCIA, VERSION_FOTO, "
        			+ "  FECHA_ALTA        ,  FECHA_MODIF       ,  FECHA_BAJA, PRECIO, UNIDADES  "
        			+ "from anuncios where id_local=? ";
    		PreparedStatement pst = null; 
    		if (!condicion.equals("A")) {
    			query = query + " AND FECHA_BAJA is null ";
    		}
        	if (id_anuncio!= 0) {
        		if (condicion.equals("A")) {
        			query = query + " AND id_anuncio = ? ";       	
        		} else if (condicion.equals("V"))  {
        			query = query + " AND id_anuncio = ? ";
        		} else {
        			query = query + "AND id_anuncio >= ? ";
        		}
        	}
        	if (condicion.equals("N")|| condicion.equals("P")) {
      		  query = query + " AND validado = 'N'  ";	
      	    } else {
      	    	if (!condicion.equals("A")  ) {
       	          query = query + " AND validado = 'S'  ";
        	    }
      	    }
      	    	
//        	if (condicion.equals("P")) {
//        		  query = query + "AND id_usuario = ? ";	
//        	}
        	if (condicion.length()>2) {
      		  query = query + "AND CATEGORIA = ? ";	
      	    }
        	if (condicion.equals("T")) {
      		  query = query + " AND TITULO LIKE ? ";	
      	    }
        	query = query + " order by id_anuncio asc limit 1";
        	//
        	pst = con.prepareStatement(query);
        	pst.setInt(i++, id_local);
        	if (id_anuncio!= 0) {
        		pst.setInt(i++, id_anuncio);
        	}
        	if (condicion.length()>2) {
        		pst.setString(i++, condicion);	        	        		
        	}
        	if (condicion.equals("T")) {
        		pst.setString(i++, "%" + texto + "%");	 	
        	}        		        	          	 
          	ResultSet rs = pst.executeQuery();                	
            if (rs.next()) {
            	anuncio = new Anuncios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),rs.getString(6), rs.getString(7), rs.getString(8), 
            			rs.getString(9), rs.getString(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getDate(14), rs.getDate(15), rs.getDate(16),
            			rs.getBigDecimal(17), rs.getInt(18));
            	anuncios.add(anuncio);
            }      
            rs.close();
            pst.close();
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             } 
           return anuncio;
    }
    
 public List<Anuncios> listAnuncios(String condicion, String texto, int id_local) {    	
	 
	Anuncios anuncio = null;		
 	List<Anuncios> anuncios = new ArrayList<Anuncios>();
    Connection con = null;
    if (condicion.equals("STARTERS")) condicion="ENTRANTES";
    if (condicion.equals("MAIN DISHES")) condicion="PRINCIPALES";
    if (condicion.equals("DESSERTS")) condicion="POSTRES";
    if (condicion.equals("DRINKS")) condicion="BEBIDAS";
    int i = 1;
 	try {    		
 		con = db.connectMySQL();
 		String query = "select id_anuncio, id_usuario, id_local, FLAG_CAMPO,  VALIDADO,  "
     			+ " TITULO,  DESCRIPCION,  CATEGORIA,  CONTACTO, DIRECCION, FOTO, ID_USU_DENUNCIA, VERSION_FOTO, "
     			+ "  FECHA_ALTA        ,  FECHA_MODIF       ,  FECHA_BAJA, PRECIO, UNIDADES  "
     			+ "from anuncios where id_local = ? and FECHA_BAJA is null AND validado = 'S' ";
 		PreparedStatement pst = null; 
 
     	if (condicion.length()>2) {
   		  query = query + "AND CATEGORIA = ? ";	
   	    }
     	if (condicion.equals("T")) {
   		  query = query + "AND TITULO LIKE ? ";	
   	    }
     	query = query + " order by CATEGORIA, id_anuncio ";
     	//
     	pst = con.prepareStatement(query);
     	pst.setInt(i++, id_local);
     	if (condicion.length()>2) {
     		pst.setString(i++, condicion);	        	        		
     	}
     	if (condicion.equals("T")) {
     		pst.setString(i++, "%" + texto + "%");	 	
     	}        		        	          	 
       	ResultSet rs = pst.executeQuery();                	
        while (rs.next()) {        	
        	anuncio = new Anuncios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),rs.getString(6), rs.getString(7), rs.getString(8), 
        			rs.getString(9), rs.getString(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getDate(14), rs.getDate(15), rs.getDate(16),
        			rs.getBigDecimal(17), rs.getInt(18));
         	anuncios.add(anuncio);
         }      
         rs.close();
         pst.close();
         } catch (SQLException ex) {
     	 System.out.println("Error SQL: " + ex);	                 
         }finally{
             if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
          } 
        return anuncios;
    }
 
   public List<Anuncios> listAnunciosNoPub(int id_local) {    	
	 
		Anuncios anuncio = null;		
	 	List<Anuncios> anuncios = new ArrayList<Anuncios>();
	    Connection con = null;
	    int i = 1;
	 	try {    		
	 		con = db.connectMySQL();
	 		String query = "select id_anuncio, id_usuario, id_local, FLAG_CAMPO,  VALIDADO,  "
	     			+ " TITULO,  DESCRIPCION,  CATEGORIA,  CONTACTO, DIRECCION, FOTO, ID_USU_DENUNCIA, VERSION_FOTO, "
	     			+ "  FECHA_ALTA        ,  FECHA_MODIF       ,  FECHA_BAJA, PRECIO, UNIDADES  "
	     			+ "from anuncios where id_local = ? and (FECHA_BAJA is not null OR validado = 'N') ";
	 		PreparedStatement pst = null; 
	     	query = query + " order by CATEGORIA, id_anuncio ";
	     	//
	     	pst = con.prepareStatement(query);
	     	pst.setInt(i++, id_local);    		        	          	 
	       	ResultSet rs = pst.executeQuery();                	
	        while (rs.next()) {        	
	        	anuncio = new Anuncios(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),rs.getString(6), rs.getString(7), rs.getString(8), 
	        			rs.getString(9), rs.getString(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getDate(14), rs.getDate(15), rs.getDate(16),
	        			rs.getBigDecimal(17), rs.getInt(18));
	         	anuncios.add(anuncio);
	         }      
	         rs.close();
	         pst.close();
	         } catch (SQLException ex) {
	     	 System.out.println("Error SQL: " + ex);	                 
	         }finally{
	             if(con!=null)
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	          } 
	        return anuncios;
	    }
 
 
    public int leerIdSigAnuncio(int id_anuncio, String condicion, int id_usuario, String texto, int id_local) {
    	
		int idAnuncio = 0;
		Connection con = null;
		int i = 1;
		if (condicion.equals("STARTERS")) condicion="ENTRANTES";
		if (condicion.equals("MAIN DISHES")) condicion="PRINCIPALES";
		if (condicion.equals("DESSERTS")) condicion="POSTRES";
		if (condicion.equals("DRINKS")) condicion="BEBIDAS";
    	try {    		
    		con = db.connectMySQL();
    		String query = "SELECT id_anuncio FROM anuncios where id_local=? and id_anuncio > ?  and FECHA_BAJA is null ";
    		if (!condicion.equals("P")) {
        		  query = query + "and validado = 'S' ";	
        	    } else {
        	    	query = query + "and validado = 'N' ";
        	    }
//    		if (condicion.equals("P")) {
//      		  query = query + "AND id_usuario = ? ";	
//      	    }
      	    if (condicion.length()>2) {
    		  query = query + "AND CATEGORIA = ? ";	
    	    }
      	    if (condicion.equals("T")) {
      		  query = query + "AND TITULO LIKE ? ";	
      	    }
    		query = query + " order by id_anuncio asc limit 1";
    		
    		PreparedStatement pst = con.prepareStatement(query);
    		pst.setInt(i++, id_local);
    		pst.setInt(i++, id_anuncio);
        	if (condicion.length()>2) {
        		pst.setString(i++, condicion);	        		
        	}    		    		
        	if (condicion.equals("T")) {
        		pst.setString(i++, "%" + texto + "%");        			 
        	}
    		ResultSet rs = pst.executeQuery();
    		if (rs.next()) {
    			idAnuncio = rs.getInt(1);
    		} 
    		rs.close();
            pst.close();    		    		  
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }finally{
                if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
             } 
           return idAnuncio;
    }
    
   public int leerIdAntAnuncio(int id_anuncio, String condicion, int id_usuario, String texto, int id_local ) {
    	
		int idAnuncio = 0;    	
		Connection con = null;
		int i = 1;
		if (condicion.equals("STARTERS")) condicion="ENTRANTES";
		if (condicion.equals("MAIN DISHES")) condicion="PRINCIPALES";
		if (condicion.equals("DESSERTS")) condicion="POSTRES";
		if (condicion.equals("DRINKS")) condicion="BEBIDAS";
    	try {    		    	
    		con = db.connectMySQL();
    		String query = "SELECT id_anuncio FROM anuncios where id_local=? and id_anuncio < ? and FECHA_BAJA is null ";
    		if (!condicion.equals("P")) {
      		  query = query + "and validado = 'S' ";	
      	    } else {
      	    	query = query + "and validado = 'N' ";
      	    }
//    		if (condicion.equals("P")) {
//      		  query = query + "AND id_usuario = ? ";	
//      	    }
      	    if (condicion.length()>2) {
    		  query = query + "AND CATEGORIA = ? ";	
    	    }
      	    if (condicion.equals("T")) {
      		  query = query + "AND TITULO LIKE ? ";	
      	    }
    		query = query + " order by id_anuncio desc limit 1";
    		
    		PreparedStatement pst = con.prepareStatement(query);    		
    		pst.setInt(i++, id_local);
    		pst.setInt(i++, id_anuncio);
//    		if (condicion.equals("P")) {
//    			pst.setLong(i++, id_usuario);
//    		}
        	if (condicion.length()>2) {
        		pst.setString(i++, condicion);	        		
        	}
        	if (condicion.equals("T")) {
        		pst.setString(i++, "%" + texto + "%");        			 
        	}
    		ResultSet rs = pst.executeQuery();
    		if (rs.next()) {
    			idAnuncio = rs.getInt(1);
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
           return idAnuncio;
    }
   public void borrarAnuncio(int id_anuncio) {
   	Connection con = null;
   	try {    		
   		con = db.connectMySQL();
   		
   		String query = "Update anuncios set FECHA_BAJA = SYSDATE(), validado = 'N' where id_anuncio = ? ";
   		
   		PreparedStatement pst = con.prepareStatement(query);         		                    	          	
         	pst.setInt(1, id_anuncio);
         	pst.execute();                	            
           pst.close();            
           } catch (SQLException ex) {
       	 System.out.println("Error SQL: " + ex);	                 
           }  finally{
               if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }   	    
   }
   public void actuDenunciaAnuncio(int id_anuncio, String indVal, int id_usu_denuncia) {
	   	Connection con = null;
	   	try {    		
	   		con = db.connectMySQL();
	   		String query = "Update anuncios set validado = ?, fecha_modif = SYSDATE(), id_usu_denuncia = ? where id_anuncio = ? ";
	   		
	   		PreparedStatement pst = con.prepareStatement(query);         		                    	          	
	         	pst.setString(1,indVal);
	         	pst.setInt(2, id_usu_denuncia);
	   		    pst.setInt(3, id_anuncio);
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
   
   public int leerUltimoIdAnuncio() {
   	Connection con = null;
   	String query = null;
   	PreparedStatement pst = null; 
   	int id_anuncio = 0;
   	try {    		    
   		con = db.connectMySQL();
   		query = "SELECT id_anuncio FROM anuncios where FECHA_BAJA is null and validado = 'S' order by id_anuncio desc limit 1"; 
   		pst = con.prepareStatement(query);
   		ResultSet rs = pst.executeQuery();    		
   	    if (rs.next())
   	    {
   	    	id_anuncio = rs.getInt(1);
   	    }
   	    rs.close();                  	            
           pst.close();           
           } catch (SQLException ex) {
       	 System.out.println("Error SQL: " + ex);	                 
           }  finally{
               if(con!=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }   	    
   	    return id_anuncio;
   }
    
   public boolean leerIdAnuncioValidado(int id_anuncio) {
	   	Connection con = null;
	   	String query = null;
	   	PreparedStatement pst = null; 
	   	boolean existe = false;
	   	try {    		    
	   		con = db.connectMySQL();
	   		query = "SELECT id_anuncio FROM anuncios where FECHA_BAJA is null and validado = 'S' and id_anuncio = ? "; 
	   		pst = con.prepareStatement(query);
	   		pst.setInt(1, id_anuncio);
	   		ResultSet rs = pst.executeQuery();    		
	   	    if (rs.next())
	   	    {
	   	    	existe = true;
	   	    }
	   	    rs.close();                  	            
	        pst.close();           
	      } catch (SQLException ex) {
	       	 System.out.println("Error SQL: " + ex);	                 
	           }  finally{
	               if(con!=null)
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            }   	    
	   	    return existe;
	   }
}
