package com.banas.liczkalorie;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.banas.liczkalorie.Database.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class tabProfile extends Fragment {

    DatabaseHelper databaseHelper;
    EditText editName, editWeight,editHeight,editAge;
    RadioGroup radioGroup;
    Button buttonSaveProfile, btnInfo2;
    String gender;
    TextView textView2;
    private int bmr, weglowodany, tluszcz, bialko;
    private double weight,height, bmi;

    public tabProfile() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    public void refresh(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_profile, container, false);

        // Inflate the layout for this fragment
        //radioGroup = container.findViewById(R.id.radioGroupGender);
        databaseHelper = new DatabaseHelper(getActivity());
        editName= (EditText) v.findViewById(R.id.editName);
        editWeight= (EditText) v.findViewById(R.id.editWeight);
        editHeight= (EditText) v.findViewById(R.id.editHeight);
        editAge= (EditText) v.findViewById(R.id.editAge);
        radioGroup= (RadioGroup) v.findViewById(R.id.radioGroupGender);
        buttonSaveProfile = (Button) v.findViewById(R.id.btnSaveProfile);
        showHints();
        //viewAll();
        addData();
        textView2 = (TextView) v.findViewById(R.id.textView2);
        Cursor res = databaseHelper.getProfileData();
        if (res.getCount()==0){
            Toast.makeText( getActivity(), "Brak danych", Toast.LENGTH_LONG).show();
        }
        if(res.moveToFirst()){
            //oblicznaie BMI
            weight=res.getDouble(1);
            height=(res.getDouble(2)/100);
            bmi=(weight/(height*height));
            bmi=Math.round(bmi*100.0)/100.0;
            //oblicznie kalorii
            if(res.getString(4).equals("m")){
                bmr = (int) (66+(13.7*res.getDouble(1))+(5*res.getInt(2))-(6.76*res.getDouble(3)));
            }
            else if(res.getString(4).equals("k")){
                bmr = (int) (655+(9.6*res.getDouble(1))+(1.8*res.getInt(2))-(4.7*res.getDouble(3)));
            }
            //obliczanie weglowodanow
            weglowodany= (int) (bmr*0.55)/4;
            //oblicznie tluszczy
            tluszcz= (int) (bmr*0.30)/9;
            //obliczanie bialek
            bialko= (int) (bmr*0.15)/4;

        }
        textView2.setText("BMI: " + bmi + "  BMR: " + bmr + "kcal" + "  W: " + weglowodany + "g"+ "  T: " + tluszcz + "g"+"  B: " + bialko + "g");

        btnInfo2=v.findViewById(R.id.info2);
        btnInfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v2 = inflater.inflate(R.layout.info_profile_dialog ,null);
                builder.setView(v2);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });

        return v;
    }

    public void showHints(){
        Cursor res = databaseHelper.getProfileData();
        if (res.getCount()==0){
            Toast.makeText( getActivity(), "Błąd bazy danych", Toast.LENGTH_LONG).show();
        }
        if(res.moveToFirst()){
            editName.setText(res.getString(0));
            editWeight.setText(res.getString(1));
            editHeight.setText(res.getString(2));
            editAge.setText(res.getString(3));
            //Toast.makeText( getActivity(), res.getString(4) , Toast.LENGTH_LONG).show();
            if (res.getString(4).equals("m"))
            {
                radioGroup.check(R.id.radioM);
            }
            else if (res.getString(4).equals("k"))
            {
                radioGroup.check(R.id.radioK);
            }
        }

    }

    /*public void viewAll(){
        buttonSaveProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = databaseHelper.getAllData();
                        if (res.getCount()==0){
                            showMessage("Error", "No data found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext()){
                            buffer.append("Name "+res.getString(0)+"\n");
                            buffer.append("Weight "+res.getString(1)+"\n");
                            buffer.append("Height "+res.getString(2)+"\n");
                            buffer.append("Age "+res.getString(3)+"\n");
                            buffer.append("Gender "+res.getString(4)+"\n");

                        }
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }*/




    public void addData(){
        buttonSaveProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId==R.id.radioM)
                    {
                        gender = "m";
                    }
                    else if (selectedId==R.id.radioK)
                    {
                        gender = "k";
                    }
                    String sName=editName.getText().toString();
                    String sWeight =editWeight.getText().toString();
                    String sHeight =editHeight.getText().toString();
                    String sAge =editAge.getText().toString();
                    if (sName.equals("")|| sWeight.equals("")||sHeight.equals("")||sAge.equals("")) {
                        Toast.makeText( getActivity(), "Nie wprowadzono zmian", Toast.LENGTH_LONG).show();
                    }
                    else{
                        boolean isInserted = databaseHelper.updateDataProfile(sName, sWeight, sHeight, sAge,gender);
                        if (isInserted=true){
                            Toast.makeText( getActivity(), "Wprowadzono zmiany", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText( getActivity(), "Nie wprowadzono zmian", Toast.LENGTH_LONG).show();
                        }
                    }
                        refresh();
                    }

                }
        );
    }







}
