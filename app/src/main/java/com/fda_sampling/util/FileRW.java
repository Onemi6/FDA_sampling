package com.fda_sampling.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fda_sampling.model.Task;
import com.fda_sampling.model.Tasks;
import com.fda_sampling.model.sampleEnterprise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileRW {
    private static String str_json;
    private static List<Task> list = new ArrayList<>();
    private static List<sampleEnterprise> sampleEnterprises;

    public static List<Task> readFile(String filepath) {
        try {
            InputStream is = new FileInputStream(filepath);
            InputStreamReader in = new InputStreamReader(is, "UTF-8");
            BufferedReader read = new BufferedReader(in);
            Gson gson = new Gson();
            list = gson.fromJson(read.readLine().toString(), new TypeToken<List<Tasks>>() {
            }.getType());
            read.close();
            in.close();
            is.close();
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.v("选择文件", "没找到此文件");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.v("选择文件", "文件编码错误");
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("选择文件", "文件读取失败");
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("选择文件", "文件解析失败");
        }
        return null;
    }

    public static void writerFile(String filepath, List<Task> list, Context context) {
        Gson gson = new Gson();
        str_json = gson.toJson(list);
        try {
            // MODE_PRIVATE 覆盖内容
            // MODE_APPEND 追加内容
            File file = new File(filepath);
            if (file.exists()) {
                //file.delete();
                //file.createNewFile();
                OutputStream out = new FileOutputStream(filepath);
                OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                //缓冲区
                BufferedWriter bufw = new BufferedWriter(osw);
                //写到缓冲区
                bufw.write(str_json);
                bufw.close();
                osw.close();
                out.close();
                Toast.makeText(context, "保存文件成功", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "保存文件失败", Toast.LENGTH_SHORT).show();
        }
    }


    public static List<sampleEnterprise> allSampleEnterprises(String filepath) {
        try {
            InputStream is = new FileInputStream(filepath);
            InputStreamReader in = new InputStreamReader(is, "UTF-8");
            BufferedReader read = new BufferedReader(in);
            Gson gson = new Gson();
            sampleEnterprises = gson.fromJson(read.readLine().toString(), new
                    TypeToken<List<sampleEnterprise>>() {
                    }.getType());
            read.close();
            in.close();
            is.close();
            return sampleEnterprises;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.v("选择文件", "没找到此文件");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.v("选择文件", "文件编码错误");
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("选择文件", "文件读取失败");
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("选择文件", "文件解析失败");
        }
        return null;
    }
}
