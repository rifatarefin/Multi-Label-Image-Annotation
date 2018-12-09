import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class script {
    public static void main(String[] args) throws FileNotFoundException {
        Set<String>set=new TreeSet<>();
        File root=new File("../../Annotation");

        for(File file:root.listFiles())
        {
            Scanner scanner=new Scanner(file);
            while (scanner.hasNext())
            {
                String s=scanner.nextLine();
                if(s.contains("<name>")==true)
                {
                    int a=s.indexOf(">");
                    int b=s.lastIndexOf("<");
                    String c=s.substring(a+1,b);
                    set.add(c);
                }

            }

        }

        int i=1;
        for (String t:set)                               //pbtxt
        {
            System.out.println("item {");
            System.out.println("  id: "+i);
            System.out.println("  name: '"+t+"'");
            System.out.println("}\n");
            i++;
        }
        System.out.println(set.size());
    }
}
