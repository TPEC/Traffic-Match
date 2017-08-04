package core.module;

import core.KModel;
import core.base.CsvReader;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Link {
    public String id;
    public int length;
    public int width;
    public int type;

    public Link[] out;
    public Link[] in;

    public Map<Long,Integer> time;
    public KModel[] k;

    public Link(){
        time=new TreeMap<>();
        k=new KModel[210];
    }

    public double getTravelTime(long date){
        long ds=date-date%120000L;
        long de=ds+120000L;
        int ts=time.getOrDefault(ds,0);
        int te=time.getOrDefault(de,0);
        if(ts>0 && te>0){
            return (double)((de-date)*ts+(date-ds)*te)/120000.0;
        }else if(ts>0){
            return ts;
        }else if(te>0){
            return te;
        }else{
            return 0;
        }
    }

    public Link loadData(final String path) throws IOException {
        CsvReader reader=new CsvReader(path+"/"+id+".csv");
        String line= reader.nextLine();
        while (line!=null){
            String[] ls=line.split(",");
            time.put(Long.valueOf(ls[0]),Integer.valueOf(ls[1]));
            line=reader.nextLine();
        }
        initKModel();
        return this;
    }

    public void initKModel(){
        for(int i=0;i<k.length;i++){
            k[i]=new KModel(this);
        }
    }
}
