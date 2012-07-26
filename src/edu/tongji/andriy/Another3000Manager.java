package edu.tongji.andriy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Pair;

public class Another3000Manager {

	public static final int UNITS_PER_PDF = 10;
	public static final int PDF_COUNT = 31;

	/**
	 * ���һ��pdfֻ��8��units�������Ķ��� @UNITS_PER_PDF ��
	 */
	public static final int UNIT_COUNT = UNITS_PER_PDF * (PDF_COUNT - 1) + 8;


	/**
	 * Ҫ���е�˳��
	 */
	private int[] recite_order = new int[UNIT_COUNT];
	
	public Another3000Manager() {
		for (int i = 0; i < recite_order.length; i++) {
			recite_order[i] = i;
		}
	}
	
	/**
	 * �ѱ��е�˳���������һ��
	 */
	public void RandomizeReciteOrder() {
		Random random = new Random();
		for (int i = 0; i < UNIT_COUNT; i++) {
			int pos1 = random.nextInt(UNIT_COUNT);
			int pos2 = random.nextInt(UNIT_COUNT);
			if (pos1 == pos2) {
				continue;
			}
			
			int temp = recite_order[pos1];
			recite_order[pos1] = recite_order[pos2];
			recite_order[pos2] = temp;
		}
	}
	
	/**
	 * �õ����е�˳��
	 * @return һ��List����ͷ����Pair<PDF_INDEX, UNIT_INDEX>
	 */
	public List<Pair<Integer, Integer>> GetReciteOrder() {
		List<Pair<Integer, Integer>> orderList = new ArrayList<Pair<Integer,Integer>>(UNIT_COUNT);
		for (int index : recite_order) {
			orderList.add(new Pair<Integer, Integer>(this.ParsePDFIndex(index), this.ParseUnitIndex(index)));			
		}
		
		return orderList;
	}

	/**
	 * @param index ��ȫ��units�е�index
	 * @return ��index���ĸ�pdf��
	 */
	private int ParsePDFIndex(int index) {
		if (index < 0 || index >= UNIT_COUNT) {
			throw new IllegalArgumentException("Invalid Unit Index!!! Caught by Andriy");
		}

		return index / UNITS_PER_PDF;
	}
	
	/**
	 * @param index ��ȫ��units�е�index
	 * @return ��index����pdf���ǵڼ���unit
	 */
	private int ParseUnitIndex(int index) {
		if (index < 0 || index >= UNIT_COUNT) {
			throw new IllegalArgumentException("Invalid Unit Index!!! Caught by Andriy");
		}
		
		return index % UNITS_PER_PDF;
	}
}
