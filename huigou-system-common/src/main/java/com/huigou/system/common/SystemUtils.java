package com.huigou.system.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class SystemUtils {

    private static final String OS_WINDOWS = "windows";

    private static final String OS_AIX = "aix";

    private static final String CMD_CHCON = "chcon -t textrel_shlib_t ";

    private static boolean isSpecifiedOS(String osName) {
        String localOSName = System.getProperties().getProperty("os.name").toLowerCase();
        return localOSName.indexOf(osName) != -1;
    }

    public static boolean isWindows() {
        return isSpecifiedOS(OS_WINDOWS);
    }

    public static boolean isAix() {
        return isSpecifiedOS(OS_AIX);
    }

    private static String getDllFileName(String filePath, String fileName) {
        String dllFileName = filePath + "/" + fileName;
        String archModel = System.getProperty("sun.arch.data.model");
        if (SystemUtils.isWindows()) {
            filePath = filePath.replace('/', '\\');
            if (archModel.equals("64")) {
                dllFileName = String.format("%s\\%s-x64.dll", filePath, fileName);
            } else {
                dllFileName = String.format("%s\\%s.dll", filePath, fileName);
            }
        } else if (archModel.equals("64")) {
            dllFileName = String.format("%s/lib%s-linux-x64.so", filePath, fileName);
        } else {
            dllFileName = String.format("%s/lib%s-linux.so", filePath, fileName);
        }
        return dllFileName;
    }

    private static boolean isSameFile(String fileName, String normalizeFileName) {
        int i = fileName.lastIndexOf(".");
        String prefixFileName = fileName.substring(0, i) + "_";
        String extensionName = fileName.substring(i);
        return (!normalizeFileName.equals(fileName)) && (normalizeFileName.startsWith(prefixFileName)) && (normalizeFileName.endsWith(extensionName));
    }

    private static void deleteNormalizeFile(String filePath, String fileName) {
        String fullFileName = getDllFileName(filePath, fileName);
        String localFileName = new File(fullFileName).getName();
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File item : files) {
            if (item.isFile() && isSameFile(localFileName, item.getName())) {
                item.delete();
            }
        }
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private static void changeContext(String fileName) {
        if (isAix()) {
            return;
        }
        String cmd = CMD_CHCON + fileName;
        Process localProcess = null;
        try {
            localProcess = Runtime.getRuntime().exec(cmd);
        } catch (IOException localIOException) {
            throw new RuntimeException(localIOException);
        } finally {
            if (localProcess != null) {
                localProcess.destroy();
            }
        }
    }

    private static String getNormalizeFileName(String path, String fileName) {
        String dllFileName = getDllFileName(path, fileName);
        int i = dllFileName.lastIndexOf(".");
        dllFileName = dllFileName.substring(0, i) + "_" + getUUID() + dllFileName.substring(i);
        return dllFileName;
    }

    private static void copyNormalizeFile(String srcFileName, String destFileName) {
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(srcFileName);
            outputStream = new FileOutputStream(destFileName);
            byte[] buffer = new byte[5120];
            int i;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String normalizeLib(String fileName) {
        String path = SystemUtils.getCatalinaHome() + "/bin/";
        return normalizeLib(path, fileName);

    }

    private static String normalizeLib(String filePath, String fileName) {
        String tmpDirPath = System.getProperty("java.io.tmpdir") + "/dll";
        File tmpDirFile = new File(tmpDirPath);
        if (!tmpDirFile.exists() || (tmpDirFile.exists() && !tmpDirFile.isDirectory())) {
            tmpDirFile.mkdirs();
        }
        tmpDirPath = tmpDirFile.getAbsolutePath();
        deleteNormalizeFile(tmpDirPath, fileName);
        String normalizeFileName = getNormalizeFileName(tmpDirPath, fileName);
        if (normalizeFileName.endsWith(".so")) {
            changeContext(tmpDirPath);
        }
        String dllFileName = getDllFileName(filePath, fileName);
        copyNormalizeFile(dllFileName, normalizeFileName);
        return normalizeFileName;
    }

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
        if (isWindows()) {
            return getCPUSerialForWindows();
        }
        return getCPUSerialForLiunx();
    }

    public static String getCPUSerialForWindows() throws IOException {
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

    public static String[] getLocalMacsForWindows() throws IOException {
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

    private static String[] getLocalMacsForLinux() throws IOException {
        List<String> rst = new ArrayList<String>();

        try {
            Enumeration<NetworkInterface> is = NetworkInterface.getNetworkInterfaces();
            while (is.hasMoreElements()) {
                NetworkInterface i = is.nextElement();
                Enumeration<InetAddress> as = i.getInetAddresses();
                while (as.hasMoreElements()) {
                    InetAddress addr = as.nextElement();
                    byte[] mac = i.getHardwareAddress();
                    if (mac == null || mac.length == 0) continue;
                    if (!isIP(addr.getHostAddress())) continue;
                    // rst.add(addr.getHostAddress());
                    rst.add(mac(mac));
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return rst.toArray(new String[] {});
    }

    private static boolean isIP(String s) {
        String[] ss = s.split("\\.");
        return ss.length == 4;
    }

    public static String mac(byte[] mac) throws SocketException {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append(":");
            }
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString().toUpperCase();
    }

    public static String getLocalMacs2() throws IOException {
        String result = "";
        String[] macs;
        if (isWindows()) {
            macs = getLocalMacsForWindows();
        } else {
            macs = getLocalMacsForLinux();
        }
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

    private static List<String> readFile(String filename) {
        return readFile(filename, true);
    }

    private static List<String> readFile(String filename, boolean reportError) {
        if (new File(filename).exists()) {
            try {
                return Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
            } catch (IOException e) {
                if (reportError) {
                }
            }
        } else if (reportError) {
        }
        return new ArrayList<>();
    }

    public static String getCPUSerialForLiunx() {
        String[] flags = new String[0];
        List<String> cpuInfo = readFile("/proc/cpuinfo");
        String stepping = null, model = null, family = null;
        for (String line : cpuInfo) {
            String[] splitLine = line.split("\\s+:\\s");
            if (splitLine.length < 2) {
                break;
            }
            switch (splitLine[0]) {
            case "vendor_id":
                // setVendor(splitLine[1]);
                break;
            case "model name":
                // setName(splitLine[1]);
                break;
            case "flags":
                flags = splitLine[1].toLowerCase().split(" ");
                //boolean found = false;
                for (String flag : flags) {
                    if ("lm".equals(flag)) {
                        //found = true;
                        break;
                    }
                }
                // setCpu64(found);
                break;
            case "stepping":
                stepping = splitLine[1];
                // setStepping(splitLine[1]);
                break;
            case "model":
                model = splitLine[1];
                // setModel(splitLine[1]);
                break;
            case "cpu family":
                family = splitLine[1];
                // setFamily(splitLine[1]);
                break;
            default:
                // Do nothing
            }
        }
        return getProcessorID(stepping, model, family, flags);
    }

    private static List<String> runNative(String cmdToRun) {
        String[] cmd = cmdToRun.split(" ");
        return runNative(cmd);
    }

    /**
     * Executes a command on the native command line and returns the result.
     *
     * @param cmdToRunWithArgs
     *            Command to run and args, in an array
     * @return A list of Strings representing the result of the command, or
     *         empty string if the command failed
     */
    private static List<String> runNative(String[] cmdToRunWithArgs) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmdToRunWithArgs);
        } catch (SecurityException | IOException e) {
            return new ArrayList<>(0);
        }
        ArrayList<String> sa = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sa.add(line);
            }
            p.waitFor();
        } catch (InterruptedException | IOException e) {
            return new ArrayList<>(0);
        }
        return sa;
    }

    private static long parseLongOrDefault(String s, long defaultLong) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return defaultLong;
        }
    }

    private static String getProcessorID(String stepping, String model, String family, String[] flags) {
        boolean procInfo = false;
        String marker = "Processor Information";
        for (String checkLine : runNative("dmidecode -t system")) {
            if (!procInfo && checkLine.contains(marker)) {
                marker = "ID:";
                procInfo = true;
            } else if (procInfo && checkLine.contains(marker)) {
                return checkLine.split(marker)[1].trim();
            }
        }
        // If we've gotten this far, dmidecode failed. Try cpuid.
        marker = "eax=";
        for (String checkLine : runNative("cpuid -1r")) {
            if (checkLine.contains(marker) && checkLine.trim().startsWith("0x00000001")) {
                String eax = "";
                String edx = "";
                for (String register : checkLine.split("\\s+")) {
                    if (register.startsWith("eax=")) {
                        eax = register.replace("eax=0x", "");
                    } else if (register.startsWith("edx=")) {
                        edx = register.replace("edx=0x", "");
                    }
                }
                return edx + eax;
            }
        }
        // If we've gotten this far, dmidecode failed. Encode arguments
        return createProcessorID(stepping, model, family, flags);
    }

    /**
     * Creates a Processor ID by encoding the stepping, model, family, and
     * feature flags.
     * 
     * @param stepping
     * @param model
     * @param family
     * @param flags
     * @return The Processor ID string
     */
    private static String createProcessorID(String stepping, String model, String family, String[] flags) {
        long processorID = 0L;
        long steppingL = parseLongOrDefault(stepping, 0L);
        long modelL = parseLongOrDefault(model, 0L);
        long familyL = parseLongOrDefault(family, 0L);
        // 3:0 – Stepping
        processorID |= steppingL & 0xf;
        // 19:16,7:4 – Model
        processorID |= (modelL & 0x0f) << 4;
        processorID |= (modelL & 0xf0) << 16;
        // 27:20,11:8 – Family
        processorID |= (familyL & 0x0f) << 8;
        processorID |= (familyL & 0xf0) << 20;
        // 13:12 – Processor Type, assume 0
        for (String flag : flags) {
            switch (flag) {
            case "fpu":
                processorID |= 1L << 32;
                break;
            case "vme":
                processorID |= 1L << 33;
                break;
            case "de":
                processorID |= 1L << 34;
                break;
            case "pse":
                processorID |= 1L << 35;
                break;
            case "tsc":
                processorID |= 1L << 36;
                break;
            case "msr":
                processorID |= 1L << 37;
                break;
            case "pae":
                processorID |= 1L << 38;
                break;
            case "mce":
                processorID |= 1L << 39;
                break;
            case "cx8":
                processorID |= 1L << 40;
                break;
            case "apic":
                processorID |= 1L << 41;
                break;
            case "sep":
                processorID |= 1L << 43;
                break;
            case "mtrr":
                processorID |= 1L << 44;
                break;
            case "pge":
                processorID |= 1L << 45;
                break;
            case "mca":
                processorID |= 1L << 46;
                break;
            case "cmov":
                processorID |= 1L << 47;
                break;
            case "pat":
                processorID |= 1L << 48;
                break;
            case "pse-36":
                processorID |= 1L << 49;
                break;
            case "psn":
                processorID |= 1L << 50;
                break;
            case "clfsh":
                processorID |= 1L << 51;
                break;
            case "ds":
                processorID |= 1L << 53;
                break;
            case "acpi":
                processorID |= 1L << 54;
                break;
            case "mmx":
                processorID |= 1L << 55;
                break;
            case "fxsr":
                processorID |= 1L << 56;
                break;
            case "sse":
                processorID |= 1L << 57;
                break;
            case "sse2":
                processorID |= 1L << 58;
                break;
            case "ss":
                processorID |= 1L << 59;
                break;
            case "htt":
                processorID |= 1L << 60;
                break;
            case "tm":
                processorID |= 1L << 61;
                break;
            case "ia64":
                processorID |= 1L << 62;
                break;
            case "pbe":
                processorID |= 1L << 63;
                break;
            default:
                break;
            }
        }
        return String.format("%016X", processorID);
    }

}
