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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;

public class Serverinterface extends JFrame{
	
	private JPanel contentPane;
	boolean TwoPhaseLocking = false;
	boolean deffered_writing = true;
	protected static int  server_windowX=328;
	protected static int  server_windowY=122;
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
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		contentPane = new JPanel();
 		contentPane.setBorder(new EmptyBorder(100, 100, 100, 100));
 		setBounds(100, 100, server_windowX, server_windowY);
 		setContentPane(contentPane);
 		contentPane.setLayout(null);
 		
 		JButton btnCrash = new JButton("Crash");
 		btnCrash.setBounds(12, 30, 141, 37);
 		btnCrash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doButtonActionCrash();
			}
		});
 		contentPane.add(btnCrash);
 		 		
 		JButton btnCheckpoint = new JButton("CheckPoint");
 		btnCheckpoint.setBounds(175, 30, 127, 37);
 		btnCheckpoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doButtonActionCheckpoint();
			}
		});
 		contentPane.add(btnCheckpoint);
 				
 				
 				
 				
	}

	public int doButtonActionCrash() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int doButtonActionCheckpoint() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	

}
