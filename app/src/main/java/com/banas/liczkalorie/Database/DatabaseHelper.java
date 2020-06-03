package com.banas.liczkalorie.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.banas.liczkalorie.Eaten;
import com.banas.liczkalorie.Product;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "liczkalorie.db";
    private static final String DBFOLDER = "data/data/com.banas.liczkalorie/databases/";
    private static final int DBVERSION=1;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseHelper(Context context){
        super(context,DBNAME,null,DBVERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.disableWriteAheadLogging(); // dla Android Pie
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void openDatabase(){
        String dbPath = context.getDatabasePath(DBNAME).getPath();
        if(database!=null && database.isOpen()){
            return;
        }
        database=SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public boolean copyDatabase(Context context){
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeDatabase(){
        if(database!=null) {
            database.close();
        }
    }

    public ArrayList<Product> getProductList(){
        Product product = null;
        ArrayList<Product> productList = new ArrayList<>();
        openDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM PRODUCTS_TABLE ORDER BY dodane DESC, id ASC",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            product = new Product(cursor.getInt(0),cursor.getString(1),
                    cursor.getInt(2), cursor.getDouble(3),
                    cursor.getDouble(4),cursor.getDouble(5), cursor.getInt(6));
            productList.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList;
    }

    public ArrayList<Eaten> getEatenList(int id){
        Eaten eaten = null;
        ArrayList<Eaten> eatenList = new ArrayList<>();
        openDatabase();
        Cursor cursor = null;
        try{
            cursor = database.rawQuery("SELECT e.id, e.id_date, e.id_product, e.amount, p.nazwa, p.wartosc_energetyczna,p.weglowodany,p.tluszcz,p.bialko,d.data FROM PRODUCTS_TABLE as p,EATEN_TABLE as e,DATE_TABLE as d WHERE p.id=e.id_product AND d.id=e.id_date AND e.id_date="+id,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                eaten = new Eaten(cursor.getInt(0),cursor.getInt(1),
                        cursor.getInt(2), cursor.getDouble(3),
                        cursor.getString(4),cursor.getDouble(5), cursor.getDouble(6),
                        cursor.getDouble(7),cursor.getDouble(8),cursor.getString(9));
                eatenList.add(eaten);
                cursor.moveToNext();
            }

        }catch (Exception e){

        }
        cursor.close();
        closeDatabase();
        return eatenList;
    }


    public boolean updateDataProfile(String name, String weight, String height, String age, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("weight", weight);
        contentValues.put("height", height);
        contentValues.put("age", age);
        contentValues.put("gender", gender);

        long result = db.update("USER_TABLE", contentValues, null, null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getProfileData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM USER_TABLE",null);
        return res;
    }

    /*public Cursor getEatenData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT e.id, e.id_date, e.id_product, e.amount, " +
                "p.id, p.nazwa, p.wartosc_energetyczna,p.weglowodany,p.tluszcz,p.bialko,d.data " +
                "FROM PRODUCTS_TABLE as p,EATEN_TABLE as e,DATE_TABLE as d WHERE p.id=e.id_product AND d.id=e.id_date AND e.id.date="+id,null);

        return res;
    }*/

    public boolean addProduct(String nazwa, String kcal, String weglowodany, String tluszcz, String bialko, int added) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nazwa", nazwa);
        contentValues.put("wartosc_energetyczna", kcal);
        contentValues.put("weglowodany", weglowodany);
        contentValues.put("tluszcz", tluszcz);
        contentValues.put("bialko", bialko);
        contentValues.put("dodane", added);
        long result = db.insert("PRODUCTS_TABLE",null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addEatenProduct(int id_date, int id_product, String amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_date", id_date);
        contentValues.put("id_product", id_product);
        contentValues.put("amount", amount);
        long result = db.insert("EATEN_TABLE",null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor checkDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM DATE_TABLE" ,null);
        return res;
    }



    public boolean removeProduct(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("PRODUCTS_TABLE", "Id="+id,null);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
        //String querry = "DELETE FROM PRODUCTS_TABLE WHERE id=" + id;
        //db.execSQL(querry);

    }
    public boolean removeEaten(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("EATEN_TABLE", "id="+id,null);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }



}
