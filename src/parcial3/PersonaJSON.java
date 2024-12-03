/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial3;

/**
 *
 * @author luisc
 */
public class PersonaJSON {
    private String identificacion;
    private String nombre;
    private String correo;

    public PersonaJSON(String identificacion, String nombre, String correo) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.correo = correo;
    }
    
    public String getIdentificacion(){
        return identificacion;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public String getCorreo(){
        return correo;
    }
    
}
