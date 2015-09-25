package derongan.upper;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UpperWidgetConfigure extends AppCompatActivity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        getSupportActionBar().setTitle("Configure your Upper");

        setContentView(R.layout.upper_widget_configure);

        Context context = UpperWidgetConfigure.this;

        SharedPreferences preferences = context.getSharedPreferences("derongan.upper", 0);

        ArrayList<String> list = new ArrayList<String>();

        Map<String, ?> allEntries = preferences.getAll();

        for(Map.Entry<String, ?> entry : allEntries.entrySet()){
            if(entry.getKey().endsWith("_count")){
                list.add(entry.getKey().substring(0, entry.getKey().length()-"_count".length()));
            }
        }

        UpperArrayAdapter arrayAdapter = new UpperArrayAdapter(context, R.layout.list_item, list);
        arrayAdapter.insert("Custom", 0);


        ListView listView = (ListView) findViewById(R.id.upper_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(arrayAdapter);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();

        if(extras != null){
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if(mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }
    }

    public void createWidget(View v){
        EditText editText = (EditText) findViewById(R.id.count_type);
        String message = editText.getText().toString();

        CheckBox checkBox = (CheckBox) findViewById(R.id.reset_count);
        Boolean checked = checkBox.isChecked();

        Context context = UpperWidgetConfigure.this;

        SharedPreferences preferences = context.getSharedPreferences("derongan.upper", 0);
        SharedPreferences.Editor edit = preferences.edit();

        edit.putString(String.valueOf(mAppWidgetId), message);

        if(checked) {
            edit.putInt(message.concat("_count"), 0);
            Intent intent1 = new Intent(context, UpperAppWidgetProvider.class);
            intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            final int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, UpperAppWidgetProvider.class));

            intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            context.sendBroadcast(intent1);
        }

        edit.commit();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        UpperAppWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
