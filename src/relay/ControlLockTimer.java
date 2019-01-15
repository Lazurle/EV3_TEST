package relay;

import telecommunication.Receiver;

public class ControlLockTimer {
	LockTimer lTimerColle = new LockTimer(true, 30); // 収集ロボットと受付所の通信が終わるまで待機
	LockTimer lTimerDeli = new LockTimer(false);
	LockTimer lTimerHQ = new LockTimer(true, 0);

	Boolean getLock(Receiver receiver) {
		if (receiver == Receiver.collector)
			return lTimerColle.getLock();
		else if (receiver == Receiver.deliver)
			return lTimerDeli.getLock();
		else if (receiver == Receiver.hq)
			return lTimerHQ.getLock();
		else
			return false;
	}

	void setLock(Receiver receiver, Boolean b) {
		if (receiver == Receiver.collector)
			lTimerColle.setLock(b);
		else if (receiver == Receiver.deliver)
			lTimerDeli.setLock(b);
		else if (receiver == Receiver.hq)
			lTimerHQ.setLock(b);
	}

	void setLimit(Receiver receiver, int time) {
		if (receiver == Receiver.collector)
			lTimerColle.setLimit(time);
		else if (receiver == Receiver.deliver)
			lTimerDeli.setLimit(time);
		else if (receiver == Receiver.hq)
			lTimerHQ.setLimit(time);
	}

}
