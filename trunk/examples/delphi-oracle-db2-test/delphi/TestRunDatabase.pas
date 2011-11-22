unit TestRunDatabase;
{
  Delphi DUnit Test Case
  ----------------------
}

interface

uses
  TestFramework, DbxOracle, DB, DBXDb2, DBXOdbc, Messages, RunDatabase,
  DBClient, Variants,  System.DateUtils,SqlTimSt,
  FMTBcd, Classes, DBXDynalink, Provider, System.SysUtils, Windows, SQLExpr;

type
  // Test methods for class DBTest

  TestDBTest = class(TTestCase)
  strict private
    FDBTest: DBTest;
  public
    procedure SetUp; override;
    procedure TearDown; override;
  published
    procedure Testconnect;
    procedure Testpackagmessage;
    procedure Testinsertmessage;
    procedure Testnumberpackage;
    procedure Testnumbermethods;
    procedure Testnumberstoredprocedure;
    procedure Testcursor;
    procedure Testfunc;
    procedure Testdate;
  private
    function getNumSP(fName: string): TSQLStoredProc;
    procedure verifyNumberMethod(fName: string; inval: double; outval: double);
    procedure truncateMessages;
    function getCursorSP: TSQLStoredProc;
  end;

implementation

function TestDBTest.getNumSP(fName: string): TSQLStoredProc;
var
  SP: TSQLStoredProc;
begin
  SP := FDBTest.getSP('A_TESTNUM', fName);
  with SP do
  begin
    Params.CreateParam(FDBTest.getBCDType, 'NUMI', TParamType.ptInput);
    Params.CreateParam(FDBTest.getBCDType, 'NUMO', TParamType.ptOutput);
  end;
  result := SP;
end;

procedure TestDBTest.SetUp;
begin
  { FDBTest := DBTest.Create(Oracle); }
  FDBTest := DBTest.Create(DB2);
end;

procedure TestDBTest.TearDown;
begin
  FDBTest.Free;
  FDBTest := nil;
end;

procedure TestDBTest.truncateMessages;
begin
  FDBTest.truncateTable('MESSAGES');
end;

{
  TestCase no 1: Simply connect and disconnect;
  Scenario:
  Step 1: Connect
  Step 2 : Disconnect
  Expected result: No exception thrown
}
procedure TestDBTest.Testconnect;
begin
  FDBTest.connect;
  FDBTest.disconnect;
end;

{
  TestCase no 2: Call package methods with IN and OUT parameters
  Scenario:
  Step 1: Call package method which adds one row to MESSAGE table
  Step 2: Call another package method which returns (OUT) parameters
  number of rows in the MESSAGES table
  Expected result: Number is equal to 1
}

procedure TestDBTest.Testpackagmessage;
begin
  FDBTest.connect;
  truncateMessages;
  { Step 1 }
  FDBTest.addMessageToPackageLog('First Message');
  { Step 2 and verification }
  CheckEquals(1, FDBTest.getNumberOfMessage);
  FDBTest.disconnect;
end;

{
  Test case no 3.
  Test executing A_LOG.ADD_MESSAGE package method.
  Scenario:
  Step 1: add four messages to MESSAGES table
  Expected result: 4 messages added
  Step 2: run SELECT * query on MESSAGES table
  Expected result: columns string value is as exptected
}


procedure TestDBTest.Testinsertmessage;
var
  Q: TSQLQuery;
  i: integer;
  s: String;
begin
  FDBTest.connect;
  truncateMessages;
  { Step 1 }
  FDBTest.addMessageToPackageLog('1 Message');
  FDBTest.addMessageToPackageLog('2 Message');
  FDBTest.addMessageToPackageLog('3 Message');
  FDBTest.addMessageToPackageLog('4 Message');
  { verify }
  CheckEquals(4, FDBTest.getNumberOfMessage);

  { Step 2 }
  Q := FDBTest.getQ('SELECT * FROM MESSAGES');
  Q.ExecSQL();
  CheckEquals(4, Q.RecordCount);
  Q.Open;
  i := 0;
  repeat
    s := Q.FieldByName('MESS').AsString;
    { verify }
    case i of
      0:
        CheckEquals('FROM PACKAGE 1 Message', s);
      1:
        CheckEquals('FROM PACKAGE 2 Message', s);
      2:
        CheckEquals('FROM PACKAGE 3 Message', s);
      3:
        CheckEquals('FROM PACKAGE 4 Message', s);
    end;
    i := i + 1;
    Q.Next;
  until Q.Eof;
  Q.Close;
  FDBTest.disconnect;
end;

{
  Test case no 4.
  Test for NUMBER data type.
  Scenario:
  Step 1: Run A_TESNUM.PREPARE_DATA stored procedure which creates table
  Step 2: execure SELECT * FROM TEST_NUMB
  Expected result:
  Check columns: NUM1 and NUM2 contains integer, NUM3 4 digits after decimal point
}
procedure TestDBTest.Testnumberpackage;
var
  SP: TSQLStoredProc;
  Q: TSQLQuery;
  num1, num2, num3: double;
  i: integer;
  idouble: double;
begin
  FDBTest.connect;
  { Step 1 }
  SP := FDBTest.getSP('A_TESTNUM', 'PREPARE_DATA');
  SP.ExecProc;
  { Step 2}
  Q := FDBTest.getQ('SELECT * FROM TABLE_NUM');
  Q.ExecSQL();
  CheckEquals(100, Q.RecordCount);
  Q.Open;
  i := 1;
  idouble := 1.1234;
  repeat
    { verification }
    num1 := Q.FieldByName('NUM1').AsFloat;
    CheckEquals(i, num1);
    num2 := Q.FieldByName('NUM2').AsFloat;
    CheckEquals(i, num2);
    num3 := Q.FieldByName('NUM3').AsFloat;
    CheckEquals(Round(idouble * 10000), Round(num3 * 10000));
    i := i + 1;
    idouble := idouble + 1;
    Q.Next;
  until Q.Eof;

  FDBTest.disconnect;
end;

procedure TestDBTest.verifyNumberMethod(fName: string; inval: double;
  outval: double);

var
  SP: TSQLStoredProc;
  res: double;
begin
  FDBTest.connect;
  SP := getNumSP(fName);
  with SP do
  begin
    ParamByName('NUMI').AsFloat := inval;
    ExecProc;
    res := ParamByName('NUMO').AsFloat;
    CheckEquals(outval, res);
  end;
  FDBTest.disconnect;
end;

{ Test case no 5.
  Check NUMBER as IN and OUT parameter for method
  Scenario:
  Step 1: run for NUMBER(10,2) parameter
  Step 2: run for NUMBER(10) parameter
  Verification: should contains 0 digits after decimal point
  Step 3: run for NUMBER parameter
}

procedure TestDBTest.Testnumbermethods;
begin
  FDBTest.connect;
  verifyNumberMethod('IO_NUM1', 11.11, 21.11);
  verifyNumberMethod('IO_NUM2', 222.22, 242);
  verifyNumberMethod('IO_NUM3', 333.1234, 363.1234);
  FDBTest.disconnect;
end;

{
  Test case no 6:
  Run stored procedure (outside package) with NUMBER parameter
  Scenario:
  Step 1: run 100 times stored procedure
  Step 2: run query on TABLE_NUM
  Verify: verify if columns contain proper NUMBER value
}
procedure TestDBTest.Testnumberstoredprocedure;
var
  i: integer;
  d: double;
  SP: TSQLStoredProc;
  Q: TSQLQuery;
  num1, num2, num3: double;
begin
  FDBTest.connect;
  FDBTest.truncateTable('TABLE_NUM');
  { Step 1}
  SP := FDBTest.getSP('INSERT_NUMS');
  with SP do
  begin
    Params.CreateParam(TFieldType.ftBCD, 'NUM1', TParamType.ptInput);
    Params.CreateParam(TFieldType.ftBCD, 'NUM2', TParamType.ptInput);
    Params.CreateParam(TFieldType.ftBCD, 'NUM3', TParamType.ptInput);
  end;

  d := 1.1234;
  for i := 0 to 99 do
    with SP do
    begin
      ParamByName('NUM1').AsFloat := d;
      ParamByName('NUM2').AsFloat := d + 10;
      ParamByName('NUM3').AsFloat := d + 20;
      ExecProc;
      d := d + 1;
    end;

  { Step 2 }
  Q := FDBTest.getQ('SELECT * FROM TABLE_NUM');
  Q.ExecSQL();
  CheckEquals(100, Q.RecordCount);
  Q.Open;
  d := 1.1234;
  { Verify }
  repeat
    num1 := Q.FieldByName('NUM1').AsFloat;
    CheckEquals(Round(d * 10000), Round(num1 * 10000));
    num2 := Q.FieldByName('NUM2').AsFloat;
    CheckEquals(Round(d + 10), num2);
    num3 := Q.FieldByName('NUM3').AsFloat;
    CheckEquals(Round((d + 20) * 10000), Round(num3 * 10000));
    d := d + 1;
    Q.Next;
  until Q.Eof;

  FDBTest.disconnect;

end;

function TestDBTest.getCursorSP: TSQLStoredProc;
var
  SP: TSQLStoredProc;
begin
  case FDBTest.getT of
    Oracle:
      begin
        SP := FDBTest.getSP('PROC_CURSOR');
        SP.Params.CreateParam(TFieldType.ftCursor, 'VARRESULT',
          TParamType.ptOutput);
      end;
    db2:
      SP := FDBTest.getSP('DB2_PROC_CURSOR');
  end;
  result := SP;
end;


{
  Test case no 7
  Check SYS_REFCURSOR.
  Scenario:
  Step 1: Run SP returning cursor
  Step 2: iterate through cursor
  Verify: check if columns contains proper values
}
procedure TestDBTest.Testcursor;
var
  SP: TSQLStoredProc;
  i: integer;
  idouble: double;
  num1, num2, num3: double;
begin
  FDBTest.connect;
  { Step 1 }
  SP := getCursorSP;
  with SP do
  begin
    Open;
    { Step 2 }
    i := 1;
    idouble := 1.1234;
    repeat
      { verify }
      num1 := FieldByName('NUM1').AsFloat;
      CheckEquals(i, num1);
      num2 := FieldByName('NUM2').AsFloat;
      CheckEquals(i, num2);
      num3 := FieldByName('NUM3').AsFloat;
      CheckEquals(Round(idouble * 10000), Round(num3 * 10000));
      i := i + 1;
      idouble := idouble + 1;
      Next;
    until Eof;
  end;

  FDBTest.disconnect;
end;

{
  Test case no 8
  Check executing PL/SQL UDF as a part of the query
  Scenario:
  Step 1: Run query
  Verify: proper value is returned
}
procedure TestDBTest.Testfunc;
var   Q: TSQLQuery;
  res : integer;
begin
  FDBTest.connect;
  { important to add ORACLE_FUNC alias, DB2 returns column number }
  Q := FDBTest.getQ('SELECT ORACLE_FUNC AS ORACLE_FUNC FROM DUAL');
  with Q do begin
    Open;
    res := FieldByName('ORACLE_FUNC').AsInteger;
    CheckEquals(123,res);
  end;

  FDBTest.disconnect;
end;

{
  Test case no 9
  Check DATE - it contains also hh:mm:ss
  Scenario:
  Step 1: Insert 10 dates into TABLE_DATE table
  Step 2: Run SELECT * query on the table
  Verification: check if also hh:mm:ss are retrieved
}
procedure TestDBTest.Testdate;
var   Q: TSQLQuery;
  res : integer;
  da,das : TDateTime;
  i : integer;
begin
  FDBTest.connect;
  FDBTest.truncateTable('TABLE_DATE');
  Q := FDBTest.getQ('INSERT INTO TABLE_DATE VALUES(:DD)');
  { Step 1 }
  da :=  System.DateUtils.EncodeDateTime(2011, 11, 22, 12, 32, 15, 0);
  with Q do begin
    for I := 0 to 10 do begin
    Params.ParamByName('DD').AsSQLTimeStamp := DateTimeToSQLTimeStamp(da);
    prepared := true;
    ExecSQL;
    da := da + 1;
    end;
  end;

  { Step 2 }
  Q := FDBTest.getQ('SELECT *  FROM TABLE_DATE ');
  da :=  System.DateUtils.EncodeDateTime(2011, 11, 22, 12, 32, 15, 0);
  with Q do begin
    Open;
    CheckEquals(11, RecordCount);
    repeat
      { verification }
      das := SQLTimeStampToDateTime(FieldByName('D').AsSQLTimeStamp);
      checkEquals(da,das);
      da := da + 1;
      next;
    until eof;

  end;
  FDBTest.disconnect;
end;



initialization

// Register any test cases with the test runner
RegisterTest(TestDBTest.Suite);

end.
