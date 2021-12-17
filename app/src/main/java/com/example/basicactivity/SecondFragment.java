package com.example.basicactivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.basicactivity.databinding.FragmentSecondBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

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
    public static final String DATA_SAVED_BRODCAST = "net.simplifiedcoding.datasaved";

    //Receptor de la trasmisión que indica el estado de la sincronozación
    private BroadcastReceiver broadcastReceiver;

    //adaptador listView  NameAdapter
    private NameAdapter nameAdapter;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        db= new DatabaseHelper(getActivity().getApplicationContext());
        names = new ArrayList<>();

        broadcastReceiver = new BroadcastReceiver(){
            public void onReceive (Context context, Intent intent){
                loadNames();
            }
        };
        registerReceiver (broadcastReceiver, new IntentFilter(DATA_SAVED_BRODCAST));

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNameToLocalStorage();

            }
        });
    }

    private void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
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
        nameAdapter = new NameAdapter(getActivity().getApplicationContext(), R.layout.names, names);
        listViewNames.setAdapter(nameAdapter);
    }

    /*Este metodo actualiza la lista*/
    private void refreshlist(){nameAdapter.notifyDataSetChanged();}

    /*Este metodo guarda los registros de forma local*/
    private void saveNameToLocalStorage(String name, int status) {
        editTextName.setText("");
        db.addName(name, status);
        Name n = new Name(name, status);
        names.add(n);
        refreshlist();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}