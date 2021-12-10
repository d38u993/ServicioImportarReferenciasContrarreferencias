package consultas;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.cj.xdevapi.Type;

import Conexion.ConexionSQL2008;

public class ConsultasSQL2008R2 
{
	ConexionSQL2008 conexion;
	
	private final static Logger LOGGER = Logger.getLogger(ConsultasSQL2008R2.class.getName());
	
	public ConsultasSQL2008R2( String servidor, String puerto, String nombre_base_datos, String usuario, String clave )
	{
		conexion = new ConexionSQL2008( servidor , puerto , nombre_base_datos , usuario , clave );
		
		try 
		{
			conexion.crearConexionSQL();
		} 
		catch (ClassNotFoundException e) 
		{
			LOGGER.log(Level.SEVERE, "EN Consultas Consultas ClassNotFoundException \n\n" + e.getMessage() + " " + e.fillInStackTrace(), " SITEMA FINALIZADO - L29" );
			System.exit( 0 );
		} 
	}
	
	public ConexionSQL2008 getConexion()
	{
		return conexion;
	}
	
	public boolean eliminarDatosPaciente( String curp, int opcion )
	{
		try
		{			
			conexion.getConexion().setAutoCommit(false);
			
			switch( opcion )
			{
				case 1://BORRO LOS CURPS GENERADOS DEL PACIENTE
				{
					conexion.setSetenciaPreparada( 
							conexion.getConexion().prepareStatement( 
									"DELETE det_pacientecurpgenerado " ) );
				}
				break;
				case 2://BORRO LOS DOMICILIOS DEL PACIENTE
				{
					conexion.setSetenciaPreparada( 
							conexion.getConexion().prepareStatement( 
									"DELETE det_domiciliopacientes WHERE idpaciente = ? " ) );
				}
				break;
				case 3://BORRO LOS TELEFONOS DEL PACIENTE
				{
					conexion.setSetenciaPreparada( 
							conexion.getConexion().prepareStatement( 
									"DELETE det_telefonospacientes WHERE idpaciente = ?" ) );
				}
				break;
				case 4://BORRO LOS TUTORES DEL PACIENTE
				{
					conexion.setSetenciaPreparada( 
							conexion.getConexion().prepareStatement( 
									"DELETE det_pacientestutor WHERE idpaciente = ? " ) );
				}
				break;
				case 5://BORRO LAS DERECHOHABIENCIAS QUE TENIA
				{
					conexion.setSetenciaPreparada( 
							conexion.getConexion().prepareStatement( 
									"DELETE det_pacientes_derechohabiencia WHERE idpaciente = ?  " ) ); 
				}
				break;
				case 6://BORRO LA INSTITUCION CON CONVENIO DE DERECHOHABIENCIA
				{
					conexion.setSetenciaPreparada( 
							conexion.getConexion().prepareStatement( 
									"DELETE det_PacientesTipo WHERE idpaciente = ?  " ) );
				}
				break;
				case 7://BORRO LA INSTITUCION CON CONVENIO DE DERECHOHABIENCIA
				{
					conexion.setSetenciaPreparada( 
							conexion.getConexion().prepareStatement( 
									"DELETE Vigencias WHERE idpaciente = ?  " ) );
				}
				break;
			}
			
			if( opcion != 1 )
			{
				conexion.getSetenciaPreparada().setString( 1, curp );
			}
			
			conexion.ejecutarConsultaModificar();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS eliminarDatosPaciente SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L107" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS eliminarDatosPaciente NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L112" );
			System.exit( 0 );
		}
		
		return conexion.getConsultaExitosa();
	}
	
	public boolean grabarPaciente( Object[] fila )
	{
		try 
		{
			conexion.getConexion().setAutoCommit(false);
			
			CallableStatement cStmt = conexion.getConexion().prepareCall("{CALL CTL_SPMttoCtl_Pacientes(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"); 
			
				cStmt.setObject( "IDPaciente", fila[ 0 ] );//@IDPaciente
				//System.out.println(fila[0]);
				cStmt.setObject( "iDUMedica", fila[ 3 ] );//@iDUMedica
				//System.out.println(fila[3]);
				cStmt.setObject( "cPaterno", fila[ 4 ] );//@cPaterno
				//System.out.println(fila[4]);
				cStmt.setObject( "cMaterno", fila[ 5 ] );//@cMaterno
				//System.out.println(fila[5]);
				cStmt.setObject( "cNombre", fila[ 6 ] );//@cNombre
				//System.out.println(fila[6]);
				cStmt.setObject( "dFechaNacimiento",fila[7], Types.DATE );//@dFechaNacimiento
				//System.out.println(fecha);
				cStmt.setObject( "nEdadAproximada", fila[ 22 ] );//@nEdadAproximada
				//System.out.println(fila[22]);
				cStmt.setObject( "cEdoNacimiento", fila[ 8 ] );//@cEdoNacimiento
				//System.out.println(fila[8]);
				cStmt.setObject( "cSexo", fila[ 9 ] );//@cSexo
				//System.out.println(fila[9]);
				cStmt.setObject( "cGrupoSanguineo", fila[ 10 ] );//@cGrupoSanguineo
				//System.out.println(fila[10]);
				cStmt.setObject( "cFactorRH", fila[ 11 ] );//@cFactorRH
				//System.out.println(fila[11]);
				cStmt.setObject( "IDEscolaridad", fila[ 13 ] );//@IDEscolaridad
				//System.out.println(fila[13]);
				cStmt.setObject( "IDEdoCivil", fila[ 14 ] );//@IDEdoCivil
				//System.out.println(fila[14]);
				cStmt.setObject( "IDReligion", fila[ 15 ] );//@IDReligion
				//System.out.println(fila[15]);
				cStmt.setObject( "IDOcupacion", fila[ 16 ] );//@IDOcupacion
				//System.out.println(fila[16]);
				cStmt.setObject( "IDPersonal", fila[ 18 ] );//@IDPersonal
				//System.out.println(fila[18]);
				cStmt.setObject( "nDesconocido", fila[ 2 ] );//@nDesconocido
				//System.out.println(fila[2]);
				cStmt.setObject( "nIndigena", fila[ 23 ] );//@nIndigena
				//System.out.println(fila[23]);
				cStmt.setObject( "nForaneo", fila[ 24 ] );//@nForaneo
				//System.out.println(fila[24]);
				cStmt.setObject( "SeConsideraIndigena", fila[ 25 ] );//@SeConsideraIndigena
				//System.out.println(fila[25]);
				cStmt.setObject( "HablaEspañol", fila[ 26 ] );//@HablaEspanol
				//System.out.println(fila[26]);
				cStmt.setObject( "HablaOEntiendeAlgunaLenguaIndigena", fila[ 27 ] );//@HablaOEntiendeAlgunaLenguaIndigena
				//System.out.println(fila[27]);
				cStmt.setObject( "idLenguaIndigena", fila[ 28 ] );//@idLenguaIndigena
				//System.out.println(fila[28]);
				
				boolean l = cStmt.execute();
				
				cStmt.close();
				System.out.println("=========" +  l +"=============");
					
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS grabarPaciente SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L159" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS grabarPaciente NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L164" );
			System.exit( 0 );
		}
		
		return conexion.getConsultaExitosa();
	}
	
	public boolean insertarDomicilioPaciente( Object[] fila )
	{
		try
		{		
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"INSERT INTO  det_domiciliopacientes (IDPaciente, cCalle, cNumExterior, cNumInterior, cEntreCalles, cCP, IDEstado, IDJurisdiccion, "
							+ "IDMunicipio, IDLocalidad, IDColonia, cColonia) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"
					));
			
				conexion.getSetenciaPreparada().setObject( 1, fila[29] );//IDPaciente
				conexion.getSetenciaPreparada().setObject( 2, fila[30] );//cCalle
				conexion.getSetenciaPreparada().setObject( 3, fila[31] );//cNumExterior
				conexion.getSetenciaPreparada().setObject( 4, fila[32] );//cNumInterior
				conexion.getSetenciaPreparada().setObject( 5, fila[33] );//cEntreCalles
				conexion.getSetenciaPreparada().setObject( 6, fila[34] );//cCP
				conexion.getSetenciaPreparada().setObject( 7, fila[35] );//IDEstado
				conexion.getSetenciaPreparada().setObject( 8, fila[36] );//IDJurisdiccion
				conexion.getSetenciaPreparada().setObject( 9, fila[37]  );//IDMunicipio
				conexion.getSetenciaPreparada().setObject(10, fila[38] );//IDLocalidad
				conexion.getSetenciaPreparada().setObject( 11, fila[39] );//IDColonia
				conexion.getSetenciaPreparada().setObject( 12, fila[40] );//cColonia
						
			conexion.ejecutarConsultaModificar();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS insertarDomilicioPaciente SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L240" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS insertarDomilicioPaciente NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L245" );
			System.exit( 0 );
		}
		
		return conexion.getConsultaExitosa();
	}
	public boolean insertarReferenciaPaciente( Object[] fila )
	{
		try
		{		
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"INSERT INTO referencias(idReferencia, IDPaciente, IDUMedicaEnvia, "
									+ "	IDUmedicaRef, cDescripcionRef, cServicio, cMotivoReferencia, cResumenClinico, "
									+ "	cTratamiento, cFecha, cStatus, cCompleta,idmotivo,curgente,cNombreMedico) "
									+ "	VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
							));
			
			conexion.getSetenciaPreparada().setObject( 1, fila[0] );//idReferencia
			conexion.getSetenciaPreparada().setObject( 2, fila[1] );//IDPaciente
			conexion.getSetenciaPreparada().setObject( 3, fila[2] );//IDUMedicaEnvia
			conexion.getSetenciaPreparada().setObject( 4, fila[3] );//IDUmedicaRef
			conexion.getSetenciaPreparada().setObject( 5, fila[4] );//cDescripcionRef
			conexion.getSetenciaPreparada().setObject( 6, fila[5] );//cServicio
			conexion.getSetenciaPreparada().setObject( 7, fila[7] );//cMotivoReferencia
			conexion.getSetenciaPreparada().setObject( 8, fila[8] );//cResumenClinico
			conexion.getSetenciaPreparada().setObject( 9, fila[9]  );//cTratamiento
			conexion.getSetenciaPreparada().setObject(10, fila[10], Types.DATE);//cFecha
			conexion.getSetenciaPreparada().setObject( 11, fila[12] );//cStatus
			conexion.getSetenciaPreparada().setObject( 12, fila[14] );//cCompleta
			conexion.getSetenciaPreparada().setObject( 13, fila[13] );//idmotivo
			conexion.getSetenciaPreparada().setObject( 14, fila[18] );//curgente
			conexion.getSetenciaPreparada().setObject( 15, fila[19] );//cNombreMedico
			
			conexion.ejecutarConsultaModificar();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS insertarReferenciaPaciente SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L240" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS insertarReferenciaPaciente NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L245" );
			System.exit( 0 );
		}
		
		return conexion.getConsultaExitosa();
	}

	public boolean existeReferenciaPaciente( Object[] fila )
	{
		boolean existe = false;
		
		try
		{		
			ResultSet resultado;
			
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"SELECT IDReferencia, IDPaciente, IDUMedicaEnvia, IDUMedicaRef, cDescripcionRef, cServicio, cDiagnostico, cMotivoReferencia, cResumenClinico,"
							+ " cTratamiento, cFecha, IDMedico, cStatus, IdMotivo, cCompleta, dFecha_Cancelacion, IdPersonal_Cancela, cMotivo_Cancelacion, cUrgente, cNombreMedico, dFechaPrimeraAtencion, cStatusEnviado"
							+ " FROM referencias WHERE IDReferencia = ? AND IDUMedicaEnvia = ?"
							));
			
			conexion.getSetenciaPreparada().setObject( 1, fila[0] );//idReferencia
			conexion.getSetenciaPreparada().setObject( 2, fila[2] );//IDUMedicaEnvia

			resultado = conexion.ejecutarConsultaObtener();
			
			if( resultado.next() )
			{
				existe = true;
			}
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS existeReferenciaPaciente SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L240" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS existeReferenciaPaciente NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L245" );
			System.exit( 0 );
		}
		
		return existe;
	}

	public boolean existePaciente( Object[] fila )
	{
		boolean existe = false;
		
		try
		{		
			ResultSet resultado;
			
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"SELECT IDPaciente FROM CTL_Pacientes WHERE IDPaciente = ?"
							));
			
			conexion.getSetenciaPreparada().setObject( 1, fila[0] );//idReferencia
			
			resultado = conexion.ejecutarConsultaObtener();
			
			if( resultado.next() )
			{
				existe = true;
			}
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS existePaciente SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L240" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS existePaciente NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L245" );
			System.exit( 0 );
		}
		
		return existe;
	}
	
	public boolean insertarDiagnosticoReferencia( Object[] fila )
	{
		try
		{		
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"INSERT INTO det_referencias_diagnosticos(IDReferencia,IdUMedica,IdConsecutivo,IdDiagnostico,cmotivoReferencia) values (?,?,?,?,?)"
					));
			
					conexion.getSetenciaPreparada().setObject( 1, fila[ 23 ] );
					conexion.getSetenciaPreparada().setObject( 2, fila[ 24 ] );
					conexion.getSetenciaPreparada().setObject( 3, fila[ 25 ] );
					conexion.getSetenciaPreparada().setObject( 4, fila[ 26 ] );
					conexion.getSetenciaPreparada().setObject( 5, fila[ 27 ] );
			
			conexion.ejecutarConsultaModificar();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS insertarDiagnosticoReferencia SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L275" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS insertarDiagnosticoReferencia NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L280" );
			System.exit( 0 );
		}
		
		return conexion.getConsultaExitosa();
	}

	public boolean insertarPacienteTutor( ArrayList<Object[]> datos )
	{
		try
		{		
			int indice = 1;
			
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"INSERT INTO det_pacientestutor VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"
							));
			
			for( Object fila[] : datos)
			{
				for( Object valores : fila )
				{
					conexion.getSetenciaPreparada().setObject( indice, valores );
				}
			}
			
			conexion.ejecutarConsultaModificar();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS insertarPacienteTutor SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L270" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS insertarPacienteTutor NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L275" );
			System.exit( 0 );
		}
		
		return conexion.getConsultaExitosa();
	}
	
	
	public boolean insertarDerechohabienciaPaciente( ArrayList<Object[]> datos )
	{
		try
		{		
			int indice = 1;
			
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"INSERT INTO det_pacientes_derechohabiencia VALUES ( ?, ? )"
							));
			
			for( Object fila[] : datos)
			{
				for( Object valores : fila )
				{
					conexion.getSetenciaPreparada().setObject( indice, valores );
				}
			}
			
			conexion.ejecutarConsultaModificar();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS insertarDerechohabienciaPaciente SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L293" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS insertarDerechohabienciaPaciente NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L298" );
			System.exit( 0 );
		}
		
		return conexion.getConsultaExitosa();
	}
	
	public boolean insertarPacienteTipo( ArrayList<Object[]> datos )
	{
		try
		{		
			int indice = 1;
			
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"INSERT INTO det_PacientesTipo VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )"
							));
			
			for( Object fila[] : datos)
			{
				for( Object valores : fila )
				{
					conexion.getSetenciaPreparada().setObject( indice, valores );
				}
			}
			
			conexion.ejecutarConsultaModificar();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS insertarPacienteTipo SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L330" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS insertarPacienteTipo NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L335" );
			System.exit( 0 );
		}
		
		return conexion.getConsultaExitosa();
	}
	
	public boolean insertarVigenciaTipoPaciente( ArrayList<Object[]> datos )
	{
		try
		{		
			int indice = 1;
			
			conexion.getConexion().setAutoCommit(false);
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
							"INSERT INTO Vigencias VALUES ( ?, ?, ?, ? )"
							));
			
			for( Object fila[] : datos)
			{
				for( Object valores : fila )
				{
					conexion.getSetenciaPreparada().setObject( indice, valores );
				}
			}
			
			conexion.ejecutarConsultaModificar();
		}
		catch (SQLException ex) 
		{
			LOGGER.log(Level.SEVERE, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS insertarVigenciaTipoPaciente SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L367" );
			System.exit( 0 );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, "EN CONSULTAS insertarVigenciaTipoPaciente NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L372" );
			System.exit( 0 );
		}
		
		return conexion.getConsultaExitosa();
	}
	/*
	public int contarReferenciasNulas()
	{
		int contadorNull = 0;

		try 
		{
			conexion.getConexion().setAutoCommit(false);
			
			ResultSet resultado;
			
			conexion.setSetenciaPreparada( 
					conexion.getConexion().prepareStatement( 
						"SELECT COUNT(*)  FROM Referencias WHERE IDMedico = ? " 
						) 
				);
		
			conexion.getSetenciaPreparada().setObject( 1, "NULL" );
			
			resultado = conexion.ejecutarConsultaObtener();
			
			if( ! resultado.wasNull() )
			{
				contadorNull = resultado.getRow();
			}
		} 
		catch (SQLException ex) 
		{
			LOGGER.log(Level.WARNING, "ERROR DE EJECUCI\u00D3N DEL QUERY CONSULTAS contarReferenciasNulas SQLException \n\n" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode() + " - L536" );
		} 
		catch( NullPointerException e)
		{
			LOGGER.log(Level.SEVERE, " EN CONSULTAS contarReferenciasNulas NullPointerException \n\n" + e.getMessage() + " " + e.fillInStackTrace() + " SISTEMA FINALIZADO - L540" );
			System.exit( 0 );
		}
		
		return contadorNull;
	}*/
	
	public void cerrarSetenciaPreparadaSQL()
	{
		try 
		{
			if( conexion != null )
			{
				conexion.getSetenciaPreparada().close();
			}
		} 
		catch (SQLException e) 
		{
			LOGGER.log(Level.WARNING, "EN CONSULTAS cerrarSetenciaPreparadaSQL SQLException \n\n" + e.getMessage() + " " + e.getSQLState() + " " + e.getErrorCode() + " - L558" );
		}
	}
	
	public void cerrarConexionSQL()
	{
		conexion.cerrarConexion();
	}
	
	public void com()
	{
		conexion.aceptarSentencias();
	}
}