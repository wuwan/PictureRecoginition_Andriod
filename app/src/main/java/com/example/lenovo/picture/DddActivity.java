package com.example.lenovo.picture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_spinner_item;
import static com.android.volley.VolleyLog.setTag;
import static com.example.lenovo.picture.R.styleable.View;
import static java.security.AccessController.getContext;

public class DddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private RequestQueue requestQueue;
    ImageView imageView;
    int i=1;
    JSONArray jArray;
    private Spinner spinner;
    private Spinner spinner1;
    int a;
    String[] arr=null;
    String[] arr1=null;
    int  j=0;
    String account;
    public static final int MSG_CREATE_RESULT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddd);
        requestQueue= Volley.newRequestQueue(this);
        imageView=(ImageView) findViewById(R.id.imageView);
        ImageButton left = (ImageButton) findViewById(R.id.left);
        ImageButton right = (ImageButton) findViewById(R.id.right);
        Button load = (Button) findViewById(R.id.tijiao);
        left.setOnClickListener(onClickListenr);
        right.setOnClickListener(onClickListenr);
        load.setOnClickListener(onClickListenr);
        spinner = (Spinner) this.findViewById(R.id.second);
        spinner1= (Spinner) this.findViewById(R.id.third);
        Intent intent = getIntent();
        String value=intent.getStringExtra("testIntent");
        account=intent.getStringExtra("account");
        String imagenum=i+".jpg";
        loadImageByVolley(imagenum,imageView);
        try
        {
            jArray = new JSONArray(value);
            for (int i = 0; i < jArray.length(); i++)
            {
                JSONObject json_data = jArray.getJSONObject(i);
                int a = json_data.getInt("parentId");
                if (a == -1)
                {
                    j++;
                }
            }
            arr = new String[j+1];
            arr[0] = "请选择";
            for(int ii=0;ii < j+1; ii++)
            {
                JSONObject data = jArray.getJSONObject(ii);
                String sclass = data.getString("name");
                arr[ii + 1] = sclass;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, simple_spinner_item,arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //spinner1.setOnItemSelectedListener(this);
    }
    public void loadImageByVolley(String imgurl,ImageView imageView)
    {
        final LruCache<String,Bitmap> lruCache=new LruCache<String,Bitmap>(20);
        ImageLoader.ImageCache imageCache=new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return lruCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                lruCache.put(s, bitmap);
            }
        };
        ImageLoader imageLoader=new ImageLoader(requestQueue,imageCache);
        ImageLoader.ImageListener listener=ImageLoader.getImageListener(imageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        try {
            imageLoader.get("http://120.24.157.7:8080/ImageIdentification/image/"+imgurl, listener);
        }catch (Exception e){
            e.printStackTrace();
        }
        }
    private View.OnClickListener onClickListenr = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tijiao) {
                if (i < 50) {
                    i++;
                    String imagenum = i + ".jpg";
                    loadImageByVolley(imagenum, imageView);
                    Toast.makeText(DddActivity.this,"保存标签成功！",Toast.LENGTH_LONG).show();
                }
            }
            if (v.getId() == R.id.left) {
                if (i > 1) {
                    i--;
                    String imagenum =i + ".jpg";
                    loadImageByVolley(imagenum, imageView);
                } else {
                    Toast.makeText(DddActivity.this,"前面没有了！",Toast.LENGTH_LONG).show();
                }
            }
            if(v.getId() == R.id.right) {
                if (i < 50) {
                    i++;
                    String imagenum = i + ".jpg";
                    loadImageByVolley(imagenum, imageView);
                } else {
                    Toast.makeText(DddActivity.this,"图片已经没有了！",Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int jj=0;
        a =position;
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                int aa = json.getInt("parentId");
                if (a==aa) {
                    jj++;
                }
            }
            arr1 = new String[jj + 1];
            arr1[0] = "请选择";
            for (int i = 0, j = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                int b = json.getInt("parentId");
                if (a==b) {
                    arr1[j + 1] = json.getString("name");
                    j++;
                } else {
                    continue;
                }
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, simple_spinner_item,arr1);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
