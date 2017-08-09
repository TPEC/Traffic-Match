package tests;

import core.Data;
import core.PreWork;
import core.base.CsvWriter;
import core.module.Link;
import org.junit.Test;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static tests.Configure.trainMax;
import static tests.Configure.trainRate;

public class UnitTest {
    @Test
    public void mainTest(){
        Data data=new Data();
        try {
            data.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dataTest(){
        File file=new File("D:\\Downloads\\gy_contest_link_traveltime_training_data.txt");
        try {
            BufferedReader br=new BufferedReader(new FileReader(file));
            String l=br.readLine();
            if(l!=null && !l.isEmpty()){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pTest(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= null;
        try {
            date = sdf.parse("2016-03-01 17:56:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date.getHours());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date));
    }

    @Test
    public void preworkTest() throws IOException, ParseException {
        PreWork.cutTravelTime();
    }

    @Test
    public void trainTest() throws IOException {
        Data data=new Data();
        data.load();
//        CsvWriter writer=new CsvWriter();
//        writer.open("D:\\Downloads\\traintest.txt");
//        writer.writeLine("asdf");
//        Iterator iterator=data.getLm().entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<String,Link> entry= (Map.Entry<String, Link>) iterator.next();
//            Map<Long,testClass> l=new TreeMap<>();
//            Iterator ite=entry.getValue().time.entrySet().iterator();
//            while (ite.hasNext()){
//                Map.Entry<Long,Double> ent= (Map.Entry<Long, Double>) ite.next();
//                Date date=new Date(ent.getKey());
//                if(data.speDate.containsKey(date.getTime()-date.getTime()%(1000L*3600L*24L))){
//                    if(date.getHours()==8){
//                        long key=data.speDate.get(date.getTime() - date.getTime() % (1000L * 3600L * 24L))*30+date.getMinutes()/2;
//                        testClass tc=l.get(key);
//                        if(tc==null) {
//                            tc=new testClass();
//                            tc.id = entry.getKey();
//                            tc.time = date.getMinutes() / 2;
//                            tc.value = ent.getValue();
//                            tc.week = data.speDate.get(date.getTime() - date.getTime() % (1000L * 3600L * 24L));
//                            tc.amount=1;
//                            l.put(key,tc);
//                        }else{
//                            tc.value=(tc.value/tc.amount+ent.getValue())/(tc.amount+1);
//                            tc.amount++;
//                        }
//                    }
//                }
//            }
//            ite=l.entrySet().iterator();
//            while (ite.hasNext()) {
//                Map.Entry<Long, testClass> ent = (Map.Entry) ite.next();
//                writer.writeLine(new String[]{
//                        entry.getKey(),
//                        String.valueOf(ent.getValue().time),
//                        String.valueOf(ent.getValue().value),
//                        String.valueOf(ent.getValue().week)
//                });
//            }
//        }
//        writer.close();
        double pre=data.train(trainRate);
        for(long i=1;i<trainMax;i++) {
            System.out.print("#"+String.valueOf(i));
            double next= data.train(trainRate);
            if(next>pre){
                break;
            }
            pre=next;
        }
        data.predictResult();
        data.output();
    }

    @Test
    public void rekTest(){
        Date date=new Date(0+1000L*3600L*24L*7L-1000L*3600L);
        System.out.println(new Date(date.getTime()-((date.getTime()+1000L*3600L*8L)%(1000L*3600L*24L))));
    }

//    class testClass{
//        public String id;
//        public int time;
//        public double value;
//        public int week;
//        public int amount;
//    }
}
