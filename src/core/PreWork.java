package core;

import core.base.CsvReader;
import core.base.CsvWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PreWork {

    private static final SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void cutTravelTime() throws IOException, ParseException {
        Map<String,Map<Long,Integer>> tt=new HashMap<>();

        CsvReader reader=new CsvReader("D:\\Downloads\\gy_contest_link_traveltime_training_data.txt");
        String line=reader.nextLine();
        while (line!=null){
            String[] ls=line.split(";");
            Map<Long,Integer> t=tt.get(ls[0]);
            if(t==null){
                t=new TreeMap<>();
                tt.put(ls[0],t);
            }
            Date date=dateFormat.parse(ls[2].split(",")[0].substring(1));
            if(date.getHours()==8) {
                long time = date.getTime();
                double value = Double.valueOf(ls[3]) * 10;
                t.put(time, (int) value);
            }
            line=reader.nextLine();
        }
        reader.close();

        System.out.println("Read finished");

        Iterator iterator=tt.entrySet().iterator();
        CsvWriter writer=new CsvWriter();
        while (iterator.hasNext()){
            Map.Entry<String,Map<Long,Integer>> entry= (Map.Entry) iterator.next();
            System.out.println("Write " +entry.getKey());
            writer.open("D:\\Downloads\\out\\"+entry.getKey()+".csv");
            writer.writeLine("time,value");
            Iterator it=entry.getValue().entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<Long,Integer> et=(Map.Entry) it.next();
                writer.writeLine(new String[]{String.valueOf(et.getKey()),String.valueOf(et.getValue())});
            }
            writer.close();
        }
    }
}
