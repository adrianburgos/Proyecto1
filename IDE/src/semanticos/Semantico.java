package semanticos;

import Reportes.ErroresGraphik;
import fabrica.Nodo;
import ide.Const;
import ide.Principal;
import java.util.LinkedList;

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
                valIzq = ejecutarValor(valor.hijos.get(0));
                if(valor.hijos.size() > 1)
                {
                    valDer = ejecutarValor(valor.hijos.get(1));
                    res = ejecutarAritmetica(valIzq, valDer, valor.getNombre());
                }
                else
                { //operador unario (-)
                    switch (valIzq.tipo) {
                        case Const.tnumero:
                            res.tipo = Const.tnumero;
                            res.valor = (-1* Integer.valueOf(valIzq.valor)) + "";
                            break;
                        case Const.tdecimal:
                            res.tipo = Const.tdecimal;
                            res.valor = (-1* Double.valueOf(valIzq.valor)) + "";
                            break;
                        default:
                                res.tipo = Const.terror;
                                res.valor = "No se pudo operar - [" + getTipo(valIzq.tipo) + "]";
                            break;
                    }
                }
                break;
            case Const.menor:
            case Const.mayor:
            case Const.menorigual:
            case Const.mayorigual:
            case Const.igualigual:
            case Const.diferente:
                valIzq = ejecutarValor(valor.hijos.get(0));
                valDer = ejecutarValor(valor.hijos.get(1));
                res = ejecutarRelacional(valIzq, valDer, valor.getNombre());
                break;
            case Const.or:
            case Const.xor:
            case Const.and:
                valIzq = ejecutarValor(valor.hijos.get(0));
                valDer = ejecutarValor(valor.hijos.get(1));
                res = ejecutarLogica(valIzq, valDer, valor.getNombre());
                break;
            case Const.not:
                valIzq = ejecutarValor(valor.hijos.get(0));
                if(valIzq.tipo == Const.tbool)
                {
                    res.tipo = Const.tbool;
                    res.valor = Const.verdadero;
                    if(valIzq.valor.equals(Const.verdadero))
                        res.valor = Const.falso;
                }
                else
                {
                    res.tipo = Const.terror;
                    res.valor = "No se pudo operar - [" + getTipo(valIzq.tipo) + "]";
                }
                break;
            case Const.lid:
                //verificar que el ultimo sea un id
                Nodo ultimo = valor.hijos.get(valor.hijos.size() -1);
                if(ultimo.hijos.isEmpty())
                {
                    Elemento ele = Pila.obtenerLid(valor);
                    if(ele != null)
                    {
                        res.tipo = ele.tipo;
                        res.valor = ele.valor;
                        if(res.tipo == Const.tals)
                        {
                            res.objeto = (Ambito) ele.objeto;
                            res.tipoAls = ele.tipoAls;
                        }
                    }
                    else
                    {
                        String error = "La variable [";
                        for(Nodo nodo : valor.hijos)
                            error += nodo.valor + ".";
                        error = error.substring(0, error.length() - 1);
                        error += "] no ha sido declarada";
                        ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                    }
                }
                else
                {//debe de ser un llamado
                    String error = "La variable [";
                    for(Nodo nodo : valor.hijos)
                        error += nodo.valor + ".";
                    error = error.substring(0, error.length() - 1);
                    error += "] debe de llevar llamar";
                    ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                }
                break;
            case Const.llamar:
                res = llamar(valor);
                break;
            default:
                //retornar el valor
                return new Objeto(valor.tipo, valor.valor);
        }
        if (res.tipo == Const.terror)
        {
            ErroresGraphik.agregarError("Error semantico", res.valor, 0, 0);
            res.tipo = Const.terror;
            res.valor = "";
        }
        return res;
    }
    
    // <editor-fold desc="metodos ejecutar operaciones">
    
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
                        res.valor = Integer.valueOf(valIzq.valor) + Integer.valueOf(valDer.valor) + "";
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
                    case 10://decimal + caracter
                        res.tipo = Const.tdecimal;
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
                        res.valor = Integer.valueOf(valIzq.valor) + Integer.valueOf(valDer.valor) + "";
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
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] + [" + getTipo(valDer.tipo) + "]";
                        break;
                    case 30://Bool + Bool
                        res.tipo = Const.tbool;
                        res.valor = Const.falso;
                        if (valIzq.valor.equals(Const.verdadero) || valDer.valor.equals(Const.verdadero))
                            res.valor = Const.verdadero;
                        break;
                }
                break;//case suma
            case Const.menos:
                switch (valDer.tipo + valIzq.tipo)
                {
                    case 2://numero - numero
                        res.tipo = Const.tnumero;
                        res.valor = Integer.valueOf(valIzq.valor) - Integer.valueOf(valDer.valor) + "";
                        break;
                    case 4://numero + decimal
                    case 6://decimal + decimal
                        res.tipo = Const.tdecimal;
                        res.valor = Double.valueOf(valIzq.valor) - Double.valueOf(valDer.valor) + "";
                        break;
                    case 8://numero - caracter
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
                        res.valor = num1 - num2 + "";            
                        break;
                    case 10://decimal - caracter
                        res.tipo = Const.tdecimal;
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
                        res.valor = dec1 - dec2 + "";            
                        break;
                    case 16://numero - Bool
                        res.tipo = Const.tnumero;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        res.valor = Integer.valueOf(valIzq.valor) - Integer.valueOf(valDer.valor) + "";
                        break;
                    case 18://decimal - Bool
                        res.tipo = Const.tdecimal;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        res.valor = Double.valueOf(valIzq.valor) - Double.valueOf(valDer.valor) + "";
                        break;
                    case 11://numero - cadena
                    case 13://decimal - cadena
                    case 14://caracter - caracter
                    case 17://caracter - cadena
                    case 20://cadena - cadena
                    case 22://caracter - Bool
                    case 25://cadena - Bool
                    case 30://Bool - Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] - [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case menos
            case Const.por:
                switch (valDer.tipo + valIzq.tipo)
                {
                    case 2://numero * numero
                        res.tipo = Const.tnumero;
                        res.valor = Integer.valueOf(valIzq.valor) * Integer.valueOf(valDer.valor) + "";
                        break;
                    case 4://numero * decimal
                    case 6://decimal * decimal
                        res.tipo = Const.tdecimal;
                        res.valor = Double.valueOf(valIzq.valor) * Double.valueOf(valDer.valor) + "";
                        break;
                    case 8://numero * caracter
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
                        res.valor = num1 * num2 + "";            
                        break;
                    case 10://decimal * caracter
                        res.tipo = Const.tdecimal;
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
                        res.valor = dec1 * dec2 + "";
                        break;
                    case 16://numero * Bool
                        res.tipo = Const.tnumero;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        res.valor = Integer.valueOf(valIzq.valor) * Integer.valueOf(valDer.valor) + "";
                        break;
                    case 18://decimal * Bool
                        res.tipo = Const.tdecimal;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        res.valor = Double.valueOf(valIzq.valor) * Double.valueOf(valDer.valor) + "";
                        break;
                    case 11://numero * cadena
                    case 13://decimal * cadena
                    case 14://caracter * caracter
                    case 17://caracter * cadena
                    case 20://cadena * cadena
                    case 22://caracter * Bool
                    case 25://cadena * Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] * [" + getTipo(valDer.tipo) + "]";
                        break;
                    case 30://Bool * Bool
                        res.tipo = Const.tbool;
                        res.valor = Const.falso;
                        if (valIzq.valor.equals(Const.verdadero) && valDer.valor.equals(Const.verdadero))
                            res.valor = Const.verdadero;
                        break;
                }
                break;//case por
            case Const.dividido:
                switch (valDer.tipo + valIzq.tipo)
                {
                    case 2://numero / numero
                        res.tipo = Const.tdecimal;
                        if (Integer.valueOf(valDer.valor) != 0)
                            res.valor = Double.valueOf(valIzq.valor) / Double.valueOf(valDer.valor) + "";
                        else
                        {
                            res.tipo = Const.terror;
                            res.valor = "Division entre 0";
                        }
                        break;
                    case 4://numero / decimal
                    case 6://decimal / decimal
                        res.tipo = Const.tdecimal;
                        if (Double.valueOf(valDer.valor) != 0)
                            res.valor = Double.valueOf(valIzq.valor) / Double.valueOf(valDer.valor) + "";
                        else
                        {
                            res.tipo = Const.terror;
                            res.valor = "Division entre 0";
                        }
                        break;
                    case 8://numero / caracter
                    case 10://decimal / caracter
                        res.tipo = Const.tdecimal;
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
                        if (dec2 != 0)
                            res.valor = dec1 * dec2 + "";
                        else
                        {
                            res.tipo = Const.terror;
                            res.valor = "Division entre 0";
                        }
                        break;
                    case 16://numero / Bool
                        res.tipo = Const.tdecimal;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        if (Integer.valueOf(valDer.valor) != 0)
                                res.valor = Integer.valueOf(valIzq.valor) / Integer.valueOf(valDer.valor) + "";
                        else
                        {
                            res.tipo = Const.terror;
                            res.valor = "Division entre 0";
                        }
                        break;
                    case 18://decimal / Bool
                        res.tipo = Const.tdecimal;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        if (Double.valueOf(valDer.valor) != 0)
                            res.valor = Double.valueOf(valIzq.valor) / Double.valueOf(valDer.valor) + "";
                        else
                        {
                            res.tipo = Const.terror;
                            res.valor = "Division entre 0";
                        }
                        break;
                    case 11://numero / cadena
                    case 13://decimal / cadena
                    case 14://caracter / caracter
                    case 17://caracter / cadena
                    case 20://cadena / cadena
                    case 22://caracter / Bool
                    case 25://cadena / Bool
                    case 30://Bool / Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] / [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case dividido
                case Const.pot:
                switch (valDer.tipo + valIzq.tipo)
                {
                    case 2://numero ^ numero
                        res.tipo = Const.tnumero;
                        res.valor = Math.round(Math.pow(Integer.valueOf(valIzq.valor) , Integer.valueOf(valDer.valor))) + "";
                        break;
                    case 4://numero ^ decimal
                    case 6://decimal ^ decimal
                        res.tipo = Const.tdecimal;
                        res.valor = Math.pow(Double.valueOf(valIzq.valor) , Double.valueOf(valDer.valor)) + "";
                        break;
                    case 8://numero ^ caracter
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
                        res.valor = Math.round(Math.pow(num1 , num2)) + "";            
                        break;
                    case 10://decimal ^ caracter
                        res.tipo = Const.tdecimal;
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
                        res.valor = Math.pow(dec1 , dec2) + "";
                        break;
                    case 16://numero ^ Bool
                        res.tipo = Const.tnumero;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        res.valor = Math.round(Math.pow(Integer.valueOf(valIzq.valor) , Integer.valueOf(valDer.valor))) + "";
                        break;
                    case 18://decimal ^ Bool
                        res.tipo = Const.tdecimal;
                        if (valIzq.valor.equals(Const.verdadero))
                            valIzq.valor = "1";
                        if (valIzq.valor.equals(Const.falso))
                            valIzq.valor = "0";
                        if (valDer.valor.equals(Const.verdadero))
                            valDer.valor = "1";
                        if (valDer.valor.equals(Const.falso))
                            valDer.valor = "0";
                        res.valor = Math.pow(Double.valueOf(valIzq.valor) , Double.valueOf(valDer.valor)) + "";
                        break;
                    case 11://numero ^ cadena
                    case 13://decimal ^ cadena
                    case 14://caracter ^ caracter
                    case 17://caracter ^ cadena
                    case 20://cadena ^ cadena
                    case 22://caracter ^ Bool
                    case 25://cadena ^ Bool
                    case 30://Bool ^ Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] ^ [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case potencia
        }
        return res;
    }
    
    private static Objeto ejecutarRelacional(Objeto valIzq, Objeto valDer, String operador)
    {
        Objeto res = new Objeto();
        if(valIzq.tipo == Const.tbool)
            if (valIzq.valor.equals(Const.verdadero))
                valIzq.valor = "1";
            else
                valIzq.valor = "0";
        if(valDer.tipo == Const.tbool)
            if (valDer.valor.equals(Const.verdadero))
                valDer.valor = "1";
            else
                valDer.valor = "0";
        switch(operador)
        {
            case Const.menor:
                switch(valIzq.tipo + valDer.tipo)
                {
                    case 2://numero < numero
                    case 4://numero < decimal
                    case 6://decimal < decimal
                    case 16://numero < Bool
                    case 18://decimal < Bool
                    case 30://Bool < Bool
                        res.tipo = Const.tbool;
                        double val1 = Double.valueOf(valIzq.valor);
                        double val2 = Double.valueOf(valDer.valor);
                        res.valor = Const.falso;
                        if(val1 < val2)
                            res.valor = Const.verdadero;
                        break;
                    case 8://numero < caracter
                    case 10://decimal < caracter
                        res.tipo = Const.tbool;
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
                        res.valor = Const.falso;
                        if(dec1 < dec2)
                            res.valor = Const.verdadero;
                        break;
                    case 14://caracter < caracter
                    case 17://caracter < cadena
                    case 20://cadena < cadena
                        res.tipo = Const.tbool;
                        res.valor = Const.falso;
                        if(valIzq.valor.compareTo(valDer.valor) < 0 )
                            res.valor = Const.verdadero;
                        break;
                    case 11://numero < cadena
                    case 13://decimal < cadena
                    case 22://caracter < Bool
                    case 25://cadena < Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] < [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case menor
                case Const.mayor:
                switch(valIzq.tipo + valDer.tipo)
                {
                    case 2://numero > numero
                    case 4://numero > decimal
                    case 6://decimal > decimal
                    case 16://numero > Bool
                    case 18://decimal > Bool
                    case 30://Bool > Bool
                        res.tipo = Const.tbool;
                        double val1 = Double.valueOf(valIzq.valor);
                        double val2 = Double.valueOf(valDer.valor);
                        res.valor = Const.falso;
                        if(val1 > val2)
                            res.valor = Const.verdadero;
                        break;
                    case 8://numero > caracter
                    case 10://decimal > caracter
                        res.tipo = Const.tbool;
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
                        res.valor = Const.falso;
                        if(dec1 > dec2)
                            res.valor = Const.verdadero;
                        break;
                    case 14://caracter > caracter
                    case 17://caracter > cadena
                    case 20://cadena > cadena
                        res.tipo = Const.tbool;
                        res.valor = Const.falso;
                        if(valIzq.valor.compareTo(valDer.valor) > 0 )
                            res.valor = Const.verdadero;
                        break;
                    case 11://numero > cadena
                    case 13://decimal > cadena
                    case 22://caracter > Bool
                    case 25://cadena > Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] > [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case mayor
                case Const.menorigual:
                switch(valIzq.tipo + valDer.tipo)
                {
                    case 2://numero <= numero
                    case 4://numero <= decimal
                    case 6://decimal <= decimal
                    case 16://numero <= Bool
                    case 18://decimal <= Bool
                    case 30://Bool <= Bool
                        res.tipo = Const.tbool;
                        double val1 = Double.valueOf(valIzq.valor);
                        double val2 = Double.valueOf(valDer.valor);
                        res.valor = Const.falso;
                        if(val1 <= val2)
                            res.valor = Const.verdadero;
                        break;
                    case 8://numero <= caracter
                    case 10://decimal <= caracter
                        res.tipo = Const.tbool;
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
                        res.valor = Const.falso;
                        if(dec1 <= dec2)
                            res.valor = Const.verdadero;
                        break;
                    case 14://caracter <= caracter
                    case 17://caracter <= cadena
                    case 20://cadena <= cadena
                        res.tipo = Const.tbool;
                        res.valor = Const.falso;
                        if(valIzq.valor.compareTo(valDer.valor) <= 0 )
                            res.valor = Const.verdadero;
                        break;
                    case 11://numero <= cadena
                    case 13://decimal <= cadena
                    case 22://caracter <= Bool
                    case 25://cadena <= Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] <= [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case menorigual
                case Const.mayorigual:
                switch(valIzq.tipo + valDer.tipo)
                {
                    case 2://numero >= numero
                    case 4://numero >= decimal
                    case 6://decimal >= decimal
                    case 16://numero >= Bool
                    case 18://decimal >= Bool
                    case 30://Bool >= Bool
                        res.tipo = Const.tbool;
                        double val1 = Double.valueOf(valIzq.valor);
                        double val2 = Double.valueOf(valDer.valor);
                        res.valor = Const.falso;
                        if(val1 >= val2)
                            res.valor = Const.verdadero;
                        break;
                    case 8://numero >= caracter
                    case 10://decimal >= caracter
                        res.tipo = Const.tbool;
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
                        res.valor = Const.falso;
                        if(dec1 >= dec2)
                            res.valor = Const.verdadero;
                        break;
                    case 14://caracter >= caracter
                    case 17://caracter >= cadena
                    case 20://cadena >= cadena
                        res.tipo = Const.tbool;
                        res.valor = Const.falso;
                        if(valIzq.valor.compareTo(valDer.valor) >= 0 )
                            res.valor = Const.verdadero;
                        break;
                    case 11://numero >= cadena
                    case 13://decimal >= cadena
                    case 22://caracter >= Bool
                    case 25://cadena >= Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] >= [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case mayorigual
                case Const.igualigual:
                switch(valIzq.tipo + valDer.tipo)
                {
                    case 2://numero == numero
                    case 4://numero == decimal
                    case 6://decimal == decimal
                    case 16://numero == Bool
                    case 18://decimal == Bool
                    case 30://Bool == Bool
                        res.tipo = Const.tbool;
                        double val1 = Double.valueOf(valIzq.valor);
                        double val2 = Double.valueOf(valDer.valor);
                        res.valor = Const.falso;
                        if(val1 == val2)
                            res.valor = Const.verdadero;
                        break;
                    case 8://numero == caracter
                    case 10://decimal == caracter
                        res.tipo = Const.tbool;
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
                        res.valor = Const.falso;
                        if(dec1 == dec2)
                            res.valor = Const.verdadero;
                        break;
                    case 14://caracter == caracter
                    case 17://caracter == cadena
                    case 20://cadena == cadena
                        res.tipo = Const.tbool;
                        res.valor = Const.falso;
                        if(valIzq.valor.compareTo(valDer.valor) == 0 )
                            res.valor = Const.verdadero;
                        break;
                    case 11://numero == cadena
                    case 13://decimal == cadena
                    case 22://caracter == Bool
                    case 25://cadena == Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] == [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case igualigual
                case Const.diferente:
                switch(valIzq.tipo + valDer.tipo)
                {
                    case 2://numero != numero
                    case 4://numero != decimal
                    case 6://decimal != decimal
                    case 16://numero != Bool
                    case 18://decimal != Bool
                    case 30://Bool != Bool
                        res.tipo = Const.tbool;
                        double val1 = Double.valueOf(valIzq.valor);
                        double val2 = Double.valueOf(valDer.valor);
                        res.valor = Const.falso;
                        if(val1 != val2)
                            res.valor = Const.verdadero;
                        break;
                    case 8://numero != caracter
                    case 10://decimal != caracter
                        res.tipo = Const.tbool;
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
                        res.valor = Const.falso;
                        if(dec1 != dec2)
                            res.valor = Const.verdadero;
                        break;
                    case 14://caracter != caracter
                    case 17://caracter != cadena
                    case 20://cadena != cadena
                        res.tipo = Const.tbool;
                        res.valor = Const.falso;
                        if(valIzq.valor.equals(valDer.valor))
                            res.valor = Const.verdadero;
                        break;
                    case 11://numero != cadena
                    case 13://decimal != cadena
                    case 22://caracter != Bool
                    case 25://cadena != Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] != [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case diferente
        }
        return res;
    }
    
    private static Objeto ejecutarLogica(Objeto valIzq, Objeto valDer, String operador) {
        Objeto res = new Objeto();
        if((valIzq.tipo + valDer.tipo) != 30)
        {
            res.tipo = Const.terror;
            res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] && [" + getTipo(valDer.tipo) + "]";
        }
        else
            switch(operador)
            {
                case Const.or:
                    res.tipo = Const.tbool;
                    res.valor = Const.falso;
                    if(valIzq.valor.equals(Const.verdadero) || valDer.valor.equals(Const.verdadero))
                        res.valor = Const.verdadero;
                    break;//case or
                case Const.xor:
                    res.tipo = Const.tbool;
                    res.valor = Const.falso;
                    if((valIzq.valor.equals(Const.verdadero) && valDer.valor.equals(Const.falso)) || (valIzq.valor.equals(Const.falso) && valDer.valor.equals(Const.verdadero)))
                        res.valor = Const.verdadero;
                    break;//case xor
                case Const.and:
                    res.tipo = Const.tbool;
                    res.valor = Const.falso;
                    if(valIzq.valor.equals(Const.verdadero) && valDer.valor.equals(Const.verdadero))
                        res.valor = Const.verdadero;
                    break;//case and
            }
        return res;
    }
    // </editor-fold>
    
    public static void asignacion(Nodo hijo) {
        Objeto valor = ejecutarValor(hijo.hijos.get(1));
        Pila.asignarValor(hijo.hijos.get(0), valor);
        System.out.println("VALOR: " + valor.valor + " ---- TIPO: " + getTipo(valor.tipo));
    }
    
    public static void declaracion(Nodo dec) {
        Objeto valor = new Objeto();
        int tipo = dec.tipo;
        String nombre = dec.valor;
        String tipoAls = dec.tipoAls;
        Elemento elemento = new Elemento(nombre, tipo, null);
        elemento.tipoAls = tipoAls;
        elemento.visibilidad = dec.visibilidad;
        Pila.agregarElemeto(elemento);
        if(dec.hijos.size() > 0)
        {//la declaracion trae una asignacion o una lista de ids
            Nodo asig = dec.hijos.get(0);
            switch(asig.nombre)
            {
                case Const.lvariables:
                    //se deben de declarar varias variables con el mismo tipo;
                    for (Nodo var : asig.hijos)
                        Pila.agregarElemeto(tipo, var.valor, tipoAls, var.visibilidad);
                    break;
                case Const.nuevo:
                        Pila.asignarNuevo(elemento, tipoAls);
                    break;
                default:
                    valor = ejecutarValor(dec.hijos.get(0));
                    Pila.asignarValor(nombre, valor);                    
            }
        }
    }
    
    public static void declaracion(Nodo dec, Ambito ambito) {
        Objeto valor = new Objeto();
        int tipo = dec.tipo;
        String nombre = dec.valor;
        String tipoAls = dec.tipoAls;
        Elemento ele = new Elemento(nombre, tipo, null);
        ele.tipoAls = tipoAls;
        Pila.agregarElemeto(ele, ambito);
        if(dec.hijos.size() > 0)
        {//la declaracion trae una asignacion o una lista de ids
            Nodo asig = dec.hijos.get(0);
            switch(asig.nombre)
            {
                case Const.lvariables:
                    //se deben de declarar varias variables con el mismo tipo;
                    for (Nodo var : asig.hijos)
                        Pila.agregarElemeto(tipo, var.valor, ambito, tipoAls);
                    break;
                case Const.nuevo:
                        Pila.asignarNuevo(ele, tipoAls);
                    break;
                default:
                    valor = ejecutarValor(dec.hijos.get(0));
                    Pila.asignarValor(nombre, valor, ambito);
            }
        }
    }
    
    public static Objeto llamar(Nodo llamado)
    {
        Objeto res = null;
        //verifica si la lista de ids coincide con la de un recorrerFuncion
        Nodo lid = llamado.hijos.get(0);
        Nodo ultimo = lid.hijos.get(lid.hijos.size() - 1);
        if(ultimo.hijos.size() == 1)
        {//si es un recorrerFuncion
            Elemento ele = Pila.obtenerLid(lid);
            res = new Objeto(ele.tipo, ele.valor);
            if(res.tipo == Const.tals)
            {
                res.tipoAls = ele.tipoAls;
                res.objeto = (Ambito) ele.objeto;
            }
        }
        else
        {
            String error = "La funcion [";
            for(Nodo nodo : lid.hijos)
                error += nodo.valor + ".";
            error = error.substring(0, error.length() - 2);
            error += "] no ha sido declarada";
            ErroresGraphik.agregarError("Error semantico", error, 0, 0);
        }
        return res;
    }
    
    static TipoRetorno si(Nodo hijo) {
        TipoRetorno retorno = new TipoRetorno();
        Objeto obj = new Objeto();
        Nodo valor = hijo.hijos.get(0);
        Objeto condicion = ejecutarValor(valor);
        if (condicion.tipo == Const.tbool)
        {
            Pila.crearAmbito();
            if (condicion.valor.equals(Const.verdadero))
                retorno = EjecutarArbol.ejecutarCuerpo(hijo.hijos.get(1));
            else
                if (hijo.hijos.size() == 3)
                    retorno = EjecutarArbol.ejecutarCuerpo(hijo.hijos.get(2));
            if (retorno.retorno)
            {
                obj = (Objeto) Pila.getRetorno();
                Pila.eliminarAmbito();
                Pila.setRetorno(obj);
            }
            else
                Pila.eliminarAmbito();
        }
        else
            ErroresGraphik.agregarError("Error semantico", "La condicion de SI no es de tipo Bool", 0,0);

        return retorno;
    }
    
    public static void imprimir(Nodo hijo) {
        Objeto valor = ejecutarValor(hijo.hijos.get(0));
        Principal.consola += valor.valor;
    }
    
    static void retornar(Nodo hijo) {
        Objeto valor = ejecutarValor(hijo.hijos.get(0));
        Pila.setRetorno(valor);
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
            case Const.als:
                return Const.tals;
            case Const.vacio:
                return Const.tvacio;
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
            case Const.tals:
                return Const.als;
            case Const.tvacio:
                return Const.vacio;
            case Const.terror:
                return "Error";
        }
        return Const.als;
    }
}
