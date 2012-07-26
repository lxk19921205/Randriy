package edu.tongji.andriy.another3000;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import android.content.Context;


public class A3KManager {

	/**
	 * 最后一个pdf只有 @UNITS_IN_LAST_PDF 个，其他的都有 @UNITS_PER_PDF 个
	 */
	public static final int UNIT_COUNT = A3KIndex.UNITS_PER_PDF
			* (A3KIndex.PDF_COUNT - 1)
			+ A3KIndex.UNITS_IN_LAST_PDF;

	/**
	 * 要背诵的顺序
	 */
	private ArrayList<A3KIndex> reciteOrder = new ArrayList<A3KIndex>(UNIT_COUNT);
	/**
	 * 已经学过的那些units
	 */
	private TreeSet<A3KIndex> recitedUnits = new TreeSet<A3KIndex>();
	
	public A3KManager() {
		this.SortReciteOrder();
	}
	
	/**
	 * 从数据库中把recite_order和背诵了哪些units读进来
	 * @param context
	 */
	public void LoadFromDB(Context context) {
		A3KDBHelper helper = new A3KDBHelper(context);

		List<A3KIndex> indices = helper.LoadReciteOrder();
		if (indices.size() ==UNIT_COUNT ) {
			this.reciteOrder.clear();
			this.reciteOrder.addAll(helper.LoadReciteOrder());
		}
		
		this.recitedUnits.clear();
		this.recitedUnits.addAll(helper.LoadRecitedList());
	}
	
	/**
	 * 把recite_order和背诵了哪些units存到数据库里去
	 * @param context
	 */
	public void SaveIntoDB(Context context) {
		A3KDBHelper helper = new A3KDBHelper(context);
		helper.SaveReciteOrder(this.reciteOrder);
		helper.SaveRecitedList(this.recitedUnits);
	}
	
	/**
	 * 清空全部的背过的东西
	 */
	public void ClearRecited() {
		this.recitedUnits.clear();
	}
	
	/**
	 * 拿接下来要背的 @param count 个units
	 * @return 包含这么些Another3000Index的List
	 */
	public List<A3KIndex> GetNextUnits(int count) {
		if (count <0 || count > UNIT_COUNT) {
			throw new IllegalArgumentException("Invalid! 怎么可以拿这么多个units呢！ Caught by Andriy");
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
	 * @return 接下来将要背的全部那些units
	 */
	public List<A3KIndex> GetNextUnits() {
		return this.GetNextUnits(UNIT_COUNT);
	}
	
	/**
	 * @return 那些已经背过的units，一个装有Another3000Index的List
	 */
	public List<A3KIndex> GetRecitedUnits() {
		List<A3KIndex> recitedList = new ArrayList<A3KIndex>(recitedUnits.size());
		recitedList.addAll(recitedUnits);
		return recitedList;
	}
	
	/**
	 * 根据 @param recited，将 @param index 设置为背过或者没背过
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
		
			A3KIndex temp = reciteOrder.get(pos1);
			reciteOrder.set(pos1, reciteOrder.get(pos2));
			reciteOrder.set(pos2, temp);
		}
	}
	
	/**
	 * 让背诵的顺序恢复正序
	 */
	public void SortReciteOrder() {
		reciteOrder.clear();
		for (int i = 0; i < UNIT_COUNT; i++) {
			reciteOrder.add(new A3KIndex(i));
		}
	}
	
	/**
	 * 得到背诵的顺序
	 * @return 一个List，里头放着Pair<PDF_INDEX, UNIT_INDEX>
	 */
	public List<A3KIndex> GetReciteOrder() {
		return this.reciteOrder;
	}

}
