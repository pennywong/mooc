package org.magnum.mobilecloud.video;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

/**
 * This simple VideoSvc allows clients to send HTTP POST requests with videos
 * that are stored in memory using a list. Clients can send HTTP GET requests to
 * receive a JSON listing of the videos that have been sent to the controller so
 * far. Stopping the controller will cause it to lose the history of videos that
 * have been sent to it because they are stored in memory.
 * 
 * Notice how much simpler this VideoSvc is than the original VideoServlet?
 * Spring allows us to dramatically simplify our service. Another important
 * aspect of this version is that we have defined a VideoSvcApi that provides
 * strong typing on both the client and service interface to ensure that we
 * don't send the wrong paraemters, etc.
 * 
 * @author jules
 *
 */

// Tell Spring that this class is a Controller that should
// handle certain HTTP requests for the DispatcherServlet
@Controller
public class VideoSvcController {

	// The VideoRepository that we are going to store our videos
	// in. We don't explicitly construct a VideoRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring. Our Application class has a method
	// annotated with @Bean that determines what object will end
	// up being injected into this member variable.
	//
	// Also notice that we don't even need a setter for Spring to
	// do the injection.
	//
	@Autowired
	private VideoRepository videos;

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
	@ResponseBody
	public Video addVideo(@RequestBody Video video) {
		return videos.save(video);
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
	@ResponseBody
	public Collection<Video> getVideos() {
		return Lists.newArrayList(videos.findAll());
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Video getVideo(@PathVariable("id") long id) {
		return getVideoById(id);
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
	@ResponseBody
	public void likeVideo(@PathVariable("id") long id, Principal p) {
		Video video = getVideoById(id);
		String user = p.getName();

		if (video.getLikeUsers().contains(user))
			throw new IllegalStateException();
		else {
			video.getLikeUsers().add(user);
			video.setLikes(video.getLikes() + 1);
			videos.save(video);
		}
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method = RequestMethod.POST)
	@ResponseBody
	public void unlikeVideo(@PathVariable("id") long id, Principal p) {
		Video video = getVideoById(id);
		String user = p.getName();

		if (!video.getLikeUsers().contains(user))
			throw new IllegalStateException();
		else {
			video.getLikeUsers().remove(user);
			video.setLikes(video.getLikes() - 1);
			videos.save(video);
		}
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/likedby", method = RequestMethod.GET)
	@ResponseBody
	public Collection<String> getUsersWhoLikedVideo(@PathVariable("id") long id) {
		Video video = getVideoById(id);
		return video.getLikeUsers();
	}

	private Video getVideoById(long id) {
		Video video = videos.findOne(id);
		if (video == null)
			throw new NullPointerException();

		return video;
	}

	@ExceptionHandler
	public void exception(Exception exception, HttpServletResponse response)
			throws IOException {
		if (exception instanceof NullPointerException)
			response.sendError(404);
		else if (exception instanceof IllegalStateException)
			response.sendError(400);
		else
			response.sendError(500);
	}
}
