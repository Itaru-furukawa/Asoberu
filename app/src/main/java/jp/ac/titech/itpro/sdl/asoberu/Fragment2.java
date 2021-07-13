package jp.ac.titech.itpro.sdl.asoberu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Fragment2 extends Fragment {

    public Fragment2() {
    }

    public int toDp(float px){
        Context context = getActivity().getApplicationContext();
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String TAG = "waaaaaaaaaaaa";
        Context context = getActivity().getApplicationContext();
        ScrollView scrollView = new ScrollView(context);
        ConstraintLayout layout = (ConstraintLayout)inflater.inflate(R.layout.fragment_fragment2, null);
        ScheduleActivity callingActivity = (ScheduleActivity) getActivity();
        Typeface custom_font =  ResourcesCompat.getFont(context, R.font.apt);

        //scheduleのデータをアクティビティからとってくる
        int member_number = callingActivity.member_number;
        String password = callingActivity.password;
        int member_id = callingActivity.member_id;
        int schedule_id = callingActivity.schedule_id;
        String name = callingActivity.name[member_id-1];
        String place = callingActivity.place[member_id-1];
        String all_members_schedules_from[][];
        String all_members_schedules_to[][];

        String[] term = new String[2];
        String[] start_time = new String[2];
        term = callingActivity.term.split(",",2);
        start_time = callingActivity.start_time.split(",",2);
        final String start = start_time[0];
        final String end = start_time[1];

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date from = new Date();
        Date to = new Date();
        try {
            from = df.parse(term[0]);
            to = df.parse(term[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayDiff = (int)(to.getTime() - from.getTime()) / (1000 * 60 * 60 * 24);

        all_members_schedules_from = new String[member_number][dayDiff + 1];
        all_members_schedules_to = new String[member_number][dayDiff + 1];
        Calendar day = Calendar.getInstance();
        day.setTime(from);

        //start_hour_minute[0] は Hour , start_hour_minute[1] は minute
        String start_hour_minute[] = start.split(":");
        //end_hour_minute[0] は Hour , end_hour_minute[1] は minute
        String end_hour_minute[] = end.split(":");

        int base_start_time_minutes = Integer.parseInt(start_hour_minute[0]) * 60 + Integer.parseInt(start_hour_minute[1]);
        int base_end_time_minutes = Integer.parseInt(end_hour_minute[0]) * 60 + Integer.parseInt(end_hour_minute[1]);
        int timeDiff = base_end_time_minutes - base_start_time_minutes;
        String day_string = "";
        Boolean time_table[] = new Boolean[timeDiff + 1];

        Log.d(TAG,time_table[0] + "");

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);


        //全日数分のTextViewを作成
        TextView textDate[] = new TextView[dayDiff + 1];
        TextView textJoint[] = new TextView[dayDiff + 1];
        int view_id_date[] = new int[dayDiff + 1];
        int view_id_joint[] = new int[dayDiff + 1];
        String disable_date[] = new String[dayDiff + 1];
        Log.d(TAG,start + end);

        //日付のTextViewを作成
        view_id_date[0] =  View.generateViewId();
        textDate[0] = new TextView(context);
        textDate[0].setId(view_id_date[0]);
        textDate[0].setText(term[0]);
        textDate[0].setTextSize(18);
        layout.addView(textDate[0]);
        //相対位置
        constraintSet.connect(view_id_date[0], ConstraintSet.TOP, R.id.textView14, ConstraintSet.BOTTOM, toDp(70));
        constraintSet.connect(view_id_date[0], ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 50);
        //heightとWidthの設定
        constraintSet.constrainHeight(view_id_date[0] , ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(view_id_date[0] , ConstraintSet.WRAP_CONTENT);

        //からを入力
        view_id_joint[0] = View.generateViewId();
        textJoint[0] = new TextView(context);
        textJoint[0].setId(view_id_joint[0]);
        textJoint[0].setTextSize(18);
        layout.addView(textJoint[0]);
        //相対位置
        constraintSet.connect(view_id_joint[0], ConstraintSet.TOP, view_id_date[0], ConstraintSet.TOP, toDp(0));
        //constraintSet.connect(view_id_joint[0], ConstraintSet.BOTTOM, view_id_date[0], ConstraintSet.BOTTOM, 0);
        constraintSet.connect(view_id_joint[0], ConstraintSet.LEFT, view_id_date[0], ConstraintSet.RIGHT, 150);
        //heightとWidthの設定
        constraintSet.constrainHeight(view_id_joint[0] , ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(view_id_joint[0] , ConstraintSet.WRAP_CONTENT);
        /*
        //全日ボタンを入力
        view_id_all[0] = View.generateViewId();
        button_all[0] = new Button(context);
        button_all[0].setId(view_id_all[0]);
        button_all[0].setText("全日");
        layout.addView(button_all[0]);
        //相対位置
        constraintSet.connect(view_id_all[0], ConstraintSet.TOP, view_id_joint[0], ConstraintSet.TOP, toDp(0));
        constraintSet.connect(view_id_all[0], ConstraintSet.BOTTOM, view_id_joint[0], ConstraintSet.BOTTOM, 0);
        constraintSet.connect(view_id_all[0], ConstraintSet.LEFT, view_id_joint[0], ConstraintSet.RIGHT, toDp(20));
        constraintSet.connect(view_id_all[0], ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, toDp(20));
        //heightとWidthの設定
        constraintSet.constrainHeight(view_id_all[0] , ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(view_id_all[0] , ConstraintSet.WRAP_CONTENT);

         */

        disable_date[0] = df.format(day.getTime()) + "";
        for (int i = 1 ; i <= dayDiff; i++){
            day.add(Calendar.DAY_OF_MONTH,1);
            day_string = df.format(day.getTime()) + "";
            disable_date[i] = day_string;
            //日付のTextViewを作成
            view_id_date[i] =  View.generateViewId();
            textDate[i] = new TextView(context);
            textDate[i].setId(view_id_date[i]);
            textDate[i].setText(day_string);
            textDate[i].setTextSize(18);
            layout.addView(textDate[i]);
            //相対位置
            constraintSet.connect(view_id_date[i], ConstraintSet.TOP, textJoint[i-1].getId(), ConstraintSet.BOTTOM, toDp(150));
            constraintSet.connect(view_id_date[i], ConstraintSet.LEFT, textDate[i-1].getId(), ConstraintSet.LEFT, 0);
            //heightとWidthの設定Joints
            constraintSet.constrainHeight(view_id_date[i] , ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(view_id_date[i] , ConstraintSet.WRAP_CONTENT);
            //からを入力
            view_id_joint[i] = View.generateViewId();
            textJoint[i] = new TextView(context);
            textJoint[i].setId(view_id_joint[i]);
            textJoint[i].setTextSize(18);
            layout.addView(textJoint[i]);
            //相対位置
            constraintSet.connect(view_id_joint[i], ConstraintSet.TOP, view_id_date[i], ConstraintSet.TOP, toDp(0));
            //constraintSet.connect(view_id_joint[i], ConstraintSet.BOTTOM, view_id_date[i], ConstraintSet.BOTTOM, 0);
            constraintSet.connect(view_id_joint[i], ConstraintSet.LEFT, view_id_date[i], ConstraintSet.RIGHT, 150);
            //heightとWidthの設定
            constraintSet.constrainHeight(view_id_joint[i] , ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(view_id_joint[i] , ConstraintSet.WRAP_CONTENT);


            /*
            //全日ボタンを入力
            view_id_all[i] = View.generateViewId();
            button_all[i] = new Button(context);
            button_all[i].setId(view_id_all[i]);
            button_all[i].setText("全日");
            layout.addView(button_all[i]);
            //相対位置
            constraintSet.connect(view_id_all[i], ConstraintSet.TOP, view_id_joint[i], ConstraintSet.TOP, toDp(0));
            constraintSet.connect(view_id_all[i], ConstraintSet.BOTTOM, view_id_joint[i], ConstraintSet.BOTTOM, 0);
            constraintSet.connect(view_id_all[i], ConstraintSet.LEFT, view_id_joint[i], ConstraintSet.RIGHT, toDp(20));
            constraintSet.connect(view_id_all[i], ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, toDp(20));
            //heightとWidthの設定
            constraintSet.constrainHeight(view_id_all[i] , ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(view_id_all[i] , ConstraintSet.WRAP_CONTENT);

             */
        }


        //入力完了ボタンの作成
        //全日ボタンを入力
        int view_id_check = View.generateViewId();
        Button complete_button = new Button(context);
        complete_button.setId(view_id_check);
        complete_button.setText("更新");
        complete_button.setTextColor(Color.rgb(255,255,255));
        complete_button.setBackgroundColor(Color.rgb(239, 65, 84));
        String[] finalTerm1 = term;
        complete_button.setOnClickListener(v->{
            //生成時にdateをとってくる処理を行う

            RequestQueue queue = Volley.newRequestQueue(context);
            String base_url = getResources().getString(R.string.API_URL);
            String url = base_url + "disable?scheId=" + schedule_id;

            String[] finalTerm = finalTerm1;
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                String date[] = finalTerm;
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
                                    //SchedulActivityにデータを渡す
                                    callingActivity.all_members_schedules_to = all_members_schedules_to;
                                    callingActivity.all_members_schedules_from = all_members_schedules_from;

                                }
                                Log.d(TAG,Arrays.deepToString(all_members_schedules_from));
                                Log.d(TAG,Arrays.deepToString(all_members_schedules_to));
                                for(int i = 0; i <= dayDiff; i++) {
                                    Arrays.fill(time_table , true);
                                    //jはメンバーの数
                                    for (int j = 0; j < all_members_schedules_from.length; j++) {
                                        //入力を判定
                                        if(all_members_schedules_from[j][i] != "NULL" && all_members_schedules_to[j][i] != "NULL"){
                                            String point_start[] = all_members_schedules_from[j][i].split(":");
                                            String point_end[] = all_members_schedules_to[j][i].split(":");
                                            int minutes_of_start = Integer.parseInt(point_start[0]) * 60 + Integer.parseInt(point_start[1]);
                                            int minutes_of_end = Integer.parseInt(point_end[0]) * 60 + Integer.parseInt(point_end[1]);

                                            if(minutes_of_start <= base_start_time_minutes){
                                                minutes_of_start = 0;
                                            } else if(minutes_of_start >= base_end_time_minutes){
                                                minutes_of_start = 0;
                                                minutes_of_end = 0;
                                            }
                                            else{
                                                minutes_of_start -= base_start_time_minutes;
                                            }

                                            if(minutes_of_end <= base_start_time_minutes){
                                                minutes_of_start = 0;
                                                minutes_of_end = 0;
                                            }else if(minutes_of_end >= base_end_time_minutes){
                                                minutes_of_end = timeDiff;
                                            }else{
                                                minutes_of_end -= base_start_time_minutes;
                                            }


                                            Arrays.fill(time_table,minutes_of_start,minutes_of_end + 1,false);
                                        }
                                    }
                                    String result = new String();
                                    int s_t = 0;
                                    boolean t_flag = true;
                                    boolean f_flag = false;
                                    int ok_minutes = 0;
                                    //Log.d(TAG,Arrays.deepToString(time_table));
                                    for(int k = 0; k <= timeDiff; k++){
                                        //Log.d(TAG ,  k + " : " + time_table[k]);
                                        if (time_table[k]){
                                            if(t_flag){
                                                s_t = k;
                                                t_flag = false;
                                            }
                                            f_flag = true;
                                            ok_minutes += 1;
                                        }else{
                                            if(f_flag){
                                                result += String.format("%02d" ,((base_start_time_minutes + s_t) / 60)) + ":"
                                                        +  String.format("%02d" ,((base_start_time_minutes + s_t) % 60))  + " 〜 "
                                                        +  String.format("%02d" ,((base_start_time_minutes + s_t + ok_minutes ) / 60)) + ":"
                                                        +  String.format("%02d" ,((base_start_time_minutes + s_t + ok_minutes) % 60))
                                                        + "  OK \n";
                                                ok_minutes = 0;
                                                t_flag = true;
                                                f_flag = false;
                                            }
                                        }
                                    }
                                    if(f_flag){
                                        result += String.format("%02d" ,((base_start_time_minutes + s_t) / 60)) + ":"
                                                +  String.format("%02d" ,((base_start_time_minutes + s_t) % 60))  + " 〜 "
                                                +  String.format("%02d" ,((base_start_time_minutes + s_t + ok_minutes - 1) / 60)) + ":"
                                                +  String.format("%02d" ,((base_start_time_minutes + s_t + ok_minutes - 1) % 60))
                                                + "  OK \n";
                                    }else if(result.equals("") || Objects.isNull(result)){
                                        result = "遊ぶ時間はありません！";
                                    }
                                    textJoint[i].setText(result);
                                    Log.d(TAG,result);
                                }
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


        });


        layout.addView(complete_button);

        //相対位置
        constraintSet.connect(view_id_check, ConstraintSet.TOP, view_id_joint[dayDiff], ConstraintSet.BOTTOM, toDp(80));
        constraintSet.connect(view_id_check, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, toDp(80));
        constraintSet.connect(view_id_check, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintSet.connect(view_id_check, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,0);
        //heightとWidthの設定
        constraintSet.constrainHeight(view_id_check , ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(view_id_check , toDp(500));

        constraintSet.applyTo(layout);
        int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
        scrollView.setLayoutParams(new ScrollView.LayoutParams(matchParent, wrapContent));
        scrollView.addView(layout);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        return scrollView;
    }

}