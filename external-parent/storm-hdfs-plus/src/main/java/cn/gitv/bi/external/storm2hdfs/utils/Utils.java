package cn.gitv.bi.external.storm2hdfs.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.storm.utils.ShellUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Geiger Hong 2016/11/30 0030.
 */
public class Utils {
    // A singleton instance allows us to mock delegated static methods in our
    // tests by subclassing.
    private static Utils _instance = new Utils();

    /**
     * Provide an instance of this class for delegates to use.  To mock out
     * delegated methods, provide an instance of a subclass that overrides the
     * implementation of the delegated method.
     * @param u a Utils instance
     * @return the previously set instance
     */
    public static Utils setInstance(Utils u) {
        Utils oldInstance = _instance;
        _instance = u;
        return oldInstance;
    }

    public static final Logger LOG = LoggerFactory.getLogger(Utils.class);
    public static final String DEFAULT_STREAM_ID = "default";
    public static final String DEFAULT_BLOB_VERSION_SUFFIX = ".version";
    public static final String CURRENT_BLOB_SUFFIX_ID = "current";
    public static final String DEFAULT_CURRENT_BLOB_SUFFIX = "." + CURRENT_BLOB_SUFFIX_ID;

    private static ClassLoader cl = null;

    public static final boolean IS_ON_WINDOWS = "Windows_NT".equals(System.getenv("OS"));
    public static final String FILE_PATH_SEPARATOR = System.getProperty("file.separator");
    public static final String CLASS_PATH_SEPARATOR = System.getProperty("path.separator");


    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String klass) {
        try {
            return newInstance((Class<T>)Class.forName(klass));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> klass) {
        return _instance.newInstanceImpl(klass);
    }

    // Non-static impl methods exist for mocking purposes.
    public <T> T newInstanceImpl(Class<T> klass) {
        try {
            return klass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String localHostname () throws UnknownHostException {
        return _instance.localHostnameImpl();
    }

    // Non-static impl methods exist for mocking purposes.
    protected String localHostnameImpl () throws UnknownHostException {
        return InetAddress.getLocalHost().getCanonicalHostName();
    }

    public static byte[] gzip(byte[] data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream out = new GZIPOutputStream(bos);
            out.write(data);
            out.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] gunzip(byte[] data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            GZIPInputStream in = new GZIPInputStream(bis);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) >= 0) {
                bos.write(buffer, 0, len);
            }
            in.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given a Tar File as input it will untar the file in a the untar directory
     * passed as the second parameter
     * <p/>
     * This utility will untar ".tar" files and ".tar.gz","tgz" files.
     *
     * @param inFile   The tar file as input.
     * @param untarDir The untar directory where to untar the tar file.
     * @throws IOException
     */
    public static void unTar(File inFile, File untarDir) throws IOException {
        if (!untarDir.mkdirs()) {
            if (!untarDir.isDirectory()) {
                throw new IOException("Mkdirs failed to create " + untarDir);
            }
        }

        boolean gzipped = inFile.toString().endsWith("gz");
        if (isOnWindows()) {
            // Tar is not native to Windows. Use simple Java based implementation for
            // tests and simple tar archives
            unTarUsingJava(inFile, untarDir, gzipped);
        } else {
            // spawn tar utility to untar archive for full fledged unix behavior such
            // as resolving symlinks in tar archives
            unTarUsingTar(inFile, untarDir, gzipped);
        }
    }

    private static void unTarUsingTar(File inFile, File untarDir,
                                      boolean gzipped) throws IOException {
        StringBuffer untarCommand = new StringBuffer();
        if (gzipped) {
            untarCommand.append(" gzip -dc '");
            untarCommand.append(inFile.toString());
            untarCommand.append("' | (");
        }
        untarCommand.append("cd '");
        untarCommand.append(untarDir.toString());
        untarCommand.append("' ; ");
        untarCommand.append("tar -xf ");

        if (gzipped) {
            untarCommand.append(" -)");
        } else {
            untarCommand.append(inFile.toString());
        }
        String[] shellCmd = {"bash", "-c", untarCommand.toString()};
        ShellUtils.ShellCommandExecutor shexec = new ShellUtils.ShellCommandExecutor(shellCmd);
        shexec.execute();
        int exitcode = shexec.getExitCode();
        if (exitcode != 0) {
            throw new IOException("Error untarring file " + inFile +
                    ". Tar process exited with exit code " + exitcode);
        }
    }

    private static void unTarUsingJava(File inFile, File untarDir,
                                       boolean gzipped) throws IOException {
        InputStream inputStream = null;
        try {
            if (gzipped) {
                inputStream = new BufferedInputStream(new GZIPInputStream(
                        new FileInputStream(inFile)));
            } else {
                inputStream = new BufferedInputStream(new FileInputStream(inFile));
            }
            try (TarArchiveInputStream tis = new TarArchiveInputStream(inputStream)) {
                for (TarArchiveEntry entry = tis.getNextTarEntry(); entry != null; ) {
                    unpackEntries(tis, entry, untarDir);
                    entry = tis.getNextTarEntry();
                }
            }
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
        }
    }

    private static void unpackEntries(TarArchiveInputStream tis,
                                      TarArchiveEntry entry, File outputDir) throws IOException {
        if (entry.isDirectory()) {
            File subDir = new File(outputDir, entry.getName());
            if (!subDir.mkdirs() && !subDir.isDirectory()) {
                throw new IOException("Mkdirs failed to create tar internal dir "
                        + outputDir);
            }
            for (TarArchiveEntry e : entry.getDirectoryEntries()) {
                unpackEntries(tis, e, subDir);
            }
            return;
        }
        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            if (!outputFile.getParentFile().mkdirs()) {
                throw new IOException("Mkdirs failed to create tar internal dir "
                        + outputDir);
            }
        }
        int count;
        byte data[] = new byte[2048];
        BufferedOutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(outputFile));

        while ((count = tis.read(data)) != -1) {
            outputStream.write(data, 0, count);
        }
        outputStream.flush();
        outputStream.close();
    }
    public static boolean isOnWindows() {
        if (System.getenv("OS") != null) {
            return System.getenv("OS").equals("Windows_NT");
        }
        return false;
    }
}
