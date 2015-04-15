import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JToggleButton;

import java.awt.GridLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;


public class View extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	csProcess model;
	
	JLabel lblProcessId;
	JTextField txtTime=new JTextField("0000");
	JButton btnEvent=new JButton("Fire Event");
	JToggleButton tglbtnPhysical;
	private final JPanel pnlSend = new JPanel();
	JTextField txtTo;
	JTextField txtMessage;
	JButton btnSend = new JButton("SEND");
	private JTable table;
	private DefaultTableModel dftEvents;
	private JScrollPane scrEvents;
	private JLabel lblClock;
	
	View(csProcess csp) {
		this.model=csp;
		
		getContentPane().setLayout(null);
		txtTime.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		txtTime.setHorizontalAlignment(SwingConstants.CENTER);
		txtTime.setEditable(false);
		txtTime.setBounds(189, 75, 134, 47);
		
		getContentPane().add(txtTime);
		
		btnEvent.setBounds(46, 130, 133, 23);
		getContentPane().add(btnEvent);
		pnlSend.setBounds(46, 164, 277, 126);
		getContentPane().add(pnlSend);
		pnlSend.setLayout(null);
		
		txtTo = new JTextField("0000");
		txtTo.setBounds(123, 6, 44, 20);
		pnlSend.add(txtTo);
		
		txtMessage = new JTextField("Message");
		txtMessage.setBounds(123, 37, 146, 46);
		pnlSend.add(txtMessage);
		
		btnSend.setBounds(171, 94, 98, 23);
		pnlSend.add(btnSend);
		
		JLabel lblToPid = new JLabel("To PID :");
		lblToPid.setBounds(35, 9, 73, 14);
		pnlSend.add(lblToPid);
		
		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setBounds(33, 37, 73, 14);
		pnlSend.add(lblMessage);
		
		tglbtnPhysical = new JToggleButton("Physical");
		tglbtnPhysical.setBounds(189, 130, 134, 23);
		getContentPane().add(tglbtnPhysical);
		
		JPanel pnlInfo = new JPanel();
		pnlInfo.setBounds(10, 11, 356, 53);
		getContentPane().add(pnlInfo);
		pnlInfo.setLayout(null);
		
		lblProcessId = new JLabel("Process ID :");
		lblProcessId.setBounds(114, 11, 109, 35);
		pnlInfo.add(lblProcessId);
		
		dftEvents=new DefaultTableModel();
		
		scrEvents = new JScrollPane();
		scrEvents.setBounds(10, 301, 356, 149);
		getContentPane().add(scrEvents);
		table = new JTable(dftEvents);
		scrEvents.setViewportView(table);
		
		lblClock = new JLabel("Clock :");
		lblClock.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblClock.setBounds(46, 75, 133, 47);
		getContentPane().add(lblClock);
		dftEvents.addColumn("Time");
		dftEvents.addColumn("Message");
		dftEvents.addColumn("From/To");
		dftEvents.addColumn("Timestamp");
		
		setDetails();
		setSize(392,499);
		setTitle("Lamports Clock Sync");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
			txtTime.setText("" + model.getTime());
			if(arg1!=null){
				String tmp=(String)arg1;
				String part[]=tmp.split("_");
				if(tmp.equals("event")){
					dftEvents.addRow(new String[]{"" + model.getTime(),"Click"," "," "});
				}else if(part[0].equals("send")){
					dftEvents.addRow(new String[]{"" + model.getTime(),"Sent:" + part[1],part[2],part[3]});
				}else if(part[0].equals("receive")){
					dftEvents.addRow(new String[]{"" + model.getTime(),"Received: " + part[1], part[2] , part[3]});
				}else if(part[0].equals("SendFailed")){
					dftEvents.addRow(new String[]{"" + model.getTime(),"Not Sent:" + part[1],part[2],part[3]});
				}
			}
	}

	public void setDetails(){
		 txtTime.setText("" + model.getTime());
		 lblProcessId.setText("<html>Process ID : " + model.pid + "<br> Started at : " + model.getTime() + "<html>");
	}
	
	public void addpEvent(int tms, String desc){
		dftEvents.addRow(new String[]{String.valueOf(tms),desc});
	}
	
	public void addController(ActionListener controller){
		btnEvent.addActionListener(controller);
		btnSend.addActionListener(controller);
		tglbtnPhysical.addActionListener(controller);
	}
}
