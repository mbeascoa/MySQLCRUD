package com.example.mysqlcrud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Toast;

//Se referencian las Clases necesarias para la conexi�n con el Servidor MySQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Ejemplo aplicaci�n Android que permite conectar con un servidor MySQl y realizar
//consultas sobre una Base de Datos creada

public class MainActivity extends AppCompatActivity {

    //Declaramos los componentes necesarios para introducir los datos de conexi�n al servidor
    private EditText edServidor;
    private EditText edPuerto;
    private EditText edUsuario;
    private EditText edPassword;
    private String baseDatos = "Tienda";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Enlazamos los componentes con los recursos definidos en el layout
        edServidor = (EditText)findViewById(R.id.edServidor);
        edPuerto  = (EditText)findViewById(R.id.edPuerto);
        edUsuario = (EditText)findViewById(R.id.edUsuario);
        edPassword = (EditText)findViewById(R.id.edPassword);
    }

    //Funci�n que establecer� la conexi�n con el Servidor si los datos introducidos son correcto.
    //Devuelve un valor de verdadero o falso que indicar� si se ha establecido la conexi�n
    public boolean conectarMySQL()
    {
        //Variable boolean que almacenar� si el estado de la conexi�n es true o false
        boolean estadoConexion = false;
        //Inicializamos la Clase Connection encarada de conectar con la base de datos.
        Connection conexionMySQL = null;

        //Se declaran varias variables de tipo String que almacenar�n los valores introducidos por pantalla
        String user = edUsuario.getText().toString();
        String password = edPassword.getText().toString();
        String puerto = edPuerto.getText().toString();
        String ip = edServidor.getText().toString();

        //Asignamos el driver a una variable de tipo String
        String driver = "com.mysql.jdbc.Driver";

        //Constru�mos la url para establecer la conexi�n
        String urlMySQL = "jdbc:mysql://" + ip + ":" + puerto + "/";
        try{
            //Cargamos el driver del conector JDBC
            Class.forName(driver).newInstance ();
            //Establecemos la conexi�n con el Servidor MySQL indic�ndole como par�metros la url construida,
            //la Base de Datos a la que vamos a conectarnos, y el usuario y contrase�a de acceso al servidor
            conexionMySQL = DriverManager.getConnection(urlMySQL + baseDatos, user, password);
            //Comprobamos que la conexi�n se ha establecido
            if(!conexionMySQL.isClosed())
            {
                estadoConexion = true;
                Toast.makeText(MainActivity.this,"Conexi�n Establecida", Toast.LENGTH_LONG).show();
            }
        }catch(Exception ex)
        {
            Toast.makeText(MainActivity.this,"Error al comprobar las credenciales:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally
        {
            try {
                conexionMySQL.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return estadoConexion;
    }

    //Evento On Click que realiza la llamada a la funci�n conectarMySQL() obteniendo el valor de verdadero
    //o falso para la petici�n de conexi�n
    public void abrirConexion(View view)
    {
        Intent intent = new Intent(this,ConsultasSQL.class);
        //Si el valor devuelto por la funci�n es true, pasaremos los datos de la conexi�n a la siguiente Activity
        if(conectarMySQL() == true)
        {
            Toast.makeText(this, "Los datos de conexi�n introducidos son correctos."
                    , Toast.LENGTH_LONG).show();
            intent.putExtra("servidor", edServidor.getText().toString());
            intent.putExtra("puerto", edPuerto.getText().toString());
            intent.putExtra("usuario", edUsuario.getText().toString());
            intent.putExtra("password", edPassword.getText().toString());
            intent.putExtra("datos", baseDatos);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
