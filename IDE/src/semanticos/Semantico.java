package semanticos;

import fabrica.Nodo;
import ide.Const;

public class Semantico {
    public static Objeto ejecutarValor(Nodo valor)
    {
        Objeto res = new Objeto();
        
        return res;
    }
    
    public static int getTipo(String tipo)
    {
        switch(tipo)
        {
            case Const.numero:
                return Const.tnumero;
            case Const.decimal:
                return Const.tdecimal;
            case Const.cadena:
                return Const.tcadena;
            case Const.caracter:
                return Const.tcaracter;
            case Const.bool:
                return Const.tbool;
        }
        return Const.tals;
    }
    
    public static String getTipo(int tipo)
    {
        switch(tipo)
        {
            case Const.tnumero:
                return Const.numero;
            case Const.tdecimal:
                return Const.decimal;
            case Const.tcadena:
                return Const.cadena;
            case Const.tcaracter:
                return Const.caracter;
            case Const.tbool:
                return Const.bool;
        }
        return Const.als;
    }
}
