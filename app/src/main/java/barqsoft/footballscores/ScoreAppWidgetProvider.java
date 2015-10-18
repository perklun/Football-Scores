package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import barqsoft.footballscores.fragment.MainScreenFragment;
import barqsoft.footballscores.service.ScoreWidgetIntentService;

/**
 * Appwidgetprovider for widget
 * Created by perklun on 10/17/2015.
 */
public class ScoreAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        context.startService(new Intent(context, ScoreWidgetIntentService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(MainScreenFragment.SCORE_DATA_UPDATED.equals(intent.getAction())){
            context.startService(new Intent(context, ScoreWidgetIntentService.class));
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, ScoreWidgetIntentService.class));
    }
}
