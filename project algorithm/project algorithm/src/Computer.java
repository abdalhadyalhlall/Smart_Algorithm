 public class Computer {
    private int maxDepth = 2;
    private int nodesCount = 0;

    public int getBestMove(Board b, int val) {
        this.nodesCount = 0;
        int bestRow = -1;
        double maxScore = -1000000;

        for (int i = 7; i < 14; i++) {
            Stone current = b.stone1[i];

            if (current.in && b.CanMove(current, current.row + val)) {
//                System.out.println("\n[ROOT] Testing Stone at Row: " + current.row);
                Board copy = b.cloneBoard();
                copy.Move(copy.stone1[i], val);
                double currentScore = expectiminimax(copy, maxDepth - 1, false, "  ");
//                System.out.println("Final Rating for Move [" + current.row + "]: " + currentScore);
                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    bestRow = current.row;
                }
            }
        }
//        printSummary(maxScore, bestRow);
        return bestRow;
    }

    private double expectiminimax(Board b, int d, boolean isMax, String tab) {
        nodesCount++;
        char win = b.IsWin();
        if (win != 'c' || d == 0) {
            double res = evaluateBoard(b);
            return res;
        }
        if (isMax) {
            return chanceNode(b, d, true, tab + "│ ");
        }
        else {
            return chanceNode(b, d, false, tab + "│ ");
        }
    }

    private double chanceNode(Board b, int d, boolean isMax, String space) {
        double total = 0;
        double[] probs = {0.0625, 0.25, 0.375, 0.25, 0.0625};
        int[] moves = {5, 1, 2, 3, 4};
        for (int i = 0; i < moves.length; i++) {
            double score = moveNode(b, d, isMax, moves[i], space + "│ ");
            total += probs[i] * score;
        }
        return total;
    }

    private double moveNode(Board b, int d, boolean isMax, int val, String sp) {
        nodesCount++;
        double result = isMax ? -1000000 : 1000000;
        boolean hasMove = false;
        int low = isMax ? 7 : 0;
        int high = isMax ? 14 : 7;

        for (int i = low; i < high; i++) {
            Stone s = b.stone1[i];
            if (s.in && b.CanMove(s, s.row + val)) {
                hasMove = true;
                Board next = b.cloneBoard();
                next.Move(next.stone1[i], val);
                double score = expectiminimax(next, d - 1, !isMax, sp + "  ");
                if (isMax) {
                    if (score > result) result = score;
                } else {
                    if (score < result) result = score;
                }
            }
        }

        if (!hasMove) {
            return evaluateBoard(b);
        }

        return result;
    }

    private void printSummary(double bestScore, int bestMoveRow) {
        System.out.println("SUMMARY:");
        System.out.println("Total Nodes Explored: " + nodesCount);
        System.out.println("Best Evaluated Score: " + bestScore);
        System.out.println("Chosen Position: " + bestMoveRow);
    }

    private double evaluateBoard(Board b) {
        double totalScore = 0;
        for (int i = 0; i < 14; i++) {
            Stone s = b.stone1[i];
            if (s.color == 'b') {
                if (!s.in) {
                    totalScore += 50.0;
                } else {
                    totalScore += (s.row * 1.5);
                }
            }
            else {
                if (!s.in) {
                    totalScore -= 50.0;
                } else {
                    totalScore -= (s.row * 1.5);
                }
            }
        }
        return totalScore;
    }


}