package test;

public class Main {
    private static final String TRAIN_DIRECTORY = "data/train/";
    private static final String TEST_DIRECTORY = "data/test/";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println("Inicjalizacja klasyfikatora językowego...");
            LanguageClassifier classifier = new LanguageClassifier(TRAIN_DIRECTORY);

            while (true) {
                System.out.println("\nWybierz opcję:");
                System.out.println("1. Trenuj model");
                System.out.println("2. Testuj model");
                System.out.println("3. Klasyfikuj tekst");
                System.out.println("4. Wyjście");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        trainModel(classifier);
                        break;
                    case 2:
                        testModel(classifier);
                        break;
                    case 3:
                        classifyNewText(classifier);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Nieprawidłowy wybór");
                }
            }
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void trainModel(LanguageClassifier classifier) {
        System.out.print("Podaj liczbę epok (10-1000): ");
        int epochs = Integer.parseInt(scanner.nextLine());

        System.out.println("Rozpoczynam trenowanie...");
        classifier.train(TRAIN_DIRECTORY, epochs);
        System.out.println("Zakończono trenowanie");
    }

    private static void testModel(LanguageClassifier classifier) {
        System.out.println("Testowanie modelu...");
        double accuracy = classifier.test(TEST_DIRECTORY);
        System.out.printf("Dokładność: %.2f%%\n", accuracy * 100);
    }

    private static void classifyNewText(LanguageClassifier classifier) {
        System.out.println("Wprowadź tekst do klasyfikacji (lub 'exit' aby wrócić):");
        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            if (input.trim().isEmpty()) {
                System.out.println("Tekst nie może być pusty. Spróbuj ponownie:");
                continue;
            }

            String predictedLanguage = classifier.classifyText(input);
            System.out.println("Wykryty język: " + predictedLanguage);
            System.out.println("\nWprowadź kolejny tekst (lub 'exit' aby wrócić):");
        }
    }
}