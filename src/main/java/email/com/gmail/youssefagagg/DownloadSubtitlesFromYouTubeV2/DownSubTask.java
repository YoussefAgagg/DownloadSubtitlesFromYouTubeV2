package email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.SAXException;
import javafx.concurrent.Task;


public class DownSubTask extends Task<List<VideoInfo>> {
	List<VideoInfo>videos;
	String lang;
	String file;
	List<VideoInfo>faildDownloadingInfos=new ArrayList<VideoInfo>();

	public DownSubTask(List<VideoInfo>v,String lang,String file) {
		this.videos=v;
		this.lang=lang;
		this.file=file;


	}
	private  void downCaption(VideoInfo video) {
		
		String fileName="["+lang+"]"+video.getTitle().replaceAll("[\\\\/:*?\"<>|]", "_");
					
		String fileDir=file+File.separator+fileName+".srt";
	//	System.out.println(fileDir);
		try {

			File f=new File(fileDir);
			if(!f.exists())f.createNewFile();
			FileWriter fw=new FileWriter(f);
			int counter=1;
			
			var rootElement=loadXMLFromString(video.getURLToDownloadTheCaption(lang)).getDocumentElement();
			
			var texts=rootElement.getChildNodes();
			for(int i=0;i<texts.getLength();i++) {
				var node=(Element)texts.item(i);
				
				var result=captionTime(node);
				
				fw.write(counter++ +"");
				fw.write(System.lineSeparator());
				fw.write(result);
				fw.write(System.lineSeparator());
				

			}

			fw.close();


		} catch ( SAXException | IOException | ParserConfigurationException  e) {
			// TODO Auto-generated catch block
			faildDownloadingInfos.add(video);
			e.printStackTrace();
		}


	}
	private  Document loadXMLFromString(String uri) throws ParserConfigurationException, SAXException, IOException 
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    return builder.parse(uri);
	}
	private  String captionTime(Element node) {
	StringBuilder result=new StringBuilder();
	var s=Double.parseDouble(node.getAttribute("start"));
	var start=time(s);
	double dur=Double.parseDouble(node.getAttribute("dur"));
	var end=time(s+dur);

	result.append(start)
			.append(" --> ")
			.append(end)
			.append(System.lineSeparator())
			.append(node.getTextContent().replace("&#39;", "'"))
			.append(System.lineSeparator());
	//	System.out.println(result);
	return result.toString();

}

	private  String time(double tt) 
	{
		String t=tt+"";
		double secondsDouble=tt;
		int seconds=(int)secondsDouble;

		int minutes=seconds/60;
		if(minutes!=0) {
			seconds=seconds%60;
		}
		int hours=minutes/60;
		if(hours!=0) {
			minutes=minutes%60;
		}

		LocalTime m=LocalTime.of(hours, minutes);
		String out=m.toString()+":";
		if(seconds>=10)
			out+=seconds+(t.indexOf(".")==-1 ? "":t.substring(t.lastIndexOf(".")));
		else
			out+="0"+seconds+(t.indexOf(".")==-1 ? "":t.substring(t.lastIndexOf(".")));


		return out;
	}

	@Override
	protected List<VideoInfo> call() throws Exception {
		// TODO Auto-generated method stub
	
		videos.forEach(v->{
			downCaption(v);
		
		});
		return faildDownloadingInfos;
	}

}
