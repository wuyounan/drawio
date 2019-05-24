package test;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import com.huigou.uasp.bmp.codingrule.application.DateTimeUtil;


public class ActivitiTest {
    
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
        String [] formats = DateTimeUtil.getTimeFormat(new Date());
        for(int i = 0; i < formats.length; i++){
            System.out.println(formats[i]);
        }
    }
    
       

}
