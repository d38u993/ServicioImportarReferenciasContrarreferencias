import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import consultas.ConsultasMySQL;
import consultas.ConsultasSQL2008R2;

/**
 * @see http://www.jc-mouse.net/
 * @author mouse
 */
public class MySystemTray {
    private Timer timer;        
    
    private String mysql_clave = ""; 
    private String mysql_usuario = "";
    private String mysql_bd = ""; 
    private String mysql_puerto = ""; 
    private String mysql_servidor = ""; 
    private String sql2008_clave = ""; 
    private String sql2008_usuario = ""; 
    private String sql2008_bd = ""; 
    private String sql2008_puerto = ""; 
    private String sql2008_servidor = "";  
    private String unidadMedica = "";
    
    public MySystemTray(String sql2008_servidor, String sql2008_puerto, String sql2008_bd, String sql2008_usuario, String sql2008_clave, String mysql_servidor, String mysql_puerto, String mysql_bd, String mysql_usuario, String mysql_clave, int tiempoBusqueda, String unidadMedica)
    {
    	this.mysql_servidor = mysql_servidor; 
    	this.mysql_puerto = mysql_puerto; 
    	this.mysql_bd = mysql_bd; 
    	this.mysql_usuario = mysql_usuario;
    	this.mysql_clave = mysql_clave; 
    	this.sql2008_servidor = sql2008_servidor;  
    	this.sql2008_puerto = sql2008_puerto; 
    	this.sql2008_bd = sql2008_bd; 
    	this.sql2008_usuario = sql2008_usuario; 
    	this.sql2008_clave = sql2008_clave; 
    	this.unidadMedica = unidadMedica; 
        
		//Se inicia una tarea cuando se minimiza           
		if(timer!=null) timer.cancel();
		timer = new Timer();           
		timer.schedule(new MyTimerTask(),2000, Integer.valueOf( tiempoBusqueda ) );//Se ejecuta cada 10 segundos
	}

    /**
     * clase interna que manejara una accion en segundo plano
     */
    class MyTimerTask extends TimerTask 
    {
    	private ConsultasSQL2008R2 consulta;
    	private ConsultasMySQL consultaMySQL;
    	
    	private Propiedades propertiSQL = new Propiedades();
    	private Propiedades propertiMySQL = new Propiedades();
    	
    	private final Logger LOGGER = Logger.getLogger(MyTimerTask.class.getName());
        
        public MyTimerTask()
        {
        	propertiSQL.cargarArchivoPropertie( "E:/eclipse-workspace/properties/Importador_MYSQL_SQL2008R2_configuracion.properties" );
        	propertiMySQL.cargarArchivoPropertie( "E:/eclipse-workspace/properties/ServicioImportarReferenciasContrarreferencias_propiedad_estado_BD.properties" );
        }

        @Override
        public void run() {  
           actionBackground();
        }

        /**
         * accion a realizar cuando la aplicacion a sido minimizada
         */
        public void actionBackground()
        { 
        	consulta = new ConsultasSQL2008R2( sql2008_servidor ,sql2008_puerto,sql2008_bd,sql2008_usuario, sql2008_clave );
        	consultaMySQL = new ConsultasMySQL( mysql_servidor , mysql_puerto, mysql_bd, mysql_usuario, mysql_clave );

        	int total_referencias = 0;
        	int ultimo_id_referencia = 0;
        	
        	total_referencias = consultaMySQL.obtenerTotalRegistrosReferencia( propertiSQL.getPropiedad().getProperty( "unidad_medica" ) );
			System.out.println( total_referencias+ " "+ propertiMySQL.getPropiedad().getProperty( "ultimo_conteo_referencias" ) + " " + propertiSQL.getPropiedad().getProperty( "unidad_medica" ));
        	if( total_referencias > Integer.valueOf( propertiMySQL.getPropiedad().getProperty( "ultimo_conteo_referencias" ) )  )
        	{
        		ArrayList<Object[]> referencias = new ArrayList<>();
        		ArrayList<Object[]> referenciasDiagnostico = new ArrayList<>();
        		ArrayList<Object[]> pacientesDomicilio = new ArrayList<>();
        		ArrayList<Object[]> pacientesTelefonos = new ArrayList<>();
        		ArrayList<Object[]> pacienteTutor = new ArrayList<>();
        		ArrayList<Object[]> pacienteDerechohabiencia = new ArrayList<>();
        		ArrayList<Object[]> pacienteTipo = new ArrayList<>();
        		ArrayList<Object[]> pacienteVigencia = new ArrayList<>();
        		
        		//crear conexion al servidor en la red ( 201.147.242.117 )
        		        		
        		//obtener el valor del ultimo ID del conteo total de referencias ( obtener el valor ID mas grande del conjunto obtenido de la consulta anterior obtenerTotalRegistrosReferencia )
        		//ultimo_id_referencia = consultaMySQL.obtenerUltimoIDRefencia( propertiSQL.getPropiedad().getProperty( "unidad_medica" ) );//2750
        		
        		//obtener las referencias encontradas nuevas de la consulta
        		referencias = consultaMySQL.obtenerReferenciasNuevas( Integer.valueOf( propertiMySQL.getPropiedad().getProperty( "ultimo_conteo_referencias" ) ) , propertiSQL.getPropiedad().getProperty( "unidad_medica" ) );
        		//obtener los diagnoticos de las referencias
        		//referenciasDiagnostico = consultaMySQL.obtenerReferenciaDiagnostico( referencias );
        		//obtener los pacientes
        		pacientesDomicilio = consultaMySQL.obtenerPacientes( referencias );
        		//obtener telefonos
        		pacientesTelefonos = consultaMySQL.obtenerTelefonos( referencias );
        		//obtener paciente tutor
        		pacienteTutor = consultaMySQL.obtenerPacienteTutor( referencias );
        		//obtener derechohabiencia
        		pacienteDerechohabiencia = consultaMySQL.obtenerPacienteDerechohabiencia( referencias );
        		//obtener el tipo de paciente
        		pacienteTipo = consultaMySQL.obtenerPacienteTipo( referencias );
        		//obtener la vigencias del tipo de paciente
        		pacienteVigencia = consultaMySQL.obtenerVigenciaTipo( pacienteTipo );
        		
        		//se guarda el numero del total de referencias encontradas
        		propertiMySQL.getPropiedad().setProperty( "ultimo_conteo_referencias", String.valueOf( total_referencias ) );
        		//propertiMySQL.getPropiedad().setProperty( "ultimo_id_referencia", String.valueOf( ultimo_id_referencia ) );
        		
        		boolean correcto = true;
        		
        		for( Object[] fila : referencias )
        		{
        			correcto = correcto && consulta.eliminarDatosPaciente( "", 1 );
        			correcto = correcto && consulta.eliminarDatosPaciente( String.valueOf( fila[ 1 ] ), 2 );
        			correcto = correcto && consulta.eliminarDatosPaciente( String.valueOf( fila[ 1 ] ), 3 );
        			correcto = correcto && consulta.eliminarDatosPaciente( String.valueOf( fila[ 1 ] ), 4 );
        			correcto = correcto && consulta.eliminarDatosPaciente( String.valueOf( fila[ 1 ] ), 5 );
        			correcto = correcto && consulta.eliminarDatosPaciente( String.valueOf( fila[ 1 ] ), 6 );
        			correcto = correcto && consulta.eliminarDatosPaciente( String.valueOf( fila[ 1 ] ), 7 );
        			
        		}
        		int indice = 1;
        		for( Object[] fila : pacientesDomicilio )
        		{
        			if( ! consulta.existePaciente( fila ) )
        			{
        				consulta.grabarPaciente( fila );
        				consulta.insertarDomicilioPaciente( fila );
        				consulta.com();
        			}
        		}

        		for( Object[] fila : referencias )
        		{
        			//no repetir las referencias ya que marcara error de clave primaria
        			if( consulta.existeReferenciaPaciente( fila ) == false )
        			{
        				consulta.insertarReferenciaPaciente( fila );
        				consulta.insertarDiagnosticoReferencia( fila );
        				consulta.com();
        			}
        			System.out.println("*****" + indice);
        			++indice;
        		}
        		/*
        		for( Object[] fila: referenciasDiagnostico)
        		{
        			consulta.insertarDiagnosticoReferencia( fila );
        			consulta.com();
        		}*/
        		//consultaMySQL.comMySQL();
        		propertiMySQL.salvarConfiguracion();
        	}
        	
			
        	
        	/*
			consultaMySQL.obtenerTotalRegistrosReferencia(mysql_bd);
			try 
			{
				Vector<Object[]> referencias;
				
				//Se comprueba si se hizo una nueva referencia obteniendo el CheckSum de la tabla Referencias
				//Ya que cada vez que hay un cambio en la tabla este CheckSum cambia
				totalReferencia = consulta.seActualizoReferencia();
				
				//Esta condicion se debe a que el CheckSum puede regresar un numero negativo
				//a si que eso lo convertimos a un numero positivo y a si sea siempre mayor a 0
				if( totalReferencia < 0 )
				{
					totalReferencia = totalReferencia * (-1);
				}
				
				consulta.com();
				
				if( totalReferencia != Long.parseLong( properti.getPropiedad().getProperty( "checksumReferencias" ) ) || 
					consulta.obtenerTotalRegistros( 3L ) != Long.parseLong( properti.getPropiedad().getProperty( "ultimoDetalleDiagnostico" )) ||
					consulta.obtenerTotalRegistros( 2L ) != Long.parseLong( properti.getPropiedad().getProperty( "ultimaContrareferencia" )) || 
					consulta.obtenerTotalRegistros( 4L ) != Long.parseLong( properti.getPropiedad().getProperty( "ultimoSamotemetria" )))
				{
					//Exporta una nueva Referencia
					totalRegistros = consulta.obtenerTotalRegistros( 1L );
					referencias = new Vector<Object[]>();
					
					if( totalRegistros > Integer.valueOf( properti.getPropiedad().getProperty( "ultimoRegistro" ) ) )
					{
						referencias = consulta.obtenerRegistrosNuevos( Long.parseLong( properti.getPropiedad().getProperty( "ultimoRegistro" ) ) , 1 );//30
						referencias = consultaMySQL.existeReferenciaMySQL( referencias );

						if( referencias.size() > 0 )
						{
							consultaMySQL.exportarMySQL( referencias , 1 );
							properti.getPropiedad().setProperty( "ultimoRegistro", String.valueOf( totalRegistros ) );
							consultaMySQL.insertarPaciente( consulta.obtenerPaciente( 2 , String.valueOf( referencias.get( 0 )[ 1 ] ) ) );
						}
						LOGGER.log(Level.FINE, referencias.size() + " REFERENCIAS NUEVAS FUERON IMPORTADAS. - L97" );
					}
					
					totalSomatometria = consulta.obtenerTotalRegistros( 4L );
					
					if( totalRegistros > Long.parseLong( properti.getPropiedad().getProperty( "ultimoSamotemetria" ) ) )
					{
						Vector<Object[]> somatometria = new Vector<Object[]>();
						
						somatometria = consulta.obtenerSomatometria( Long.parseLong( properti.getPropiedad().getProperty( "ultimoSamotemetria" ) ) );
						consultaMySQL.insertarSomatometria( consultaMySQL.existeSomatometriaMySQL( somatometria, consulta.obtenerUnidadMedica() ) );
						properti.getPropiedad().setProperty( "ultimoSamotemetria" , String.valueOf( totalSomatometria ) );
						LOGGER.log(Level.FINE, "FINALIZADO CORRECTAMENTE. - L109" );
					}
					
					totalRegistros = consulta.obtenerTotalRegistros( 2L );
					
					if( totalRegistros > Long.parseLong( properti.getPropiedad().getProperty( "ultimaContrareferencia" ) ) )
					{
						consultaMySQL.exportarMySQL( consultaMySQL.existeContrarreferenciaMySQL(consulta.obtenerRegistrosNuevos( Long.parseLong( properti.getPropiedad().getProperty( "ultimaContrareferencia" ) ) , 2 ) ), 2 );
						LOGGER.log(Level.FINE, totalRegistros - Integer.valueOf( properti.getPropiedad().getProperty( "ultimaContrareferencia" ) ) + " CONTRAREFERENCIAS NUEVAS FUERON IMPORTADAS. - L126" );
						properti.getPropiedad().setProperty( "ultimaContrareferencia", String.valueOf( totalRegistros ) );
					}
					
					totalRegistros = consulta.obtenerTotalRegistros( 3L );
					
					if( totalRegistros > Long.parseLong( properti.getPropiedad().getProperty( "ultimoDetalleDiagnostico" ) ) )
					{
						Long ultimaContrareferencia = Long.parseLong( properti.getPropiedad().getProperty( "ultimaContrareferencia" ) );
						consultaMySQL.exportarMySQL( consultaMySQL.existeDiagDetaContraMySQL( consulta.obtenerRegistrosNuevos( ultimaContrareferencia , 3 ) ) , 3 );
						LOGGER.log(Level.FINE, totalRegistros - Long.parseLong( properti.getPropiedad().getProperty( "ultimoDetalleDiagnostico" ) ) + " DETALLES DIAGN\u00D3STICO DE EGRESO NUEVAS FUERON IMPORTADAS - L127" );
						properti.getPropiedad().setProperty( "ultimoDetalleDiagnostico", String.valueOf( totalRegistros ) );
					}
					
					totalReferencia = consulta.seActualizoReferencia();
					
					if( totalReferencia < 0 )
					{
						totalReferencia = totalReferencia * (-1);
					}
					
					properti.getPropiedad().setProperty( "checksumReferencias" , String.valueOf( totalReferencia ) );
					
					consultaMySQL.getConexionMySQL().aceptarSentencias();
					consulta.getConexion().aceptarSentencias();
					
					if(consultaMySQL.getConexionMySQL().getConsultaExitosa() && consulta.getConexion().getConsultaExitosa())
					{
						properti.salvarConfiguracion();
					}
					
					consultaMySQL.cerrarConexionMySQL();
				}
				LOGGER.log(Level.FINE, " NADA QUE SUBIR. - L147" );
				consulta.cerrarSetenciaPreparadaSQL();
				consulta.cerrarConexionSQL();
			/*} 
			catch ( NullPointerException e) 
			{
				LOGGER.log(Level.SEVERE, "EN MyTimerTask actionBackground NullPointerException " + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L156");
				System.exit( 0 );
			}
			catch ( Exception e) 
			{
				LOGGER.log(Level.SEVERE, "EN MyTimerTask actionBackground Exception " + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L161");
				System.exit( 0 );
			}  */ 
        }
    }
}