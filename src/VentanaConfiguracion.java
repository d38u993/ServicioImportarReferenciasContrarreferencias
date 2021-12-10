import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import consultas.ConsultasMySQL;
import consultas.ConsultasSQL2008R2;

public class VentanaConfiguracion{

	//private ConsultasSQL2008R2 consulta;
	//private ConsultasMySQL consultaMySQL;
	
	private MySystemTray mySystemTray = null;
	
	private Propiedades propiedad_config_conex_bd = new Propiedades();
	private Propiedades propiedad_config_servicio = new Propiedades();
	
	private String sql2008_servidor = "";
	private String sql2008_puerto = "";
	private String sql2008_bd = "";
	private String sql2008_usuario = "";
	private String sql2008_clave = "";
	private String mysql_servidor = "";
	private String mysql_puerto = "";
	private String mysql_bd = "";
	private String mysql_usuario = "";
	private String mysql_clave = "";
	private String unidadMedica = "";
	
	private int tiempo_busqueda = 0;
	
	private final static Logger LOGGER = Logger.getLogger(VentanaConfiguracion.class.getName());
	
	public VentanaConfiguracion() 
	{
		propiedad_config_conex_bd.cargarArchivoPropertie( "E:/eclipse-workspace/properties/Importador_MYSQL_SQL2008R2_configuracion.properties" );
		propiedad_config_servicio.cargarArchivoPropertie( "D:/Users/salud/eclipse-workspace/properties/ServicioImportarReferenciasContrarreferencias_propiedad_estado_BD.properties" );
		
		sql2008_servidor = propiedad_config_conex_bd.getPropiedad().getProperty( "sql2008_servidor" );
		sql2008_puerto = propiedad_config_conex_bd.getPropiedad().getProperty( "sql2008_puerto" );
		sql2008_bd = propiedad_config_conex_bd.getPropiedad().getProperty( "sql2008_bd" );
		sql2008_usuario = propiedad_config_conex_bd.getPropiedad().getProperty( "sql2008_usuario" );
		sql2008_clave = propiedad_config_conex_bd.getPropiedad().getProperty( "sql2008_clave" );

		mysql_servidor = propiedad_config_conex_bd.getPropiedad().getProperty( "mySQL_servidor" );
		mysql_puerto = propiedad_config_conex_bd.getPropiedad().getProperty( "mySQL_puerto" );
		mysql_bd = propiedad_config_conex_bd.getPropiedad().getProperty( "mySQL_bd" );
		mysql_usuario = propiedad_config_conex_bd.getPropiedad().getProperty( "mySQL_usuario" );
		mysql_clave = propiedad_config_conex_bd.getPropiedad().getProperty( "mySQL_clave" );
		
		unidadMedica = propiedad_config_conex_bd.getPropiedad().getProperty( "unidad_medica" );
		
		tiempo_busqueda = Integer.valueOf( propiedad_config_conex_bd.getPropiedad().getProperty( "tiempo_busqueda" ) );
		
		if(Integer.valueOf( tiempo_busqueda ) >= 5000)//Son 10s en milisegundos (10000)
		{
			//probarConexion();
			mySystemTray = new MySystemTray( sql2008_servidor ,sql2008_puerto,sql2008_bd,sql2008_usuario, sql2008_clave, mysql_servidor , mysql_puerto, mysql_bd, mysql_usuario, mysql_clave, tiempo_busqueda, unidadMedica );
		}
		/*else
		//{
			//LOGGER.log(Level.FINE, "TIEMPO DE B\u00DASQUEDA NO PUEDE SER MENOR A 10s. - L51" );
		}*/
	}
	/*
	public void probarConexion()
	{
		consulta = new ConsultasSQL2008R2( sql2008_servidor ,sql2008_puerto,sql2008_bd,sql2008_usuario, sql2008_clave );
		consultaMySQL = new ConsultasMySQL( mysql_servidor , mysql_puerto, mysql_bd, mysql_usuario, mysql_clave );
		
		try
		{
			if( consulta.getConexion().getConexion() != null )
			{
				LOGGER.log(Level.FINE, "CONEXI\u00D3N EXITOSA HACIA EL SERVIDOR SIGHO. - L64" );
				propiedad_config_conex_bd.getPropiedad().setProperty( "servidor.error_conexion" , "false" );
			}
			if( consultaMySQL.getConexionMySQL().getConexion() != null )
			{
				LOGGER.log(Level.FINE, "CONEXI\u00D3N EXITOSA HACIA EL SERVIDOR CENTRAL. - L69" );
				propiedad_config_conex_bd.getPropiedad().setProperty( "servidor.error_conexion" , "false" );
			}
			if( consulta.getConexion().getConexion() == null )
			{
				LOGGER.log(Level.SEVERE, "HAY UN PROBLEMA CON LA CONEXI\u00D3N HACIA EL SERVIDOR SIGHO. \n REVISE LOS DATOS DE SU CONFIGURACI\u00D3N E INTENTE NUEVAMENTE. - L74" );
				propiedad_config_conex_bd.getPropiedad().setProperty( "servidor.error_conexion" , "true" );
			}
			if( consultaMySQL.getConexionMySQL().getConexion() == null )
			{
				LOGGER.log(Level.SEVERE, "HAY UN PROBLEMA CON LA CONEXI\u00D3N, HACIA EL SERVIDOR CENTRAL. \n REVISE LOS DATOS DE LA IP CENTRAL. - L79" );
				propiedad_config_conex_bd.getPropiedad().setProperty( "servidor.error_conexion" , "true" );
			}
		}
		catch (ArrayIndexOutOfBoundsException e1) {
			LOGGER.log(Level.SEVERE, "VentanaConfiguracion ArrayIndexOutOfBoundsException \n" + e1.fillInStackTrace() + " ERROR DE SISTEMA  - L84" );
		}
		
		consulta.cerrarConexionSQL();
		consultaMySQL.cerrarConexionMySQL();
		
		propiedad_config_conex_bd.salvarConfiguracion();
	}*/
	/*
	public void comenzarBusquedaReferencias()
	{
		if(  propiedad_config_conex_bd.getPropiedad().getProperty( "sql2008_conexion_exitosa" ).matches( "false" ) )
		{
			LOGGER.log(Level.WARNING, "EN VentanaConfiguracion HAY UN PROBLEMA CON LA CONEXI\u00D3N. REVISE LOS DATOS DE SU CONFIGURACI\u00D3N E INTENTE NUEVAMENTE. - L97" );
		}
		else
		{
			LOGGER.log(Level.FINE, "COMENZANDO CON LA B\u00DASQUEDA DE REFERENCIAS. - L101" );
			
			consultaMySQL = new ConsultasMySQL( mysql_servidor , mysql_puerto, mysql_bd, mysql_usuario, mysql_clave );
			
			int total_referencias = 0;
			int ultimo_id_referencia = 0;
			
			try 
			{
				if( Long.parseLong( propiedad_config_servicio.getPropiedad().getProperty( "ultimoRegistro" ) ) == 0 )
				{
					total_referencias = consultaMySQL.obtenerTotalRegistrosReferencia( propiedad_config_servicio.getPropiedad().getProperty( "unidad_medica" ) );
					propiedad_config_servicio.getPropiedad().setProperty( "ultimo_conteo_referencias", String.valueOf( total_referencias ) );

					ultimo_id_referencia = consultaMySQL.obtenerUltimoIDRefencia( propiedad_config_servicio.getPropiedad().getProperty( "unidad_medica" ) );
					propiedad_config_servicio.getPropiedad().setProperty( "ultimo_id_referencia", String.valueOf( ultimo_id_referencia ) );
					
					
					consultaMySQL.comMySQL();
					
					if(consultaMySQL.getConexionMySQL().getConsultaExitosa() )
					{
						propiedad_config_conex_bd.salvarConfiguracion();
						consulta.cerrarSetenciaPreparadaSQL();
					}
					
					consulta.cerrarConexionSQL();
					consultaMySQL.cerrarConexionMySQL();
				}
				else
				{
					consulta.cerrarConexionSQL();
					consultaMySQL.cerrarConexionMySQL();
				}
			} 
			catch (Exception e) 
			{
				LOGGER.log(Level.SEVERE, "EN VentanaConfiguracion btn_salvar_conexion Exception " + e.getMessage() + " " + e.fillInStackTrace() + " PROGRAMA FINALIZADO. - L186" );
				System.exit( 0 );
			}
		}
	}*/
}