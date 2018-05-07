package xyz.liyanan.contracts.dao;

import xyz.liyanan.contracts.bean.Constract;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContractDao {
    private SQLiteDatabase db;

    public ContractDao(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
    }

    public boolean insert(Constract constract) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", constract.getName());
        contentValues.put("phoneNum", constract.getPhoneNum());

        long inserResult = db.insert("contract", null, contentValues);
        if (inserResult == -1) {
            return false;
        }
        return true;
    }

    public boolean delete(Constract constract) {
        int deleteResult = db.delete("contract", "person_id=?", new String[]{constract.getId() + ""});
        if (deleteResult == 0) {
            return false;
        }
        return true;
    }

    public boolean update(Constract constract) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", constract.getName());
        contentValues.put("phoneNum", constract.getPhoneNum());
        int UpdateResult = db.update("contract", contentValues, "person_id=?",
                new String[]{constract.getId() + ""});
        if (UpdateResult == 0) {
            return false;
        }
        return true;
    }

    public Constract queryOne(Constract constract) {
        Cursor cursor = db.query("contract", null, "name=?", new String[]{constract.getName()},
                null, null, null);
        while (cursor.moveToNext()) {
            constract.setId(cursor.getInt(0));
            constract.setPhoneNum(cursor.getString(2));
        }
        return constract;
    }

    public List<Constract> queryAll() {
        List<Constract> constractList = new ArrayList<>();
        Cursor cursor = db.query("contract", null, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            Constract constract = new Constract();
            constract.setId(cursor.getInt(0));
            constract.setName(cursor.getString(1));
            constract.setPhoneNum(cursor.getString(2));
            constractList.add(constract);
        }
        return constractList;
    }
}
