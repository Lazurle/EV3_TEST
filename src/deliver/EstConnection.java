/*
 /* 驟埼＃繝ｭ繝懊ャ繝�(繧ｵ繝ｳ繝励Ν繝励Ο繧ｰ繝ｩ繝�
*/
package deliver;

//import lejos.utility.Delay;
import telecommunication.Telecommunication;
import telecommunication.Receiver;

import java.io.IOException;

public class EstConnection extends Thread{
	private Telecommunication tele;
	private String sendDetail;
	
	public EstConnection(Telecommunication tele){
		this.tele=tele;
	}
	
	 public void start(String sendDetail){
		 this.sendDetail=sendDetail;
		 this.start();
	}
	
	public void run(){//繧ｳ繝阪け繧ｷ繝ｧ繝ｳ繧堤｢ｺ遶九＠縺溘∪縺ｾ蠕�ｩ溘☆繧�
		this.tele.setSyncThread();
		try {
			this.tele.sendSignal(this.sendDetail, Receiver.house, Receiver.deliver, 0);//waitTime=0 縺ｧ辟｡髯仙ｾ�ｩ溘�諢丞袖
			
			//System.out.printf("send(wait) : %s\n",this.sendDetail);//////////////////////////////
		} catch (IOException e) {
			;
		}
		this.tele.resetSyncThread();
	}
}