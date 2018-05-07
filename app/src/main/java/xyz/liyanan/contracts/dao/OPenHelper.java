package xyz.liyanan.contracts.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OPenHelper extends SQLiteOpenHelper {
    private static final String name = "contract.db";//数据库名为contract
    private static final int version = 1;//数据库版本

    public OPenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "contract(person_id INTEGER primary key autoincrement," +
                "name varchar(32),phoneNum varchar(11))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
