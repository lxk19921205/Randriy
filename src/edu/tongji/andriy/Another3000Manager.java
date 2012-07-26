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
	 * 最后一个pdf只有 @UNITS_IN_LAST_PDF 个，其他的都有 @UNITS_PER_PDF 个
	 */
	public static final int UNIT_COUNT = UNITS_PER_PDF * (PDF_COUNT - 1) + UNITS_IN_LAST_PDF;


	/**
	 * 要背诵的顺序
	 */
	private int[] reciteOrder = new int[UNIT_COUNT];
	/**
	 * 已经学过的那些units
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
	 * 拿接下来要背的 @param count 个units
	 * @return 包含这么些Pair<PDF, UNIT>的List
	 */
	public List<Pair<Integer, Integer>> GetNextUnits(int count) {
		if (count <0 || count > UNIT_COUNT) {
			throw new IllegalArgumentException("Invalid! 怎么可以拿这么多个units呢！ Caught by Andriy");
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
	 * @return 那些已经背过的units，一个装有Pair<PDF, UNIT>的List
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
	 * 将 @param pdf 的那个PDF里的 @param unit 那个UNIT，根据 @param recited 设为学过或者没有学过
	 * @param pdf 从1开始
	 * @param unit 从1开始 
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
	 * 把背诵的顺序随机打乱一下
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
	 * 得到背诵的顺序
	 * @return 一个List，里头放着Pair<PDF_INDEX, UNIT_INDEX>
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
	 * @param index 在全部units中的index
	 * @return 此index在哪个pdf中
	 */
	private int ParsePDFIndex(int index) {
		return index / UNITS_PER_PDF;
	}
	
	/**
	 * @param index 在全部units中的index
	 * @return 此index在其pdf中是第几个unit
	 */
	private int ParseUnitIndex(int index) {
		return index % UNITS_PER_PDF;
	}
	
	/**
	 * @param pdf 第几个PDF，从0开始计数
	 * @param unit 此PDF中的第几个unit，从0开始计数
	 * @return 在所有units中的index
	 */
	private int ParseTotalIndex(int pdf, int unit) {
		return pdf * UNITS_PER_PDF + unit;
	}
}
