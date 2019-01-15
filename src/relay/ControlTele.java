package relay;

import java.io.IOException;
import java.util.Scanner;

import lejos.utility.Delay;
import telecommunication.Receiver;
import telecommunication.Telecommunication;

public class ControlTele {
	Telecommunication tele = new Telecommunication();

	Boolean send(Receiver receiver, String sendDetail) {
		int waitTime = 8;
		if (receiver == Receiver.hq)
			waitTime = 0;
		Boolean connection = false;
		for (int i = 0; i < 3; i++) {
			try {
				connection = tele.sendSignal(sendDetail, receiver, Receiver.relay, waitTime);
			} catch (IOException | RuntimeException e) {
				e.printStackTrace();
			}
			if (connection) {
				break;
			}
			Delay.msDelay(1500); // 時間をおく
		}
		return connection;
	}

	String receive(Receiver receiver) {
		int waitTime = 8;
		if (receiver == Receiver.hq)
			waitTime = 0;
		String receDetail = "";
		for (int i = 0; i < 3; i++) {
			try {
				receDetail = tele.receiveSignal(receiver, Receiver.relay, waitTime);
			} catch (IOException | RuntimeException e) {
				e.printStackTrace();
			}
			if (!receDetail.equals("")) {
				break;
			}
			Delay.msDelay(1500); // 時間をおく
		}
		return receDetail;
	}

	// デバッグ用送信メソッド
	Boolean send(Receiver receiver, String sendDetail, String debug) {
		System.out.print("[S:" + receiver + "]'" + sendDetail + "'");
		System.out.print(" push [f] or any -> ");
		Scanner scanner = new Scanner(System.in);
		String result = scanner.nextLine();
		return !result.equals("f"); // f:送信失敗
	}

	// デバッグ用受信メソッド
	String receive(Receiver receiver, String debug) {
		System.out.print("[R:" + receiver + "] -> ");
		Scanner scanner = new Scanner(System.in);
		String receDetail = scanner.nextLine();
		return receDetail;
	}
}
