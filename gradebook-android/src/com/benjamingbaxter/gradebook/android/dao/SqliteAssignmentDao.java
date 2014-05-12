package com.benjamingbaxter.gradebook.android.dao;


import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.benjamingbaxter.gradebook.dao.AssignmentDao;
import com.benjamingbaxter.gradebook.dao.AssignmentTypeDao;
import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.dao.GradedAssignmentDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.Assignment;
import com.benjamingbaxter.gradebook.model.Course;
import com.benjamingbaxter.gradebook.model.GradedAssignment;
import com.benjamingbaxter.gradebook.model.Student;

public class SqliteAssignmentDao extends AbstractSqliteRepository<Assignment> 
	implements AssignmentDao {
	
    private static final String[] ALL_COLUMN_NAMES = new String[] {
    	GradebookContract.Assignment._ID,
    	GradebookContract.Assignment.COLUMN_NAME_UUID,
    	GradebookContract.Assignment.COLUMN_NAME_CREATED,
    	GradebookContract.Assignment.COLUMN_NAME_UPDATED,
    	GradebookContract.Assignment.COLUMN_NAME_DELETED,
    	GradebookContract.Assignment.COLUMN_NAME_TITLE,
    	GradebookContract.Assignment.COLUMN_NAME_POSSIBLE_POINTS,
    	GradebookContract.Assignment.COLUMN_NAME_COURSE_ID,
    	GradebookContract.Assignment.COLUMN_NAME_STUDENT_ID,
    	GradebookContract.Assignment.COLUMN_NAME_ASSIGNMENT_TYPE_ID
    };

    private CourseDao courseDao;
    private AssignmentTypeDao assignmentTypeDao;
    private GradedAssignmentDao gradedAssignmentDao;
    
	public SqliteAssignmentDao(GradebookDbHelper dbHelper) {
		super(dbHelper);
		courseDao = new SqliteCourseDao(dbHelper);
		assignmentTypeDao = new SqliteAssignmentTypeDao(dbHelper);
		gradedAssignmentDao = new SqliteGradedAssignmentDao(dbHelper, this);
	}

	@Override
	protected String getTableName() {
		return GradebookContract.Assignment.TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return ALL_COLUMN_NAMES;
	}

	@Override
	protected Assignment readObject(Cursor cursor) {
		int index = 0;
		
		long id = cursor.getLong(index++);
		String uuid = cursor.getString(index++);
		Date created = new Date(cursor.getLong(index++));
		
		Assignment assignment= new Assignment(id, uuid, created);
		
		assignment.setUpdateDate(new Date(cursor.getLong(index++)));
		assignment.setDeleted(intColumnToBoolean(cursor.getInt(index++)));
		
		assignment.setTitle(cursor.getString(index++));
		assignment.setPossiblePoints(cursor.getDouble(index++));
		
		Course course = courseDao.findById(cursor.getLong(index++));
		assignment.setCourse(course);
		
		assignment.setType(assignmentTypeDao.findById(cursor.getLong(index++)));
		
		return assignment;
	}

	@Override
	protected ContentValues getContentValues(Assignment object) {
		ContentValues values = new ContentValues();

		values.put(GradebookContract.Assignment.COLUMN_NAME_UUID, object.getUuid());
		values.put(GradebookContract.Assignment.COLUMN_NAME_CREATED, object.getCreationDate().getTime());
		values.put(GradebookContract.Assignment.COLUMN_NAME_UPDATED, object.getUpdateDate().getTime());
		values.put(GradebookContract.Assignment.COLUMN_NAME_DELETED, booleanToIntColumn(object.isDeleted()));

    	values.put(GradebookContract.Assignment.COLUMN_NAME_TITLE, object.getTitle());
    	values.put(GradebookContract.Assignment.COLUMN_NAME_POSSIBLE_POINTS, object.getPossiblePoints());
		
		
		values.put(GradebookContract.Assignment.COLUMN_NAME_COURSE_ID, object.getCourse().getId());
		values.put(GradebookContract.Assignment.COLUMN_NAME_ASSIGNMENT_TYPE_ID, object.getType().getId());
		
		return values;
	}

	@Override
	protected long createFilledInObject(Assignment object) {
		long id = super.createFilledInObject(object);
		object.setId(id);
		for(Student student : object.getStudents()) {
			GradedAssignment grade = new GradedAssignment();
			grade.setAssignment(object);
			grade.setCourse(object.getCourse());
			grade.setStudent(student);
			gradedAssignmentDao.create(grade);
		}
		
		return id;
	}
	
	@Override
	public Query<Assignment> findByCourseId(Long id) {
		return find(GradebookContract.Assignment.COLUMN_NAME_COURSE_ID + " = ?", 
				new String[]{ String.valueOf(id) });
	}

	@Override
	public Query<Assignment> findByCourseIdAndAssignmentTypeId(Long courseId, Long assId) {
		return find(GradebookContract.Assignment.COLUMN_NAME_COURSE_ID + " = ? and "
				+ GradebookContract.Assignment.COLUMN_NAME_ASSIGNMENT_TYPE_ID + " = ?", 
				new String[]{ String.valueOf(courseId), String.valueOf(assId) });
	}
	
	@Override
	public void deleteByCourseId(Long id) {
		mDbHelper.getWritableDatabase().delete(getTableName(), 
				GradebookContract.Assignment.COLUMN_NAME_COURSE_ID + " = ?", 
				new String[] { String.valueOf(id) });
	}

	@Override
	public Query<Assignment> findByDisplayCriteria(String searchText) {
		return find(GradebookContract.Assignment.COLUMN_NAME_TITLE + " like '?' ",
				new String[]{ "%" + searchText + "%"});
	}
	
}
