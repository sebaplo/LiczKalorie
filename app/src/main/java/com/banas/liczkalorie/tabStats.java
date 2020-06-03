package com.banas.liczkalorie;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.banas.liczkalorie.Adapter.EatenListAdapter;
import com.banas.liczkalorie.Database.DatabaseHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class tabStats extends Fragment {
    private BarChart barchart,barchart1,barchart2,barchart3;
    private Calendar calendar = new GregorianCalendar();

    private ArrayList<Eaten> EatenList;

    private String strDate;
    private Date date;
    private String date2;
    private int dateFinal;
    private DatabaseHelper databaseHelper;
    private double eBmr, eWeglowodany, eTluszcz, eBialko;
    private int bmr, weglowodany, tluszcz, bialko;


    public tabStats() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_tab_stats, container, false);
        databaseHelper=new DatabaseHelper(getActivity());
        calendar = new GregorianCalendar();
        strDate = DateFormat.getDateInstance().format(calendar.getTime());
        checkDate();
        EatenList=databaseHelper.getEatenList(dateFinal);
        getSumOfEaten();


        /*calendar.add(Calendar.DAY_OF_MONTH,-1);
        strDate = DateFormat.getDateInstance().format(calendar.getTime());
        checkDate();
        EatenList.clear();
        EatenList=databaseHelper.getEatenList(dateFinal);
        getSumOfEaten();*/


        barchart=(BarChart) v.findViewById(R.id.barchart);
        barchart.setDrawBarShadow(false);
        barchart.setDrawValueAboveBar(true);
        barchart.setMaxVisibleValueCount(50);
        barchart.setPinchZoom(false);
        barchart.setDrawGridBackground(true);
        barchart.animateY(800);

        barchart1=(BarChart) v.findViewById(R.id.barchart1);
        barchart1.setDrawBarShadow(false);
        barchart1.setDrawValueAboveBar(true);
        barchart1.setMaxVisibleValueCount(50);
        barchart1.setPinchZoom(false);
        barchart1.setDrawGridBackground(true);
        barchart1.animateY(800);

        barchart2=(BarChart) v.findViewById(R.id.barchart2);
        barchart2.setDrawBarShadow(false);
        barchart2.setDrawValueAboveBar(true);
        barchart2.setMaxVisibleValueCount(50);
        barchart2.setPinchZoom(false);
        barchart2.setDrawGridBackground(true);
        barchart2.animateY(800);

        barchart3=(BarChart) v.findViewById(R.id.barchart3);
        barchart3.setDrawBarShadow(false);
        barchart3.setDrawValueAboveBar(true);
        barchart3.setMaxVisibleValueCount(50);
        barchart3.setPinchZoom(false);
        barchart3.setDrawGridBackground(true);
        barchart3.animateY(800);

        String bBmr = Double.valueOf(eBmr).toString();
        String bWeglowodany = Double.valueOf(eWeglowodany).toString();
        String bTluszcz = Double.valueOf(eTluszcz).toString();
        String bBialko = Double.valueOf(eBialko).toString();

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        ArrayList<BarEntry> barEntries2 = new ArrayList<>();
        ArrayList<BarEntry> barEntries3 = new ArrayList<>();
        barEntries.add(new BarEntry(7, Float.parseFloat(bBmr)));
        barEntries1.add(new BarEntry(7, Float.parseFloat(bWeglowodany)));
        barEntries2.add(new BarEntry(7, Float.parseFloat(bTluszcz)));
        barEntries3.add(new BarEntry(7, Float.parseFloat(bBialko)));
        for(int i=6;i>0;i--){
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            strDate = DateFormat.getDateInstance().format(calendar.getTime());
            checkDate();
            EatenList.clear();
            EatenList=databaseHelper.getEatenList(dateFinal);
            getSumOfEaten();
            bBmr = Double.valueOf(eBmr).toString();
            barEntries.add(new BarEntry(i,Float.parseFloat(bBmr)));
            bWeglowodany = Double.valueOf(eWeglowodany).toString();
            barEntries1.add(new BarEntry(i,Float.parseFloat(bWeglowodany)));
            bTluszcz = Double.valueOf(eTluszcz).toString();
            barEntries2.add(new BarEntry(i,Float.parseFloat(bTluszcz)));
            bBialko = Double.valueOf(eBialko).toString();
            barEntries3.add(new BarEntry(i,Float.parseFloat(bBialko)));
        }

        /*calendar.clear();
        calendar = new GregorianCalendar();
        strDate = DateFormat.getDateInstance().format(calendar.getTime());
        checkDate();


        for(int i=6;i>0;i--){
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            strDate = DateFormat.getDateInstance().format(calendar.getTime());
            checkDate();
            EatenList.clear();
            EatenList=databaseHelper.getEatenList(dateFinal);
            getSumOfEaten();

        }
        calendar.clear();
        calendar = new GregorianCalendar();
        strDate = DateFormat.getDateInstance().format(calendar.getTime());
        checkDate();

        for(int i=6;i>0;i--){
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            strDate = DateFormat.getDateInstance().format(calendar.getTime());
            checkDate();
            EatenList.clear();
            EatenList=databaseHelper.getEatenList(dateFinal);
            getSumOfEaten();

        }
        calendar.clear();
        calendar = new GregorianCalendar();
        strDate = DateFormat.getDateInstance().format(calendar.getTime());
        checkDate();
        ArrayList<BarEntry> barEntries3 = new ArrayList<>();

        for(int i=6;i>0;i--){
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            strDate = DateFormat.getDateInstance().format(calendar.getTime());
            checkDate();
            EatenList.clear();
            EatenList=databaseHelper.getEatenList(dateFinal);
            getSumOfEaten();

        }
*/
        ArrayList<String> labels = new ArrayList<String>();
        calendar.clear();
        calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        strDate = DateFormat.getDateInstance().format(calendar.getTime());
        checkDate();
        labels.add(strDate.substring(0,4));
        for(int i=0;i<7;i++){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            strDate = DateFormat.getDateInstance().format(calendar.getTime());
            checkDate();
            labels.add(strDate.substring(0,5));
        }

        XAxis xaxis = barchart.getXAxis();
        xaxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(1);
        xaxis.setCenterAxisLabels(false);

        XAxis xaxis1 = barchart1.getXAxis();
        xaxis1.setValueFormatter(new IndexAxisValueFormatter(labels));
        xaxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis1.setGranularity(1);
        xaxis1.setCenterAxisLabels(false);

        XAxis xaxis2 = barchart2.getXAxis();
        xaxis2.setValueFormatter(new IndexAxisValueFormatter(labels));
        xaxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis2.setGranularity(1);
        xaxis2.setCenterAxisLabels(false);

        XAxis xaxis3 = barchart3.getXAxis();
        xaxis3.setValueFormatter(new IndexAxisValueFormatter(labels));
        xaxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis3.setGranularity(1);
        xaxis3.setCenterAxisLabels(false);

        BarDataSet barDataSet = new BarDataSet(barEntries,"ilość spożytych kalorii");
        barDataSet.setColors(0xFF4BB6D6);
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);

        BarDataSet barDataSet1 = new BarDataSet(barEntries1,"ilość spożytych węglowodanów");
        barDataSet1.setColors( 	Color.rgb(255, 160, 0));
        BarData data1 = new BarData(barDataSet1);
        data.setBarWidth(0.9f);

        BarDataSet barDataSet2 = new BarDataSet(barEntries2,"ilość spożytych tłuszczy");
        barDataSet2.setColors(Color.rgb(136, 162, 31));
        BarData data2 = new BarData(barDataSet2);
        data.setBarWidth(0.9f);

        BarDataSet barDataSet3 = new BarDataSet(barEntries3,"ilość spożytych białek");
        barDataSet3.setColors(Color.rgb(187, 96, 225));
        BarData data3 = new BarData(barDataSet3);
        data.setBarWidth(0.9f);

        Cursor res = databaseHelper.getProfileData();
        if (res.getCount()==0){
            Toast.makeText( getActivity(), "Brak danych", Toast.LENGTH_LONG).show();
        }
        if(res.moveToFirst()){
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
        String strBmr = Integer.valueOf(bmr).toString()+" kcal";
        String strWeglowodany = Integer.valueOf(weglowodany).toString()+" g";
        String strTluszcz = Integer.valueOf(tluszcz).toString()+" g";
        String strBialko = Integer.valueOf(bialko).toString()+" g";

        LimitLine limitLine = new LimitLine(bmr,strBmr);
        limitLine.setLineWidth(2f);
        limitLine.enableDashedLine(20f, 10f, 0f);
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        limitLine.setTextSize(12f);

        LimitLine limitLine1 = new LimitLine(weglowodany,strWeglowodany);
        limitLine1.setLineWidth(2f);
        limitLine1.enableDashedLine(20f, 10f, 0f);
        limitLine1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        limitLine1.setTextSize(12f);

        LimitLine limitLine2 = new LimitLine(tluszcz,strTluszcz);
        limitLine2.setLineWidth(2f);
        limitLine2.enableDashedLine(20f, 10f, 0f);
        limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        limitLine2.setTextSize(12f);

        LimitLine limitLine3 = new LimitLine(bialko,strBialko);
        limitLine3.setLineWidth(2f);
        limitLine3.enableDashedLine(20f, 10f, 0f);
        limitLine3.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        limitLine3.setTextSize(12f);

        YAxis leftAxis = barchart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(limitLine);

        YAxis leftAxis1 = barchart1.getAxisLeft();
        leftAxis1.removeAllLimitLines();
        leftAxis1.addLimitLine(limitLine1);

        YAxis leftAxis2 = barchart2.getAxisLeft();
        leftAxis2.removeAllLimitLines();
        leftAxis2.addLimitLine(limitLine2);

        YAxis leftAxis3 = barchart3.getAxisLeft();
        leftAxis3.removeAllLimitLines();
        leftAxis3.addLimitLine(limitLine3);

        barchart.setData(data);
        barchart1.setData(data1);
        barchart2.setData(data2);
        barchart3.setData(data3);

        calendar.clear();
        strDate=null;
        databaseHelper.close();
        return v;
    }

    public void checkDate(){
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

        Cursor res2 = databaseHelper.checkDate(date2);
        if (res2.getCount()==0){
            Toast.makeText( getActivity(), "Brak danych", Toast.LENGTH_LONG).show();
        }

        while (!res2.isAfterLast()) {
            res2.moveToNext();
            if (res2.getString(1).equals(date2)) {
                dateFinal = res2.getInt(0);
                break;
            }
        }
    }

    public void getSumOfEaten(){
        eBmr=0;
        eWeglowodany=0;
        eTluszcz=0;
        eBialko=0;
        for(int i=0; i<EatenList.size(); i++){
            double amount = EatenList.get(i).getAmount();
            eBmr=eBmr+(EatenList.get(i).getKcal()*amount/100);
            eWeglowodany=eWeglowodany+(EatenList.get(i).getWeglowodany()*amount/100);
            eTluszcz=eTluszcz+(EatenList.get(i).getTluszcz()*amount/100);
            eBialko=eBialko+(EatenList.get(i).getBialko()*amount/100);
        }
    }


}


