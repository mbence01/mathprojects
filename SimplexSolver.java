import java.util.Scanner;

public class SimplexSolver {
    public static final int PIVOT_BLAND = 1;
    public static final int PIVOT_LEGNAGYOBBNOVEKMENY = 2;
    public static final int PIVOT_KLASSZIKUS = 3;

    static int iterationCount = 0;

    static int[] baseIndexes;
    static double[] tableConstants;
    static double[][] tableVars;

    static double functionConstant = 0;
    static double[] functionVars;

    static int rowCount;
    static int varCount;

    public static void main(String[] args) {
        //------------------------------------------------------------------------------------------------------

        Scanner sc = new Scanner(System.in);

        System.out.println("Kezdo szotar megadasa");
        System.out.println("=====================\n");

        System.out.print("Add meg a sorok szamat: ");
        rowCount = sc.nextInt();

        System.out.print("Add meg a valtozok szamat: ");
        varCount = sc.nextInt();

        baseIndexes = new int[rowCount];
        tableConstants = new double[rowCount];
        tableVars = new double[rowCount][varCount];

        functionVars = new double[varCount];

        //------------------------------------------------------------------------------------------------------

        int c = 0;
        while(c <= rowCount) {
            if(c == rowCount) {
                System.out.print("Add meg a celfuggveny konstans erteket: ");
                functionConstant = sc.nextInt();

                int d = 0;
                while (d < varCount) {
                    System.out.print("Add meg x" + (d + 1) + " erteket a celfuggvenyben: ");
                    functionVars[d++] = sc.nextInt();
                }
            } else {
                System.out.println((c + 1) + ". sor megadasa:");

                System.out.print("Add meg a bazisvaltozo indexet: ");
                baseIndexes[c] = sc.nextInt();

                System.out.print("Add meg a konstans erteket: ");
                tableConstants[c] = sc.nextInt();

                int d = 0;
                while (d < varCount) {
                    System.out.print("Add meg x" + (d + 1) + " erteket az " + (c + 1) + ". sorban: ");
                    tableVars[c][d++] = sc.nextInt();
                }
            }
            c++;
        }

        //------------------------------------------------------------------------------------------------------

        System.out.println("Valassz pivot elem valasztasi strategiat: ");

        for (int i = 1; i <= 3; i++) {
            System.out.println(i + " -> " + getPivotStepName(i));
        }
        System.out.print("> ");

        int pivot = sc.nextInt();

        simplexAlgorithm(pivot);

        sc.close();
    }

    public static void showTable() {
        for (int i = 0; i < baseIndexes.length; i++) {
            System.out.print("x" + baseIndexes[i] + " = " + tableConstants[i]);

            int c = 0;
            while(c < tableVars[i].length) {
                if(tableVars[i][c] != 0) {
                    double tempVar = (tableVars[i][c] > 0) ? (tableVars[i][c]) : (tableVars[i][c] * -1);

                    System.out.print(((tableVars[i][c] > 0) ? " + " : " - ") + tempVar + "x" + (c+1));
                } else {
                    System.out.print(" + 0x" + (c+1));
                }

                c++;
            }
            System.out.println("");
        }
        System.out.println("---------------------------------------");
        System.out.print("max z = " + functionConstant);

        int c = 0;
        while(c < functionVars.length) {
            if(functionVars[c] != 0) {
                double tempVar = (functionVars[c] > 0) ? (functionVars[c]) : (functionVars[c] * -1);

                System.out.print(((functionVars[c] > 0) ? " + " : " - ") + tempVar + "x" + (c+1));
            } else {
                System.out.print(" + 0x" + (c+1));
            }

            c++;
        }
        System.out.println("");
    }

    public static void simplexAlgorithm(int pivot) {
        System.out.println("\n");
        System.out.println("=========================================");
        System.out.println("============== " + ++iterationCount + ". ITERACIO ==============");
        System.out.println("=========================================");

        boolean functionVarCheck = true;

        for(int i = 0; i < functionVars.length; i++) {
            if(functionVars[i] > 0) {
                functionVarCheck = false;
                break;
            }
        }

        if(functionVarCheck) {
            System.out.println("Az aktualis bazismegoldas optimalis, az algoritmus megall!");
            return;
        }

        System.out.println("Pivot elem valasztasi strategia: " + getPivotStepName(pivot) + "\n");
        int[] pivotElem = getPivot(pivot);

        System.out.println("Kivalasztott elem: x" + (pivotElem[1]+1) + " a " + (pivotElem[0]+1) + ". egyenletbol...\n");

        boolean tableVarCheck = true;

        for (int i = 0; i < rowCount; i++) {
            if(tableVars[i][pivotElem[1]] < 0) {
                tableVarCheck = false;
                break;
            }
        }

        if(tableVarCheck) {
            System.out.println("Az LP feladat nem korlatos, az algoritmus megall!");
            return;
        }

        doPivot(pivotElem);
        showTable();

        System.out.println("\n");
        System.out.println("============== " + iterationCount + ". ITERACIO VEGE ==============");
        System.out.println("\n");

        simplexAlgorithm(pivot);
    }

    public static int[] getPivot(int which) {
        int[] returnValue = new int[2];

        int SOR = 0;
        int OSZLOP = 1;

        switch (which) {
            case PIVOT_LEGNAGYOBBNOVEKMENY -> {
                double[] valList = new double[varCount];

                for (int i = 0; i < varCount; i++) {
                    if(functionVars[i] <= 0) continue;

                    valList[i] = functionVars[i];
                    double minValue = Double.MAX_VALUE;
                    for (int j = 0; j < rowCount; j++) {
                        if(tableVars[j][i] >= 0) continue;

                        double tempVar = Math.abs(tableConstants[j] / tableVars[j][i]);
                        if(tempVar < minValue) {
                            minValue = tempVar;
                        }
                    }
                    valList[i] *= minValue;
                }

                double maxValue = Double.MIN_VALUE;
                int minIndex = Integer.MAX_VALUE;
                for (int i = valList.length - 1; i >= 0; i--) {
                    if(valList[i] >= maxValue && i < minIndex) {
                        maxValue = valList[i];
                        minIndex = i;
                        returnValue[OSZLOP] = i;
                    }
                }

                double minValue = Double.MAX_VALUE;
                for (int i = rowCount - 1; i >= 0; i--) {
                    if(tableVars[i][returnValue[OSZLOP]] >= 0) continue;

                    double tempVar = Math.abs(tableConstants[i] / tableVars[i][returnValue[OSZLOP]]);
                    if(tempVar <= minValue) {
                        minValue = tempVar;
                        returnValue[SOR] = i;
                    }
                }
                return returnValue;
            }

            case PIVOT_BLAND -> {
                int minValue = Integer.MAX_VALUE;
                for (int i = functionVars.length - 1; i >= 0; i--) {
                    if(functionVars[i] < 0) continue;

                    if(i < minValue) {
                        minValue = i;
                        returnValue[OSZLOP] = i;
                    }
                }

                double minVal = Double.MAX_VALUE;
                int minIndex = Integer.MAX_VALUE;

                for (int i = 0; i < rowCount; i++) {
                    double tempVal = Math.abs(tableConstants[i] / tableVars[i][returnValue[OSZLOP]]);

                    if(tempVal < minVal && baseIndexes[i] < minIndex) {
                        minVal = tempVal;
                        minIndex = baseIndexes[i];

                        returnValue[SOR] = i;
                    }
                }
                return returnValue;
            }

            case PIVOT_KLASSZIKUS -> {
                double maxValue = Double.MIN_VALUE;
                for (int i = functionVars.length - 1; i >= 0; i--) {
                    if(functionVars[i] >= maxValue) {
                        maxValue = functionVars[i];
                        returnValue[OSZLOP] = i;
                    }
                }

                double minValue = Double.MAX_VALUE;
                for (int i = rowCount - 1; i >= 0; i--) {
					if(tableVars[i][returnValue[OSZLOP]] >= 0) continue;
					
                    double tempRes = Math.abs(tableConstants[i] / tableVars[i][returnValue[OSZLOP]]);

                    if(tempRes <= minValue) {
                        minValue = tempRes;
                        returnValue[SOR] = i;
                    }
                }
                return returnValue;
            }
        }
        return null;
    }

    public static void doPivot(int[] pivotElem) {
        double elem = (tableVars[pivotElem[0]][pivotElem[1]] * -1);
        int row = pivotElem[0];
        int col = pivotElem[1];

        System.out.println("x" + (col + 1) + " kifejezese...\n");

        tableVars[row][baseIndexes[row]-1]--;

        baseIndexes[row] = col + 1;

        tableVars[row][col] = 0;

        tableConstants[row] /= elem;
        for (int i = 0; i < varCount; i++) {
            tableVars[row][i] /= elem;
        }

        for(int i = 0; i <= rowCount; i++) {
            if(i == row) continue;
            if(i < rowCount && tableVars[i][col] == 0) continue;

            double constant = (i == rowCount) ? functionVars[col] : tableVars[i][col];
            for (int j = 0; j < varCount; j++) {
                if(j == col) continue;

                if(i == rowCount) {
                    functionVars[j] += (constant * tableVars[row][j]);
                } else {
                    tableVars[i][j] += (constant * tableVars[row][j]);
                }
            }
            if(i != rowCount) {
                tableConstants[i] += (constant * tableConstants[row]);
                tableVars[i][col] = 0;
            } else {
                functionConstant += (constant * tableConstants[row]);
                functionVars[col] = 0;
            }
        }
    }

    public static String getPivotStepName(int pivot) {
        if(pivot == PIVOT_BLAND) return "Bland";
        else if(pivot == PIVOT_KLASSZIKUS) return "Klasszikus";
        else if(pivot == PIVOT_LEGNAGYOBBNOVEKMENY) return "Legnagyobb novekmeny";
        return "Ismeretlen";
    }
}
