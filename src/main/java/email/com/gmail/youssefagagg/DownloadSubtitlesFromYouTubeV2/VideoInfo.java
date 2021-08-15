package email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import com.github.kiulian.downloader.downloader.request.RequestSubtitlesDownload;

import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import javafx.scene.control.CheckBox;

public class VideoInfo {
	private VideoDetails videoDetails;
	private CheckBox checkBox;

	private boolean selected=true;
	private List<SubtitlesInfo> subtitles;



	@SuppressWarnings("exports")
	public VideoInfo(VideoDetails details, List<SubtitlesInfo> subtitlesInfo) {
		checkBox=new CheckBox();


		// video details
		videoDetails =details;
		subtitles=subtitlesInfo;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return videoDetails.title();
	}


	/**
	 * @return the uRLToDownloadTheCaption
	 */
	public String getURLToDownloadTheCaption(String lang) {
		var supOptional = findSubtitleInfoByLangCodeName(lang);
		if(supOptional.isPresent()) {  
			var supInfo=supOptional.get();
			RequestSubtitlesDownload requestSub = new RequestSubtitlesDownload(supInfo);
			return requestSub.getDownloadUrl();
		}

		return null;
	}



	/**
	 * @return the checkBox
	 */
	@SuppressWarnings("exports")
	public CheckBox getCheckBox() {
		return checkBox;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	/**
	 * @return the avalibleLangs
	 */
	public String getAvalibleLangs() {
		String captions=subtitles.stream()
				.map(sub->sub.getLanguage())
				.collect(Collectors.joining(", "));

		return captions;
	}
	public boolean isCaptionAvalab(String lang) {
		var supOptional = findSubtitleInfoByLangCodeName(lang);
		return supOptional.isPresent();
	}
	private Optional<SubtitlesInfo> findSubtitleInfoByLangCodeName(String lang) {
		var supOptional=subtitles.stream()
				.filter(sup->sup.getLanguage().equalsIgnoreCase(lang))
				.findAny();
		return supOptional;
	}



}
