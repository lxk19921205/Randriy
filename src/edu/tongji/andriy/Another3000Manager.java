package edu.tongji.andriy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import android.util.Pair;

public class Another3000Manager {

	public static final int UNITS_PER_PDF = 10;
	public static final int UNITS_IN_LAST_PDF = 8;
	public static final int PDF_COUNT = 31;

	/**
	 * ���һ��pdfֻ�� @UNITS_IN_LAST_PDF ���������Ķ��� @UNITS_PER_PDF ��
	 */
	public static final int UNIT_COUNT = UNITS_PER_PDF * (PDF_COUNT - 1) + UNITS_IN_LAST_PDF;


	/**
	 * Ҫ���е�˳��
	 */
	private int[] reciteOrder = new int[UNIT_COUNT];
	/**
	 * �Ѿ�ѧ������Щunits
	 */
	private HashSet<Integer> recitedUnits = new HashSet<Integer>();
	
	public Another3000Manager() {
		for (int i = 0; i < reciteOrder.length; i++) {
			reciteOrder[i] = i;
//			recitedUnits.add(reciteOrder[i]);
		}
		
		Random random = new Random();
		for (int i = 0; i < UNIT_COUNT/2; i++) {
			recitedUnits.add(random.nextInt(UNIT_COUNT));
		}
	}
	
	/**
	 * �ý�����Ҫ���� @param count ��units
	 * @return ������ôЩPair<PDF, UNIT>��List
	 */
	public List<Pair<Integer, Integer>> GetNextUnits(int count) {
		if (count <0 || count > UNIT_COUNT) {
			throw new IllegalArgumentException("Invalid! ��ô��������ô���units�أ� Caught by Andriy");
		}
		
		List<Pair<Integer, Integer>> unitsList = new ArrayList<Pair<Integer,Integer>>(count);
		for (int searched = 0, recitePos = 0; searched < count && recitePos < reciteOrder.length; recitePos++) {
			int total_index = reciteOrder[recitePos];
			if (!recitedUnits.contains(total_index)) {
				searched++;
				
				if (total_index < 0 || total_index >= UNIT_COUNT) {
					throw new IllegalArgumentException("Invalid Unit Index!!! Caught by Andriy");
				}
				
				unitsList.add(new Pair<Integer, Integer>(this.ParsePDFIndex(total_index), this.ParseUnitIndex(total_index)));
			}
		}

		return unitsList;
	}
	
	/**
	 * @return ��Щ�Ѿ�������units��һ��װ��Pair<PDF, UNIT>��List
	 */
	public List<Pair<Integer, Integer>> GetRecitedUnits() {
		Integer [] recitedIntegers = new Integer[recitedUnits.size()];
		recitedUnits.toArray(recitedIntegers);
		Arrays.sort(recitedIntegers, new Comparator<Integer>() {

			@Override
			public int compare(Integer lhs, Integer rhs) {
				return lhs - rhs;
			}
		});

		List<Pair<Integer, Integer>> recitedList = new ArrayList<Pair<Integer,Integer>>(recitedUnits.size());
		for (Integer recited : recitedIntegers) {
			if (recited < 0 || recited >= UNIT_COUNT) {
				throw new IllegalArgumentException("Invalid Unit Index!!! Caught by Andriy");
			}
			
			recitedList.add(new Pair<Integer, Integer>(this.ParsePDFIndex(recited), this.ParseUnitIndex(recited)));
		}
		return recitedList;
	}
	
	/**
	 * �� @param pdf ���Ǹ�PDF��� @param unit �Ǹ�UNIT������ @param recited ��Ϊѧ������û��ѧ��
	 * @param pdf ��1��ʼ
	 * @param unit ��1��ʼ 
	 */
	public void SetUnitStudied(int pdf, int unit, boolean recited) {
		pdf--;
		unit--;
		
		if (pdf < 0 || pdf >= PDF_COUNT) {
			throw new IllegalArgumentException("Invalid! No such PDF! Caught by Andriy");
		}
		if (pdf < PDF_COUNT-1 && (unit < 0 || unit >= UNITS_PER_PDF)) {
			throw new IllegalArgumentException("Invalid! No such UNIT in that PDF! Caught by Andriy");
		}
		if (pdf == PDF_COUNT-1 && (unit < 0 || unit >= UNITS_IN_LAST_PDF)) {
			throw new IllegalArgumentException("Invalid! No such UNIT in that PDF! Caught by Andriy");
		}
		
		int index = ParseTotalIndex(pdf, unit);
		if (recited) {
			recitedUnits.add(index);
		}
		else {
			recitedUnits.remove(index);
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
			
			int temp = reciteOrder[pos1];
			reciteOrder[pos1] = reciteOrder[pos2];
			reciteOrder[pos2] = temp;
		}
	}
	
	/**
	 * �õ����е�˳��
	 * @return һ��List����ͷ����Pair<PDF_INDEX, UNIT_INDEX>
	 */
	public List<Pair<Integer, Integer>> GetReciteOrder() {
		List<Pair<Integer, Integer>> orderList = new ArrayList<Pair<Integer,Integer>>(UNIT_COUNT);
		for (int index : reciteOrder) {
			if (index < 0 || index >= UNIT_COUNT) {
				throw new IllegalArgumentException("Invalid Unit Index!!! Caught by Andriy");
			}

			orderList.add(new Pair<Integer, Integer>(this.ParsePDFIndex(index), this.ParseUnitIndex(index)));			
		}
		
		return orderList;
	}

	/**
	 * @param index ��ȫ��units�е�index
	 * @return ��index���ĸ�pdf��
	 */
	private int ParsePDFIndex(int index) {
		return index / UNITS_PER_PDF;
	}
	
	/**
	 * @param index ��ȫ��units�е�index
	 * @return ��index����pdf���ǵڼ���unit
	 */
	private int ParseUnitIndex(int index) {
		return index % UNITS_PER_PDF;
	}
	
	/**
	 * @param pdf �ڼ���PDF����0��ʼ����
	 * @param unit ��PDF�еĵڼ���unit����0��ʼ����
	 * @return ������units�е�index
	 */
	private int ParseTotalIndex(int pdf, int unit) {
		return pdf * UNITS_PER_PDF + unit;
	}
}
