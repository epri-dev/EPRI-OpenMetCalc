package com.epri.metric_calculator.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

/**
 * Support to compress and extract
 * 
 * @author JoWookJae
 *
 */
public class ZipUtil {

	/**
	 * Size of buffer
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * Level of compression
	 */
	private static final int COMPRESSION_LEVEL = 8;

	/**
	 * Compress to single zip file
	 * 
	 * @param sourcePath
	 * @param output
	 * @throws IOException
	 */
	public static File zip(String sourcePath) throws IOException {
		File sourceFile = new File(sourcePath);
		if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
			throw new FileNotFoundException();
		}

		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;
		FileOutputStream fos = null;

		try {
			File tempFile = File.createTempFile(Long.toString(System.currentTimeMillis()), ".tmp");
			// When MetCalc exit, delete temp file.
			tempFile.deleteOnExit();

			fos = new FileOutputStream(tempFile);
			bos = new BufferedOutputStream(fos);
			zos = new ZipOutputStream(bos);
			zos.setLevel(COMPRESSION_LEVEL);
			zipEntry(sourceFile, sourcePath, zos);
			zos.finish();
			return tempFile;
		} finally {
			if (zos != null) {
				zos.close();
			}
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * Compress entry
	 * 
	 * @param sourceFile
	 * @param sourcePath
	 * @param zos
	 * @throws IOException
	 */
	private static void zipEntry(File sourceFile, String sourcePath, ZipOutputStream zos) throws IOException {
		if (sourceFile.isDirectory()) {
			if (sourceFile.getName().equalsIgnoreCase(".metadata")) {
				return;
			}
			File[] fileArray = sourceFile.listFiles();
			for (int i = 0; i < fileArray.length; i++) {
				zipEntry(fileArray[i], sourcePath, zos);
			}
		} else {
			BufferedInputStream bis = null;
			try {
				String sFilePath = sourceFile.getPath();
				String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());

				bis = new BufferedInputStream(new FileInputStream(sourceFile));
				ZipEntry zentry = new ZipEntry(zipEntryName);
				zentry.setTime(sourceFile.lastModified());
				zos.putNextEntry(zentry);

				byte[] buffer = new byte[BUFFER_SIZE];
				int cnt = 0;
				while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
					zos.write(buffer, 0, cnt);
				}
				zos.closeEntry();
			} finally {
				if (bis != null) {
					bis.close();
				}
			}
		}
	}

	/**
	 * Extract
	 * 
	 * @param inputStream
	 * @param targetDir
	 * @throws IOException
	 */
	public static void unzip(InputStream inputStream, File targetDir) throws IOException {
		ZipInputStream zis = null;
		ZipEntry zentry = null;

		try {
			zis = new ZipInputStream(inputStream);

			while ((zentry = zis.getNextEntry()) != null) {
				String fileNameToUnzip = zentry.getName();
				File targetFile = new File(targetDir, fileNameToUnzip);
				if (zentry.isDirectory()) {
					FileUtils.forceMkdir(targetFile);
				} else {
					if (!targetFile.getParentFile().exists()) {
						FileUtils.forceMkdir(targetFile.getParentFile());
					}

					unzipEntry(zis, targetFile);
				}
			}
		} finally {
			if (zis != null) {
				zis.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * Extract single entry
	 * 
	 * @param zis
	 * @param targetFile
	 * @return
	 * @throws Exception
	 */
	private static File unzipEntry(ZipInputStream zis, File targetFile) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFile);

			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = zis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		return targetFile;
	}
}
