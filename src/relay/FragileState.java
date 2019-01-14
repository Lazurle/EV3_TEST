package relay;

public enum FragileState {
	NEED_SEND_ARRIVED, // 中継所到着報告待ち
	NEEDINFO, // 中継所到着報告と依頼情報要求の受信待ち
	DELIVERABLE, // 配達ロボットに引き渡し可能
	ONDELVER, // 配達中（配達ロボットが依頼を遂行中）
	ONDELIVER_REPORTED, // 本部に配達開始を報告済み
	RETURNED, //
	REPORTED, // 配達完了or宛先間違い
}
