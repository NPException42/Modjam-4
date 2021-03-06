/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket.common.core.utils;

import java.util.List;
import java.util.Properties;

import net.gtn.dimensionalpocket.common.core.utils.DPCrashAnalyzer.CrashWrapper;
import net.gtn.dimensionalpocket.common.lib.Reference;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICrashCallable;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import de.npe.gameanalytics.events.GADesignEvent;
import de.npe.gameanalytics.events.GAEvent;
import de.npe.gameanalytics.minecraft.MCSimpleAnalytics;


/**
 * @author NPException
 *
 */
public class DPAnalytics extends MCSimpleAnalytics {
	public static final String GA_GAME_KEY = "12345";
	public static final String GA_SECRET_KEY = "12345";

	public static DPAnalytics analytics;

	private static final String CAT_POCKET = "Pocket:";

	private static final String CAT_PLAYER = "Player:";
	private static final String CAT_TELEPORT = "Teleport:";
	private static final String CAT_TRANSFER = "Transfer:";
	private static final String CAT_STATE = "State:";
	private static final String CAT_ITEM = "Item:";
	private static final String CAT_CRAFTED = "Crafted:";
	private static final String CAT_ENERGY_RF = "EnergyRF:";
	private static final String CAT_FLUIDS = "Fluids:";
	private static final String CAT_TRAPPED = "Trapped:";

	private static final String VAL_TRAPPED_INSIDE_NOT_PLACED = "Inside_NotPlaced";
	private static final String VAL_TRAPPED_INSIDE_EXIT_BLOCKED = "Inside_ExitBlocked";
	private static final String VAL_TRAPPED_OUTSIDE_EXIT_BLOCKED = "Outside_ExitBlocked";
	private static final String VAL_DIRECTION_IN = "In";
	private static final String VAL_DIRECTION_OUT = "Out";


	private static final String ANALYITCS_PLAYER_TELEPORT_IN = CAT_POCKET + CAT_PLAYER + CAT_TELEPORT + VAL_DIRECTION_IN;
	private static final String ANALYITCS_PLAYER_TELEPORT_OUT = CAT_POCKET + CAT_PLAYER + CAT_TELEPORT + VAL_DIRECTION_OUT;
	private static final String ANALYITCS_PLAYER_TRAPPED_INSIDE_NOT_PLACED = CAT_POCKET + CAT_PLAYER + CAT_TRAPPED + VAL_TRAPPED_INSIDE_NOT_PLACED;
	private static final String ANALYITCS_PLAYER_TRAPPED_INSIDE_EXIT_BLOCKED = CAT_POCKET + CAT_PLAYER + CAT_TRAPPED + VAL_TRAPPED_INSIDE_EXIT_BLOCKED;
	private static final String ANALYITCS_PLAYER_TRAPPED_OUTSIDE_EXIT_BLOCKED = CAT_POCKET + CAT_PLAYER + CAT_TRAPPED + VAL_TRAPPED_OUTSIDE_EXIT_BLOCKED;
	private static final String ANALYITCS_TRANSFER_ENERGY_RF_IN = CAT_POCKET + CAT_TRANSFER + CAT_ENERGY_RF + VAL_DIRECTION_IN;
	private static final String ANALYITCS_TRANSFER_ENERGY_RF_OUT = CAT_POCKET + CAT_TRANSFER + CAT_ENERGY_RF + VAL_DIRECTION_OUT;
	private static final String ANALYITCS_TRANSFER_FLUIDS_IN = CAT_POCKET + CAT_TRANSFER + CAT_FLUIDS + VAL_DIRECTION_IN;
	private static final String ANALYITCS_TRANSFER_FLUIDS_OUT = CAT_POCKET + CAT_TRANSFER + CAT_FLUIDS + VAL_DIRECTION_OUT;
	private static final String ANALYTICS_POCKET_PLACED = CAT_POCKET + CAT_STATE + "Placed";
	private static final String ANALYTICS_POCKET_MINED = CAT_POCKET + CAT_STATE + "Mined";

	private static final String ANALYTICS_ITEM_CRAFTED = CAT_ITEM + CAT_CRAFTED;


	public DPAnalytics() {
		super(Reference.VERSION, GA_GAME_KEY, GA_SECRET_KEY);
	}

	@Override
	public boolean isActive() {
		return Reference.MAY_COLLECT_ANONYMOUS_USAGE_DATA && super.isActive();
	}

	@Override
	protected String getConfigFileName() {
		return "net.gtn.dimensionalpocket.DPAnalytics";
	}

	////////////////////////////////////////////
	// Logging of player teleport (-attempts) //
	////////////////////////////////////////////

	private GAEvent teleportIn;

	public void logPlayerTeleportInEvent() {
		if (teleportIn == null) {
			teleportIn = new GADesignEvent(this, ANALYITCS_PLAYER_TELEPORT_IN, null, Float.valueOf(1f));
		}
		event(teleportIn, false);
	}

	private GAEvent teleportOut;

	public void logPlayerTeleportOutEvent() {
		if (teleportOut == null) {
			teleportOut = new GADesignEvent(this, ANALYITCS_PLAYER_TELEPORT_OUT, null, Float.valueOf(1f));
		}
		event(teleportOut, false);
	}

	private GAEvent trappedNotPlaced;

	public void logPlayerTrappedInside_NotPlaced_Event() {
		if (trappedNotPlaced == null) {
			trappedNotPlaced = new GADesignEvent(this, ANALYITCS_PLAYER_TRAPPED_INSIDE_NOT_PLACED, null, Float.valueOf(1f));
		}
		event(trappedNotPlaced, false);
	}

	private GAEvent trappedBlocked;

	public void logPlayerTrappedInside_ExitBlocked_Event() {
		if (trappedBlocked == null) {
			trappedBlocked = new GADesignEvent(this, ANALYITCS_PLAYER_TRAPPED_INSIDE_EXIT_BLOCKED, null, Float.valueOf(1f));
		}
		event(trappedBlocked, false);
	}

	private GAEvent trappedOutside;

	public void logPlayerTrappedOutside_ExitBlocked_Event() {
		if (trappedOutside == null) {
			trappedOutside = new GADesignEvent(this, ANALYITCS_PLAYER_TRAPPED_OUTSIDE_EXIT_BLOCKED, null, Float.valueOf(1f));
		}
		event(trappedOutside, false);
	}

	///////////////////////////////////
	// Logging of RF energy transfer //
	///////////////////////////////////

	public void logRFTransferIn(int amount) {
		eventDesign(ANALYITCS_TRANSFER_ENERGY_RF_IN, Integer.valueOf(amount));
	}

	public void logRFTransferOut(int amount) {
		eventDesign(ANALYITCS_TRANSFER_ENERGY_RF_OUT, Integer.valueOf(amount));
	}

	//////////////////////////////////
	// Logging of fluid mb transfer //
	//////////////////////////////////

	public void logFluidTransferIn(int amount) {
		eventDesign(ANALYITCS_TRANSFER_FLUIDS_IN, Integer.valueOf(amount));
	}

	public void logFluidTransferOut(int amount) {
		eventDesign(ANALYITCS_TRANSFER_FLUIDS_OUT, Integer.valueOf(amount));
	}

	////////////////////////////////////////////////
	// Logging of pocket placed + mined + crafted //
	////////////////////////////////////////////////

	private GAEvent pocketPlaced;

	public void logPocketPlaced() {
		if (pocketPlaced == null) {
			pocketPlaced = new GADesignEvent(this, ANALYTICS_POCKET_PLACED, null, Float.valueOf(1f));
		}
		event(pocketPlaced, false);
	}

	private GAEvent pocketMined;

	public void logPocketMined() {
		if (pocketMined == null) {
			pocketMined = new GADesignEvent(this, ANALYTICS_POCKET_MINED, null, Float.valueOf(1f));
		}
		event(pocketMined, false);
	}

	public void logItemCrafted(String name, int amount) {
		eventDesign(ANALYTICS_ITEM_CRAFTED + name, Integer.valueOf(amount));
	}

	///////////////////
	// Shutdown hook //
	///////////////////

	private static final String CRASH_CHECK_LABEL = "DPAnalytics Crash Check";
	private static boolean hasRegisteredCrash;

	/**
	 * Creates a shutdown hook that looks for crashes
	 */
	public void initShutdownHook() {
		FMLCommonHandler.instance().registerCrashCallable(new ICrashCallable() {
			@Override
			public String call() throws Exception {
				hasRegisteredCrash = true;
				return analytics.isActive() ? ("Will analyze crash-log before shutdown and send it to the " + Reference.MOD_NAME + " developer") : "[inactive]";
			}

			@Override
			public String getLabel() {
				return CRASH_CHECK_LABEL;
			}
		});

		// startup check. 1) to initialize the crash analyzer. 2) to get a crashlog we might not had the time to check yet
		checkCrashLogs();

		Runtime.getRuntime().addShutdownHook(new Thread("DPAnalytics-ShutdownHook") {
			@Override
			public void run() {
				if (analytics.isActive()) {
					if (hasRegisteredCrash) {
						checkCrashLogs();
					} else {
						System.out.println("No crash, we are good.");
					}
				}
			}
		});
	}

	private void checkCrashLogs() {
		CrashWrapper cw = null;
		String type = analytics.isClient ? "[C]" : "[S]";
		try {
			Properties config = analytics.loadConfig();
			cw = DPCrashAnalyzer.analyzeCrash(config, analytics.isClient);
			if (cw != null) {
				analytics.saveConfig(config);

				// this is the same on client and server
				String descritpionAndTrace = cw.report.substring(cw.report.indexOf("Description: ") + 12, cw.report.indexOf("A detailed walkthrough")).trim();

				StringBuilder sb = new StringBuilder(type);
				sb.append(" Filtered -> ").append(descritpionAndTrace).append('\n');

				List<ModContainer> mods = Loader.instance().getActiveModList();
				for (ModContainer mod : mods) {
					sb.append('\n').append(mod.getName());
					sb.append(" {").append(mod.getVersion()).append("}");
				}

				String message = sb.toString();

				//				try {
				//					message = Gzip.compressToBase64(message);
				//				} catch (Exception ex) {
				//					// failed to compress
				//				}
				DPAnalytics.this.eventErrorNOW(cw.severity, message);
			}
		} catch (Exception ex) {
			if (cw == null) {
				DPLogger.warning("We tried to analyze crash reports but failed for some reason: " + ex);
			} else {
				DPLogger.info("We couldn't extract the important bits from the crash-report, so we just send the whole thing");
				String message = cw.report;
				try {
					message = Gzip.compressToBase64(message);
				} catch (Exception e) {
					// failed to compress
				}
				DPAnalytics.this.eventErrorNOW(cw.severity, type + " Complete -> " + message);
			}
		}
	}
}
