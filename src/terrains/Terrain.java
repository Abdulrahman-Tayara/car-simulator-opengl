package terrains;

import entities.Car;
import entities.Entity;
import entities.Environment;
import models.RawModel;
import org.pushingpixels.substance.api.SubstanceConstants;
import renderEngine.Loader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
	
	private float SIZE;
	private static final int VERTEX_COUNT = 128;

	private float x;
	private float z;
	private float RotX;
	private float RotY;
	private float RotZ;
	private RawModel model;
	private List<Environment> environmentsInTerrain;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;

	public float[] heights;

	public Terrain(int gridX, int gridZ, float SIZE, Loader loader, TerrainTexturePack texturePack,
				   TerrainTexture blendMap, List<Environment> environmentsInTerrain){
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX;
		this.z = gridZ;
		this.SIZE = SIZE;
		this.model = generateTerrain(loader);
		this.environmentsInTerrain = environmentsInTerrain;
		heights = new float[(int) SIZE];
	}

	public float getX() {
		return x;
	}



	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

	public List<Environment> getEnvironmentsInTerrain() {
		return environmentsInTerrain;
	}

	public float getRotX() {
		return RotX;
	}

	public void setRotX(float rotX) {
		RotX = rotX;
	}

	public float getRotZ() {
		return RotZ;
	}

	public void setRotZ(float rotZ) {
		RotZ = rotZ;
	}

	public float getRotY() {
		return RotY;
	}

	public void setRotY(float rotY) {
		RotY = rotY;
	}

	private RawModel generateTerrain(Loader loader){

		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	private void calculateHeights(){

		//	for(int j=0; j < depth ;j++){
		//		int xPosition = (int) (getPosition().x-j);
		//		int zPosition = (int) ((getPosition().z)-i);
		//		heights[xPosition][-zPosition] = ;
		//	}
	}
	public float getSIZE() {
		return SIZE;
	}
}
