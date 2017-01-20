package kadai14.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import kadai14.data.FileAnnotation;

/**
 * FileAnnotation の拡張クラスで、Image の配置場所や、レンダリング機能を備える。
 *
 * @author i
 *
 */
public class ImageFileAnnotation extends FileAnnotation {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int photoSize = 200; /* 写真の「最大」表示サイズ */
	static final int rimSize = 3;     /* 縁のサイズ */
	static final Color defaultColor = Color.WHITE;
	static final Color selectedColor = Color.ORANGE;

	private transient Image image;   /* 取り込んだ写真のイメージ */
	private int width, height;       /* Image の表示サイズ */
	Point p;                         /* Image の表示位置（左上隅） */
	
	/**
	 * コンストラクタ
	 * @param file 対象ファイル
	 * @param p 写真の配置場所の左上座標を示す
	 */
	public ImageFileAnnotation(){}
	public ImageFileAnnotation(File file, Point p) {
		super(file);
		this.p = p;
		setup();
	}
	/**
	 * widthを取得
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * heightを取得
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * レンダリング用の image の作成及び、サイズ情報の初期化。実装済み
	 */
	public void setup() {
		try {
			BufferedImage image0 = ImageIO.read(file);
			if(image0.getWidth() > photoSize || image0.getHeight() > photoSize) { /* 縮小しましょ */
				if(image0.getWidth()> image0.getHeight()) {
					image =image0.getScaledInstance(photoSize, -1, BufferedImage.SCALE_DEFAULT);
				} else {
					image =image0.getScaledInstance(-1, photoSize, BufferedImage.SCALE_DEFAULT);
				}
			} else {
				image = image0;
			}
			width =image.getWidth(null);
			height=image.getHeight(null);
		} catch (IOException e) {
			System.err.println("Can not read image file: "+file.getAbsolutePath());
		}
	}

	/**
	 * 描画ルーチン。
	 * p を左上角とし、width, height を幅、高さとして、画像を表示
	 * 写真の縁は、写真より rimSize 上下左右に大きな四角形を、事前に描画すればよいでしょう。
	 * selected ==true の際は、縁相当の色を変えましょう。
	 * @param g
	 * @param selected 写真が選択状態であることを示す
	 */
	public void paintImage(Graphics g, boolean selected) {
		Color c;
		if(selected)
			c=selectedColor;
		else c=defaultColor;
		
		g.setColor(c);
		g.fillRect(p.x-rimSize, p.y-rimSize, width+rimSize*2, height+rimSize*2);
		g.drawImage(image, p.x, p.y, width, height, null);
	}

	/**
	 * 与えられた位置が、写真の描画位置（縁を含む）に含まれるか判定しましょう。
	 * クリック判定に利用します。
	 *
	 * @param target 判定対象の座標
	 * @return target を含む場合、true
	 */
	public boolean contain(Point target) {
		if(target.x>=p.x-rimSize && target.x <= p.x+width+rimSize && target.y >= p.y-rimSize && target.y<=p.y+height+rimSize){
			return true;
		}
		return false;
	}

}
