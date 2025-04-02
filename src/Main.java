import java.util.Scanner;

public class Main {
    private static final String TRAIN_DIRECTORY = "C:\\Users\\wiecz\\OneDrive\\Pulpit\\NAI\\mini-projekty\\Single-Layer-Perceptron\\src\\data\\train";
    private static final String TEST_DIRECTORY = "C:\\Users\\wiecz\\OneDrive\\Pulpit\\NAI\\mini-projekty\\Single-Layer-Perceptron\\src\\data\\test";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println("Inicjalizacja klasyfikatora językowego...");

            // Pobieranie i walidacja współczynnika uczenia
            double alpha;
            while (true) {
                System.out.print("Podaj współczynnik uczenia (ALPHA) (0.0001-0.1): ");
                try {
                    alpha = Double.parseDouble(scanner.nextLine());
                    if (isValidAlpha(alpha)) break;
                    System.out.println("Nieprawidłowa wartość ALPHA. Spróbuj ponownie.");
                } catch (NumberFormatException e) {
                    System.out.println("Nieprawidłowy format liczby. Spróbuj ponownie.");
                }
            }

            // Pobieranie i walidacja progu aktywacji
            double theta;
            while (true) {
                System.out.print("Podaj próg aktywacji (THETA) (-1.0-1.0): ");
                try {
                    theta = Double.parseDouble(scanner.nextLine());
                    if (isValidTheta(theta)) break;
                    System.out.println("Nieprawidłowa wartość THETA. Spróbuj ponownie.");
                } catch (NumberFormatException e) {
                    System.out.println("Nieprawidłowy format liczby. Spróbuj ponownie.");
                }
            }

            System.out.println("\nTworzenie klasyfikatora z parametrami:");
            System.out.println("ALPHA: " + alpha);
            System.out.println("THETA: " + theta);

            LanguageClassifier classifier = new LanguageClassifier(TRAIN_DIRECTORY, alpha, theta);

            while (true) {
                try {
                    System.out.println("\nWybierz opcję:");
                    System.out.println("1. Trenuj model");
                    System.out.println("2. Testuj model");
                    System.out.println("3. Klasyfikuj tekst");
                    System.out.println("4. Pokaż parametry modelu");
                    System.out.println("5. Wyjście");

                    System.out.print("\nWybór: ");
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
                            System.out.println("Zamykanie programu...");
                            return;
                        default:
                            System.out.println("Nieprawidłowy wybór. Wybierz opcję 1-5.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Błąd: Wprowadź poprawną liczbę.");
                } catch (Exception e) {
                    System.out.println("Wystąpił błąd: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Krytyczny błąd programu: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void trainModel(LanguageClassifier classifier) {
        while (true) {
            try {
                System.out.print("Podaj liczbę epok (10-1000): ");
                int epochs = Integer.parseInt(scanner.nextLine());

                if (epochs < 10 || epochs > 1000) {
                    System.out.println("Liczba epok musi być z zakresu 10-1000. Spróbuj ponownie.");
                    continue;
                }

                System.out.println("\nRozpoczynam trenowanie modelu...");
                classifier.train(TRAIN_DIRECTORY, epochs);
                System.out.println("Zakończono trenowanie modelu.");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Błąd: Wprowadź poprawną liczbę epok.");
            } catch (Exception e) {
                System.out.println("Błąd podczas trenowania: " + e.getMessage());
                break;
            }
        }
    }

    private static void testModel(LanguageClassifier classifier) {
        try {
            System.out.println("\nTestowanie modelu...");
            double accuracy = classifier.test(TEST_DIRECTORY);
            System.out.printf("Dokładność modelu: %.2f%%\n", accuracy * 100);
        } catch (Exception e) {
            System.out.println("Błąd podczas testowania: " + e.getMessage());
        }
    }

    private static void classifyNewText(LanguageClassifier classifier) {
        System.out.println("\nWprowadź tekst do klasyfikacji (lub 'exit' aby wrócić):");
        while (true) {
            try {
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Powrót do menu głównego.");
                    break;
                }

                if (input.isEmpty()) {
                    System.out.println("Tekst nie może być pusty. Spróbuj ponownie:");
                    continue;
                }

                String predictedLanguage = classifier.classifyText(input);
                System.out.println("Wykryty język: " + predictedLanguage);
                System.out.println("\nWprowadź kolejny tekst (lub 'exit' aby wrócić):");
            } catch (Exception e) {
                System.out.println("Błąd podczas klasyfikacji: " + e.getMessage());
                System.out.println("Spróbuj ponownie lub wpisz 'exit' aby wrócić.");
            }
        }
    }

    private static void showModelParameters(LanguageClassifier classifier, double alpha, double theta) {
        System.out.println("\nParametry modelu:");
        System.out.println("----------------------------------------");
        System.out.println("Współczynnik uczenia (ALPHA): " + alpha);
        System.out.println("Próg aktywacji (THETA): " + theta);
        System.out.println("Liczba wykrytych języków: " + classifier.getLanguages().size());
        System.out.println("Dostępne języki: " + String.join(", ", classifier.getLanguages()));
        System.out.println("----------------------------------------");
    }

    private static boolean isValidAlpha(double alpha) {
        return alpha >= 0.0001 && alpha <= 0.1;
    }

    private static boolean isValidTheta(double theta) {
        return theta >= -1.0 && theta <= 1.0;
    }
}