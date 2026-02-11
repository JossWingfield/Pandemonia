package main.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Texture {

	private String filePath;
	private transient int texID;
	private int width, height;

	
	public Texture() {
		texID = -1;
		width = -1;
		height = -1;
	}
	   public Texture(int texID, int width, int height) {
	        this.texID = texID;
	        this.width = width;
	        this.height = height;
	        this.filePath = "Generated";
	    }
	
	   public Texture(int width, int height, int[] pixels) {
		    this.width = width;
		    this.height = height;

		    texID = glGenTextures();
		    glBindTexture(GL_TEXTURE_2D, texID);

		    ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

		    for (int pixel : pixels) {
		        buffer.put((byte)((pixel >> 16) & 0xFF)); // R
		        buffer.put((byte)((pixel >> 8) & 0xFF));  // G
		        buffer.put((byte)(pixel & 0xFF));         // B
		        buffer.put((byte)((pixel >> 24) & 0xFF)); // A
		    }

		    buffer.flip();

		    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8,
		                 width, height, 0,
		                 GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		}
	public Texture(int width, int height) {
		this.filePath = "Generated";
		//this.width = width;
		//this.height = height;
		
		//Generate texture on GPU
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
	}
	
	public void init(String fileName) {
		this.filePath = fileName;
		
		//Generate texture on GPU
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
		//Set the texture parameters
		//Repeat the image in both directions
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		//When stretching the image pixelate
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		//When skrinking also pixelate
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		stbi_set_flip_vertically_on_load(true);
		ByteBuffer image = stbi_load(filePath, width, height, channels, 0);
		
		if (image == null) {
		    throw new RuntimeException("Error: (Texture) Could not load Image '" + filePath + "' - " + stbi_failure_reason());
		}

		// upload to GPU
		this.width = width.get(0);
		this.height = height.get(0);
		if (channels.get(0) == 3) {
		    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
		} else if (channels.get(0) == 4) {
		    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		} else {
		    throw new RuntimeException("Error: (Texture) Unknown number of channels '" + filePath + "'");
		}

		// Free memory safely
		stbi_image_free(image);
	}
	
	public void init(ByteBuffer data, int width, int height, int channels, boolean flipY) {
	    this.width = width;
	    this.height = height;

	    texID = glGenTextures();
	    glBindTexture(GL_TEXTURE_2D, texID);


	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

	    // Determine OpenGL format
	    int internalFormat = GL_RED;
	    int format = GL_RED;

	    if (channels == 3) {
	        internalFormat = GL_RGB;
	        format = GL_RGB;
	    } else if (channels == 4) {
	        internalFormat = GL_RGBA;
	        format = GL_RGBA;
	    }

	    glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);

	    glBindTexture(GL_TEXTURE_2D, 0);
	}
    public void init(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();

        int[] pixelsRaw = new int[width * height];
        image.getRGB(0, 0, width, height, pixelsRaw, 0, width);

        // Convert to RGBA byte buffer
        ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Flip y
                int flippedY = height - 1 - y;
                int argb = pixelsRaw[flippedY * width + x];

                byte a = (byte) ((argb >> 24) & 0xFF);
                byte r = (byte) ((argb >> 16) & 0xFF);
                byte g = (byte) ((argb >> 8) & 0xFF);
                byte b = (byte) (argb & 0xFF);

                pixels.put(r).put(g).put(b).put(a);
            }
        }
        pixels.flip();

        // Generate OpenGL texture
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        glBindTexture(GL_TEXTURE_2D, 0);
    }
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texID);
	}
	
	public void unBind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public String getFilePath() {
		return this.filePath;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	
	public int getTexId() {
		return texID;
	}
	public TextureRegion getSubimage(int x, int y, int w, int h) {
		// Convert pixel coordinates to UVs (0..1)
	    float u0 = x / (float) width;
	    float v0 = y / (float) height;
	    float u1 = (x + w) / (float) width;
	    float v1 = (y + h) / (float) height;

	    // Flip V for OpenGL origin at bottom-left
	    float tmpV0 = 1f - v1;
	    float tmpV1 = 1f - v0;

	    return new TextureRegion(this, u0, tmpV0, u1, tmpV1);
	}
	public TextureRegion toTextureRegion() {
	    // Full texture UVs (0..1)
	    float u0 = 0f;
	    float v0 = 0f;
	    float u1 = 1f;
	    float v1 = 1f;

	    // Flip V for OpenGL origin at bottom-left
	    float tmpV0 = 1f - v1;
	    float tmpV1 = 1f - v0;

	    return new TextureRegion(this, u0, tmpV0, u1, tmpV1);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(!(o instanceof Texture)) return false;
		Texture oTex = (Texture)o;
		return oTex.getWidth() == this.width && oTex.getHeight() == this.height && oTex.getTexId() == this.texID && oTex.getFilePath().equals(this.filePath);
	}
	
}
