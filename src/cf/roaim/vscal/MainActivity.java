package cf.roaim.vscal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView.*;
import java.text.DecimalFormat;
import android.widget.GridView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.text.Html;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.Context;
import android.app.Activity;

public class MainActivity extends Activity
{
    GridView gv ;
	TextView tv;
	ArrayAdapter<String> adapter ;
	String digit = "";
	String fRes;
	SharedPreferences sp;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		getActionBar()
		.setTitle(Html.fromHtml("<font color=\"#eeeeee\">" + getString(R.string.app_name) + "</font>"));
		init();
		gv.setAdapter(adapter);
    }

	private void init()
	{
			sp = getSharedPreferences("vsc",Context.MODE_PRIVATE);
		String[] digits={"1","2","3","4","5","6","7","8","9",".","0","="};
		tv=(TextView) findViewById(R.id.mainTextView1);
		if(!sp.getString("digit","0").equals("")){
					digit=sp.getString("digit","0");
				tv.setText(sp.getString("digit","0")+" TK");
		}
		gv=(GridView) findViewById(R.id.mainGridView1);
		adapter = new ArrayAdapter<String>(this,R.layout.view,digits){
			@Override
			public View getView(int pos,View cv,ViewGroup par){
				final String item = getItem(pos);
				if(cv==null){
					cv=View.inflate(MainActivity.this,R.layout.view,null);
				}
				Button bt = (Button) cv.findViewById(R.id.viewButton1);
				bt.setText(item);
				bt.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1){
						if(item.equals("=")){
							if(!digit.equals("")){
								double d = Double.parseDouble(digit);
								String result = getResultVS(d);
							fRes=result;
							sp.edit().putString("digit","").apply();
							} else{
								fRes="";
							}
							digit="";
							} else if(item.equals(".")){
								if(digit.equals("")){
									digit+="0"+item;
								} else if(!digit.contains(".")) {
									digit+=item;
								}
							fRes=digit;
							} else if(item.equals("0")){
								if(digit.equals("")){
									digit="";
									fRes="";
								} else{
									digit+=item;
									fRes=digit;
								}
							}else {
								digit+=item;
								fRes=digit;
							}
						if(fRes.equals("")) fRes="0";
						tv.setText(fRes+" TK");
					}

					private String getResultVS(double d)
					{
						String res = "";
						double vat = d+(d*.15);
						double sd = vat+(vat*.03);
						DecimalFormat formatter = new DecimalFormat("#.##");
						res = formatter.format(sd);
						return res;
					}
						});
				return cv;
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3B4B52")));
		menu.add("Del").setIcon(android.R.drawable.ic_menu_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1)
				{
					if(digit.length()>0){
						if(digit.equals("0.")){
							digit="";
						}else{
							digit=digit.substring(0,digit.length()-1);		
							}
					}
					if(!digit.equals("")){
						tv.setText(digit+" TK");
					} else tv.setText("0 TK");
					return true;
				}
			});
		menu.add("About").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				private View aboutView;

				@Override
				public boolean onMenuItemClick(MenuItem p1)
				{
					aboutView = View.inflate(MainActivity.this,R.layout.about,null);
					new AlertDialog.Builder(MainActivity.this).
					setCancelable(true).
					setView(aboutView).
					show();
					return true;
				}
			});
		return super.onCreateOptionsMenu(menu);
	}
	
}
