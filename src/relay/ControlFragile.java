package relay;

import java.util.HashMap;

import fragile.ClientInfo;
import fragile.Fragile;
import fragile.deliRecord.DeliStats;
import fragile.deliRecord.ObsStats;

public class ControlFragile {
	NumList demandReport = new NumList();
	FragileList demandObs = new FragileList();
	FragileList waitingDeliver = new FragileList();
	FragileList waitingPassing = new FragileList();
	FragileList onDeliver = new FragileList();
	FragileList reportedPassing = new FragileList();
	FragileList failed = new FragileList();
	FragileList absent = new FragileList();
	FragileList wrong = new FragileList();
	FragileList completed = new FragileList();
	FragileList reported = new FragileList();
	HashMap<Long, Integer> hMap = new HashMap<>();

	void saveFragileNum(Long num) {
		demandReport.addNum(num);
	}

	void saveArriveReported(Long num) {
		demandReport.deleteNum(num);
		String arrivetime = "2000|12|24|12|24"; // いらない
		demandObs.addFragile(new Fragile(arrivetime, null, num, DeliStats.awaiting, ObsStats.none));
	}

	void saveFragileInfo(Fragile tmp) {
		demandObs.getFragile(tmp.getFrglNum());
		waitingDeliver.addFragile(tmp);
	}

	void saveFragileInfo(String snum, String cname, String caddr, String hname, String haddr) {
		Long num = Long.parseLong(snum);
		ClientInfo cInfo = new ClientInfo();
		cInfo.setClientInfo(cname, null, caddr);
		cInfo.setHouseInfo(hname, null, haddr);
		Fragile tmp = demandObs.getFragile(num);
		tmp.setAllInfo(cInfo);
		waitingDeliver.addFragile(tmp);
	}

	void setWaitingPassing() {
		Fragile tmp = waitingDeliver.takeFragile();
		waitingPassing.addFragile(tmp);
	}

	void setOnDeliver() {
		Fragile tmp = waitingPassing.takeFragile();
		tmp.setDeliStats(DeliStats.delivering);
		onDeliver.addFragile(tmp);
	}

	void setReportedPassing(Long num) {
		Fragile tmp = onDeliver.getFragile(num);
		reportedPassing.addFragile(tmp);
	}

	void setFailed(Long num, ObsStats oStats) {
		Fragile tmp = reportedPassing.takeFragile();
		tmp.setDeliStats(DeliStats.delivered);
		tmp.setObsStats(oStats);
		failed.addFragile(tmp);
	}

	void setCompleted(Long num, Integer minTime) {
		Fragile tmp = reportedPassing.getFragile(num);
		hMap.put(num, minTime);
		completed.addFragile(tmp);
	}

	void reDemandObs(Long num) {
		Fragile tmp = absent.getFragile(num);
		tmp.setDeliStats(DeliStats.awaiting);
		demandObs.addFragile(tmp);
	}

	void setReportedWrong(Long num) {
		Fragile tmp = wrong.getFragile(num);
		reported.addFragile(tmp);
	}

	Long getDemandReport() {
		return demandReport.getNum();
	}

	Long getDemandObs() {
		Fragile tmp = demandObs.getFragile();
		if (tmp == null)
			return null;
		return tmp.getFrglNum();
	}

	Long getWaitingDeliver() {
		Fragile tmp = waitingDeliver.getFragile();
		if (tmp == null)
			return null;
		return tmp.getFrglNum();
	}

	Boolean hasDeliverableFragile() {
		return waitingDeliver.isNotEmpty();
	}

	Fragile getWaitingPassing() {
		return waitingPassing.getFragile();

	}

	Long getOnDeliver() {
		Fragile tmp = onDeliver.getFragile();
		if (tmp == null)
			return null;
		return tmp.getFrglNum();
	}

	Long getAbsent() {
		Fragile tmp = absent.getFragile();
		if (tmp == null)
			return null;
		return tmp.getFrglNum();
	}

	Long getWrong() {
		Fragile tmp = wrong.getFragile();
		if (tmp == null)
			return null;
		return tmp.getFrglNum();
	}

	Long getCompleted() {
		Fragile tmp = completed.getFragile();
		if (tmp == null)
			return null;
		return tmp.getFrglNum();
	}

	Integer getFragileTime(Long num) {
		return hMap.get(num);
	}

	void deleteCompleted() {
		completed.takeFragile();
	}

	Fragile getFailed() {
		return failed.takeFragile();
	}

	void distribute() {
		Fragile tmp = null;
		while ((tmp = getFailed()) != null) {
			ObsStats oStats = tmp.getObsStats();

			if (oStats == ObsStats.absent) {
				absent.addFragile(tmp);
			} else if (oStats == ObsStats.wrongHouse) {
				wrong.addFragile(tmp);
			}
		}
	}

	// デバッグ用
	void printList() {
		System.out.println("demandReport:" + demandReport.size());
		demandReport.printNum();

		System.out.println("demandObs:" + demandObs.size());
		demandObs.printFragile();

		System.out.println("waitingDeliver:" + waitingDeliver.size());
		waitingDeliver.printFragile();

		System.out.println("waitingPassing:" + waitingPassing.size());
		waitingPassing.printFragile();

		System.out.println("onDeliver:" + onDeliver.size());
		onDeliver.printFragile();

		System.out.println("reportedPassing:" + reportedPassing.size());
		reportedPassing.printFragile();

		System.out.println("failed:" + failed.size());
		failed.printFragile();

		System.out.println("absent:" + absent.size());
		absent.printFragile();

		System.out.println("wrong:" + wrong.size());
		wrong.printFragile();

		System.out.println("completed:" + completed.size());
		completed.printFragile();

		System.out.println("reported:" + reported.size());
		reported.printFragile();
	}

}
