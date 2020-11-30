package pagerank.local;

import pagerank.Neighbors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class LocalPageRank {

    public void execute(int iterations) throws IOException {
        AuxStorage auxStorage = new AuxStorage();

        ConcurrentHashMap<String, Neighbors> localGraph =
                (ConcurrentHashMap<String, Neighbors>) auxStorage.get_lambari("localGraph");
        ConcurrentHashMap<String, String> localGraphNeighbors =
                (ConcurrentHashMap<String, String>) auxStorage.get_lambari("localGraphNeighbors");

        BufferedWriter buffWrite = new BufferedWriter(new FileWriter("arquivo.txt"));

        for (int i = 0; i < iterations; i++) {
            for (String key : localGraph.keySet()) {
                Neighbors current_node = localGraph.get(key);
                ConcurrentSkipListSet<String> neighbors = current_node.neighbors;


                if (neighbors != null) {
                    float accumulated_PR = 0;

                    for (String neighbor : neighbors) {
                        String[] split = localGraphNeighbors.get(neighbor).split(":");
                        int links = Integer.parseInt(split[1]);
                        float current_PR = Float.parseFloat(split[0]);

                        accumulated_PR += (current_PR / links);
                        current_node.pagerank = (float) (0.15 + (0.85 * accumulated_PR));
                    }

                    localGraph.put(key, current_node);

                    if (i == iterations - 1)
                        buffWrite.append("key: ").append(key).append(" PageRank: ")
                                .append(String.valueOf(current_node.pagerank)).append(System.lineSeparator());
                }
            }


            ConcurrentSkipListSet<Neighbors> keys = localGraph.keySet().stream()
                    .map(localGraph::get).collect(Collectors.toCollection(ConcurrentSkipListSet::new));


            Object[] args = {keys};
            auxStorage.update(args);

        }
        buffWrite.close();

    }

}
