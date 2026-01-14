import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class FactoryGUI extends JFrame {
    private int rows, cols;
    private JLabel[][] cells;
    private JPanel gridPanel;
    private JPanel legendPanel;
    private JLabel fitnessLabel; // 👈 fitness display

    private static final Map<F, Color> colorMap = new HashMap<>();
    static {
        colorMap.put(F.A, Color.RED);
        colorMap.put(F.B, Color.BLUE);
        colorMap.put(F.C, Color.GREEN);
        colorMap.put(F.D, Color.ORANGE);
        colorMap.put(F.H, Color.LIGHT_GRAY);
    }

    public FactoryGUI(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new JLabel[rows][cols];

        setTitle("Factory Layout - Best Fitness Grid");
        setLayout(new BorderLayout());

        // ===== Top fitness label =====
        fitnessLabel = new JLabel("Fitness: N/A", SwingConstants.CENTER);
        fitnessLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(fitnessLabel, BorderLayout.NORTH);

        // ===== Grid Panel =====
        gridPanel = new JPanel(new GridLayout(rows, cols));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JLabel cell = new JLabel();
                cell.setOpaque(true);
                cell.setBackground(Color.WHITE);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[i][j] = cell;
                gridPanel.add(cell);
            }
        }

        // ===== Legend with affinities =====
        legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));

        for (F type : F.values()) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JPanel block = new JPanel();
            block.setBackground(colorMap.get(type));
            block.setPreferredSize(new Dimension(20, 20));
            rowPanel.add(block);

            // Gather affinities for this type
            StringBuilder sb = new StringBuilder(type.name() + " → ");
            for (F other : F.values()) {
                String key1 = type.name() + "_" + other.name();
                String key2 = other.name() + "_" + type.name();
                int value = 0;
                if (Affinity.hasAffinity(key1)) {
                    value = Affinity.getAffinity(type, other);
                } else if (Affinity.hasAffinity(key2)) {
                    value = Affinity.getAffinity(other, type);
                }
                sb.append(other.name()).append(":").append(value).append("  ");
            }

            rowPanel.add(new JLabel(sb.toString()));
            legendPanel.add(rowPanel);
        }

        add(gridPanel, BorderLayout.CENTER);
        add(new JScrollPane(legendPanel), BorderLayout.SOUTH);

        setSize(700, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 👇 update grid + fitness score
    public void updateGrid(M best) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                N station = best.getStation(i, j);
                if (station != null) {
                    cells[i][j].setBackground(colorMap.get(station.getType()));
                }
            }
        }
        fitnessLabel.setText("Fitness: " + best.calculateFitness()); // update score
    }
}
