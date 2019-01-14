package relay;

import java.util.ArrayList;

import fragile.Fragile;
import telecommunication.code.Relay_Deliver;
import telecommunication.code.Relay_HQ;

public class InfoEditor {
	String adjustInfo(Fragile send) {
		ArrayList<String> tmp = new ArrayList<String>();
		String[] tmpClient = send.getClientInfo();
		String[] tmpHouse = send.getHouseInfo();

		tmp.add(Relay_Deliver.syncFrglInfo.toString());
		tmp.add(String.valueOf(send.getFrglNum()));
		tmp.add(tmpClient[0]);
		tmp.add(tmpClient[2]);
		tmp.add(tmpHouse[0]);
		tmp.add(tmpHouse[2]);

		return combineInfo(tmp.toArray(new String[tmp.size()]));
	}

	private String adjustInfo(String order, String num) {
		String[] tmp = { order, num };
		return combineInfo(tmp);
	}

	String adjustInfo(Relay_HQ order, Long num) {
		return adjustInfo(order.toString(), num.toString());
	}

	private String adjustInfo(String order, String num, String time) {
		String[] tmp = { order, num, time };
		return combineInfo(tmp);
	}

	String adjustInfo(Relay_HQ order, Long num, Integer time) {
		return adjustInfo(order.toString(), num.toString(), time.toString());
	}

	private String combineInfo(String[] strings) {
		String combined = "";
		int tail = strings.length - 1;
		for (int i = 0; i < tail; i++)
			combined += strings[i] + "|";
		return combined + strings[tail];
	}

}
