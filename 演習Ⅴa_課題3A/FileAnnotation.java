package kadai14.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class FileAnnotation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String comment; // Photo Comment
	public TreeSet<String> tags; // tag list;
	public long date;
	public File file;

	public FileAnnotation(File file, String comment, TreeSet<String> tags) {
		this.comment = comment;
		this.date = file.lastModified();
		this.tags = tags;
		this.file = file;
	}
	public FileAnnotation(){
	}
	
	public FileAnnotation (File file) {
		this(file, "", new TreeSet<String>());
	}

	@Override
	public String toString() {
		// TODO 下記はtoString() の能力を把握してもらうためのダミーコードです。
		return "FileAnnotation[ date: "+date+"="+getDateStr()+", size: "+file.length()+", comment: "+comment+", tags: "+ tags + ", name: " + file.getAbsoluteFile() +" " ;
	}
	/**
	 * コメント内容の変更
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * タグをひとつ追加
	 * @param tag
	 */
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	/**
	 * 複数のタグを追加
	 * @param tag
	 */
	public void addTags(Collection<String> tags) {
		this.tags.addAll(tags);
	}

	/**
	 * date を人間がわかるような時間表記に変換。
	 * @return
	 */
	public String getDateStr() {
		return new Date(date).toString();
	}

	/**
	 * File f がファイルの場合、末尾が suffixes のいずれかに合致する場合、new FileAnnotation(f) を list に加える。
	 * f が directory の場合、その directory もしくは 子孫 directory に含まれるすべてのファイル file に対し、
	 * new FileAnnotation(file) を list に加える。
	 * @param f
	 * @param suffixes
	 * @param list
	 */
	public static void gather(File f, String[] suffixes, Collection<FileAnnotation> list) {
		if(!f.exists()) return;
		if(f.isFile()) {
			String name = f.getName();
			for(String suffix: suffixes) {
				if(name.endsWith(suffix)) {
					list.add(new FileAnnotation(f));
				}
			}
		} else {
			File[] children = f.listFiles();
			if(children==null) return;
			for(File child: children) {
				gather(child, suffixes, list);
			}
		}
		// 匿名クラスとか使えるようになると、FileFilter や FilenameFilter などを使ってもよいかも。
	}

	/**
	 * 上記 gather() を使って集めたファイルに、seed に基づいた乱数で、
	 * コメント＆タグを付与したものを作成。
	 * @param seed
	 * @return
	 */
	public static List<FileAnnotation> genList(long seed) {
		List<FileAnnotation> list = new ArrayList<FileAnnotation>();
		gather(new File("."), new String[]{".java", ".class"}, list);
		String[] tags = new String[] {"tagA", "tagB", "tagC", "tagD"};

		// テストデータを擬似ランダム生成
		Random r1 = new Random(seed), r2 = new Random(seed);

		for(FileAnnotation fa: list) {
			fa.setComment("c"+ (1000+r1.nextInt(8000)));
			fa.addTag(tags[r2.nextInt(4)]);
			fa.addTag(tags[r2.nextInt(4)]);
		}
		return list;
	}

	/**
	 * list の各要素 fa に対して、System.out.println(fa); を実行してください。
	 * @param list
	 */
	public static void printList(List<FileAnnotation> list) {
		Iterator<FileAnnotation> it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());	
		}
	}


	public static void main(String[] args) {
		// このメソッドの中はいじらないように
		long start = System.currentTimeMillis();
		System.out.println("Start time: " + new Date(start) +", " + start);

		List<FileAnnotation> list = genList(start);
		System.out.println("Sample:" + list.get(0));

		System.out.println("Input Data ----------");
		printList(list);

		// File サイズでソートしてみよう (サイズが小さいものが先頭)。サイズが同じなら、前の並びに従う
		Collections.sort(list, new CompareByFileSize());
		System.out.println("Sorted By File Size (in ascending order) ----------");
		printList(list);

		// 日付順でソートしてみよう (古いものが先頭）。時刻が同じなら、前の並びに従う
		Collections.sort(list, new CompareByDate());
		System.out.println("Sorted By Date (in ascending order) ----------");
		printList(list);

		// 指定タグの含み方に応じてスコア配点。詳細は、Comparator に記載, 評価値が同じなら、ソート前のならびに従う
		Collections.sort(list, new CompareByTagSimilarity(new String[]{"tagA", "tagB", "tagC"}));
		System.out.println("Sorted By Tag Similarity (in ascending order) ----------");
		printList(list);

		// 指定時刻(実行30分前)に近いかどうかでソート。 評価値が同じなら、ソート前のならびに従う
		long target = start-1000L*60*30;
		Collections.sort(list, new CompareByDateSimilarity(target));
		System.out.println("Sorted By Date Similarity to [" + target +"="+new Date(target) + "] ----------");
		printList(list);
	}
}
