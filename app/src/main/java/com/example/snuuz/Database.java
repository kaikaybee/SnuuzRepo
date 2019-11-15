package com.example.snuuz;

        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;

public class Database extends AppCompatActivity {
    MyDB db;
    EditText date;
    EditText wake_up;
    EditText sleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // change layout to main activity
        setContentView(R.layout.activity_main);
        db = new MyDB(this, "Sleep_Tracker", null, 1);
        date = findViewById(R.id.edit1);
        wake_up = findViewById(R.id.edit2);
        sleep = findViewById(R.id.edit3);
    }

    public void insert(View view) {
        String s1 = date.getText().toString();
        String s2 = wake_up.getText().toString();
        String s3 = sleep.getText().toString();
        db.insert(s1, s2, s3);
    }

    public void delete(View view) {
        String s = date.getText().toString();
        db.delete(s);
    }

    public void view(View view) {
        db.getAll();
    }

    public void update(View view) {
        String s1 = date.getText().toString();
        String s2 = wake_up.getText().toString();
        String s3 = sleep.getText().toString();
        db.update(s1, s2, s3);
    }
}
