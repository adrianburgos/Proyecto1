package semanticos;

import fabrica.Nodo;
import ide.Const;

public class Semantico {
    public static Objeto ejecutarValor(Nodo valor)
    {
        Objeto valIzq, valDer;
        Objeto res = new Objeto();
        switch(valor.getNombre())
        {
            case Const.mas:
            case Const.por:
            case Const.dividido:
            case Const.pot:
                valIzq = ejecutarValor(valor.hijos.get(0));
                valDer = ejecutarValor(valor.hijos.get(1));
                res = ejecutarAritmetica(valIzq, valDer, valor.getNombre());
                break;
            case Const.menos:
                break;
            default:
                //retornar el valor
                return new Objeto(valor.tipo, valor.valor);
        }
        return res;
    }
    
    private static Objeto ejecutarAritmetica(Objeto valIzq, Objeto valDer, String operador)
    {
        Objeto res = new Objeto();
        switch (operador)
        {
            case Const.mas:
                switch (valDer.tipo + valIzq.tipo)
                {
                    case 2://numero + numero
                        res.tipo = Const.tnumero;
                        res.valor = Double.valueOf(valIzq.valor) + Double.valueOf(valDer.valor) + "";
                        break;
                    case 4://numero + decimal
                    case 6://decimal + decimal
                        res.tipo = Const.tdecimal;
                        res.valor = Double.valueOf(valIzq.valor) + Double.valueOf(valDer.valor) + "";
                        break;
                    case 8://numero + caracter
                        res.tipo = Const.tnumero;
                        int num1 = 0;
                        int num2 = 0;
                        if(valIzq.tipo == 1)
                        {
                            num1 = Integer.valueOf(valIzq.valor);
                            num2 = (int) valDer.valor.charAt(0);
                        }
                        else
                        {
                            num1 = (int) valIzq.valor.charAt(0);
                            num2 = Integer.valueOf(valDer.valor);
                        }
                        res.valor = num1 + num2 + "";            
                        break;
                    case 10://numero + caracter
                        res.tipo = Const.tnumero;
                        double dec1 = 0;
                        double dec2 = 0;
                        if(valIzq.tipo == 1)
                        {
                            dec1 = Double.valueOf(valIzq.valor);
                            dec2 = (int) valDer.valor.charAt(0);
                        }
                        else
                        {
                            dec1 = (int) valIzq.valor.charAt(0);
                            dec2 = Double.valueOf(valDer.valor);
                        }
                        res.valor = dec1 + dec2 + "";            
                        break;
                    case 11://numero + cadena
                    case 13://decimal + cadena
                    case 14://caracter + caracter
                    case 17://caracter + cadena
                    case 20://cadena + cadena
                        res.tipo = Const.tcadena;
                        res.valor = quitarComillas(valIzq.valor) + quitarComillas(valDer.valor);
                        break;
                    case 16://numero + Bool
                        res.tipo = Const.tnumero;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        res.valor = Double.valueOf(valIzq.valor) + Double.valueOf(valDer.valor) + "";
                        break;
                    case 18://decimal + Bool
                        res.tipo = Const.tdecimal;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        res.valor = Double.valueOf(valIzq.valor) + Double.valueOf(valDer.valor) + "";
                        break;
                    case 22://caracter + Bool
                    case 25://cadena + Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] - [" + getTipo(valDer.tipo) + "]";
                        break;
                    case 30://Bool + Bool
                        res.tipo = Const.tbool;
                        res.valor = Const.falso;
                        if (valIzq.valor.equals(Const.verdadero) || valDer.valor.equals(Const.verdadero))
                            res.valor = Const.verdadero;
                        break;
                }
                break;
        }
        return res;
    }
    
    public static void asignacion(Nodo hijo) {
        Objeto valor = ejecutarValor(hijo.hijos.get(1));
        System.out.println("VALOR: " + valor.valor);
    }
    
    public static String quitarComillas(String s)
    {
        s = s.replace("\"", "");
        return s;
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
            case "Error":
                return Const.terror;
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
            case Const.terror:
                return "Error";
        }
        return Const.als;
    }
}
