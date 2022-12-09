package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = false;
	String username;
	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuItemCloseTicket;

	
	public Tickets(Boolean isAdmin) {

        chkIfAdmin = isAdmin;
        createMenu();
        prepareGUI();

    }
		

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);
		
		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);
		
		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);
		
		//initialize first sub menu item for tickets main menu
		mnuItemCloseTicket = new JMenuItem("Close Ticket");
		//add to Admin main menu item
		mnuAdmin.add(mnuItemCloseTicket);
		
		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);
		
		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
		mnuItemCloseTicket.addActionListener(this);

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above
		 */

	}	

	private void prepareGUI() {

        // create JMenu bar
        JMenuBar bar = new JMenuBar();
        bar.add(mnuFile); // add main menu items in order, to JMenuBar
        bar.add(mnuAdmin);
        bar.add(mnuTickets);
        // add menu bar components to frame
        setJMenuBar(bar);

        addWindowListener(new WindowAdapter() {
            // define a window close operation
            public void windowClosing(WindowEvent wE) {
                System.exit(0);
            }
        });
        // set frame options
        setSize(400, 400);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setLocationRelativeTo(null);
        setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.out.println("Ticket system exited");
			System.exit(0);
			
		} else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

			// insert ticket information to database

			if(ticketName == null || ticketDesc == null) {
				JOptionPane.showMessageDialog(null, "Ticket creation failed");
				System.out.println("Ticket creation failed");
			}
			else {
				int id = dao.insertRecords(ticketName, ticketDesc);
				
				// display results if successful or not to console / dialog box
				if (id != 0) {
					System.out.println("Ticket ID : " + id + " created successfully!!!");
					JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
				
				} 
				else { 
				System.out.println("Ticket cannont be created!!!");
			}
		}
	}
			else if (e.getSource() == mnuItemViewTicket) {
				// retrieve all tickets details for viewing in JTable
	            try {

	                // Use JTable built in functionality to build a table model and
	                // display the table model off your result set!!!
	                JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
	                jt.setBounds(30, 40, 200, 400);
	                JScrollPane sp = new JScrollPane(jt);
	                add(sp);
	                setVisible(true); // refreshes or repaints frame on screen

	            } catch (SQLException e1) {
	                e1.printStackTrace();
	            }
		}
		
			else if (e.getSource() == mnuItemUpdate) {
				if (chkIfAdmin == true){
					
				    int id = 0;
				  
				    //get ticket id and description user wants to update
				    String ticketId = JOptionPane.showInputDialog(null,"Enter the id of the ticket you want to update");
				    String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
				    //request update by calling update record
				    id = Integer.parseInt(ticketId); 
				    dao.updateRecords(ticketId,ticketDesc);
				    System.out.print(id);
				  
				    if (id != 0) { 
				    	System.out.println("Ticket ID : " + id + " updated successfully!!!"); JOptionPane.showMessageDialog(null,"Ticket id: " + id + " updated"); 	
				    } 
				    else System.out.println("Ticket cannot be updated!!!");
				  }
				else {
					JOptionPane.showMessageDialog(null, "Access Denied");
				}
			}

			else if(e.getSource() == mnuItemDelete) {
				
				if (chkIfAdmin == true){
					int id = 0;
		  
					//get ticket id user wants to delete
					String ticketId = JOptionPane.showInputDialog(null, "Enter the id of the ticket you want to delete");
					//validation check
					if(ticketId == null || (ticketId == null && ticketId.isEmpty())) {
						
						JOptionPane.showInputDialog(null, "Please select an valid ID to delete.");
						System.out.println("Cannot delete ticket!"); 	
					}
					//request delete by calling delete records
					id = Integer.parseInt(ticketId); 
					dao.deleteRecords(id); 
					System.out.print(id);
		  
					if (id != 0) { 
						System.out.println("Ticket ID : " + id + " deleted successfully!!!"); JOptionPane.showMessageDialog(null, "Ticket id: " + id + " deleted"); 
						} 
					else
						System.out.println("Ticket cannot be deleted!!!");
		  }
				else {
					JOptionPane.showMessageDialog(null, "Access Denied");
				}
		  }
		
			else if(e.getSource() == mnuItemCloseTicket) {
				
			  
				if (chkIfAdmin == true){
					int id = 0;
					
		  
					//get ticket id user wants to close 
					String ticketId = JOptionPane.showInputDialog(null, "Enter the id of the ticket you want to close");
					//validation check
					if(ticketId == null || (ticketId == null && ticketId.isEmpty())) {
						JOptionPane.showInputDialog(null, "Please select an valid ID to close.");
						System.out.println("Cannot close ticket!"); 	
					}
		  		    //request close by calling close record
					id = Integer.parseInt(ticketId); 
					dao.closeRecord(ticketId); 
					System.out.print(id);
		  
					if (id != 0) { 
						System.out.println("Ticket ID : " + id + " closed successfully!!!"); JOptionPane.showMessageDialog(null, "Ticket id: " + id + " closed"); 
					} 
					else
						System.out.println("Ticket cannot be closed!!!");
		  }
			  else {
				  JOptionPane.showMessageDialog(null, "Access Denied");  
			  }
		  
		  }
		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */
		
	}

}
