package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.ejb.EJB;
import session.CarRentalSessionRemote;
import rental.ReservationConstraints;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import session.ManagerSessionRemote;

public class Main extends AbstractScriptedSimpleTripTest{
    
    @EJB
    static CarRentalSessionRemote session;
    
    @EJB
    static ManagerSessionRemote managerSession;
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
                case 1:Main.addReservationConstraintMenu(); break;
                case 2:Main.displayCurrentQuotesMenu(); break;
                case 3:Main.confirmQuotesMenu();break;
                    
                default: break;
            }
            Main.displayMainMenu();
            input = Main.readLine();
        }
        
        System.out.println("Terminating session...");
    }
    
    private static void addReservationConstraintMenu()
    {
        // for now a bogus reservation constraint
        ReservationConstraints constraint = new ReservationConstraints(new Date(), new Date(), "bogusCarType");
        System.out.println("press any key to try to register quote : " + constraint);
        Main.readLine();
        
        String result = session.createQuote(constraint.getStartDate().toString(), 
                            constraint.getEndDate().toString(),
                            constraint.getCarType());
        
        System.out.println("Quote registered as:\n" + result + "\n");
    }
    
    private static void displayCurrentQuotesMenu()
    {
        System.out.println("The current quotes in the system are:");
        for(String q : session.getCurrentQuotes())
        {
            System.out.println(q);
        }
    }
    
    private static void confirmQuotesMenu()
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

    public Main(String scriptFile) {
        super(scriptFile);
    }
    
// TODO REPLACE
    @Override
    protected Object getNewReservationSession(String name) throws Exception {
        return session;
    }

// TODO REPLACE
    @Override
    protected Object getNewManagerSession(String name, String carRentalName) throws Exception {
        return managerSession;
    }

    @Override
    protected void checkForAvailableCarTypes(Object session, Date start, Date end) throws Exception {
        ManagerSessionRemote mSession = (ManagerSessionRemote)session;
        System.out.println(mSession.getSupportedCarTypes());
    }

    @Override
    protected void addQuoteToSession(Object session, String name, Date start, Date end, String carType, String carRentalName) throws Exception {
        
        CarRentalSessionRemote crSession = (CarRentalSessionRemote) session;
        crSession.createQuote(start.toString(), end.toString(), carType);
        
    }

    @Override
    protected void confirmQuotes(Object session, String name) throws Exception {
        CarRentalSessionRemote crSession = (CarRentalSessionRemote) session;
        crSession.confirmQuotes();
    }

    @Override
    protected int getNumberOfReservationsBy(Object ms, String clientName) throws Exception {
        ManagerSessionRemote mSession = (ManagerSessionRemote)session;
        return Integer.parseInt(mSession.getNumberOfReservationsForClient(clientName));
    }

    @Override
    protected int getNumberOfReservationsForCarType(Object ms, String carRentalName, String carType) throws Exception {
        
        ManagerSessionRemote mSession = (ManagerSessionRemote)session;
        return Integer.parseInt(mSession.getNumberOfReservationsForCarType(carType));}
}
