
public class PointD implements Comparable<PointD>
{
  final double x;
  final double y;

  public PointD(double x, double y)
  {
    this.x = x;
    this.y = y;
  }

  public PointD(Point p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  @Override
  public String toString()
  {
    return String.format("(%f %f)", x,y);
  }

  @Override
  public int hashCode()
  {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object o)
  {
    if (o instanceof Point)
    {
      Point op = (Point) o;
      if (x != op.x) return false;
      if (y != op.y) return false;
      return true;
    }
    return false;
  }

  @Override
  public int compareTo(PointD p)
  {
    if (x < p.x) return -1;
    if (x > p.x) return 1;

    if (y < p.y) return -1;
    if (y > p.y) return 1;

    return 0;
  }

  public PointD add(Point p)
  {
    return new PointD(x + p.x, y +  p.y);
  }
  public PointD mult(double m)
  {
    return new PointD(x * m, y * m);
  }

  public double getDist(PointD p)
  {
    return Math.sqrt(getDist2(p));
  }

  public double getDist2(PointD p)
  {
    double dx=Math.abs(p.x - x); 
    double dy=Math.abs(p.y - y); 

    return dx*dx+dy*dy;

  }


}
