package consultas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import Conexion.ConexionMySQL;

public class ConsultasMySQL {

	ConexionMySQL conexion;
	
	private final static Logger LOGGER = Logger.getLogger(ConsultasMySQL.class.getName());
	
	public ConsultasMySQL( String servidor, String puerto, String bd, String usuario, String clave )
	{
		conexion = new ConexionMySQL( servidor, puerto, bd );
		
		conexion.crearConexionMySQL();
	} 
	
	public ConexionMySQL getConexionMySQL()
	{
		return conexion;
	}
	
	public int obtenerTotalRegistrosReferencia( String unidad_medica )
	{
		int contadorReferencias = 0;
		
		try 
		{
			conexion.getConexion().setAutoCommit( false );
			
			ResultSet resultado;
			
			conexion.setSetenciaPreparada( conexion.getConexion().prepareStatement(
					"SELECT COUNT( `IDReferencia` ) as Total FROM `referencias` WHERE `IDUMedicaRef` = ? "
					));
			
			conexion.getSetenciaPreparada().setString( 1, unidad_medica );
			
			resultado = conexion.ejecutarConsultaObtener();
			
			if( resultado.next() )
			{
				contadorReferencias = Integer.valueOf(resultado.getString(1)); //Integer.valueOf( resultado.getFetchSize() );
			}
			
		}
		catch( SQLException e )
		{
			LOGGER.log(Level.SEVERE, "EN ConsultasMySQL obtenerTotalRegistrosReferencia SQLException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L58");
			System.exit( 0 );
		}
		catch (NullPointerException e) 
		{
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " - L63" );
			System.exit( 0 );
		}
		
		return contadorReferencias;
	}
	
	public int obtenerUltimoIDRefencia( String unidad_medica )
	{
		int ultimo_id = 0;
		
		try
		{
			conexion.getConexion().setAutoCommit( false );
			
			ResultSet resultado;
			
			conexion.setSetenciaPreparada( conexion.getConexion().prepareStatement(
					"SELECT `id` FROM `referencias` WHERE `IDUMedicaRef` = ? AND `id` > 380 ORDER BY `id` DESC"
					));
			
			conexion.getSetenciaPreparada().setString( 1, unidad_medica );
			
			resultado = conexion.ejecutarConsultaObtener();
			
			ultimo_id = resultado.getInt( "id" );
		}
		catch( SQLException e)
		{
			LOGGER.log(Level.SEVERE, " EN ConsultasMySQL obtenerUltimoIDRefencia SQLException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L92");
			System.exit( 0 );
		}
		catch (NullPointerException e) 
		{
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " - L97" );
			System.exit( 0 );
		}
		
		return ultimo_id;
	}
	//Me quede en obtener las filas nuevas
	public ArrayList<Object[]> obtenerReferenciasNuevas( int inicioValor, String unidadMedica )
	{
		ArrayList<Object[]> valores = new ArrayList<>();
		
		ResultSet resultado;
		
		try
		{
			conexion.setSetenciaPreparada( conexion.getConexion().prepareStatement(
					"SELECT ref.`id`, ref.`IDReferencia`, ref.`IDPaciente`, ref.`IDUMedicaEnvia`, ref.`IDUMedicaRef`, ref.`cDescripcionRef`, ref.`cServicio`, ref.`cDiagnostico`, ref.`cMotivoReferencia`, ref.`cResumenClinico`, ref.`cTratamiento`, ref.`cFecha`, "
							+ " ref.`IDMedico`, ref.`cStatus`, ref.`IdMotivo`, ref.`cCompleta`, ref.`dFecha_Cancelacion`, ref.`IdPersonal_Cancela`, ref.`cMotivo_Cancelacion`, ref.`cUrgente`, ref.`cNombreMedico`, ref.`dFechaPrimeraAtencion`, ref.`cStatusEnviado`,"
							+ "refDiag.`idRefeDiag`, refDiag.`IdReferencia`, refDiag.`IdUMedica`, refDiag.`IdConsecutivo`, refDiag.`IdDiagnostico`, refDiag.`cMotivoReferencia` "
							+ "FROM `referencias` ref, `det_referencias_diagnosticos` refDiag "
							+ "WHERE ref.id > ? AND ref.IDUMedicaRef = ? AND ref.`IDReferencia` = refDiag.`IDReferencia` AND ref.IDUMedicaEnvia = refDiag.idUMedica"
			));
			
			conexion.getSetenciaPreparada().setInt( 1 , inicioValor );
			conexion.getSetenciaPreparada().setString( 2 , unidadMedica );
			
			resultado = conexion.ejecutarConsultaObtener();
			
			while( resultado.next() )
			{
				valores.add( new Object[]
						{
							resultado.getString( 2 ) , resultado.getString( 3 ) , resultado.getString( 4 ) , //2
							resultado.getString( 5 ) , resultado.getString( 6 ),resultado.getString( 7 ), //5
							resultado.getString( 8 ), resultado.getString( 9 ),resultado.getString( 10 ),//8
							resultado.getString( 11 ),resultado.getString( 12 ),resultado.getString( 13 ),//11
							resultado.getString( 14 ),resultado.getString( 15 ),resultado.getString( 16 ),//14
							resultado.getString( 17 ),resultado.getString( 18 ),resultado.getString( 19 ),//17
							resultado.getString( 20 ),resultado.getString( 21 ),resultado.getString( 22 ),//20
							resultado.getString( 23 ),resultado.getString( 24 ), resultado.getString( 25 ), 
							resultado.getString( 26 ), resultado.getString( 27 ), resultado.getString( 28 ),
							resultado.getString( 29 )
						} );
			}
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL SQLException \n\n" + ex.getSQLState() + " " + ex.fillInStackTrace() + " - L141" );
			System.exit( 0 );
		}
		catch (NullPointerException e) 
		{
			LOGGER.log(Level.SEVERE, "EN ConexionMySQL ClassNotFoundException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " - L146" );
			System.exit( 0 );
		}
		
		return valores;
	}
	
	public ArrayList<Object[]> obtenerPacientes( ArrayList<Object[]> datos )
	{
		ArrayList<Object[]> pacientesDomicilio = new ArrayList<Object[]>();
		
		try 
		{
			ResultSet resultado;
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"SELECT DISTINCT pac.IDPaciente, pac.nCURPGenerado, pac.nDesconocido, pac.IDUMedica, pac.cPaterno, pac.cMaterno, pac.cNombre, pac.dFechaNacimiento, pac.cEdoNacimiento, pac.cSexo, pac.cGrupoSanguineo, pac.cFactorRH,"
									+ "pac.cSituacion, pac.IDEscolaridad, pac.IDEdoCivil, pac.IDReligion, pac.IDOcupacion, pac.dFechaRegistro, pac.IDPersonal, pac.cStatus,pac.cclavepaciente_anterior,pac.dfecha_estudio_factor_y_grupo,"
									+ "pac.nEdadAproximada, pac.nIndigena,pac.nForaneo, pac.SeConsideraIndigena, pac.HablaEspanol, pac.HablaOEntiendeAlgunaLenguaIndigena, pac.idLenguaIndigena, dom.IDPaciente, dom.cCalle, dom.cNumExterior,dom.cNumInterior,"
									+ "dom.cEntreCalles, dom.cCP, dom.IDEstado, dom.IDJurisdiccion, dom.IDMunicipio, dom.IDLocalidad, dom.IDColonia, dom.cColonia "
									+ "  FROM CTL_Pacientes pac, Det_DomicilioPacientes dom"
									+ " WHERE pac.idpaciente = ? AND pac.IDPaciente = dom.IDPaciente" ));
			
			conexion.getConexion().setAutoCommit(false);
			
			for (Object[] curp : datos) 
			{
				conexion.getSetenciaPreparada().setObject( 1 , curp[ 1 ] );
				resultado = conexion.ejecutarConsultaObtener();
						
			
				while( resultado.next() )
				{
					pacientesDomicilio.add( new Object[] 
							{
								resultado.getString( 1 ),resultado.getString( 2 ),resultado.getString( 3 ),
								resultado.getString( 4 ),resultado.getString( 5 ),resultado.getString( 6 ),
								resultado.getString( 7 ),resultado.getString( 8 ),resultado.getString( 9 ),
								resultado.getString( 10 ),resultado.getString( 11 ),resultado.getString( 12 ),
								resultado.getString( 13 ),resultado.getString( 14 ),resultado.getString( 15 ),
								resultado.getString( 16 ),resultado.getString( 17 ),resultado.getString( 18 ),
								resultado.getString( 19 ),resultado.getString( 20 ),resultado.getString( 21 ),
								resultado.getString( 22 ),resultado.getString( 23 ),resultado.getString( 24 ),
								resultado.getString( 25 ),resultado.getString( 26 ),resultado.getString( 27 ),
								resultado.getString( 28 ),resultado.getString( 29 ),resultado.getString( 30 ),
								resultado.getString( 31 ),resultado.getString( 32 ),resultado.getString( 33 ),
								resultado.getString( 34 ),resultado.getString( 35 ),resultado.getString( 36 ),
								resultado.getString( 37 ),resultado.getString( 38 ),resultado.getString( 39 ),
								resultado.getString( 40 ),resultado.getString( 41 )
							} );
				}
			}
			
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY ConsultasMySQL obtenerPacientes SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L203" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, " EN ConsultasMySQL obtenerPacientes NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L208");
			System.exit( 0 );
		}
		
		return pacientesDomicilio;
	}
	
	public ArrayList<Object[]> obtenerTelefonos( ArrayList<Object[]> datos )
	{
		ArrayList<Object[]> telefonos = new ArrayList<>();
		
		try 
		{
			ResultSet resultado;
			
			
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"SELECT `IDPaciente`, `IDOrden`, `cDescripcion`, `cNumero` FROM `det_telefonospacientes` WHERE `IDPaciente` = ?" ) );
			
			for( Object[] fila : datos )
			{
				conexion.getSetenciaPreparada().setObject( 1, fila[ 1 ] );
			}
				
			resultado = conexion.ejecutarConsultaObtener();
			
			while( resultado.next() )
			{
				telefonos.add( new Object[] 
						{
							resultado.getString( 1 ),resultado.getString( 2 ),resultado.getString( 3 ),
							resultado.getString( 4 )
						}
				);
			}
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY obtenerTelefonos insertarPaciente SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L249");
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN ConsultasMySQL obtenerTelefonos NullPointerException \n\n" + e.getMessage() + "\n" + e.fillInStackTrace() + " SISTEMA FINALIZADO - L254");
			System.exit( 0 );
		}
		
		return telefonos;
	}
	
	public ArrayList<Object[]> obtenerPacienteTutor( ArrayList<Object[]> datos )
	{
		ArrayList<Object[]> pacienteTutor = new ArrayList<Object[]>();
		
		ResultSet resultado;
		
		try 
		{
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( "SELECT `IDPaciente`, `cNombre`, `nParentesco`, `cDomicilio`, `cTelefono`, `IDEstado`, `IDJurisdiccion`, `IDMunicipio`, `IDLocalidad`, `IDColonia`, `cColonia`, `nRenglon` "
															 + " FROM `det_pacientestutor` WHERE `IDPaciente` = ?" ) );
			
			for( Object[] fila : datos )
			{
				conexion.getSetenciaPreparada().setObject( 1, fila[ 1 ] );
				
				resultado = conexion.ejecutarConsultaObtener();
				
				if( resultado.next() )
				{
					pacienteTutor.add( new Object[] 
							{ 
								resultado.getString( 1 ),resultado.getString( 2 ),resultado.getString( 3 ),
								resultado.getString( 4 ),resultado.getString( 5 ),resultado.getString( 6 ),
								resultado.getString( 7 ),resultado.getString( 8 ),resultado.getString( 9 ),
								resultado.getString( 10 ),resultado.getString( 11 ),resultado.getString( 12 )
							} );
				}
			}
			
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY ConsultasMySQL existeSomatometriaMySQL SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L296");
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN ConsultasMySQL existeSomatometriaMySQL NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L301");
			System.exit( 0 );
		}
		
		return pacienteTutor;
	}
	
	public ArrayList<Object[]> obtenerPacienteDerechohabiencia( ArrayList<Object[]> datos )
	{
		ArrayList<Object[]> pacienteDerechohabiencia = new ArrayList<Object[]>();
		
		ResultSet resultado;
		
		try 
		{
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"SELECT `IDTipoDerechohabiente`, `IDPaciente`, `IDUmedica` FROM `det_pacientes_derechohabiencia` WHERE `IDPaciente` = ? AND `IDUmedica` = ?" ) );
			
			for( Object[] fila : datos )
			{
				conexion.getSetenciaPreparada().setObject( 1, fila[ 1 ] );				
				conexion.getSetenciaPreparada().setObject( 2, fila[ 2 ] );				
			}
			
			resultado = conexion.ejecutarConsultaObtener();
			
			while( resultado.next() )
			{
				pacienteDerechohabiencia.add( 
						new Object[] 
							{ 
								resultado.getString( 1 ),resultado.getString( 2 ),resultado.getString( 3 )
							}
						);
			}
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY ConsultasMySQL obtenerPacienteDerechohabiencia SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L342");
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN ConsultasMySQL obtenerPacienteDerechohabiencia NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L347");
			System.exit( 0 );
		}
		
		return pacienteDerechohabiencia;
	}

	public ArrayList<Object[]> obtenerPacienteTipo( ArrayList<Object[]> datos )
	{
		ArrayList<Object[]> pacienteTipo = new ArrayList<Object[]>();
		
		ResultSet resultado;
		
		try 
		{
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"SELECT `IDPaciente`, `IDTipoPaciente`, `cFolio1`, `cFolio2`, `nTipo`, `dFechaRegistro`, `nRegistroDesdePadron`, `CConvenio_principal`, `IDUmedica` FROM `det_pacientestipo` "
							+ "WHERE `IDPaciente` = ? AND `IDUmedica` = ?" ) );
			
			for( Object[] fila : datos )
			{
				conexion.getSetenciaPreparada().setObject( 1, fila[ 1 ] );				
				conexion.getSetenciaPreparada().setObject( 2, fila[ 2 ] );				
			}
			
			resultado = conexion.ejecutarConsultaObtener();
			
			while( resultado.next() )
			{
				pacienteTipo.add( 
						new Object[] 
								{ 
									resultado.getString( 1 ),resultado.getString( 2 ),resultado.getString( 3 ),
									resultado.getString( 4 ),resultado.getString( 5 ),resultado.getString( 6 ),
									resultado.getString( 7 ),resultado.getString( 8 )
								}
						);
			}
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY ConsultasMySQL obtenerPacienteTipo SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L391");
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN ConsultasMySQL obtenerPacienteTipo NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L396");
			System.exit( 0 );
		}
		
		return pacienteTipo;
	}

	public ArrayList<Object[]> obtenerVigenciaTipo( ArrayList<Object[]> datos )
	{
		ArrayList<Object[]> vigenciaTipo = new ArrayList<Object[]>();
		
		ResultSet resultado;
		
		try 
		{
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"SELECT `idPaciente`, `idTipoPaciente`, `dIniVigencia`, `dFinVigencia`, `IDUmedica` "
							+ "FROM `vigencias` WHERE `idPaciente` = ? AND `IDUmedica` = ? " ) );
			
			for( Object[] fila : datos )
			{
				conexion.getSetenciaPreparada().setObject( 1, fila[ 0 ] );				
				conexion.getSetenciaPreparada().setObject( 2, fila[ 7 ] );				
			}
			
			resultado = conexion.ejecutarConsultaObtener();
			
			while( resultado.next() )
			{
				vigenciaTipo.add( 
						new Object[] 
								{ 
									resultado.getString( 1 ),resultado.getString( 2 ),resultado.getString( 3 ),
									resultado.getString( 4 ),resultado.getString( 5 )
								}
						);
			}
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY ConsultasMySQL obtenerPacienteTipo SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L439");
			System.exit( 0 );
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN ConsultasMySQL obtenerPacienteTipo NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L444");
			System.exit( 0 );
		}
		
		return vigenciaTipo;
	}
	
	/*
	
	public ArrayList<Object[]> obtenerReferenciaDiagnostico( ArrayList<Object[]> datos )
	{
		ArrayList<Object[]> vigenciaTipo = new ArrayList<Object[]>();
		
		ResultSet resultado;
		
		try 
		{
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"SELECT `IdReferencia`, `IdUMedica`, `IdConsecutivo`, `IdDiagnostico`, `cMotivoReferencia` "
							+ " FROM `det_referencias_diagnosticos` WHERE `IdReferencia` = ?" ) );
			
			for( Object[] fila : datos )
			{
				conexion.getSetenciaPreparada().setObject( 1, fila[ 0 ] );				
			}
			
			resultado = conexion.ejecutarConsultaObtener();
			
			while( resultado.next() )
			{
				vigenciaTipo.add( 
						new Object[] 
								{ 
									resultado.getString( 1 ),resultado.getString( 2 ),resultado.getString( 3 ),
									resultado.getString( 4 )
								}
						);
			}
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY ConsultasMySQL obtenerReferenciaDiagnostico SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L483");
			System.exit(0);
		}
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN ConsultasMySQL obtenerReferenciaDiagnostico NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L487");
			System.exit( 0 );
		}
		
		return vigenciaTipo;
	}
	/*
	public Vector<Object[]> existeContrarreferenciaMySQL( Vector<Object[]> datos )
	{
		Vector<Object[]> nuevoVector = new Vector<Object[]>();
		
		try 
		{
			conexion.getConexion().setAutoCommit(false);
			
			ResultSet resultado;
			
			
			for (Object[] objects : datos) 
			{
				conexion.setSetenciaPreparada( 
						conexion.getConexion().prepareStatement( 
								"SELECT * FROM contrareferencias WHERE IDUMedica = ? AND IDContraReferencia = ? AND IDReferencia = ?" 
								) 
						);
				
				conexion.getSetenciaPreparada().setObject( 1, objects[ 0 ] );
				conexion.getSetenciaPreparada().setObject( 2, objects[ 1 ] );
				conexion.getSetenciaPreparada().setObject( 3, objects[ 8 ] );
				
				resultado = conexion.ejecutarConsultaObtener();
				
				if( ! resultado.next() )
				{
					nuevoVector.addElement( objects );
				}
			}
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.WARNING, "ERROR DE EJECUCI\u00D3N DEL QUERY ConsultasMySQL existeContrarreferenciaMySQL SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L321");
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, " EN ConsultasMySQL existeContrarreferenciaMySQL NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEA FINALIZADO - L325");
			System.exit( 0 );
		}
		
		return nuevoVector;
	}*/
	
	
	public void cerrarConexionMySQL()
	{
		conexion.cerrarConexion();
	}
	
	public void comMySQL()
	{
		conexion.aceptarSentencias();
	}
}