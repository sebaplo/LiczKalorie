package com.banas.liczkalorie;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.banas.liczkalorie.Adapter.EatenListAdapter;
import com.banas.liczkalorie.Adapter.ProductListAdapter;
import com.banas.liczkalorie.Database.DatabaseHelper;
import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.String.valueOf;


public class tabMain extends Fragment {

    private ListView listViewEaten;
    private EatenListAdapter adapter;
    private ArrayList<Eaten> EatenList; //był List
    DatabaseHelper databaseHelper;
    public TextView textHello, textKcal, textWeglowodany, textTluszcz,textBialko, textDay;
    Button btnAddDay, btnSubstractDay, btnRemoveEaten, btnRefresh, btnInfo;
    private int e_id, dateFinal;

    private String strDate;
    private Date date;
    private String date2;

    private double weight,height, bmi, eBmr, eWeglowodany, eTluszcz, eBialko;
    private int bmr, weglowodany, tluszcz, bialko;

    ProgressBar prbarKcal, prbarWeglowodany, prbarTluszcz, prbarBialko;

    private Calendar calendar = new GregorianCalendar();

    SimpleDateFormat df,df1,df2;

    public tabMain() {
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_tab_main, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        btnRemoveEaten=v.findViewById(R.id.remove_eaten);

        btnAddDay = v.findViewById(R.id.dayForward);
        btnSubstractDay = v.findViewById(R.id.dayBackward);
        textDay = v.findViewById(R.id.day);
        strDate = DateFormat.getDateInstance().format(calendar.getTime());
        textDay.setText(strDate);
        checkDate();

        btnAddDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH,1);
                strDate = DateFormat.getDateInstance().format(calendar.getTime());
                if(strDate.equals("02.05.2020")){
                    btnSubstractDay.setEnabled(true);
                }
                if(strDate.equals("2020-05-02")){
                    btnSubstractDay.setEnabled(true);
                }
                if(strDate.equals("May 2, 2020")){
                    btnSubstractDay.setEnabled(true);
                }
                textDay.setText(strDate);
                checkDate();
                EatenList.clear();
                adapter.clear();
                listViewEaten.setAdapter(null);
                EatenList=databaseHelper.getEatenList(dateFinal);
                adapter=new EatenListAdapter(getActivity(), R.layout.item_listvieweaten, EatenList);
                listViewEaten.setAdapter(adapter);
                getSumOfEaten();
                setTextView();
            }
        });
        btnSubstractDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH,-1);
                strDate = DateFormat.getDateInstance().format(calendar.getTime());
                if(strDate.equals("01.05.2020")){
                    btnSubstractDay.setEnabled(false);
                }
                if(strDate.equals("2020-05-01")){
                    btnSubstractDay.setEnabled(false);
                }
                if(strDate.equals("May 1, 2020")){
                    btnSubstractDay.setEnabled(false);
                }
                textDay.setText(strDate);
                checkDate();
                EatenList.clear();
                adapter.clear();
                listViewEaten.setAdapter(null);
                EatenList=databaseHelper.getEatenList(dateFinal);
                adapter=new EatenListAdapter(getActivity(), R.layout.item_listvieweaten, EatenList);
                listViewEaten.setAdapter(adapter);
                getSumOfEaten();
                setTextView();
            }
        });

        textHello=(TextView) v.findViewById(R.id.hello);
        textKcal=(TextView) v.findViewById(R.id.textKcal);
        textWeglowodany=(TextView) v.findViewById(R.id.textWeglowodany);
        textTluszcz=(TextView) v.findViewById(R.id.textTluszcz);
        textBialko=(TextView) v.findViewById(R.id.textBialko);
        prbarKcal=(ProgressBar) v.findViewById(R.id.prbarKcal);
        prbarWeglowodany=(ProgressBar) v.findViewById(R.id.prbarWeglowodany);
        prbarTluszcz=(ProgressBar) v.findViewById(R.id.prbarTluszcz);
        prbarBialko=(ProgressBar) v.findViewById(R.id.prbarBialko);


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
            //hello
            textHello.setText(valueOf("Witaj "+res.getString(0)+"!"));
        }

        listViewEaten=v.findViewById(R.id.listViewEaten);
        EatenList=databaseHelper.getEatenList(dateFinal);
        adapter=new EatenListAdapter(getActivity(), R.layout.item_listvieweaten, EatenList);
        listViewEaten.setAdapter(adapter);

        listViewEaten.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                e_id=EatenList.get(position).getId_eaten();
                //Toast.makeText(getActivity(), "Id:"+e_id, Toast.LENGTH_LONG).show();
            }
        });


        listViewEaten.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                getSumOfEaten();
                setTextView();
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                getSumOfEaten();
                setTextView();
                //Toast.makeText(getActivity(), "Id:"+getActivity(), Toast.LENGTH_LONG).show();
            }
        });

        btnRefresh=v.findViewById(R.id.refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        btnInfo=v.findViewById(R.id.info1);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v2 = inflater.inflate(R.layout.info_main_dialog ,null);
                builder.setView(v2);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });


        prbarKcal.setMax(bmr);
        prbarWeglowodany.setMax(weglowodany);
        prbarTluszcz.setMax(tluszcz);
        prbarBialko.setMax(bialko);
        getSumOfEaten();
        setTextView();
        databaseHelper.close();
        return v;
    }

    public void checkDate(){
        df = new SimpleDateFormat("dd.MM.yyyy");
        df1 = new SimpleDateFormat("MMM dd, yyyy");
        df2 = new SimpleDateFormat("yyyy-MM-dd");
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

        Cursor res2 = databaseHelper.checkDate(date2);
        if (res2.getCount()==0){
            Toast.makeText( getActivity(), "Brak danych", Toast.LENGTH_LONG).show();
        }
String a;
        while (!res2.isAfterLast()) {
            res2.moveToNext();
            a=res2.getString(1);
            if (res2.getString(1).equals(date2)) {
                dateFinal = res2.getInt(0);
                break;
            }
        }
    }

    public void getSumOfEaten(){
        eBmr=0.0;
        eWeglowodany=0.0;
        eTluszcz=0.0;
        eBialko=0.0;
        for(int i=0; i<EatenList.size(); i++){
            double amount = EatenList.get(i).getAmount();
            eBmr=eBmr+(EatenList.get(i).getKcal()*amount/100.0);
            eWeglowodany=eWeglowodany+(EatenList.get(i).getWeglowodany()*amount/100.0);
            eTluszcz=eTluszcz+(EatenList.get(i).getTluszcz()*amount/100.0);
            eBialko=eBialko+(EatenList.get(i).getBialko()*amount/100.0);
        }
    }

    public void setTextView(){
        String finalBmi = Double.valueOf(bmi).toString();
        String finalBmr = Integer.valueOf(bmr).toString();
        String finalWeglowodany = Integer.valueOf(weglowodany).toString();
        String finalTluszcz = Integer.valueOf(tluszcz).toString();
        String finalBialko = Integer.valueOf(bialko).toString();
        String actualBmr = Double.valueOf(Math.round(eBmr*100.00)/100.00).toString();
        String actualWeglowodany = Double.valueOf(Math.round(eWeglowodany*100.00)/100.00).toString();
        String actualTluszcz = Double.valueOf(Math.round(eTluszcz*100.00)/100.00).toString();
        String actualBialko = Double.valueOf(Math.round(eBialko*100.00)/100.00).toString();
        //textBMI.setText("BMI: " + finalBmi + "  BMR: " + finalBmr + "kcal" + "  W: " + finalWeglowodany + "g"+ "  T: " + finalTluszcz + "g"+"  B: " + finalBialko + "g");
        if(eBmr>bmr){
            textKcal.setText(valueOf("Kalorie: " + actualBmr+ " / " + finalBmr + "kcal - !!!"));
        }else{
            textKcal.setText(valueOf("Kalorie: " + actualBmr+ " / " + finalBmr + "kcal"));
        }
        if(eWeglowodany>weglowodany){
            textWeglowodany.setText(valueOf("Węglowodany: " + actualWeglowodany+ " / " + finalWeglowodany + "g - !!!"));
        }else{
            textWeglowodany.setText(valueOf("Węglowodany: " + actualWeglowodany+ " / " + finalWeglowodany + "g"));
        }
        if(eTluszcz>tluszcz){
            textTluszcz.setText(valueOf("Tłuszcz: " + actualTluszcz+ " / " + finalTluszcz + "g - !!!"));
        }else{
            textTluszcz.setText(valueOf("Tłuszcz: " + actualTluszcz+ " / " + finalTluszcz + "g"));
        }
        if(eBialko>bialko){
            textBialko.setText(valueOf("Białko: " +actualBialko + " / " + finalBialko + "g - !!!"));
        }else{
            textBialko.setText(valueOf("Białko: " +actualBialko + " / " + finalBialko + "g"));
        }
        prbarKcal.setProgress((int)eBmr);
        prbarWeglowodany.setProgress((int)eWeglowodany);
        prbarTluszcz.setProgress((int)eTluszcz);
        prbarBialko.setProgress((int)eBialko);
    }

}
