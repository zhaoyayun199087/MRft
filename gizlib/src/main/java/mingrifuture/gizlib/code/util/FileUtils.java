package mingrifuture.gizlib.code.util;

import android.os.Environment;

import com.wlt.http.client.util.WLTUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
	public static final boolean DEBUG = false;
	public static final String LOG_FILE_NAME = "mingrifuture_LOG.txt";

	private static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");

	public static void writeLogToFile1(String title, byte[] content) {

		String date = mDateFormat.format(new Date(System.currentTimeMillis()));
		if (content == null) {
			return;
		}
		String filepath = "";
		File file = new File(Environment.getExternalStorageDirectory(),
				LOG_FILE_NAME);
		if (file.exists()) {
			filepath = file.getAbsolutePath();
		} else {
			filepath = "不适用";
		}

		// 第二个参数意义是说是否以append方式添加内容
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(date + " " + title);
			String hexStr = WLTUtils.decodeBytesToHexString(content);
			if (hexStr != null)
				bw.write(WLTUtils.decodeBytesToHexString(content));
			bw.write("\r\n");
			bw.flush();
		} catch (Exception e) {

		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeLogToFile(String title, byte[] content) {
		if (!DEBUG) {
			return;
		}

		String date = mDateFormat.format(new Date(System.currentTimeMillis()));
		if (content == null) {
			return;
		}
		String filepath = "";
		File file = new File(Environment.getExternalStorageDirectory(),
				LOG_FILE_NAME);
		if (file.exists()) {
			filepath = file.getAbsolutePath();
		} else {
			filepath = "不适用";
		}

		// 第二个参数意义是说是否以append方式添加内容
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(date + " " + title);
			String hexStr = WLTUtils.decodeBytesToHexString(content);
			if (hexStr != null)
				bw.write(WLTUtils.decodeBytesToHexString(content));
			bw.write("\r\n");
			bw.flush();
		} catch (Exception e) {

		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void printExceptionToFile(Exception e, String desc) {
		if (!DEBUG) {
			return;
		}
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			// 将出错的栈信息输出到printWriter中
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					// e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
		writeLogToFile(desc + sw.toString(), new byte[] {});
	}


	/**
	 * 删除单个文件
	 *
	 * @param fileName
	 *            要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				System.out.println("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			System.out.println("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}
}
