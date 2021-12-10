import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import consultas.ConsultasSQL2008R2;

public class Propiedades 
{
	private Properties prop = new Properties();
	
	private InputStream is = null;
	
	private final static Logger LOGGER = Logger.getLogger(Propiedades.class.getName());
	
	public void cargarArchivoPropertie( String ruta)
	{
		try 
		{
			is = new FileInputStream( ruta );
			prop.load( is );
		} 
		catch( FileNotFoundException file )
		{
			LOGGER.log(Level.SEVERE, "ARCHIVO DE CONFIGURACI\u00D3N PARA LA CONEXI\u00D3N NO ENCONTRADO FileNotFoundException \n\n" + file.getMessage() + " " + file.fillInStackTrace(), " SITEMA FINALIZADO - L31" );
			System.exit( 0 );
		}
		catch( IOException e) 
		{
			LOGGER.log(Level.SEVERE, "EN PROPIEDADES cargarArchivoPropertie IOException \n\n" + e.getMessage() + " " + e.fillInStackTrace(), " SITEMA FINALIZADO - L36" );
			System.exit( 0 );
		}
		catch( NullPointerException e) 
		{
			LOGGER.log(Level.SEVERE, "EN PROPIEDADES cargarArchivoPropertie NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace(), " SITEMA FINALIZADO - L41" );
			System.exit( 0 );
		}
	}
	
	public Properties getPropiedad()
	{
		return  prop;
	}
	
	public void salvarConfiguracion()
	{
		try 
		{
			prop.store( new FileWriter( "E:/eclipse-workspace/properties/ServicioImportarReferenciasContrarreferencias_propiedad_estado_BD.properties" ), "" );
		} 
		catch(IOException ioe)
		{
			LOGGER.log(Level.SEVERE, "EN PROPIEDADES salvarConfiguracion IOException \n\n" + ioe.getMessage() + " " + ioe.fillInStackTrace(), " SITEMA FINALIZADO - L59" );
			System.exit( 0 );
		}
	}
}
