package relay;

import fragile.deliRecord.ObsStats;
import telecommunication.Receiver;
import telecommunication.code.Collector_Relay;
import telecommunication.code.Relay_Deliver;
import telecommunication.code.Relay_HQ;

public class Relay {
	ControlOrder co = new ControlOrder(this);
	ControlInfo ci = new ControlInfo();
	ControlFragile cf = new ControlFragile();
	ControlTele ct = new ControlTele();
	ControlLock cl = new ControlLock();

	void execute() {
		while (true) {
			cl.printLock();
			wait(Receiver.deliver);
			cl.printLock();
			wait(Receiver.collector);
			scanFragileList();
		}
	}

	private void wait(Receiver receiver) {
		String receDetail = receive(receiver);
		if (receDetail == null)
			return;
		co.idtOrder(receiver, receDetail);
	}

	private Boolean send(Receiver receiver, String sendDetail) {
		return ct.send(receiver, sendDetail);
		// return ct.send(receiver, sendDetail, "debug");
	}

	private String receive(Receiver receiver) {
		return ct.receive(receiver);
		// return ct.receive(receiver, "debug");
	}

	// 本部へのタスクがあるかどうかを調べる
	void scanFragileList() {
		cf.printList();
		sendRelayArrive(cf.getDemandReport());
		sendDemandObs(cf.getDemandObs());
		sendStart(cf.getOnDeliver());
		cf.distribute();
		sendAbsent(cf.getAbsent());
		sendWrongHouse(cf.getWrong());
		sendComplete(cf.getCompleted());
	}

	private void sendRelayArrive(Long num) {
		if (num == null)
			return;
		String sendDetail = Relay_HQ.setRelayArrive + "|" + num;
		if (send(Receiver.hq, sendDetail))
			cf.saveArriveReported(num);
		;
	}

	private void sendDemandObs(Long num) {
		if (num == null)
			return;
		String sendDetail = Relay_HQ.getObs + "|" + num;
		if (send(Receiver.hq, sendDetail))
			wait(Receiver.hq);
	}

	private void sendStart(Long num) {
		if (num == null)
			return;
		String sendDetail = Relay_HQ.setStartDeli + "|" + num;
		if (send(Receiver.hq, sendDetail))
			cf.setReportedPassing(num);
	}

	private void sendAbsent(Long num) {
		if (num == null)
			return;
		String sendDetail = Relay_HQ.setAbsent + "|" + num;
		if (send(Receiver.hq, sendDetail))
			cf.reDemandObs(num);
	}

	private void sendWrongHouse(Long num) {
		if (num == null)
			return;
		String sendDetail = Relay_HQ.setWrgHouse + "|" + num;
		if (send(Receiver.hq, sendDetail))
			cf.setReportedWrong(num);
	}

	private void sendComplete(Long num) {
		if (num == null)
			return;
		String sendDetail = Relay_HQ.reportDeliComp.toString();
		sendDetail += "|" + num;
		sendDetail += "|" + cf.getFragileTime(num);
		if (send(Receiver.hq, sendDetail))
			cf.deleteCompleted();
	}

	void saveFragileNum(String[] msg) {
		Long num = Long.parseLong(msg[1]);
		cf.saveFragileNum(num);
	}

	// getObs|200012241224|clientman|09064758475284632|2-2|houseman|090244867442749563|1-3
	void saveFrglInfo(String[] msg) {
		cf.saveFragileInfo(msg[1], msg[2], msg[4], msg[5], msg[7]);
	}

	void syncLock(Receiver receiver) {
		String sendDetail = Collector_Relay.syncLock + "|" + cl.getLock();
		if (send(receiver, sendDetail))
			if (!cl.getLock())
				cl.setLock(true);
	}

	void sendHasFrgl() {
		String sendDetail = Relay_Deliver.syncHasFrgl + "|" + cf.hasDeliverableFragile();
		if (send(Receiver.deliver, sendDetail))
			if (cf.hasDeliverableFragile())
				cf.setWaitingPassing();
	}

	void sendFragile() {
		String sendDetail = Relay_Deliver.syncFrglInfo + "|";
		sendDetail += ci.adjustInfo(cf.getWaitingPassing());
		if (send(Receiver.deliver, sendDetail))
			cf.setOnDeliver();
	}

	// msg[]="reportDeliFail|200012241224|absent"
	void saveDeliFail(String[] msg) {
		Long num = Long.parseLong(msg[1]);
		ObsStats oStats = ObsStats.valueOf(msg[2]);
		cf.setFailed(num, oStats);
	}

	// msg[]="reportDeliResult|200012241224|5"
	void saveDeliComp(String[] msg) {
		Long num = Long.parseLong(msg[1]);
		Integer time = Integer.parseInt(msg[2]);
		cf.setCompleted(num, time);
	}

	// msg[]="protocol|relay"
	void judgeProtocol(Receiver receiver, String[] msg) {
		if (msg[1].equals("relay")) {
			sendCorrectConnection(receiver);
		} else {
			sendWrongConnection(receiver);
		}
	}

	private void sendCorrectConnection(Receiver receiver) {
		if (send(receiver, "protocol|true"))
			ct.receive(receiver, "debug");
	}

	private void sendWrongConnection(Receiver receiver) {
		send(receiver, "protocol|false");
	}
}
