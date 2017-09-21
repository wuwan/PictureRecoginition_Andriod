package com.example.lenovo.picture;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistActivity extends AppCompatActivity {

    private EditText tvRegistUsername = null;
    private EditText tvRegistPassword = null;
    private EditText tvRegistPPassword = null;
    private DB db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        db = new DB(this,"picture.db",null,1);
        db.getReadableDatabase();
        tvRegistUsername = (EditText) findViewById(R.id.resetpwd_edit_name);
        tvRegistPassword = (EditText)findViewById(R.id.resetpwd_edit_pwd_old);
        tvRegistPPassword = (EditText)findViewById(R.id.resetpwd_edit_pwd_new);
    }
    public void btnRegistregist_Click(View view)
    {
        String username ="";
        String password="";
        String ppassword="";
        username= tvRegistUsername.getText().toString();
        password = tvRegistPassword.getText().toString();
        ppassword = tvRegistPPassword.getText().toString();
        //判断
        if(username.length()<=0||password.length()<=0||ppassword.length()<=0)
        {
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(username.length()>0)
        {
            String sql="select * from login where Username=?";
            Cursor cursor=db.getWritableDatabase().rawQuery(sql, new String[]{username});
            if(cursor.moveToFirst())
            {
                Toast.makeText(this, "用户名已经存在", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(!password.equals(ppassword))
        {
            Toast.makeText(this, "两次输入的密码不同", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            SQLiteDatabase dbo = db.getWritableDatabase();
            String strSql = "";
            strSql = "insert into login values('"+username+"','"+password+"','0','无任务')";
            dbo.execSQL(strSql);
            Toast.makeText(this, "用户注册成功", Toast.LENGTH_LONG).show();
            Intent intent=new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }
    }
    public void btnRegicalcel_Click(View view)
    {
        Intent intent=new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
