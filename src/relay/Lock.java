package relay;

public class Lock {
	private Boolean lock = false;

	Boolean getLock() {
		return this.lock;
	}

	void setLock(Boolean lock) {
		this.lock = lock;
	}

	// デバッグ用
	void printLock() {
		System.out.println("lock:" + getLock());
	}

}
