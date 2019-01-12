package deliver;

import fragile.Fragile;
//障害状況の判定に使用
//use for obstacle judgement
import fragile.deliRecord.ObsStats;
import lineTrace.LineTrace_Deli;
import telecommunication.Telecommunication;
//通信クラスのメソッドで引数として必要
//use for Telecommunication methods
import telecommunication.Receiver;
//命令番号の列挙型
//Commands as enumeration
import telecommunication.code.*;
import lejos.utility.Delay;
//時間計測に必要
//needs for time measurement
import lejos.utility.Stopwatch;
//通信時の例外キャッチに必要
//needs in try-catch
import java.io.IOException;

public class Deliver {
	private int reciTime = 0;
	private boolean hasFragile =false;
	//private int countGray = 0;
	//加工保存の際に共有変数の保持をする
	//hold lock in method save()
	private boolean lock=false;
	//加工保存された宛先情報をif分岐で使用
	//use for saved correctHouse judgement
	private boolean correctHouse=false;
	//時間計測に必要
	//needs for time measurement
	//参考文献 Reference: https://qiita.com/amapyon/items/7853b070240a86de5efa
	Stopwatch stopwatch=null;
	//住所から移動すべきx-y座標を特定するための変数
	//x-y axis for moving to supposed address
	private int x, y;

	private Fragile fragile=new Fragile();////////////////////////////////////////
	private LineTrace_Deli lineTrace=new LineTrace_Deli();
	private Telecommunication telecommunication=new Telecommunication();
	//プロトコルが正しいかを判定
	//judge whether protocol is correct
	private boolean isProtocol;
	
	//マルチスレッド
	//multithread
	//コネクションの確立をするために使用
	//use for connection establishment
	private EstConnection est;
	
	//fixed 1
	private void lock() {
		//送受信の内容を保持する変数
		//hold send/receive details
		String syncDetail;

		//通信時に使用するフラグを保持するための変数
		//hold telecommunication flag
		boolean flag=false;

		do{
			syncDetail=this.adjust(Adjustment.odrNumLock);

			do{
				try{
					flag=this.telecommunication.sendSignal(syncDetail, Receiver.relay, Receiver.deliver, 2);
				}
				catch(IOException ioe){
					continue;
				}
			}while(flag==false);

			do{
				try{
					syncDetail=this.telecommunication.receiveSignal(Receiver.relay, Receiver.deliver, 2);
				}
				catch(IOException ioe){
					continue;
				}
			}while(syncDetail=="");

			this.exeRelayOrder(syncDetail);
		} while(this.lock);
	}

	private void freeLock() {
		//送受信の内容を保持する変数
		//hold send/receive details
		String syncDetail;

		//通信時に使用するフラグを保持するための変数
		//hold telecommunication flag
		boolean flag=false;


		syncDetail=this.adjust(Adjustment.lockFalse);

		do{
			try{
				flag=this.telecommunication.sendSignal(syncDetail, Receiver.relay, Receiver.deliver, 2);
			}
			catch(IOException ioe){
				continue;
			}
		}while(flag==false);
	}
	
	//fixed 2
	private void backRelay() {
		//excluded when organized test
		//this.lineTrace.ReturnToRelay(this.y, this.x);

		//相手識別
		//recognize opponent
		do{
			this.makeProtocol(Receiver.relay);
		} while(this.isProtocol==false);
		
		this.lock();
		
		//excluded when organized test
		//this.lineTrace.ToRelay();
	}

	private void reportResult() {
		//送受信の内容を保持する変数
		//hold send/receive details
		String syncDetail;

		//通信時に使用するフラグを保持するための変数
		//hold telecommunication flag
		boolean flag=false;


		if(this.fragile.getObsStats()!=ObsStats.none){
			syncDetail=this.adjust(Adjustment.odrFrglNumObs);

			do{
				try{
					flag=this.telecommunication.sendSignal(syncDetail, Receiver.relay, Receiver.deliver, 2);
				}
				catch(IOException ioe){
					continue;
				}
			}while(flag==false);
		}
		else{
			syncDetail=this.adjust(Adjustment.odrFrglNumObsReciTime);

			do{
				try{
					flag=this.telecommunication.sendSignal(syncDetail, Receiver.relay, Receiver.deliver, 2);
				}
				catch(IOException ioe){
					continue;
				}
			}while(flag==false);
		}

		//this.hasFragile=this.takeFragile();
	}

	private void deliveToHouse() {
		//住所をこの変数で得て、加工し、x-y座標を得るための前処理
		//hold house information before getting x-y axis
		String address;

		//excluded when organized test
		//this.lineTrace.RelayToCollision();

		//相手識別
		//recognize opponent
		do{
			this.makeProtocol(Receiver.relay);
		} while(this.isProtocol==false);
		
		this.freeLock();

		Delay.msDelay(500);
		
		
		//multi-thread phase start
		
		//相手識別
		//recognize opponent
		do{
			this.makeProtocol(Receiver.house);
		} while(this.isProtocol==false);
		
		this.est=new EstConnection(this.telecommunication);
		
		//スレッドの開始
		//start thread
		this.est.start(String.valueOf(this.fragile.getFrglNum()));
		
		//コネクション確立まで待機
		//wait until connection establish
		while(!this.telecommunication.isWaitThread());
		
		//multi-thread end
		

		//excluded when organized test
		//住所を得て、加工し、x-y座標を得るための前処理
		//method before getting x-y axis
		String[] tmp=this.fragile.getHouseInfo();
		address=tmp[2];
		//住所が「2-3」とあれば、x=2, y=1を抽出し、int型に直す
		//if address is "2-3", get x=2, y=3 and adjust them Integer
		this.x=Integer.parseInt(address.substring(0, 1));
		this.y=Integer.parseInt(address.substring(2, 3));

		//this.lineTrace.ToHouse(this.y, this.x);
	}

	private void backWaitingArea() {
		//excluded when organized test
		//this.lineTrace.RelayToCollision();

		//相手識別
		//recognize opponent
		do{
			this.makeProtocol(Receiver.relay);
		} while(this.isProtocol==false);
		
		this.freeLock();
		
		//exclude when organized test
		//this.lineTrace.ReturnToWaiting();
	}

	private void moveRelay() {
		//excluded when organized test
		//this.lineTrace.WaitingToCollision();

		//相手識別
		//recognize opponent
		do{
			this.makeProtocol(Receiver.relay);
		} while(this.isProtocol==false);
		
		this.lock();
		
		//excluded when organized test
		//this.lineTrace.ToRelay();
	}

	private boolean takeFragile() {
		//送受信の内容を保持する変数
		//hold send/receive details
		String syncDetail;

		//通信時に使用するフラグを保持するための変数
		//hold telecommunication flag
		boolean flag=false;

		
		syncDetail=this.adjust(Adjustment.odrNumHasFragile);
		
		do{
			try{
				flag=this.telecommunication.sendSignal(syncDetail, Receiver.relay, Receiver.deliver, 2);
			}
			catch(IOException ioe){
				continue;
			}
		}while(flag==false);

		do{
			try{
				syncDetail=this.telecommunication.receiveSignal(Receiver.relay, Receiver.deliver, 10/*?*/);
			}
			catch(IOException ioe){
				continue;
			}
		}while(syncDetail=="");
		
		this.exeRelayOrder(syncDetail);
		
		//receive fragile num
		if(this.hasFragile){
			do{
				try{
					syncDetail=this.telecommunication.receiveSignal(Receiver.relay, Receiver.deliver, 10/*?*/);
				}
				catch(IOException ioe){
					continue;
				}
			}while(syncDetail=="");
			
			this.exeRelayOrder(syncDetail);
			
			this.startTime();
		}

		return this.hasFragile;
	}

	//fixed 1
	private void passFragile() {
		//送受信の内容を保持する変数
		//hold send/receive details
		String syncDetail;

		//通信時に使用するフラグを保持するための変数
		//hold telecommunication flag
		boolean flag=false;
		
		
		syncDetail=this.adjust(Adjustment.clientHouseNameAddr);
			
		Delay.msDelay(10);

		try{
			flag=this.telecommunication.sendSignal(syncDetail, Receiver.house, Receiver.deliver, 120);
		}
		catch(IOException ioe){
			flag=false;
			System.out.println("House "+this.x+"-"+this.y+" did not reply.  Recorded Absent...");
		}

		if(flag){
			do{
				Delay.msDelay(10);
				try{
					syncDetail=this.telecommunication.receiveSignal(Receiver.house, Receiver.deliver, 2);
				}
				catch(IOException ioe){
					continue;
				}
			}while(syncDetail=="");

			this.exeHouseOrder(syncDetail);

			if(this.correctHouse==false) this.fragile.setObsStats(ObsStats.wrongHouse);
			else{
				this.fragile.setObsStats(ObsStats.none);

				syncDetail=this.adjust(Adjustment.adjustFragile);

				do{
					Delay.msDelay(10);
					try{
						flag=this.telecommunication.sendSignal(syncDetail, Receiver.house, Receiver.deliver, 2);
					}
					catch(IOException ioe){
						continue;
					}
				} while(flag==false);

				do{
					Delay.msDelay(10);
					try{
						syncDetail=this.telecommunication.receiveSignal(Receiver.house, Receiver.deliver, 2);
					}
					catch(IOException ioe){
						continue;
					}
				} while(syncDetail=="");

				this.exeHouseOrder(syncDetail);

				this.stopTime();///////////////////////////////////////////////////
				
				this.hasFragile=false;
			}
		}
		else this.fragile.setObsStats(ObsStats.absent);
	}

	private void startTime() {
		//最初のnullの状態でインスタンスを作成し、2回目以降はリセットすればひとつのインスタンスを使い回せる
		//make instance for the first time (null) and recycle it from next time with method reset()
		if(this.stopwatch==null){
			//インスタンスを作ると時間の計測が始まる
			//starts time measurement when made a new instance.
			this.stopwatch=new Stopwatch();
		}
		else this.stopwatch.reset();  //経過時間をリセット reset passed time
	}

	private void stopTime() {
		//返される時間の単位はミリ秒
		//returned time is millisecond
		this.reciTime=this.stopwatch.elapsed();
	}

	private String adjust(Adjustment order) {
		//加工した内容を返すための変数
		//String for returning detail
		String detail="";
		
		
		if(order==Adjustment.relayProtocol){
			detail+="protocol|relay";
		}
		else if(order==Adjustment.houseProtocol){
			detail+="protocol|house";
		}
		else if(order==Adjustment.odrNumHasFragile){
			detail+="sendHasFrgl";
		}
		else if(order==Adjustment.odrFrglNumObs){
			detail+="reportDeliFail|";
			detail+=String.valueOf(this.fragile.getFrglNum());
			detail+="|";
			detail+=String.valueOf(this.fragile.getObsStats());
		}
		else if(order==Adjustment.odrFrglNumObsReciTime){
			detail+="reportDeliResult|";
			detail+=String.valueOf(this.fragile.getFrglNum());
			//detail+="|none|";//cancel
			detail+=String.valueOf(this.reciTime);
		}
		else if(order==Adjustment.odrNumLock){
			detail+="sendLock";
		}
		else if(order==Adjustment.lockFalse){
			if(this.hasFragile) detail+="goHouse";
			else detail+="backWaitingArea";
			this.lock=false;
			//detail+=String.valueOf(this.lock);  //delete here
		}
		else if(order==Adjustment.clientHouseNameAddr){
			//依頼情報の内容を保持するための変数
			//String array for holding Client Info
			String[] tmpClient, tmpHouse;////////////////////////////////////////
			
			tmpClient=this.fragile.getClientInfo();//////////////////////////////
			tmpHouse=this.fragile.getHouseInfo();///////////////////////////////
			
			//テスト時に使用
			//for test usage
			//detail="personAddrName|Ren Sato|0-0|Yuduki Suzuki|0-1";
			
			detail="personAddName|"+tmpClient[0]+"|"+tmpClient[2]+"|"+tmpHouse[0]+"|"+tmpHouse[2];
		}
		else if(order==Adjustment.adjustFragile){
			detail+="syncFrglNum|";
			detail+=String.valueOf(this.fragile.getFrglNum());
		}
		else System.err.println("Warning!!  Error when adjust.");

		return detail;
	}

	private void save(String reciDetail, Save order) {
		//命令番号をよけるために使う
		//for avoiding code
		String[] tmp=reciDetail.split("\\|");
		
		if(order==Save.issueProtocol){
			this.isProtocol=Boolean.valueOf(tmp[1]);
		}
		else if(order==Save.hasFragile){
			this.hasFragile=Boolean.valueOf(tmp[1]);
			//if(tmp.length==2) this.fragile.setFrglNum(Long.valueOf(tmp[2]));
		}
		else if(order==Save.frglInfo){
			this.fragile.setFrglNum(Long.valueOf(tmp[1]));
			this.fragile.setClientInfo(tmp[2], null, tmp[3]);
			this.fragile.setHouseInfo(tmp[4], null, tmp[5]);
		}
		else if(order==Save.saveLock){
			this.lock=Boolean.valueOf(tmp[1]);
		}
		else if(order==Save.houseInfo){
			this.correctHouse=Boolean.valueOf(tmp[1]);
		}
		else if(order==Save.takenConf){
			//荷物の受取確認に使用
			//use for fragile passed confirmation
			boolean hasConfirmed;
			hasConfirmed=Boolean.valueOf(tmp[1]);
			if(hasConfirmed) System.out.println("Delivery Completed!!  Go back to Relay area!!");
		}
		else System.err.println("Warning!!  Error when save.");
	}
	
	//中継所間の命令番号を参照し、然るべき操作をする
	//See code of Relay_Deliver, execute appropriate operation
	private void exeRelayOrder(String syncDetail){
		String[] tmp=syncDetail.split("\\|");
		Relay_Deliver code=Relay_Deliver.valueOf(tmp[0]);
		
		switch(code){
		case protocol:
			this.save(syncDetail, Save.issueProtocol);
			break;
		case sendLock:
			System.out.println("Received code, sendLock but not used...");
			break;
		case syncLock:
			this.save(syncDetail, Save.saveLock);
			break;
		case sendHasFrgl:
			System.out.println("Received code, sendHasFrgl but not used...");
			break;
		case syncHasFrgl:
			this.save(syncDetail, Save.hasFragile);
			break;
		case syncFrglInfo:
			this.save(syncDetail, Save.frglInfo);
			break;
		case setLockFalse:
			System.out.println("Received code, setLockFalse but not used...");
			break;
		/*case backWaitingArea:
			System.out.println("Received code, backWaitingArea but not used...");
			break;
		case goHouse:
			System.out.println("Received code, backWaitingArea but not used...");*/
		case reportDeliFail:
			break;
		case reportDeliResult:
			System.out.println("Received code, reportDeliResult but not used...");
			break;
		default:
			System.err.println("Error: Received unexpected code.");
			this.isProtocol=false;
			break;
		}
	}
	
	//受取人宅間の命令番号を参照し、然るべき操作をする
	//See code of Deliver_House, execute appropriate operation
	private void exeHouseOrder(String syncDetail){
		String[] tmp=syncDetail.split("\\|");
		//Debug
		System.out.println("Debug: "+tmp[0]);
		
		Deliver_House code=Deliver_House.valueOf(tmp[0]);
		
		switch(code){
		case protocol:
			this.save(syncDetail, Save.issueProtocol);
			break;
		case personAddrName:
			System.out.println("Received code, personAddrName but not used...");
			break;
		case syncJudge:
			this.save(syncDetail, Save.houseInfo);
			break;
		case syncFrglNum:
			System.out.println("Received code, syncFrglNum but not used...");
			break;
		case syncHasReceived:
			this.save(syncDetail, Save.takenConf);
			break;
		default:
			System.err.println("Error: Received unexpected code.");
			this.isProtocol=false;
			break;
		}
	}
	
	//プロトコル通信　partner:希望する通信相手
	//protocol communication, partner:partner user wishes
	void makeProtocol(Receiver partner){
		String syncDetail="";
		boolean flag=false;
		
		
		if(partner==Receiver.relay) syncDetail=adjust(Adjustment.relayProtocol);
		else if(partner==Receiver.house) syncDetail=adjust(Adjustment.houseProtocol);
		
		Delay.msDelay(10);
		
		//サブシステム名を要求する
		//demand opponent after established connection
		do{
			try{
				flag=this.telecommunication.sendSignal(syncDetail, partner, Receiver.deliver, 120);
			}
			catch(IOException ioe){
				continue;
			}
		} while(flag==false);
		
		Delay.msDelay(10);
		
		//通信相手が正しいかを知る
		//judge who communicate with
		do{
			try{
				syncDetail=this.telecommunication.receiveSignal(partner, Receiver.deliver, 20);
			}
			catch(IOException ioe){
				continue;
			}
		} while(syncDetail.equals(""));
		
		if(partner==Receiver.relay) this.exeRelayOrder(syncDetail);
		else if(partner==Receiver.house) this.exeHouseOrder(syncDetail);
		else{
			System.err.println("Illegal input on isProtocol(): Input relay or house.");
		}
		
	}

	/*
	//for testing
	private void deliveFragile() {
		//this.moveRelay();

		//this.hasFragile=this.takeFragile();

		if(true/*this.hasFragile){
			//this.deliveToHouse();

			this.passFragile();
		}
		else this.backWaitingArea();
	}

	private void clearMission() {
		this.backRelay();

		this.reportResult();

		this.hasFragile=this.takeFragile();

		if(this.hasFragile) this.deliveToHouse();
		else this.backWaitingArea();
	}
	*/
	
	//コントロールメソッド
	//control method
	public void control(){
		int test=0;
		
		//Line trace is excluded
		while(true){
			System.out.println("Debug No.1");
			this.moveRelay();
			
			System.out.println("Debug No.2");
			//相手識別
			//recognize opponent
			do{
				this.makeProtocol(Receiver.relay);
			} while(this.isProtocol==false);
			
			System.out.println("Debug No.3");
			
			this.hasFragile=this.takeFragile();
			
			//荷物がないときはif内の処理はしない
			//not process inside if sentence when no fragile
			if(this.hasFragile){
				System.out.println("Debug No.4");
				
				this.deliveToHouse();
				
				//for test usage with house
				/*final int time=30;
				System.out.println("For Test: Now line tracing");
				System.out.println("For Test: please sand by...");
				Delay.msDelay(time*1000);*/
				
				//通信開始
				//start communicating
				this.telecommunication.notifyThread();
				
				//通信処理が全て終わるまで待つ
				//wait until all communication process ends
				while(this.est.isAlive());
				
				this.passFragile();
				
				//test with relay
				//System.out.println("Debug No.5");
				//this.stopTime();
				
				//test with house
				//if(this.correctHouse/*==false*/){
						//test++;
						//System.out.println("test OK with house:"+(test++)+" time(s).");
						//Delay.msDelay(5000);  //for test
				//}
				
				System.out.println("Debug No.5");
				
				this.backRelay();
			
				System.out.println("Debug No.6");
				
				//相手識別
				//recognize opponent
				do{
					this.makeProtocol(Receiver.relay);
				} while(this.isProtocol==false);
				
				System.out.println("Debug No.7");
				
				this.reportResult();
			}
			
			System.out.println("Debug No.8");
			
			this.backWaitingArea();
				
			System.out.println("test OK with relay:"+(test++)+" time(s).");
		}
	}
}
