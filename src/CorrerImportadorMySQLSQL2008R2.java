import java.io.IOException;

import com.vogella.logger.MyLogger;

public class CorrerImportadorMySQLSQL2008R2 {

	public static void main(String[] args) {
		try {
		      MyLogger.setup();
		    } catch (IOException e) {
		      e.printStackTrace();
		      throw new RuntimeException("Problems with creating the log files");
		    }
		VentanaConfiguracion configuracion = new VentanaConfiguracion();	
	}
}