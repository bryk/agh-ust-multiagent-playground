package pl.edu.agh.multiagent.jade;

import com.google.common.base.Preconditions;

import jade.android.AndroidHelper;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.MicroRuntime;
import jade.core.Profile;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Controller for Jade related operations (such as connecting, creating agents).
 */
public class JadeController {
	/** Logging tag. */
	private static final String TAG = "AgentController";

	/** Jade service used for messaging. */
	private MicroRuntimeServiceBinder microRuntimeServiceBinder;

	/** Connection to the Jade service. */
	private ServiceConnection serviceConnection;

	/** Jade configuration. */
	private final Properties jadeConfiguration = new Properties();

	/** {@link Activity} that works on this controller. */
	private final Activity mainAppActivity;

	/** Creates controller. On emulator use address: 10.0.2.2:1099. */
	public JadeController(String host, String port, Activity mainAppActivity) {
		jadeConfiguration.setProperty(Profile.MAIN_HOST, host);
		jadeConfiguration.setProperty(Profile.MAIN_PORT, port);
		jadeConfiguration.setProperty(Profile.MAIN, Boolean.FALSE.toString());
		jadeConfiguration.setProperty(Profile.JVM, Profile.ANDROID);
		jadeConfiguration.setProperty(Profile.LOCAL_HOST, AndroidHelper.getLocalIPAddress());
		this.mainAppActivity = mainAppActivity;
	}

	/** Starts Jade runtime service. */
	public void startJadeRuntimeService(final RuntimeCallback<Void> callback) {
		if (microRuntimeServiceBinder == null) {
			serviceConnection = new ServiceConnection() {
				public void onServiceConnected(ComponentName className, IBinder service) {
					Log.i(TAG, "Successfully started Jade service");
					microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
					startJadeContainer(callback);
				};

				public void onServiceDisconnected(ComponentName className) {
					microRuntimeServiceBinder = null;
					Log.i(TAG, "Stopped Jade service");
				}
			};
			Log.i(TAG, "Starting Jade service...");
			Intent intent = new Intent(mainAppActivity.getApplicationContext(),
					MicroRuntimeService.class);
			mainAppActivity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
		} else {
			Log.i(TAG, "Jade service already started");
			startJadeContainer(callback);
		}
	}

	/** Starts GameAgent. Needs to be called after starting Jade service. */
	public void startGameAgent(final RuntimeCallback<AgentController> agentStartupCallback) {
		Preconditions.checkNotNull(microRuntimeServiceBinder, "Call startJadeRuntimeService first");
		final String name = "GameAgent" + System.currentTimeMillis();
		microRuntimeServiceBinder.startAgent(name, GameAgent.class.getName(),
				new Object[] { mainAppActivity.getApplicationContext() },
				new RuntimeCallback<Void>() {
					@Override
					public void onSuccess(Void thisIsNull) {
						Log.i(TAG, "Successfully start of the " + GameAgent.class.getName() + "...");
						try {
							agentStartupCallback.onSuccess(MicroRuntime.getAgent(name));
						} catch (ControllerException e) {
							// Should never happen
							agentStartupCallback.onFailure(e);
						}
					}

					@Override
					public void onFailure(Throwable throwable) {
						Log.e(TAG, "Failed to start the " + GameAgent.class.getName() + "...");
						agentStartupCallback.onFailure(throwable);
					}
				});
	}

	/** Starts Jade container. */
	private void startJadeContainer(final RuntimeCallback<Void> callback) {
		if (!MicroRuntime.isRunning()) {
			microRuntimeServiceBinder.startAgentContainer(jadeConfiguration,
					new RuntimeCallback<Void>() {
						@Override
						public void onSuccess(Void thisIsNull) {
							Log.i(TAG, "Successfully started Jade container...");
							callback.onSuccess(thisIsNull);
						}

						@Override
						public void onFailure(Throwable throwable) {
							Log.e(TAG, "Failed to start Jade container...");
							callback.onFailure(throwable);
						}
					});
		} else {
			Log.e(TAG, "Jade container already started");
			callback.onSuccess(null);
		}
	}
}
