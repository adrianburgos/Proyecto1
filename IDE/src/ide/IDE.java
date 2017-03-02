package ide;

import Generadores.GenerarArchivos;
import java.util.Locale;

public class IDE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GenerarArchivos.generarLexico("src/Analisis/LexicoHaskellTerminal.jflex");
        GenerarArchivos.generarSintactico();
        Principal principal = new Principal();
        principal.setVisible(true);
    }
    
}
