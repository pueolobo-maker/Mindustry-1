package example;

import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;

public class ExampleJavaMod extends Mod {
    public static Block coolantOverdrive;

    @Override
    public void loadContent() {
        coolantOverdrive = new OverdriveProjector("coolant-overdrive") {{
            localizedName = "Coolant Overdrive Projector";
            description = "Requires Overdrive Coolant. Boosts nearby buildings by 350% and permanently overdrives friendly units that enter its range.";
            size = 3;
            range = 220f;
            speedBoost = 3.5f;
            speedBoostPhase = 0f;
            useTime = 300f;
            phaseRangeBoost = 0f;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 40f;
            requirements(Category.effect, ItemStack.with(
                Items.lead, 180,
                Items.titanium, 120,
                Items.silicon, 120,
                Items.surgeAlloy, 60
            ));

            consumePower(8f);
            consumeLiquid(Vars.content.liquid("mindustry-1-overdrive-coolant"), 0.2f);
        }

        @Override
        public OverdriveBuild newBuilding() {
            return new OverdriveBuild() {
                @Override
                public void updateTile() {
                    super.updateTile();
                    if (efficiency > 0f) {
                        Units.nearby(team, x, y, range, unit -> {
                            unit.apply(StatusEffects.overdrive, 9999999f);
                        });
                    }
                }
            };
        }};
    }
}
