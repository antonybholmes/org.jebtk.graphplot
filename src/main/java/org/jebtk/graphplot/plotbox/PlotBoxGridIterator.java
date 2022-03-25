package org.jebtk.graphplot.plotbox;

import java.util.Iterator;

/**
 * Iterate over a 2D list of objects, allowing for cells to be null. Iterates
 * over the whole 2d array row centrically to find the first non null value
 * which is returned. Repeated calls to next advance from the current position
 * until the next non null entry is found
 * 
 * @author antony
 *
 * @param <T>
 */
public class PlotBoxGridIterator<T> implements Iterator<T> {

	private T[][] mData;

	private int mC = -1;

	private int mLastIndex;

	public PlotBoxGridIterator(T[][] data) {
		mData = data;

		mLastIndex = mData.length * mData[0].length - 1;
	}

	@Override
	public boolean hasNext() {
		if (mC >= mLastIndex) {
			return false;
		}

		int c = 0; // mC + 1;

		// int i = c / mData[0].length * mData[0].length + c % mData[0].length;

		for (int i = 0; i < mData.length; ++i) {
			for (int j = 0; j < mData[0].length; ++j) {
				if (c > mC) {
					return mData[i][j] != null;
				}

				++c;
			}
		}

		return false;
	}

	@Override
	public T next() {
		T ret = null;

		int c = 0;

		for (int i = 0; i < mData.length; ++i) {
			for (int j = 0; j < mData[0].length; ++j) {
				if (c > mC) {
					T v = mData[i][j];

					mC = c;

					if (v != null) {
						ret = v;
						break;
					}

				}

				++c;
			}

			if (ret != null) {
				break;
			}
		}

		return ret;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
