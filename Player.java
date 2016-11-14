import java.util.*;
import java.io.*;
import java.math.*;

import javax.swing.tree.VariableHeightLayoutCache;

/**
 * Shoot enemies before they collect all the incriminating data!
 * The closer you are to an enemy, the more damage you do but don't get too close or you'll get killed.
 **/
 
    class Enemy{
        int x=0;
        int y=0;
        int id=0;
        int life=0;
    }
    class Data{
        int x=0;
        int y=0;
        int id=0;
    }
    class Wolf{
    	int x;
    	int y;
    	List<Enemy> enemies;
    	int damage;
    	State wState;
    	/*Wolf(){
        	this.damage=
    	
    	}*/
    	/*Wolf(List<Enemy> enemies){
    		this.enemies=enemies;
    	}*/
        public void update() {
        	if (wState != null) {
                wState.start();
            }
            wState.act(this);
        }
    }
    abstract class State{
    	enum wolfState{
    		Succes,
    		Failure,
    		Running,
    	}
    	wolfState state;
        State(){}
    	void start(){
    	    System.err.println("started");
    		this.state = wolfState.Running;
    	}
    	abstract void reset();
    	abstract void act(Wolf wolf);
    	void succeed(){
    		System.err.println("succed");
    		this.state=wolfState.Succes;
    	}
    	void fail(){
    		this.state=wolfState.Failure;
    	}
    	public boolean isSuccess(){
    	    System.err.println("succes");
    		return state.equals(wolfState.Succes);
    	}
    	public boolean isFailure(){
    	    System.err.println("failed");
    		return state.equals(wolfState.Failure);
    	}
    	public boolean isRunning(){
    	    System.err.println("runnig");
    		return state.equals(wolfState.Running);
    	}
    	public wolfState getState(){
    		return state;
    	}
    }
    class moveToState extends State{
    	final int  destX;
    	final int destY;
    	final List<Enemy> enemies;
    	moveToState(int destX,int destY,List<Enemy> enemies){
    		super();
    		this.destX=destX;
    		this.destY=destY;
    		this.enemies=enemies;
    	}
    	
		@Override
		void reset() {
			start();
		}

		@Override
		void act(Wolf wolf) {
			if(isRunning()){
				for(int i=0;i<enemies.size();i++){
					if(Math.sqrt((destX-enemies.get(i).x)*(destX-enemies.get(i).x)+(destY-enemies.get(i).y)*(destY-enemies.get(i).y))<=2000){
						fail();
						System.err.println("failed");
						return;
					}
				}
				if(!wolfIsAtDestination(wolf))
					moveWolf(wolf);
					return;
			}
		}
		void moveWolf(Wolf wolf){
			if(destY!=wolf.y||destX!=wolf.x)
			{    wolf.x=destX;
			    wolf.y=destY;
				System.out.println("Move "+destX+" "+destY);
			}
			if(wolfIsAtDestination(wolf))
			succeed();
		}
		boolean wolfIsAtDestination(Wolf wolf){
		return (wolf.x==destX&&wolf.y==destY);
		}
    }
    class attackEnemyState extends State{
    	int life,id;
    	Enemy enemy;
    	List<Enemy> enemies;
    	attackEnemyState(Enemy enemy,List<Enemy> enemies){
    		super();
    		this.enemy =enemy;
    		//System.err.println(enemy.id);
    		this.enemies=enemies;
    	}
		@Override
		void reset() {
			start();
		}

		@Override
		void act(Wolf wolf) {
			if(isRunning()){
				for(int i=0;i<enemies.size();i++){
					if(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y))<2000){
						State runAwayState = new runAwayState(enemies);
                        wolf.wState= runAwayState;
                        wolf.update();
                    	fail();
						return;
					}
				}
				id=enemy.id;
				life=enemy.life;
				if(!wolfAttackedEnemy(wolf))
					wolfAttack(wolf);
			}
			
		}
    	void wolfAttack(Wolf wolf){
    	    wolf.damage=(int)Math.round(125000/Math.pow(Math.sqrt((wolf.x-enemy.x)*(wolf.x-enemy.x)+(wolf.y-enemy.y)*(wolf.y-enemy.y)),1.2));
    		life=enemy.life-wolf.damage;
    		System.err.println(id);
    		System.out.println("Shoot "+enemy.id);
    	    if(wolfAttackedEnemy(wolf))
    	    {
    	        succeed();
    	    }
    	}
    	boolean wolfAttackedEnemy(Wolf wolf){
    	    	if(enemy.id == id)
    		        {if(life!=enemy.life)
    		            {  return true;
    			        }
    			    }
    		
    		return false;
    	}
    }
    class oneShotEnemyState extends State{
    	int life,id;
    	Enemy enemy;
    	List<Enemy> enemies;
    	oneShotEnemyState(List<Enemy> enemies){
    		super();
    		this.enemies=enemies;
    	}
		@Override
		void reset() {
			start();
		}

		@Override
		void act(Wolf wolf) {
		    if(isRunning()){
				for(int i=0;i<enemies.size();i++){
					if(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y))<=2500){
	                    State runAwayState = new runAwayState(enemies);
                        wolf.wState= runAwayState;
                        wolf.update();
                        fail();
						return;
					}
				}
				enemy=enemies.get(0);
				id = enemy.id;
				//System.err.println(id);
				life=enemy.life;
				if(!wolfOsedEnemy(wolf))
					wolfOs(wolf);
						
			}
			
		}
    	void wolfOs(Wolf wolf){
    	    System.err.println("oups");
    		for(int i=0;i<enemies.size();i++)
    		{wolf.damage=(int)Math.round(125000/Math.pow(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y)),1.2));
    			if(enemies.get(i).life-wolf.damage<=0)
    			{	enemy=enemies.get(i);
					id = enemy.id;
					life=enemies.get(i).life-wolf.damage;
					System.err.println(enemies.get(i).id);
					System.out.println("Shoot "+enemies.get(i).id);
					if(wolfOsedEnemy(wolf))
    	            {succeed();
    	    		}
    	    		return;
    	    	//enemies.remove(i);
    			}
    		}
    	    if (!wolfOsedEnemy(wolf))
    	    {	int j=0;
        	if (enemies.size()>=1){
            float distEnemyMin=(float)Math.sqrt((wolf.x-enemies.get(j).x)*(wolf.x-enemies.get(j).x)+(wolf.y-enemies.get(j).y)*(wolf.y-enemies.get(j).y));        
            for(int i=1;i<enemies.size();i++){
                float distEnemy0;
                float distEnemy;
                distEnemy=(float)Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y));
                if(distEnemy<distEnemyMin&&enemies.get(i).life>0)
                {   //System.err.println(enemies.get(i).life);
                    //System.err.println(enemies.get(i).id);
                    distEnemyMin=distEnemy;
                    j=i;
                }
            }
        }   
            //System.err.println(j);
			State attackEnemyState = new attackEnemyState(enemies.get(j), enemies);
            wolf.wState= attackEnemyState;
            wolf.update();
            
		}
    	}
    	boolean wolfOsedEnemy(Wolf wolf){
    	    	if(enemy.id == id)
    		        {if(life!=enemy.life)
    		            {   System.err.println("easy e");
    		                return true;
    			        }
    			    }
    		
    		return false;
    	}
    }

    class runAwayState extends State{
    	final List<Enemy> enemies;
    	int X=0;
        int Y=0;
        
    	public runAwayState(List <Enemy> enemies) {
    		this.enemies=enemies;
		}
		@Override
		void reset() {
		start();	
		}

		@Override
		void act(Wolf wolf) {
			if(isRunning()){
				for(int i=0;i<enemies.size();i++){
					if(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y))<1500){
						fail();
						return;
					}
				}
				if(!wolfEscaped(wolf))
					escape(wolf);
				}
			
		}
		void escape(Wolf wolf){
			int j=0;
	     	for(int i=0;i<enemies.size();i++){
	    	   if(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y))<=2500)
	    		{	
	    		    X+=enemies.get(i).x;
	    		    Y+=enemies.get(i).y;
	    			j++ ;
	    		}
	    	}
	    	if(j!=0)
	     	{
	     	    X=X/j;
	        	Y=Y/j;
	        	X=2*wolf.x-X;
	        	Y=2*wolf.y-Y;
	     	    System.out.println("Move "+X+" "+Y);
	     	
	     	}
	    
		}
		boolean wolfEscaped(Wolf wolf){
			return (wolf.x==X&&wolf.y==Y);
		}
    
    }
class Player {
	
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        // game loop
        Wolf wolf = new Wolf();
        while (true) {
        	
            wolf.x = in.nextInt();
            wolf.y = in.nextInt();
            int dataCount = in.nextInt();
            List<Data> datas = new ArrayList<Data>();
            for (int i = 0; i < dataCount; i++) {
                 Data data = new Data();
                 data.id = in.nextInt();
                 data.x = in.nextInt();
                 data.y = in.nextInt();
                 datas.add(data);
            }
            List<Enemy> enemies = new ArrayList<Enemy>(); 
            int enemyCount = in.nextInt();
            for (int i = 0; i < enemyCount; i++) {
                Enemy enemy = new Enemy();
                enemy.id = in.nextInt();
                enemy.x = in.nextInt();
                enemy.y = in.nextInt();
                //System.err.println(enemyX);
                enemy.life = in.nextInt();
                enemies.add(enemy);
            }
            //wolf.enemies=enemies;
            State oneShotEnemyState = new oneShotEnemyState(enemies);
            wolf.wState= oneShotEnemyState;
            wolf.update();
             
        }  
    }
}
