import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.api.ManagerFactory;
import com.mapotempo.fleet.api.MapotempoFleetManagerInterface;
import com.mapotempo.fleet.api.model.CompanyInterface;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.UserInterface;
import com.mapotempo.fleet.api.model.accessor.AccessInterface;
import com.mapotempo.fleet.core.exception.CoreException;

import java.util.List;
import java.util.Scanner;

/**
 * Created by maxime on 08/08/17.
 */
public class Main {
    public static void main(String[] args) throws CoreException {
        MapotempoFleetManagerInterface mMapotempoFleetManager = null;
        ManagerFactory.getManager(new JavaContext(), "static", "static", new MapotempoFleetManagerInterface.OnServerConnexionVerify() {
            @Override
            public void connexion(Status status, MapotempoFleetManagerInterface mapotempoFleetManager) {
                mapotempoFleetManager = mapotempoFleetManager;
            }
        });

        while (mMapotempoFleetManager == null) {

        }

        List<MissionInterface> missions = mMapotempoFleetManager.getMissionAccess().getAll();

        mMapotempoFleetManager.getMissionAccess().addChangeListener(new AccessInterface.ChangeListener<MissionInterface>() {
            @Override
            public void changed(List<MissionInterface> items) {
                for (MissionInterface m : items) {
                    System.out.println("-----------------");
                    System.out.println(m.getName());
                    System.out.println(m.getCompanyId());
                    System.out.println(m.getDate());
                    System.out.println(m.getLocation());
                }
            }
        });

        boolean run = true;

        while (run) {
            Scanner keyboard = new Scanner(System.in);
            String nextLine = keyboard.nextLine();
            if (nextLine.equals("")) {
                continue;
            } else if (nextLine.equals("company")) {
                CompanyInterface c = mMapotempoFleetManager.getCompany();
                System.out.println(c.getName());
            } else if (nextLine.equals("company_up")) {

            } else if (nextLine.equals("mission")) {
                for (MissionInterface m : missions) {
                    System.out.println("-----------------");
                    System.out.println(m.getName());
                    System.out.println(m.getCompanyId());
                    System.out.println(m.getDate());
                    System.out.println(m.getLocation());
                    System.out.println(m.getAddress());
                }
            } else if (nextLine.equals("user")) {
                UserInterface u = mMapotempoFleetManager.getUser();
                System.out.println(u.getSyncUser());
            } else if (nextLine.equals("all")) {
                //mapotempoFleetManager.mDatabaseHandler.printAllData();
            } else if (nextLine.equals("exit")) {
                mMapotempoFleetManager.release();
                run = false;
                continue;
            }
        }

    }
}
