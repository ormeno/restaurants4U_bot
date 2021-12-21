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
import bot.bo.ProductosPedido;
import bot.bo.Pedidos;

public class ProductoPedidoDAO {
	
database db = new database();
	
	public ProductoPedidoDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	 
public int altaProductoPedido(int id_pedido,int id_anuncio, BigDecimal precio) {     	    
     	
    	Connection con = null;
    	int pk = 0;
    	try {    	
    		con = db.connectMySQL(); 
    		String query = " insert into productosPedido (id_pedido, id_anuncio, cantidad, precioProductoPedido, fecha_alta)"
    			           + " values (?,?,1,?,?)";
    		Calendar calendar = Calendar.getInstance();
    	    java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	      
        	PreparedStatement pst2 = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); 
          	pst2.setInt(1, id_pedido);          	
          	pst2.setInt(2, id_anuncio);
          	pst2.setBigDecimal(3, precio); 
          	pst2.setDate(4, startDate);
          	pst2.execute();                	            
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

  public int buscarIdProdPedidoAnuncio(int id_pedido,int id_anuncio) {
	int res = 0;    	
    Connection con = null;
	try {
		con = db.connectMySQL();
    	PreparedStatement pst = con.prepareStatement("SELECT id_productoPedido FROM productosPedido where id_pedido = ? AND id_anuncio = ? AND FECHA_BAJA is null ");        	
    	pst.setInt(1, id_pedido);          	
      	pst.setInt(2, id_anuncio);         	
      	ResultSet rs = pst.executeQuery();                	
        while (rs.next()) {        	        		
        	res = rs.getInt(1);
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
       return res;
  }
  
  public void actuPrecioConDescuentoCarro(BigDecimal descuento, int id_productoPedido)  {
  	Connection con = null;
  	int numRow = 0;
  	try {       		
  		con = db.connectMySQL(); 

          String query = "Update productosPedido set precioProductoPedido = ?, flagDescuento= 'S' "
  				+ " where id_productoPedido = ? ";
  	    PreparedStatement pst2 = con.prepareStatement(query);      
  	    pst2.setBigDecimal(1, descuento);   
        pst2.setInt(2, id_productoPedido);
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
  
	 public void actuCantidadProductoPedido(int id_pedido,int id_anuncio) {
	    	Connection con = null;
	    	try {    		
	    		con = db.connectMySQL();
	    		
	    		String query = "Update productosPedido set cantidad = cantidad + 1, fecha_modif = SYSDATE() where id_pedido = ? and id_anuncio = ? ";
	    		
	    		PreparedStatement pst = con.prepareStatement(query);
	          	pst.setInt(1, id_pedido);          	
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
	 
  
  public List<ProductosPedido> obtenerProductosPedido(int id_pedido) {    	
	ProductosPedido proPedido = null;
  	List<ProductosPedido> prodcutosPedido = new ArrayList<ProductosPedido>();	        
  	Connection con = db.connectMySQL();
  	try {  
  		PreparedStatement pst = null; 
  		String query = "select id_productoPedido, id_pedido, id_anuncio, cantidad, precioProductoPedido, flagDescuento, "
      			+ "fecha_alta, fecha_modif, fecha_baja "
      			+ " FROM productosPedido where id_pedido = ? ";
  		query = query + " order by fecha_alta";
  		pst = con.prepareStatement(query);
	    pst.setInt(1, id_pedido);
        ResultSet rs = pst.executeQuery();                	
        while (rs.next()) {
        	proPedido = new ProductosPedido(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getBigDecimal(5), rs.getString(6),
          			rs.getDate(7), rs.getDate(8),rs.getDate(9));
        	prodcutosPedido.add(proPedido);
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
         return prodcutosPedido;
  } 
  
  public ProductosPedido obtenerProductoPedido(int id_productoPedido) {    	
	ProductosPedido proPedido = null;       
  	Connection con = null;
  	try {    		
  		con = db.connectMySQL();
  		String query = "select id_productoPedido, id_pedido, id_anuncio, cantidad, precioProductoPedido, flagDescuento, "
      			+ "fecha_alta, fecha_modif, fecha_baja "
      			+ " FROM productosPedido where id_productoPedido = ? ";
      	PreparedStatement pst = con.prepareStatement(query);        	
        pst.setInt(1, id_productoPedido);  
        ResultSet rs = pst.executeQuery();                	
        while (rs.next()) { 
       	  proPedido = new ProductosPedido(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getBigDecimal(5),rs.getString(6),
            			rs.getDate(7), rs.getDate(8),rs.getDate(9));
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
         return proPedido;
  }
  
  public int borraProductoPedido(int id_productoPedido) {
  	Connection con = null;
  	int numRow = 0;
  	try {    		
  		con = db.connectMySQL();
  		
  		String query = "delete from productosPedido where id_productoPedido = ? ";
  		
  		PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, id_productoPedido);
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
  
  public int borrarCarro(int id_pedido) {
	  	Connection con = null;
	  	int numRow = 0;
	  	try {    		
	  		con = db.connectMySQL();
	  		
	  		String query = "delete from productosPedido where id_pedido = ? ";
	  		
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
  
}
