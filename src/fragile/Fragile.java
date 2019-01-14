package fragile;

import java.util.Calendar;

import fragile.deliRecord.DeliRecord;
import fragile.deliRecord.DeliStats;
import fragile.deliRecord.ObsStats;

public class Fragile {

	private long frglNum = 0; //荷物番号

	private ClientInfo clientInfo = new ClientInfo(); //依頼情報

	private DeliRecord deliRecord = new DeliRecord(); //配達記録


	////////////////////////////////////コンストラクタ//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @受付時間 not 負の値
	 * @依頼情報 not null
	 * @荷物番号 not 負の値
	 * @配達状況 not 存在しない配達状況
	 * @障害状況 not 存在しない障害状況
	 */

	/*受付所から呼ぶコンストラクタ
	 * receptionTime:受付時間
	 * clientInfo:依頼情報
	 * frglNum:荷物番号
	 * deliStats:配達状況
	 * obsStats:障害状況
	 */
	public Fragile(Calendar receptionTime, ClientInfo clientInfo, long frglNum, DeliStats deliStats, ObsStats obsStats) {
		this.deliRecord.saveTime("receptionTime",receptionTime);
		try{
			this.clientInfo = clientInfo;
		}catch(NullPointerException e){
			System.out.println("依頼情報が不適切です");
		}
		this.setFrglNum(frglNum);
		this.deliRecord.setDeliStats(deliStats);
		this.deliRecord.setObsStats(obsStats);
	}

	/*中継所から呼ぶコンストラクタ
	 * clientInfo:依頼情報
	 * frglNum:荷物番号
	 * deliStats:配達状況
	 * obsStats:障害状況
	 */
	public Fragile(ClientInfo clientInfo, long frglNum, DeliStats deliStats, ObsStats obsStats) {
		this.frglNum = frglNum;
		this.clientInfo = clientInfo;
		this.deliRecord.setDeliStats(deliStats);
		this.deliRecord.setObsStats(obsStats);
	}

	//受取人宅から呼ぶコンストラクタ
	public Fragile(){
	}

	/*本部から呼ぶコンストラクタ
	 * frglNum:荷物番号
	 * clientInfo:依頼人情報
	 * receptionTime:受け取り時間
	 * sendTime:発送時間
	 */
	public Fragile(long frglNum, ClientInfo clientInfo, String receptionTime, String sendTime){
		this.frglNum = frglNum;
		this.clientInfo = clientInfo;
		this.saveTime("receptionTime",receptionTime);
		this.saveTime("sendTime",sendTime);
	}

	////////////////////////////////////setter//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @荷物番号 not 負の値や大きすぎる値
	 */
	/*荷物番号を記録する
	 * frglNum:荷物番号
	 */
	public void setFrglNum(long frglNum){
		this.frglNum = frglNum;
	}

	/**
	 * @更新する時間 not 存在しない時間の種類
	 * @時間 not 時間のフォーマットに従っていない
	 */
	/*時間を更新する
	 * setTime:記録先のフィールド名
	 * time:記録する値
	 */
	public void saveTime(String setTime, String time) {
		deliRecord.saveTime(setTime, time);
	}

	/**
	 * @配達状況 not Enumrationで定義された値以外
	 */
	/*配達状況を更新する
	 * deliStats:配達状況
	 */
	public void setDeliStats(DeliStats deliStats) throws IllegalArgumentException{
		deliRecord.setDeliStats(deliStats);
	}

	/**
	 * @依頼人情報 not null, 空文字
	 */
	/*依頼人情報を更新する
	 * clientName:依頼人氏名
	 * clientTel:依頼人電話番号
	 * clientAddr:依頼人住所
	 */
	public void setClientInfo(String clientName,String clientTel,String clientAddr) {
		this.clientInfo.setClientInfo(clientName,clientTel,clientAddr);
	}

	/**
	 * @受取人情報 not null, 空文字
	 */
	/*受取人情報を更新する
	 * houseName:受取人氏名
	 * houseTel:受取人電話番号
	 * houseAddr:受取人住所
	 */
	public void setHouseInfo(String houseName,String houseTel,String houseAddr) {
		this.clientInfo.setHouseInfo(houseName,houseTel,houseAddr);
	}

	/**
	 * @障害状況 not 存在しない障害状況
	 */
	/*障害状況を更新する
	 * obsStats:障害状況
	 */
	public void setObsStats(ObsStats obsStats){
		try{
			this.deliRecord.setObsStats(obsStats);
		}catch(IllegalArgumentException e){
			throw new IllegalArgumentException("未定義の配達状況です");
		}
	}

	////////////////////////////////////getter//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//荷物番号を返す
	public long getFrglNum() {
		return this.frglNum;
	}

	//配達状況を取得する
	public DeliStats getDeliStats() {
		return this.deliRecord.getDeliStats();
	}

	//依頼人情報を取得する
	public String[] getClientInfo() {
		String[] str = new String[3];
		str[0] = this.clientInfo.getClientName();
		str[1] = this.clientInfo.getClientTel();
		str[2] = this.clientInfo.getClientAddr();
		return str;
	}

	//受取人情報を取得する
	public String[] getHouseInfo() {
		String[] str = new String[3];
		str[0] = this.clientInfo.getHouseName();
		str[1] = this.clientInfo.getHouseTel();
		str[2] = this.clientInfo.getHouseAddr();
		return str;
	}

	//障害状況を取得する
	public ObsStats getObsStats() {
		return this.deliRecord.getObsStats();
	}

	/*配達時間をカレンダー型で取得する
	 * getTime:取得したい時間のフィールド名
	 */
	public Calendar getCalTime(String getTime){
		return this.deliRecord.getCalTime(getTime);
	}

	public String getStrTime(String getTime){
		return this.deliRecord.getStrTime(getTime);
	}

	/**
	 * @依頼情報 not null
	 */
	public void setAllInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}

}
