package org.jebtk.graphplot.figure.props;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.NameGetter;
import org.jebtk.core.event.ChangeListeners;

public class Tick extends ChangeListeners implements Comparable<Tick>, NameGetter {

	private static final long serialVersionUID = 1L;
	private String mName;
	private double mValue;

	public Tick(double v) {
		this(v, Double.toString(v));
	}

	public Tick(double v, String name) {
		mValue = v;
		mName = name;
	}

	@Override
	public int compareTo(Tick t) {
		if (mValue > t.mValue) {
			return 1;
		} else if (mValue < t.mValue) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public String getName() {
		return mName;
	}

	public double getValue() {
		return mValue;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Tick) {
			Tick t = (Tick) o;

			return t.getName().equals(mName) && compareTo(t) == 0;
		} else {
			return false;
		}
	}

	public static List<Tick> toTicks(List<Double> values) {
		List<Tick> ret = new ArrayList<Tick>(values.size());

		DecimalFormat format = new DecimalFormat("#,###.##");

		for (double v : values) {
			ret.add(new Tick(v, format.format(v)));
		}

		return ret;
	}

	public void setName(String name) {
		mName = name;

		fireChanged();
	}

	public static List<String> toString(List<Tick> ticks) {
		List<String> ret = new ArrayList<String>(ticks.size());

		for (Tick t : ticks) {
			ret.add(t.getName());
		}

		return ret;
	}
}
