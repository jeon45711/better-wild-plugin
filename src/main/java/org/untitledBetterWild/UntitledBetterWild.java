package org.untitledBetterWild;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class UntitledBetterWild extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // 이벤트 리스너 등록
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("BetterWild 플러그인이 활성화되었습니다!");
    }

    // 1. 환영 메시지 기능
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // 전체 공지 메시지 변경
        event.setJoinMessage(ChatColor.YELLOW + "[BetterWild] " + ChatColor.WHITE + player.getName() + "님이 입장하셨습니다!");

        // 들어온 유저에게만 따로 환영 인사
        player.sendMessage(ChatColor.GREEN + "친구들과 함께하는 야생 서버에 오신 것을 환영합니다!");
    }

    // 2. 데스포인트 기능
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location loc = player.getLocation();

        // 사망자에게 좌표 전송 (소수점 버리고 정수로 표시)
        player.sendMessage(ChatColor.RED + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        player.sendMessage(ChatColor.GOLD + " [사망 좌표] " + ChatColor.WHITE +
                "X: " + loc.getBlockX() +
                " / Y: " + loc.getBlockY() +
                " / Z: " + loc.getBlockZ());
        player.sendMessage(ChatColor.RED + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    // 3. 농작물 우클릭 수확 (Replant) 기능
    @EventHandler
    public void onHarvest(PlayerInteractEvent event) {
        // 우클릭 액션이고, 블록을 클릭했을 때만 실행
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block == null) return;

            // 블록 데이터가 나이를 먹는(Ageable) 식물인지 확인 (밀, 감자, 당근 등)
            if (block.getBlockData() instanceof Ageable) {
                Ageable ageable = (Ageable) block.getBlockData();

                // 식물이 다 자랐는지 확인 (최대 성장치 확인)
                if (ageable.getAge() == ageable.getMaximumAge()) {
                    // 1. 아이템 드랍 (자연스럽게 파괴)
                    block.breakNaturally();

                    // 2. 다시 심기 (나이를 0으로 설정)
                    ageable.setAge(0);
                    block.setBlockData(ageable);

                    // (선택 사항) 씨앗 소모 로직을 넣을 수도 있지만, 편의를 위해 무한 재배로 둠
                }
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("BetterWild 플러그인이 비활성화되었습니다.");
    }
}