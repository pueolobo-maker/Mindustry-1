package example;

import arc.graphics.Color;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.entities.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.production.*;

public class ExampleJavaMod extends Mod {
    public static Liquid overdriveCoolant;
    public static Block overdriveMixer;
    public static Block coolantOverdrive;

    @Override
    public void loadContent() {
        overdriveCoolant = new Liquid("overdrive-coolant", Color.valueOf("f3e979")) {{
            localizedName = "Overdrive Coolant";
            description = "An ultra-efficient coolant infused with Surge energy that massively boosts turret fire rates and production speeds.";
            barColor = Color.valueOf("f3e979");
            lightColor = Color.valueOf("f3e97980");
            heatCapacity = 1.8f;
            temperature = 0.1f;
            viscosity = 0.3f;
            flammability = 0f;
            explosiveness = 0f;
            coolant = true;
            effect = StatusEffects.overdrive;
        }};

        overdriveMixer = new GenericCrafter("overdrive-mixer") {{
            localizedName = "Overdrive Coolant Mixer";
            description = "Infuses Cryofluid with Surge Alloy to produce Overdrive Coolant.";
            size = 2;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 40f;
            craftTime = 60f;
            outputLiquid = new LiquidStack(overdriveCoolant, 0.3f);
            requirements(Category.crafting, ItemStack.with(
                Items.lead, 80,
                Items.titanium, 60,
                Items.silicon, 60,
                Items.surgeAlloy, 25
            ));

            consumePower(2.5f);
            consumeItem(Items.surgeAlloy, 1);
            consumeLiquid(Liquids.cryofluid, 0.3f);
        }};

        coolantOverdrive = new OverdriveProjector("coolant-overdrive") {{
            localizedName = "Overdrive Coolant Projector";
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
            consumeLiquid(overdriveCoolant, 0.2f);
        }

        public class CoolantOverdriveBuild extends OverdriveBuild {
            @Override
            public void updateTile() {
                super.updateTile();
                if (efficiency > 0f) {
                    Units.nearby(team, x, y, range, unit -> {
                        unit.apply(StatusEffects.overdrive, 9999999f);
                    });
                }
            }
        }};

        // Serpulo Tech Tree nodes
        TechNode cryoNode = TechTree.all.find(t -> t.content == Liquids.cryofluid);
        if (cryoNode != null) {
            new TechNode(cryoNode, overdriveCoolant, overdriveCoolant.researchRequirements());
        }

        TechNode mixerNode = TechTree.all.find(t -> t.content == Blocks.cryofluidMixer);
        if (mixerNode != null) {
            new TechNode(mixerNode, overdriveMixer, overdriveMixer.researchRequirements());
        }

        TechNode domeNode = TechTree.all.find(t -> t.content == Blocks.overdriveDome);
        if (domeNode != null) {
            new TechNode(domeNode, coolantOverdrive, coolantOverdrive.researchRequirements());
        }
    }
}
