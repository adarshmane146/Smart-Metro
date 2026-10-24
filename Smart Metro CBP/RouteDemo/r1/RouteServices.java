package r1;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class RouteServices implements RouteOperations
{
	public static RouteData obj = new RouteData();

	@Override
	public void viewRedLineStations()
	{
		System.out.println("================= Red Line Stations =================");
		for(int i = 0; i < obj.redLine.length; i++)
			System.out.println((i + 1) + ". " + obj.redLine[i]);
	}

	@Override
	public void viewGreenLineStations()
	{
		System.out.println("================= Green Line Stations =================");
		for(int i = 0; i < obj.greenLine.length; i++)
			System.out.println((i + 1) + ". " + obj.greenLine[i]);
	}

	@Override
	public void viewBlueLineStations()
	{
		System.out.println("================= Blue Line Stations =================");
		for(int i = 0; i < obj.blueLine.length; i++)
			System.out.println((i + 1) + ". " + obj.blueLine[i]);
	}

	@Override
	public void displayMap()
	{
		// Updated path to be relative / flexible
		File file = new File("RouteDemo/HMRRouteMap_new.pdf");

		if (!file.exists()) {
			// Try absolute path as fallback
			file = new File("d:/Smart Metro_CBP/Smart Metro CBP/RouteDemo/HMRRouteMap_new.pdf");
		}

		try
		{
			Desktop.getDesktop().open(file);
		}
		catch(IOException e)
		{
			System.out.println("Unable to open metro map.");
		}
	}

	@Override
	public String findStation(String input)
	{
		if(input == null)
			return null;

		String compactInput = normalizeStationName(input);

		if(compactInput.length() == 0 || isGenericInput(compactInput))
			return null;

		String result;

		result = findExact(compactInput, obj.redLine, "red");
		if(result != null) return result;

		result = findExact(compactInput, obj.blueLine, "blue");
		if(result != null) return result;

		result = findExact(compactInput, obj.greenLine, "green");
		if(result != null) return result;

		if(compactInput.length() >= 3)
		{
			result = findPartial(compactInput, obj.redLine, "red");
			if(result != null) return result;

			result = findPartial(compactInput, obj.blueLine, "blue");
			if(result != null) return result;

			result = findPartial(compactInput, obj.greenLine, "green");
			if(result != null) return result;
		}

		result = findShortcut(input, obj.redLine, "red");
		if(result != null) return result;

		result = findShortcut(input, obj.blueLine, "blue");
		if(result != null) return result;

		result = findShortcut(input, obj.greenLine, "green");
		if(result != null) return result;

		return null;
	}

	public String findExact(String input, String[] line, String lineName)
	{
		for(int i = 0; i < line.length; i++)
		{
			if(normalizeStationName(line[i]).equals(input))
				return lineName + i;
		}

		return null;
	}

	public String findPartial(String input, String[] line, String lineName)
	{
		for(int i = 0; i < line.length; i++)
		{
			if(normalizeStationName(line[i]).contains(input))
				return lineName + i;
		}

		return null;
	}

	public String findShortcut(String input, String[] line, String lineName)
	{
		String compactInput = normalizeStationName(input);

		if(compactInput.length() < 3)
			return null;

		for(int i = 0; i < line.length; i++)
		{
			if(isShortcutMatch(input, line[i]))
				return lineName + i;
		}

		return null;
	}

	public boolean isShortcutMatch(String input, String station)
	{
		String compactInput = normalizeStationName(input);

		if(compactInput.length() < 3)
			return false;

		if(isInitialMatch(compactInput, station))
			return true;

		if(isWordShortcutMatch(input, station))
			return true;

		return false;
	}

	public boolean isInitialMatch(String input, String station)
	{
		String[] words = station.toLowerCase().split("\\s+");
		String abbreviation = "";

		for(String word : words)
		{
			word = word.replaceAll("[^a-z0-9]", "");

			if(word.length() == 0)
				continue;

			if(word.length() <= 2)
				abbreviation = abbreviation + word;
			else
				abbreviation = abbreviation + word.charAt(0);
		}

		return abbreviation.startsWith(input);
	}

	public boolean isWordShortcutMatch(String input, String station)
	{
		String cleanedInput = normalizeWords(input);
		String cleanedStation = normalizeWords(station);

		if(cleanedInput.length() == 0)
			return false;

		String[] inputWords = cleanedInput.split(" ");
		String[] stationWords = cleanedStation.split(" ");

		if(inputWords.length > stationWords.length)
			return false;

		for(int i = 0; i < inputWords.length; i++)
		{
			if(!stationWords[i].startsWith(inputWords[i]))
				return false;
		}

		return true;
	}

	public boolean isGenericInput(String input)
	{
		String[] genericWords = {"station", "stations", "metro", "line", "colony", "road", "roads", "junction", "stop", "stops"};

		for(String word : genericWords)
		{
			if(input.equals(word))
				return true;
		}

		return false;
	}

	@Override
	public String getStationName(String lineName, int index)
	{
		if(lineName.equals("red")) return obj.redLine[index];
		if(lineName.equals("blue")) return obj.blueLine[index];
		if(lineName.equals("green")) return obj.greenLine[index];

		return null;
	}

	@Override
	public String getInterchangeStation(String mix)
	{
		if(mix.equals("redblue")) return obj.redBlue;
		if(mix.equals("bluered")) return obj.blueRed;
		if(mix.equals("redgreen")) return obj.redGreen;
		if(mix.equals("greenred")) return obj.greenRed;
		if(mix.equals("bluegreen")) return obj.blueGreen;
		if(mix.equals("greenblue")) return obj.greenBlue;

		return null;
	}

	@Override
	public void displayRoute(String source, String destination, String lineName)
	{
		String[] line = getLine(lineName);

		if(line == null)
		{
			System.out.println("Invalid line.");
			return;
		}

		int sourceIndex = findIndex(source, line);
		int destinationIndex = findIndex(destination, line);

		if(sourceIndex == -1 || destinationIndex == -1)
		{
			System.out.println("Station not found on the selected line.");
			return;
		}

		System.out.println();
		printCentered("========== " + lineName.toUpperCase() + " LINE ==========");
		System.out.println();
		displayPart(line, sourceIndex, destinationIndex);
	}

	@Override
	public void displayRoute(String source, String destination, String sourceLine, String destinationLine, String lineByLine, String midStation)
	{
		String[] firstLine = getLine(sourceLine);
		String[] secondLine = getLine(destinationLine);

		if(firstLine == null || secondLine == null)
		{
			System.out.println("Invalid line.");
			return;
		}

		String firstInterchange = midStation;
		String secondInterchange = getDestinationInterchange(lineByLine, destinationLine, midStation);

		int sourceIndex = findIndex(source, firstLine);
		int firstInterchangeIndex = findIndex(firstInterchange, firstLine);
		int secondInterchangeIndex = findIndex(secondInterchange, secondLine);
		int destinationIndex = findIndex(destination, secondLine);

		if(sourceIndex == -1 || firstInterchangeIndex == -1 || secondInterchangeIndex == -1 || destinationIndex == -1)
		{
			System.out.println("Unable to build route.");
			return;
		}

		System.out.println();
		printCentered("========== " + sourceLine.toUpperCase() + " LINE ==========");
		System.out.println();
		displayPart(firstLine, sourceIndex, firstInterchangeIndex);

		System.out.println();
		System.out.println();
		printCentered("========== CHANGE LINE ==========");
		System.out.println();
		printCentered("Interchange Station : " + firstInterchange);
		System.out.println();
		printCentered("Change from " + sourceLine.toUpperCase() + " LINE");
		printCentered("TO " + destinationLine.toUpperCase() + " LINE");

		System.out.println();
		printCentered("========== " + destinationLine.toUpperCase() + " LINE ==========");
		System.out.println();
		displayPart(secondLine, secondInterchangeIndex, destinationIndex);
	}

	public String[] getLine(String lineName)
	{
		if(lineName.equals("red")) return obj.redLine;
		if(lineName.equals("blue")) return obj.blueLine;
		if(lineName.equals("green")) return obj.greenLine;

		return null;
	}

	public int findIndex(String station, String[] line)
	{
		String normalizedStation = normalizeStationName(station);

		for(int i = 0; i < line.length; i++)
		{
			if(normalizeStationName(line[i]).equals(normalizedStation))
				return i;
		}

		if(normalizedStation.equals("paradeground"))
		{
			for(int i = 0; i < line.length; i++)
			{
				if(normalizeStationName(line[i]).equals("jbsparadeground"))
					return i;
			}
		}

		if(normalizedStation.equals("jbsparadeground"))
		{
			for(int i = 0; i < line.length; i++)
			{
				if(normalizeStationName(line[i]).equals("paradeground"))
					return i;
			}
		}

		return -1;
	}

	public String getDestinationInterchange(String mix, String destinationLine, String midStation)
	{
		if(mix.equals("bluegreen") && destinationLine.equals("green"))
			return "JBS Parade Ground";

		if(mix.equals("greenblue") && destinationLine.equals("blue"))
			return "Parade Ground";

		return midStation;
	}

	public void displayPart(String[] line, int start, int end)
	{
		if(start <= end)
		{
			for(int i = start; i <= end; i++)
			{
				printCentered(line[i]);

				if(i < end)
				{
					System.out.println();
					printCentered("|");
					printCentered("v");
					System.out.println();
				}
			}
		}
		else
		{
			for(int i = start; i >= end; i--)
			{
				printCentered(line[i]);

				if(i > end)
				{
					System.out.println();
					printCentered("|");
					printCentered("^");
					System.out.println();
				}
			}
		}
	}

	public void printCentered(String value)
	{
		int width = 70;
		int spaces = (width - value.length()) / 2;

		if(spaces < 0)
			spaces = 0;

		for(int i = 0; i < spaces; i++)
			System.out.print(" ");

		System.out.println(value);
	}

	public static String normalizeStationName(String station)
	{
		if(station == null)
			return "";

		station = station.trim().toLowerCase();
		station = station.replace(".", "");
		station = station.replace(",", "");
		station = station.replace("-", " ");
		station = station.replaceAll("\\s+", " ");
		station = station.replaceAll("\\s", "");

		return station;
	}

	public static String normalizeWords(String station)
	{
		if(station == null)
			return "";

		station = station.trim().toLowerCase();
		station = station.replace(".", "");
		station = station.replace(",", "");
		station = station.replace("-", " ");
		station = station.replaceAll("[^a-z0-9 ]", " ");
		station = station.replaceAll("\\s+", " ").trim();

		return station;
	}

	public int calculateDistance(String source, String destination, String lineName)
	{
		String[] line = getLine(lineName);

		if(line == null)
			return -1;

		int sourceIndex = findIndex(source, line);
		int destinationIndex = findIndex(destination, line);

		if(sourceIndex == -1 || destinationIndex == -1)
			return -1;

		return Math.abs(destinationIndex - sourceIndex);
	}

	public int calculateFare(int distance)
	{
		if(distance <= 2)
			return 10;
		else if(distance <= 5)
			return 20;
		else if(distance <= 10)
			return 30;
		else
			return 40;
	}
}
