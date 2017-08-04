package core;

import core.module.Link;

import java.util.Date;

public class KModel {
    public double b;//base
    public double[] k;
    public double v;//final

    public int tag;

    public KModel nextMin, preMin;
    public KModel nextDay, preDay;
    public KModel nextWeek, preWeek;

    public int timeIndex;
    public Link bindLink;

    public KModel() {
        k = new double[3 + bindLink.in.length];
        for (int i = 0; i < k.length; i++) {
            k[i] = 1.0;
        }
        b = 1.0;
        v = 0;
    }

    public double calcV() {
        v = b;
        v += k[0] * preMin.v;
        v += k[1] * preDay.v;
        v += k[2] * preWeek.v;
        if (timeIndex > 0) {    //2min前的上一条路
            for (int i = 0; i < bindLink.in.length; i++) {
                v += k[i + 3] * bindLink.in[i].k[timeIndex - 1].v;
            }
        }
        return v;
    }

}
