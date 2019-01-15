package relay;

public class LockTimer {
	Lock lock = new Lock();
	private long start = 0;
	private int limit = 0;

	public LockTimer(Boolean b) {
		setStart();
		setLock(b);
	}

	public LockTimer(Boolean b, int time) {
		setLock(b);
		setLimit(time);
	}

	public void setLock(Boolean b) {
		lock.setLock(b);
	}

	public Boolean getLock() {
		return lock.getLock() && isNoTimeLimit();
	}

	// このメソッドを呼び出した時点から、time(s)侵入できない
	public void setLimit(int time) {
		setStart();
		limit = time;
	}

	private void setStart() {
		start = System.currentTimeMillis();
	}

	private Boolean isNoTimeLimit() {
		int dif = limit - getTimeSec();
		if (dif > 0) {
			return false;
		} else
			return true;
	}

	// 経過時間を取得
	private int getTimeSec() {
		long end = System.currentTimeMillis();
		long dif = end - start;
		int res = (int) (dif / 1000);
		return res;
	}

}
