package Interface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class Clientinterface extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */

//	public void showPanel(String name){	
//	EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Clientinterface frame = new Clientinterface(name);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public Clientinterface(String name) {
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 211, 133);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnSendInstr = new JButton("Send Next Instruction");
		btnSendInstr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(doButtonAction()==0){
					
			dispose();
				}
			}


		});
		btnSendInstr.setBounds(10, 42, 178, 42);
		contentPane.add(btnSendInstr);
	}
	
	public int doButtonAction() {
	//Must be implement at child
		System.out.println("You should implement this Function");
		return 0;
	}
}
