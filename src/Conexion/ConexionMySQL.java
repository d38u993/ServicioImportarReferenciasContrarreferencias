package Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
//
public class ConexionMySQL 
{
	private Connection conexion = null;
	private PreparedStatement cstmt = null;
	private ResultSet rs = null;
	
	private int estado; 
	//
	private String servidor;
	private String puerto;
	private String bd;
	
	private String url = "";
	private boolean correcto = false;
	
	private final static Logger LOGGER = Logger.getLogger(ConexionMySQL.class.getName());
	
	public ConexionMySQL( String servidor, String puerto, String bd) {
		this.servidor = servidor;
		this.puerto = puerto;
		this.bd = bd;
	}
	
	public void crearConexionMySQL()
	{
		try 
		{
			url = "jdbc:mysql://localhost:" + puerto + "/" + bd + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection(url,"root","");//System.out.println("MySql => " + conexion);
			conexion.setAutoCommit(false);
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL SQLException \n\n" + ex.getSQLState() + " " + ex.fillInStackTrace() + " - L45" );
		}
		catch (ClassNotFoundException e) 
		{
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL ClassNotFoundException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " - L49" );
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
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL ejecutarConsultaObtener SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L61" );
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, " EN ConexionMySQL ejecutarConsultaObtener NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L66" );
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
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL ejecutarConsultaModificar SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " SSITEMA FINALIZADO - L81" );
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, " EN ConexionMySQL ejecutarConsultaModificar NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L86" );
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
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL cerrarConexion SQLException \n\n" + e.getMessage() + " " + e.getSQLState() + " " + e.getErrorCode() + " - L114" );
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, " EN ConexionMySQL cerrarConexion NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L119" );
			System.exit( 0 );
		}
	}
	
	public void cerrarSetenciaPreparada() {
		try 
		{
			if( conexion != null )
			{
				cstmt.close();
			}
		} 
		catch (SQLException e) 
		{
			LOGGER.log(Level.SEVERE,  "EN ConexionMySQL cerrarSetenciaPreparadaMySQL SQLException \n\n" + e.getMessage() + " " + e.getSQLState() + " " + e.getErrorCode() + " - L134");
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
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL aceptarSetencias SQLException \n\n" + e.getMessage() + " " + e.getSQLState() + " " + e.getErrorCode() + " - L154" );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, " EN ConexionMySQL aceptarSetencias NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L158" );
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
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL desHacerSentencia SQLException \n\n" + e.getMessage() + " " + e.getSQLState() + " " + e.getErrorCode() + " - L172" );
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, " EN ConexionMySQL desHacerSentencia NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L177" );
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