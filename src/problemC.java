
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Scanner;


public class problemC {

    private static Scanner dummy;
    private int[] inputLenghtArray;
    private Point2D.Double[] vectorPoints;
    private LinkedList<Integer> linkedLenghts;
    private int lenghtSum;
    private double maxLenght;

    public problemC(Scanner dummy) { //Detta är bara för att få igång den, kunde ha skrivit allt i class men skulle vara för stor huvudvärk
        problemC.dummy = dummy;
    }

    public static void main(String[] args) {

        new problemC(dummy).calculation();
    }

    private void calculation() { //kalk funktionen som kommer kalla runt saker
        Scanner scan = new Scanner(System.in);
        int inputLenght = scan.nextInt();
        inputLenghtArray = new int[inputLenght];
        linkedLenghts = new LinkedList<>();
        lenghtSum = 0;
        for (int i = 0; i < inputLenght; ++i) { // kör igenom så många input som man fick från början
            //Spara ner alla inputs
            inputLenghtArray[i] = scan.nextInt();
            linkedLenghts.add(i);
            lenghtSum += inputLenghtArray[i];
        }
        vectorPoints = new Point2D.Double[inputLenght];//Skapa vector points

        maxLenght = 0.0; //Default värde, kommer ändras senare
        for (int i = 0; i < inputLenght; ++i) {
            //Börja bygg segmenterna
            int build = linkedLenghts.removeFirst();
            maxLenght = Math.max(maxLenght, buildingBlock(build));
            linkedLenghts.addLast(build);
            //När allt är klart kommer man få resultat
        }
        System.out.println(maxLenght);
        scan.close();
    }

    private double buildingBlock(int i) { //Bygg upp allt och see vad som är möjligt
        vectorPoints[0] = new Point2D.Double(0.0, 0.0);
        vectorPoints[1] = new Point2D.Double(inputLenghtArray[i], 0.0);
        return sumLenghtCalc(0, 1, inputLenghtArray[i], 0.0,  new Point2D.Double(((double) inputLenghtArray[i]) / 2.0, -1.0));
    }

    private double sumLenghtCalc(int i, int j, int lenghtSumUsed, double height, Point2D.Double offset) {
        if (linkedLenghts.size() == 0) {
            return 0.0;
        }
        if (height + (double) (lenghtSum - lenghtSumUsed) < maxLenght) { //Kollar om vi har något kvar om inte sätt noll eller om det spelar ingen roll den som är kvar
            return 0.0;
        }
        //Lite komplex summering
        double maxPointBeginging = Math.max(0.0, Math.max(vectorPoints[i].y, vectorPoints[j].y));
        int nRemain = linkedLenghts.size();
        for (int x = 0; x < nRemain; ++x) {
            int edge1 = linkedLenghts.removeFirst();
            int lenghtAdd = inputLenghtArray[edge1];
            for (int z = 0; z < nRemain - 1; ++z) {
                int linkedLenght = linkedLenghts.removeFirst();
                int lenghtNextAdd = inputLenghtArray[linkedLenght];
                Point2D.Double[] v = checkLenghPathCalc(vectorPoints[i], vectorPoints[j],
                        inputLenghtArray[edge1], inputLenghtArray[linkedLenght], offset);
                if (v != null) {
                    for (Point2D.Double aDouble : v) {
                        vectorPoints[j + 1] = aDouble;
                        maxPointBeginging = Math.max(maxPointBeginging, aDouble.y);
                        double h1 = sumLenghtCalc(i, j + 1, lenghtSumUsed + lenghtAdd + lenghtNextAdd,
                                maxPointBeginging, vectorPoints[j]);
                        maxPointBeginging = Math.max(maxPointBeginging, h1);
                        double h2 = sumLenghtCalc(j, j + 1, lenghtSumUsed + lenghtAdd + lenghtNextAdd,
                                maxPointBeginging, vectorPoints[i]);
                        maxPointBeginging = Math.max(maxPointBeginging, h2);
                    }
                }
                linkedLenghts.addLast(linkedLenght);
            }
            linkedLenghts.addLast(edge1);
        }
        return maxPointBeginging;
    }

    private Point2D.Double[] checkLenghPathCalc(Point2D.Double pointVectorOne, Point2D.Double pointVectorTwo, double first, double second, Point2D.Double offset) {
        double lenght = pointVectorOne.distance(pointVectorTwo);
        if (lenght == 0.0 || lenght >= (first+second) || first >= (lenght + second) || second >= (lenght + first)){
            return null;
        }
        double interSect = (first*first - second*second + lenght*lenght) / (2.0 * lenght);
        double squareRoot = first*first - interSect*interSect;

        double lenghtHieght = Math.sqrt(squareRoot);

        double x0 = pointVectorOne.x + interSect * (pointVectorTwo.x - pointVectorOne.x) / lenght;
        double y0 = pointVectorOne.y + interSect * (pointVectorTwo.y - pointVectorOne.y) / lenght;

        double x1 = x0 + lenghtHieght * (pointVectorTwo.y - pointVectorOne.y) /lenght;
        double y1 = y0 - lenghtHieght * (pointVectorTwo.x - pointVectorOne.x) /lenght;

        double x2 = x0 - lenghtHieght * (pointVectorTwo.y - pointVectorOne.y) /lenght;
        double y2 = y0 + lenghtHieght * (pointVectorTwo.x - pointVectorOne.x) /lenght;

        Point2D.Double[] results = new Point2D.Double[2];
        results[0] = new Point2D.Double(x1, y1);
        results[1] = new Point2D.Double(x2, y2);

        return results;
    }
}