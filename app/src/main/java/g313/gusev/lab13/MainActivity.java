package g313.gusev.lab13;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String link = "https://suchimauzic.pythonanywhere.com/";
    String trigonAngel = "radian";
    EditText editFirst, editSecond;
    TextView textResult;
    RadioGroup rg;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get activity's elements
        editFirst = findViewById(R.id.editFirst);
        editSecond = findViewById(R.id.editSecond);
        textResult = findViewById(R.id.textAnswer);
        rg = findViewById(R.id.rgTrig);

        textResult.setText("");

        // Click listener for radiogroup
        rg.setOnCheckedChangeListener((radiogroup, id) -> {
            RadioButton radio = findViewById(id);
            trigonAngel = radio.getText().toString().toLowerCase();
            Log.e("trigonAngel", trigonAngel);
        });

        db = new DB(this, "equals.db", null, 1);
    }

    public void btnCalc_click(View v) {

        // Url
        String url = "";
        String result = "";

        // Numbers
        float first = 0f;
        float second = 0f;

        // Text numbers
        String textFirst = editFirst.getText().toString();
        String textSecond = editSecond.getText().toString();

        // For the request
        String td = "";

        // Calculating action
        String action = ((Button)v).getText().toString().toLowerCase();

        if (TextUtils.isEmpty(textFirst)) {
            if (!action.equals("sin") && !action.equals("cos") && !action.equals("tan") && !action.equals("sqrt"))
                if (TextUtils.isEmpty(textFirst))
                    Toast.makeText(this, "Boxes can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            first = Float.parseFloat(textFirst);
            if (!action.equals("sin") && !action.equals("cos") && !action.equals("tan") && !action.equals("sqrt"))
                second = Float.parseFloat(textSecond);
        } catch (Exception ex) {
            Toast.makeText(this, "Вы указали цифры неверно, попробуйте исправить и повторить попытку", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("Action", action);

        if (!textFirst.contains(".") && !textSecond.contains(".")) {
            td = "int";
        } else {
            td = "float";
        }

        Log.e("TD", td);

        // Creating a url
        url = link + action + "?fir=";

        if (td == "int") {
            url += (int)first;
        } else {
            url += first;
        }

        if (action.equals("sin") || action.equals("cos") || action.equals("tan")) {
            url += "&mode=" + trigonAngel;
        } else if (action.equals("sqrt")) {
            // Nothing
        } else {
            if (td == "int") {
                url += "&sec=" + (int)second;
            } else {
                url += "&sec=" + second;
            }
        }
        url += "&td=" + td;

        Log.e("Url", url);

        HttpRequest req = new HttpRequest(this) {
            @Override
            public void onRequestComplete(String response) {
                Log.e("Result", response);
                textResult.setText(response);

                // DB
                String answer = editFirst.getText().toString();

                switch (action) {
                    case "add":
                        answer += " + ";
                        break;

                    case "sub":
                        answer += " - ";
                        break;
                    case "mul":
                        answer += " * ";
                        break;

                    case "div":
                        answer += " / ";
                        break;

                    case "mod":
                        answer += " mod ";
                        break;

                    case "pow":
                        answer += " ^ ";
                        break;

                    case "sin":
                        answer = "sin(" + editFirst.getText().toString() + " " + trigonAngel + ") = ";
                        break;

                    case "cos":
                        answer = "cos(" + editFirst.getText().toString() + " " + trigonAngel + ") = ";
                        break;

                    case "tan":
                        answer = "tan(" + editFirst.getText().toString() + " " + trigonAngel + ") = ";
                        break;

                    case "sqrt":
                        answer = "sqrt(" + editFirst.getText().toString() + " " + trigonAngel + ") = ";
                        break;
                }

                if (!action.equals("sin") && !action.equals("cos") && !action.equals("tan") && !action.equals("sqrt"))
                    answer += editSecond.getText().toString() + " = ";

                answer += textResult.getText().toString();

                db.insert(answer);
            }
        };

        req.makeRequest(url);
    }

    public void btnHistory_click(View v) {
        Notes.notes = db.getAllNotes();
        Intent i = new Intent(this, ActivityHistory.class);
        startActivityForResult(i, 404);
    }
}