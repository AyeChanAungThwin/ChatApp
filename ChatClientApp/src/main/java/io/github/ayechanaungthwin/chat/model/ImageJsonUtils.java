package io.github.ayechanaungthwin.chat.model;

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
	
	public static String getJson(String img) {
		String path = System.getProperty("user.dir")+"\\src\\main\\resources\\images\\"+img+".png";
        File file = new File(path);
        
        String json = null;
        try {
        	//Image to byte array conversion
        	BufferedImage bufferedImage = ImageIO.read(file);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", output);
            byte[] data = output.toByteArray();
             
            json = objectMapper.writeValueAsString(data);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        return json;
	}
	
	public static BufferedImage getBufferedImage(String json) {
        BufferedImage image = null;
        try {
            byte[] get = objectMapper.readValue(json, byte[].class);
            image = ImageIO.read(new ByteArrayInputStream(get));
            ImageIO.write(image, "png", new File("outputImage.png"));
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        return image;
	}
	
	
	public static Image getImage(BufferedImage img){
	    //converting to a good type, read about types here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
	    BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
	    newImg.createGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

	    //converting the BufferedImage to an IntBuffer
	    int[] type_int_agrb = ((DataBufferInt) newImg.getRaster().getDataBuffer()).getData();
	    IntBuffer buffer = IntBuffer.wrap(type_int_agrb);

	    //converting the IntBuffer to an Image, read more about it here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
	    PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
	    PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer(newImg.getWidth(), newImg.getHeight(), buffer, pixelFormat);
	    return new WritableImage(pixelBuffer);
	}
}
