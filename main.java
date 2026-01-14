import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.Timer;

public class main {
    private static volatile M bestSolution; // shared between GA and GUI

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int rows = 10;
        int cols = 10;
        int populationSize = 96;
        int eliteSize = 64;
        int threadsCount = 32;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of GA iterations: ");
        int iterations = scanner.nextInt();
        scanner.close();

        // Start GUI in its own thread
        SwingUtilities.invokeLater(() -> {
            FactoryGUI gui = new FactoryGUI(rows, cols);
            // Refresh GUI every second with the latest bestSolution
            new Timer(1000, e -> {
                if (bestSolution != null) {
                    gui.updateGrid(bestSolution);
                }
            }).start();
        });

        // Generate initial population
        List<M> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(generateRandomM(rows, cols));
        }

        System.out.println("Initial population generated.");

        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);

        for (int iter = 1; iter <= iterations; iter++) {
            // 1. Sort population by fitness
            sortByFitness(population);

            // update bestSolution without slowing GA
            bestSolution = population.get(0);

            // 2. Remove lowest 32
            population = new ArrayList<>(population.subList(0, eliteSize));

            // 3. Pairwise crossover + mutation
            List<Callable<M>> tasks = new ArrayList<>();
            for (int i = 0; i < eliteSize; i += 2) {
                M parent1 = population.get(i);
                M parent2 = population.get(i + 1);

                tasks.add(() -> {
                    M child = parent1.crossover(parent2);
                    child.mutate();
                    return child;
                });
            }

            // Parallel execution
            List<Future<M>> futures = executor.invokeAll(tasks);
            for (Future<M> future : futures) {
                population.add(future.get());
            }

            if (iter % 50 == 0) { // optional checkpoint log
                System.out.println("Iteration " + iter + ", top fitness: " + bestSolution.calculateFitness());
            }
        }

        executor.shutdown();

        // Final best
        sortByFitness(population);
        bestSolution = population.get(0);
        System.out.println("\nGA completed. Best M fitness: " + bestSolution.calculateFitness());
    }

    private static M generateRandomM(int rows, int cols) {
        Random rand = new Random();
        M m = new M(rows, cols);
        F[] types = F.values();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m.setStation(i, j, new N(types[rand.nextInt(types.length)]));
            }
        }
        return m;
    }

    private static void sortByFitness(List<M> population) {
        population.sort((m1, m2) -> Integer.compare(m2.calculateFitness(), m1.calculateFitness()));
    }
}