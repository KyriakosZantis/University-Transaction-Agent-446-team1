package Interface;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	private JPanel contentPane;
	private ButtonGroup bgUpdate = new ButtonGroup();
	private ButtonGroup bgType = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 362, 271);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		JRadioButton rdbtnImmediateUpdate = new JRadioButton("Immediate Update");
		
		JRadioButton rdbtnDeferredUpdate = new JRadioButton("Deferred Update");
		
		bgUpdate.add(rdbtnImmediateUpdate);
		bgUpdate.add(rdbtnDeferredUpdate);
		
		JRadioButton rdbtnWoundAndWait = new JRadioButton("Wound and wait");
		
		JRadioButton rdbtnWaitAndDie = new JRadioButton("Wait and die");
		
		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(doButtonAction()==0){
					dispose();
				}
			}
		});
		
		bgType.add(rdbtnWoundAndWait);
		bgType.add(rdbtnWaitAndDie);
		bgType.add(btnStartServer);
		
		JRadioButton rdbtnCautiousWaiting = new JRadioButton("Cautious Waiting");
		
		JLabel lblUpdateType = new JLabel("Update Type");
		lblUpdateType.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		JLabel lblDeadlockPreventionType = new JLabel("Deadlock Type");
		lblDeadlockPreventionType.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(23)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(rdbtnDeferredUpdate, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(rdbtnImmediateUpdate)
								.addComponent(lblUpdateType, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
							.addGap(39)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDeadlockPreventionType, GroupLayout.PREFERRED_SIZE, 151, Short.MAX_VALUE)
								.addComponent(rdbtnWoundAndWait, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(rdbtnWaitAndDie, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(rdbtnCautiousWaiting, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(119)
							.addComponent(btnStartServer)))
					.addContainerGap(38, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(32, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUpdateType, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDeadlockPreventionType, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(rdbtnWoundAndWait)
							.addGap(3)
							.addComponent(rdbtnWaitAndDie)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnCautiousWaiting))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(rdbtnImmediateUpdate)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(rdbtnDeferredUpdate)))
					.addGap(18)
					.addComponent(btnStartServer)
					.addGap(22))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public int doButtonAction() {
		//Must be implement at child
		System.out.println("You should implement this Function");
		return 0;
	}
}
