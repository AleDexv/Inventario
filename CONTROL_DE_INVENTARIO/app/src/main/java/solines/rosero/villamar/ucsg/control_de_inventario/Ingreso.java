package solines.rosero.villamar.ucsg.control_de_inventario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.Empleados;

public class Ingreso extends Fragment {

    private Button btnescanear, btnguardar;
    private EditText txtcodigo, txtdescripcion, txtcantidad, txtprecio;
    private Spinner spbodega;
    public static int validar = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.activity_ingreso, container, false);
        View view = inflater.inflate(R.layout.activity_ingreso, container, false);

        btnescanear = (Button) view.findViewById(R.id.btnescanear);
        btnguardar = (Button) view.findViewById(R.id.btnguardar);
        txtcodigo = (EditText) view.findViewById(R.id.txtcodigo);
        txtdescripcion = (EditText) view.findViewById(R.id.txtdescripcion);
        txtcantidad = (EditText) view.findViewById(R.id.txtcantidad);
        txtprecio = (EditText) view.findViewById(R.id.txtprecio);
        spbodega = (Spinner) view.findViewById(R.id.spbodega);

        loadSpinnerDataBodega();

        btnescanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(Ingreso.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);//PUEDES CAMBIAR EL TIPO DE CODIGOS
                //integrator.setPrompt("Scan"); //Mensaje en el escaner
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);//sonido del beep
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);//cambia la orientacion vertical o horizontal(revisar el manifest activity)
                integrator.initiateScan();
            }
        });

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtcodigo.getText().toString().length()>0 && txtdescripcion.toString().length()>0
                        && txtcantidad.toString().length()>0 && txtprecio.getText().toString().length()>0)
                {
                    Menu.dataSource.insertarIngreso(txtcodigo.getText().toString(),txtdescripcion.getText().toString(),
                            Float.valueOf(txtprecio.getText().toString()),Integer.valueOf(txtcantidad.getText().toString()),spbodega.getSelectedItem().toString());
                    limpiar();
                    if (validar==1)
                        Toast.makeText(getActivity(), "Ya existe el codigo de producto", Toast.LENGTH_SHORT).show();
                    else if(validar==2)
                    {Toast.makeText(getActivity(), "Ya existe el nombre de producto", Toast.LENGTH_SHORT).show();}
                    else
                        Toast.makeText(getActivity(), "Ingreso guardado", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Faltan campos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void limpiar()
    {
        txtcodigo.setText(null);
        txtdescripcion.setText(null);
        txtcantidad.setText(null);
        txtprecio.setText(null);
        loadSpinnerDataBodega();
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
