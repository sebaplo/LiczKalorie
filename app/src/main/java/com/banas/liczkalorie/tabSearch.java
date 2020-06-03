package com.banas.liczkalorie;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.banas.liczkalorie.Adapter.ProductListAdapter;
import com.banas.liczkalorie.Database.DatabaseHelper;
import java.util.ArrayList;

public class tabSearch extends Fragment{

    private ListView listViewProducts;
    public ProductListAdapter adapter;
    private ArrayList<Product> prProductList; //był List
    private DatabaseHelper prDatabaseHelper;
    private SearchView searchView;
    private Button addButton, removeButton;
    public int pr_id;
    AddProductDialog addProductDialog;
    AddEatenDialog addEatenDialog;

    public tabSearch() {
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
        View v = inflater.inflate(R.layout.fragment_tab_search, container, false);
        listViewProducts = v.findViewById(R.id.listViewProduct);
        prDatabaseHelper = new DatabaseHelper(getActivity());
        addButton = v.findViewById(R.id.btnAddProduct);
        removeButton = v.findViewById(R.id.remove_product);

        /*File database = v.getContext().getDatabasePath(DatabaseHelper.DBNAME);
        if(false==database.exists()){
            prDatabaseHelper.getReadableDatabase();
            if(copyDatabase(getActivity())){
                Toast.makeText(getActivity(),"Baza danych aktywna", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(),"Błąd danych", Toast.LENGTH_SHORT).show();
            }
        }*/
        prProductList=prDatabaseHelper.getProductList();
        //adapter=new ProductListAdapter(getActivity(), prProductList);
        //listViewProducts.setAdapter(adapter);
        adapter=new ProductListAdapter(getActivity(), R.layout.item_listviewproduct, prProductList);
        searchView= v.findViewById(R.id.searchView);
        listViewProducts.setAdapter(adapter);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*prProductList.clear(); //naprawia usuwanie produktu ale miga całą listą
                adapter.clear();
                listViewProducts.setAdapter(null);
                prProductList=prDatabaseHelper.getProductList();
                adapter=new ProductListAdapter(getActivity(), R.layout.item_listviewproduct, prProductList);
                listViewProducts.setAdapter(adapter);*/
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pr_id=prProductList.get(position).getId();
                //Toast.makeText(getActivity(), "Id:"+pr_id, Toast.LENGTH_LONG).show();
                openDialog2();
                //przekazanie id do dialogu
                /*Intent intent = new Intent(getContext(), AddEatenDialog.class);
                intent.putExtra("pr_id", pr_id);
                getActivity().startActivity(intent);*/
            }
        });

        prDatabaseHelper.close();
        return v;
}


    public void openDialog(){
        addProductDialog = new AddProductDialog();
        addProductDialog.setTargetFragment(this,1);
        addProductDialog.show(getFragmentManager(),"dialog");
    }

    public void openDialog2(){
        Bundle bundle = new Bundle();
        bundle.putInt("pr_id",pr_id);
        addEatenDialog = new AddEatenDialog();
        addEatenDialog.setTargetFragment(this,1);
        addEatenDialog.setArguments(bundle);
        addEatenDialog.show(getFragmentManager(),"dialog2");
    }


    /*private boolean copyDatabase(Context context){
        try{
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBFOLDER + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length=inputStream.read(buff))>0){
                outputStream.write(buff,0,length);
            }
            outputStream.flush();
            outputStream.close();
            Log.v("fragment_tab_search", "Baza skopiowana");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/





}
