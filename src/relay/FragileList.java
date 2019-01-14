package relay;

import java.util.ArrayList;
import java.util.Iterator;

import fragile.Fragile;
import fragile.deliRecord.DeliStats;
import fragile.deliRecord.ObsStats;

public class FragileList {
	ArrayList<Fragile> fragiles = new ArrayList<Fragile>();

	void addFragile(Fragile fragile) {
		fragiles.add(fragile);
	}

	@SuppressWarnings("null")
	private int getIndex(Long num) {
		int i = 0;
		for (Iterator<Fragile> itr = fragiles.iterator(); itr.hasNext();) {
			Fragile fragile = itr.next();
			if (fragile.getFrglNum() == num) {
				return i;
			}
			i++;
		}
		return (Integer) null;
	}
	/*
	 * Fragile getFragile(Long num) { for (Iterator<Fragile> itr =
	 * fragiles.iterator(); itr.hasNext();) { Fragile fragile = itr.next(); if
	 * (fragile.getFrglNum() == num) { return fragile; } } return null; }
	 */

	Fragile getFragile(Long num) {
		try {
			int i = getIndex(num);
			return fragiles.get(i);
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	ObsStats getObs(Long num) {
		return getFragile(num).getObsStats();
	}

	void setStats(Long num, ObsStats oStats) {
		int i = getIndex(num);
		Fragile tmp = fragiles.get(i);
		tmp.setObsStats(oStats);
		fragiles.set(i, tmp);
	}

	void setStats(Long num, DeliStats dStats) {
		int i = getIndex(num);
		Fragile tmp = fragiles.get(i);
		tmp.setDeliStats(dStats);
		fragiles.set(i, tmp);
	}

	void setStats(Long num, DeliStats dStats, ObsStats obsStats) {
		int i = getIndex(num);
		Fragile tmp = fragiles.get(i);
		tmp.setDeliStats(dStats);
		tmp.setObsStats(obsStats);
		fragiles.set(i, tmp);
	}

	Boolean isNotEmpty() {
		return true ^ fragiles.isEmpty();
	}

	// デバッグ用
	void printFragile() {
		for (Fragile fragile : fragiles) {
			System.out.println("[" + fragile.getFrglNum() + "]");
		}
	}

}
