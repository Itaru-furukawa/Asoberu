package jp.ac.titech.itpro.sdl.asoberu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScheduleActivity extends AppCompatActivity {
    private final static String TAG = "test1";
    private final static String TAG2 = "test2";

    //メンバー切り替えに使用
    int member_number = 0;
    int schedule_id = 0;
    int days = 0;
    int member_id = 0;
    boolean created_first_time = true;

    //スケジュールの設定データ
    String term = "";
    String password = "";
    String start_time = "";

    //メニューで切り替える画面
    public View fragment1_lay;
    public View fragment2_lay;

    //(key1)member_id → (key2)startから離れている日数 → (value)不可能な時間
    String all_members_schedules_from[][];
    String all_members_schedules_to[][];

    //key : ユーザーID  初期値は名無しさんがいいかも？
    String name[];
    String place[];

    View lay;
    int button_selector = 0;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //メインアクティビティから引っ張ってくるデータ
        member_number = getIntent().getIntExtra("member_number",0);
        term = getIntent().getStringExtra("term");
        start_time = getIntent().getStringExtra("start_time");
        schedule_id = getIntent().getIntExtra("schedule_id",0) ;
        name = getIntent().getStringArrayExtra("name");
        place =  getIntent().getStringArrayExtra("place");
        member_id = getIntent().getIntExtra("member_id" , 1);

        Log.d("schedule id is" , schedule_id + "");
        Log.d("member id is" , member_id + "");
        Log.d("member's name are" , name + "");
        Log.d("member's place is " ,  place + "");
        Log.d("term is " ,  term + "desi");

        String split_term[] = term.split(",",2);
        final String start = split_term[0];
        final String end = split_term[1];

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date from = new Date();
        Date to = new Date();
        try {
            from = df.parse(start);
            to = df.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        days = (int)(to.getTime() - from.getTime()) / (1000 * 60 * 60 * 24);

        all_members_schedules_from = new String[member_number + 1][days+1];
        all_members_schedules_to = new String[member_number + 1][days+1];
        //テスト用のデータ
        //int m_id ,String member_info,String members_schedules,String disable_date)
        /*
        String s[] = new String[2];
        s[0] =  "ももたろう";
        s[1] = "おにがしま";
        String s2 = "[\"ももたろう\",\"鬼ヶ島 \"]";
        String s3 = "[\"2021-06-15\",\"2021-06-16\"]";
        String disable_date[] = {"2021-06-15","2021-06-16"};
        String disable_time[][] = {{"12:00-13:00"},{"13:00-14:00"}};
         */

        //putMemberAPI(2,s, disable_date , disable_time);
        Log.d(TAG,start_time);

        //ViewPagerの取得
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        //TabLayoutを取得してそれをViewPagerにセット
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        //Adapterを取得してそれをViewPagerにセット
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy in " + Thread.currentThread());
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.schedule_menu, menu);
        for(int i = 0; i < member_number; i++){
            menu.add(0,i + 1 , i,name[i]);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() > 0){
            reloadActivity(item.getItemId());
            //Toast.makeText(getApplicationContext() , "びよーん", Toast.LENGTH_LONG).show();
            finish();

            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }


    public void getAlldatas(){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        String base_url = getResources().getString(R.string.API_URL);
        String url1 = base_url + "members?scheId=" + schedule_id;
        String url2 = base_url + "disable?scheId=" + schedule_id;
        RequestFuture<String> future = RequestFuture.newFuture();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray datas = new JSONArray(response);
                            for(int i = 0; i < datas.length(); i++){

                                JSONObject data = datas.getJSONObject(i);
                                final int memid = data.getInt("member_id");
                                name[memid-1] = data.getString("name");
                                place[memid-1] = data.getString("place");
                            }
                            Log.d(TAG,Arrays.deepToString(name));
                        } catch (JSONException e) {
                            Log.d(TAG,"hogehoge");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest1);

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            String date[] = term.split(",",2);
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date from = new Date();
                            try {
                                from = df.parse(date[0]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            JSONArray datas = new JSONArray(response);
                            for(int i = 0; i < datas.length(); i++){
                                //APIから取得したデータの処理
                                JSONObject data = datas.getJSONObject(i);
                                final int memid = data.getInt("member_id");
                                final String disable_date = data.getString("disable_date");
                                String from_to[] = new String[2];
                                from_to = data.getString("disable_time").split("-",2);


                                Date disable_time_date = new Date();
                                disable_time_date = df.parse(disable_date);

                                final int diff = (int)(disable_time_date.getTime() - from.getTime()) / (1000 * 60 * 60 * 24);

                                all_members_schedules_from[memid-1][diff] = from_to[0];
                                if(from_to.length == 2){
                                    all_members_schedules_to[memid-1][diff] = from_to[1];
                                }else{
                                    all_members_schedules_to[memid-1][diff] = "NULL";
                                }
                            }
                            Log.d(TAG2,Arrays.deepToString(all_members_schedules_from));

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest2);
    }


    public void putMemberAPI(int m_id ,String member_info[],String disable_date[],String disable_time[][]){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        String base_url = getResources().getString(R.string.API_URL);
        String url1 =base_url + "members/" + schedule_id + "?mid=" + m_id;
        String url2 =base_url + "disable/" + schedule_id + "?mid=" + m_id;
        // Request a string response from the provided URL.

        StringRequest putMembersRequest = new StringRequest(Request.Method.PUT, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            reloadActivity(m_id);
                            Toast.makeText(context , "メンバー情報を更新しました", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context , error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                // パラメータ設定

                Map<String,String> params = new HashMap<String,String>();
                params.put("member_name",member_info[0]);
                params.put("member_place",member_info[1]);
                return params;
            }
        };
        Log.d(TAG,Arrays.deepToString(member_info));
        queue.add(putMembersRequest);

        JSONObject jsonObject = new JSONObject();
        for(int i = 0 ; i < disable_date.length; i++){
            try {
                jsonObject.accumulate("disable_date", disable_date[i]);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        try {
            jsonObject.accumulate("disable_time" , "");
        }catch(JSONException e){
            e.printStackTrace();
        }
        for(int i = 0 ; i < disable_time.length; i++){
            JSONArray child = new JSONArray();
            for(int j = 0; j < disable_time[i].length; j++){
                child.put(disable_time[i][j].toString());
            }
            try {
                jsonObject.accumulate("disable_time" , child);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }


        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.PUT, url2, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonOb) {
                        Toast.makeText(context , "日程を更新しました", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        Log.d(TAG2,"だめでした");
                    }
                });

        queue.add(jobReq);
    }


    public void reloadActivity(int member_id){
        Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        String base_url = getResources().getString(R.string.API_URL);
        String url1 = base_url + "members?scheId=" + schedule_id;
        String url2 = base_url + "disable?scheId=" + schedule_id;
        RequestFuture<String> future = RequestFuture.newFuture();

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            String date[] = term.split(",",2);
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date from = new Date();
                            try {
                                from = df.parse(date[0]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            JSONArray datas = new JSONArray(response);
                            for(int i = 0; i < datas.length(); i++){
                                //APIから取得したデータの処理
                                JSONObject data = datas.getJSONObject(i);
                                final int memid = data.getInt("member_id");
                                final String disable_date = data.getString("disable_date");
                                String from_to[] = new String[2];
                                from_to = data.getString("disable_time").split("-",2);


                                Date disable_time_date = new Date();
                                disable_time_date = df.parse(disable_date);

                                final int diff = (int)(disable_time_date.getTime() - from.getTime()) / (1000 * 60 * 60 * 24);

                                all_members_schedules_from[memid-1][diff] = from_to[0];
                                if(from_to.length == 2){
                                    all_members_schedules_to[memid-1][diff] = from_to[1];
                                }else{
                                    all_members_schedules_to[memid-1][diff] = "NULL";
                                }
                            }
                            Log.d(TAG2,Arrays.deepToString(all_members_schedules_from));
                            intent.putExtra("member_number", member_number);
                            intent.putExtra("term", term);
                            intent.putExtra("schedule_id", schedule_id);
                            intent.putExtra("start_time", start_time);
                            intent.putExtra("name", name);
                            intent.putExtra("place",place);
                            intent.putExtra("member_id",member_id);

                            startActivity(intent);
                            finish();

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray datas = new JSONArray(response);
                            for(int i = 0; i < datas.length(); i++){

                                JSONObject data = datas.getJSONObject(i);
                                final int memid = data.getInt("member_id");
                                name[memid-1] = data.getString("name");
                                place[memid-1] = data.getString("place");
                            }
                            queue.add(stringRequest2);
                            Log.d(TAG,Arrays.deepToString(name));
                        } catch (JSONException e) {
                            Log.d(TAG,"hogehoge");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest1);
    }

}
