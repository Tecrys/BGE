/*     */ package bge.data.scripts;
/*     */ 
import bge.data.scripts.world.BGEGen;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BGEModPlugin
/*     */   extends BaseModPlugin
/*     */ {
/*     */   private static void initBGE()
/*     */   {
/*     */     try
/*     */     {
/*  35 */       Global.getSettings().getScriptClassLoader().loadClass("data.scripts.world.ExerelinGen");
/*     */ 
/*     */     }
/*     */     catch (ClassNotFoundException ex)
/*     */     {
/*     */ 
/*  41 */       new BGEGen().generate(Global.getSector());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 

/*     */ }


/* Location:              C:\Games\Starsector08a\mods\BGE\jars\JARCreatorbefore.jar!\bge\data\scripts\BGEModPlugin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */