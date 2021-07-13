package jp.ac.titech.itpro.sdl.asoberu;//package your.package.name;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import java.util.Calendar;

public class myTimePicker1 extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String h = hourOfDay + "";
        String m = String.format("%02d" , minute);
        String val = h + ":" + m;
        MainActivity callingActivity = (MainActivity) getActivity();
        View dialogLayout = callingActivity.lay;

        switch (callingActivity.button_selector){
            case 3:
                Button b3 = dialogLayout.findViewById(R.id.button6);
                b3.setText(val);
                break;
            case 4:
                Button b4 = dialogLayout.findViewById(R.id.button7);
                b4.setText(val);
                break;
            default:
                break;
        }

    }
}
