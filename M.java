import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class M {
    private N[][] grid;
    private Random rand = new Random();


    public M(int rows, int cols) {
        grid = new N[rows][cols];
    }

    public void setStation(int row, int col, N station) {
        grid[row][col] = station;
    }

    public N getStation(int row, int col) {
        return grid[row][col];
    }

    public int getRows() {
        return grid.length;
    }

    public int getCols() {
        return grid[0].length;
    }


    public int calculateFitness() {
        int fitness = 0;

        int[][] directions = {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1}   // right
        };

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                N station = grid[i][j];
                if (station == null) continue;

                for (int[] dir : directions) {
                    int ni = i + dir[0];
                    int nj = j + dir[1];
                    if (ni >= 0 && ni < getRows() && nj >= 0 && nj < getCols()) {
                        N neighbor = grid[ni][nj];
                        if (neighbor != null) {
                            fitness += Affinity.getAffinity(
                                station.getType(), neighbor.getType()
                            );
                        }
                    }
                }
            }
        }

        // Divide by 2 to avoid double counting pairs
        fitness /= 2;

        // --- New part: enforce minimum 5 of each type ---
        Map<F, Integer> counts = new HashMap<>();
        for (F type : F.values()) {
            counts.put(type, 0);
        }

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                N station = grid[i][j];
                if (station != null) {
                    counts.put(station.getType(), counts.get(station.getType()) + 1);
                }
            }
        }

        // check constraint
        for (F type : F.values()) {
            if (counts.get(type) < 10) {
                fitness -= 250;
                break; // only subtract once
            }
        }

        return fitness;
    }



    public M crossover(M other) {
        int rows = getRows();
        int cols = getCols();

        if (rows != other.getRows() || cols != other.getCols()) {
            throw new IllegalArgumentException("Both parents must have the same size");
        }

        M child = new M(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Randomly choose station from this parent or other parent
                if (rand.nextBoolean()) {
                    child.setStation(i, j, this.getStation(i, j));
                } else {
                    child.setStation(i, j, other.getStation(i, j));
                }
            }
        }

        return child;
    }


    public void mutate() {
        int rows = getRows();
        int cols = getCols();

        for (int k = 0; k < 10; k++) {
            // Pick first random cell
            int r1 = rand.nextInt(rows);
            int c1 = rand.nextInt(cols);

            // Pick second random cell (different from first)
            int r2 = rand.nextInt(rows);
            int c2 = rand.nextInt(cols);
            while (r1 == r2 && c1 == c2) {
                r2 = rand.nextInt(rows);
                c2 = rand.nextInt(cols);
            }

            // Swap the two stations
            N temp = grid[r1][c1];
            grid[r1][c1] = grid[r2][c2];
            grid[r2][c2] = temp;
        }
    }

    public void printFactory() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print((grid[i][j] != null ? grid[i][j] : " ") + " ");
            }
            System.out.println();
        }
    }
}