package pagerank;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Future;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;
import interfaces.kernel.JCL_result;
import pagerank.Neighbors;

public class LoadGraphInJCL {

    public static void loadInJcl(ConcurrentHashMap<String, Neighbors> graphNeighbors) {

        JCL_facade jcl = JCL_FacadeImpl.getInstance();

        // regerar o principal.jar

        File[] jar = {new File("../JCLPageRank-2.0/lib/Principal.jar")};

        System.out.println(jcl.register(jar, "LocalStorage"));

        List<Map.Entry<String, String>> devices = jcl.getDevices();

        ConcurrentHashMap<String, Neighbors>[] manyGraphNeighbors = new ConcurrentHashMap[devices.size()];
        ConcurrentHashMap<String, String>[] neighborsMap = new ConcurrentHashMap[devices.size()];

        //inicialização em paralelo
        Arrays.parallelSetAll(manyGraphNeighbors, i -> new ConcurrentHashMap<>());
        Arrays.parallelSetAll(neighborsMap, i -> new ConcurrentHashMap<>());

        ConcurrentSkipListSet<String> keys = new ConcurrentSkipListSet<>(graphNeighbors.keySet());

        for (String key : keys) {

            int hostNum = Math.abs(key.hashCode() % devices.size());

            Neighbors n = graphNeighbors.get(key);
            manyGraphNeighbors[hostNum].put(key, n);
            for (String v : n.neighbors) {
                if (graphNeighbors.get(v) != null)
                    // valor inicial do PageRank 1.0
                    neighborsMap[hostNum].put(v, "1.0:" + graphNeighbors.get(v).edgeOut);
            }

        }

        graphNeighbors.clear();

        List<Future<JCL_result>> tickets = new LinkedList<>();

        for (int i = 0; i < devices.size(); i++) {
            Object[] args = {manyGraphNeighbors[i], neighborsMap[i]};
            tickets.add(jcl.executeOnDevice(devices.get(i), "LocalStorage", args));
        }


        jcl.getAllResultBlocking(tickets);


        for (int i = 0; i < devices.size(); i++) {
            manyGraphNeighbors[i].clear();
            neighborsMap[i].clear();
        }
    }


}
