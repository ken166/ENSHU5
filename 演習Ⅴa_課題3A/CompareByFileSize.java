package kadai14.data;

import java.util.Comparator;

/**
 * FileAnnotation を、FileAnnotation#file のサイズ順に並べる。
 * @author i
 *
 */
public class CompareByFileSize implements Comparator<FileAnnotation> {

	@Override
	public int compare(FileAnnotation o1, FileAnnotation o2) {
		return Long.compare(o1.file.length(),o2.file.length());	
	}

}
