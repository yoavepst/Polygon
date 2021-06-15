/**
 * This class represents a point, from the 1ST quarter, in a Cartesian system values. 
 *
 * @author (Yoav Epstein)
 * @version (16/11/2020)
 */
public class Point
{
    // Instance variables
    private double _radius;
    private double _alpha;

    private final double DEFAULT_VALUE = 0;
    private final double RAD_DEG_CONVERSION = 180;
    // Constructors 
    /**
     * Constructs a new point by decoding the specified radius and angle using
     * the setted x & y values.
     * @param x The horizontal location of the point
     * @param y The vertical location of the point
     */
    public Point(double x, double y)
    {
        if (x<=DEFAULT_VALUE && y<=DEFAULT_VALUE){
            _radius = DEFAULT_VALUE;
            _alpha = radToDeg(Math.PI/2);
        }
        else if (x<=DEFAULT_VALUE){
            _radius = y;
            _alpha = radToDeg(Math.PI/2);
        }
        else if (y<=DEFAULT_VALUE){
            _radius = x;
            _alpha = radToDeg(Math.atan(DEFAULT_VALUE));
        }
        else{
            _radius = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
            _alpha = radToDeg(Math.atan(y/x));
        }
    }

    /**
     * Constructs a new point by decoding the specified radius and angle copying 
     * the values form the other point.
     * @param other The other point
     * 
     */
    public Point (Point other)
    {        
        _radius = other._radius;
        _alpha = other._alpha;        
    }

    // Converting between radians to degrees and the opposite
    private double radToDeg (double angle)
    {
        return angle*(RAD_DEG_CONVERSION/Math.PI);
    }

    private double degToRad (double angle)
    {        
        return angle*(Math.PI/RAD_DEG_CONVERSION);
    }

    // Methods
    /**
     * Gets the horizontal location of the point.
     * @return The horizontal location of the point
     */
    public double getX()
    {
        return (_radius*Math.cos(degToRad(_alpha)));
    }

    /**
     * Gets the vertical location of the point.
     * @return The vertical location of the point
     */
    public double getY()
    {
        return(_radius*Math.sin(degToRad(_alpha)));
    }

    /** 
     * Sets the horizontal location of the point.
     * @param setX The value to be set
     */
    public void setX (double num)
    {
        double tempY = getY();
        if (num>0){        
            _radius = Math.sqrt(Math.pow(num,2)+Math.pow(tempY,2));
            _alpha = radToDeg(Math.atan(tempY/num)); 
        }
        else if (num==0){
            _radius = Math.sqrt(Math.pow(num,2)+Math.pow(tempY,2));
            _alpha = radToDeg(Math.PI/2); 
        }
    }

    /** 
     * Sets the vertical location of the point.
     * @param setY The value to be set
     */
    public void setY (double num)
    {
        double tempX = getX();
        if (num>=0){        
            _radius = Math.sqrt(Math.pow(tempX,2)+Math.pow(num,2));
            _alpha = radToDeg(Math.atan(num/tempX)); 
        }        
    }

    /**
     * Returns a string that represents this point.
     * @return String that represents this point
     * in the following format:
     * (x,y)
     */
    public String toString ()
    {
        double x,y;
        x = Math.round(getX()*10000)/(double)10000;
        y = Math.round(getY()*10000)/(double)10000;
        return "("+ x + "," + y + ")";    
    }

    /** 
     * Checks if 2 points are equal (the same), using its Cartesian system values.
     * @param other The point to compare this point to
     * @return True if the points are the same
     */
    public boolean equals (Point other)
    {
        if (_alpha == other._alpha && _radius == other._radius)
            return true;
        return false;
    }

    /** 
     *  Check if this point is above other point.
     *  @param other The point to compare this point to
     *  @return True if this point is above other point
     */
    public boolean isAbove (Point other)
    {
        if (_alpha > other._alpha)
            return true;
        else if (_alpha == other._alpha && _radius > other._radius)
            return true;
        return false;
    }

    /** 
     *  Check if this point is under other point.
     *  @param other The point to compare this point to
     *  @return True if this point is under other point
     */
    public boolean isUnder (Point other)
    {
        if (other.isAbove(this))
            return true;
        return false;
    }

    /** 
     *  Check if this point is located from the left hand side of other point.
     *  @param other The point to compare this point to
     *  @return True if this point is located from the left hand side other point
     */
    boolean isLeft (Point other)
    {
        if (this.getX() < other.getX())
            return true;
        return false;
    }

    /** 
     *  Check if this point is located from the right hand side of other point.
     *  @param other The point to compare this point to
     *  @return True if this point is located from the left hand side other point
     */
    boolean isRight (Point other)
    {
        if (other.isLeft(this))
            return true;
        return false;  
    }

    /**
     * Measures the distance between two points.
     * @param p The point that this point is measured its distance to.
     * @return The distance between two points
     */
    double distance (Point p)
    {
        double r1, r2, cos;
        r1 = Math.pow (_radius,2);
        r2 = Math.pow (p._radius,2);
        cos = Math.cos(degToRad(_alpha - p._alpha));
        return Math.sqrt(r1+r2-2*_radius*p._radius*cos);//The alternative to the x,y distance calculation
    }

    /**
     * Shifting the point horizontal and vertical.
     * @param dx The horizontal shifting distance
     * @param dy The vertical shifting distance
     */
    public void move (double dx, double dy)
    {
        double x,y;
        x = getX();
        y = getY();
        if (x+dx >=0 && y+dy>=0){
            setX(x+dx);
            setY(y+dy);
        }
    }
}
