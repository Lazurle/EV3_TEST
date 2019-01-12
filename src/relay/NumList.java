package relay;

import java.util.LinkedList;

public class NumList {
	private LinkedList<Long> numList = new LinkedList<Long>();

	// デバッグ用
	void printNum() {
		for (Long long1 : numList) {
			System.out.println(long1);
		}
	}

	int size() {
		return numList.size();
	}

	void addNum(Long num) {
		numList.add(num);
	}

	void addNum(String num) {
		this.addNum(Long.parseLong(num));
	}

	Long getNum() {
		return numList.peek();
	}

	Long popNum() {
		return numList.poll();
	}

	void deleteNum(Long num) {
		numList.remove(num);
	}

	void deleteNum(String num) {
		this.deleteNum(Long.parseLong(num));
	}
}
