package relay;

import fragile.Fragile;
import fragile.deliRecord.ObsStats;
import telecommunication.Receiver;
import telecommunication.code.Collector_Relay;
import telecommunication.code.Relay_Deliver;
import telecommunication.code.Relay_HQ;

public class Relay {
	ControlOrder co = new ControlOrder(this);
	InfoEditor iEditor = new InfoEditor();
	ControlFragile cf = new ControlFragile();
	ControlTele ct = new ControlTele();
	Lock lock = new Lock();
	LockTimer lTimerColle = new LockTimer(false, 120);
	LockTimer lTimerDeli = new LockTimer(true);
	LockTimer lTimerHQ = new LockTimer(true);

	private Boolean judgeTele(Receiver receiver) {
		if (receiver == Receiver.collector)
			return judgeTeleColle();
		else if (receiver == Receiver.deliver)
			return judgeTeleDeli();
		else if (receiver == Receiver.hq)
			return judgeTeleHQ();
		else
			return false;
	}

	private Boolean judgeTeleColle() {
		return lTimerColle.getLock();
	}

	private Boolean judgeTeleDeli() {
		return lTimerDeli.getLock();
	}

	private Boolean judgeTeleHQ() {
		return lTimerHQ.getLock();
	}

	private void setLock(Receiver receiver, Boolean b) {
		if (receiver == Receiver.collector)
			lTimerColle.setLock(b);
		else if (receiver == Receiver.deliver)
			lTimerDeli.setLock(b);
		else if (receiver == Receiver.hq)
			lTimerHQ.setLock(b);
	}

	void execute() {
		while (true) {
			wait(Receiver.deliver);
			scanFragileList();
			wait(Receiver.collector);
			scanFragileList();
		}
	}

	private void wait(Receiver receiver) {
		if (!judgeTele(receiver))
			return;
		System.out.println("[W:" + receiver + "]");
		String receDetail = receive(receiver);
		if (!receDetail.equals(""))
			co.idtOrder(receiver, receDetail);
	}

	private Boolean send(Receiver receiver, String sendDetail) {
		if (!judgeTele(receiver))
			return false;
		return ct.send(receiver, sendDetail);
		// return ct.send(receiver, sendDetail, "debug");
	}

	private String receive(Receiver receiver) {
		return ct.receive(receiver);
		// return ct.receive(receiver, "debug");
	}

	private void scanFragileList() {
		cf.printList();
		sendArrive(cf.getNum());
		if (cf.hasNeedInfo())
			wait(Receiver.hq);
		sendStart(cf.getOnDeliver());
		cf.distribute();
		sendReturned(cf.getReturned());
	}

	private void sendArrive(Long num) {
		if (num == null)
			return;
		String sendDetail = iEditor.adjustInfo(Relay_HQ.setRelayArrive, num);
		if (send(Receiver.hq, sendDetail)) {
			cf.setNeedInfo(num);
			wait(Receiver.hq);
		}
	}

	private void sendStart(Long num) {
		if (num == null)
			return;
		String sendDetail = iEditor.adjustInfo(Relay_HQ.setStartDeli, num);
		if (send(Receiver.hq, sendDetail))
			cf.setReportedPassing(num);
	}

	void sendReturned(Long num) {
		if (num == null)
			return;
		ObsStats oStats = cf.getObs(num);
		if (oStats == ObsStats.absent) {
			sendAbsent(num);
		} else if (oStats == ObsStats.wrongHouse) {
			sendWrongHouse(num);
		} else {
			sendComplete(num);
		}
	}

	private void sendAbsent(Long num) {
		if (num == null)
			return;
		String sendDetail = iEditor.adjustInfo(Relay_HQ.setAbsent, num);
		if (send(Receiver.hq, sendDetail))
			cf.reWaiting(num);
	}

	private void sendWrongHouse(Long num) {
		if (num == null)
			return;
		String sendDetail = iEditor.adjustInfo(Relay_HQ.setWrgHouse, num);
		if (send(Receiver.hq, sendDetail))
			cf.setReported(num);
	}

	private void sendComplete(Long num) {
		if (num == null)
			return;
		String sendDetail = iEditor.adjustInfo(Relay_HQ.reportDeliComp, num, cf.getDeliTime(num));
		if (send(Receiver.hq, sendDetail))
			cf.setReported(num);
	}

	void saveFragileNum(String numStr) {
		Long num = Long.parseLong(numStr);
		cf.saveFragileNum(num);
	}

	void saveFrglInfo(String numStr, String cname, String caddr, String hname, String haddr) {
		Long num = Long.parseLong(numStr);
		cf.saveFragileInfo(num, cname, caddr, hname, haddr);
	}

	void sendLock(Receiver receiver) {
		String sendDetail = Collector_Relay.syncLock + "|" + lock.getLock();
		if (send(receiver, sendDetail))
			if (!lock.getLock())
				lock.setLock(true);
	}

	void prepareFrgl() {
		if (cf.hasDeliverableFragile()) {
			sendFragile();
		} else {
			sendNoFrgl();
		}
	}

	private void sendNoFrgl() {
		String sendDetail = Relay_Deliver.noFrgl.toString();
		send(Receiver.deliver, sendDetail);
	}

	void sendFragile() {
		Fragile tmp = cf.getDeliverableFragile();
		String sendDetail = iEditor.adjustInfo(tmp);
		if (send(Receiver.deliver, sendDetail))
			cf.setOnDeliver(tmp.getFrglNum());
	}

	void saveDeliFail(String numStr, String oStatsStr) {
		Long num = Long.parseLong(numStr);
		ObsStats oStats = ObsStats.valueOf(oStatsStr);
		cf.setFailed(num, oStats);
	}

	void saveDeliComp(String numStr, String timeStr) {
		Long num = Long.parseLong(numStr);
		Integer time = Integer.parseInt(timeStr);
		cf.setCompleted(num, time);
	}

	void judgeProtocol(Receiver receiver, String msg) {
		if (msg.equals("relay")) {
			sendCorrectConnection(receiver);
		} else {
			sendWrongConnection(receiver);
		}
	}

	private void sendCorrectConnection(Receiver receiver) {
		if (send(receiver, "protocol|true")) {
			setLock(receiver, true);
			wait(receiver);
		}
	}

	private void sendWrongConnection(Receiver receiver) {
		send(receiver, "protocol|false");
	}
}
