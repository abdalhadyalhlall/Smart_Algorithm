import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Board {
    Stone[] stone1 = new Stone[14];
    Map<Integer,String> special = new HashMap<>();
    String[] state = new String[30];
    int w = 0;
    int b =1;
    int[] stick = new int[4];
    int countwhite=0;
    int countblack=0;
    //تهيئة الرقعة للعب
    public Board(){
        for (int i = 0; i < 14; i++) {
            if(i<7){
                stone1[i] = new Stone('w', w);
                state[w]="⚪";
                w +=2;
            }
            if(6<i){
                stone1[i] = new Stone('b', b);
                state[b]="⚫";
                b +=2;
            }
        }
     intilize();
        for (int i = 15; i < 30; i++) {
            if (state[i] == null){
                state[i] = "✅";
            }
        }
    }

    //تحديد العلامات المخصصة و وضعها في اماكنها
    public void intilize(){
        special.put(14,"🎪");
        state[14] = special.get(14);
        special.put(25,"⛔");
        state[25] = special.get(25);
        special.put(26,"♒");
        state[26] = special.get(26);
        special.put(27,"🦆");
        state[27] = special.get(27);
        special.put(28,"🧑‍🤝‍🧑");
        state[28] = special.get(28);
        special.put(29,"🔳");
        state[29] = special.get(29);

    }

    //مسؤولة عن نسخ الرقعة الحالية بكامل تفاصيلها المهمه
    public Board cloneBoard() {
        Board copy = new Board();
        for (int i = 0; i < 14; i++) {
            Stone s = this.stone1[i];
            copy.stone1[i] = new Stone(s.color, s.row);
            copy.stone1[i].in = s.in;
        }
        for (int i = 0; i < 30; i++) {
            copy.state[i] = this.state[i];
        }
        copy.countwhite = countwhite;
        copy.countblack = countblack;
        copy.special.putAll(this.special);
        return copy;
    }

    //مسؤالة عن معالجة رمي الاعواد الاربعة و القيم الناتجة عنها
    public int  ValueStick(){
        Random random =  new Random();
        int WhiteCount = 0 ;
        for(int i=0 ;i<4; i++){
            stick[i]=random.nextInt(2);

            if (stick[i] == 0){
                WhiteCount++;
            }
        }
        if (WhiteCount == 4){
            return 5;
        }
        return 4-WhiteCount;
    }

    //طباعة رقعة اللعب في كل خطوة
    public void print(){
        for (int i = 0; i < 10; i++) {
            System.out.print(state[i]);
        }
        System.out.print("\n");

        for (int i = 19; i >9 ; i--) {
            System.out.print(state[i]);

        }
        System.out.print("\n");
        for (int i = 20; i <30 ; i++) {
            System.out.print(state[i]);

        }

        System.out.println("\n the black stone out :"+countblack);
        System.out.print("the white stone out :"+countwhite);
    }

    //هذه الدالة تقوم  بإدارة الحركات التي تخالف قواعد اللعبة
    public void home(Stone stone){
        int i=14;
        while(state[i].equals("⚫") ||  state[i].equals("⚪") ){
            i-=1;
        }
           if (i>0){
        state[stone.row] = special.containsKey(stone.row) ? special.get(stone.row) : "✅" ;
        state[i] = getShape(stone);
        stone.row =i;}
}

    //هذه الدالة تقوم بتحويل الرمز المعبر عن اللون الى شكله الحقيقي
    public String getShape(Stone stone){
        if (stone.color == 'w'){
            return "⚪";
        }
        return "⚫";
}

    //هذه الدالة تقوم بالتحقق من صحة حركة الحجر قبل تنفيذ الحركة
    public boolean CanMove(Stone stone,int to){
        if (stone == null ||!stone.in){
            return false;
        }


        if(to<0 || to>29){
            return stone.row >= 25;
        }

        if (state[to].equals(state[stone.row])){
            return false;
        }
      if(to>25 && stone.row != 25){
            return false ;}


        return true;
}

    //هذه الدالة المسؤوالة عن إدارة القطع و التحريك الفعلي و كيفية التبادل بينها
    public boolean NextStep(Stone stone,int value){
        if(stone.row + value >= 30){
            if ( stone.row == 27 && value > 3){
                home(stone);
                return true;
            }
            if ( stone.row == 28 && value > 2){
                home(stone);
                return true;
            }
            stone.in = false;
            state[stone.row] = special.get(stone.row);
            if(stone.color == 'w'){
                countwhite ++;}
                else{
                    countblack++;
                }
                return true;
            }
        else{
           if ( stone.row == 27 && value < 3){
        home(stone);
        return true;
        }
       if ( stone.row == 28 && value < 2){
        home(stone);
        return true;
       }
            if(stone.row+value == 26){
                home(stone);
                return true;
            }

    if (stone.row == 25 ){
        state[stone.row+value] = state[stone.row];
        state[stone.row] = special.get(stone.row);
        stone.row +=value;
        return true;
    }


    for (String shape :special.values()){
        if (state[stone.row+value].equals(shape) ){
            state[stone.row] = "✅";
            state[stone.row+value] = getShape(stone);
            stone.row = stone.row + value ;
            return true;
        }
    }

    for (int i = 0; i < 14; i++) {
        if(stone1[i].row == stone.row+value && stone1[i].in){
            if (stone1[i].color != stone.color){
            String tempshape = state[stone.row+value];
            state[stone.row +value] = state[stone.row];
            state[stone.row] = tempshape;
            stone1[i].row = stone.row;
            stone.row = stone.row+value;
            return true ;}

            return true;
        }

    }

    if(state[stone.row+value].equals("✅") ){
        if (stone.row == 14){
            state[stone.row+value] = state[stone.row];
            state[14] = special.get(14);
            stone.row += value;
            return true;
        }
        else {
            state[stone.row+value] = state[stone.row];
            state[stone.row] = "✅";
            stone.row += value;
           return  true;
        }
    }}
    return false;
}


// يقوم بتحريك حجر معين وفق الشرطيين وهما أمكانية الحركة والحركة التالية ونعياد الأحجار التي من نفس اللون التي تقع في المواقع الثلاث الاخيرة
    public boolean Move(Stone stone,int value){
        if(!CanMove(stone,stone.row+value )){
            return false ;
        }
        if (!stone.in){
            return false;}


       boolean valid =NextStep(stone,value);
       if (valid){
    for (int i = 0; i < 14; i++) {
        if(stone1[i].color == stone.color &&stone1[i].row!=stone.row&&stone1[i].in ){
            if (stone1[i].row == 27 ||stone1[i].row == 28 || stone1[i].row==29 ){
                home(stone1[i]);
            }
        }
    }
       }


return valid;
    }

//تابع الفوز ويتحقق أذا كان عدد الاحجار الخارجة من الرقعة للون واحد تساوي سبعة وإلا تستمر باللعب
    public char IsWin(){


    if (countblack == 7){
        return 'b';
    }
    if(countwhite == 7){
        return 'w';
    }
   return 'c';
}
//تابع اللعب بين لاعبين اثنين بالتناوب
    public void PlayGame() {
        Scanner scan = new Scanner(System.in);
        int player = 0;
        this.print();

        while (true) {
            if (IsWin() != 'c') {
                System.out.println("player : " + IsWin() + " winner");
                break;
            }
            if (player == 0) {
                int value1 = ValueStick();
                System.out.println("\nstick value:" + value1);
                System.out.println("turn white");
                System.out.print("enter site stone : ");
                int row = scan.nextInt();

                Stone white = null;
                for (int i = 0; i < 7; i++) {
                    if (stone1[i].row == row && stone1[i].in) {
                        white = stone1[i];
                        break;
                    }
                }
                if (white == null || !state[row].equals("⚪") || !Move(white, value1)) {
                    boolean validMove = false;
                    while (!validMove) {
                        System.out.print(" Enter valid site: ");
                        row = scan.nextInt();
                        white = null;
                        for (int i = 0; i < 7; i++) {
                            if (stone1[i].row == row && stone1[i].in) {
                                white = stone1[i];
                                break;
                            }
                        }
                        if (white != null && state[row].equals("⚪") && Move(white, value1)) {
                            validMove = true;
                        }
                    }
                }

                this.print();
                player = 1;
            }
            else {
                int value2 = ValueStick();
                System.out.println("\nstick value:" + value2);
                System.out.println(" turn black");
                System.out.print("enter site stone: ");
                int row = scan.nextInt();

                Stone black = null;
                for (int i = 7; i < 14; i++) {
                    if (stone1[i].row == row && stone1[i].in) {
                        black = stone1[i];
                        break;
                    }
                }

                if (black == null || !state[row].equals("⚫") || !Move(black, value2)) {
                    boolean validMove = false;
                    while (!validMove) {
                        System.out.print(" Enter valid site: ");
                        row = scan.nextInt();
                        black = null;
                        for (int i = 7; i < 14; i++) {
                            if (stone1[i].row == row) {
                                black = stone1[i];
                                break;
                            }
                        }

                        if (black != null  && state[row].equals("⚫") && Move(black, value2)) {
                            validMove = true;
                        }
                    }
                }

                this.print();
                player = 0;
            }
        }
        scan.close();
    }
//تابع اللعب مع الخوارزمية بالتناوب
    public void PlayGameWithAlgorithm() {
        Scanner scan = new Scanner(System.in);
        Computer c = new Computer();
        int player = 0;
        this.print();
        while (true) {
            if (IsWin() != 'c') {
                System.out.println("\n the winner is: " + IsWin()  );
                break;
            }
            int value = ValueStick();
            System.out.println("\n stick value: " + value);
            if (player == 0) {
                System.out.println(" turn white");
                boolean validMoveMade = false;

                while (!validMoveMade) {
                    System.out.print("enter site stone: ");
                    int row = scan.nextInt();

                    Stone white = null;
                    for (int i = 0; i < 7; i++) {
                        if (stone1[i].row == row && stone1[i].in) {
                            white = stone1[i];
                            break;
                        }
                    }
                    if (white != null && state[row].equals("⚪")&& white.in && Move(white, value)) {
                        validMoveMade = true;
                    }
                }
                player = 1;
            }
            else {
                System.out.println("AI turn Black");
                int bestMoveRow = c.bestmove(this, value);

                if (bestMoveRow != -1) {
                    Stone black = null;
                    for (int i = 7; i < 14; i++) {
                        if (stone1[i].row == bestMoveRow && stone1[i].in) {
                            black = stone1[i];
                            break;
                        }
                    }
                    Move(black, value);
                    System.out.println("AI moved stone from: " + bestMoveRow);
                }
                player = 0;
            }
            this.print();
        }
    }
}


