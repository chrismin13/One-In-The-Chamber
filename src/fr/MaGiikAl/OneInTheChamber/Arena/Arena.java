package fr.MaGiikAl.OneInTheChamber.Arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;
import fr.MaGiikAl.OneInTheChamber.Sign.SignManager;
import fr.MaGiikAl.OneInTheChamber.Utils.UtilChatColor;
import fr.MaGiikAl.OneInTheChamber.Utils.UtilSendMessage;

public class Arena {

	private Type type = Type.LIVES;

	private String name;
	private String displayName;

	private int maxplayers = 8;
	private int minplayers = 4;

	private boolean active = true;

	private Location startLocation;

	private ArrayList<Location> spawnsLocations = new ArrayList<Location>();

	private int lives = 5;
	private int points = 15;

	private boolean privateChat = true;

	public int countdownBeforeStart = 20;

	private Status status = Status.JOINABLE;

	public ScoreboardManager scoremanager;
	public Scoreboard board;
	public Objective objective;

	private HashMap<Player, PlayerArena> players = new HashMap<Player, PlayerArena>();

	public Arena(String arenaName) {
		this.name = arenaName;
		this.displayName = arenaName;

		this.scoremanager = OneInTheChamber.instance.getServer().getScoreboardManager();
		this.board = scoremanager.getNewScoreboard();
	}

	public String getName() {
		return this.name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public int getMaxPlayers() {
		return this.maxplayers;
	}

	public int getMinPlayers() {
		return this.minplayers;
	}

	public Location getStartLocation() {
		return this.startLocation;
	}

	public ArrayList<Location> getSpawnsLocations() {
		return this.spawnsLocations;
	}

	public int getLives() {
		return this.lives;
	}

	public int getMaxPoints() {
		return this.points;
	}

	public Type getType() {
		return this.type;
	}

	public Status getStatus() {
		return this.status;
	}

	public ArrayList<PlayerArena> getPlayers() {
		ArrayList<PlayerArena> players = new ArrayList<PlayerArena>();
		for (PlayerArena player : this.players.values()) {
			players.add(player);
		}
		return players;
	}

	public int getCountdownBeforeStart() {
		return this.countdownBeforeStart;
	}

	public boolean isActive() {
		return this.active;
	}

	public boolean isPrivateChat() {
		return this.privateChat;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDisplayName(String displayName) {
		if (!displayName.isEmpty()) {
			this.displayName = displayName;
		} else {
			this.displayName = this.name;
		}
		SignManager.updateSigns(this);
	}

	public void setMaxPlayers(int mp) {
		this.maxplayers = mp;
		SignManager.updateSigns(this);
	}

	public void setMinPlayers(int mp) {
		this.minplayers = mp;
	}

	public void setActive(boolean active) {
		if (this.active == false && active == true) {
			this.status = Status.JOINABLE;
		}
		if (this.active == true && active == false) {
			File fichier_language = new File(
					OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
			FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);
			String end = Language.getString("Language.Arena.End");
			this.stop(end, Status.NOTJOINABLE);
		}
		this.active = active;
		SignManager.updateSigns(this);
	}

	public void setStartLocation(Location startLoc) {
		this.startLocation = startLoc;
	}

	public void setType(Type type) {
		this.type = type;
		SignManager.updateSigns(this);
	}

	public void addSpawnLocation(Location spawnLoc) {
		this.spawnsLocations.add(spawnLoc);
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void setMaxPoints(int points) {
		this.points = points;
	}

	public void setCountdownBeforeStart(int countdown) {
		this.countdownBeforeStart = countdown;
	}

	public void setStatus(Status status) {
		this.status = status;
		SignManager.updateSigns(this);
	}

	public void setPrivateChat(boolean privateChatOrNot) {
		this.privateChat = privateChatOrNot;
	}

	public boolean isFull() {
		if (this.players.size() >= this.maxplayers) {
			SignManager.updateSigns(this);
			return true;
		}
		return false;
	}

	public void addPlayer(Player player) {

		File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

		String noLobby = Language.getString("Language.Error.Lobby_is_not_set");
		String inGame = Language.getString("Language.Arena.In_game");
		String notActive = Language.getString("Language.Arena.Not_active");
		String isFull = Language.getString("Language.Arena.Full");
		String disable = Language.getString("Language.Arena.Disable");
		String notEnoughSpawns = Language.getString("Language.Error.Not_enough_spawns");
		String alreadyInArena = Language.getString("Language.Arena.Already_in_game");
		String noStart = Language.getString("Language.Error.No_start_location");

		if (!ArenaManager.getArenaManager().isInArena(player)) {
			if (ArenaManager.getArenaManager().getLobbyLocation() != null) {
				if (this.status != Status.INGAME) {
					if (this.status != Status.NOTJOINABLE) {
						if (this.active == true) {
							if (!this.isFull()) {
								if (this.spawnsLocations.size() >= this.maxplayers) {
									if (this.startLocation != null) {

										if (this.objective != null) {
											this.objective.unregister();
											this.objective = null;
										}

										PlayerArena pa = new PlayerArena(player, this);
										this.players.put(player, pa);

										SignManager.updateSigns(this);

										pa.save();
										pa.clear(this.objective);
										pa.getPlayer().updateInventory();

										player.teleport(this.getStartLocation());

										String join = Language.getString("Language.Arena.Join")
												.replaceAll("%player", player.getName())
												.replaceAll("%number", this.players.size() + "")
												.replaceAll("%max", this.maxplayers + "");

										this.broadcast(join);

										if (this.players.size() == this.minplayers) {
											this.startCooldown();
										}
									} else {
										UtilSendMessage.sendMessage(player, noStart);
									}
								} else {
									UtilSendMessage.sendMessage(player, notEnoughSpawns);
								}
							} else {
								UtilSendMessage.sendMessage(player, isFull);
							}
						} else {
							UtilSendMessage.sendMessage(player, notActive);
						}
					} else {
						UtilSendMessage.sendMessage(player, disable);
					}
				} else {
					UtilSendMessage.sendMessage(player, inGame);
				}
			} else {
				UtilSendMessage.sendMessage(player, noLobby);
			}
		} else {
			UtilSendMessage.sendMessage(player, alreadyInArena);
		}
	}

	public void removePlayer(Player player, String pourJoueur, String pourJoueursArene) {
		if (ArenaManager.getArenaManager().isInArena(player)) {
			PlayerArena pa = PlayerArena.getPlayerArenaByPlayer(player);

			pa.clear(this.objective);
			pa.getPlayer().updateInventory();
			pa.restore();
			pa.getPlayer().updateInventory();

			// Remove flight by doublejump
			player.setAllowFlight(false);
			player.setFlying(false);

			pa.teleportToLobby();

			if (!pourJoueur.isEmpty()) {
				pa.sendMessage(pourJoueur);
			}

			this.players.remove(player);
			pa.remove();

			if (!pourJoueursArene.isEmpty()) {
				broadcast(pourJoueursArene);
			}
			if (this.players.size() == 1 && this.status == Status.INGAME) {
				for (PlayerArena winner : this.getPlayers()) {
					this.stop("", Status.JOINABLE);
					this.win(winner.getPlayer());
				}
			}
			if (this.players.isEmpty()) {
				this.stop("", Status.JOINABLE);
			}
			SignManager.updateSigns(this);
		}
	}

	public void stop(String pourJoueurs, Status status) {
		this.setStatus(status);
		if (!this.getPlayers().isEmpty()) {
			for (PlayerArena pa : this.getPlayers()) {
				Player player = pa.getPlayer();
				this.removePlayer(player, pourJoueurs, "");
			}
		}
		SignManager.updateSigns(this);
	}

	public boolean isReady() {
		if (this.name != null && this.displayName != null && this.active != false && this.startLocation != null
				&& !this.spawnsLocations.isEmpty()) {
			if (!(this.maxplayers < this.minplayers)) {
				if (!(this.spawnsLocations.size() < this.minplayers)) {
					return true;
				}
			}
		}
		return false;
	}

	public void win(Player player) {
		File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

		String Gagnant = Language.getString("Language.Arena.Win");

		File fichier_player = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Players"
				+ File.separator + player.getName() + ".yml");
		FileConfiguration playerFile = YamlConfiguration.loadConfiguration(fichier_player);

		playerFile.set("Wins", playerFile.getInt("Wins") + 1);
		try {
			playerFile.save(fichier_player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		UtilSendMessage.sendMessage(player, Gagnant);
	}

	public void saveConfig() {

		File fichier_arena = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Arenas"
				+ File.separator + this.name + ".yml");
		FileConfiguration arenaFile = YamlConfiguration.loadConfiguration(fichier_arena);

		arenaFile.set("Type", this.type.toString());
		arenaFile.set("DisplayName", this.displayName);
		arenaFile.set("Active", this.active);
		arenaFile.set("Lives", this.lives);
		arenaFile.set("Points", this.points);
		arenaFile.set("MaxPlayers", this.maxplayers);
		arenaFile.set("MinPlayers", this.minplayers);
		arenaFile.set("CountdownBeforeStart", this.countdownBeforeStart);
		arenaFile.set("PrivateChat", this.privateChat);

		if (this.startLocation == null) {
			arenaFile.set("StartLocation", "");
		} else {
			if (this.startLocation != null) {
				arenaFile.set("StartLocation",
						this.startLocation.getWorld().getName() + ", " + this.startLocation.getX() + ", "
								+ this.startLocation.getY() + ", " + this.startLocation.getZ() + ", "
								+ this.startLocation.getYaw() + ", " + this.startLocation.getPitch());
			}
		}

		if (this.spawnsLocations.isEmpty()) {

			arenaFile.set("Spawns", "");

		} else {

			if (!this.spawnsLocations.isEmpty()) {

				if (this.spawnsLocations != null) {

					List<String> spawnsLocationsList = new ArrayList<String>();

					for (Location spawnLocation : this.spawnsLocations) {
						spawnsLocationsList.add(spawnLocation.getWorld().getName() + ", " + spawnLocation.getX() + ", "
								+ spawnLocation.getY() + ", " + spawnLocation.getZ() + ", " + spawnLocation.getYaw()
								+ ", " + spawnLocation.getPitch());
					}

					arenaFile.set("Spawns", spawnsLocationsList);
				}

			}
		}
		try {
			arenaFile.save(fichier_arena);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcast(String message) {
		for (PlayerArena pa : this.players.values()) {
			pa.sendMessage(message);
		}
	}

	public void chat(String message) {
		for (PlayerArena pa : this.players.values()) {
			pa.tell(message);
		}
	}

	public void startCooldown() {
		if (this.status != Status.INGAME || this.status != Status.STARTING) {
			this.setStatus(Status.STARTING);
			if (this.objective != null) {
				this.objective.unregister();
				this.objective = null;
			}
			@SuppressWarnings("unused")
			BukkitTask task = new Timer(this, this.countdownBeforeStart).runTaskTimer(OneInTheChamber.instance, 0L,
					20L);
		}
		SignManager.updateSigns(this);
	}

	public void start() {

		File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

		String NomScoreboard = UtilChatColor.colorizeString(Language.getString("Language.Scoreboard.Name"));

		String Demarrage = Language.getString("Language.Arena.Start");

		if (this.objective != null) {
			this.objective.unregister();
			this.objective = null;
		}

		this.objective = this.board.registerNewObjective("Kills", "KillCount");

		this.objective.setDisplayName(NomScoreboard);
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		if (this.players.size() <= 0)
			return;

		this.setStatus(Status.INGAME);

		SignManager.updateSigns(this);

		int i = 0;
		for (PlayerArena pa : this.getPlayers()) {

			pa.reset();

			File fichier_player = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Players"
					+ File.separator + pa.getName() + ".yml");
			FileConfiguration playerFile = YamlConfiguration.loadConfiguration(fichier_player);

			playerFile.set("Played", playerFile.getInt("Played") + 1);

			try {
				playerFile.save(fichier_player);
			} catch (IOException e) {
				e.printStackTrace();
			}

			pa.getPlayer().teleport(this.spawnsLocations.get(i));
			i++;
			pa.loadGameInventory();

			pa.playSound(Sound.ENTITY_WITHER_DEATH, 1.0F);
		}
		this.updateScores();
		broadcast(Demarrage);
	}

	public void updateScores() {
		for (PlayerArena pa : this.getPlayers()) {
			pa.setScoreboard(this.objective);
		}
	}
}
