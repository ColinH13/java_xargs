import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Supplier; 


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


        String[] inputList = sanitizedInput.trim().split(" "); // This array holds initial arguments
        // System.out.println("Arguments:"); // Test printing out arguments
           for (String str : inputList) {
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
        String baseCmd = "";
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

                default: 
                baseCmd = arg;
                System.out.println("arg parser: " + arg);
            }
        }

        if (rFlag) {
            System.out.println("length: " + inputList.length);
            if (inputList.length == 1) {
                System.out.println("Program intentionally terminated as no input was provided");
                System.exit(0);
            }
        }
        
        // Initialize base command (baseCmd)
        List <String> commandList = new ArrayList<String>();

        for (int i  = 0; i < inputList.length; ++i) {
            if (iFlag) {
                baseCmd.replace(placeholder,inputList[i]);
            }
            int endIndex;
            if (nFlag) {
                for (int j = 0; j < inputList.length; j+=maxArgs) { // TODO: Might need to remove this for loop
                    if (j + maxArgs > inputList.length) {
                        endIndex = inputList.length;
                    } else {endIndex = j + maxArgs;}
                    for (int n = j; n < endIndex; n++) {
                        System.out.print(baseCmd + " " + inputList[n]); // Tests groupings
                        commandList.add(baseCmd + " " + inputList[n]); // Add commands to commandList to be executed
                    }
                    System.out.println(""); // Test groupings, to be removed
                }
            }
            else {
                String cmd = baseCmd + baseCmd + " " + inputList[i];
            }
        }

        System.out.print("commandList: ");
        for (String arg: commandList) {
            System.out.println("arg" + arg);
        }


        




            String ex = " pb command not set"; // Shows Work in Progress
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "echo" + ex); // Default to echo cmd
            int l = 0;
            pb.command(); // Updates the command that pb will run
            pb.inheritIO(); // Make the subprocess use the same I/O as the parent process
    
            try {
                Process process = pb.start();
                process.waitFor(); // Wait for the process to finish
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
    }    
    }
