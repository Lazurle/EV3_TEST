package relay;

import java.util.Iterator;
import java.util.LinkedList;

import fragile.Fragile;

public class FragileList {
	LinkedList<Fragile> fragiles = new LinkedList<>();

	void addFragile(Fragile fragile) {
		fragiles.add(fragile);
	}

	Fragile getFragile() {
		return fragiles.peek();
	}

	Fragile takeFragile() {
		return fragiles.poll();
	}

	Fragile getFragile(Long num) {
		for (Iterator<Fragile> itr = fragiles.iterator(); itr.hasNext();) {
			Fragile fragile = itr.next();
			if (fragile.getFrglNum() == num) {
				fragiles.remove(fragile);
				return fragile;
			}
		}
		return null;
	}

	Fragile getFragile(String num) {
		return this.getFragile(Long.parseLong(num));
	}

	Boolean isNotEmpty() {
		return true ^ fragiles.isEmpty();
	}

	// デバッグ用
	void printFragile() {
		for (Fragile fragile : fragiles) {
			System.out.println(fragile.getFrglNum());
			System.out.println(fragile.getStrTime("relayArriveTime"));
		}
	}

	int size() {
		return fragiles.size();
	}

}
