//OperationsTrainsTest.java

package jmri.jmrit.operations.trains;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


import jmri.jmrit.operations.locations.Location;
import jmri.jmrit.operations.locations.Track;
import jmri.jmrit.operations.locations.LocationManager;
import jmri.jmrit.operations.locations.Schedule;
import jmri.jmrit.operations.locations.ScheduleItem;
import jmri.jmrit.operations.locations.ScheduleManager;
import jmri.jmrit.operations.rollingstock.cars.Car;
import jmri.jmrit.operations.rollingstock.cars.CarManager;
import jmri.jmrit.operations.rollingstock.cars.CarTypes;
import jmri.jmrit.operations.rollingstock.engines.Consist;
import jmri.jmrit.operations.rollingstock.engines.Engine;
import jmri.jmrit.operations.rollingstock.engines.EngineTypes;
import jmri.jmrit.operations.rollingstock.engines.EngineManager;
import jmri.jmrit.operations.routes.Route;
import jmri.jmrit.operations.routes.RouteLocation;
import jmri.jmrit.operations.setup.Setup;

import java.util.List;
import jmri.jmrit.operations.rollingstock.cars.CarColors;
import jmri.jmrit.operations.rollingstock.cars.CarLengths;
import jmri.jmrit.operations.rollingstock.cars.CarLoads;
import jmri.jmrit.operations.rollingstock.cars.CarOwners;
import jmri.jmrit.operations.rollingstock.cars.CarRoads;
import jmri.jmrit.operations.rollingstock.cars.Kernel;
import jmri.jmrit.operations.rollingstock.engines.EngineModels;
import jmri.jmrit.operations.routes.RouteManager;

/**
 * Tests for the Operations Trains class
 * Last manually cross-checked on 20090131
 * 
 * Still to do:
 *  Train: DepartureTime, ArrivalTime
 *  Train: numberCarsWorked
 *  Train: isTraininRoute
 *  Train: getBuild, setBuild, buildIfSelected
 *  Train: printBuildReport, printManifest, printReport
 *  Train: getPrint, setPrint, printIfSelected
 *  Train: setTrainIconCoordinates
 *  Train: terminateIfSelected
 *  Train: load/move/get/create Train Icon
 *  Train: get/set Lead Engine
 *  Train: setIconColor
 *  Train: reset
 *  Train: xml read/write
 *  Train: Most build scenarios.
 * 
 *  TrainBuilder: Everything.
 *  TrainSwitchLists: Everything.
 *  
 * @author	Bob Coleman Copyright (C) 2008, 2009
 * @version $Revision: 1.32 $
 */
public class OperationsTrainsTest extends TestCase {

	private final int DIRECTION_ALL = Location.EAST+Location.WEST+Location.NORTH+Location.SOUTH;

	synchronized void releaseThread() {
		try {
			Thread.sleep(20);
			// super.wait(100);
		}
		catch (InterruptedException e) {
			Assert.fail("failed due to InterruptedException");
		}
	}


	// test Train creation
	public void testCreate() {
		Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

		Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
		Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());
	}

	// test Train public constants
	public void testConstants() {
		Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

		Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
		Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

		Assert.assertEquals("Train Constant NONE", 0, Train.NONE);
		Assert.assertEquals("Train Constant CABOOSE", 1, Train.CABOOSE);
		Assert.assertEquals("Train Constant FRED", 2, Train.FRED);

		Assert.assertEquals("Train Constant ALLROADS", "All", Train.ALLROADS);
		Assert.assertEquals("Train Constant INCLUDEROADS", "Include", Train.INCLUDEROADS);
		Assert.assertEquals("Train Constant EXCLUDEROADS", "Exclude", Train.EXCLUDEROADS);

		Assert.assertEquals("Train Constant DISPOSE_CHANGED_PROPERTY", "dispose", Train.DISPOSE_CHANGED_PROPERTY);
		Assert.assertEquals("Train Constant STOPS_CHANGED_PROPERTY", "stops", Train.STOPS_CHANGED_PROPERTY);
		Assert.assertEquals("Train Constant TYPES_CHANGED_PROPERTY", "Types", Train.TYPES_CHANGED_PROPERTY);
		Assert.assertEquals("Train Constant ROADS_CHANGED_PROPERTY", "Road", Train.ROADS_CHANGED_PROPERTY);
		Assert.assertEquals("Train Constant LENGTH_CHANGED_PROPERTY", "length", Train.LENGTH_CHANGED_PROPERTY);
		Assert.assertEquals("Train Constant ENGINELOCATION_CHANGED_PROPERTY", "EngineLocation", Train.ENGINELOCATION_CHANGED_PROPERTY);
		Assert.assertEquals("Train Constant NUMBERCARS_CHANGED_PROPERTY", "numberCarsMoves", Train.NUMBERCARS_CHANGED_PROPERTY);
		Assert.assertEquals("Train Constant STATUS_CHANGED_PROPERTY", "status", Train.STATUS_CHANGED_PROPERTY);
		Assert.assertEquals("Train Constant DEPARTURETIME_CHANGED_PROPERTY", "departureTime", Train.DEPARTURETIME_CHANGED_PROPERTY);
		
		Assert.assertEquals("Train Constant AUTO", "Auto", Train.AUTO);
	}

	// test TrainIcon attributes
	public void testTrainIconAttributes() {
		Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

		Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
		Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());
		Assert.assertEquals("Train toString", "TESTTRAINNAME", train1.toString());

		TrainIcon trainicon1 = new TrainIcon();
		trainicon1.setTrain(train1);
		Assert.assertEquals("TrainIcon set train", "TESTTRAINNAME", trainicon1.getTrain().getName());
	}

	// test Train attributes
	public void testAttributes() {
		Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

		Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
		Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());
		Assert.assertEquals("Train toString", "TESTTRAINNAME", train1.toString());

		train1.setName("TESTNEWNAME");
		Assert.assertEquals("Train New Name", "TESTNEWNAME", train1.getName());
		train1.setComment("TESTCOMMENT");
		Assert.assertEquals("Train Comment", "TESTCOMMENT", train1.getComment());
		train1.setDescription("TESTDESCRIPTION");
		Assert.assertEquals("Train Description", "TESTDESCRIPTION", train1.getDescription());
		train1.setCabooseRoad("TESTCABOOSEROAD");
		Assert.assertEquals("Train Caboose Road", "TESTCABOOSEROAD", train1.getCabooseRoad());
		train1.setEngineModel("TESTENGINEMODEL");
		Assert.assertEquals("Train Engine Model", "TESTENGINEMODEL", train1.getEngineModel());
		train1.setEngineRoad("TESTENGINEROAD");
		Assert.assertEquals("Train Engine Road", "TESTENGINEROAD", train1.getEngineRoad());
		train1.setBuilt(true);
		Assert.assertTrue("Train Built true", train1.getBuilt());
		train1.setBuilt(false);
		Assert.assertFalse("Train Built false", train1.getBuilt());
		train1.setNumberEngines("13");
		Assert.assertEquals("Train Number Engines", "13", train1.getNumberEngines());
		train1.setRoadOption("INCLUDEROADS");
		Assert.assertEquals("Train Road Option INCLUDEROADS", "INCLUDEROADS", train1.getRoadOption());
		train1.setRoadOption("EXCLUDEROADS");
		Assert.assertEquals("Train Road Option EXCLUDEROADS", "EXCLUDEROADS", train1.getRoadOption());
		train1.setRoadOption("ALLROADS");
		Assert.assertEquals("Train Road Option ALLROADS", "ALLROADS", train1.getRoadOption());
		train1.setStatus("TESTSTATUS");
		Assert.assertEquals("Train Status", "TESTSTATUS", train1.getStatus());
		train1.setRequirements(Train.CABOOSE);
		Assert.assertEquals("Train Requirements CABOOSE", 1, train1.getRequirements());
		train1.setRequirements(Train.FRED);
		Assert.assertEquals("Train Requirements FRED", 2, train1.getRequirements());
		train1.setRequirements(Train.NONE);
		Assert.assertEquals("Train Requirements NONE", 0, train1.getRequirements());
		train1.setDepartureTime("12", "55");
		Assert.assertEquals("Train departure hour", "12", train1.getDepartureTimeHour());
		Assert.assertEquals("Train departure minute", "55", train1.getDepartureTimeMinute());
		Assert.assertEquals("Train departure hour and minute", "12:55", train1.getDepartureTime());
	}

	// test Train route
	public void testRoute() {
		Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

		Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
		Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

		Route r1 = new Route("TESTROUTEID", "TESTROUTENAME");

		train1.setRoute(r1);
		Assert.assertEquals("Train Route Name", "TESTROUTENAME", train1.getTrainRouteName());

		Route rnew = new Route("TESTROUTEID2", "TESTNEWROUTENAME");

		Location l1 = new Location("TESTLOCATIONID1", "TESTNEWROUTEDEPTNAME");
		rnew.addLocation(l1);
		Location l2 = new Location("TESTLOCATIONID2", "TESTLOCATIONNAME2");
		rnew.addLocation(l2);
		Location l3 = new Location("TESTLOCATIONID3", "TESTNEWROUTECURRNAME");
		rnew.addLocation(l3);
		Location l4 = new Location("TESTLOCATIONID4", "TESTLOCATIONNAME4");
		rnew.addLocation(l4);
		Location l5 = new Location("TESTLOCATIONID5", "TESTNEWROUTETERMNAME");
		rnew.addLocation(l5);

		train1.setRoute(rnew);
		Assert.assertEquals("Train New Route Name", "TESTNEWROUTENAME", train1.getTrainRouteName());

		Assert.assertEquals("Train New Route Departure Name", "TESTNEWROUTEDEPTNAME", train1.getTrainDepartsName());
		Assert.assertEquals("Train New Route Terminates Name", "TESTNEWROUTETERMNAME", train1.getTrainTerminatesName());

		RouteLocation rl1test;
		rl1test= rnew.getLastLocationByName("TESTNEWROUTECURRNAME");
		train1.setCurrentLocation(rl1test);
		Assert.assertEquals("Train New Route Current Name", "TESTNEWROUTECURRNAME", train1.getCurrentLocationName());
		rl1test= train1.getCurrentLocation();
		Assert.assertEquals("Train New Route Current Name by Route Location", "TESTNEWROUTECURRNAME", rl1test.getName());
	}

	// test Train skip locations support
	public void testSkipLocations() {
		Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

		Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
		Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

		train1.addTrainSkipsLocation("TESTLOCATIONID2");
		Assert.assertTrue("Location 2 to be skipped", train1.skipsLocation("TESTLOCATIONID2"));

		train1.addTrainSkipsLocation("TESTLOCATIONID4");
		Assert.assertTrue("Location 4 to be skipped", train1.skipsLocation("TESTLOCATIONID4"));

		train1.deleteTrainSkipsLocation("TESTLOCATIONID2");
		Assert.assertFalse("Location 2 not to be skipped", train1.skipsLocation("TESTLOCATIONID2"));
		Assert.assertTrue("Location 4 still to be skipped", train1.skipsLocation("TESTLOCATIONID4"));

		train1.deleteTrainSkipsLocation("TESTLOCATIONID4");
		Assert.assertFalse("Location 2 still not to be skipped", train1.skipsLocation("TESTLOCATIONID2"));
		Assert.assertFalse("Location 4 not to be skipped", train1.skipsLocation("TESTLOCATIONID4"));
	}

	// test Train accepts types support
	public void testAcceptsTypes() {
		Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

		Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
		Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

		train1.addTypeName("Caboose");
		Assert.assertTrue("Train accepts type name Caboose", train1.acceptsTypeName("Caboose"));
		Assert.assertFalse("Train does not accept type name Hopper", train1.acceptsTypeName("Hopper"));

		train1.addTypeName("Hopper");
		Assert.assertTrue("Train still accepts type name Caboose", train1.acceptsTypeName("Caboose"));
		Assert.assertTrue("Train accepts type name Hopper", train1.acceptsTypeName("Hopper"));

		train1.deleteTypeName("Caboose");
		Assert.assertFalse("Train no longer accepts type name Caboose", train1.acceptsTypeName("Caboose"));
		Assert.assertTrue("Train still accepts type name Hopper", train1.acceptsTypeName("Hopper"));
	}

	// test train accepts road names support
	public void testAcceptsRoadNames() {
		Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

		Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
		Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

		train1.setRoadOption(Train.ALLROADS);
		Assert.assertTrue("Train accepts (ALLROADS) Road name CP", train1.acceptsRoadName("CP"));
		Assert.assertTrue("Train accepts (ALLROADS) Road name VIA", train1.acceptsRoadName("VIA"));

		train1.setRoadOption(Train.INCLUDEROADS);
		train1.addRoadName("CP");
		Assert.assertTrue("Train accepts (INCLUDEROADS) Road name CP", train1.acceptsRoadName("CP"));
		Assert.assertFalse("Train does not accept (INCLUDEROADS) Road name VIA", train1.acceptsRoadName("VIA"));

		train1.addRoadName("VIA");
		Assert.assertTrue("Train still accepts (INCLUDEROADS) Road name CP", train1.acceptsRoadName("CP"));
		Assert.assertTrue("Train accepts (INCLUDEROADS) Road name VIA", train1.acceptsRoadName("VIA"));

		train1.deleteRoadName("CP");
		Assert.assertFalse("Train no longer accepts (INCLUDEROADS) Road name CP", train1.acceptsRoadName("CP"));
		Assert.assertTrue("Train still accepts (INCLUDEROADS) Road name VIA", train1.acceptsRoadName("VIA"));

		train1.setRoadOption(Train.EXCLUDEROADS);
		Assert.assertTrue("Train does accept (EXCLUDEROADS) Road name CP", train1.acceptsRoadName("CP"));
		Assert.assertFalse("Train does not accept (EXCLUDEROADS) Road name VIA", train1.acceptsRoadName("VIA"));

		train1.addRoadName("CP");
		Assert.assertFalse("Train does not accept (EXCLUDEROADS) Road name CP", train1.acceptsRoadName("CP"));
		Assert.assertFalse("Train still does not accept (EXCLUDEROADS) Road name VIA", train1.acceptsRoadName("VIA"));

		train1.deleteRoadName("VIA");
		Assert.assertFalse("Train still does not accepts (EXCLUDEROADS) Road name CP", train1.acceptsRoadName("CP"));
		Assert.assertTrue("Train now accepts (EXCLUDEROADS) Road name VIA", train1.acceptsRoadName("VIA"));
	}

	// test train staging to staging
	public void testStagingtoStaging() {
		TrainManager tmanager = TrainManager.instance();
		RouteManager rmanager = RouteManager.instance();
		LocationManager lmanager = LocationManager.instance();
		EngineManager emanager = EngineManager.instance();
		CarManager cmanager = CarManager.instance();
		CarTypes ct = CarTypes.instance();
		EngineTypes et = EngineTypes.instance();

		// register the car and engine types used
		ct.addName("Boxcar");
		ct.addName("Caboose");
		ct.addName("Flat");
		et.addName("Diesel");

		// Set up four engines in two consists 
		Consist con1 = new Consist("C16");
		Consist con2 = new Consist("C14");

		Engine e1 = new Engine("CP", "5016");
		e1.setModel("GP40");
		e1.setConsist(con1);
		e1.setMoves(123);
		Assert.assertEquals("Engine 1 Length", "59", e1.getLength());
		emanager.register(e1);

		Engine e2 = new Engine("CP", "5019");
		e2.setModel("GP40");
		e2.setConsist(con1);
		e2.setMoves(321);
		Assert.assertEquals("Engine 2 Length", "59", e2.getLength());
		emanager.register(e2);

		Engine e3 = new Engine("CP", "5524");
		e3.setModel("SD45");
		e3.setConsist(con2);
		Assert.assertEquals("Engine 3 Length", "66", e3.getLength());
		emanager.register(e3);

		Engine e4 = new Engine("CP", "5559");
		e4.setModel("SD45");
		e4.setConsist(con2);
		Assert.assertEquals("Engine 4 Length", "66", e4.getLength());
		emanager.register(e4);

		// Set up two cabooses and six box cars
		Car c1 = new Car("CP", "C10099");
		c1.setType("Caboose");
		c1.setLength("32");
		c1.setMoves(23);
		c1.setCaboose(true);
		Assert.assertEquals("Caboose 1 Length", "32", c1.getLength());
		cmanager.register(c1);

		Car c2 = new Car("CP", "C20099");
		c2.setType("Caboose");
		c2.setLength("32");
		c2.setMoves(54);
		c2.setCaboose(true);
		Assert.assertEquals("Caboose 2 Length", "32", c2.getLength());
		cmanager.register(c2);

		Car c3 = new Car("CP", "X10001");
		c3.setType("Boxcar");
		c3.setLength("40");
		Assert.assertEquals("Box Car X10001 Length", "40", c3.getLength());
		cmanager.register(c3);

		Car c4 = new Car("CP", "X10002");
		c4.setType("Boxcar");
		c4.setLength("40");
		c4.setMoves(4444);
		Assert.assertEquals("Box Car X10002 Length", "40", c4.getLength());
		cmanager.register(c4);

		Car c5 = new Car("CP", "X20001");
		c5.setType("Boxcar");
		c5.setLength("40");
		Assert.assertEquals("Box Car X20001 Length", "40", c5.getLength());
		cmanager.register(c5);

		Car c6 = new Car("CP", "X20002");
		c6.setType("Boxcar");
		c6.setLength("40");
		Assert.assertEquals("Box Car X20002 Length", "40", c6.getLength());
		cmanager.register(c6);
		
		Car c7 = new Car("CP", "777");
		c7.setType("Boxcar");
		c7.setLength("50");
		// should not pick this car up, moves = 6 vs 0 for c8
		c7.setMoves(6);	
		Assert.assertEquals("Box Car 777 Length", "50", c7.getLength());
		cmanager.register(c7);
		
		Car c8 = new Car("CP", "888");
		c8.setType("Boxcar");
		c8.setLength("60");
		Assert.assertEquals("Box Car 888 Length", "60", c8.getLength());
		cmanager.register(c8);
		
		Car c9 = new Car("CP", "99");
		c9.setType("Flat");
		c9.setLength("90");
		Assert.assertEquals("Box Car 888 Length", "90", c9.getLength());
		cmanager.register(c9);
		
		// do cars have the right default loads?
		Assert.assertEquals("Car c1 load should be E", "E", c1.getLoad());
		Assert.assertEquals("Car c2 load should be E", "E", c2.getLoad());
		Assert.assertEquals("Car c3 load should be E", "E", c3.getLoad());
		Assert.assertEquals("Car c4 load should be E", "E", c4.getLoad());
		Assert.assertEquals("Car c5 load should be E", "E", c5.getLoad());
		Assert.assertEquals("Car c6 load should be E", "E", c6.getLoad());
		Assert.assertEquals("Car c7 load should be E", "E", c7.getLoad());
		Assert.assertEquals("Car c8 load should be E", "E", c8.getLoad());
		Assert.assertEquals("Car c9 load should be E", "E", c9.getLoad());

		// Set up a route of 3 locations: North End Staging (2 tracks), 
		// North Industries (1 track), and South End Staging (2 tracks).
		Location l1 = new Location("1", "North End");
		Assert.assertEquals("Location 1 Id", "1", l1.getId());
		Assert.assertEquals("Location 1 Name", "North End", l1.getName());
		Assert.assertEquals("Location 1 Initial Length", 0, l1.getLength());
		l1.setLocationOps(Location.STAGING);
		l1.setTrainDirections(DIRECTION_ALL);
		l1.setSwitchList(true);
		l1.addTypeName("Diesel");
		l1.addTypeName("Boxcar");
		l1.addTypeName("Caboose");
		lmanager.register(l1);

		Track l1s1 = new Track("1s1", "North End 1", Track.STAGING);
		l1s1.setLength(300);
		Assert.assertEquals("Location 1s1 Id", "1s1", l1s1.getId());
		Assert.assertEquals("Location 1s1 Name", "North End 1", l1s1.getName());
		Assert.assertEquals("Location 1s1 LocType", "Staging", l1s1.getLocType());
		Assert.assertEquals("Location 1s1 Length", 300, l1s1.getLength());
		l1s1.setTrainDirections(DIRECTION_ALL);
		l1s1.addTypeName("Diesel");
		l1s1.addTypeName("Boxcar");
		l1s1.addTypeName("Caboose");
		l1s1.setRoadOption(Track.ALLROADS);
		l1s1.setDropOption(Track.ANY);
		l1s1.setPickupOption(Track.ANY);

		Track l1s2 = new Track("1s2", "North End 2", Track.STAGING);
		l1s2.setLength(400);
		Assert.assertEquals("Location 1s2 Id", "1s2", l1s2.getId());
		Assert.assertEquals("Location 1s2 Name", "North End 2", l1s2.getName());
		Assert.assertEquals("Location 1s2 LocType", "Staging", l1s2.getLocType());
		Assert.assertEquals("Location 1s2 Length", 400, l1s2.getLength());
		l1s2.setTrainDirections(DIRECTION_ALL);
		l1s2.addTypeName("Diesel");
		l1s2.addTypeName("Boxcar");
		l1s2.addTypeName("Caboose");
		l1s2.setRoadOption(Track.ALLROADS);
		l1s2.setDropOption(Track.ANY);
		l1s2.setPickupOption(Track.ANY);

		l1.addTrack("North End 1", Track.STAGING);
		l1.addTrack("North End 2", Track.STAGING);
		List<String> templist1 = l1.getTracksByNameList("");
		for (int i = 0; i < templist1.size(); i++){
			if (i == 0) {
				Assert.assertEquals("RL 1 Staging 1 Name", "North End 1", templist1.get(i));
			}
			if (i == 1) {
				Assert.assertEquals("RL 1 Staging 2 Name", "North End 2", templist1.get(i));
			}
		}

		l1.register(l1s1);
		l1.register(l1s2);

		Assert.assertEquals("Location 1 Length", 700, l1.getLength());

		Location l2 = new Location("2", "North Industries");
		Assert.assertEquals("Location 2 Id", "2", l2.getId());
		Assert.assertEquals("Location 2 Name", "North Industries", l2.getName());
		l2.setLocationOps(Location.NORMAL);
		l2.setTrainDirections(DIRECTION_ALL);
		l2.setSwitchList(true);
		l2.addTypeName("Diesel");
		l2.addTypeName("Boxcar");
		l2.addTypeName("Caboose");
		l2.addTypeName("Flat");
		lmanager.register(l2);
				
		Track l2s1 = new Track("2s1", "NI Yard", Track.YARD);
		l2s1.setLength(432);
		Assert.assertEquals("Location 2s1 Id", "2s1", l2s1.getId());
		Assert.assertEquals("Location 2s1 Name", "NI Yard", l2s1.getName());
		Assert.assertEquals("Location 2s1 LocType", Track.YARD, l2s1.getLocType());
		Assert.assertEquals("Location 2s1 Length", 432, l2s1.getLength());
		l2s1.setTrainDirections(DIRECTION_ALL);
		l2s1.addTypeName("Boxcar");
		l2s1.addTypeName("Flat");
		
		l2.register(l2s1);
		Assert.assertEquals("Location 2 Length", 432, l2.getLength());

		Location l3 = new Location("3", "South End");
		Assert.assertEquals("Location 3 Id", "3", l3.getId());
		Assert.assertEquals("Location 3 Name", "South End", l3.getName());
		Assert.assertEquals("Location 3 Initial Length", 0, l3.getLength());
		l3.setLocationOps(Location.STAGING);
		l3.setTrainDirections(DIRECTION_ALL);
		l3.setSwitchList(true);
		l3.addTypeName("Diesel");
		l3.addTypeName("Boxcar");
		l3.addTypeName("Flat");
		l3.addTypeName("Caboose");
		lmanager.register(l3);

		Track l3s1 = new Track("3s1", "South End 1", Track.STAGING);
		l3s1.setLength(300);
		Assert.assertEquals("Location 3s1 Id", "3s1", l3s1.getId());
		Assert.assertEquals("Location 3s1 Name", "South End 1", l3s1.getName());
		Assert.assertEquals("Location 3s1 LocType", "Staging", l3s1.getLocType());
		Assert.assertEquals("Location 3s1 Length", 300, l3s1.getLength());
		l3s1.setTrainDirections(DIRECTION_ALL);
		l3s1.addTypeName("Diesel");
		l3s1.addTypeName("Boxcar");
		l3s1.addTypeName("Caboose");
		l3s1.addTypeName("Flat");
		l3s1.setRoadOption(Track.ALLROADS);
		l3s1.setDropOption(Track.ANY);
		l3s1.setPickupOption(Track.ANY);

		Track l3s2 = new Track("3s2", "South End 2", Track.STAGING);
		l3s2.setLength(400);
		Assert.assertEquals("Location 3s2 Id", "3s2", l3s2.getId());
		Assert.assertEquals("Location 3s2 Name", "South End 2", l3s2.getName());
		Assert.assertEquals("Location 3s2 LocType", "Staging", l3s2.getLocType());
		Assert.assertEquals("Location 3s2 Length", 400, l3s2.getLength());
		l3s2.setTrainDirections(DIRECTION_ALL);
		l3s2.addTypeName("Diesel");
		l3s2.addTypeName("Boxcar");
		l3s2.addTypeName("Caboose");
		l3s2.addTypeName("Flat");
		l3s2.setRoadOption(Track.ALLROADS);
		l3s2.setDropOption(Track.ANY);
		l3s2.setPickupOption(Track.ANY);

		l3.addTrack("South End 1", Track.STAGING);
		l3.addTrack("South End 2", Track.STAGING);
		List<String> templist3 = l3.getTracksByNameList("");
		for (int i = 0; i < templist3.size(); i++){
			if (i == 0) {
				Assert.assertEquals("RL 3 Staging 1 Name", "South End 1", templist3.get(i));
			}
			if (i == 1) {
				Assert.assertEquals("RL 3 Staging 2 Name", "South End 2", templist3.get(i));
			}
		}

		l3.register(l3s1);
		l3.register(l3s2);

		Assert.assertEquals("Location 3 Length", 700, l3.getLength());

		// Place Engines on Staging tracks
		Assert.assertEquals("Location 1s1 Init Used Length", 0, l1s1.getUsedLength());
		Assert.assertEquals("Location 1 Init Used Length", 0, l1s1.getUsedLength());
		Assert.assertEquals("Place e1", Engine.OKAY, e1.setLocation(l1, l1s1));
		Assert.assertEquals("Location 1s1 e1 Used Length", 63, l1s1.getUsedLength());
		Assert.assertEquals("Location 1 e1 Used Length", 63, l1.getUsedLength());
		Assert.assertEquals("Place e2", Engine.OKAY, e2.setLocation(l1, l1s1));
		Assert.assertEquals("Location 1s1 e2 Used Length", 126, l1s1.getUsedLength());
		Assert.assertEquals("Location 1 e2 Used Length", 126, l1.getUsedLength());

		Assert.assertEquals("Location 1s2 Init Used Length", 0, l1s2.getUsedLength());
		Assert.assertEquals("Place e3", Engine.OKAY, e3.setLocation(l1, l1s2));
		Assert.assertEquals("Location 1s2 e3 Used Length", 70, l1s2.getUsedLength());
		Assert.assertEquals("Location 1 e3 Used Length", 196, l1.getUsedLength());
		Assert.assertEquals("Place e4", Engine.OKAY, e4.setLocation(l1, l1s2));
		Assert.assertEquals("Location 1s2 e4 Used Length", 140, l1s2.getUsedLength());
		Assert.assertEquals("Location 1 e4 Used Length", 266, l1.getUsedLength());

		// Place 4 Boxcars on Staging tracks
		Assert.assertTrue("l1 Accepts Boxcar", l1.acceptsTypeName("Boxcar"));
		Assert.assertTrue("l1s1 Accepts Boxcar", l1s1.acceptsTypeName("Boxcar"));

		Assert.assertEquals("Place c3", Car.OKAY, c3.setLocation(l1, l1s1));
		Assert.assertEquals("Location 1s1 c3 Used Length", 170, l1s1.getUsedLength());
		Assert.assertEquals("Location 1 c3 Used Length", 310, l1.getUsedLength());
		Assert.assertEquals("Place c4", Car.OKAY, c4.setLocation(l1, l1s1));
		Assert.assertEquals("Location 1s1 c4 Used Length", 214, l1s1.getUsedLength());
		Assert.assertEquals("Location 1 c4 Used Length", 354, l1.getUsedLength());

		Assert.assertEquals("Place c5", Car.OKAY, c5.setLocation(l1, l1s2));
		Assert.assertEquals("Location 1s2 c5 Used Length", 184, l1s2.getUsedLength());
		Assert.assertEquals("Location 1 c5 Used Length", 398, l1.getUsedLength());
		Assert.assertEquals("Place c6", Car.OKAY, c6.setLocation(l1, l1s2));
		Assert.assertEquals("Location 1s2 c6 Used Length", 228, l1s2.getUsedLength());
		Assert.assertEquals("Location 1 c6 Used Length", 442, l1.getUsedLength());
		
		// Place 2 Boxcars and Flat in yard
		Assert.assertEquals("Place c7", Car.OKAY, c7.setLocation(l2, l2s1));
		Assert.assertEquals("Location 2s1 c7 Used Length", 54, l2s1.getUsedLength());
		Assert.assertEquals("Location 2 c7 Used Length", 54, l2.getUsedLength());
		Assert.assertEquals("Place c8", Car.OKAY, c8.setLocation(l2, l2s1));
		Assert.assertEquals("Location 2s1 c4 Used Length", 118, l2s1.getUsedLength());
		Assert.assertEquals("Location 2 c4 Used Length", 118, l2.getUsedLength());
		Assert.assertEquals("Place c9", Car.OKAY, c9.setLocation(l2, l2s1));
		Assert.assertEquals("Location 2s1 c9 Used Length", 212, l2s1.getUsedLength());
		Assert.assertEquals("Location 2 c9 Used Length", 212, l2.getUsedLength());

		
		// Place Cabooses on Staging tracks
		Assert.assertEquals("Place c1", Car.OKAY, c1.setLocation(l1, l1s1));
		Assert.assertEquals("Location 1s1 c1 Used Length", 250, l1s1.getUsedLength());
		Assert.assertEquals("Location 1 c1 Used Length", 478, l1.getUsedLength());

		Assert.assertEquals("Place c2", Car.OKAY, c2.setLocation(l1, l1s2));
		Assert.assertEquals("Location 1s2 c2 Used Length", 264, l1s2.getUsedLength());
		Assert.assertEquals("Location 1 c2 Used Length", 514, l1.getUsedLength());

		// Define the route.
		Route r1 = new Route("1", "Southbound Main Route");
		Assert.assertEquals("Route Id", "1", r1.getId());
		Assert.assertEquals("Route Name", "Southbound Main Route", r1.getName());

		RouteLocation rl1 = new RouteLocation("1r1", l1);
		rl1.setSequenceId(1);
		rl1.setTrainDirection(RouteLocation.SOUTH);
		rl1.setMaxCarMoves(5);
		rl1.setMaxTrainLength(1000);
		Assert.assertEquals("Route Location 1 Id", "1r1", rl1.getId());
		Assert.assertEquals("Route Location 1 Name", "North End", rl1.getName());
		RouteLocation rl2 = new RouteLocation("1r2", l2);
		rl2.setSequenceId(2);
		rl2.setTrainDirection(RouteLocation.SOUTH);
		// test for only 1 pickup and 1 drop
		rl2.setMaxCarMoves(2);
		rl2.setMaxTrainLength(1000);
		Assert.assertEquals("Route Location 2 Id", "1r2", rl2.getId());
		Assert.assertEquals("Route Location 2 Name", "North Industries", rl2.getName());
		RouteLocation rl3 = new RouteLocation("1r3", l3);
		rl3.setSequenceId(3);
		rl3.setTrainDirection(RouteLocation.SOUTH);
		rl3.setMaxCarMoves(5);
		rl3.setMaxTrainLength(1000);
		Assert.assertEquals("Route Location 3 Id", "1r3", rl3.getId());
		Assert.assertEquals("Route Location 3 Name", "South End", rl3.getName());

		r1.register(rl1);
		r1.register(rl2);
		r1.register(rl3);

		rmanager.register(r1);

		// Finally ready to define the train.
		Train train1 = new Train("1", "Southbound Through Freight");
		Assert.assertEquals("Train Id", "1", train1.getId());
		Assert.assertEquals("Train Name", "Southbound Through Freight", train1.getName());
		train1.setEngineRoad("CP");
		train1.setEngineModel("GP40");
		train1.setNumberEngines("2");
		train1.setRequirements(Train.CABOOSE);
		train1.setCabooseRoad("CP");
		train1.addTypeName("Caboose");
		train1.addTypeName("Boxcar");
		train1.addTypeName("Diesel");
		train1.setRoadOption("All");
		train1.setRoute(r1);
		train1.setDepartureTime("6", "5");
		tmanager.register(train1);
		
		Train train2 = new Train("2", "Southbound Fast Freight");
		Assert.assertEquals("Train Id", "2", train2.getId());
		Assert.assertEquals("Train Name", "Southbound Fast Freight", train2.getName());
		train2.addTypeName("Diesel");
		// this should fail, there are two engines in staging
		train2.setNumberEngines("1");
		train2.setRoute(r1);
		train2.setDepartureTime("22", "45");
		tmanager.register(train2);

		//  Last minute checks.
		Assert.assertEquals("Train 1 Departs Name", "North End", train1.getTrainDepartsName());
		Assert.assertEquals("Train 1 Route Departs Name", "North End", train1.getTrainDepartsRouteLocation().getName());
		Assert.assertEquals("Train 1 Terminates Name", "South End", train1.getTrainTerminatesName());
		Assert.assertEquals("Train 1 Route Terminates Name", "South End", train1.getTrainTerminatesRouteLocation().getName());
		Assert.assertEquals("Train 1 Next Location Name", "", train1.getNextLocationName());
		Assert.assertEquals("Train 1 Route Name", "Southbound Main Route", train1.getRoute().getName());

		//  Build the train!!
		train1.build(false);
		Assert.assertEquals("Train 1 After Build Departs Name", "North End", train1.getTrainDepartsName());
		Assert.assertEquals("Train 1 After Build Terminates Name", "South End", train1.getTrainTerminatesName());
		Assert.assertEquals("Train 1 After Build Next Location Name", "North Industries", train1.getNextLocationName());
		Assert.assertEquals("Train 1 After Build Built Status", true, train1.getBuilt());
		
		// Are the proper engines and cars assigned to train 1?
		Assert.assertEquals("Engine e1 After Build should be assigned to Train 1", train1, e1.getTrain());
		Assert.assertEquals("Engine e2 After Build should be assigned to Train 1", train1, e2.getTrain());
		Assert.assertEquals("Car c1 After Build should be assigned to Train 1", train1, c1.getTrain());
		Assert.assertEquals("Car c3 After Build should be assigned to Train 1", train1, c3.getTrain());
		Assert.assertEquals("Car c4 After Build should be assigned to Train 1", train1, c4.getTrain());
		Assert.assertEquals("Car c8 After Build should be assigned to Train 1", train1, c8.getTrain());
		
		// Are the engine and car destinations correct?
		Assert.assertEquals("Engine e1 After Build destination", "South End", e1.getDestinationName());
		Assert.assertEquals("Engine e2 After Build destination", "South End", e2.getDestinationName());
		Assert.assertEquals("Car c1 After Build destination", "South End", c1.getDestinationName());
		Assert.assertEquals("Car c3 After Build destination", "North Industries", c3.getDestinationName());
		Assert.assertEquals("Car c4 After Build destination", "South End", c4.getDestinationName());
		Assert.assertEquals("Car c8 After Build destination", "South End", c8.getDestinationName());
		
		// Are the engine and car destination track correct?
		Assert.assertEquals("Engine e1 After Build track", "South End 1", e1.getDestinationTrackName());
		Assert.assertEquals("Engine e2 After Build track", "South End 1", e2.getDestinationTrackName());
		Assert.assertEquals("Car c1 After Build track", "South End 1", c1.getDestinationTrackName());
		Assert.assertEquals("Car c3 After Build track", "NI Yard", c3.getDestinationTrackName());
		Assert.assertEquals("Car c4 After Build track", "South End 1", c4.getDestinationTrackName());
		Assert.assertEquals("Car c8 After Build track", "South End 1", c8.getDestinationTrackName());
	
		// Are the location pickup and drop counts correct?
		Assert.assertEquals("Drop count for North End", 0, l1.getDropRS());  
		Assert.assertEquals("Drop count for North Industries", 1, l2.getDropRS()); 
		Assert.assertEquals("Drop count for South End", 5, l3.getDropRS()); 
		Assert.assertEquals("Pickup count for North End", 5, l1.getPickupRS());  
		Assert.assertEquals("Pickup count for North Industries", 1, l2.getPickupRS()); 
		Assert.assertEquals("Pickup count for South End", 0, l3.getPickupRS()); 
		
		// Are the track pickup and drop counts correct?
		Assert.assertEquals("Drop count for North End, track North End 1", 0, l1s1.getDropRS()); 
		Assert.assertEquals("Drop count for North End, track North End 2", 0, l1s2.getDropRS()); 
		Assert.assertEquals("Pickup count for North End, track North End 1", 5, l1s1.getPickupRS()); 
		Assert.assertEquals("Pickup count for North End, track North End 2", 0, l1s2.getPickupRS()); 
		Assert.assertEquals("Drop count for North Industries, track NI Yard", 1, l2s1.getDropRS()); 
		Assert.assertEquals("Pickup count for North Industries, track NI Yard", 1, l2s1.getDropRS()); 
		Assert.assertEquals("Drop count for South End, track South End 1", 5, l3s1.getDropRS()); 
		Assert.assertEquals("Drop count for South End, track South End 2", 0, l3s2.getDropRS()); 
		Assert.assertEquals("Pickup count for South End, track South End 1", 0, l3s1.getPickupRS()); 
		Assert.assertEquals("Pickup count for South End, track South End 2", 0, l3s2.getPickupRS()); 

		// Are the other engines and cars NOT assigned to train 1?
		Assert.assertEquals("Engine e3 After Build should NOT be assigned to Train 1", null, e3.getTrain());
		Assert.assertEquals("Engine e4 After Build should NOT be assigned to Train 1", null, e4.getTrain());
		Assert.assertEquals("Car c2 After Build should NOT be assigned to Train 1", null, c2.getTrain());
		Assert.assertEquals("Car c5 After Build should NOT be assigned to Train 1", null, c5.getTrain());
		Assert.assertEquals("Car c6 After Build should NOT be assigned to Train 1", null, c6.getTrain());
		Assert.assertEquals("Car c7 After Build should NOT be assigned to Train 1", null, c7.getTrain());
		Assert.assertEquals("Car c9 After Build should NOT be assigned to Train 1", null, c9.getTrain());
		
		// Check expected arrival times
		Assert.assertEquals("Train 1 expected departure time", "06:05", train1.getExpectedArrivalTime(rl1));
		// Check time for car moves and train travel times
		Assert.assertEquals("Per Car move time", 11, Setup.getSwitchTime());
		Assert.assertEquals("Train travel time", 111, Setup.getTravelTime());
		
		Assert.assertEquals("Train 1 expected North End", "07:56", train1.getExpectedArrivalTime(rl2));
		// one car dropped and one is picked up at North End, so travel time + two car moves
		Assert.assertEquals("Train 1 expected North Industries", "10:09", train1.getExpectedArrivalTime(rl3));

		// Reset the train!
		Assert.assertEquals("Train 1 Reset should be true", true, train1.reset());
		Assert.assertEquals("Train 1 After Reset Departs Name", "North End", train1.getTrainDepartsName());
		Assert.assertEquals("Train 1 After Reset Terminates Name", "South End", train1.getTrainTerminatesName());
		Assert.assertEquals("Train 1 After Reset Next Location Name", "", train1.getNextLocationName());
		Assert.assertEquals("Train 1 After Reset Built Status", false, train1.getBuilt());
		
		// Are the engines and cars released from train 1?
		Assert.assertEquals("Engine e1 After Reset should NOT be assigned to Train 1", null, e1.getTrain());
		Assert.assertEquals("Engine e2 After Reset should NOT be assigned to Train 1", null, e2.getTrain());
		Assert.assertEquals("Car c1 After Reset should NOT be assigned to Train 1", null, c1.getTrain());
		Assert.assertEquals("Car c3 After Reset should NOT be assigned to Train 1", null, c3.getTrain());
		Assert.assertEquals("Car c4 After Reset should NOT be assigned to Train 1", null, c4.getTrain());
		Assert.assertEquals("Car c8 After Reset should NOT be assigned to Train 1", null, c8.getTrain());
		
		// Are the location pickup and drop counts correct?
		Assert.assertEquals("Reset Drop count for North End", 0, l1.getDropRS());  
		Assert.assertEquals("Reset Drop count for North Industries", 0, l2.getDropRS()); 
		Assert.assertEquals("Reset Drop count for South End", 0, l3.getDropRS()); 
		Assert.assertEquals("Reset Pickup count for North End", 0, l1.getPickupRS());  
		Assert.assertEquals("Reset Pickup count for North Industries", 0, l2.getPickupRS()); 
		Assert.assertEquals("Reset Pickup count for South End", 0, l3.getPickupRS()); 

		// Are the track pickup and drop counts correct?
		Assert.assertEquals("Reset Drop count for North End, track North End 1", 0, l1s1.getDropRS()); 
		Assert.assertEquals("Reset Drop count for North End, track North End 2", 0, l1s2.getDropRS()); 
		Assert.assertEquals("Reset Pickup count for North End, track North End 1", 0, l1s1.getPickupRS()); 
		Assert.assertEquals("Reset Pickup count for North End, track North End 2", 0, l1s2.getPickupRS()); 
		Assert.assertEquals("Reset Drop count for North Industries, track NI Yard", 0, l2s1.getDropRS()); 
		Assert.assertEquals("Reset Pickup count for North Industries, track NI Yard", 0, l2s1.getDropRS()); 
		Assert.assertEquals("Reset Drop count for South End, track South End 1", 0, l3s1.getDropRS()); 
		Assert.assertEquals("Reset Drop count for South End, track South End 2", 0, l3s2.getDropRS()); 
		Assert.assertEquals("Reset Pickup count for South End, track South End 1", 0, l3s1.getPickupRS()); 
		Assert.assertEquals("Reset Pickup count for South End, track South End 2", 0, l3s2.getPickupRS()); 

		
		//  Build the train again!!  This should change the staging track to Sound End 2
		train1.build(false);
		Assert.assertEquals("Train 1 After Build Departs Name", "North End", train1.getTrainDepartsName());
		Assert.assertEquals("Train 1 After Build Terminates Name", "South End", train1.getTrainTerminatesName());
		Assert.assertEquals("Train 1 After Build Next Location Name", "North Industries", train1.getNextLocationName());
		Assert.assertEquals("Train 1 After Build Built Status", true, train1.getBuilt());

		//  Move the train!!
		train1.move();
		Assert.assertEquals("Train 1 After 1st Move Current Name", "North Industries", train1.getCurrentLocationName());
		Assert.assertEquals("Train 1 After 1st Move Next Location Name", "South End", train1.getNextLocationName());
		
		// Is the train in route?
		Assert.assertEquals("Train 1 in route after 1st", true, train1.isTrainInRoute());
		
		// Try and reset the train
		Assert.assertEquals("Train 1 Reset should be false", false, train1.reset());

		// Are the engine and car locations correct?
		Assert.assertEquals("Engine e1 After After 1st Move", "North Industries", e1.getLocationName());
		Assert.assertEquals("Engine e2 After After 1st Move", "North Industries", e2.getLocationName());
		Assert.assertEquals("Car c1 After After 1st Move", "North Industries", c1.getLocationName());
		Assert.assertEquals("Car c3 After After 1st Move", "North Industries", c3.getLocationName());
		Assert.assertEquals("Car c4 After After 1st Move", "North Industries", c4.getLocationName());
		Assert.assertEquals("Car c8 After After 1st Move", "North Industries", c8.getLocationName());

		// Are the location pickup and drop counts correct?
		Assert.assertEquals("Move 1 Drop count for North End", 0, l1.getDropRS());  
		Assert.assertEquals("Move 1 Drop count for North Industries", 1, l2.getDropRS()); 
		Assert.assertEquals("Move 1 Drop count for South End", 5, l3.getDropRS()); 
		Assert.assertEquals("Move 1 Pickup count for North End", 0, l1.getPickupRS());  
		Assert.assertEquals("Move 1 Pickup count for North Industries", 1, l2.getPickupRS()); 
		Assert.assertEquals("Move 1 Pickup count for South End", 0, l3.getPickupRS()); 
		
		// Are the track pickup and drop counts correct?
		Assert.assertEquals("Move 1 Drop count for North End, track North End 1", 0, l1s1.getDropRS()); 
		Assert.assertEquals("Move 1 Drop count for North End, track North End 2", 0, l1s2.getDropRS()); 
		Assert.assertEquals("Move 1 Pickup count for North End, track North End 1", 0, l1s1.getPickupRS()); 
		Assert.assertEquals("Move 1 Pickup count for North End, track North End 2", 0, l1s2.getPickupRS()); 
		Assert.assertEquals("Move 1 Drop count for North Industries, track NI Yard", 1, l2s1.getDropRS()); 
		Assert.assertEquals("Move 1 Pickup count for North Industries, track NI Yard", 1, l2s1.getDropRS()); 
		Assert.assertEquals("Move 1 Drop count for South End, track South End 1", 0, l3s1.getDropRS()); 
		Assert.assertEquals("Move 1 Drop count for South End, track South End 2", 5, l3s2.getDropRS()); 
		Assert.assertEquals("Move 1 Pickup count for South End, track South End 1", 0, l3s1.getPickupRS()); 
		Assert.assertEquals("Move 1 Pickup count for South End, track South End 2", 0, l3s2.getPickupRS()); 

		//  Move the train again!!
		train1.move();
		Assert.assertEquals("Train 1 After 2nd Move Current Name", "South End", train1.getCurrentLocationName());
		Assert.assertEquals("Train 1 After 2nd Move Next Location Name", "South End", train1.getNextLocationName());
		// Is the train in route?
		Assert.assertEquals("Train 1 in route after 2nd", true, train1.isTrainInRoute());

		// Are the engine and car locations correct?
		Assert.assertEquals("Engine e1 After After 2nd Move", "South End", e1.getLocationName());
		Assert.assertEquals("Engine e2 After After 2nd Move", "South End", e2.getLocationName());
		Assert.assertEquals("Car c1 After After 2nd Move", "South End", c1.getLocationName());
		Assert.assertEquals("Car c3 After After 2nd Move", "North Industries", c3.getLocationName());
		Assert.assertEquals("Car c4 After After 2nd Move", "South End", c4.getLocationName());
		Assert.assertEquals("Car c8 After After 2nd Move", "South End", c8.getLocationName());
		
		// was c3 released from train?
		Assert.assertEquals("Car c3 After drop should NOT be assigned to Train 1", null, c3.getTrain());
		Assert.assertEquals("Car c3 destination After 2nd Move", "", c3.getDestinationTrackName());
		Assert.assertEquals("Car c3 After 2nd Move location", "North Industries", c3.getLocationName());
		Assert.assertEquals("Car c3 After 2nd Move", "NI Yard", c3.getTrackName());
		Assert.assertEquals("Car c3 Moves after drop should be 2", 2, c3.getMoves());
		
		// Are the location pickup and drop counts correct?
		Assert.assertEquals("Move 2 Drop count for North End", 0, l1.getDropRS());  
		Assert.assertEquals("Move 2 Drop count for North Industries", 0, l2.getDropRS()); 
		Assert.assertEquals("Move 2 Drop count for South End", 5, l3.getDropRS()); 
		Assert.assertEquals("Move 2 Pickup count for North End", 0, l1.getPickupRS());  
		Assert.assertEquals("Move 2 Pickup count for North Industries", 0, l2.getPickupRS()); 
		Assert.assertEquals("Move 2 Pickup count for South End", 0, l3.getPickupRS()); 
		
		// Are the track pickup and drop counts correct?
		Assert.assertEquals("Move 2 Drop count for North End, track North End 1", 0, l1s1.getDropRS()); 
		Assert.assertEquals("Move 2 Drop count for North End, track North End 2", 0, l1s2.getDropRS()); 
		Assert.assertEquals("Move 2 Pickup count for North End, track North End 1", 0, l1s1.getPickupRS()); 
		Assert.assertEquals("Move 2 Pickup count for North End, track North End 2", 0, l1s2.getPickupRS()); 
		Assert.assertEquals("Move 2 Drop count for North Industries, track NI Yard", 0, l2s1.getDropRS()); 
		Assert.assertEquals("Move 2 Pickup count for North Industries, track NI Yard", 0, l2s1.getDropRS()); 
		Assert.assertEquals("Move 2 Drop count for South End, track South End 1", 0, l3s1.getDropRS()); 
		Assert.assertEquals("Move 2 Drop count for South End, track South End 2", 5, l3s2.getDropRS()); 
		Assert.assertEquals("Move 2 Pickup count for South End, track South End 1", 0, l3s1.getPickupRS()); 
		Assert.assertEquals("Move 2 Pickup count for South End, track South End 2", 0, l3s2.getPickupRS()); 

		//  Move the train again!!
		train1.move();
		Assert.assertEquals("Train 1 After 3rd Move Current Name", "South End", train1.getCurrentLocationName());
		Assert.assertEquals("Train 1 After 3rd Move Next Location Name", "South End", train1.getNextLocationName());
		// Is the train in route?
		Assert.assertEquals("Train 1 in route after 3rd", true, train1.isTrainInRoute());

		// Are the engine and car destinations correct?
		Assert.assertEquals("Engine e1 After 3rd Move", "", e1.getDestinationTrackName());
		Assert.assertEquals("Engine e2 After 3rd Move", "", e2.getDestinationTrackName());
		Assert.assertEquals("Car c1 After 3rd Move", "", c1.getDestinationTrackName());
		Assert.assertEquals("Car c4 After 3rd Move", "", c4.getDestinationTrackName());
		Assert.assertEquals("Car c8 After 3rd Move", "", c8.getDestinationTrackName());

		// Are the engine and car final locations correct?
		Assert.assertEquals("Engine e1 After Terminate location", "South End", e1.getLocationName());
		Assert.assertEquals("Engine e2 After Terminate location", "South End", e2.getLocationName());
		Assert.assertEquals("Car c1 After Terminate location", "South End", c1.getLocationName());
		Assert.assertEquals("Car c4 After Terminate location", "South End", c4.getLocationName());
		Assert.assertEquals("Car c8 After Terminate location", "South End", c8.getLocationName());
	
		// Are the engine and car final staging track correct?
		Assert.assertEquals("Engine e1 After Terminate track", "South End 2", e1.getTrackName());
		Assert.assertEquals("Engine e2 After Terminate track", "South End 2", e2.getTrackName());
		Assert.assertEquals("Car c1 After Terminate track", "South End 2", c1.getTrackName());
		Assert.assertEquals("Car c4 After Terminate track", "South End 2", c4.getTrackName());
		Assert.assertEquals("Car c8 After Terminate track", "South End 2", c8.getTrackName());
		
		// Did the engine and car moves increment by 2, one for build/reset and one for build?
		Assert.assertEquals("Engine e1 Moves after Terminate should be 125", 125, e1.getMoves());
		Assert.assertEquals("Engine e2 Moves after Terminate should be 323", 323, e2.getMoves());
		Assert.assertEquals("Car c1 Moves after Terminate should be 25", 25, c1.getMoves());
		Assert.assertEquals("Car c4 Moves after Terminate should be 4446", 4446, c4.getMoves());
		Assert.assertEquals("Car c8 Moves after Terminate should be 2", 2, c8.getMoves());

		// Are the location pickup and drop counts correct?
		Assert.assertEquals("Move 3 Drop count for North End", 0, l1.getDropRS());  
		Assert.assertEquals("Move 3 Drop count for North Industries", 0, l2.getDropRS()); 
		Assert.assertEquals("Move 3 Drop count for South End", 0, l3.getDropRS()); 
		Assert.assertEquals("Move 3 Pickup count for North End", 0, l1.getPickupRS());  
		Assert.assertEquals("Move 3 Pickup count for North Industries", 0, l2.getPickupRS()); 
		Assert.assertEquals("Move 3 Pickup count for South End", 0, l3.getPickupRS()); 
		
		// Are the track pickup and drop counts correct?
		Assert.assertEquals("Move 3 Drop count for North End, track North End 1", 0, l1s1.getDropRS()); 
		Assert.assertEquals("Move 3 Drop count for North End, track North End 2", 0, l1s2.getDropRS()); 
		Assert.assertEquals("Move 3 Pickup count for North End, track North End 1", 0, l1s1.getPickupRS()); 
		Assert.assertEquals("Move 3 Pickup count for North End, track North End 2", 0, l1s2.getPickupRS()); 
		Assert.assertEquals("Move 3 Drop count for North Industries, track NI Yard", 0, l2s1.getDropRS()); 
		Assert.assertEquals("Move 3 Pickup count for North Industries, track NI Yard", 0, l2s1.getDropRS()); 
		Assert.assertEquals("Move 3 Drop count for South End, track South End 1", 0, l3s1.getDropRS()); 
		Assert.assertEquals("Move 3 Drop count for South End, track South End 2", 0, l3s2.getDropRS()); 
		Assert.assertEquals("Move 3 Pickup count for South End, track South End 1", 0, l3s1.getPickupRS()); 
		Assert.assertEquals("Move 3 Pickup count for South End, track South End 2", 0, l3s2.getPickupRS()); 

		//  Move the train again!! This should terminate the train
		train1.move();
		Assert.assertEquals("Train 1 After 4th Move Current Name", "", train1.getCurrentLocationName());
		Assert.assertEquals("Train 1 After 4th Move Next Location Name", "", train1.getNextLocationName());
		Assert.assertEquals("Train 1 After 4th Move Status", Train.TERMINATED, train1.getStatus());
		
		// Is the train in route?
		Assert.assertEquals("Train 1 sould not be in route", false, train1.isTrainInRoute());
		
		// Are the engines and cars released from train 1?
		Assert.assertEquals("Engine e1 After Terminate should NOT be assigned to Train 1", null, e1.getTrain());
		Assert.assertEquals("Engine e2 After Terminate should NOT be assigned to Train 1", null, e2.getTrain());
		Assert.assertEquals("Car c1 After Terminate should NOT be assigned to Train 1", null, c1.getTrain());
		Assert.assertEquals("Car c4 After Terminate should NOT be assigned to Train 1", null, c4.getTrain());
	
		// do cars have the right loads?
		Assert.assertEquals("Car c1 load after Terminate", "E", c1.getLoad());
		Assert.assertEquals("Car c2 load after Terminate", "E", c2.getLoad());
		Assert.assertEquals("Car c3 load after Terminate", "E", c3.getLoad());
		Assert.assertEquals("Car c4 load after Terminate", "E", c4.getLoad());
		Assert.assertEquals("Car c5 load after Terminate", "E", c5.getLoad());
		Assert.assertEquals("Car c6 load after Terminate", "E", c6.getLoad());
		Assert.assertEquals("Car c7 load after Terminate", "E", c7.getLoad());
		Assert.assertEquals("Car c8 load after Terminate", "E", c8.getLoad());
		Assert.assertEquals("Car c9 load after Terminate", "E", c9.getLoad());	
		
		// Are the location pickup and drop counts correct?
		Assert.assertEquals("Terminated Drop count for North End", 0, l1.getDropRS());  
		Assert.assertEquals("Terminated Drop count for North Industries", 0, l2.getDropRS()); 
		Assert.assertEquals("Terminated Drop count for South End", 0, l3.getDropRS()); 
		Assert.assertEquals("Terminated Pickup count for North End", 0, l1.getPickupRS());  
		Assert.assertEquals("Terminated Pickup count for North Industries", 0, l2.getPickupRS()); 
		Assert.assertEquals("Terminated Pickup count for South End", 0, l3.getPickupRS()); 

		// Are the track pickup and drop counts correct?
		Assert.assertEquals("Terminated Drop count for North End, track North End 1", 0, l1s1.getDropRS()); 
		Assert.assertEquals("Terminated Drop count for North End, track North End 2", 0, l1s2.getDropRS()); 
		Assert.assertEquals("Terminated Pickup count for North End, track North End 1", 0, l1s1.getPickupRS()); 
		Assert.assertEquals("Terminated Pickup count for North End, track North End 2", 0, l1s2.getPickupRS()); 
		Assert.assertEquals("Terminated Drop count for North Industries, track NI Yard", 0, l2s1.getDropRS()); 
		Assert.assertEquals("Terminated Pickup count for North Industries, track NI Yard", 0, l2s1.getDropRS()); 
		Assert.assertEquals("Terminated Drop count for South End, track South End 1", 0, l3s1.getDropRS()); 
		Assert.assertEquals("Terminated Drop count for South End, track South End 2", 0, l3s2.getDropRS()); 
		Assert.assertEquals("Terminated Pickup count for South End, track South End 1", 0, l3s1.getPickupRS()); 
		Assert.assertEquals("Terminated Pickup count for South End, track South End 2", 0, l3s2.getPickupRS()); 

		// now build train 2 testing failure modes
		train2.build(false);
		// build required 1 engine and there were two
		Assert.assertEquals("Train 2 After Build require 1 engine", false, train2.getBuilt());
		// should work for 0
		train2.setNumberEngines("0");
		train2.build(false);
		Assert.assertEquals("Train 2 After Build require 0 engine", true, train2.getBuilt());
		train2.setNumberEngines("3");
		train2.build(false);
		Assert.assertEquals("Train 2 After Build require 3 engines", false, train2.getBuilt());
		train2.setNumberEngines("2");
		train2.build(false);
		Assert.assertEquals("Train 2 After Build require 2 engine", true, train2.getBuilt());
		// try different road
		train2.setEngineRoad("PC");
		train2.build(false);
		Assert.assertEquals("Train 2 After Build require road PC", false, train2.getBuilt());
		train2.setEngineRoad("CP");
		// try requiring FRED, should fail
		train2.setRequirements(Train.FRED);
		train2.build(false);
		Assert.assertEquals("Train 2 After Build requires FRED", false, train2.getBuilt());
		// Add FRED to boxcar
		c5.setFred(true);
		train2.build(false);
		Assert.assertEquals("Train 2 After Build 2 requires FRED", true, train2.getBuilt());
		// try engine wrong model
		train2.setEngineModel("DS45");
		train2.build(false);
		Assert.assertEquals("Train 2 After Build 2 requires model DS45", false, train2.getBuilt());
		// try engine correct model
		train2.setEngineModel("SD45");
		train2.build(false);
		Assert.assertEquals("Train 2 After Build 2 requires model SD45", true, train2.getBuilt());
	
		// Are the engines and cars assigned to train 2?
		Assert.assertEquals("Engine e3 After Build should be assigned to Train 2", train2, e3.getTrain());
		Assert.assertEquals("Engine e4 After Build should be assigned to Train 2", train2, e4.getTrain());
		Assert.assertEquals("Car c2 After Build should be assigned to Train 2", train2, c2.getTrain());
		Assert.assertEquals("Car c5 After Build should be assigned to Train 2", train2, c5.getTrain());
		Assert.assertEquals("Car c6 After Build should be assigned to Train 2", train2, c6.getTrain());
		// train 2 does not accept Boxcars or Flat
		Assert.assertEquals("Car c3 After Build should NOT be assigned to Train 2", null, c3.getTrain());
		Assert.assertEquals("Car c7 After Build should NOT be assigned to Train 2", null, c7.getTrain());
		Assert.assertEquals("Car c9 After Build should NOT be assigned to Train 2", null, c9.getTrain());
		// now allow Boxcars
		train2.addTypeName("Boxcar");
		train2.build(false);
		// c3 has less moves than c7, and c9 is a Flat
		Assert.assertEquals("Car c3 After Build 2 should be assigned to Train 2", train2, c3.getTrain());
		Assert.assertEquals("Car c7 After Build 2 should NOT be assigned to Train 2", null, c7.getTrain());
		Assert.assertEquals("Car c9 After Build 2 should NOT be assigned to Train 2", null, c9.getTrain());
		// now allow Flat
		train2.addTypeName("Flat");
		train2.build(false);
		// c9 has less moves than c3 and c7, but there's not enough room for c9 at destination
		Assert.assertEquals("Car c3 After Build 3 should be assigned to Train 2", train2, c3.getTrain());
		Assert.assertEquals("Car c7 After Build 3 should NOT be assigned to Train 2", null, c7.getTrain());
		Assert.assertEquals("Car c9 After Build 3 should NOT be assigned to Train 2", null, c9.getTrain());
		// c3 is assigned to Staging Track South End 1 its load will swap
		Assert.assertEquals("Car c3 After Build 3 destination", "South End 1", c3.getDestinationTrackName());
		Assert.assertEquals("Car c3 load After Build 3", "E", c3.getLoad());
		// increase the size of staging
		l3s1.setLength(400);
		// allow default load swaps
		l3s1.enableLoadSwaps(true);  // South End 1
		
		train2.build(false);
		// Check expected arrival times
		Assert.assertEquals("Train 2 expected departure time", "22:45", train2.getExpectedArrivalTime(rl1));
		Assert.assertEquals("Train 2 expected North End", "1:00:36", train2.getExpectedArrivalTime(rl2));
		// one car dropped and one is picked up at North End, so travel time + two car moves
		Assert.assertEquals("Train 2 expected North Industries", "1:02:49", train2.getExpectedArrivalTime(rl3));

		// the build first resets which removes cars from the train, c3 load will swap
		Assert.assertEquals("Car c3 load After Build 4", "L", c3.getLoad());
		// c9 has less moves than c3 and c7, and now there's enough room for c9
		Assert.assertEquals("Car c3 After Build 4 should NOT be assigned to Train 2", null, c3.getTrain());
		Assert.assertEquals("Car c7 After Build 4 should NOT be assigned to Train 2", null, c7.getTrain());
		Assert.assertEquals("Car c9 After Build 4 should be assigned to Train 2", train2, c9.getTrain());
		// move the train!
		train2.move();
		// Is the train in route?
		Assert.assertEquals("Train 2 in route after 1st", true, train2.isTrainInRoute());
		train2.move();
		// Is the train in route?
		Assert.assertEquals("Train 2 in route after 2nd", true, train2.isTrainInRoute());
		train2.move();
		// Is the train in route?
		Assert.assertEquals("Train 2 in route after 3rd", true, train2.isTrainInRoute());
		train2.move();
		// Is the train in route?
		Assert.assertEquals("Train 2 in route after 4th", false, train2.isTrainInRoute());
		
		// Are the engine and car final tracks correct?
		Assert.assertEquals("Engine e1 After Terminate track", "South End 2", e1.getTrackName());
		Assert.assertEquals("Engine e2 After Terminate track", "South End 2", e2.getTrackName());
		Assert.assertEquals("Engine e3 After Terminate track", "South End 1", e3.getTrackName());
		Assert.assertEquals("Engine e4 After Terminate track", "South End 1", e4.getTrackName());
		Assert.assertEquals("Car c1 After Terminate track", "South End 2", c1.getTrackName());
		Assert.assertEquals("Car c2 After Terminate track", "South End 1", c2.getTrackName());
		Assert.assertEquals("Car c3 After Terminate track", "NI Yard", c3.getTrackName());
		Assert.assertEquals("Car c4 After Terminate track", "South End 2", c4.getTrackName());
		Assert.assertEquals("Car c5 After Terminate track", "South End 1", c5.getTrackName());
		Assert.assertEquals("Car c6 After Terminate track", "NI Yard", c6.getTrackName());
		Assert.assertEquals("Car c7 After Terminate track", "NI Yard", c7.getTrackName());
		Assert.assertEquals("Car c8 After Terminate track", "South End 2", c8.getTrackName());
		Assert.assertEquals("Car c9 After Terminate track", "South End 1", c9.getTrackName());
		
		// do cars have the right loads?
		Assert.assertEquals("Car c1 load after Terminate Train 2", "E", c1.getLoad());
		Assert.assertEquals("Car c2 load after Terminate Train 2", "E", c2.getLoad());
		Assert.assertEquals("Car c3 load after Terminate Train 2", "L", c3.getLoad());
		Assert.assertEquals("Car c4 load after Terminate Train 2", "E", c4.getLoad());
		Assert.assertEquals("Car c5 load after Terminate Train 2", "E", c5.getLoad());
		Assert.assertEquals("Car c6 load after Terminate Train 2", "E", c6.getLoad());
		Assert.assertEquals("Car c7 load after Terminate Train 2", "E", c7.getLoad());
		Assert.assertEquals("Car c8 load after Terminate Train 2", "E", c8.getLoad());
		Assert.assertEquals("Car c9 load after Terminate Train 2", "L", c9.getLoad());	

	}

	// test train staging to staging
	public void testStagingtoStaging2() {
		TrainManager tmanager = TrainManager.instance();
		RouteManager rmanager = RouteManager.instance();
		LocationManager lmanager = LocationManager.instance();
		EngineManager emanager = EngineManager.instance();
		CarManager cmanager = CarManager.instance();
		CarColors cc = CarColors.instance();
		CarLengths cl = CarLengths.instance();
		CarOwners co = CarOwners.instance();
		CarRoads cr = CarRoads.instance();
		CarLoads cld = CarLoads.instance();
		CarTypes ct = CarTypes.instance();
		EngineModels em = EngineModels.instance();

		// Clear out the global lists
		cc.dispose();  // Clear out the CarColors
		cl.dispose();  // Clear out the CarLengths
		co.dispose();  // Clear out the CarOwners
		cr.dispose();  // Clear out the CarRoads
		ct.dispose();  // Clear out the CarTypes
		cld.dispose(); // Clear out the CarLoads
		em.dispose();  // Clear out the EngineModels
		cmanager.dispose();  // Clear out the Cars
		emanager.dispose();  // Clear out the Engines
		lmanager.dispose();  // Clear out the Locations
		rmanager.dispose();  // Clear out the Routes
		tmanager.dispose();  // Clear out the Trains

		// register the car colors used
		Assert.assertEquals("Bob Test CarColor Silver false", false, cc.containsName("Silver"));
		Assert.assertEquals("Bob Test CarColor Black false", false, cc.containsName("Black"));
		Assert.assertEquals("Bob Test CarColor Red false", false, cc.containsName("Red"));
		cc.addName("Silver");
		Assert.assertEquals("Bob Test CarColor Silver true", true, cc.containsName("Silver"));
		cc.addName("Black");
		Assert.assertEquals("Bob Test CarColor Black true", true, cc.containsName("Black"));
		cc.addName("Red");
		Assert.assertEquals("Bob Test CarColor Red true", true, cc.containsName("Red"));

		// register the car lengths used
		Assert.assertEquals("Bob Test CarLength 32 false", false, cl.containsName("32"));
		Assert.assertEquals("Bob Test CarLength 38 false", false, cl.containsName("38"));
		Assert.assertEquals("Bob Test CarLength 40 false", false, cl.containsName("40"));
		cl.addName("32");
		Assert.assertEquals("Bob Test CarLength 32 true", true, cl.containsName("32"));
		cl.addName("38");
		Assert.assertEquals("Bob Test CarLength 38 true", true, cl.containsName("38"));
		cl.addName("40");
		Assert.assertEquals("Bob Test CarLength 40 true", true, cl.containsName("40"));

		// register the car owners used
		Assert.assertEquals("Bob Test CarOwner Owner1 false", false, co.containsName("Owner1"));
		Assert.assertEquals("Bob Test CarOwner Owner2 false", false, co.containsName("Owner2"));
		Assert.assertEquals("Bob Test CarOwner Owner3 false", false, co.containsName("Owner3"));
		co.addName("Owner1");
		Assert.assertEquals("Bob Test CarOwner Owner1 true", true, co.containsName("Owner1"));
		co.addName("Owner2");
		Assert.assertEquals("Bob Test CarOwner Owner2 true", true, co.containsName("Owner2"));
		co.addName("Owner3");
		Assert.assertEquals("Bob Test CarOwner Owner3 true", true, co.containsName("Owner3"));

		// register the car roads used
		Assert.assertEquals("Bob Test CarRoads CP false", false, cr.containsName("CP"));
		Assert.assertEquals("Bob Test CarRoads Road2 false", false, cr.containsName("Road2"));
		Assert.assertEquals("Bob Test CarRoads Road3 false", false, cr.containsName("Road3"));
		cr.addName("CP");
		Assert.assertEquals("Bob Test CarRoads CP true", true, cr.containsName("CP"));
		cr.addName("Road2");
		Assert.assertEquals("Bob Test CarRoads Road2 true", true, cr.containsName("Road2"));
		cr.addName("Road3");
		Assert.assertEquals("Bob Test CarRoads Road3 true", true, cr.containsName("Road3"));

		// register the car types used
		Assert.assertEquals("Bob Test CarType Caboose false", false, ct.containsName("Caboose"));
		Assert.assertEquals("Bob Test CarType Tanker false", false, ct.containsName("Tanker"));
		Assert.assertEquals("Bob Test CarType Boxcar false", false, ct.containsName("Boxcar"));
		ct.addName("Caboose");
		Assert.assertEquals("Bob Test CarType Caboose true", true, ct.containsName("Caboose"));
		ct.addName("Tanker");
		Assert.assertEquals("Bob Test CarType Tanker true", true, ct.containsName("Tanker"));
		ct.addName("Boxcar");
		Assert.assertEquals("Bob Test CarType Boxcar true", true, ct.containsName("Boxcar"));

		// register the car loads used
		cld.addType("Boxcar");
		Assert.assertEquals("Bob Test CarLoad Boxcar Flour false", false, cld.containsName("Boxcar", "Flour"));
		Assert.assertEquals("Bob Test CarLoad Boxcar Bags false", false, cld.containsName("Boxcar", "Bags"));
		cld.addName("Boxcar", "Flour");
		Assert.assertEquals("Bob Test CarLoad Boxcar Flour true", true, cld.containsName("Boxcar", "Flour"));
		cld.addName("Boxcar", "Bags");
		Assert.assertEquals("Bob Test CarLoad Boxcar Bags true", true, cld.containsName("Boxcar", "Bags"));

		// register the engine models used
		Assert.assertEquals("Bob Test EngineModel GP40 false", false, em.containsName("GP40"));
		Assert.assertEquals("Bob Test EngineModel GP30 false", false, em.containsName("GP30"));
		em.addName("GP40");
		Assert.assertEquals("Bob Test EngineModel GP40 true", true, em.containsName("GP40"));
		em.addName("GP30");
		Assert.assertEquals("Bob Test EngineModel GP30 true", true, em.containsName("GP30"));

		// Create locations used
		Location loc1;
		loc1 = lmanager.newLocation("Westend");
		loc1.setTrainDirections(Location.WEST + Location.EAST);
		loc1.addTypeName("Diesel");
		loc1.addTypeName("Boxcar");
		loc1.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Location Westend Name", "Westend", loc1.getName());
		Assert.assertEquals("Bob Test Location Westend Directions", 3, loc1.getTrainDirections());
		Assert.assertEquals("Bob Test Location Westend Type Diesel", true, loc1.acceptsTypeName("Diesel"));
		Assert.assertEquals("Bob Test Location Westend Type Boxcar", true, loc1.acceptsTypeName("Boxcar"));
		Assert.assertEquals("Bob Test Location Westend Type Caboose", true, loc1.acceptsTypeName("Caboose"));

		Location loc2;
		loc2 = lmanager.newLocation("Midtown");
		loc2.setTrainDirections(Location.WEST + Location.EAST);
		loc2.addTypeName("Diesel");
		loc2.addTypeName("Boxcar");
		loc2.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Location Midtown Name", "Midtown", loc2.getName());
		Assert.assertEquals("Bob Test Location Midtown Directions", 3, loc2.getTrainDirections());
		Assert.assertEquals("Bob Test Location Midtown Type Diesel", true, loc2.acceptsTypeName("Diesel"));
		Assert.assertEquals("Bob Test Location Midtown Type Boxcar", true, loc2.acceptsTypeName("Boxcar"));
		Assert.assertEquals("Bob Test Location Midtown Type Caboose", true, loc2.acceptsTypeName("Caboose"));

		Location loc3;
		loc3 = lmanager.newLocation("Eastend");
		loc3.setTrainDirections(Location.WEST + Location.EAST);
		loc3.addTypeName("Diesel");
		loc3.addTypeName("Boxcar");
		loc3.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Location Eastend Name", "Eastend", loc3.getName());
		Assert.assertEquals("Bob Test Location Eastend Directions", 3, loc3.getTrainDirections());
		Assert.assertEquals("Bob Test Location Eastend Type Diesel", true, loc3.acceptsTypeName("Diesel"));
		Assert.assertEquals("Bob Test Location Eastend Type Boxcar", true, loc3.acceptsTypeName("Boxcar"));
		Assert.assertEquals("Bob Test Location Eastend Type Caboose", true, loc3.acceptsTypeName("Caboose"));

		Track loc1trk1;
		loc1trk1 = loc1.addTrack("Westend Staging 1", Track.YARD);
		loc1trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc1trk1.setLength(500);
		loc1trk1.addTypeName("Diesel");
		loc1trk1.addTypeName("Boxcar");
		loc1trk1.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Track Westend Staging 1 Name", "Westend Staging 1", loc1trk1.getName());
		Assert.assertEquals("Bob Test Track Westend Staging 1 Directions", 3, loc1trk1.getTrainDirections());
		Assert.assertEquals("Bob Test Track Westend Staging 1 Length", 500, loc1trk1.getLength());
		Assert.assertEquals("Bob Test Track Westend Staging 1 Type Diesel", true, loc1trk1.acceptsTypeName("Diesel"));
		Assert.assertEquals("Bob Test Track Westend Staging 1 Type Boxcar", true, loc1trk1.acceptsTypeName("Boxcar"));
		Assert.assertEquals("Bob Test Track Westend Staging 1 Type Caboose", true, loc1trk1.acceptsTypeName("Caboose"));

		Track loc2trk1;
		loc2trk1 = loc2.addTrack("Midtown Inbound from West", Track.YARD);
		loc2trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk1.setLength(500);
		loc2trk1.addTypeName("Diesel");
		loc2trk1.addTypeName("Boxcar");
//		loc2trk1.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Track Midtown West Inbound Name", "Midtown Inbound from West", loc2trk1.getName());
		Assert.assertEquals("Bob Test Track Midtown West Inbound Directions", 3, loc2trk1.getTrainDirections());
		Assert.assertEquals("Bob Test Track Midtown West Inbound Length", 500, loc2trk1.getLength());

		Track loc2trk2;
		loc2trk2 = loc2.addTrack("Midtown Inbound from East", Track.YARD);
		loc2trk2.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk2.setLength(500);
		loc2trk2.addTypeName("Diesel");
		loc2trk2.addTypeName("Boxcar");
//		loc2trk2.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Track Midtown East Inbound Name", "Midtown Inbound from East", loc2trk2.getName());
		Assert.assertEquals("Bob Test Track Midtown East Inbound Directions", 3, loc2trk2.getTrainDirections());
		Assert.assertEquals("Bob Test Track Midtown East Inbound Length", 500, loc2trk2.getLength());

		Track loc2trk3;
		loc2trk3 = loc2.addTrack("Midtown Outbound to West", Track.YARD);
		loc2trk3.setTrainDirections(Track.WEST);
		loc2trk3.setLength(500);
		loc2trk3.addTypeName("Diesel");
		loc2trk3.addTypeName("Boxcar");
		Assert.assertEquals("Bob Test Track Midtown West Outbound Name", "Midtown Outbound to West", loc2trk3.getName());
		Assert.assertEquals("Bob Test Track Midtown West Outbound Directions", 2, loc2trk3.getTrainDirections());
		Assert.assertEquals("Bob Test Track Midtown West Outbound Length", 500, loc2trk3.getLength());

		Track loc2trk4;
		loc2trk4 = loc2.addTrack("Midtown Outbound to East", Track.YARD);
		loc2trk4.setTrainDirections(Track.EAST);
		loc2trk4.setLength(500);
		loc2trk4.addTypeName("Diesel");
		loc2trk4.addTypeName("Boxcar");
		Assert.assertEquals("Bob Test Track Midtown East Outbound Name", "Midtown Outbound to East", loc2trk4.getName());
		Assert.assertEquals("Bob Test Track Midtown East Outbound Directions", 1, loc2trk4.getTrainDirections());
		Assert.assertEquals("Bob Test Track Midtown East Outbound Length", 500, loc2trk4.getLength());

		Track loc2trkc1;
		loc2trkc1 = loc2.addTrack("Midtown Caboose to East", Track.YARD);
		loc2trkc1.setTrainDirections(Track.EAST);
		loc2trkc1.setLength(100);
		loc2trkc1.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Track Midtown East Caboose Name", "Midtown Caboose to East", loc2trkc1.getName());
		Assert.assertEquals("Bob Test Track Midtown East Caboose Directions", 1, loc2trkc1.getTrainDirections());
		Assert.assertEquals("Bob Test Track Midtown East Caboose Length", 100, loc2trkc1.getLength());

		Track loc2trkc2;
		loc2trkc2 = loc2.addTrack("Midtown Caboose to West", Track.YARD);
		loc2trkc2.setTrainDirections(Track.WEST);
		loc2trkc2.setLength(100);
		loc2trkc2.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Track Midtown West Caboose Name", "Midtown Caboose to West", loc2trkc2.getName());
		Assert.assertEquals("Bob Test Track Midtown West Caboose Directions", 2, loc2trkc2.getTrainDirections());
		Assert.assertEquals("Bob Test Track Midtown west Caboose Length", 100, loc2trkc2.getLength());

		Track loc2trke1;
		loc2trke1 = loc2.addTrack("Midtown Engine to East", Track.YARD);
		loc2trke1.setTrainDirections(Track.EAST);
		loc2trke1.setLength(200);
		loc2trke1.addTypeName("Diesel");
		Assert.assertEquals("Bob Test Track Midtown East Engine Name", "Midtown Engine to East", loc2trke1.getName());
		Assert.assertEquals("Bob Test Track Midtown East Engine Directions", 1, loc2trke1.getTrainDirections());
		Assert.assertEquals("Bob Test Track Midtown East Engine Length", 200, loc2trke1.getLength());

		Track loc2trke2;
		loc2trke2 = loc2.addTrack("Midtown Engine to West", Track.YARD);
		loc2trke2.setTrainDirections(Track.WEST);
		loc2trke2.setLength(200);
		loc2trke2.addTypeName("Diesel");
		Assert.assertEquals("Bob Test Track Midtown West Engine Name", "Midtown Engine to West", loc2trke2.getName());
		Assert.assertEquals("Bob Test Track Midtown West Engine Directions", 2, loc2trke2.getTrainDirections());
		Assert.assertEquals("Bob Test Track Midtown west Engine Length", 200, loc2trke2.getLength());

		Track loc3trk1;
		loc3trk1 = loc3.addTrack("Eastend Staging 1", Track.YARD);
		loc3trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc3trk1.setLength(500);
		loc3trk1.addTypeName("Diesel");
		loc3trk1.addTypeName("Boxcar");
		loc3trk1.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Track Eastend Staging 1 Name", "Eastend Staging 1", loc3trk1.getName());
		Assert.assertEquals("Bob Test Track Eastend Staging 1 Directions", 3, loc3trk1.getTrainDirections());
		Assert.assertEquals("Bob Test Track Eastend Staging 1 Length", 500, loc3trk1.getLength());
		Assert.assertEquals("Bob Test Track Eastend Staging 1 Type Diesel", true, loc3trk1.acceptsTypeName("Diesel"));
		Assert.assertEquals("Bob Test Track Eastend Staging 1 Type Boxcar", true, loc3trk1.acceptsTypeName("Boxcar"));
		Assert.assertEquals("Bob Test Track Eastend Staging 1 Type Caboose", true, loc3trk1.acceptsTypeName("Caboose"));

		Assert.assertEquals("Bob Test Location Westend Length", 500, loc1.getLength());
		Assert.assertEquals("Bob Test Location Midtown Length", 2600, loc2.getLength());
		Assert.assertEquals("Bob Test Location Eastend Length", 500, loc3.getLength());

		// Create engines used
		Engine e1;
		e1 = emanager.newEngine("CP", "5501");
		e1.setModel("GP30");
		e1.setMoves(5);
		Assert.assertEquals("Bob Test Engine CP1801 Type", "Diesel", e1.getType());
		Assert.assertEquals("Bob Test Engine CP1801 Length", "56", e1.getLength());
		Assert.assertEquals("Bob Test Engine CP1801 Hp", "2250", e1.getHp());
		// Test that first "Diesel" is an acceptable type at all locations and tracks
		Assert.assertEquals("Bob Test Engine CP1801 SetLocation 1s1", "okay", e1.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Bob Test Engine CP1801 SetLocation 2s1", "okay", e1.setLocation(loc2, loc2trk1));
		Assert.assertEquals("Bob Test Engine CP1801 SetLocation 2s2", "okay", e1.setLocation(loc2, loc2trk2));
		Assert.assertEquals("Bob Test Engine CP1801 SetLocation 2s3", "okay", e1.setLocation(loc2, loc2trk3));
		Assert.assertEquals("Bob Test Engine CP1801 SetLocation 2s4", "okay", e1.setLocation(loc2, loc2trk4));
		Assert.assertEquals("Bob Test Engine CP1801 SetLocation 3s1", "okay", e1.setLocation(loc3, loc3trk1));
		Assert.assertEquals("Bob Test Engine CP1801 SetLocation 2s4 for real", "okay", e1.setLocation(loc2, loc2trke1));

		Engine e2;
		e2 = emanager.newEngine("CP", "5888");
		e2.setModel("GP40");
		Assert.assertEquals("Bob Test Engine CP5801 Type", "Diesel", e2.getType());
		Assert.assertEquals("Bob Test Engine CP5801 Length", "59", e2.getLength());
		Assert.assertEquals("Bob Test Engine CP5801 Hp", "3000", e2.getHp());
		Assert.assertEquals("Bob Test Engine CP5801 SetLocation 2s4", "okay", e2.setLocation(loc2, loc2trke2));

		// Create cars used
		Car b1;
		b1 = cmanager.newCar("CP", "81234567");
		b1.setType("Boxcar");
		b1.setLength("40");
		b1.setLoad("L");
		b1.setMoves(5);
		Assert.assertEquals("Bob Test Car CP81234567 Length", "40", b1.getLength());
		Assert.assertEquals("Bob Test Car CP81234567 Load", "L", b1.getLoad());
		// Test that first "Boxcar" is an acceptable type at all locations and tracks
		Assert.assertEquals("Bob Test Test Car CP81234567 SetLocation 1s1", "okay", b1.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Bob Test Test Car CP81234567 SetLocation 2s1", "okay", b1.setLocation(loc2, loc2trk1));
		Assert.assertEquals("Bob Test Test Car CP81234567 SetLocation 2s2", "okay", b1.setLocation(loc2, loc2trk2));
		Assert.assertEquals("Bob Test Test Car CP81234567 SetLocation 2s3", "okay", b1.setLocation(loc2, loc2trk3));
		Assert.assertEquals("Bob Test Test Car CP81234567 SetLocation 2s4", "okay", b1.setLocation(loc2, loc2trk4));
		Assert.assertEquals("Bob Test Test Car CP81234567 SetLocation 3s1", "okay", b1.setLocation(loc3, loc3trk1));
		Assert.assertEquals("Bob Test Test Car CP81234567 SetLocation 2s4 for real", "okay", b1.setLocation(loc2, loc2trk4));

		Car b2;
		b2 = cmanager.newCar("CP", "81234568");
		b2.setType("Boxcar");
		b2.setLength("40");
//		b2.setLoad("E");
		b2.setMoves(5);
		Assert.assertEquals("Bob Test Car CP81234568 Length", "40", b2.getLength());
		Assert.assertEquals("Bob Test Car CP81234568 Load", "E", b2.getLoad());
		Assert.assertEquals("Bob Test Test Car CP81234568 SetLocation 2s4", "okay", b2.setLocation(loc2, loc2trk4));

		Car b3;
		b3 = cmanager.newCar("CP", "81234569");
		b3.setType("Boxcar");
		b3.setLength("40");
		b3.setLoad("Flour");
		b3.setMoves(5);
		Assert.assertEquals("Bob Test Car CP81234569 Length", "40", b3.getLength());
		Assert.assertEquals("Bob Test Car CP81234569 Load", "Flour", b3.getLoad());
		Assert.assertEquals("Bob Test Test Car CP81234569 SetLocation 2s4", "okay", b3.setLocation(loc2, loc2trk4));

		Car b4;
		b4 = cmanager.newCar("CP", "81234566");
		b4.setType("Boxcar");
		b4.setLength("40");
		b4.setLoad("Bags");
		b4.setMoves(5);
		Assert.assertEquals("Bob Test Car CP81234566 Length", "40", b4.getLength());
		Assert.assertEquals("Bob Test Car CP81234566 Load", "Bags", b4.getLoad());
		Assert.assertEquals("Bob Test Test Car CP81234566 SetLocation 2s4", "okay", b4.setLocation(loc2, loc2trk4));

		Car b5;
		b5 = cmanager.newCar("CP", "71234567");
		b5.setType("Boxcar");
		b5.setLength("40");
//		b5.setLoad("E");
		Assert.assertEquals("Bob Test Car CP71234567 Length", "40", b5.getLength());
		Assert.assertEquals("Bob Test Car CP71234567 Load", "E", b5.getLoad());
		Assert.assertEquals("Bob Test Test Car CP71234567 SetLocation 2s4", "okay", b5.setLocation(loc2, loc2trk3));

		Car b6;
		b6 = cmanager.newCar("CP", "71234568");
		b6.setType("Boxcar");
		b6.setLength("40");
//		b6.setLoad("E");
		Assert.assertEquals("Bob Test Car CP71234568 Length", "40", b6.getLength());
		Assert.assertEquals("Bob Test Car CP71234568 Load", "E", b6.getLoad());
		Assert.assertEquals("Bob Test Test Car CP71234568 SetLocation 2s4", "okay", b6.setLocation(loc2, loc2trk3));

		Car b7;
		b7 = cmanager.newCar("CP", "71234569");
		b7.setType("Boxcar");
		b7.setLength("40");
//		b7.setLoad("E");
		Assert.assertEquals("Bob Test Car CP71234569 Length", "40", b7.getLength());
		Assert.assertEquals("Bob Test Car CP71234569 Load", "E", b7.getLoad());
		Assert.assertEquals("Bob Test Test Car CP71234569 SetLocation 2s4", "okay", b7.setLocation(loc2, loc2trk3));

		Car b8;
		b8 = cmanager.newCar("CP", "71234566");
		b8.setType("Boxcar");
		b8.setLength("40");
//		b2.setLoad("E");
		Assert.assertEquals("Bob Test Car CP71234566 Length", "40", b8.getLength());
		Assert.assertEquals("Bob Test Car CP71234566 Load", "E", b8.getLoad());
		Assert.assertEquals("Bob Test Test Car CP71234566 SetLocation 2s4", "okay", b8.setLocation(loc2, loc2trk3));

		// Create cars used
		Car c1;
		c1 = cmanager.newCar("CP", "12345678");
		c1.setType("Caboose");
		c1.setLength("32");
		c1.setCaboose(true);
		c1.setMoves(5);
		Assert.assertEquals("Bob Test Caboose CP12345678 Length", "32", c1.getLength());
		Assert.assertEquals("Bob Test Caboose CP12345678 Load", "E", c1.getLoad());
		// Test that first "Caboose" is an acceptable type at all locations and tracks
		Assert.assertEquals("Bob Test Test Caboose CP12345678 SetLocation 1s1", "okay", c1.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Bob Test Test Caboose CP12345678 SetLocation 3s1", "okay", c1.setLocation(loc3, loc3trk1));
		Assert.assertEquals("Bob Test Test Caboose CP12345678 SetLocation 2s5 for real", "okay", c1.setLocation(loc2, loc2trkc1));

		Car c2;
		c2 = cmanager.newCar("CP", "12345679");
		c2.setType("Caboose");
		c2.setLength("32");
		c2.setCaboose(true);
		Assert.assertEquals("Bob Test Caboose CP12345679 Length", "32", c2.getLength());
		Assert.assertEquals("Bob Test Caboose CP12345679 Load", "E", c2.getLoad());
		Assert.assertEquals("Bob Test Test Caboose CP12345679 SetLocation 2s5 for real", "okay", c2.setLocation(loc2, loc2trkc2));

		Assert.assertEquals("Bob Test Location Westend Used Length", 0, loc1.getUsedLength());
		// 56+4 + 59+4 + 2*(4*(40+4) + 32+4) = 123 + 2*(176 + 36) = 547
		Assert.assertEquals("Bob Test Location Midtown Used Length", 547, loc2.getUsedLength());
		Assert.assertEquals("Bob Test Location Eastend Used Length", 0, loc3.getUsedLength());

		// Create routes used
		Route rte1;
		rte1 = rmanager.newRoute("Midtown to Eastend Through");
		Assert.assertEquals("Bob Test Route rte1 Name", "Midtown to Eastend Through", rte1.getName());

		RouteLocation rte1rln1;
		rte1rln1 = rte1.addLocation(loc2);
		rte1rln1.setTrainDirection(RouteLocation.EAST);
		Assert.assertEquals("Bob Test Route Location rte1rln1 Name", "Midtown", rte1rln1.getName());
		Assert.assertEquals("Bob Test Route Location rte1rln1 Seq", 1, rte1rln1.getSequenceId());

		RouteLocation rte1rln2;
		rte1rln2 = rte1.addLocation(loc3);
		rte1rln2.setTrainDirection(RouteLocation.EAST);
		Assert.assertEquals("Bob Test Route Location rte1rln2 Name", "Eastend", rte1rln2.getName());
		Assert.assertEquals("Bob Test Route Location rte1rln2 Seq", 2, rte1rln2.getSequenceId());

		Route rte2;
		rte2 = rmanager.newRoute("Midtown to Westend Through");
		Assert.assertEquals("Bob Test Route rte2 Name", "Midtown to Westend Through", rte2.getName());

		RouteLocation rte2rln1;
		rte2rln1 = rte2.addLocation(loc2);
		rte2rln1.setTrainDirection(RouteLocation.WEST);
		Assert.assertEquals("Bob Test Route Location rte2rln1 Name", "Midtown", rte2rln1.getName());
		Assert.assertEquals("Bob Test Route Location rte2rln1 Seq", 1, rte2rln1.getSequenceId());

		RouteLocation rte2rln2;
		rte2rln2 = rte2.addLocation(loc1);
		rte2rln2.setTrainDirection(RouteLocation.WEST);
		Assert.assertEquals("Bob Test Route Location rte2rln2 Name", "Westend", rte2rln2.getName());
		Assert.assertEquals("Bob Test Route Location rte2rln2 Seq", 2, rte2rln2.getSequenceId());

		// Create trains used
		Train train1;
		train1 = tmanager.newTrain("Midtown to Eastend Through 0800");
		train1.setRoute(rte1);
		train1.setNumberEngines("1");
		train1.setRequirements(Train.CABOOSE);
		train1.addTypeName("Diesel");
		train1.addTypeName("Boxcar");
		train1.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Train train1 Name", "Midtown to Eastend Through 0800", train1.getName());
		Assert.assertEquals("Bob Test Train train1 Departs Name", "Midtown", train1.getTrainDepartsName());
		Assert.assertEquals("Bob Test Train train1 Terminates Name", "Eastend", train1.getTrainTerminatesName());

		Train train2;
		train2 = tmanager.newTrain("Midtown to Westend Through 0900");
		train2.setRoute(rte2);
		train2.setNumberEngines("1");
		train2.setRequirements(Train.CABOOSE);
		train2.addTypeName("Diesel");
		train2.addTypeName("Boxcar");
		train2.addTypeName("Caboose");
		Assert.assertEquals("Bob Test Train train2 Name", "Midtown to Westend Through 0900", train2.getName());
		Assert.assertEquals("Bob Test Train train2 Departs Name", "Midtown", train2.getTrainDepartsName());
		Assert.assertEquals("Bob Test Train train2 Terminates Name", "Westend", train2.getTrainTerminatesName());

		// Build trains
		train1.build(false);
		train2.build(false);
		
	}
	
	// Test a route of one location (local train).
	// Locations that don't have a train direction assigned
	// can only be served by a local train.
	// Creates two locations Westford and Chelmsford and 9 cars.
	// Westford has 2 yards, 2 sidings, 3 interchange tracks.
	// Chelmsford has 1 yard.  Chelmsford is used to test that a
	// train with two locations will not service certain tracks.
	public void testLocal(){
		TrainManager tmanager = TrainManager.instance();
		RouteManager rmanager = RouteManager.instance();
		LocationManager lmanager = LocationManager.instance();
		CarManager cmanager = CarManager.instance();
		CarTypes ct = CarTypes.instance();
		
		// Create locations used
		Location loc1;
		loc1 = lmanager.newLocation("Westford");
		loc1.setTrainDirections(DIRECTION_ALL);	
		loc1.addTypeName("Flat Car");
		loc1.addTypeName("Boxcar");
		
		Location loc2;
		loc2 = lmanager.newLocation("Chelmsford");
		loc2.setTrainDirections(DIRECTION_ALL);	
		loc2.addTypeName("Flat Car");
		loc2.addTypeName("Boxcar");
		
		Track loc1trk1;
		loc1trk1 = loc1.addTrack("Westford Yard 1", Track.YARD);
		loc1trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc1trk1.setLength(500);
		loc1trk1.addTypeName("Flat Car");
		loc1trk1.addTypeName("Boxcar");
		
		Track loc1trk2;
		loc1trk2 = loc1.addTrack("Westford Yard 2", Track.YARD);
		loc1trk2.setTrainDirections(Track.WEST + Track.EAST);
		loc1trk2.setLength(500);
		loc1trk2.addTypeName("Flat Car");
		loc1trk2.addTypeName("Boxcar");
		
		Track loc1trk3;
		loc1trk3 = loc1.addTrack("Westford Siding 3", Track.SIDING);
		loc1trk3.setTrainDirections(0);		// Only local moves allowed
		loc1trk3.setLength(300);
		loc1trk3.addTypeName("Flat Car");
		loc1trk3.addTypeName("Boxcar");
		
		Track loc1trk4;
		loc1trk4 = loc1.addTrack("Westford Siding 4", Track.SIDING);
		loc1trk4.setTrainDirections(0);		// Only local moves allowed
		loc1trk4.setLength(300);
		loc1trk4.addTypeName("Flat Car");
		loc1trk4.addTypeName("Boxcar");
		
		Track loc1trk5;
		loc1trk5 = loc1.addTrack("Westford Interchange 5", Track.INTERCHANGE);
		loc1trk5.setTrainDirections(0);		// Only local moves allowed
		loc1trk5.setLength(300);
		loc1trk5.addTypeName("Flat Car");
		loc1trk5.addTypeName("Boxcar");
		
		Track loc1trk6;
		loc1trk6 = loc1.addTrack("Westford Interchange 6", Track.INTERCHANGE);
		loc1trk6.setTrainDirections(Track.WEST + Track.EAST);		
		loc1trk6.setLength(300);
		loc1trk6.addTypeName("Flat Car");
		loc1trk6.addTypeName("Boxcar");
		
		Track loc1trk7;
		loc1trk7 = loc1.addTrack("Westford Interchange 7", Track.INTERCHANGE);
		loc1trk7.setTrainDirections(0);		// Only local moves allowed
		loc1trk7.setLength(300);
		loc1trk7.addTypeName("Flat Car");
		loc1trk7.addTypeName("Boxcar");
		
		Track loc2trk1;
		loc2trk1 = loc2.addTrack("Chelmsford Yard 1", Track.YARD);
		loc2trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk1.setLength(900);
		loc2trk1.addTypeName("Flat Car");
		loc2trk1.addTypeName("Boxcar");
		
		// now bias track selection by moves
		loc1trk1.setMoves(3);		// no yard to yard moves expected
		loc1trk2.setMoves(4);		// no yard to yard moves expected
		loc1trk3.setMoves(10);		// this will be the 5th location assigned
		loc1trk4.setMoves(10);		// this will be the 6th location assigned
		loc1trk5.setMoves(9);		// this will be the 2nd location assigned
		loc1trk6.setMoves(9);		// this will be the 3rd location assigned
		loc1trk7.setMoves(8);		// this will be the first and 4th location assigned

		// Create route with only one location
		Route rte1;
		rte1 = rmanager.newRoute("Local Route");
		Setup.setCarMoves(7);	// set the default moves to 7
		RouteLocation rl1 = rte1.addLocation(loc1);
		
		// Create train
		Train train1;
		train1 = tmanager.newTrain("Local Train");
		train1.setRoute(rte1);
		train1.addTypeName("Boxcar");
		train1.addTypeName("Flat Car");
		
		// Set up 7 box cars and 2 flat cars
		Car c1 = new Car("BM", "1");
		c1.setType("Boxcar");
		c1.setLength("90");
		c1.setMoves(17);		// should be the 7th car assigned to train
		cmanager.register(c1);

		Car c2 = new Car("CP", "2");
		c2.setType("Boxcar");
		c2.setLength("80");
		c2.setMoves(15);		// should be the 6th car assigned to train
		cmanager.register(c2);

		Car c3 = new Car("XP", "3");
		c3.setType("Flat Car");
		c3.setLength("70");
								// default c3 moves = 0 should be the 1st car assigned
		cmanager.register(c3);

		Car c4 = new Car("UP", "4");
		c4.setType("Boxcar");
		c4.setLength("60");
		c4.setMoves(6);			// should be the 5th car assigned to train
		cmanager.register(c4);

		Car c5 = new Car("UP", "5");
		c5.setType("Boxcar");
		c5.setLength("50");
		c5.setMoves(1);			// should be the 2nd car assigned to train
		cmanager.register(c5);

		Car c6 = new Car("CP", "6");
		c6.setType("Boxcar");
		c6.setLength("40");
		c6.setMoves(3);			// should be the 4th car assigned to train
		cmanager.register(c6);
		
		Car c7 = new Car("UP", "7");
		c7.setType("Boxcar");
		c7.setLength("50");
		c7.setMoves(18);	
		cmanager.register(c7);
		
		Car c8 = new Car("XP", "8");
		c8.setType("Boxcar");
		c8.setLength("60");
		c8.setMoves(2);			// should be the 2rd car assigned to train
		cmanager.register(c8);
		
		Car c9 = new Car("XP", "9");
		c9.setType("Flat Car");
		c9.setLength("90");
		c9.setMoves(19);
		cmanager.register(c9);
		
		Assert.assertEquals("Westford should not accept Flat Car", false, loc1.acceptsTypeName("Flat Car"));
		// add Flat Car as a valid type so Westford will accept
		ct.addName("Flat Car");
		Assert.assertEquals("Westford should now accepts Flat Car", true, loc1.acceptsTypeName("Flat Car"));
		
		// place the cars in the yards
		Assert.assertEquals("Place c1", Car.OKAY, c1.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c2", Car.OKAY, c2.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c3", Car.OKAY, c3.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c4", Car.OKAY, c4.setLocation(loc1, loc1trk1));
		
		Assert.assertEquals("Place c5", Car.OKAY, c5.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place c6", Car.OKAY, c6.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place c7", Car.OKAY, c7.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place c8", Car.OKAY, c8.setLocation(loc1, loc1trk2));
		
		Assert.assertEquals("Place c9", Car.OKAY, c9.setLocation(loc1, loc1trk3));

		// do cars have the right default loads?
		Assert.assertEquals("Car c1 load should be E", "E", c1.getLoad());
		Assert.assertEquals("Car c2 load should be E", "E", c2.getLoad());
		Assert.assertEquals("Car c3 load should be E", "E", c3.getLoad());
		Assert.assertEquals("Car c4 load should be E", "E", c4.getLoad());
		Assert.assertEquals("Car c5 load should be E", "E", c5.getLoad());
		Assert.assertEquals("Car c6 load should be E", "E", c6.getLoad());
		Assert.assertEquals("Car c7 load should be E", "E", c7.getLoad());
		Assert.assertEquals("Car c8 load should be E", "E", c8.getLoad());
		Assert.assertEquals("Car c9 load should be E", "E", c9.getLoad());
		
		// Build train
		train1.build(false);

		Assert.assertEquals("Train 1 After Build Departs Name", "Westford", train1.getTrainDepartsName());
		Assert.assertEquals("Train 1 After Build Terminates Name", "Westford", train1.getTrainTerminatesName());
		Assert.assertEquals("Train 1 After Build Next Location Name", "Westford", train1.getNextLocationName());
		Assert.assertEquals("Train 1 After Build Built Status", true, train1.getBuilt());

		// are the right cars assigned to the train?
		// the default moves is 7, therefore only 7 cars should be moved based on their move counts
		Assert.assertEquals("Car c1 After Build should be assigned to Train 1", train1, c1.getTrain());
		Assert.assertEquals("Car c2 After Build should be assigned to Train 1", train1, c2.getTrain());
		Assert.assertEquals("Car c3 After Build should be assigned to Train 1", train1, c3.getTrain());
		Assert.assertEquals("Car c4 After Build should NOT be assigned to Train 1", train1, c4.getTrain());
		Assert.assertEquals("Car c5 After Build should be assigned to Train 1", train1, c5.getTrain());
		Assert.assertEquals("Car c6 After Build should be assigned to Train 1", train1, c6.getTrain());
		Assert.assertEquals("Car c7 After Build should NOT be assigned to Train 1", null, c7.getTrain());
		Assert.assertEquals("Car c8 After Build should be assigned to Train 1", train1, c8.getTrain());
		Assert.assertEquals("Car c9 After Build should NOT be assigned to Train 1", null, c9.getTrain());
		
		// now check to see if cars are going to be delivered to the right places?
		Assert.assertEquals("Car c1 After Build destination", "Westford Interchange 5", c1.getDestinationTrackName());
		Assert.assertEquals("Car c2 After Build destination", "Westford Siding 4", c2.getDestinationTrackName());
		Assert.assertEquals("Car c3 After Build destination", "Westford Interchange 7", c3.getDestinationTrackName());
		Assert.assertEquals("Car c4 After Build destination", "Westford Siding 3", c4.getDestinationTrackName());
		Assert.assertEquals("Car c5 After Build destination", "Westford Interchange 5", c5.getDestinationTrackName());
		Assert.assertEquals("Car c6 After Build destination", "Westford Interchange 7", c6.getDestinationTrackName());
		Assert.assertEquals("Car c8 After Build destination", "Westford Interchange 6", c8.getDestinationTrackName());
		
		// are the pickup and drop counts correct?
		Assert.assertEquals("Drop count for Westford", 7, loc1.getDropRS()); 
		Assert.assertEquals("Drop count for Westford track Westford Yard 1", 0, loc1trk1.getDropRS());
		Assert.assertEquals("Drop count for Westford track Westford Yard 2", 0, loc1trk2.getDropRS());
		Assert.assertEquals("Drop count for Westford track Westford Siding 3", 1, loc1trk3.getDropRS());
		Assert.assertEquals("Drop count for Westford track Westford Siding 4", 1, loc1trk4.getDropRS());
		Assert.assertEquals("Drop count for Westford track Westford Interchange 5", 2, loc1trk5.getDropRS());
		Assert.assertEquals("Drop count for Westford track Westford Interchange 6", 1, loc1trk6.getDropRS());
		Assert.assertEquals("Drop count for Westford track Westford Interchange 7", 2, loc1trk7.getDropRS());
		Assert.assertEquals("Pickup count for Westford", 7, loc1.getPickupRS());
		Assert.assertEquals("Pickup count for Westford track Westford Yard 1", 4, loc1trk1.getPickupRS());
		Assert.assertEquals("Pickup count for Westford track Westford Yard 2", 3, loc1trk2.getPickupRS());
		Assert.assertEquals("Pickup count for Westford track Westford Siding 3", 0, loc1trk3.getPickupRS());
		Assert.assertEquals("Pickup count for Westford track Westford Siding 4", 0, loc1trk4.getPickupRS());
		Assert.assertEquals("Pickup count for Westford track Westford Interchange 5", 0, loc1trk5.getPickupRS());
		Assert.assertEquals("Pickup count for Westford track Westford Interchange 6", 0, loc1trk6.getPickupRS());
		Assert.assertEquals("Pickup count for Westford track Westford Interchange 7", 0, loc1trk7.getPickupRS());

		train1.move();
		// Train should not be in route since there's only one location
		Assert.assertEquals("Train 1 in route", false, train1.isTrainInRoute());
		// check for correct tracks
		Assert.assertEquals("Car c1 After Move location", "Westford Interchange 5", c1.getTrackName());
		Assert.assertEquals("Car c2 After Move location", "Westford Siding 4", c2.getTrackName());
		Assert.assertEquals("Car c3 After Move location", "Westford Interchange 7", c3.getTrackName());
		Assert.assertEquals("Car c4 After Move location", "Westford Siding 3", c4.getTrackName());
		Assert.assertEquals("Car c5 After Move location", "Westford Interchange 5", c5.getTrackName());
		Assert.assertEquals("Car c6 After Move location", "Westford Interchange 7", c6.getTrackName());
		Assert.assertEquals("Car c8 After Move location", "Westford Interchange 6", c8.getTrackName());

		// do cars have the right loads?
		Assert.assertEquals("Car c1 After Move load should be E", "E", c1.getLoad());
		Assert.assertEquals("Car c2 After Move load should be L", "L", c2.getLoad());
		Assert.assertEquals("Car c3 After Move load should be E", "E", c3.getLoad());
		Assert.assertEquals("Car c4 After Move load should be L", "L", c4.getLoad());
		Assert.assertEquals("Car c5 After Move load should be E", "E", c5.getLoad());
		Assert.assertEquals("Car c6 After Move load should be E", "E", c6.getLoad());
		Assert.assertEquals("Car c7 After Move load should be E", "E", c7.getLoad());
		Assert.assertEquals("Car c8 After Move load should be E", "E", c8.getLoad());
		Assert.assertEquals("Car c9 After Move load should be E", "E", c9.getLoad());

		// are the pickup and drop counts correct?
		Assert.assertEquals("Move 1 Drop count for Westford", 0, loc1.getDropRS()); 
		Assert.assertEquals("Move 1 Drop count for Westford track Westford Yard 1", 0, loc1trk1.getDropRS());
		Assert.assertEquals("Move 1 Drop count for Westford track Westford Yard 2", 0, loc1trk2.getDropRS());
		Assert.assertEquals("Move 1 Drop count for Westford track Westford Siding 3", 0, loc1trk3.getDropRS());
		Assert.assertEquals("Move 1 Drop count for Westford track Westford Siding 4", 0, loc1trk4.getDropRS());
		Assert.assertEquals("Move 1 Drop count for Westford track Westford Interchange 5", 0, loc1trk5.getDropRS());
		Assert.assertEquals("Move 1 Drop count for Westford track Westford Interchange 6", 0, loc1trk6.getDropRS());
		Assert.assertEquals("Move 1 Drop count for Westford track Westford Interchange 7", 0, loc1trk7.getDropRS());
		Assert.assertEquals("Move 1 Pickup count for Westford", 0, loc1.getPickupRS());
		Assert.assertEquals("Move 1 Pickup count for Westford track Westford Yard 1", 0, loc1trk1.getPickupRS());
		Assert.assertEquals("Move 1 Pickup count for Westford track Westford Yard 2", 0, loc1trk2.getPickupRS());
		Assert.assertEquals("Move 1 Pickup count for Westford track Westford Siding 3", 0, loc1trk3.getPickupRS());
		Assert.assertEquals("Move 1 Pickup count for Westford track Westford Siding 4", 0, loc1trk4.getPickupRS());
		Assert.assertEquals("Move 1 Pickup count for Westford track Westford Interchange 5", 0, loc1trk5.getPickupRS());
		Assert.assertEquals("Move 1 Pickup count for Westford track Westford Interchange 6", 0, loc1trk6.getPickupRS());
		Assert.assertEquals("Move 1 Pickup count for Westford track Westford Interchange 7", 0, loc1trk7.getPickupRS());

		// This move should terminate the train.
		train1.move();
		Assert.assertEquals("Train 1 After 2nd Move Status", Train.TERMINATED, train1.getStatus());

		// build the train again, now there are cars on all tracks
		rl1.setMaxCarMoves(10);	// try and use all 9/10 of the cars
		train1.build(false);
		// c1, c3, c5, c6, c8 are at interchange tracks and should not be assigned to train1
		Assert.assertEquals("Car c1 After Build 2 should NOT be assigned to Train 1", null, c1.getTrain());
		Assert.assertEquals("Car c2 After Build 2 should be assigned to Train 1", train1, c2.getTrain());
		Assert.assertEquals("Car c3 After Build 2 should NOT be assigned to Train 1", null, c3.getTrain());
		Assert.assertEquals("Car c4 After Build 2 should be assigned to Train 1", train1, c4.getTrain());
		Assert.assertEquals("Car c5 After Build 2 should NOT be assigned to Train 1", null, c5.getTrain());
		Assert.assertEquals("Car c6 After Build 2 should NOT be assigned to Train 1", null, c6.getTrain());
		Assert.assertEquals("Car c7 After Build 2 should be assigned to Train 1", train1, c7.getTrain());
		Assert.assertEquals("Car c8 After Build 2 should NOT be assigned to Train 1", null, c8.getTrain());
		Assert.assertEquals("Car c9 After Build 2 should be assigned to Train 1", train1, c9.getTrain());

		// now check to see if cars are going to be delivered to the right places?
		Assert.assertEquals("Car c2 After Build 2 destination", "Westford Yard 1", c2.getDestinationTrackName());
		Assert.assertEquals("Car c4 After Build 2 destination", "Westford Yard 1", c4.getDestinationTrackName());
		Assert.assertEquals("Car c7 After Build 2 destination", "Westford Interchange 6", c7.getDestinationTrackName());
		Assert.assertEquals("Car c9 After Build 2 destination", "Westford Yard 2", c9.getDestinationTrackName());
		// move and terminate
		train1.move();
		train1.move();
		Assert.assertEquals("Train 1 After 2nd build Status", Train.TERMINATED, train1.getStatus());
		
		// are cars at the right location?
		Assert.assertEquals("Car c2 After Move 2 location", "Westford Yard 1", c2.getTrackName());
		Assert.assertEquals("Car c4 After Move 2 location", "Westford Yard 1", c4.getTrackName());
		Assert.assertEquals("Car c7 After Move 2 location", "Westford Interchange 6", c7.getTrackName());
		Assert.assertEquals("Car c9 After Move 2 location", "Westford Yard 2", c9.getTrackName());
	
		// do cars have the right loads?
		Assert.assertEquals("Car c1 After Move 2 load should be E", "E", c1.getLoad());
		Assert.assertEquals("Car c2 After Move 2 load should be L", "L", c2.getLoad());
		Assert.assertEquals("Car c3 After Move 2 load should be E", "E", c3.getLoad());
		Assert.assertEquals("Car c4 After Move 2 load should be L", "L", c4.getLoad());
		Assert.assertEquals("Car c5 After Move 2 load should be E", "E", c5.getLoad());
		Assert.assertEquals("Car c6 After Move 2 load should be E", "E", c6.getLoad());
		Assert.assertEquals("Car c7 After Move 2 load should be E", "E", c7.getLoad());
		Assert.assertEquals("Car c8 After Move 2 load should be E", "E", c8.getLoad());
		Assert.assertEquals("Car c9 After Move 2 load should be E", "E", c9.getLoad());
		
		// try a new route, this should allow cars to move from interchange
		// Create route with only one location
		Route rte2;
		rte2 = rmanager.newRoute("Local Route 2");
		RouteLocation rl2 = rte2.addLocation(loc1);
		rl2.setMaxCarMoves(8);	// move 8 of the 9 cars available
		// and assign the new route to train 1
		train1.setRoute(rte2);
		train1.build(false);
		// we should be able to pickup cars at the interchange tracks
		Assert.assertEquals("Car c1 After Build 3 should be assigned to Train 1", train1, c1.getTrain());
		Assert.assertEquals("Car c2 After Build 3 should be assigned to Train 1", train1, c2.getTrain());
		Assert.assertEquals("Car c3 After Build 3 should be assigned to Train 1", train1, c3.getTrain());
		Assert.assertEquals("Car c4 After Build 3 should be assigned to Train 1", train1, c4.getTrain());
		Assert.assertEquals("Car c5 After Build 3 should be assigned to Train 1", train1, c5.getTrain());
		Assert.assertEquals("Car c6 After Build 3 should be assigned to Train 1", train1, c6.getTrain());
		Assert.assertEquals("Car c7 After Build 3 should be assigned to Train 1", train1, c7.getTrain());
		Assert.assertEquals("Car c8 After Build 3 should be assigned to Train 1", train1, c8.getTrain());
		Assert.assertEquals("Car c9 After Build 3 should NOT be assigned to Train 1", null, c9.getTrain());

		// now check to see if cars are going to be delivered to the right places?
		Assert.assertEquals("Car c1 After Build 3 destination", "Westford Yard 1", c1.getDestinationTrackName());
		Assert.assertEquals("Car c2 After Build 3 destination", "Westford Siding 3", c2.getDestinationTrackName());
		Assert.assertEquals("Car c3 After Build 3 destination", "Westford Yard 1", c3.getDestinationTrackName());
		Assert.assertEquals("Car c4 After Build 3 destination", "Westford Interchange 7", c4.getDestinationTrackName());
		Assert.assertEquals("Car c5 After Build 3 destination", "Westford Yard 2", c5.getDestinationTrackName());
		Assert.assertEquals("Car c6 After Build 3 destination", "Westford Yard 2", c6.getDestinationTrackName());
		Assert.assertEquals("Car c7 After Build 3 destination", "Westford Yard 2", c7.getDestinationTrackName());
		Assert.assertEquals("Car c8 After Build 3 destination", "Westford Yard 1", c8.getDestinationTrackName());
		// move and terminate
		train1.move();
		train1.move();
		Assert.assertEquals("Train 1 After 2nd build Status", Train.TERMINATED, train1.getStatus());
		
		// Final check to see if cars were delivered. 
		Assert.assertEquals("Car c1 After Move 3 location", "Westford Yard 1", c1.getTrackName());
		Assert.assertEquals("Car c2 After Move 3 location", "Westford Siding 3", c2.getTrackName());
		Assert.assertEquals("Car c3 After Move 3 location", "Westford Yard 1", c3.getTrackName());
		Assert.assertEquals("Car c4 After Move 3 location", "Westford Interchange 7", c4.getTrackName());
		Assert.assertEquals("Car c5 After Move 3 location", "Westford Yard 2", c5.getTrackName());
		Assert.assertEquals("Car c6 After Move 3 location", "Westford Yard 2", c6.getTrackName());
		Assert.assertEquals("Car c7 After Move 3 location", "Westford Yard 2", c7.getTrackName());
		Assert.assertEquals("Car c8 After Move 3 location", "Westford Yard 1", c8.getTrackName());

		// do cars have the right loads?
		Assert.assertEquals("Car c1 After Move 3 load should be E", "E", c1.getLoad());
		Assert.assertEquals("Car c2 After Move 3 load should be E", "E", c2.getLoad());
		Assert.assertEquals("Car c3 After Move 3 load should be E", "E", c3.getLoad());
		Assert.assertEquals("Car c4 After Move 3 load should be L", "L", c4.getLoad());
		Assert.assertEquals("Car c5 After Move 3 load should be E", "E", c5.getLoad());
		Assert.assertEquals("Car c6 After Move 3 load should be E", "E", c6.getLoad());
		Assert.assertEquals("Car c7 After Move 3 load should be E", "E", c7.getLoad());
		Assert.assertEquals("Car c8 After Move 3 load should be E", "E", c8.getLoad());
		Assert.assertEquals("Car c9 After Move 3 load should be E", "E", c9.getLoad());
		
		// check car move counts
		Assert.assertEquals("Car c1 Move count", 19, c1.getMoves());
		Assert.assertEquals("Car c2 Move count", 18, c2.getMoves());
		Assert.assertEquals("Car c3 Move count", 2, c3.getMoves());
		Assert.assertEquals("Car c4 Move count", 9, c4.getMoves());
		Assert.assertEquals("Car c5 Move count", 3, c5.getMoves());
		Assert.assertEquals("Car c6 Move count", 5, c6.getMoves());
		Assert.assertEquals("Car c7 Move count", 20, c7.getMoves());
		Assert.assertEquals("Car c8 Move count", 4, c8.getMoves());
		Assert.assertEquals("Car c9 Move count", 20, c9.getMoves());

		// now try and use a train with more than one location
		// Create route with two locations
		Route rte3;
		rte3 = rmanager.newRoute("Westford to Chelmsford");
		RouteLocation rl3 = rte3.addLocation(loc1);
		rl3.setTrainDirection(RouteLocation.WEST);
		rl3.setMaxCarMoves(10);
		RouteLocation rl4 = rte3.addLocation(loc2);
		rl4.setTrainDirection(RouteLocation.WEST);
		// and assign the new route to train 1
		train1.setRoute(rte3);
		rl4.setMaxCarMoves(10);
		train1.build(false);
		// should not pick up cars at Westford Siding 3, Westford Siding 4, Westford Interchange 5
		// and Westford Interchange 7
		Assert.assertEquals("Car c1 After Build 4 should be assigned to Train 1", train1, c1.getTrain());
		Assert.assertEquals("Car c2 After Build 4 should NOT be assigned to Train 1", null, c2.getTrain());
		Assert.assertEquals("Car c3 After Build 4 should be assigned to Train 1", train1, c3.getTrain());
		Assert.assertEquals("Car c4 After Build 4 should NOT be assigned to Train 1", null, c4.getTrain());
		Assert.assertEquals("Car c5 After Build 4 should be assigned to Train 1", train1, c5.getTrain());
		Assert.assertEquals("Car c6 After Build 4 should be assigned to Train 1", train1, c6.getTrain());
		Assert.assertEquals("Car c7 After Build 4 should be assigned to Train 1", train1, c7.getTrain());
		Assert.assertEquals("Car c8 After Build 4 should be assigned to Train 1", train1, c8.getTrain());
		Assert.assertEquals("Car c9 After Build 4 should be assigned to Train 1", train1, c9.getTrain());

		train1.move();
		// Train in route since there's two locations
		Assert.assertEquals("Train 1 in route to Chelmsford", true, train1.isTrainInRoute());
		train1.move();
		// 7 cars should in Chelmsford, the other 2 in Westford
		Assert.assertEquals("Car c1 After Move 4 location", "Chelmsford Yard 1", c1.getTrackName());
		Assert.assertEquals("Car c2 verify location", "Westford Siding 3", c2.getTrackName());
		Assert.assertEquals("Car c3 After Move 4 location", "Chelmsford Yard 1", c3.getTrackName());
		Assert.assertEquals("Car c4 verify location", "Westford Interchange 7", c4.getTrackName());
		Assert.assertEquals("Car c5 After Move 4 location", "Chelmsford Yard 1", c5.getTrackName());
		Assert.assertEquals("Car c6 After Move 4 location", "Chelmsford Yard 1", c6.getTrackName());
		Assert.assertEquals("Car c7 After Move 4 location", "Chelmsford Yard 1", c7.getTrackName());
		Assert.assertEquals("Car c8 After Move 4 location", "Chelmsford Yard 1", c8.getTrackName());
		Assert.assertEquals("Car c9 After Move 4 location", "Chelmsford Yard 1", c9.getTrackName());
	
		// do cars have the right loads?
		Assert.assertEquals("Car c1 After Move 4 load should be E", "E", c1.getLoad());
		Assert.assertEquals("Car c2 After Move 4 load should be E", "E", c2.getLoad());
		Assert.assertEquals("Car c3 After Move 4 load should be E", "E", c3.getLoad());
		Assert.assertEquals("Car c4 After Move 4 load should be L", "L", c4.getLoad());
		Assert.assertEquals("Car c5 After Move 4 load should be E", "E", c5.getLoad());
		Assert.assertEquals("Car c6 After Move 4 load should be E", "E", c6.getLoad());
		Assert.assertEquals("Car c7 After Move 4 load should be E", "E", c7.getLoad());
		Assert.assertEquals("Car c8 After Move 4 load should be E", "E", c8.getLoad());
		Assert.assertEquals("Car c9 After Move 4 load should be E", "E", c9.getLoad());

		// check car move counts
		Assert.assertEquals("Car c1 Move count", 20, c1.getMoves());
		Assert.assertEquals("Car c2 Move count", 18, c2.getMoves());
		Assert.assertEquals("Car c3 Move count", 3, c3.getMoves());
		Assert.assertEquals("Car c4 Move count", 9, c4.getMoves());
		Assert.assertEquals("Car c5 Move count", 4, c5.getMoves());
		Assert.assertEquals("Car c6 Move count", 6, c6.getMoves());
		Assert.assertEquals("Car c7 Move count", 21, c7.getMoves());
		Assert.assertEquals("Car c8 Move count", 5, c8.getMoves());
		Assert.assertEquals("Car c9 Move count", 21, c9.getMoves());
		
		train1.move();
		Assert.assertEquals("Train 1 After 4th build Status", Train.TERMINATED, train1.getStatus());

	}
	
	public void testScheduleLoads(){
		TrainManager tmanager = TrainManager.instance();
		RouteManager rmanager = RouteManager.instance();
		LocationManager lmanager = LocationManager.instance();
		CarManager cmanager = CarManager.instance();
		ScheduleManager smanager = ScheduleManager.instance();		
		CarTypes ct = CarTypes.instance();
		
		ct.addName("Gon");
		ct.addName("Coil Car");		
		
		// create schedules
		Schedule sch1 = smanager.newSchedule("Schedule 1");
		ScheduleItem sch1Item1 = sch1.addItem("Boxcar");
		// request a UP Boxcar
		sch1Item1.setRoad("UP");
		ScheduleItem sch1Item2 = sch1.addItem("Flat Car");
		// request an empty car and load it with Scrap
		sch1Item2.setLoad("E");
		sch1Item2.setShip("Scrap");
		ScheduleItem sch1Item3 = sch1.addItem("Gon");
		// request a loaded car and load it with Tin
		sch1Item3.setLoad("L");
		sch1Item3.setShip("Tin");
		
		Schedule sch2 = smanager.newSchedule("Schedule 2");
		ScheduleItem sch2Item1 = sch2.addItem("Coil Car");
		sch2Item1.setCount(2);
		sch2.addItem("Boxcar");
		
		// Create locations used
		Location loc1;
		loc1 = lmanager.newLocation("New Westford");
		loc1.setTrainDirections(DIRECTION_ALL);	
		loc1.addTypeName("Flat Car");
		loc1.addTypeName("Boxcar");
		loc1.addTypeName("Gon");
		loc1.addTypeName("Coil Car");
		
		Location loc2;
		loc2 = lmanager.newLocation("New Chelmsford");
		loc2.setTrainDirections(DIRECTION_ALL);	
		loc2.addTypeName("Flat Car");
		loc2.addTypeName("Boxcar");
		loc2.addTypeName("Gon");
		loc2.addTypeName("Coil Car");
		
		Location loc3;
		loc3 = lmanager.newLocation("New Bedford");
		loc3.setTrainDirections(DIRECTION_ALL);	
		loc3.addTypeName("Flat Car");
		loc3.addTypeName("Boxcar");
		loc3.addTypeName("Gon");
		loc3.addTypeName("Coil Car");
		
		Track loc1trk1;
		loc1trk1 = loc1.addTrack("Westford Yard 1", Track.YARD);
		loc1trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc1trk1.setLength(900);
		loc1trk1.addTypeName("Flat Car");
		loc1trk1.addTypeName("Boxcar");
		loc1trk1.addTypeName("Gon");
		loc1trk1.addTypeName("Coil Car");
		
		Track loc1trk2;
		loc1trk2 = loc1.addTrack("Westford Yard 2", Track.YARD);
		loc1trk2.setTrainDirections(Track.WEST + Track.EAST);
		loc1trk2.setLength(500);
		loc1trk2.addTypeName("Flat Car");
		loc1trk2.addTypeName("Boxcar");
		loc1trk2.addTypeName("Gon");
		
		Track loc1trk3;
		loc1trk3 = loc1.addTrack("Westford Express 3", Track.SIDING);
		loc1trk3.setTrainDirections(Track.WEST + Track.EAST);	
		loc1trk3.setLength(300);
		loc1trk3.addTypeName("Flat Car");
		loc1trk3.addTypeName("Boxcar");
		
		Track loc1trk4;
		loc1trk4 = loc1.addTrack("Westford Express 4", Track.SIDING);
		loc1trk4.setTrainDirections(Track.WEST + Track.EAST);
		loc1trk4.setLength(300);
		loc1trk4.addTypeName("Flat Car");
		loc1trk4.addTypeName("Boxcar");
		
		Track loc2trk1;
		loc2trk1 = loc2.addTrack("Chelmsford Freight 1", Track.SIDING);
		loc2trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk1.setLength(900);
		loc2trk1.addTypeName("Flat Car");
		loc2trk1.addTypeName("Boxcar");
		loc2trk1.addTypeName("Gon");
		loc2trk1.setScheduleName(sch1.getName());
		// start the schedule with 2nd item Flat Car
		loc2trk1.setScheduleItemId(sch1.getItemsBySequenceList().get(1));
		
		Track loc2trk2;
		loc2trk2 = loc2.addTrack("Chelmsford Freight 2", Track.SIDING);
		loc2trk2.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk2.setLength(900);
		loc2trk2.addTypeName("Flat Car");
		loc2trk2.addTypeName("Boxcar");
		loc2trk2.addTypeName("Gon");
		loc2trk2.setScheduleName(sch1.getName());
		// start the schedule with 3rd item Gon
		loc2trk2.setScheduleItemId(sch1.getItemsBySequenceList().get(2));
		
		Track loc2trk3;
		loc2trk3 = loc2.addTrack("Chelmsford Yard 3", Track.YARD);
		loc2trk3.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk3.setLength(900);
		loc2trk3.addTypeName("Flat Car");
		loc2trk3.addTypeName("Boxcar");
		
		Track loc2trk4;
		loc2trk4 = loc2.addTrack("Chelmsford Freight 4", Track.SIDING);
		loc2trk4.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk4.setLength(900);
		loc2trk4.addTypeName("Flat Car");
		loc2trk4.addTypeName("Boxcar");
		loc2trk4.addTypeName("Coil Car");
		loc2trk4.setScheduleName(sch2.getName());
		
		Track loc3trk1;
		loc3trk1 = loc3.addTrack("Bedford Yard 1", Track.STAGING);
		loc3trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc3trk1.setLength(900);
		loc3trk1.addTypeName("Flat Car");
		loc3trk1.addTypeName("Boxcar");
		loc3trk1.addTypeName("Gon");
		loc3trk1.addTypeName("Coil Car");
		loc3trk1.enableRemoveLoads(true);
		
		// Create route with 2 location
		Route rte1;
		rte1 = rmanager.newRoute("Two Location Route");
		RouteLocation rl1 = rte1.addLocation(loc1);
		rl1.setTrainDirection(RouteLocation.EAST);
		rl1.setMaxCarMoves(12);
		RouteLocation rl2 = rte1.addLocation(loc2);
		rl2.setTrainDirection(RouteLocation.EAST);
		rl2.setMaxCarMoves(12);
		
		// Create train
		Train train1;
		train1 = tmanager.newTrain("New Westford to New Chelmsford");
		train1.setRoute(rte1);
		train1.addTypeName("Boxcar");
		train1.addTypeName("Flat Car");
		train1.addTypeName("Gon");
		train1.addTypeName("Coil Car");
		
		// Set up 13 cars
		Car c1 = new Car("BM", "S1");
		c1.setType("Gon");
		c1.setLength("90");
		c1.setMoves(9);
		c1.setLoad("L");
		cmanager.register(c1);

		Car c2 = new Car("UP", "S2");
		c2.setType("Boxcar");
		c2.setLength("80");
		c2.setMoves(8);		
		cmanager.register(c2);

		Car c3 = new Car("XP", "S3");
		c3.setType("Flat Car");
		c3.setLength("70");
		c3.setMoves(7);						
		cmanager.register(c3);

		Car c4 = new Car("PU", "S4");
		c4.setType("Boxcar");
		c4.setLength("60");
		c4.setMoves(6);			
		cmanager.register(c4);

		// place two cars in a kernel
		Kernel k1 = new Kernel("TwoCars");
				
		Car c5 = new Car("UP", "S5");
		c5.setType("Gon");
		c5.setLength("50");
		c5.setMoves(5);	
		c5.setLoad("L");
		c5.setKernel(k1);
		cmanager.register(c5);

		Car c6 = new Car("CP", "S6");
		c6.setType("Boxcar");
		c6.setLength("40");
		c6.setMoves(4);	
		c6.setLoad("L");
		cmanager.register(c6);
		
		Car c7 = new Car("UP", "S7");
		c7.setType("Boxcar");
		c7.setLength("50");
		c7.setMoves(3);	
		cmanager.register(c7);
		
		Car c8 = new Car("XP", "S8");
		c8.setType("Gon");
		c8.setLength("60");
		c8.setMoves(2);			
		cmanager.register(c8);
		
		Car c9 = new Car("XP", "S9");
		c9.setType("Flat Car");
		c9.setLength("90");
		c9.setMoves(1);
		c9.setLoad("L");
		cmanager.register(c9);
		
		Car c10 = new Car("CP", "S10");
		c10.setType("Coil Car");
		c10.setLength("40");
		c10.setLoad("L");
		cmanager.register(c10);
		
		Car c11 = new Car("CP", "S11");
		c11.setType("Coil Car");
		c11.setLength("40");
		c11.setLoad("Coils");
		cmanager.register(c11);
		
		Car c12 = new Car("CP", "S12");
		c12.setType("Coil Car");
		c12.setLength("40");
		c12.setMoves(1);
		cmanager.register(c12);
		
		// place car in kernel with c5
		Car c13 = new Car("UP", "S13");
		c13.setType("Gon");
		c13.setLength("50");
		c13.setMoves(4);	
		c13.setLoad("L");
		c13.setKernel(k1);
		cmanager.register(c13);
		
		// place the cars in the yards
		Assert.assertEquals("Place c1", Car.OKAY, c1.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c2", Car.OKAY, c2.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c3", Car.OKAY, c3.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c4", Car.OKAY, c4.setLocation(loc1, loc1trk1));
		
		Assert.assertEquals("Place c5", Car.OKAY, c5.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place c6", Car.OKAY, c6.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place c7", Car.OKAY, c7.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place c8", Car.OKAY, c8.setLocation(loc1, loc1trk2));	
		Assert.assertEquals("Place c9", Car.OKAY, c9.setLocation(loc1, loc1trk2));
		
		Assert.assertEquals("Place c10", Car.OKAY, c10.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c11", Car.OKAY, c11.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c12", Car.OKAY, c12.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c13", Car.OKAY, c13.setLocation(loc1, loc1trk2));
	
		train1.build(false);
		
		// Schedule sch1 should cause c2 to be delivered to Chelmsford Freight 1
		Assert.assertEquals("c2 destination", "Chelmsford Freight 2", c2.getDestinationTrackName());
		Assert.assertEquals("c2 next load", "", c2.getNextLoad());
		// Schedule sch1 should cause c3 to be delivered to Chelmsford Freight 1
		Assert.assertEquals("c3 destination", "Chelmsford Freight 1", c3.getDestinationTrackName());
		Assert.assertEquals("c3 next load", "Scrap", c3.getNextLoad());
		// Schedule sch1 should cause c5 & c13 to be delivered to Chelmsford Freight 2
		Assert.assertEquals("c5 destination", "Chelmsford Freight 2", c5.getDestinationTrackName());
		Assert.assertEquals("c5 next load", "Tin", c5.getNextLoad());
		// C13 is part of kernel, load will flip between E and L
		Assert.assertEquals("c13 destination", "Chelmsford Freight 2", c13.getDestinationTrackName());
		Assert.assertEquals("c13 next load", "", c13.getNextLoad());

		// move and terminate train	
		train1.move();
		train1.move();
		train1.move();
	
		Assert.assertEquals("c1 track", "Chelmsford Freight 1", c1.getTrackName());
		Assert.assertEquals("c1 load", "Tin", c1.getLoad());
		Assert.assertEquals("c2 track", "Chelmsford Freight 2", c2.getTrackName());
		Assert.assertEquals("c2 load", "L", c2.getLoad());
		Assert.assertEquals("c3 track", "Chelmsford Freight 1", c3.getTrackName());
		Assert.assertEquals("c3 load", "Scrap", c3.getLoad());
		Assert.assertEquals("c4 track", "Chelmsford Freight 4", c4.getTrackName());
		Assert.assertEquals("c4 load", "L", c4.getLoad());
		Assert.assertEquals("c5 track", "Chelmsford Freight 2", c5.getTrackName());
		Assert.assertEquals("c5 load", "Tin", c5.getLoad());
		Assert.assertEquals("c6 track", "Chelmsford Yard 3", c6.getTrackName());
		Assert.assertEquals("c6 load", "L", c6.getLoad());
		Assert.assertEquals("c7 track", "Chelmsford Yard 3", c7.getTrackName());
		Assert.assertEquals("c7 load", "E", c7.getLoad());
		Assert.assertEquals("c8 track", "Westford Yard 2", c8.getTrackName());
		Assert.assertEquals("c8 load", "E", c8.getLoad());
		Assert.assertEquals("c9 track", "Chelmsford Yard 3", c9.getTrackName());
		Assert.assertEquals("c9 load", "L", c9.getLoad());
		Assert.assertEquals("c10 track", "Chelmsford Freight 4", c10.getTrackName());
		Assert.assertEquals("c10 load", "E", c10.getLoad());
		Assert.assertEquals("c11 track", "Chelmsford Freight 4", c11.getTrackName());
		Assert.assertEquals("c11 load", "E", c11.getLoad());
		Assert.assertEquals("c12 track", "Westford Yard 1", c12.getTrackName());
		Assert.assertEquals("c12 load", "E", c12.getLoad());
		Assert.assertEquals("c13 track", "Chelmsford Freight 2", c13.getTrackName());
		Assert.assertEquals("c13 load", "E", c13.getLoad());

	
		// create a route to staging to test remove schedule load
		// Create route with 2 location
		Route rte2;
		rte2 = rmanager.newRoute("Chelmsford to Staging");
		RouteLocation r2rl1 = rte2.addLocation(loc2);
		r2rl1.setTrainDirection(RouteLocation.EAST);
		r2rl1.setMaxCarMoves(12);
		RouteLocation r2rl3 = rte2.addLocation(loc3);
		r2rl3.setTrainDirection(RouteLocation.EAST);
		r2rl3.setMaxCarMoves(12);
		
		train1.setRoute(rte2);
		train1.setName("Chelmsford to Bedford");
		train1.build(false);
		
		// move and terminate train	
		train1.move();
		train1.move();
		train1.move();
		
		Assert.assertEquals("c1 track to staging", "Bedford Yard 1", c1.getTrackName());
		Assert.assertEquals("c1 load to staging", "E", c1.getLoad());
		Assert.assertEquals("c2 track to staging", "Bedford Yard 1", c2.getTrackName());
		Assert.assertEquals("c2 load to staging", "L", c2.getLoad());
		Assert.assertEquals("c3 track to staging", "Bedford Yard 1", c3.getTrackName());
		Assert.assertEquals("c3 load to staging", "E", c3.getLoad());
		Assert.assertEquals("c4 track to staging", "Bedford Yard 1", c4.getTrackName());
		Assert.assertEquals("c4 load to staging", "L", c4.getLoad());
		Assert.assertEquals("c5 track to staging", "Bedford Yard 1", c5.getTrackName());
		Assert.assertEquals("c5 load to staging", "E", c5.getLoad());
		Assert.assertEquals("c6 track to staging", "Bedford Yard 1", c6.getTrackName());
		Assert.assertEquals("c6 load to staging", "L", c6.getLoad());
		Assert.assertEquals("c7 track to staging", "Bedford Yard 1", c7.getTrackName());
		Assert.assertEquals("c7 load to staging", "E", c7.getLoad());
		Assert.assertEquals("c8 track to staging", "Westford Yard 2", c8.getTrackName());
		Assert.assertEquals("c8 load to staging", "E", c8.getLoad());
		Assert.assertEquals("c9 track to staging", "Bedford Yard 1", c9.getTrackName());
		Assert.assertEquals("c9 load to staging", "L", c9.getLoad());
		Assert.assertEquals("c10 track to staging", "Bedford Yard 1", c10.getTrackName());
		Assert.assertEquals("c10 load to staging", "E", c10.getLoad());
		Assert.assertEquals("c11 track to staging", "Bedford Yard 1", c11.getTrackName());
		Assert.assertEquals("c11 load to staging", "E", c11.getLoad());
		Assert.assertEquals("c12 track to staging", "Westford Yard 1", c12.getTrackName());
		Assert.assertEquals("c12 load to staging", "E", c12.getLoad());
		Assert.assertEquals("c13 track to staging", "Bedford Yard 1", c13.getTrackName());
		Assert.assertEquals("c13 load to staging", "E", c13.getLoad());

		
		// create a route from staging to test generate schedule load
		// Create route with 3 locations
		Route rte3;
		rte3 = rmanager.newRoute("Staging to Chelmsford");
		RouteLocation r3rl1 = rte3.addLocation(loc3);
		r3rl1.setTrainDirection(RouteLocation.EAST);
		r3rl1.setMaxCarMoves(0);		// all cars must move from staging
		RouteLocation r3rl2 = rte3.addLocation(loc2);
		r3rl2.setTrainDirection(RouteLocation.EAST);
		r3rl2.setMaxCarMoves(12);
		RouteLocation r3rl3 = rte3.addLocation(loc1);
		r3rl3.setTrainDirection(RouteLocation.EAST);
		r3rl3.setMaxCarMoves(12);
		
		loc3trk1.enableRemoveLoads(false);
		loc3trk1.enableAddLoads(true);		// generate schedule loads
		
		sch1Item1.setLoad("Metal 1");		// request these loads from staging
		sch1Item2.setLoad("Metal 2");
		sch1Item3.setLoad("Metal 3");
		
		train1.setRoute(rte3);
		train1.setName("Bedford Chelmsford Westford");
		train1.build(false);
		
		Assert.assertEquals("c1 load from staging", "Metal 3", c1.getLoad());
		Assert.assertEquals("c2 load from staging", "L", c2.getLoad());
		Assert.assertEquals("c3 load from staging", "Metal 2", c3.getLoad());
		Assert.assertEquals("c4 load from staging", "L", c4.getLoad());
		Assert.assertEquals("c5 load from staging", "E", c5.getLoad());
		Assert.assertEquals("c6 load from staging", "L", c6.getLoad());
		Assert.assertEquals("c7 load from staging", "Metal 1", c7.getLoad());
		Assert.assertEquals("c8 load from staging", "E", c8.getLoad());
		Assert.assertEquals("c9 load from staging", "L", c9.getLoad());
		Assert.assertEquals("c10 load from staging", "E", c10.getLoad());
		Assert.assertEquals("c11 load from staging", "E", c11.getLoad());
		Assert.assertEquals("c13 load from staging", "E", c13.getLoad());
		
		// move and terminate train	
		train1.move();
		train1.move();
		train1.move();
		train1.move();
		
		Assert.assertEquals("c1 track from staging terminated", "Chelmsford Freight 1", c1.getTrackName());
		Assert.assertEquals("c1 load from staging terminated", "Tin", c1.getLoad());
		Assert.assertEquals("c2 track from staging terminated", "Westford Express 4", c2.getTrackName());
		Assert.assertEquals("c2 load from staging terminated", "E", c2.getLoad());
		Assert.assertEquals("c3 track from staging terminated", "Chelmsford Freight 1", c3.getTrackName());
		Assert.assertEquals("c3 load from staging terminated", "Scrap", c3.getLoad());
		Assert.assertEquals("c4 track from staging terminated", "Chelmsford Yard 3", c4.getTrackName());
		Assert.assertEquals("c4 load from staging terminated", "L", c4.getLoad());
		Assert.assertEquals("c5 track from staging terminated", "Westford Yard 1", c5.getTrackName());
		Assert.assertEquals("c5 load from staging terminated", "E", c5.getLoad());
		Assert.assertEquals("c6 track from staging terminated", "Westford Express 3", c6.getTrackName());
		Assert.assertEquals("c6 load from staging terminated", "E", c6.getLoad());
		Assert.assertEquals("c7 track from staging terminated", "Chelmsford Freight 1", c7.getTrackName());
		Assert.assertEquals("c7 load from staging terminated", "E", c7.getLoad());
		Assert.assertEquals("c8 track from staging terminated", "Westford Yard 2", c8.getTrackName());
		Assert.assertEquals("c8 load from staging terminated", "E", c8.getLoad());
		Assert.assertEquals("c9 track from staging terminated", "Westford Yard 2", c9.getTrackName());
		Assert.assertEquals("c9 load from staging terminated", "L", c9.getLoad());
		Assert.assertEquals("c10 track from staging terminated", "Westford Yard 1", c10.getTrackName());
		Assert.assertEquals("c10 load from staging terminated", "E", c10.getLoad());
		Assert.assertEquals("c11 track from staging terminated", "Chelmsford Freight 4", c11.getTrackName());
		Assert.assertEquals("c11 load from staging terminated", "L", c11.getLoad());
		Assert.assertEquals("c12 track from staging terminated", "Westford Yard 1", c12.getTrackName());
		Assert.assertEquals("c12 load from staging terminated", "E", c12.getLoad());
		Assert.assertEquals("c13 track from staging terminated", "Westford Yard 1", c13.getTrackName());
		Assert.assertEquals("c13 load from staging terminated", "E", c13.getLoad());


	}
	
	public void testInterchange(){
		TrainManager tmanager = TrainManager.instance();
		RouteManager rmanager = RouteManager.instance();
		LocationManager lmanager = LocationManager.instance();
		CarManager cmanager = CarManager.instance();
		CarTypes ct = CarTypes.instance();
		
		Setup.setTrainLength(500);
		ct.addName("Gon");
		ct.addName("Coil Car");	
		ct.addName("XCaboose");	
		
		// Create locations used
		Location loc1;
		loc1 = lmanager.newLocation("Old Westford");
		loc1.setTrainDirections(DIRECTION_ALL);	
		loc1.addTypeName("Flat Car");
		loc1.addTypeName("Boxcar");
		loc1.addTypeName("Gon");
		loc1.addTypeName("Coil Car");
		loc1.addTypeName("XCaboose");
		
		Location loc2;
		loc2 = lmanager.newLocation("Old Chelmsford");
		loc2.setTrainDirections(DIRECTION_ALL);	
		loc2.addTypeName("Flat Car");
		loc2.addTypeName("Boxcar");
		loc2.addTypeName("Gon");
		loc2.addTypeName("Coil Car");
		loc2.addTypeName("XCaboose");
		
		Location loc3;
		loc3 = lmanager.newLocation("Old Bedford");
		loc3.setTrainDirections(DIRECTION_ALL);	
		loc3.addTypeName("Flat Car");
		loc3.addTypeName("Boxcar");
		loc3.addTypeName("Gon");
		loc3.addTypeName("Coil Car");
		loc3.addTypeName("XCaboose");
		
		Track loc1trk1;
		loc1trk1 = loc1.addTrack("Westford Yard 1", Track.YARD);
		loc1trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc1trk1.setLength(900);
		loc1trk1.addTypeName("Flat Car");
		loc1trk1.addTypeName("Boxcar");
		loc1trk1.addTypeName("Gon");
		loc1trk1.addTypeName("Coil Car");
		loc1trk1.addTypeName("XCaboose");
		
		Track loc1trk2;
		loc1trk2 = loc1.addTrack("Westford Yard 2", Track.YARD);
		loc1trk2.setTrainDirections(Track.WEST + Track.EAST);
		loc1trk2.setLength(500);
		loc1trk2.addTypeName("Flat Car");
		loc1trk2.addTypeName("Boxcar");
		loc1trk2.addTypeName("Gon");
		
		Track loc2trk1;
		loc2trk1 = loc2.addTrack("Chelmsford Interchange 1", Track.INTERCHANGE);
		loc2trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk1.setLength(900);
		loc2trk1.addTypeName("Flat Car");
		loc2trk1.addTypeName("Boxcar");
		loc2trk1.addTypeName("Gon");

		Track loc2trk2;
		loc2trk2 = loc2.addTrack("Chelmsford Interchange 2", Track.INTERCHANGE);
		loc2trk2.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk2.setLength(900);
		loc2trk2.addTypeName("Flat Car");
		loc2trk2.addTypeName("Boxcar");
		loc2trk2.addTypeName("Gon");
		loc2trk2.addTypeName("Coil Car");
		
		Track loc2trk3;
		loc2trk3 = loc2.addTrack("Chelmsford Yard 3", Track.YARD);
		loc2trk3.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk3.setLength(900);
		loc2trk3.addTypeName("Flat Car");
		loc2trk3.addTypeName("Boxcar");
		
		Track loc2trk4;
		loc2trk4 = loc2.addTrack("Chelmsford Freight 4", Track.SIDING);
		loc2trk4.setTrainDirections(Track.WEST + Track.EAST);
		loc2trk4.setLength(900);
		loc2trk4.addTypeName("Flat Car");
		loc2trk4.addTypeName("Boxcar");
		loc2trk4.addTypeName("Coil Car");
		
		loc2trk3.setMoves(20);	// bias interchange tracks
		loc2trk4.setMoves(20);
		
		Track loc3trk1;
		loc3trk1 = loc3.addTrack("Bedford Yard 1", Track.YARD);
		loc3trk1.setTrainDirections(Track.WEST + Track.EAST);
		loc3trk1.setLength(900);
		loc3trk1.addTypeName("Flat Car");
		loc3trk1.addTypeName("Boxcar");
		loc3trk1.addTypeName("Gon");
		loc3trk1.addTypeName("Coil Car");
		loc3trk1.addTypeName("XCaboose");
		
		// Create route with 3 location
		Route rte1;
		rte1 = rmanager.newRoute("Route 1 East");
		RouteLocation r1l1 = rte1.addLocation(loc1);
		r1l1.setTrainDirection(RouteLocation.EAST);
		r1l1.setMaxCarMoves(4);
		RouteLocation r1l2 = rte1.addLocation(loc2);
		r1l2.setTrainDirection(RouteLocation.EAST);
		r1l2.setMaxCarMoves(3);
		RouteLocation r1l3 = rte1.addLocation(loc3);
		r1l3.setTrainDirection(RouteLocation.EAST);
		r1l3.setMaxCarMoves(3);
		
		// Create route with 3 location
		Route rte2;
		rte2 = rmanager.newRoute("Route 2 East");
		RouteLocation r2l1 = rte2.addLocation(loc1);
		r2l1.setTrainDirection(RouteLocation.EAST);
		r2l1.setMaxCarMoves(2);
		RouteLocation r2l2 = rte2.addLocation(loc2);
		r2l2.setTrainDirection(RouteLocation.EAST);
		r2l2.setMaxCarMoves(6);
		RouteLocation r2l3 = rte2.addLocation(loc3);
		r2l3.setTrainDirection(RouteLocation.EAST);
		r2l3.setMaxCarMoves(6);
		
		// Create trains
		Train train1;
		train1 = tmanager.newTrain("Train 1 Old Westford to Old Bedford");
		train1.setRoute(rte1);
		train1.addTypeName("Boxcar");
		train1.addTypeName("Flat Car");
		train1.addTypeName("Gon");
		train1.addTypeName("Coil Car");
		
		Train train2;
		train2 = tmanager.newTrain("Train 2 Old Westford to Old Bedford");
		train2.setRoute(rte1);
		train2.addTypeName("Boxcar");
		train2.addTypeName("Flat Car");
		train2.addTypeName("Gon");
		train2.addTypeName("Coil Car");
		
		Train train3;
		train3 = tmanager.newTrain("Train 3 Old Westford to Old Bedford");
		train3.setRoute(rte1);
		train3.addTypeName("Boxcar");
		train3.addTypeName("Flat Car");
		train3.addTypeName("Gon");
		train3.addTypeName("Coil Car");
		train3.addTypeName("XCaboose");
		
		// Set up 7 box cars and 2 flat cars
		Car c1 = new Car("BM", "Q1");
		c1.setType("Gon");
		c1.setLength("90");
		c1.setMoves(9);
		c1.setLoad("L");
		c1.setWeightTons("10");
		cmanager.register(c1);

		Car c2 = new Car("UP", "Q2");
		c2.setType("Boxcar");
		c2.setLength("80");
		c2.setMoves(8);	
		c2.setWeightTons("20");
		cmanager.register(c2);

		Car c3 = new Car("XP", "Q3");
		c3.setType("Flat Car");
		c3.setLength("70");
		c3.setMoves(7);	
		c3.setWeightTons("30");
		cmanager.register(c3);

		Car c4 = new Car("PU", "Q4");
		c4.setType("Boxcar");
		c4.setLength("60");
		c4.setMoves(6);	
		c4.setWeightTons("40");
		cmanager.register(c4);

		Car c5 = new Car("UP", "Q5");
		c5.setType("Gon");
		c5.setLength("50");
		c5.setMoves(5);	
		c5.setLoad("L");
		c5.setWeightTons("50");
		cmanager.register(c5);

		Car c6 = new Car("CP", "Q6");
		c6.setType("Boxcar");
		c6.setLength("40");
		c6.setMoves(4);	
		c6.setLoad("L");
		c6.setWeightTons("60");
		cmanager.register(c6);
		
		Car c7 = new Car("UP", "Q7");
		c7.setType("Boxcar");
		c7.setLength("50");
		c7.setMoves(3);
		c7.setWeightTons("70");
		cmanager.register(c7);
		
		Car c8 = new Car("XP", "Q8");
		c8.setType("Gon");
		c8.setLength("60");
		c8.setMoves(2);
		c8.setWeightTons("80");
		cmanager.register(c8);
		
		Car c9 = new Car("XP", "Q9");
		c9.setType("Flat Car");
		c9.setLength("90");
		c9.setMoves(1);
		c9.setLoad("L");
		c9.setWeightTons("90");
		cmanager.register(c9);
		
		Car c10 = new Car("CP", "Q10");
		c10.setType("Coil Car");
		c10.setLength("40");
		c10.setLoad("L");
		c10.setWeightTons("100");
		cmanager.register(c10);
		
		Car c11 = new Car("CP", "Q11");
		c11.setType("Coil Car");
		c11.setLength("40");
		c11.setLoad("Coils");
		c11.setWeightTons("110");
		cmanager.register(c11);
		
		Car c12 = new Car("CP", "Q12");
		c12.setType("Coil Car");
		c12.setLength("40");
		c12.setMoves(1);
		c12.setWeightTons("120");
		cmanager.register(c12);
		
		Car c13 = new Car("CP", "Q13");
		c13.setType("XCaboose");
		c13.setCaboose(true);
		c13.setLength("40");
		c13.setMoves(1);
		c13.setWeightTons("130");
		cmanager.register(c13);
		
		// place the cars in the yards
		Assert.assertEquals("Place c1", Car.OKAY, c1.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c2", Car.OKAY, c2.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c3", Car.OKAY, c3.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c4", Car.OKAY, c4.setLocation(loc1, loc1trk1));
		
		Assert.assertEquals("Place c5", Car.OKAY, c5.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place c6", Car.OKAY, c6.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place c7", Car.OKAY, c7.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place c8", Car.OKAY, c8.setLocation(loc1, loc1trk2));	
		Assert.assertEquals("Place c9", Car.OKAY, c9.setLocation(loc1, loc1trk2));
		
		Assert.assertEquals("Place c10", Car.OKAY, c10.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c11", Car.OKAY, c11.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c12", Car.OKAY, c12.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c13", Car.OKAY, c13.setLocation(loc1, loc1trk1));
		
		train1.build(false);
		train2.build(false);
		
		// now check to where cars are going to be delivered
		Assert.assertEquals("c9 destination", "Chelmsford Interchange 1", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination", "Bedford Yard 1", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination", "Chelmsford Interchange 2", c11.getDestinationTrackName());
		Assert.assertEquals("c12 destination", "Bedford Yard 1", c12.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination", "Chelmsford Interchange 2", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination", "Bedford Yard 1", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination", "Chelmsford Interchange 1", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination", "Bedford Yard 1", c8.getDestinationTrackName());
		
		// now check which trains
		Assert.assertEquals("c9 train", train1, c9.getTrain());
		Assert.assertEquals("c10 train", train1, c10.getTrain());
		Assert.assertEquals("c11 train", train1, c11.getTrain());
		Assert.assertEquals("c12 train", train1, c12.getTrain());
		
		Assert.assertEquals("c5 train", train2, c5.getTrain());
		Assert.assertEquals("c6 train", train2, c6.getTrain());
		Assert.assertEquals("c7 train", train2, c7.getTrain());
		Assert.assertEquals("c8 train", train2, c8.getTrain());
		
		// try restricting interchange 1 to train1 and interchange 2 to train2
		
		loc2trk1.setDropOption(Track.TRAINS);
		loc2trk1.addDropId(train1.getId());
		loc2trk2.setDropOption(Track.TRAINS);
		loc2trk2.addDropId(train2.getId());
		
		train1.build(false);
		train2.build(false);
		
		// now check to where cars are going to be delivered
		Assert.assertEquals("c9 destination 2", "Chelmsford Interchange 1", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 2", "Bedford Yard 1", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 2", "Bedford Yard 1", c11.getDestinationTrackName());
		Assert.assertEquals("c12 destination 2", "Chelmsford Freight 4", c12.getDestinationTrackName());

		Assert.assertEquals("c4 destination 2", "Chelmsford Interchange 2", c4.getDestinationTrackName());
		Assert.assertEquals("c6 destination 2", "Bedford Yard 1", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 2", "Chelmsford Interchange 2", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 2", "Bedford Yard 1", c8.getDestinationTrackName());

		// now check which trains
		Assert.assertEquals("c9 train", train1, c9.getTrain());
		Assert.assertEquals("c10 train", train1, c10.getTrain());
		Assert.assertEquals("c11 train", train1, c11.getTrain());
		Assert.assertEquals("c12 train", train1, c12.getTrain());
		
		Assert.assertEquals("c4 train", train2, c4.getTrain());
		Assert.assertEquals("c6 train", train2, c6.getTrain());
		Assert.assertEquals("c7 train", train2, c7.getTrain());
		Assert.assertEquals("c8 train", train2, c8.getTrain());

		// move and terminate
		Assert.assertEquals("Check train 1 departure location name", "Old Westford", train1.getCurrentLocationName());
		Assert.assertEquals("Check train 1 departure location", r1l1, train1.getCurrentLocation());
		train1.move();
		Assert.assertEquals("Check train 1 location name", "Old Chelmsford", train1.getCurrentLocationName());
		Assert.assertEquals("Check train 1 location", r1l2, train1.getCurrentLocation());
		train1.move();
		Assert.assertEquals("Check train 1 location name", "Old Bedford", train1.getCurrentLocationName());
		Assert.assertEquals("Check train 1 location", r1l3, train1.getCurrentLocation());
		train1.move();
		Assert.assertEquals("Check train 1 location name", "Old Bedford", train1.getCurrentLocationName());
		Assert.assertEquals("Check train 1 location", r1l3, train1.getCurrentLocation());

		Assert.assertEquals("Check train 2 departure location name", "Old Westford", train2.getCurrentLocationName());
		Assert.assertEquals("Check train 2 departure location", r1l1, train2.getCurrentLocation());
		train2.move();
		Assert.assertEquals("Check train 2 location name", "Old Chelmsford", train2.getCurrentLocationName());
		Assert.assertEquals("Check train 2 location", r1l2, train2.getCurrentLocation());
		train2.move();
		Assert.assertEquals("Check train 2 location name", "Old Bedford", train2.getCurrentLocationName());
		Assert.assertEquals("Check train 2 location", r1l3, train2.getCurrentLocation());
		train2.move();
		Assert.assertEquals("Check train 2 location name", "Old Bedford", train2.getCurrentLocationName());
		Assert.assertEquals("Check train 2 location", r1l3, train2.getCurrentLocation());
		
		r1l1.setMaxCarMoves(2);
		r1l2.setMaxCarMoves(6);
		r1l3.setMaxCarMoves(6);
		train3.build(false);	// note that train3 uses rte1, should not pickup cars at interchange
		
		Assert.assertEquals("c1 destination 3", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 3", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 3", "Chelmsford Yard 3", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 3", "", c4.getDestinationTrackName());
		Assert.assertEquals("c5 destination 3", "Bedford Yard 1", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 3", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 3", "", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 3", "", c8.getDestinationTrackName());
		Assert.assertEquals("c9 destination 3", "", c9.getDestinationTrackName());
		Assert.assertEquals("c12 destination 3", "Bedford Yard 1", c12.getDestinationTrackName());
		
		// Change the route to 2, should be able to pickup c4, c7, c9
		train3.reset();
		train3.setRoute(rte2);
		train3.build(false);
		
		Assert.assertEquals("c1 destination 4", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 4", "Chelmsford Yard 3", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 4", "", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 4", "Bedford Yard 1", c4.getDestinationTrackName());
		Assert.assertEquals("c5 destination 4", "Bedford Yard 1", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 4", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 4", "Bedford Yard 1", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 4", "", c8.getDestinationTrackName());
		Assert.assertEquals("c9 destination 4", "Bedford Yard 1", c9.getDestinationTrackName());
		Assert.assertEquals("c12 destination 4", "Bedford Yard 1", c12.getDestinationTrackName());

		// Change back to route to 1, should be able to pickup c4, c7
		train3.reset();
		train3.setRoute(rte1);
		loc2trk2.setPickupOption(Track.TRAINS);
		loc2trk2.addPickupId(train3.getId());
		train3.build(false);
		
		Assert.assertEquals("c1 destination 5", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 5", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 5", "Chelmsford Freight 4", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 5", "Bedford Yard 1", c4.getDestinationTrackName());
		Assert.assertEquals("c5 destination 5", "Bedford Yard 1", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 5", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 5", "Bedford Yard 1", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 5", "", c8.getDestinationTrackName());
		Assert.assertEquals("c9 destination 5", "", c9.getDestinationTrackName());
		Assert.assertEquals("c12 destination 5", "Bedford Yard 1", c12.getDestinationTrackName());

		// Change back to route to 1, should be able to pickup c4, c7, and c9
		train3.reset();
		train3.setRoute(rte1);
		loc2trk1.setPickupOption(Track.ROUTES);
		loc2trk1.addPickupId(rte1.getId());
		train3.build(false);
		
		Assert.assertEquals("c1 destination 6", "Bedford Yard 1", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 6", "Chelmsford Yard 3", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 6", "", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 6", "Bedford Yard 1", c4.getDestinationTrackName());
		Assert.assertEquals("c5 destination 6", "", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 6", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 6", "Bedford Yard 1", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 6", "", c8.getDestinationTrackName());
		Assert.assertEquals("c9 destination 6", "Bedford Yard 1", c9.getDestinationTrackName());
		Assert.assertEquals("c12 destination 6", "Bedford Yard 1", c12.getDestinationTrackName());

		// now allow train 3 to drop
		train3.reset();
		loc2trk1.setDropOption(Track.ROUTES);
		loc2trk1.addDropId(rte1.getId());
		train3.build(false);
		
		Assert.assertEquals("c1 destination 7", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 7", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 7", "Chelmsford Interchange 1", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 7", "Bedford Yard 1", c4.getDestinationTrackName());
		Assert.assertEquals("c5 destination 7", "Bedford Yard 1", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 7", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 7", "Bedford Yard 1", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 7", "", c8.getDestinationTrackName());
		Assert.assertEquals("c9 destination 7", "Bedford Yard 1", c9.getDestinationTrackName());
		Assert.assertEquals("c12 destination 7", "Bedford Yard 1", c12.getDestinationTrackName());

		// move and terminate
		train3.move();
		train3.move();
		train3.move();
		train3.move();
		
		// check tracks
		Assert.assertEquals("c1 track", "Westford Yard 1", c1.getTrackName());
		Assert.assertEquals("c2 track", "Westford Yard 1", c2.getTrackName());
		Assert.assertEquals("c3 track", "Chelmsford Interchange 1", c3.getTrackName());
		Assert.assertEquals("c4 track", "Bedford Yard 1", c4.getTrackName());
		Assert.assertEquals("c5 track", "Bedford Yard 1", c5.getTrackName());
		Assert.assertEquals("c6 track", "Bedford Yard 1", c6.getTrackName());
		Assert.assertEquals("c7 track", "Bedford Yard 1", c7.getTrackName());
		Assert.assertEquals("c8 track", "Bedford Yard 1", c8.getTrackName());
		Assert.assertEquals("c9 track", "Bedford Yard 1", c9.getTrackName());
		Assert.assertEquals("c10 track", "Bedford Yard 1", c10.getTrackName());
		Assert.assertEquals("c11 track", "Bedford Yard 1", c11.getTrackName());
		Assert.assertEquals("c12 track", "Bedford Yard 1", c12.getTrackName());
		
		// check train length and tonnage
		Assert.assertEquals("Depart Westford length", 128, r1l1.getTrainLength());
		Assert.assertEquals("Depart Chelmsford length", 310, r1l2.getTrainLength());
		
		Assert.assertEquals("Depart Westford tonnage", 80, r1l1.getTrainWeight());
		Assert.assertEquals("Depart Chelmsford tonnage", 370, r1l2.getTrainWeight());
		
		// test route pickup and drop controls
		train3.setRequirements(Train.CABOOSE);
		r1l1.setCanPickup(false);
		train3.build(false);
		
		Assert.assertEquals("c1 destination 8", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 8", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 8", "Bedford Yard 1", c3.getDestinationTrackName());
		Assert.assertEquals("c13 destination 8", "Bedford Yard 1", c13.getDestinationTrackName());
		
		r1l1.setCanPickup(true);
		r1l2.setCanPickup(false);
		train3.build(false);
		
		Assert.assertEquals("c1 destination 9", "Chelmsford Interchange 1", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 9", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 9", "", c3.getDestinationTrackName());
		Assert.assertEquals("c13 destination 9", "Bedford Yard 1", c13.getDestinationTrackName());

		r1l2.setCanPickup(true);
		r1l2.setCanDrop(false);
		train3.build(false);
		
		Assert.assertEquals("c1 destination 10", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 10", "Bedford Yard 1", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 10", "Bedford Yard 1", c3.getDestinationTrackName());
		Assert.assertEquals("c13 destination 10", "Bedford Yard 1", c13.getDestinationTrackName());
		
		r1l2.setCanDrop(true);
		r1l3.setCanDrop(false);	// Should be able to drop off caboose
		train3.build(false);
		
		Assert.assertEquals("c1 destination 11", "Chelmsford Interchange 1", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 11", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 11", "", c3.getDestinationTrackName());
		Assert.assertEquals("c13 destination 11", "Bedford Yard 1", c13.getDestinationTrackName());
	
		// test to see if FRED also get delivered
		train3.setRequirements(Train.FRED);
		c2.setFred(true);
		train3.build(false);
		
		Assert.assertEquals("c1 destination 12", "Chelmsford Interchange 1", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 12", "Bedford Yard 1", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 12", "", c3.getDestinationTrackName());
		Assert.assertEquals("c13 destination 12", "", c13.getDestinationTrackName());
		
		// move and terminate
		train3.move();
		train3.move();
		train3.move();
		train3.move();
		
		Assert.assertEquals("c1 track 12", "Chelmsford Interchange 1", c1.getTrackName());
		Assert.assertEquals("c2 track 12", "Bedford Yard 1", c2.getTrackName());
		Assert.assertEquals("c3 track 12", "Chelmsford Interchange 1", c3.getTrackName());
		Assert.assertEquals("c13 track 12", "Westford Yard 1", c13.getTrackName());

		// interchange testing done, now test replace car type and road
		
		// replace should modify locations and trains
		ct.replaceName("Boxcar", "boxcar");
		
		Assert.assertFalse("loc1 should not accept Boxcar", loc1.acceptsTypeName("Boxcar"));
		Assert.assertFalse("loc2 should not accept Boxcar", loc2.acceptsTypeName("Boxcar"));
		Assert.assertFalse("track loc1trk1 should not accept Boxcar", loc1trk1.acceptsTypeName("Boxcar"));
		Assert.assertFalse("track loc2trk1 should not accept Boxcar", loc2trk1.acceptsTypeName("Boxcar"));
		Assert.assertFalse("train 1 should not accept Boxcar", train1.acceptsTypeName("Boxcar"));
		Assert.assertFalse("train 2 should not accept Boxcar", train2.acceptsTypeName("Boxcar"));
		Assert.assertFalse("train 3 should not accept Boxcar", train3.acceptsTypeName("Boxcar"));

		Assert.assertTrue("loc1 should accept boxcar", loc1.acceptsTypeName("boxcar"));
		Assert.assertTrue("loc2 should accept boxcar", loc2.acceptsTypeName("boxcar"));
		Assert.assertTrue("track loc1trk1 should accept boxcar", loc1trk1.acceptsTypeName("boxcar"));
		Assert.assertTrue("track loc2trk1 should accept boxcar", loc2trk1.acceptsTypeName("boxcar"));
		Assert.assertTrue("train 1 should accept boxcar", train1.acceptsTypeName("boxcar"));
		Assert.assertTrue("train 2 should accept boxcar", train2.acceptsTypeName("boxcar"));
		Assert.assertTrue("train 3 should accept boxcar", train3.acceptsTypeName("boxcar"));

		ct.replaceName("boxcar", "Boxcar");
		
		Assert.assertTrue("loc1 should accept Boxcar", loc1.acceptsTypeName("Boxcar"));
		Assert.assertTrue("loc2 should accept Boxcar", loc2.acceptsTypeName("Boxcar"));
		Assert.assertTrue("track loc1trk1 should accept Boxcar", loc1trk1.acceptsTypeName("Boxcar"));
		Assert.assertTrue("track loc2trk1 should accept Boxcar", loc2trk1.acceptsTypeName("Boxcar"));
		Assert.assertTrue("train 1 should accept Boxcar", train1.acceptsTypeName("Boxcar"));
		Assert.assertTrue("train 2 should accept Boxcar", train2.acceptsTypeName("Boxcar"));
		Assert.assertTrue("train 3 should accept Boxcar", train3.acceptsTypeName("Boxcar"));

		// now test road name replace
				
		loc1trk1.setRoadOption(Track.INCLUDEROADS);
		loc1trk1.addRoadName("CP");
		loc1trk1.addRoadName("PC");
		train1.setRoadOption(Train.INCLUDEROADS);
		train1.addRoadName("CP");
		train1.addRoadName("PC");
		train1.setCabooseRoad("CP");
		train1.setEngineRoad("CP");
		
		Assert.assertTrue("track loc1trk1 should accept road CP", loc1trk1.acceptsRoadName("CP"));
		Assert.assertTrue("track loc1trk1 should accept road PC", loc1trk1.acceptsRoadName("PC"));
		Assert.assertFalse("track loc1trk1 should Not accept road PC", loc1trk1.acceptsRoadName("UP"));
		Assert.assertTrue("Train 1 should accept road CP", train1.acceptsRoadName("CP"));
		Assert.assertTrue("Train 1 should accept road PC", train1.acceptsRoadName("PC"));
		Assert.assertFalse("Train 1 should Not accept road UP", train1.acceptsRoadName("UP"));
		Assert.assertEquals("Caboose road", "CP", train1.getCabooseRoad());
		Assert.assertEquals("Engine road", "CP", train1.getEngineRoad());

		CarRoads cr = CarRoads.instance();
		cr.replaceName("CP", "UP");
		
		Assert.assertFalse("after replace track loc1trk1 should Not accept road CP", loc1trk1.acceptsRoadName("CP"));
		Assert.assertTrue("after replace track loc1trk1 should accept road PC", loc1trk1.acceptsRoadName("PC"));
		Assert.assertTrue("after replace track loc1trk1 should accept road PC", loc1trk1.acceptsRoadName("UP"));
		Assert.assertFalse("after replace Train 1 should Not accept road CP", train1.acceptsRoadName("CP"));
		Assert.assertTrue("after replace Train 1 should accept road PC", train1.acceptsRoadName("PC"));
		Assert.assertTrue("after replace Train 1 should accept road UP", train1.acceptsRoadName("UP"));
		Assert.assertEquals("Caboose road", "UP", train1.getCabooseRoad());
		Assert.assertEquals("Engine road", "UP", train1.getEngineRoad());

	}
	
	// test train requires caboose or FRED
	// uses defaults for location and train direction
	// assumes 7 moves per route location which was set by previous test
	public void testCaboose() {
		TrainManager tmanager = TrainManager.instance();
		RouteManager rmanager = RouteManager.instance();
		LocationManager lmanager = LocationManager.instance();
		EngineManager emanager = EngineManager.instance();
		CarManager cmanager = CarManager.instance();
		CarTypes ct = CarTypes.instance();
		EngineTypes et = EngineTypes.instance();
		
		// register the car and engine types used
		ct.addName("Boxcar");
		ct.addName("Caboose");
		ct.addName("Flat");
		et.addName("Diesel");
		
		// place two engines in a consist
		Consist con1 = new Consist("C1");
		
		Engine e1 = emanager.newEngine("UP", "1");
		e1.setModel("GP40");
		e1.setConsist(con1);
		Engine e2 = emanager.newEngine("SP", "2");
		e2.setModel("GP40");
		e2.setConsist(con1);
		
		// Set up three cabooses and six box cars
		Car c1 = cmanager.newCar("UP", "1");
		c1.setType("Caboose");
		c1.setLength("32");
		c1.setMoves(10);
		c1.setCaboose(true);
		
		Car c2 = cmanager.newCar("SP", "2");
		c2.setType("Caboose");
		c2.setLength("30");
		c2.setMoves(5);
		c2.setCaboose(true);
		
		Car c3 = cmanager.newCar("NH", "3");
		c3.setType("Caboose");
		c3.setLength("33");
		c3.setCaboose(true);
		
		Car c4 = cmanager.newCar("UP", "4");
		c4.setType("Boxcar");
		c4.setLength("40");
		c4.setMoves(16);
		c4.setFred(true);

		Car c5 = cmanager.newCar("SP", "5");
		c5.setType("Boxcar");
		c5.setLength("40");
		c5.setMoves(8);
		c5.setFred(true);
		
		Car c6 = cmanager.newCar("NH", "6");
		c6.setType("Boxcar");
		c6.setLength("40");
		c6.setMoves(2);
		c6.setFred(true);
		
		Car c7 = cmanager.newCar("UP", "7");
		c7.setType("Flat");
		c7.setLength("40");
		c7.setMoves(5);

		Car c8 = cmanager.newCar("SP", "8");
		c8.setType("Boxcar");
		c8.setLength("40");
		c8.setMoves(4);
		
		Car c9 = cmanager.newCar("NH", "9");
		c9.setType("Boxcar");
		c9.setLength("40");
		c9.setMoves(3);
		
		Car c10 = cmanager.newCar("NH", "10");
		c10.setType("Boxcar");
		c10.setLength("40");
		c10.setMoves(10);
		
		Car c11 = cmanager.newCar("SP", "11");
		c11.setType("Boxcar");
		c11.setLength("40");
		c11.setMoves(3);

		// Create 3 locations
		Location loc1 = lmanager.newLocation("Harvard");
		loc1.addTypeName("Boxcar");
		loc1.addTypeName("Diesel");
		loc1.addTypeName("Flat");
		loc1.addTypeName("Caboose");
		
		Track loc1trk1 = loc1.addTrack("Harvard Yard", Track.YARD);
		loc1trk1.setLength(1000);
		loc1trk1.addTypeName("Boxcar");
		loc1trk1.addTypeName("Diesel");
		loc1trk1.addTypeName("Flat");
		loc1trk1.addTypeName("Caboose");
		
		Location loc2 = lmanager.newLocation("Arlington");
		loc2.addTypeName("Boxcar");
		loc2.addTypeName("Diesel");
		loc2.addTypeName("Flat");
		loc2.addTypeName("Caboose");
		
		Track loc2trk1 = loc2.addTrack("Arlington Yard", Track.YARD);
		loc2trk1.setLength(1000);
		loc2trk1.addTypeName("Boxcar");
		loc2trk1.addTypeName("Diesel");
		loc2trk1.addTypeName("Flat");
		loc2trk1.addTypeName("Caboose");
		
		Location loc3 = lmanager.newLocation("Boston");
		loc3.addTypeName("Boxcar");
		loc3.addTypeName("Diesel");
		loc3.addTypeName("Flat");
		loc3.addTypeName("Caboose");
		
		Track loc3trk1 = loc3.addTrack("Boston Yard", Track.YARD);
		loc3trk1.setLength(1000);
		loc3trk1.addTypeName("Boxcar");
		loc3trk1.addTypeName("Flat");
		loc3trk1.addTypeName("Caboose");
		
		Track loc3trk2 = loc3.addTrack("Boston Engine Yard", Track.YARD);
		loc3trk2.setLength(200);
		loc3trk2.addTypeName("Diesel");
		
		// Create route with 3 location
		Route rte1 = rmanager.newRoute("Route 2 Boston");
		rte1.addLocation(loc1);
		RouteLocation rl2 = rte1.addLocation(loc2);
		RouteLocation rl3 = rte1.addLocation(loc3);
		
		// Create train
		Train train1 = tmanager.newTrain("Harvard to Boston");
		train1.setRoute(rte1);
		train1.addTypeName("Boxcar");
		train1.addTypeName("Diesel");
		train1.addTypeName("Flat");
		train1.addTypeName("Caboose");
		
		// Place cars
		Assert.assertEquals("Place c1", Car.OKAY, c1.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c2", Car.OKAY, c2.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c3", Car.OKAY, c3.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c4", Car.OKAY, c4.setLocation(loc1, loc1trk1));
		
		Assert.assertEquals("Place c5", Car.OKAY, c5.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c6", Car.OKAY, c6.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c7", Car.OKAY, c7.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c8", Car.OKAY, c8.setLocation(loc1, loc1trk1));
		
		Assert.assertEquals("Place c9", Car.OKAY, c9.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place c10", Car.OKAY, c10.setLocation(loc2, loc2trk1));
		Assert.assertEquals("Place c11", Car.OKAY, c11.setLocation(loc2, loc2trk1));
		
		// Place engines
		Assert.assertEquals("Place e1", Engine.OKAY, e1.setLocation(loc1, loc1trk1));
		Assert.assertEquals("Place e2", Engine.OKAY, e2.setLocation(loc1, loc1trk1));
		
		// no requirements, so no caboose or FRED or engines
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 1", true, train1.getBuilt());
		
		// check destinations
		Assert.assertEquals("c1 destination 1", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 1", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 1", "", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 1", "", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 1", "", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 1", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 1", "Boston Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 1", "Arlington Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 1", "Boston Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 1", "Boston Yard", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 1", "Boston Yard", c11.getDestinationTrackName());

		Assert.assertEquals("e1 destination 1", "", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 1", "", e2.getDestinationTrackName());

		// no engines, so the caboose with least moves should be used
		train1.setRequirements(Train.CABOOSE);
		// don't allow pickups at second location Arlington
		rl2.setCanPickup(false);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 2", true, train1.getBuilt());
		
		// check destinations
		Assert.assertEquals("c1 destination 2", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 2", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 2", "Boston Yard", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 2", "", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 2", "", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 2", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 2", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 2", "Boston Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 2", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 2", "", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 2", "", c11.getDestinationTrackName());

		Assert.assertEquals("e1 destination 2", "", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 2", "", e2.getDestinationTrackName());
		
		// there's a caboose c1 that matches lead engine
		train1.setNumberEngines("2");
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 3", true, train1.getBuilt());
		
		// check destinations
		Assert.assertEquals("c1 destination 3", "Boston Yard", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 3", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 3", "", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 3", "", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 3", "", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 3", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 3", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 3", "Boston Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 3", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 3", "", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 3", "", c11.getDestinationTrackName());

		Assert.assertEquals("e1 destination 3", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 3", "Boston Engine Yard", e2.getDestinationTrackName());

		// should default to the caboose with the least moves
		e1.setRoad("X");
		// allow pickups at Arlington
		rl2.setCanPickup(true);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 4", true, train1.getBuilt());
		
		// check destinations
		Assert.assertEquals("c1 destination 4", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 4", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 4", "Boston Yard", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 4", "", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 4", "", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 4", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 4", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 4", "Boston Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 4", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 4", "Boston Yard", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 4", "Boston Yard", c11.getDestinationTrackName());

		Assert.assertEquals("e1 destination 4", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 4", "Boston Engine Yard", e2.getDestinationTrackName());

		// don't allow drops at Boston, caboose and engines should still drop there
		rl3.setCanDrop(false);
		// should take NH engine even if NH is excluded, see a couple of tests below
		// should not take NH caboose
		e1.setRoad("NH");
		// now require a SP caboose
		train1.setCabooseRoad("SP");
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 5", true, train1.getBuilt());
		// check destinations
		Assert.assertEquals("c1 destination 5", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 5", "Boston Yard", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 5", "", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 5", "", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 5", "", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 5", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 5", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 5", "Arlington Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 5", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 5", "", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 5", "", c11.getDestinationTrackName());

		Assert.assertEquals("e1 destination 5", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 5", "Boston Engine Yard", e2.getDestinationTrackName());

		// allow drops at Boston
		rl3.setCanDrop(true);
		// should take car with FRED and road SP
		train1.setRequirements(Train.FRED);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 6", true, train1.getBuilt());
		// check destinations
		Assert.assertEquals("c1 destination 6", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 6", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 6", "", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 6", "", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 6", "Boston Yard", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 6", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 6", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 6", "Boston Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 6", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 6", "Boston Yard", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 6", "Boston Yard", c11.getDestinationTrackName());

		Assert.assertEquals("e1 destination 6", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 6", "Boston Engine Yard", e2.getDestinationTrackName());

		// should take car with FRED least number of moves
		train1.setCabooseRoad("");
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 7", true, train1.getBuilt());
		// check destinations
		Assert.assertEquals("c1 destination 7", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 7", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 7", "", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 7", "", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 7", "", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 7", "Boston Yard", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 7", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 7", "Boston Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 7", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 7", "Boston Yard", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 7", "Boston Yard", c11.getDestinationTrackName());
		
		Assert.assertEquals("e1 destination 7", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 7", "Boston Engine Yard", e2.getDestinationTrackName());

		// now exclude road NH, engine road is NH and should be taken
		train1.addRoadName("NH");
		train1.setRoadOption(Train.EXCLUDEROADS);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 8", true, train1.getBuilt());
		// check destinations
		Assert.assertEquals("c1 destination 8", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 8", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 8", "", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 8", "", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 8", "Boston Yard", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 8", "", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 8", "Boston Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 8", "Arlington Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 8", "", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 8", "", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 8", "Boston Yard", c11.getDestinationTrackName());

		Assert.assertEquals("e1 destination 8", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 8", "Boston Engine Yard", e2.getDestinationTrackName());

		// now only include NH
		train1.setRoadOption(Train.INCLUDEROADS);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 9", true, train1.getBuilt());
		// check destinations
		Assert.assertEquals("c1 destination 9", "", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 9", "", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 9", "", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 9", "", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 9", "", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 9", "Boston Yard", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 9", "", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 9", "", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 9", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 9", "Boston Yard", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 9", "", c11.getDestinationTrackName());

		Assert.assertEquals("e1 destination 9", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 9", "Boston Engine Yard", e2.getDestinationTrackName());

		train1.reset();
		// add staging
		Track loc1trk2 = loc1.addTrack("Harvard Staging", Track.STAGING);
		loc1trk2.setLength(1000);
		loc1trk2.addTypeName("Boxcar");
		loc1trk2.addTypeName("Diesel");
		loc1trk2.addTypeName("Flat");
		loc1trk2.addTypeName("Caboose");
		// now depart staging, must take all cars in staging
		// Place cars
		Assert.assertEquals("Move c1", Car.OKAY, c1.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Move c2", Car.OKAY, c2.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Move c3", Car.OKAY, c3.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Move c4", Car.OKAY, c4.setLocation(loc1, loc1trk2));
		
		Assert.assertEquals("Move c5", Car.OKAY, c5.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Move c6", Car.OKAY, c6.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Move c7", Car.OKAY, c7.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Move c8", Car.OKAY, c8.setLocation(loc1, loc1trk2));	
		Assert.assertEquals("Move c9", Car.OKAY, c9.setLocation(loc1, loc1trk2));
		
		// Place engines
		Assert.assertEquals("Place e1", Engine.OKAY, e1.setLocation(loc1, loc1trk2));
		Assert.assertEquals("Place e2", Engine.OKAY, e2.setLocation(loc1, loc1trk2));

		// program requires only staging at any location, so we don't test with yard
		loc1.deleteTrack(loc1trk1);
		// All engines and cars in staging must move!  Cabooses and cars with FRED to terminal
		train1.setNumberEngines("0");
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 10", true, train1.getBuilt());
		// check destinations
		Assert.assertEquals("c1 destination 10", "Boston Yard", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 10", "Boston Yard", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 10", "Boston Yard", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 10", "Boston Yard", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 10", "Boston Yard", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 10", "Boston Yard", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 10", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 10", "Arlington Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 10", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 10", "Boston Yard", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 10", "", c11.getDestinationTrackName());

		Assert.assertEquals("e1 destination 10", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 10", "Boston Engine Yard", e2.getDestinationTrackName());

		// exclude road NH
		train1.setRoadOption(Train.EXCLUDEROADS);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 11", true, train1.getBuilt());
		// check destinations
		Assert.assertEquals("c1 destination 11", "Boston Yard", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 11", "Boston Yard", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 11", "Boston Yard", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 11", "Boston Yard", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 11", "Boston Yard", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 11", "Boston Yard", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 11", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 11", "Arlington Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 11", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 11", "", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 11", "Boston Yard", c11.getDestinationTrackName());
		
		Assert.assertEquals("e1 destination 11", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 11", "Boston Engine Yard", e2.getDestinationTrackName());

		// reduce Boston moves to 5, to force non caboose and FRED cars to Arlington
		rl3.setMaxCarMoves(5);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 12", true, train1.getBuilt());
		// check destinations
		Assert.assertEquals("c1 destination 12", "Boston Yard", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 12", "Boston Yard", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 12", "Boston Yard", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 12", "Boston Yard", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 12", "Boston Yard", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 12", "Boston Yard", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 12", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 12", "Arlington Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 12", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 12", "", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 12", "", c11.getDestinationTrackName());
		
		Assert.assertEquals("e1 destination 12", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 12", "Boston Engine Yard", e2.getDestinationTrackName());

		// Reduce Arlington to only two moves, this should cause train build to fail
		rl2.setMaxCarMoves(2);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 13", false, train1.getBuilt());
		
		// restore number of moves
		rl2.setMaxCarMoves(7);
		rl3.setMaxCarMoves(7);
		// don't allow drops at Boston
		rl3.setCanDrop(false);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 14", true, train1.getBuilt());
		
		// check destinations
		Assert.assertEquals("c1 destination 14", "Boston Yard", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 14", "Boston Yard", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 14", "Boston Yard", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 14", "Boston Yard", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 14", "Boston Yard", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 14", "Boston Yard", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 14", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 14", "Arlington Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 14", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 14", "", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 14", "", c11.getDestinationTrackName());
		
		Assert.assertEquals("e1 destination 14", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 14", "Boston Engine Yard", e2.getDestinationTrackName());

		// Reduce Arlington to only two moves, this should cause train build to fail
		rl2.setMaxCarMoves(2);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 15", false, train1.getBuilt());
		
		// Don't allow cabooses at Boston, should cause build failure
		rl2.setMaxCarMoves(7);
		loc3.deleteTypeName("Caboose");
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 16", false, train1.getBuilt());
		
		// Don't allow boxcars, should also cause build failure
		loc3.addTypeName("Caboose");
		loc3.deleteTypeName("Boxcar");
		train1.setRequirements(Train.NONE);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 17", false, train1.getBuilt());
		
		// allow the three road names we're testing
		loc3.addTypeName("Boxcar");
		loc3trk1.addRoadName("NH");
		loc3trk1.addRoadName("SP");
		loc3trk1.addRoadName("UP");
		loc3trk1.setRoadOption(Track.INCLUDEROADS);
		loc3trk2.addRoadName("NH");
		loc3trk2.addRoadName("SP");
		loc3trk2.addRoadName("UP");
		loc3trk2.setRoadOption(Track.INCLUDEROADS);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 18", true, train1.getBuilt());
		
		// now remove type Diesel, this should cause a failure
		loc3trk2.deleteTypeName("Diesel");
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 19", false, train1.getBuilt());
		
		// now restore type Diesel
		loc3trk2.addTypeName("Diesel");
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 20", true, train1.getBuilt());
		
		// Set the track length too short missing one set of couplers
		loc3trk2.setLength(Integer.parseInt(e1.getLength())+ Integer.parseInt(e2.getLength())+Engine.COUPLER);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 20.1", false, train1.getBuilt());
	
		// restore track length
		loc3trk2.setLength(Integer.parseInt(e1.getLength())+Integer.parseInt(e2.getLength())+2*Engine.COUPLER);
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 20.2", true, train1.getBuilt());

		// change lead engine road name, should cause build failure since Boston only 
		// accepts NH, SP, and UP.
		e1.setRoad("X");
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 21", false, train1.getBuilt());
		
		e1.setRoad("UP");
		loc3trk1.deleteRoadName("NH");	// this test that a caboose fails
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 22", false, train1.getBuilt());
		
		loc3trk1.addRoadName("NH");	
		c6.setRoad("X");	// this test that a car with FRED fails
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 23", false, train1.getBuilt());
		
		loc3trk1.addRoadName("X");
		loc2trk1.deleteTypeName("Flat");	// this test that an ordinary car must move
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 24", false, train1.getBuilt());
		
		loc2trk1.addTypeName("Flat");	// restore
		train1.build(false);
		Assert.assertEquals("Train 1 After Build 25", true, train1.getBuilt());

		// check destinations
		Assert.assertEquals("c1 destination 25", "Boston Yard", c1.getDestinationTrackName());
		Assert.assertEquals("c2 destination 25", "Boston Yard", c2.getDestinationTrackName());
		Assert.assertEquals("c3 destination 25", "Boston Yard", c3.getDestinationTrackName());
		Assert.assertEquals("c4 destination 25", "Boston Yard", c4.getDestinationTrackName());
		
		Assert.assertEquals("c5 destination 25", "Boston Yard", c5.getDestinationTrackName());
		Assert.assertEquals("c6 destination 25", "Boston Yard", c6.getDestinationTrackName());
		Assert.assertEquals("c7 destination 25", "Arlington Yard", c7.getDestinationTrackName());
		Assert.assertEquals("c8 destination 25", "Arlington Yard", c8.getDestinationTrackName());
		
		Assert.assertEquals("c9 destination 25", "Arlington Yard", c9.getDestinationTrackName());
		Assert.assertEquals("c10 destination 25", "", c10.getDestinationTrackName());
		Assert.assertEquals("c11 destination 25", "", c11.getDestinationTrackName());
		
		Assert.assertEquals("e1 destination 25", "Boston Engine Yard", e1.getDestinationTrackName());
		Assert.assertEquals("e2 destination 25", "Boston Engine Yard", e2.getDestinationTrackName());

		train1.move();
		train1.move();
		train1.move();
		
		// check final locations
		Assert.assertEquals("c1 location 24", "Boston Yard", c1.getTrackName());
		Assert.assertEquals("c2 location 24", "Boston Yard", c2.getTrackName());
		Assert.assertEquals("c3 location 24", "Boston Yard", c3.getTrackName());
		Assert.assertEquals("c4 location 24", "Boston Yard", c4.getTrackName());
		
		Assert.assertEquals("c5 location 24", "Boston Yard", c5.getTrackName());
		Assert.assertEquals("c6 location 24", "Boston Yard", c6.getTrackName());
		Assert.assertEquals("c7 location 24", "Arlington Yard", c7.getTrackName());
		Assert.assertEquals("c8 location 24", "Arlington Yard", c8.getTrackName());
		
		Assert.assertEquals("c9 location 24", "Arlington Yard", c9.getTrackName());
		Assert.assertEquals("c10 location 24", "Arlington Yard", c10.getTrackName());
		Assert.assertEquals("c11 location 24", "Arlington Yard", c11.getTrackName());
		
		Assert.assertEquals("e1 location 24", "Boston Engine Yard", e1.getTrackName());
		Assert.assertEquals("e2 location 24", "Boston Engine Yard", e2.getTrackName());

	}

	// test location Xml create support
	public void testXMLCreate() throws Exception {

		TrainManager manager = TrainManager.instance();
		List<String> temptrainList = manager.getTrainsByIdList();

		Assert.assertEquals("Starting Number of Trains", 0, temptrainList.size());
		manager.newTrain("Test Number 1");
		manager.newTrain("Test Number 2");
		manager.newTrain("Test Number 3");

		temptrainList = manager.getTrainsByIdList();

		Assert.assertEquals("New Number of Trains", 3, temptrainList.size());
		/*                
                Assert.assertEquals("New Engine by Id 1", "Test Number 1", manager.getEngineById("CPTest Number 1").getNumber());
                Assert.assertEquals("New Engine by Id 2", "Test Number 2", manager.getEngineById("ACLTest Number 2").getNumber());
                Assert.assertEquals("New Engine by Id 3", "Test Number 3", manager.getEngineById("CPTest Number 3").getNumber());

                Assert.assertEquals("New Location by Road+Name 1", "Test Number 1", manager.getEngineByRoadAndNumber("CP", "Test Number 1").getNumber());
                Assert.assertEquals("New Location by Road+Name 2", "Test Number 2", manager.getEngineByRoadAndNumber("ACL", "Test Number 2").getNumber());
                Assert.assertEquals("New Location by Road+Name 3", "Test Number 3", manager.getEngineByRoadAndNumber("CP", "Test Number 3").getNumber());

                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setBuilt("1923");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setColor("Black");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setComment("Nice runner");
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setConsist(consist);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setDestination(destination, load to staging);
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setHp("23");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setLength("50");
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setLocation(location, track);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setModel("E8");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setMoves(5);
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setOwner("TestOwner");
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setRouteDestination(routeDestination);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setRouteLocation(routeLocation);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setSavedRouteId(id);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setTrain(train);
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setWeight("87");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setWeightTons("97");


                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setType("Gas Turbine");

                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setModel("E8");
		 */                
		/*
		manager.getLocationByName("Test Location 1").setLocationOps(Location.NORMAL);
		manager.getLocationByName("Test Location 1").setSwitchList(true);
		manager.getLocationByName("Test Location 1").setTrainDirections(Location.EAST+Location.WEST);
		manager.getLocationByName("Test Location 1").addTypeName("Baggage");
		manager.getLocationByName("Test Location 1").addTypeName("BoxCar");
		manager.getLocationByName("Test Location 1").addTypeName("Caboose");
		manager.getLocationByName("Test Location 1").addTypeName("Coal");
		manager.getLocationByName("Test Location 1").addTypeName("Engine");
		manager.getLocationByName("Test Location 1").addTypeName("Hopper");
                manager.getLocationByName("Test Location 2").setComment("Test Location 2 Comment");
		manager.getLocationByName("Test Location 2").setLocationOps(Location.NORMAL);
		manager.getLocationByName("Test Location 2").setSwitchList(true);
		manager.getLocationByName("Test Location 2").setTrainDirections(Location.EAST+Location.WEST);
		manager.getLocationByName("Test Location 2").addTypeName("Baggage");
		manager.getLocationByName("Test Location 2").addTypeName("BoxCar");
		manager.getLocationByName("Test Location 2").addTypeName("Caboose");
		manager.getLocationByName("Test Location 2").addTypeName("Coal");
		manager.getLocationByName("Test Location 2").addTypeName("Engine");
		manager.getLocationByName("Test Location 2").addTypeName("Hopper");
                manager.getLocationByName("Test Location 3").setComment("Test Location 3 Comment");
		manager.getLocationByName("Test Location 3").setLocationOps(Location.NORMAL);
		manager.getLocationByName("Test Location 3").setSwitchList(true);
		manager.getLocationByName("Test Location 3").setTrainDirections(Location.EAST+Location.WEST);
		manager.getLocationByName("Test Location 3").addTypeName("Baggage");
		manager.getLocationByName("Test Location 3").addTypeName("BoxCar");
		manager.getLocationByName("Test Location 3").addTypeName("Caboose");
		manager.getLocationByName("Test Location 3").addTypeName("Coal");
		manager.getLocationByName("Test Location 3").addTypeName("Engine");
		manager.getLocationByName("Test Location 3").addTypeName("Hopper");
		 */
		/*                
                locationList = manager.getLocationsByIdList();
                Assert.assertEquals("New Number of Locations", 3, locationList.size());

                for (int i = 0; i < locationList.size(); i++) {
                    String locationId = (String)locationList.get(i);
                    Location loc = manager.getLocationById(locationId);
                    String locname = loc.getName();
                    if (i == 0) {
                        Assert.assertEquals("New Location by Id List 1", "Test Location 2", locname);
                    }
                    if (i == 1) {
                        Assert.assertEquals("New Location by Id List 2", "Test Location 1", locname);
                    }
                    if (i == 2) {
                        Assert.assertEquals("New Location by Id List 3", "Test Location 3", locname);
                    }
                }

		 */
		/*                
                locationList = manager.getLocationsByNameList();
                Assert.assertEquals("New Number of Locations", 3, locationList.size());

                for (int i = 0; i < locationList.size(); i++) {
                    String locationId = (String)locationList.get(i);
                    Location loc = manager.getLocationById(locationId);
                    String locname = loc.getName();
                    if (i == 0) {
                        Assert.assertEquals("New Location by Name List 1", "Test Location 1", locname);
                    }
                    if (i == 1) {
                        Assert.assertEquals("New Location by Name List 2", "Test Location 2", locname);
                    }
                    if (i == 2) {
                        Assert.assertEquals("New Location by Name List 3", "Test Location 3", locname);
                    }
                }
		 */


		TrainManagerXml.instance().writeOperationsTrainFile();

		// Add some more engines and write file again
		// so we can test the backup facility
		manager.newTrain("Test Number 4");
		manager.newTrain("Test Number 5");
		manager.newTrain("Test Number 6");
//		manager.getRouteByRoadAndNumber("ACL", "Test Number 2").setComment("Test Engine 2 Changed Comment");

		TrainManagerXml.instance().writeOperationsTrainFile();
	}

	// TODO: Add test of build

	// TODO: Add test to create xml file

	// TODO: Add test to read xml file

	// from here down is testing infrastructure

	// Ensure minimal setup for log4J
	@Override
	protected void setUp() {
		apps.tests.Log4JFixture.setUp();

		// Need to clear out TrainManager global variables
		TrainManager manager = TrainManager.instance();
		manager.dispose();
	}

	public OperationsTrainsTest(String s) {
		super(s);
	}

	// Main entry point
	static public void main(String[] args) {
		String[] testCaseName = {"-noloading", OperationsTrainsTest.class.getName()};
		junit.swingui.TestRunner.main(testCaseName);
	}

	// test suite from all defined tests
	public static Test suite() {
		TestSuite suite = new TestSuite(OperationsTrainsTest.class);
		return suite;
	}

	// The minimal setup for log4J
	@Override
	protected void tearDown() { apps.tests.Log4JFixture.tearDown(); }
}
