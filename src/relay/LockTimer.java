package relay;

public class LockTimer {
	Lock lock = new Lock();
	private long start = System.currentTimeMillis();
	private int timer = 0;

	public LockTimer(Boolean b) {
		setLock(b);
	}

	public LockTimer(Boolean b, int time) {
		setLock(b);
		setTimer(time);
	}

	public void setLock(Boolean b) {
		lock.setLock(b);
	}

	public Boolean getLock() {
		return lock.getLock() && timeJudge();
	}

	public void setStart() {
		start = System.currentTimeMillis();
	}

	public int getTimeSec() {
		long end = System.currentTimeMillis();
		long dif = end - start;
		int res = (int) (dif * 1000);
		return res;
	}

	public void setTimer(int time) {
		timer = time;
	}

	private Boolean timeJudge() {
		int dif = timer - getTimeSec();
		if (dif > 0)
			return false;
		else
			return true;
	}

}
