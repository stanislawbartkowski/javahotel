unit UnitNoWait;

interface

uses Data.DB,
  Data.SqlExpr,
  Data.DBXCommon, DBAccess;

type
  TestNoWait = class
  public
  constructor Create(paramT: DatabaseType);
  procedure runTest;
  strict private
    DBTest: DBConnect;
    procedure runQueryNoWait;
    procedure runLockNoWait;
  end;

implementation


  Constructor TestNoWait.Create(paramT: DatabaseType);
  begin
    DBTest := DBConnect.Create(paramT);
  end;

  procedure TestNoWait.runQueryNoWait;
  var
    Q: TSQLQuery;
  begin
    Q := DBTest.getQ('SELECT * FROM TESTL FOR UPDATE');
    DBTest.executeSQLNoWait(Q,true);
    Q.Close;
    Q := DBTest.getQ('SELECT * FROM TESTL FOR UPDATE');
    Q.ExecSQL();
    Q.Open;
    Q.Close;
  end;

 procedure TestNoWait.runLockNoWait;
  var
    Q: TSQLQuery;
  begin
    Q := DBTest.getQ('LOCK TABLE TESTL IN SHARE MODE');
    DBTest.executeSQLNoWait(Q, false);
    Q.Close;
    { In DB2 we have to issue COMMIT explicitly to remove lock on table }
    Q := DBTest.getQ('COMMIT');
    Q.ExecSQL;
    Q := DBTest.getQ('LOCK TABLE TESTL IN SHARE MODE');
    Q.ExecSQL();
    Q.Close;
  end;


  procedure TestNoWait.runTest;
  begin
    DBTest.connect;
{    runQueryNoWait;  }
    runLockNoWait;

    DBTest.disconnect;
  end;

end.
