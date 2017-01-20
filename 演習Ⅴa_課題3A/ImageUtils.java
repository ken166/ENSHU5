package android.com;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
 
public class ImageUtils {
     
    private static final String TAG = "ImageUtils";
     
    public static Images resizeBitmapToDisplaySize(Activity activity, Bitmap src,Bitmap src2,Bitmap src3){
    	Images images=new Images();
        int srcWidth = src.getWidth(); // 元画像のwidth
        int srcHeight = src.getHeight(); // 元画像のheight
        Log.d(TAG, "srcWidth = " + String.valueOf(srcWidth)
                + " px, srcHeight = " + String.valueOf(srcHeight) + " px");
 
        // 画面サイズを取得する
        Matrix matrix = new Matrix();
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float screenWidth = (float) metrics.widthPixels;
        float screenHeight = (float) metrics.heightPixels;
        Log.d(TAG, "screenWidth = " + String.valueOf(screenWidth)
                + " px, screenHeight = " + String.valueOf(screenHeight) + " px");
 
        float widthScale = screenWidth / srcWidth;
        float heightScale = screenHeight / srcHeight;
        Log.d(TAG, "widthScale = " + String.valueOf(widthScale)
                + ", heightScale = " + String.valueOf(heightScale));
        if (widthScale > heightScale) {
            matrix.postScale(heightScale, heightScale);
        } else {
            matrix.postScale(widthScale, widthScale);
        }
        // リサイズ
        images.goban = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);
        images.black = Bitmap.createBitmap(src2, 0, 0, src2.getWidth(), src2.getHeight(), matrix, true);
        images.white = Bitmap.createBitmap(src3, 0, 0, src3.getWidth(), src3.getHeight(), matrix, true);
        /*
        int dstWidth = dst.getWidth(); // 変更後画像のwidth
        int dstHeight = dst.getHeight(); // 変更後画像のheight
        Log.d(TAG, "dstWidth = " + String.valueOf(dstWidth)
                + " px, dstHeight = " + String.valueOf(dstHeight) + " px");
                */
        src = null;
        src2=null;
        src3=null;
        return images;
    }
}