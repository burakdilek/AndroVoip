/* Copyright (C) 2010-2011, Mamadou Diop.
*  Copyright (C) 2011, Doubango Telecom.
*
* Contact: Mamadou Diop <diopmamadou(at)doubango(dot)org>
*	
* This file is part of imsdroid Project (http://code.google.com/p/imsdroid)
*
* imsdroid is free software: you can redistribute it and/or modify it under the terms of 
* the GNU General Public License as published by the Free Software Foundation, either version 3 
* of the License, or (at your option) any later version.
*	
* imsdroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
* See the GNU General Public License for more details.
*	
* You should have received a copy of the GNU General Public License along 
* with this program; if not, write to the Free Software Foundation, Inc., 
* 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package org.doubango.ngn;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.doubango.ngn.utils.NgnStringUtils;
import org.doubango.utils.AndroidUtils;
import org.doubango.utils.CpuFeatures_t;

import android.app.ActivityManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/** @mainpage Foreword
 * 
 * <h1>Foreword</h1>
 * <b>android-ngn-stack</b> is a <a href="http://en.wikipedia.org/wiki/Next_generation_network">NGN</a> (Next Generation Network) stack for Android 2.x (or later) devices. <br />
 * The Stack is based on <a href="http://doubango.org">doubango</a> framework. <a href="http://doubango.org">doubango</a> is the world's most advanced open source 
 * 3GPP IMS/RCS framework for both embedded and desktop systems. <br />
 * The main purpose is to provide an open source stack for the developers to build their own VoIP applications. <br />
 * This framework offers a unique set of features ranging from audio/video calls, content sharing, messaging, conferencing, enhanced address book to social presence.
 * All these features are implemented in accordance with the standards: GSMA RCS, 3GPP IMS or VoLTE.<br />
 * 
 * @page Introduction
 * This document has been written by us (Doubango Telecom) to help developers to quickly create innovative multimedia applications
 * for the Android OS. If you are a developer and is looking for the best way to develop a NGN (VoIP, Messaging, Video Conferencing, ...) or rich application for Android
 * then your are at the right place. <br />
 * If you want to get help or have some feedbacks then please visit our website: <a href="http://code.google.com/p/imsdroid/">http://code.google.com/p/imsdroid/</a>
 * 
 * <h2>Doubango Solution</h2>
 * <b>android-ngn-stack</b> is part of Doubango Solution which include many components such as:
 * 
 * <h3>Client-side components</h3>
 * - <a href="http://code.google.com/p/boghe/">Boghe</a>: IMS/RCS Client for Windows
 * - <a href="http://code.google.com/p/imsdroid/">IMSDroid</a>: IMS/RCS Client for Android using <b>android-ngn-stack</b>
 * - <a href="http://code.google.com/p/idoubs/">iDoubs</a>: IMS/RCS Client for iOS (iPhone, iPad and iPod Touch)
 * 
 * <h3>Server-side components</h3>
 * - <a href="http://code.google.com/p/openvcs/">OpenVCS</a>: OpenVCS stands for Open Source Video Conferencing Server and is used to manage Multipoint Control Units (MCU). Each MCU (a.k.a Bridge) can handle up to 64 participants
 * - <a href="http://code.google.com/p/flash2ims/">Flash2IMS</a>: Adobe(c) Flash(c) to SIP/IMS Gateway.
 * 
 * <h2>Highlights</h2>
 *     
 * - SIP(RFC 3261, 3GPP TS 24.229 Rel-9)
 * - TCP and UDP over IPv4 or IPv6
 * - Signaling Compression, SigComp(RFC 3320, 3485, 4077, 4464, 4465, 4896, 5049, 5112 and 1951)
 *
 * - Enhanced Address Book (XCAP storage, authorizations, presence, ...)
 * - GSMA Rich Communication Suite release 3
 * - Partial supports for One Voice Profile V1.0.0 (GSMA VoLTE)
 * - Partial supports for MMTel UNI (used by GSMA RCS and GSMA VoLTE)
 *
 * - IMS-AKA registration (both AKA-v1 and AKA-v2), Digest MD5, Basic
 * - 3GPP Early IMS Security (3GPP TS 33.978)
 * - Proxy-CSCF discovery using DNS NAPTR+SRV
 * - Private extension headers for 3GPP
 * - Service Route discovery
 * - Subscription to reg event package (Honoring network initiated (re/de/un)-registration events)
 *
 * - 3GPP SMS Over IP (3GPP TS 23.038, 24.040, 24.011, 24.341 and 24.451)
 * - Voice Call (G729AB1, AMR-NB, iLBC, GSM, PCMA, PCMU, Speex-NB)
 * - Video Call (H264, MP4V-ES, Theora, H.263, H.263-1998, H.261)
 * - DTMF (RFC 4733)
 * - QoS negotiation using Preconditions (RFC 3312, 4032 and 5027)
 * - SIP Session Timers (RFC 4028)
 * - Provisional Response Acknowledgments (PRACK)
 * - Communication Hold (3GPP TS 24.610)
 * - Message Waiting Indication (3GPP TS 24.606)
 * - Calling E.164 numbers by using ENUM protocol (RFC 3761)
 * - NAT Traversal using STUN2 (RFC 5389) with possibilities to automatically discover the server by using DNS SRV (TURN already implemented and ICE is under tests) 
 * 
 * - One2One and Group Chat
 * - File Transfer and Content sharing
 * 
 * 
 * @page page__Setting_Up_NGN_project Setting up NGN project
 * @anchor anchor_Setting_Up_NGN_project
 * This section explain how to setup a NGN project using Eclipse.<br />
 * 
 * <h2>Checking out the source code</h2>
 * To check out the source code of the NGN library you will need a SVN client.<br />
 * Use this command to anonymously check out the last project source:
 * @code
 * svn checkout http://imsdroid.googlecode.com/svn imsdroid
 * @endcode
 * The source code of the library is under:
 * @code
 * imsdroid/branches/2.0/android-ngn-stack
 * @endcode
 * 
 * <h2>Importing the NGN project into Eclipse</h2>
 * The NGN project is the Next Generation Network library.
 * - Open eclipse
 * - Go to File -> Import -> General -> Existing Project into workspace
 * - Select <b> android-ngn-stack </b> folder and click <b>Finish</b>
 * 
 * @image html ngn_eclipse_import.png "Importing project into your workspace"
 * 
 * <h2>Creating you first NGN application using Eclipse</h2>
 * - Open Eclipse and select File -> New -> Android Project
 * - From the next window (<b>"New Android Project"</b>) fill the text fields like this:<br />
 * 		- Project name: <b>myFirstApp</b><br />
 * 		- Location: < set any path ><br />
 * 		- Build Target: <b>Android 2.0</b> (at least)<br />
 * 		- Application name: <b>myFirstApp</b><br />
 * 		- Package name: <b>org.doubango.test</b><br />
 * 		- Check <b>"Create Activity"</b> and name it <b>"Main"</b><br />
 * 
 * @image html ngn_eclipse_newproj.png "Create your first NGN application"
 * - Click on Finish to create the project
 * - From the Eclipse package explorer, right click on <b>myFirstApp</b> and select <b>"Properties"</b> then "Android" from the left<br/><br/>
 * @image html ngn_eclipse_properties_1.png "NGN application properties"
 * - From the properties window, select <b>"Add"</b> button then select <b>android-ngn-stack</b> from the list of the available libraries<br/><br/>
 * @image html ngn_eclipse_properties_2.png "Add dependencies"
 * - Select <b>"Java Compiler"</b> from the left and change the version from 1.5 to 1.6<br/><br/>
 * @image html ngn_eclipse_jdk_version.png "Java Compiler version"
 * - Select <b>"Java Build Path"</b> from the left, then <b>"Libraries"</b><br/><br/>
 * @image html ngn_eclipse_java_buil_path_1.png "Java Build Path 1/2"
 * - From "Java Build Path 1/2", select <b>"Add JARs..."</b> then <b>android-ngn-stack/libs/simple-xml-2.3.4.jar</b>, then <b>"OK"</b> to close the window<br/><br/>
 * @image html ngn_eclipse_java_buil_path_2.png "Java Build Path 2/2"
 * - Click on <b>"OK"</b> to close the window
 * 
 * <h2>Setting up Android Permissions</h2>
 * In order to use the framework you must enable some user-permission in your Android manifest. <br />
 * Open <b>myFirstApp/AndroidManifest.xml</b>, then add this:
 * @code
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
 * 
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.CAMERA" />
 * <uses-permission android:name="android.permission.WAKE_LOCK" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 * <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
 * <uses-permission android:name="android.permission.VIBRATE" />
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 * 
 * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
 * <uses-permission android:name="android.permission.READ_CONTACTS"/>
 * <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 * <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
 * <uses-permission android:name="android.permission.CALL_PHONE" />
 * <uses-permission android:name="android.permission.RAISED_THREAD_PRIORITY"/>
 *  @endcode
 *  ... just before @code </manifest> @endcode
 * 
 * 
 * <h2>Declaring your app as NGN</h2>
 * Decalaring your app as NGN is recommended if your are programming at <b>high</b> level. <br />
 * - From the Eclipse package explorer, open <b>AndroidManifest.xml</b> and select <b>Application</b> tab from below
 * - Click on <b>browse</b> (on the right of <b>Name</b>) then, select <b>"NgnApplication"</b> from the list<br/><br/>
 * @image html ngn_eclipse_declaring_ngn_app.png "Declaring your app as NGN"
 * 
 * 
 * @page Architecture
 * The stack offers three levels of programming: <b>Low</b>, <b>Medium</b> and <b>High</b>.<br />
 * Before building and running your project, you should take a look at the section @ref anchor_Setting_Up_NGN_project "explaining how to setup a NGN project".
 * <h2>Low level</h2>
 * This level allow you to directly have access to doubango functions through JNI.
 * This level is the most flexible one but is out of scoop because it's too difficult to manage. <br />
 * All functions used in this level are in one single package: <b>org.doubango.tinyWRAP</b><br />
 * For example, the code below shows how to register to a SIP/IMS server:
 * @code
 * final String realm = "sip:doubango.org";
 * final String privateIdentity = "001";
 * final String publicIdentity = "sip:001@doubango.org";
 * final String password = "my secret";
 * final String proxyHost = "192.168.0.1";
 * RegistrationSession registrationSession;
 * // Sip Callback
 * final SipCallback callback = new SipCallback(){
 * 	@Override
 * 	public int OnDialogEvent(DialogEvent e) {
 * 		final SipSession sipSession = e.getBaseSession();
 * 		final long sipSessionId = sipSession.getId();
 * 		final short code = e.getCode();
 * 		switch (code){
 * 			case tinyWRAPConstants.tsip_event_code_dialog_connecting:
 * 				if(registrationSession != null && registrationSession.getId() == sipSessionId){
 * 					// Registration in progress
 * 				}
 * 				break;
 * 			case tinyWRAPConstants.tsip_event_code_dialog_connected:
 * 				if(registrationSession != null && registrationSession.getId() == sipSessionId){
 * 					// You are registered
 * 				}
 * 				break;
 * 			case tinyWRAPConstants.tsip_event_code_dialog_terminating:
 * 				if(registrationSession != null && registrationSession.getId() == sipSessionId){
 * 					// You are unregistering
 * 				}
 * 				break;
 * 			case tinyWRAPConstants.tsip_event_code_dialog_terminated:
 * 				if(registrationSession != null && registrationSession.getId() == sipSessionId){
 * 					// You are unregistered
 * 				}
 * 				break;
 * 		}
 * 			
 * 		return 0;
 * 	}
 * 
 * 	@Override
 * 	public int OnRegistrationEvent(RegistrationEvent e) {
 * 		// low level events
 * 		return 0;
 * 	}
 * };
 * // Create the SipStack
 * SipStack sipStack = new SipStack(callback, realm, privateIdentity, publicIdentity);
 * // Set Proxy Host and port
 * sipStack.setProxyCSCF(proxyHost, 5060, "UDP", "IPv4");
 * // Set password
 * sipStack.setPassword(password);
 * if(sipStack.isValid()){
 * 	if(sipStack.start()){
 * 		registrationSession = new RegistrationSession(sipStack);
 * 		registrationSession.setFromUri(publicIdentity);
 * 		// Send SIP register request
 * 		registrationSession.register_();
 * 	}
 * }
 * @endcode
 * 
 * <h2>Medium level</h2>
 * This level is built on of the <b>low</b> level. The main advantage of this level is that it's flexible without
 * being too complicated as all low level functions are wrapped into comprehensive API.
 * For example, if you want to implement a multi-stack (multi-account) application this is the right level.
 * 
 * <h2>High level</h2>
 * This level is built in top of the <b>low</b> level and is much easier than the later.
 * The High level is composed of a set of Services managed by a single NGN engine instance. Each service is responsible for
 * a particular task. For example, you have one service for SIP, one for contact management, one for networking etc etc <br />
 * 
 * <h3>NGN Engine</h3>
 * The engine is a black box containing all the services. You must always retrieve the services through the engine. <br />
 * You must also start/stop the services through the NGN engine.<br />
 * The code below shows how to get an instance of the engine:
 * @code
 * // Gets an instance of the engine. This function will always returns the same instance
 * // which means that you can call it as many as you want from anywhere in your code 
 * final NgnEngine mEngine = NgnEngine.getInstance();
 * @endcode
 * The code below shows how to get some services from the engine:
 * @code
 * // Gets the configuration service
 * INgnConfigurationService mConfigurationService = mEngine.getConfigurationService();
 * // Gets the SIP/IMS service
 * INgnSipService mSipService = mEngine.getSipService();
 * // etc etc
 * @endocde
 * The code below shows how to start/stop the engine.
 * @code
 * // Starts the engine
 * mEngine.start();
 * // Stops the engine
 * mEngine.stop();
 * @endcode
 * Starting/Stopping the engine will start/stop all underlying services.
 * 
 * 


/**
 * Global object defining the application. You should extends this class in your own
 * Android application. 
 */
public class NgnApplication extends Application{
	private final static String TAG = NgnApplication.class.getCanonicalName();
	
	private static NgnApplication sInstance;
	private static PackageManager sPackageManager;
    private static String sPackageName;
    private static String sDeviceURN;
    private static String sDeviceIMEI;
    private static int sSdkVersion;
    private static int sVersionCode;
    private static AudioManager sAudioManager;
    private static SensorManager sSensorManager;
    private static KeyguardManager sKeyguardManager;
    private static ConnectivityManager sConnectivityManager;
    private static PowerManager sPowerManager;
    private static PowerManager.WakeLock sPowerManagerLock;
    private static int sGlEsVersion;
    static final String sBuildModel = Build.MODEL.toLowerCase();
    static final String[] sSLEs2FriendlyBuildModels = 
    {
    	"galaxy nexus", /* 4.1 */
    	"gt-i9100", /* Galaxy SII, 4.0.4 */
    	/*"gt-s5360",*//* 2.3.6 :robotic*/ 
    	"gt-s5570i", /* 2.3.0 */
    	"xt890", /* Motorola Razer i 4.0.4 */
    	"lg-p970" /* 2.3.4 */
    };
    static final String[] sSLEs2UnFriendlyBuildModels = 
	{
    	"gt-s5360",/* 2.3.6 :robotic*/
	};
    static final String[] sSetModeFriendlyBuildModels = 
    {
    	"gt-s5570i" /* 2.3.6 */
    };
    
    // This function is called by the package manager, you must never explicitly invoke it.
    // Do not forget to add/change the <application /> section in your manifest as done in IMSDroid or all other test apps
    public NgnApplication() {
    	sInstance = this;
    }

    public static NgnApplication getInstance(){
    	return sInstance;
    }
    
    /**
     * Retrieve application's context
     * @return Android context
     */
    public static Context getContext() {
        return getInstance();
    }
    
    @Override
	public void onCreate() {
		super.onCreate();
		
		sPackageManager = sInstance.getPackageManager();    		
		sPackageName = sInstance.getPackageName();
		
		Log.d(TAG,"Build.MODEL="+sBuildModel);
		Log.d(TAG,"Build.VERSION.SDK="+Build.VERSION.SDK);
	}
    
    /**
     * Gets Android SDK version
     * @return Android SDK version used to build the project
     */
    public static int getSDKVersion(){
    	if(sSdkVersion == 0){
    		sSdkVersion = Integer.parseInt(Build.VERSION.SDK);
    	}
    	return sSdkVersion;
    }
    
    /**
     * Whether we need special hack for buggy speaker. For example, all Samsung devices 
     * need to be hacked.
     * @return true if we need to apply the hack and false otherwise
     */
    public static boolean useSetModeToHackSpeaker(){
        return  (isSamsung() && !isSamsungGalaxyMini() && getSDKVersion()<= 7) ||
                
        sBuildModel.equalsIgnoreCase("blade")    ||		// ZTE Blade
                
        sBuildModel.equalsIgnoreCase("htc_supersonic") || //HTC EVO
                
        sBuildModel.equalsIgnoreCase("U8110") || // Huawei U8110
        sBuildModel.equalsIgnoreCase("U8150")  // Huawei U8110
                
                ;
    }   
    
    public static boolean isARMv7WithoutNeon(){
        return (isCpuARMv7() && !isCpuNeon());
    }
    
    public static boolean isCpuARMv7(){
    	return ((AndroidUtils.getCpuFeatures().intValue() & CpuFeatures_t.ARMv7.swigValue()) != 0);
    }
    
    public static boolean isCpuNeon(){
    	return ((AndroidUtils.getCpuFeatures().intValue() & CpuFeatures_t.NEON.swigValue()) != 0);
    }
    
    /**
     * Whether the stack is running on a Samsung Galaxy Mini device
     * @return true if the stack is running on a Samsung Galaxy Mini device and false otherwise
     */
    public static boolean isSamsungGalaxyMini(){
    	return sBuildModel.equalsIgnoreCase("gt-i5800");
    }
        
    /**
     * Whether the stack is running on a Samsung Galaxy Mini device
     * @return true if the stack is running on a Samsung Galaxy Mini device and false otherwise
     */
    public static boolean isSamsung(){
    	return sBuildModel.startsWith("gt-") 
		|| sBuildModel.contains("samsung") 
		|| sBuildModel.startsWith("sgh-") 
		|| sBuildModel.startsWith("sph-") 
		|| sBuildModel.startsWith("sch-");
    }
    
    /**
     * Whether the stack is running on a HTC device
     * @return true if the stack is running on a HTC device and false otherwise
     */
    public static boolean isHTC(){
    	return sBuildModel.startsWith("htc");
    }
    
    /**
     * Whether the stack is running on a ZTE device
     * @return true if the stack is running on a ZTE device and false otherwise
     */
    public static boolean isZTE(){
    	return sBuildModel.startsWith("zte");
    }
    
    /**
     * Whether the stack is running on a LG device
     * @return true if the stack is running on a LG device and false otherwise
     */
    public static boolean isLG(){
    	return sBuildModel.startsWith("lg-");
    }
    
    /**
     * Whether the stack is running on a Toshiba device
     * @return true if the stack is running on a Toshiba device and false otherwise
     */
    public static boolean isToshiba(){
    	return sBuildModel.startsWith("toshiba");
    }
    
    /**
     * Whether the stack is running on a Hovis box
     * @return true if the stack is running on a Hovis box and false otherwise
     */
    public static boolean isHovis(){
    	return sBuildModel.startsWith("hovis_box_");
    }
    
    public static boolean isAudioRecreateRequired(){
    	return false;
    }
    
    public static boolean isSetModeAllowed(){
    	return isZTE() || isLG() || Arrays.asList(sSetModeFriendlyBuildModels).contains(sBuildModel);
    }
    
    public static boolean isBuggyProximitySensor(){
    	return isZTE();
    }
    
    public static boolean isAGCSupported(){
    	return isSamsung() || isHTC();
    }
    
    public static int getVersionCode(){
    	if(sVersionCode == 0 && sPackageManager != null){
    		try {
    			sVersionCode = sPackageManager.getPackageInfo(sPackageName, 0).versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
    	}
    	return sVersionCode;
    }
    
    public static String getVersionName(){
    	if(sPackageManager != null){
    		try {
				return sPackageManager.getPackageInfo(sPackageName, 0).versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
    	}
    	return "0.0";
    }
    
    public static String getDeviceURN(){
    	if(NgnStringUtils.isNullOrEmpty(sDeviceURN)){
	    	try{
		    	final TelephonyManager telephonyMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
		        final String msisdn = telephonyMgr.getLine1Number();
		        if(NgnStringUtils.isNullOrEmpty(msisdn)){
		        	sDeviceURN = String.format("urn:imei:%s", telephonyMgr.getDeviceId());
		        }
		        else{
		        	sDeviceURN = String.format("urn:tel:%s", msisdn);
		        }
	    	}
	    	catch(Exception e){
	    		Log.d(TAG, e.toString());
	    		sDeviceURN = "urn:uuid:3ca50bcb-7a67-44f1-afd0-994a55f930f4";
	    	}
    	}
    	return sDeviceURN;
    }
    
    public static String getDeviceIMEI(){
    	if(NgnStringUtils.isNullOrEmpty(sDeviceIMEI)){
    		final TelephonyManager telephonyMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    		sDeviceIMEI = telephonyMgr.getDeviceId();
    	}
    	return sDeviceIMEI;
    }
    
    public static AudioManager getAudioManager(){
    	if(sAudioManager == null){
    		sAudioManager = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
    	}
    	return sAudioManager;
    }
    
    public static SensorManager getSensorManager(){
    	if(sSensorManager == null){
    		sSensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
    	}
    	return sSensorManager;
    }
    
    public static KeyguardManager getKeyguardManager(){
    	if(sKeyguardManager == null){
    		sKeyguardManager = (KeyguardManager)getContext().getSystemService(Context.KEYGUARD_SERVICE);
    	}
    	return sKeyguardManager;
    }
    
    public static ConnectivityManager getConnectivityManager(){
    	if(sConnectivityManager == null){
    		sConnectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
    	}
    	return sConnectivityManager;
    }
    
    public static PowerManager getPowerManager(){
    	if(sPowerManager == null){
    		sPowerManager = (PowerManager) getContext().getSystemService(POWER_SERVICE);
    	}
    	return sPowerManager;
    }
    
    public static Display getDefaultDisplay(){
    	return ((WindowManager)getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
    }
    
    public static String getABI(){
    	try {
			Field field = android.os.Build.class.getField("CPU_ABI");
			String abi = field.get(null).toString();
			if(abi == null){
				return "unknown";
			}
			return abi;
		} catch (Exception e) {
			e.printStackTrace();
			return "unknown";
		}
    }
    
    public static int getGlEsVersion(){
    	if(sGlEsVersion == 0){
		    sGlEsVersion = ((ActivityManager) NgnApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().reqGlEsVersion;
		    Log.d(TAG, "sGlEsVersion=" + sGlEsVersion);
    	}
    	return sGlEsVersion;
    }
    
    public static boolean isGlEs2Supported(){
    	return getGlEsVersion() >= 0x20000;
    }
    
    public static boolean isSLEs2Supported(){
    	return (NgnApplication.getSDKVersion() >= 9);
    }
    
    public static boolean isSLEs2KnownToWork(){
    	// FIXME: one-way audio on Galaxy SII and Nexus
    	// return isSLEs2Supported() && Arrays.asList(sSLEs2FriendlyBuildModels).contains(sBuildModel); // AcceptOnlyIn(Array)
    	// return isSLEs2Supported() && !Arrays.asList(sSLEs2UnFriendlyBuildModels).contains(sBuildModel); // AcceptAllExceptIn(Array)
    	return isHovis(); //false;
    }

    /**
     * http://developer.android.com/intl/ja/reference/android/os/PowerManager.html#isIgnoringBatteryOptimizations(java.lang.String)
     * @return whether the given application package name is on the device's power whitelist. Apps can be placed on the whitelist through the settings UI invoked by ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
     */
    public static boolean isIgnoringBatteryOptimizations() {
        if (NgnApplication.getSDKVersion() >= 23) {
            try {
                final Method isIgnoringBatteryOptimizationsMth = PowerManager.class.getDeclaredMethod("isIgnoringBatteryOptimizations", String.class);
                final Boolean ignoring = (Boolean) isIgnoringBatteryOptimizationsMth.invoke(NgnApplication.getPowerManager(), NgnApplication.getContext().getPackageName());
                return ignoring.booleanValue();
            } catch (NoSuchMethodException e) {
                Log.e(TAG, e.toString());
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.toString());
            } catch (InvocationTargetException e) {
                Log.e(TAG, e.toString());
            }
        }
        // Before Android-M there were no Doze-StanBy feature (http://developer.android.com/intl/ja/training/monitoring-device-state/doze-standby.html)
        return true;
    }

    /**
     * Raises "ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS" intent
     * @param context The context to use to raise the intent
     * @return true if succeed; otherwise false
     */
    public static boolean ignoringBatteryOptimizations(Context context) {
        if (NgnApplication.getSDKVersion() < 23) {
            return true;
        }
        final Context context_ = context != null ? context : NgnApplication.getContext();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS_ = null;
        try {
            ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS_ = (String)android.
                    provider.Settings.class.getField("ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS").get(null);
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.toString());
            return false;
        } catch (NoSuchFieldException e) {
            Log.e(TAG, e.toString());
            return false;
        }
        if (NgnApplication.isIgnoringBatteryOptimizations()) {
            intent.setAction(ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS_);
        } else {
            final String UriString = "package:" + context_.getPackageName();
            final Uri uriObj = Uri.parse(UriString);
            intent.setAction(ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS_);
            intent.setData(uriObj);
        }
        context_.startActivity(intent);
        return true;
    }

    /**
     * Raises "ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS" intent. The application context will be used to raise the intent.
     * @return true if succeed; otherwise false
     */
    public static boolean ignoringBatteryOptimizations() {
        return NgnApplication.ignoringBatteryOptimizations(null);
    }

    public static boolean acquirePowerLock(){
    	if(sPowerManagerLock == null){
    		final PowerManager powerManager = getPowerManager();
    		if(powerManager == null){
    			Log.e(TAG, "Null Power manager from the system");
    			return false;
    		}
    		
			if((sPowerManagerLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG)) == null){
				Log.e(TAG, "Null Power manager lock from the system");
				return false;
			}
			sPowerManagerLock.setReferenceCounted(false);
    	}
    	
    	synchronized(sPowerManagerLock){
	    	if(!sPowerManagerLock.isHeld()){
	    		Log.d(TAG,"acquirePowerLock()");
				sPowerManagerLock.acquire();	
			}
    	}
    	return true;
    }
    
    public static boolean releasePowerLock(){
    	if(sPowerManagerLock != null){
    		synchronized(sPowerManagerLock){
    	    	if(sPowerManagerLock.isHeld()){
    	    		Log.d(TAG,"releasePowerLock()");
    				sPowerManagerLock.release();
    			}
        	}
    	}
    	return true;
    }
}
