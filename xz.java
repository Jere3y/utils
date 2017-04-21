import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class xz {

	private static final int BUFFER_SIZE = 1024*500;
	private static final String ERROR_PARSE_URL = "地址解析失败，请检查后重新尝试！";
	private static final String PLEASE_INPUT_URL = "请输入下载地址！";
	private static final String ERROR_OPEN_CONNECTION = "地址链接失败，请检查地址是否正确！";
	private static final String START_CONNECTION = "开始建立连接...";
	private static final String SUCCESS_CONNECTION = "连接建立成功....";
	private static final String SUCCESS_GET_INPUTSTREAM = "文件获取成功，开始下载...";
	private static final String CREATE_FILE = "创建本地文件，保存在当前目录下，文件名为：";
    private static final String URL_TEST = "http://localhost:8080/visualcppbuildtools_full.exe";

	private static final int THREAD_COUNT = 3;

	public static void main(String[] args) {
		if (args == null || args.equals("")) {
			System.out.println(PLEASE_INPUT_URL);
		}
		xz df = new xz();
		df.start(args[0]);
	}

	private void start(String urlStr) {
        
		HttpURLConnection connection;
		URL url;
		InputStream inputStream = null;
		try {
			url = new URL(urlStr);
			System.out.println(START_CONNECTION);

			connection = (HttpURLConnection) url.openConnection();
			System.out.println(SUCCESS_CONNECTION);

			int code = connection.getResponseCode();
            System.out.println("code ---->"+code);
			if (code == 200) {
				inputStream = connection.getInputStream();
				System.out.println(SUCCESS_GET_INPUTSTREAM);

				final int length = connection.getContentLength();
				System.out.println("文件大小获取成功，大小为：" + length);

				String[] split = urlStr.split("/");
				String defFileName = split[split.length - 1];
				RandomAccessFile raf = new RandomAccessFile(defFileName, "rw");
				raf.setLength(length);
				System.out.println(CREATE_FILE + defFileName);
				raf.close();
				int partSize = length / THREAD_COUNT;
                
				for (int i = 0; i < THREAD_COUNT; i++) {
                    int startPosition, endPosition;
					startPosition = i * partSize;					
                    endPosition = (i + 1) * partSize - 1;
                    if (i == THREAD_COUNT -1) {					
						endPosition = length - 1;
					}
					new DownloadThread(url, defFileName, startPosition, endPosition, i).start();
				}
                
			}
		} catch (MalformedURLException e) {
			System.out.println(ERROR_PARSE_URL);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(ERROR_OPEN_CONNECTION);
			e.printStackTrace();
		}finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
        
	}

	class DownloadThread extends Thread {
		URL url;
		String defFileName;
		int startPosition;
		int endPosition;
		int id;

		public DownloadThread(URL url, String defFileName, int startPosition, int endPosition, int id) {
			super();
			this.url = url;
			this.defFileName = defFileName;
			this.startPosition = startPosition;
			this.endPosition = endPosition;
			this.id = id;
		}

		@Override
		public void run() {
			
			Long startTime = System.currentTimeMillis();
			
			System.out.println("线程开始id：" + id);
			HttpURLConnection connection;
			InputStream inputStream = null;
			try {

				connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);

                int code =connection.getResponseCode();
               
				if (206 == code) {

					inputStream = connection.getInputStream();

					RandomAccessFile raf = new RandomAccessFile(defFileName, "rwd");
					raf.seek(startPosition);

					System.out.println("线程" + id + "创建成功，开始下载...");

					int hasRead = -1;
					byte[] buffer = new byte[BUFFER_SIZE];
					while ((hasRead = inputStream.read(buffer)) != -1) {
						raf.write(buffer,0,hasRead);
					}
                    raf.close();
					

				}
				Long endTime = System.currentTimeMillis();
				System.out.println("线程" + id + "下载完毕，耗时："+ (endTime - startTime)/1000 +"秒");
			} catch (MalformedURLException e) {
				System.out.println("线程" + id + ERROR_PARSE_URL);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("线程" + id + ERROR_OPEN_CONNECTION);
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
	}

}
