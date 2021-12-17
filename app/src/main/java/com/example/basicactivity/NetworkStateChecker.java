package com.example.basicactivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkStateChecker extends BroadcastReceiver {

    private Context context;
    private DatabaseHelper db;

    @SuppressLint("Range")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        db = new DatabaseHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //si hay red
        if (activeNetwork !=null){
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                //traer todos los registros no sincronizados
                Cursor cursor = db.getUnsynceNames();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveName(
                                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))
                        );
                    } while (cursor.moveToNext());
                }
            }
    }
}
    /*Si el registro se envio correctamente se atualiza en sqlite*/
    private void saveName(final int id, final String name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SecondFragment.URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //actualizar estado en sqlite
                                db.updateNameStatus(id, SecondFragment.NAME_SYNCED_WITH_SERVER);

                                //enviando  trasmision para refrescar la lista
                                context.sendBroadcast(new Intent((SecondFragment.DATA_SAVED_BRODCAST)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    protected Map <String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name);
                        return params;
                        }
                    };
                    VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
           }
}
