package solines.rosero.villamar.ucsg.control_de_inventario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.DataSource;

public class Registro extends AppCompatActivity {

    private Button limpiar, btnregistrarse, btnlistarestaurantes;
    private EditText txtusuario,txtcontraseña,txtcorreo,txtnuevorestarurante;
    private ArrayAdapter aa_spinner;
    private Spinner genero;
    static DataSource dataSource;
    public static int validar = 0;
    public static EditText txtlistarestaurante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //************************************************
        dataSource = new DataSource(getApplicationContext());
        dataSource.open();//Apertura de la DB
        //************************************************

        genero = (Spinner) findViewById(R.id.sgenero);
        limpiar = (Button) findViewById(R.id.btnlimpiar);
        btnregistrarse = (Button) findViewById(R.id.btnregistrarse);
        txtusuario = (EditText) findViewById(R.id.txtusuario);
        txtcontraseña = (EditText) findViewById(R.id.txtcontraseña);
        txtcorreo = (EditText) findViewById(R.id.txtcorreo);
        txtlistarestaurante = (EditText) findViewById(R.id.txtlistarestaurante);
        btnlistarestaurantes = (Button) findViewById(R.id.btnlistarestaurantes);
        txtnuevorestarurante = (EditText) findViewById(R.id.txtnuevorestarurante);
        //Carga de Spinner
        aa_spinner = ArrayAdapter.createFromResource(this, R.array.genero, R.layout.support_simple_spinner_dropdown_item);
        aa_spinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genero.setAdapter(aa_spinner);

        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Limpieza();
            }
        });
        btnlistarestaurantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registro.this, Lista_Restaurantes.class);
                startActivity(i);
            }
        });
        btnregistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtusuario.getText().toString().length()>0 && txtcontraseña.getText().toString().length()>0 &&
                        txtcorreo.getText().toString().length()>0)
                {
                    //Insercion de restaurante nuevo
                    if(txtnuevorestarurante.getText().toString().length()>0 && txtlistarestaurante.getText().length()==0)
                    {
                        dataSource.insertarregistro(txtusuario.getText().toString(),txtcontraseña.getText().toString()
                                ,genero.getSelectedItem().toString(),txtcorreo.getText().toString(),txtnuevorestarurante.getText().toString());
                        if (validar==1)
                            Toast.makeText(getApplicationContext(), "El nombre restaurante ya existe.", Toast.LENGTH_SHORT).show();
                        else
                        {
                            if (validar==2)
                                Toast.makeText(getApplicationContext(), "El email ya existe.", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getApplicationContext(), "Registro Guardado", Toast.LENGTH_SHORT).show();
                                Limpieza();
                            }
                        }
                    }
                    //Insercion de restaurante de la lista
                    else if(txtnuevorestarurante.getText().toString().length()==0 && txtlistarestaurante.getText().length()>0)
                    {
                        dataSource.insertarlista(txtusuario.getText().toString(),txtcontraseña.getText().toString()
                                ,genero.getSelectedItem().toString(),txtcorreo.getText().toString(),txtlistarestaurante.getText().toString());
                        if (validar==2)
                            Toast.makeText(getApplicationContext(), "El email ya existe.", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(getApplicationContext(), "Registro Guardado", Toast.LENGTH_SHORT).show();
                            Limpieza();
                        }
                    }
                    else if(txtnuevorestarurante.getText().length()>0 && txtlistarestaurante.getText().length()>0)
                    {
                        Toast.makeText(Registro.this, "No pueden haber 2 campos restaurantes por favor limpie", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Registro.this, "Falta campo restaurante", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Campos obligatorio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void Limpieza()
    {
        txtusuario.setText(null);
        txtcontraseña.setText(null);
        genero.setAdapter(aa_spinner);
        txtcorreo.setText(null);
        txtlistarestaurante.setText(null);
        txtnuevorestarurante.setText(null);
    }
}
