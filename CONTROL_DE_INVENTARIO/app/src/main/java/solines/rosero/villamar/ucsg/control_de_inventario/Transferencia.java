package solines.rosero.villamar.ucsg.control_de_inventario;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.Empleados;

public class Transferencia extends Fragment {

    private Spinner spbodegaorigen,spbodegadestino,spproductos;
    private EditText txtcantdisponible;
    private Button btntransferencia;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.activity_transferencia, container, false);
        View view = inflater.inflate(R.layout.activity_transferencia, container, false);

        spbodegaorigen = (Spinner) view.findViewById(R.id.spbodegaorigen);
        spbodegadestino = (Spinner) view.findViewById(R.id.spbodegadestino);
        spproductos = (Spinner) view.findViewById(R.id.spproductos);
        txtcantdisponible = (EditText) view.findViewById(R.id.txtcantdisponible);
        btntransferencia = (Button) view.findViewById(R.id.btntransferencia);

        loadSpinnerDataBodegaOrigen();
        loadSpinnerDataBodegaDestino();
        loadSpinnerDataProductos();
        loadcantidadDisponible();

        spbodegaorigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadSpinnerDataProductos();
                loadcantidadDisponible();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spproductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadcantidadDisponible();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btntransferencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spbodegaorigen.getSelectedItem().toString().equals(spbodegadestino.getSelectedItem().toString()))
                {
                    Toast.makeText(getActivity(), "No se puede transferir de la misma Bodega "+spbodegaorigen.getSelectedItem().toString()+" y Bodega "+spbodegadestino.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Menu.dataSource.insertarTransferencia(spbodegaorigen.getSelectedItem().toString(),spproductos.getSelectedItem().toString(),txtcantdisponible.getText().toString(),spbodegadestino.getSelectedItem().toString());
                    Toast.makeText(getActivity(), "Se Transfirió producto " +spproductos.getSelectedItem().toString()+ ", enviado a "+spbodegadestino.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    loadSpinnerDataBodegaOrigen();
                    loadSpinnerDataBodegaDestino();
                    loadSpinnerDataProductos();
                    loadcantidadDisponible();
                }
            }
        });

        return view;
    }
    private void loadSpinnerDataBodegaOrigen() {
        Empleados e = new Empleados();
        List<String> lables = Menu.dataSource.listaBodega(e.getCod_restaurantes());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spbodegaorigen.setAdapter(dataAdapter);
    }
    private void loadSpinnerDataBodegaDestino() {
        Empleados e = new Empleados();
        List<String> lables = Menu.dataSource.listaBodega(e.getCod_restaurantes());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spbodegadestino.setAdapter(dataAdapter);
    }
    private void loadSpinnerDataProductos(){
        Empleados e = new Empleados();
        List<String> lables = Menu.dataSource.listaProductos(spbodegaorigen.getSelectedItem().toString(),e.getCod_restaurantes());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spproductos.setAdapter(dataAdapter);
    }
    private void loadcantidadDisponible(){
        Empleados e = new Empleados();
        String disponible = Menu.dataSource.Obtener_cantidad(spbodegaorigen.getSelectedItem().toString(), spproductos.getSelectedItem().toString(), e.getCod_restaurantes());
        txtcantdisponible.setText(disponible);
    }
}
