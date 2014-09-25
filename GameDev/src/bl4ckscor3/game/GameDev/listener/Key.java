package bl4ckscor3.game.GameDev.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import bl4ckscor3.game.GameDev.core.Screen;

public class Key implements KeyListener
{
	public static List<Integer> keysPressed = new ArrayList<Integer>();
	
	@Override
	public void keyTyped(KeyEvent event)
	{
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		if(!keysPressed.contains(event.getKeyCode()))
			keysPressed.add(event.getKeyCode());
		
		//toggle of debug ui
		if(event.getKeyCode() == 114) //F3
		{
			if(!Screen.shouldDisplayDebug)
				Screen.shouldDisplayDebug = true;
			else
				Screen.shouldDisplayDebug = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		keysPressed.remove((Object)event.getKeyCode());
	}
}