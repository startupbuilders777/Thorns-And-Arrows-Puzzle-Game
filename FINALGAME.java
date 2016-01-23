import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

//Game Programmer: Harman Singh
//Date:June 10, 2013
/*Purpose: The Mathematical/Logical game requires the user to calculate the sum of the arrows
pointing at boxes in the middle. After calculating the sum, the User must change the arrows direction's
set up around the boxes to make the sum of the arrows pointing at each box equal to the number on top of that box*/



public class FINALGAME extends Applet implements ActionListener
{
    Panel p_card; //to hold all of the screens

    GAMEPANEL game; //This is the actual panel which holds the code for the game as well the game's action performed code

    JLayeredPane playlayer, titlelayer, goodlayer, instructlayer; //p_card holds JLayeredPanes (which are just a layer of JPanels or its inherited subclasses)
    //There are 4 screens
    CardLayout cdLayout = new CardLayout ();    //The layout of the screen for screen switching

    JLabel levellab;

    int level = 1;
    int back = 0; // Instruction screen to title or to play screen?
    //Labels used in the different screens that represent different stats held by the game such as number of moves or points


    String playername; //The playername is obtained by the program. Used for the playlayer to represent the User


    String[] possibledif = {"Very Easy", "Easy", "Normal", "Hard", "Very Hard"}; //The difficulty settings
    String selecteddif; //The chosen difficulty during a play through, game - reset button for new difficulty

    //Load Images for quicker access
    ImageIcon arrowjpg = createImageIcon ("ArrowBack.jpg");     //Title Screen - www.google.com/image/ILOVDKJFA$##Sdsf.html
    ImageIcon thornjpg = createImageIcon ("THORNS.jpg");        //Title Screen - www.google.com/image/BHFSKJAGDSFGAsbf.html
    ImageIcon happy = createImageIcon ("goodjob.jpg"); //Original artwork by Harman Singh
    ImageIcon example = createImageIcon ("EXAMPLE.jpg");  //Original artwork by Harman Singh - Example pic of the game running
    //These images are loaded here and used in the GAMEPLAY subclass of the JPanel
    //They represent the directions of different arrows for the buttons surrounding the middle "thornbox"
    //They are high res, and scaled in GAMEPLAY depending the number of buttons and the size of each
     //Original artwork by Harman Singh
    ImageIcon AUP = createImageIcon ("ARROWPOINTUP.jpg");
    ImageIcon ADOWN = createImageIcon ("ARROWPOINTDOWN.jpg");
    ImageIcon ALEFT = createImageIcon ("ARROWPOINTLEFT.jpg");
    ImageIcon ARIGHT = createImageIcon ("ARROWPOINTRIGHT.jpg");
    ImageIcon AUPLEFT = createImageIcon ("ARROWPOINTUPLEFT.jpg");
    ImageIcon AUPRIGHT = createImageIcon ("ARROWPOINTUPRIGHT.jpg");
    ImageIcon ADLEFT = createImageIcon ("ARROWPOINTDLEFT.jpg");
    ImageIcon ADRIGHT = createImageIcon ("ARROWPOINTDRIGHT.jpg");
    ImageIcon BIGTHORNS = createImageIcon ("THORNSCOUNT.jpg"); //www.natureandlife.com/nature/beauty


    public void init ()  //Initialize and setup main screens and cdLayout
    {

	p_card = new Panel ();
	p_card.setLayout (cdLayout);


	//make the 6 screens


	inittitle ();
	initinstructions ();
	initplayscreen ();
	initgoodscreen ();


	resize (1024, 668); //original was 768, but school monitors would not accept that resolution
	setLayout (new BorderLayout ());
	add ("Center", p_card);

    }


    //Title Screen that appears when Game is loaded



    public void inittitle ()
    {
	titlelayer = new JLayeredPane (); //Overlays the title panel, a 2d graphic background, 2d animation


	TransparentPanel titlescreen = new TransparentPanel (); //Main title panel
	titlescreen.setLayout (new BorderLayout ());

	TransparentPanel infopanel = new TransparentPanel (); //Holds the different buttons appeaing in Title
	infopanel.setLayout (new GridLayout (4, 1));

	JLabel thornlab = new JLabel (thornjpg); //The Thorn Picture
	JLabel arrowlab = new JLabel (arrowjpg); //The Arrow Picture


	JLabel title = new JLabel ("                  THORNS and ARROWS");
	title.setFont (new Font ("Arial", Font.BOLD, 50));
	title.setForeground (Color.blue);


	JButton enter = new JButton ("Enter...");
	enter.setFont (new Font ("Broadway", Font.BOLD, 14));
	enter.setForeground (Color.BLUE);
	enter.setBackground (Color.yellow);
	enter.setPreferredSize (new Dimension (120, 120));
	enter.addActionListener (this);
	enter.setActionCommand ("play"); //Leads to platlayer

	JButton exit = new JButton ("Exit");
	exit.setFont (new Font ("Arial", Font.ITALIC, 15));
	exit.setForeground (Color.black);
	exit.setBackground (Color.green);
	exit.setPreferredSize (new Dimension (120, 120));
	exit.addActionListener (this);
	exit.setActionCommand ("exit"); //Stops game



	JButton instructionbut = new JButton ("Instructions");
	instructionbut.setFont (new Font ("Arial", Font.BOLD, 12));
	instructionbut.setForeground (Color.black);
	instructionbut.setBackground (Color.green);
	instructionbut.setPreferredSize (new Dimension (120, 120));
	instructionbut.addActionListener (this);
	instructionbut.setActionCommand ("instructionstitle"); //Leads to instruction screen

	infopanel.add (enter);
	infopanel.add (instructionbut);
	infopanel.add (exit);


	titlescreen.add (title, BorderLayout.NORTH);
	titlescreen.add (infopanel, BorderLayout.EAST);
	titlescreen.add (thornlab, BorderLayout.WEST);
	titlescreen.add (arrowlab, BorderLayout.CENTER);


	//Background panel 2D graphics green to blue
	BackPanel green = new BackPanel ();

	//ArrowPanel 2D animation with timer Arrows shooting from top left to bottom right
	ArrowPanel flyingarrows = new ArrowPanel ();
	flyingarrows.start ();

	// Set Bounds is needed because JLayeredPanes do not have a default Layout Manager
	titlelayer.setBounds (0, 0, 1024, 768);

	titlescreen.setBounds (0, 0, 1024, 768);
	flyingarrows.setBounds (0, 0, 1024, 768);
	green.setBounds (0, 0, 1024, 768);


	//Panels Overlayed: titlescreen over background over arrows animation
	titlelayer.add (titlescreen, new Integer (2));
	titlelayer.add (flyingarrows, new Integer (0));
	titlelayer.add (green, new Integer (1));

	p_card.add ("title", titlelayer); // The "password" for the pane is "title"


    }


    public void initplayscreen ()
    {
	TransparentPanel playscreen = new TransparentPanel (); //The main screen set up
	playscreen.setLayout (new BorderLayout ());

	game = new GAMEPANEL (); //Game Panel is the class which contains the actual game
	game.PanelBuilder (2, 6, game); //harman is the biggest pountank in the world


	//Builds the Button setup for the game to take place
	//as well as sets up arrays and the Actioncommands for the buttons to GAMEPANEL's Action Listener.


	//Different JLables and JButtons on the play screen set up here:

	levellab = new JLabel ("Level 1: ");
	levellab.setFont (new Font ("Arial", Font.BOLD, 60));
	levellab.setForeground (Color.black);
	levellab.setBackground (Color.green);

	JButton instructionbut = new JButton ("Instructions");
	instructionbut.setFont (new Font ("Arial", Font.BOLD, 12));
	instructionbut.setForeground (Color.black);
	instructionbut.setBackground (Color.green);
	instructionbut.addActionListener (this);
	instructionbut.setActionCommand ("instructionsplay"); //Go to instructions screen

	JButton menuback = new JButton ("Back to Menu");
	menuback.setFont (new Font ("Arial", Font.BOLD, 12));
	menuback.setForeground (Color.black);
	menuback.setBackground (Color.green);
	menuback.addActionListener (this);
	menuback.setActionCommand ("title"); //Go to titlescreen

	JButton done = new JButton ("DONE");
	done.setFont (new Font ("Arial", Font.BOLD, 12));
	done.setForeground (Color.black);
	done.setBackground (Color.green);
	done.addActionListener (this);
	done.setActionCommand ("done"); //Check if puzzle is correct and go to next puzzle


	JButton reset = new JButton ("RESET");
	reset.setFont (new Font ("Arial", Font.BOLD, 12));
	reset.setForeground (Color.black);
	reset.setBackground (Color.green);
	reset.addActionListener (this);
	reset.setActionCommand ("reset"); //Check if puzzle is correct and go to next puzzle

	TransparentPanel bottomplay = new TransparentPanel (); //Set up to hold all the JButtons excluding those made by GAMEPANEL
	bottomplay.setLayout (new GridLayout (1, 4));
	bottomplay.add (reset);
	bottomplay.add (menuback);
	bottomplay.add (instructionbut);
	bottomplay.add (done);



	// bottomplay.setPreferredSize (new Dimension (768, 140)); //fix for small resolution monitors


	//The different Panels and the gamepanel added to the main painel for this screen
	playscreen.add (bottomplay, BorderLayout.SOUTH);
	playscreen.add (game, BorderLayout.CENTER);
	playscreen.add (levellab, BorderLayout.NORTH);


	//The 2D graphics background in the back
	BackPanel green = new BackPanel ();

	playlayer = new JLayeredPane (); //Main layered pane for this screen

	playlayer.setBounds (0, 0, 1024, 668);
	green.setBounds (0, 0, 1024, 668);
	playscreen.setBounds (0, 0, 1024, 668);

	playlayer.add (green, new Integer (0));
	playlayer.add (playscreen, new Integer (1));

	p_card.add ("play", playlayer);
    }




    public void initinstructions ()  //The instruction screen is set up
    {
	TransparentPanel instructscreen = new TransparentPanel ();
	//THE INSTRUCTIONS
	JTextArea TA = new JTextArea (5, 15);
	TA.setForeground (Color.white);
	TA.setBackground (Color.black);
	TA.setFont (new Font ("Calibri", Font.BOLD, 13));

	TA.setText ("Welcome to THORNS and ARROWS. You are an archer and you must destroy \n");
	TA.append ("those blasted thorns growing on your boxes. You do not want to touch the \n");
	TA.append ("thorns however because they are poisonous. Instead you have a bunch of arrows\n");
	TA.append ("and you choose to shoot down the thorns on each box from a far instead.\n");
	TA.append ("The problem however, is you only have a few arrows and alot more thorns.\n");
	TA.append ("You coat your arrows with a potion which allow them to go through multiple thorns\n");
	TA.append ("at once. With potion in hand, you decide to clear away those pesky thorns.\n\n");

	TA.append ("You begin by figuring out how many thorns are on each box. Counting the all you realize\n");
	TA.append ("that you can set up vintage points around the thorns in a square like pattern to shoot them all down. \n");
	TA.append ("You choose to only aim your arrows at the boxes because you are not stupid, and you remember that you\n");
	TA.append ("have a calculator to add up how many arrows are shooting a particular box at one time.\n");

	TA.append ("You begin by figuring out how many thorns are on each box. Counting them all you realize\n");
	TA.append ("that you can set up vintage points around the thorns in a square like pattern to shoot them all down. \n");
	TA.append ("You choose to only aim your arrows at the boxes because you are not stupid, and you remember that you\n");
	TA.append ("have a calculator to add up how many arrows are shooting at particular box at one time.\n\n");

	TA.append ("Click on the vintage points to change the direction of the arrows and click on the boxes to use your calculator\n");
	TA.append ("Click done when you are able to clear all the thorns off the boxes with your current set up. You will recieve new boxes which have more thorns on them\n\n");

	TA.append ("Good Luck!!!");


	JLabel pic = new JLabel (example); //Example pic of the game running

	JButton back = new JButton ("BACK"); //Goes back to either the title or the play screen depending on where instructions was accessed
	back.setFont (new Font ("Arial", Font.BOLD, 12));
	back.setForeground (Color.black);
	back.setBackground (Color.green);
	back.addActionListener (this);
	back.setActionCommand ("back");
	back.setPreferredSize (new Dimension (100, 100));

	instructscreen.add (TA);
	instructscreen.add (pic);
	instructscreen.add (back);


	//The 2D graphics background in the back
	BackPanel green = new BackPanel ();

	instructlayer = new JLayeredPane (); //Main layered pane for this screen

	instructlayer.setBounds (0, 0, 1024, 668);
	green.setBounds (0, 0, 1024, 668);
	instructscreen.setBounds (0, 0, 1024, 668);

	instructlayer.add (green, new Integer (0));
	instructlayer.add (instructscreen, new Integer (1));

	p_card.add ("instructions", instructlayer);
    }



    public void initgoodscreen ()  //GOOD JOB: the screen is just a way to reset the play screen for the new level
    {
	goodlayer = new JLayeredPane (); 

	TransparentPanel goodscreen = new TransparentPanel ();

	JButton goodbutton = new JButton (happy); //Appears when Level is completed
	goodbutton.setActionCommand ("play");
	goodbutton.addActionListener (this);
	goodbutton.setPreferredSize (new Dimension (900, 600));
	goodscreen.add (goodbutton);

	//Background panel 2D graphics green to blue
	BackPanel green = new BackPanel ();

	// Set Bounds is needed because JLayeredPanes do not have a default Layout Manager
	goodlayer.setBounds (0, 0, 1024, 668);

	goodscreen.setBounds (0, 0, 1024, 668);
	green.setBounds (0, 0, 1024, 668);


	//Panels Overlayed: titlescreen over background over arrows animation
	goodlayer.add (goodscreen, new Integer (2));
	goodlayer.add (green, new Integer (1));

	p_card.add ("good", goodlayer);        // The "password" for the pane is "goodlayer"
    }


    //level determines size of square that needs to be completed
    //difficulty determines how much of the square you need to complete
    public void nextlevel (String difficulty, int level)
    {
	
    
    //The difficulty is dependent on the level and the difficulty setting
    //There are 50 levels in the game, and the game gets progressively harder
    //The code was hardwired because there is no real algorithm to determine a good game setting
    //Panel Builder is used to set how many thorns there arein the game and how many are already given to the user
    
	if (difficulty.equals ("Very Easy"))    //Level 1 to 6 first, then level
	{
	    if (level < 5)
	      game.PanelBuilder (level + 1, ((level + 1) * 4) - 3, game); //Panel builder (size, arrows given, panel used in Play screen)

	     else if (level < 10)
		game.PanelBuilder (level - 3, (level * 4) - 4, game);

	    else if (level < 15)
		game.PanelBuilder (level - 8, (level * 4) - 5, game);

	    else if (level < 20)
		game.PanelBuilder (level - 13, (level * 4) - 6, game);

	    else if (level < 25)
		game.PanelBuilder (level - 18, (level * 4) - 7, game);

	    else if (level < 30)
		game.PanelBuilder (level - 23, (level * 4) - 8, game);

	    else if (level < 35)
		game.PanelBuilder (level - 28, (level * 4) - 9, game);

	    else if (level < 40)
		game.PanelBuilder (level - 33, (level * 4) - 10, game);

	    else if (level < 45)
		game.PanelBuilder (level - 38, (level * 4) - 11, game);

	    else if (level < 49)
		game.PanelBuilder (level - 43, (level * 4) - 11, game);

	    else if (level == 50)
		game.PanelBuilder (3, 0, game);

	}



	else if (difficulty.equals ("Easy"))
	{
	    if (level < 5)
		game.PanelBuilder (level + 1, ((level + 1) * 4) - 4, game);

	    else if (level < 10)
		game.PanelBuilder (level - 3, (level * 4) - 5, game);

	    else if (level < 15)
		game.PanelBuilder (level - 8, (level * 4) - 6, game);

	    else if (level < 20)
		game.PanelBuilder (level - 13, (level * 4) - 7, game);

	    else if (level < 25)
		game.PanelBuilder (level - 18, (level * 4) - 8, game);

	    else if (level < 30)
		game.PanelBuilder (level - 23, (level * 4) - 9, game);

	    else if (level < 35)
		game.PanelBuilder (level - 28, (level * 4) - 10, game);

	    else if (level < 40)
		game.PanelBuilder (level - 33, (level * 4) - 11, game);

	    else if (level < 45)
		game.PanelBuilder (level - 38, (level * 4) - 12, game);

	    else if (level < 49)
		game.PanelBuilder (level - 42, (level * 4) - 13, game);

	    else if (level == 50)
		game.PanelBuilder (5, 0, game);

	}

	else if (difficulty.equals ("Normal"))
	{
	    if (level < 5)
		game.PanelBuilder (level + 1, ((level + 1) * 4) - 3, game);

	    else if (level < 10)
		game.PanelBuilder (level - 2, (level * 4) - 4, game);

	    else if (level < 15)
		game.PanelBuilder (level - 6, (level * 4) - 5, game);

	    else if (level < 20)
		game.PanelBuilder (level - 10, (level * 4) - 6, game);

	    else if (level < 25)
		game.PanelBuilder (level - 14, (level * 4) - 7, game);

	    else if (level < 30)
		game.PanelBuilder (level - 18, (level * 4) - 8, game);

	    else if (level < 35)
		game.PanelBuilder (level - 22, (level * 4) - 9, game);

	    else if (level < 40)
		game.PanelBuilder (level - 26, (level * 4) - 10, game);

	    else if (level < 45)
		game.PanelBuilder (level - 30, (level * 4) - 11, game);

	    else if (level < 49)
		game.PanelBuilder (level - 34, (level * 4) - 11, game);

	    else if (level == 50)
		game.PanelBuilder (7, 0, game);

	}



	else if (difficulty.equals ("Hard"))
	{
	    if (level < 5)
		game.PanelBuilder (level + 1, ((level + 1) * 4) - 3, game);

	    else if (level < 10)
		game.PanelBuilder (level + 1, (level * 4) - 3, game);

	    else if (level < 15)
		game.PanelBuilder (level - 10, (level * 4) - 5, game);

	    else if (level < 20)
		game.PanelBuilder (level - 10, (level * 4) - 5, game);

	    else if (level < 25)
		game.PanelBuilder (level - 14, (level * 4) - 7, game);

	    else if (level < 30)
		game.PanelBuilder (level - 14, (level * 4) - 7, game);

	    else if (level < 35)
		game.PanelBuilder (level - 22, (level * 4) - 9, game);

	    else if (level < 40)
		game.PanelBuilder (level - 22, (level * 4) - 9, game);

	    else if (level < 45)
		game.PanelBuilder (level - 30, (level * 4) - 11, game);

	    else if (level < 49)
		game.PanelBuilder (level - 30, (level * 4) - 11, game);

	    else if (level == 50)
		game.PanelBuilder (8, 0, game);

	}



	else
	{

	    if (level < 5)
		game.PanelBuilder (level + 3, 15, game);

	    else if (level < 10)
		game.PanelBuilder (level, 35, game);

	    else if (level < 15)
		game.PanelBuilder (level, 43, game);

	    else if (level < 20)
		game.PanelBuilder (level, 63, game);

	    else if (level < 25)
		game.PanelBuilder (level, 83, game);

	    else if (level < 30)
		game.PanelBuilder (level, 103, game);

	    else if (level < 35)
		game.PanelBuilder (level, 123, game);

	    else if (level < 40)
		game.PanelBuilder (level, 143, game);

	    else if (level < 45)
		game.PanelBuilder (level, 163, game);

	    else if (level < 49)
		game.PanelBuilder (level, 183, game);

	    else if (level == 50)
		game.PanelBuilder (13, 0, game);

	}
    }


    public void actionPerformed (ActionEvent e)
    {

	if (e.getActionCommand ().equals ("title"))
	    cdLayout.show (p_card, "title");


	else if (e.getActionCommand ().equals ("instructionsplay") || e.getActionCommand ().equals ("instructionstitle"))
	{
	
	    if (e.getActionCommand ().equals ("instructionsplay"))
	    {
		cdLayout.show (p_card, "instructions");
		back = 1;//to find where instructions was accessed -playscreen
	    }
	    if (e.getActionCommand ().equals ("instructionstitle"))
	    {
		cdLayout.show (p_card, "instructions");
		back = 2;//to find where instructions was accessed - Menu Title
	    }


	}

	else if (e.getActionCommand ().equals ("back"))
	{
	    if (back == 1) //Return to playscreen
	    {
		cdLayout.show (p_card, "play");
	    }
	    if (back == 2)//Return to instructions
	    {
		cdLayout.show (p_card, "title");
	    }


	}

	else if (e.getActionCommand ().equals ("play"))
	{
	    cdLayout.show (p_card, "play"); //When play button is clicked, the User's name is always prompted
	    if (selecteddif == null) //Choose a difficulty only when there is no difficulty setting
	    {
		selecteddif = (String) JOptionPane.showInputDialog (null,
			"Choose one", "Input", JOptionPane.INFORMATION_MESSAGE, null,
			possibledif, possibledif [0]);
	    }
	}


	else if (e.getActionCommand ().equals ("done"))
	{
	    if (game.didyouwin () == true) //Accesses method in playscreen class to check if game is won
	    {
		cdLayout.show (p_card, "good"); //resets screen for new level
		nextlevel (selecteddif, level);
		level++;
		levellab.setText ("Level: " + level); //Change level
	    }
	    else
	    {
		showStatus ("Nic try, but NO.");

	    }

	}


	else if (e.getActionCommand ().equals ("reset"))
	{
	    int resetchoice = JOptionPane.showConfirmDialog (null, "ARE YOU SURE????",
		    "Are you sure you want to reset the game?", JOptionPane.YES_NO_OPTION);

	    if (resetchoice == 0) //Resets playscreen to level 1 with ability to choose new difficulty
	    {
		level = 1;
		levellab.setText ("Level: " + level);
		selecteddif = null;
		cdLayout.show (p_card, "title");
		game.PanelBuilder (2, 6, game); //Builds default level 1 screen
	    }

	    else if (resetchoice == 1)
	    {
		showStatus ("Good Boy.");  //KEEP PLAYING 
	    }
	}

	else if (e.getActionCommand ().equals ("exit"))
	    System.exit (0); //Game exit

    }



    class GAMEPANEL extends JPanel implements ActionListener //Has its own ActionPerformed method
    {
	//The different arrows images are saved again here so that when they are scaled,
	//they do not change the original arrow pciture and allow for multiple instances of the GAMEPANEL for multiple randomized levels

	ImageIcon aUP = AUP;
	ImageIcon aDOWN = ADOWN;
	ImageIcon aLEFT = ALEFT;
	ImageIcon aRIGHT = ARIGHT;
	ImageIcon aUPLEFT = AUPLEFT;
	ImageIcon aUPRIGHT = AUPRIGHT;
	ImageIcon aDLEFT = ADLEFT;
	ImageIcon aDRIGHT = ADRIGHT;

	ImageIcon bigthorns = BIGTHORNS;

	JButton[] [] thornbox;
	//The thornbox is the array set up in the middle,
	//which acts as the graphics for the game. It contains both the arrow VANTAGE points around the thorns 
	//MORE ON GRAPHICS THAN ACTUALLY DOING SOMETHING

	int[] [] count;
	//Holds the next direction that will appear when an arrow button is clicked
	//and is a sort of password to check which direction arrow boxes are pointing and to what direction the arrow button should point when the arrow button is clicked and an action is performed
	int[] [] currentdir; //holds the current set up of the arrows

	int[] [] sum; //FINDS THE CURRENT SUM OF ARROWS FOR ALL ARROWS AND STORES IN HERE 

	int[] [] randomizer; //HOLDS THE ANSWER WHICH IS RANDOMIZED

	int[] [] keepsomearrows; //Chooses which arrows should alrady be available to the user using a 0 or a 1


	int globallength = 0; // Global Length changes for every instance. It is the length of the thornboxes + 2 (The 2 accounts for the fact that arrows are set up around the thornboxes on each side like a square in a square)
	int row = 0, col = 0; //The row and column of the whole JButton array (row = col cause it is a square). Used as placeholders

	String[] coordinateparts;
	//Used by actionperformed to split the action command sent by a Jbutton to interperate 2 coordinates


	public void PanelBuilder (int length, int given, GAMEPANEL p)  //length of panel and the game panel instance that wants to be set up are the parameters
	{

	    //The method assumes a new level is beginning over an old one, so it clears the panel and fixes it up before it is set up again
	    p.removeAll ();
	    revalidate ();



	    p.setLayout (new GridLayout (length + 2, length + 2, 0, 0));

	    int row = length + 2;
	    int col = length + 2;
	    globallength = length + 2;

	    //Thorn Box setup w/ Action Command based on its coordinates. The corners were chopped off for Math/Logic reasons  (They would always be pointing in 1 direction, user could not change it)
	    thornbox = new JButton [row] [col];
	    for (int r = 0 ; r < row ; r++)
	    {
		for (int c = 0 ; c < col ; c++)
		{
		    thornbox [r] [c] = new JButton ();
		    p.add (thornbox [r] [c]);

		    if ((r == 0 && c == 0) || (r == 0 && c == length + 1) || (r == length + 1 && c == length + 1) || (r == length + 1 && c == 0))
			thornbox [r] [c].setVisible (false); //The corners are cut off for logistic reasons

		    thornbox [r] [c].addActionListener (this);
		    thornbox [r] [c].setBackground (Color.red);
		    thornbox [r] [c].setPreferredSize (new Dimension (p.getWidth () / length, p.getWidth () / length)); //The buttons are resized based on how many there are
		    thornbox [r] [c].setActionCommand (r + "and" + c);
		    thornbox [r] [c].setForeground (new Color (100, 255, 100));
		    thornbox [r] [c].setFont (new Font ("SansSerif", Font.BOLD, 500 / length));

		}
	    }


	    //The imageIcon arrow directions are resized based on the button size
	    aUP = RESIZE (AUP, thornbox [1] [1]);
	    aDOWN = RESIZE (ADOWN, thornbox [1] [1]);
	    aLEFT = RESIZE (ALEFT, thornbox [1] [1]);
	    aRIGHT = RESIZE (ARIGHT, thornbox [1] [1]);
	    aUPLEFT = RESIZE (AUPLEFT, thornbox [1] [1]);
	    aUPRIGHT = RESIZE (AUPRIGHT, thornbox [1] [1]);
	    aDLEFT = RESIZE (ADLEFT, thornbox [1] [1]);
	    aDRIGHT = RESIZE (ADRIGHT, thornbox [1] [1]);
	    bigthorns = RESIZE (BIGTHORNS, thornbox [1] [1]);


	    //All the essential arrays for tha gaame are set up
	    //For count and current dir, all the buttons that are outside the thornbox represent Arrow Vantage points which have default arrow directions attached to them
	    //1 = Down, 2 = DLEFT, 3 = DRIGHT, 4 = RIGHT, 5 = LEFT, 6=UP, 7= UPLEFT, 8 = UPRIGHT
	    
	    count = new int [row] [col];
	    currentdir = new int [row] [col];
	    sum = new int [row] [col];
	    randomizer = new int [row] [col];
	    keepsomearrows = new int [row] [col];
	    
	    for (int r = 0 ; r < row ; r++)
	    {
		for (int c = 0 ; c < col ; c++)
		{
		    keepsomearrows [r] [c] = 0; // Given arrows arent given yet!



		    if (r == 0 && c == 1)//top arrow layer right corner 
		    {
			count [r] [c] = 3;  //next pic in arrow cycle
			currentdir [r] [c] = 1; //current pic in arrow cycle
			thornbox [r] [c].setIcon (aDOWN); //Default for the layer 

		    }

		    else if (r == 0 && c == globallength - 2) // Top arrow Layer left corner
		    {
			count [r] [c] = 2;  //next pic in cycle
			currentdir [r] [c] = 1; //curret pic in cycle
			thornbox [r] [c].setIcon (aDOWN); //Default for the layer
		    }

		    else if (r == globallength - 1 && c == 1) //Bottom arrow layer right corner
		    {
			count [r] [c] = 8; //next pic in cycle
			currentdir [r] [c] = 6;//curret pic in cycle
			thornbox [r] [c].setIcon (aUP);
		    }


		    else if (r == globallength - 1 && c == globallength - 2) //Bottom arrow layer left corner
		    {
			count [r] [c] = 7; //next pic in cycle
			currentdir [r] [c] = 6;//curret pic in cycle
			thornbox [r] [c].setIcon (aUP);
		    }


		    else if (c == 0 && r == 1) //Left arrow layer top corner
		    {
			count [r] [c] = 3; //next pic in cycle
			currentdir [r] [c] = 4;//curret pic in cycle
			thornbox [r] [c].setIcon (aRIGHT);
		    }


		    else if (c == 0 && r == globallength - 2) //Left arrow layer bottom corner
		    {
			count [r] [c] = 8; //next pic in cycle
			currentdir [r] [c] = 4;//curret pic in cycle
			thornbox [r] [c].setIcon (aRIGHT);
		    }

		    else if (c == globallength - 1 && r == 1) //Right arrow layer top corner
		    {
			count [r] [c] = 2; //next pic in cycle
			currentdir [r] [c] = 5;//curret pic in cycle
			thornbox [r] [c].setIcon (aLEFT);
		    }


		    else if (c == globallength - 1 && r == globallength - 2)//Right arrow layer bottom corner
		    {
			count [r] [c] = 7; //next pic in cycle
			currentdir [r] [c] = 5;//curret pic in cycle
			thornbox [r] [c].setIcon (aLEFT);
		    }

		    else if (r == 0) //Top layer no corners
		    {
			count [r] [c] = 2;  //next pic in cycle
			currentdir [r] [c] = 1;//curret pic in cycle
			thornbox [r] [c].setIcon (aDOWN);
		    }
		    else if (r == globallength - 1) //Bottom layer no corners
		    {
			count [r] [c] = 7; //next pic in cycle
			currentdir [r] [c] = 6;//curret pic in cycle
			thornbox [r] [c].setIcon (aUP);
		    }
		    else if (c == 0)//left layer no corners
		    {
			count [r] [c] = 8; //next pic in cycle
			currentdir [r] [c] = 4;//curret pic in cycle
			thornbox [r] [c].setIcon (aRIGHT);
		    }
		    else if (c == globallength - 1)//Right layer no corners
		    {
			count [r] [c] = 7; //next pic in cycle
			currentdir [r] [c] = 5;//curret pic in cycle
			thornbox [r] [c].setIcon (aLEFT);
		    }
		    else
		    {
			count [r] [c] = 0; //if it is not an arrow direction, then it is a thorn box

		    }
		}
	    }

	    randomizelvl (); //Creates level, randomizes level, provides answers, 
	    chooserandomdir (given); //How many arrows do you want to be given? Given is an int parameter

	}


	//Method which resizes ImageIcon
	public ImageIcon RESIZE (ImageIcon icon, JButton b)
	{
	    Image image = icon.getImage (); // transform it
	    int lengthx = 0;

	    if ((globallength - 2) < 3)
		lengthx = 400 / (globallength - 2);

	    else if ((globallength - 2) < 6)
		lengthx = 550 / (globallength - 2);

	    else
		lengthx = 650 / (globallength - 2);

	    Image newimg = image.getScaledInstance (lengthx, lengthx, java.awt.Image.SCALE_SMOOTH);  // scale it the smooth way
	    icon = new ImageIcon (newimg); // transform it back
	    return icon;
	}



	//Override Paint Component  for custom graphics (2d graphic gradient)

	public void paintComponent (Graphics g)
	{
	    super.paintComponent (g);
	    Graphics2D g2 = (Graphics2D) g; //2D Background
	    int w = getWidth ();
	    int h = getHeight ();
	    GradientPaint greentoyellow = new GradientPaint (0, 0, Color.yellow, 1024, 768, Color.blue); //Yellow to Blue
	    g2.setPaint (greentoyellow);
	    g2.fillRect (0, 0, w, h);
	    setBackground (new Color (0, 0, 0, 0));
	    setOpaque (false);
	}


	public void randomizedir (int r, int c) //Clicks the arrow button several times to randomize, the parameters are the coordinates of tha arrows
	{
	    int x = (int) (Math.random () * 3) + 1;
	    for (int i = 0 ; i < x ; i++)
		thornbox [r] [c].doClick ();
	}


	public void randomizelvl () 
	{

	    for (int i = 1 ; i < globallength - 1 ; i++) //Randomize all the buttons by clciking on them using the Randomizedir method
	    {
		randomizedir (0, i);
		randomizedir (globallength - 1, i);
		randomizedir (i, 0);
		randomizedir (i, globallength - 1);
	    }

	    check (currentdir, sum); //Find the sum of all the arrows pointing at each thorn box

	    for (int r = 0 ; r < sum.length ; r++) 
	    {
		for (int c = 0 ; c < sum.length ; c++)
		{

		    if (r != 0 && c != 0 && r != globallength - 1 && c != globallength - 1)

			{
			    randomizer [r] [c] = sum [r] [c];//Store the answer to randomizer so that sum can be used again  (such as checking whether user's answer is correct)
			    thornbox [r] [c].setText ("" + randomizer [r] [c]); //Put the "question" on each thorn box
			    thornbox [r] [c].setIcon (bigthorns);
			   //Center the number over the image icon on the Thorn boxes
			    thornbox [r] [c].setHorizontalTextPosition (JButton.CENTER); 
			    thornbox [r] [c].setVerticalTextPosition (JButton.CENTER);

			}

		}
	    }
	}



	public void chooserandomdir (int x) //How may arrows do you want to be already give?? (that is what the parameter is) 
	{

	//The arrows given are in random locations
	    
	    int num = 0; //the counter to check if all of the arrows are given to the user
	    int choosesection = 0; //Which layer do you want to randomize? (top,bottom,right,left)
	    int choosepart = 0; //Which part of the layer do you want to randomize? (The second arrow button on the right layer for instace)
	    
	    while (num < x) 
	    {
	    //Randomize
		choosesection = (int) ((Math.random () * 4) + 1);
		choosepart = (int) (Math.random () * keepsomearrows.length);

		
		//The decision statements check whther the arrow is in the top, bottom, left, or right arrow layer
		//The decision statements also make sure that the corners cut off from the arrow layers do ot count for the giving the arrows
		
		if (choosesection == 1 && choosepart != 0 && choosepart != globallength - 1 && keepsomearrows [0] [choosepart] != 1) 
		{
		    keepsomearrows [0] [choosepart] = 1; //Sets the arrow as given
		    thornbox [0] [choosepart].setEnabled (false); //Disables its current postion (since it has not changed from its randomization when initializing the game)
		    num++; //Get closer to X
		}
		
	       
		else if (choosesection == 2 && choosepart != 0 && choosepart != globallength - 1 && keepsomearrows [choosepart] [0] != 1)
		{
		    keepsomearrows [choosepart] [0] = 1;
		    thornbox [choosepart] [0].setEnabled (false);
		    num++;
		}

		else if (choosesection == 3 && choosepart != 0 && choosepart != globallength - 1 && keepsomearrows [globallength - 1] [choosepart] != 1)
		{
		    keepsomearrows [globallength - 1] [choosepart] = 1;
		    thornbox [globallength - 1] [choosepart].setEnabled (false);
		    num++;
		}

		else if (choosesection == 4 && choosepart != 0 && choosepart != globallength - 1 && keepsomearrows [choosepart] [globallength - 1] != 1)
		{
		    keepsomearrows [choosepart] [globallength - 1] = 1;
		    thornbox [choosepart] [globallength - 1].setEnabled (false);
		    num++;
		}
	    }


	    for (int i = 1 ; i < globallength - 1 ; i++)
	    {
		randomizedir (0, i);
		randomizedir (globallength - 1, i);
		randomizedir (i, 0);
		randomizedir (i, globallength - 1);
	    }

	}


	//Adds up the number of arrows pointing at each thorn box
	public void check (int currentdir[] [], int[] [] sum)

	{
	    for (int r = 0 ; r < sum.length ; r++)
	    {
		for (int c = 0 ; c < sum.length ; c++)
		{
		    sum [r] [c] = 0;
		}
	    }

	    //Not part of the objective of this method, this code just randomizes all the arrows that have not been retained and given to the user 
	    for (int i = 1 ; i < globallength - 1 ; i++)
	    {
		sum = POINTERSUM (0, i, currentdir, sum);
		sum = POINTERSUM (globallength - 1, i, currentdir, sum);
		sum = POINTERSUM (i, 0, currentdir, sum);
		sum = POINTERSUM (i, globallength - 1, currentdir, sum);
	    }
	}



	//Depending on the direction of the arrows, the sum is added up diagonally, vertically, or horizontally
	//This method checks the direction and adds to the thornbox appropriately



	public int[] [] POINTERSUM (int r, int c, int currentdir[] [], int sum[] [])
	{
	    
	//The code cheks what direction the arrow is pointing using currentdir array
	//Then the code keeps adding in that specific direction until the next square is an arrow button
	
	    if (currentdir [r] [c] == 1) //Down
	    {
		for (int i = 1 ; i < globallength - 1 ; i++)
		{
		    sum [i] [c] = sum [i] [c] + 1;  //Keep adding down
		}

	    }

	    else if (currentdir [r] [c] == 4) //Right
	    {
		for (int i = 1 ; i < globallength - 1 ; i++)
		{
		    sum [r] [i] = sum [r] [i] + 1; //Keep adding right
		}

	    }

	    else if (currentdir [r] [c] == 5) //Left
	    {
		for (int i = globallength - 2 ; i > 0 ; i--)
		{
		    sum [r] [i] = sum [r] [i] + 1;  //Keep adding left
		}

	    }

	    else if (currentdir [r] [c] == 6) //UP
	    {
		for (int i = globallength - 2 ; i > 0 ; i--)
		{
		    sum [i] [c] = sum [i] [c] + 1;//Keep adding up
		}
	    }

	    else if (currentdir [r] [c] == 2) //DLeft
	    {
		int y = r + 1;

		int i = c - 1;

		while (y < globallength - 1 && i > 0)   //Keep adding down left
		{
		    sum [y] [i] = sum [y] [i] + 1;
		    y++;
		    i--;
		}

	    }

	    else if (currentdir [r] [c] == 8) //UPright
	    {

		int y = r - 1;

		int i = c + 1;

		while (i < globallength - 1 && y > 0) //Keep adding up right and so on for the rest
		{
		    sum [y] [i] = sum [y] [i] + 1;
		    y--;
		    i++;
		}

	    }

	    else if (currentdir [r] [c] == 3) //DRight
	    {

		int y = r + 1;

		int i = c + 1;

		while (i < globallength - 1 && y < globallength - 1)
		{
		    sum [y] [i] = sum [y] [i] + 1;

		    y++;
		    i++;
		}

	    }

	    else if (currentdir [r] [c] == 7) //UPleft
	    {

		int y = r - 1;

		int i = c - 1;

		while (i > 0 && y > 0)
		{
		    sum [y] [i] = sum [y] [i] + 1;

		    y--;
		    i--;
		}

	    }

	    return sum;
	}
	
	public boolean didyouwin () //Checks whether player has won, accesed from outside the class
	{
	    check (currentdir, sum); //First finds the current value of the sum array (which  counts how may arrows are pointing at each thornbox currently)

	    boolean same = true; //Sets up the condition before the test

	    for (int i = 0 ; i < sum.length ; i++)
	    {
		for (int j = 0 ; j < sum.length ; j++)
		{
		//Logistically using some Harman Tests, I have exained that there is only one possibile answer for any given "question" in this game    
		if (sum [i] [j] != randomizer [i] [j]) //Only when there is a mistake will the condition be false and the player did not match up the answer with the current set up
			same = false;
		}
	    }
	    return same;
	}

	//1 = Down, 2 = DLEFT, 3 = DRIGHT, 4 = RIGHT, 5 = LEFT, 6=UP, 7= UPLEFT, 8 = UPRIGHT

	public void actionPerformed (ActionEvent e)  //The actionperformed within the class
	{

	    //Coordinates of Action Command are first extracted

	    coordinateparts = e.getActionCommand ().split ("and");

	    int posc = Integer.parseInt (coordinateparts [1]);
	    int posr = Integer.parseInt (coordinateparts [0]);


	    /*Depending on the arrow button clicked,
	    the new ImageIcon overwriting the old one will only point in specific possible directions
	    The corner arrow buttons have 2 possibilites
	    The other arrow buttons have three possibilities
	    This code cycles through the different arrow directions */

	    if (posr == 0 && posc == 1)  //down/downleft/downright
	    {

		switch (count [posr] [posc])
		{
		    case 1:
			thornbox [posr] [posc].setIcon (aDOWN);
			currentdir [posr] [posc] = 1;
			count [posr] [posc] = 3;

			break;
		    case 3:
			thornbox [posr] [posc].setIcon (aDRIGHT);
			currentdir [posr] [posc] = 3;
			count [posr] [posc] = 1;
			break;

		}

	    }

	    else if (posr == 0 && posc == globallength - 2)  //down/downleft/downright
	    {

		switch (count [posr] [posc])
		{
		    case 1:
			thornbox [posr] [posc].setIcon (aDOWN);
			currentdir [posr] [posc] = 1;
			count [posr] [posc] = 2;
			break;
		    case 2:
			thornbox [posr] [posc].setIcon (aDLEFT);
			currentdir [posr] [posc] = 2;
			count [posr] [posc] = 1;
			break;

		}

	    }



	    else if (posr == globallength - 1 && posc == 1)
	    {
		switch (count [posr] [posc])
		{
		    case 6:
			thornbox [posr] [posc].setIcon (aUP);
			currentdir [posr] [posc] = 6;
			count [posr] [posc] = 8;
			break;

		    case 8:
			thornbox [posr] [posc].setIcon (aUPRIGHT);
			currentdir [posr] [posc] = 8;
			count [posr] [posc] = 6;
			break;
		}
	    }

	    else if (posr == globallength - 1 && posc == globallength - 2)
	    {
		switch (count [posr] [posc])
		{
		    case 6:
			thornbox [posr] [posc].setIcon (aUP);
			currentdir [posr] [posc] = 6;
			count [posr] [posc] = 7;
			break;

		    case 7:
			thornbox [posr] [posc].setIcon (aUPLEFT);
			currentdir [posr] [posc] = 7;
			count [posr] [posc] = 6;
			break;
		}
	    }

	    else if (posc == 0 && posr == 1)
	    {

		switch (count [posr] [posc])
		{
		    case 4:
			thornbox [posr] [posc].setIcon (aRIGHT);
			currentdir [posr] [posc] = 4;
			count [posr] [posc] = 3;
			break;

		    case 3:
			thornbox [posr] [posc].setIcon (aDRIGHT);
			currentdir [posr] [posc] = 3;
			count [posr] [posc] = 4;
			break;
		}
	    }

	    else if (posc == 0 && posr == globallength - 2)
	    {

		switch (count [posr] [posc])
		{
		    case 4:
			thornbox [posr] [posc].setIcon (aRIGHT);
			currentdir [posr] [posc] = 4;
			count [posr] [posc] = 8;
			break;
		    case 8:
			thornbox [posr] [posc].setIcon (aUPRIGHT);
			currentdir [posr] [posc] = 8;
			count [posr] [posc] = 4;
			break;
		}

	    }

	    else if (posc == globallength - 1 && posr == 1)
	    {

		switch (count [posr] [posc])
		{
		    case 5:
			thornbox [posr] [posc].setIcon (aLEFT);
			currentdir [posr] [posc] = 5;
			count [posr] [posc] = 2;
			break;

		    case 2:
			thornbox [posr] [posc].setIcon (aDLEFT);
			currentdir [posr] [posc] = 2;
			count [posr] [posc] = 5;
			break;
		}

	    }

	    else if (posc == globallength - 1 && posr == globallength - 2)
	    {

		switch (count [posr] [posc])
		{
		    case 5:
			thornbox [posr] [posc].setIcon (aLEFT);
			currentdir [posr] [posc] = 5;
			count [posr] [posc] = 7;
			break;
		    case 7:
			thornbox [posr] [posc].setIcon (aUPLEFT);
			currentdir [posr] [posc] = 7;
			count [posr] [posc] = 5;
			break;
		}

	    }

	    else if (posr == 0)  //down/downleft/downright
	    {

		switch (count [posr] [posc])
		{
		    case 1:
			thornbox [posr] [posc].setIcon (aDOWN);
			currentdir [posr] [posc] = 1;
			count [posr] [posc] = 2;

			break;
		    case 2:
			thornbox [posr] [posc].setIcon (aDLEFT);
			currentdir [posr] [posc] = 2;
			count [posr] [posc] = 3;
			break;
		    case 3:
			thornbox [posr] [posc].setIcon (aDRIGHT);
			currentdir [posr] [posc] = 3;
			count [posr] [posc] = 1;
			break;
		}
	    }

	    else if (posr == globallength - 1)
	    {
		switch (count [posr] [posc])
		{
		    case 6:
			thornbox [posr] [posc].setIcon (aUP);
			currentdir [posr] [posc] = 6;
			count [posr] [posc] = 7;
			break;
		    case 7:
			thornbox [posr] [posc].setIcon (aUPLEFT);
			currentdir [posr] [posc] = 7;
			count [posr] [posc] = 8;
			break;
		    case 8:
			thornbox [posr] [posc].setIcon (aUPRIGHT);
			currentdir [posr] [posc] = 8;
			count [posr] [posc] = 6;
			break;
		}
	    }

	    else if (posc == 0)
	    {

		switch (count [posr] [posc])
		{
		    case 4:
			thornbox [posr] [posc].setIcon (aRIGHT);
			currentdir [posr] [posc] = 4;
			count [posr] [posc] = 8;
			break;
		    case 8:
			thornbox [posr] [posc].setIcon (aUPRIGHT);
			currentdir [posr] [posc] = 8;
			count [posr] [posc] = 3;
			break;
		    case 3:
			thornbox [posr] [posc].setIcon (aDRIGHT);
			currentdir [posr] [posc] = 3;
			count [posr] [posc] = 4;
			break;
		}
	    }

	    else if (posc == globallength - 1)
	    {

		switch (count [posr] [posc])
		{
		    case 5:
			thornbox [posr] [posc].setIcon (aLEFT);
			currentdir [posr] [posc] = 5;
			count [posr] [posc] = 7;
			break;
		    case 7:
			thornbox [posr] [posc].setIcon (aUPLEFT);
			currentdir [posr] [posc] = 7;
			count [posr] [posc] = 2;
			break;
		    case 2:
			thornbox [posr] [posc].setIcon (aDLEFT);
			currentdir [posr] [posc] = 2;
			count [posr] [posc] = 5;
			break;
		}
	    }

	    else
	    {

		check (currentdir, sum); //adds up all the arrows pointing at the thornboxes 
	    }

	    showStatus ("SUM: " + sum [posr] [posc]); //When thornbox button is clicked, game checks how many arrows are pointing at it
	}
    }


    class TransparentPanel extends JPanel //The panel is transparent so that other panels can be overlayed on it
    {

	//@OVERIDE
	public void paintComponent (Graphics g)
	{
	    super.paintComponent (g);
	    setBackground (new Color (0, 0, 0, 0)); //transperant
	    setOpaque (false); //no background image
	}
    }


    class BackPanel extends JPanel //2D Background graphics class
    {
	//ImageIcon arrow = createImageIcon ("ArrowBack.jpg");

	public void paintComponent (Graphics g)
	{
	    super.paintComponent (g);



	    Graphics2D g2 = (Graphics2D) g; //2D Background
	    int w = getWidth ();
	    int h = getHeight ();
	    GradientPaint greentoyellow = new GradientPaint (0, 0, Color.yellow, 1024, 768, Color.blue); //Yellow to Blue
	    g2.setPaint (greentoyellow);
	    g2.fillRect (0, 0, w, h);
	    setBackground (new Color (0, 0, 0, 0));
	    setOpaque (false);
	}
    }


    class ArrowPanel extends JPanel implements ActionListener //Arrow Animations
    {

	Timer animate = new Timer (2, this); //Timer implemented with Action Commands
	int now[] []; //Multiple arrows
	int velX = 10, velY = 10; //"Velocity" of arrows moving from top right to bottom left

	public void start ()  // Initializes a bunch of arrows and randomizes their positioning, and starts timer/
	{

	    now = new int [10] [2];

	    for (int y = 0 ; y < 2 ; y++)
	    {
		for (int i = 0 ; i < 10 ; i++)
		{
		    now [i] [y] = (int) (Math.random () * getHeight ());

		}
	    }

	    animate.start ();

	}


	//@OVERIDE
	public void paintComponent (Graphics g)
	{

	    super.paintComponent (g);

	    for (int i = 0 ; i < 9 ; i++)
		arrowart (now [i] [0], now [i] [1]);

	    setBackground (new Color (0, 0, 0, 0)); // TRANSPERINCY

	}


	public void arrowart (int x, int y)  //The graphics for the arrows. The X and Y represent their positon on the panel.
	{
	    Graphics g = getGraphics ();
	    int xa[] = {x + 54, x + 62, x + 64, x + 61, x + 92, x + 95, x + 92, x + 84};
	    int ya[] = {y + 15, y + 14, y + 12, y + 4, y + 27, y + 37, y + 40, y + 40};

	    g.setColor (Color.black);
	    g.fillPolygon (xa, ya, 7);

	    int xb[] = {x + 93, x + 112, x + 107, x + 143, x + 117, x + 116, x + 94};
	    int yb[] = {y + 39, y + 57, y + 59, y + 79, y + 45, y + 55, y + 36};

	    g.setColor (Color.gray);

	    g.fillPolygon (xb, yb, 7);
	    g.dispose ();
	}


	public void actionPerformed (ActionEvent e)
	{

	    //Timer initiates actionPerformed every 1 ms - reason for flickering
	    for (int i = 0 ; i < 9 ; i++)
	    {
		now [i] [0] += velX;
		now [i] [1] += velY;

		if (now [i] [0] > 1024 || now [i] [1] > 768) //When arrow leaves screen, new random arrow set up to take its place at the top right corner of screen
		{
		    now [i] [0] = (int) (Math.random () * getWidth ());
		    now [i] [1] = (int) (Math.random () * getHeight ());
		    //   System.out.println (now[i][0] + " and " + now[i][1]);
		}
	    }

	    repaint (); //Repaint the entire screen every ms
	}
    }



    protected static ImageIcon createImageIcon (String path)
    {
	java.net.URL imgURL = FINALGAME.class.getResource (path);
	if (imgURL != null)
	    return new ImageIcon (imgURL);
	else
	{
	    System.err.println ("Couldn't find file: " + path);
	    return null;
	}
    }




}
















