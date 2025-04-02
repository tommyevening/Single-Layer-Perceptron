import java.util.Scanner;

public class Main {
    private static final String TRAIN_DIRECTORY = "C:\\Users\\wiecz\\OneDrive\\Pulpit\\NAI\\mini-projekty\\Single-Layer-Perceptron\\src\\data\\train";
    private static final String TEST_DIRECTORY = "C:\\Users\\wiecz\\OneDrive\\Pulpit\\NAI\\mini-projekty\\Single-Layer-Perceptron\\src\\data\\test";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println("Inicjalizacja klasyfikatora językowego...");

            // Pobieranie współczynnika uczenia przed inicjalizacją klasyfikatora
            System.out.print("Podaj współczynnik uczenia (ALPHA) (0.0001-0.1): ");
            double alpha = Double.parseDouble(scanner.nextLine());

            // Pobieranie progu aktywacji
            System.out.print("Podaj próg aktywacji (THETA) (-1.0-1.0): ");
            double theta = Double.parseDouble(scanner.nextLine());

            LanguageClassifier classifier = new LanguageClassifier(TRAIN_DIRECTORY, alpha, theta);

            while (true) {
                System.out.println("\nWybierz opcję:");
                System.out.println("1. Trenuj model");
                System.out.println("2. Testuj model");
                System.out.println("3. Klasyfikuj tekst");
                System.out.println("4. Pokaż parametry modelu");
                System.out.println("5. Wyjście");

                System.out.print("Wybór: ");
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
                        showModelParameters(classifier, alpha, theta);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Nieprawidłowy wybór");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Błąd: Wprowadzono nieprawidłową wartość liczbową");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void trainModel(LanguageClassifier classifier) {
        try {
            System.out.print("Podaj liczbę epok (10-1000): ");
            int epochs = Integer.parseInt(scanner.nextLine());

            if (epochs < 10 || epochs > 1000) {
                System.out.println("Liczba epok musi być z zakresu 10-1000");
                return;
            }

            System.out.println("Rozpoczynam trenowanie...");
            classifier.train(TRAIN_DIRECTORY, epochs);
            System.out.println("Zakończono trenowanie");
        } catch (NumberFormatException e) {
            System.out.println("Błąd: Wprowadzono nieprawidłową liczbę epok");
        }
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

    private static void showModelParameters(LanguageClassifier classifier, double alpha, double theta) {
        System.out.println("\nParametry modelu:");
        System.out.println("Współczynnik uczenia (ALPHA): " + alpha);
        System.out.println("Próg aktywacji (THETA): " + theta);
        System.out.println("Liczba wykrytych języków: " + classifier.getLanguages().size());
        System.out.println("Dostępne języki: " + String.join(", ", classifier.getLanguages()));
    }

    private static boolean isValidAlpha(double alpha) {
        return alpha > 0.0001 && alpha <= 0.1;
    }

    private static boolean isValidTheta(double theta) {
        return theta >= -1.0 && theta <= 1.0;
    }
}