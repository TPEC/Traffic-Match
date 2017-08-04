package core;

import core.base.CsvReader;
import core.factory.LinkFactory;
import core.module.Link;

import java.io.*;
import java.util.*;

public class Data {
    Map<String,Link> lm=new HashMap<>();

    public Data load() throws IOException {
        long spentTime= System.currentTimeMillis();

        System.out.println("Loading link info...");
        CsvReader reader=new CsvReader("D:\\Downloads\\gy_contest_link_info.txt");
        String line=reader.nextLine();
        while(line!=null){
            Link link= LinkFactory.createLink(line);
            lm.put(link.id,link);
            line=reader.nextLine();
        }
        reader.close();

        System.out.println("Loading link top...");
        reader.open("D:\\Downloads\\gy_contest_link_top(20170715).txt");
        line=reader.nextLine();
        while(line!=null){
            String[] s=line.split(";");
            Link link=lm.get(s[0]);
            if(!s[1].isEmpty()){
                link.in=generateLinkArray(s[1]);
            }else{
                link.in=new Link[0];
            }
            if(s.length>=3 && !s[2].isEmpty()){
                link.out=generateLinkArray(s[2]);
            }else{
                link.out=new Link[0];
            }
            line=reader.nextLine();
        }
        reader.close();

        System.out.println("Loading travel time...");
        Iterator iterator=lm.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,Link> entry= (Map.Entry) iterator.next();
            entry.getValue().loadData("D:\\Downloads\\out8");
        }

        spentTime=System.currentTimeMillis()-spentTime;
        System.out.println("Loading finished in "+String.valueOf(spentTime)+"ms");

        return this;
    }

    public Link getLink(String id){
        return lm.get(id);
    }

    public Map<String,Link> getLm(){
        return lm;
    }

    private Link[] generateLinkArray(String cvsItem){
        String[] s=cvsItem.split("#");
        Link[] r=new Link[s.length];
        for(int i=0;i<s.length;i++){
            r[i]=getLink(s[i]);
        }
        return r;
    }


    public static final double kLength=1000*10;
    public static final double kMinute=0.5;
    public static final double kDay=0.2;
    public static final double kWeek=0.1;
    public static final double kIn=0.25;
    public void train_Time(){
        List<Long> timeList=new ArrayList<>();
        for(int i=0;i<timeList.size();i++) {
            Date date=new Date(timeList.get(i));
            Iterator iterator = lm.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Link> entry = (Map.Entry<String, Link>) iterator.next();
                Link link=entry.getValue();
                double value=link.getTravelTime(date.getTime());
                if(value>0) {
                    double preMin = link.getTravelTime(date.getTime() - 1000L * 120L);
                    double preDay = link.getTravelTime(date.getTime() - 1000L * 3600L * 24L);
                    double preWeek = link.getTravelTime(date.getTime() - 1000L * 3600L * 24L * 7L);
                    double[] preLink=new double[link.in.length];
                    for(int j=0;j<preLink.length;j++){
                        preLink[j]=link.in[j].getTravelTime((long) (date.getTime()-(link.length+link.in[j].length)*kLength));
                    }
                    KModel k=link.k[date.getDay() * 30 + date.getMinutes() / 2];
                    double v = k.calcV(preMin, preDay, preWeek, preLink);
                    double rate;
                    if(v==0)
                        rate=0;
                    else
                        rate=value/v-1;
                    k.k[0]*=1+rate*kMinute;
                    k.k[1]*=1+rate*kDay;
                    k.k[2]*=1+rate*kWeek;
                    for(int j=0;j<preLink.length;j++){
                        k.k[3+j]*=1+rate*kIn;
                    }
                }
            }
        }
    }
}
