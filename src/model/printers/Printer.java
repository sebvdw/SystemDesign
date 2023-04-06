package model.printers;

import model.Print;
import model.Spool;

public class Printer {
    private int id;
    private String name;
    private String manufacturer;
    private String printStrategy;
    private Boolean inUse = false;
    private int printerType;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private Spool[] spools;

    public Printer(int id, String printerName, String manufacturer, String printStrategy, int maxX, int maxY, int maxZ, int maxSpools, int printerType) {
        this.id = id;
        this.name = printerName;
        this.manufacturer = manufacturer;
        this.printStrategy = printStrategy;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.spools = new Spool[maxSpools];
        this.printerType = printerType;
    }
    public int getId() {
        return id;
    }

    public int CalculatePrintTime(String filename) {
        return 0;
    }
    public int getPrinterType() {
        return printerType;
    }
    public Spool[] getCurrentSpools(){return spools;};
    public void setCurrentSpools(Spool[] spools) {
        this.spools = spools;
    }
    public boolean printFits(Print print) {
        return print.getHeight() <= maxZ && print.getWidth() <= maxX && print.getLength() <= maxY;
    }
    public String getPrintStrategy(){
        return printStrategy;
    }
    public void setPrintStrategy(String printStrategy) {
        this.printStrategy = printStrategy;
    }
    public void setInUse() {
        this.inUse = true;
    }
    public void setNotInUse() {
        this.inUse = false;
    }
    public Boolean isInUse() {
        return inUse;
    }
    public int getMaxSpools() {
        return spools.length;
    }
    @Override
    public String toString() {
        return  "<--------" + System.lineSeparator() +
                "- ID: " + id + System.lineSeparator() +
                "- Name: " + name + System.lineSeparator() +
                "- Manufacturer: " + manufacturer + System.lineSeparator() +
                "-------->";
    }

    public String getName(){
        return name;
    }
}
