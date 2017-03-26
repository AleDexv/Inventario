package solines.rosero.villamar.ucsg.control_de_inventario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.DataSource;

public class Login extends AppCompatActivity {

    private Button iniciar, registro;
    private EditText txtcorreo, txtcontraseña;
    static DataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //************************************************
        dataSource = new DataSource(getApplicationContext());
        dataSource.open();//Apertura de la DB
        //************************************************

        iniciar = (Button) findViewById(R.id.btniniciar);
        registro = (Button) findViewById(R.id.btnregistro);
        txtcorreo = (EditText) findViewById(R.id.txtcorreo);
        txtcontraseña = (EditText) findViewById(R.id.txtcontraseña);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtcorreo.getText().toString();
                String pass = txtcontraseña.getText().toString();
                if(dataSource.getUser(email, pass))
                {
                    Intent i = new Intent(Login.this, Menu.class);
                    startActivity(i);
                    finish();
                }
                else
                    Toast.makeText(Login.this, "Correo/Constraseña incorrecto", Toast.LENGTH_SHORT).show();
            }
        });
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registro.class);
                startActivity(i);
            }
        });
    }
}
