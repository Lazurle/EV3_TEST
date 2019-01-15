package fragile.deliRecord;

import java.util.Calendar;

public class DeliRecord {

	private DeliStats deliStats = null; //配達状況

	private ObsStats obsStats = null; //障害状況

	private Calendar relayArriveTime = Calendar.getInstance(); //中継所到着時間

	private Calendar receptionTime = Calendar.getInstance(); //受付時間

	private Calendar receiveTime = Calendar.getInstance(); //受取時間

	private Calendar sendTime = Calendar.getInstance(); //発送時間

	private Calendar deliFinishTime = Calendar.getInstance(); //配達完了時間

	private Calendar deliStartTime = Calendar.getInstance(); //配達開始時間

	////////////////////////////////////setter//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @配達状況 not 存在しない配達状況
	 */
	/*配達状況を更新する
	 * deliStats:配達状況
	 */
	public void setDeliStats(DeliStats deliStats) {
		this.deliStats = deliStats;
	}

	/**
	 * @障害状況 not 存在しない障害状況
	 */
	/*障害状況を更新する
	 * obsStats:障害状況
	 */
	public void setObsStats(ObsStats obsStats) {
		this.obsStats = obsStats;
	}

	/*時間を記録する.時間の形式は文字列
	 * setTime:記録するフィールド名
	 * time:記録する値
	 */
	public void saveTime(String setTime, String time) throws IllegalArgumentException {
		//指定されたフィールド名でフィールドを更新する
		switch (setTime) {
		case "relayArriveTime":
			this.setTime(relayArriveTime, time);
			break;
		case "receptionTime":
			this.setTime(receptionTime, time);
			break;
		case "receiveTime":
			this.setTime(receiveTime, time);
			break;
		case "sendTime":
			this.setTime(sendTime, time);
			break;
		case "deliFinishTime":
			this.setTime(deliFinishTime, time);
			break;
		case "deliStartTime":
			this.setTime(deliStartTime, time);
			break;
		default:
			//フィールド名が存在しない場合
			throw new IllegalArgumentException("指定された時間フィールドが存在しません");
		}
	}

	/*時間を記録する.時間の形式はカレンダー型
	 * setTime:記録するフィールド名
	 * time:記録する値
	 */
	public void saveTime(String setTime, Calendar time) throws IllegalArgumentException {
		//指定されたフィールド名でフィールドを更新する
		switch (setTime) {
		case "relayArriveTime":
			this.relayArriveTime = time;
			break;
		case "receptionTime":
			this.receptionTime = time;
			break;
		case "receiveTime":
			this.receiveTime = time;
			break;
		case "sendTime":
			this.sendTime = time;
			break;
		case "deliFinishTime":
			this.deliFinishTime = time;
			break;
		case "deliStartTime":
			this.deliStartTime = time;
			break;
		default:
			//フィールド名が存在しない場合
			throw new IllegalArgumentException("指定された時間フィールドが存在しません");
		}
	}

	/*指定されたフィールドの時間を更新するメソッド
	 * c:更新するフィールド
	 * time:年月日時分の情報
	 */
	private void setTime(Calendar c, String time) throws IllegalArgumentException {
		String[] date;
		//文字列を区切り文字で分割
		date = time.split("\\|", 0);
		if (date.length != 5) {
			throw new IllegalArgumentException("時間のフォーマットが不適切です");
		}
		//指定されたフィールドに時間を格納
		c.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]),
				Integer.parseInt(date[3]), Integer.parseInt(date[4]));
	}

	////////////////////////////////////getter//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//配達状況を返す
	public DeliStats getDeliStats() {
		return this.deliStats;
	}

	//障害状況を返す
	public ObsStats getObsStats() {
		return this.obsStats;
	}

	/*配達記録フィールドの各時間を返す
	 * getTime:フィールド名
	 */

	public Calendar getCalTime(String getTime) throws IllegalArgumentException {
		//指定されたフィールド名の時間を返す
		switch (getTime) {
		case "relayArriveTime":
			return this.relayArriveTime;
		case "receptionTime":
			return this.receptionTime;
		case "receiveTime":
			return this.receiveTime;
		case "sendTime":
			return this.sendTime;
		case "deliFinishTime":
			return this.deliFinishTime;
		case "deliStartTime":
			return this.deliStartTime;
		default:
			//フィールド名が存在しない場合
			throw new IllegalArgumentException("指定された時間フィールドが存在しません");
		}
	}

	public String getStrTime(String getTime) throws IllegalArgumentException {
		switch (getTime) {
		case "relayArriveTime":
			return this.makeTimeStr(this.relayArriveTime);
		case "receptionTime":
			return this.makeTimeStr(this.receptionTime);
		case "receiveTime":
			return this.makeTimeStr(this.receiveTime);
		case "sendTime":
			return this.makeTimeStr(this.sendTime);
		case "deliFinishTime":
			return this.makeTimeStr(this.deliFinishTime);
		case "deliStartTime":
			return this.makeTimeStr(this.deliStartTime);
		default:
			//フィールド名が存在しない場合
			throw new IllegalArgumentException("指定された時間フィールドが存在しません");
		}
	}

	private String makeTimeStr(Calendar time) {

		String str = "";
		str = str.concat(String.valueOf(time.get(Calendar.YEAR)));

		if (time.get(Calendar.MONTH) < 9) {
			str = str.concat("0");
		}
		str = str.concat(String.valueOf(time.get(Calendar.MONTH) + 1));
		if (time.get(Calendar.DATE) < 10) {
			str = str.concat("0");
		}
		str = str.concat(String.valueOf(time.get(Calendar.DATE)));
		if (time.get(Calendar.HOUR_OF_DAY) < 10) {
			str = str.concat("0");
		}
		str = str.concat(String.valueOf(time.get(Calendar.HOUR_OF_DAY)));
		if (time.get(Calendar.MINUTE) < 10) {
			str = str.concat("0");
		}
		str = str.concat(String.valueOf(time.get(Calendar.MINUTE)));
		return str;
	}
}
