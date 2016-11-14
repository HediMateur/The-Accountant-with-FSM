{\rtf1\ansi\ansicpg1252\deff0\nouicompat\deflang1036{\fonttbl{\f0\fnil\fcharset0 Calibri;}}
{\*\generator Riched20 10.0.10586}\viewkind4\uc1 
\pard\sa200\sl276\slmult1\f0\fs22\lang12 import java.util.*;\par
import java.io.*;\par
import java.math.*;\par
\par
import javax.swing.tree.VariableHeightLayoutCache;\par
\par
/**\par
 * Shoot enemies before they collect all the incriminating data!\par
 * The closer you are to an enemy, the more damage you do but don't get too close or you'll get killed.\par
 **/\par
 \par
    class Enemy\{\par
        int x=0;\par
        int y=0;\par
        int id=0;\par
        int life=0;\par
    \}\par
    class Data\{\par
        int x=0;\par
        int y=0;\par
        int id=0;\par
    \}\par
    class Wolf\{\par
    \tab int x;\par
    \tab int y;\par
    \tab List<Enemy> enemies;\par
    \tab int damage;\par
    \tab State wState;\par
    \tab /*Wolf()\{\par
        \tab this.damage=\par
    \tab\par
    \tab\}*/\par
    \tab /*Wolf(List<Enemy> enemies)\{\par
    \tab\tab this.enemies=enemies;\par
    \tab\}*/\par
        public void update() \{\par
        \tab if (wState != null) \{\par
                wState.start();\par
            \}\par
            wState.act(this);\par
        \}\par
    \}\par
    abstract class State\{\par
    \tab enum wolfState\{\par
    \tab\tab Succes,\par
    \tab\tab Failure,\par
    \tab\tab Running,\par
    \tab\}\par
    \tab wolfState state;\par
        State()\{\}\par
    \tab void start()\{\par
    \tab     System.err.println("started");\par
    \tab\tab this.state = wolfState.Running;\par
    \tab\}\par
    \tab abstract void reset();\par
    \tab abstract void act(Wolf wolf);\par
    \tab void succeed()\{\par
    \tab\tab System.err.println("succed");\par
    \tab\tab this.state=wolfState.Succes;\par
    \tab\}\par
    \tab void fail()\{\par
    \tab\tab this.state=wolfState.Failure;\par
    \tab\}\par
    \tab public boolean isSuccess()\{\par
    \tab     System.err.println("succes");\par
    \tab\tab return state.equals(wolfState.Succes);\par
    \tab\}\par
    \tab public boolean isFailure()\{\par
    \tab     System.err.println("failed");\par
    \tab\tab return state.equals(wolfState.Failure);\par
    \tab\}\par
    \tab public boolean isRunning()\{\par
    \tab     System.err.println("runnig");\par
    \tab\tab return state.equals(wolfState.Running);\par
    \tab\}\par
    \tab public wolfState getState()\{\par
    \tab\tab return state;\par
    \tab\}\par
    \}\par
    class moveToState extends State\{\par
    \tab final int  destX;\par
    \tab final int destY;\par
    \tab final List<Enemy> enemies;\par
    \tab moveToState(int destX,int destY,List<Enemy> enemies)\{\par
    \tab\tab super();\par
    \tab\tab this.destX=destX;\par
    \tab\tab this.destY=destY;\par
    \tab\tab this.enemies=enemies;\par
    \tab\}\par
    \tab\par
\tab\tab @Override\par
\tab\tab void reset() \{\par
\tab\tab\tab start();\par
\tab\tab\}\par
\par
\tab\tab @Override\par
\tab\tab void act(Wolf wolf) \{\par
\tab\tab\tab if(isRunning())\{\par
\tab\tab\tab\tab for(int i=0;i<enemies.size();i++)\{\par
\tab\tab\tab\tab\tab if(Math.sqrt((destX-enemies.get(i).x)*(destX-enemies.get(i).x)+(destY-enemies.get(i).y)*(destY-enemies.get(i).y))<=2000)\{\par
\tab\tab\tab\tab\tab\tab fail();\par
\tab\tab\tab\tab\tab\tab System.err.println("failed");\par
\tab\tab\tab\tab\tab\tab return;\par
\tab\tab\tab\tab\tab\}\par
\tab\tab\tab\tab\}\par
\tab\tab\tab\tab if(!wolfIsAtDestination(wolf))\par
\tab\tab\tab\tab\tab moveWolf(wolf);\par
\tab\tab\tab\tab\tab return;\par
\tab\tab\tab\}\par
\tab\tab\}\par
\tab\tab void moveWolf(Wolf wolf)\{\par
\tab\tab\tab if(destY!=wolf.y||destX!=wolf.x)\par
\tab\tab\tab\{    wolf.x=destX;\par
\tab\tab\tab     wolf.y=destY;\par
\tab\tab\tab\tab System.out.println("Move "+destX+" "+destY);\par
\tab\tab\tab\}\par
\tab\tab\tab if(wolfIsAtDestination(wolf))\par
\tab\tab\tab succeed();\par
\tab\tab\}\par
\tab\tab boolean wolfIsAtDestination(Wolf wolf)\{\par
\tab\tab return (wolf.x==destX&&wolf.y==destY);\par
\tab\tab\}\par
    \}\par
    class attackEnemyState extends State\{\par
    \tab int life,id;\par
    \tab Enemy enemy;\par
    \tab List<Enemy> enemies;\par
    \tab attackEnemyState(Enemy enemy,List<Enemy> enemies)\{\par
    \tab\tab super();\par
    \tab\tab this.enemy =enemy;\par
    \tab\tab //System.err.println(enemy.id);\par
    \tab\tab this.enemies=enemies;\par
    \tab\}\par
\tab\tab @Override\par
\tab\tab void reset() \{\par
\tab\tab\tab start();\par
\tab\tab\}\par
\par
\tab\tab @Override\par
\tab\tab void act(Wolf wolf) \{\par
\tab\tab\tab if(isRunning())\{\par
\tab\tab\tab\tab for(int i=0;i<enemies.size();i++)\{\par
\tab\tab\tab\tab\tab if(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y))<2000)\{\par
\tab\tab\tab\tab\tab\tab State runAwayState = new runAwayState(enemies);\par
                        wolf.wState= runAwayState;\par
                        wolf.update();\par
                    \tab fail();\par
\tab\tab\tab\tab\tab\tab return;\par
\tab\tab\tab\tab\tab\}\par
\tab\tab\tab\tab\}\par
\tab\tab\tab\tab id=enemy.id;\par
\tab\tab\tab\tab life=enemy.life;\par
\tab\tab\tab\tab if(!wolfAttackedEnemy(wolf))\par
\tab\tab\tab\tab\tab wolfAttack(wolf);\par
\tab\tab\tab\}\par
\tab\tab\tab\par
\tab\tab\}\par
    \tab void wolfAttack(Wolf wolf)\{\par
    \tab     wolf.damage=(int)Math.round(125000/Math.pow(Math.sqrt((wolf.x-enemy.x)*(wolf.x-enemy.x)+(wolf.y-enemy.y)*(wolf.y-enemy.y)),1.2));\par
    \tab\tab life=enemy.life-wolf.damage;\par
    \tab\tab System.err.println(id);\par
    \tab\tab System.out.println("Shoot "+enemy.id);\par
    \tab     if(wolfAttackedEnemy(wolf))\par
    \tab     \{\par
    \tab         succeed();\par
    \tab     \}\par
    \tab\}\par
    \tab boolean wolfAttackedEnemy(Wolf wolf)\{\par
    \tab     \tab if(enemy.id == id)\par
    \tab\tab         \{if(life!=enemy.life)\par
    \tab\tab             \{  return true;\par
    \tab\tab\tab         \}\par
    \tab\tab\tab     \}\par
    \tab\tab\par
    \tab\tab return false;\par
    \tab\}\par
    \}\par
    class oneShotEnemyState extends State\{\par
    \tab int life,id;\par
    \tab Enemy enemy;\par
    \tab List<Enemy> enemies;\par
    \tab oneShotEnemyState(List<Enemy> enemies)\{\par
    \tab\tab super();\par
    \tab\tab this.enemies=enemies;\par
    \tab\}\par
\tab\tab @Override\par
\tab\tab void reset() \{\par
\tab\tab\tab start();\par
\tab\tab\}\par
\par
\tab\tab @Override\par
\tab\tab void act(Wolf wolf) \{\par
\tab\tab     if(isRunning())\{\par
\tab\tab\tab\tab for(int i=0;i<enemies.size();i++)\{\par
\tab\tab\tab\tab\tab if(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y))<=2500)\{\par
\tab                     State runAwayState = new runAwayState(enemies);\par
                        wolf.wState= runAwayState;\par
                        wolf.update();\par
                        fail();\par
\tab\tab\tab\tab\tab\tab return;\par
\tab\tab\tab\tab\tab\}\par
\tab\tab\tab\tab\}\par
\tab\tab\tab\tab enemy=enemies.get(0);\par
\tab\tab\tab\tab id = enemy.id;\par
\tab\tab\tab\tab //System.err.println(id);\par
\tab\tab\tab\tab life=enemy.life;\par
\tab\tab\tab\tab if(!wolfOsedEnemy(wolf))\par
\tab\tab\tab\tab\tab wolfOs(wolf);\par
\tab\tab\tab\tab\tab\tab\par
\tab\tab\tab\}\par
\tab\tab\tab\par
\tab\tab\}\par
    \tab void wolfOs(Wolf wolf)\{\par
    \tab\tab for(int i=0;i<enemies.size();i++)\par
    \tab\tab\{wolf.damage=(int)Math.round(125000/Math.pow(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y)),1.2));\par
    \tab\tab\tab if(enemies.get(i).life-wolf.damage<=0)\par
    \tab\tab\tab\{\tab enemy=enemies.get(i);\par
\tab\tab\tab\tab\tab id = enemy.id;\par
\tab\tab\tab\tab\tab life=enemies.get(i).life-wolf.damage;\par
\tab\tab\tab\tab\tab System.err.println(enemies.get(i).id);\par
\tab\tab\tab\tab\tab System.out.println("Shoot "+enemies.get(i).id);\par
\tab\tab\tab\tab\tab if(wolfOsedEnemy(wolf))\par
    \tab             \{succeed();\par
    \tab     \tab\tab\}\par
    \tab     \tab\tab return;\par
    \tab     \tab //enemies.remove(i);\par
    \tab\tab\tab\}\par
    \tab\tab\}\par
    \tab     if (!wolfOsedEnemy(wolf))\par
    \tab     \{\tab int j=0;\par
        \tab if (enemies.size()>=1)\{\par
            float distEnemyMin=(float)Math.sqrt((wolf.x-enemies.get(j).x)*(wolf.x-enemies.get(j).x)+(wolf.y-enemies.get(j).y)*(wolf.y-enemies.get(j).y));        \par
            for(int i=1;i<enemies.size();i++)\{\par
                float distEnemy0;\par
                float distEnemy;\par
                distEnemy=(float)Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y));\par
                if(distEnemy<distEnemyMin&&enemies.get(i).life>0)\par
                \{   //System.err.println(enemies.get(i).life);\par
                    //System.err.println(enemies.get(i).id);\par
                    distEnemyMin=distEnemy;\par
                    j=i;\par
                \}\par
            \}\par
        \}   \par
            //System.err.println(j);\par
\tab\tab\tab State attackEnemyState = new attackEnemyState(enemies.get(j), enemies);\par
            wolf.wState= attackEnemyState;\par
            wolf.update();\par
            \par
\tab\tab\}\par
    \tab\}\par
    \tab boolean wolfOsedEnemy(Wolf wolf)\{\par
    \tab     \tab if(enemy.id == id)\par
    \tab\tab         \{if(life!=enemy.life)\par
    \tab\tab             \{   System.err.println("easy e");\par
    \tab\tab                 return true;\par
    \tab\tab\tab         \}\par
    \tab\tab\tab     \}\par
    \tab\tab\par
    \tab\tab return false;\par
    \tab\}\par
    \}\par
\par
    class runAwayState extends State\{\par
    \tab final List<Enemy> enemies;\par
    \tab int X=0;\par
        int Y=0;\par
        \par
    \tab public runAwayState(List <Enemy> enemies) \{\par
    \tab\tab this.enemies=enemies;\par
\tab\tab\}\par
\tab\tab @Override\par
\tab\tab void reset() \{\par
\tab\tab start();\tab\par
\tab\tab\}\par
\par
\tab\tab @Override\par
\tab\tab void act(Wolf wolf) \{\par
\tab\tab\tab if(isRunning())\{\par
\tab\tab\tab\tab for(int i=0;i<enemies.size();i++)\{\par
\tab\tab\tab\tab\tab if(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y))<1500)\{\par
\tab\tab\tab\tab\tab\tab fail();\par
\tab\tab\tab\tab\tab\tab return;\par
\tab\tab\tab\tab\tab\}\par
\tab\tab\tab\tab\}\par
\tab\tab\tab\tab if(!wolfEscaped(wolf))\par
\tab\tab\tab\tab\tab escape(wolf);\par
\tab\tab\tab\tab\}\par
\tab\tab\tab\par
\tab\tab\}\par
\tab\tab void escape(Wolf wolf)\{\par
\tab\tab\tab int j=0;\par
\tab      \tab for(int i=0;i<enemies.size();i++)\{\par
\tab     \tab    if(Math.sqrt((wolf.x-enemies.get(i).x)*(wolf.x-enemies.get(i).x)+(wolf.y-enemies.get(i).y)*(wolf.y-enemies.get(i).y))<=2500)\par
\tab     \tab\tab\{\tab\par
\tab     \tab\tab     X+=enemies.get(i).x;\par
\tab     \tab\tab     Y+=enemies.get(i).y;\par
\tab     \tab\tab\tab j++ ;\par
\tab     \tab\tab\}\par
\tab     \tab\}\par
\tab     \tab if(j!=0)\par
\tab      \tab\{\par
\tab      \tab     X=X/j;\par
\tab         \tab Y=Y/j;\par
\tab         \tab X=2*wolf.x-X;\par
\tab         \tab Y=2*wolf.y-Y;\par
\tab      \tab     System.out.println("Move "+X+" "+Y);\par
\tab      \tab\par
\tab      \tab\}\par
\tab     \par
\tab\tab\}\par
\tab\tab boolean wolfEscaped(Wolf wolf)\{\par
\tab\tab\tab return (wolf.x==X&&wolf.y==Y);\par
\tab\tab\}\par
    \par
    \}\par
class Player \{\par
\tab\par
    public static void main(String args[]) \{\par
        Scanner in = new Scanner(System.in);\par
        // game loop\par
        Wolf wolf = new Wolf();\par
        while (true) \{\par
        \tab\par
            wolf.x = in.nextInt();\par
            wolf.y = in.nextInt();\par
            int dataCount = in.nextInt();\par
            List<Data> datas = new ArrayList<Data>();\par
            for (int i = 0; i < dataCount; i++) \{\par
                 Data data = new Data();\par
                 data.id = in.nextInt();\par
                 data.x = in.nextInt();\par
                 data.y = in.nextInt();\par
                 datas.add(data);\par
            \}\par
            List<Enemy> enemies = new ArrayList<Enemy>(); \par
            int enemyCount = in.nextInt();\par
            for (int i = 0; i < enemyCount; i++) \{\par
                Enemy enemy = new Enemy();\par
                enemy.id = in.nextInt();\par
                enemy.x = in.nextInt();\par
                enemy.y = in.nextInt();\par
                //System.err.println(enemyX);\par
                enemy.life = in.nextInt();\par
                enemies.add(enemy);\par
            \}\par
            //wolf.enemies=enemies;\par
            State oneShotEnemyState = new oneShotEnemyState(enemies);\par
            wolf.wState= oneShotEnemyState;\par
            wolf.update();\par
             \par
        \}  \par
    \}\par
\}\par
}
 