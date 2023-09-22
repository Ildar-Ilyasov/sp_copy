import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main{

    public static void main(String[] args) {
        String sourceFile1 = "src/first.txt";
        String destinationFile1 = "src/first_copy.txt";
        long startTime = System.currentTimeMillis();
        sequentialCopy(sourceFile1, destinationFile1);
        long endTime = System.currentTimeMillis();
        long sequentialTime = endTime - startTime;
        startTime = System.currentTimeMillis();
        parallelCopy(sourceFile1, destinationFile1);
        endTime = System.currentTimeMillis();
        long parallelTime = endTime - startTime;
        System.out.println("Последовательное копирование: " + sequentialTime + " ms");
        System.out.println("Параллельное копирование: " + parallelTime + " ms");
    }
    public static void sequentialCopy(String sourcePath, String destinationPath) {
        try (FileReader reader = new FileReader(sourcePath);
             FileWriter writer = new FileWriter(destinationPath)) {
            int character;
            while ((character = reader.read()) != -1) {
                writer.write(character);
            }
            System.out.println("Успешно завершено!");
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
    public static void parallelCopy(String sourcePath, String destinationPath) {
        Thread copyThread = new Thread(() -> {
            try (FileReader reader = new FileReader(sourcePath);
                 FileWriter writer = new FileWriter(destinationPath)) {
                int character;
                while ((character = reader.read()) != -1) {
                    writer.write(character);
                }
                System.out.println("Успешно завершено");
            } catch (IOException e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        });
        copyThread.start();
        try {
            copyThread.join();
        } catch (InterruptedException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}