package calendear.action;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.*;

import org.junit.Test;

import calendear.util.*;

public class ActionTest {
	
	/**[0:name][1:type][2:starttime]
	[3:endtime][4:location][5:note]
	[6:tag][7:important][8:finished]
*/


	@Test
	public void testUpdate() throws ParseException{
		Action action1 = new Action("action1.txt");
		int nextIndex = action1.getAmount();
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		GregorianCalendar newTime = new GregorianCalendar(2, 1, 2001);
		String newName = "newName";
		GregorianCalendar newST = new GregorianCalendar(3, 3, 2003);
		String newTag = "newTag";
		//System.out.println(nextIndex);
		boolean[] chklst = {true, false, true, true, false, false, false, false, false};
		Object[] objArr = {newName, null, null, (Object) newTime, null, null, null, null};
		CommandUpdate cU = new CommandUpdate(nextIndex, chklst, objArr);
		action1.exeUpdate(cU);
		assertEquals(t1.getEndTime(), newTime);
	}
	
	@Test
	public void testUndo() throws ParseException{
		Action action1 = new Action("action2.txt");
		int nextIndex = action1.getAmount();
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		GregorianCalendar newTime = new GregorianCalendar(2, 1, 2001);
		//System.out.println(nextIndex);
		boolean[] chklst = {false, false, false, true, false, false, false, false, false};
		Object[] objArr = {null, null, null, (Object) newTime, null, null, null, null};
		CommandUpdate cU = new CommandUpdate(nextIndex, chklst, objArr);
		action1.exeUpdate(cU);
		assertEquals(t1.getEndTime(), newTime);
		
		action1.exeUndo();
		assertEquals(t1.getEndTime(), originalTime);
	}
	
	@Test
	public void testUndoDelete() throws ParseException{
		Action action1 = new Action("action2.txt");
		int nextIndex = action1.getAmount();
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		int nextNextIndex = action1.getAmount(); 
		System.out.println(nextIndex + " ");
		
		action1.exeUndo();
		assertEquals(t1.getEndTime(), originalTime);
	}

	@Test
	public void testRedo() throws ParseException{
		Action action1 = new Action("action3.txt");
		int nextIndex = action1.getAmount();
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		GregorianCalendar newTime = new GregorianCalendar(2, 1, 2001);
		//System.out.println(nextIndex);
		boolean[] chklst = {false, false, false, true, false, false, false, false, false};
		Object[] objArr = {null, null, null, (Object) newTime, null, null, null, null};
		CommandUpdate cU = new CommandUpdate(nextIndex, chklst, objArr);
		action1.exeUpdate(cU);
		assertEquals(t1.getEndTime(), newTime);
		
		action1.exeUndo();
		assertEquals(t1.getEndTime(), originalTime);
		action1.exeRedo();
		assertEquals(t1.getEndTime(), newTime);
	}
}
