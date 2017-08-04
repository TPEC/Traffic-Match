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
        for(int i=0;i<k.length;i++){
            k[i]=new KModel();
            k[i].timeIndex =i;
            k[i].bindLink=this;
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
        return this;
    }
}
