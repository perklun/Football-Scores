package barqsoft.footballscores.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoreAppWidgetProvider;
import barqsoft.footballscores.Utilities;
import barqsoft.footballscores.activities.MainActivity;

import barqsoft.footballscores.ScoresAdapter;

/**
 * Intent Service to update widget
 * Created by perklun on 10/17/2015.
 */
public class ScoreWidgetIntentService extends IntentService {
    public ScoreWidgetIntentService() {
        super("ScoreWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ScoreAppWidgetProvider.class));
        for (int i=0; i<appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            // Get the layout for the App Widget
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.score_widget);
            //update views by querying database for socres with a date
            Date fragmentdate = new Date(System.currentTimeMillis());
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            String date = (mformat.format(fragmentdate));
            Cursor cursor = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, new String[]{date}, null);
            if(cursor != null){
                if(cursor.moveToFirst()) {
                    //Set textviews and content descriptors in widget with team names, scores
                    views.setTextViewText(R.id.tv_widget_home_name, cursor.getString(ScoresAdapter.COL_HOME));
                    views.setContentDescription(R.id.tv_widget_home_name, cursor.getString(ScoresAdapter.COL_HOME));
                    views.setTextViewText(R.id.tv_widget_away_name, cursor.getString(ScoresAdapter.COL_AWAY));
                    views.setContentDescription(R.id.tv_widget_away_name, cursor.getString(ScoresAdapter.COL_AWAY));
                    views.setTextViewText(R.id.tv_widget_data_textview, cursor.getString(ScoresAdapter.COL_DATE));
                    views.setContentDescription(R.id.tv_widget_data_textview, cursor.getString(ScoresAdapter.COL_DATE));
                    String score = Utilities.getScores(cursor.getInt(ScoresAdapter.COL_HOME_GOALS), cursor.getInt(ScoresAdapter.COL_AWAY_GOALS));
                    views.setTextViewText(R.id.tv_widget_score_textview, score);
                    views.setContentDescription(R.id.tv_widget_data_textview, score);
                    // Tell the AppWidgetManager to perform an update on the current app widget
                    cursor.close();
                    Intent open_app_intent = new Intent(this, MainActivity.class);
                    views.setOnClickPendingIntent(R.id.ll_widget, PendingIntent.getActivity(this,0, open_app_intent, PendingIntent.FLAG_CANCEL_CURRENT));
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }
        }
    }
}
