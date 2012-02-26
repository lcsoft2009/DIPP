package hipi.util;
import hipi.container.HARIndexContainer;

import java.util.Comparator;

public class HARIndexContainerSorter implements Comparator<HARIndexContainer>{

	public int compare(HARIndexContainer arg0, HARIndexContainer arg1) {
		return arg0.hash - arg1.hash;
	}

}
