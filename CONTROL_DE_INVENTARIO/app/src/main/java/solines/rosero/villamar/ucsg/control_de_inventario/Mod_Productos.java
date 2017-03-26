package solines.rosero.villamar.ucsg.control_de_inventario;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Mod_Productos extends AppCompatActivity {

    private EditText txtcodigo,txtdescripcion,txtcantidad,txtprecio;
    private Button btnmodificar, btnvolver, btneliminar;
    private static int codigo, cantidad;
    private static String cod_qr, descripcion, estado;
    private static float precio;

    public int getCodigo() {
        return codigo;
    }

    public static void setCodigo(int codigo) {
        Mod_Productos.codigo = codigo;
    }
    public String getCod_qr() {
        return cod_qr;
    }

    public static void setCod_qr(String cod_qr) {
        Mod_Productos.cod_qr = cod_qr;
    }

    public int getCantidad() {
        return cantidad;
    }

    public static void setCantidad(int cantidad) {
        Mod_Productos.cantidad = cantidad;
    }

    public static String getDescripcion() {
        return descripcion;
    }

    public static void setDescripcion(String descripcion) {
        Mod_Productos.descripcion = descripcion;
    }

    public static String getEstado() {
        return estado;
    }

    public static void setEstado(String estado) {
        Mod_Productos.estado = estado;
    }

    public float getPrecio() {
        return precio;
    }

    public static void setPrecio(float precio) {
        Mod_Productos.precio = precio;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod__productos);

        txtcodigo = (EditText) findViewById(R.id.txtcodigo);
        txtdescripcion = (EditText) findViewById(R.id.txtdescripcion);
        txtcantidad = (EditText) findViewById(R.id.txtcantidad);
        txtprecio = (EditText) findViewById(R.id.txtprecio);
        btnmodificar = (Button) findViewById(R.id.btnmodificar);
        btneliminar = (Button) findViewById(R.id.btneliminar);
        btnvolver = (Button) findViewById(R.id.btnvolver);

        txtcodigo.setText(getCod_qr());
        txtdescripcion.setText(getDescripcion());
        txtcantidad.setText(String.valueOf(getCantidad()));
        txtprecio.setText(String.valueOf(getPrecio()));

        btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnmodificar.getText().toString().equals("Guardar"))
                {
                    txtcodigo.setEnabled(false);
                    txtdescripcion.setEnabled(false);
                    txtcantidad.setEnabled(false);
                    txtprecio.setEnabled(false);
                    btnmodificar.setText("Modificar");
                    Menu.dataSource.modificarProductos(getCodigo(),txtcodigo.getText().toString(),txtdescripcion.getText().toString(),
                            txtcantidad.getText().toString(),txtprecio.getText().toString());
                    Toast.makeText(Mod_Productos.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txtcodigo.setEnabled(true);
                    txtdescripcion.setEnabled(true);
                    txtcantidad.setEnabled(true);
                    txtprecio.setEnabled(true);
                    btnmodificar.setText("Guardar");
                }
            }
        });
        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu.dataSource.eliminarProductos(getCodigo());
                Toast.makeText(Mod_Productos.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
