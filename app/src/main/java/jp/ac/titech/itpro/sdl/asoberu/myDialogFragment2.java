package jp.ac.titech.itpro.sdl.asoberu;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class myDialogFragment2 extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle saveInstanceState){
        final EditText editText1 = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("タイトル")
                .setMessage("ここにメッセージを入力します")
                .setView(editText1)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("キャンセル", null)
                .setNeutralButton("あとで", null);
        return builder.create();
    }
}
