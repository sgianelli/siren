package edu.siren.renderer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class Shader {
    int vid, fid, program;

    public Shader(String vertex, String fragment) throws IOException {
        vid = loadShader(vertex, GL20.GL_VERTEX_SHADER);
        fid = loadShader(fragment, GL20.GL_FRAGMENT_SHADER);
        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vid);
        GL20.glAttachShader(program, fid);
        GL20.glLinkProgram(program);
        GL20.glBindAttribLocation(program, 0, "position");
        GL20.glBindAttribLocation(program, 1, "color");
        GL20.glBindAttribLocation(program, 2, "tex");
        GL20.glValidateProgram(program);
    }

    public void use() {
        GL20.glUseProgram(program);
    }

    public void release() {
        GL20.glUseProgram(0);
    }

    private int loadShader(String filename, int type) throws IOException {
        StringBuilder shaderSource = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            shaderSource.append(line).append("\n");
        }
        reader.close();

        System.out.println(shaderSource.toString());

        int id = GL20.glCreateShader(type);
        GL20.glShaderSource(id, shaderSource);
        GL20.glCompileShader(id);

        if (GL20.glGetShader(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetShaderInfoLog(id, 1000));
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }
        return id;
    }

    public void update(String string, Matrix4f mat) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        mat.store(buffer);
        buffer.flip();
        int uniform = GL20.glGetUniformLocation(program, "world");
        GL20.glUniformMatrix4(uniform, false, buffer);
    }

}
