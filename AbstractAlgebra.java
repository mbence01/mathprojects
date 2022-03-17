import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class AbstractAlgebra {
    static Scanner scanner = new Scanner(System.in);

    static char[] list = new char[] { 'a', 'b', 'c', 'd' };

    public static void main(String[] args) {
        String line = null;

        do {
            System.out.print("Adj meg egy parancsot (help): ");
            line = scanner.nextLine();

            if(line != null) {
                doCommand(line);
            }
        } while(!"quit".equals(line));

        scanner.close();
    }

    public static void doCommand(String cmd) {
        switch(cmd) {
            case "help" -> {
                showHelp();
            }
            case "tulajdonsagok" -> {
                showRules();
            }
            case "addgrupoid" -> {
                addGrupoid();
            }
            case "addmaradek" -> {
                addMaradek();
            }
            case "add" -> {
                add();
            }
        }
    }
    
    public static void showRules() {
        System.out.println("Legyen '*' egy művelet az A halmazon:");
        System.out.println("Kommutatív: ha a*b == b*a");
        System.out.println("Asszociatív: ha (a*b)*c == a*(b*c)");
        System.out.println("Zéruselem: minden a ∈ A esetén a*z == z == z*a");
        System.out.println("Egységelem: minden a ∈ A esetén a*e == a == e*a");
        System.out.println("Ha e egységelem, akkor a és b egymás inverzei, ha a*b == e == b*a");
        System.out.println("Kancellatív: ha a*c == b*c, akkor a == b");
    }

    public static void add() {
        System.out.print("Szorzás vagy összeadás? ('szorzas', 'osszeadas'): ");
        String res = scanner.nextLine().toLowerCase(Locale.ROOT);

        System.out.print("Add meg a sorok számát: ");
        int max = scanner.nextInt();

        int[][] maradek = new int[max][max];

        for(int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                System.out.print("Add meg a(z) " + (i+1) + ". sor " + (j+1) + ". oszlopát: ");
                maradek[i][j] = scanner.nextInt();
            }
        }

        System.out.print(res.equals("szorzas") ? "* | " : "+ | ");
        for (int i = 0; i < max; i++) {
            System.out.print(i + " ");
        }
        System.out.print("\n--|-");
        for (int i = 0; i < max*2; i++) System.out.print("-");
        System.out.println("");

        for (int i = 0; i < max; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < max; j++) {
                System.out.print(maradek[i][j] + " ");
            }
            System.out.println("");
        }

        checkDetails(maradek);
    }

    public static void addMaradek() {
        System.out.print("Szorzás vagy összeadás? ('szorzas', 'osszeadas'): ");
        String res = scanner.nextLine().toLowerCase(Locale.ROOT);

        System.out.print("Add meg melyik számnak a maradékosztálya: ");
        int max = scanner.nextInt();

        int[][] maradek = new int[max][max];

        for(int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                if(res.equals("szorzas")) maradek[i][j] = (i*j)%max;
                else maradek[i][j] = (i+j)%max;
            }
        }

        System.out.print(res.equals("szorzas") ? "* | " : "+ | ");
        for (int i = 0; i < max; i++) {
            System.out.print(i + " ");
        }
        System.out.print("\n--|-");
        for (int i = 0; i < max*2; i++) System.out.print("-");
        System.out.println("");

        for (int i = 0; i < max; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < max; j++) {
                System.out.print(maradek[i][j] + " ");
            }
            System.out.println("");
        }

        checkDetails(maradek);
    }

    public static void addGrupoid() {
        char[][] grupoid = new char[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print("Add meg a(z) " + (i + 1) + ". sor " + (j + 1) + ". oszlop elemét: ");
                grupoid[i][j] = scanner.nextLine().charAt(0);
            }
        }

        System.out.println("* | a b c d");
        System.out.println("--|--------");
        for (int i = 0; i < 4; i++) {
            System.out.print(list[i] + " | ");
            for (int j = 0; j < 4; j++) {
                System.out.print(grupoid[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");

        checkDetails(grupoid);
    }

    public static void checkDetails(int[][] maradek) {
        String struktura = "grupoid";
        String line = "A művelet kommutatív.";
        int egysegelem = 9999;
        boolean komm = true;

        for (int i = 0; i < maradek.length; i++) {
            for (int j = 0; j < maradek.length; j++) {
                if(maradek[i][j] != maradek[j][i]) {
                    line = "A művelet nem kommutatív.";
                    komm = false;
                    break;
                }
            }
            if("A művelet nem kommutatív.".equals(line)) break;
        }
        System.out.println(line);

        line = "A művelet asszociatív.";

        for (int i = 0; i < maradek.length; i++) {
            for (int j = 0; j < maradek.length; j++) {
                for (int k = 0; k < maradek.length; k++) {
                    if(maradek[maradek[i][j]][k] != maradek[i][maradek[j][k]]) {
                        line = "A művelet nem asszociatív.";
                        break;
                    }
                }
                if("A művelet nem asszociatív.".equals(line)) break;
            }
            if("A művelet nem asszociatív.".equals(line)) break;
        }
        if("A művelet asszociatív.".equals(line)) struktura = "félcsoport";

        System.out.println(line);

        int c = 0; // a-b-c-d
        boolean zerus = true;
        while(c != maradek.length) {
            zerus = true;
            for (int i = 0; i < maradek.length; i++) {
                if(maradek[i][c] != c || maradek[c][i] != c) {
                    zerus = false;
                    break;
                }
            }
            if(zerus) {
                System.out.println("A műveletnek van zéruseleme: " + c);
                break;
            }
            c++;
        }
        if(!zerus) System.out.println("A műveletnek nincs zéruseleme.");


        c = 0; // a-b-c-d
        zerus = true;
        while(c != maradek.length) {
            zerus = true;
            for (int i = 0; i < maradek.length; i++) {
                if(maradek[i][c] != i || maradek[c][i] != i) {
                    zerus = false;
                    break;
                }
            }
            if(zerus) {
                System.out.println("A műveletnek van egységeleme: " + c);
                egysegelem = c;
                break;
            }
            c++;
        }
        if(!zerus) System.out.println("A műveletnek nincs egységeleme.");

        if(egysegelem != 9999) {
            if(struktura.equals("félcsoport")) struktura = "monoid";

            List<Integer> inverz_list = new ArrayList<>();

            System.out.println("Inverzek:");
            for (int i = 0; i < maradek.length; i++) {
                for (int j = 0; j < maradek.length; j++) {
                    if (maradek[i][j] == maradek[j][i] && maradek[i][j] == egysegelem) {
                        System.out.println(i + " inverze " + j + "-nak/nek");
                        inverz_list.add(i);
                    }
                }
            }

            int invC = 0;
            for (int i = 0; i < maradek.length; i++) {
                if(inverz_list.contains(i)) {
                    invC++;
                }
            }
            if(invC == maradek.length) struktura = "csoport";
        }

        line = "A művelet kancellatív.";

        for (int a = 0; a < maradek.length; a++) {
            for(int b = 0; b < maradek.length; b++) {
                for (c = 0; c < maradek.length; c++) {
                    if(maradek[a][c] == maradek[b][c] && a != b) {
                        line = "A művelet nem kancellatív.";
                        break;
                    }
                    if(maradek[c][a] == maradek[c][b] && a != b) {
                        line = "A művelet nem kancellatív.";
                        break;
                    }
                }
                if("A művelet nem kancellatív.".equals(line)) break;
            }
            if("A művelet nem kancellatív.".equals(line)) break;
        }
        System.out.println(line);

        if(struktura.equals("csoport") && komm) {
            struktura = "Abel-csoport";
        }

        System.out.println("\nA művelet: " + struktura);
    }

    public static void checkDetails(char[][] grupoid) {
        String line = "A művelet kommutatív.";
        char egysegelem = 'x';
        String struktura = "grupoid";
        boolean komm = true;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(grupoid[i][j] != grupoid[j][i]) {
                    line = "A művelet nem kommutatív.";
                    komm = false;
                    break;
                }
            }
            if("A művelet nem kommutatív.".equals(line)) break;
        }
        System.out.println(line);

        line = "A művelet asszociatív.";

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if(grupoid[indexOf(grupoid[i][j], list)][k] != grupoid[i][indexOf(grupoid[j][k], list)]) {
                        line = "A művelet nem asszociatív.";
                        break;
                    }
                }
                if("A művelet nem asszociatív.".equals(line)) break;
            }
            if("A művelet nem asszociatív.".equals(line)) break;
        }
        if("A művelet asszociatív.".equals(line)) struktura = "félcsoport";

        System.out.println(line);

        int c = 0; // a-b-c-d
        boolean zerus = true;
        while(c != 4) {
            zerus = true;
            for (int i = 0; i < 4; i++) {
                if(grupoid[i][c] != list[c] || grupoid[c][i] != list[c]) {
                    zerus = false;
                    break;
                }
            }
            if(zerus) {
                System.out.println("A műveletnek van zéruseleme: " + list[c]);
                break;
            }
            c++;
        }
        if(!zerus) System.out.println("A műveletnek nincs zéruseleme.");


        c = 0; // a-b-c-d
        zerus = true;
        while(c != 4) {
            zerus = true;
            for (int i = 0; i < 4; i++) {
                if(grupoid[i][c] != list[i] || grupoid[c][i] != list[i]) {
                    zerus = false;
                    break;
                }
            }
            if(zerus) {
                System.out.println("A műveletnek van egységeleme: " + list[c]);
                egysegelem = list[c];
                break;
            }
            c++;
        }
        if(!zerus) System.out.println("A műveletnek nincs egységeleme.");

        if(egysegelem != 'x') {
            if(struktura.equals("félcsoport")) struktura = "monoid";

            List<Character> inverz_list = new ArrayList<>();

            System.out.println("Inverzek:");
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (grupoid[i][j] == grupoid[j][i] && grupoid[i][j] == egysegelem) {
                        System.out.println(list[i] + " * " + list[j] + " = " + grupoid[i][j]);
                        inverz_list.add(list[i]);
                    }
                }
            }

            int invC = 0;
            for (int i = 0; i < 4; i++) {
                if(inverz_list.contains(list[i])) {
                    invC++;
                }
            }
            if(invC == 4) struktura = "csoport";
        }

        line = "A művelet kancellatív.";

        for (int a = 0; a < 4; a++) {
            for(int b = 0; b < 4; b++) {
                for (c = 0; c < 4; c++) {
                    if(grupoid[a][c] == grupoid[b][c] && a != b) {
                        line = "A művelet nem kancellatív.";
                        break;
                    }
                    if(grupoid[c][a] == grupoid[c][b] && a != b) {
                        line = "A művelet nem kancellatív.";
                        break;
                    }
                }
                if("A művelet nem kancellatív.".equals(line)) break;
            }
            if("A művelet nem kancellatív.".equals(line)) break;
        }
        System.out.println(line);

        if(struktura.equals("csoport") && komm) {
            struktura = "Abel-csoport";
        }

        System.out.println("\nA művelet: " + struktura);
    }

    public static int indexOf(char ch, char[] arr) {
        if(arr == null) return -1;
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == ch) return i;
        }
        return -1;
    }

    public static void showHelp() {
        System.out.println("Elérhető parancsok:");
        System.out.println("'help' -> Elérhető parancsok listázása");
        System.out.println("'tulajdonsagok' -> Műveleti tulajdonságok");
        System.out.println("'addgrupoid' -> Grupoid létrehozása és a tulajdonságok kiértékelése");
        System.out.println("'addmaradek' -> Maradékosztály létrehozása és a tulajdonságok kiértékelése");
    }
}
