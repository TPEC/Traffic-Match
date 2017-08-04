package core;

import core.base.CsvReader;
import core.factory.LinkFactory;
import core.module.Link;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Data {
    Map<String,Link> lm=new HashMap<>();

    public void load() throws IOException {
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
}
