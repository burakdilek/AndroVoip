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
package org.doubango.ngn.events;

/**
 * List of all supported types associated to SIP INVITE event arguments
 */
public enum NgnInviteEventTypes {
	INCOMING,
    INPROGRESS,
    RINGING,
    EARLY_MEDIA,
    CONNECTED,
    TERMWAIT,
    TERMINATED,
    LOCAL_HOLD_OK,
    LOCAL_HOLD_NOK,
    LOCAL_RESUME_OK,
    LOCAL_RESUME_NOK,
    REMOTE_HOLD,
    REMOTE_RESUME,
    MEDIA_UPDATING,
    MEDIA_UPDATED,
    SIP_RESPONSE,
    REMOTE_DEVICE_INFO_CHANGED,
    LOCAL_TRANSFER_TRYING,
    LOCAL_TRANSFER_ACCEPTED,
    LOCAL_TRANSFER_COMPLETED,
    LOCAL_TRANSFER_FAILED,
    LOCAL_TRANSFER_NOTIFY,
    REMOTE_TRANSFER_REQUESTED,
    REMOTE_TRANSFER_NOTIFY,
    REMOTE_TRANSFER_INPROGESS,
    REMOTE_TRANSFER_FAILED,
    REMOTE_TRANSFER_COMPLETED
}
