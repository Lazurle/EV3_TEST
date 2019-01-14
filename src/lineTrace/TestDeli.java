package lineTrace;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class TestDeli {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		LineTrace_Deli lDel = new LineTrace_Deli();
		LCD.drawString("Ready",0,1);
		while(Button.ENTER.isUp()) {

		}
		lDel.WaitingToCollision();
		lDel.ToRelay();
		lDel.RelayToCollision();
		lDel.ToHouse(2, 2);
		lDel.ReturnToRelay(2, 2);
		lDel.ToRelay();
		lDel.RelayToCollision();
		lDel.ReturnToWaiting();
		lDel.lt.close();
	}

}
