package relay;

import java.io.IOException;
import java.util.Scanner;

import telecommunication.Receiver;
import telecommunication.Telecommunication;

public class ControlTele {
	Telecommunication tele = new Telecommunication();

	Boolean send(Receiver receiver, String sendDetail) {
		try {
			return tele.sendSignal(sendDetail, receiver, Receiver.relay, 10);
		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
		}
		return false;
	}

	String receive(Receiver receiver) {
		try {
			return tele.receiveSignal(receiver, Receiver.relay, 10);
		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
		}
		return "";
	}

	// デバッグ用送信メソッド
	Boolean send(Receiver receiver, String sendDetail, String debug) {
		System.out.println("send to " + receiver + " : " + sendDetail);
		return true;
	}

	// デバッグ用受信メソッド
	String receive(Receiver receiver, String debug) {
		System.out.println("connected to " + receiver);
		System.out.println("expected msg ->");
		Scanner scanner = new Scanner(System.in);
		String receDetail = scanner.nextLine();
		return receDetail;
	}
}
