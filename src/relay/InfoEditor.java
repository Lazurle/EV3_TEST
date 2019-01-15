package relay;

import fragile.Fragile;
import telecommunication.code.Relay_Deliver;
import telecommunication.code.Relay_HQ;

public class InfoEditor {
	String adjustInfo(Relay_Deliver order) {
		return order.toString();
	}

	String adjustInfo(Relay_HQ order, Long num) {
		return adjustInfo(order.toString(), num.toString());
	}

	String adjustInfo(Relay_HQ order, Long num, Integer time) {
		return adjustInfo(order.toString(), num.toString(), time.toString());
	}

	String adjustInfo(Fragile send) {
		String[] tmpC = send.getClientInfo();
		String[] tmpH = send.getHouseInfo();
		String order = Relay_Deliver.syncFrglInfo.toString();
		String num = String.valueOf(send.getFrglNum());
		return adjustInfo(order, num, tmpC[0], tmpC[2], tmpH[0], tmpH[2]);

	}

	private String adjustInfo(String order, String num) {
		String[] tmp = { order, num };
		return combineInfo(tmp);
	}

	private String adjustInfo(String order, String num, String time) {
		String[] tmp = { order, num, time };
		return combineInfo(tmp);
	}

	private String adjustInfo(String order, String num, String cname, String caddr, String hname, String haddr) {
		String[] tmp = { order, num, cname, caddr, hname, haddr };
		return combineInfo(tmp);
	}

	private String combineInfo(String[] strings) {
		String combined = "";
		int tail = strings.length - 1;
		for (int i = 0; i < tail; i++)
			combined += strings[i] + "|";
		return combined + strings[tail];
	}

}
