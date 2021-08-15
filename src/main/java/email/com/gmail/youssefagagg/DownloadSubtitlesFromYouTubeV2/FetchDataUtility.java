package email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchDataUtility {
	private static 	Pattern pVideoID=Pattern.compile("v=([\\w-]+)");
	private static 	Pattern pPlayListID=Pattern.compile("list=([\\w-]+)");

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
