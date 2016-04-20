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
		setBounds(100, 100, 211, 133);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(100, 100, 100, 100));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3,3));
		
		JCheckBox TwoPL = new JCheckBox("Strict 2PL");
		TwoPL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Click_Strict() == 1)
					TwoPhaseLocking = true;
				else
					TwoPhaseLocking = false;
				//System.out.println(TwoPhaseLocking);
			}
		});
		contentPane.add(TwoPL);
		
		JRadioButton deffered = new JRadioButton("Deffered");
		JRadioButton immediate = new JRadioButton("Immediate");
		ButtonGroup bG = new ButtonGroup();
	    bG.add(deffered);
	    bG.add(immediate);
	    deffered.setSelected(true);
		deffered.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (deffered.isSelected())
					deffered_writing = true;
				//System.out.println(deffered_writing);
			}
		});
		immediate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (immediate.isSelected())
					deffered_writing = false;
				//System.out.println(deffered_writing);
			}
		});
		contentPane.add(deffered);
		contentPane.add(immediate);
	}

	
	
	public int Click_Strict() {
		//Must be implement at child
		return 0;
	}
}
