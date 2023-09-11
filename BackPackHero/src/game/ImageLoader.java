package game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

public class ImageLoader {

	private final BufferedImage[] images;
	private final HashMap<String, Integer> namesToIndex;

	public static ImageLoader createLoader(String dir) throws IOException {
		List<String> list = new ArrayList<>();
		var input = Files.newDirectoryStream(Path.of(dir));
		for (var entry : input) {
			list.add(entry.getFileName().toString());
		}
		return new ImageLoader(dir, list);
	}

	private ImageLoader(String dir, List<String> pics) {
		Objects.requireNonNull(pics);

		namesToIndex = new HashMap<>();
		images = new BufferedImage[pics.size()];
		for (var i = 0; i < pics.size(); i++) {
			setImage(i, dir, pics.get(i));
		}
	}

	private void setImage(int position, String dirPath, String imagePath) {
		Objects.requireNonNull(dirPath);
		Objects.requireNonNull(imagePath);
		var path = Path.of(dirPath + "/" + imagePath);
		try (var input = Files.newInputStream(path)) {
			images[position] = ImageIO.read(input);
			namesToIndex.put(imagePath, position);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedImage image(int i) {
		Objects.checkIndex(i, images.length);
		return images[i];
	}

	public BufferedImage image(String name) {
		Objects.requireNonNull(name);
		return images[namesToIndex.get(name)];
	}

	public int size() {
		return images.length;
	}
}