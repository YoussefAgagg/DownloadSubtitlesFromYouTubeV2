module email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2 {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.youtube.downloader;
	requires java.xml;
	requires java.sql;

    opens email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2 to javafx.fxml;
    exports email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;
}
