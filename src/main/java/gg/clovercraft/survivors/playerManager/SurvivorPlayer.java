package gg.clovercraft.survivors.playerManager;

import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SurvivorPlayer {

    private PlayerEntity player;

    public Integer id;
    public String uuid;
    public String name;
    public Integer lives;
    public Boolean hunter;
    public Boolean vampire;
    public Boolean spartan;

    private long lastAttacked = 0;
    private static final long defendingCooldown = 600;

    public SurvivorPlayer( PlayerEntity PLAYER ) {
        player = PLAYER;
        lastAttacked = 0;
    }

    public boolean isHostile() {
        return ( lives == 1 ) || hunter || vampire;
    }

    public boolean isDefending() {
        long now = player.getWorld().getTime();
        return ( now - lastAttacked ) < defendingCooldown;
    }

    public void setDefending() {
        lastAttacked = player.getWorld().getTime();
    }


}
