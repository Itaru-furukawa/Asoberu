package jp.ac.titech.itpro.sdl.asoberu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class myDatePicker extends DialogFragment implements
        DatePickerDialog.OnDateSetListener{
            @Override
            @NonNull
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(getActivity(),
                        this,  year, month, day);
            }
            @Override
            public void onDateSet(android.widget.DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                String y = year + "";
                String m = String.format("%02d" , monthOfYear + 1);
                String d = dayOfMonth + "";
                String val = y + "-" + m + "-" + d;
                MainActivity callingActivity = (MainActivity) getActivity();

                /*
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("my_dialog1");
                ((myDialogFragment) fragment).returnChildValue(y + "-" + m + "-" + d);
                */
                View dialogLayout = callingActivity.lay;

                switch (callingActivity.button_selector){
                    case 1:
                        Button b1 = dialogLayout.findViewById(R.id.button4);
                        b1.setText(val);
                        break;
                    case 2:
                        Button b2 = dialogLayout.findViewById(R.id.button5);
                        b2.setText(val);
                        break;
                    default:
                        break;
                }
            }


    }
