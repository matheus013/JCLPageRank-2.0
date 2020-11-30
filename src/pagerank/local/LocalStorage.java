package pagerank.local;

import java.util.concurrent.ConcurrentHashMap;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;
import pagerank.Neighbors;

public class LocalStorage {
    JCL_facade jclLambari = JCL_FacadeImpl.getInstanceLambari();


    public void execute(ConcurrentHashMap<String, Neighbors> graphNeighbor, ConcurrentHashMap<String, String> neighborsMap) {

        jclLambari.instantiateGlobalVar("localGraph", graphNeighbor);
        jclLambari.instantiateGlobalVar("localGraphNeighbors", neighborsMap);
    }

}

