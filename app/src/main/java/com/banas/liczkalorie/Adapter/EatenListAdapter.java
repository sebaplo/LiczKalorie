package com.banas.liczkalorie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.banas.liczkalorie.Database.DatabaseHelper;
import com.banas.liczkalorie.Eaten;
import com.banas.liczkalorie.MainActivity;
import com.banas.liczkalorie.R;
import com.google.android.material.tabs.TabItem;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class EatenListAdapter extends ArrayAdapter<Eaten> {
    private int layout;
    private Context eContext;
    private ArrayList<Eaten> EatenList;
    private DatabaseHelper databaseHelper;

    public EatenListAdapter(@NonNull Context eContext, int resource, @NonNull ArrayList<Eaten> EatenList) {
        super(eContext, resource, EatenList);
        this.eContext=eContext;
        this.EatenList = EatenList;
        layout=resource;
        databaseHelper=new DatabaseHelper(getContext());
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView=inflater.inflate(layout, parent, false );
            viewHolder = new ViewHolder();
            viewHolder.eNazwa = (TextView) convertView.findViewById(R.id.e_nazwa);
            viewHolder.eIlosc = (TextView) convertView.findViewById(R.id.e_ilosc);
            viewHolder.eKcal = (TextView) convertView.findViewById(R.id.e_kcal);
            viewHolder.eBialko = (TextView) convertView.findViewById(R.id.e_bialko);
            viewHolder.eTluszcz = (TextView) convertView.findViewById(R.id.e_tluszcz);
            viewHolder.eWeglowodany = (TextView) convertView.findViewById(R.id.e_weglowodany);
            viewHolder.remove_eaten = (Button) convertView.findViewById(R.id.remove_eaten);
            convertView.setTag(viewHolder);

        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
            //mainViewHolder.prNazwa.setText(valueOf(getItem(position)));
        }

        viewHolder.eNazwa.setText(EatenList.get(position).getNazwa());
        viewHolder.eIlosc.setText(valueOf("Ilość[g]: " +EatenList.get(position).getAmount()));
        viewHolder.eKcal.setText(valueOf("WO: " + Math.round(EatenList.get(position).getKcal()*EatenList.get(position).getAmount()/100*100.0)/100.0)+" kcal");
        viewHolder.eBialko.setText(valueOf("B: " + Math.round(EatenList.get(position).getBialko()*EatenList.get(position).getAmount()/100*100.0)/100.0)+" g");
        viewHolder.eTluszcz.setText(valueOf("T: " + Math.round(EatenList.get(position).getTluszcz()*EatenList.get(position).getAmount()/100*100.0)/100.0)+" g");
        viewHolder.eWeglowodany.setText(valueOf("W: " + Math.round(EatenList.get(position).getWeglowodany()*EatenList.get(position).getAmount()/100*100.0)/100.0)+" g");
        viewHolder.remove_eaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDeleted = databaseHelper.removeEaten(EatenList.get(position).getId_eaten());
                if (isDeleted=true){
                    //Toast.makeText( getContext(), "Data deleted" + eContext, Toast.LENGTH_LONG).show();
                    EatenList.remove(position);
                    notifyDataSetChanged();
                }
                else{
                    Toast.makeText( getContext(), "Data not deleted", Toast.LENGTH_LONG).show();
                }
                //((FragmentActivity)eContext).getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }
        });
        return convertView;
    }

    @Nullable
    @Override
    public Eaten getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public class ViewHolder{
        TextView eNazwa;
        TextView eIlosc;
        TextView eKcal;
        TextView eWeglowodany;
        TextView eTluszcz;
        TextView eBialko;
        Button remove_eaten;
    }
}
