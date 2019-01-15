package relay;

import java.util.HashMap;

import fragile.ClientInfo;
import fragile.Fragile;
import fragile.deliRecord.DeliStats;
import fragile.deliRecord.ObsStats;

public class ControlFragile {
	NumList nList = new NumList();
	FragileList fList = new FragileList();
	HashMap<Long, Integer> hMap = new HashMap<>();
	ControlFragileState cfs = new ControlFragileState();

	public ControlFragile() {
	}

	// テスト用コンストラクタ
	public ControlFragile(String debug) {
		Long num = 12345678L;
		String cname = "asdf";
		String caddr = "1-1";
		String hname = "zxcv";
		String haddr = "2-2";
		// ObsStats oStats = ObsStats.absent;
		ObsStats oStats = ObsStats.wrongHouse;

		saveFragileNum(num);
		setNeedInfo(num);
		saveFragileInfo(num, cname, caddr, hname, haddr);
		setOnDeliver(num);
		setReportedPassing(num);
		setFailed(num, oStats);
	}

	void saveFragileNum(Long num) {
		nList.addNum(num);
		cfs.setState(num, FragileState.NEED_SEND_ARRIVED);
	}

	void setNeedInfo(Long num) {
		nList.deleteNum(num);
		cfs.setState(num, FragileState.NEEDINFO);
	}

	void saveFragileInfo(Long num, String cname, String caddr, String hname, String haddr) {
		ClientInfo cInfo = new ClientInfo();
		cInfo.setClientInfo(cname, null, caddr);
		cInfo.setHouseInfo(hname, null, haddr);
		nList.popNum();
		fList.addFragile(new Fragile(cInfo, num, DeliStats.awaiting, ObsStats.none));
		cfs.setState(num, FragileState.DELIVERABLE);
	}

	void setOnDeliver(Long num) {
		fList.setStats(num, DeliStats.delivering);
		cfs.setState(num, FragileState.ONDELIVER);
	}

	void setReportedPassing(Long num) {
		cfs.setState(num, FragileState.ONDELIVER_REPORTED);
	}

	void setFailed(Long num, ObsStats oStats) {
		fList.setStats(num, DeliStats.delivered, oStats);
		cfs.setState(num, FragileState.RETURNED);
	}

	void setCompleted(Long num, Integer minTime) {
		fList.setStats(num, DeliStats.delivered);
		hMap.put(num, minTime);
		cfs.setState(num, FragileState.RETURNED);
	}

	void reWaiting(Long num) {
		fList.setStats(num, DeliStats.awaiting, ObsStats.none);
		cfs.setState(num, FragileState.DELIVERABLE);
	}

	void setReported(Long num) {
		cfs.setState(num, FragileState.REPORTED);
		distribute(num);
	}

	Long getNum() {
		return nList.getNum();
	}

	Long getNeedInfo() {
		return cfs.findFragile(FragileState.NEEDINFO);
	}

	Boolean hasNeedInfo() {
		if (getNeedInfo() == null)
			return false;
		else
			return true;
	}

	Long getDeliverable() {
		return cfs.findFragile(FragileState.DELIVERABLE);
	}

	Fragile getDeliverableFragile() {
		Long num = getDeliverable();
		return fList.getFragile(num);
	}

	Boolean hasDeliverableFragile() {
		return cfs.hasDeliverableFragile();
	}

	Long getOnDeliver() {
		return cfs.findFragile(FragileState.ONDELIVER);
	}

	Long getReturned() {
		return cfs.findFragile(FragileState.RETURNED);
	}

	Long getReported() {
		return cfs.findFragile(FragileState.REPORTED);
	}

	ObsStats getObs(Long num) {
		return fList.getObs(num);
	}

	Integer getDeliTime(Long num) {
		return hMap.get(num);
	}

	private void distribute(Long num) {
		if (num != null) {
			ObsStats oStats = getObs(num);
			if (oStats == ObsStats.absent) {
				cfs.setState(num, FragileState.DELIVERABLE);
			} else if (oStats == ObsStats.wrongHouse) {
				cfs.setState(num, FragileState.REPORTED);
			}
		}
	}

	// デバッグ用
	void printList() {
		cfs.printState();
	}
}
