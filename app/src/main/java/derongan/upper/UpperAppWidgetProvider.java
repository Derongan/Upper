package derongan.upper;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.os.Bundle;

import java.util.Map;

public class UpperAppWidgetProvider extends AppWidgetProvider {

    private static final String INCREMENT_COUNT = "derongan.upper.UpperAppWidgetProvider.INCREMENT_COUNT";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);

        if(INCREMENT_COUNT.equals(intent.getAction())){

            Bundle extras = intent.getExtras();
            int mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            SharedPreferences preferences = context.getSharedPreferences("derongan.upper", 0);
            String thingToCount = preferences.getString(String.valueOf(mAppWidgetId), "Counter");
            int counter = preferences.getInt(thingToCount.concat("_count"), 0);

            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt(thingToCount.concat("_count"), counter+1);
            edit.commit();

            Intent intent1 = new Intent(context, UpperAppWidgetProvider.class);
            intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            final int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, UpperAppWidgetProvider.class));

            intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            context.sendBroadcast(intent1);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        SharedPreferences preferences = context.getSharedPreferences("derongan.upper", 0);

        String thingToCount = preferences.getString(String.valueOf(appWidgetId), "Counter");
        int counter = preferences.getInt(thingToCount.concat("_count"), 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.upper_appwidget);
        views.setTextViewText(R.id.upper_type, thingToCount);
        views.setTextViewText(R.id.counter, String.valueOf(counter));

        Intent intent = new Intent(context, UpperAppWidgetProvider.class);
        intent.setAction(INCREMENT_COUNT);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.increment, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}