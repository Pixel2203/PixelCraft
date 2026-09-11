package com.example.examplemod.api.circleMagic.processors;

import com.example.examplemod.api.circleMagic.rituals.RitualState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RitualPipelineHandler {
   private static HashMap<RitualState, MagicCircleProcessor> processorRegistry = new HashMap<RitualState, MagicCircleProcessor>();


   public RitualPipelineHandler() {
       processorRegistry.put(RitualState.FREE, ProcessorRegistry.CIRCLE_INITIATE_RITUAL_PROCESSOR);
       processorRegistry.put(RitualState.COLLECTING, ProcessorRegistry.CIRCLE_COLLECT_PROCESSOR);
       processorRegistry.put(RitualState.EVALUATING, ProcessorRegistry.CIRCLE_EVALUATE_RECIPE_PROCESSOR);
       processorRegistry.put(RitualState.SPAWN_ITEM, ProcessorRegistry.CIRCLE_SPAWN_ITEM_PROCESSOR);
       processorRegistry.put(RitualState.RITUAL_SETUP, ProcessorRegistry.CIRCLE_SETUP_RITUAL_PROCESSOR);
       processorRegistry.put(RitualState.RITUAL_PROCESSING, ProcessorRegistry.CIRCLE_RITUAL_TICK_PROCESSOR);
   }

   public @NotNull MagicCircleProcessor getProcessor(RitualState ritualState) { return processorRegistry.get(ritualState); }



   private class ProcessorRegistry {
       public static MagicCircleProcessor CIRCLE_INITIATE_RITUAL_PROCESSOR = new RitualInitiatingProcessor();
       public static MagicCircleProcessor CIRCLE_COLLECT_PROCESSOR = new ItemCollectingProcessor();
       public static MagicCircleProcessor CIRCLE_EVALUATE_RECIPE_PROCESSOR = new RecipeMatchingProcessor();
       public static MagicCircleProcessor CIRCLE_RITUAL_TICK_PROCESSOR = new RitualExecutionProcessor();
       public static MagicCircleProcessor CIRCLE_SPAWN_ITEM_PROCESSOR = new ItemSpawningProcessor();
       public static MagicCircleProcessor CIRCLE_SETUP_RITUAL_PROCESSOR = new RitualSetupProcessor();

   }
}
