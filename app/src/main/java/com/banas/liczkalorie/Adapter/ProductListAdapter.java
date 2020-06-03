package com.banas.liczkalorie.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.banas.liczkalorie.Database.DatabaseHelper;
import com.banas.liczkalorie.Product;
import com.banas.liczkalorie.R;
import com.banas.liczkalorie.tabMain;
import com.banas.liczkalorie.tabSearch;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class ProductListAdapter extends ArrayAdapter<Product> implements Filterable{
    private int layout;
    private Context prContext;
    private ArrayList<Product> prProductList;
    private List<Product> prProductListFull;
    private DatabaseHelper databaseHelper;
    private List<Product> filteredList;

    public ProductListAdapter(@NonNull Context prContext, int resource, @NonNull ArrayList<Product> prProductList) {
        super(prContext, resource, prProductList);
        this.prContext=prContext;
        this.prProductList = prProductList;
        prProductListFull= new ArrayList<>(prProductList);
        layout=resource;
        databaseHelper=new DatabaseHelper(getContext());

    }

    @Override public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(prProductListFull); //UWAGA MOŻE NIE DZIAŁAĆ
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Product item : prProductListFull) {
                    if (item.getNazwa().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            prProductList.clear();
            prProductList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder=null;

        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView=inflater.inflate(layout, parent, false );
            viewHolder = new ViewHolder();
            viewHolder.prNazwa = (TextView) convertView.findViewById(R.id.pr_nazwa);
            viewHolder.prKcal = (TextView) convertView.findViewById(R.id.pr_kcal);
            viewHolder.prBialko = (TextView) convertView.findViewById(R.id.pr_bialko);
            viewHolder.prTluszcz = (TextView) convertView.findViewById(R.id.pr_tluszcz);
            viewHolder.prWeglowodany = (TextView) convertView.findViewById(R.id.pr_weglowodany);
            viewHolder.remove_button = (Button) convertView.findViewById(R.id.remove_product);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.prNazwa.setText(prProductList.get(position).getNazwa());
        viewHolder.prKcal.setText(valueOf("WO: " + prProductList.get(position).getKcal()+" kcal") );
        viewHolder.prBialko.setText(valueOf("B: " + prProductList.get(position).getBialko()+" g"));
        viewHolder.prTluszcz.setText(valueOf("T: " + prProductList.get(position).getTluszcz()+" g"));
        viewHolder.prWeglowodany.setText(valueOf("W: " + prProductList.get(position).getWeglowodany()+" g"));
        if(prProductList.get(position).isAdded()==0) {
            viewHolder.remove_button.setVisibility(View.INVISIBLE);
        }else if(prProductList.get(position).isAdded()==1){
            viewHolder.remove_button.setVisibility(View.VISIBLE);
        }

        viewHolder.remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDeleted = databaseHelper.removeProduct(prProductList.get(position).getId());
                //Toast.makeText(getContext(), "Cos tam "+prProductList.get(position).isAdded(), Toast.LENGTH_LONG).show();
                if (isDeleted=true){
                    Toast.makeText( getContext(), "Produkt usunięty", Toast.LENGTH_SHORT).show();
                    prProductList.remove(position);
                    prProductListFull.remove(position);
                    filteredList.remove(position);
                    notifyDataSetChanged();
                }
                else{
                    Toast.makeText( getContext(), "Produkt nie został usunięty", Toast.LENGTH_SHORT).show();
                }

                //getTargetFragment().getActivity().getSupportFragmentManager().beginTransaction().detach(getTargetFragment()).attach(getTargetFragment()).commit();
            }
        });
        return convertView;
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {

        return prProductList.size();
    }


    public class ViewHolder{
        TextView prNazwa;
        TextView prKcal;
        TextView prWeglowodany;
        TextView prTluszcz;
        TextView prBialko;
        Button remove_button;
    }


}