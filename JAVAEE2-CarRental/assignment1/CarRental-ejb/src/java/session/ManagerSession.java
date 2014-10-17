package session;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class ManagerSession implements ManagerSessionRemote
{

    @Override
    public List<String> getSupportedCarTypes() {
        List<String> toreturn = new ArrayList<String>();
        toreturn.add("type 1");
        toreturn.add("type 2");
        return toreturn;
    }

    @Override
    public String getNumberOfReservationsForCarType(String type) {
        return "0";
    }

    @Override
    public String getNumberOfReservationsForClient(String clientID) {
        return "0";
    }
    
}
