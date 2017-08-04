package tests;

import core.Data;
import core.PreWork;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    }
}
