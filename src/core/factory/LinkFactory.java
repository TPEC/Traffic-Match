package core.factory;

import core.module.Link;

public class LinkFactory {
    public static Link createLink(String cvsLine){
        Link l=new Link();
        String[] s=cvsLine.split(";");
        l.id=s[0];
        l.length=Integer.valueOf(s[1]);
        l.width=Integer.valueOf(s[2]);
        l.type=Integer.valueOf(s[3]);
        return l;
    }
}
