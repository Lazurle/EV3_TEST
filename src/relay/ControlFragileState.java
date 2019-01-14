package relay;

import java.util.HashMap;
import java.util.Map.Entry;

public class ControlFragileState {
	HashMap<Long, FragileState> map = new HashMap<>();

	public void setState(Long num, FragileState state) {
		map.put(num, state);
	}

	public Long findFragile(FragileState state) {
		for (Entry<Long, FragileState> entry : map.entrySet()) {
			if (entry.getValue().equals(state))
				return entry.getKey();
		}
		return null;
	}

	public Boolean hasDeliverableFragile() {
		if (findFragile(FragileState.DELIVERABLE) == null)
			return false;
		else
			return true;
	}

	public void printState() {
		for (Entry<Long, FragileState> entry : map.entrySet()) {
			System.out.print(entry.getKey() + ":");
			System.out.println(entry.getValue());
		}
	}
}
