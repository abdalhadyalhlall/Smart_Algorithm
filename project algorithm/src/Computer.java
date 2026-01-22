 public class Computer {
    private int depth = 3;
    private int count = 0;


//      تابع لاختيار افضل حركة بناء على قيمة الرمية
    public int bestmove(Board b, int val) {
        this.count = 0;
        int row = -1;
        double maxscore = -1000000;

        for (int i = 7; i < 14; i++) {
            Stone current = b.stone1[i];

            if (current.in && b.CanMove(current, current.row + val)) {
                Board copy = b.cloneBoard();
                copy.Move(copy.stone1[i], val);
                double currentscore = expectiminimax(copy, depth - 1, false);
                System.out.println("rating [" + current.row + "]: " + currentscore);
                if (currentscore > maxscore) {
                    maxscore = currentscore;
                     row = current.row;
                }
            }
        }
        printsummary(maxscore, row);
        return row;
    }

//     الخوارزمية الاساسية في اللعبة التي تفحص حالة اللعبة والعمق
    private double expectiminimax(Board b, int d, boolean isMax) {
        count++;
        char win = b.IsWin();
        if (win != 'c' || d == 0) {
            double res = evaluate(b);
            return res;
        }
        if (isMax) {
            return chancenode(b, d, true);
        }
        else {
            return chancenode(b, d, false);
        }
    }

//     حساب القيمة المتوقعة للوحة بناءً على احتمالات نتائج رمي العصي
    private double chancenode(Board b, int d, boolean isMax) {
        double total = 0;
        double[] probs = {0.0625, 0.25, 0.375, 0.25, 0.0625};
        int[] moves = {5, 1, 2, 3, 4};
        for (int i = 0; i < moves.length; i++) {
            double score = moveNode(b, d, isMax, moves[i]);
            total += probs[i] * score;
        }
        return total;
    }

//     لعب دور الكمبيوتر لاختيار الحركة التي تحقق أعلى تقييم
     private double moveMax(Board b, int d, int val) {
         count++;
         double bestscore = -1000000;
         boolean canmove = false;

         for (int i = 7; i < 14; i++) {
             Stone s = b.stone1[i];
             if (s.in && b.CanMove(s, s.row + val)) {
                 canmove = true;
                 Board next = b.cloneBoard();
                 next.Move(next.stone1[i], val);
                 double score = expectiminimax(next, d - 1, false);
                 if (score > bestscore) bestscore = score;
             }
         }
         if (!canmove) {
            return evaluate(b);
        }
         return bestscore;
     }

//      لعب دور الخصم لتوقع الحركة التي تضعف score الكمبيوتر
     private double moveMin(Board b, int d, int val) {
         count++;
         double worstscore = 1000000;
         boolean canmove = false;

         for (int i = 0; i < 7; i++) {
             Stone s = b.stone1[i];
             if (s.in && b.CanMove(s, s.row + val)) {
                 canmove = true;
                 Board next = b.cloneBoard();
                 next.Move(next.stone1[i], val);
                 double score = expectiminimax(next, d - 1, true);
                 if (score < worstscore) worstscore = score;
             }
         }
         if (!canmove) {
             return evaluate(b);
         }
         return worstscore;
     }


//      توجيه البحث للكمبيوتر او للخصم
     private double moveNode(Board b, int d, boolean isMax, int val) {
         if (isMax) {
             return moveMax(b, d, val);
         } else {
             return moveMin(b, d, val);
         }
     }

//   طباعة احصائيات عملية البحث
    private void printsummary(double bestscore, int bestmove) {
        System.out.println("summary:");
        System.out.println("total nodes explored: " + count);
        System.out.println("best evaluated: " + bestscore);
        System.out.println("chosen position: " + bestmove);
    }

//    لحساب القوة الحالية للوحية
    private double evaluate(Board b) {
        double total = 0;
        for (int i = 0; i < 14; i++) {
            Stone s = b.stone1[i];
            if (s.color == 'b') {
                if (!s.in) {
                    total += 50;
                } else {
                    total += (s.row * 1.5);
                }
            }
            else {
                if (!s.in) {
                    total -= 50;
                } else {
                    total -= (s.row * 1.5);
                }
            }
        }
        return total;
    }


}