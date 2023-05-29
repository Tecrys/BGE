package bge.data.scripts.world.himmel;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import bge.data.scripts.world.BaseSpawnPoint;
import bge.data.scripts.world.BGEVariantSelector;
// Original script by Cycerin, all credit to him. I merely used it as a template.
@SuppressWarnings("unchecked")
public class BGEhiveSpawnPoint extends BaseSpawnPoint {


	public BGEhiveSpawnPoint(SectorAPI sector, LocationAPI location, 
									float daysInterval, int maxFleets, SectorEntityToken anchor) {
		super(sector, location, daysInterval, maxFleets, anchor);
	}

	
	@Override
	public CampaignFleetAPI spawnFleet() {
		//if ((float) Math.random() < 0.75f) return null;
		String type = "hive";
		
		CampaignFleetAPI fleet = getSector().createFleet("BGE", type);
		BGEVariantSelector.randomizeFleet(fleet);
		getLocation().spawnFleet(getAnchor(), 0, 0, fleet);
		
		fleet.setPreferredResupplyLocation(getAnchor());
		
		fleet.addAssignment(FleetAssignment.DEFEND_LOCATION, getAnchor(), 100000);
		
		return fleet;
	}
	
}






