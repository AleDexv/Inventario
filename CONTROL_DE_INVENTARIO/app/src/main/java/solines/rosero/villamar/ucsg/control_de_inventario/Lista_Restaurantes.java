package solines.rosero.villamar.ucsg.control_de_inventario;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.Restaurantes;

public class Lista_Restaurantes extends AppCompatActivity {

    private Button volver;
    private ArrayList<String> al_lista;
    private ArrayAdapter<String> aa_lista;
    private ListView lv_lista;

    private List<Restaurantes> restaurantes;
    private String restaurante_seleccionado="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__restaurantes);

        volver = (Button) findViewById(R.id.btnvolver);

        al_lista = new ArrayList<String>();

        restaurantes = Registro.dataSource.consultar();

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (int i = 0; i < restaurantes.size(); i++)
        {
            al_lista.add(restaurantes.get(i).getNombre());
        }

        aa_lista = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al_lista);
        lv_lista = (ListView) findViewById(R.id.lv_restaurantes);

        lv_lista.setAdapter(aa_lista);

        lv_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                restaurante_seleccionado = al_lista.get(i);
                Registro.txtlistarestaurante.setText(restaurante_seleccionado);
                Toast.makeText(Lista_Restaurantes.this, restaurante_seleccionado, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
