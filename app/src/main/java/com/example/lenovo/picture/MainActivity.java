package com.example.lenovo.picture;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;


public class MainActivity extends AppCompatActivity {

    private DB db = null;
    private EditText tvMainUsername = null;
    private EditText tvMainpassword = null;
    String responseMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DB(this,"picture.db",null,1);
        db.getReadableDatabase();
        tvMainUsername = (EditText) findViewById(R.id.login_edit_account);
        tvMainpassword = (EditText)findViewById(R.id.login_edit_pwd);
        Button login = (Button)this.findViewById(R.id.login_btn_login);
        login.setOnClickListener(onClickListenr);
    }

    private View.OnClickListener onClickListenr = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(v.getId()==R.id.login_btn_login)
            {
                Thread loginThread = new Thread(new LoginThread());
                loginThread.start();
            }
        }
    };
    class LoginThread implements Runnable
    {
        public void run()
        {
            loginServer();
        }
    }
    public void loginServer()
    {
        String username = "";
        String password = "";
        username = tvMainUsername.getText().toString();
        password = tvMainpassword.getText().toString();
        if (username.length() <= 0 || password.length() <= 0) {
           // Toast.makeText(MainActivity.this,"标签成功！",Toast.LENGTH_LONG).show();
            return;
        } else {
            String sql = "select * from login where Username=?";
            Cursor cursor = db.getWritableDatabase().rawQuery(sql, new String[]{username});
            if (cursor.moveToFirst()) {
                if (password.equals(cursor.getString(cursor.getColumnIndex("Password")))) {
                 //   Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    String urlStr = "http://www.touyoushuo.cn:8080/ImageIdentification/servlet/QueryClassifyServlet?";
                    HttpPost request = new HttpPost(urlStr);
                    try {
                        HttpClient client = getHttpClient();
                        HttpResponse response = client.execute(request);
                        if (response.getStatusLine().getStatusCode() == 200) {
                            responseMsg = EntityUtils.toString(response.getEntity());
                            Intent intent = new Intent(this, DddActivity.class);
                            intent.putExtra("testIntent", responseMsg);
                            intent.putExtra("account",username);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                  //  Toast.makeText(this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public HttpClient getHttpClient() {
        BasicHttpParams httpParams = new BasicHttpParams();
        return new DefaultHttpClient(httpParams);
    }
    public void btnMainRegist_Click(View view)
    {
        Intent intent = new Intent(this,RegistActivity.class);
        startActivity(intent);
    }
}
