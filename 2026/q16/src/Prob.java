import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.magicwerk.brownies.collections.BigList;


public class Prob
{

  public static void main(String args[]) throws Exception
  {
    new Prob();
  }
  public Prob()
    throws Exception
  {
    for(File f : new File(System.getProperty("user.dir")).listFiles())
    {
      if (f.getName().endsWith(".p1"))
      {
        System.out.println("Part 1 - " + f.getName() + ":");
        System.out.println(Part1(new Scanner(new FileInputStream(f))));
      }
    }

    for(File f : new File(System.getProperty("user.dir")).listFiles())
    {
      if (f.getName().endsWith(".p2"))
      {
        System.out.println("Part 2 - " + f.getName() + ":");
        System.out.println(Part2(new Scanner(new FileInputStream(f))));
      }
    }

    for(File f : new File(System.getProperty("user.dir")).listFiles())
    {
      if (f.getName().endsWith(".p3"))
      {
        System.out.println("Part 3 - " + f.getName() + ":");
        System.out.println(Part3(new Scanner(new FileInputStream(f))));
      }

    }

  }

  Random rnd=new Random();


  int doSort(BigList<Character> library)
  {
    int scan_idx = 0;

    while(scan_idx < library.size())
    {
      char z = library.get(scan_idx);
      while((scan_idx + 1 < library.size()) && (library.get(scan_idx+1) == z))
      {
        scan_idx++;
      }
      if (library.get(scan_idx) != z) E.er();

      int last_idx = library.lastIndexOf(z);
      if (last_idx > scan_idx)
      {
        // We have sorting to do

        int start = scan_idx +1;
        int swaps = (last_idx - scan_idx) / 2;

        //System.out.println("Moving " + z + " at " + start + " swaps " + swaps + " " + last_idx);
        int ops = last_idx - scan_idx;

        library.reverse(start, last_idx-start+1);

        return ops;

      }

      scan_idx++;

    }

    return 0;

  }
  

  int scan_dex;
  long dogSortInside(DogList dl, TreeSet<Character> good_set)
  {
    int scan_dex = 0;

    while(scan_dex < dl.size())
    {
      char z = dl.get(scan_dex);
      while((scan_dex + 1 < dl.size()) && (dl.get(scan_dex+1) == z))
      {
        scan_dex++;
      }
      if (dl.get(scan_dex) != z) E.er();

      if (!good_set.contains(z))
      {
        int last_idx = dl.lastIndexOf(z);
        if (last_idx > scan_dex)
        {
          // We have sorting to do
          int start = scan_dex + 1;

          int len = last_idx-start+1;
          //System.out.println("Moving " + z + " at " + start + " to " + last_idx + " len: " + len);
          int ops = last_idx - scan_dex;

          //System.out.println("  Before: " + dl);
          dl.reverse(start, len);
          //System.out.println("   After: " + dl);

          return ops;
        }
        else
        {
          good_set.add(z);
        }
      }
      scan_dex++;
    }
    return 0;
  }

  long dogSort(BigList<Character> library)
  {
    long ops = 0;
    DogList dl = new DogList(library);

    TreeSet<Character> good_set=new TreeSet<>();
    
    scan_dex=0;

    while(true)
    {
      long v = 
        dogSortInside(dl, good_set);
      if (v == 0) break;
      //System.out.println("dogsort: " + v);
      if (dl.list_start.size() % 1000 == 0) System.out.println(dl.list_start.size());
      ops += v;

    }

    return ops;
  }

  public class DogList
  {
    BigList<Character> list_start = new BigList<Character>();
    BigList<Character> chew_toy = new BigList<Character>();
    BigList<Character> list_end = new BigList<Character>();
    int chew_order=0;

    public DogList(List<Character> input)
    {
      list_start.addAll(input);

    }

    public int lastIndexOf(char z)
    {
      int last = list_end.lastIndexOf(z);
      if (last >= 0)
      {
        return last + list_start.size() + chew_toy.size();
      }
      if (chew_order % 2 == 0)
      {
        last = chew_toy.lastIndexOf(z);
        if (last >= 0)
        {
          return last + list_start.size();
        }
      }
      else
      {
        //System.out.println("MEEM");
        last = chew_toy.indexOf(z);
        if (last >= 0)
        {
          return list_start.size() + (chew_toy.size() - last - 1);
        }
      }
      return list_start.lastIndexOf(z);
    }
    public char get(int idx)
    {
      if (idx < 0) E.er();
      if (idx < list_start.size()) return list_start.get(idx);
      if (idx < (list_start.size() + chew_toy.size()))
      {
        idx -= list_start.size();
        if (chew_order % 2 == 0)
        {
          return chew_toy.get(idx);
        }
        else
        { 
          return chew_toy.get( chew_toy.size() - 1 - idx); 
        }
      }
      idx -= list_start.size();
      idx -= chew_toy.size();
      return list_end.get(idx);
    }
    public long size()
    {
      return list_start.size() + chew_toy.size() + list_end.size();
    }

    public void reverse(int idx, int len)
    {
      while (list_start.size() > idx)
      {
        char z = list_start.pollLast();
        if (chew_order % 2 == 0)
        {
          chew_toy.addFirst(z);
        }
        else
        {
          chew_toy.addLast(z);
        }
      }
      while (list_start.size() < idx)
      {
        char z;
        if (chew_toy.size() > 0)
        {
          if (chew_order % 2 == 0)
          {
            z = chew_toy.pollFirst();
          }
          else
          {
            z = chew_toy.pollLast();
          }
        }
        else
        {
          z = list_end.pollFirst();
        }
        list_start.addLast(z);
      }


      while (chew_toy.size() < len)
      {
        char z = list_end.pollFirst();
        if (chew_order % 2 == 0)
        {
          chew_toy.addLast(z);
        }
        else
        {
          chew_toy.addFirst(z);
        }

      }
      while (chew_toy.size() > len)
      {
        char z;
        if (chew_order % 2 == 0)
        {
          z = chew_toy.pollLast();
        }
        else
        {
          z = chew_toy.pollFirst();
        }
        list_end.addFirst(z);
      }
      chew_order++;
    }

    public String toString()
    {
      BigList<Character> inside = new BigList<>();
      inside.addAll(chew_toy);
      if (chew_order % 2 == 1) inside.reverse();

      return Str.listtos(list_start) + "/" + Str.listtos(inside) + "/" + Str.listtos(list_end);

    }
  }

  public String Part1(Scanner scan)
  {
    BigList<Character> library = new BigList<>();

    library.addAll(Str.stolist(scan.nextLine()));

    int ops = 0;
    while(doSort(library) > 0)
    {
      ops++;
      //String line = Str.listtos(library);
      //System.out.println("int: " + line);
    }
    String line = Str.listtos(library);
    System.out.println("fin: " + line);


    return "" + ops;
  }

  public String Part2(Scanner scan)
  {
    BigList<Character> library = new BigList<>();
    String input = scan.nextLine();
    for(int i=0; i<10; i++)
    {
      library.addAll(Str.stolist(input));
    }

    //String int_line = Str.listtos(library);
    //System.out.println("int: " + int_line);
    int ops = 0;

    while(true)
    {
      int new_ops = doSort(library);
      ops+=new_ops;

      if (new_ops == 0) break;

    }

    //String line = Str.listtos(library);
    //System.out.println("fin: " + line);

    return "" + ops;
  }

  public String Part3(Scanner scan)
  {
    testDogList();

    BigList<Character> library = new BigList<>();
    String input = scan.nextLine();
    long last = 0;
    long ops = 0;

    int j=100;
    //for(int j=1; j<=100; j++)

    {
      library.clear();
      ops=0;

      for(int i=0; i<j; i++)
      {
        library.addAll(Str.stolist(input));
      }

      System.out.println("Size: " + library.size());

      //String int_line = Str.listtos(library);
      //System.out.println("int: " + int_line);

      ops = dogSort(library);

      long diff = ops - last;
      System.out.println(String.format("Length %d - %d ops - %d diff", j, ops, diff));
      last = ops;

    }

    //String line = Str.listtos(library);
    //System.out.println("fin: " + line);

    return "" + ops;

  }

  public void testDogList()
  {
    String in="ABCDEFGH";

    DogList dl = new DogList( Str.stolist(in));

    System.out.println(dl);
    dl.reverse(3,3);
    System.out.println(dl);
    System.out.println("D: " +dl.lastIndexOf('D'));
    for(int i=0; i<dl.size(); i++)
    {
      System.out.print( dl.get(i));
    }
    System.out.println();
    dl.reverse(0,6);
    System.out.println(dl);


    //E.er();


  }

}
