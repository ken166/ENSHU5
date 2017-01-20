package kadai14.data;

import java.util.Comparator;
import java.util.Iterator;

/**
 * FileAnnotation を 指定したタグ(tags) を含むかどうかで評価値をつけ、
 * そのスコアで並び替え。
 * @author i
 *
 */
public class CompareByTagSimilarity implements Comparator<FileAnnotation> {

	/**
	 * 指定されたタグ。
	 * 要素数 N に対して、
	 * 最初のタグから順に
	 * N 点, N-1点, ..., 1点を割り振る
	 */
	public final String[] tags;

	public CompareByTagSimilarity(String[] tags) {
		this.tags = tags;
	}
	/**
	 * 指定された this.tags に、それぞれ点数をつけ、
	 * 対処 FileAnnotation o の tags に含まれていれば、その分加点する。
	 *
	 * tags が要素数 N からなる場合、最初の tag から順に
	 * N点, N-1点, ..., 1点
	 * と配点する。
	 *
	 * 例：
	 * o.tags: {"A", "C", "P"}
	 * this.tags: {"A", "B", "C" }
	 * なら、 socre(o) は、 3 + 1 = 4 点とする
	 * @param o socre対象
	 * @return
	 */
	public int score(FileAnnotation o) {
		int n;
		int score=0;
		String otag;
		Iterator<String> it = o.tags.iterator();
		while (it.hasNext()) {
			 otag=it.next();
			 n=tags.length;
			 for(int i=0;i<tags.length;i++){
				 if(otag.equals(tags[i])){
					 score+=n;
				 }
				 n--;
			 }
		}
		return score;
	}


	@Override
	public int compare(FileAnnotation o1, FileAnnotation o2) {
		return Integer.compare(score(o1), score(o2));
	}

}
