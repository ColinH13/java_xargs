import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MyXargs {

    public static void main(String[] args) {

        boolean nFlag = false;              // Determines if  n flag is used, indicates to look for maxArgs
        int maxArgs = Integer.MAX_VALUE;    // Indicates max number of arguments to pass to command at one time
        
        boolean iFlag = false;              // Determines if I flag is used, indicates to look for placeholder
        String placeholder = null;          // Indicates placeholder string to be replaced by input read from stdin
        
        boolean tFlag = false;              // Indicates to print entire command before executing
        boolean rFlag = false;              // Indicates to not run the command if no input is provided

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

        System.out.println("input = " + input);
        String sanitizedInput = input.toString().replaceAll("[;&|<>*()?$]", "");
        System.out.println("Sanitized input: " + sanitizedInput);


        String[] arguments = sanitizedInput.trim().split(" "); // This array holds initial arguments
        // System.out.println("Arguments:"); // Test printing out arguments
           for (String str : arguments) {
               System.out.println(str);
           }

        // Testing to determine the arguments after the file executable.
        int p = 0;

        for (String arg: args) {
            System.out.println("args at " + p + ": " + arg);
            p++;
        }
        

        // Determine flags and logic
        int k = 0;
        for (String arg: args) {
            k++;
            switch (arg) {
                case "-n":
                nFlag = true;
                maxArgs = Integer.parseInt(args[k + 1]); // Assigns int value of next arg to maxArgs variable.
                break;

                case "-I": 
                iFlag = true;
                placeholder = args[k + 1];
                break;

                case "-t":
                tFlag = true;
                break;

                case "-r":
                rFlag = true;
                break;

                default: System.out.println("arg parser: " + arg);
            }
        }

        




            String ex = " pb command not set"; // Shows Work in Progress
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "echo" + ex);
        
            pb.inheritIO(); // Make the subprocess use the same I/O as the parent process
    
            try {
                Process process = pb.start();
                process.waitFor(); // Wait for the process to finish
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }




    }    
    }
