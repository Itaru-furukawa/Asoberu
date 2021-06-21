package jp.ac.titech.itpro.sdl.asoberu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private String name = "";
    private final static String KEY_NAME = "MainActivity.name";
    // Instantiate the RequestQueue.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);



        if (savedInstanceState != null) {
            name = savedInstanceState.getString(KEY_NAME);
        }
        /*
        outputView = findViewById(R.id.output_view);
        inputName = findViewById(R.id.input_name);
        Button okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(v -> {
            name = inputName.getText().toString().trim();
            greet();
        });
        */
        /*
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.0.5:3000/api/v1/users";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        outputView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    outputView.setText("That didn't work!");
                }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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

    private void greet() {
        if (name != null && !name.isEmpty()) {
            //outputView.setText(getString(R.string.output_view_format, name));
        }
    }
    public void showDialog(View view) {
        DialogFragment dialogFragment = new myDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "my_dialog");
    }
}