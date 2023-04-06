import model.*;
import model.printers.Printer;
import utils.Console;
import utils.FileReader;
import utils.PrinterAdapter;

import java.util.*;

public class PrinterManager {
    Console console = new Console();

    ArrayList<Printer> printersList = new ArrayList<Printer>();
    ArrayList<Spool> spoolList = new ArrayList<Spool>();
    ArrayList<Print> printList = new ArrayList<Print>();
    ArrayList<PrintTask> pendingPrintTasks = new ArrayList<PrintTask>();
    HashMap<Printer, PrintTask> runningPrintTasks = new HashMap();
    PrinterAdapter printerAdapter = new PrinterAdapter();


    void addNewPrintTask(int printerNumber, int filamentNumber, int colorNumber) {
        Print print = selectPrintFromList(printerNumber);
        FilamentType type = selectFilamentType(filamentNumber);
        ArrayList<String> availableColors = getColorsList(type);
        ArrayList<String> colors = selectColor(availableColors, colorNumber);
        addPrintTaskToQueue(print, colors, type);
    }
    void printBeginNewTask() {
        console.printStartNewTask();
    }
    void printPrintsList() {
        console.printMinimalPrintsList(printList);
    }
    void printSpoolList() {
        console.printSpoolList(spoolList);
    }
    void printFilamentTypes() {
        console.printFilament();
    }
    void printRunningTasks() {
        console.printCurrentlyRunningPrinters(runningPrintTasks);
        if(runningPrintTasks.isEmpty()) {
            console.printNoPrintersFound();
        }
    }
    void printColorsListForSpecificFilamentType(int filamentNumber) {
        FilamentType type = selectFilamentType(filamentNumber);
        ArrayList<String> availableColors = getColorsList(type);
        console.printColorsList(availableColors, type);
    }
    void printPrinters() {
        console.printPrintersList(printersList);
    }
    void changePrintStrategy(int printerNumber, int strategyNumber) {
        Printer printer = selectPrinterFromList(printerNumber);
        console.printAllPrintStrategies(getAllPrinterStrategies());
        String strategy = selectStrategyFromList(strategyNumber);
        printer.setPrintStrategy(strategy);
        console.printEndLine();
    }
    void printPrinterStrategies() {
        console.printAllPrintStrategies(getAllPrinterStrategies());
    }
    String selectStrategyFromList(int strategyNumber) {
        return getAllPrinterStrategies().get(strategyNumber-1);
    }

    Printer selectPrinterFromList(int printerNumber) {
        return printersList.get(printerNumber-1);
    }
    ArrayList<String> getAllPrinterStrategies() {
        ArrayList<String> strategies = new ArrayList<>();
        for (Printer p : printersList) {
            if(!strategies.contains(p.getPrintStrategy())) {
                strategies.add(p.getPrintStrategy());
            }
        }
        return strategies;
    }
    void startPrintQueue() {
        console.printStartPrintQueue();
        for(PrintTask pt : pendingPrintTasks) {
            for (Printer p : printersList) {
                if(printerCompatibleWithPrintTask(p, pt)) {
                    runningPrintTasks.put(p, pt);
                    console.printStartedPrintTask(p, pt);
                    p.setInUse();
                    break;
                }
            }
        }
        cleanUpPendingPrintTasks();
    }

    private void cleanUpPendingPrintTasks() {
        pendingPrintTasks.removeIf(pt -> runningPrintTasks.containsValue(pt));
        console.printEndLine();
    }
    private Boolean printNotInOrder(Spool[] spools, PrintTask pt) {
        for (Spool spool : spools) {
            if(spool == null) {
                return true;
            }
            if (spool.getFilamentType() != pt.getFilamentType()) {
                return true;
            }
        }
        return false;
    }
    private Spool getSpoolByColor(String color) {
        for (Spool spool : spoolList) {
            if (spool.getColor().equals(color) && !spool.isInUse()){
                return spool;
            }
        }
        return null;
    }
    private void requestForSpoolChange(Printer p, PrintTask pt) {
        assert p.getCurrentSpools().length == pt.getColors().size();
        //Check if theres any null values in the array
        if(printNotInOrder(p.getCurrentSpools(), pt)) {
            Spool[] spools = new Spool[p.getCurrentSpools().length];
            pt.getColors().forEach(color -> {
                Spool spool = getSpoolByColor(color);
                if(spool != null) {
                    spools[pt.getColors().indexOf(color)] = spool;
                    spool.setInUse();
                    console.printSpoolChangeRequest(
                            spool,
                            p,
                            pt.getColors().indexOf(color) == 0 ? 1 : pt.getColors().indexOf(color) +1);
                } else {
                    console.printSpoolNotFound(color);
                }
            });
            p.setCurrentSpools(spools);
        }
    }
    Boolean printerCompatibleWithPrintTask(Printer p, PrintTask pt) {
        if(p.printFits(pt.getPrint())) {
            if(!p.isInUse() &&
               printerAdapter.getPrinterFilamentType(p).contains(pt.getFilamentType()) &&
               pt.getColors().size() == p.getMaxSpools()) {
                //The spools logic might need to improved here
                requestForSpoolChange(p, pt);
                return true;
            }
        }
        return false;
    }

    private void reduceSpoolLengthAndFreeSpools(Spool[] spools, PrintTask pt) {
        for (Spool spool : spools) {
            spool.reduceLength(pt.getPrint().getLength());
            spool.setNotInUse();
        }
    }
    void registerPrintCompletion(int printerId) {
        registerCompletionOrFailure(printerId);
    }
    void registerPrinterFailure(int printerId) {
        registerCompletionOrFailure(printerId);
    }

    private void registerCompletionOrFailure(int printerId) {
        Map.Entry<Printer, PrintTask> foundEntry = null;
        for (Map.Entry<Printer, PrintTask> entry : runningPrintTasks.entrySet()) {
            if (entry.getKey().getId() == printerId) {
                foundEntry = entry;
                break;
            }
        }
        assert foundEntry != null;
        PrintTask task = foundEntry.getValue();
        Printer printer = foundEntry.getKey();
        runningPrintTasks.remove(foundEntry.getKey());
        console.printTaskRemovedFromPrinter(task, printer);
        printer.setNotInUse();
        reduceSpoolLengthAndFreeSpools(printer.getCurrentSpools(), task);
    }

    void showPendingPrintTasks() {
        console.printPendingTasks(pendingPrintTasks);
    }
    void showSpools() {
        console.printSpoolList(spoolList);
    }
    private void showPrinters() {
        console.printPrintersList(printersList);
    }
    void showPrints() {
        console.printPrintsList(printList);
    }
    void addPrintTaskToQueue(Print print, List<String> colors, FilamentType type) {
        assert colors.size() > 0;
        assert type != null;
        assert print != null;

        PrintTask task = new PrintTask(print, colors, type);
        pendingPrintTasks.add(task);
        console.printSuccess();
    }
    Print selectPrintFromList(int printNumber) {
        return printList.get(printNumber-1);
    }
    FilamentType selectFilamentType(int ftype) {
        assert ftype > 0 && ftype < 4;
        FilamentType type = null;
        switch (ftype) {
            case 1:
                type = FilamentType.PLA;
                break;
            case 2:
                type = FilamentType.PETG;
                break;
            case 3:
                type = FilamentType.ABS;
                break;
        }
        return type;
    }
    ArrayList<String> getColorsList(FilamentType type) {
        ArrayList<String> availableColors = new ArrayList<>();
        for (var spool : spoolList) {
            String colorString = spool.getColor();
            if(type == spool.getFilamentType() && !availableColors.contains(colorString)) {
                availableColors.add(colorString);
            }
        }
        return availableColors;
    }
    ArrayList<String> selectColor(ArrayList<String> availableColors, int colorNumber) {
        String color = availableColors.get(colorNumber-1);
        ArrayList<String> colors = new ArrayList<>();
        colors.add(color);
        return colors;
    }
    void loadDataFromFile() {
        FileReader fileReader = new FileReader();
        printersList = fileReader.readPrintersFromFile("");
        spoolList = fileReader.readSpoolsFromCsvFile("");
        printList = fileReader.readPrintsFromFile("");
    }

}
