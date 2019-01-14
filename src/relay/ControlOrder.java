package relay;

import telecommunication.Receiver;
import telecommunication.code.Collector_Relay;
import telecommunication.code.Relay_Deliver;
import telecommunication.code.Relay_HQ;

public class ControlOrder {
	private Relay relay;

	public ControlOrder(Relay relay) {
		this.relay = relay;
	}

	void idtOrder(Receiver receiver, String receDetail) {
		if (receDetail.equals(""))
			return;

		String[] msg = receDetail.split("\\|", 0);

		try {
			switch (receiver) {
			case deliver:
				Relay_Deliver deliOdr = Relay_Deliver.valueOf(msg[0]);
				exeOrder(deliOdr, msg);
				break;

			case collector:
				Collector_Relay ColleOdr = Collector_Relay.valueOf(msg[0]);
				exeOrder(ColleOdr, msg);
				break;

			case hq:
				Relay_HQ hqOder = Relay_HQ.valueOf(msg[0]);
				exeOrder(hqOder, msg);
				break;

			default:
				break;
			}
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	void exeOrder(Collector_Relay odrNum, String[] msg) {
		switch (odrNum) {
		// "protocol|relay"
		case protocol:
			relay.judgeProtocol(Receiver.collector, msg[1]);
			break;

		// "sendFrglNum|20190113012832"
		case sendFrglNum:
			relay.saveFragileNum(msg[1]);
			break;

		case sendLock:
			relay.sendLock(Receiver.collector);
			break;

		case setLockFalse:
			relay.lock.setLock(false);
			break;

		default:
			break;
		}
	}

	void exeOrder(Relay_Deliver odrNum, String[] msg) {
		switch (odrNum) {
		// "protocol|relay"
		case protocol:
			relay.judgeProtocol(Receiver.deliver, msg[1]);
			break;

		case sendHasFrgl:
			relay.prepareFrgl();
			break;

		case syncFrglInfo:
			relay.sendFragile();
			break;

		case sendLock:
			relay.sendLock(Receiver.deliver);
			break;

		case setLockFalse:
			relay.lock.setLock(false);
			break;

		// "reportDeliFail|200012241224|absent"
		case reportDeliFail:
			relay.saveDeliFail(msg[1], msg[2]);
			break;

		// "reportDeliResult|200012241224|5"
		case reportDeliResult:
			relay.saveDeliComp(msg[1], msg[2]);
			break;

		default:
			break;
		}
	}

	void exeOrder(Relay_HQ odrNum, String[] m) {
		switch (odrNum) {
		// syncObs|200012241224|clientman|09064758475284632|2-2|houseman|090244867442749563|1-3
		case syncObs:
			relay.saveFrglInfo(m[1], m[2], m[4], m[5], m[7]);
			break;

		default:
			break;
		}
	}

	void setLock(Receiver receiver, Boolean b) {
		if (receiver == Receiver.collector)
			relay.lTimerColle.setLock(b);
		else if (receiver == Receiver.deliver)
			relay.lTimerDeli.setLock(b);
		else if (receiver == Receiver.hq)
			relay.lTimerHQ.setLock(b);
	}

}
