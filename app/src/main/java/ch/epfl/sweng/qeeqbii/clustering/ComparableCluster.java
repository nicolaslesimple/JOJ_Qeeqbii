package ch.epfl.sweng.qeeqbii.clustering;

import java.io.Serializable;
import java.util.Comparator;


public class ComparableCluster implements Comparator<ComparableCluster>, Serializable {

    private double distance;
    private ClusterTypeSecondLevel cluster;

    ComparableCluster() {
        this.distance = -999.0;
        this.cluster = null;
    }

    ComparableCluster(ClusterTypeSecondLevel cluster, double distance) {
        this.distance = distance;
        this.cluster = cluster;
    }

    public double getDistance() {
        return distance;
    }

    public ClusterTypeSecondLevel getCluster() {
        return cluster;
    }

    @Override
    public int compare(ComparableCluster x, ComparableCluster y) {
        if (x.getDistance() < y.getDistance()) {
            return -1;
        }
        else if (x.getDistance() > y.getDistance()) {
            return 1;
        }
        else {
            return 0;
        }
    }
}