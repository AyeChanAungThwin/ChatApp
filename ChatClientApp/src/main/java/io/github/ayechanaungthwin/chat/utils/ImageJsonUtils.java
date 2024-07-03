package io.github.ayechanaungthwin.chat.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.image.Image;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class ImageJsonUtils {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static String getJson(String path) {
        File file = new File(path);
        
        String extension = path.toLowerCase().contains(".png")?"png":"jpeg";
        
        String json = null;
        try {
        	//Image to byte array conversion
        	BufferedImage bufferedImage = ImageIO.read(file);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, extension, output);
            byte[] data = output.toByteArray();
             
            json = objectMapper.writeValueAsString(data);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        return json;
	}
	
	public static BufferedImage getBufferedImage(String json) {
		String extension = json.toLowerCase().contains(".png")?"png":"jpeg";
		
        BufferedImage image = null;
        try {
            byte[] get = objectMapper.readValue(json, byte[].class);
            image = ImageIO.read(new ByteArrayInputStream(get));
            ImageIO.write(image, extension, new File("outputImage.png"));
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        return image;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Image getImage(BufferedImage img){
	    //converting to a good type, read about types here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
	    BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
	    newImg.createGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

	    //converting the BufferedImage to an IntBuffer
	    int[] type_int_agrb = ((DataBufferInt) newImg.getRaster().getDataBuffer()).getData();
	    IntBuffer buffer = IntBuffer.wrap(type_int_agrb);

	    //converting the IntBuffer to an Image, read more about it here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
	    PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
	    @SuppressWarnings("unchecked")
		PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer(newImg.getWidth(), newImg.getHeight(), buffer, pixelFormat);
	    return new WritableImage(pixelBuffer);
	}
}
