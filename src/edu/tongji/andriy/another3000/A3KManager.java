package edu.tongji.andriy.another3000;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;


public class A3KManager {

	/**
	 * ���һ��pdfֻ�� @UNITS_IN_LAST_PDF ���������Ķ��� @UNITS_PER_PDF ��
	 */
	public static final int UNIT_COUNT = A3KIndex.UNITS_PER_PDF
			* (A3KIndex.PDF_COUNT - 1)
			+ A3KIndex.UNITS_IN_LAST_PDF;

	/**
	 * Ҫ���е�˳��
	 */
	private ArrayList<A3KIndex> reciteOrder = new ArrayList<A3KIndex>(UNIT_COUNT);
	/**
	 * �Ѿ�ѧ������Щunits
	 */
	private TreeSet<A3KIndex> recitedUnits = new TreeSet<A3KIndex>();
	
	public A3KManager() {
		for (int i = 0; i < UNIT_COUNT; i++) {
			reciteOrder.add(new A3KIndex(i));
//			recitedUnits.add(reciteOrder[i]);
		}
		
		Random random = new Random();
		for (int i = 0; i < UNIT_COUNT/2; i++) {
			recitedUnits.add(new A3KIndex(random.nextInt(UNIT_COUNT)));
		}
	}
	
	/**
	 * �ý�����Ҫ���� @param count ��units
	 * @return ������ôЩAnother3000Index��List
	 */
	public List<A3KIndex> GetNextUnits(int count) {
		if (count <0 || count > UNIT_COUNT) {
			throw new IllegalArgumentException("Invalid! ��ô��������ô���units�أ� Caught by Andriy");
		}
		
		List<A3KIndex> unitsList = new ArrayList<A3KIndex>(count);
		for (int searched = 0, recitePos = 0; searched < count && recitePos < UNIT_COUNT; recitePos++) {
			A3KIndex index = reciteOrder.get(recitePos);
			if (!recitedUnits.contains(index)) {
				searched++;
				unitsList.add(index);
			}
		}
		return unitsList;
	}
	
	/**
	 * @return ��Щ�Ѿ�������units��һ��װ��Another3000Index��List
	 */
	public List<A3KIndex> GetRecitedUnits() {
		List<A3KIndex> recitedList = new ArrayList<A3KIndex>(recitedUnits.size());
		recitedList.addAll(recitedUnits);
		return recitedList;
	}
	
	/**
	 * ���� @param recited���� @param index ����Ϊ��������û����
	 */
	public void SetUnitStudied(A3KIndex index, boolean recited) {
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
		
			A3KIndex temp = reciteOrder.get(pos1);
			reciteOrder.set(pos1, reciteOrder.get(pos2));
			reciteOrder.set(pos2, temp);
		}
	}
	
	/**
	 * �õ����е�˳��
	 * @return һ��List����ͷ����Pair<PDF_INDEX, UNIT_INDEX>
	 */
	public List<A3KIndex> GetReciteOrder() {
		return this.reciteOrder;
	}

}
