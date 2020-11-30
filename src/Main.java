import pagerank.Simulation;

public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        if (args.length == 0) {
            System.out.println("Filename not found");
            return;
        }

        Simulation simulation = new Simulation();

        simulation.run(args[0]);
    }

}