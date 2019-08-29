package com.hqmy.market.common;

import com.hqmy.market.bean.SortBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<SortBean> {

	public int compare(SortBean o1, SortBean o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return -1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return 1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
