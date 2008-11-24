package com.weltevree.ego.model;


/**
 * Classes that must provide an eventmodel can use this interface. Listeners can
 * implement IGoEventListener.
 * 
 * @author Weltevree jongw
 * @copyright Weltevree Beheer BV 2005
 * @see IGoEventListener
 * 
 */
public interface IGoEventProvider {
	/**
	 * Add event
	 */
	public static final int EVENT_ADD = 1;

	/**
	 * Change event
	 */
	public static final int EVENT_CHANGE = 2;

	/**
	 * Delete Event
	 */
	public static final int EVENT_DELETE = 3;

	/**
	 * Delete Event
	 */
	public static final int EVENT_EXPAND = 4;
	
	/**
	 * Other event
	 */
	public static final int EVENT_OTHER = 255;

	/**
	 * Adds a listener.
	 * 
	 * @param IGoEventListener as listener
	 */
	public void addEventListener(IGoEventListener listener);
	
	/**
	 * Fires an event of one of the event types.
	 * 
	 * @param event
	 */
	public void fireEvent(final int event);

}
