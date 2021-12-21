package com.example.basicactivity;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorNombre extends RecyclerView.Adapter<AdaptadorNombre.ViewHolderNombres> {
    ArrayList<Name> listNombres = new ArrayList<Name>();

    //constructor
    public AdaptadorNombre (ArrayList<Name> listNombres){
        this.listNombres = listNombres;
    }

    @NonNull

    @Override
    //creando vista del layout
    public ViewHolderNombres onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AdaptadorNombre.ViewHolderNombres holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AdaptadorNombre holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderNombres {
    }
}
