package org.magnum.dataup;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class VideoSvcController {
	public static final String DATA_PARAMETER = "data";
	public static final String ID_PARAMETER = "id";
	public static final String VIDEO_SVC_PATH = "/video";
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";

	private static final AtomicLong id = new AtomicLong(0L);

	private Map<Long, Video> videos = new ConcurrentHashMap<Long, Video>();

	@RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.GET)
	@ResponseBody
	public Collection<Video> getVideos() {
		return videos.values();
	}

	@RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.POST)
	@ResponseBody
	public Video addVideo(@RequestBody Video video) {
		video.setId(id.incrementAndGet());
		video.setDataUrl(getDataUrl(video.getId()));

		videos.put(video.getId(), video);

		return video;
	}

	@RequestMapping(value = VIDEO_DATA_PATH, method = RequestMethod.POST)
	@ResponseBody
	public VideoStatus setVideoData(@PathVariable(ID_PARAMETER) long id,
			@RequestParam(DATA_PARAMETER) MultipartFile videoData)
			throws IOException {
		Video video = videos.get(id);
		VideoFileManager.get().saveVideoData(video, videoData.getInputStream());
		return new VideoStatus(VideoState.READY);
	}

	@RequestMapping(value = VIDEO_DATA_PATH, method = RequestMethod.GET)
	public void getData(@PathVariable(ID_PARAMETER) long id,
			HttpServletResponse response) throws IOException {
		Video video = videos.get(id);
		VideoFileManager.get().copyVideoData(video, response.getOutputStream());
	}

	@ExceptionHandler
	public void exception(Exception exception, HttpServletResponse response)
			throws IOException {
		if (exception instanceof IOException)
			response.sendError(500);
		else
			response.sendError(404);
	}

	private String getDataUrl(long videoId) {
		String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
		return url;
	}

	private String getUrlBaseForLocalServer() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String base = "http://"
				+ request.getServerName()
				+ ((request.getServerPort() != 80) ? ":"
						+ request.getServerPort() : "");
		return base;
	}

}
