package net.akami.yggdrasil.api.spell;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class AbstractSpellCaster extends SpellCaster {

    public AbstractSpellCaster() {
        super();
        super.generator = loadGenerator();
        super.baseSequence = loadSequence();
        super.manaUsage = loadManaUsage();
        super.locationRequiredTiers = loadLocationRequiredTiers();
    }

    protected abstract Supplier<Spell> loadGenerator();
    protected abstract List<ElementType> loadSequence();
    protected abstract BiFunction<Float, Integer, Float> loadManaUsage();
    protected List<Integer> loadLocationRequiredTiers() { return Collections.emptyList(); }
}
