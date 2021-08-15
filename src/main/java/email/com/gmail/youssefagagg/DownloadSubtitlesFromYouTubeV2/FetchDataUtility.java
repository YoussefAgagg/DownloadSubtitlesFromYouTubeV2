package email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.kiulian.downloader.YoutubeDownloader;

public class FetchDataUtility {
	private static 	Pattern pVideoID=Pattern.compile("v=([\\w-]+)");
	private static 	Pattern pPlayListID=Pattern.compile("list=([\\w-]+)");
	@SuppressWarnings("exports")
	public static final YoutubeDownloader downloader = new YoutubeDownloader();
	public  static String getVideoId(String url) {



		Matcher m=pVideoID.matcher(url);
		if(m.find()) {
			//System.out.println(m.group(1));
			return m.group(1);
		}else {
			System.err.println("unvalid link");
			return null;
		}


	}
	public static byte[] getUrlBytes(URL url) throws IOException {

		HttpURLConnection connection= (HttpURLConnection) url.openConnection();
		try {
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			InputStream in=connection.getInputStream();
			if (connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
				throw new IOException(connection.getResponseMessage() +
						": with " +
						url);
			}
			int bytesRead=0;
			byte [] buffer=new byte[1024];
			while ((bytesRead=in.read(buffer))>0){
				out.write(buffer,0,bytesRead);
			}
			out.close();
			return out.toByteArray();

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			connection.disconnect();
		}
		return null;
	}
	public static String getPlayListId(String url) {
		Matcher m=pPlayListID.matcher(url);
		if(m.find()) {
			//System.out.println(m.group(1));
			return m.group(1);
		}else {
			System.err.println("unvalid link");
			return null;
		}
	}
}
