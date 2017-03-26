package solines.rosero.villamar.ucsg.control_de_inventario.SQLITE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import solines.rosero.villamar.ucsg.control_de_inventario.Bodega;
import solines.rosero.villamar.ucsg.control_de_inventario.Ingreso;
import solines.rosero.villamar.ucsg.control_de_inventario.Mod_Productos;
import solines.rosero.villamar.ucsg.control_de_inventario.Registro;


/**
 * Created by sukasa on 12/02/2017.
 */

public class DataSource {
    private SQLiteDatabase database; //Objeto para ejecutar sentencias DML
    private MySQLiteHelper dbHelper; //Objeto de la clase que trabaja con sentencias DDL
    private String[] allColumnsRegistro = {"codigo","usuario","contraseña","genero","email","estado","cod_restaurantes"},
    allcolumnsRestaurantes = {"codigo","nombre","estado"}, allColumnsBodega = {"codigo","nombre","estado","cod_restaurantes"},
            allColumnsItems = {"codigo","descripcion","precio","estado"};
    private int codigo=0;
    private String nombre="", emails="";

    public DataSource(Context context)
    {
        //Se instancia el objeto de la clase que ejecuta sentencias DDL MYSQLiteHelper
        dbHelper = new MySQLiteHelper(context);
    }
    public void open() throws SQLException {
        //Se realiza la apertura de la base de datos si existe la abre, si no existe la crea
        database = dbHelper.getReadableDatabase();
    }
    public void close(){
        //Se cierra la instancia de la Base de datos
        dbHelper.close();
    }
    //SENTENCIAS REGISTRO INICIO
    private int verificar_restaurante(String nom_restaurante)
    {
        Cursor cursor = database.query("RESTAURANTES",allcolumnsRestaurantes,"nombre='"+nom_restaurante+"'",null,null,null,null);
        cursor.moveToFirst();
        //Manera de verificar opcion 1 (codigo detallado)
        while (!cursor.isAfterLast())
        {
            nombre = cursor.getString(1);
            cursor.moveToNext();
        }
        cursor.close();
        if (nom_restaurante.equals(nombre))
            return 1;
        else
            return 0;
    }
    private int verificar_email(String email)
    {
        Cursor cursor = database.query("EMPLEADOS",allColumnsRegistro,"email='"+email+"'",null,null,null,null);
        cursor.moveToFirst();
        //Manera de verificar opcion 2 (codigo minificado)
        if (cursor.getCount()>0)
        {
            cursor.close();
            return 2;
        }
        else {
            cursor.close();
            return 0;
        }
    }
    public void insertarregistro(String usuario, String contraseña, String genero, String email, String nom_restaurante){
        ContentValues Vempleados = new ContentValues();
        ContentValues Vrestaurantes = new ContentValues();

        if(verificar_restaurante(nom_restaurante)==1)
        {
            Registro.validar = 1;
        }
        else if (verificar_email(email)==2)
        {
            Registro.validar = 2;
        }
        else
        {
            Vrestaurantes.put("nombre", nom_restaurante);
            Vrestaurantes.put("estado","A");
            database.insert("RESTAURANTES", null, Vrestaurantes);

            Cursor cursor = database.query("RESTAURANTES",allcolumnsRestaurantes,"nombre='"+nom_restaurante+"'",null,null,null,null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                codigo = cursor.getInt(0);
                nombre = cursor.getString(1);
                cursor.moveToNext();
            }
            //Hace cerrar el cursor
            cursor.close();

            Vempleados.put("usuario", usuario);
            Vempleados.put("contraseña", contraseña);
            Vempleados.put("genero", genero);
            Vempleados.put("email", email);
            Vempleados.put("estado", "A");

            Vempleados.put("cod_restaurantes", codigo);

            //values.put("estado", "A"); // Por cada columna de la tavla se requiere invocar al metodo put
            //Si el registro no se inserta el metodo insert retorna -1
            database.insert("EMPLEADOS", null, Vempleados);
            Registro.validar = 0;
        }
    }
    public void insertarlista(String usuario, String contraseña, String genero, String email, String list_restaurante)
    {
        ContentValues Vempleados = new ContentValues();


        if (verificar_email(email)==2)
        {
            Registro.validar = 2;
        }
        else
        {
            Cursor cursor = database.query("RESTAURANTES",allcolumnsRestaurantes,"nombre='"+list_restaurante+"'",null,null,null,null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                codigo = cursor.getInt(0);
                nombre = cursor.getString(1);
                cursor.moveToNext();
            }
            //Hace cerrar el cursor
            cursor.close();

            Vempleados.put("usuario", usuario);
            Vempleados.put("contraseña", contraseña);
            Vempleados.put("genero", genero);
            Vempleados.put("email", email);
            Vempleados.put("estado", "A");

            Vempleados.put("cod_restaurantes", codigo);

            //values.put("estado", "A"); // Por cada columna de la tavla se requiere invocar al metodo put
            //Si el registro no se inserta el metodo insert retorna -1
            database.insert("EMPLEADOS", null, Vempleados);
            Registro.validar = 0;
        }
    }
    public List<Restaurantes> consultar()
    {
        //Se crea el arreglo para almacenar los registos de la tabla
        List<Restaurantes> restaurantes = new ArrayList<Restaurantes>();
        //Se ejecuta el query de consulta y los registros son retornados como objeto tipo Cursor
        Cursor cursor = database.query("RESTAURANTES", allcolumnsRestaurantes, null, null, null, null, null);
        //EL cursor se ubica al inicio del primer registro
        cursor.moveToFirst();
        //Mientras el cursor no se encuentre en el ultimo registro
        while (!cursor.isAfterLast())
        {
            //Se agrega al ArrayList el objeto de tipo Proveedor
            restaurantes.add(cursorToRestaurantes(cursor));
            //El cursor se mueve al siguiente registro
            cursor.moveToNext();
        }
        //Hace cerrar el cursor
        cursor.close();

        return restaurantes;
    }
    //El registro actual del cursor lo convierte a un objeto de tipo Coment
    private Restaurantes cursorToRestaurantes(Cursor cursor)
    {
        Restaurantes restaurantes = new Restaurantes();
        restaurantes.setCodigo(cursor.getInt(0));
        restaurantes.setNombre(cursor.getString(1));
        restaurantes.setEstado(cursor.getString(2));
        return restaurantes;
    }
    //SENTENCIAS REGISTRO FIN
    //SENTENCIA LOGIN INICIO
    public boolean getUser(String email, String pass){
        Cursor cursor = database.query("EMPLEADOS",allColumnsRegistro,"email='"+email+"' AND contraseña ='" +pass+"'",null,null,null,null);
        cursor.moveToFirst();
        //Manera de verificar hay registro opcion 2 (codigo minificado)
        if (cursor.getCount()>0)
        {
            Empleados.setNombre(cursor.getString(1));
            Empleados.setEmail(cursor.getString(4));
            Empleados.setCod_restaurantes(cursor.getInt(6));
            cursor.close();
            database.close();
            return true;
        }
        else
            return false;
    }
    //SETENCIA LOGIN FIN
    //SETENCIA MENU INICIO
    public boolean verficar_bodega(int cod_restaurantes)
    {
        Cursor cursor = database.query("BODEGA",allColumnsBodega,"cod_restaurantes="+cod_restaurantes,null,null,null,null);
        cursor.moveToFirst();
        //Manera de verificar hay registro opcion 2 (codigo minificado)
        if (cursor.getCount()>0)
        {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }
    public boolean verficar_productos(int cod_restaurantes)
    {
        Cursor cursor = database.rawQuery("SELECT ITEMS.*\n" +
                "FROM RESTAURANTES JOIN BODEGA ON RESTAURANTES.codigo = BODEGA.cod_restaurantes\n" +
                "JOIN STOCK ON BODEGA.codigo = STOCK.cod_bodega\n" +
                "JOIN ITEMS ON ITEMS.codigo = STOCK.cod_items\n" +
                "WHERE RESTAURANTES.codigo="+cod_restaurantes,null);
        cursor.moveToFirst();
        //Manera de verificar hay registro opcion 2 (codigo minificado)
        if (cursor.getCount()>0)
        {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }
    //SETENCIA MENU FIN
    //SETENCIA BODEGA INICIO
    private boolean verficar_si_existe_bodega(String txtbodega, int cod_restaurantes)
    {
        //int codigo_bodega = Obtener_codigobodega(txtbodega);
        Cursor cursor = database.query("BODEGA",allColumnsBodega, "nombre='"+txtbodega+"' AND cod_restaurantes="+cod_restaurantes,null,null,null,null);
        cursor.moveToFirst();
        //Manera de verificar hay registro opcion 2 (codigo minificado)
        if (cursor.getCount()>0)
        {
            cursor.close();
            return true;
        }
        else
            return false;
    }
    public void insertarBodega(String txtbodega, int cod_restaurantes)
    {
        ContentValues Vbodega = new ContentValues();
        //Empleados e = new Empleados();
        if (verficar_si_existe_bodega(txtbodega,cod_restaurantes))
        {
            Bodega.validar = true;
        }
        else {
            Vbodega.put("nombre", txtbodega);
            Vbodega.put("estado", "A");
            Vbodega.put("cod_restaurantes", cod_restaurantes);
            database.insert("BODEGA", null, Vbodega);
            Bodega.validar = false;
        }
    }
    //SETENCIA BODEGA FIN
    //SETENCIA INGRESO INICIO
    public List<String> listaBodega(int cod_restaurantes){
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT * FROM BODEGA WHERE cod_restaurantes="+cod_restaurantes;


        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return labels;
    }
    private int verificar_cod_items(String cod_qr, int codigo)
    {
        Cursor cursor = database.rawQuery("SELECT ITEMS.codigo\n" +
                "FROM BODEGA JOIN STOCK ON BODEGA.codigo = STOCK.cod_bodega\n" +
                "JOIN ITEMS ON STOCK.cod_items = ITEMS.codigo\n" +
                "WHERE BODEGA.codigo="+codigo+" AND ITEMS.cod_qr="+cod_qr,null);
        cursor.moveToFirst();
        //Manera de verificar opcion 2 (codigo minificado)
        if (cursor.getCount()>0)
        {
            cursor.close();
            return 1;
        }
        else {
            cursor.close();
            return 0;
        }
    }
    private int Obtener_codigobodega(String bodega)
    {
        int codigo=0;
        Empleados e = new Empleados();
        Cursor cursor = database.rawQuery("SELECT BODEGA.codigo\n" +
                "FROM BODEGA\n" +
                "WHERE BODEGA.nombre='"+bodega+"' AND BODEGA.cod_restaurantes="+e.getCod_restaurantes(),null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            codigo = cursor.getInt(0);
        }
        cursor.close();
        return codigo;
    }
    private int verificar_descripcion(int codigo_bodega, String descripcion)
    {
        Empleados e = new Empleados();
        Cursor cursor = database.rawQuery("SELECT ITEMS.descripcion\n" +
                "FROM BODEGA JOIN STOCK ON BODEGA.codigo = STOCK.cod_bodega JOIN ITEMS ON ITEMS.codigo = STOCK.cod_items\n" +
                "WHERE BODEGA.codigo="+codigo_bodega+" AND BODEGA.cod_restaurantes="+e.getCod_restaurantes()+" AND ITEMS.descripcion='"+descripcion+"'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.close();
            return 2;
        }
        else {
            cursor.close();
            return 0;
        }

    }
    public void insertarIngreso(String cod_qr, String descripcion, float precio, int cantidad, String bodega)
    {
        int codigo_bodega = Obtener_codigobodega(bodega);
        ContentValues Vitems = new ContentValues();
        ContentValues Vstock = new ContentValues();
        if (verificar_cod_items(cod_qr,codigo_bodega)==1)
        {
            Ingreso.validar = 1;
        }
        else if (verificar_descripcion(codigo_bodega,descripcion)==2)
        {
            Ingreso.validar = 2;
        }
        else
        {
            Vitems.put("cod_qr", cod_qr);
            Vitems.put("descripcion", descripcion);
            Vitems.put("precio", precio);
            Vitems.put("estado", "A");
            database.insert("ITEMS", null, Vitems);


            Cursor cursor = database.rawQuery("SELECT LAST_INSERT_ROWID()",null);
            //Stock s = new Stock();
            cursor.moveToFirst();
            if (cursor.getCount()>0)
            {
                int cod_items = cursor.getInt(0);
                Vstock.put("cantidad", cantidad);
                Vstock.put("estado", "A");
                Vstock.put("cod_bodega", codigo_bodega);
                Vstock.put("cod_items", cod_items);
                database.insert("STOCK", null, Vstock);
                Ingreso.validar = 0;
            }
            cursor.close();
        }
    }
    //SENTENCIA INGRESO FIN
    //SENTENCIA CONSULTA INICIO
    public List<Items> consultarbusqueda(String codigo, String bodega, int cod_restaurante)
    {
        List<Items> lista = new ArrayList<Items>();
        Cursor cursor;
            cursor = database.rawQuery("SELECT Items.descripcion "+
                    "FROM ITEMS INNER JOIN STOCK ON ITEMS.codigo = STOCK.cod_items INNER JOIN BODEGA ON BODEGA.codigo = STOCK.cod_bodega WHERE (ITEMS.cod_qr ='"+codigo+"' OR ITEMS.descripcion LIKE '%"+codigo+"%') AND (BODEGA.nombre = '"+bodega+"' AND cod_restaurantes="+cod_restaurante+")",null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            lista.add(cursorToItems(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return lista;
    }
    //El registro actual del cursor lo convierte a un objeto de tipo Coment
    private Items cursorToItems(Cursor cursor)
    {
        Items items = new Items();
        items.setDescripcion(cursor.getString(0));
        return items;
    }
    //private int Obtener_codigo_item(String )
    public void ObtenerDatosDescripcion(String descripcion, String bodega)
    {
        int codigo_bodega = Obtener_codigobodega(bodega);
        Empleados e = new Empleados();
        Cursor cursor = database.rawQuery("SELECT ITEMS.*, STOCK.cantidad\n" +
                "FROM BODEGA JOIN STOCK ON BODEGA.codigo = STOCK.cod_bodega JOIN ITEMS ON ITEMS.codigo = STOCK.cod_items\n" +
                "WHERE ITEMS.descripcion='"+descripcion+"' AND BODEGA.cod_restaurantes="+e.getCod_restaurantes()+" AND BODEGA.codigo="+codigo_bodega,null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            Mod_Productos.setCodigo(cursor.getInt(0));
            Mod_Productos.setCod_qr(cursor.getString(1));
            Mod_Productos.setDescripcion(cursor.getString(2));
            Mod_Productos.setPrecio(cursor.getFloat(3));
            Mod_Productos.setEstado(cursor.getString(4));
            Mod_Productos.setCantidad(cursor.getInt(5));
            cursor.close();
        }
    }
    //SENTENCIA CONSULTA FIN
    //SENTENCIA MODIFICAR PRODUCTOS INICIO
    public void modificarProductos(int codigo, String cod_qr, String descripcion, String cantidad, String precio)
    {
        ContentValues Vitems = new ContentValues();
        ContentValues Vstock = new ContentValues();
        Vitems.put("cod_qr", cod_qr);
        Vitems.put("descripcion", descripcion);
        Vitems.put("precio", precio);

        Vstock.put("cantidad", cantidad);

        database.update("ITEMS", Vitems, "codigo=" +codigo, null);
        database.update("STOCK", Vstock, "cod_items=" +codigo, null);
    }
    public void eliminarProductos(int codigo)
    {
        database.delete("STOCK", "cod_items=" +codigo, null);
        database.delete("ITEMS", "codigo=" +codigo, null);
    }
    //SENTENCIA MODIFICAR PRODUCTOS FIN
    //SENTENCIA EGRESO INICIO
    public List<String> listaProductos(String bodega, int cod_restaurantes)
    {
        List<String> labels = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT ITEMS.descripcion "+
                "FROM BODEGA JOIN STOCK ON BODEGA.codigo = STOCK.cod_bodega "+
                "JOIN ITEMS ON STOCK.cod_items = ITEMS.codigo "+
                "WHERE BODEGA.nombre='"+bodega+"' AND BODEGA.cod_restaurantes="+cod_restaurantes,null);
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return labels;
    }
    public String Obtener_cantidad(String bodega, String descripcion, int cod_restaurantes)
    {
        String cantidad="";
        Cursor cursor = database.rawQuery("SELECT STOCK.cantidad "+
                "FROM BODEGA JOIN STOCK ON BODEGA.codigo = STOCK.cod_bodega "+
                "JOIN ITEMS ON STOCK.cod_items = ITEMS.codigo "+
                "WHERE BODEGA.nombre='"+bodega+"' AND ITEMS.descripcion='"+descripcion+"' AND BODEGA.cod_restaurantes="+cod_restaurantes,null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cantidad = cursor.getString(0);
        }
        cursor.close();
        return cantidad;
    }
    public List<Integer> Obtener_cod_BI(String bodega, String descripcion, int cod_restaurantes)
    {
        ArrayList<Integer> labels = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT STOCK.cod_bodega, STOCK.cod_items\n" +
                "FROM BODEGA JOIN STOCK ON BODEGA.codigo = STOCK.cod_bodega\n" +
                "JOIN ITEMS ON STOCK.cod_items = ITEMS.codigo\n" +
                "WHERE BODEGA.nombre='"+bodega+"' AND ITEMS.descripcion='"+descripcion+"' AND BODEGA.cod_restaurantes="+cod_restaurantes,null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            labels.add(cursor.getInt(0));
            labels.add(cursor.getInt(1));
        }
        cursor.close();
        return labels;
    }
    public void actualizar_cantidad(int cantidad, String bodega, String descripcion, int cod_restaurantes)
    {
        List<Integer> cod_BI = Obtener_cod_BI(bodega,descripcion,cod_restaurantes);
        int cod_bodega=0,cod_items=0;
        cod_bodega = cod_BI.get(0);
        cod_items = cod_BI.get(1);
        ContentValues Vstock = new ContentValues();

        Vstock.put("cantidad", cantidad);

        database.update("STOCK", Vstock, "cod_bodega="+cod_bodega+" AND cod_items="+cod_items, null);

    }
    //SENTENCIA EGRESO FIN
    //SENTENCIA TRANSFERENCIA INICIO
    public List<String> Obtener_datos_items(int cod_restaurantes, String descripcion, int codigo_bodega)
    {
        ArrayList<String> labels = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT ITEMS.*\n" +
                "FROM RESTAURANTES JOIN BODEGA ON RESTAURANTES.codigo = BODEGA.cod_restaurantes\n" +
                "JOIN STOCK ON BODEGA.codigo = STOCK.cod_bodega\n" +
                "JOIN ITEMS ON ITEMS.codigo = STOCK.cod_items\n" +
                "WHERE RESTAURANTES.codigo="+cod_restaurantes+" AND ITEMS.descripcion='"+descripcion+"' AND BODEGA.codigo="+codigo_bodega,null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            labels.add(cursor.getString(0));
            labels.add(cursor.getString(1));
            labels.add(cursor.getString(3));
            labels.add(cursor.getString(4));
        }
        cursor.close();
        return labels;
    }
    public void insertarTransferencia(String bodegaorigen, String descripcion, String cantidad, String bodegadestino)
    {
        Empleados e = new Empleados();
        int codigo_bodegaorigen = Obtener_codigobodega(bodegaorigen);
        int codigo_bodegadestino = Obtener_codigobodega(bodegadestino);
        List<String> lista_items = Obtener_datos_items(e.getCod_restaurantes(),descripcion,codigo_bodegaorigen);
        int codigo = Integer.valueOf(lista_items.get(0));
        String cod_qr = lista_items.get(1);
        String precio = lista_items.get(2);
        String estado = lista_items.get(3);
        ContentValues Vitems = new ContentValues();
        ContentValues Vstock = new ContentValues();

        Vitems.put("cod_qr", cod_qr);
        Vitems.put("descripcion", descripcion);
        Vitems.put("precio", precio);
        Vitems.put("estado", estado);

        database.insert("ITEMS", null, Vitems);

        Cursor cursor = database.rawQuery("SELECT LAST_INSERT_ROWID()",null);

        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            int cod_items = cursor.getInt(0);

            Vstock.put("cantidad", cantidad);
            Vstock.put("estado", "A");
            Vstock.put("cod_bodega", codigo_bodegadestino);
            Vstock.put("cod_items", cod_items);

            database.insert("STOCK", null, Vstock);
            eliminarProductos(codigo);
        }
        cursor.close();
    }
    //SENTENCIA TRANSFERENCIA FIN
}
