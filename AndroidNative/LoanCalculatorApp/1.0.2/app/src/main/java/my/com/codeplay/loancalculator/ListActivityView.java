package my.com.codeplay.loancalculator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

public class ListActivityView extends AppCompatActivity {
    private Database db = null;
    private Cursor result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        ListView listView = (ListView) findViewById(android.R.id.list);

        db = new Database(this);

        result = db.query();
        listView.setAdapter(new MyCursorAdapter(this, result));
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (null == db) {
            db = new Database(this);
            result = db.query();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (db != null)
            db.close();
    }
}
