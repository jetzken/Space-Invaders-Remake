package assignment_JavaVersion;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageLoader {
	
	private BufferedImage image;
	
	public BufferedImage loadImage (String path) throws IOException {
		image = ImageIO.read(getClass().getResourceAsStream(path));
		return image;
	}
	
}
