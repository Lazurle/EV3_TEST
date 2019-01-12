package relay;

import fragile.ClientInfo;
import fragile.Fragile;
import fragile.deliRecord.DeliStats;
import fragile.deliRecord.ObsStats;
import telecommunication.code.Relay_Deliver;

public class ControlInfo {
	String adjustInfo(Fragile send) {
		String sendDetail = Relay_Deliver.syncFrglInfo.toString();
		Long num = send.getFrglNum();
		String[] tmpClient = send.getClientInfo();
		String[] tmpHouse = send.getHouseInfo();

		sendDetail += "|" + num;
		sendDetail += "|" + tmpClient[0] + "|" + tmpClient[2];
		sendDetail += "|" + tmpHouse[0] + "|" + tmpHouse[2];

		return sendDetail;
	}

	Fragile makeFrgl(String[] msg) {
		ClientInfo ci = new ClientInfo();
		long frglNum = Long.parseLong(msg[1]);
		ci.setClientInfo(msg[2], msg[3], msg[4]);
		ci.setHouseInfo(msg[5], msg[6], msg[7]);
		return new Fragile("1234|12|12|15|15", ci, frglNum, DeliStats.awaiting, ObsStats.none);
	}
}
