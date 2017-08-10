import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.MapotempoFleetManager;
import com.mapotempo.fleet.api.MapotempoFleetManagerInterface;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Company;
import com.mapotempo.fleet.core.model.Mission;
import com.mapotempo.fleet.core.model.User;

import java.util.List;
import java.util.Scanner;

/**
 * Created by maxime on 08/08/17.
 */
public class Main {
    public static void main(String [ ] args) throws CoreException
    {
        MapotempoFleetManagerInterface mapotempoFleetManager = MapotempoFleetManager.getManager(new JavaContext(), "static", "static");
        List<Mission> missions = mapotempoFleetManager.getMissionAccess().getAll();

        mapotempoFleetManager.getMissionAccess().addChangeListener(new Access.ChangeListener<Mission>() {
            @Override
            public void changed(List<Mission> items) {
                for (Mission m : items) {
                    System.out.println("-----------------");
                    System.out.println(m.getName());
                    System.out.println(m.getCompanyId());
                    System.out.println(m.getDeliveryDate());
                    System.out.println(m.getLocation());
                }

            }
        });

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
                    System.out.println(m.getAddress());
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
