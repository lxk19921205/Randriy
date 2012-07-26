package edu.tongji.andriy.another3000;

/**
 * �洢��Щunits����Ϣ�����ĸ�pdf�У����Ǹ�pdf�ĵڼ���unit
 * @author Andriy
 *
 */
public class A3KIndex implements Comparable<A3KIndex>{

	public static final int UNITS_PER_PDF = 10;
	public static final int UNITS_IN_LAST_PDF = 8;
	public static final int PDF_COUNT = 31;

	/**
	 * ���ĸ�pdf�У���0��ʼ����
	 */
	private int pdfIndex = 0;
	/**
	 * ��pdf�еĵڼ���unit����0��ʼ����
	 */
	private int unitIndex = 0;
	
	public A3KIndex() {}
	
	public A3KIndex(int totalIndex) {
		this.SetIndex(totalIndex);
	}
	
	public A3KIndex(int pdf, int unit) {
		this.SetIndex(pdf, unit);
	}
	
	/**
	 * @return ��һ��pdf����0��ʼ����
	 */
	public int GetPDF() {
		return this.pdfIndex;
	}
	
	/**
	 * @return ��pdf�е���һ��unit����0��ʼ����
	 */
	public int GetUnit() {
		return this.unitIndex;
	}
	
	/**
	 * �������ݣ�ȫ����0��ʼ����
	 * @param pdf
	 * @param unit
	 */
	public void SetIndex(int pdf, int unit) {
		if (pdf < 0 || pdf >= PDF_COUNT) {
			throw new IllegalArgumentException("Invalid! No such PDF! Caught by Andriy");
		}
		this.pdfIndex = pdf;

		int units_limit = this.pdfIndex == PDF_COUNT-1 ? UNITS_IN_LAST_PDF : UNITS_PER_PDF;
		if (unit < 0 || unit >= units_limit) {
			throw new IllegalArgumentException("Invalid! No such UNIT in that PDF! Caught by Andriy");				
		}
		this.unitIndex = unit;
	}

	/**
	 * ������ȫ��units�е�index����0��ʼ����
	 * @param totalIndex
	 */
	public void SetIndex(int totalIndex) {
		int pdf = totalIndex / UNITS_PER_PDF;
		int unit = totalIndex % UNITS_PER_PDF;
		
		this.SetIndex(pdf, unit);
	}
	
	/**
	 * @return ������units�е�index
	 */
	public int GetTotalIndex() {
		return this.pdfIndex * UNITS_PER_PDF + this.unitIndex;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !this.getClass().equals(o.getClass())) {
			return false;
		}
		
		A3KIndex rhs = (A3KIndex) o;
		return this.pdfIndex == rhs.pdfIndex && this.unitIndex == rhs.unitIndex;
	}

	@Override
	public int compareTo(A3KIndex another) {
		if (another == null) {
			return 1;
		}
		
		return this.GetTotalIndex() - another.GetTotalIndex();
	}

}
