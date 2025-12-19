package main.renderer;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Shader {

	private int shaderProgramID;
	private boolean beingUsed = false;
	
	private String vertexSource, fragmentSource, filePath;
	
	
	public Shader(String filePath) {
		this.filePath = filePath;
		
		try {
			String source = new String(Files.readAllBytes(Paths.get(filePath)));
			String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
			
			int index = source.indexOf("#type") + 6;
			int eol = source.indexOf("\n", index);
			String firstPattern = source.substring(index, eol).trim();
			
			index = source.indexOf("#type", eol) + 6;
			eol = source.indexOf("\n", index);
			String secondPattern = source.substring(index, eol).trim();
			
			if(firstPattern.equals("vertex")) {
				vertexSource = splitString[1];
			} else if(firstPattern.equals("fragment")) {
				fragmentSource = splitString[1];
			} else {
				throw new IOException("Unexepected token " + firstPattern);
			}
			
			if(secondPattern.equals("vertex")) {
				vertexSource = splitString[2];
			} else if(secondPattern.equals("fragment")) {
				fragmentSource = splitString[2];
			} else {
				throw new IOException("Unexepected token " + secondPattern);
			}
		} catch(IOException e) {
			e.printStackTrace();
			assert false : "Error: Could not open file for shader: " + filePath;
		}
		
	}
	
	
	public void compile() {
		int vertexID, fragmentID;
		
		//Load and compile the vertex shader
		vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		//Pass shader source code to GPU
		GL20.glShaderSource(vertexID, vertexSource);
		GL20.glCompileShader(vertexID);
				
		//Check for errors in compilation process
		int success = GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS);
		if(success == GL20.GL_FALSE) {
			int len = GL20.glGetShaderi(vertexID, GL20.GL_INFO_LOG_LENGTH);
			System.out.println(filePath);
			System.out.println("ERROR: Vertex shader compilation failed.");
			System.out.println(GL20.glGetShaderi(vertexID, len));
			assert false : "";
		}

		//Load and compile the vertex shader
		fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		//Pass shader source code to GPU
		GL20.glShaderSource(fragmentID, fragmentSource);
		GL20.glCompileShader(fragmentID);
						
		//Check for errors in compilation process
		success = GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS);
		if(success == GL20.GL_FALSE) {
			int len = GL20.glGetShaderi(fragmentID, GL20.GL_INFO_LOG_LENGTH);
			System.out.println(filePath);
			System.out.println("ERROR: Fragment shader compilation failed.");
			System.out.println(GL20.glGetShaderi(fragmentID, len));
			assert false : "";
		}
		

		//Link shaders and check for errors
		shaderProgramID = GL20.glCreateProgram();
		GL20.glAttachShader(shaderProgramID, vertexID);
		GL20.glAttachShader(shaderProgramID, fragmentID);
		GL20.glLinkProgram(shaderProgramID);

		//Check for linking errors
		success = GL20.glGetProgrami(shaderProgramID, GL20.GL_LINK_STATUS);
		if(success == GL20.GL_FALSE) {
			int len = GL20.glGetProgrami(shaderProgramID, GL20.GL_INFO_LOG_LENGTH);
			System.out.println(filePath);
			System.out.println("ERROR: Linking of shaders failed.");
			System.out.println(GL20.glGetProgrami(shaderProgramID, len));
			assert false : "";
		}
	}
	
	public void use() {
		if(!beingUsed) {
			//Bind shader program
			GL30.glUseProgram(shaderProgramID);
			beingUsed = true;
		}
	}
	
	public void detach() {
		GL30.glUseProgram(0);
		beingUsed = false;
	}
	
	public void uploadMat4f(String varName, Matrix4f mat4) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		GL20.glUniformMatrix4fv(varLocation, false, matBuffer);
	}
	public void uploadMat3f(String varName, Matrix3f mat4) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat4.get(matBuffer);
		GL20.glUniformMatrix3fv(varLocation, false, matBuffer);
	}
	public void uploadVec4f(String varName, Vector4f vec) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		GL20.glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
	}
	public void uploadVec3f(String varName, Vector3f vec) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		GL20.glUniform3f(varLocation, vec.x, vec.y, vec.z);
	}
	public void uploadVec2f(String varName, Vector2f vec) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		GL20.glUniform2f(varLocation, vec.x, vec.y);
	}
	public void uploadFloat(String varName, float val) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		GL20.glUniform1f(varLocation, val);
	}
	public void uploadInt(String varName, int val) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		GL20.glUniform1i(varLocation, val);
	}
	public void uploadTexture(String varName, int slot) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		GL20.glUniform1i(varLocation, slot);
	}
	public void uploadIntArray(String varName, int[] array) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		GL20.glUniform1iv(varLocation, array);
	}
	public void uploadFloatArray(String varName, float[] array) {
	    int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
	    use();
	    GL20.glUniform1fv(varLocation, array);
	}
	public void uploadVec2fArray(String varName, Vector2f[] vecArray) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		use();
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vecArray.length * 2);
		for (Vector2f vec : vecArray) {
			buffer.put(vec.x).put(vec.y);
		}
		buffer.flip(); // Prepare the buffer for reading
		GL20.glUniform2fv(varLocation, buffer);
	}
	public void uploadVec3fArray(String varName, Vector3f[] vecArray) {
	    int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
	    use();

	    FloatBuffer buffer = BufferUtils.createFloatBuffer(vecArray.length * 3);
	    for (Vector3f vec : vecArray) {
	        buffer.put(vec.x).put(vec.y).put(vec.z);
	    }
	    buffer.flip(); // Prepare the buffer for reading
	    GL20.glUniform3fv(varLocation, buffer);
	}
	public void uploadVec4fArray(String varName, Vector4f[] vecArray) {
	    int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
	    use();

	    FloatBuffer buffer = BufferUtils.createFloatBuffer(vecArray.length * 4);
	    for (Vector4f vec : vecArray) {
	        buffer.put(vec.x).put(vec.y).put(vec.z).put(vec.w);
	    }
	    buffer.flip(); // Prepare the buffer for reading
	    GL20.glUniform4fv(varLocation, buffer);
	}
	public void uploadBool(String varName, boolean value) {
	    int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
	    use();
	    GL20.glUniform1i(varLocation, value ? 1 : 0);
	}
	
}
