
/**
 * Compilation (only !) tester for Maman 12, 20441, 2021a
 * 
 * @author Course staff 
 * @version 2_1
 */
public class PointTester
{
    public static void main()
    {
        System.out.println("============ Testing class Point =============");
        Point p1 = new Point(3.95864, 4.4);
        Point p2 = new Point(p1);
        p2.setX(4.456899);
        System.out.println(p2.isAbove(p1));
        System.out.println(p2.isUnder(p1));

    }
}

