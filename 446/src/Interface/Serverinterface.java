package Interface;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JTextArea;

public class Serverinterface extends JFrame{
	
	private JPanel contentPane;
	boolean TwoPhaseLocking = false;
	boolean deffered_writing = true;

	/*public void showPanel(String name){	
		EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Serverinterface frame = new Serverinterface(name);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}*/
	
	public Serverinterface(String name){
 		contentPane = new JPanel();
 		contentPane.setBorder(new EmptyBorder(100, 100, 100, 100));
 		setContentPane(contentPane);
 		contentPane.setLayout(null);
 		JButton btnCrash = new JButton("Crash");
 			btnCrash.setBounds(12, 237, 120, 23);
 			contentPane.add(btnCrash);
 		 		
 				JButton btnCheckpoint = new JButton("CheckPoint");
 				btnCheckpoint.setBounds(271, 237, 120, 23);
 				contentPane.add(btnCheckpoint);
 				
 				JLabel lblRandomLabel = new JLabel("Random Label");
 				lblRandomLabel.setBounds(10, 11, 43, 14);
 				contentPane.add(lblRandomLabel);
	
	}

	
	
	public int Click_Strict() {
		//Must be implement at child
		return 0;
	}
}
