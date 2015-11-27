package cf.roaim.vscal;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.widget.Button;
import android.widget.RemoteViews;
import android.os.Bundle;
import android.util.Log;
import android.content.SharedPreferences;
import java.text.DecimalFormat;

public class VSCWidget extends AppWidgetProvider
{
		private SharedPreferences sp ;
		private String tvText = "0";
		private static final String KEY = "tv";
		private static final String tvTexts[] = {"0","1","2","3","4","5","6","7","8","9",".","="};
		private static final String KEYSP="digit";
		private static final int[] getBtIds ( ) {
				int[] ids = {R.id.wBt0,R.id.wBt1,R.id.wBt2,R.id.wBt3,R.id.wBt4,R.id.wBt5,R.id.wBt6,R.id.wBt7,R.id.wBt8,R.id.wBt9,R.id.wBtP,R.id.wBtE};
				return ids;
		}

		@Override
		public void onReceive ( Context context, Intent intent ) {
				// TODO: Implement this method
				lg("on rec");
				sp = context.getSharedPreferences("vsc",Context.MODE_PRIVATE);
				String sDigit = sp.getString(KEYSP,"");
				if(intent!=null && intent.getIntExtra(KEY,-5)==17){
						String digit = intent.getAction();
						if(digit.equals("=")){
								if(!sDigit.equals("")){
										tvText = calculate(sDigit);
										sDigit="";
										sp.edit().putString(KEYSP,sDigit).apply();
								} else{
										tvText="0";
								}
						} else if(digit.equals(".")){
								if(!sDigit.contains(".")){
										sDigit+=".";
										tvText=sDigit;
										sp.edit().putString(KEYSP,sDigit).apply();
								} else return;
						} else {
								sDigit+=digit;
								tvText=sDigit;
								sp.edit().putString(KEYSP,sDigit).apply();
						}
						int[] ids = {intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,-1)};
						lg("intent not null: \nId: "+ids+" text: "+tvText);
						AppWidgetManager awm = AppWidgetManager.getInstance(context);
						onUpdate(context,awm,ids);
				}
				super.onReceive ( context, intent );
		}

		private String calculate ( String sDigit ) {
				double input = 0;
				try{
						input = Double.parseDouble(sDigit);
						input+=(input*.15);
						input+=(input*.03);
				} catch(Exception e){
						lg(e.toString());
				}
				return new DecimalFormat("#.##").format(input);
		}
		
		@Override
		public void onUpdate ( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds ) {
				// TODO: Implement this method
				lg("on update");
				for(int i=0;i<appWidgetIds.length;i++){
						int id=appWidgetIds[i];
						lg("id: "+id);
						RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.vsc_widget);
						lg("tvText: "+tvText);
						rv.setTextViewText(R.id.wTv,tvText);
						for(int x=0;x<getBtIds().length;x++){
								Intent intent = getBtIntent(context,id,x);
								PendingIntent pi = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
								rv.setOnClickPendingIntent(getBtIds()[x],pi);
						}
						appWidgetManager.updateAppWidget(id,rv);
						lg("updated");
				}
		}
		
		private Intent getBtIntent(Context context,int ids,int position){
				Intent intent = new Intent(context,VSCWidget.class);
				intent.setAction(tvTexts[position]);
				intent.putExtra(KEY,17);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,ids);
				return intent;
		}
		private static void lg(String log){
				Log.i("rah",log);
		}
}
