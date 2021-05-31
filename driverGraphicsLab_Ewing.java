/*Name: <Grant Ewing>
*Class: <Spring - COSC 1437.82001>
*Project: <2>:<Case Study>
*Revision: <1.6>
*Date: <5/10/2021>
*Description: <This program will demonstrate a platformer type game that can shoot and traverse areas>
*Assets and who from:<Audio - myself from creation of songs and SFX in Ableton Live 10 
*					Samus - 
*					Block images - 
*							>
*/
package Final1437;



import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class driverGraphicsLab_Ewing 
{
	public static final int FRAME_WIDTH = 1200;
	public static final int FRAME_HEIGHT = 800;
	public static int ScreenX = -200;

	static JFrame frame;
	static GameState gameState = GameState.PreGame;	
	static JPanel mainPanel;
	static GamePanel gamePanel;
	static PausePanel pausePanel;
	static TitlePanel titlePanel;
	static GameOverPanel gameOverPanel;
	static EndGamePanel endGamePanel;
	static CardLayout layout;
	
	public static void main(String args[])
	{
		frame = new JFrame ("Alien Robot Hunt");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		gamePanel = new GamePanel(FRAME_WIDTH,FRAME_HEIGHT,frame);	
		
		mainPanel = new JPanel();		
		pausePanel = new PausePanel();
		titlePanel = new TitlePanel();
		gameOverPanel = new GameOverPanel();
		endGamePanel = new EndGamePanel();
		layout = new CardLayout();
		
		mainPanel.setLayout(layout);
		mainPanel.add(titlePanel, GameState.PreGame.toString());
		mainPanel.add(gamePanel, GameState.Running.toString());
		mainPanel.add(pausePanel, GameState.Paused.toString());	
		mainPanel.add(gameOverPanel, GameState.GameOver.toString());
		mainPanel.add(endGamePanel, GameState.EndGame.toString());
		
		frame.add(mainPanel);
		frame.setVisible(true);	
		GamePanel.gameLoop();
	}
}
class Sounds
{
	public static void playSound(String f)
	{
		try {
			File file = new File(f);
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.
					getAudioInputStream(file);
			clip.open(ais);									
			//for looping
//			clip.setLoopPoints(0,-1);
//			clip.loop(1000);
			//soundfx
//			clip.start();
			
			long clipTimePosition = clip.getMicrosecondPosition();
			clip.setMicrosecondPosition(clipTimePosition);
			clip.start();
			}catch(Exception e){e.printStackTrace();}
	}
}
class PausePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5218451603363393220L;

	public PausePanel()
	{
		this.setSize(new Dimension(driverGraphicsLab_Ewing.FRAME_WIDTH,driverGraphicsLab_Ewing.FRAME_HEIGHT));
		JLabel label = new JLabel("Paused");
		JButton Exit = new JButton("Exit");
		//placement and size of button is changed here
		Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);			
			}			
		});
		this.add(label);
		this.add(Exit);
	}
}
class EndGamePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3514470026623719575L;
	public static int enemiesDefeated;
	public EndGamePanel()
	{
		this.setSize(new Dimension(driverGraphicsLab_Ewing.FRAME_WIDTH,driverGraphicsLab_Ewing.FRAME_HEIGHT));
		JLabel label = new JLabel("YOU WON!!");
		JButton Exit = new JButton("Exit");
		System.out.println(enemiesDefeated);
		JButton KillCount = new JButton(String.valueOf(enemiesDefeated));
		
		Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);			
			}			
		});
		this.add(label);
		this.add(Exit);
		this.add(KillCount);
	}
}
class GameOverPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3513834910045665478L;

	public GameOverPanel()
	{
		this.setSize(new Dimension(driverGraphicsLab_Ewing.FRAME_WIDTH,driverGraphicsLab_Ewing.FRAME_HEIGHT));
		JLabel label = new JLabel("YOU DIE");
		JButton Exit = new JButton("Exit");
		JButton BOSS = new JButton("Try the boss instead?");
		JButton Restart = new JButton("Restart?");
		
		Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);			
			}			
		});
		BOSS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				driverGraphicsLab_Ewing.gameState = GameState.Running;
			}			
		});		
		this.add(label);
		this.add(Exit);
		this.add(BOSS);
	}
}
class TitlePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7552193298114747160L;
	public TitlePanel()
	{
		
		this.setSize(new Dimension(driverGraphicsLab_Ewing.FRAME_WIDTH,driverGraphicsLab_Ewing.FRAME_HEIGHT));
		JLabel label = new JLabel("Title Screen");
		JButton Start = new JButton("Start Game");
		//placement and size of button is changed here
		Start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e3) {

				driverGraphicsLab_Ewing.gameState = GameState.Running;
			}			
		});
		this.add(label);
		this.add(Start);
	}
}
class GamePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2618646678995915781L;
	private Image raster;
	private int width, height;
	private Graphics2D rasterGraphics;	
	public JFrame myFrame;	
	static ArrayList<Block> allBlocks;
	static Player player;
	static ArrayList<Enemy> allEnemy;
	static ArrayList<ScreenObjects> allScreenObjects;
	static ArrayList<Actor> allActors;
	static int PHEALTH;

	public GamePanel(int w, int h,JFrame frame)
	{
		myFrame=frame;
		width =w;
		height = h;
		raster = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		rasterGraphics = (Graphics2D) raster.getGraphics();
		
		//giving gamePanel ownership of the arrays
		allBlocks = new ArrayList<Block>();
		allEnemy = new ArrayList<Enemy>();
		allActors = new ArrayList<Actor>();		
		//JFrame heavyweight
		//JPanel lightweight
	}
	
	public static void gameLoop()
	{
		System.out.println("Game loop started");				
		System.out.println(ScreenObjects.allScreenObjects.size());
//		loadLevel(ScreenObjects.PATH+"Level 1 Map.txt");
		loadLevel(ScreenObjects.PATH+"Level BOSS Map.txt");
	
		System.out.println(ScreenObjects.allScreenObjects.size());
		driverGraphicsLab_Ewing.frame.addKeyListener(new KeyListener() 
		{
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) 
			{
				if (e.getKeyCode()== KeyEvent.VK_P)
					if(driverGraphicsLab_Ewing.gameState == GameState.Running)
					{
						driverGraphicsLab_Ewing.gameState= GameState.Paused;
						driverGraphicsLab_Ewing.layout.show(driverGraphicsLab_Ewing.mainPanel, GameState.Paused.toString());
					}
					else
					{
						driverGraphicsLab_Ewing.gameState = GameState.Running;
						driverGraphicsLab_Ewing.layout.show(driverGraphicsLab_Ewing.mainPanel, GameState.Running.toString());;
					}
				//because having the escape key to exit is just nice to have
				if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
					System.exit(0);
			}
		});
		driverGraphicsLab_Ewing.frame.addKeyListener(GamePanel.player);
		driverGraphicsLab_Ewing.frame.addMouseListener(GamePanel.player);
		
		//TODO this is not the place to add a music track
		if(!ScreenObjects.DEBUG) 
			Sounds.playSound(ScreenObjects.PATH+"Level1Mastered-5dB-9.75Limit.wav");
		
		while(true)	//main loop
		{
			while(!ScreenObjects.LevelComplete)
			{

				long time = System.currentTimeMillis();
				
				if(driverGraphicsLab_Ewing.gameState == GameState.Running)
				{	
					driverGraphicsLab_Ewing.frame.requestFocus();
					//draw and move background and screen objects
					driverGraphicsLab_Ewing.gamePanel.drawGame();	
				}
				
				long deltaTime = System.currentTimeMillis()-time;
				try {Thread.sleep(15);}catch(Exception e) {}				
			}//level complete
			//load boss
			ScreenObjects.allScreenObjects.clear();
			loadLevel(ScreenObjects.PATH+"Level BOSS Map.txt");
			driverGraphicsLab_Ewing.frame.addKeyListener(player);
			driverGraphicsLab_Ewing.frame.addMouseListener(player);
			ScreenObjects.LevelComplete=false;
			driverGraphicsLab_Ewing.ScreenX=-200;
		}//game complete
	}

	public static void loadLevel(String filename)
	{
		ScreenObjects.allScreenObjects.clear();
		allActors.clear();
		Scanner sc;
		try
		{
			sc = new Scanner(new File(filename));
			while (sc.hasNextLine())
			{
				String lineTokens[] = sc.nextLine().split(", ");
				if(lineTokens[0].equals("block"))
				{
					allBlocks.add(new Block(new Vector2D(
							Integer.parseInt(lineTokens[1]),
							Integer.parseInt(lineTokens[2]))));
				}
				if(lineTokens[0].equals("enemy"))
				{
					allEnemy.add(new Enemy(new Vector2D(
							Integer.parseInt(lineTokens[1]),
							Integer.parseInt(lineTokens[2])),
							//size
							new Vector2D(
							Integer.parseInt(lineTokens[3]),
							Integer.parseInt(lineTokens[4]))
							));			
				}
				if(lineTokens[0].equals("player"))
				{
					player = (new Player(new Vector2D(
							Integer.parseInt(lineTokens[1]),
							Integer.parseInt(lineTokens[2]))));
					
				}
				if(lineTokens[0].equals("andro"))
				{
					allEnemy.add(new midEnemy (
							//location
							new Vector2D(
							Integer.parseInt(lineTokens[1]),
							Integer.parseInt(lineTokens[2])),
							//size
							new Vector2D(
							Integer.parseInt(lineTokens[3]),
							Integer.parseInt(lineTokens[4])),player							
							));					
				}
				if(lineTokens[0].equals("small"))
				{
					allEnemy.add(new smallEnemy (

							//location
							new Vector2D(
							Integer.parseInt(lineTokens[1]),
							Integer.parseInt(lineTokens[2])),
							//size
							new Vector2D(
							Integer.parseInt(lineTokens[3]),
							Integer.parseInt(lineTokens[4])),player
							));					
				}
				if(lineTokens[0].equals("exit"))
				{
					allBlocks.add(new ExitBlock(new Vector2D(
							Integer.parseInt(lineTokens[1]),
							Integer.parseInt(lineTokens[2]))));
				}
				if(lineTokens[0].equals("boss"))
				{
					allEnemy.add(new Boss(new Vector2D(
							Integer.parseInt(lineTokens[1]),
							Integer.parseInt(lineTokens[2])),
							//size
							new Vector2D(
							Integer.parseInt(lineTokens[3]),
							Integer.parseInt(lineTokens[4])),player
							));
					
				}
			}
		}catch(FileNotFoundException e) {}
	}
	public void drawGame()
	{
		//using a static Image that follows the character completely
		Image mountains = new ImageIcon(ScreenObjects.PATH+"MountainBG.png").getImage();
		//some "clouds" that are set a certain points that look like they're moving by following ScreenX
		Image clouds = new ImageIcon(ScreenObjects.PATH+"Clouds.png").getImage();
		//setup the graphics, the added values to the images just help to add a little more visual smoothness
		rasterGraphics.drawImage(mountains, getX(), getY(), mountains.getWidth(myFrame)+200, mountains.getHeight(myFrame)+200, null);
		rasterGraphics.drawImage(clouds, getX()-driverGraphicsLab_Ewing.ScreenX+200, getY(), clouds.getWidth(myFrame)+200, clouds.getHeight(myFrame), null);
		rasterGraphics.drawImage(clouds, getX()-driverGraphicsLab_Ewing.ScreenX+1200, getY(), clouds.getWidth(myFrame)+200, clouds.getHeight(myFrame), null);
		rasterGraphics.drawImage(clouds, getX()-driverGraphicsLab_Ewing.ScreenX+2200, getY(), clouds.getWidth(myFrame)+200, clouds.getHeight(myFrame), null);
		rasterGraphics.drawImage(clouds, getX()-driverGraphicsLab_Ewing.ScreenX+3200, getY(), clouds.getWidth(myFrame)+200, clouds.getHeight(myFrame), null);		

//		Image helmet = new ImageIcon(ScreenObjects.PATH+"Helmet.png").getImage();		
		Image healthBar = new ImageIcon(ScreenObjects.PATH+"HealthBar.png").getImage();
		rasterGraphics.setColor(Color.red);

		rasterGraphics.fillRect(getX(), getY(), PHEALTH, 25);
		rasterGraphics.drawImage(healthBar,getX(),getY(),100,25,null);
		
		for(ScreenObjects sO:ScreenObjects.allScreenObjects)
		{
			sO.draw(rasterGraphics);

			if (sO instanceof Actor)
			{
				((Actor) sO).Act(ScreenObjects.allScreenObjects);
			}
		}
		ScreenObjects.allScreenObjects.removeAll(ScreenObjects.allScreenObjectsToRemove);
		Bullet.HandleBullets(rasterGraphics,ScreenObjects.allScreenObjects);
		
		int current = (int)player.location.getX();
		if (current - driverGraphicsLab_Ewing.ScreenX > 800)
			driverGraphicsLab_Ewing.ScreenX = current - 800;
		else if(current - driverGraphicsLab_Ewing.ScreenX < 400)
			driverGraphicsLab_Ewing.ScreenX = current - 400;
		
		
		this.getGraphics().drawImage(raster, 0, 0, null);		
	}
}

enum direction
{
	left,right,down,up,downLeft,downRight;
}
enum GameState
{
	PreGame,
	Running,
	Paused,
	GameOver,
	EndGame
}
interface hasCollision
{
	Shape getCollision();
}
class Block extends ScreenObjects implements hasCollision
{	//blocks for each individual space in environment
	public Vector2D location, size;
	private BufferedImage blockImage;

	public Block(Vector2D location)
	{
		this.location=location;
		size = new Vector2D(50,50);					
		try {
			blockImage = ImageIO.read(new File(ScreenObjects.PATH+"bigGrassBlock.png"));
		} catch (IOException e3) {e3.printStackTrace();}
		velocity = new Vector2D(0,0);
		allScreenObjects.add(this);

	}
	public void draw(Graphics2D g)
	{	
		g.drawImage(blockImage,(int)location.getX()-driverGraphicsLab_Ewing.ScreenX, (int)location.getY(), null);
	}
	public Rectangle getCollision()
	{
		return new Rectangle((int)location.getX(), (int)location.getY(), (int)size.getX(), (int)size.getY());
	}
}
class ExitBlock extends Block
{
	private BufferedImage exitImage;
	private Vector2D size;
	public ExitBlock(Vector2D location)
	{
		super(location);
		this.location = location;
		this.size = new Vector2D(40,65);					
		try {
			this.exitImage = ImageIO.read(new File(ScreenObjects.PATH+"exitDoor.png"));
		} catch (IOException e3) {e3.printStackTrace();}
		allScreenObjects.add(this);
	}
	public void draw(Graphics2D g)
	{	
		g.drawImage(exitImage,(int)location.getX()-driverGraphicsLab_Ewing.ScreenX, (int)location.getY(), null);
	}
}
class Rocket extends Bullet
{
	private Vector2D velocity;
	private float timeToLive = 120;
	private int damage = 80;
	public Rocket(Vector2D location, direction d) 
	{
		super(location, d);
		this.location = location;
		if(d== direction.left)
			velocity = new Vector2D(-1,0);
		if(d== direction.right)
			velocity = new Vector2D(5,0);
		if(d== direction.up)
			velocity = new Vector2D(0,-5);
		if(d== direction.down)
			velocity = new Vector2D(0,5);
		if(d== direction.downRight)
			velocity = new Vector2D(5,5);
		if(d== direction.downLeft)
			velocity = new Vector2D(-5,5);
		allBullets.add(this);
		size = new Vector2D(10,10);
	}
	public void draw(Graphics2D g) 
	{
		g.setColor(Color.RED);
		g.fillRect((int)location.getX()-driverGraphicsLab_Ewing.ScreenX,(int)location.getY(), (int)size.getX(), (int)size.getY());
	}
	public boolean Act(ArrayList<ScreenObjects> allScreenObjects)
	{
		getCollision();
		//rockets will ignore gravity
		velocity.multiply(.1f);
		location = location.add(velocity);	//update bullets current location
		timeToLive--;
		if (timeToLive<=0)
		{
			allBullets.remove(this);
			return true;
		}
		return false;
	}
	public Shape getCollision() {
		Rectangle collision = null;
		Rectangle myCollision = new Rectangle((int)location.getX(),(int)location.getY(), (int)size.getX(), (int)size.getY());
		
		//check Velocity to see if can still move that direction						
		for(ScreenObjects sO:allScreenObjects)
		{
			collision = (Rectangle) sO.getCollision();
			if (myCollision.intersects(collision))
			{
				//unfortunately if I had done Actor health it decreases from everythings health at the same time
				if(sO instanceof Enemy)
					sO.health-=damage;					
				if(sO instanceof Player)
					sO.health-=damage;
				System.out.println("Bullet hit");
				//make animations here
				velocity = new Vector2D (0,0);
				allBullets.remove(this);
				allScreenObjectsToRemove.add(this);
			}
		}
		return collision;
	}
	
}
class Bullet extends ScreenObjects implements hasCollision
{
	public static ArrayList<Bullet> allBullets = new ArrayList<>();
	public Vector2D location, velocity,size;
	private float timeToLive = 50;
	private int damage = 10;
	
	public Bullet(Vector2D location, direction d)
	{
		this.location = location;
		if(d== direction.left)
			velocity = new Vector2D(-23,-4);
		if(d== direction.right)
			velocity = new Vector2D(23,-4);
		if(d== direction.up)
			velocity = new Vector2D(0,-22);
		if(d== direction.down)
			velocity = new Vector2D(0,22);
		if(d== direction.downRight)
			velocity = new Vector2D(22,22);
		if(d== direction.downLeft)
			velocity = new Vector2D(-22,22);
		allBullets.add(this);
		size = new Vector2D(8,8);
	}
	
	public static void HandleBullets(Graphics2D g, ArrayList<ScreenObjects> allScreenObjects) 
	{
		for (Bullet b: allBullets)
		{
			b.draw(g);
		}
		for (int i=0;i<allBullets.size();i++)
		{
			if (allBullets.get(i).Act(allScreenObjects))
				i--;
		}
	}

	public void draw(Graphics2D g) 
	{
		g.setColor(Color.CYAN);
		g.fillRect((int)location.getX()-driverGraphicsLab_Ewing.ScreenX,(int)location.getY(), (int)size.getX(), (int)size.getY());
	}

	private boolean Act(ArrayList<ScreenObjects> allScreenObjects)
	{
		getCollision();
		velocity = velocity.add(ScreenObjects.GRAVITY);
		velocity = velocity.multiply(1.3f);	
		location = location.add(velocity);	//update bullets current location
		timeToLive--;
		if (timeToLive<=0)
		{
			allBullets.remove(this);
			return true;
		}
		return false;
	}


	public Shape getCollision() {
		Rectangle collision = null;
		Rectangle myCollision = new Rectangle((int)location.getX(),(int)location.getY(), (int)size.getX(), (int)size.getY());
		
		//check Velocity to see if can still move that direction						
		for(ScreenObjects sO:allScreenObjects)
		{
			collision = (Rectangle) sO.getCollision();
			if (myCollision.intersects(collision))
			{
				//unfortunately if I had done Actor health it decreases from everythings health at the same time
				if(sO instanceof Enemy)
					sO.health-=damage;					
				if(sO instanceof Player)
					sO.health-=damage;
				System.out.println("Bullet hit");
				//make animations here
				velocity = new Vector2D (0,0);
				allBullets.remove(this);
//				allScreenObjects.remove(this);

			}
		}
		return collision;
	}
}


abstract class ScreenObjects implements hasCollision
{
	public int health;
	public static ArrayList<ScreenObjects> allScreenObjects = new ArrayList<>();
	//helper ArrayList to allow concurrent modification
	public static ArrayList<ScreenObjects> allScreenObjectsToRemove = new ArrayList<>();
	Vector2D location, size;
	Vector2D velocity = new Vector2D (0,0);
	public static final Vector2D GRAVITY=new Vector2D(0,.92f);
	public static final boolean DEBUG=true;
	public static boolean LevelComplete = false;
	public static final String PATH = "E:\\schoolWork\\COSC 1437\\COSC1437\\src\\Final1437\\";
	public void draw(Graphics2D g) {}
}
abstract class Actor extends ScreenObjects
{
	public abstract void draw(Graphics2D g);
	public abstract void Act(ArrayList<ScreenObjects> allScreenObjects);
	public abstract void Die();
}
class Sprite implements Serializable
{
       /**
	 * 
	 */
	private static final long serialVersionUID = -8045358719729233583L;
	public BufferedImage spriteSheet ;
       public BufferedImage currentFrame ;
       public int animationNumber = 0; 		//vertical
       public int frameNumber = 0; 			//horizontal
       public int tileXSize = 0;
       public int tileYSize = 0;
       public int FRAME_NUMBER = 10;
      
       public Sprite(String fileName, int tileWidth,int tileHeight, int frameNumber )
       {     
    	  this.tileXSize = tileWidth;
    	  this.tileYSize = tileHeight;
    	  this.FRAME_NUMBER = frameNumber;
             try {
                    spriteSheet = ImageIO.read( new File( fileName ));
             } catch (IOException e ) {
                    e .printStackTrace();
             }     
             //init currentFrame
             currentFrame = spriteSheet .getSubimage(0,0, tileXSize , tileYSize );
       }
       public void Update()
       {
             frameNumber = ( frameNumber + 1) % FRAME_NUMBER ;
             currentFrame = spriteSheet .getSubimage( frameNumber * tileXSize , animationNumber * tileYSize , tileXSize , tileYSize );
       }     
}
/**
 * a big ship that shoots a mixture of rockets and bullets at the player
 * @author Grant Ewing
 *
 */
class Boss extends Enemy
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3444409412851464952L;
	private direction last;
	private Player p;
	private Image enemyImage;
	private boolean SHOOT,ROCKET,GIGA,SLAM;
	private float Speed = .7f;
	public Boss(Vector2D location,Vector2D size,Player p) 
	{		
		super(location,size);
		enemyImage = new ImageIcon(PATH+"bigShip.png").getImage();
		this.health = 1000;
		this.p = p;
		last = direction.right;
		velocity = new Vector2D(0,0);
		allScreenObjects.add(this);
		GamePanel.allActors.add(this);
	}
	public void draw(Graphics2D g) 
	{
		if(DEBUG)
		{
			g.setColor(Color.magenta);
			Rectangle collision = (Rectangle)getCollision();
			collision.x -= driverGraphicsLab_Ewing.ScreenX;
			g.draw(collision);
			g.drawString("Health: "+health, this.location.getX()-driverGraphicsLab_Ewing.ScreenX, this.location.getY()-25);
		}	
		g.drawImage(enemyImage,(int)location.getX()-driverGraphicsLab_Ewing.ScreenX, (int)location.getY(), null);
	}
	private long lastUpdate=0;
	public void Act(ArrayList<ScreenObjects> allScreenObjects)
	{
		if(health<=0)
			Die();
		//go left to right
		if (last == direction.right)
			velocity = new Vector2D(2,0);
		if (last == direction.left)
			velocity = new Vector2D(-2,0);
		if(last == direction.down)
			velocity= new Vector2D(0,10);
		if(last == direction.up)
			velocity= new Vector2D(0,-10);		

		
		//check to turn around
		for(ScreenObjects sO: allScreenObjects)
		{
			if (sO == this)
				continue;
			if (last == direction.right)
			{
				Point p1 = new Point((int)(location.getX()+size.getX()+5), (int)(location.getY()+size.getY()/2));
				if(sO.getCollision().contains(p1))
				{
					last = direction.left;
				}
			}
			if (last == direction.left)
			{
				Point p2 = new Point((int)(location.getX()-5), (int)(location.getY()+size.getY()/2));
				if(sO.getCollision().contains(p2))
				{
					last = direction.right;
				}
			}
			if(last == direction.down)
			{
				//extra points for collision against player below the ship
				Point p1 = new Point((int)(location.getX()+size.getX()), (int)(location.getY()+size.getY()+30));
				Point p2 = new Point((int)(location.getX()+(size.getX()/2)), (int)(location.getY()+size.getY()+30));
				Point p3 = new Point((int)(location.getX()), (int)(location.getY()+size.getY()+30));
				if(sO.getCollision().contains(p1)||sO.getCollision().contains(p2)||sO.getCollision().contains(p3))
				{
					last = direction.up;
				}
			}
			if(last == direction.up)
			{
				Point p4 = new Point((int)(location.getX()-5), (int)(location.getY()-size.getY()));
				if(sO.getCollision().contains(p4))
				{
					int randomNumb = (int)(Math.random()*2)+1;
					System.out.println("SLAM Number: "+ randomNumb);
					switch(randomNumb)
					{
					case 1:
						last = direction.right;
					case 2:
						last = direction.left;
					}
					//TODO make this switch from right to left randomly, but evenly
//					last = direction.right;
				}
			}
		}
				
		location = location.add(velocity);	
		
		if (System.currentTimeMillis() - lastUpdate > 1000)
		{
			lastUpdate = System.currentTimeMillis();
			//some type of charge animation
			Attack();

			//then whatever regular animation
			//		sprite.Update();
			//		oneSprite.Update();
		}
	}
	/**
	 * With the boss being a more complex enemy with a variety of attacks to choose from
	 * his own method devoted to just doing that works great
	 */
	private void Attack()
	{
		GIGA=false;
		System.out.println("Attack called");
		//random select between the 3
		int j = 0;
		if (health<250)
			j=1;

			int randomNumb = (int)(Math.random()*3+j);
			System.out.println("Attack Number: "+randomNumb);
			switch(randomNumb)
			{
			case 0: //shoot bullets		
				for(int i=0;i<10;i++)
				{
					SHOOT=true;
					if (SHOOT)
					{
							Vector2D dirToTarget = p.location.subtract(location);
							dirToTarget.normalize();						
							Vector2D Center = new Vector2D(location.getX()-size.getX()/2, location.getY()-size.getY()/2);					
							float xDelta = p.location.getX() - Center.getX();						
	
							if(p.last == direction.left && xDelta < 0)
							{
									new Bullet(new Vector2D(this.location.getX()+size.getX()/2,this.location.getY()+size.getY()+10),direction.downLeft);
							}
							else if(p.last == direction.right && xDelta > 0)
							{
									new Bullet(new Vector2D(this.location.getX()+size.getX()/2,this.location.getY()+size.getY()+10),direction.downRight);
							}						
							else
							{
									new Bullet(new Vector2D(this.location.getX()+size.getX()/2,this.location.getY()+size.getY()+10),direction.down);
							}		
						SHOOT = false;
					}
				}
					break;
								
			case 1: //shoot rockets
				ROCKET=true;
				if (ROCKET)
				{					
					Vector2D dirToTarget = p.location.subtract(location);
					dirToTarget.normalize();
					
					double disToTarget = Vector2D.Distance(p.location,location);
					
					Vector2D Center = new Vector2D(location.getX()-size.getX()/2, location.getY()-size.getY()/2);
					
		
					float xDelta = p.location.getX() - Center.getX();
					
		
					System.out.println(p.last);
					System.out.println(p.location);
					for(int i=0;i<5;i++)
					{						
						if(p.last == direction.left && xDelta < 0)
						{
							new Rocket(new Vector2D(this.location.getX()+size.getX()/2,this.location.getY()+size.getY()+10),direction.downLeft);
						}
						else if(p.last == direction.right && xDelta > 0)
						{
							new Rocket(new Vector2D(this.location.getX()+size.getX()/2,this.location.getY()+size.getY()+10),direction.downRight);
						}						
						else
						{
							new Rocket(new Vector2D(this.location.getX()+size.getX()/2,this.location.getY()+size.getY()+10),direction.down);
						}
					}
					ROCKET = false;
				}
				break;
				
			case 2: //slam into player
				SLAM=true;
				if(SLAM)
				{		
					last = direction.down;
				}
				SLAM=false;
				break;

					//move 400 down and then 400 back up
				
			case 3://HEAVY
				GIGA=true;
				if(GIGA)
				{
					//a barrage a rockets fired straight down until the next attack is called
						new Rocket(new Vector2D(this.location.getX()+size.getX()/2,this.location.getY()+size.getY()+10),direction.down);
				}
				break;
			}
		
	}
	public void Die()
	{
		ScreenObjects.LevelComplete = true;
		allScreenObjectsToRemove.addAll(allScreenObjects);
		driverGraphicsLab_Ewing.gameState = GameState.EndGame;
		driverGraphicsLab_Ewing.layout.show(driverGraphicsLab_Ewing.mainPanel, GameState.EndGame.toString());
	}
}
class midEnemy extends Enemy
{
	//andromalius is 31x50

	/**
	 * 
	 */
	private static final long serialVersionUID = -2975947620438602000L;
	private direction last;
	private float Speed = .7f;
	private Player p;
	
	public midEnemy(Vector2D location, Vector2D size, Player p) 
	{
		super(location,size);		
		this.velocity = new Vector2D(0,0);
		this.location = location;
		this.size = size;
		this.p = p;
		last = direction.right;
		this.health = 75;
		this.sprite = new Sprite(PATH+"andromaliusEDIT.png",(int)size.getX(),(int)size.getY(),16);
		allScreenObjects.add(this);
		GamePanel.allActors.add(this);
	}
	long lastUpdate = 0;
	public void Act(ArrayList<ScreenObjects> allScreenObjects)
	{
		if(health<=0)
			Die();
		Vector2D Center = new Vector2D(location.getX()-size.getX()/2, location.getY()-size.getY()/2);

		//if player is out of the 800 pixel range, squidy wont attack
		if(Vector2D.Distance(p.location, Center)>800)
			return;

		float xDelta = p.location.getX() - Center.getX();
		if(p.last == direction.left && xDelta > 0)
			velocity = velocity.multiply(.8f);
		
		else if(p.last == direction.right && xDelta < 0)
			velocity = velocity.multiply(.8f);
		
		else
		{
			//direction to player
			Vector2D dirToTarget = p.location.subtract(location);
			dirToTarget.normalize();
			velocity = velocity.add(dirToTarget).multiply(Speed);
		}
		for(ScreenObjects sO:allScreenObjects)
		if (sO instanceof Player)
		{
			Point p = new Point((int)(location.getX()+size.getX()), (int)(location.getY()));
			Point p2 = new Point((int)(location.getX()+size.getX()/2), (int)(location.getY()));
			Point p3 = new Point((int)(location.getX()), (int)(location.getY()));
			if(sO.getCollision().contains(p)||sO.getCollision().contains(p2)||sO.getCollision().contains(p3))
				health-=10;
		}		
		location = location.add(velocity);
		
		if (System.currentTimeMillis() - lastUpdate > 80)
		{
			lastUpdate = System.currentTimeMillis();
			sprite.Update();
		}
	}
	public void draw(Graphics2D g)
	{
		if(DEBUG)
		{
			g.setColor(Color.magenta);
			Rectangle collision = (Rectangle)getCollision();
			collision.x -= driverGraphicsLab_Ewing.ScreenX;
			g.draw(collision);
			g.drawString("Enemy Health: "+health, (int)location.getX()-driverGraphicsLab_Ewing.ScreenX,(int)location.getY());
			g.drawString("V: "+velocity, (int)location.getX()-driverGraphicsLab_Ewing.ScreenX,(int)location.getY()-25);
		}	
		g.drawImage(sprite.currentFrame.getScaledInstance((int)size.getX(),(int)size.getY(), BufferedImage.SCALE_FAST),
				(int)location.getX()-driverGraphicsLab_Ewing.ScreenX,(int)location.getY(),null);
		
		if(velocity.getX()<0)
		{
			last=direction.left;
			sprite.animationNumber = 1;
		}
		if(velocity.getX()>0)
		{
			last=direction.right;
			sprite.animationNumber = 0;	
		}
		if(velocity.getY()<0)
		{
			last=direction.up;
		}
		if(velocity.getY()>0)
		{
			last=direction.down;
		}
	}
	/**
	 * andromalius needed a collision that was just a little bigger than his sprite size 
	 * doing it this way makes him just a little more forgiving for the player to hit
	 */
	public Shape getCollision() 
	{
		Rectangle collision = new Rectangle((int)location.getX(),(int)location.getY(),(int)size.getX()+7,(int)size.getY()+7);
		return collision;
	}
}
class smallEnemy extends Enemy
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2982743892388388070L;
	private direction last;
	private Player p;
	public smallEnemy(Vector2D location, Vector2D size,Player p) 
	{
		super(location, size);
		this.velocity = new Vector2D(0,0);
		this.location = location;
		this.size = size;
		this.p = p;
		last = direction.right;
		this.health = 50;
		this.sprite = new Sprite(PATH+"MegaManSm.png",(int)size.getX(),(int)size.getY(),1);
		allScreenObjects.add(this);
		GamePanel.allActors.add(this);
	}
	long lastUpdate = 0;
	public void draw(Graphics2D g)
	{
		if(DEBUG)
		{
			g.setColor(Color.magenta);
			Rectangle collision = (Rectangle)getCollision();
			collision.x -= driverGraphicsLab_Ewing.ScreenX;
			g.draw(collision);
			g.drawString("Enemy Health: "+health, (int)location.getX()-driverGraphicsLab_Ewing.ScreenX,(int)location.getY());
		}	
		g.drawImage(sprite.currentFrame.getScaledInstance((int)size.getX(),(int)size.getY(), BufferedImage.SCALE_FAST),
				(int)location.getX()-driverGraphicsLab_Ewing.ScreenX,(int)location.getY(),null);
	}
}
class Enemy extends Actor implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8153004023221978721L;
	ArrayList<Enemy> allEnemy;
	private Image enemyImage;
	private direction last;
	public Vector2D velocity;
	public Sprite sprite;		
	boolean onGround, alive;
	public Enemy(Vector2D location, Vector2D size)//#1
	{
		this.size = size;
		this.location=location;
		//using ImageIcon seems to work best in the swing library
		enemyImage = new ImageIcon(PATH+"MegaManSm.png").getImage();
		velocity = new Vector2D(0,0);
		last = direction.right;
		GamePanel.allActors.add(this);
		this.health = 10;
	}
	public Shape getCollision() 
	{
		Rectangle myCollision = new Rectangle((int)location.getX(),(int)location.getY(),(int)size.getX(),(int)size.getY());
		return myCollision;
	}

	public void draw(Graphics2D g) 
	{
		if(DEBUG)
		{
			g.setColor(Color.magenta);
			Rectangle collision = (Rectangle)getCollision();
			collision.x -= driverGraphicsLab_Ewing.ScreenX;
			g.draw(collision);
		}	
		g.drawImage(enemyImage,(int)location.getX()-driverGraphicsLab_Ewing.ScreenX, (int)location.getY(), null);
		//g.setTransform(new AffineTransform());
		
	}
	long lastUpdate = 0;
	/**
	 * Default movement for all enemies go until hit a wall, then turn around
	 * @param 
	 */
	public void Act(ArrayList<ScreenObjects> allScreenObjects) 
	{
		if(health<=0)
			Die();
		//go left to right
		if (last == direction.right)
			velocity = new Vector2D(1,0);
		if (last == direction.left)
			velocity = new Vector2D(-1,0);

		//check to turn around
		for(ScreenObjects sO: allScreenObjects)
		{		
			if (sO == this)
				continue;
			if (sO instanceof Player)
			{
				Point p = new Point((int)(location.getX()+size.getX()), (int)(location.getY()));
				Point p2 = new Point((int)(location.getX()+size.getX()/2), (int)(location.getY()));
				Point p3 = new Point((int)(location.getX()), (int)(location.getY()));
				if(sO.getCollision().contains(p)||sO.getCollision().contains(p2)||sO.getCollision().contains(p3))
					health-=10;
			}
			if (last == direction.right)
			{
				Point p = new Point((int)(location.getX()+size.getX()+5), (int)(location.getY()+size.getY()/2));
				if(sO.getCollision().contains(p))
				{
					last = direction.left;
					sprite.animationNumber = 1;
				}
			}
			if (last == direction.left)
			{
				Point p = new Point((int)(location.getX()-5), (int)(location.getY()+size.getY()/2));
				if(sO.getCollision().contains(p))
				{
					last = direction.right;
					sprite.animationNumber = 0;
				}
			}
		}
		
		location = location.add(velocity);
		velocity = velocity.add(ScreenObjects.GRAVITY);		
		
		if (System.currentTimeMillis() - lastUpdate > 80)
		{
			lastUpdate = System.currentTimeMillis();
		

//		sprite.Update();
//		oneSprite.Update();
		}
	}
	@Override
	public void Die() 
	{
		EndGamePanel.enemiesDefeated++;
		alive = false;
		ScreenObjects.allScreenObjectsToRemove.add(this);
	}
}
class Player extends Actor implements KeyListener,MouseListener		//and if we want mouse clicks just implement those as well
{
	public Vector2D location, velocity;
	private int rotation; //0-360 degrees be sure to convert to raidian when used
	private boolean UP,DOWN,LEFT,RIGHT,JUMP,CLICK,ROCKET, alive, onGround;
	public direction last;
	private int playerHeight=47;
	private int playerWidth=40;
	boolean landed = false;
	boolean moving = false;

	private 	Sprite sprite = new Sprite(PATH+"SamusSheet.png",playerWidth,playerHeight,5);
	private 	Sprite oneSprite = new Sprite(PATH+"SamusSheet.png",playerWidth,playerHeight,1);
	
	public Player(Vector2D pLocation)
	{
		health=200;
		alive = true;
		location = pLocation;
		velocity = new Vector2D(0,0);
		last = direction.right;
		allScreenObjects.add(this);
		GamePanel.allActors.add(this);
	}
	public Shape getCollision()
	{
		Rectangle collision = new Rectangle(0,0,playerWidth,playerHeight);
		return collision;
	}
	public void draw(Graphics2D g)
	{
		g.translate((int)(location.getX()), (int)(location.getY()));
		if(DEBUG)
		{	//the collision box can't be drawn with the other DEBUG info or else it renders off
			g.setColor(Color.magenta);
			Rectangle collision = (Rectangle)getCollision();
			collision.x -= driverGraphicsLab_Ewing.ScreenX;
			g.draw(collision);
		}
		
		//if she moves, then give animation sprites
		if(moving)
			g.drawImage(sprite.currentFrame.getScaledInstance(playerWidth, playerHeight, BufferedImage.SCALE_FAST),
				-driverGraphicsLab_Ewing.ScreenX,0,null);
		//using the same png but with only one frame so there is no animation
		else	
			g.drawImage(oneSprite.currentFrame.getScaledInstance(playerWidth, playerHeight, BufferedImage.SCALE_FAST),
					-driverGraphicsLab_Ewing.ScreenX,0,null);
		
		g.setTransform(new AffineTransform());
		
		if(DEBUG)
		{
			//for debugging Player Events
			g.drawString("On Ground: "+onGround, 100, 100);
			g.drawString("Click: "+CLICK, 100, 125);
			g.drawString("Player Health: "+health,100,150);		
			
			//for debugging collision
			g.setColor(Color.yellow);
			//bottom
			g.fillRect((int)(location.getX()-driverGraphicsLab_Ewing.ScreenX+(playerWidth)/8),(int)location.getY()+playerHeight,3,3);
			g.fillRect((int)(location.getX()-driverGraphicsLab_Ewing.ScreenX+(playerWidth)/2),(int)location.getY()+playerHeight,3,3);
			g.fillRect((int)(location.getX()-driverGraphicsLab_Ewing.ScreenX+(playerWidth)*7/8),(int)location.getY()+playerHeight,3,3);
			//left
			g.fillRect((int)(location.getX()-driverGraphicsLab_Ewing.ScreenX),(int)(location.getY()+playerHeight*1/3),3,3);
			g.fillRect((int)(location.getX()-driverGraphicsLab_Ewing.ScreenX),(int)(location.getY()+playerHeight*2/3),3,3);
			//right
			g.fillRect((int)(location.getX()+playerWidth-driverGraphicsLab_Ewing.ScreenX),(int)(location.getY()+playerHeight*1/3),3,3);
			g.fillRect((int)(location.getX()+playerWidth-driverGraphicsLab_Ewing.ScreenX),(int)(location.getY()+playerHeight*2/3),3,3);
			//up
			g.fillRect((int)(location.getX()-driverGraphicsLab_Ewing.ScreenX+(playerWidth)/8),(int)location.getY(),3,3);
			g.fillRect((int)(location.getX()-driverGraphicsLab_Ewing.ScreenX+(playerWidth)/2),(int)location.getY(),3,3);
			g.fillRect((int)(location.getX()-driverGraphicsLab_Ewing.ScreenX+(playerWidth)*7/8),(int)location.getY() ,3,3);
		}
	}
	long lastUpdate = 0;
	long rocketUpdate = 0;
	public void Act(ArrayList<ScreenObjects> allScreenObjects)
	{
		onGround= false;
		
		if(!DEBUG)		//because death during most of debugging is just annoying
		{
			if (health <= 0||location.getY()>3000)
				Die();
		}
		else if(!alive||location.getY()>3000)
			restart();
		
		GamePanel.PHEALTH=health/2;

		double rad = Math.toRadians(rotation);
		if (UP)
		{
			last = direction.up;
		}		
		if (DOWN)
		{
			last = direction.down;
		}
		if (RIGHT)
		{
			velocity=velocity.add(new Vector2D(
					(float)Math.cos(rad),(float)Math.sin(rad)));
			last = direction.right;
		}
		if (LEFT)
		{
			velocity=velocity.add(new Vector2D(
					(float)Math.cos(rad),(float)Math.sin(rad)).multiply(-1));
			last = direction.left;
		}
		if (CLICK)
		{
			if(last == direction.left)
			{
				new Bullet(new Vector2D(location.getX()-6,location.getY()+playerHeight/3),last);
			}
			if(last == direction.up)
			{
				new Bullet(new Vector2D(location.getX()+playerWidth/2,location.getY()-6),last);
			}
			if(last == direction.right)
			{
				new Bullet(new Vector2D(location.getX()+playerWidth+5,location.getY()+playerHeight/3),last);
			}
			if(last == direction.down)
			{
				new Bullet(new Vector2D(location.getX()+playerWidth/2,location.getY()+playerHeight+5),last);
			}
			CLICK = false;
		}
		
		
		if (ROCKET)
		{
			if (System.currentTimeMillis() - rocketUpdate > 1000)
				{
					rocketUpdate = System.currentTimeMillis();
					//some type of charge animation
	
					//then whatever regular animation
					//		sprite.Update();
					//		oneSprite.Update();
				
				if(last == direction.left)
				{
					new Rocket(new Vector2D(location.getX()-6,location.getY()+playerHeight/3),last);
				}
				if(last == direction.up)
				{
					new Rocket(new Vector2D(location.getX()+playerWidth/2,location.getY()-6),last);
				}
				if(last == direction.right)
				{
					new Rocket(new Vector2D(location.getX()+playerWidth+5,location.getY()+playerHeight/3),last);
				}
				if(last == direction.down)
				{
					new Rocket(new Vector2D(location.getX()+playerWidth/2,location.getY()+playerHeight+5),last);
				}
				ROCKET = false;
			}
		}

		//handling animations
		moving = true;
		if (velocity.getX()>0.6)
		{
			last = direction.right;
			sprite.animationNumber = 8;
		}
		else if(velocity.getX()<-0.6)
		{
			last = direction.left;
			sprite.animationNumber = 9;
		}
		else
			moving = false;
		
		//Animation direction
		if (System.currentTimeMillis() - lastUpdate > 100)
		{
			lastUpdate = System.currentTimeMillis();
		
		if (!moving && last == direction.right)
			oneSprite.animationNumber = 4;
		if (!moving && last == direction.up)
			oneSprite.animationNumber = 3;
		if (!moving && last == direction.down)
			oneSprite.animationNumber = 5;
		else if (!moving && last == direction.left)
			oneSprite.animationNumber = 12;
		sprite.Update();
		oneSprite.Update();
		}				
		
		//air friction
		velocity = velocity.multiply(0.96f);
		
		//adding gravity to the player
		velocity = velocity.add(GRAVITY);
		
		Rectangle.Float myCollision = new Rectangle.Float((int)location.getX(),(int)location.getY(),playerWidth,playerHeight);
		//check Velocity to see if can still move that direction
		for(ScreenObjects sO:allScreenObjects)
		{
			Rectangle collision = (Rectangle)sO.getCollision();
			//collision detection using intersection resolved by using points
			if(myCollision.intersects(collision))
			{			
				if (sO instanceof Boss)
				{
					System.out.println("OOF");
					health -= 40;
				}
				if(sO instanceof ExitBlock)
				{
					System.out.println("Exit Block touched");
					ScreenObjects.LevelComplete=true;
				}
					
				Point2D down1 = new Point((int)(location.getX()+(playerWidth)/8),(int)location.getY()+playerHeight);
				Point2D down2 = new Point((int)(location.getX()+(playerWidth)/2),(int)location.getY()+playerHeight);
				Point2D down3 = new Point((int)(location.getX()+(playerWidth)*7/8),(int)location.getY()+playerHeight);
				if(collision.contains(down1)||collision.contains(down2)||collision.contains(down3))
				{
					if(sO instanceof Enemy)
					{
						sO.health-=10;
						if(health<200)	//prevention from getting overshields on the life steal
							health+=10;
						velocity.setY(-16);
					}
					else if(velocity.getY()>0)
					{
						velocity.setY(0);
						if(!landed)
						{
							//must be 16bit .wav
						Sounds.playSound(PATH+"LANDZ.wav");
						landed=true;
						}
					}
					onGround = true;
				}
				Point2D left1 = new Point((int)(location.getX()),(int)(location.getY()+playerHeight*1/3));
				Point2D left2 = new Point((int)(location.getX()),(int)(location.getY()+playerHeight*2/3));
				if(collision.contains(left1)||collision.contains(left2))
				{
					if (sO instanceof Enemy)
					{
						System.out.println("OOF");
						health -= 10;
					}
					if(velocity.getX()<0)
					velocity.setX(0);
				}
				Point2D right1 = new Point((int)(location.getX()+playerWidth),(int)(location.getY()+playerHeight*1/3));
				Point2D right2 = new Point((int)(location.getX()+playerWidth),(int)(location.getY()+playerHeight*2/3));
				if(collision.contains(right1)||collision.contains(right2))
				{
					if (sO instanceof Enemy)
					{
						System.out.println("OOF");
						health -= 10;
						velocity.setX(-3);
					}
					if(velocity.getX()>0)
					velocity.setX(0);
				}
				Point2D up1 = new Point((int)(location.getX()+(playerWidth)/8),(int)location.getY());
				Point2D up2 = new Point((int)(location.getX()+(playerWidth)/2),(int)location.getY());
				Point2D up3 = new Point((int)(location.getX()+(playerWidth)*7/8),(int)location.getY());
				if(collision.contains(up1)||collision.contains(up2)||collision.contains(up3))
				{
					//to help prevent going through walls he needs a small bounce back down
					velocity.setY(3);
					onGround = true;
				}
			}
		}
		if (velocity.getY()>0)
			landed=false;
		if (JUMP && onGround)
		{
			if (System.currentTimeMillis() - lastUpdate > 88)
			{
				lastUpdate = System.currentTimeMillis();
				velocity = velocity.add(new Vector2D(0,-20));

			}
		}
		if (JUMP && !onGround)
		{						//another check to keep from going through ceilings
			JUMP = false;
		}
		if (onGround)
		{	//adding ground friction
			velocity = velocity.multiply(.87f);
		}
		location = location.add(velocity);	
	}
	
	public void Die()
	{
		if(DEBUG)
			restart();
		else
		{
			alive = false;
			ScreenObjects.LevelComplete = true;
			allScreenObjectsToRemove.addAll(allScreenObjects);
			driverGraphicsLab_Ewing.gameState = GameState.GameOver;
			driverGraphicsLab_Ewing.layout.show(driverGraphicsLab_Ewing.mainPanel, GameState.GameOver.toString());
		}
	}
	public void restart()
	{
		velocity = new Vector2D(0,0);
		location = new Vector2D(200,300);
		health = 100;
		landed = false;
		moving = false;
		last = direction.right;
		alive = true;
	}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_W)
    	{
    		UP=true;
    	}
    	if (e.getKeyCode() == KeyEvent.VK_S)
    	{
    		DOWN=true;
    	}
    	if (e.getKeyCode() == KeyEvent.VK_D)
    	{
    		RIGHT=true;
    	}
    	if (e.getKeyCode() == KeyEvent.VK_A)
    	{
    		LEFT=true;
    	}
    	if (e.getKeyCode() == KeyEvent.VK_SPACE)
    	{
    		JUMP=true;
    	}
    	if (e.getKeyCode() == KeyEvent.VK_CONTROL)
    	{
    		ROCKET=true;
    	}
    	
	}
	public void keyReleased(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_W)
    	{
    		UP=false;
     	}
    	if (e.getKeyCode() == KeyEvent.VK_S)
    	{
    		DOWN=false;
    	}
    	if (e.getKeyCode() == KeyEvent.VK_D)
    	{
    		RIGHT=false;
    	}
    	if (e.getKeyCode() == KeyEvent.VK_A)
    	{
    		LEFT=false;
    	}
    	if (e.getKeyCode() == KeyEvent.VK_SPACE)
    	{
    		JUMP=false;
    	}
    	if (e.getKeyCode() == KeyEvent.VK_CONTROL)
    	{
    		ROCKET=false;
    	}
	}
	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) 
	{
			CLICK = false;
	}

	public void mouseReleased(MouseEvent e) 
	{
		CLICK = true;
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}



class Vector2D
{
    private float x;
    private float y;

    public Vector2D() 
    {
        this.setX(0);
        this.setY(0);
    }
    
    public Vector2D(float x, float y) 
    {
        this.setX(x);
        this.setY(y);
    }
    public Vector2D(Vector2D v) 
    {
        this.setX(v.getX());
        this.setY(v.getY());
    }
    
    public static double Distance(Vector2D position2, Vector2D position3) 
	{
		return Math.sqrt(Math.pow(position2.getX()-position3.getX(),2) + Math.pow(position2.getY()-position3.getY(),2));
	}
    public double Distance(Vector2D position3) 
	{
		return Math.sqrt(Math.pow(getX()-position3.getX(),2) + Math.pow(getY()-position3.getY(),2));
	}
    
    public void set(float x, float y) 
    {
        this.setX(x);
        this.setY(y);
    }

    public void setX(float x) 
    {
        this.x = x;
    }

    public void setY(float y) 
    {
        this.y = y;
    }

    public float getX() 
    {
        return x;
    }
    public float getY() 
    {    	
        return y;
    }
    public void rotate(double angle) 
    {
    	Vector2D newVect = new Vector2D(this);
		newVect.setX(getX() * (float)Math.cos(Math.toRadians(angle)) + 
				getY() * (float)Math.sin(Math.toRadians(angle)));
		newVect.setY(-getX() * (float)Math.sin(Math.toRadians(angle)) + 
				getY() * (float)Math.cos(Math.toRadians(angle)));
		this.set(newVect.getX(),newVect.getY());
    }
    //U x V = Ux*Vy-Uy*Vx
    public static float Cross(Vector2D U, Vector2D V)
    {
    	return U.x * V.y - U.y * V.x;
    }
    public float dot(Vector2D v2) 
    {
    	float result = 0.0f;
        result = this.getX() * v2.getX() + this.getY() * v2.getY();
        return result;
    }

    public float getLength() 
    {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

    public Vector2D add(Vector2D v2) 
    {
        Vector2D result = new Vector2D();
        result.setX(getX() + v2.getX());
        result.setY(getY() + v2.getY());
        return result;
    }

    public Vector2D subtract(Vector2D v2) 
    {
        Vector2D result = new Vector2D();
        result.setX(this.getX() - v2.getX());
        result.setY(this.getY() - v2.getY());
        return result;
    }

    public Vector2D multiply(float scaleFactor) 
    {
        Vector2D result = new Vector2D();
        result.setX(this.getX() * scaleFactor);
        result.setY(this.getY() * scaleFactor);
        return result;
    }

    //Specialty method used during calculations of ball to ball collisions.
    public Vector2D normalize() 
    {
    	float length = getLength();
        if (length != 0.0f) 
        {
            this.setX(this.getX() / length);
            this.setY(this.getY() / length);
        } 
        else 
        {
            this.setX(0.0f);
            this.setY(0.0f);
        }
        return this;
    }
    public String toString()
    {
    	return "("+x+", "+y+")";
    }
}