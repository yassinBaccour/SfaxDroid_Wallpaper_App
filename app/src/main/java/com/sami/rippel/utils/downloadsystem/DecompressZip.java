package com.sami.rippel.utils.downloadsystem;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DecompressZip {
    private static final int BUFFER_SIZE = 8192;
    private String _zipFile;
    private String _location;
    private byte[] _buffer;

    public DecompressZip(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;
        _buffer = new byte[BUFFER_SIZE];
        dirChecker("");
    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        copyStream(is, os, buffer, BUFFER_SIZE);
    }

    public static void copyStream(InputStream is, OutputStream os,
                                  byte[] buffer, int bufferSize) throws IOException {
        try {
            for (; ; ) {
                int count = is.read(buffer, 0, bufferSize);
                if (count == -1) {
                    break;
                }
                os.write(buffer, 0, count);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public void unzip() {
        FileInputStream fin = null;
        ZipInputStream zin = null;
        OutputStream fout = null;

        File outputDir = new File(_location);
        File tmp = null;

        try {
            fin = new FileInputStream(_zipFile);
            zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.d("Decompress", "Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    dirChecker(ze.getName());
                } else {
                    tmp = File.createTempFile("decomp", ".tmp", outputDir);
                    fout = new BufferedOutputStream(new FileOutputStream(tmp));
                    copyStream(zin, fout, _buffer, BUFFER_SIZE);
                    zin.closeEntry();
                    fout.close();
                    fout = null;
                    tmp.renameTo(new File(_location + ze.getName()));
                    tmp = null;
                }
            }
            zin.close();
            zin = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tmp != null) {
                try {
                    tmp.delete();
                } catch (Exception ignore) {
                    ;
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception ignore) {
                    ;
                }
            }
            if (zin != null) {
                try {
                    zin.closeEntry();
                } catch (Exception ignore) {
                    ;
                }
            }
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception ignore) {
                    ;
                }
            }
        }
    }

    private void dirChecker(String dir) {
        File f = new File(_location + dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }
}