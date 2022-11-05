package ar.edu.itba.api.mapreduce.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class TopNCollator implements Collator<Map.Entry<String, Double>, Map<String, Double>> {
    long qtyToShow;

    public TopNCollator(long qtyToShow) {
        this.qtyToShow = qtyToShow;
    }

    @Override
    public Map<String, Double> collate(Iterable<Map.Entry<String, Double>> iterable) {
        List<Map.Entry<String, Double>> list = new ArrayList<>();
        for (Map.Entry<String, Double> entry : iterable){
            list.add(entry);
        }
        list.sort(((o1, o2) -> o2.getValue().compareTo(o1.getValue())));

        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < qtyToShow && i < list.size(); i++){
            map.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return map;
    }
}
