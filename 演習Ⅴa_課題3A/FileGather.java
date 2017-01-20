package kadai14.data;

import java.io.File;
import java.util.Collection;

public abstract class FileGather<T> {

	public abstract T convert(File f);

	public void gather(File f, String[] suffixes, Collection<T> list) {
		if(!f.exists()) return;
		if(f.isFile()) {
			String name = f.getName();
			for(String suffix: suffixes) {
				if(name.endsWith(suffix)) {
					list.add(this.convert(f));
				}
			}
		} else {
			File[] children = f.listFiles();
			if(children==null) return;
			for(File child: children) {
				gather(child, suffixes, list);
			}
		}
	}
}
