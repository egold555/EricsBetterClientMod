package org.golde.forge.betterminecraft;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.client.gui.GuiControls;

/**
 * Simple reflection class
 * @author Eric
 *
 */
public class Reflection {

	/**
	 * 
	 * @param thiss Just pass in this
	 * @param classToModify something.class that has the field to modify in it
	 * @param field String of field name
	 * @return Object to be cast to what field is
	 */
	public static Object getField(Object thiss, Class<?> classToModify, String field) {
		try {
			Field f = classToModify.getDeclaredField(field);
			f.setAccessible(true);
			Field modifiersField = f.getClass().getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
			return f.get(thiss);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	/**
	 * 
	 * @param thiss Just pass in this
	 * @param classToModify something.class that has the field to modify in it
	 * @param field String of field name
	 * @param to What to set value too
	 */
	public static void setField(Object thiss, Class<?> classToModify, String field, Object to) {
		try {
			Field f = classToModify.getDeclaredField(field);
			f.setAccessible(true);
			Field modifiersField = f.getClass().getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
			f.set(thiss, to);
			f.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
