package solines.rosero.villamar.ucsg.control_de_inventario.SQLITE;

/**
 * Created by sukasa on 23/02/2017.
 */

public class Empleados {
    private static int cod_restaurantes;
    private static String nombre, email;

    public String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        Empleados.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    static void setEmail(String email) {
        Empleados.email = email;
    }

    public int getCod_restaurantes() {
        return cod_restaurantes;
    }

    static void setCod_restaurantes(int cod_restaurantes) {
        Empleados.cod_restaurantes = cod_restaurantes;
    }
}
