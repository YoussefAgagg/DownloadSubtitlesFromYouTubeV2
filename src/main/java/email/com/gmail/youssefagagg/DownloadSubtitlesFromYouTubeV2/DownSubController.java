package email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;




import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.kiulian.downloader.YoutubeDownloader;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleObjectProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import javafx.stage.DirectoryChooser;



public class DownSubController {

	@FXML
	private HBox radioButtonContainer;

	@FXML
	private RadioButton videoRadioButton;

	@FXML
	private ToggleGroup urlKind;

	@FXML
	private RadioButton playlistRadioButton;

	@FXML
	private TextField linkTextField;

	@FXML
	private Button goButton;

	@FXML
	private TextField folderTextField;

	@FXML
	private Button browseFoldersButton;

	@FXML
	private HBox downloadButtonContainer;

	@FXML
	private CheckBox selectCheckBox;

	@FXML
	private TextField langTextField;

	@FXML
	private Label captionLangLabel;

	@FXML
	private Button downButton;

	@FXML
	private TableView<VideoInfo> table;
	@FXML
	private TableColumn<VideoInfo, CheckBox> selectedColume;

	@FXML
	private TableColumn<VideoInfo, String> TitleColume;

	@FXML
	private TableColumn<VideoInfo, String> captionLangsColume;

	@FXML
	private TextArea errorTextArea;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Label loadingLable;

	private ObservableList<VideoInfo> tableListItems = FXCollections.observableArrayList();

	private String fileDir=System.getProperty("user.home");

	private ExecutorService executorService =
			Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-1);

	private String lang="en";
	private YoutubeDownloader youtubeDownloader;

	public void initialize() 
	{
		youtubeDownloader=new YoutubeDownloader();
		folderTextField.setText(fileDir);
		constructTheTable();
		
		Tooltip tooltip=new Tooltip(
				"enter the code of the language\nand hit enter");
		tooltip.setStyle("\n"
                + "    -fx-border-color: black;\n"
                + "    -fx-border-width: 1px;\n"
                + "    -fx-font: normal bold 12pt \"Times New Roman\" ;\n"
                + "    -fx-background-color: silver;\n"
                + "    -fx-text-fill: black;\n"
                + "    -fx-background-radius: 4;\n"
                + "    -fx-border-radius: 4;\n"
                + "    -fx-opacity: 1.0;");
			
		langTextField.setTooltip(tooltip);


	}

	//add column value to the table
	private void constructTheTable() {

		table.setSelectionModel(null);
		table.setItems(tableListItems);

		DoubleBinding usedWidth = selectedColume.widthProperty().add(TitleColume.widthProperty());

		captionLangsColume.prefWidthProperty().bind(table.widthProperty().subtract(usedWidth));


		selectedColume.setCellValueFactory((arg0)-> {
			VideoInfo video = arg0.getValue();

			CheckBox checkBox = video.getCheckBox();

			checkBox.selectedProperty().setValue(video.isSelected());

			//add listener to checkbox
			checkBox.setOnAction(v->{

				video.setSelected(checkBox.isSelected());
				System.out.println(checkBox.isSelected());;
			});
			return new SimpleObjectProperty<CheckBox>(checkBox);

		}

				);

		TitleColume.setCellValueFactory( new PropertyValueFactory<>("title") );
		captionLangsColume.setCellValueFactory( new PropertyValueFactory<>("avalibleLangs") );


	}

	@FXML
	void browseButtonClicked(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
    	directoryChooser.setInitialDirectory(new File(fileDir));

    	File selectedDirectory = directoryChooser.showDialog(null);
    	if(selectedDirectory!=null) {
    		fileDir=selectedDirectory.getAbsolutePath();
    		folderTextField.setText(fileDir);
    	}
	}

	@FXML
	void downloadButtonClicked(ActionEvent event) {
		progressBar.setProgress(0);
		downLangCaption();

	}
	private void downLangCaption() {
		System.out.println(lang);
		errorTextArea.setText("");

		List<VideoInfo>list=new ArrayList<VideoInfo>();
		List<VideoInfo>cantDownload=new ArrayList<VideoInfo>();

		tableListItems.forEach(v->{
			if(v.isSelected())
				if(v.isCaptionAvalab(lang))
					list.add(v);
				else cantDownload.add(v);
		});
		if(cantDownload.size()!=0) {
			String text="";
			for(VideoInfo v:cantDownload){
				text+=v.getTitle()+" doesn't have a caption for language:"+lang+"\n";

			}
			errorTextArea.setText(text);
		}
		System.out.println(list.size());
		if(list.size()==0) {
			if(cantDownload.size()==0)
				errorTextArea.setText("please select a file to download");
		}else {

			loadingLable.setText("downloading....");
			downButton.setDisable(true);
			goButton.setDisable(true);
			Task<List<VideoInfo>>task=new DownSubTask(list,lang, fileDir);
			task.setOnSucceeded(value->{
				downButton.setDisable(false);
				goButton.setDisable(false);
				loadingLable.setText("done");
				progressBar.setProgress(100);
				String text=errorTextArea.getText();
				for(VideoInfo v:task.getValue()) {
					text+="faild to download: "+v.getTitle()+"\n";
					
				}
				errorTextArea.setText(text);
				
			});
			task.setOnFailed(value->{
				downButton.setDisable(false);
				goButton.setDisable(false);
				loadingLable.setText("done");
				errorTextArea.setText("failed to download");
				progressBar.setProgress(0);

			});
			taskExecuter(task);

		}
	}


	@FXML
	void goButtonClicked(ActionEvent event) {
		errorTextArea.setText("");
		goButton.setDisable(true);
		downloadButtonContainer.setDisable(true);
		tableListItems.clear();
		progressBar.setProgress(0);
		if(videoRadioButton.isSelected()) {

			String id=FetchDataUtility.getVideoId(linkTextField.getText());
			if(id==null) {
				errorTextArea.setText("enter valid url");
				goButton.setDisable(false);
				
			}else {
				
				getVideoInfoAndAddToTable(id,1,1);
				
			}

		}else {

			String listId=FetchDataUtility.getPlayListId(linkTextField.getText());
			if(listId==null) {
				errorTextArea.setText("enter valid url");
				goButton.setDisable(false);
			}else {
				
				Task<List<String>>task=new GetPlaylistIDsTask(listId,youtubeDownloader);
				
				task.setOnRunning(value->{
					loadingLable.setText("loading...");
					
				});
				task.setOnSucceeded(value->{
					goButton.setDisable(false);
					
					
					List<String>l=task.getValue();
					if(l.size()==0) {
						errorTextArea.setText("unvalid url");
					}
					int index=1;
					for(var id:l){
						getVideoInfoAndAddToTable(id,index++,l.size());
						
					}
					
					loadingLable.setText("done");
					
				});
				task.setOnFailed(value->{
					goButton.setDisable(false);
					errorTextArea.setText(value.toString());
				});
				taskExecuter(task);

			}

		}



	}
	

	private void getVideoInfoAndAddToTable(String id,int index,int size) {
	
		Task<VideoInfo>task=new GetVideoInfoTask(id,youtubeDownloader);
		task.setOnRunning(value->{
			loadingLable.setText(index+"/"+size+" loading...");
			
		});
		task.setOnSucceeded(value->{
			goButton.setDisable(false);
			if(videoRadioButton.isSelected()) {
			downloadButtonContainer.setDisable(false);
			loadingLable.setText("done");
			}
			
			
			if(task.getValue()!=null)
				tableListItems.add(task.getValue());
			else errorTextArea.setText("unvalid url");
			
			
			if(tableListItems.size()==size) {
				downloadButtonContainer.setDisable(false);
				loadingLable.setText("done");
			
			}
		});
		task.setOnFailed(value->{
			goButton.setDisable(false);
			errorTextArea.setText("error");
		});
		taskExecuter(task);


	}
	private void taskExecuter(Task<?> task) {	
		executorService.execute(task); // start the task
	}
	@FXML
	void langTextFieldOnEnter(ActionEvent event) {
		if(!langTextField.getText().isEmpty()&&!langTextField.getText().isBlank()) {
    		lang=langTextField.getText();
    		captionLangLabel.setText("the caption language will download in "+lang);
    	}

	}

	@FXML
	void selectChecBoxChecked(ActionEvent event) {
		if(selectCheckBox.isSelected()) {
    		tableListItems.forEach(v->{
    			v.getCheckBox().setSelected(true);
    			v.setSelected(true);
    		});
    	}else {
    		tableListItems.forEach(v->{
    			v.getCheckBox().setSelected(false);
    			v.setSelected(false);
    		});
    		
    	}

	}

	public void appClosed() {
		executorService.shutdown();


	}

}
