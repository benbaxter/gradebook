package com.benjamingbaxter.gradebook.android.dao;


import java.util.Collections;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.benjamingbaxter.gradebook.dao.AssignmentDao;
import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.dao.GradedAssignmentDao;
import com.benjamingbaxter.gradebook.dao.ListQuery;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.dao.StudentDao;
import com.benjamingbaxter.gradebook.model.GradedAssignment;

public class SqliteGradedAssignmentDao extends AbstractSqliteRepository<GradedAssignment> 
	implements GradedAssignmentDao {
	
    private static final String[] ALL_COLUMN_NAMES = new String[] {
    	GradebookContract.GradedAssignment._ID,
    	GradebookContract.GradedAssignment.COLUMN_NAME_UUID,
    	GradebookContract.GradedAssignment.COLUMN_NAME_CREATED,
    	GradebookContract.GradedAssignment.COLUMN_NAME_UPDATED,
    	GradebookContract.GradedAssignment.COLUMN_NAME_DELETED,
    	GradebookContract.GradedAssignment.COLUMN_NAME_EARNED_POINTS,
    	GradebookContract.GradedAssignment.COLUMN_NAME_FEEDBACK,
    	GradebookContract.GradedAssignment.COLUMN_NAME_COURSE_ID,
    	GradebookContract.GradedAssignment.COLUMN_NAME_STUDENT_ID,
    	GradebookContract.GradedAssignment.COLUMN_NAME_ASSIGNMENT_ID
    };

    private CourseDao courseDao;
    private StudentDao studentDao;
    private AssignmentDao assignmentDao;
    
	public SqliteGradedAssignmentDao(GradebookDbHelper dbHelper, AssignmentDao assignmentDao) {
		super(dbHelper);
		courseDao = new SqliteCourseDao(dbHelper);
		studentDao = new SqliteStudentDao(dbHelper);
		this.assignmentDao = assignmentDao;
	}

	@Override
	protected String getTableName() {
		return GradebookContract.GradedAssignment.TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return ALL_COLUMN_NAMES;
	}

	@Override
	protected GradedAssignment readObject(Cursor cursor) {
		int index = 0;
		
		long id = cursor.getLong(index++);
		String uuid = cursor.getString(index++);
		Date created = new Date(cursor.getLong(index++));
		
		GradedAssignment assignment= new GradedAssignment(id, uuid, created);
		
		assignment.setUpdateDate(new Date(cursor.getLong(index++)));
		assignment.setDeleted(intColumnToBoolean(cursor.getInt(index++)));
		
		assignment.setEarnedPoints(cursor.getDouble(index++));
		assignment.setFeedback(cursor.getString(index++));
		
		assignment.setCourse(courseDao.findById(cursor.getLong(index++)));
		
		assignment.setStudent(studentDao.findById(cursor.getLong(index++)));
		
		assignment.setAssignment(assignmentDao.findById(cursor.getLong(index++)));
		
		return assignment;
	}

	@Override
	protected ContentValues getContentValues(GradedAssignment object) {
		ContentValues values = new ContentValues();

		values.put(GradebookContract.GradedAssignment.COLUMN_NAME_UUID, object.getUuid());
		values.put(GradebookContract.GradedAssignment.COLUMN_NAME_CREATED, object.getCreationDate().getTime());
		values.put(GradebookContract.GradedAssignment.COLUMN_NAME_UPDATED, object.getUpdateDate().getTime());
		values.put(GradebookContract.GradedAssignment.COLUMN_NAME_DELETED, booleanToIntColumn(object.isDeleted()));

    	values.put(GradebookContract.GradedAssignment.COLUMN_NAME_EARNED_POINTS, object.getEarnedPoints());
		values.put(GradebookContract.GradedAssignment.COLUMN_NAME_FEEDBACK, object.getFeedback());
		
		values.put(GradebookContract.GradedAssignment.COLUMN_NAME_COURSE_ID, object.getCourse().getId());
		values.put(GradebookContract.GradedAssignment.COLUMN_NAME_STUDENT_ID, object.getStudent().getId());
		values.put(GradebookContract.GradedAssignment.COLUMN_NAME_ASSIGNMENT_ID, object.getAssignment().getId());
		
		return values;
	}
	
	@Override
	public Query<GradedAssignment> findByCourseIdAndAssignmentIdAndStudentId
					(Long courseId, Long assId, Long studentId) {
		return find(GradebookContract.GradedAssignment.COLUMN_NAME_COURSE_ID + " = ? and "
				+ GradebookContract.GradedAssignment.COLUMN_NAME_ASSIGNMENT_ID + " = ? and "
				+ GradebookContract.GradedAssignment.COLUMN_NAME_STUDENT_ID + " = ? ",
				new String[]{ String.valueOf(courseId), String.valueOf(assId), String.valueOf(studentId) });
	}
	
	@Override
	public Query<GradedAssignment> findByDisplayCriteria(String searchText) {
		return new ListQuery<GradedAssignment>(Collections.EMPTY_LIST);
	}
	
}
