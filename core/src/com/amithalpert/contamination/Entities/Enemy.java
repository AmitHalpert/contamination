package com.amithalpert.contamination.Entities;

import com.amithalpert.contamination.Entities.Objects.Bullet;
import com.amithalpert.contamination.Screens.PlayerVComputerScreen;
import com.amithalpert.contamination.Tools.MapObject;
import com.amithalpert.contamination.Tools.ObjectAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Enemy {

    public enum EnemyState {
        Running,
        Jumping,
        Idle,
        dead
    }


    public EnemyState state;

    // Initializing players' final Variables
    public final int PLAYER_WIDTH = 170;
    public final int PLAYER_HEIGHT = 170;

    public final float ANIMATIONS_TIME = 0.5f;
    public final float SHOOT_WAIT_TIME = 1f;
    public final int MOVEMENT_SPEED = 350;
    public final int JUMP_FORCE = 1050;
    public final float GRAVITATIONAL_FORCE = 15f;

    // player characteristics
    public float EnemyX;
    public float EnemyY;
    public int EnemyHealth;
    public int width;
    public int height;
    double velX, velY;
    boolean isFacingLeft;
    boolean isOnGround;

    public boolean IsPlayerFrozen;
    public Rectangle EnemyBounds;
    // for detecting the player
    public Rectangle LeftRay;
    public Rectangle RightRay;
    // for detecting map objects
    public Rectangle LeftFootRay, RightFootRay;
    public Rectangle LeftGunRay, RightGunRay;


    // gun parameters
    float TimeBetweenShots;
    boolean isPlayerHoldingGun;
    public Array<Bullet> bullets;

    // Animation parameters
    float dead_elapsedTime;
    float dead_animation_time;
    float idle_animation_time;
    Texture outputTexture;
    Texture playerTexture;

    // SFX
    Music PlayerDeathSound;
    Sound gunshot;

    ////////////////////////
    // types of the player animation and textures
    ////////////////////////
    Texture player_not_exiting;
    // create ObjectAnimation for every type of animation
    ObjectAnimation player_dead_animation;
    // right animations
    ObjectAnimation player_running_animation;
    ObjectAnimation player_jumping_animation;
    ObjectAnimation player_idle_animation;
    // player with gun animations
    ObjectAnimation player_running_gun_animation;
    ObjectAnimation player_jumping_gun_animation;
    ObjectAnimation player_idle_gun_animation;
    //left animations
    ObjectAnimation flipped_player_running_animation;
    ObjectAnimation flipped_player_jumping_animation;
    ObjectAnimation flipped_player_idle_animation;
    // left player with gun animations
    ObjectAnimation flipped_player_running_gun_animation;
    ObjectAnimation flipped_player_idle_gun_animation;
    ObjectAnimation flipped_player_jumping_gun_animation;


    public Enemy(float x, float y){
        this.EnemyX = x;
        this.EnemyY = y;

        width = PLAYER_WIDTH;
        height = PLAYER_HEIGHT;

        // Ray casts

        EnemyBounds = new Rectangle(EnemyX, EnemyY, 50, 70);
        // for detecting player
        LeftRay = new Rectangle(EnemyX, EnemyY, 1000, 700);
        RightRay = new Rectangle(EnemyX, EnemyY, 1000, 700);
        // for jumping
        LeftFootRay = new Rectangle(EnemyX, EnemyY, 30, 20);
        RightFootRay = new Rectangle(EnemyX, EnemyY, 30, 20);
        // for detecting and shooting player
        LeftGunRay = new Rectangle(EnemyX, EnemyY, 800, 20);
        RightGunRay = new Rectangle(EnemyX, EnemyY, 800, 20);




        EnemyHealth = 1;
        isFacingLeft = false;
        isPlayerHoldingGun = true;
        IsPlayerFrozen = false;
        isOnGround = false;
        state = EnemyState.Idle;
        idle_animation_time = 0;
        dead_animation_time = 0;
        dead_elapsedTime = 0;
        PlayerDeathSound = Gdx.audio.newMusic(Gdx.files.internal("death-sound.mp3"));
        PlayerDeathSound.setVolume(0.3f);


        // Initializing gun parameters
        TimeBetweenShots = 0;
        bullets = new Array<>();
        gunshot = Gdx.audio.newSound(Gdx.files.internal("gun-shot-fx.wav"));


        player_dead_animation = new ObjectAnimation();
        player_dead_animation.loadAnimation("Orange-Player-Death_", 5);
        player_not_exiting = new Texture("player_dead_5.png");

        // player right animations
        player_running_animation = new ObjectAnimation();
        player_running_animation.loadAnimation("Orange-Player-Running_", 4);
        player_jumping_animation = new ObjectAnimation();
        player_jumping_animation.loadAnimation("Orange-Player-Jumping_", 2);
        player_idle_animation = new ObjectAnimation();
        player_idle_animation.loadAnimation("Orange-Player-Idle_", 1);
        playerTexture = new Texture(Gdx.files.internal("Orange-Player-Idle_1.png"));

        // player with a gun right animations
        player_running_gun_animation = new ObjectAnimation();
        player_running_gun_animation.loadAnimation("Orange-Player-Running_Gun_", 4);
        player_jumping_gun_animation = new ObjectAnimation();
        player_jumping_gun_animation.loadAnimation("Orange-Player-Jumping_Gun_", 1);
        player_idle_gun_animation = new ObjectAnimation();
        player_idle_gun_animation.loadAnimation("Orange-Player-Idle-gun_", 1);

        // player left animations
        flipped_player_running_animation = new ObjectAnimation();
        flipped_player_running_animation.loadAnimation("flipped-orange-running_", 4);
        flipped_player_jumping_animation = new ObjectAnimation();
        flipped_player_jumping_animation.loadAnimation("flipped-Orange-Player-Jumping_", 1);
        flipped_player_idle_animation = new ObjectAnimation();
        flipped_player_idle_animation.loadAnimation("flipped-Orange-Player-idle_", 1);
        // player with a gun left animations
        flipped_player_running_gun_animation = new ObjectAnimation();
        flipped_player_running_gun_animation.loadAnimation("flipped-Orange-Player-Running-gun_", 4);
        flipped_player_jumping_gun_animation = new ObjectAnimation();
        flipped_player_jumping_gun_animation.loadAnimation("flipped-Orange-Player-Jumping-gun_", 1);
        flipped_player_idle_gun_animation = new ObjectAnimation();
        flipped_player_idle_gun_animation.loadAnimation("flipped-Orange-Player-idle_gun_", 1);



        // it is important to set the outputTexture to not be Null
        outputTexture = playerTexture;
    }




    public Texture render(float delta, Array<MapObject> Ground, Array<MapObject> WorldBorder, Array<MapObject> RadioActivePool) {

        GetEnemyState();

        velY -= GRAVITATIONAL_FORCE * Gdx.graphics.getDeltaTime();


        velX = 0;

        if (!IsPlayerFrozen) {
            collisionHandling(delta, Ground, WorldBorder, RadioActivePool);
            updatePlayerPosition();
        }



        if(velY > 1050) velY = 1050;

        switch (state) {

            case dead:
                PlayerDeathSound.play();
                IsPlayerFrozen = true;
                dead_animation_time += delta;
                if(dead_animation_time >= 0.04f) {
                    outputTexture = player_dead_animation.getFrame(delta);
                    dead_animation_time = 0;
                    dead_elapsedTime += delta;
                }
                if(dead_elapsedTime >= 0.2f){
                    // removes player
                    outputTexture = player_not_exiting;
                    this.dispose();
                }

                player_idle_gun_animation.resetAnimation();
                player_running_gun_animation.resetAnimation();
                player_running_animation.resetAnimation();
                player_jumping_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                flipped_player_jumping_animation.resetAnimation();
                flipped_player_idle_animation.resetAnimation();
                flipped_player_running_animation.resetAnimation();
                break;


            case Running:
                // gun running animation
                if(isPlayerHoldingGun && !isFacingLeft){
                    outputTexture = player_running_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_running_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_gun_animation.resetAnimation();
                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else

                if(isFacingLeft){
                    outputTexture = flipped_player_running_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    idle_animation_time = 0;
                }
                else
                    outputTexture = player_running_animation.getFrame(ANIMATIONS_TIME * delta);
                player_jumping_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;


            case Jumping:

                if(isPlayerHoldingGun && !isFacingLeft){
                    outputTexture = player_jumping_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_jumping_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_gun_animation.resetAnimation();
                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else

                if(isFacingLeft){

                    outputTexture = flipped_player_jumping_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    idle_animation_time = 0;
                }
                else

                if (player_jumping_animation.currentFrame >= player_jumping_animation.frames.size - 2) {
                    outputTexture = player_jumping_animation.frames.get(player_jumping_animation.currentFrame);
                } else {
                    outputTexture = player_jumping_animation.getFrame(ANIMATIONS_TIME * delta);
                }
                player_running_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;

            case Idle:

                if(isPlayerHoldingGun && !isFacingLeft){

                    outputTexture = player_idle_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                    flipped_player_running_animation.resetAnimation();
                }
                else if (isPlayerHoldingGun){
                    outputTexture = flipped_player_idle_gun_animation.getFrame(ANIMATIONS_TIME * delta);

                    player_running_gun_animation.resetAnimation();
                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                    player_idle_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();
                    flipped_player_idle_animation.resetAnimation();
                }
                else

                if(isFacingLeft){
                    outputTexture = flipped_player_idle_animation.getFrame(ANIMATIONS_TIME * delta);

                    flipped_player_running_animation.resetAnimation();
                    flipped_player_jumping_animation.resetAnimation();

                    player_running_animation.resetAnimation();
                    player_jumping_animation.resetAnimation();
                }
                else

                if (idle_animation_time > 2f) {
                    outputTexture = player_idle_animation.getFrame(ANIMATIONS_TIME * delta);

                    if (idle_animation_time > 3f) {
                        idle_animation_time = 0;
                    }
                } else outputTexture = playerTexture;

                player_running_animation.resetAnimation();
                player_jumping_animation.resetAnimation();
                idle_animation_time += delta;
                break;
        }

        // Returns the (Enemy state) animation
        return outputTexture;
    }


    public void GetEnemyState() {
        // checks if the player is moving up or down
        if (velY > 0) {
            state = EnemyState.Jumping;
        } else if (velY < 0) {
            state = EnemyState.Jumping;
        } else {



            if (velX != 0 && velY == 0){
                state = EnemyState.Running;
                // check if the player is not moving
            } else if (velY == 0 && velX == 0) {
                state = EnemyState.Idle;
            }
        }
        // checks if the player is dead
        if (EnemyHealth <= 0) {
            state = EnemyState.dead;
        }

    }


    public void updatePlayerPosition(){

        //Updates X AND Y Position of the player
        EnemyX += velX;
        EnemyY += velY;

        LeftRay.x = EnemyX - 1000;
        LeftRay.y = EnemyY - 150;
        RightRay.x = EnemyX + 40;
        RightRay.y = EnemyY - 150;


        LeftFootRay.x = EnemyX - 30;
        LeftFootRay.y = EnemyY;
        RightFootRay.x = EnemyX + 80;
        RightFootRay.y = EnemyY;


        RightGunRay.x = EnemyX + 30;
        RightGunRay.y = EnemyY + 30;
        LeftGunRay.x = EnemyX - 800;
        LeftGunRay.y = EnemyY + 30;


        EnemyBounds.x = EnemyX;
        EnemyBounds.y = EnemyY;

    }


    public void collisionHandling(float delta, Array<MapObject> Ground,Array<MapObject> WorldBorder,Array<MapObject> RadioActivePool) {


        LeftRay.x += velX;
        LeftRay.y += velY;


        TimeBetweenShots += Gdx.graphics.getDeltaTime();
        if((PlayerVComputerScreen.player.PlayerBounds.overlaps(LeftGunRay) || PlayerVComputerScreen.player.PlayerBounds.overlaps(RightGunRay)) && TimeBetweenShots >= SHOOT_WAIT_TIME){
            ShootBullets();
            gunshot.play(0.3f);
            TimeBetweenShots = 0;
        }

        if(PlayerVComputerScreen.player.PlayerBounds.overlaps(LeftRay)){
            velX -= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = true;
        }


        if(PlayerVComputerScreen.player.PlayerBounds.overlaps(RightRay)){
            velX += MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
            isFacingLeft = false;
        }


        Array<Bullet> bullet = PlayerVComputerScreen.player.getBullets();
        for (Iterator<Bullet> iter = bullet.iterator(); iter.hasNext(); ) {
            Bullet B = iter.next();
            if (B.hitBox.overlaps(EnemyBounds)){
                EnemyHealth--;
                iter.remove();
            }
        }



        // kills player if you touch RadioActivePool
        for(MapObject Pools : RadioActivePool){
            if(EnemyBounds.overlaps(Pools.hitBox)){
                EnemyHealth = 0;
            }
        }



        // vertical & grounds  Collision
        EnemyBounds.y += velY;
        for (MapObject grounds : Ground) {
            if (EnemyBounds.overlaps(grounds.hitBox)) {
                velY -= velY;
            }
        }


        // horizontal & borders Collision
        EnemyBounds.x += velX;
        for (MapObject borders: WorldBorder) {
            if (EnemyBounds.overlaps(borders.hitBox)) {
                velX -= velX;
            }
            else if (LeftFootRay.overlaps(borders.hitBox) && velY == -velY){
                velY += JUMP_FORCE * Gdx.graphics.getDeltaTime();
            }
            else if(RightFootRay.overlaps(borders.hitBox) && velY == -velY){
                velY += JUMP_FORCE * Gdx.graphics.getDeltaTime();
            }
        }



    }

    public void ShootBullets() {
        // creates a new bullet and add it to the array
        Bullet bullet;

        // check isFacingLeft and adjust where the bullet coming from.
        if(isFacingLeft){
            bullet = new Bullet(EnemyX + 170, EnemyY, true);
        }else{
            bullet = new Bullet(EnemyX, EnemyY, false);
        }

        bullets.add(bullet);
    }

    public void dispose(){

        gunshot.dispose();
        PlayerDeathSound.dispose();
        player_dead_animation.dispose();
        player_running_animation.dispose();
        player_jumping_animation.dispose();
        player_idle_animation.dispose();
        playerTexture.dispose();
        player_running_gun_animation.dispose();
        player_jumping_gun_animation.dispose();
        player_idle_gun_animation.dispose();

        flipped_player_running_animation.dispose();
        flipped_player_jumping_animation.dispose();
        flipped_player_idle_animation.dispose();
        flipped_player_running_gun_animation.dispose();
        flipped_player_jumping_gun_animation.dispose();
        flipped_player_idle_gun_animation.dispose();
    }


}
