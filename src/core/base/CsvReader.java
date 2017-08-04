package core.base;

import java.io.*;

public class CsvReader {
    private BufferedReader br;

    public CsvReader(){
        br=null;
    }

    public CsvReader(final String path) throws IOException {
        open(path);
    }

    public void open(final String path) throws IOException {
        br=new BufferedReader(new FileReader(new File(path)));
        if(br.readLine()==null){
            throw new IOException("Empty File :"+path);
        }
    }

    public String nextLine() throws IOException{
        if(br!=null) {
            return br.readLine();
        }else{
            return null;
        }
    }

    public void close() throws IOException {
        if(br!=null){
            br.close();
        }
    }
}
