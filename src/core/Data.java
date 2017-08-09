package core;

import core.base.CsvReader;
import core.base.CsvWriter;
import core.factory.LinkFactory;
import core.module.Link;
import tests.Configure;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import tests.Configure.*;

import static tests.Configure.*;

public class Data {
    Map<String,Link> lm=new TreeMap<>();
    public Map<Long,Integer> speDate=new TreeMap<>();

    public Data load() throws IOException {
        long spentTime= System.currentTimeMillis();

        System.out.println("Loading link info...");
        CsvReader reader=new CsvReader(Configure.PATH_LINK_INFO);
        String line=reader.nextLine();
        while(line!=null){
            Link link= LinkFactory.createLink(line);
            lm.put(link.id,link);
            line=reader.nextLine();
        }
        reader.close();

        System.out.println("Loading link top...");
        reader.open(Configure.PATH_LINK_TOP);
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
            entry.getValue().loadData(Configure.PATH_FOLDER_TRAVELTIME);
        }

        System.out.println("Loading average base...");
        reader.open(Configure.PATH_AVERAGE_TRAVELTIME);
        line=reader.nextLine();
        while (line!=null){
            String[] ls=line.split(",");
            lm.get(ls[0]).k[Integer.valueOf(ls[3])*30+Integer.valueOf(ls[1])].b=Double.valueOf(ls[2])*0.9;
            line=reader.nextLine();
        }
        reader.close();

        speDate.put(new Date(116,3,4,8,0,0).getTime(),7);
        speDate.put(new Date(116,4,2,8,0,0).getTime(),7);
        speDate.put(new Date(116,5,9,8,0,0).getTime(),7);
        speDate.put(new Date(116,5,10,8,0,0).getTime(),6);
        speDate.put(new Date(116,5,12,8,0,0).getTime(),4);

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

    public double train(double fx){
        long spentTime=System.currentTimeMillis();
        System.out.println("Training...");

        List<Long> timeList=new ArrayList<>();
        Date date=new Date(116,2,8,8,0,0);
        for(long day=0;day<85;day++){
            long t=date.getTime()+1000L*3600L*24L*day;
            for(long minute=0;minute<60;minute+=2){
                long t2=t+1000L*60*minute;
                timeList.add(t2);
            }
        }
        double chaju=0;
        Random rnd=new Random();
        for(int i=0;i<timeList.size();i++) {
            date=new Date(timeList.get(i));
            Iterator iterator = lm.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Link> entry = (Map.Entry<String, Link>) iterator.next();
                Link link=entry.getValue();
                int ki=date.getDay() * 30 + date.getMinutes() / 2;
                if(speDate.containsKey(date.getTime()-date.getTime()%(1000L*3600L*24L))){
                    ki=speDate.get(date.getTime()-date.getTime()%(1000L*3600L*24L))*30+date.getMinutes()/2;
                }
                double value=link.getTravelTime(date.getTime());
                if(value>0) {
                    double pre2Min = link.getTravelTime(date.getTime() - 1000L * 120L);
                    double preDay = link.getTravelTime(date.getTime() - 1000L * 3600L * 24L);
                    double preWeek = link.getTravelTime(date.getTime() - 1000L * 3600L * 24L * 7L);
                    double[] preLink=new double[link.in.length];
                    for(int j=0;j<preLink.length;j++){
                        long deltT= (long) ((link.length + link.in[j].length) * kLength);
                        if(deltT<1000L*120L)
                            deltT=1000L*120L;
                        preLink[j] = link.in[j].getTravelTime(date.getTime() - deltT);
                    }
                    KModel k=link.k[ki];
                    double v = k.calcV(pre2Min, preDay, preWeek, preLink);
                    chaju+=Math.abs(v-value)/(value);
                    double rate;
                    if(v==0)
                        rate=0;
                    else
                        rate=1-value/v;
                    if(rate>0)
                        k.b*=1-rate*0.2*fx;
                    if(pre2Min!=0)
                        k.k[0]*=1-rate*kMinute*fx;
                    if(preDay!=0)
                        k.k[1]*=1-rate*kDay*fx;
                    if(preWeek!=0)
                        k.k[2]*=1-rate*kWeek*fx;
                    for(int j=0;j<preLink.length;j++){
                        if(preLink[j]!=0)
                            k.k[3+j]*=1-rate*kIn*fx;
                    }
                }
            }
        }
        chaju/=132*timeList.size();
        System.out.println("Chaju:"+ String.valueOf(chaju));

        spentTime=System.currentTimeMillis()-spentTime;
        System.out.println("Training finished in "+spentTime+"ms");

        return chaju;
    }

    public void predictResult(){
        long spentTime=System.currentTimeMillis();
        System.out.println("Predicting...");

        List<Long> timeList=new ArrayList<>();
        Date date=new Date(116,5,1,8,0,0);
        for(long day=0;day<30;day++){
            long t=date.getTime()+1000L*3600L*24L*day;
            for(long minute=0;minute<60;minute+=2){
                long t2=t+1000L*60*minute;
                timeList.add(t2);
            }
        }
        for(int i=0;i<timeList.size();i++){
            date=new Date(timeList.get(i));
            Iterator iterator = lm.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Link> entry = (Map.Entry<String, Link>) iterator.next();
                Link link=entry.getValue();
                int ki=date.getDay() * 30 + date.getMinutes() / 2;
                if(speDate.containsKey(date.getTime()-date.getTime()%(1000L*3600L*24L))){
                    ki=speDate.get(date.getTime()-date.getTime()%(1000L*3600L*24L))*30+date.getMinutes()/2;
                }
                double pre2Min = link.getTravelTime(date.getTime() - 1000L * 120L);
                double preDay = link.getTravelTime(date.getTime() - 1000L * 3600L * 24L);
                double preWeek = link.getTravelTime(date.getTime() - 1000L * 3600L * 24L * 7L);
                double[] preLink = new double[link.in.length];
                for (int j = 0; j < preLink.length; j++) {
                    long deltT= (long) ((link.length + link.in[j].length) * kLength);
                    if(deltT<1000L*120L)
                        deltT=1000L*120L;
                    preLink[j] = link.in[j].getTravelTime(date.getTime() - deltT);
                }
                KModel k = link.k[ki];
                entry.getValue().time.put(date.getTime(), k.calcV(pre2Min, preDay, preWeek, preLink));
            }
        }

        spentTime=System.currentTimeMillis()-spentTime;
        System.out.println("Predicting finished in "+spentTime+"ms");
    }

    public void output() throws IOException {
        CsvWriter writer=new CsvWriter();
        writer.open("D:\\Downloads\\myResult.csv");
        Iterator iterator=lm.entrySet().iterator();
        SimpleDateFormat sdf0=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (iterator.hasNext()){
            Map.Entry<String,Link> entry= (Map.Entry<String, Link>) iterator.next();
            Date date=new Date(116,5,1,8,0,0);
            for(int day=1;day<=30;day++) {
                date.setDate(day);
                for(int minute=0;minute<60;minute+=2) {
                    date.setMinutes(minute);
                    String[] o = new String[4];
                    o[0] = entry.getKey();
                    o[1]=sdf0.format(date);
                    o[2]="["+ sdf1.format(date)+","+sdf1.format(new Date(date.getTime()+1000L*120L))+")";
                    o[3]=String.valueOf(entry.getValue().time.get(date.getTime())/10);
                    writer.writeLine(o, "#");
                }
            }
        }
        writer.close();
    }
}
