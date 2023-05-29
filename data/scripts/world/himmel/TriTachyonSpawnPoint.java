package bge.data.scripts.world.himmel;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;

import bge.data.scripts.world.BaseSpawnPoint;

public class TriTachyonSpawnPoint extends BaseSpawnPoint {

	private final SectorEntityToken station;

	public TriTachyonSpawnPoint(SectorAPI sector, LocationAPI location, 
							float daysInterval, int maxFleets, SectorEntityToken anchor,
							SectorEntityToken station) {
		super(sector, location, daysInterval, maxFleets, anchor);
		this.station = station;
	}

	@Override
	public CampaignFleetAPI spawnFleet() {
		//if ((float) Math.random() < 0.5f) return null;
		
		String type = null;
		float r = (float) Math.random();
		if (r > .8f) {
			type = "scout";
		} else if (r > 0.5f) {
			type = "raiders";
		} else if (r > 0.2f) {
			type = "attackFleet";
		} else {
			type = "securityDetachment";
		}
		
		CampaignFleetAPI fleet = getSector().createFleet("tritachyon", type);
		getLocation().spawnFleet(getAnchor(), 0, 0, fleet);

		fleet.setPreferredResupplyLocation(getAnchor());
		
		if (type.equals("scout") || type.equals("raiders")) {
			if ((float) Math.random() > 0.75f) {
				fleet.addAssignment(FleetAssignment.RAID_SYSTEM, ((StarSystemAPI)getLocation()).getHyperspaceAnchor(), 30);
				fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, getAnchor(), 1000);
			} else {
				fleet.addAssignment(FleetAssignment.RAID_SYSTEM, null, 30);
				fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, getAnchor(), 1000);
			}
		} else {
			if ((float) Math.random() > 0.3f) {
				fleet.addAssignment(FleetAssignment.ATTACK_LOCATION, station, 50);
				fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, getAnchor(), 1000);
			} else {
				fleet.addAssignment(FleetAssignment.DEFEND_LOCATION, getAnchor(), 20);
				fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, getAnchor(), 1000);
			}
		}
		return fleet;
	}

}
