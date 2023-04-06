package utils;

import model.*;
import model.printers.Printer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileReader {

    private String defaultPrintStrategy = "Less Spool Changes";

    public void initializeData() {
        readPrintsFromFile("");
        readSpoolsFromFile("");
        readPrintersFromFile("");
    }

    public ArrayList<Printer> readPrintersFromFile(String filename) {
        ArrayList<Printer> printersList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        if (filename.length() == 0) {
            filename = "printers.json";
        }
        URL printersResource = getClass().getResource("/" + filename);
        if (printersResource == null) {
            System.out.println("File Reader: Warning: Could not find printers.json file");
            return null;
        }
        try (java.io.FileReader reader = new java.io.FileReader(URLDecoder.decode(printersResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray printers = (JSONArray) jsonParser.parse(reader);
            for (Object p : printers) {
                JSONObject printer = (JSONObject) p;
                int id = ((Long) printer.get("id")).intValue();
                int type = ((Long) printer.get("type")).intValue();
                String name = (String) printer.get("name");
                String manufacturer = (String) printer.get("manufacturer");
                int maxX = ((Long) printer.get("maxX")).intValue();
                int maxY = ((Long) printer.get("maxY")).intValue();
                int maxZ = ((Long) printer.get("maxZ")).intValue();
                int maxColors = ((Long) printer.get("maxColors")).intValue();


                printersList.add(returnPrinter(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return printersList;
    }

    public Printer returnPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxSpools) {
        return new Printer(id, printerName, manufacturer,defaultPrintStrategy, maxX, maxY, maxZ,maxSpools, printerType);
    }

    public ArrayList<Spool> readSpoolsFromCsvFile(String filename) {
        //Read spools from csv file
        if (filename.length() == 0) {
            filename = "spools.csv";
        }
        String line = "";
        String csvSplitBy = ",";
        ArrayList<Spool> spools = new ArrayList<Spool>();
        URL printersResource = getClass().getResource("/" + filename);
        if (printersResource == null) {
            System.out.println("File Reader: Warning: Could not find spools.csv file");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new java.io.FileReader(URLDecoder.decode(printersResource.getPath(), StandardCharsets.UTF_8)))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] spoolData = line.split(csvSplitBy);
                int id = Integer.parseInt(spoolData[0]);
                String color = spoolData[1];
                String filamentType = spoolData[2];
                double length = Double.parseDouble(spoolData[3]);
                Spool spool = new Spool(id, color, getFilamentType(filamentType), length);
                spools.add(spool);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return spools;
    }

    public ArrayList<Spool> readSpoolsFromFile(String filename) {
        ArrayList<Spool> spoolsList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        if(filename.length() == 0) {
            filename = "spools.json";
        }
        URL spoolsResource = getClass().getResource("/" + filename);
        if (spoolsResource == null) {
            System.err.println("Warning: Could not find spools.json file");
            return null;
        }
        try (java.io.FileReader reader = new java.io.FileReader(URLDecoder.decode(spoolsResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray spools = (JSONArray) jsonParser.parse(reader);
            for (Object p : spools) {
                JSONObject spool = (JSONObject) p;
                int id = ((Long) spool.get("id")).intValue();
                String color = (String) spool.get("color");
                String filamentType = (String) spool.get("filamentType");
                double length = (Double) spool.get("length");
                FilamentType type = getFilamentType(filamentType);
                spoolsList.add(new Spool(id, color, type, length));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return spoolsList;
    }

    public ArrayList<Print> readPrintsFromFile(String filename) {
        ArrayList<Print> printsList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        if (filename.length() == 0) {
            filename = "prints.json";
        }
        URL printResource = getClass().getResource("/" + filename);
        if (printResource == null) {
            // ph.printError("Warning: Could not find prints.json file");
            return null;
        }
        try (java.io.FileReader reader = new java.io.FileReader(URLDecoder.decode(printResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray prints = (JSONArray) jsonParser.parse(reader);
            for (Object p : prints) {
                JSONObject print = (JSONObject) p;
                String name = (String) print.get("name");
                int height = ((Long) print.get("height")).intValue();
                int width = ((Long) print.get("width")).intValue();
                int length = ((Long) print.get("length")).intValue();
                //int filamentLength = ((Long) print.get("filamentLength")).intValue();
                JSONArray fLength = (JSONArray) print.get("filamentLength");
                int printTime = ((Long) print.get("printTime")).intValue();
                ArrayList<Double> filamentLength = new ArrayList();
                for (int i = 0; i < fLength.size(); i++) {
                    filamentLength.add(((Double) fLength.get(i)));
                }
                printsList.add(new Print(name, height, width, length, filamentLength, printTime));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return printsList;
    }
    private FilamentType getFilamentType(String filamentType) {
        FilamentType type;
        switch (filamentType) {
            case "PLA" -> type = FilamentType.PLA;
            case "PETG" -> type = FilamentType.PETG;
            case "ABS" -> type = FilamentType.ABS;
            default -> {
                System.out.println("- Not a valid filamentType, bailing out");
                return null;
            }
        }
        return type;
    }
}

