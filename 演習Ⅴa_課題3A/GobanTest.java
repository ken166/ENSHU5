package android.com;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.*;

public class GobanTest extends View {
	private static Bitmap goban,black_image,white_image;
	private static float mas,r;
	private int black=1;
	private int white=2;
	private int turn=black;
	private int[][] board=new int[21][21];
	public GobanTest(Context context) {
		super(context);
		int i,j;
		for(i=0;i<21;i++){
			for(j=0;j<21;j++){
				if(0<i && i<20 && 0<j && j<20)
					board[i][j]=0;
				else
					board[i][j]=-1;
			}
		}
		board[1][1]=black;
		board[1][2]=black;
		board[3][3]=white;
		
	}
	static public void setBitmap(Images images){
		goban=images.goban;
		black_image=images.black;
		white_image=images.white;
		mas=(goban.getWidth()-goban.getWidth()*26/513)/18;
		r=black_image.getWidth()/2;
	}
	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		Paint p = new Paint();
		c.drawBitmap(goban,0,0,p);
		
		for(int i=1;i<=19;i++){
			for(int j=1;j<=19;j++){
				if(board[i][j]==black)
					c.drawBitmap(black_image,(float)(mas*(i-1)+mas*26/27*0.5-r),(float)(mas*(j-1)+mas*26/27*0.5-r),p);
				else if(board[i][j]==white)
					c.drawBitmap(white_image,(float)(mas*(i-1)+mas*26/27*0.5-r),(float)(mas*(j-1)+mas*26/27*0.5-r),p);
			}
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int X = (int)((event.getX()-mas*0.1)/mas+1);
        int Y = (int)((event.getY()-mas*0.1)/mas+1);
        Log.d("aaa",String.valueOf(mas));
        System.out.println(event.getX());
        Log.d("mas",String.valueOf(mas));
        Log.d("getX",String.valueOf(event.getX()));
        Log.d("X",String.valueOf(X));
        if(X <=19 && Y <=19){
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
        if(board[X][Y]==0){
        	board[X][Y]=turn;
        	if(turn==black)
        		turn=white;
        	else
        		turn=black;
        	invalidate();
        	}
        }
        }
       return true;
       }
       

}