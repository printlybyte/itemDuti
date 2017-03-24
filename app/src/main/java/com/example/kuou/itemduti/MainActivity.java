package com.example.kuou.itemduti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Person> mList=new ArrayList<>();
    public String url = "http://api.meituan.com/mmdb/movie/v2/list/rt/order/coming.json?ci=1&limit=12&token=&__vhost=api.maoyan.com&utm_campaign=AmovieBmovieCD-1&movieBundleVersion=6801&utm_source =小蜜＆utm_medium = Android和utm_term = 6.8.0＆的utm_content = 868030022327462＆净值= 255＆dModel = MI％205 UUID = 0894DE03C76F6045D55977B6D4E32B7F3C6AAB02F9CEA042987B380EC5687C43与纬度= 40.100673＆LNG = 116.378619＆__ skck = 6a375bce8c66a0dc293860dfa83833ef＆__ skts = 1463704714271＆__贼鸥= 7e01cf8dd30a179800a7a93979b430b2＆__ skno = 1a0b4a9b-44ec-42fc-B110-ead68bcc2824＆__ skcy = sXcDKbGi20CGXQPPZvhCU3％2FkzdE％3D";
   // public String url = "http:192.168.155.211/movie/coming.json";

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        client = new OkHttpClient();
        run(url);


    }

    private void run(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                   try {
                        json(response.body().string());
                      //  Log.i("qwe",response.body().string().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void json(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        JSONObject obj2 = obj.getJSONObject("data");
        JSONArray array = obj2.getJSONArray("coming");
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj3 = array.getJSONObject(i);
            Person pp = new Person();
            pp.name = obj3.getString("boxInfo");
            pp.nameimage=obj3.getString("img");
            pp.comingTitle=obj3.getString("comingTitle");
            mList.add(pp);
        }
        MyRecyclerAdapter aa=new MyRecyclerAdapter(this,mList);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(manager);


        recyclerView.addItemDecoration(new SectionDecoration(mList,MainActivity.this, new SectionDecoration.DecorationCallback() {
            //返回标记id (即每一项对应的标志性的字符串)
            @Override
            public String getGroupId(int position) {
                if(mList.get(position).name!=null) {
                    return mList.get(position).name;
                }
                return "-1";
            }

            //获取同组中的第一个内容
            @Override
            public String getGroupFirstLine(int position) {
                if(mList.get(position).name!=null) {
                    return mList.get(position).name;
                }
                return "";
            }
        }));

        recyclerView.setAdapter(aa);
    }

    }

