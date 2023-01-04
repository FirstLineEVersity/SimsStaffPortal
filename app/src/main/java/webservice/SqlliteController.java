package webservice;

/**
 * Created by Firstline Infotech on 03-05-2019.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.StringTokenizer;

public class SqlliteController  extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public SqlliteController(Context applicationcontext){
        super(applicationcontext, "androidsqlitestaffportal.db", null, 1);
        Log.d(LOGCAT,"Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database){
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
//        String query;
//        query = "DROP TABLE IF EXISTS Students";
//        database.execSQL(query);
//        onCreate(database);
    }

    public void deleteLoginStaffDetails(){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM stafflogindetails";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
            deleteQuery = "DELETE FROM userwisemenuaccessrights";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }
    public void deletePaySlipPayPeriod(long lngEmployeeId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM payslippayperiod WHERE employeeid="+lngEmployeeId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);

            deleteQuery = "DROP TABLE payslippayperiod";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertPaySlipPayPeriod(long lngEmployeeId,int intOfficeId, int intPayStructureId,
                                       int intPayperiodId,String strPayPeriod,String strPayPeriodPDFFilename){
        SQLiteDatabase database = this.getWritableDatabase();
//        String query = "DROP TABLE payslippayperiod";
//        database.execSQL(query);
        String query= "CREATE TABLE IF NOT EXISTS payslippayperiod (employeeid INTEGER," +
                "officeid integer," +
                "paystructureid integer," +
                "payperiodid integer," +
                "payperioddesc VARCHAR(50)," +
                "payslipfilename VARCHAR(150),"+
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime'))," +
                "lastloggedin DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("officeid",intOfficeId);
        values.put("paystructureid",intPayStructureId);
        values.put("payperiodid",intPayperiodId);
        values.put("payperioddesc",strPayPeriod);
        values.put("payslipfilename",strPayPeriodPDFFilename);
        database.insert("payslippayperiod", null, values);
        database.close();
    }

    public void deleteNotificationDetails(long lngEmployeeId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM notificationdetails where employeeid = " + lngEmployeeId;
            Log.d("query", deleteQuery);
            String query= "DROP TABLE notificationdetails ";
            database.execSQL(query);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertNotificationDetails(long lngEmployeeId,String strNotificationDate,String strReceviedFrom,String strNotificationTime,String strNotificationTitle,
                                          String strNotificationMessage){
        SQLiteDatabase database = this.getWritableDatabase();
        //String query= "DROP TABLE notificationdetails ";
        //database.execSQL(query);
        String query= "CREATE TABLE IF NOT EXISTS notificationdetails (employeeid INTEGER, " +
                "receivedfrom VARCHAR(50)," +
                "notificationtitle VARCHAR(50)," +
                "notificationmessage TEXT," +
                "notificationdate DATE," +
                "notificationtime TIME," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("receivedfrom",strReceviedFrom);
        values.put("notificationtitle",strNotificationTitle);
        values.put("notificationmessage",strNotificationMessage);
        values.put("notificationdate",strNotificationDate);
        values.put("notificationtime",strNotificationTime);
        Log.d("insert query",String.valueOf(values));
        database.insert("notificationdetails", null, values);
        database.close();
    }

    public void insertStaffList(int intEmpCategoryId,String strEmpCategoryDesc,String strEmployeeCode,String strEmployeeName,long lngEmployeeId){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS stafflist (employeecategoryid INTEGER, " +
                "employeecategory VARCHAR(50)," +
                "employeecode VARCHAR(10),"+
                "employeename VARCHAR(75),"+
                "employeeid INTEGER,"+
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("employeecategoryid",intEmpCategoryId);
        values.put("employeecategory",strEmpCategoryDesc);
        values.put("employeecode",strEmployeeCode);
        values.put("employeename",strEmployeeName);
        // Log.d("insert query",String.valueOf(values));
        database.insert("stafflist", null, values);
        database.close();
    }

    public void deleteStaffList(){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM stafflist";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
            deleteQuery = "DROP TABLE stafflist";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertStaffSubjects(long lngEmployeeId,String strProgramSection,String strSubjectCode,
                                    String strSubjectDesc,long lngProgSecId,long lngSubjectId){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS staffsubjetcs (employeeid INTEGER, " +
                "programsection TEXT," +
                "subjectcode VARCHAR(30)," +
                "subjectdesc TEXT," +
                "programsectionid INTEGER," +
                "subjectid INTEGER," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("programsection",strProgramSection);
        values.put("subjectcode",strSubjectCode);
        values.put("subjectdesc",strSubjectDesc);
        values.put("programsectionid",lngProgSecId);
        values.put("subjectid",lngSubjectId);
        Log.d("insert query",String.valueOf(values));
        database.insert("staffsubjetcs", null, values);
        database.close();
    }

    public void deleteStaffSubjects(long lngEmployeeId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM staffsubjetcs where employeeid = " + lngEmployeeId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void cancelLeaveApplication(long lngLeaveApplnId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE * FROM leavestatus WHERE leaveapplicationid = " + lngLeaveApplnId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertLoginStaffDetails(long lngEmployeeId,String strStaffName,String strDepartment,String strDesignation,String strMenuIds){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS stafflogindetails (employeeid INTEGER," +
                "employeename VARCHAR(75)," +
                "department VARCHAR(30)," +
                "designation VARCHAR(100)," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime'))," +
                "lastloggedin DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        //query = "DROP TABLE userwisemenuaccessrights";
        //database.execSQL(query);
        query= "CREATE TABLE IF NOT EXISTS userwisemenuaccessrights (employeeid INTEGER," +
                "menuid INTEGER," +
                "menuname VARCHAR(50)," +
                "menusortnumber INTEGER," +
                "iconname VARCHAR(100)," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);

        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("employeename",strStaffName);
        values.put("department",strDepartment);
        values.put("designation",strDesignation);
        database.insert("stafflogindetails", null, values);
        values = new ContentValues();
        StringTokenizer strMenuId = new StringTokenizer(strMenuIds,",");
        long lngMenuId=0;
        while (strMenuId.hasMoreTokens()){
            StringTokenizer strMenuIdInner = new StringTokenizer(strMenuId.nextToken().trim(),"##");
            values.put("employeeid",lngEmployeeId);
            lngMenuId=Long.parseLong(strMenuIdInner.nextToken().trim());
            values.put("menuid",lngMenuId);
            values.put("menuname",strMenuIdInner.nextToken().trim());
            if (lngMenuId == 1){
                values.put("iconname", "R.drawable.icon_profile");
            }else if (lngMenuId == 2){
                values.put("iconname", "R.drawable.icon_timetable");
            } else if (lngMenuId == 3){
                values.put("iconname", "R.drawable.icon_leavestatus");
            }else if (lngMenuId == 4){
                values.put("iconname", "R.drawable.icon_leaveentry");
            }else if (lngMenuId == 5){
                values.put("iconname", "R.drawable.icon_leaveapproval");
            }else if (lngMenuId == 6){
                values.put("iconname", "R.drawable.icon_studentattendance");
            }else if (lngMenuId == 7){
                values.put("iconname", "R.drawable.icon_markentry");
            }else if (lngMenuId == 8){
                values.put("iconname", "R.drawable.icon_biometriclog");
            }else if (lngMenuId == 9){
                values.put("iconname", "R.drawable.icon_payslip");
            }else if (lngMenuId == 10){
                values.put("iconname", "R.drawable.icon_notification");
            }else if (lngMenuId == 50){
                values.put("iconname", "R.drawable.icon_logout");
            }
            values.put("menusortnumber",lngMenuId);
            database.insert("userwisemenuaccessrights", null, values);
        }
        database.close();
    }

    public void insertStaffPhoto(long lngStaffId,byte[] photo){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS staffphoto (staffid INTEGER PRIMARY KEY, staffphoto blob,"+
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        try {
            String deleteQuery = "DELETE FROM staffphoto where staffid = " + lngStaffId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch (Exception e){}
        ContentValues values = new ContentValues();
        values.put("staffid",lngStaffId);
        values.put("staffphoto",photo);
        database.insert("staffphoto", null, values);
        database.close();
    }

    public void insertQuestionAnswer(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS dailyQuestioner (employeeid INTEGER," +
                "employeename VARCHAR(75)," +
                "department VARCHAR(30)," +
                "designation VARCHAR(100)," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime'))," +
                "lastloggedin DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        //query = "DROP TABLE userwisemenuaccessrights";
        //database.execSQL(query);
        query= "CREATE TABLE IF NOT EXISTS userwisemenuaccessrights (employeeid INTEGER," +
                "menuid INTEGER," +
                "menuname VARCHAR(50)," +
                "menusortnumber INTEGER," +
                "iconname VARCHAR(100)," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);

        database.close();
    }

    public void insertProfileDetails(long lngEmployeeId,
                                     String strEmployeeName,String strDivision,
                                     String strDesignation,String strDob,String strDoj,
                                     String strMobile,String strEmail,String strQualification,
                                     String strAddress){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String dropQuery = "DROP TABLE profiledetails";
            Log.d("query", dropQuery);
            database.execSQL(dropQuery);
        }catch (Exception e){}
        String query= "CREATE TABLE IF NOT EXISTS profiledetails (employeeid INTEGER PRIMARY KEY, " +
                "employeename VARCHAR(100)," +
                "division VARCHAR(100)," +
                "designation VARCHAR(100)," +
                "dob VARCHAR(20)," +
                "doj VARCHAR(20)," +
                "mobile VARCHAR(10)," +
                "email VARCHAR(120)," +
                "address TEXT," +
                "qualification VARCHAR(100)," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        Log.d("query", query);
        database.execSQL(query);
        try {
            String deleteQuery = "DELETE FROM profiledetails where employeeid=" + lngEmployeeId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch (Exception e){}
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("employeename",strEmployeeName);
        values.put("division",strDivision);
        values.put("designation",strDesignation);
        values.put("qualification",strQualification);
        values.put("dob",strDob);
        values.put("doj",strDoj);
        values.put("mobile",strMobile);
        values.put("email",strEmail);
        values.put("address",strAddress);
        database.insert("profiledetails", null, values);
        database.close();
    }

    public void deleteDailyActivityQuestioner(){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM dailyactivityquestioner";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertDailyActivityQuestioner(int intSlNo, long lngQuestionId,String strQuestionDesc,String strAnswerDesc,String strAnswerId,
                                 String strSubAnswerDesc,String strSubAnswerId){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS dailyactivityquestioner (slno integer," +
                "questionid INTEGER, " +
                "questiondesc text," +
                "answerdesc VARCHAR(300)," +
                "answerid VARCHAR(300)," +
                "subanswerdesc VARCHAR(300)," +
                "subanswerid VARCHAR(300)," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("slno",intSlNo);
        values.put("questionid",lngQuestionId);
        values.put("questiondesc",strQuestionDesc);
        values.put("answerdesc",strAnswerDesc);
        values.put("answerid",strAnswerId);
        values.put("subanswerdesc",strSubAnswerDesc);
        values.put("subanswerid",strSubAnswerId);

        Log.d("insert query",String.valueOf(values));
        database.insert("dailyactivityquestioner", null, values);
        database.close();
    }

    public void insertDailyActivity(long lngQuestionId,int intAnswerId,int intSubAnswerId,String strInputAnswer){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS dailyactivityfeedback (questionid INTEGER, " +
                "answerid INTEGER," +
                "subanswerid INTEGER," +
                "inputanswer text)";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("questionid",lngQuestionId);
        values.put("answerid",intAnswerId);
        values.put("subanswerid",intSubAnswerId);
        values.put("inputanswer",strInputAnswer);
        Log.d("insert query",String.valueOf(values));
        database.insert("dailyactivityfeedback", null, values);
        database.close();
    }



}



