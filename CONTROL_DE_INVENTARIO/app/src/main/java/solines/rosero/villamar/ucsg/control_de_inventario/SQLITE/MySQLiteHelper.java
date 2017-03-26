package solines.rosero.villamar.ucsg.control_de_inventario.SQLITE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by sukasa on 12/02/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Bases.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLA_EMPLEADOS = "CREATE TABLE EMPLEADOS(" +
            " codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
            " usuario TEXT NOT NULL, " +
            " contraseña TEXT NOT NULL, " +
            " genero TEXT NOT NULL, " +
            " email TEXT UNIQUE NOT NULL, " +
            " estado TEXT, " +
            " cod_restaurantes INTEGER," +
            " FOREIGN KEY(cod_restaurantes) REFERENCES RESTAURANTES(codigo)" +
            ");";
    private static final String TABLA_RESTAURANTES = "CREATE TABLE RESTAURANTES (" +
            " codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " nombre TEXT UNIQUE NOT NULL, " +
            " estado TEXT " + ");";
    private static final String TABLA_BODEGA = "CREATE TABLE BODEGA(" +
            " codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " nombre TEXT NOT NULL, " +
            " estado TEXT, " +
            " cod_restaurantes INTEGER," +
            " FOREIGN KEY(cod_restaurantes) REFERENCES RESTAURANTES(codigo)" +
            ");";
    private static final String TABLA_STOCK = "CREATE TABLE STOCK (" +
            " codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " cantidad INTEGER NOT NULL, " +
            " estado TEXT, " +
            " cod_bodega INTEGER, " +
            " cod_items INTEGER, " +
            " FOREIGN KEY(cod_bodega) REFERENCES BODEGA(codigo), " +
            " FOREIGN KEY(cod_items) REFERENCES ITEMS(codigo)" +
            ");";
    private static final String TABLA_ITEMS = "CREATE TABLE ITEMS (" +
            " codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " cod_qr TEXT NOT NULL, " +
            " descripcion TEXT NOT NULL, " +
            " precio DECIMAL(5,2) NOT NULL, " +
            " estado TEXT " + ");";
    /*private static final String FK_EMPLEADOS = "ALTER TABLE EMPLEADOS ADD COLUMN cod_restaurantes INTEGER REFERENCES RESTAURANTES(codigo)";
    private static final String FK_BODEGA = "ALTER TABLE BODEGA ADD COLUMN cod_restaurantes INTEGER REFERENCES RESTAURANTES(codigo)";
    private static final String FK_STOCK = "ALTER TABLE STOCK ADD COLUMN cod_bodega INTEGER REFERENCES BODEGA(codigo);\n" +
            "ALTER TABLE STOCK ADD COLUMN cod_items INTEGER REFERENCES ITEMS(codigo);";*/
    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_EMPLEADOS);
        db.execSQL(TABLA_RESTAURANTES);
        db.execSQL(TABLA_BODEGA);
        db.execSQL(TABLA_STOCK);
        db.execSQL(TABLA_ITEMS);
        /*db.execSQL(FK_EMPLEADOS);
        db.execSQL(FK_BODEGA);
        db.execSQL(FK_STOCK);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS EMPLEADOS");
        db.execSQL("DROP TABLE IF EXISTS RESTAURANTES");
        db.execSQL("DROP TABLE IF EXISTS BODEGA");
        db.execSQL("DROP TABLE IF EXISTS STOCK");
        db.execSQL("DROP TABLE IF EXISTS ITEMS");
        onCreate(db);
    }
}
