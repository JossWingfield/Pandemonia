package main.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import main.GamePanel;

import static org.lwjgl.opengl.GL30.*;

public class DebugDraw {
	
	GamePanel gp;
	
	private int MAX_LINES = 900;
	
	private List<Line2D> lines = new ArrayList<>();
	//6 floats per vertex, 2 vertices per line
	private float[] vertexArray = new float[MAX_LINES * 6 * 2];
	private Shader shader = AssetPool.getShader("/shaders/default.glsl");
	
	private int vaoID;
	private int vboID;
	
	private boolean started = false;
	
	public DebugDraw(GamePanel gp) {
		this.gp = gp;
	}
	
	public void start() {
		//Generate the vao
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		//Create vbo and buffer some memore
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);
		
		//Enable vertex array attributes
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 6*Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 6*Float.BYTES, 3*Float.BYTES);
		glEnableVertexAttribArray(1);
		
		glLineWidth(2.0f);
	}
	
	public void beginFrame() {
		if(!started) {
			start();
			started = true;
		}
		
		//Remove dead lines
		for(int i = 0; i < lines.size(); i++) {
			if(lines.get(i).beginFrame() < 0) {
				lines.remove(i);
				i--;
			}
		}
	}
	
	public void draw() {
		if(lines.size() <= 0) {
			return;
		}
		
		int index = 0;
		for(Line2D line: lines) {
			for(int i = 0; i < 2; i++) {
				Vector2f position = i == 0 ? line.getFrom() : line.getTo();
				Vector3f color = line.getColor();
				
				//Load position
				vertexArray[index] = position.x;
				vertexArray[index + 1] = position.y;
				vertexArray[index + 2] = -10.0f;
				
				//Load the color
				vertexArray[index + 3] = color.x;
				vertexArray[index + 4] = color.y;
				vertexArray[index + 5] = color.z;
				
				index += 6;
			}
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));
		
		//Use our shader
		shader.use();
		shader.uploadMat4f("uProjection", gp.camera.getProjectionMatrix());
		shader.uploadMat4f("uView", gp.camera.getViewMatrix());
		
		//Bind the vao
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		//Draw the batch
		glDrawArrays(GL_LINES, 0, lines.size());
		
		//Disable location
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
		//Unbind shader
		shader.detach();
	}
	
	//ADD LINE2D METHODS
	public void addLine2D(Vector2f from, Vector2f to) {
		//TODO add constants for common colours
		addLine2D(from, to, new Vector3f(0, 1, 0), 1);
	}
	
	public void addLine2D(Vector2f from, Vector2f to, Vector3f color) {
		addLine2D(from, to, color, 1);
	}
	
	public void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        GLSLCamera camera = gp.camera;
        Vector2f cameraLeft = new Vector2f(camera.position).add(new Vector2f(-2.0f, -2.0f));
        Vector2f cameraRight = new Vector2f(camera.position).
                add(new Vector2f(camera.getProjectionSize()).mul(camera.getZoom()).
                add(new Vector2f(4.0f, 4.0f)));
        boolean lineInView =
                ((from.x >= cameraLeft.x && from.x <= cameraRight.x) && (from.y >= cameraLeft.y && from.y <= cameraRight.y)) ||
                ((to.x >= cameraLeft.x && to.x <= cameraRight.x) && (to.y >= cameraLeft.y && to.y <= cameraRight.y));
		if(lines.size() >= MAX_LINES || !lineInView) {
			return;
		}
		lines.add(new Line2D(from, to, color, lifetime));
	}
	
	//Box2D methods
	
	public void addBox2D(Vector2f center, Vector2f dimensions, float rotation) {
		addBox2D(center, dimensions, rotation, new Vector3f(0, 0, 1), 1);
	}
	
	public void addBox2D(Vector2f center, Vector2f dimensions, Vector3f color, float rotation) {
		addBox2D(center, dimensions, rotation, color, 1);
	}
	
	public void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color, int lifetime) {
		Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
		Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));
		
		Vector2f[] vertices = {
			new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
			new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
		};
		
		if(rotation != 0.0f) {
			for(Vector2f vert: vertices) {
				JMath.rotate(vert, rotation, center);
			}
		}
		
		addLine2D(vertices[0], vertices[1], color, lifetime);
		addLine2D(vertices[0], vertices[3], color, lifetime);
		addLine2D(vertices[1], vertices[2], color, lifetime);
		addLine2D(vertices[2], vertices[3], color, lifetime);
	}
	
	//Add circle methods
	public void addCircle(Vector2f center, float radius) {
		addCircle(center, radius, new Vector3f(0, 1, 0), 1);
	}
	
	public void addCircle(Vector2f center, float radius, Vector3f color) {
		addCircle(center, radius, color, 1);
	}
	
	public void addCircle(Vector2f center, float radius, Vector3f color, int lifetime) {
		Vector2f[] points = new Vector2f[20];
		int increment = 360 / points.length;
		int currentAngle = 0;
		
		for(int i = 0; i < points.length; i++) {
			Vector2f tmp = new Vector2f(0, radius);
			JMath.rotate(tmp, currentAngle, new Vector2f(0, 0));
			points[i] = new Vector2f(tmp).add(center);
			
			if(i > 0) {
				addLine2D(points[i-1], points[i], color, lifetime);
			}
			currentAngle += increment;
		}
		
		addLine2D(points[points.length-1], points[0], color, lifetime);
	}
	
	
}
