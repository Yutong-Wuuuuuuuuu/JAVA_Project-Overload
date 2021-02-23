package components;

import components.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * DESCRIPTION
 * A program that simulates the overload situation in a circuit.
 * @author Yutong Wu
 */
public class Overload {

    public static final int BAD_ARGS = 1;
    public static final int FILE_NOT_FOUND = 2;
    public static final int BAD_FILE_FORMAT = 3;
    public static final int UNKNOWN_COMPONENT = 4;
    public static final int REPEAT_NAME = 5;
    public static final int UNKNOWN_COMPONENT_TYPE = 6;
    public static final int UNKNOWN_USER_COMMAND = 7;
    public static final int UNSWITCHABLE_COMPONENT = 8;

    private static final String WHITESPACE_REGEX = "\\s+";
    private static final String[] NO_STRINGS = new String[ 0 ];

    private static final String PROMPT = "? ";
    private static Reporter Support;

    static {
        Reporter.addError(
                BAD_ARGS, "Usage: java components.Overload <configFile>" );
        Reporter.addError( FILE_NOT_FOUND, "Config file not found" );
        Reporter.addError( BAD_FILE_FORMAT, "Error in config file" );
        Reporter.addError(
                UNKNOWN_COMPONENT,
                "Reference to unknown component in config file"
        );
        Reporter.addError(
                REPEAT_NAME,
                "Component name repeated in config file"
        );
        Reporter.addError(
                UNKNOWN_COMPONENT_TYPE,
                "Reference to unknown type of component in config file"
        );
        Reporter.addError(
                UNKNOWN_USER_COMMAND,
                "Unknown user command"
        );
    }

    /**
     * Takes in a file and build a Hashmap containing the components of the circuit.
     * @param configFileName A string of the filename
     * @return A Hashmap that contains the components of the circuit.
     */
    private static HashMap<String,Component> readConfiguration(String configFileName){
        HashMap<String, Component> config = new HashMap<>();
        try(Scanner configFile = new Scanner(new File(configFileName))){
            while(configFile.hasNextLine()){
                String[] line = configFile.nextLine().split(WHITESPACE_REGEX);
                if(config.containsKey(line[1])) {
                    Support.usageError(REPEAT_NAME);
                }else if (line[0].equals("PowerSource")){
                    PowerSource s = new PowerSource(line[1]);
                    config.put(line[1], s);
                }else if(line[0].equals("CircuitBreaker")){
                    CircuitBreaker cb = new CircuitBreaker(line[1],config.get(line[2]), Integer.parseInt(line[3]));
                    config.put(line[1],cb);
                }else if(line[0].equals("Outlet")){
                    Outlet o = new Outlet(line[1],config.get(line[2]));
                    config.put(line[1],o);
                }else if(line[0].equals("Appliance")){
                    Appliance a = new Appliance(line[1],config.get(line[2]),Integer.parseInt(line[3]));
                    config.put(line[1],a);
                }else{
                    Support.usageError(UNKNOWN_COMPONENT_TYPE);

                }
            }
        }catch(FileNotFoundException fnfe){
            Support.usageError(FILE_NOT_FOUND);
        }
        return config;
    }

    /**
     * Prints out the components in the circuit nicely.
     * @param config A hashmap with all the components in the circuit
     */
    public static void display(HashMap<String, Component> config){
        for(Component c: config.values()){
            if(c instanceof PowerSource){
                c.display();
            }
        }
    }

    /**
     * Toggles a specific component of the circuit:
     * If the component is switched off: switch it on.
     * If the component is switched on: switch it off.
     * @param c A component to be toggled
     * @param config A list containing all the components.
     * @return A HashMap contaning all the components after component c is toggled.
     */
    public static HashMap<String,Component> toggle(Component c, HashMap<String, Component> config){
        if(c instanceof CircuitBreaker){
            CircuitBreaker cb = (CircuitBreaker) c;
            if(!cb.isSwitchOn()){
                cb.turnOn();
            }else{
                cb.turnOff();
            }
            config.put(cb.getName(),cb);
        }else if(c instanceof Appliance){
            Appliance app = (Appliance) c;
            if(!app.isSwitchOn()){
                app.turnOn();
            }else{
                app.turnOff();
            }
            config.put(app.getName(),app);
        }else{
            Support.usageError(UNSWITCHABLE_COMPONENT);
        }
        return config;
    }

    /**
     * Initialize the circuit after the HashMap is built. It engages the power source and the circuit breakers.
     * @param config A Hashmap contaning all the components in the circuit.
     * @return A Hashmap with the modified components.
     */
    public static HashMap<String, Component> initialize(HashMap<String, Component> config){
        for(Component c: config.values()){
            if(c instanceof PowerSource){
                System.out.println(c.toString() + ": powering up");
                c.engage();
                config.put(c.getName(),c);
            }
        }
        return config;
    }

    /**
     * Connects a new appliance to the circuit.
     * @param config A HashMap containing all the components in the circuit.
     * @param name The name of the appliance.
     * @param source The source of the appliance.
     * @param rate The rate of the appliance.
     * @return A modified HashMap with the newly added appliance.
     */
    public static HashMap<String,Component> connect(HashMap<String, Component> config, String name, String source, String rate){
        Appliance a = new Appliance(name, config.get(source), Integer.parseInt(rate));
        config.put(name, a);
        return config;
    }

    /**
     * This is the main program of the function.
     * It opens a file for configurations, and takes in input for commands.
     * @param args Command Line.
     */
    public static void main( String[] args ) {
        System.out.println( "Overload Project, CS2" );
        java.util.Scanner scanner = new Scanner(System.in);
        if(args.length < 1){
            Support.usageError(BAD_ARGS);
        }else{
            HashMap<String,Component> config = readConfiguration(args[0]);
            System.out.println(config.size() + " components created.");
            System.out.println("Starting up the main circuit(s).");
            config = initialize(config);
            while(true){
                System.out.print("?  ->");
                String input = scanner.nextLine();
                String[] command = input.split(WHITESPACE_REGEX);
                if(input.equals("quit")){
                    break;
                }else{
                    if(command[0].equals("toggle")){
                        config = toggle(config.get(command[1]),config);
                    }else if(command[0].equals("display")){
                        display(config);
                    }else if(command[0].equals("connect")){
                        if(command.length < 5){
                            Support.usageError(BAD_FILE_FORMAT);
                        }
                        config = connect(config,command[2],command[3],command[4]);
                    }else{
                        Support.usageError(UNKNOWN_USER_COMMAND);
                    }
                }
            }
        }
    }

}
