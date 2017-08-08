import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.MapotempoFleetManager;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.model.Company;
import com.mapotempo.fleet.model.Mission;
import com.mapotempo.fleet.model.User;

import java.util.List;
import java.util.Scanner;

/**
 * Created by maxime on 08/08/17.
 */
public class Main {
    public static void main(String [ ] args) throws CoreException
    {
        MapotempoFleetManager mapotempoFleetManager = new MapotempoFleetManager(new JavaContext(), "static", "static");
        List<Mission> missions = mapotempoFleetManager.getMissionAccess().getAll();

        while(true) {
            Scanner keyboard = new Scanner(System.in);
            String nextLine = keyboard.nextLine();
            if (nextLine.equals("")) {
                continue;
            }
            else if(nextLine.equals("company")) {
                Company c = mapotempoFleetManager.getCompany();
                System.out.println(c.getName());
            }
            else if(nextLine.equals("company_up")) {

            }
            else if(nextLine.equals("mission")) {
                for (Mission m : missions) {
                    System.out.println("-----------------");
                    System.out.println(m.getName());
                    System.out.println(m.getCompanyId());
                    System.out.println(m.getDeliveryDate());
                    System.out.println(m.getLocation());
                    //m.setName("toto");
                }
            }
            else if(nextLine.equals("user")) {
                User u = mapotempoFleetManager.getUser();
                System.out.println(u.getName());
            }
            else if(nextLine.equals("all")) {
                //mapotempoFleetManager.mDatabaseHandler.printAllData();
            }
        }

    }
}
