package android.com;

import android.app.Activity;
import android.com.R.id;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Images images=new Images();
		Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.goban19);
		Bitmap src2 = BitmapFactory.decodeResource(getResources(), R.drawable.black);
		Bitmap src3 = BitmapFactory.decodeResource(getResources(), R.drawable.white);
		images = ImageUtils.resizeBitmapToDisplaySize(this,src,src2,src3);
	    GobanTest.setBitmap(images);	
	    //画面を縦方向で固定する
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //レイアウトを用意する
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        setContentView(l);
        //Viewをセットする
        Button button1 = new Button(this);
        button1.setText("Button");
        l.addView(button1);
        l.addView(new GobanTest(this));
        /*
		setContentView(R.layout.test);
		*/
		/*
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		*/
        /*
		 TextView txt1 = (TextView) findViewById(id.txt1);
		 TextView txt2 = (TextView) findViewById(id.txt2);
		 */
		 /*
		 CanvasTest ca=(CanvasTest) findViewById(id.sv_main);
		 ca.flag=1;
		 */
	}
/*
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
      super.onWindowFocusChanged(hasFocus);
      int width = findViewById(R.id.txt1).getWidth();
      int height = findViewById(R.id.txt2).getHeight();
    }
*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
