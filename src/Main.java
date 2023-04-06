import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    Scanner scanner = new Scanner(System.in);
    PrinterManager printerManager = new PrinterManager();
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
    public void run() {
        loadDataFromFile();
        int choice = 1;
        while (choice > 0 && choice < 10) {
            printMenu();
            choice = menuChoice(9, "- Choose an option: ");
            switch(choice) {
                // Simply adds a new task to the array of tasks
                case 1 -> addNewPrintTask();
//                // When a printer has printed/is printing something (there is no difference between these two "states",
//                // hence the confusion nature of this project). This will allow you to remove the task from the printer
//                // history
                case 2 -> registerPrintCompletion();
//                // My current suspicion is that this function below has no difference to the one above.
//                // Edit: Suspicions confirmed, slightly, the only difference is that this function adds
//                // a failed print back to the queue
                case 3 -> registerPrinterFailure();
//                // This function changes the print strategy of a printer. Whatever the print strategy is not important.
//                // What is important is that this function requires you to enter an ID of a printer
                case 4 -> changePrintStrategy();
//                // This is where the magic happens. A printer with the right requirements should be selected and then
//                // assigned the task of printing an item within the queue, this happens with each item.
                case 5 -> startPrintQueue();
                // Basic D of CRUD
                case 6 -> showPrints();
                // Basic D of CRUD
                case 7 -> showPrinters();
                // Basic D of CRUD
                case 8 -> showSpools();
                // Basic D of CRUD
                case 9 -> showPendingPrintTasks();
            }
        }
    }
    private void loadDataFromFile() {
        printerManager.loadDataFromFile();
    }
    private void addNewPrintTask() {
        printerManager.printBeginNewTask();
        printerManager.printPrintsList();
        int printerNumber = numberInput("- Print number: ");
        printerManager.printFilamentTypes();
        int filamentNumber = numberInput("- Filament number: ");
        printerManager.printColorsListForSpecificFilamentType(filamentNumber);
        int colorNumber = numberInput("- Color number: ");
        printerManager.addNewPrintTask(printerNumber, filamentNumber, colorNumber);
    }
    private void registerPrintCompletion() {
        printerManager.printRunningTasks();
        int printerId = numberInput("- Printer that is done (ID): ");
        printerManager.registerPrintCompletion(printerId);
    }
    private void registerPrinterFailure() {
        printerManager.printRunningTasks();
        int printerId = numberInput("- Printer that is done (ID): ");
        printerManager.registerPrinterFailure(printerId);
    }
    private void changePrintStrategy() {
        printerManager.printPrinters();
        int printerId = numberInput("- Printer ID: ");
        printerManager.printPrinterStrategies();
        int strategyId = numberInput("- Printer Strategy ID: ");
        printerManager.changePrintStrategy(printerId , strategyId);
    }
    private void startPrintQueue() {
        printerManager.startPrintQueue();
    }
    private void showPrints() {
        printerManager.showPrints();
    }
    private void showPrinters() {
        printerManager.showSpools();
    }
    private void showSpools() {
        printerManager.showSpools();
    }
    private void showPendingPrintTasks() {
        printerManager.showPendingPrintTasks();
    }
    private int menuChoice(int max, String message) {
        System.out.print(message);
        int choice = -1;
        while (choice < 0 || choice > max) {
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
        System.out.println("----------------------------------->>");
        return choice;
    }

    private int numberInput(String message) {
        System.out.print(message);
        int input = scanner.nextInt();
        System.out.println("-------------------------------------->");
        return input;
    }

    private void printMenu() {
        System.out.println("<<------------- Menu ----------------");
        System.out.println("- 1) Add new Print Task");
        System.out.println("- 2) Register Printer Completion");
        System.out.println("- 3) Register Printer Failure");
        System.out.println("- 4) Change printing style");
        System.out.println("- 5) Start Print Queue");
        System.out.println("- 6) Show prints");
        System.out.println("- 7) Show printers");
        System.out.println("- 8) Show spools");
        System.out.println("- 9) Show pending print tasks");
        System.out.println("- 0) Exit");
    }

}
