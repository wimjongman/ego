/**
 * 
 */
package com.weltevree.ego.model;

import java.util.EventListener;
import java.util.EventObject;


/**
 * @author Weltevree jongw
 * @copyright Weltevree Beheer BV 2005
 *
 */
public interface IGoEventListener extends EventListener {
	
	
	/**
	 * Handles an event that was fired from <code>IGoEventProvider</code>.
	 * 
	 * 
	 * @param int Event as declared in IGoEventProvider
	 * @param EventObject the object on which the event occured.
	 * @see IGoEventProvider
	 */
	public void eventFired(final int event, EventObject object);

}
