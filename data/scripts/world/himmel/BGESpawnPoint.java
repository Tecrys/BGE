package bge.data.scripts.world.himmel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import bge.data.scripts.world.BGEVariantSelector;
import java.util.*;
import org.lazywizard.lazylib.CollectionUtils;

import bge.data.scripts.world.BaseSpawnPoint;
// Original script by Cycerin, all credit to him. I merely used it as a template.
public class BGESpawnPoint extends BaseSpawnPoint {

	public BGESpawnPoint(SectorAPI sector, LocationAPI location, 
							float daysInterval, int maxFleets, SectorEntityToken anchor) {
		super(sector, location, daysInterval, maxFleets, anchor);
	}

	@Override
	protected CampaignFleetAPI spawnFleet() {
            
		Map potentialFleets = new HashMap();
	potentialFleets.put("scout", 30f);
        potentialFleets.put("securitypat", 20f);
        potentialFleets.put("hunterKillers", 20f);
        potentialFleets.put("bgecarriergroup", 15f);
        potentialFleets.put("strikeforce", 10f);
        potentialFleets.put("warFleet", 5f);		
		
		CampaignFleetAPI fleet = getSector().createFleet("BGE", 
                (String) CollectionUtils.weightedRandom(potentialFleets));
                BGEVariantSelector.randomizeFleet(fleet);
                getLocation().spawnFleet(getAnchor(), 0, 0, fleet);
		fleet.setPreferredResupplyLocation(getAnchor());
		
                StarSystemAPI corvus = Global.getSector().getStarSystem("Corvus");
		if (potentialFleets.equals("scout") || potentialFleets.equals("strikeforce") || potentialFleets.equals("warFleet") || potentialFleets.equals("hunterKillers")) {
			if ((float) Math.random() > 0.5f) {
				fleet.addAssignment(FleetAssignment.RAID_SYSTEM, corvus.getHyperspaceAnchor(), 30);
				fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, getAnchor(), 1000);
			} else {
			    fleet.addAssignment(FleetAssignment.RAID_SYSTEM, corvus.getStar(), 30);
				fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, getAnchor(), 1000);
			}
		} else {
			if ((float) Math.random() > 0.5f) {
				fleet.addAssignment(FleetAssignment.RAID_SYSTEM, null, 30);
				fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, getAnchor(), 1000);
			} else {
				fleet.addAssignment(FleetAssignment.DEFEND_LOCATION, getAnchor(), 20);
				fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, getAnchor(), 1000);
			}
		}
		
		return fleet;
	}

}
