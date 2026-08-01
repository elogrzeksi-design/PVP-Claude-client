package com.claudeclient.modules;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Licznik kliknięć na sekundę, osobno dla lewego (atak) i prawego (użycie) przycisku myszy.
 * MouseMixin wywołuje registerLeftClick()/registerRightClick() przy każdym kliknięciu.
 */
public class CpsModule extends Module {

	private final Deque<Long> leftClicks = new ArrayDeque<>();
	private final Deque<Long> rightClicks = new ArrayDeque<>();

	public CpsModule() {
		super("CPS", "Liczy kliknięcia myszy na sekundę (LPM/PPM)", true);
	}

	public void registerLeftClick() {
		leftClicks.addLast(System.currentTimeMillis());
	}

	public void registerRightClick() {
		rightClicks.addLast(System.currentTimeMillis());
	}

	public int getLeftCps() {
		return countRecent(leftClicks);
	}

	public int getRightCps() {
		return countRecent(rightClicks);
	}

	private int countRecent(Deque<Long> clicks) {
		long now = System.currentTimeMillis();
		while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000) {
			clicks.pollFirst();
		}
		return clicks.size();
	}
}
