package com.huigou.loader;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class Util {

    public static String getCatalinaHome() {
        String result = System.getProperty("catalina.home", System.getProperty("user.dir"));
        return result;
    }

    public static String getAppName() {
        String result = System.getProperty("app.name");
        return result;
    }

    public static byte[] getByteCode(String jarFileName, String classFileName) throws FileNotFoundException, IOException {
        if (jarFileName == null || "".equals(jarFileName)) {
            throw new RuntimeException("jarFileName not null or empty.");
        }

        if (classFileName == null || "".equals(classFileName)) {
            throw new RuntimeException("classFileName not null or empty.");
        }

        classFileName = classFileName + ".class";
        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarFileName));
        byte[] bytes = new byte[1024];
        ZipEntry entry;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        boolean found = false;
        String entryFileName;
        int len = 0;
        while ((entry = jarIn.getNextJarEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            entryFileName = entry.getName().replace("/", ".");
            if (entryFileName.equals(classFileName)) {
                len = jarIn.read(bytes, 0, 1024);
                while (len != -1) {
                    outStream.write(bytes, 0, len);
                    len = jarIn.read(bytes, 0, 1024);
                }
                found = true;
                break;
            }
        }

        jarIn.close();
        if (!found) {
            throw new RuntimeException(String.format("not found classFileName : %s.", classFileName));
        }
        return outStream.toByteArray();
    }

        public static String getCPUSerial() throws IOException {
        String result = "";

        File file = File.createTempFile("tmp", ".vbs");
        file.deleteOnExit();
        FileWriter fw = new java.io.FileWriter(file);
        String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n" + "Set colItems = objWMIService.ExecQuery _ \n"
                     + "   (\"Select * from Win32_Processor\") \n" + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.ProcessorId \n"
                     + "    exit for  ' do the first cpu only! \n" + "Next \n";

        fw.write(vbs);
        fw.close();
        Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = input.readLine()) != null) {
            result += line;
        }
        input.close();
        file.delete();
        return result.trim();
    }

    public static String[] getLocalMacs() throws IOException {
        HashSet<Object> localHashSet = new HashSet<Object>();
        Process localProcess = Runtime.getRuntime().exec("cmd /c %SYSTEMROOT%\\SYSTEM32\\ipconfig /all");
        try {
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
            for (String str = localBufferedReader.readLine(); str != null; str = localBufferedReader.readLine()) {
                int i = str.lastIndexOf(':');
                if (i != -1) {
                    str = str.substring(i + 1).trim();
                    if ((str.length() == 17)
                        && (str.matches("[0-9|a-f|A-F]{2}-[0-9|a-f|A-F]{2}-[0-9|a-f|A-F]{2}-[0-9|a-f|A-F]{2}-[0-9|a-f|A-F]{2}-[0-9|a-f|A-F]{2}"))) {
                        str = str.replace('-', ':');
                        localHashSet.add(str);
                    }
                }
            }
        } finally {
            localProcess.destroy();
        }
        return (String[]) localHashSet.toArray(new String[localHashSet.size()]);
    }

    public static String getLocalMacs2() throws IOException {
        String result = "";
        String[] macs = getLocalMacs();
        List<String> macList = Arrays.asList(macs);
        macList = sortStringList(macList);
        for (String item : macList) {
            result += item;
        }
        return result;
    }

    public static List<String> sortStringList(List<String> values) {
        if (values == null) {
            return null;
        }
        for (int i = 0; i < values.size() - 1; i++)
            for (int j = i + 1; j < values.size(); j++)
                if (((String) values.get(i)).compareTo((String) values.get(j)) > 0) {
                    String str = (String) values.get(i);
                    values.set(i, values.get(j));
                    values.set(j, str);
                }
        return values;
    }

}
