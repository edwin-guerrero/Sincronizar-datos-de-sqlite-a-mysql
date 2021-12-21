package com.example.basicactivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.basicactivity.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //URl del webservice
    public static final String URL_SAVE_NAME = "http:";

    //objeto database
    private DatabaseHelper db;

    //vista objetos
    private Button buttonSave;
    private EditText editTextName;
    private ListView listViewNames;

    //Lista que almacena los nombres
    private List<Name> names;

    //1 sincronizado 0 no sincronizado
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //trasmision para saber si los datos se sincornizaron
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Receptor de la trasmisión que indica el estado de la sincronozación
    private BroadcastReceiver broadcastReceiver;

    //adaptador listView  NameAdapter
    private NameAdapter nameAdapter;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        db= new DatabaseHelper(this);
        names = new ArrayList<>();

        loadNames();

        //receptor de transmisión para actualizar el estado de sincronización
        broadcastReceiver = new BroadcastReceiver(){
            public void onReceive (Context context, Intent intent){
                //Carga los registros nuevamente
                loadNames();
            }
        };

    }

    /*Este metodo carga los nombres de la base local con estado de sincronizacion */
    private void loadNames() {
        names.clear();
        Cursor cursor = db.getNames();
        if (cursor.moveToFirst()){
            do{
                @SuppressLint("Range") Name name = new Name(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATE))
                );
                names.add(name);
            }while (cursor.moveToNext());
        }

        nameAdapter = new NameAdapter(this, R.layout.names, names);
        //nameAdapter = new NameAdapter(binding.getRoot().getContext(), binding.listViewNames.getContext(), names);


        //nameAdapter = new NameAdapter(activity, names);
        //binding.listViewNames.setAdapter(NameAdapter);
        listViewNames.setAdapter(nameAdapter);

    }

    /*Este metodo actualiza la lista*/
    private void refreshlist(){nameAdapter.notifyDataSetChanged();}

    /*Este metodo guarda los registros de forma local*/
    private void saveNameToLocalStorage(String name, int status) {
        binding.editTextName.setText("");
        db.addName(name, status);
        Name n = new Name(name, status);
        names.add(n);
        refreshlist();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}