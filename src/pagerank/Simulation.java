package pagerank;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;
import interfaces.kernel.JCL_result;
import pagerank.local.LocalPageRank;
import pagerank.util.ReadInput;

import java.util.List;
import java.util.concurrent.Future;

import static pagerank.util.Log.reportPerformanceFor;


public class Simulation {
	


    public void run(String filename) {
    	System.out.println("pre_run");
        JCL_facade jcl = JCL_FacadeImpl.getInstance();
        System.out.println("pos_run");

        long time = System.currentTimeMillis();
        reportPerformanceFor("Starting loading from file", time);

        LoadGraphInJCL.loadInJcl(ReadInput.readInput(filename));
        reportPerformanceFor("graph allocation", time);


        time = System.currentTimeMillis();
        reportPerformanceFor("Starting PageRank ", time);

        List<Future<JCL_result>> tickets;
        jcl.register(LocalPageRank.class, "LocalPageRank");
        jcl.register(RemoteUpdates.class, "RemoteUpdates");

        int iterations = 2;
        Object[] args = {iterations};

        tickets = jcl.executeAll("LocalPageRank", args);
        jcl.getAllResultBlocking(tickets);

        reportPerformanceFor("Ending PageRank", time);

        jcl.destroy();
    }
}
