import java.util.regex.*;
import java.util.Vector;

public class polygonAlterTest {
    
    public enum toStringErrorCode { 
        Okay, 
        Length, 
        Exception, 
        PolygonString, 
        PointsSpaces, 
        Points 
    };

    // ------------------------------------------------------------------------------------

    public static
    void DO_TEST(boolean condition, String statement,
                 Object expected, Object actual) 
    {
        System.out.print("TESTING: (" + statement + ") - ");

        if(condition == true)
            System.out.println("Correct!");
        else 
        {
            if(actual instanceof toStringErrorCode)
            {
                System.out.print("Wrong! - Information = ");
                
                switch((toStringErrorCode)actual)
                {
                case Points:
                    System.out.println("Possibly you have wrong points values?");
                    break;
                case PointsSpaces:
                    System.out.println("You have spaces between each Point!");
                    break;
                case PolygonString:
                    System.out.println("Your first statement (before \\n) is wrong!");
                    break;
                case Exception:
                    System.out.println("Tried to throw an exception!");
                    break;
                case Length: default:
                    System.out.println("Unknown");
                    break;
                }
            }
            else 
                System.out.println("Wrong! - Expected Value = " + expected + " , Actual = " + actual);
        }
    
    
    }

    // ------------------------------------------------------------------------------------

    public static
    boolean check_double(double value, double expected) {
        return Math.abs(expected - value) <= 0.1;
    }

    // ------------------------------------------------------------------------------------

    public static
    Vector<Double> parse_points(Point... points)
    {
        Vector<Double> vec = new Vector<Double>(points.length * 2);
        for(Point point : points) 
        {
            vec.add(point.getX());
            vec.add(point.getY());
        }

        return vec;
    }

    public static
    boolean check_toString_spaces(String text)
    {
        for(char ch : text.toCharArray()) 
        {
            if(ch == ' ')
                return false;
        }

        return true;
    }

    public static
    boolean check_toString_regex(String text, Point... points)
    {
        final String PointPattern = 
            "\\([+-]?(\\d+([.]\\d*)?([eE][+-]?\\d+)?|[.]\\d+([eE][+-]?\\d+)?)" +
             "\\,"                                                             +
             "[+-]?(\\d+([.]\\d*)?([eE][+-]?\\d+)?|[.]\\d+([eE][+-]?\\d+)?)\\)";

        Matcher matcher = Pattern.compile(PointPattern).matcher(text);

        Vector<Double> vec = parse_points(points);
        
        int checked = 0;
        while(matcher.find())
        {
            for(int j = 0; j <= matcher.groupCount(); j++)
            {
                try 
                {
                    if(matcher.group(j).charAt(0) != '.')
                    {
                        double value = Double.parseDouble(matcher.group(j));
                        if(!check_double(value, vec.get(checked)))
                            return false;
                        checked++;
                    }
                } 
                catch(Exception e) {/*NOT HANDLING*/}
            }
        }

        return checked == (points.length * 2);
    }

    public static
    String check_toString_without_points(String text, int num) 
    {
        try 
        {
            String expected_string = "The polygon has " + num + " vertices:\n";
    
            String substr = text.substring(0, expected_string.length());
            if(!substr.equals(expected_string))
                return null;      
    
            return text.replace(substr, "");
        }
        catch(Exception e) {
            return null;
        }
    }

    public static
    toStringErrorCode check_toString(String text, Point... points)
    {
        // Expect only points above 0
        if(points.length == 0)
            return toStringErrorCode.Length;

        try 
        {
            String fixed_string = check_toString_without_points(text, points.length);
            if(fixed_string == null)
                return toStringErrorCode.PolygonString;

            if(!check_toString_spaces(fixed_string))
                return toStringErrorCode.PointsSpaces;

            if(!check_toString_regex(fixed_string, points))
                return toStringErrorCode.Points;
            return toStringErrorCode.Okay;
        } 
        catch(Exception e) {
            return toStringErrorCode.Exception;
        }
    }

    // ------------------------------------------------------------------------------------

    public static
    void main(String[] args)
    {
        System.out.println("-------------------------------- TEST STARTED --------------------------------");

        System.out.println("---------------------------- Empty Convex Polygon ----------------------------");
        {
            Polygon convex_poly = new Polygon();
            DO_TEST(convex_poly.highestVertex() == null, "convex_poly.highestVertex() == null", null, convex_poly.highestVertex());

            Point point = new Point(10, 10);
            {
                int res = convex_poly.findVertex(point);
                DO_TEST(res == -1, "convex_poly.findVertex(point) == -1", -1, res);
            }
            {
                Point res = convex_poly.getNextVertex(point);
                DO_TEST(res == null, "convex_poly.getNextVertex(point) == null", null, res);
            }

            {
                String res = convex_poly.toString();
                DO_TEST(convex_poly.toString().equals(res), "convex_poly.toString().equals('" + res + "')", "The polygon has 0 vertices.", res);
            }

            {
                double res = convex_poly.calcPerimeter();
                DO_TEST(res == 0, "convex_poly.calcPerimeter() == 0", 0, res);
            }
            
            {
                double res = convex_poly.calcArea();
                DO_TEST(res == 0, "convex_poly.calcArea() == 0", 0, res);
            }

            {
                Polygon poly = convex_poly.getBoundingBox();
                DO_TEST(poly == null, "convex_poly.getBoundingBox() == null", null, "Allocated");
            }
        }
        System.out.println("-------------------------------------------------------------------------------");

        System.out.println("---------------------------------- Convex Polygon -----------------------------");
        {
            Polygon convex_polygon = new Polygon();
            Point[/*5*/] points = {
                new Point(6.09, 5.80), // A
                new Point(8.00, 2.83), // B
                new Point(5.76, 0.10), // C
                new Point(2.48, 1.38), // D
                new Point(2.68, 4.90)  // E
            };

            for(Point p : points) 
                convex_polygon.addVertex(p.getX(), p.getY());

            // begin tests
            for(int i = 0; i < points.length; i++)
            {
                int res = convex_polygon.findVertex(points[i]);
                DO_TEST(res == i, "convex_polygon.findVertex" + points[i].toString(), i, res);
            }

            {
                Point res = convex_polygon.highestVertex();
                DO_TEST(res.equals(points[0]), "convex_polygon.highestVertex().equals(new Point(6.09, 5.80))", points[0].toString(), res.toString());
            }

            for(int i = 1; i < points.length; i++)
            {
                int comp  = (i + 1) % points.length;
                Point res = convex_polygon.getNextVertex(points[i]);
                DO_TEST(res.equals(points[comp]), "convex_polygon.getNextVertex" + points[i], points[comp].toString(), res.toString());
            }

            {
                Polygon poly = convex_polygon.getBoundingBox();
                {
                    Point pt = new Point(2.48, 5.80);
                    DO_TEST(poly.findVertex(pt) != -1, "poly_bounds.findVertex" + pt, "not -1", -1);
                }
                {
                    Point pt = new Point(2.48, 0.10);
                    DO_TEST(poly.findVertex(pt) != -1, "poly_bounds.findVertex" + pt, "not -1", -1);
                }
                {
                    Point pt = new Point(8.00, 0.10);
                    DO_TEST(poly.findVertex(pt) != -1, "poly_bounds.findVertex" + pt, "not -1", -1);
                }
                {
                    Point pt = new Point(8.00, 5.80);
                    DO_TEST(poly.findVertex(pt) != -1, "poly_bounds.findVertex" + pt, "not -1", -1);
                }
                {
                    double res = poly.calcPerimeter();
                    DO_TEST(check_double(res, 22.44), "poly_bounds.calcPerimeter() == ~22.44", "~22.44", res);
                }
                {
                    boolean res1 = poly.isBigger(convex_polygon);
                    DO_TEST(res1 == true, "poly.isBigger(convex_polygon)", true, res1);
                    boolean res2 = convex_polygon.isBigger(poly);
                    DO_TEST(res2 == false, "convex_polygon.isBigger(poly)", false, res2);
                }
            }

            {
                double res = convex_polygon.calcArea();
                DO_TEST(check_double(res, 21.4), "poly.calcArea() == ~21.4", "~21.4", res);
            }

            {
                toStringErrorCode res = check_toString(convex_polygon.toString(), points);
                DO_TEST(res == toStringErrorCode.Okay, "check_toString(convex_polygon) == true", true, res);
            }

            {
                double res = convex_polygon.calcPerimeter();
                DO_TEST(check_double(res, 17.65), "poly.calcPerimeter() == ~17.65", "~17.65", res);
            }   

            for(int i = 0; i < 5; i++)
                convex_polygon.addVertex(0.0, 0.0);

            {
                boolean res = convex_polygon.addVertex(0.0, 0.0);
                DO_TEST(res == false, "convex_polygon.addVertex(0.0, 0.0) == FULL", false, res);
            }
        }

        System.out.println("------------------------------ Another Convex Polygon -------------------------");
        {
            Point[/*10*/] points = {
                new Point(4.24, 5.90),
                new Point(6.09, 5.80),
                new Point(7.52, 4.62),
                new Point(8.00, 2.83),
                new Point(7.32, 1.10),
                new Point(5.76, 0.10),
                new Point(3.91, 0.20),
                new Point(2.48, 1.38),
                new Point(2.00, 3.17),
                new Point(2.68, 4.90)
            };

            Polygon decagon = new Polygon();
            for(Point p : points) 
                decagon.addVertex(p.getX(), p.getY());

            {
                Point res = decagon.highestVertex();
                DO_TEST(res.equals(points[0]), "decagon.highestVertex().equals(new Point(4.24, 5.90))", points[0].toString(), res.toString());
            }

            {
                Polygon res = decagon.getBoundingBox();
                {
                    int found = res.findVertex(new Point(2.00, 5.90));
                    DO_TEST(found != -1, "bound.findVertex(new Point(2.00, 5.90))", "found != -1", -1);
                }
                {
                    int found = res.findVertex(new Point(2.00, 0.10));
                    DO_TEST(found != -1, "bound.findVertex(new Point(2.00, 0.10))", "found != -1", -1);
                }
                {
                    int found = res.findVertex(new Point(8.00, 5.90));
                    DO_TEST(found != -1, "bound.findVertex(new Point(8.00, 5.90))", "found != -1", -1);
                }
                {
                    int found = res.findVertex(new Point(8.00, 0.10));
                    DO_TEST(found != -1, "bound.findVertex(new Point(8.00, 0.10))", "found != -1", -1);
                }
            }

            {
                double res = decagon.calcArea();
                DO_TEST(check_double(res, 26.45), "decagon.calcArea() == ~26.45", "~26.45", res);
            }

            {
                double res = decagon.calcPerimeter();
                DO_TEST(check_double(res, 18.52), "decagon.calcPerimeter() == ~18.52", "~18.52", res);
            }

            {
                boolean res = decagon.addVertex(0.0, 0.0);
                DO_TEST(res == false, "decagon.addVertex(0.0, 0.0) == FULL", false, res);
            }

            {
                toStringErrorCode res = check_toString(decagon.toString(), points);
                DO_TEST(res == toStringErrorCode.Okay, "check_toString(decagon) == true", true, res);
            }
        }

        System.out.println("-------------------------------- TEST FINISHED --------------------------------");
    }

}
