/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial3;

/**
 *
 * @author luisc
 */
public class Correo {

    public void validarCorreo(String Correo) throws CorreoExcepcion {
        if (!Correo.contains("@") || !Correo.endsWith(".com")) {
            throw new CorreoExcepcion("el correo debe contener @ Y .COM");
        } else {
            System.out.println("EL CORREO ESTA BIEN");
        }
    }
}
