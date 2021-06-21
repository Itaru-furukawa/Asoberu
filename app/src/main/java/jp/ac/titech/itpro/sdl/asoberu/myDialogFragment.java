package jp.ac.titech.itpro.sdl.asoberu;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class myDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle saveInstanceState){
        final EditText editText1 = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.create_dialog, null);
        builder.setTitle("日程調整をはじめよう！")
                .setMessage("あいことばを決めてください（なんでもおっけー）")
                .setView(editText1)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogFragment dialogFragment = new myDialogFragment2();
                        dialogFragment.show(getActivity().getSupportFragmentManager(), "my_dialog");
                    }
                })
                .setNegativeButton("キャンセル", null)
                .setView(view);

        return builder.create();
    }
}
