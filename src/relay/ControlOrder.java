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

	}

	void exeOrder(Collector_Relay odrNum, String[] msg) {
		switch (odrNum) {
		case protocol:
			relay.judgeProtocol(Receiver.collector, msg);
			break;

		case sendFrglNum:
			relay.saveFragileNum(msg);
			break;

		case syncLock: // sendLock
			relay.syncLock(Receiver.collector);
			break;

		case setLockFalse:
			relay.cl.setLock(false);
			break;

		default:
			break;
		}
	}

	void exeOrder(Relay_Deliver odrNum, String[] msg) {
		switch (odrNum) {
		case protocol:
			relay.judgeProtocol(Receiver.deliver, msg);
			break;

		case sendHasFrgl:
			relay.sendHasFrgl();
			break;

		case syncFrglInfo:
			relay.sendFragile();
			break;

		case sendLock:
			relay.syncLock(Receiver.deliver);
			break;

		case setLockFalse:
			relay.cl.setLock(false);
			break;

		case backWaitingArea:
			relay.cl.setLock(false);
			break;

		case goHouse:
			relay.cl.setLock(false);
			break;

		case reportDeliFail:
			relay.saveDeliFail(msg);
			break;

		case reportDeliResult:
			relay.saveDeliComp(msg);
			break;

		default:
			break;
		}
	}

	void exeOrder(Relay_HQ odrNum, String[] msg) {
		switch (odrNum) {
		case getObs:
			relay.saveFrglInfo(msg);
			break;

		default:
			break;
		}
	}

}
