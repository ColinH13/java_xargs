import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MyXargs {
    public static void main(String[] args) {

        int maxArgs = Integer.MAX_VALUE;
        String placeholder = null;
        boolean printOption = false;
        boolean preventEmptyInput = false;

        // Verifies there are arguments after 
        if (args.length == 0) {
            System.out.println("Usage: java MyXargs.java [-n num] [-I replace] [-t] [-r] command");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder input = new StringBuilder(); 
        String line;

        // This try/catch block determines the arguments that were piped into stdin
        try {
            if (reader.ready()) {
                line = reader.readLine();
                input.append(line).append(" "); // Append each line to input with a space separator
            }
        }   catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
        
        if (input != null) {
        String[] arguments = input.toString().trim().split(" "); // This array holds initial arguments
           // System.out.println("Arguments:"); // Test printing out arguments
           for (String str : arguments) {
               System.out.println(str);
           }
        }

        // Testing to determine the arguments after the file executable.
        int p = 0;

        for (String arg: args) {
            System.out.println("args at " + p + ": " + arg);
            p++;
        }

        


        
        
    }
}