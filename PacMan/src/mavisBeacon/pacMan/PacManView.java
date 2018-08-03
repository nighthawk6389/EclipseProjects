package mavisBeacon.pacMan;

import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


public class PacManView extends View implements Runnable{
	
	Context mContext;
	
    Point ballsTop [];   
    Point ballsBot [];
    Point ballsRight [];
    Point ballsLeft [];
    Point ballsMidT [];
    Point ballsMidB [];
    Point limits1[];
    Point limits2 [];
    Point aiPoints [];
    Point transPort[];
    AI ai [];
    Point pacMan;
    Point test;
    Point dir[];
    Point attempt;
    int color [];
    int smallSide;
    int sideBox;
    int sideYbox;
    int smallXturn;
    int x;
    int y;
    int origin;
    int directionX;
    int directionY;
    int oldX;
    int oldY;
    int speed;
    int amountDir;
    int timePac;
    int changePacColor;
    int allDone;
    int score;
    int numOfAI;
    int total;
    int blow;
    int step;
    boolean on=false;
    boolean start =true;
    boolean suspend=false;
    boolean crazyPac=false;
    boolean gameOver=false;
    boolean attemptTry=false;
    boolean attemptTryTrue=false;
    boolean blowUp=false;
    
    MediaPlayer mp1=MediaPlayer.create(this.getContext(),R.raw.eatghosts);
    MediaPlayer mp2=MediaPlayer.create(this.getContext(),R.raw.eating);
    MediaPlayer mp3=MediaPlayer.create(this.getContext(),R.raw.pacmandies);
    Bitmap pac1 = BitmapFactory.decodeResource(getResources(), R.drawable.pacmanalone);
    Bitmap pac2 = BitmapFactory.decodeResource(getResources(), R.drawable.pacmandown);
    Bitmap pac3 = BitmapFactory.decodeResource(getResources(), R.drawable.pacmanleft);
    Bitmap pac4 = BitmapFactory.decodeResource(getResources(), R.drawable.pacmanup);
    Bitmap pac5 = BitmapFactory.decodeResource(getResources(), R.drawable.blinky);
    Bitmap pac6 = BitmapFactory.decodeResource(getResources(), R.drawable.inky);
    Bitmap pac7 = BitmapFactory.decodeResource(getResources(), R.drawable.explode);
    Bitmap pac8 = BitmapFactory.decodeResource(getResources(), R.drawable.greenghost);

	public PacManView(Context context) {
		super(context);
		this.mContext=context;
		
        this.setFocusable(true);
        //setLayout(new BorderLayout());
        x=getWidth();
        y=getHeight();
        smallSide=40;
        sideBox=130;
        sideYbox=70;
        smallXturn=30;
        step=10;
        directionX=step;
        directionY=0;
        oldX=step;
        oldY=0;
        speed=150;
        amountDir=0;
        timePac=0;
        changePacColor=0;
        allDone=0;
        score=0;
        numOfAI=3;
        blow=40;
        color= new int[3];
        dir= new Point[4];
        aiPoints= new Point[(sideBox/10)*2+9];
        test= new Point(1,1);
        pacMan= new Point(x/2,(y-10)-(origin/2)-10);  
       // System.out.println(x+" x y "+y);
	}
    
    public void initiate(){
        x=getWidth(); 
        y=getHeight();
        smallSide=40;
        smallXturn=30;
        sideBox=130;
        sideYbox=70;
        origin= (y-10)-(y-smallSide);
        ballsTop=new Point[x/10-2];   
        ballsBot=new Point[x/10-2];
        ballsRight= new Point[y/10-5];
        ballsLeft=new Point[y/10-5];
        ballsMidT= new Point[x/10-10+1];
        ballsMidB= new Point[x/10-10+1];
        aiPoints= new Point[(sideBox/10)*2+9];
        transPort= new Point[4];
        ai= new AI[numOfAI];
        limits1=new Point[ballsTop.length+ballsLeft.length+ballsMidT.length+aiPoints.length+transPort.length/2];
        limits2= new Point[ballsBot.length+ballsRight.length+ballsMidB.length+aiPoints.length+transPort.length/2];

   // System.out.println("limits"+limits1.length+limits2.length);
   // System.out.println("ai amount"+aiPoints.length);
        start=false;
        crazyPac=false;
        allDone=0;
        total=limits1.length+limits2.length-(2*aiPoints.length)-transPort.length;
      //  System.out.println("allDone"+allDone);
        int limit=0;
        int z=0;
      //  System.out.println("initiate");
        
        //balls
        for(int num=10;num<getWidth()-15;num+=10){
            //System.out.println(z+"initiate");
              ballsTop[z]=    new Point(num,smallSide/2+1);
              limits1[limit]= new Point(num,smallSide/2+1);
              ballsBot[z]=    new Point(num,y-(smallSide/2)-1);
              limits2[limit]= new Point(num,y-(smallSide/2)-1);
              z++;
              limit++;
        }
        z=0;
        for(int num=smallSide-10;num<y-smallSide+10;num+=10){
            ballsLeft[z]=     new Point(smallXturn+((((x/2)-sideBox)-smallXturn)/2)-6,num+1);
            limits1[limit]=   new Point(smallXturn+((((x/2)-sideBox)-smallXturn)/2)-6,num+1);
            ballsRight[z]=    new Point((x-smallXturn)-(((x-smallXturn)-((x/2)+sideBox))/2)+2,num+1);
            limits2[limit]=   new Point((x-smallXturn)-(((x-smallXturn)-((x/2)+sideBox))/2)+2,num+1);
            System.out.print(ballsRight[z].y+" ");
            z++;
            limit++;
        }
                System.out.println();
        z=0;
        for(int num=smallXturn+((((x/2)-sideBox)-smallXturn)/2)-6+10;num<=(x-smallXturn)-(((x-smallXturn)-((x/2)+sideBox))/2)+1;num+=10){
            ballsMidT[z]=   new Point(num,(smallSide+sideYbox)+((y/2-(sideYbox/2))-((smallSide+sideYbox)))/2+3);
            limits1[limit]= new Point(num,(smallSide+sideYbox)+((y/2-(sideYbox/2))-((smallSide+sideYbox)))/2+3);
            ballsMidB[z]= new Point (num,(y/2)+(sideYbox/2)-((y/2+(sideYbox/2))-(y-smallSide-sideYbox))/2+7);
            limits2[limit]= new Point(num,(y/2)+(sideYbox/2)-((y/2+(sideYbox/2))-(y-smallSide-sideYbox))/2+7);
            z++;
            limit++;
        }
        z=0;
        for(int num=x/2-sideBox-2+10;num<x/2+sideBox-10;num+=10){
            aiPoints[z]= new Point(num,y/2);
            limits1[limit]= new Point(num,y/2);
            limits2[limit]= new Point(num,y/2);
            z++;
            limit++;
        }
        for(int num=y/2-(sideYbox/2)+5-10;num<y/2+(sideYbox/2)+20;num+=10){
           // System.out.println(x/2);
            aiPoints[z]= new Point(x/2-2,num);
            limits1[limit]= new Point(x/2-2,num);
            limits2[limit]= new Point(x/2-2,num);
            z++;
            limit++;
        }
       //transPort balls
       transPort[0]= new Point(smallXturn+((((x/2)-sideBox)-smallXturn)/2)-6-10,aiPoints[0].y-10);
       transPort[1]= new Point(smallXturn+((((x/2)-sideBox)-smallXturn)/2)-6-20,aiPoints[0].y-10);
       transPort[2]= new Point((x-smallXturn)-(((x-smallXturn)-((x/2)+sideBox))/2)+2+10,aiPoints[0].y-10);
       transPort[3]= new Point((x-smallXturn)-(((x-smallXturn)-((x/2)+sideBox))/2)+2+20,aiPoints[0].y-10);
               System.out.println(transPort[0].y+" "+transPort[1].y+" "+transPort[2].y+" "+transPort[3].y+" ");
               System.out.println();
       limits1[limit]= new Point(transPort[0]);
       limits2[limit]= new Point(transPort[2]);
       limit++;
       limits1[limit]= new Point(transPort[1]);
       limits2[limit]= new Point(transPort[3]);
       limit++;
       //System.out.println(transPort[1].x+" transort 1 "+transPort[1].y);
        //pacman
        System.out.println(ballsMidB[0].x);
        pacMan= new Point(ballsBot[((getWidth()-30)/10)/2].x,(y-(smallSide/2)-1));
        System.out.println("PC: "+pacMan.y);
        
        //AI
        for(int num=0;num<ai.length;num++){
            ai[num]= new AI(aiPoints[num].x,aiPoints[num].y);
            System.out.println(num+"ai: "+aiPoints[num].x+" "+aiPoints[num].y);
        }
        
        //ability  DIR
        for(int num=0;num<dir.length;num++){
            dir[num]= new Point(0,0); 
        }
        
        //colors
            color[0]= Color.rgb(250,0,0);
            color[1]= Color.rgb(0,250,0);
            color[2]= Color.rgb(0,0,250);

            
            if(!on){
                on=true;
                run();
            }
            //System.out.println(limits1.length+limits2.length-ai.length-ai.length+" LENGTH OF SUPPOSED END jko");
    }                                                        
    @Override
    public void onDraw(Canvas canvas){
        // System.out.println(x+" "+y+" "+smallSide);
    	
    	Paint g=new Paint();
        
        if(start){
            initiate();
        }
       //System.out.println("paint line");
        g.setColor(Color.BLUE);
        //two horizontal top bottom lines
        canvas.drawLine(10,10,x-10,10,g);
        canvas.drawLine(10,y-10,x-10,y-10,g);
        
        //four small vertical outer lines
        canvas.drawLine(10,10,10,smallSide,g);
        canvas.drawLine(x-10,10,x-10,smallSide,g);
        canvas.drawLine(10,y-10,10,y-smallSide,g);
        canvas.drawLine(x-10,y-10,x-10,y-smallSide,g);
        
        //four small horizontal outer to inner lines
        canvas.drawLine(10,smallSide,smallXturn,smallSide,g);
        canvas.drawLine(x-10,smallSide,x-smallXturn,smallSide,g);
        canvas.drawLine(10,y-smallSide,smallXturn,y-smallSide,g);
        canvas.drawLine(x-10,y-smallSide,x-smallXturn,y-smallSide,g);
        
        //four inner boundary vertical lines
        canvas.drawLine(smallXturn,smallSide,smallXturn,(y-smallSide)/2-10,g);
        canvas.drawLine(x-smallXturn,smallSide,x-smallXturn,(y-smallSide)/2-10,g);
        canvas.drawLine(smallXturn,y-smallSide,smallXturn,y/2+10,g);
        canvas.drawLine(x-smallXturn,y-smallSide,x-smallXturn,y/2+10,g);
        
        //top inner box
        canvas.drawLine((x/2)-sideBox,smallSide,(x/2)+sideBox,smallSide,g);
        canvas.drawLine((x/2)-sideBox,smallSide,(x/2)-sideBox,smallSide+sideYbox,g);
        canvas.drawLine((x/2)+sideBox,smallSide,(x/2)+sideBox,smallSide+sideYbox,g);
        canvas.drawLine(x/2-sideBox,smallSide+sideYbox,x/2+sideBox,smallSide+sideYbox,g);
        
        //lower inner box
        canvas.drawLine(x/2-sideBox,y-smallSide,x/2+sideBox,y-smallSide,g);
        canvas.drawLine(x/2+sideBox,y-smallSide,x/2+sideBox,y-smallSide-sideYbox,g); 
        canvas.drawLine(x/2-sideBox,y-smallSide,x/2-sideBox,y-smallSide-sideYbox,g);
        canvas.drawLine(x/2+sideBox,y-smallSide-sideYbox,x/2-sideBox,y-smallSide-sideYbox,g); 
        
        //middle inner box
        canvas.drawLine(x/2-sideBox,y/2-(sideYbox/2),x/2-sideBox,y/2+(sideYbox/2),g);
        canvas.drawLine(x/2+sideBox,y/2-(sideYbox/2),x/2+sideBox,y/2+(sideYbox/2),g);
        canvas.drawLine(x/2-sideBox,y/2-(sideYbox/2),x/2-20,y/2-(sideYbox/2),g);
        canvas.drawLine(x/2+sideBox,y/2-(sideYbox/2),x/2+20,y/2-(sideYbox/2),g); 
        canvas.drawLine(x/2-sideBox,y/2+(sideYbox/2),x/2-20,y/2+(sideYbox/2),g);
        canvas.drawLine(x/2+sideBox,y/2+(sideYbox/2),x/2+20,y/2+(sideYbox/2),g);
        
        
        //balls
        g.setColor(Color.GREEN);
        for(int num=0;num<ballsTop.length;num++){
            canvas.drawCircle(ballsTop[num].x,ballsTop[num].y,5,g);
            canvas.drawCircle(ballsBot[num].x,ballsBot[num].y,5,g);
        }
        for(int num=0;num<ballsRight.length;num++){
            //System.out.println("num "+num);
           canvas.drawCircle(ballsRight[num].x,ballsRight[num].y,5,g);
           canvas.drawCircle(ballsLeft[num].x,ballsLeft[num].y,5,g); 
        }
        for(int num=0;num<ballsMidT.length;num++){
            canvas.drawCircle(ballsMidT[num].x,ballsMidT[num].y,5,g);
            canvas.drawCircle(ballsMidB[num].x,ballsMidB[num].y,5,g);
        }
        g.setColor(Color.BLUE);
        for(int num=0;num<aiPoints.length;num++){
            canvas.drawCircle(aiPoints[num].x,aiPoints[num].y,5,g);
        }
        //transport balls
        canvas.drawCircle(transPort[0].x,transPort[0].y,5,g);
        canvas.drawCircle(transPort[1].x,transPort[1].y,5,g);
        canvas.drawCircle(transPort[2].x,transPort[2].y,5,g);
        canvas.drawCircle(transPort[3].x,transPort[3].y,5,g);
        //bigBalls
        g.setColor(Color.MAGENTA);
        canvas.drawCircle(ballsTop[0].x,ballsTop[0].y,10,g);
        canvas.drawCircle(ballsTop[ballsTop.length-1].x,ballsTop[ballsTop.length-1].y,10,g);
        canvas.drawCircle(ballsBot[0].x,ballsBot[0].y,10,g);
        canvas.drawCircle(ballsBot[ballsBot.length-1].x,ballsBot[ballsBot.length-1].y,10,g);
        
        //pacman
        if(!crazyPac)
            g.setColor(Color.RED);
        else{
           // System.out.println(timePac+" TIMEPAC!!!!!!");
            timePac++;
            g.setColor(color[changePacColor++]);
            if(timePac>30){
                crazyPac=false;
                timePac=0;
            }
            if(changePacColor>=3)
                changePacColor=0;
        }
        int pic=0;
         //System.out.println(pacMan.x+" pacX pacY "+pacMa);
        if(directionX+directionY==-10){
            if(directionX==-10)
                pic=1;
            else
                pic=2;
        }
        else{
            if(directionX==10)
                pic=0;
            else
                pic=3;
        }
         
        canvas.drawCircle(pacMan.x-5,pacMan.y-5,15,g);

        //AI
        for(int num=0;num<ai.length;num++){
        if(crazyPac)
            canvas.drawBitmap(pac5,ai[num].x,ai[num].y,g);
        else{
            g.setColor(Color.MAGENTA);
            canvas.drawBitmap(pac4,ai[num].x,ai[num].y,g);
        }
        }
        if(blowUp){
            canvas.drawBitmap(pac7,pacMan.x-blow/2,pacMan.y-blow/2,g);
            if(blow>=getWidth() || blow>=getHeight())
                blowUp=false;
            else
                blow+=10;
            //g.drawImage(image[7],getWidth()/2,getHeight()/2,this);
            suspend=true;
        }
        if(allDone==total){
            canvas.drawText("You Win!!!!!",getWidth()/2-8,smallSide+(sideYbox/2),g);
            
        }
        else if(gameOver){
            canvas.drawText("Play Again? Press Enter",getWidth()/2-30,smallSide+(sideYbox/2),g);
            //suspend=true;
           // label= new FlashingLabel();
            //this.add(label,BorderLayout.CENTER);
        }
         else{
        	 canvas.drawText("Score: "+score,getWidth()/2-8,smallSide+(sideYbox/2),g);
        	 canvas.drawText("Bad Guys: "+numOfAI,getWidth()/2-100,smallSide+(sideYbox/2),g);
         }
        //System.out.println("allDone "+allDone);
    }

    @Override
    public void run() {
        while(!gameOver){
        checkBounds();
        checkEaten();
        checkDone();
   
        for(int num=0;num<ai.length;num++){
            if(ai[num].dontMove==0){
                
                if(crazyPac){
                    System.out.println("AI num="+num+" MOVING AWAY");
                    moveAway(num);

                }
                else if(isNear(ai[num])){
                    System.out.println("AI num="+num+" MOVING NEAR");
                    moveNear(num);
                }
                else{
                    moveRandom(num);
                }
            }
            else if(ai[num].dontMove>40){
                ai[num].dontMove=0;
            }
            else{
                ai[num].dontMove++;
            }
        }
            checkEaten();
       if(attemptTryTrue){
           attemptTry=true;
           attemptTryTrue=false;
       }
       this.invalidate();
       try{
                Thread.sleep(speed);
                waitSuspend();
            }
            catch(InterruptedException e){}
        }
    }

    
    public boolean checkDone(){
        if(allDone==total){
            numOfAI++;
            Toast.makeText(this.getContext(), "YOU WON!!!", Toast.LENGTH_SHORT).show();
            suspend=true;
            //repaint();
            return true;
        }
        return false;
    }
    public void checkEaten(){
            for(int num=0;num<ai.length;num++){
                
                if(pacMan.equals(ai[num])){
                    if(!crazyPac){
                        mp3.start();
                   suspend=true;
                   gameOver=true;
                   score=0;
                    }
                    else{
                        mp1.start();
                        ai[num]=new AI(aiPoints[0].x,aiPoints[0].y);
                        ai[num].dontMove=1;
                        score+=100;
                    }
                }
            }
    }

    public boolean isNear(AI a){
        
        if(Math.abs(pacMan.x-a.x)<150 && Math.abs(pacMan.y-a.y)<159)
            return true;
        else 
            return false;
    }
    public void moveAway(int num){
        int move= (int)(Math.random()*4-.1);
        if(move!=3 && move!=2){
        if(isAbility(num)){
            
            for(int z=0;z<dir.length;z++){
                if(Math.abs(ai[num].x+dir[z].x-pacMan.x)>=Math.abs(ai[num].x+ai[num].dXA-pacMan.x) && Math.abs(ai[num].y+dir[z].y-pacMan.y)>=Math.abs(ai[num].y+ai[num].dYA-pacMan.y)){
                    ai[num].dXA=dir[z].x;
                    ai[num].dYA=dir[z].y;
                    
                    break;
                }
            }
        ai[num].x= ai[num].x+ai[num].dXA;
        ai[num].y= ai[num].y+ai[num].dYA;
        }
        else if(Math.abs(pacMan.x-ai[num].x)<80 && Math.abs(pacMan.y-ai[num].y)<80){
            
            if(Math.abs(ai[num].x+ai[num].dXA-pacMan.x)<=Math.abs(ai[num].x-ai[num].dXA-pacMan.x) && Math.abs(ai[num].y+ai[num].dYA-pacMan.y)<=Math.abs(ai[num].y-ai[num].dYA-pacMan.y)){
               
                ai[num].dXA=-ai[num].dXA;
                ai[num].dYA=-ai[num].dYA;
            }
            if(!checkAI(num)){
                  //  System.out.println("didnt work away"+ai[num].dXA+" "+ai[num].dYA);
                    ai[num].dXA=-ai[num].dXA;
                    ai[num].dYA=-ai[num].dYA;
            }
                ai[num].x= ai[num].x+ai[num].dXA;
                ai[num].y= ai[num].y+ai[num].dYA;
            
        }
        else
            moveRandom(num);
        }
        
        
    }
    public void moveNear(int num){
       // int oldX1=0;
       // int oldY1=0;
        //System.out.println("moveNear ");

     if(isAbility(num)){
                    
       // System.out.println("moveNear !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
       // oldX1=ai[num].dXA;
        //oldY1=ai[num].dYA; 
        
           for(int z=0;z<dir.length;z++){
                if(Math.abs(ai[num].x+dir[z].x-pacMan.x)<=Math.abs(ai[num].x+ai[num].dXA-pacMan.x) && Math.abs(ai[num].y+dir[z].y-pacMan.y)<=Math.abs(ai[num].y+ai[num].dYA-pacMan.y)){
                    ai[num].dXA=dir[z].x;
                    ai[num].dYA=dir[z].y;
                    //System.out.println("FOLLLOW");
                    break;
                }
           }
        ai[num].x= ai[num].x+ai[num].dXA;
        ai[num].y= ai[num].y+ai[num].dYA;
       

     }
     else if(Math.abs(pacMan.x-ai[num].x)<80 && Math.abs(pacMan.y-ai[num].y)<80){
            //System.out.println("Danger close");
            if(Math.abs(ai[num].x+ai[num].dXA-pacMan.x)>=Math.abs(ai[num].x-ai[num].dXA-pacMan.x) && Math.abs(ai[num].y+ai[num].dYA-pacMan.y)>Math.abs(ai[num].y-ai[num].dYA-pacMan.y)){
                //System.out.println("would be closer if reversed");
                ai[num].dXA=-ai[num].dXA;
                ai[num].dYA=-ai[num].dYA;
            }
            if(!checkAI(num)){
                   // System.out.println("didnt work out back to normal"+ai[num].dXA+" "+ai[num].dYA);
                    ai[num].dXA=-ai[num].dXA;
                    ai[num].dYA=-ai[num].dYA;
            }
                ai[num].x= ai[num].x+ai[num].dXA;
                ai[num].y= ai[num].y+ai[num].dYA;
            
     }
     else{
         moveRandom(num);
     }
         
    }
    public void moveRandom(int num){
        //System.out.println("NUM "+ num);
        if(isAbility(num)){
            int a=(int)(Math.random()*amountDir-.1);
            //System.out.println(a+" Math Random HERE HERE HERE");
            ai[num].dXA= dir[a].x;
            ai[num].dYA=dir[a].y;
            ai[num].x= ai[num].x+ai[num].dXA;
            ai[num].y= ai[num].y+ai[num].dYA; 
            //System.out.println(ai[num].dXA+" "+ai[num].dYA+" "+dir[a].x+" "+dir[a].y);
        }
        else{
            //System.out.println("else false");
            if(!checkAI(num)){
                ai[num].dXA=-ai[num].dXA;
                ai[num].dYA=-ai[num].dYA;
            }
            ai[num].x=ai[num].x+ai[num].dXA;
            ai[num].y=ai[num].y+ai[num].dYA;
            //System.out.println(ai[num].dXA+" HEY "+ai[num].dYA);
        }
    }
    
    public boolean isAbility(int num){
        int z=0;
        amountDir=0;
       int oldX1=ai[num].dXA;
       int oldY1=ai[num].dYA;
        if(ai[num].dXA!=0){
                ai[num].dXA=0;
                ai[num].dYA=10;
                if(checkAI(num)){
                    amountDir++;
                    dir[z]= new Point(0,10);
                    z++;
                }
                ai[num].dXA=0;
                ai[num].dYA=-10;
                if(checkAI(num)){
                    amountDir++;
                    dir[z]=new Point(0,-10);
                    z++;
                }
        }
        else if(ai[num].dYA!=0){
                ai[num].dXA=-10;
                ai[num].dYA=0;
                if(checkAI(num)){
                    amountDir++;
                    dir[z]=new Point(-10,0);
                    z++;
                }
                ai[num].dXA=10;
                ai[num].dYA=0;
                if(checkAI(num)){
                    amountDir++;
                    dir[z]= new Point(10,0);
                    z++;
                }
        }
        ai[num].dXA=oldX1;
        ai[num].dYA=oldY1;
       // if(checkAI(num))
            
        
        //backwards
                if(amountDir>0){
                    dir[z]=new Point(oldX1,oldY1);
                    z++;
                    amountDir++;
                    dir[z]= new Point(-oldX1,-oldY1);
                  //System.out.println("ability true !!!!!!!!!!!!!!!!!!!!!! ");
                    return true;
                }
                else{
                    dir[z]= new Point(-oldX1,-oldY1);
                    //System.out.println("ability false");
                    return false;
                }
    }
   
    
    public boolean checkAI(int j){
        //System.out.println("CheckAI");
        Point test= new Point(ai[j].x+ai[j].dXA,ai[j].y+ai[j].dYA);
        for(int num=0;num<limits1.length;num++){
            if(test.equals(limits1[num])||test.equals(limits2[num])){
                //ai[j]= new AI(test,ai[j].dXA,ai[j].dYA);
                return true;
            }
        }
        //System.out.println(" AI false return");
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        // System.out.println("before "+oldX+" "+oldY+" " +directionX+" "+directionY);
        oldX=directionX;
        oldY=directionY;
       // System.out.println("key Event");
        if(keyCode== KeyEvent.KEYCODE_DPAD_UP){
            directionY=-step; 
            directionX=0;
            System.out.println("Up");
        }
           // test= new Point(pacMan.x,pacMan.y-10);
        else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
            directionY=step;
            directionX=0;
        }
            //test=new Point(pacMan.x,pacMan.y+10);
        else if(keyCode== KeyEvent.KEYCODE_DPAD_LEFT){
            directionX=-step;
            directionY=0;
        }
            //test= new Point(pacMan.x-10,pacMan.y);
        else if(keyCode== KeyEvent.KEYCODE_DPAD_RIGHT){
            directionX=step;
            directionY=0;
        }
        /*
        else if(e.getKeyCode()== KeyEvent.VK_BACK_SPACE){
            suspend=true;
            String string = JOptionPane.showInputDialog("Enter Speed");
            speed= Integer.parseInt(string);
            try {
                resume();
             } 
             catch (InterruptedException ex) {
                ex.printStackTrace();
             }
        }
        else if(e.getKeyCode()==KeyEvent.VK_P){
            suspend=true;
        }
        else if(e.getKeyCode()==KeyEvent.VK_O){
             try {
                resume();
             } 
             catch (InterruptedException ex) {
                ex.printStackTrace();
             }
        }
        else if(e.getKeyCode()==KeyEvent.VK_ENTER){
            //allDone=0;
            gameOver=false;
            if(label!=null)
            this.remove(label);
                initiate();
             try {
              resume();
             } catch (InterruptedException ex) {
                 ex.printStackTrace();
             }
        }
        else if(e.getKeyCode()==KeyEvent.VK_SPACE){
            blowUp=true;
        }
        **/
        
        if(directionX==oldX && directionY==oldY)
           System.out.println("DONT TRY IT");
        
        else if(!checkBounds()){
            //attempt= new Point(directionX,directionY);
            directionX=oldX;
            directionY=oldY;
            //attemptTryTrue=true;
        }
        
        return true;
    }
    
    private boolean checkBounds() {
        Point test;
         if(attemptTry){
            oldX=directionX;
            oldY=directionY;
            //System.out.println("before attempt isnt null");
            test =new Point(pacMan.x+attempt.x,pacMan.y+attempt.y);
            directionX=attempt.x;
            directionY=attempt.y;
            attemptTry=false;
        } 
        else 
            test= new Point(pacMan.x+directionX,pacMan.y+directionY);
        
        for(int num=0;num<limits1.length;num++){
            if(test.equals(transPort[1])){
                pacMan = new Point(transPort[3].x,transPort[3].y);
                return true;
            }
            if(test.equals(transPort[3])){
                pacMan = new Point(transPort[1].x,transPort[1].y);
                return true;
            }
                             
            if(test.equals(limits1[num])||test.equals(limits2[num])){
                pacMan= new Point(test);
                checkEat();
                return true;
            }
        }
       // System.out.println("false return");
      /*  if(attemptTry){
            attemptTry=false;
            
        }*/
       // System.out.println(oldX+" "+oldY+" "+directionX+" "+directionY);
        //directionX=oldX;
       // directionY=oldY;
        return false;
    }
        public synchronized void waitSuspend() throws InterruptedException{
        while(suspend)
            wait();   
    }
    public synchronized void resume() throws InterruptedException{
        if(suspend){
            suspend=false;
            notify();
        }
    }

    private void checkEat() {
        if(pacMan.equals(ballsTop[0]) || pacMan.equals(ballsTop[ballsTop.length-1]) || pacMan.equals(ballsBot[0])
        || pacMan.equals(ballsBot[ballsBot.length-1])){
            crazyPac=true;
            //System.out.println("crazypac true");
        }
        for(int num=0;num<ballsTop.length;num++){
            if(pacMan.equals(ballsTop[num])){
                ballsTop[num].set(-1,-1);
                allDone++;
                score+=10;
                mp1.start();
                break;
            }
            else if(pacMan.equals(ballsBot[num])){
              ballsBot[num].set(-1,-1);
              allDone++;
              score+=10;
              mp1.start();
              break;
            }
        }
        for(int num=0;num<ballsLeft.length;num++){
             if(pacMan.equals(ballsLeft[num])){
                ballsLeft[num].set(-1,-1);
                allDone++;
                score+=10;
                mp1.start();
                break;
            }
            else if(pacMan.equals(ballsRight[num])){
                ballsRight[num].set(-1,-1);
                allDone++;
                score+=10;
                mp1.start();
                break;
            }
        }
        for(int num=0;num<ballsMidT.length;num++){
            if(pacMan.equals(ballsMidT[num])){
                ballsMidT[num].set(-1,-1);
                allDone++;
                score+=10;
                mp1.start();
                break;
            }
            else if(pacMan.equals(ballsMidB[num])){
                ballsMidB[num].set(-1,-1);
                allDone++;
                score+=10;
                mp1.start();
                break;
            }//end else if
        }//end for
    }//end checkEat
}//end program
