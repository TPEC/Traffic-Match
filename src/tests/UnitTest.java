package tests;

import core.Data;
import core.PreWork;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
        for(int i=0;i<10;i++) {
            System.out.print("#"+String.valueOf(i));
            data.train();
        }
        data.predictResult();
        data.output();
    }

    @Test
    public void rekTest(){
        Random rnd=new Random();
        double k=rnd.nextDouble();
        for(int i=0;i<10;i++) {
            double vt = rnd.nextDouble() * 100-50;
            double vn = rnd.nextDouble() * 100-50;

            System.out.printf("%.3f,\t%.3f\n", vt, k * vn);
            double rate =1- vt/(vn*k);
            k *= 1 - rate * 0.5;
            System.out.println("k:"+k);
            System.out.printf("%.3f,\t%.3f\n",vt,k*vn);
            System.out.println();
        }
    }
}
