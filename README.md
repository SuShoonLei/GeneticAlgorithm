# 🧬 Genetic Algorithm — Factory Layout Optimizer

A parallel genetic algorithm that optimizes the placement of functional stations across a 2D factory floor to **maximize total affinity** between neighboring stations. Designed to run on multi-core machines (32+ cores) with real-time graphical visualization of evolving solutions.

---

## 🏭 Problem Description

Given a one-floor factory represented as a 2D grid of spots (possibly irregular with holes), the goal is to optimally place **N stations** across **M available spots** to maximize the total **affinity score** — a metric measuring the benefit of placing two stations near each other based on their types and distance.

### Constraints

| Parameter | Requirement |
|-----------|-------------|
| **N** — number of stations | ≥ 48 |
| **M** — number of spots on the floor | ≥ N |
| **F** — number of station functional types | ≥ 4 |
| **K** — number of parallel tasks | ≥ 32 (smaller K allowed for development) |
| **Floor shape** | Any 2D shape; may contain unoccupied "holes" |
| **Station shapes** | Multi-spot, occupying one or more adjacent grid cells |

---

## 🔬 How It Works

### Affinity Function
Each pair of stations *A* and *B* has an affinity score based on:
- Their **functional types**
- Their **distance** on the factory floor
- Type-specific **capacity or rate limits** that cap the maximum benefit

The optimizer seeks the arrangement that **maximizes total affinity** across all station pairs.

### Genetic Algorithm

The algorithm runs **K parallel tasks**, each maintaining and evolving its own candidate solution:

1. **Mutation** — Randomly swapping station positions or modifying spot assignments (including holes)
2. **Crossover** — Periodically exchanging partial solutions between parallel tasks
3. **Selection** — Keeping higher-affinity arrangements across generations
4. **Convergence** — Runs until solutions converge or a set number of iterations is reached

The key engineering challenge is the **concurrent coordination** between parallel tasks during solution exchange — ensuring thread safety while sharing genetic material across K workers.

---

## 🖥️ Visualization

The program renders the current best layout **graphically**, refreshing approximately **twice per second**, showing:
- Station positions on the factory floor
- Color-coded station types
- Convergence progress over time

---

## ⚙️ Setup & Usage

### Requirements
- Machine with **32+ CPU cores** (recommended)
- Java JDK 11+

### Running the Program

```bash
# Clone the repository
git clone https://github.com/SuShoonLei/GeneticAlgorithm.git
cd GeneticAlgorithm

# Compile
javac -d out src/**/*.java

# Run with default parameters
java -cp out Main

# Run with custom parameters (example)
java -cp out Main --stations 48 --spots 64 --types 4 --tasks 32 --iterations 10000
```

> 💡 For development, you can run with a smaller `K` (e.g. `--tasks 4`) on any machine.

### Parameters

| Flag | Description | Default |
|------|-------------|---------|
| `--stations` / `-N` | Number of stations to place | 48 |
| `--spots` / `-M` | Number of available spots on the floor | 64 |
| `--types` / `-F` | Number of station functional types | 4 |
| `--tasks` / `-K` | Number of parallel worker tasks | 32 |
| `--iterations` | Max number of GA iterations | Until convergence |

---

## 📐 Architecture

```
GeneticAlgorithm/
├── src/
│   ├── model/
│   │   ├── Station.java            # Station with type and shape
│   │   ├── Floor.java              # 2D grid with spots and holes
│   │   └── Solution.java           # Candidate placement solution
│   ├── ga/
│   │   ├── GeneticAlgorithm.java   # Core GA logic
│   │   ├── Mutation.java           # Swap / modify operations
│   │   ├── Crossover.java          # Inter-task solution exchange
│   │   └── AffinityFunction.java   # Scoring metric
│   ├── parallel/
│   │   └── TaskCoordinator.java    # Concurrent task management
│   └── viz/
│       └── FloorRenderer.java      # Real-time graphical display
└── README.md
```

---

## 📊 Performance

Designed to scale with available hardware. With K=32 parallel tasks on a 32-core machine, population diversity and crossover frequency allow the algorithm to escape local optima and converge toward near-optimal factory layouts.

---

## 📖 References

- Holland, J. H. (1975). *Adaptation in Natural and Artificial Systems*
- Goldberg, D. E. (1989). *Genetic Algorithms in Search, Optimization, and Machine Learning*

---

*Parallel Genetic Algorithm — Factory Station Layout Optimization*
