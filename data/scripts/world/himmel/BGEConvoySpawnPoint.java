// chopped and screwed, should fix restock issue and cleaned up some unused code/changed references to deprecated scripts
package bge.data.scripts.world.himmel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoAPI.CrewXPLevel;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import bge.data.scripts.world.BaseSpawnPoint;
import bge.data.scripts.world.BGEVariantSelector;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import org.lazywizard.lazylib.CollectionUtils; <-- deprecated
import com.fs.starfarer.api.util.WeightedRandomPicker; // <-- the new shit
// Original script by Cycerin, all credit to him. I merely used it as a template.
@SuppressWarnings("unchecked")
public class BGEConvoySpawnPoint extends BaseSpawnPoint
{
    private final SectorEntityToken convoyDestination;

    public BGEConvoySpawnPoint(SectorAPI sector, LocationAPI location,
            float daysInterval, int maxFleets, SectorEntityToken anchor,
            SectorEntityToken convoyDestination)
    {
        super(sector, location, daysInterval, maxFleets, anchor);
        this.convoyDestination = convoyDestination;
    }
    private static int convoyNumber = 0;

    @Override
    protected CampaignFleetAPI spawnFleet()
    {

        WeightedRandomPicker potentialFleets = new WeightedRandomPicker();
        potentialFleets.add("fuelConvoy", 2f);
        potentialFleets.add("explorationFleet", 2f);
        potentialFleets.add("supplyConvoy", 2.5f);

		String type = (String) potentialFleets.pick();
        CampaignFleetAPI fleet = getSector().createFleet("BGE", type);
	    BGEVariantSelector.randomizeFleet(fleet);		
        getLocation().spawnFleet(getAnchor(), 0, 0, fleet);

        fleet.setPreferredResupplyLocation(getAnchor());

        addRandomShips(fleet, (int) (Math.random() * 7f) + 4);

        Script script = null;
        if (type == "supplyConvoy")
        {
			CargoAPI cargo = fleet.getCargo();
            addRandomWeapons(cargo, 5);
            script = createArrivedScript(fleet);
            {
                if (fleet.isInCurrentLocation())
                {
                    Global.getSector().getCampaignUI().addMessage("A supply fleet has arrived for Biomancy Genetic Engineering, bound for rendezvous with their station.");
                }
            }
        }

        fleet.addAssignment(FleetAssignment.DELIVER_RESOURCES, convoyDestination, 1000, script);
        fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, getAnchor(), 1000);

        return fleet;
    }

    private Script createArrivedScript(final CampaignFleetAPI fleet)
    {
        return new Script()
        {
            public void run()
            {
                if (fleet.isInCurrentLocation())
                {
                    Global.getSector().getCampaignUI().addMessage("A Biomancy transport fleet has delivered new equipment to their station.");
                }
            }
        };
    }

    private void addRandomWeapons(CargoAPI cargo, int count)
    {
        List weaponIds = getSector().getAllWeaponIds();
        for (int i = 0; i < count; i++)
        {
            String weapon = (String) weapons[(int) (weapons.length * Math.random())];
            int quantity = (int) (Math.random() * 4f + 2f);
            cargo.addWeapons(weapon, quantity);

        }
    }

    private void addRandomShips(CampaignFleetAPI fleet, int count)
    {
        for (int i = 0; i < count; i++)
        {
            if ((float) Math.random() > 0.7f)
            {
                String wing = (String) wings[(int) (wings.length * Math.random())];
                FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.FIGHTER_WING, wing);
                fleet.getFleetData().addFleetMember(member);
                member.getRepairTracker().setMothballed(true);
            }
            else
            {
                String ship = (String) ships[(int) (ships.length * Math.random())];
                FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, ship);
                fleet.getFleetData().addFleetMember(member);
                member.getRepairTracker().setMothballed(true);
            }
        }
    }
///////////////////////HERE INSERT THE LIST OF SHIPS\WINGS\WEAPONS YOU WANT TO BE DELIVERED, I JUST ADD FEW////////////////////
    private static String[] ships =
    {
   "bge_koenig_Hull",
   "bge_waechter_Hull",   
   "bge_arbiter_Hull",  
   "bge_koenig_Hull", 
   "bge_zophe_Hull",
   "bge_flusskrebs_Hull",  
   "bge_vergifter_Hull",
   "bge_krieger_Hull",	
    };
    private static String[] wings =
    {
        "bge_hornisse_standard_wing",
        "bge_nymphe_standard_wing",

    };
    private static String[] weapons =
    {
        "lrg_scythe_right",
        "lrg_scythe_left",
        "med_scythe_left",
        "med_scythe_right",
        "bge_grappler",
        "lrg_pincer_right",
        "lrg_pincer_left",
        "med_pincer_right",
        "med_pincer_left",
        "acidthrower",

		
    };
}