package solines.rosero.villamar.ucsg.control_de_inventario;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.DataSource;
import solines.rosero.villamar.ucsg.control_de_inventario.SQLITE.Empleados;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static DataSource dataSource;
    private TextView lblusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Llama a la base DataSource para abrir la base de datos
        //************************************************
        dataSource = new DataSource(getApplicationContext());
        dataSource.open();//Apertura de la DB
        //************************************************

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //CARGA LOS DATOS nombre y email en MENU LATERAL
        View view =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView) view.findViewById(R.id.lblusuario);
        TextView nav_email = (TextView) view.findViewById(R.id.lblcorreo);
        Empleados e = new Empleados();
        nav_user.setText(e.getNombre());
        nav_email.setText(e.getEmail());

        if(dataSource.verficar_bodega(e.getCod_restaurantes())) {
            //CARGA PRIMERO EN EL CONTENEDOR CONSULTA
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Consulta()).commit();
            setTitle(R.string.consulta);
        }
        else
        //Muestra cuadro de dialogo
        new SimpleDialog().show(getSupportFragmentManager(), "SimpleDialog");

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_consulta) {
            // Handle the camera action
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Consulta()).commit();
            setTitle(R.string.consulta);
        } else if (id == R.id.nav_ingreso) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Ingreso()).commit();
            setTitle(R.string.ingreso);
        } else if (id == R.id.nav_egreso) {
            Empleados e = new Empleados();
            if (dataSource.verficar_productos(e.getCod_restaurantes())) {
                fragmentManager.beginTransaction().replace(R.id.contenedor, new Egreso()).commit();
                setTitle(R.string.egreso);
            }
            else
            {new SimpleDialogEgreso().show(getSupportFragmentManager(), "SimpleDialog");}
        } else if (id == R.id.nav_transferencia) {
            Empleados e = new Empleados();
            if (dataSource.verficar_productos(e.getCod_restaurantes())) {
                fragmentManager.beginTransaction().replace(R.id.contenedor, new Transferencia()).commit();
                setTitle(R.string.transferencia);
            }
            else
            {new SimpleDialogEgreso().show(getSupportFragmentManager(), "SimpleDialog");}
        } else if (id == R.id.nav_bodega) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Bodega()).commit();
            setTitle(R.string.bodega);
        } /*else if (id == R.id.nav_miperfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new MiPerfil()).commit();
            setTitle(R.string.miperfil);
        }*/ else if (id == R.id.nav_cerrarsesion) {
            Intent i = new Intent(Menu.this, Login.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class SimpleDialog extends DialogFragment {

        public SimpleDialog() {
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return createSimpleDialog();
        }
        //sirve para al momento de dar toque fuera del dialogo
        @Override
        public void onCancel(DialogInterface dialog) {
            getActivity().finish();
        }

        /**
         * Crea un diálogo de alerta sencillo
         * @return Nuevo diálogo
         */
        public AlertDialog createSimpleDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Aviso")

                    .setMessage("Por favor, debe ingresar productos antes de entrar a engreso.")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.contenedor, new Bodega()).commit();
                                    getActivity().setTitle(R.string.bodega);
                                }
                            })
                    .setNegativeButton("CANCELAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getActivity(), Login.class);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            });
            return builder.create();
        }
    }
    public static class SimpleDialogEgreso extends DialogFragment {

        public SimpleDialogEgreso() {
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return createSimpleDialog();
        }
        //sirve para al momento de dar toque fuera del dialogo
        @Override
        public void onCancel(DialogInterface dialog) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Ingreso()).commit();
            getActivity().setTitle(R.string.ingreso);
        }

        /**
         * Crea un diálogo de alerta sencillo
         * @return Nuevo diálogo
         */
        public AlertDialog createSimpleDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Aviso")

                    .setMessage("Por favor cree una bodega antes de hacer el ingreso.")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.contenedor, new Ingreso()).commit();
                                    getActivity().setTitle(R.string.ingreso);
                                }
                            });
            return builder.create();
        }
    }
}

