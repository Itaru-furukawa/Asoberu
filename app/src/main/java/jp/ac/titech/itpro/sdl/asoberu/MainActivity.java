package jp.ac.titech.itpro.sdl.asoberu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private String name = "";
    private final static String KEY_NAME = "MainActivity.name";
    private String text = "";
    public int button_selector = 0;
    public View lay;
    DialogFragment dialogFragment = new myDialogFragment();
    DialogFragment dialogFragment2 = new myDialogFragment2();
    // Instantiate the RequestQueue.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            name = savedInstanceState.getString(KEY_NAME);
        }
        TextView test_view = findViewById(R.id.textView);
        /*
        Button okButton = findViewById(R.id.button);

        okButton.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
            int member_number = 4;
            String term = "2021-06-15,2021-06-30";
            String start_time = "17:00,20:00";
            String name[] = {"いたる","かなみ","John","桃太郎"};
            String place[] = {"西八王子駅" , "南行徳駅" , "ブラジル","あふりか"};
            int member_id = 1;

            intent.putExtra("member_number", member_number);
            intent.putExtra("term", term);
            intent.putExtra("schedule_id", 30);
            intent.putExtra("start_time", start_time);
            intent.putExtra("name", name);
            intent.putExtra("place",place);
            intent.putExtra("member_id",member_id);
            //スケジュールを作成の際はTrueをいれる
            intent.putExtra("created_first_time",true);
            startActivity(intent);



        });

         */


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

    public void showDialog(View view) {
        //DialogFragment dialogFragment = new myDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "my_dialog1");
    }
    public void showDialog2(View view) {
        //DialogFragment dialogFragment = new myDialogFragment();
        dialogFragment2.show(getSupportFragmentManager(), "my_dialog1");
    }
    public void sendAPI(int member_number , String term , String password,String start_time){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        String base_url = getResources().getString(R.string.API_URL);
        String url1 =base_url + "schedules";
        String url2 =base_url + "members?scheId=";
        Intent intent = new Intent(context, ScheduleActivity.class);

        // Request a string response from the provided URL.

        StringRequest postSchedule = new StringRequest(Request.Method.POST, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int schedule_id = 0;
                        try {
                            JSONObject res = new JSONObject(response);
                            schedule_id = res.getInt("max(id)") + 1;
                            Log.d("わああああああああああああああああああああ",schedule_id + "");
                            intent.putExtra("schedule_id", schedule_id);
                            intent.putExtra("member_number", member_number);
                            intent.putExtra("term", term);
                            intent.putExtra("start_time", start_time);
                            intent.putExtra("member_id",1);

                            postMembersInfo(intent , schedule_id , member_number , term.split(",",2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context , error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                // パラメータ設定
                Map<String,String> params = new HashMap<String,String>();
                params.put("member_number" , String.valueOf(member_number));
                params.put("term" , term);
                params.put("password" , password);
                params.put("start_time" , start_time);
                return params;
            }
        };


        // Add the request to the RequestQueue.
        queue.add(postSchedule);
    }

    public void postMembersInfo(Intent intent,int schedule_id,int member_number , String term[]) throws JSONException {
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        String base_url = getResources().getString(R.string.API_URL);
        String url1 =base_url + "members?scheId=" + schedule_id;
        String url2 =base_url + "disable?scheId=" + schedule_id;

        //APIに送る情報をJsonに入力する
        JSONObject jsonObject = new JSONObject();
        String place[] = new String[member_number];
        String name[] = new String[member_number];
        //nameの生成・JSONobjectにnameをいれる
        for(int i = 0 ; i < member_number; i++){
           name[i] = "名無しさん" + (i + 1);
           place[i] = "NULL";
           jsonObject.accumulate("member_info" , name[i]);

        }

        //JSONobjectにmember_numberをいれる
        jsonObject.put("member_number" , member_number);
        Log.d(TAG, jsonObject.toString());

        JsonObjectRequest postMembersInfo = new JsonObjectRequest(Request.Method.POST, url1, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonOb) {
                        intent.putExtra("member_number" , member_number);
                        intent.putExtra("name" , name);
                        intent.putExtra("place" , place);
                        startActivity(intent);
                        Toast.makeText(context , "日程調整を作成しました", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        Log.d(TAG,"だめでした");
                    }
                });

        //APIに送る情報をJsonに入力する
        JSONObject disable_time = new JSONObject();
        //nameの生成・JSONobjectにnameをいれる
        for(int i = 0 ; i < 2; i++){
            disable_time.accumulate("term" , term[i]);
        }
        disable_time.put("member_number" , member_number);

        Log.d(TAG, disable_time.toString());

        JsonObjectRequest postDisableInfo = new JsonObjectRequest(Request.Method.POST, url2, disable_time,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonOb) {
                        queue.add(postMembersInfo);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        Log.d(TAG,"だめでした1");
                    }
                });



        queue.add(postDisableInfo);
    }

    public void getAPI(String schedule_id , String password){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        String base_url = getResources().getString(R.string.API_URL);
        String url1 =base_url + "schedules/" + schedule_id + "?q= \"" + password + "\"";
        Log.d(TAG,url1);
        String url2 = base_url + "members?scheId=" + schedule_id;
        Intent intent = new Intent(context, ScheduleActivity.class);

        // Request a string response from the provided URL.

        StringRequest getMembersData = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray datas = new JSONArray(response);
                            String name[] = new String[datas.length()];
                            String place[] = new String[datas.length()];
                            for(int i = 0; i < datas.length(); i++){

                                JSONObject data = datas.getJSONObject(i);
                                final int memid = data.getInt("member_id");
                                name[memid-1] = data.getString("name");
                                place[memid-1] = data.getString("place");
                            }
                            intent.putExtra("name" , name);
                            intent.putExtra("place" , place);
                            startActivity(intent);
                            Toast.makeText(context , "日程調整の参加に成功しました", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            Log.d(TAG,"dameyan");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context , "スケジュールIDまたはパスワードが正しくありません！", Toast.LENGTH_LONG).show();
            }
        });

        StringRequest getSchedule = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject res = new JSONObject(response);
                            int member_number = res.getInt("member_number");
                            String term = res.getString("term");
                            String start_time = res.getString("start_time");
                            Log.d(TAG,schedule_id + "だぜい");
                            intent.putExtra("schedule_id", Integer.parseInt(schedule_id));
                            intent.putExtra("member_number", member_number);
                            intent.putExtra("term", term);
                            intent.putExtra("start_time", start_time);
                            intent.putExtra("member_id",1);
                            queue.add(getMembersData);


                        } catch (JSONException e) {
                            Toast.makeText(context , "しぼう", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context , "スケジュールIDまたはパスワードが正しくありません！", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(getSchedule);
    }

}