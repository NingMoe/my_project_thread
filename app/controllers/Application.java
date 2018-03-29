package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import utils.DateUtils;

import java.util.Date;

public class Application extends Controller {


    public  Result index() {
        return Results.TODO;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(DateUtils.getMonth(new Date())-2);
    }

}