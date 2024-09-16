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

        // Verifies there are arguments for the program
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
                //System.out.println("line: " + line);
                input.append(line).append(" ");     // Append each line to input with a space separator
            }
        }   catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
        
        // Sanitize args and input
        for (int i = 0; i < args.length; ++i) {
            args[i] = args[i].replaceAll("[;&|<>*()?$]", "");
        }
        String sanitizedInput = input.toString().replaceAll("[;&|<>*()?$]", "");
        //System.out.println("Sanitized input: " + sanitizedInput);
        String[] inputList = sanitizedInput.trim().split(" "); // This array holds initial arguments
        
        /*
        // System.out.println("Arguments:"); // Test printing out arguments
           for (String str : inputList) {
               System.out.println(str);
           }

        // Testing to determine the arguments after the file executable.
        for (int i = 0; i < args.length; ++i) {
            System.out.println("args at " + i + ": " + args[i]);
        }
        */

        // Determines flags and logic
        // StringBuilder baseCmd = new StringBuilder("");
        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "-n":
                nFlag = true;
                if (isInt(args[i+1])) {maxArgs = Integer.parseInt(args[i + 1]); } // Assigns int value of next arg to maxArgs variable.
                break;

                case "-I":
                iFlag = true;
                placeholder = args[i + 1];
                // System.out.println("placeholder: " + placeholder); // Tests if expected placeholder is printed
                break;

                case "-t":
                tFlag = true;
                break;

                case "-r":
                rFlag = true;
                break;

                default: 
                if (args[i].startsWith("-")) {
                    break; // Ignore unrecognized flags
                } if (args[i] == placeholder) {break;}
                if (isInt(args[i])) { break; }
                baseArgs.add(args[i]);
                //System.out.println("arg parser: " + baseCmd); // TODO: Remove Testing method
            }
        }

        String baseCmd = String.join(" ", baseArgs);
        //System.out.println("Base Command: " + baseCmd);       // Outputs command that program runs (i.e. rm, echo, etc.)
        //System.out.println("placeholder: " + placeholder);    // Outputs placeholder for -I testing
        nFlag = true; 

        if (rFlag) {
            if (inputList.length == 1) {
                //System.out.println("Program intentionally terminated as no input was provided");
                System.exit(0);
            }
        }
        
        // Initialize base command (baseCmd)
        List <String> commandList = new ArrayList<String>();

       if (maxArgs > inputList.length) {
        maxArgs = inputList.length;
       }
        String finalCmd = baseCmd.toString();
        for (int i  = 0; i < inputList.length; ++i) {
            if (iFlag) {
                int cmdCounter = 0;
                //if (nFlag && maxArgs >= inputList.length) {
                    finalCmd = baseCmd.replace(placeholder, "");
                    int endIndex;
                    if (i + maxArgs > inputList.length) {
                        endIndex = inputList.length;
                    } else {endIndex = i + maxArgs;}
                    StringBuilder newCmd = new StringBuilder(finalCmd);
                        for (int j = i; j < endIndex; j++) {
                        if (cmdCounter == 0) {newCmd.append("").append(inputList[j]);
                    }
                        else {newCmd.append(" ").append(inputList[j]);}
                        cmdCounter++;
                        }
                        cmdCounter = 0;
                    
                    commandList.add(newCmd.toString());
                i += maxArgs - 1;
                if (i == inputList.length) {i--;}
                finalCmd = baseCmd.replace(placeholder,inputList[i]);
            }
            else if (nFlag) {
                    int endIndex;
                    if (i + maxArgs > inputList.length) {
                        endIndex = inputList.length;
                    } else {endIndex = i + maxArgs;}
                    StringBuilder newCmd = new StringBuilder(finalCmd);
                        for (int j = i; j < endIndex; j++) {
                        if (!iFlag) {newCmd.append(" ");}
                        newCmd.append(inputList[j]);
                        }
                    
                    commandList.add(newCmd.toString());
                i += maxArgs - 1;
            }
            else {
                String cmd = finalCmd + " " + inputList[i];
                commandList.add(cmd);
            }
        }
        /* 
        System.out.println("commandList: ");
        for (String cmd: commandList) {
            System.out.println("cmd: " + cmd);
        } */

        for (String cmd: commandList) {
            String[] cmdArray = cmd.split(" ");
            List<String> argumentList = new ArrayList<>(Arrays.asList(cmdArray));
            ProcessBuilder pb = new ProcessBuilder(argumentList);
            pb.inheritIO();

            if (tFlag) {System.out.println("+ " + cmd);} // Outputs command before executing

            try {
                Process process = pb.start();
                process.waitFor();          // Wait for the process to finish
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        /* 
            // Executes all commands in the commandList
            String ex = " pb command not set"; // Shows Work in Progress
            ProcessBuilder pb = new ProcessBuilder("echo" + ex); // Default to echo cmd
            
            pb.inheritIO(); // Make the subprocess use the same I/O as the parent process

            System.out.println("Command output: "); // TODO: Remove testing print
            // For each cmd in the commandList, update the pb cmd and execute
            for (String cmd : commandList) {
                pb.command("cmd.exe", "/c", cmd);            // Updates the command that pb will run
                if (tFlag) {System.out.println("+ " + cmd);} // Outputs command before executing  
        }
        */
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
