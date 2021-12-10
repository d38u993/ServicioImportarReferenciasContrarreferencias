package Conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class ConexionSQL2008 
{
	private Connection conexion = null;
	private PreparedStatement cstmt = null;
	private ResultSet rs = null;
	
	private int estado; 
	
	private String servidor;
	private String nombre_base_datos;
	private String usuario;
	private String clave;
	private String puerto;
	
	private boolean correcto = false;
	
	private final static Logger LOGGER = Logger.getLogger(ConexionMySQL.class.getName());
	
	public ConexionSQL2008( String servidor, String puerto, String nombre_base_datos, String usuario, String clave ) {
		this.servidor = servidor;
		this.puerto = puerto;
		this.nombre_base_datos = nombre_base_datos;
		this.usuario = usuario;
		this.clave = clave;
	}
	
	public void crearConexionSQL() throws ClassNotFoundException
	{
		try 
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conexion = DriverManager.getConnection("jdbc:sqlserver://"+servidor+":"+puerto+";databaseName="+nombre_base_datos+";user="+usuario+";password="+clave);
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "CONEXI\u00D3N crearConexionSQL SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.fillInStackTrace() + " SISTEMA FINALIZADO - L49" );
			System.exit( 0 );
		}
		
	}
	
	public ResultSet ejecutarConsultaObtener()
	{
		try
		{
    		rs = cstmt.executeQuery();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, " EN CONEXI\u00D3N ejecutarConsultaObtener SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.fillInStackTrace() + " SISTEMA FINALIZADO - L149" );
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS ejecutarConsultaObtener NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L149" );
			System.exit( 0 );
		}
		
		return rs;
	}
	
	public int ejecutarConsultaModificar()
	{
		try
		{
			estado = cstmt.executeUpdate();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "EN CONEXI\u00D3N ejecutarConsultaModificar SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.fillInStackTrace() + " SISTEMA FINALIZADO - L83" );
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, " EN EN CONSULTAS ejecutarConsultaModificar NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L88" );
			System.exit( 0 );
		}
		
		return estado;
	}
	
	public PreparedStatement getSetenciaPreparada()
	{
		return cstmt;
	}
	
	public Connection getConexion()
	{
		return conexion;
	}
	
	public void cerrarConexion()
	{
		try 
		{
			if( conexion != null )
			{
				conexion.close();
			}
		} 
		catch (SQLException e) 
		{
			LOGGER.log(Level.SEVERE, "EN CONEXI\u00D3N cerrarConexion SQLException SQLException \n\n" + e.getMessage() + " " + e.getSQLState() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L116" );
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS cerrarConexion NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L121" );
			System.exit( 0 );
		}
	}
	
	public void setSetenciaPreparada( PreparedStatement setenciaPreparada )
	{
		cstmt = setenciaPreparada;
	}
	
	public void aceptarSentencias()
	{
		try 
		{
			conexion.commit();
			consultaExitosa( true );
		}
		catch (SQLException e) 
		{
			desHacerSentencia();
			LOGGER.log(Level.SEVERE, "EN CONEXI\u00D3N aceptarSetencias SQLException \n\n" + e.getMessage() + " " + e.getSQLState() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L141" );
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS aceptarSetencias NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L146" );
			System.exit( 0 );
		}
	}
	
	public void desHacerSentencia()
	{
		try 
		{
			conexion.rollback();
			consultaExitosa( false );
		} 
		catch (SQLException e) 
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS desHacerSentencia SQLException \n\n" + e.getMessage() + " " + e.getSQLState() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L160" );
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS desHacerSentencia NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L165" );
			System.exit( 0 );
		}
	}
	
	public void consultaExitosa( boolean respuesta )
	{
		correcto = respuesta;
	}
	
	public boolean getConsultaExitosa()
	{
		return correcto;
	}
}
