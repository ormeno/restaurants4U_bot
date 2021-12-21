package bot.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bot.database;
import bot.bo.Anuncios;
import bot.bo.Pedidos;
import bot.bo.Usuarios;

public class PedidoDAO {
	
    database db = new database();
	
	public PedidoDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int altaPedido(int id_usuario, Pedidos pedido) {     	    
     	
    	Connection con = null;
    	int pk = 0;
    	try {    	
    		con = db.connectMySQL(); 
    		
    		//  id_pedido, id_usuario, id_anuncio, estado, id_stripe, payload, precio, ciudad, cod_pais, cod_postal, estadoDir, direccion1, direccion2, nombre_ped, mail_ped, telefono_ped, fecha_alta, fecha_modif, fecha_baja
    		
    		String query = " insert into pedidos (id_usuario, id_anuncio, id_local, estado, id_stripe,  payload, precio_total, ciudad, cod_pais, cod_postal, estadoDir, direccion1, direccion2, nombre_ped, mail_ped, telefono_ped, fecha_alta)"
    			           + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    		Calendar calendar = Calendar.getInstance();
    	    java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	      
        	PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); 
          	pst.setInt(1, id_usuario);         
          	pst.setInt(2, pedido.getId_anuncio());
          	// NOTA: id_local a 0 mientras el pago por tarjeta no esté disponible
          	pst.setInt(3, 0);
          	pst.setString(4, pedido.getEstado());
          	pst.setString(5, pedido.getId_stripe());
          	pst.setString(6, pedido.getPayload());
          	pst.setBigDecimal(7, pedido.getPrecio_total());
          	pst.setString(8, pedido.getCiudad());
          	pst.setString(9, pedido.getCod_pais());
          	pst.setString(10, pedido.getCod_postal());
          	pst.setString(11, pedido.getEstadoDir());
          	pst.setString(12, pedido.getDireccion1());
          	pst.setString(13, pedido.getDireccion2());
          	pst.setString(14, pedido.getNombre_ped());
          	pst.setString(15, pedido.getMail_ped());
          	pst.setString(16, pedido.getTelefono_ped());
          	pst.setDate(17, startDate);
          	pst.execute();                	            
            
            System.out.println("Alta del pedido!! ");
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
	
    public int altaPedidoMetalico(int id_usuario,int id_anuncio, BigDecimal precio, int id_local) {     	    
     	
    	Connection con = null;
    	int pk = 0;
    	try {    	
    		con = db.connectMySQL(); 
    		String query = " insert into pedidos (id_usuario, id_anuncio, id_local, id_stripe, estado, precio_total, cod_pais, fecha_alta)"
    			           + " values (?,?,?,'Pedido Metalico','GESTION',?,'ES',?)";
    		Calendar calendar = Calendar.getInstance();
    	    java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	      
        	PreparedStatement pst2 = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); 
          	pst2.setInt(1, id_usuario);          	
          	pst2.setInt(2, id_anuncio);
          	pst2.setInt(3, id_local);
          	pst2.setBigDecimal(4, precio); 
          	pst2.setDate(5, startDate);
          	pst2.execute();                	            
            
            //System.out.println("Alta de pedido en metálico!! ");
            ResultSet rs = pst2.getGeneratedKeys();  
            pk = rs.next() ? rs.getInt(1) : 0;
            pst2.close();
          //  if(pk!=0){
          //      System.out.println("Generated key="+pk);
          //  }
            
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
	
	public Pedidos obtenerPedido(int id_pedido) {    	
    	Pedidos pedido = null;       
    	Connection con = null;
    	try {    		
    		con = db.connectMySQL();
        	PreparedStatement pst = con.prepareStatement("select id_pedido, id_usuario, id_anuncio, estado, id_stripe, payload, precio_total, ciudad, cod_pais,"
        			+ " cod_postal, estadoDir, direccion1, direccion2, nombre_ped, mail_ped, telefono_ped, fecha_alta, fecha_modif, fecha_baja, flag, id_local "
        			+ " FROM pedidos WHERE id_pedido = ? ");        	
          	pst.setInt(1, id_pedido);  
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {       	        
            	pedido = new Pedidos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
            			rs.getString(6), rs.getBigDecimal(7), rs.getString(8), rs.getString(9),
            			rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), 
            			rs.getString(14), rs.getString(15), rs.getString(16),
            			rs.getDate(17), rs.getDate(18),rs.getDate(19), rs.getString(20), rs.getInt(21));
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
           return pedido;
    }
	
	 public List<Pedidos> obtenerPedidos(String estado, int id_local) {    	
		    Pedidos pedido = null;
		    if (estado.equals("PENDING")) estado="PENDIENTE";
	    	if (estado.equals("IN KITCHEN")) estado="EN COCINA";
	    	if (estado.equals("PREPARED")) estado="PREPARADO";
	    	if (estado.equals("ON THE WAY")) estado="EN CAMINO";
	    	if (estado.equals("DELIVERED")) estado="ENTREGADO";
	    	if (estado.equals("CANCELED")) estado="ANULADO";
	    	List<Pedidos> pedidos = new ArrayList<Pedidos>();	        
	    	Connection con = db.connectMySQL();
	    	try {  
	    		PreparedStatement pst = null; 
	    		String query = "select id_pedido, id_usuario, id_anuncio, estado, id_stripe, "
	        			+ "payload, precio_total, ciudad, cod_pais,"
	        			+ " cod_postal, estadoDir, direccion1, direccion2, "
	        			+ "nombre_ped, mail_ped, telefono_ped, "
	        			+ "fecha_alta, fecha_modif, fecha_baja, flag, id_local "
	        			+ " FROM pedidos where id_local = ? ";
	    		if (!estado.equals("TODOS")) {
	    		  query = query + " AND estado = ?  ";
	    		}
	    		query = query + " order by id_pedido";
	    		pst = con.prepareStatement(query);
	    		pst.setInt(1, id_local);
	    		if (!estado.equals("TODOS")) {
		        	  pst.setString(2, estado);
		    		}
	          	ResultSet rs = pst.executeQuery();                	
	            while (rs.next()) {        	        
	            	pedido = new Pedidos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
	            			rs.getString(6), rs.getBigDecimal(7), rs.getString(8), rs.getString(9),
	            			rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), 
	            			rs.getString(14), rs.getString(15), rs.getString(16),
	            			rs.getDate(17), rs.getDate(18),rs.getDate(19), rs.getString(20), rs.getInt(21));
	                pedidos.add(pedido);
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
	           return pedidos;
	    } 
	 
	 public void actualizarEstadoPedido(String estado, int id_pedido) {
	    	Connection con = null;
	    	if (estado.equals("PENDING")) estado="PENDIENTE";
	    	if (estado.equals("IN KITCHEN")) estado="EN COCINA";
	    	if (estado.equals("PREPARED")) estado="PREPARADO";
	    	if (estado.equals("ON THE WAY")) estado="EN CAMINO";
	    	if (estado.equals("DELIVERED")) estado="ENTREGADO";
	    	if (estado.equals("CANCELED")) estado="ANULADO";
	    	try {    		
	    		con = db.connectMySQL();
	    		
	    		String query = "Update pedidos set estado = ?, fecha_modif = SYSDATE() where id_pedido = ? ";
	    		
	    		PreparedStatement pst = con.prepareStatement(query);
	    		pst.setString(1, estado);
	          	pst.setInt(2, id_pedido);
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
	 
	 
	 public int borrarPedido(int id_pedido) {
	    	Connection con = null;
	    	int numRow = 0;
	    	int numRow2 = 0;
	    	try {    		
	    		con = db.connectMySQL();
	    		
	    		String query = "delete from pedidos where id_pedido = ? ";
	    		
	    		PreparedStatement pst = con.prepareStatement(query);
	          	pst.setInt(1, id_pedido);
	          	numRow = pst.executeUpdate();                	            
	            pst.close();        
	            
	            String queryHijos = "delete from productosPedido where id_pedido = ? ";
	            PreparedStatement pst2 = con.prepareStatement(queryHijos);
	          	pst2.setInt(1, id_pedido);
	          	numRow2 = pst2.executeUpdate();                	            
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
	    	return numRow;
	    }
	 
	 public int actuPedidoPagoTarjeta(Pedidos pedido) {
	    	Connection con = null;
	    	int numRow = 0;
	    	try {    		
	    		con = db.connectMySQL();
	    		
	    		String query = "Update pedidos set estado = ?, id_stripe = ?, payload=?, "
	    				+ " ciudad=?, cod_pais=?, cod_postal=?, estadoDir=?, direccion1=?, direccion2=?,"
	    				+ " nombre_ped=?, mail_ped=?, telefono_ped=?, fecha_modif = SYSDATE() "
	    				+ "where id_pedido = ? ";
	    		
	    		PreparedStatement pst = con.prepareStatement(query);
	    		
	          	pst.setString(1, pedido.getEstado());
	          	pst.setString(2, pedido.getId_stripe());
	          	pst.setString(3, pedido.getPayload());
	          	pst.setString(4, pedido.getCiudad());
	          	pst.setString(5, pedido.getCod_pais());
	          	pst.setString(6, pedido.getCod_postal());
	          	pst.setString(7, pedido.getEstadoDir());
	          	pst.setString(8, pedido.getDireccion1());
	          	pst.setString(9, pedido.getDireccion2());
	          	pst.setString(10, pedido.getNombre_ped());
	          	pst.setString(11, pedido.getMail_ped());
	          	pst.setString(12, pedido.getTelefono_ped());
	          	pst.setInt(13, pedido.getId_pedido());

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
	 
	 
	 public Pedidos pedidoPendiente(int id_usuario) {	
	    	Pedidos pedido = null;    	
	        Connection con = null;
	    	try {
	    		con = db.connectMySQL();
	        	PreparedStatement pst = con.prepareStatement("SELECT id_pedido, id_usuario, id_anuncio, estado, id_stripe, "
	        			+ " payload, precio_total, ciudad, cod_pais,"
	    				+ " cod_postal, estadoDir, direccion1, direccion2,"
	    				+ " nombre_ped, mail_ped, telefono_ped,"
	    				+ " fecha_alta, fecha_modif, fecha_baja, flag, id_local "
	        			+ " FROM pedidos where id_usuario = ? and estado = 'GESTION' and FECHA_BAJA is null ");        	
	          	pst.setInt(1, id_usuario);            	
	          	ResultSet rs = pst.executeQuery();                	
	            while (rs.next()) {        	        		
	            	pedido = new Pedidos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
	            			rs.getString(6), rs.getBigDecimal(7), rs.getString(8), rs.getString(9),
	            			rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), 
	            			rs.getString(14), rs.getString(15), rs.getString(16),
	            			rs.getDate(17), rs.getDate(18),rs.getDate(19), rs.getString(20), rs.getInt(21));
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
	           return pedido;
	    }
	 
	 public Pedidos pedidoPendienteLocal(int id_usuario,int id_local) {	
	    	Pedidos pedido = null;    	
	        Connection con = null;
	    	try {
	    		con = db.connectMySQL();
	        	PreparedStatement pst = con.prepareStatement("SELECT id_pedido, id_usuario, id_anuncio, estado, id_stripe, "
	        			+ " payload, precio_total, ciudad, cod_pais,"
	    				+ " cod_postal, estadoDir, direccion1, direccion2,"
	    				+ " nombre_ped, mail_ped, telefono_ped,"
	    				+ " fecha_alta, fecha_modif, fecha_baja, flag, id_local "
	        			+ " FROM pedidos where id_usuario = ? and id_local = ? and estado = 'GESTION' and FECHA_BAJA is null ");        	
	          	pst.setInt(1, id_usuario);   
	          	pst.setInt(2, id_local); 
	          	ResultSet rs = pst.executeQuery();                	
	            while (rs.next()) {        	        		
	            	pedido = new Pedidos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
	            			rs.getString(6), rs.getBigDecimal(7), rs.getString(8), rs.getString(9),
	            			rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), 
	            			rs.getString(14), rs.getString(15), rs.getString(16),
	            			rs.getDate(17), rs.getDate(18),rs.getDate(19), rs.getString(20), rs.getInt(21));
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
	           return pedido;
	    }
	 
	 public int actuFlagPedido(String texto, int id_pedido) {
	    	Connection con = null;   	
	    	int numRow = 0;
	    	try {       		
	    		con = db.connectMySQL();    		
	    		
	    		// 
               
	    		 String query = "Update pedidos SET flag = ";
	    		 
	    		 if (texto == null) {
	    			 query = query +  " null where estado = 'GESTION' and id_pedido = ? ";
	    		 } else if (texto.equals("DATOSENVIO")) {
	    			 query = query +  " 'DATOSENVIO' where estado = 'GESTION' and id_pedido = ? ";
	    		 } else if (texto.equals("DIRECCION")) {
	    			 query = query +  " 'direccion1' where estado = 'GESTION' and id_pedido = ? ";
	    		 } else if (texto.equals("CIUDAD")) {
	    			 query = query +  " 'ciudad' where estado = 'GESTION' and id_pedido = ? ";
	    		 } else if (texto.equals("COD. POSTAL")) {
	    			 query = query +  " 'cod_postal' where estado = 'GESTION' and id_pedido = ? ";
	    		 } else if (texto.equals("DESTINATARIO")) {
	    			 query = query +  " 'nombre_ped' where estado = 'GESTION' and id_pedido = ? ";
	    		 } else if (texto.equals("TELEFONO")) {
	    			 query = query +  " 'telefono_ped' where estado = 'GESTION' and id_pedido = ? ";
	    		 }
	    				 		  
	    		  PreparedStatement pst = con.prepareStatement(query);          
	    	      pst.setInt(1, id_pedido);   
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
	 
	 public Pedidos actuPedido(String texto, int id_pedido)  {
	    	Connection con = null;
	    	Pedidos pedido = null;    	
	    	try {       		
	    		con = db.connectMySQL();   
	    	
	    		PreparedStatement pst = con.prepareStatement("select id_pedido, id_usuario, id_anuncio, estado, id_stripe,"
	    				+ " payload, precio_total, ciudad, cod_pais,"
	    				+ " cod_postal, estadoDir, direccion1, direccion2,"
	    				+ " nombre_ped, mail_ped, telefono_ped,"
	    				+ " fecha_alta, fecha_modif, fecha_baja, flag, id_local "
	        			+ " FROM pedidos WHERE id_pedido = ?");        	
	          	pst.setInt(1, id_pedido);  
	          	ResultSet rs = pst.executeQuery();                	
	          	if (rs.next()) {     
	            	pedido = new Pedidos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
	            			rs.getString(6), rs.getBigDecimal(7), rs.getString(8), rs.getString(9),
	            			rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), 
	            			rs.getString(14), rs.getString(15), rs.getString(16),
	            			rs.getDate(17), rs.getDate(18),rs.getDate(19), rs.getString(20), rs.getInt(21));
	            }       
	    		rs.close();
	            pst.close();

	            if (pedido.getFlag() != null) {    
	    		  String query = "Update pedidos SET " + pedido.getFlag() + " = ?, flag = NULL ";    		
	      		  query = query + " where id_pedido = ?  ";    
	    		  PreparedStatement pst2 = con.prepareStatement(query);         
	    		  
	    		  pst2.setString(1, texto);   
	          	  pst2.setLong(2, id_pedido);
	          	  pst2.execute();                	            
	              pst2.close();
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
	    	 return pedido;    	
	    }
	 
	 public void actuDireccionEnvioDirecto(String texto, int id_pedido)  {
	    	Connection con = null;
	    	int numRow = 0;
	    	try {       		
	    		con = db.connectMySQL(); 

	            String query = "Update pedidos set direccion1 = ?, fecha_modif = SYSDATE() "
	    				+ " where id_pedido = ? and estado='GESTION' ";
	    	    PreparedStatement pst2 = con.prepareStatement(query);      
	    	    pst2.setString(1, texto);   
	            pst2.setInt(2, id_pedido);
	            numRow = pst2.executeUpdate();                	            
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
	    }
	 
	 public void actuPrecioConDescuento(BigDecimal descuento, int id_pedido)  {
	    	Connection con = null;
	    	int numRow = 0;
	    	try {       		
	    		con = db.connectMySQL(); 

	            String query = "Update pedidos set precio_total = ? "
	    				+ " where id_pedido = ? and estado='GESTION' ";
	    	    PreparedStatement pst2 = con.prepareStatement(query);      
	    	    pst2.setBigDecimal(1, descuento);   
	            pst2.setInt(2, id_pedido);
	            numRow = pst2.executeUpdate();                	            
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
	    }
	 public int actuEnvioDirecto(int id_pedido)  {
	    	Connection con = null;
	    	int numRow = 0;
	    	try {       		
	    		con = db.connectMySQL(); 
	            String query = "Update pedidos set flag= null, estado = 'PENDIENTE', fecha_modif = SYSDATE() "
	    				+ " where id_pedido = ? and estado='GESTION' ";
	    	    PreparedStatement pst2 = con.prepareStatement(query);      
	            pst2.setInt(1, id_pedido);
	            numRow = pst2.executeUpdate();                	            
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
	    	return numRow;
	    }
	 
	 public int envioPedido(int id_pedido) {
	    	Connection con = null;
	    	Pedidos pedido = null; 
	    	int numRow = 0;
	    	try {    		
	    		con = db.connectMySQL();
	    		
	    		PreparedStatement pst = con.prepareStatement("select id_pedido, id_usuario, id_anuncio, estado, id_stripe,"
	    				+ " payload, precio_total, ciudad, cod_pais,"
	    				+ " cod_postal, estadoDir, direccion1, direccion2,"
	    				+ " nombre_ped, mail_ped, telefono_ped,"
	    				+ " fecha_alta, fecha_modif, fecha_baja, flag, id_local "
	        			+ " FROM pedidos WHERE id_pedido = ?");        	
	          	pst.setInt(1, id_pedido);  
	          	ResultSet rs = pst.executeQuery();                	
	          	if (rs.next()) {     
	            	pedido = new Pedidos(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
	            			rs.getString(6), rs.getBigDecimal(7), rs.getString(8), rs.getString(9),
	            			rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), 
	            			rs.getString(14), rs.getString(15), rs.getString(16),
	            			rs.getDate(17), rs.getDate(18),rs.getDate(19), rs.getString(20), rs.getInt(21));
	            }       
	    		rs.close();
	            pst.close();

	            if (pedido.getDireccion1() != null && pedido.getCod_postal()!= null && pedido.getCiudad()!= null && pedido.getNombre_ped()!= null && pedido.getTelefono_ped()!= null) {    
			    	String query = "Update pedidos set flag= null, estado = 'PENDIENTE', fecha_modif = SYSDATE() "
			    				+ " where id_pedido = ? and estado='GESTION' ";
			    	PreparedStatement pst2 = con.prepareStatement(query);         		                    	          	
			        pst2.setInt(1, id_pedido);
			        numRow = pst2.executeUpdate();                	            
	                pst2.close();            
	              } else {
	            	  numRow=2;
	              }
	            
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
	    	return numRow;
	    }
}
