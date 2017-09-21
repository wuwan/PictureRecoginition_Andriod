package com.example.lenovo.picture;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB extends SQLiteOpenHelper
{
    // 函数*********************************************************************
    // 构造
    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    // 创建
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String strSQL = "create table login("
                + "Username text not null,"
                + "Password text not null,"
                + "Integral text not null,"
                + "condition text not null,"
                + "constraint PK_Studnet primary key(Username)"
                + ")";

        db.execSQL(strSQL);
    }

    // 升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }


}
