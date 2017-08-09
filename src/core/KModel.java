package core;

import core.module.Link;

import java.util.Date;

public class KModel {
    public double b;//base
    public double[] k;
    public double v;//final

    public int tag;

    public Link bindLink;

    public KModel(Link bindLink) {
        this.bindLink=bindLink;
        k = new double[3 + bindLink.in.length];
        k[0]=0.1;
        k[1]=0.03;
        k[2]=0.03;
        for (int i = 3; i < k.length; i++) {
            k[i] = 0.04/(k.length-3);
        }
        v = 0;
    }

    public double calcV(final double pre2Min, final double preDay, final double preWeek, final double[] preLink) {
        v = b;
        v += k[0] * pre2Min;
        v += k[1] * preDay;
        v += k[2] * preWeek;
        if (preLink != null) {    //2min前的上一条路
            for (int i = 0; i < preLink.length; i++) {
                v += k[i + 3] * preLink[i];
            }
        }
        return v;
    }

}
