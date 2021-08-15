package email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;




import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;

import javafx.concurrent.Task;
public class GetVideoInfoTask extends Task<VideoInfo> {
	private String videoId;
	
	public GetVideoInfoTask(String videoId) {
		
		this.videoId = videoId;
	}
	

	private  VideoInfo getVideoInfo(String id) throws Exception {
		
		RequestVideoInfo request = new RequestVideoInfo(id);
		var response = FetchDataUtility.downloader.getVideoInfo(request);
		var video = response.data();

			if(video!=null)
			return new VideoInfo(video.details(),video.subtitlesInfo());
			
			else return null;
		
	}


	@Override
	protected VideoInfo call() throws Exception {
		
		return getVideoInfo(videoId);
	}

}
