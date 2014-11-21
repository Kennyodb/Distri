package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.ejb.EJB;
import session.CarRentalSessionRemote;
import rental.ReservationConstraints;
import rental.Quote;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    
    @EJB
    static CarRentalSessionRemote session;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("found rental companies: "+session.getAllRentalCompanies());
        
        Main.displayMainMenu();
        String input = Main.readLine();
        while(!input.toLowerCase().startsWith("q"))
        {
            int choice = Integer.parseInt(input);
            switch(choice)
            {
                case 1:Main.addReservationConstraint(); break;
                case 2:Main.displayCurrentQuotes(); break;
                case 3:Main.confirmQuotes();break;
                    
                default: break;
            }
            Main.displayMainMenu();
            input = Main.readLine();
        }
        
        System.out.println("Terminating session...");
    }
    
    private static void addReservationConstraint()
    {
        // for now a bogus reservation constraint
        ReservationConstraints constraint = new ReservationConstraints(new Date(), new Date(), "bogusCarType");
        System.out.println("press any key to try to register quote : " + constraint);
        Main.readLine();
        
        if(session.createQuote(constraint) != null)
            System.out.println("Quote registered");
    }
    
    private static void displayCurrentQuotes()
    {
        System.out.println("The current quotes in the system are:");
        for(Quote q : session.getCurrentQuotes())
        {
            System.out.println(q);
        }
    }
    
    private static void confirmQuotes()
    {
        System.out.println("Press <ENTER> to confirm all current quotes.");
        Main.readLine();
        boolean succes = session.confirmQuotes();
        System.out.println("Confirming quotes " + ((succes==true)?"succeeded":"FAILED"));
    }
    
    private static void displayMainMenu()
    {
        System.out.println("Do you wish to \n(1) add another reservation constraint\n(2) display all reservation constraints" + 
                            "\n(3) finalize all reservation constraints\n(Q) quit");
    }
    
    private static String readLine()
    {
       String line = "";
        try {
                BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
                line=buffer.readLine();    
        } catch (IOException ex) 
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return line;
    }
}
