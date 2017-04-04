package semanticos;

import Reportes.ErroresGraphik;
import Reportes.ReporteProcesar;
import fabrica.Nodo;
import fabrica.NodoOperacion;
import ide.Const;
import ide.Fila;
import ide.Principal;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.LinkedList;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.w3c.dom.css.RGBColor;
import static semanticos.EjecutarArbol.raiz;
import static semanticos.Pila.obtenerLid;
import semanticos.haskell.Haskell;
import semanticos.terminal.EjecutarTerm;
import semanticos.terminal.PilaHaskell;

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
                if(ultimo.hijos.isEmpty() || ultimo.nombre.equals(Const.lcorchetes))
                {
                    Elemento ele = Pila.obtenerLid(valor);
                    if(ele != null)
                    {
                        if(ultimo.nombre.equals(Const.lcorchetes))
                        {
                            int map = Pila.mapeo(ultimo, ele);
                            res.tipo = ele.tipo;
                            res.valor = ele.lvalores.get(map).valor;
                        }
                        else
                        {
                            res.tipo = ele.tipo;
                            res.valor = ele.valor;
                            if(res.tipo == Const.tals)
                            {
                                res.objeto = (Ambito) ele.objeto;
                                res.tipoAls = ele.tipoAls;
                            }
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
            case Const.llamado:
                res = Haskell.llamado(valor);
                break;
            case Const.succ:
            case Const.decc:
            case Const.mod:
            case Const.sqrt:
            case Const.sum:
            case Const.product:
            case Const.max:
            case Const.length:
            case Const.poslista:
                res = EjecutarTerm.ejecutarInst(valor);
                break;
            case Const.porcentaje:
                res = EjecutarTerm.porcentaje;
                break;
            case Const.columna:
                Objeto pos = ejecutarValor(valor.hijos.get(0));
                Objeto col = new Objeto();
                if(pos.tipo == Const.tnumero)
                {
                    Integer x = Integer.valueOf(pos.valor);
                    Fila fila = Principal.datos.get(Principal.fila);
                    col = fila.datos.get(x - 1);
                    if(col.tipo != Const.tnumero && col.tipo != Const.tdecimal)
                    {
                        String error = "La columna [" + (x - 1) + "] no contiene valores numericos (entero o decimal)";
                        ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                        return new Objeto();
                    }
                }
                else
                {
                    String error = "El valor para columna debe de ser tipo entero [" + pos.valor + "]";
                    ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                }
                res = col;
                break;
            case Const.id:
                Nodo lid = new Nodo(Const.lid, Const.lid);
                lid.hijos.add(valor);
                res = ejecutarValor(lid);
                if(res.tipo == 0 || res.tipo == Const.terror)
                {
                    Elemento x = PilaHaskell.buscar(valor.valor);
                    if(x != null)
                    {
                        res = new Objeto(x.tipo, x.valor);
                        res.dim = x.dim;
                        res.lvalores = x.lvalores;
                        return res;
                    }
                    else
                    {
                        String error = "La variable [" + valor.valor + "] no ha sido declarada";
                        ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                        return new Objeto();
                    }
                }
                else
                    return res;
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
        if(hijo.hijos.get(1).nombre.equals(Const.nuevo))
        {
            Elemento elemento = obtenerLid(hijo.hijos.get(0));
            Pila.asignarNuevo(elemento, hijo.hijos.get(1).valor);
        }
        else
        {
            Objeto valor = ejecutarValor(hijo.hijos.get(1));
            Pila.asignarValor(hijo.hijos.get(0), valor);
            System.out.println("VALOR: " + valor.valor + " ---- TIPO: " + getTipo(valor.tipo));
        }
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
                case Const.asignacionArr:
                        Pila.asignarArreglo(elemento, asig);
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
    
    public static TipoRetorno si(Nodo hijo) {
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
    
    static TipoRetorno seleccion(Nodo hijo) {
        TipoRetorno retorno = new TipoRetorno();
        Objeto obj = new Objeto();
        Nodo valor = hijo.hijos.get(0),
                lcasos = hijo.hijos.get(1);
        Nodo defecto = null;
        if(hijo.hijos.size() == 3)
            defecto = hijo.hijos.get(2);
        int i = 0;
        int pos = -1;
        Nodo caso = null;
        Nodo cond = null;
        Objeto condicion = new Objeto();
        while(i < lcasos.hijos.size() && pos == -1)
        {
            caso = lcasos.hijos.get(i).hijos.get(0);
            cond = NodoOperacion.crearRelacional(Const.igualigual, valor, caso);
            condicion = ejecutarValor(cond);
            if(condicion.tipo == Const.tbool)
            {
                if(condicion.valor.equals(Const.verdadero))
                    pos = i;
            }
            else
                ErroresGraphik.agregarError("Error semantico", "El caso (" + (i + 1) + ") no coincide con el tipo de la variable", 0,0);
            i++;
        }
        Pila.crearAmbito();
        if (pos != -1)
        {
            while(!(retorno.terminar || retorno.retorno || retorno.continuar) && pos < lcasos.hijos.size())
            {
                Nodo cuerpo = lcasos.hijos.get(pos).hijos.get(1);
                retorno = EjecutarArbol.ejecutarCuerpo(cuerpo);
                pos++;
            }
            if(retorno.terminar || retorno.continuar)
            {
                retorno.terminar = false;
            }
            else
                if(defecto != null)
                    retorno = EjecutarArbol.ejecutarCuerpo(defecto.hijos.get(0));
        }
        else
            if(defecto != null)
                retorno = EjecutarArbol.ejecutarCuerpo(defecto.hijos.get(0));
        if (retorno.retorno)
        {
            obj = (Objeto) Pila.getRetorno();
            Pila.eliminarAmbito();
            Pila.setRetorno(obj);
        }
        else
            Pila.eliminarAmbito();
                

        return retorno;
    }
    static TipoRetorno para(Nodo hijo) {
        TipoRetorno retorno = new TipoRetorno();
        Objeto obj = new Objeto();
        //se obtienen los 4 hijos de la sentencia PARA
        Nodo hijoInicio = hijo.hijos.get(0),
                hijoCond = hijo.hijos.get(1),
                hijoAumento = hijo.hijos.get(2),
                cuerpo = hijo.hijos.get(3);
        boolean esDec = false;
        Pila.crearAmbito();
        if(hijoInicio.nombre.equals(Const.declaracion))
            Semantico.declaracion(hijoInicio);
        else
            Semantico.asignacion(hijoInicio);
        Objeto condicion = ejecutarValor(hijoCond);
        if (condicion.tipo == Const.tbool)
        {
            while (condicion.valor.equals(Const.verdadero))
            {
                retorno = EjecutarArbol.ejecutarCuerpo(cuerpo);
                if (retorno.continuar)
                    retorno.continuar = false;
                if (retorno.terminar)
                {
                    retorno.terminar = false;
                    break;
                }
                if (retorno.retorno)
                    break;
                Semantico.asignacion(hijoAumento);
                condicion = ejecutarValor(hijoCond);
                if(ErroresGraphik.contErrores > 0)
                    break;
            }
        }
        else
            ErroresGraphik.agregarError("Error semantico", "La condicion de MIENTRAS no es de tipo Bool", 0,0);
        if (retorno.retorno)
        {
            obj = (Objeto) Pila.getRetorno();
            Pila.eliminarAmbito();
            Pila.setRetorno(obj);
        }
        else
            Pila.eliminarAmbito();
        return retorno;
    }
    static TipoRetorno mientras(Nodo hijo) {
        TipoRetorno retorno = new TipoRetorno();
        Objeto obj = new Objeto();
        Nodo valor = hijo.hijos.get(0);
        Objeto condicion = ejecutarValor(valor);
        if (condicion.tipo == Const.tbool)
        {
            Pila.crearAmbito();
            while (condicion.valor.equals(Const.verdadero))
            {
                retorno = EjecutarArbol.ejecutarCuerpo(hijo.hijos.get(1));
                if (retorno.continuar)
                    retorno.continuar = false;
                if (retorno.terminar)
                {
                    retorno.terminar = false;
                    break;
                }
                if (retorno.retorno)
                    break;
                condicion = ejecutarValor(valor);
            }
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
            ErroresGraphik.agregarError("Error semantico", "La condicion de MIENTRAS no es de tipo Bool", 0,0);
        return retorno;
    }
    static TipoRetorno hacer(Nodo hijo) {
        TipoRetorno retorno = new TipoRetorno();
        Objeto obj = new Objeto();
        Nodo valor = hijo.hijos.get(0);
        Objeto condicion = ejecutarValor(valor);
        if (condicion.tipo == Const.tbool)
        {
            Pila.crearAmbito();
            do
            {
                retorno = EjecutarArbol.ejecutarCuerpo(hijo.hijos.get(1));
                if (retorno.continuar)
                    retorno.continuar = false;
                if (retorno.terminar)
                {
                    retorno.terminar = false;
                    break;
                }
                if (retorno.retorno)
                    break;
                condicion = ejecutarValor(valor);
            }while(condicion.valor.equals(Const.verdadero));
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
            ErroresGraphik.agregarError("Error semantico", "La condicion de MIENTRAS no es de tipo Bool", 0,0);
        return retorno;
    }
    
    public static void imprimir(Nodo hijo) {
        Objeto valor = ejecutarValor(hijo.hijos.get(0));
        Objeto casteo = Pila.implicito(Const.tcadena, "", valor);
        if(casteo.tipo == Const.terror)
        {
            ErroresGraphik.agregarError("Error semantico", casteo.valor, 0, 0);
            ErroresGraphik.agregarError("Error semantico", "imprimir([" + getTipo(valor.tipo) + "]) no es posible ejecutar", 0,0);
        }
        else
            Principal.consola += casteo.valor + "\n";
    }
    
    static void graficar(Nodo graphikar) {
        Elemento ele1 = Pila.obtenerLid(graphikar.hijos.get(0));
        Elemento ele2 = Pila.obtenerLid(graphikar.hijos.get(1));
        if(ele1.dim != null && ele1.dim.size() > 0 && ele2.dim != null && ele2.dim.size() > 0)
        {
            if(ele1.dim.size() == 1 && ele2.dim.size() == 1)
            {
                XYSeries g = new XYSeries("Graphikar Funcion");
                for(int i = 0; i < ele1.lvalores.size(); i++)
                {
                    Double x = Double.valueOf(ele1.lvalores.get(i).valor);
                    Double y = Double.valueOf(ele2.lvalores.get(i).valor);
                    g.add(x, y);
                }
                XYSeriesCollection dataset = new XYSeriesCollection();
                dataset.addSeries(g);
                JFreeChart xylineChart = ChartFactory.createXYLineChart( "Grafica", "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
                XYPlot plot = xylineChart.getXYPlot();
                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
               
                renderer.setSeriesPaint(0, Color.BLUE);
                renderer.setSeriesStroke(0, new BasicStroke(2.0f));
                plot.setRenderer(renderer);
               
                ChartPanel panel = new ChartPanel(xylineChart);
 
                // Creamos la ventana
                JFrame ventana = new JFrame("Salida");
                ventana.setVisible(true);
                ventana.setSize(800, 600);
                ventana.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
 
                ventana.add(panel);
            }
        }
        else
            ErroresGraphik.agregarError("Error semantico", "Alguno de los parametros de graphikar_funcion no es un arreglo", 0,0);
    }
    
    static void retornar(Nodo hijo) {
        Objeto valor = ejecutarValor(hijo.hijos.get(0));
        Pila.setRetorno(valor);
    }
    
    public static String quitarComillas(String s)
    {
        return s;
    }
    
    static void llamarDatos(Nodo hijo)
    {
        Principal.procesar.clear();
        Nodo arbolAls = EjecutarArbol.buscarClasePrincipal(EjecutarArbol.raiz.hijos.get(1)).get(0);
        Nodo funcion = null;
        if(arbolAls != null)
            for(Nodo h : arbolAls.hijos.get(1).hijos)//se busca en los hijos de LCUERPOALS
                if(h.nombre.equals(Const.datos))
                    funcion = h;
        if(funcion != null)
        {
            Nodo DONDE = funcion.hijos.get(1);
            Nodo PROCESAR = funcion.hijos.get(0);
            Fila fila = new Fila();
            for(Principal.fila = 0; Principal.fila < Principal.datos.size(); Principal.fila++ )
            {
                Objeto dondePos = ejecutarValor(DONDE.hijos.get(0));
                fila = new Fila();
                switch(DONDE.nombre)
                {
                    case Const.donde:
                        if(dondePos.tipo == Const.tnumero)
                        {
                            Principal.filtro = ejecutarValor(DONDE.hijos.get(1)).valor;
                            Principal.colFiltro = Integer.valueOf(dondePos.valor) - 1;
                            if(Principal.datos.get(Principal.fila).datos.get(Principal.colFiltro).valor.equals(Principal.filtro))
                            {
                                Principal.filtro = ejecutarValor(DONDE.hijos.get(1)).valor;
                                Objeto valor = ejecutarValor(PROCESAR.hijos.get(0));
                                fila.datos.add(new Objeto(0, Principal.filtro));
                                fila.datos.add(valor);
                                Principal.procesar.add(fila);
                            }
                        }
                        else
                            ErroresGraphik.agregarError("Error semantico", "La referencia para la columna de Donde no es de tipo entero [" + dondePos.valor + "]", 0,0);
                        break;
                    case Const.dondecada:
                        if(dondePos.tipo == Const.tnumero)
                        {
                            Principal.colFiltro = Integer.valueOf(dondePos.valor) - 1;
                            Fila f = Principal.datos.get(Principal.fila);
                            Principal.filtro = f.datos.get(Principal.colFiltro).valor;
                            Objeto valor = ejecutarValor(PROCESAR.hijos.get(0)); //ejecuta el procesar
                            Fila filaFiltro = buscarFiltro();
                            if(filaFiltro != null)
                            {
                                Objeto x = filaFiltro.datos.get(1);
                                x.valor = Double.valueOf(x.valor) + Double.valueOf(valor.valor) + "";
                            }
                            else
                            {
                                fila.datos.add(new Objeto(0, Principal.filtro));
                                fila.datos.add(valor);
                                Principal.procesar.add(fila);
                            }
                        }
                        break;
                }
            }
            if(DONDE.nombre.equals(Const.dondetodo))
            {
                Objeto dondePos = ejecutarValor(DONDE.hijos.get(0));
                Double x = 0.0;
                if(dondePos.tipo == Const.tnumero)
                {
                    for(Principal.fila = 0; Principal.fila < Principal.datos.size(); Principal.fila++ )
                    {
                        Objeto valor = ejecutarValor(PROCESAR.hijos.get(0)); //ejecuta el procesar
                        x += Double.valueOf(valor.valor);
                    }
                }
                fila.datos.add(new Objeto(0, "Todo"));
                fila.datos.add(new Objeto(Const.tdecimal, x + ""));
                Principal.procesar.add(fila);
            }
            ReporteProcesar.generarProcesar(DONDE.nombre);
        }
    }
    
    public static Fila buscarFiltro()
    {
        for(Fila f : Principal.procesar)
            if(f.datos.get(0).valor.equals(Principal.filtro))
                return f;
        return null;
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
        return "No tiene valor";
    }
}
