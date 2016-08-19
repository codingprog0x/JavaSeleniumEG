import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.*;
import java.util.List;

import javax.swing.*;

public class Main {
	public static File theFile;
	public static JFrame frame;
	public static JTextField testAmtField; 
	private JComboBox<String> comboBox;
	private JComboBox<String> scrapeComboBox;
	private JTextField scrapeURLField;
	
	//if more options are added here, need to also add it to JComboBox<String> comboBox's actionPerformed method
	//located on line 120-ish
	private String[] actionList = {"Test Transaction", "Add Property", "Property Existing Merch", "TIN Check", "Special Property", "Pull"};
	
	private String[] scrapeList = {"Property"};
	
	public static void main(String[] args) {
		Main nMain = new Main();
		nMain.go();
	}
	
	public void go() {
		frame = new JFrame("Automation Interface");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//create JPanel to hold JComboBox
		JPanel topPanel = new JPanel();
		
		//create JPanel to hold file's drag-and-drop zone
		JPanel centerPanel = new JPanel();
		centerPanel.setSize(250, 200);
		
		//create JComboBox to hold list of actionable options
		comboBox = new JComboBox<String>(actionList);
		topPanel.add(comboBox);
		
		//create JButton to begin selected operations
		JButton theButt = new JButton("Begin");
		topPanel.add(theButt);
		theButt.addActionListener(new ThingsGo());
		//theButt.addActionListener(this);
		
		//create JTextField for test transaction amount declaration from user
		testAmtField = new JTextField(9);
		testAmtField.setText("Test Amount?");
		
		//when user clicks on box, delete contents
		testAmtField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				testAmtField.setText("");
			}
		});
		topPanel.add(testAmtField);
		
		//create JTextField for file's drag-and-drop zone
		JTextField dropZone = new JTextField("Drag and drop a .csv file into this box");
		dropZone.setHorizontalAlignment(JTextField.CENTER);
		dropZone.setEditable(false);
		dropZone.setBackground(Color.lightGray);
		dropZone.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					
					List<File> droppedFiles = (List<File>) 
							evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					
					for (File file : droppedFiles) {
						theFile = file;
						dropZone.setText(String.valueOf(theFile));
						//System.out.println(theFile);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		//centerPanel.add(dropZone);
		frame.getContentPane().add(BorderLayout.CENTER, dropZone);
		frame.getContentPane().add(BorderLayout.NORTH, topPanel);
		//frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
		
		//create JPanel for SOUTH 
		JPanel bottomPanel = new JPanel();
		frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);
		
		scrapeComboBox = new JComboBox<String>(scrapeList);
		bottomPanel.add(scrapeComboBox);
		
		JButton scrapeButt = new JButton("Scrape");
		bottomPanel.add(scrapeButt);
		scrapeButt.addActionListener(new ScrapeGo());
		
		scrapeURLField = new JTextField(9);
		scrapeURLField.setText("Paste link here");
		scrapeURLField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				scrapeURLField.setText("");
			}
		});
		bottomPanel.add(scrapeURLField);
	
		frame.setSize(700, 400);
		frame.setVisible(true);
	}
	
	class ThingsGo implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (comboBox.getSelectedItem().equals(actionList[0])) {
				new TestTrans(theFile, testAmtField.getText()).go();
			} else if (comboBox.getSelectedItem().equals(actionList[1])) {
				new NewProperty(theFile, true, false).go();
			} else if (comboBox.getSelectedItem().equals(actionList[2])) {
				new NewProperty(theFile, false, false).go();
			} else if (comboBox.getSelectedItem().equals(actionList[3])) {
				new TinCheck(theFile).go();
			} else if (comboBox.getSelectedItem().equals(actionList[4])) {
				new NewProperty(theFile, true, true).go();
			} else if (comboBox.getSelectedItem().equals(actionList[5])) { 
				new Pull().go();
			} else {
				System.out.println("ERROR");
			}
		}
	}
	
	class ScrapeGo implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (scrapeComboBox.getSelectedItem().equals(scrapeList[0])) {
				new ScrapePropForm(scrapeURLField.getText()).go(); 
			} else {
				System.out.println("ERROR");
			}
		}
	}
}
