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

public class Egreso extends Fragment {

    private Spinner spbodega,spproductos;
    private EditText txtcantdisponible,txtcantidad;
    private Button btnegresar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.activity_egreso, container, false);
        View view = inflater.inflate(R.layout.activity_egreso, container, false);

        spbodega = (Spinner) view.findViewById(R.id.spbodega);
        spproductos = (Spinner) view.findViewById(R.id.spproductos);
        txtcantdisponible = (EditText) view.findViewById(R.id.txtcantdisponible);
        txtcantidad = (EditText) view.findViewById(R.id.txtcantidad);
        btnegresar = (Button) view.findViewById(R.id.btnegresar);

        loadSpinnerDataBodega();
        loadSpinnerDataProductos();
        loadcantidadDisponible();

        spbodega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        btnegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtcantidad.getText().toString().length() > 0) {
                    if (Integer.valueOf(txtcantidad.getText().toString()) <= Integer.valueOf(txtcantdisponible.getText().toString())) {
                        Empleados e = new Empleados();
                        int resultado_cantidad = Integer.valueOf(txtcantdisponible.getText().toString()) - Integer.valueOf(txtcantidad.getText().toString());
                        Menu.dataSource.actualizar_cantidad(resultado_cantidad, spbodega.getSelectedItem().toString(), spproductos.getSelectedItem().toString(), e.getCod_restaurantes());
                        Toast.makeText(getActivity(), "Se egreso satisfactoriamente", Toast.LENGTH_SHORT).show();
                        txtcantidad.setText(null);
                        loadcantidadDisponible();
                    } else
                        Toast.makeText(getActivity(), "Solo hay " + txtcantdisponible.getText().toString() + " cantidad disponible", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Faltan campos", Toast.LENGTH_SHORT).show();
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
    private void loadSpinnerDataProductos(){
        Empleados e = new Empleados();
        List<String> lables = Menu.dataSource.listaProductos(spbodega.getSelectedItem().toString(),e.getCod_restaurantes());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spproductos.setAdapter(dataAdapter);
    }
    private void loadcantidadDisponible(){
        Empleados e = new Empleados();
        String disponible = Menu.dataSource.Obtener_cantidad(spbodega.getSelectedItem().toString(), spproductos.getSelectedItem().toString(), e.getCod_restaurantes());
        txtcantdisponible.setText(disponible);
    }
}
