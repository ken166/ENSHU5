package kadai14.viewer;

import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import kadai14.data.FileAnnotation;


/**
 * ImageFileAnnotation の並び順や選択情報を管理するクラス。
 * ImageFileAnnotation は、List などで管理すればよいでしょう。
 * 課題３で、永続化処理をしてもらうつもり。
 *
 * 描画処理やクリック処理は、ImagePanel 側が担当
 *
 * @author i
 *
 */
public class ImageViewManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<ImageFileAnnotation> list=new ArrayList<ImageFileAnnotation>();
	ImageFileAnnotation selectedImage=null;
	/**
	 * ImageAnnotation のリストを返す
	 * 先頭が奥（背面）、後備が手前（前面）を意味する
	 * @return
	 */
	public ImageViewManager(){
		
	}
	public List<ImageFileAnnotation> getImageList() {
		return list;
	}

	/**
	 * item を選択状態にする。
	 * 選択できる ImageFileAnnotation はひとつとし、
	 * select()起動以前の選択要素は未選択となる
	 *
	 * @param item null の場合は、未選択状態となる。
	 */
	public void select(ImageFileAnnotation item) {
		selectedImage = item;
	}


	/**
	 * 現在選択状態にある ImageFileAnnotation を返す。
	 * @return null は未選択状態であることを示す。
	 */
	public ImageFileAnnotation getSelected() {
		return selectedImage;
	}

	/**
	 * 課題１に倣って、指定 directory 以下の画像ファイルを検索して、ImageFileAnnotation をリストに追加する
	 * 配置場所は、(0,0)-(width, height)内に、並べるなり、ランダムなりで適当に配置してもらってOKです。
	 * @param file
	 */
	public void gatherFiles(File file, int width, int height) {
		
		if(!file.exists()|| !file.isDirectory()) return;
		int w,h;
		w=0;
		h=0;
		for(File f:file.listFiles()){
		Point p = new Point(w,h);
		ImageFileAnnotation image=new ImageFileAnnotation(f,p);
		list.add(image);
		//画像を斜めに少し重ねて表示
		w=w+image.getWidth()-100;
		h=h+image.getHeight()-100;
		}	
		
	}

	/**
	 * 指定された Comparator<FileAnnotation>でソートを行う。
	 * そう、課題１で作った Comparator を使ってみましょう。
	 *
	 * @param comp 比較器
	 */
	public void sortBy(Comparator<FileAnnotation> comp) {
		Collections.sort(list, comp);	//ソート
		int w,h;
		w=0;
		h=0;
		Iterator<ImageFileAnnotation> it=list.iterator();
		ImageFileAnnotation image;
		while(it.hasNext()){
			image=it.next();
			//以下新たな配置場所の設定
			image.p.x=w;
			image.p.y=h;
			w=w+image.getWidth()-100;
			h=h+image.getHeight()-100;
		}
	}
	/**
	 * 保存
	 */
	public void saveContext(){
		try {
			ObjectOutputStream out=
					new ObjectOutputStream(new FileOutputStream("manager.save"));
			
		
				out.writeObject(this);
			out.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	}
}
