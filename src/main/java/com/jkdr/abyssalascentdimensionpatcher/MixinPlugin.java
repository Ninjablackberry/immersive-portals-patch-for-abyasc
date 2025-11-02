package com.jkdr.abyssalascentdimensionpatcher;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.objectweb.asm.tree.ClassNode;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {

    private static Boolean c2meloaded = false;

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        if (mixinClassName.endsWith("MixinItemRandomizer") || mixinClassName.endsWith("MixinBlightwillowRootsPlacer")) {
            return isC2MEInstalled();
        }

        return true; // default
    }

    private static boolean isC2MEInstalled() {
        if (c2meloaded == null) {
            c2meloaded = ModList.get().isLoaded("c2me");
        }
        return c2meloaded;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
