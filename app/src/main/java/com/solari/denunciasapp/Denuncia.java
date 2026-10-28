package com.solari.denunciasapp;

import java.io.Serializable;

public class Denuncia implements Serializable {
    public int id;
    public String dni;
    public String fecha;
    public String tipo;
    public String direccion;
    public String descripcion;
    public int estado;
}