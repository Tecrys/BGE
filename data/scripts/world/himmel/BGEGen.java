package bge.data.scripts.world.himmel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.CoreCampaignPluginImpl;
import java.awt.Color;

// Original script by Cycerin, all credit to him. I merely used it as a template.
@SuppressWarnings("unchecked")
public class BGEGen implements SectorGeneratorPlugin {

   public void generate(SectorAPI sector) {
   
    		StarSystemAPI system = sector.createStarSystem("Himmel");
                system.getLocation().set(-2200, 1200);
		system.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg");

		/*
		 * addPlanet() parameters:
		 * 1. What the planet orbits (orbit is always circular)
		 * 2. Name
		 * 3. Planet type id in planets.json
		 * 4. Starting angle in orbit, i.e. 0 = to the right of the star
		 * 5. Planet radius, pixels at default zoom
		 * 6. Orbit radius, pixels at default zoom
		 * 7. Days it takes to complete an orbit. 1 day = 10 seconds.
		 */		
		
		PlanetAPI himmel = system.initStar("himmel", 450f);
		PlanetAPI mirakel = system.addPlanet(himmel, "Mirakel", "mirakel", 300, 140, 3400, 80);
		PlanetAPI leben = system.addPlanet(mirakel, "Leben", "leben", 30, 50, 500, 25);
		PlanetAPI nephalim = system.addPlanet(himmel, "Nephalim", "arid", 100, 130, 4800, 100);
		PlanetAPI cherubim = system.addPlanet(nephalim, "Cherubim", "barren", 40, 60, 800, 30);
		PlanetAPI krill = system.addPlanet(himmel, "Krill", "krill", 230, 340, 8500, 250);
		PlanetAPI perle = system.addPlanet(krill, "Perle", "frozen", 100, 30, 700, 80);		

		system.addAsteroidBelt(himmel, 50, 1600, 255, 40, 80);
		system.addAsteroidBelt(himmel, 70, 5600, 128, 40, 100);
		system.addAsteroidBelt(krill, 70, 900, 128, 20, 40);		

		system.addRingBand(krill, "misc", "rings1", 256f, 2, Color.red, 256f, 300, 40f);
		system.addRingBand(krill, "misc", "rings1", 256f, 2, Color.red, 256f, 310, 60f);
		system.addRingBand(krill, "misc", "rings1", 256f, 2, Color.red, 256f, 320, 80f);		
                
                SectorEntityToken BGEstation = system.addOrbitalStation(leben, 270, 100, 80, "Genetic Engineering Facility", "BGE");
		initBGECargo(BGEstation);
                
                JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("Jump Point Alpha");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(leben, 0, 500, 30);
		jumpPoint.setOrbit(orbit);
		jumpPoint.setRelatedPlanet(leben);
		jumpPoint.setStandardWormholeToHyperspaceVisual();
		system.addEntity(jumpPoint);
                
                LocationAPI hyper = Global.getSector().getHyperspace();
                system.addSpawnPoint(new BGEConvoySpawnPoint(sector, hyper, 3, 1, hyper.createToken(4000, 3500), BGEstation));

		BGESpawnPoint BGESpawn = new BGESpawnPoint(sector, system, 5, 5, BGEstation );
		system.addSpawnPoint(BGESpawn);
		for (int i = 0; i < 5; i++) BGESpawn.spawnFleet();
		
		TriTachyonSpawnPoint triSpawn = new TriTachyonSpawnPoint(sector, system, 5, 10, system.getEntityByName("Nephalim"), krill);
		system.addScript(triSpawn);
		for (int i = 0; i < 10; i++)
			triSpawn.spawnFleet();
			
                
		system.autogenerateHyperspaceJumpPoints(true, true);
                
                BGEhiveSpawnPoint hiveSpawn = new BGEhiveSpawnPoint(sector, system, 30, 1, BGEstation);
		system.addScript(hiveSpawn);		
                hiveSpawn.spawnFleet();
                
                Global.getSector().setRespawnLocation(Global.getSector().getCurrentLocation());
                Global.getSector().getRespawnCoordinates().set(-2500, -3500);
                
		initFactionRelationships(sector);
		
		sector.registerPlugin(new CoreCampaignPluginImpl());
    }
   
   	private void initBGECargo(SectorEntityToken BGEstation) {
		CargoAPI BGEcargo = BGEstation.getCargo();

		BGEcargo.addWeapons("med_scythe_left", 10);
                BGEcargo.addWeapons("med_scythe_right", 10);		
                BGEcargo.addWeapons("lrg_pincer_right", 5);
                BGEcargo.addWeapons("lrg_pincer_left", 5);					
                BGEcargo.addWeapons("bge_grappler", 16);
                BGEcargo.addWeapons("med_pincer_left", 6);
                BGEcargo.addWeapons("med_pincer_right", 6);					
		BGEcargo.addWeapons("lrg_scythe_left", 8);
		BGEcargo.addWeapons("lrg_scythe_right", 8);			
		BGEcargo.addWeapons("acidthrower", 8);		
				
		
		BGEcargo.addCrew(CargoAPI.CrewXPLevel.ELITE, 10);
		BGEcargo.addCrew(CargoAPI.CrewXPLevel.VETERAN, 50);
		BGEcargo.addCrew(CargoAPI.CrewXPLevel.REGULAR, 500);
		BGEcargo.addMarines(75);
		BGEcargo.addSupplies(645);
		BGEcargo.addFuel(100);

		BGEcargo.addMothballedShip(FleetMemberType.SHIP, "bge_vergifter_Hull", null);
		BGEcargo.addMothballedShip(FleetMemberType.SHIP, "bge_flusskrebs_Hull", null);
		BGEcargo.addMothballedShip(FleetMemberType.SHIP, "bge_waechter_Hull", null);
                BGEcargo.addMothballedShip(FleetMemberType.SHIP, "bge_arbiter_Hull", null);
		BGEcargo.addMothballedShip(FleetMemberType.SHIP, "bge_zophe_Hull", null);
		BGEcargo.addMothballedShip(FleetMemberType.SHIP, "bge_krieger_Hull", null);
	

		BGEcargo.addMothballedShip(FleetMemberType.FIGHTER_WING, "bge_hornisse_standard_wing", null);
		BGEcargo.addMothballedShip(FleetMemberType.FIGHTER_WING, "bge_nymphe_standard_wing", null);

		
	}
   
    private void initFactionRelationships(SectorAPI sector) {
        FactionAPI BGE = sector.getFaction("BGE");
        BGE.setRelationship("hegemony", -1);
        BGE.setRelationship("tritachyon", -1);
        BGE.setRelationship("pirates", -1);
        BGE.setRelationship("independent", 0); 
        BGE.setRelationship("player", 0);
        }
     }