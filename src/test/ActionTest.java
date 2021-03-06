package test;
//@@author A0129998B
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import calendear.action.*;
import org.junit.Before;
import org.junit.Test;

import calendear.util.*;

public class ActionTest {
	
	/**[0:name][1:type][2:start time]
	[3:end time][4:location][5:note]
	[6:tag][7:important][8:finished]
	 * @throws IOException 
*/
	String originalName;
	TASK_TYPE originalType;
	GregorianCalendar originalStartTime;
	GregorianCalendar originalEndTime;
	String originalLocation;
	String originalNote;
	String originalTag;
	boolean originalImportance;
	boolean originalDone;
	
	String finalName;
	TASK_TYPE finalType;
	GregorianCalendar finalStartTime;
	GregorianCalendar finalEndTime;
	String finalLocation;
	String finalNote;
	String finalTag;
	boolean finalImportance;
	boolean finalDone;
	
	boolean[] chklst;
	Object[] info;
	
	@Before
	public void init(){
		originalName = "task1";
		originalType = TASK_TYPE.DEADLINE;
		originalStartTime = null;
		originalEndTime = new GregorianCalendar( 2016,4,2,15,0);
		originalLocation = "";
		originalNote = "";
		originalTag = "";
		originalImportance = false;
		originalDone = false;
		
		finalName = "finalName";
		finalType = TASK_TYPE.EVENT;
		finalStartTime = new GregorianCalendar(2016, 5, 2, 13, 0);
		finalEndTime = new GregorianCalendar(2016, 5, 2, 15, 0);
		finalLocation = "loc1";
		finalNote = "note1";
		finalTag = "tag1";
		finalImportance = true;
		finalDone = true;
	}

	@Test
	public void testUpdate() throws ParseException, LogicException, IOException{

		Action action1 = new Action("action1.txt");
		action1.exeClear(new CommandClear());
		int nextIndex = action1.getAmount();
		
		Task task = new Task();
		task.setName(originalName);
		task.setType(originalType);
		task.setStartTime(originalStartTime);
		task.setEndTime(originalEndTime);
		task.setTag(originalTag);
		task.setIsImportant(originalImportance);
		task.setIsFinished(originalDone);
		
		action1.exeAdd(new CommandAdd(task));
		boolean[] chklst = {true, true, true, true, true, true, true, true, true};
		Object[] info = {finalName, finalType, finalStartTime, finalEndTime, finalLocation, finalNote, 
				finalTag, finalImportance, finalDone};
		CommandUpdate commandUpdate = new CommandUpdate(nextIndex, chklst, info);
		action1.exeUpdate(commandUpdate);
		
		assertEquals(finalName, task.getName() );
		assertEquals(finalType,task.getType() );
		assertEquals(finalStartTime,task.getStartTime() );
		assertEquals(finalEndTime,task.getEndTime());
		assertEquals(finalTag, task.getTag());
		assertEquals(finalImportance,task.isImportant());
		assertEquals(finalDone, task.isFinished() );
		
		action1.exeClear(new CommandClear());
	}
	


	@Test
	public void testUndoDelete() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action2.txt");
		action1.exeClear(new CommandClear());
		GregorianCalendar originalTime = new GregorianCalendar(01, 01, 2001);
		Task t1 = new Task("task2", originalTime);
		CommandAdd cA = new CommandAdd(t1);
		action1.exeAdd(cA);
		
		action1.exeUndo();
		assertEquals(originalTime, t1.getEndTime());
	}

	

	@Test
	public void testSearchName() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action4.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("search test1 aaaa");
		Task test2 = new Task("search Test2 bbbb");
		Task test3 = new Task("search test3 cccc");
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {true, false, false, false, false, false, false, false, false};
		Object[] searchWith = {"aaa", null, null, null, null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith );
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(null);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);

		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}


	//@@author A0107350U

	@Test
	public void testSearchStartTime() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action5.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("search startTime1", 
				new GregorianCalendar( 2016,4,2,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		Task test2 = new Task("search startTime2", 
				new GregorianCalendar( 2016,4,2,12,0),
				new GregorianCalendar( 2016,4,2,16,0));
		Task test3 = new Task("search startTime3", 
				new GregorianCalendar( 2016,4,1,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, true, false, false, false, false, false, false};
		Object[] searchWith = {null, null, new GregorianCalendar( 2016,4,2,11,0), null, null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(test2);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	

		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
	
	@Test

	public void testSearchEndTime() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action6.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("search startTime1", 
				new GregorianCalendar( 2016,4,2,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		Task test2 = new Task("search startTime2", 
				new GregorianCalendar( 2016,4,2,12,0),
				new GregorianCalendar( 2016,4,2,16,0));
		Task test3 = new Task("search startTime3", 
				new GregorianCalendar( 2016,4,1,13,0),
				new GregorianCalendar( 2016,4,2,15,0));
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, false, true, false, false, false, false, false};
		Object[] searchWith = {null, null, null, new GregorianCalendar( 2016,4,2,15,30), null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(null);
		expectedResult.add(test3);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	

		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
	
	@Test
	public void testSearchStartAndEndTime() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action7.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("search startTime1", 
				new GregorianCalendar( 2016,1,1,13,0),
				new GregorianCalendar( 2016,1,30,15,0));
		Task test2 = new Task("search startTime2", 
				new GregorianCalendar( 2016,1,12,12,0),
				new GregorianCalendar( 2016,1,21,16,0));
		Task test3 = new Task("search startTime3", 
				new GregorianCalendar( 2016,1,15,15,0));
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, true, true, false, false, false, false, false};
		Object[] searchWith = {null, null, new GregorianCalendar( 2016,1,10,11,0), 
				new GregorianCalendar( 2016,1,20,11,0), null, null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(null);
		expectedResult.add(null);
		expectedResult.add(test3);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	

		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
	
	@Test
	public void testSearchTag() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action8.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("tag test1");
		Task test2 = new Task("tag test2");
		Task test3 = new Task("tag test3");
		
		test1.setTag("school");
		test2.setTag("school # social");
		test3.setTag("social");
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, false, false, false, false, true, false, false};
		Object[] searchWith = {null, null, null, null, null, null, "school", null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(test2);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	


		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
	
	@Test
	public void testSearchLocation() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action9.txt");
		action1.exeClear(new CommandClear());
		Task test1 = new Task("location test1");
		Task test2 = new Task("location test2");
		Task test3 = new Task("location test3");
		
		test1.setLocation("school");
		test2.setLocation("home");
		test3.setLocation("shopping mall");
		
		action1.exeAdd(new CommandAdd(test1));
		action1.exeAdd(new CommandAdd(test2));
		action1.exeAdd(new CommandAdd(test3));
		
		boolean[] toShow = {false, false, false, false, true, false, false, false, false};
		Object[] searchWith = {null, null, null, null, "school", null, null, null};
		
		CommandSearch commandSearch = new CommandSearch(toShow, searchWith);
		
		ArrayList<Task> expectedResult = new ArrayList<Task>();
		expectedResult.add(null);
		expectedResult.add(test1);
		expectedResult.add(null);
		expectedResult.add(null);
		
		ArrayList<Task> testResult = action1.exeSearch(commandSearch);	


		assertEquals(expectedResult,testResult);
		
		action1.exeClear(new CommandClear());
	}
    
	
	@Test
	public void testUndoUpdate() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action10.txt");
		action1.exeClear(new CommandClear());
		int nextIndex = action1.getAmount();
		
		Task task = new Task();
		task.setName(originalName);
		task.setType(originalType);
		task.setStartTime(originalStartTime);
		task.setEndTime(originalEndTime);
		task.setTag(originalTag);
		task.setIsImportant(originalImportance);
		task.setIsFinished(originalDone);
		
		action1.exeAdd(new CommandAdd(task));
		boolean[] chklst = {true, true, true, true, true, true, true, true, true};
		Object[] info = {finalName, finalType, finalStartTime, finalEndTime, finalLocation, finalNote, 
				finalTag, finalImportance, finalDone};
		CommandUpdate commandUpdate = new CommandUpdate(nextIndex, chklst, info);
		action1.exeUpdate(commandUpdate);
		action1.exeUndo();
		
		assertEquals(originalName,task.getName() );
		assertEquals(originalType,task.getType() );
		assertEquals(originalStartTime,task.getStartTime());
		assertEquals(originalEndTime,task.getEndTime());
		assertEquals(originalTag,task.getTag());
		assertEquals(originalImportance,task.isImportant());
		assertEquals(originalDone,task.isFinished());
		
		action1.exeClear(new CommandClear());
		
	}
	
	@Test
	public void testRedoUpdate() throws ParseException, LogicException, IOException{
		Action action1 = new Action("action10.txt");
		action1.exeClear(new CommandClear());
		int nextIndex = action1.getAmount();
		
		Task task = new Task();
		task.setName(originalName);
		task.setType(originalType);
		task.setStartTime(originalStartTime);
		task.setEndTime(originalEndTime);
		task.setTag(originalTag);
		task.setIsImportant(originalImportance);
		task.setIsFinished(originalDone);
		
		action1.exeAdd(new CommandAdd(task));
		boolean[] chklst = {true, true, true, true, true, true, true, true, true};
		Object[] info = {finalName, finalType, finalStartTime, finalEndTime, finalLocation, finalNote, 
				finalTag, finalImportance, finalDone};
		CommandUpdate commandUpdate = new CommandUpdate(nextIndex, chklst, info);
		action1.exeUpdate(commandUpdate);
		action1.exeUndo();
		
		
		assertEquals(originalName,task.getName() );
		assertEquals(originalType,task.getType() );
		assertEquals(originalStartTime,task.getStartTime());
		assertEquals(originalEndTime,task.getEndTime());
		assertEquals(originalTag,task.getTag());
		assertEquals(originalImportance,task.isImportant());
		assertEquals(originalDone,task.isFinished());
		
		action1.exeRedo();
		
		assertEquals(finalName, task.getName() );
		assertEquals(finalType,task.getType() );
		assertEquals(finalStartTime,task.getStartTime() );
		assertEquals(finalEndTime,task.getEndTime());
		assertEquals(finalTag, task.getTag());
		assertEquals(finalImportance,task.isImportant());
		assertEquals(finalDone, task.isFinished() );
		
	}
}
