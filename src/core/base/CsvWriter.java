package core.base;

import java.io.*;

public class CsvWriter {
    private BufferedWriter bw;

    public CsvWriter(){
        bw =null;
    }

    public CsvWriter(final String path) throws IOException {
        open(path);
    }

    public void open(final String path) throws IOException {
        bw =new BufferedWriter(new FileWriter(new File(path)));
    }


    private String generateLine(final String[] lines, final String split){
        String line="";
        String spl=(split==null)?",":split;
        if(lines.length>0) {
            line=lines[0];
            for (int i = 1; i < lines.length; i++) {
                line+=spl+lines[i];
            }
        }
        return line;
    }

    public void writeLine(final String[] lines, final String split) throws IOException {
        writeLine(generateLine(lines,split));
    }

    public void writeLine(final String[] lines) throws IOException {
        writeLine(lines,null);
    }

    public void writeLine(final String line) throws IOException{
        if(bw !=null) {
            bw.write(line+"\n");
        }else{
            throw new IOException("file not opened.");
        }
    }

    public void close() throws IOException {
        if(bw !=null){
            bw.flush();
            bw.close();
        }
    }
}
