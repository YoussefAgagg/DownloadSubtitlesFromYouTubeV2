package email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;




import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;

import javafx.concurrent.Task;
public class GetVideoInfoTask extends Task<VideoInfo> {
	private String videoId;
	private YoutubeDownloader downloader;
	
	@SuppressWarnings("exports")
	public GetVideoInfoTask(String videoId,YoutubeDownloader downloader) {
		
		this.videoId = videoId;
		this.downloader=downloader;
	}
	

	private  VideoInfo getVideoInfo(String id)  {
		
		RequestVideoInfo request = new RequestVideoInfo(id);
		var response = downloader.getVideoInfo(request);
		var video = response.data();

			if(video!=null)
			return new VideoInfo(video.details(),video.subtitlesInfo());
			
			else return null;
		
	}


	@Override
	protected VideoInfo call()   {
		
		return getVideoInfo(videoId);
	}

}
