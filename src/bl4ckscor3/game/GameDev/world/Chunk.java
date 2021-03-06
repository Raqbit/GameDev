package bl4ckscor3.game.gamedev.world;

import java.awt.Graphics;
import java.util.ArrayList;

import bl4ckscor3.game.gamedev.Main;
import bl4ckscor3.game.gamedev.game.Game;
import bl4ckscor3.game.gamedev.game.Screen;
import bl4ckscor3.game.gamedev.util.DebugUI;
import bl4ckscor3.game.gamedev.util.Utilities;
import bl4ckscor3.game.gamedev.util.Vector2D;
import bl4ckscor3.game.gamedev.world.content.Material;
import bl4ckscor3.game.gamedev.world.content.PlaceableObject;
import bl4ckscor3.game.gamedev.world.content.Tile;
import bl4ckscor3.game.gamedev.world.generation.SimplexNoise;

@SuppressWarnings("unchecked")
public class Chunk
{
	//amount of tiles horizontally
	public static final int chunkSizeX = 16;
	//amount of tiles vertically
	public static final int chunkSizeY = 16;
	//x position of whole chunk
	public int chunkX;
	//y position of whole chunk
	public int chunkY;
	public Tile[][] tiles = new Tile[chunkSizeX][chunkSizeY];
	public ArrayList<PlaceableObject> placedObjects = new ArrayList<PlaceableObject>();

	public Chunk(int x, int y)
	{
		chunkX = x;
		chunkY = y;
	}

	/**
	 * Determines which material to draw
	 */
	public void populate(int seed)
	{
		SimplexNoise sn = new SimplexNoise(7, 0.1, seed);
		double xStart = chunkX * chunkSizeX;
		double yStart = chunkY * chunkSizeY;
		double xEnd = xStart + chunkSizeX;
		double yEnd = yStart + chunkSizeY;
		int xRes = chunkSizeX;
		int yRes = chunkSizeY;
		double[][] data = new double[xRes][yRes];

		for(int i = 0; i < xRes; i++)
		{
			for(int j = 0; j < yRes; j++)
			{
				Material mat;
				int x = (int) (xStart + (i * (xEnd - xStart) / xRes));
				int y = (int) (yStart + (j * (yEnd - yStart) / yRes));
				double noise = (1 + sn.getNoise(x, y)) / 2; //number between 0 and 1

				//determining which texture to use
				if(noise < 0.490F)
				{
					mat = Material.WATER_DEEP;
					tiles[i][j] = new Tile(mat);
				}
				else if(noise < 0.5F)
				{
					mat = Material.WATER_NORMAL;
					tiles[i][j] = new Tile(mat);
				}
				else if(noise < 0.520F)
				{
					mat = Material.SAND;
					tiles[i][j] = new Tile(mat);
				}
				else
				{
					mat = Material.GRASS;
					tiles[i][j] = new Tile(mat, "grass/", 12);
				}

				data[i][j] = noise;
			}
		}
	}

	/**
	 * Renders the current chunk
	 */
	public void render(Graphics g)
	{
		//drawing chunks
		int posX = Utilities.ceil((chunkX * chunkSizeX * Screen.tileSize * Screen.pixelSize - Game.player.position.x * Screen.tileSize * Screen.pixelSize - Screen.tileSize * Screen.pixelSize / 2) * Main.screen.pixelScaleWidth + Main.width / 2);
		int posY = Utilities.ceil((chunkY * chunkSizeY * Screen.tileSize * Screen.pixelSize - Game.player.position.y * Screen.tileSize * Screen.pixelSize - Screen.tileSize * Screen.pixelSize / 2) * Main.screen.pixelScaleHeight + Main.height / 2);

		for(int x = 0; x < tiles.length; x++)
		{
			for(int y = 0; y < tiles[x].length; y++)
			{
				//texture to use, pos inside of chunk + pos x of chunk, same for y, width of chunk, height of chunk
				tiles[x][y].render(g, Utilities.ceil(x * Screen.tileSize * Screen.pixelSize * Main.screen.pixelScaleWidth) + posX, Utilities.ceil(y * Screen.tileSize * Screen.pixelSize * Main.screen.pixelScaleHeight) + posY);
			}
		}

		for(PlaceableObject po : (ArrayList<PlaceableObject>)placedObjects.clone())
		{
			po.render(g, Utilities.ceil(po.getPos().x * Screen.tileSize * Screen.pixelSize * Main.screen.pixelScaleWidth) + posX, Utilities.ceil(po.getPos().y * Screen.tileSize * Screen.pixelSize * Main.screen.pixelScaleHeight) + posY);
		}

		if(Screen.displayDebug)
			DebugUI.drawChunkGrid(g, this, posX, posY);
	}
	
	/**
	 * Gets called every gametick
	 * @param tick The current tick
	 */
	public void tick(int tick)
	{
		for(int x = 0; x < tiles.length; x++)
		{
			for(int y = 0; y < tiles[0].length; y++)
			{
				tiles[x][y].tick();
			}
		}
		
		for(PlaceableObject po : placedObjects)
		{
			po.tick();
		}
	}

	/**
	 * Gets the tile at the given innerchunk position
	 * @param vec The Vector2D holding the x and y position
	 * @return The Tile
	 */
	public Tile getTile(Vector2D pos)
	{
		return tiles[pos.x][pos.y];
	}

	/**
	 * Sets the tile at the given position to the given material
	 * @param pos The position of the tile to change the material of
	 * @param m The material to change the tile to
	 */
	public void setTile(Vector2D pos, Material m)
	{
		tiles[pos.x][pos.y] = new Tile(m);
	}
	
	/**
	 * Places a PlaceableObject into the world
	 * @param po The PlaceableObject to place
	 */
	public void placeObject(PlaceableObject po)
	{
		placedObjects.add(po);
	}
	
	/**
	 * Checks wether the Chunk has a PlaceableObject on the given position
	 * (The correct chunk is being calculated within the method)
	 * @param pos The position to check
	 * @return The PlaceableObject on the given position if there is one, null otherwise
	 */
	public PlaceableObject getPlaceableObject(Vector2D pos)
	{
		return getPlaceableObject(pos, 0, 0);
	}
	
	/**
	 * Checks wether the Chunk has a PlaceableObject on the given position
	 * (The correct chunk is being calculated within the method)
	 * @param pos The position to check
	 * @param x x-coordinate modifier of the position to check
	 * @param y y-coordinate modifier of the position to check
	 * @return The PlaceableObject on the given position if there is one, null otherwise
	 */
	public PlaceableObject getPlaceableObject(Vector2D pos, int x, int y)
	{
		for(PlaceableObject po : (ArrayList<PlaceableObject>)placedObjects.clone())
		{
			if(po.getPos().x == pos.x + x && po.getPos().y == pos.y + y)
				return po;
		}
		
		return null;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Chunk))
			return false;

		Chunk c = (Chunk)o;

		return chunkX == c.chunkX && chunkY == c.chunkY;
	}
}
