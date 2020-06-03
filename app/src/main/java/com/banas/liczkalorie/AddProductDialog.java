package com.banas.liczkalorie;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.banas.liczkalorie.Database.DatabaseHelper;

public class AddProductDialog extends DialogFragment {
    private EditText edtNazwa;
    private EditText edtKcal;
    private EditText edtWeglowodany;
    private EditText edtTluszcz;
    private EditText edtBialko;
    DatabaseHelper databaseHelper;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        databaseHelper = new DatabaseHelper(getActivity());
        View view = inflater.inflate(R.layout.add_product_dialog, null);
        builder.setView(view);
        builder.setTitle("Dodaj produkt");
        builder.setPositiveButton("dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nazwa = edtNazwa.getText().toString();
                String kcal = edtKcal.getText().toString();
                String weglowodany = edtWeglowodany.getText().toString();
                String tluszcz = edtTluszcz.getText().toString();
                String bialko = edtBialko.getText().toString();
                if (nazwa.equals("") || kcal.equals("") || weglowodany.equals("") || tluszcz.equals("") || bialko.equals((""))) {
                    Toast.makeText(getActivity(), "Uzupełnij wszystkie pola", Toast.LENGTH_LONG).show();
                } else {
                    boolean isInserted = databaseHelper.addProduct(nazwa, kcal, weglowodany, tluszcz, bialko, 1);
                    if (isInserted) {
                        Toast.makeText(getActivity(), "Dodano produkt", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Nie dodano produktu, popraw błędy", Toast.LENGTH_LONG).show();
                    }
                }
                getTargetFragment().getActivity().getSupportFragmentManager().beginTransaction().detach(getTargetFragment()).attach(getTargetFragment()).commit();
            }
        });
        builder.setNegativeButton("anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //getTargetFragment().getActivity().getSupportFragmentManager().beginTransaction().detach(getTargetFragment()).attach(getTargetFragment()).commit();
            }
        });

        edtNazwa=(EditText) view.findViewById(R.id.nazwa);
        edtKcal=(EditText) view.findViewById(R.id.kcal);
        edtWeglowodany=(EditText) view.findViewById(R.id.weglowodany);
        edtTluszcz=(EditText) view.findViewById(R.id.tluszcz);
        edtBialko=(EditText) view.findViewById(R.id.bialko);
        return builder.create();
    }

}
