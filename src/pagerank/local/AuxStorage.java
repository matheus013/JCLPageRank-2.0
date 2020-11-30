package pagerank.local;

import implementations.dm_kernel.user.JCL_FacadeImpl;
import interfaces.kernel.JCL_facade;
import interfaces.kernel.JCL_result;

import java.util.List;
import java.util.concurrent.Future;

public class AuxStorage {


    private JCL_facade lambari;
    private JCL_facade pacu;

    public AuxStorage() {
        this.lambari = JCL_FacadeImpl.getInstanceLambari(); // local storage
        this.pacu = JCL_FacadeImpl.getInstance(); // global storage
    }

    public Object get_lambari(String name) {
        return lambari.getValue(name).getCorrectResult();
    }

    public void update(Object[] args) {
        List<Future<JCL_result>> tickets;
        tickets = this.pacu.executeAll("RemoteUpdates", args);

        this.pacu.getAllResultBlocking(tickets);


    }
}
