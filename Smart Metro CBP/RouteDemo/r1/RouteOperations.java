package r1;

public interface RouteOperations
{
	void viewRedLineStations();
	void viewGreenLineStations();
	void viewBlueLineStations();
	void displayMap();
	String findStation(String userStation);
	String getStationName(String lineName, int index);
	String getInterchangeStation(String mix);
	void displayRoute(String source, String destination, String lineName);
	void displayRoute(String source, String destination, String sourceLine, String destinationLine, String lineByLine, String midStation);
}
