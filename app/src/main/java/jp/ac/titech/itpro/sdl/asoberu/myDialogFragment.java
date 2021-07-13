package jp.ac.titech.itpro.sdl.asoberu;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class myDialogFragment extends DialogFragment {

    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle saveInstanceState){
        MainActivity callingActivity = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogLayout = LayoutInflater.from(getActivity()).inflate(
                R.layout.create_dialog, null);

        final Button button1 = dialogLayout.findViewById(R.id.button4);
        final Button button2 = dialogLayout.findViewById(R.id.button5);
        final Button button3 = dialogLayout.findViewById(R.id.button6);
        final Button button4 = dialogLayout.findViewById(R.id.button7);
        final EditText m_num = dialogLayout.findViewById(R.id.editTextNumber3);
        final EditText pass = dialogLayout.findViewById(R.id.editTextTextPersonName);
        String button_explain = button1.getText().toString();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            //((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            // Add the request to the RequestQueue.
                            int member_number;
                            String term;
                            String password;
                            String start_time;
                            if(m_num.getText().toString().length() != 0) {
                                member_number = Integer.parseInt(m_num.getText().toString());
                            }else{
                                member_number = 3;
                            }
                            if(button1.getText().toString() != button_explain  && button2.getText().toString() != button_explain ){
                                term = button1.getText().toString() + "," + button2.getText().toString();
                            }else{
                                term = "2021-06-18,2021-06-30";
                            }
                            if(pass.getText().toString().length() != 0){
                                password = pass.getText().toString();
                            }else{
                                password = "ららら";
                            }
                            if(button3.getText().toString() != button_explain && button4.getText().toString() != button_explain ){
                                start_time = button3.getText().toString() + "," + button4.getText().toString();
                            }else{
                                start_time = "17:00,20:00";
                            }



                            callingActivity.sendAPI(member_number , term,password,start_time);
                            //callingActivity.sendAPI(3 , "2021-06-18,2021-06-30","ららら","17:00,20:00");

                            //changeIntent();
                    }
                })
                .setNegativeButton("キャンセル", null)
                .setView(dialogLayout);
        callingActivity.lay = dialogLayout;
        myDatePicker d = new myDatePicker();
        myTimePicker1 t = new myTimePicker1();
        // lambda式
        button1.setOnClickListener( v -> {
            callingActivity.button_selector = 1;
            d.show(getActivity().getSupportFragmentManager(), "my_dialog2");
        });
        button2.setOnClickListener( v -> {
            callingActivity.button_selector = 2;
            d.show(getActivity().getSupportFragmentManager(), "my_dialog3");
        });
        button3.setOnClickListener( v -> {
            callingActivity.button_selector = 3;
            t.show(getActivity().getSupportFragmentManager(), "my_dialog2");
        });
        button4.setOnClickListener( v -> {
            callingActivity.button_selector = 4;
            t.show(getActivity().getSupportFragmentManager(), "my_dialog2");
        });

        return builder.create();
    }

    public void returnChildValue (String v){
        MainActivity callingActivity = (MainActivity) getActivity();
        View dialogLayout = callingActivity.lay;
        Button b1 = dialogLayout.findViewById(R.id.button4);
        b1.setText("wawawa");
    }

    public void changeIntent(){
        Intent intent = new Intent(getContext(), ScheduleActivity.class);
        //intent.putExtra(AnswerActivity.NAME_EXTRA, name);
        startActivity(intent);
    }
}
