import implementations.dm_kernel.user.JCL_FacadeImpl;
import pagerank.Simulation;
import interfaces.kernel.JCL_facade;
import interfaces.kernel.JCL_result;

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