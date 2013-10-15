/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.graphic;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Focusrite
 */
public class VertexDefination {

   //private Vertex[] vertices;
   private FloatBuffer positionBuffer;
   private FloatBuffer uvBuffer;
   private int vertexPositionBufferId;
   private int vertexUVBufferId;
   private int vertexArrayId;
   private int drawMethod;
   private int size;
   private static final int VERTEX_SIZE = 3;
   private static final int UV_SIZE = 2;

   public VertexDefination(float[] vertices, float[] uv, int drawMethod) {//float[] uv
      assert vertices.length % VERTEX_SIZE == 0 && vertices.length % VERTEX_SIZE == uv.length % UV_SIZE;
      //this.vertices = new Vertex[vertices.length % VERTEX_SIZE];
      this.drawMethod = drawMethod;
      this.size = vertices.length / VERTEX_SIZE;
      //Position buffer
      positionBuffer = BufferUtils.createFloatBuffer(vertices.length);
      for (int i = 0; i < vertices.length; i++) {
         positionBuffer.put(vertices[i]);
      }
      positionBuffer.flip();

      //Color buffer
      uvBuffer = BufferUtils.createFloatBuffer(uv.length);
      for (int i = 0; i < uv.length; i++) {
         uvBuffer.put(uv[i]);
      }
      uvBuffer.flip();

      init(); //sets up VAO and VBO
   }

   private void init() {
      createVBO();
      createVAO();
   }

   private void createVBO() {
      //Position buffer
      vertexPositionBufferId = GL15.glGenBuffers();
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexPositionBufferId);
      GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
      //UV buffer
      vertexUVBufferId = GL15.glGenBuffers();
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexUVBufferId);
      GL15.glBufferData(GL15.GL_ARRAY_BUFFER, uvBuffer, GL15.GL_STATIC_DRAW);
      //unbind
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
   }

   private void createVAO() {
      vertexArrayId = GL30.glGenVertexArrays();
      GL30.glBindVertexArray(vertexArrayId);
      //Enable attrib array for shader
      GL20.glEnableVertexAttribArray(0); //Position at 0
      GL20.glEnableVertexAttribArray(1); //UV at 1

      //Bind position to 0
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexPositionBufferId);
      GL20.glVertexAttribPointer(0, VERTEX_SIZE, GL11.GL_FLOAT, false, 0, 0);

      //Bind UV to 1
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexUVBufferId);
      GL20.glVertexAttribPointer(1, UV_SIZE, GL11.GL_FLOAT, false, 0, 0);

      //Unbind
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
   }

   public int getVBO() {
      return vertexPositionBufferId;
   }

   public int getVAO() {
      return vertexArrayId;
   }

   public int getDrawMethod() {
      return drawMethod;
   }

   public FloatBuffer getBuffer() {
      return positionBuffer;
   }

   public int getDefinitionSize() {
      return size;
   }

   public void bindDefiniaton() {
      GL30.glBindVertexArray(vertexArrayId);
      GL20.glEnableVertexAttribArray(0); // VertexPosition
      GL20.glEnableVertexAttribArray(1); // VertexUV
   }

   public void unbind() {
      GL20.glDisableVertexAttribArray(0);
      GL20.glDisableVertexAttribArray(1);
      GL30.glBindVertexArray(0);
   }
}
