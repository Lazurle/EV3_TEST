package fragile;

public class ClientInfo {

	private String clientName = null;

	private String clientTel = null;

	private String clientAddr = null;

	private String houseName = null;

	private String houseTel = null;

	private String houseAddr = null;

	/**
	 * @依頼人情報 not null, 空文字
	 */
	/*依頼人情報を更新する
	 * clientName:依頼人氏名
	 * clientTel:依頼人電話番号
	 * clientAddr:依頼人住所
	 */
	public void setClientInfo(String clientName,String clientTel,String clientAddr){
		//フィールドを更新する
		this.clientName = clientName;
		this.clientTel =  clientTel;
		this.clientAddr = clientAddr;
	}

	/**
	 * @受取人情報 not null, 空文字
	 */
	/*受取人情報を更新する
	 * houseName:受取人氏名
	 * houseTel:受取人電話番号
	 * houseAddr:受取人住所
	 */
	public void setHouseInfo(String houseName,String houseTel,String houseAddr){
		//フィールドを更新する
		this.houseName = houseName;
		this.houseTel =  houseTel;
		this.houseAddr = houseAddr;
	}

	//依頼人の名前を返す
	public String getClientName(){
		return this.clientName;
	}
	
	//依頼人の電話番号を返す
	public String getClientTel(){
		return this.clientTel;
	}
	
	//依頼人の住所を返す
	public String getClientAddr(){
		return this.clientAddr;
	}
	
	//受取人の名前を返す
	public String getHouseName(){
		return this.houseName;
	}
	
	//受取人の電話番号を返す
	public String getHouseTel(){
		return this.houseTel;
	}
	
	//受取人の住所を返す
	public String getHouseAddr(){
		return this.houseAddr;
	}
	
	//依頼人情報を返す
	public String getClientInfo() {
		return this.clientName+"|"+this.clientTel+"|"+this.clientAddr;
	}
	
	//受取人情報を返す
	public String getHouseInfo(){
		return this.houseName+"|"+this.houseTel+"|"+this.houseAddr;
	}

}
