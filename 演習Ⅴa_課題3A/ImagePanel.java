package kadai14.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

/**
 * ImageViewManager の状態に応じて、写真の描画を行う JPanel。
 *
 *
 *
 * @author i
 *
 */
public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<ImageFileAnnotation> list=new ArrayList<ImageFileAnnotation>();
	public ImageViewManager manager; /* 表示対象の ImageViewManager */

	/**
	 * コンストラクタ
	 */
	public ImagePanel() {
		this.setDoubleBuffered(true); /* DoubleBuffer 設定で、paintComponent実行中は表示画面は変わらず、描画完了時に絵が切り替わってくれる */
	}

	/**
	 * ImageViewManager の設定。
	 * setManager 自体は、repaint() 処理を行わないので、プログラマが明示的にrepaint()
	 * しましょう。
	 * 同様に、manager の状態変化に応じて、自動で repaint() は行わないので、
	 * プログラマが明示的に repaint() を呼びましょう。
	 *
	 * （※実装によっては、ImagePanel & ImageViewManager 側で、repaint() の自動化をしてもよいです。
	 * 上記は、（簡単化のための）お勧め設定ということです。 ）
	 *
	 * @param manager
	 */
	public void setManager(ImageViewManager manager) {
		// 実装済み
		this.manager = manager;
	}

	/*
	 * 描画ルーチンのオーバーライド
	 *
	 *  (非 Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		// とりあえず、灰色に塗りつぶしてみる
		g.setColor(Color.gray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if(manager!=null) {
			list=manager.getImageList();
			Iterator<ImageFileAnnotation> it=list.iterator();
			ImageFileAnnotation image;
			while(it.hasNext()){
				
				image=it.next();
				
				image.paintImage(g,image.equals(manager.getSelected()));
			}
			// ImageViewManager に含まれる各 ImageFileAnnotation を先頭（＝奥）から順に描画すべし。
			// ImageFileAnnotation の描画は、ImageFileAnnotation#paintImage(Graphics g, boolean selected)
			// を利用すること。
		}
		
	}

	/**
	 * Click 箇所に該当する ImageFileAnnotation を返す
	 * @param p
	 * @return click 箇所を含む最前面の ImageFileAnnotation を返す。どのImageFileAnnotation にも含まれない場合は、null を返す
	 */
	public ImageFileAnnotation clicked(Point p) {
		ImageFileAnnotation image;
		list=manager.getImageList();
		Iterator<ImageFileAnnotation> it=list.iterator();
		while(it.hasNext()){
			image=it.next();
			if(image.contain(p)){
				return image;
			}		
		}
		return null;
	}
}
