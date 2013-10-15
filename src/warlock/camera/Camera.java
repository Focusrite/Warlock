/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.camera;

import org.lwjgl.util.glu.GLU;
import warlock.phys.Vector;
import org.lwjgl.util.vector.*;

/**
 *
 * @author Focusrite
 */
public class Camera {

   private Vector3f lookAt;
   private Vector3f cameraPosition;
   private Vector3f tilt;

   private Matrix4f viewMatrix;
   private Matrix4f projectionMatrix;

   private float width;
   private float height;

   private static final float EYE_X = 0;
   private static final float EYE_Y = 0;
   private static final float EYE_Z = 0;
   private static final float LOOKAT_Z = 1;
   private static final float NEAR = -1.0f;
   private static final float FAR = 1.0f;

   public static int SCROLL_ZONE_SIZE = 40;

   public Camera(float x, float y, float width, float height) {
      viewMatrix = new Matrix4f();
      projectionMatrix = new Matrix4f();
      this.width = width;
      this.height = height;
      reset();
      move(x, y); //builds view matrix
      buildProjectionMatrix();
   }

   public final void reset() {
      lookAt = new Vector3f(EYE_X, EYE_Y, LOOKAT_Z);
      cameraPosition = new Vector3f(EYE_X, EYE_Y, EYE_Z);
      tilt = new Vector3f(0.0f, 1.0f, 0.0f);
      buildViewMatrix();
      buildProjectionMatrix();
   }

   public void move(float dx, float dy, float dz) {
      lookAt.x += dx;
      lookAt.y += dy;
      cameraPosition.x = lookAt.x;
      cameraPosition.y = lookAt.y;
      cameraPosition.z += dz;
      buildViewMatrix(); //Update view matrix
   }

   public void setX(float x) {
      this.lookAt.x = x;
      this.cameraPosition.x = x;
      buildViewMatrix(); //Update view matrix
   }

   public void setY(float y) {
      this.lookAt.y = y;
      this.cameraPosition.y = y;
      buildViewMatrix(); //Update view matrix
   }

   public final void move(float dx, float dy) {
      move(dx, dy, 0);
   }

   public float getLookAtX() {
      return lookAt.x;
   }

   public float getLookAtY() {
      return lookAt.y;
   }

   public float getLookAtZ() {
      return lookAt.z;
   }

   public float getCameraPositionX() {
      return cameraPosition.x;
   }

   public float getCameraPositionY() {
      return cameraPosition.y;
   }

   public float getCameraPositionZ() {
      return cameraPosition.z;
   }

   public final void buildViewMatrix() {
      //Calculation vectors
      Vector3f zaxis = new Vector3f();
      Vector3f.sub(lookAt, cameraPosition, zaxis);
      zaxis.normalise();

      Vector3f xaxis = new Vector3f();
      Vector3f.cross(tilt, zaxis, xaxis);
      xaxis.normalise();

      Vector3f yaxis = new Vector3f();
      Vector3f.cross(zaxis, xaxis, yaxis);

      //Matrices
      Matrix4f orientation = new Matrix4f();
      Matrix4f tranlation = new Matrix4f();
      Matrix4f result = new Matrix4f();

      //Orientation
      orientation.m00 = xaxis.x;
      orientation.m10 = xaxis.y;
      orientation.m20 = xaxis.z;

      orientation.m01 = yaxis.x;
      orientation.m11 = yaxis.y;
      orientation.m21 = yaxis.z;

      orientation.m02 = zaxis.x;
      orientation.m12 = zaxis.y;
      orientation.m22 = zaxis.z;

      //Translation
      tranlation.m30 = -cameraPosition.x;
      tranlation.m31 = -cameraPosition.y;
      tranlation.m32 = -cameraPosition.z;

      Matrix4f.mul(tranlation, orientation, result);
      viewMatrix = result;
   }

   public final void buildProjectionMatrix() {
      //orthogonal projection matrix
      Matrix4f projection = new Matrix4f();
      //Some linear algebra and aid from "Introduction to 3D game programming" by Frank Luna..
      projection.m00 = 2 / width;
      projection.m11 = 2 / height;
      projection.m22 = -2 / (FAR - NEAR);

      projection.m30 = -1;
      projection.m31 = -1;
      projection.m32 = -(FAR + NEAR) / (FAR - NEAR);
      projectionMatrix = projection;
   }

   public Matrix4f getViewMatrix() {
      return viewMatrix;
   }

   public Matrix4f getProjectionMatrix() {
      return projectionMatrix;
   }

   public float getWidth() {
      return width;
   }

   public float getHeight() {
      return height;
   }
}
