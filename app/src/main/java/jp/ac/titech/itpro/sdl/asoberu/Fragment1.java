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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Fragment1 extends Fragment {

    public Fragment1() {
    }

    public int toDp(float px){
        Context context = getActivity().getApplicationContext();
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String TAG = "In fragment1";
        Context context = getActivity().getApplicationContext();
        ScrollView scrollView = new ScrollView(context);
        ConstraintLayout layout = (ConstraintLayout)inflater.inflate(R.layout.fragment_fragment1, null);
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
        TextView display_schedule_id = layout.findViewById(R.id.textView16);
        display_schedule_id.setText("スケジュールIDは" + schedule_id + "です！");

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
        String day_string = "";

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        //name,placeのviewを取得
        EditText name_input = layout.findViewById(R.id.editTextTextPersonName2);
        EditText place_input = layout.findViewById(R.id.editTextTextPersonName3);
        name_input.setText(name);
        if(Objects.isNull(place)){
            place_input.setText(place);
        }
        Log.d(TAG,place + "");

        //全日数分のTextViewを作成
        TextView textDate[] = new TextView[dayDiff + 1];
        TextView textJoint[] = new TextView[dayDiff + 1];
        Button button_from[] = new Button[dayDiff + 1];
        Button button_to[] = new Button[dayDiff + 1];
        Button button_all[] = new Button[dayDiff + 1];
        int view_id_date[] = new int[dayDiff + 1];
        int view_id_from[] = new int[dayDiff + 1];
        int view_id_joint[] = new int[dayDiff + 1];
        int view_id_to[] = new int[dayDiff + 1];
        int view_id_all[] = new int[dayDiff + 1];
        String disable_date[] = new String[dayDiff + 1];
        String schedules_from[] = callingActivity.all_members_schedules_from[member_id-1];
        String schedules_to[] = callingActivity.all_members_schedules_to[member_id-1];

        //日付のTextViewを作成
        view_id_date[0] =  View.generateViewId();
        textDate[0] = new TextView(context);
        textDate[0].setId(view_id_date[0]);
        textDate[0].setText(term[0]);
        layout.addView(textDate[0]);
        //相対位置
        constraintSet.connect(view_id_date[0], ConstraintSet.TOP, R.id.textView14, ConstraintSet.BOTTOM, toDp(70));
        constraintSet.connect(view_id_date[0], ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 50);
        //heightとWidthの設定
        constraintSet.constrainHeight(view_id_date[0] , ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(view_id_date[0] , ConstraintSet.WRAP_CONTENT);

        //時刻の入力ボタンを作成（から）
        view_id_from[0] = View.generateViewId();
        button_from[0] = new Button(context);
        button_from[0].setId(view_id_from[0]);
        layout.addView(button_from[0]);
        //相対位置
        constraintSet.connect(view_id_from[0], ConstraintSet.TOP, view_id_date[0], ConstraintSet.TOP, toDp(0));
        constraintSet.connect(view_id_from[0], ConstraintSet.BOTTOM, view_id_date[0], ConstraintSet.BOTTOM, 0);
        constraintSet.connect(view_id_from[0], ConstraintSet.LEFT, view_id_date[0], ConstraintSet.RIGHT, toDp(20));
        //heightとWidthの設定
        constraintSet.constrainHeight(view_id_from[0] , ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(view_id_from[0] , ConstraintSet.WRAP_CONTENT);

        //からを入力
        view_id_joint[0] = View.generateViewId();
        textJoint[0] = new TextView(context);
        textJoint[0].setId(view_id_joint[0]);
        textJoint[0].setText("から");
        layout.addView(textJoint[0]);
        //相対位置
        constraintSet.connect(view_id_joint[0], ConstraintSet.TOP, view_id_from[0], ConstraintSet.TOP, toDp(0));
        constraintSet.connect(view_id_joint[0], ConstraintSet.BOTTOM, view_id_from[0], ConstraintSet.BOTTOM, 0);
        constraintSet.connect(view_id_joint[0], ConstraintSet.LEFT, view_id_from[0], ConstraintSet.RIGHT, toDp(0));
        //heightとWidthの設定
        constraintSet.constrainHeight(view_id_joint[0] , ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(view_id_joint[0] , ConstraintSet.WRAP_CONTENT);


        //時刻の入力ボタンを作成（まで）
        view_id_to[0] = View.generateViewId();
        button_to[0] = new Button(context);
        button_to[0].setId(view_id_to[0]);
        layout.addView(button_to[0]);
        //相対位置
        constraintSet.connect(view_id_to[0], ConstraintSet.TOP, view_id_joint[0], ConstraintSet.TOP, toDp(0));
        constraintSet.connect(view_id_to[0], ConstraintSet.BOTTOM, view_id_joint[0], ConstraintSet.BOTTOM, 0);
        constraintSet.connect(view_id_to[0], ConstraintSet.LEFT, view_id_joint[0], ConstraintSet.RIGHT, toDp(0));
        //heightとWidthの設定
        constraintSet.constrainHeight(view_id_to[0] , ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(view_id_to[0] , ConstraintSet.WRAP_CONTENT);

        //全日ボタンを入力
        view_id_all[0] = View.generateViewId();
        button_all[0] = new Button(context);
        button_all[0].setId(view_id_all[0]);
        button_all[0].setText("全日");
        layout.addView(button_all[0]);
        //相対位置
        constraintSet.connect(view_id_all[0], ConstraintSet.TOP, view_id_to[0], ConstraintSet.TOP, toDp(0));
        constraintSet.connect(view_id_all[0], ConstraintSet.BOTTOM, view_id_to[0], ConstraintSet.BOTTOM, 0);
        constraintSet.connect(view_id_all[0], ConstraintSet.LEFT, view_id_to[0], ConstraintSet.RIGHT, toDp(20));
        constraintSet.connect(view_id_all[0], ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, toDp(20));
        //heightとWidthの設定
        constraintSet.constrainHeight(view_id_all[0] , ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(view_id_all[0] , ConstraintSet.WRAP_CONTENT);

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
            layout.addView(textDate[i]);
            //相対位置
            constraintSet.connect(view_id_date[i], ConstraintSet.TOP, textDate[i-1].getId(), ConstraintSet.BOTTOM, toDp(150));
            constraintSet.connect(view_id_date[i], ConstraintSet.LEFT, textDate[i-1].getId(), ConstraintSet.LEFT, 0);
            //heightとWidthの設定
            constraintSet.constrainHeight(view_id_date[i] , ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(view_id_date[i] , ConstraintSet.WRAP_CONTENT);

            //時刻の入力ボタンを作成（から）
            view_id_from[i] = View.generateViewId();
            button_from[i] = new Button(context);
            button_from[i].setId(view_id_from[i]);
            layout.addView(button_from[i]);
            //相対位置
            constraintSet.connect(view_id_from[i], ConstraintSet.TOP, view_id_date[i], ConstraintSet.TOP, toDp(0));
            constraintSet.connect(view_id_from[i], ConstraintSet.BOTTOM, view_id_date[i], ConstraintSet.BOTTOM, 0);
            constraintSet.connect(view_id_from[i], ConstraintSet.LEFT, view_id_date[i], ConstraintSet.RIGHT, toDp(20));
            //heightとWidthの設定
            constraintSet.constrainHeight(view_id_from[i] , ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(view_id_from[i] , ConstraintSet.WRAP_CONTENT);

            //からを入力
            view_id_joint[i] = View.generateViewId();
            textJoint[i] = new TextView(context);
            textJoint[i].setId(view_id_joint[i]);
            textJoint[i].setText("から");
            layout.addView(textJoint[i]);
            //相対位置
            constraintSet.connect(view_id_joint[i], ConstraintSet.TOP, view_id_from[i], ConstraintSet.TOP, toDp(0));
            constraintSet.connect(view_id_joint[i], ConstraintSet.BOTTOM, view_id_from[i], ConstraintSet.BOTTOM, 0);
            constraintSet.connect(view_id_joint[i], ConstraintSet.LEFT, view_id_from[i], ConstraintSet.RIGHT, toDp(0));
            //heightとWidthの設定
            constraintSet.constrainHeight(view_id_joint[i] , ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(view_id_joint[i] , ConstraintSet.WRAP_CONTENT);


            //時刻の入力ボタンを作成（まで）
            view_id_to[i] = View.generateViewId();
            button_to[i] = new Button(context);
            button_to[i].setId(view_id_to[i]);
            layout.addView(button_to[i]);
            //相対位置
            constraintSet.connect(view_id_to[i], ConstraintSet.TOP, view_id_joint[i], ConstraintSet.TOP, toDp(0));
            constraintSet.connect(view_id_to[i], ConstraintSet.BOTTOM, view_id_joint[i], ConstraintSet.BOTTOM, 0);
            constraintSet.connect(view_id_to[i], ConstraintSet.LEFT, view_id_joint[i], ConstraintSet.RIGHT, toDp(0));
            //heightとWidthの設定
            constraintSet.constrainHeight(view_id_to[i] , ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(view_id_to[i] , ConstraintSet.WRAP_CONTENT);

            //全日ボタンを入力
            view_id_all[i] = View.generateViewId();
            button_all[i] = new Button(context);
            button_all[i].setId(view_id_all[i]);
            button_all[i].setText("全日");
            layout.addView(button_all[i]);
            //相対位置
            constraintSet.connect(view_id_all[i], ConstraintSet.TOP, view_id_to[i], ConstraintSet.TOP, toDp(0));
            constraintSet.connect(view_id_all[i], ConstraintSet.BOTTOM, view_id_to[i], ConstraintSet.BOTTOM, 0);
            constraintSet.connect(view_id_all[i], ConstraintSet.LEFT, view_id_to[i], ConstraintSet.RIGHT, toDp(20));
            constraintSet.connect(view_id_all[i], ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, toDp(20));
            //heightとWidthの設定
            constraintSet.constrainHeight(view_id_all[i] , ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(view_id_all[i] , ConstraintSet.WRAP_CONTENT);
        }


        //fontサイズやフォントの設定・クリックイベントの設定
        myTimePicker2 d = new myTimePicker2();

        for(int i = 0; i <= dayDiff; i++){
            final int from_id , to_id , all_id;
            final int final_i = i;
            from_id = button_from[i].getId();
            to_id = button_to[i].getId();
            all_id = button_all[i].getId();
                    textDate[i].setTypeface(custom_font,Typeface.BOLD);
            textDate[i].setTextSize(16);
            textJoint[i].setTypeface(custom_font);
            button_from[i].setTypeface(custom_font);
            button_from[i].setOnClickListener(v -> {
                callingActivity.button_selector = from_id;
                d.show(getActivity().getSupportFragmentManager(), "oepn datePicker");
            });

            button_to[i].setTypeface(custom_font);
            button_to[i].setOnClickListener(v -> {
                callingActivity.button_selector = to_id;
                d.show(getActivity().getSupportFragmentManager(), "oepn datePicker");
            });

            button_all[i].setTypeface(custom_font);
            button_all[i].setOnClickListener(v -> {
                callingActivity.button_selector = from_id;
                button_from[final_i].setText(start);
                button_to[final_i].setText(end);
            });
        }
        //入力完了ボタンの作成
        //全日ボタンを入力
        int view_id_check = View.generateViewId();
        Button complete_button = new Button(context);
        complete_button.setId(view_id_check);
        complete_button.setText("入力完了");
        complete_button.setTextColor(Color.rgb(255,255,255));
        complete_button.setBackgroundColor(Color.rgb(239, 65, 84));
        complete_button.setOnClickListener(v->{
            //APIに送るデータの準備
            String member_info[] = new String[2];
            String disable_time[][] = new String[dayDiff + 1][1];
            member_info[0] = name_input.getText().toString();
            member_info[1] = place_input.getText().toString();
            for(int i = 0; i <= dayDiff; i++){
                String f = button_from[i].getText().toString();
                String t = button_to[i].getText().toString();
                if(f == "Tap!" || t == "Tap!"){
                    disable_time[i][0] = "NULL";
                }else{
                    disable_time[i][0] = f + "-" + t;
                }
            }
            callingActivity.putMemberAPI(member_id , member_info , disable_date , disable_time);
            Log.d(TAG, Arrays.deepToString(disable_date));
            Log.d(TAG, Arrays.deepToString(disable_time));
        });
        layout.addView(complete_button);

        //相対位置
        constraintSet.connect(view_id_check, ConstraintSet.TOP, view_id_date[dayDiff], ConstraintSet.BOTTOM, toDp(80));
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


        //生成時にdateをとってくる処理を行う

        RequestQueue queue = Volley.newRequestQueue(context);
        String base_url = getResources().getString(R.string.API_URL);
        String url = base_url + "disable?scheId=" + schedule_id;

        String[] finalTerm = term;
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
                            for(int i = 0; i <= dayDiff; i++){
                                if(all_members_schedules_from[member_id-1][i].equals("NULL")){
                                    button_from[i].setText("Tap!");
                                }else{
                                    button_from[i].setText(all_members_schedules_from[member_id-1][i]);
                                }
                                if(all_members_schedules_to[member_id-1][i].equals("NULL")){
                                    button_to[i].setText("Tap!");
                                }else{
                                    button_to[i].setText(all_members_schedules_to[member_id-1][i]);
                                }

                            }
                            Log.d(TAG,Arrays.deepToString(all_members_schedules_from[member_id - 1]));

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



        callingActivity.lay = scrollView;
        return scrollView;
    }


}