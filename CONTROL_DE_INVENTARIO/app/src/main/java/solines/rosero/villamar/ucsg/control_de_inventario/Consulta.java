package solines.rosero.villamar.ucsg.control_de_inventario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.Empleados;
import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.Items;

public class Consulta extends Fragment {

    private Button btnescanear,btnbuscar;
    private EditText txtcodigo;
    private Spinner spbodega;
    private ArrayList<String> al_items;
    private ArrayAdapter<String> aa_items;
    private ListView lv_items;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.activity_consulta, container, false);
        View view = inflater.inflate(R.layout.activity_consulta, container, false);

        btnescanear = (Button) view.findViewById(R.id.btnescanear);
        txtcodigo = (EditText) view.findViewById(R.id.txtcodigo);
        btnbuscar = (Button) view.findViewById(R.id.btnbuscar);
        spbodega = (Spinner) view.findViewById(R.id.spbodega);
        lv_items = (ListView) view.findViewById(R.id.lv_items);

        loadSpinnerDataBodega();

        btnescanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(Consulta.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);//PUEDES CAMBIAR EL TIPO DE CODIGOS
                //integrator.setPrompt("Scan"); //Mensaje en el escaner
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);//sonido del beep
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);//cambia la orientacion vertical o horizontal(revisar el manifest activity)
                integrator.initiateScan();
            }
        });
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al_items = new ArrayList<String>();
                Empleados E = new Empleados();
                List<Items> items = Menu.dataSource.consultarbusqueda(txtcodigo.getText().toString(),spbodega.getSelectedItem().toString(), E.getCod_restaurantes());

                for (int i = 0; i < items.size(); i++)
                {
                    al_items.add(items.get(i).getDescripcion());
                }
                aa_items = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al_items);
                lv_items.setAdapter(aa_items);
            }
        });
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String seleccionadoDescripcion = al_items.get(i);
                Toast.makeText(getActivity(), seleccionadoDescripcion, Toast.LENGTH_SHORT).show();
                Menu.dataSource.ObtenerDatosDescripcion(seleccionadoDescripcion,spbodega.getSelectedItem().toString());
                Intent e = new Intent(getActivity(), Mod_Productos.class);
                startActivity(e);
            }
        });
        return view;
    }
    private void loadSpinnerDataBodega() {
        Empleados e = new Empleados();
        List<String> lables = Menu.dataSource.listaBodega(e.getCod_restaurantes());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spbodega.setAdapter(dataAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(getContext(), "Se cancelo el escaneo", Toast.LENGTH_SHORT).show();
            }
            else
            {
                txtcodigo.setText(result.getContents());
                Toast.makeText(getContext(), "Codigo de barra agregado", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
