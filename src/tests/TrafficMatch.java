package tests;

import core.Data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import static tests.Configure.*;

public class TrafficMatch {
    public static void main(String[] args) throws IOException {
        if (args.length>=6) {
            kLength = Double.valueOf(args[0]);
            kMinute = Double.valueOf(args[1]);
            kDay = Double.valueOf(args[2]);
            kWeek = Double.valueOf(args[3]);
            kIn = Double.valueOf(args[4]);
            trainRate = Double.valueOf(args[5]);
        }
//        System.out.printf("kLength=%f, kMinute=%f, kDay=%f, kWeek=%f, kIn=%f, trainRate=%f\n",kLength,kMinute,kDay,kWeek,kIn,trainRate);
//        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
//        System.out.print("PATH:");
//        String l=br.readLine();
        String l="/home/zhangwei/trafficMatch";
        if(l!=null && !l.isEmpty()) {
            PATH_LINK_INFO = l+"/gy_contest_link_info.txt";
            PATH_LINK_TOP=l+"/gy_contest_link_top(20170715).txt";
            PATH_FOLDER_TRAVELTIME=l+"/traveltime";
            PATH_AVERAGE_TRAVELTIME=l+"/result.csv";
            PATH_OUTPUT=l+"/output.txt";
        }

        Data data=new Data();
        data.load();
        double pre=data.train(trainRate);
        for(long i=1;i<trainMax;i++) {
            double next= data.train(trainRate);
            if(next>pre){
                System.out.println(pre);
                break;
            }
            pre=next;
        }
        data.predictResult();
        data.output();
    }
}
