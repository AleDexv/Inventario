package solines.rosero.villamar.ucsg.control_de_inventario;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.Empleados;

public class Bodega extends Fragment {

    private EditText txtbodega;
    private Button btncrearbodega;
    public static boolean validar = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_bodega, container, false);
        View view =  inflater.inflate(R.layout.fragment_bodega, container, false);

        txtbodega = (EditText) view.findViewById(R.id.txtbodega);
        btncrearbodega = (Button) view.findViewById(R.id.btncrearbodega);

        btncrearbodega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtbodega.getText().toString().length()>0)
                {
                    Empleados e = new Empleados();
                    Menu.dataSource.insertarBodega(txtbodega.getText().toString(),e.getCod_restaurantes());
                    txtbodega.setText(null);
                    if (validar)
                        Toast.makeText(getActivity(), "Ya existe el nombre bodega", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Bodega guardada", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "La bodega esta vacia.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
