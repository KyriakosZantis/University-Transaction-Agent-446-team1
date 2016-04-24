package Interface;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
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
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 390, 309);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(100, 100, 100, 100));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3,3));
		
		JTextArea textInstructions = new JTextArea();
		textInstructions.setText("asdffsadfsdfsadjkfsdfsdsd\nfsadfsadfsadfsad");
		//to insert Slider to show what the log file has in it
		contentPane.add(textInstructions);
		
	
	}

	
	
	public int Click_Strict() {
		//Must be implement at child
		return 0;
	}
}
