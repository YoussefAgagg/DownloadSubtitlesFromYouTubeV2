package email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;

import java.util.List;
import java.util.stream.Collectors;


import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.response.Response;

import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import javafx.concurrent.Task;
import java.sql.*;
public class GetPlaylistIDsTask extends Task<List<String>> {
	private String playlistId;

	 

	public GetPlaylistIDsTask(String playlistId) {
		this.playlistId = playlistId;
	}

	@Override
	protected List<String> call() throws Exception {
		// TODO Auto-generated method stub
		List<String>ids=fetchVideosIDsByJavaYoutubeDownloderLib();
		System.out.println(ids.size());
		return ids;
	}
	private List<String>fetchVideosIDsByJavaYoutubeDownloderLib(){
		
    	
    	RequestPlaylistInfo request = new RequestPlaylistInfo(playlistId);
    	Response<PlaylistInfo> response = FetchDataUtility.downloader.getPlaylistInfo(request);
    	PlaylistInfo playlistInfo = response.data();
    	
    	List<String> list=playlistInfo.videos().stream()
    			.map(video->video.videoId())
    			.collect(Collectors.toList());
    	
		return list;
	}
	
}
