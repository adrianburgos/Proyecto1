/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticos;

/**
 *
 * @author Adrian
 */
public class TipoRetorno {
    public Boolean retorno;
    public Boolean continuar;
    public Boolean terminar;

    public TipoRetorno()
    {
        retorno = continuar = terminar = false;
    }

    public TipoRetorno(Boolean retorno, Boolean continuar, Boolean terminar)
    {
        this.retorno = retorno;
        this.continuar = continuar;
        this.terminar = terminar;
    }
}
