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

        List<String> baseArgs = new ArrayList<>();

        // Verifies there are arguments after
        if (args.length == 0) {
            System.out.println("Usage: java MyXargs.java [-n num] [-I replace] [-t] [-r] command");
            System.exit(0);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder input = new StringBuilder(); 
        String line;

        // This try/catch block determines the arguments that were piped into stdin
        try {
            if (reader.ready()) {
                line = reader.readLine();
                System.out.println("line: " + line);
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
        //StringBuilder baseCmd = new StringBuilder("");
        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "-n":
                nFlag = true;
                if (isInt(args[i+1])) {maxArgs = Integer.parseInt(args[i + 1]); } // Assigns int value of next arg to maxArgs variable.
                
                break;

                case "-I":
                iFlag = true;
                placeholder = args[i + 1];
                System.out.println("placeholder: " + placeholder);
                System.out.println("args[i + 1]" + args[i + 1]);
                System.out.println("blabalsdasdjhlsadjlsakdjlaskdjlsakjdlsajd");
                break;

                case "-t":
                tFlag = true;
                break;

                case "-r":
                rFlag = true;
                break;

                case "-outputFormat":
                break;

                default: 
                if (args[i].startsWith("-")) {
                    break; // Ignore unrecognized flags
                }
                if (isInt(args[i])) { break; }
                baseArgs.add(args[i]);
                //baseCmd.append(args[i]);
                //System.out.println("arg parser: " + baseCmd); // TODO: Remove Testing method
            }
        }

        System.out.println("iflag is: "+ iFlag);
        System.out.println("baseArgs: " + baseArgs);
        String baseCmd = String.join(" ", baseArgs);
        System.out.println("Base command: " + baseCmd);
        System.out.println("placeholder: " + placeholder);
        nFlag = true; // 

        if (rFlag) {
            System.out.println("length: " + inputList.length);
            if (inputList.length == 1) {
                System.out.println("Program intentionally terminated as no input was provided");
                System.exit(0);
            }
        }
        
        // Initialize base command (baseCmd)
        List <String> commandList = new ArrayList<String>();

       
       
        String finalCmd = baseCmd.toString();
        for (int i  = 0; i < inputList.length; ++i) {
            if (iFlag) {
                finalCmd = baseCmd.toString().replace(placeholder,inputList[i]);
            }
            if (nFlag) {
                    int endIndex;
                    if (i + maxArgs > inputList.length) {
                        endIndex = inputList.length;
                    } else {endIndex = i + maxArgs;}
                    StringBuilder newCmd = new StringBuilder(baseCmd);
                        System.out.println();
                        for (int j = i; j < endIndex; j++) {
                        System.out.println("appending here: ");
                        System.out.println(newCmd + " " + inputList[j]); // TODO: Tests groupings
                        newCmd.append(" ").append(inputList[j]);
                        }
                    
                    System.out.println("\nnewCmd: " + newCmd);
                    commandList.add(newCmd.toString());
                    System.out.println("Command: " + newCmd.toString());
                i += maxArgs - 1;
            }
            else {
                String cmd = baseCmd + " " + inputList[i];
                commandList.add(cmd);
                System.out.println("Command: " + cmd); // Test to print command groupings
            }
        }


        System.out.println("commandList: ");
        for (String cmd: commandList) {
            System.out.println("cmd: " + cmd);
        }

            // Executes all commands in the commandList
            String ex = " pb command not set"; // Shows Work in Progress
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "echo" + ex); // Default to echo cmd
            int l = 0;
            pb.inheritIO(); // Make the subprocess use the same I/O as the parent process

            System.out.println("Command output: "); // TODO: Remove testing print
            // For each cmd in the commandList, update the pb cmd and execute
            for (String cmd : commandList) {
                pb.command("cmd.exe", "/c", cmd);            // Updates the command that pb will run
                if (tFlag) {System.out.println(cmd);} // Outputs command before executing
            try {
                Process process = pb.start();
                process.waitFor();          // Wait for the process to finish
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }



    } // end of main method
    
    
    
    // Checks if the provided string can be converted to an integer
    public static boolean isInt(String str) {
            try {
                Integer.parseInt(str);
                return true;  
            } catch (NumberFormatException e) {
                return false; 
            }
        
    }

    } // end of main class
