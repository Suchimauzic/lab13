package g313.gusev.lab13;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityHistory extends AppCompatActivity {

    ArrayAdapter<String> adp;
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        lst = findViewById(R.id.listAnswers);
        adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lst.setAdapter(adp);
        for (int i = 0; i < Notes.notes.toArray().length; i++) {
            adp.add(Notes.notes.get(i).info());
        }
    }

    public void btnBack_Click(View v) {
        finish();
    }
}