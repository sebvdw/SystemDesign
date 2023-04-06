package utils;
import model.*;
import model.printers.Printer;

import java.util.ArrayList;
import java.util.HashMap;

public class Console {
    public void printSpoolList(ArrayList<Spool> spoolList){
        System.out.println("<<---------- Spools ---------->");
        for (Spool spool : spoolList) {
            System.out.println(spool);
        }
        System.out.println("<-------------------------------------->>");
    }
    public void printPendingTasks(ArrayList<PrintTask> printTaskList){
        System.out.println("<<--------- Pending Print Tasks --------->");
        for (PrintTask printTask : printTaskList) {
            System.out.println(printTask);
        }
        System.out.println("<-------------------------------------->>");
    }

    public void printColorsList(ArrayList<String> colorList, FilamentType type){
        System.out.println("<---------- Colors ----------");
        int index = 1;
        for (String p : colorList) {
            System.out.println("- "+index+": "+p + " ("+type+")");
            index++;
        }
    }
    public void printStartNewTask(){
        System.out.println("<<---------- New Print Task ---------->");
    }

    public void printPrintsList(ArrayList<Print> printList){
        System.out.println("<<---------- Available prints ---------->");
        for (Print print : printList) {
            System.out.println(print);
        }
        System.out.println("<-------------------------------------->>");
    }
    public void printMinimalPrintsList(ArrayList<Print> printList){
        System.out.println("<---------- Available Prints ----------");
        int index = 1;
        for (Print print : printList) {
            System.out.println("- "+index+": "+print.getName());
            index++;
        }
    }
    public void printPrintersList(ArrayList<Printer> printerList){
        System.out.println("<---------- Available Printers ----------");
        for (Printer printer : printerList) {
            //print printers name and id
            System.out.println("- "+printer.getId()+":"+printer.getName());

        }
        System.out.println("<-------------------------------------->>");
    }
    public void printSuccess(){
        System.out.println(
                "Added task to queue\n" +
                "<---------------------------->>");
    }
    public void printFilament(){
        System.out.println("<---------- Filament Type ----------");
        System.out.println("- 1: PLA");
        System.out.println("- 2: PETG");
        System.out.println("- 3: ABS");
    }
    public void printStartPrintQueue(){
        System.out.println("<<---------- Start Print Queue ---------->");
    }
    public void printStartedPrintTask(Printer printer, PrintTask printTask) {
        System.out.println("- Started task: " + printTask + " on printer " + printer.getName());
    }
    public void printSpoolChangeRequest(Spool spool, Printer printer) {
        System.out.println("- Spool change: Please place spool " + spool.getId() + " in printer " + printer.getName());
    }
    public void printSpoolChangeRequest(Spool spool, Printer printer, int position) {
        System.out.println("- Spool change: Please place spool " + spool.getId() + " in printer " + printer.getName() + " at position " + position);
    }
    public void printSpoolNotFound(String color) {
        System.out.println("- Spool not found: Please add spool with color " + color);
    }
    public void printCurrentlyRunningPrinters(HashMap<Printer, PrintTask> currentlyRunningTasks) {
        System.out.println("<<---------- Currently Running Printers ---------->");
        for (Printer printer : currentlyRunningTasks.keySet()) {
            System.out.println("- " +printer.getId() + ": " + printer.getName() + ": " + currentlyRunningTasks.get(printer));
        }
        System.out.println("<-------------------------------------->>");

    }
    public void printPrinterNotFound(int id) {
        System.out.println("- Printer not found: Printer with id " + id + " not found");
    }
    public void printTaskRemovedFromPrinter(PrintTask printTask, Printer printer) {
        System.out.println("- Task " + printTask + " removed from printer " + printer.getName());
    }
    public void printNoPrintersFound() {
        System.out.println("- No printers found: Please start queue");
    }
    public void printEndLine() {
        System.out.println("<-------------------------------------->>");
    }
    public void printAllPrintStrategies(ArrayList<String> printStrategies) {
        System.out.println("<---------- Available Print Strategies ----------");
        int index = 1;
        for (String printStrategy : printStrategies) {
            System.out.println("- "+index+": "+printStrategy);
            index++;
        }
    }


}
