package kadai14.data;

import java.util.Comparator;

/**
 * FileAnnotation を 指定した時刻(targetDate)に近いもの順に並べる。
 * 値が同じ場合は、ソート前の並び順にしたがうこと。
 * @author i
 *
 */
public class CompareByDateSimilarity implements Comparator<FileAnnotation> {

	/* 指定時刻 */
	private long targetDate;

	public CompareByDateSimilarity(long date) {
		this.targetDate = date;
	}

	@Override
	public int compare(FileAnnotation o1, FileAnnotation o2) {
		return Long.compare(Math.abs(o2.date-targetDate),Math.abs(o1.date-targetDate));
	}
}
