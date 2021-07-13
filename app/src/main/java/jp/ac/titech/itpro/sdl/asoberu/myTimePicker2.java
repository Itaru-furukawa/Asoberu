package jp.ac.titech.itpro.sdl.asoberu;//package your.package.name;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import java.util.Calendar;

public class myTimePicker2 extends DialogFragment implements
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
            ScheduleActivity callingActivity = (ScheduleActivity) getActivity();
            View layout = callingActivity.lay;
            int button_id = callingActivity.button_selector;
            Log.d("aaa",button_id + "");
            Button b = layout.findViewById(button_id);
            b.setText(val);
    }
}
