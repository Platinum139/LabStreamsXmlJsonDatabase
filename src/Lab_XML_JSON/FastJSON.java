package Lab_XML_JSON;

import java.io.*;
import com.alibaba.fastjson.JSON;

public class FastJSON implements XmlJson{

    @Override
    public void serialize(Object object, File file){
        String jsonStr = JSON.toJSONString(object);
        try {
            FileWriter writer = new FileWriter(file.getName());
            writer.write(jsonStr);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Object deserialize(Class c, File file){
        String jsonStr = "";
        try{
            FileReader reader = new FileReader(file.getName());
            int buf;
            while ((buf = reader.read()) != -1) {
                jsonStr += (char) buf;
            }
        }catch (IOException e){
            System.out.println("FileReader: " + e.getMessage());
            return null;
        }
        try{
            return JSON.parseObject(jsonStr, c);
        } catch (Exception e){
            System.out.println("FastJSON: " + e.getMessage());
            return null;
        }
    }
}
