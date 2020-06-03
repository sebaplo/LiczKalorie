package com.banas.liczkalorie;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.banas.liczkalorie.Adapter.ProductListAdapter;
import com.banas.liczkalorie.Database.DatabaseHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddEatenDialog extends DialogFragment {

    private EditText edtAmount;
    private TextView textDay2;
    private int pr_id, dateFinal;
    Button btnAddDay2, btnSubstractDay2;
    public String strDate;
    public Date date;
    public String date2;
    Calendar calendar = new GregorianCalendar();

    DatabaseHelper databaseHelper;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //pr_id=Integer.parseInt(getActivity().getIntent().getExtras().get("pr_id").toString());
        Bundle bundle = getArguments();
        pr_id = bundle.getInt("pr_id");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        databaseHelper = new DatabaseHelper(getActivity());
        View view = inflater.inflate(R.layout.add_eaten_dialog, null);

        //kalendarz
        btnAddDay2 = view.findViewById(R.id.dayForward2);
        btnSubstractDay2 = view.findViewById(R.id.dayBackward2);
        textDay2 = view.findViewById(R.id.day2);

        strDate = DateFormat.getDateInstance().format(calendar.getTime());
        textDay2.setText(strDate);

        btnAddDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH,1);
                strDate = DateFormat.getDateInstance().format(calendar.getTime());
                if(strDate.equals("02.05.2020")){
                    btnSubstractDay2.setEnabled(true);
                }
                if(strDate.equals("2020-05-02")){
                    btnSubstractDay2.setEnabled(true);
                }
                if(strDate.equals("May 2, 2020")){
                    btnSubstractDay2.setEnabled(true);
                }
                textDay2.setText(strDate);
            }
        });

        btnSubstractDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH,-1);
                strDate = DateFormat.getDateInstance().format(calendar.getTime());
                if(strDate.equals("01.05.2020")){
                    btnSubstractDay2.setEnabled(false);
                }
                if(strDate.equals("2020-05-01")){
                    btnSubstractDay2.setEnabled(false);
                }
                if(strDate.equals("May 1, 2020")){
                    btnSubstractDay2.setEnabled(false);
                }
                textDay2.setText(strDate);
            }
        });

        builder.setView(view);
        builder.setTitle("Dodaj produkt");
        builder.setPositiveButton("dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat df1 = new SimpleDateFormat("MMM dd, yyyy");
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                //format polski
                try {
                    date = df.parse(strDate);
                    if (date != null) {
                        date2 = df2.format(date);
                    }
                }catch (Exception e) {
                }
                //format angielski
                try {
                    date = df1.parse(strDate);
                    if (date != null) {
                        date2 = df2.format(date);
                    }
                }catch (Exception e) {
                }

                String amount = edtAmount.getText().toString();
                Cursor res = databaseHelper.checkDate(date2);
                if (res.getCount()==0){
                    Toast.makeText( getActivity(), "Brak danych", Toast.LENGTH_SHORT).show();
                }

                while (!res.isAfterLast()) {
                    res.moveToNext();
                    if (res.getString(1).equals(date2)) {
                        dateFinal = res.getInt(0);
                        break;
                    }
                }

                //Toast.makeText( getActivity(), res.getString(0)+res.getString(1)+res.getString(2) , Toast.LENGTH_LONG).show();
                if (amount.equals(("")) || dateFinal==0 ) {
                    Toast.makeText(getActivity(), "Wprowadź poprawne dane", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = databaseHelper.addEatenProduct(dateFinal,pr_id,amount);
                    if (isInserted) {
                        Toast.makeText(getActivity(), "Dodano produkt", Toast.LENGTH_SHORT).show();
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
                getTargetFragment().getActivity().getSupportFragmentManager().beginTransaction().detach(getTargetFragment()).attach(getTargetFragment()).commit();
            }
        });

        edtAmount=(EditText) view.findViewById(R.id.amount);

        return builder.create();
    }


}
