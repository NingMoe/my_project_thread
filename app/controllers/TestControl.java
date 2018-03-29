package controllers;

import play.mvc.Controller;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/7/10 0010.
 */
public class TestControl extends Controller{
    public static void main(String[] args) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long( System.currentTimeMillis());
        long lt1 = new Long( "319199271");
//        System.out.println("long"+(lt-lt1));
//        System.out.println("long"+(lt-lt1));
     /*   Date date = new Date(lt1);
        res = simpleDateFormat.format(date);
       System.out.println(res);
        System.out.println(System.currentTimeMillis());
*/
        double d = (double) 148*52/100;
        int i = (int)Math.round(d)+1;
        System.out.println( i);//71.04
        System.out.println( Math.round(12.9));//69.56
      //  System.out.println( Math.round());//85.84
        System.out.println( Math.round(148*51/100));//75.48
        System.out.println( Math.ceil(d));//71.04


    }
}
