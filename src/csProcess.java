import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

//Model

public class csProcess extends Observable implements Runnable {

	boolean phyClock=false;
	private int time;
	int pid;
	
	csProcess(int pid,int baseTime){
		this.pid=pid;
		time=baseTime;
		
		new Thread(this).start();
	}
	
	public void incrTime(){
		time++;
		setChanged();
		notifyObservers();
	}
	
	public void fireEvent(){
		incrTime();
		setChanged();
		notifyObservers("event");
	}
	
	public void startPhysicalClock(){
		if(phyClock==true){//already running
			return;
		}
		phyClock=true;
		(new Thread(){
			@Override
			public void run() {
				while(phyClock==true){
					incrTime();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}
	
	public void stopPhysicalClock(){
		phyClock=false;
	}
	
	void sendMsg(int dest,String msg){
		//send msg and timestamp
		Socket s;
		int timestamp=0;
		try {
			s = new Socket("127.0.0.1",dest);

			DataOutputStream os=new DataOutputStream(s.getOutputStream());
			
			os.writeInt(pid);
			timestamp=time;
			os.writeInt(timestamp);
			os.writeUTF(msg);
			s.close();
			setChanged();
			notifyObservers("send_" + msg + "_" + String.valueOf(dest) + "_" + timestamp);
		} catch (IOException e) {
			e.printStackTrace();
			setChanged();
			notifyObservers("SendFailed_" + msg + "_" + String.valueOf(dest) + "_" + timestamp);
		}
	}
	
	
	public void run(){
		String msg;
		Socket s;
		try{
			ServerSocket srv=new ServerSocket(pid);
			System.out.println("Listening to other process started..");
			while(true){
				s=srv.accept();
				DataInputStream is=new DataInputStream(s.getInputStream());
				
				int remotePid=is.readInt();
				int remoteTime=is.readInt();
				msg=is.readUTF();

				receiveMsg(remotePid,msg,remoteTime);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	void receiveMsg(int sender,String msg,int timestamp){
		if(timestamp>time){ //message timetamp is greater than local
			time=timestamp+1; //set our time more than remote process
		}else{
			
		}
		
		System.out.println("Process" +sender + ":[" +timestamp + "] " + msg);
		setChanged();
		notifyObservers("receive_" + msg + "_" + String.valueOf(sender) + "_" + String.valueOf(timestamp));
	}

	public int getTime(){
		return time;
	}
}
