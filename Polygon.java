/**
 * This class represents a polygon, with maximum of 10 veritces
 * 
 *
 * @author (Yoav Epstein)
 * @version (10/12/2020)
 */
public class Polygon
{
    // instance variables
    private Point [] _vertices;
    private int _noOfVertices;

    private final int MAX_VERTICES = 10;
    private final int MIN_VERTICES = 0;

    /**
     * Constructor for objects of class Polygon
     */
    public Polygon()
    {
        _vertices = new Point [MAX_VERTICES];
        _noOfVertices = 0;
    }

    //Methods
    /**
     * Adding a new vertex (a point) to the polygon.
     * @return True - if the vertex was added successfully 
     */
    public boolean addVertex(double x, double y)
    {
        boolean added = false;
        if(_noOfVertices < MAX_VERTICES){
            _vertices[_noOfVertices] = new Point (x,y);
            _noOfVertices++;
            added = true;
        }
        return added;
    }

    /**
     * Finding the highest vertex of the polygon.
     * @return The point in the highest location. 
     */
    public Point highestVertex()
    {        
        Point high;
        if (_noOfVertices == MIN_VERTICES)//No vertices added 
            high = null;
        else{
            high = new Point (_vertices[0]);
            for (int i = 1; i<_noOfVertices; i++){                
                if (_vertices[i].isAbove(high))
                    high = new Point (_vertices[i]);                        
            }
        }
        return high;
    }

    /**
     * Returns a string that represents this polygon.
     * @return String that represents this polygon
     * in the following format: 
     * The polygon has 10 vertices:
     * ((x1,y1),(x2,y2)...(x10,y10)) 
     */    
    public String toString()
    {                
        String headline = "";
        String body = "";
        if (_noOfVertices == MIN_VERTICES)
            headline = "The polygon has " + _noOfVertices + " vertices.";
        else{
            headline = "The polygon has " + _noOfVertices + " vertices:" + "\n";
            for (int i = 0; i<_noOfVertices-1; i++)            
                body = body + _vertices[i].toString() + ",";

            body = body + _vertices[_noOfVertices-1].toString();//Fence post
        }
        return headline + "(" + body + ")";
    }

    /**
     * Finding the perimeter of the polygon.
     * @return The perimeter perimeter of the polygon. 
     */
    public double calcPerimeter ()
    {
        double perimeter;
        if (_noOfVertices <= 1)// The 0 or 1 points in the array case
            perimeter = 0;
        else if (_noOfVertices == 2)// The 2 points in the array case
            perimeter = _vertices[0].distance(_vertices[1]); 
        else{// All other cases
            double temp = 0;
            for(int i = _noOfVertices - 1; i>=1; i--)                
                temp += _vertices[i].distance(_vertices[i-1]);                           
            perimeter = temp + _vertices[_noOfVertices - 1].distance(_vertices[0]);// Adding the distance between
            //  the first and the last points of the polygon
        }
        return perimeter;
    }

    /**
     * Finding the area of the polygon.
     * @return The area perimeter of the polygon. 
     */
    public double calcArea()
    {
        double area = 0;// The 0 to 2 points in the array case
        if (_noOfVertices >= 3){// All other cases 
            Point pivot =  new Point (_vertices[0]);//Capable for a polygon
            int j = 1;
            for (int i = 2; i < _noOfVertices; i++)
            {
                area += triangleArea(pivot, _vertices[i], _vertices[j]);
                j++;
            }
        }
        return area;
    }

    // Finding the area of a given triangle
    private double triangleArea (Point p1, Point p2, Point p3)
    {
        double a,b,c;
        a = p1.distance(p2);
        b = p2.distance(p3);
        c = p3.distance(p1);

        double s = (a + b + c)/2.0;// Heron's formula

        return Math.sqrt(s*(s-b)*(s-a)*(s-c));// Heron's formula
    }

    /**
     * Compering a given polygon to other and check who has the biggest
     * area.
     * @param other The other polygon to be compered to. 
     * @return True if this polygon is bigger then the other. 
     */
    public boolean isBigger(Polygon other)
    {
        boolean bigger = false;
        if (calcArea() > other.calcArea())
            bigger = true;
        return bigger;
    }

    /**
     * Finding whether a given point is a vertex of the polygon.
     * @param p The point it is looking to find. 
     * @return The index of the point in the polygon. 
     */
    public int findVertex(Point p)
    {
        int find = -1;//The default value in case there is no match
        for (int i = 0; i < _noOfVertices && find == -1; i++)
        {
            if (p.equals(_vertices[i]))
                find = i;
        }
        return find;
    }

    /**
     * Finding what is the next vertex of the polygon, according to a given point.
     * @param p The point that the next point will be attached to. 
     * @return The next point that this point will be attached to . 
     */
    public Point getNextVertex(Point p)
    {
        Point next;
        int i = findVertex(p);
        if (i == -1)
            next = null;//The default value in case there is no match
        else if (i == _noOfVertices - 1)// The last points in the array case, including a single point case 1`q;l'p ````
            next = new Point (_vertices[0]);
        else// All other cases 
            next = new Point (_vertices[i+1]);
        return next;
    }

    //Finding the leftest vertex of the polygon.
    private Point leftestVertex()
    {        
        Point left;
        if (_noOfVertices == 0)//No vertices added 
            left = null;
        else{
            left = new Point (_vertices[0]);
            for (int i = 1; i<_noOfVertices; i++)
            {                
                if (_vertices[i].isLeft(left))
                    left = new Point (_vertices[i]);                        
            }
        }
        return left;
    }

    //Finding the rightest vertex of the polygon.
    private Point rightestVertex()
    {        
        Point right;
        if (_noOfVertices == 0)//No vertices added 
            right = null;
        else{
            right = new Point (_vertices[0]);
            for (int i = 1; i<_noOfVertices; i++)
            {                
                if (_vertices[i].isRight(right))
                    right = new Point (_vertices[i]);                        
            }
        }
        return right;
    }    

    //Finding the lowest vertex of the polygon.
    private Point lowestVertex()
    {        
        Point low;
        if (_noOfVertices == 0)//No vertices added 
            low = null;
        else{
            low = new Point (_vertices[0]);
            for (int i = 1; i<_noOfVertices; i++)
            {                
                if (_vertices[i].isUnder(low))
                    low = new Point (_vertices[i]);                        
            }
        }
        return low;
    }

    /**
     * Finding what is the rectangle (polygon object) who is confining the given polygon.
     * @return The next rectangle (polygon object) that is confining the given polygon.
     *  */
    public Polygon getBoundingBox()
    {
        Polygon box = null;//The default value in case there is no vertices added 
        double left,right, high, low;
        if(calcArea() != 0){//Checks if there are more then 2 vertices and it is not a line 
            box = new Polygon();
            //Left and right will effect the horizontal values of the points
            left = leftestVertex().getX();
            right = rightestVertex().getX();
            //High and low will effect the vertical values of the points
            high = highestVertex().getY();
            low = lowestVertex().getY();

            box.addVertex(left,low);
            box.addVertex(right,low);
            box.addVertex(right,high);
            box.addVertex(left,high);
        }

        return box;
    }
}//End of class Polygon
