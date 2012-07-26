package edu.tongji.andriy;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

public class Another3000Manager {

	public static final int UNITS_PER_PDF = 10;
	public static final int PDF_COUNT = 31;

	/**
	 * 最后一个pdf只有8个units，其他的都有 @UNITS_PER_PDF 个
	 */
	public static final int UNIT_COUNT = UNITS_PER_PDF * (PDF_COUNT - 1) + 8;


	/**
	 * 要背诵的顺序
	 */
	private int[] recite_order = new int[UNIT_COUNT];
	
	public Another3000Manager() {
		for (int i = 0; i < recite_order.length; i++) {
			recite_order[i] = i;
		}
	}
	
	/**
	 * 把背诵的顺序随机打乱一下
	 */
	public void RandomizeReciteOrder() {
		
	}
	
	/**
	 * 得到背诵的顺序
	 * @return 一个List，里头放着Pair<PDF_INDEX, UNIT_INDEX>
	 */
	public List<Pair<Integer, Integer>> GetReciteOrder() {
		List<Pair<Integer, Integer>> orderList = new ArrayList<Pair<Integer,Integer>>(UNIT_COUNT);
		for (int index : recite_order) {
			orderList.add(new Pair<Integer, Integer>(this.ParsePDFIndex(index), this.ParseUnitIndex(index)));			
		}
		
		return orderList;
	}

	/**
	 * @param index 在全部units中的index
	 * @return 此index在哪个pdf中
	 */
	private int ParsePDFIndex(int index) {
		if (index < 0 || index >= UNIT_COUNT) {
			throw new IllegalArgumentException("Invalid Unit Index!!! Caught by Andriy");
		}

		return index / UNITS_PER_PDF;
	}
	
	/**
	 * @param index 在全部units中的index
	 * @return 此index在其pdf中是第几个unit
	 */
	private int ParseUnitIndex(int index) {
		if (index < 0 || index >= UNIT_COUNT) {
			throw new IllegalArgumentException("Invalid Unit Index!!! Caught by Andriy");
		}
		
		return index % UNITS_PER_PDF;
	}
}
