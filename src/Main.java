import java.io.*;

public class Main {
    private static final String sourceFilePath = "src/1.txt";
    private static final String destinationFilePath = "src/1_copy.txt";

    static class SequentialCopy implements Runnable {
        @Override
        public void run() {
            try {
                FileReader fileReader = new FileReader(sourceFilePath);
                FileWriter fileWriter = new FileWriter(destinationFilePath);

                int character;
                while ((character = fileReader.read()) != -1) {
                    fileWriter.write(character);
                }

                fileReader.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class ParallelCopy implements Runnable {
        private int start;
        private int end;

        public ParallelCopy(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            try {
                FileReader fileReader = new FileReader(sourceFilePath);
                FileWriter fileWriter = new FileWriter(destinationFilePath);

                fileReader.skip(start);
                for (int i = start; i < end; i++) {
                    int character = fileReader.read();
                    if (character == -1) break;
                    fileWriter.write(character);
                }

                fileReader.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        SequentialCopy sequentialCopy = new SequentialCopy();
        sequentialCopy.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Время, затрачиваемое на последовательное копирование: " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        int fileSize = (int) new File(sourceFilePath).length();
        int mid = fileSize / 2;

        Thread thread1 = new Thread(new ParallelCopy(0, mid));
        Thread thread2 = new Thread(new ParallelCopy(mid, fileSize));
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        endTime = System.currentTimeMillis();
        System.out.println("Время, затраченное на параллельное копирование: " + (endTime - startTime) + " ms");
    }
}