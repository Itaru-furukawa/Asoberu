package jp.ac.titech.itpro.sdl.asoberu;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class myDialogFragment2 extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle saveInstanceState){
        MainActivity callingActivity = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogLayout = LayoutInflater.from(getActivity()).inflate(
                R.layout.get_dialog, null);


        final EditText inserted_schedule_id = dialogLayout.findViewById(R.id.editTextNumber3);
        final EditText inserted_password = dialogLayout.findViewById(R.id.editTextTextPersonName);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String schedule_id = inserted_schedule_id.getText().toString();
                String password = inserted_password.getText().toString();
                schedule_id = "35";
                password = "test";
                Log.d("waaaaaaaaa",schedule_id + password + "hoge") ;
                callingActivity.getAPI(schedule_id,password);
            }
        })
                .setNegativeButton("キャンセル", null)
                .setView(dialogLayout);

        return builder.create();
    }
}
