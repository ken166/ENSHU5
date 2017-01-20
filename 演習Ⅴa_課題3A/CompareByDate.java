package kadai14.data;

import java.util.Comparator;

/**
 * {@link FileAnnotation#date} の値に応じて、降順に並べる。
 * 値が同じ場合は、ソート前の並び順にしたがうこと。
 * @author i
 *
 */
public class CompareByDate implements Comparator<FileAnnotation> {

	@Override
	public int compare(FileAnnotation o1, FileAnnotation o2) {
		return Long.compare(o1.date,o2.date);
	}
}
