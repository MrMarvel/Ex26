import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static final String ANSI_MAGENTA	= "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static int tries = 1;
    private static int[][] matrix;
    private static int max;
    private static int minX;
    private static HashSet<Pair> reversedCells = new HashSet<>();
    private static HashSet<Integer> modifications_with_operator_plus = new HashSet<>();
    private static HashSet<Integer> modifications_with_operator_multiply = new HashSet<>();
    private static HashSet<Integer> modifications_with_operator_div = new HashSet<>();
    private static final File INPUT_FILE = new File("input.txt");

    public static void main(String[] args) throws FileNotFoundException {
	// write your code here
        long started = System.currentTimeMillis();
        //
        run();
        //
        long ended = System.currentTimeMillis();
        System.out.println("---------------");
        System.out.println(String.format("Successfully passed for %dms",ended-started));
    }
    public static void run() throws FileNotFoundException {
        Scanner sc = new Scanner(INPUT_FILE);
        max = sc.nextInt();
        System.out.println("Играем до "+max+" очков");
        String msg;
        while(sc.hasNext()) {
            msg = sc.next();
            if (!(msg.startsWith("+") || msg.startsWith("*") || msg.startsWith("/"))) continue;
            String action = msg.substring(0,1);
            int modification = Integer.parseInt(msg.substring(1));
            if (modification <= 0) continue;

            switch(action) {
                case "+":
                    modifications_with_operator_plus.add(modification);
                    break;
                case "*":
                    if (modification <= 1) break;
                    modifications_with_operator_multiply.add(modification);
                    break;
                case "/":
                    if (modification <= 1) break;
                    //modifications_with_operator_multiply.add(modification);
                    break;
            }
        }
        System.out.println("Действия:");
        System.out.println(Arrays.toString(modifications_with_operator_plus.stream().map(v -> "+" + v).toArray()).replace("[","").replace("]",""));
        System.out.println(Arrays.toString(modifications_with_operator_multiply.stream().map(v -> "*" + v).toArray()).replace("[","").replace("]",""));
        System.out.println(Arrays.toString(modifications_with_operator_div.stream().map(v -> "/" + v).toArray()).replace("[","").replace("]",""));

        System.out.println("Представим декартову систему координат XOY. Но X -→ , а Y ↓");
        System.out.println("Обнулим Y(Y=0) и найдём минимальный X, чтобы 1Игрок победил 1ходом");
        minX = max;
        int lengthOfMinX = (int) Math.ceil(Math.log10(minX));
        for (int modification : modifications_with_operator_plus) {
            if (max - modification < minX) minX = max - modification;
            System.out.println(String.format("X + %d <= %d; X <= %d",modification,max,max-modification));
        }
        for (int modification : modifications_with_operator_multiply) {
            if (max / modification < minX) minX = (int) Math.ceil(max*1d / modification);
            System.out.println(String.format("X * %d <= %d; X <= %d",modification,max,(int) Math.ceil(max*1d / modification)));
        }
        System.out.println(String.format("X <= %d",minX));
        System.out.println(String.format("Теперь мы построим таблицу выйгрышных ходов от 0;0 до %d;%d",minX,minX));

        matrix = new int[minX+1][minX+1];
        int maxSolves = 0;
        for (int y = 0; y < matrix[0].length; y++) {
            for (int x = 0; x < matrix.length; x++) {
                if (matrix[x][y] == 0) check(x, y);
                if (matrix[x][y] > maxSolves) maxSolves = matrix[x][y];
            }
        }
        int lengthOfMaxSolves = (int) Math.ceil(Math.log10(maxSolves));
        String tabRepeat = " ".repeat(lengthOfMaxSolves);
        String tabLeft = " ".repeat(lengthOfMinX)+tabRepeat;
        boolean c = false;
        for (int i = lengthOfMinX-1; i >= 0; i--) {
            System.out.print(tabLeft);
            System.out.print(c ? ANSI_RED : ANSI_CYAN);
            int min = (int) Math.pow(10, i);
            for (int x = 0; x < matrix.length; x++) {
                System.out.print(x < min && i != 0 ? " " : (x/min)%10);
                if (x < matrix.length - 1) System.out.print(tabRepeat);
            }
            System.out.println(ANSI_RESET);
            c = !c;
        }
        /*System.out.print(tabLeft+ANSI_RED);
        for (int x = 0; x < matrix.length; x++) {
            System.out.print(x < 10 ? " " : x/10);
            if (x < matrix.length - 1) System.out.print(tabRepeat);
        }
        System.out.println(ANSI_RESET);
        System.out.print(tabLeft+ANSI_CYAN);
        for (int x = 0; x < matrix.length; x++) {
            System.out.print(x%10);
            if (x < matrix.length - 1) System.out.print(tabRepeat);
        }
        System.out.println(ANSI_RESET);*/
        c = false; //Toggle color
        for (int y = 0; y < matrix[0].length; y++) {
            System.out.print(String.format("%s%"+lengthOfMinX+"d%s ",ANSI_BLUE,y,ANSI_RESET));
            for (int x = 0; x < matrix.length; x++) {
                int res = matrix[x][y];
                switch (res) {
                    case 1: System.out.print(ANSI_GREEN);break;
                    case 2: System.out.print(ANSI_YELLOW);break;
                    case 3: System.out.print(ANSI_PURPLE);break;
                    case 4: System.out.print(ANSI_MAGENTA);break;
                    default:
                        if (res <= 0) {System.out.print(ANSI_RED);break;}
                        System.out.print(ANSI_WHITE);
                }
                System.out.print(String.format("%"+lengthOfMaxSolves+"s%s",res > 0 ? res : "X",ANSI_RESET));
                //System.out.print((res > 0 ? res : "X")+ANSI_RESET);
                c = !c;
                if (x < matrix.length - 1) System.out.print(" ");
            }
            System.out.println(ANSI_RESET);
        }
    }
    private static int check(int x, int y) {
        tries++;
        if (tries > 10000) {
            System.out.println("DEBUG");
        }
        if (x < matrix.length && y < matrix[0].length) if (matrix[x][y] != 0) return matrix[x][y];

        if (modifications_with_operator_plus.stream().anyMatch(v -> x+y+v >= max)) {
            if (x < matrix.length && y < matrix[0].length) matrix[x][y] = 1;
            return 1;
        }
        if (modifications_with_operator_multiply.stream().anyMatch(v -> x*v+y >= max || x+y*v >= max)) {
            if (x < matrix.length && y < matrix[0].length) matrix[x][y] = 1;
            return 1;
        }

        HashSet<Integer> min_resolves = new HashSet<>();
        for (int modification : modifications_with_operator_plus) {
            min_resolves.add(check(x+modification, y));
            min_resolves.add(check(x, y+modification));
        }
        for (int modification : modifications_with_operator_multiply) {
            if (x > 0) min_resolves.add(check(x*modification, y));
            if (y > 0) min_resolves.add(check(x, y*modification));
        }
        if (reversedCells.stream().noneMatch(p -> p.get()[0] == x && p.get()[1] == y)) {
            for (int modification : modifications_with_operator_div) {
                if (x >= 0) {
                    reversedCells.add(new Pair(x,y));
                    min_resolves.add(check(x/modification, y));
                }
                if (y >= 0) {
                    reversedCells.add(new Pair(x,y));
                    min_resolves.add(check(x, y/modification));
                }
            }
        }
        Optional<Integer> oi;
        int min_resolve = 0;
        //Если хоть один ход после этого хода будет являться ПРОИГРЫШНЫМ, то этот ход будет ВЫЙГРЫШНЫМ
        if (min_resolves.stream().anyMatch(v -> v < 0)) {//Выйгрышный ход
            oi = min_resolves.stream().filter(v -> v<0).max(Comparator.comparing(Integer::valueOf));
            if (oi.isPresent()) {
                min_resolve = -1*oi.get()+1;
                matrix[x][y] = min_resolve;
            }
        } else {//Проигрышный ход
            oi = min_resolves.stream().filter(v -> v>0).min(Comparator.comparing(Integer::valueOf));
            if (oi.isPresent()) {
                min_resolve = oi.get();
                matrix[x][y] = -1*min_resolve;
            }
        }
        return matrix[x][y];
    }
}
