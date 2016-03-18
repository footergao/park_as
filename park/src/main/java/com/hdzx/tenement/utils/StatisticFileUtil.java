package com.hdzx.tenement.utils;

/**
 * 
 */

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jesley
 *
 */
public class StatisticFileUtil
{
    public static void writeLogtoFile(String path, String data)
    {
        PrintWriter pw = null;
        File file = new File(path);
        try
        {
            try
            {
                if(file.exists())
                {
                    
                }
                else
                {
                    file.createNewFile();
                }
                
                pw = new PrintWriter(new FileOutputStream(file, true), true);
                pw.println(data);
                pw.flush();
            }
            finally
            {
                if (pw != null)
                {
                    pw.close();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static List<Map<String, String>> readLogFromFile(String path)
    {
        List<Map<String, String>> LogList = null;
        BufferedReader sr = null;
        try
        {
            try
            {
                sr = new BufferedReader(new FileReader(path));
                String json = null;
                Gson gson = new Gson();
                while ((json = sr.readLine()) != null)
                {
                    try
                    {
                        Map<String, String> map = gson.fromJson(json, Map.class);
                        if (LogList == null)
                        {
                            LogList = new ArrayList<Map<String, String>>();
                        }
                        LogList.add(map);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            
            finally
            {
                if (sr != null)
                {
                    sr.close();
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return LogList;
    }
    
    public static String readLogJsonArrayFromFile(String path)
    {
        BufferedReader sr = null;
        String jsonString = null;
        try
        {
            try
            {
                sr = new BufferedReader(new FileReader(path));
                String json = null;
                while ((json = sr.readLine()) != null)
                {
                    try
                    {
                        if (jsonString == null)
                        {
                            jsonString = "[";
                        }
                        else
                        {
                            jsonString = jsonString + ",";
                        }
                        
                        jsonString = jsonString + json;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                
                if (jsonString != null)
                {
                    jsonString = jsonString + "]";
                }
            }
            
            finally
            {
                if (sr != null)
                {
                    sr.close();
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return jsonString;
    }
    
    public static String getFilePath(String dirPath, String file)
    {
        String path = null;
        
        if (dirPath.endsWith(File.separator))
        {
            path = dirPath + file;
        }
        else
        {
            path = dirPath + File.separator + file;
        }
        
        return path;
    }
}
