package utils;

import model.FilamentType;
import model.printers.Printer;

import java.util.ArrayList;

public class PrinterAdapter {
    public String getPrinterType(Printer p) {
        switch(p.getPrinterType()) {
            case 1:
                return "StandardFDM";
            case 2:
                return "HousedPrinter";
            case 3:
                return "MultiColor";
            case 4:
                return "MultiColorAcceptsABS";
            default:
                return "Unknown";
        }
    }
    public ArrayList<FilamentType> getPrinterFilamentType(Printer p) {
        ArrayList<FilamentType> filamentTypes = new ArrayList<>();
        switch(p.getPrinterType()) {
            case 1 -> {
                //StandardFDM
                filamentTypes.add(FilamentType.PLA);
                filamentTypes.add(FilamentType.PETG);
            }
            case 2 -> {
                //HousedPrinter
                filamentTypes.add(FilamentType.PLA);
                filamentTypes.add(FilamentType.PETG);
                filamentTypes.add(FilamentType.ABS);
            }
            case 3 -> {
                //MultiColor
                filamentTypes.add(FilamentType.PLA);
                filamentTypes.add(FilamentType.PETG);
            }
            default-> {
                //MultiColorAcceptsABS
                filamentTypes.add(FilamentType.PLA);
                filamentTypes.add(FilamentType.PETG);
                filamentTypes.add(FilamentType.ABS);
            }
        }
        return filamentTypes;
    }

    public int getPrinterMaxColors(Printer p) {
        return switch (p.getPrinterType()) {
            //StandardFDM
            case 1 -> 1;
            //HousedPrinter
            case 2 -> 1;
            //Default
            default -> p.getMaxSpools();
        };
    }
}
