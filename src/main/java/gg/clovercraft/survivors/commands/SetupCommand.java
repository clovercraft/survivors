package gg.clovercraft.survivors.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import gg.clovercraft.survivors.playerManager.PlayerManager;
import gg.clovercraft.survivors.playerManager.SurvivorPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardCriterion.RenderType;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class SetupCommand implements Command<ServerCommandSource> {

    public static final int OP = 4;
    private Scoreboard scoreboard;
    private PlayerManager players;

    public SetupCommand( PlayerManager PLAYERS ) {
        players = PLAYERS;
    }


    @Override
    public int run( CommandContext<ServerCommandSource> context ) {

        if( context.getSource().getEntity().isPlayer() ) {
            try {
                ServerPlayerEntity player = context.getSource().getPlayer();
                if( player.hasPermissionLevel( OP ) ) {
                    doSetup( player );
                } else {
                    player.sendMessage( Text.of( "You don't have permission to do that."), false );
                }
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }

        return 1;
    }

    public void doSetup( ServerPlayerEntity player ) {
        // create teams
        scoreboard = player.getWorld().getScoreboard();
        addTeam( "survivors_healthy", Formatting.DARK_GREEN, "Healthy" );
        addTeam( "survivors_three", Formatting.GREEN, "Three Lives" );
        addTeam( "survivors_two", Formatting.YELLOW, "Two Lives" );
        addTeam( "survivors_one", Formatting.RED, "Last Life" );

        // create the timer, but don't start it.
        scoreboard.addObjective( "survivors_insession", ScoreboardCriterion.DUMMY, Text.of( "In Session" ), RenderType.INTEGER );
        ScoreboardPlayerScore score = new ScoreboardPlayerScore( scoreboard, scoreboard.getObjective( "survivors_insession" ), "$running" );
        score.setScore(0);

        registerPlayers( player.getWorld() );
    }

    private void addTeam( String name, Formatting color, String displayName ) {
        scoreboard.addTeam( name );
        Team team = scoreboard.getTeam( name );
        team.setColor( color );
        team.setDisplayName( Text.of( displayName ) );
    }

    private void registerPlayers( World world ) {
        List<? extends PlayerEntity> serverPlayers = world.getPlayers();
        for( int i = 0; i < serverPlayers.size(); i++ ) {
            PlayerEntity player = serverPlayers.get( i );
            SurvivorPlayer survivor = players.getRegisteredSurvivor( player );
            switch( survivor.lives ) {
                case 6:
                case 5:
                case 4:
                    scoreboard.addPlayerToTeam( player.getEntityName(), scoreboard.getTeam( "survivors_healthy" ) );
                    break;
                case 3:
                default:
                    scoreboard.addPlayerToTeam( player.getEntityName(), scoreboard.getTeam( "survivors_three" ) );
                    break;
                case 2:
                    scoreboard.addPlayerToTeam( player.getEntityName(), scoreboard.getTeam( "survivors_two" ) );
                    break;
            }
        }
    }
}
