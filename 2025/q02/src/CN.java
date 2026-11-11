import java.util.*;

public class CN
{
  List<Long> val;
  public CN(List<Long> val)
  {
    this.val = val;

  }

  public CN(long x,long y)
  {
    val = new ArrayList<>();
    val.add(x);
    val.add(y);
  }



  public String toString()
  {
    return "" + val.toString().replace(" ","");
  }

  public static CN add(CN a, CN b)
  {
    List<Long> n = new ArrayList<>();
    for(int i=0; i<a.val.size(); i++)
    {
      n.add(a.val.get(i) + b.val.get(i));
    }
    return new CN(n);

  }
  public static CN mult(CN a, CN b)
  {
    List<Long> n = new ArrayList<>();

    long x1 = a.val.get(0);
    long y1 = a.val.get(1);

    long x2 = b.val.get(0);
    long y2 = b.val.get(1);

    n.add(x1 * x2 - y1 * y2);
    n.add( x1 * y2 + y1 * x2);

    return new CN(n);

  }
  public static CN div(CN a, CN b)
  {
    List<Long> n = new ArrayList<>();

    long x1 = a.val.get(0);
    long y1 = a.val.get(1);

    long x2 = b.val.get(0);
    long y2 = b.val.get(1);

    n.add(x1 / x2);
    n.add( y1 / y2);

    return new CN(n);

  }



}
