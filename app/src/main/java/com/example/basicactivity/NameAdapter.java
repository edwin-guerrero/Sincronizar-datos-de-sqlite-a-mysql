package com.example.basicactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class NameAdapter extends ArrayAdapter<Name> {

    /*Almacena todos los nombres de la lista*/
    private List<Name> names;

    private Context context;


    public NameAdapter(@NonNull Context context, int resource, List<Name> names) {
        super(context, resource);
        this.context = context;
        this.names = names;
    }

    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //obtiene lista de items
        View listViewItem = inflater.inflate(R.layout.names,null,true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView);
        ImageView imageViewStatus = (ImageView) listViewItem.findViewById(R.id.imageViewStatus);

        //obteniendo nombre actual
        Name name = names.get(position);

        //pasando nombres al text view
        textViewName.setText(name.getName());

        //si el estado es 0 se muestra icono en cola , adicional miestra el icono
        if (name.getStatus()==0)
            imageViewStatus.setImageResource(android.R.drawable.presence_away);
        else
            imageViewStatus.setImageResource(android.R.drawable.presence_online);

        return listViewItem;

    }

}
