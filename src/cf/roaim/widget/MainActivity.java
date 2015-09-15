package cf.roaim.widget;

import android.app.Activity;
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

public class MainActivity extends Activity
{
    GridView gv ;
	TextView tv;
	ArrayAdapter<String> adapter ;
	String digit = "";
	String fRes;
	
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
		String[] digits={"1","2","3","4","5","6","7","8","9",".","0","="};
		tv=(TextView) findViewById(R.id.mainTextView1);
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
		return super.onCreateOptionsMenu(menu);
	}
	
}
