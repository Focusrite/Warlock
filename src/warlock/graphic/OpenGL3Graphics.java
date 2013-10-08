/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.graphic;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import warlock.camera.Camera;
import warlock.font.FontHandler;

/**
 *
 * @author Focusrite
 */
public class OpenGL3Graphics extends Graphic {
   private static final int SHADERMODE_COLOR = 0;
   private static final int SHADERMODE_TEXTURE = 1;

   private Map<String, VertexDefination> vertexDefinations = new HashMap<>();
   private static final int CIRCLE_DENSITY = 12;
   private static final float Z_ELEVATION = 0.01f;
   //Texture
   private int textureId;
   //Shader handles
   private ShaderProgram shader;
   private int mvpId;
   private int colorId;
   private int shaderModeId;

   public OpenGL3Graphics(int screenWidth, int screenHeight) {
      super(screenWidth, screenHeight);
   }

   public Matrix4f buildModelMatrix(Vector3f translate, double rotation, Vector3f scaling) {
      Matrix4f translateMatrix = new Matrix4f();
      translateMatrix.m30 = translate.x;
      translateMatrix.m31 = translate.y;
      translateMatrix.m32 = translate.z;

      Matrix4f scalingMatrix = new Matrix4f();
      scalingMatrix.m00 = scaling.x;
      scalingMatrix.m11 = scaling.y;
      scalingMatrix.m22 = scaling.z;

      //rotation around z-axis
      Matrix4f rotationMatrix = new Matrix4f();
      rotationMatrix.m00 = (float) Math.cos(rotation);
      rotationMatrix.m10 = -(float) Math.sin(rotation);
      rotationMatrix.m01 = (float) Math.sin(rotation);
      rotationMatrix.m11 = (float) Math.cos(rotation);

      Matrix4f left = translateMatrix;
      Matrix4f right = rotationMatrix;
      Matrix4f result = new Matrix4f();
      Matrix4f.mul(left, right, result);
      right = scalingMatrix;
      Matrix4f.mul(result, right, left);
      return left;
   }

   public Matrix4f buildMVPMatrix(Matrix4f model, Camera camera) {
      //If screen coordinates are set to be used, disregard camera position
      Matrix4f viewMatrix = (isScreenCoordinates()) ? new Matrix4f() : camera.getViewMatrix();

      Matrix4f result = new Matrix4f();
      Matrix4f.mul(camera.getProjectionMatrix(), viewMatrix, result);
      Matrix4f.mul(result, model, result);
      return result;
   }

   @Override
   public void drawRectangle(int x, int y, int w, int h, double rot, Color color) {
      drawRectangle(x, y, 0, w, h, rot, color);
   }

   @Override
   public void drawRectangle(int x, int y, int z, int w, int h, double rot, Color color) {
      VertexDefination def = vertexDefinations.get("rectangle");
      bindColor(color);
      Matrix4f mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y, Z_ELEVATION * z), rot, new Vector3f(w / 2, h / 2, 1)), getCamera());
      drawDefination(def, mvp);
   }

   @Override
   public void drawUnfilledRectangle(int x, int y, int w, int h, int thickness, double rot, Color color) {
      drawUnfilledRectangle(x, y, 0, w, h, thickness, rot, color);
   }

   @Override
   public void drawUnfilledRectangle(int x, int y, int z, int w, int h, int thickness, double rot, Color color) {
      VertexDefination def = vertexDefinations.get("rectangle");
      bindColor(color);

      //Left
      Matrix4f mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x - (w / 2), y, Z_ELEVATION * z), rot, new Vector3f(thickness, h / 2, 1)), getCamera());
      drawDefination(def, mvp);

      //Right
      mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x + (w / 2), y, Z_ELEVATION * z), rot, new Vector3f(thickness, h / 2, 1)), getCamera());
      drawDefination(def, mvp);

      //Top
      mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y + (h / 2), Z_ELEVATION * z), rot, new Vector3f(w / 2, thickness, 1)), getCamera());
      drawDefination(def, mvp);

      //Bottom
      mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y - (h / 2), Z_ELEVATION * z), rot, new Vector3f(w / 2, thickness, 1)), getCamera());
      drawDefination(def, mvp);
   }

   @Override
   public void drawBorderedRectangle(int x, int y, int w, int h, int thickness, double rot, Color fill, Color border) {
      drawBorderedRectangle(x, y, 0, w, h, thickness, rot, fill, border);
   }

   @Override
   public void drawBorderedRectangle(int x, int y, int z, int w, int h, int thickness, double rot, Color fill, Color border) {
      drawRectangle(x, y, z, w, h, rot, fill);
      drawUnfilledRectangle(x, y, z, w, h, thickness, rot, border);
   }

   @Override
   public void drawCircle(int x, int y, int r, Color color) {
      drawCircle(x, y, 0, r, color);
   }

   @Override
   public void drawCircle(int x, int y, int z, int r, Color color) {
      VertexDefination def = vertexDefinations.get("circle");
      bindColor(color);
      Matrix4f mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y, Z_ELEVATION * z), 0, new Vector3f(r, r, 1)), getCamera());
      drawDefination(def, mvp);
   }

   @Override
   public void drawTriangle(int x, int y, int w, int h, double rot, Color color) {
      drawTriangle(x, y, 0, w, h, rot, color);
   }

   @Override
   public void drawTriangle(int x, int y, int z, int w, int h, double rot, Color color) {
      VertexDefination def = vertexDefinations.get("triangle");
      bindColor(color);
      Matrix4f mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y, Z_ELEVATION * z), rot, new Vector3f(w / 2, h / 2, 1)), getCamera());
      drawDefination(def, mvp);
   }

   public void drawDefination(VertexDefination def, Matrix4f mvp) {
      //Bind
      def.bindDefiniaton();
      //Bind MVP to shader
      FloatBuffer fBuffer = BufferUtils.createFloatBuffer(16);
      mvp.store(fBuffer);
      fBuffer.flip();
      glUniformMatrix4(mvpId, false, fBuffer);
      //Draw
      glDrawArrays(def.getDrawMethod(), 0, def.getDefinitionSize());
      //Cleanup
      def.unbind();
   }

   private void bindColor(Color color) {
      float[] arr = color.toArray();
      glUniform4f(colorId, arr[0], arr[1], arr[2], arr[3]); //rgba
   }

   @Override
   public void init() {
      //Init vbo
      createVertexDefinations();

      //2D Initialization
      glEnable(GL_DEPTH_TEST);
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      glClearColor(0.0f, 0.0f, 0.4f, 0.0f);

      //Init shaders, obviously load in a better way
      loadShaders("shaders/vertexshader.glsl", "shaders/fragmentshader.glsl");
   }

   private void bindTexture(Texture tex) {

   }

   private void createVertexDefinations() {
      //Rectangle
      float vertices[] = {
         -1.0f, 1.0f, 0.0f, //upper left
         1.0f, 1.0f, 0.0f, //upper right
         -1.0f, -1.0f, 0.0f //lower left
      };
      float uv[] = {
         0.0f, 1.0f,
         1.0f, 1.0f,
         0.0f, 0.0f,
      };
      VertexDefination triangle = new VertexDefination(vertices, uv, GL_TRIANGLES);
      vertexDefinations.put("triangle", triangle);

      //Rectangle
      vertices = new float[]{
         -1.0f, -1.0f, 0.0f,
         -1.0f, 1.0f, 0.0f,
         1.0f, -1.0f, 0.0f,
         1.0f, 1.0f, 0.0f
      };
      uv = new float[]{
         0.0f, 0.0f,
         0.0f, 1.0f,
         1.0f, 0.0f,
         1.0f, 1.0f,
      };
      VertexDefination rectangle = new VertexDefination(vertices, uv, GL_TRIANGLE_STRIP);
      vertexDefinations.put("rectangle", rectangle);

      //Circle
      vertices = new float[(CIRCLE_DENSITY + 2) * 3];
      uv = new float[(CIRCLE_DENSITY + 2) * 2];
      //Center
      vertices[0] = 0.0f;
      vertices[1] = 0.0f;
      vertices[2] = 0.0f;
      uv[0] = 0.5f;
      uv[1] = 0.5f;
      //Outer vertices
      for (int i = 1; i <= CIRCLE_DENSITY + 1; i++) {
         float theta = (float) (2.0f * Math.PI * i / (float) CIRCLE_DENSITY);

         float x = (float) Math.cos(theta);//calculate the x component
         float y = (float) Math.sin(theta);//calculate the y component

         vertices[i * 3] = x;
         vertices[i * 3 + 1] = y;
         vertices[i * 3 + 2] = 0.0f;
         uv[i * 2] = (x / 2) + uv[0]; //Clamp to [0,1]
         uv[i * 2 +1] = (y / 2) + uv[1]; //Clamp to [0,1]

      }
      VertexDefination circle = new VertexDefination(vertices, uv, GL_TRIANGLE_FAN);
      vertexDefinations.put("circle", circle);
   }

   @Override
   public void resize() {
      //2D Scene
      glViewport(0, 0, getScreenWidth(), getScreenHeight());

      glMatrixMode(GL_PROJECTION);
      //glLoadIdentity();
      //gluOrtho2D(0.0f, getScreenWidth(), 0.0f, getScreenHeight());
      //glPushMatrix();

      //glMatrixMode(GL_MODELVIEW);
      //glLoadIdentity();
      //glPushMatrix();
   }

   private void setShaderMode(int mode) {
      glUniform1i(shaderModeId, mode);
   }

   @Override
   public void preRender() {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      glUseProgram(shader.getProgramId());
      glLoadIdentity();
      setShaderMode(SHADERMODE_COLOR);
   }

   @Override
   public void postRender() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void loadShaders(String vertexPath, String fragmentPath) {
      shader = new ShaderProgram();
      shader.init(vertexPath, fragmentPath);
      mvpId = glGetUniformLocation(shader.getProgramId(), "MVP");
      colorId = glGetUniformLocation(shader.getProgramId(), "color");
      //shaderModeId = glGetUniformLocation(shader.getProgramId(), "shaderMode");
   }

   @Override
   public void drawText(String name, int x, int y, String text, Color color) {
      FontHandler.drawFont(name, x, y, text, color);
   }
}
