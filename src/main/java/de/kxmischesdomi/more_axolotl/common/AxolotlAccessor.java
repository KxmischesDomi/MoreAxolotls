package de.kxmischesdomi.more_axolotl.common;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface AxolotlAccessor {

	int getVariantId();

	void setLeaf(boolean leaf);
	boolean hasLeaf();

	float getLeafDurability();
	void setLeafDurability(float damage);

	int getMouthOpenTicks();
	void setMouthOpenTicks(int ticks);

	int getGuiAge();
	void setGuiAge(int age);


}
