import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;


public class Controller implements ActionListener{
csProcess model;
View view;

	Controller(){
		int tmpID=new Random().nextInt(100) + 2000;
		model=new csProcess(tmpID, 10);
		view=new View(model);
		model.addObserver(view);
		view.addController(this);
	}
	public static void main(String args[]){
		new Controller();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object btn=arg0.getSource();
		if(btn == view.btnSend){
			System.out.println("Fired Message");
			model.sendMsg(Integer.parseInt(view.txtTo.getText()), view.txtMessage.getText());
		}else if(btn == view.btnEvent){
			System.out.println("Fired event");
			model.fireEvent();
		}else if(btn==view.tglbtnPhysical){
			JToggleButton btn2=(JToggleButton)btn;
			if(btn2.isSelected()){
				model.startPhysicalClock();
			}else{
				model.stopPhysicalClock();
			}
		}
		
		
	}//actionPer
}
